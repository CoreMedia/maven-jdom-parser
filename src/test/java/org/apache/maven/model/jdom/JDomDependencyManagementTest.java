package org.apache.maven.model.jdom;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.model.Dependency;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JDomDependencyManagementTest {

  private SAXBuilder builder = new SAXBuilder();

  private JDomDependencyManagement dependencyManagement;

  @Before
  public void setUp() throws IOException, JDOMException {
    String dependencyContent = "<dependency><groupId>x</groupId><artifactId>y</artifactId></dependency>";
    String dependenciesContent = "<dependencies>" + dependencyContent + "</dependencies>";
    String dependencyManagementContent = "<dependencyManagement>" + dependenciesContent + "</dependencyManagement>";
    String projectContent = "<project>" + dependencyManagementContent + "</project>";

    Element elementPrj = builder.build(new StringReader(projectContent)).getRootElement();
    Element elementDepMgmt = elementPrj.getChildren().get(0);

    dependencyManagement = new JDomDependencyManagement(elementDepMgmt, new JDomModel(elementPrj));
  }

  @Test
  public void testGetDependencies() throws Exception {
    String content = "<dependencyManagement></dependencyManagement>";
    Document document = builder.build(new StringReader(content));
    assertNotNull(new JDomDependencyManagement(document.getRootElement(), null).getDependencies());
    assertEquals(0, new JDomDependencyManagement(document.getRootElement(), null).getDependencies().size());

    content = "<dependencyManagement><dependencies/></dependencyManagement>";
    document = builder.build(new StringReader(content));
    assertEquals(0, new JDomDependencyManagement(document.getRootElement(), null).getDependencies().size());

    content = "<dependencyManagement><dependencies><dependency/></dependencies></dependencyManagement>";
    document = builder.build(new StringReader(content));
    assertEquals(1, new JDomDependencyManagement(document.getRootElement(), null).getDependencies().size());
  }

  @Test
  public void testAddDependency() throws Exception {
    assertEquals(1, dependencyManagement.getDependencies().size());

    Dependency dependency = new Dependency();
    dependency.setGroupId("a");
    dependency.setArtifactId("b");
    dependencyManagement.addDependency(dependency);

    assertEquals(2, dependencyManagement.getDependencies().size());
  }

  @Test
  public void testRemoveJDomDependency() throws Exception {
    assertEquals(1, dependencyManagement.getDependencies().size());

    String dependencyContent = "<dependency> <groupId>x</groupId> <artifactId>y</artifactId> </dependency>";
    Element dependencyElement = builder.build(new StringReader(dependencyContent)).getRootElement();
    dependencyManagement.removeDependency(new JDomDependency(dependencyElement));

    assertEquals(0, dependencyManagement.getDependencies().size());
  }

  @Test
  public void testRemoveModelDependency() throws Exception {
    assertEquals(1, dependencyManagement.getDependencies().size());

    Dependency dependency = new Dependency();
    dependency.setGroupId("x");
    dependency.setArtifactId("y");
    dependencyManagement.removeDependency(dependency);

    assertEquals(0, dependencyManagement.getDependencies().size());
  }

  @Test
  public void testSetDependenciesListOfDependency() throws Exception {
    assertEquals(1, dependencyManagement.getDependencies().size());

    Dependency dependency1 = new Dependency();
    dependency1.setGroupId("a");
    dependency1.setArtifactId("b");
    Dependency dependency2 = new Dependency();
    dependency2.setGroupId("c");
    dependency2.setArtifactId("d");
    dependencyManagement.setDependencies(asList(dependency1, dependency2));

    assertEquals(2, dependencyManagement.getDependencies().size());
  }
}
