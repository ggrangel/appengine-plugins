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

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.appengine.tools.admin.AppCfg;
import com.google.cloud.tools.app.config.StageConfiguration;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Stages an application to be deployed.
 */
public class StageAction extends AppAction {

  private StageConfiguration configuration;

  public StageAction(StageConfiguration configuration) {
    Preconditions.checkNotNull(configuration);
    Preconditions.checkNotNull(configuration.getSourceDirectory());
    Preconditions.checkNotNull(configuration.getStagingDirectory());
    Preconditions.checkNotNull(configuration.getAppEngineSdkRoot());

    this.configuration = configuration;
  }

  @Override
  public boolean execute() {
    List<String> arguments = new ArrayList<>();
    arguments.add("stage");
    arguments.add(configuration.getSourceDirectory().toString());
    arguments.add(configuration.getStagingDirectory().toString());
    if (configuration.isEnableQuickstart()) {
      arguments.add("--enable_quickstart");
    }
    if (configuration.isDisableUpdateCheck()) {
      arguments.add("--disable_update_check");
    }
    if (!Strings.isNullOrEmpty(configuration.getVersion())) {
      arguments.add("--version");
      arguments.add(configuration.getVersion());
    }
    if (!Strings.isNullOrEmpty(configuration.getCloudProject())) {
      arguments.add("-A");
      arguments.add(configuration.getCloudProject());
    }
    if (configuration.isEnableJarSplitting()) {
      arguments.add("--enable_jar_splitting");
    }
    if (!Strings.isNullOrEmpty(configuration.getJarSplittingExcludes())) {
      arguments.add("--jar_splitting_excludes");
      arguments.add(configuration.getJarSplittingExcludes());
    }
    if (configuration.isRetainUploadDir()) {
      arguments.add("--retain_upload_dir");
    }
    if (!Strings.isNullOrEmpty(configuration.getCompileEncoding())) {
      arguments.add("--compile_encoding");
      arguments.add(configuration.getCompileEncoding());
    }
    if (configuration.isForce()) {
      arguments.add("--force");
    }
    if (configuration.isDeleteJsps()) {
      arguments.add("--delete_jsps");
    }
    if (configuration.isEnableJarClasses()) {
      arguments.add("--enable_jar_classes");
    }
    if (!Strings.isNullOrEmpty(configuration.getRuntime())) {
      arguments.add("--runtime");
      arguments.add(configuration.getRuntime());
    }

    // AppCfg requires this system property to be set.
    System.setProperty("appengine.sdk.root", configuration.getAppEngineSdkRoot().toString());
    AppCfg.main(arguments.toArray(new String[arguments.size()]));

    return true;
  }
}
