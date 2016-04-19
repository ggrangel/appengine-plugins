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

import com.google.cloud.tools.app.config.StageGenericJavaConfiguration;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link StageGenericJavaConfiguration}.
 */
public class DefaultStageGenericJavaConfiguration implements StageGenericJavaConfiguration {

  private final Path appYaml;
  private final Path dockerfile;
  private final Path artifact;
  private final Path stagingDirectory;

  private DefaultStageGenericJavaConfiguration(@Nullable Path appYaml, @Nullable Path dockerfile,
      Path artifact, Path stagingDirectory) {
    this.appYaml = appYaml;
    this.dockerfile = dockerfile;
    this.artifact = artifact;
    this.stagingDirectory = stagingDirectory;
  }

  public Path getAppYaml() {
    return appYaml;
  }

  public Path getDockerfile() {
    return dockerfile;
  }

  public Path getArtifact() {
    return artifact;
  }

  public Path getStagingDirectory() {
    return stagingDirectory;
  }

  public static Builder newBuilder(Path artifact, Path stagingDirectory) {
    Preconditions.checkNotNull(artifact);
    Preconditions.checkNotNull(stagingDirectory);

    return new Builder(artifact, stagingDirectory);
  }

  public static class Builder {
    private Path appYaml;
    private Path dockerfile;
    private Path artifact;
    private Path stagingDirectory;

    private Builder(Path artifact, Path stagingDirectory) {
      this.artifact = artifact;
      this.stagingDirectory = stagingDirectory;
    }

    public Builder appYaml(Path appYaml) {
      this.appYaml = appYaml;
      return this;
    }

    public Builder dockerfile(Path dockerfile) {
      this.dockerfile = dockerfile;
      return this;
    }

    public StageGenericJavaConfiguration build() {
      return new DefaultStageGenericJavaConfiguration(appYaml, dockerfile, artifact,
          stagingDirectory);
    }
  }
}
