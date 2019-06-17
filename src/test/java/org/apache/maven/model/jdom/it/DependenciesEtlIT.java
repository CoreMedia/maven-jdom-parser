package org.apache.maven.model.jdom.it;

/*
 * Copyright 2018 CoreMedia AG, Hamburg
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

import org.apache.maven.model.Dependency;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Tests transformations of {@code dependencies}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class DependenciesEtlIT extends AbstractJDomModelEtlIT {

  @Test
  public void addFirstDependency() throws IOException {
    Dependency dependency = new Dependency();
    dependency.setGroupId("org.apache.commons");
    dependency.setArtifactId("commons-lang3");
    dependency.setVersion("3.8.1");
    this.getDependenciesFromModel().add(dependency);
    assertTransformation();
  }

  @Test
  public void addJDomDependencyAtIndex0() throws IOException {
    Dependency dependency = getSourceModel().getDependencies().get(0);
    getDependenciesFromModel().add(0, dependency);
    assertTransformation();
  }

  @Test
  public void addJDomDependencyAtIndex1() throws IOException {
    Dependency dependency = getSourceModel().getDependencies().get(0);
    getDependenciesFromModel().add(1, dependency);
    assertTransformation();
  }

  @Test
  public void changeDependencyGroupId() throws IOException {
    for (Dependency dependency : getDependenciesFromModel().toArray(new Dependency[]{})) {
      if (dependency.getGroupId().equals("com.coremedia.test")) {
        dependency.setGroupId("${project.groupId}");
      }
    }
    assertTransformation();
  }

  @Test
  public void changeDependencyVersion() throws IOException {
    for (Dependency dependency : getDependenciesFromModel().toArray(new Dependency[]{})) {
      if (dependency.getArtifactId().equals("my-dependency")) {
        dependency.setVersion("1.0");
      }
    }
    assertTransformation();
  }

  @Test
  public void removeLastDependency() throws IOException {
    this.getDependenciesFromModel().remove(0);
    assertTransformation();
  }

  @Test
  public void renameDependency() throws IOException {
    for (Dependency dependency : getDependenciesFromModel().toArray(new Dependency[]{})) {
      if (dependency.getArtifactId().equals("my-dependency")) {
        dependency.setArtifactId("my-renamed-dependency");
      }
    }
    assertTransformation();
  }

  @Test
  public void setDependencyVersionNull() throws IOException {
    for (Dependency dependency : getDependenciesFromModel().toArray(new Dependency[]{})) {
      if (dependency.getArtifactId().equals("commons-lang3")) {
        dependency.setVersion(null);
      }
    }
    assertTransformation();
  }

  @Test
  public void removeDependency() throws IOException {
    List<String> dependencies = Arrays.asList("commons-lang3", "commons-another", "commons-collections4");
    for (String artifactId : dependencies) {
      Dependency dependency = new Dependency();
      dependency.setGroupId("org.apache.commons");
      dependency.setArtifactId(artifactId);
      dependency.setVersion("1.0");
      subjectModel.removeDependency(dependency);
    }
    assertTransformation();
  }

  @Test
  public void removeAllDependencies() throws IOException {
    List<String> dependencies = Arrays.asList("commons-lang3", "commons-collections4", "commons-test", "commons-another", "commons-more");
    for (String artifactId : dependencies) {
      Dependency dependency = new Dependency();
      dependency.setGroupId("org.apache.commons");
      dependency.setArtifactId(artifactId);
      dependency.setVersion("1.0");
      subjectModel.removeDependency(dependency);
    }
    assertTransformation();
  }

  protected List<Dependency> getDependenciesFromModel() {
    return subjectModel.getDependencies();
  }
}
