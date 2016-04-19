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

import com.google.cloud.tools.app.config.DeployConfiguration;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.nio.file.Path;
import java.util.Collection;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link DeployConfiguration}.
 */
public class DefaultDeployConfiguration implements DeployConfiguration {

  private final Collection<Path> deployables;
  private final String bucket;
  private final String dockerBuild;
  private final Boolean force;
  private final String imageUrl;
  private final Boolean promote;
  private final String server;
  private final Boolean stopPreviousVersion;
  private final String version;

  private DefaultDeployConfiguration(Collection<Path> deployables, @Nullable String bucket,
      @Nullable String dockerBuild, @Nullable Boolean force, @Nullable String imageUrl,
      @Nullable Boolean promote, @Nullable String server, @Nullable Boolean stopPreviousVersion,
      @Nullable String version) {
    this.deployables = deployables;
    this.bucket = bucket;
    this.dockerBuild = dockerBuild;
    this.force = force;
    this.imageUrl = imageUrl;
    this.promote = promote;
    this.server = server;
    this.stopPreviousVersion = stopPreviousVersion;
    this.version = version;
  }

  public Collection<Path> getDeployables() {
    return deployables;
  }

  public String getBucket() {
    return bucket;
  }

  public String getDockerBuild() {
    return dockerBuild;
  }

  public Boolean isForce() {
    return force;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Boolean isPromote() {
    return promote;
  }

  public String getServer() {
    return server;
  }

  public Boolean isStopPreviousVersion() {
    return stopPreviousVersion;
  }

  public String getVersion() {
    return version;
  }

  public static Builder newBuilder(Path... deployables) {
    Preconditions.checkArgument(deployables.length > 0);

    return new Builder(ImmutableList.copyOf(deployables));
  }

  public static class Builder {
    private Collection<Path> deployables;
    private String bucket;
    private String dockerBuild;
    private Boolean force;
    private String imageUrl;
    private Boolean promote;
    private String server;
    private Boolean stopPreviousVersion;
    private String version;

    private Builder(Collection<Path> deployables) {
      this.deployables = deployables;
    }

    public Builder bucket(String bucket) {
      this.bucket = bucket;
      return this;
    }

    public Builder dockerBuild(String dockerBuild) {
      this.dockerBuild = dockerBuild;
      return this;
    }

    public Builder force(Boolean force) {
      this.force = force;
      return this;
    }

    public Builder imageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Builder promote(Boolean promote) {
      this.promote = promote;
      return this;
    }

    public Builder server(String server) {
      this.server = server;
      return this;
    }

    public Builder stopPreviousVersion(Boolean stopPreviousVersion) {
      this.stopPreviousVersion = stopPreviousVersion;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public DeployConfiguration build() {
      return new DefaultDeployConfiguration(deployables, bucket, dockerBuild, force, imageUrl,
          promote, server, stopPreviousVersion, version);
    }
  }
}
