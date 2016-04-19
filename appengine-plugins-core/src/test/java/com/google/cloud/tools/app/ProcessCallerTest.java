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

import static com.google.cloud.tools.app.ProcessCaller.getDevAppserverPath;
import static com.google.cloud.tools.app.ProcessCaller.getGCloudPath;
import static org.junit.Assert.assertEquals;

import com.google.cloud.tools.app.ProcessCaller.Tool;
import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

/**
 * Unit tests for {@link ProcessCaller}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProcessCallerTest {

  @Test
  public void testPrepareCommand_gcloud() {
    Collection<String> arguments = ImmutableList.of("deploy", "./app.yaml", "--bucket", "bucket",
        "--docker-build", "docker", "--force", "true", "--image-url", "image url", "--promote",
        "true", "--server", "server.com", "--version", "v1", "--quiet");
    ProcessCaller caller = ProcessCaller.getFactory().newProcessCaller(Tool.GCLOUD, arguments);

    Collection<String> expectedCommand = ImmutableList.of(getGCloudPath().toString(),
        "preview", "app", "deploy", "./app.yaml", "--bucket", "bucket", "--docker-build", "docker",
        "--force", "true", "--image-url", "image url", "--promote", "true", "--server",
        "server.com", "--version", "v1", "--quiet");

    assertEquals(expectedCommand, caller.getCommand());
  }

  @Test
  public void testPrepareCommand_devAppserver() {
    Collection<String> arguments = ImmutableList.of(
        "app.yaml", "--threadsafe_override", "default:False,backend:True", "--storage_path",
        "storage path", "--python_startup_script", "start this", "--host", "host",
        "--default_gcs_bucket_name", "buckets", "--automatic_restart", "true",
        "--dev_appserver_log_level", "info", "--runtime", "java", "--skip_sdk_update_check",
        "false", "--admin_port", "8080", "--port", "8000", "--use_mtime_file_watcher", "true",
        "--admin_host", "adminHost", "--log_level", "debug", "--max_module_instances", "3",
        "--jvm_flag", "flag", "--allow_skipped_files", "true", "--custom_entrypoint",
        "custom entrypoint", "--api_port", "8091", "--auth_domain", "example.com",
        "--python_startup_args", "arguments");
    ProcessCaller caller = ProcessCaller.getFactory().newProcessCaller(Tool.GCLOUD, arguments);

    Collection<String> expectedCommand = ImmutableList.of(getDevAppserverPath().toString(),
        "app.yaml", "--threadsafe_override", "default:False,backend:True", "--storage_path",
        "storage path", "--python_startup_script", "start this", "--host", "host",
        "--default_gcs_bucket_name", "buckets", "--automatic_restart", "true",
        "--dev_appserver_log_level", "info", "--runtime", "java", "--skip_sdk_update_check",
        "false", "--admin_port", "8080", "--port", "8000", "--use_mtime_file_watcher", "true",
        "--admin_host", "adminHost", "--log_level", "debug", "--max_module_instances", "3",
        "--jvm_flag", "flag", "--allow_skipped_files", "true", "--custom_entrypoint",
        "custom entrypoint", "--api_port", "8091", "--auth_domain", "example.com",
        "--python_startup_args", "arguments");

    assertEquals(expectedCommand, caller.getCommand());
  }

  @Test
  public void testToolGetInitialCommand_gcloud() {
    assertEquals(ImmutableList.of(getGCloudPath().toString(), "preview", "app"),
        Tool.GCLOUD.getInitialCommand());
  }

  @Test
  public void testToolGetInitialCommand_devAppserver() {
    assertEquals(ImmutableList.of(getDevAppserverPath()),
        Tool.DEV_APPSERVER.getInitialCommand());
  }
}
