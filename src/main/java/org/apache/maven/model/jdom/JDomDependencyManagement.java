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
import org.apache.maven.model.DependencyManagement;
import org.jdom2.Element;

import java.util.Collections;
import java.util.List;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEPENDENCIES;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms DEPENDENCYMANAGEMENT element
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomDependencyManagement extends DependencyManagement implements JDomBacked {

  private final Element jdomElement;

  public JDomDependencyManagement(Element jdomElement) {
    this.jdomElement = jdomElement;
  }

  @Override
  public List<Dependency> getDependencies() {
    Element dependenciesElm = jdomElement.getChild(POM_ELEMENT_DEPENDENCIES, jdomElement.getNamespace());
    if (dependenciesElm == null) {
      return Collections.emptyList();
    } else {
      return new JDomDependencies(dependenciesElm);
    }
  }

  @Override
  public void setDependencies(List<Dependency> dependencies) {
    if (dependencies == null) {
      rewriteElement(POM_ELEMENT_DEPENDENCIES, null, jdomElement);
    } else {
      new JDomDependencies(insertNewElement(POM_ELEMENT_DEPENDENCIES, jdomElement)).addAll(dependencies);
    }
  }

  /** {@inheritDoc} */
  @Override
  public DependencyManagement clone() {
    return super.clone();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
