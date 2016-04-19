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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.google.cloud.tools.app.config.StageGenericJavaConfiguration;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Stages a Java JAR/WAR Managed VMs application to be deployed.
 */
public class StageGenericJavaAction extends AppAction {

  private static Logger logger = Logger.getLogger(StageGenericJavaAction.class.getName());
  private StageGenericJavaConfiguration configuration;

  public StageGenericJavaAction(StageGenericJavaConfiguration configuration) {
    Preconditions.checkNotNull(configuration);
    Preconditions.checkNotNull(configuration.getStagingDirectory());
    Preconditions.checkNotNull(configuration.getArtifact());

    this.configuration = configuration;
  }

  /**
   * Copies app.yaml, Dockerfile and the application artifact to the staging area.
   *
   * <p>If app.yaml or Dockerfile do not exist, gcloud deploy will create them.
   */
  public boolean execute() throws IOException {
    if (Files.notExists(configuration.getStagingDirectory())) {
      logger.severe("Staging directory does not exist. Location: "
          + configuration.getStagingDirectory().toString());
      return false;
    }
    if (!Files.isDirectory(configuration.getStagingDirectory())) {
      logger.severe("Staging location is not a directory. Location: "
          + configuration.getStagingDirectory().toString());
      return false;
    }

    // Copy app.yaml to staging.
    if (configuration.getAppYaml() != null && Files.exists(configuration.getAppYaml())) {
      Files.copy(configuration.getAppYaml(),
          Paths.get(configuration.getStagingDirectory().toString(),
              configuration.getAppYaml().getFileName().toString()),
          REPLACE_EXISTING);
    }

    // Copy Dockerfile to staging.
    if (configuration.getDockerfile() != null && Files.exists(configuration.getDockerfile())) {
      Files.copy(configuration.getDockerfile(),
          Paths.get(configuration.getStagingDirectory().toString(),
              configuration.getDockerfile().getFileName().toString()),
          REPLACE_EXISTING);
    }

    // Copy the JAR/WAR file to staging.
    if (configuration.getArtifact() != null && Files.exists(configuration.getArtifact())) {
      Files.copy(configuration.getArtifact(),
          Paths.get(configuration.getStagingDirectory().toString(),
              configuration.getArtifact().getFileName().toString()),
          REPLACE_EXISTING);
    }

    return true;
  }
}
