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
package com.google.cloud.tools.app.module;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.cloud.tools.app.AppAction;
import com.google.cloud.tools.app.GCloudExecutionException;
import com.google.cloud.tools.app.ProcessCaller.Tool;
import com.google.cloud.tools.app.config.module.ListConfiguration;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists the versions for a module, or every version of every module if no module is specified.
 */
public class ListAction extends AppAction {

  private ListConfiguration configuration;

  public ListAction(ListConfiguration configuration) {
    Preconditions.checkNotNull(configuration);
    Preconditions.checkNotNull(configuration.getModules());

    this.configuration = configuration;
  }

  @Override
  public boolean execute() throws GCloudExecutionException, IOException {
    List<String> arguments = new ArrayList<>();
    arguments.add("modules");
    arguments.add("list");
    arguments.addAll(configuration.getModules());
    if (!Strings.isNullOrEmpty(configuration.getServer())) {
      arguments.add("--server");
      arguments.add(configuration.getServer());
    }

    return processCallerFactory.newProcessCaller(Tool.GCLOUD, arguments).call();
  }
}
