/*
 * Copyright (c) 2016 Google Inc. All Right Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.cloud.tools.maven;

import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.concurrent.CountDownLatch;

/**
 * Starts running App Engine Development App Server asynchronously.
 */
@Mojo(name = "start")
@Execute(phase = LifecyclePhase.PACKAGE)
public class RunAsyncMojo extends RunMojo {

  private CountDownLatch waitStartedLatch;

  /**
   * Configures an asynchronous process runner for running server.
   */
  public RunAsyncMojo() {
    super();
    processRunner.setAsync(true);
  }

}
