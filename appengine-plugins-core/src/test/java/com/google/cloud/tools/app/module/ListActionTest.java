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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.tools.app.GCloudExecutionException;
import com.google.cloud.tools.app.ProcessCaller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link ListAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListActionTest {
  @Mock
  private ProcessCaller callerMock;

  @Before
  public void setUp() throws GCloudExecutionException {
    when(callerMock.getGCloudPath()).thenReturn("here");
    when(callerMock.call()).thenReturn(true);
  }

  @Test
  public void testPrepareCommand() {
//    ListAction action = new ListAction(
//        ImmutableList.of("module1"),
//        ImmutableMap.<Option, String>of());
//
//    Set<String> expected = ImmutableSet.of(action.getProcessCaller().getGCloudPath(), "preview",
//        "app", "modules", "list", "module1");
//    Set<String> actual = new HashSet<>(action.getProcessCaller().getCommand());
//    assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testPrepareCommand_noModule() {
//    ListAction action = new ListAction(ImmutableList.<String>of(),
//        ImmutableMap.of(Option.SERVER, "server.com"));
//
//    Set<String> expected = ImmutableSet.of(action.getProcessCaller().getGCloudPath(), "preview", "app",
//        "modules", "list", "--server", "server.com");
//    Set<String> actual = new HashSet<>(action.getProcessCaller().getCommand());
//
//    assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testCheckFlags_allFlags() {
//    Map<Option, String> flags = ImmutableMap.of(
//        Option.SERVER, "server.com"
//    );
//
//    new ListAction(ImmutableList.of("module1", "module2"), flags);
//  }
//
//  @Test
//  public void testCheckFlags_noFlags() {
//    new ListAction(ImmutableList.of("module1", "module2"), ImmutableMap.<Option, String>of());
//  }
//
//  @Test(expected = InvalidFlagException.class)
//  public void testCheckFlags_error() {
//    Map<Option, String> flags = ImmutableMap.of(
//        Option.SERVER, "server.com",
//        Option.ADMIN_HOST, "disallowed flag!!!"
//    );
//
//    new ListAction(ImmutableList.of("module1", "module2"), flags);
//  }
//
//  @Test
//  public void testExecute() throws GCloudExecutionException, IOException {
//    ListAction action = new ListAction(
//        ImmutableList.of("module1", "module2"),
//        ImmutableMap.<Option, String>of()
//    );
//    action.setProcessCaller(callerMock);
//
//    action.execute();
//
//    verify(callerMock, times(1)).call();
  }
}
