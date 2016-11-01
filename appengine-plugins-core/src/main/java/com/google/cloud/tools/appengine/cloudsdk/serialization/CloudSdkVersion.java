/*
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

package com.google.cloud.tools.appengine.cloudsdk.serialization;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the version of the Cloud SDK. Expects a version to be a series of integers separated
 * by the '.' character. This class does not handle versions strings that are intended to be
 * human-readable, i.e 'v1beta3-1.0.0'.
 */
public class CloudSdkVersion implements Comparable<CloudSdkVersion> {

  private final List<Integer> versionComponents;
  private final String version;

  /**
   * Constructs a CloudSdkVersion from a version string.
   * @param version a non-null, nonempty string of the form 130.0.0
   * @throws NumberFormatException if the string cannot be parsed
   */
  public CloudSdkVersion(String version) throws NumberFormatException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(version));

    this.version = version;
    this.versionComponents = buildVersionComponents(version);
  }

  @Override
  public String toString() {
    return version;
  }

  @Override
  public int compareTo(CloudSdkVersion otherVersion) {
    List<Integer> mine = new ArrayList<>(this.versionComponents);
    List<Integer> other = new ArrayList<>(otherVersion.getVersionComponents());

    // if both versions don't have the same number of components, pad the smaller one with zeros
    rightPadZerosUntilSameLength(mine, other);

    // compare version integers from left to right
    for (int i = 0; i < mine.size(); i++) {
      int result = mine.get(i).compareTo(other.get(i));
      if (result != 0) {
        return result;
      }
    }
    return 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(versionComponents);
  }

  /**
   * Compares objects for equality. Two CloudSdkVersions are considered equal if, from left to
   * right, their version integers are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    CloudSdkVersion otherVersion = (CloudSdkVersion) obj;

    return this.compareTo(otherVersion) == 0;
  }

  protected List<Integer> getVersionComponents() {
    return versionComponents;
  }

  private void rightPadZerosUntilSameLength(List<Integer> first, List<Integer> second) {
    while (first.size() < second.size()) {
      first.add(0);
    }
    while (first.size() > second.size()) {
      second.add(0);
    }
  }

  private List<Integer> buildVersionComponents(String version) throws NumberFormatException {
    String[] components = version.split("\\.");
    ImmutableList.Builder builder = ImmutableList.builder();
    for (String num : components) {
      builder.add(Integer.parseInt(num));
    }
    return builder.build();
  }
}
