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
import org.apache.maven.model.DependencyManagement;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Tests transformations of {@code dependencies} in {@code dependencyManagement}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class DependencyManagementEtlIT extends DependenciesEtlIT {

  @Test
  public void addJDomDependency() throws IOException {
    Dependency dependency = getSourceModel().getDependencies().get(0);
    subjectModel.getDependencyManagement().addDependency(dependency);
    assertTransformation();
  }

  @Test
  public void addJDomDependencyAtIndex() throws IOException {
    Dependency dependency = getSourceModel().getDependencies().get(0);
    subjectModel.getDependencyManagement().getDependencies().add(0, dependency);
    assertTransformation();
  }

  @Test
  public void addModelDependency() throws IOException {
    Dependency dependency = new Dependency();
    dependency.setGroupId("org.apache.commons");
    dependency.setArtifactId("commons-exec");
    dependency.setVersion("1.3");
    subjectModel.getDependencyManagement().addDependency(dependency);
    assertTransformation();
  }

  @Test
  public void newJDomDependencyManagement() throws IOException {
    DependencyManagement dependencyManagement = getSourceModel().getDependencyManagement();
    subjectModel.setDependencyManagement(dependencyManagement);
    assertTransformation();
  }

  @Test
  public void newModelDependencyManagement() throws IOException {
    Dependency dependency = new Dependency();
    dependency.setGroupId("org.apache.commons");
    dependency.setArtifactId("commons-exec");
    dependency.setVersion("1.3");
    DependencyManagement dependencyManagement = new DependencyManagement();
    dependencyManagement.addDependency(dependency);
    subjectModel.setDependencyManagement(dependencyManagement);
    assertTransformation();
  }

  @Test
  public void removeDependency() throws IOException {
    DependencyManagement dependencyManagement = subjectModel.getDependencyManagement();
    for (Dependency dependency : dependencyManagement.getDependencies().toArray(new Dependency[]{})) {
      if (dependency.getArtifactId().equals("commons-collections4")) {
        dependencyManagement.removeDependency(dependency);
      }
    }
    assertTransformation();
  }

  @Override
  protected List<Dependency> getDependenciesFromModel() {
    return subjectModel.getDependencyManagement().getDependencies();
  }
}
