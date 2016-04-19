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
package com.google.cloud.tools.app.config.impl;

import com.google.cloud.tools.app.config.GenConfigConfiguration;

import java.nio.file.Path;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link GenConfigConfiguration}.
 */
public class DefaultGenConfigConfiguration implements GenConfigConfiguration {

  private final Path sourceDirectory;
  private final String config;
  private final Boolean custom;
  private final String runtime;

  private DefaultGenConfigConfiguration(@Nullable Path sourceDirectory, @Nullable String config,
      @Nullable Boolean custom, @Nullable String runtime) {
    this.sourceDirectory = sourceDirectory;
    this.config = config;
    this.custom = custom;
    this.runtime = runtime;
  }

  public Path getSourceDirectory() {
    return sourceDirectory;
  }

  public String getConfig() {
    return config;
  }

  public Boolean isCustom() {
    return custom;
  }

  public String getRuntime() {
    return runtime;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private Path sourceDirectory;
    private String config;
    private Boolean custom;
    private String runtime;

    public Builder sourceDirectory(Path sourceDirectory) {
      this.sourceDirectory = sourceDirectory;
      return this;
    }

    public Builder config(String config) {
      this.config = config;
      return this;
    }

    public Builder custom(Boolean custom) {
      this.custom = custom;
      return this;
    }

    public Builder runtime(String runtime) {
      this.runtime = runtime;
      return this;
    }

    public GenConfigConfiguration build() {
      return new DefaultGenConfigConfiguration(sourceDirectory, config, custom, runtime);
    }
  }
}
