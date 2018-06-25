/*
 * Copyright 2018 Google LLC. All Rights Reserved.
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

package com.google.cloud.tools.maven;

import org.apache.maven.plugin.MojoExecutionException;

public interface AppEngineDeployer {

  String APPENGINE_CONFIG = "APPENGINE_CONFIG";
  String GCLOUD_CONFIG = "GCLOUD_CONFIG";

  class Factory {
    static AppEngineDeployer newDeployer(AbstractDeployMojo deployConfiguration)
        throws MojoExecutionException {
      if (deployConfiguration.getArtifact() == null
          || !deployConfiguration.getArtifact().exists()) {
        throw new MojoExecutionException(
            "\nCould not determine appengine environment, did you package your application?"
                + "\nRun 'mvn package appengine:deploy'");
      }
      return deployConfiguration.isStandardStaging()
          ? new AppEngineStandardDeployer(deployConfiguration)
          : new AppEngineFlexibleDeployer(deployConfiguration);
    }
  }

  void deploy() throws MojoExecutionException;

  void deployAll() throws MojoExecutionException;

  void deployCron() throws MojoExecutionException;

  void deployDispatch() throws MojoExecutionException;

  void deployDos() throws MojoExecutionException;

  void deployIndex() throws MojoExecutionException;

  void deployQueue() throws MojoExecutionException;
}
