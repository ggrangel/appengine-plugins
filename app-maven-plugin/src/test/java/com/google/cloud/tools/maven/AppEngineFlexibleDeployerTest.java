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

import java.io.IOException;
import junitparams.JUnitParamsRunner;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnitParamsRunner.class)
public class AppEngineFlexibleDeployerTest {
  private static final String PROJECT_BUILD = "project-build";
  private static final String VERSION_BUILD = "version-build";

  private static final String GCLOUD_CONFIG = "GCLOUD_CONFIG";
  private static final String APPENGINE_CONFIG = "APPENGINE_CONFIG";

  private static final String CONFIG_PROJECT_ERROR =
      "Deployment project must be defined or configured to read from system state\n"
          + "1. Set <project>my-project-name</project>\n"
          + "2. Set <project>"
          + GCLOUD_CONFIG
          + "</project> to use project from gcloud config.\n"
          + "3. Using <project>"
          + APPENGINE_CONFIG
          + "</project> is not allowed for flexible environment projects";

  private static final String CONFIG_VERSION_ERROR =
      "Deployment version must be defined or configured to read from system state\n"
          + "1. Set <version>my-version</version>\n"
          + "2. Set <version>"
          + GCLOUD_CONFIG
          + "</version> to use version from gcloud config.\n"
          + "3. Using <version>"
          + APPENGINE_CONFIG
          + "</version> is not allowed for flexible environment projects";

  @Rule public TemporaryFolder tempFolder = new TemporaryFolder();

  @Mock private MavenProject mavenProject;
  @InjectMocks private AbstractDeployMojo deployMojo = new DeployMojo();

  @Before
  public void setup() throws IOException {
    MockitoAnnotations.initMocks(this);
    deployMojo.sourceDirectory = tempFolder.newFolder("source");
    deployMojo.setProject("some-project");
    deployMojo.setVersion("some-version");
    Mockito.when(mavenProject.getBasedir()).thenReturn(tempFolder.getRoot());
  }

  @Test
  public void testSetDeploymentProjectAndVersion_fromBuildConfig() {
    deployMojo.setVersion(VERSION_BUILD);
    deployMojo.setProject(PROJECT_BUILD);
    new AppEngineFlexibleDeployer(deployMojo);
    Assert.assertEquals(VERSION_BUILD, deployMojo.getVersion());
    Assert.assertEquals(PROJECT_BUILD, deployMojo.getProject());
  }

  @Test
  public void testSetDeploymentProjectAndVersion_fromGcloud() {
    deployMojo.setVersion(GCLOUD_CONFIG);
    deployMojo.setProject(GCLOUD_CONFIG);
    new AppEngineFlexibleDeployer(deployMojo);
    Assert.assertEquals(null, deployMojo.getVersion());
    Assert.assertEquals(null, deployMojo.getProject());
  }

  @Test
  public void testSetDeploymentProjectAndVersion_projectFromAppengineWebXml() {
    deployMojo.setVersion(VERSION_BUILD);
    deployMojo.setProject(APPENGINE_CONFIG);
    try {
      new AppEngineFlexibleDeployer(deployMojo);
      Assert.fail();
    } catch (IllegalArgumentException ex) {
      Assert.assertEquals(CONFIG_PROJECT_ERROR, ex.getMessage());
    }
  }

  @Test
  public void testSetDeploymentProjectAndVersion_versionFromAppengineWebXml() {
    deployMojo.setVersion(APPENGINE_CONFIG);
    deployMojo.setProject(PROJECT_BUILD);
    try {
      new AppEngineFlexibleDeployer(deployMojo);
      Assert.fail();
    } catch (IllegalArgumentException ex) {
      Assert.assertEquals(CONFIG_VERSION_ERROR, ex.getMessage());
    }
  }

  @Test
  public void testSetDeploymentProjectAndVersion_noProjectSet() {
    deployMojo.setVersion(VERSION_BUILD);
    deployMojo.setProject(null);
    try {
      new AppEngineFlexibleDeployer(deployMojo);
      Assert.fail();
    } catch (IllegalArgumentException ex) {
      Assert.assertEquals(CONFIG_PROJECT_ERROR, ex.getMessage());
    }
  }

  @Test
  public void testSetDeploymentProjectAndVersion_noVersionSet() {
    deployMojo.setVersion(null);
    deployMojo.setProject(PROJECT_BUILD);
    try {
      new AppEngineFlexibleDeployer(deployMojo);
      Assert.fail();
    } catch (IllegalArgumentException ex) {
      Assert.assertEquals(CONFIG_VERSION_ERROR, ex.getMessage());
    }
  }
}
