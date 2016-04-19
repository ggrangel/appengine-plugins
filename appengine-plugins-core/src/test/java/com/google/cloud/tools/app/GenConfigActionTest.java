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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Unit tests for {@link GenConfigAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GenConfigActionTest {

  @Mock
  private ProcessCaller callerMock;

  @Before
  public void setUp() throws GCloudExecutionException {
    when(callerMock.getGCloudPath()).thenReturn("here");
    when(callerMock.call()).thenReturn(true);
  }

  @Test
  public void testPrepareCommand_withFlags() {
    GenConfigAction action = new GenConfigAction(
        "source",
        ImmutableMap.of(Option.CONFIG, "app.yaml",
            Option.CUSTOM, "true",
            Option.RUNTIME, "java"));

    Set<String> expected = ImmutableSet.of(action.getProcessCaller().getGCloudPath(), "preview",
        "app", "gen-config", "source", "--config", "app.yaml", "--custom", "true", "--runtime",
        "java");
    Set<String> actual = new HashSet<>(action.getProcessCaller().getCommand());
    assertEquals(expected, actual);
  }

  @Test
  public void testPrepareCommand_noFlags() {
    GenConfigAction action = new GenConfigAction(
        "source",
        ImmutableMap.<Option, String>of());

    Set<String> expected = ImmutableSet.of(action.getProcessCaller().getGCloudPath(), "preview",
        "app", "gen-config", "source");
    Set<String> actual = new HashSet<>(action.getProcessCaller().getCommand());
    assertEquals(expected, actual);
  }

  @Test(expected = NullPointerException.class)
  public void testNullSource() {
    new GenConfigAction(null, ImmutableMap.<Option, String>of());
  }

  @Test
  public void testCheckFlags_allFlags() {
    Map<Option, String> flags = ImmutableMap.of(
        Option.CONFIG, "app.yaml",
        Option.CUSTOM, "true",
        Option.RUNTIME, "java"
    );

    new GenConfigAction("source", flags);
  }

  @Test
  public void testCheckFlags_oneFlag() {
    Map<Option, String> flags = ImmutableMap.of(Option.CONFIG, "app.yaml");
    new GenConfigAction("source", flags);
  }

  @Test(expected = InvalidFlagException.class)
  public void testCheckFlags_error() {
    Map<Option, String> flags = ImmutableMap.of(
        Option.CONFIG, "app.yaml",
        Option.CUSTOM, "true",
        Option.RUNTIME, "java",
        Option.SERVER, "server.com",
        Option.ADMIN_HOST, "disallowed flag!!!"
    );

    new GenConfigAction("source", flags);
  }

  @Test
  public void testExecute() throws GCloudExecutionException, IOException {
    GenConfigAction action = new GenConfigAction(
        "source",
        ImmutableMap.<Option, String>of());
    action.setProcessCaller(callerMock);

    action.execute();

    verify(callerMock, times(1)).call();
  }
}
