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

import com.google.cloud.tools.appengine.AppEngineDescriptor;
import com.google.cloud.tools.appengine.api.AppEngineException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.maven.plugin.MojoExecutionException;
import org.xml.sax.SAXException;

public class AppEngineStandardDeployer implements AppEngineDeployer {

  private AbstractDeployMojo deployMojo;
  private AppEngineStandardStager stager;

  AppEngineStandardDeployer(AbstractDeployMojo deployMojo) {
    this(deployMojo, new AppEngineStandardStager(deployMojo));
  }

  @VisibleForTesting
  AppEngineStandardDeployer(AbstractDeployMojo deployMojo, AppEngineStandardStager stager) {
    this.deployMojo = deployMojo;
    this.stager = stager;

    stager.overrideAppEngineDirectory();
    setDeploymentProjectAndVersion();
  }

  @Override
  public void deploy() throws MojoExecutionException {
    stager.stage();
    deployMojo.setDeployables(ImmutableList.of(deployMojo.getStagingDirectory()));

    try {
      deployMojo.getAppEngineFactory().deployment().deploy(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Standard application deployment failed", ex);
    }
  }

  @Override
  public void deployAll() throws MojoExecutionException {
    stager.stage();
    ImmutableList.Builder<File> standardDeployables = ImmutableList.builder();

    // Look for app.yaml
    File appYaml = deployMojo.getStagingDirectory().toPath().resolve("app.yaml").toFile();
    if (!appYaml.exists()) {
      throw new MojoExecutionException("Failed to deploy all: could not find app.yaml.");
    }
    deployMojo.getLog().info("deployAll: Preparing to deploy app.yaml");
    standardDeployables.add(appYaml);

    // Look for config yamls
    String[] configYamls = {"cron.yaml", "dispatch.yaml", "dos.yaml", "index.yaml", "queue.yaml"};
    Path configPath =
        deployMojo.getStagingDirectory().toPath().resolve("WEB-INF").resolve("appengine-generated");
    for (String yamlName : configYamls) {
      File yaml = configPath.resolve(yamlName).toFile();
      if (yaml.exists()) {
        deployMojo.getLog().info("deployAll: Preparing to deploy " + yamlName);
        standardDeployables.add(yaml);
      }
    }

    deployMojo.setDeployables(standardDeployables.build());

    try {
      deployMojo.getAppEngineFactory().deployment().deploy(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Failed to deploy", ex);
    }
  }

  @Override
  public void deployCron() throws MojoExecutionException {
    stager.stage();
    try {
      deployMojo.getAppEngineFactory().deployment().deployCron(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Failed to deploy", ex);
    }
  }

  @Override
  public void deployDispatch() throws MojoExecutionException {
    stager.stage();
    try {
      deployMojo.getAppEngineFactory().deployment().deployDispatch(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Failed to deploy", ex);
    }
  }

  @Override
  public void deployDos() throws MojoExecutionException {
    stager.stage();
    try {
      deployMojo.getAppEngineFactory().deployment().deployDos(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Failed to deploy", ex);
    }
  }

  @Override
  public void deployIndex() throws MojoExecutionException {
    stager.stage();
    try {
      deployMojo.getAppEngineFactory().deployment().deployIndex(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Failed to deploy", ex);
    }
  }

  @Override
  public void deployQueue() throws MojoExecutionException {
    stager.stage();
    try {
      deployMojo.getAppEngineFactory().deployment().deployQueue(deployMojo);
    } catch (AppEngineException ex) {
      throw new MojoExecutionException("Failed to deploy", ex);
    }
  }

  @VisibleForTesting
  void setDeploymentProjectAndVersion() {
    File appengineWebXml =
        deployMojo
            .getSourceDirectory()
            .toPath()
            .resolve("WEB-INF")
            .resolve("appengine-web.xml")
            .toFile();

    String project = deployMojo.getProject();
    if (project == null || project.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Deployment project must be defined or configured to read from system state\n"
              + "1. Set <project>my-project-name</project>\n"
              + "2. Set <project>"
              + APPENGINE_CONFIG
              + "</project> to use <application> from appengine-web.xml\n"
              + "3. Set <project>"
              + GCLOUD_CONFIG
              + "</project> to use project from gcloud config.");
    } else if (project.equals(APPENGINE_CONFIG)) {
      try {
        AppEngineDescriptor appEngineDescriptor =
            AppEngineDescriptor.parse(new FileInputStream(appengineWebXml));
        String appengineWebXmlProject = appEngineDescriptor.getProjectId();
        if (appengineWebXmlProject == null || appengineWebXmlProject.trim().isEmpty()) {
          throw new IllegalStateException("<application> was not found in appengine-web.xml");
        }
        deployMojo.setProject(appengineWebXmlProject);
      } catch (AppEngineException | IOException | SAXException e) {
        throw new RuntimeException("Failed to read project from appengine-web.xml");
      }
    } else if (project.equals(GCLOUD_CONFIG)) {
      deployMojo.setProject(null);
    }

    String version = deployMojo.getVersion();
    if (version == null || version.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Deployment version must be defined or configured to read from system state\n"
              + "1. Set <version>my-version</version>\n"
              + "2. Set <version>"
              + APPENGINE_CONFIG
              + "</version> to use <version> from appengine-web.xml\n"
              + "3. Set <version>"
              + GCLOUD_CONFIG
              + "</version> to use version from gcloud config.");
    } else if (version.equals(APPENGINE_CONFIG)) {
      try {
        AppEngineDescriptor appEngineDescriptor =
            AppEngineDescriptor.parse(new FileInputStream(appengineWebXml));
        String appengineWebXmlVersion = appEngineDescriptor.getProjectVersion();
        if (appengineWebXmlVersion == null || appengineWebXmlVersion.trim().isEmpty()) {
          throw new IllegalStateException("<version> was not found in appengine-web.xml");
        }
        deployMojo.setVersion(appengineWebXmlVersion);
      } catch (AppEngineException | IOException | SAXException e) {
        throw new RuntimeException("Failed to read version from appengine-web.xml");
      }
    } else if (version.equals(GCLOUD_CONFIG)) {
      deployMojo.setVersion(null);
    }
  }
}
