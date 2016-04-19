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

import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for {@link StageAction}.
 */
public class StageActionTest {

  @Test
  public void testCheckFlags_allFlags() {
    Map<Option, String> flags = new HashMap<>();
    flags.put(Option.ENABLE_QUICKSTART, "true");
    flags.put(Option.DISABLE_UPDATE_CHECK, "false");
    flags.put(Option.VERSION, "v1");
    flags.put(Option.GCLOUD_PROJECT, "project");
    flags.put(Option.ENABLE_JAR_SPLITTING, "true");
    flags.put(Option.JAR_SPLITTING_EXCLUDES, "suffix,suffix2");
    flags.put(Option.RETAIN_UPLOAD_DIR, "false");
    flags.put(Option.COMPILE_ENCODING, "UTF8");
    flags.put(Option.FORCE, "true");
    flags.put(Option.DELETE_JSPS, "false");
    flags.put(Option.ENABLE_JAR_CLASSES, "true");
    flags.put(Option.RUNTIME, "java");

    new StageAction(".", ".", ".", flags);
  }

  @Test
  public void testCheckFlags_oneFlag() {
    Map<Option, String> flags = ImmutableMap.of(Option.RUNTIME, "java");
    new StageAction(".", ".", ".", flags);
  }

  @Test(expected = InvalidFlagException.class)
  public void testCheckFlags_error() {
    Map<Option, String> flags = ImmutableMap.of(Option.DOCKER_BUILD, "docker");
    new StageAction(".", ".", ".", flags);
  }
}
