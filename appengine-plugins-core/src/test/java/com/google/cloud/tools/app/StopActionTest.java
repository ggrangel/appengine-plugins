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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Unit tests for {@link StopAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class StopActionTest {

  @Mock
  HttpURLConnection connection;

  @Before
  public void setUp() throws IOException {
    doNothing().when(connection).setReadTimeout(anyInt());
    doNothing().when(connection).connect();
    doNothing().when(connection).disconnect();
    when(connection.getResponseMessage()).thenReturn("response");
  }

  @Test
  public void testCheckFlags() throws GCloudExecutionException {
    Map<Option, String> flags = ImmutableMap.of(
        Option.ADMIN_HOST, "adminHost",
        Option.ADMIN_PORT, "9090",
        Option.SERVER, "server.com"
    );

    new StopAction(flags);
  }

  @Test(expected = InvalidFlagException.class)
  public void testCheckFlags_error() throws GCloudExecutionException {
    Map<Option, String> flags = ImmutableMap.of(
        Option.ADMIN_HOST, "adminHost",
        Option.PORT, "9000"
    );

    new StopAction(flags);
  }

  @Test
  public void testSendRequest() throws GCloudExecutionException, IOException {
    when(connection.getResponseCode()).thenReturn(200);
    StopAction action = new StopAction(ImmutableMap.<Option, String>of());
    action.setConnection(connection);
    assertTrue(action.execute());
  }

  @Test
  public void testSendRequest_less200() throws GCloudExecutionException, IOException {
    when(connection.getResponseCode()).thenReturn(100);
    StopAction action = new StopAction(ImmutableMap.<Option, String>of());
    action.setConnection(connection);
    assertFalse(action.execute());
  }

  @Test
  public void testSendRequest_500() throws GCloudExecutionException, IOException {
    when(connection.getResponseCode()).thenReturn(500);
    StopAction action = new StopAction(ImmutableMap.<Option, String>of());
    action.setConnection(connection);
    assertFalse(action.execute());
  }
}
