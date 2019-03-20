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
import org.apache.maven.model.jdom.JDomDependency;
import org.apache.maven.model.jdom.JDomDependencyManagement;
import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.apache.maven.model.jdom.etl.ModelETLRequest.UNIX_LS;

/**
 * Tests transformations of {@code dependencies} in {@code dependencyManagement}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class DependencyManagementEtlIT extends DependenciesEtlIT {

  @Test
  public void addJDomDependency() throws IOException {
    // NOTE: Adding elements with bad indentations to test if 'resetIndentations' works.
    jDomModelETL.getModel().getDependencyManagement().addDependency(
            new JDomDependency(
                    new Element("dependency", "http://maven.apache.org/POM/4.0.0")
                            .addContent(new Text(UNIX_LS + "  "))
                            .addContent(new Element("groupId", "http://maven.apache.org/POM/4.0.0")
                                    .addContent("org.apache.commons"))
                            .addContent(new Text(UNIX_LS + "    "))
                            .addContent(new Element("artifactId", "http://maven.apache.org/POM/4.0.0")
                                    .addContent("commons-text"))
                            .addContent(new Text(UNIX_LS + "      "))
                            .addContent(new Element("version", "http://maven.apache.org/POM/4.0.0")
                                    .addContent("1.6"))
                            .addContent(new Text(UNIX_LS + "      "))
            )
    );
    assertTransformation();
  }

  @Test
  public void addModelDependency() throws IOException {
    Dependency dependency = new Dependency();
    dependency.setGroupId("org.apache.commons");
    dependency.setArtifactId("commons-exec");
    dependency.setVersion("1.3");
    jDomModelETL.getModel().getDependencyManagement().addDependency(dependency);
    assertTransformation();
  }

  @Test
  public void newJDomDependencyManagement() throws IOException {
    jDomModelETL.getModel().setDependencyManagement(
            new JDomDependencyManagement(
                    new Element("dependencyManagement", "http://maven.apache.org/POM/4.0.0")
                            .addContent(new Text(UNIX_LS + "    "))
                            .addContent(
                                    new Element("dependencies", "http://maven.apache.org/POM/4.0.0")
                                            .addContent(new Text(UNIX_LS + "      "))
                                            .addContent(
                                                    new Element("dependency", "http://maven.apache.org/POM/4.0.0")
                                                            .addContent(new Text(UNIX_LS + "        "))
                                                            .addContent(new Element("groupId", "http://maven.apache.org/POM/4.0.0")
                                                                    .addContent("org.apache.commons"))
                                                            .addContent(new Text(UNIX_LS + "        "))
                                                            .addContent(new Element("artifactId", "http://maven.apache.org/POM/4.0.0")
                                                                    .addContent("commons-text"))
                                                            .addContent(new Text(UNIX_LS + "        "))
                                                            .addContent(new Element("version", "http://maven.apache.org/POM/4.0.0")
                                                                    .addContent("1.6"))
                                                            .addContent(new Text(UNIX_LS + "      ")))
                                            .addContent(new Text(UNIX_LS + "    ")))
                            .addContent(new Text(UNIX_LS + "  "))
            )
    );
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
    jDomModelETL.getModel().setDependencyManagement(dependencyManagement);
    assertTransformation();
  }

  @Test
  public void removeDependency() throws IOException {
    DependencyManagement dependencyManagement = jDomModelETL.getModel().getDependencyManagement();
    for (Dependency dependency : dependencyManagement.getDependencies().toArray(new Dependency[]{})) {
      if (dependency.getArtifactId().equals("commons-collections4")) {
        dependencyManagement.removeDependency(dependency);
      }
    }
    assertTransformation();
  }

  @Override
  protected List<Dependency> getDependenciesFromModel() {
    return jDomModelETL.getModel().getDependencyManagement().getDependencies();
  }
}
