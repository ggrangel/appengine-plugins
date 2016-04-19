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
package com.google.cloud.tools.app.config;

import java.nio.file.Path;

/**
 * Configuration for {@link com.google.cloud.tools.app.StageAction}.
 */
public interface StageConfiguration {

  Path getSourceDirectory();

  Path getStagingDirectory();

  Path getAppEngineSdkRoot();

  Boolean isEnableQuickstart();

  Boolean isDisableUpdateCheck();

  String getVersion();

  String getCloudProject();

  Boolean isEnableJarSplitting();

  String getJarSplittingExcludes();

  Boolean isRetainUploadDir();

  String getCompileEncoding();

  Boolean isForce();

  Boolean isDeleteJsps();

  Boolean isEnableJarClasses();

  String getRuntime();
}
