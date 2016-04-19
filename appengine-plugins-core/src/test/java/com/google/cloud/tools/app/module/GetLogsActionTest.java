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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.tools.app.GCloudExecutionException;
import com.google.cloud.tools.app.ProcessCaller;
import com.google.cloud.tools.app.ProcessCaller.ProcessCallerFactory;
import com.google.cloud.tools.app.ProcessCaller.Tool;
import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

/**
 * Unit tests for {@link GetLogsAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetLogsActionTest {

  @Mock
  private ProcessCaller callerMock;
  @Mock
  private ProcessCallerFactory processCallerFactory;

  @Before
  public void setUp() throws GCloudExecutionException {
    when(callerMock.call()).thenReturn(true);
    when(processCallerFactory.newProcessCaller(eq(Tool.GCLOUD), isA(Collection.class)))
        .thenReturn(callerMock);
  }

  @Test
  public void testNewGetLogsAction() {
    GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoModules() {
    GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1"))
        .setModules(ImmutableList.of("mod2"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoVersions() {
    GetLogsAction.newGetLogsAction()
        .setVersion("v1")
        .setVersion("v1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoLogFileLocations() {
    GetLogsAction.newGetLogsAction()
        .setLogFileLocation("here")
        .setLogFileLocation("there");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoAppends() {
    GetLogsAction.newGetLogsAction()
        .setAppend("file1")
        .setAppend("file2");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoDays() {
    GetLogsAction.newGetLogsAction()
        .setDays("20")
        .setDays("30");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_badDays() {
    GetLogsAction.newGetLogsAction().setDays("not an integer");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoDetails() {
    GetLogsAction.newGetLogsAction()
        .setDetails(true)
        .setDetails(false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoEndDates() {
    GetLogsAction.newGetLogsAction()
        .setEndDate("2000-10-10")
        .setEndDate("2000-10-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoServers() {
    GetLogsAction.newGetLogsAction()
        .setServer("appengine.google.com")
        .setServer("appengine.google.com");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoSeverities() {
    GetLogsAction.newGetLogsAction()
        .setSeverity("debug")
        .setSeverity("debug");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewGetLogsAction_addTwoVhosts() {
    GetLogsAction.newGetLogsAction()
        .setVhost("vhost")
        .setVhost("vhost");
  }

  @Test
  public void testArguments_all() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--days", "30", "--details", "false", "--end-date",
        "2000-10-10", "--server", "appengine.google.com", "--severity", "debug", "--vhost",
        "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noLogFile() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2",
        "--version", "v1", "--append", "file", "--days", "30", "--details", "false", "--end-date",
        "2000-10-10", "--server", "appengine.google.com", "--severity", "debug", "--vhost",
        "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noAppend() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--days", "30", "--details", "false", "--end-date",
        "2000-10-10", "--server", "appengine.google.com", "--severity", "debug", "--vhost",
        "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noDays() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--details", "false", "--end-date",
        "2000-10-10", "--server", "appengine.google.com", "--severity", "debug", "--vhost",
        "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noDetails() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--days", "30", "--end-date", "2000-10-10",
        "--server", "appengine.google.com", "--severity", "debug", "--vhost", "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noEndDate() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--days", "30", "--details", "false",
        "--server", "appengine.google.com", "--severity", "debug", "--vhost", "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setServer("appengine.google.com")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noServer() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--days", "30", "--details", "false", "--end-date",
        "2000-10-10", "--severity", "debug", "--vhost", "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setSeverity("debug")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noSeverity() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--days", "30", "--details", "false", "--end-date",
        "2000-10-10", "--server", "appengine.google.com", "--vhost", "vhost");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setVhost("vhost");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }

  @Test
  public void testArguments_noVhost() throws GCloudExecutionException {
    Collection<String> arguments = ImmutableList.of("modules", "get-logs", "mod1", "mod2", "there",
        "--version", "v1", "--append", "file", "--days", "30", "--details", "false", "--end-date",
        "2000-10-10", "--server", "appengine.google.com", "--severity", "debug");
    GetLogsAction action = GetLogsAction.newGetLogsAction()
        .setModules(ImmutableList.of("mod1", "mod2"))
        .setVersion("v1")
        .setLogFileLocation("there")
        .setAppend("file")
        .setDays("30")
        .setDetails(false)
        .setEndDate("2000-10-10")
        .setServer("appengine.google.com")
        .setSeverity("debug");
    action.setProcessCallerFactory(processCallerFactory);
    action.execute();

    verify(processCallerFactory, times(1)).newProcessCaller(eq(Tool.GCLOUD), eq(arguments));
  }
}
