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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.tools.app.ProcessCaller.ProcessCallerFactory;
import com.google.cloud.tools.app.ProcessCaller.Tool;
import com.google.cloud.tools.app.config.DeployConfiguration;
import com.google.cloud.tools.app.config.impl.DefaultDeployConfiguration;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Unit tests for {@link DeployAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeployActionTest {

  @Mock
  private ProcessCallerFactory processCallerFactory;
  @Mock
  private ProcessCaller callerMock;

  @Before
  public void setUp() throws GCloudExecutionException, IOException {
    when(callerMock.call()).thenReturn(true);
    when(processCallerFactory.newProcessCaller(eq(Tool.GCLOUD), isA(Collection.class)))
        .thenReturn(callerMock);
  }

  @Test
  public void testNewDeployAction() {
    Path appYaml1 = Paths.get("appYaml1");
    Path appYaml2 = Paths.get("appYaml2");

    DeployConfiguration configuration = DefaultDeployConfiguration.newBuilder(appYaml1, appYaml2)
        .bucket("gs://a-bucket")
        .dockerBuild("remote")
        .force(true)
        .imageUrl("imageUrl")
        .promote(false)
        .server("appengine.google.com")
        .stopPreviousVersion(true)
        .version("v1")
        .build();

    DeployAction action = new DeployAction(configuration);
    action.setProcessCallerFactory(processCallerFactory);

    Collection<String> expectedCommand = ImmutableList.of(ProcessCaller.getGCloudPath().toString(),
        "preview", "app", "deploy", "appYaml1", "--bucket", "gs://a-bucket", "--docker-build",
        "remote", "--force", "--image-url", "imageUrl", "--server",
        "appengine.google.com", "--stop-previous-version", "--version", "v1", "--quiet");

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(expectedCommand));
  }
}
