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

import com.google.cloud.tools.app.AppAction;
import com.google.cloud.tools.app.GCloudExecutionException;
import com.google.cloud.tools.app.ProcessCaller.Tool;
import com.google.cloud.tools.app.config.module.SetManagedByConfiguration;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sets a specific instance or every instance of a module to managed by Google or Self.
 */
public class SetManagedByAction extends AppAction {

  private SetManagedByConfiguration configuration;

  public SetManagedByAction(SetManagedByConfiguration configuration) {
    Preconditions.checkNotNull(configuration);
    Preconditions.checkNotNull(configuration.getModules());
    Preconditions.checkArgument(configuration.getModules().size() > 0);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(configuration.getVersion()));
    Preconditions.checkNotNull(configuration.getManager());

    this.configuration = configuration;
  }

  public boolean execute() throws GCloudExecutionException, IOException {
    List<String> arguments = new ArrayList<>();

    arguments.add("modules");
    arguments.add("set-managed-by");
    arguments.addAll(configuration.getModules());
    arguments.add("--version");
    arguments.add(configuration.getVersion());
    arguments.add(configuration.getManager().getFlagForm());
    if (!Strings.isNullOrEmpty(configuration.getInstance())) {
      arguments.add("--instance");
      arguments.add(configuration.getInstance());
    }

    return processCallerFactory.newProcessCaller(Tool.GCLOUD, arguments).call();
  }

  public enum Manager {
    SELF("--self"),
    GOOGLE("--google");

    private String flagForm;

    Manager(String flagForm) {
      this.flagForm = flagForm;
    }

    public String getFlagForm() {
      return flagForm;
    }
  }
}
