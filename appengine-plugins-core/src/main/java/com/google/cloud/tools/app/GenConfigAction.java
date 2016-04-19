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

import com.google.cloud.tools.app.ProcessCaller.Tool;
import com.google.cloud.tools.app.config.GenConfigConfiguration;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Generates missing configuration files.
 */
public class GenConfigAction extends AppAction {

  private static Logger logger = Logger.getLogger(GenConfigAction.class.getName());
  private GenConfigConfiguration configuration;

  public GenConfigAction(GenConfigConfiguration configuration) {
    Preconditions.checkNotNull(configuration);

    this.configuration = configuration;
  }

  public boolean execute() throws GCloudExecutionException, IOException {
    if (Files.notExists(configuration.getSourceDirectory())) {
      logger.severe("Source directory does not exist. Location: "
          + configuration.getSourceDirectory().toString());
    }
    if (!Files.isDirectory(configuration.getSourceDirectory())) {
      logger.severe("Source location is not a directory. Location: "
          + configuration.getSourceDirectory().toString());
    }

    List<String> arguments = new ArrayList<>();
    arguments.add("gen-config");

    if (configuration.getSourceDirectory() != null) {
      arguments.add(configuration.getSourceDirectory().toString());
    }
    if (!Strings.isNullOrEmpty(configuration.getConfig())) {
      arguments.add("--config");
      arguments.add(configuration.getConfig());
    }
    if (configuration.isCustom()) {
      arguments.add("--custom");
    }
    if (!Strings.isNullOrEmpty(configuration.getRuntime())) {
      arguments.add("--runtime");
      arguments.add(configuration.getRuntime());
    }

    return processCallerFactory.newProcessCaller(Tool.GCLOUD, arguments).call();
  }
}
