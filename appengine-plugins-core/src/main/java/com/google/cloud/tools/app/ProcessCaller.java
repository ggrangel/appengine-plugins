/**
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.tools.app;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Calls external tools like gcloud or dev_appserver. Hides system process invocation logic from
 * {@link AppAction}.
 *
 * <p>All the logic for generating and running commands is contained in this class.
 */
public class ProcessCaller {

  // TODO(joaomartins): Will this work in e.g., Windows?
  private static final Path DEFAULT_WORKING_DIR = Paths.get(".");
  private static Logger logger = Logger.getLogger(ProcessCaller.class.getName());
  private List<String> command;
  private final Path workingDirectory;
  private final boolean synchronous;
  private static Path cloudSdkPath;

  private ProcessCaller(Tool tool, Collection<String> arguments, boolean synchronous,
      Path cloudSdkPath) {
    this(tool, arguments, DEFAULT_WORKING_DIR, synchronous, cloudSdkPath);
  }

  private ProcessCaller(Tool tool, Collection<String> arguments, Path workingDirectory,
      boolean synchronous, Path cloudSdkPath) {
    this.workingDirectory = workingDirectory;
    this.synchronous = synchronous;
    this.command = prepareCommand(tool, arguments);
    this.cloudSdkPath = cloudSdkPath;
  }

  public boolean call() throws GCloudExecutionException, IOException {
    logger.info("Calling " + Joiner.on(" ").join(command));

    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.directory(workingDirectory.toFile());
    processBuilder.inheritIO();
    try {
      Process gcloudProcess = processBuilder.start();

      // If call is synchronous, wait for the process to end.
      if (synchronous) {
        int exitStatus = gcloudProcess.waitFor();

        if (exitStatus != 0) {
          throw new GCloudExecutionException(exitStatus);
        }
      }
    } catch (InterruptedException ex) {
      logger.severe("Error running gcloud CLI. " + ex);
      return false;
    }

    return true;
  }

  /**
   * Finds the executable path for gcloud.
   */
  @VisibleForTesting
  static Path getGCloudPath() {
    Path gcloudPath = Paths.get(cloudSdkPath.toString(), "bin", "gcloud");
    if (Files.notExists(gcloudPath)) {
      throw new RuntimeException("Could not locate gcloud from Cloud SDK directory \""
          + Paths.get(cloudSdkPath.toString(), "bin") + "\". Please provide the correct "
          + "Cloud SDK root directory.");
    }

    return gcloudPath;
  }

  /**
   * Finds the executable path for dev_appserver.py.
   */
  static Path getDevAppserverPath() {
    Path devAppserverPath = Paths.get(cloudSdkPath.toString(), "bin", "dev_appserver.py");
    if (Files.notExists(devAppserverPath)) {
      throw new RuntimeException("Could not locate dev_appserver from Cloud SDK directory \""
          + Paths.get(cloudSdkPath.toString(), "bin") + "\". Please provide the correct Cloud " +
          "SDK root directory.");
    }

    return devAppserverPath;
  }

  @VisibleForTesting
  List<String> getCommand() {
    return command;
  }

  /**
   * Prepares the gcloud command ran by the {@link AppAction}.
   */
  private List<String> prepareCommand(Tool tool, Collection<String> arguments) {
    List<String> command = new ArrayList<>();

    command.addAll(tool.getInitialCommand());

    // Command and flags. e.g. "deploy --version=v1" or "modules list".
    command.addAll(arguments);

    return command;
  }

  public enum Tool {
    GCLOUD(getGCloudPath().toString(), "preview", "app"),
    DEV_APPSERVER(getDevAppserverPath().toString());

    private Collection<String> initialCommand = new ArrayList<>();

    Tool(String... commandTokens) {
      Collections.addAll(initialCommand, commandTokens);
    }

    public Collection<String> getInitialCommand() {
      return initialCommand;
    }
  }

  public static ProcessCallerFactory getFactory() {
    return new ProcessCallerFactory();
  }

  public static class ProcessCallerFactory {

    Path cloudSdkPath = Paths.get(System.getProperty("user.home"), "google-cloud-sdk");

    public ProcessCaller newProcessCaller(Tool tool, Collection<String> arguments) {
      return newProcessCaller(tool, arguments, true);
    }

    public ProcessCaller newProcessCaller(Tool tool, Collection<String> arguments,
        Boolean synchronous) {
      return new ProcessCaller(tool, arguments, synchronous, cloudSdkPath);
    }

    public void setCloudSdkPath(String cloudSdkLocation) {
      if (!Strings.isNullOrEmpty(cloudSdkLocation)) {
        Path cloudSdkPath = Paths.get(cloudSdkLocation);
        if (Files.notExists(cloudSdkPath)) {
          throw new IllegalArgumentException("Provided Cloud SDK path does not exist.");
        }
        if (!Files.isDirectory(cloudSdkPath)) {
          throw new IllegalArgumentException("Provided Cloud SDK path is not a directory.");
        }
        Path gcloudPath = Paths.get(cloudSdkLocation, "bin", "gcloud");
        Path devAppserverPath = Paths.get(cloudSdkLocation, "bin", "dev_appserver.py");
        if (Files.notExists(gcloudPath)) {
          throw new IllegalArgumentException("gcloud can not be found at " + gcloudPath.toString());
        }
        if (Files.notExists(devAppserverPath)) {
          throw new IllegalArgumentException(
              "dev_appserver.py can not be found at " + devAppserverPath.toString());
        }

        this.cloudSdkPath = cloudSdkPath;
      }
    }
  }
}
