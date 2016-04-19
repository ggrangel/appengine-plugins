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

import com.google.cloud.tools.app.config.StageConfiguration;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

/**
 * Default implementation of {@link StageConfiguration}.
 */
public class DefaultStageConfiguration implements StageConfiguration {

  private final Path sourceDirectory;
  private final Path stagingDirectory;
  private final Path appEngineSdkRoot;
  private final Boolean enableQuickstart;
  private final Boolean disableUpdateCheck;
  private final String version;
  private final String cloudProject;
  private final Boolean enableJarSplitting;
  private final String jarSplittingExcludes;
  private final Boolean retainUploadDir;
  private final String compileEncoding;
  private final Boolean force;
  private final Boolean deleteJsps;
  private final Boolean enableJarClasses;
  private final String runtime;

  private DefaultStageConfiguration(Path sourceDirectory, Path stagingDirectory,
      Path appEngineSdkRoot, Boolean enableQuickstart, Boolean disableUpdateCheck,
      String version, String cloudProject, Boolean enableJarSplitting,
      String jarSplittingExcludes, Boolean retainUploadDir, String compileEncoding,
      Boolean force, Boolean deleteJsps, Boolean enableJarClasses, String runtime) {
    this.sourceDirectory = sourceDirectory;
    this.stagingDirectory = stagingDirectory;
    this.appEngineSdkRoot = appEngineSdkRoot;
    this.enableQuickstart = enableQuickstart;
    this.disableUpdateCheck = disableUpdateCheck;
    this.version = version;
    this.cloudProject = cloudProject;
    this.enableJarSplitting = enableJarSplitting;
    this.jarSplittingExcludes = jarSplittingExcludes;
    this.retainUploadDir = retainUploadDir;
    this.compileEncoding = compileEncoding;
    this.force = force;
    this.deleteJsps = deleteJsps;
    this.enableJarClasses = enableJarClasses;
    this.runtime = runtime;
  }

  public Path getSourceDirectory() {
    return sourceDirectory;
  }

  public Path getStagingDirectory() {
    return stagingDirectory;
  }

  public Path getAppEngineSdkRoot() {
    return appEngineSdkRoot;
  }

  public Boolean isEnableQuickstart() {
    return enableQuickstart;
  }

  public Boolean isDisableUpdateCheck() {
    return disableUpdateCheck;
  }

  public String getVersion() {
    return version;
  }

  public String getCloudProject() {
    return cloudProject;
  }

  public Boolean isEnableJarSplitting() {
    return enableJarSplitting;
  }

  public String getJarSplittingExcludes() {
    return jarSplittingExcludes;
  }

  public Boolean isRetainUploadDir() {
    return retainUploadDir;
  }

  public String getCompileEncoding() {
    return compileEncoding;
  }

  public Boolean isForce() {
    return force;
  }

  public Boolean isDeleteJsps() {
    return deleteJsps;
  }

  public Boolean isEnableJarClasses() {
    return enableJarClasses;
  }

  public String getRuntime() {
    return runtime;
  }

  public static Builder newBuilder(Path sourceDirectory, Path stagingDirectory,
      Path appEngineSdkRoot) {
    Preconditions.checkNotNull(sourceDirectory);
    Preconditions.checkNotNull(stagingDirectory);
    Preconditions.checkNotNull(appEngineSdkRoot);

    return new Builder(sourceDirectory, stagingDirectory, appEngineSdkRoot);
  }

  public static class Builder {
    private Path sourceDirectory;
    private Path stagingDirectory;
    private Path appEngineSdkRoot;
    private Boolean enableQuickstart;
    private Boolean disableUpdateCheck;
    private String version;
    private String cloudProject;
    private Boolean enableJarSplitting;
    private String jarSplittingExcludes;
    private Boolean retainUploadDir;
    private String compileEncoding;
    private Boolean force;
    private Boolean deleteJsps;
    private Boolean enableJarClasses;
    private String runtime;

    private Builder(Path sourceDirectory, Path stagingDirectory, Path appEngineSdkRoot) {
      this.sourceDirectory = sourceDirectory;
      this.stagingDirectory = stagingDirectory;
      this.appEngineSdkRoot = appEngineSdkRoot;
    }

    public Builder enableQuickstart(Boolean enableQuickstart) {
      this.enableQuickstart = enableQuickstart;
      return this;
    }

    public Builder disableUpdateCheck(Boolean disableUpdateCheck) {
      this.disableUpdateCheck = disableUpdateCheck;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public Builder cloudProject(String cloudProject) {
      this.cloudProject = cloudProject;
      return this;
    }

    public Builder enableJarSplitting(Boolean enableJarSplitting) {
      this.enableJarSplitting = enableJarSplitting;
      return this;
    }

    public Builder jarSplittingExcludes(String jarSplittingExcludes) {
      this.jarSplittingExcludes = jarSplittingExcludes;
      return this;
    }

    public Builder retainUploadDir(Boolean retainUploadDir) {
      this.retainUploadDir = retainUploadDir;
      return this;
    }

    public Builder compileEncoding(String compileEncoding) {
      this.compileEncoding = compileEncoding;
      return this;
    }

    public Builder force(Boolean force) {
      this.force = force;
      return this;
    }

    public Builder deleteJsps(Boolean deleteJsps) {
      this.deleteJsps = deleteJsps;
      return this;
    }

    public Builder enableJarClasses(Boolean enableJarClasses) {
      this.enableJarClasses = enableJarClasses;
      return this;
    }

    public Builder runtime(String runtime) {
      this.runtime = runtime;
      return this;
    }

    public StageConfiguration build() {
      return new DefaultStageConfiguration(sourceDirectory, stagingDirectory, appEngineSdkRoot,
          enableQuickstart, disableUpdateCheck, version, cloudProject, enableJarSplitting,
          jarSplittingExcludes, retainUploadDir, compileEncoding, force, deleteJsps,
          enableJarClasses, runtime);
    }
  }
}
