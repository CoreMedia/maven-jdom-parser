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

import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.jdom.util.JDomUtils;
import org.jdom2.Element;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;

/**
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomModelBase implements JDomBacked {

  private final Element jdomElement;

  public JDomModelBase(Element jdomElement) {
    this.jdomElement = jdomElement;
  }

  public Build getBuild() {
    Element elm = jdomElement.getChild("build", jdomElement.getNamespace());
    if (elm == null) {
      return null;
    } else {
      // this way build setters change DOM tree immediately
      return new JDomBuild(elm);
    }
  }

  public void setBuild(BuildBase build) {
    throw new UnsupportedOperationException();
  }

  public DistributionManagement getDistributionManagement() {
    throw new UnsupportedOperationException();
  }

  public void setDistributionManagement(DistributionManagement distributionManagement) {
    throw new UnsupportedOperationException();
  }

  public List<Dependency> getDependencies() {
    Element dependenciesElm = jdomElement.getChild("dependencies", jdomElement.getNamespace());
    if (dependenciesElm == null) {
      return Collections.emptyList();
    } else {
      return new JDomDependencies(dependenciesElm);
    }
  }

  public void setDependencies(List<Dependency> dependencies) {
    throw new UnsupportedOperationException();
  }

  public DependencyManagement getDependencyManagement() {
    Element elm = jdomElement.getChild("dependencyManagement", jdomElement.getNamespace());
    if (elm == null) {
      return null;
    } else {
      // this way build setters change DOM tree immediately
      return new JDomDependencyManagement(elm);
    }
  }

  public void setDependencyManagement(DependencyManagement dependencyManagement) {
    if (dependencyManagement == null) {
      JDomUtils.rewriteElement("dependencyManagement", null, jdomElement, jdomElement.getNamespace());
    } else {
      DependencyManagement jdomDependencyManagement = getDependencyManagement();
      if (jdomDependencyManagement == null) {
        Element dependencyManagementRoot = insertNewElement("dependencyManagement", jdomElement);
        jdomDependencyManagement = new JDomDependencyManagement(dependencyManagementRoot);
      }

      jdomDependencyManagement.setDependencies(dependencyManagement.getDependencies());
    }
  }

  public List<String> getModules() {
    Element modulesElement = getChildElement("modules", jdomElement);
    if (modulesElement == null) {
      return Collections.emptyList();
    } else {
      return new JDomModules(modulesElement);
    }
  }

  public void setModules(List<String> modules) {
    if (modules == null) {
      JDomUtils.rewriteElement("modules", null, jdomElement, jdomElement.getNamespace());
    } else {
      List<String> jDomModules = getModules();
      if (jDomModules instanceof JDomModules) {
        jDomModules.clear();
      } else {
        jDomModules = new JDomModules(insertNewElement("modules", jdomElement));
      }
      jDomModules.addAll(modules);
    }
  }

  public List<Repository> getPluginRepositories() {
    throw new UnsupportedOperationException();
  }

  public void setPluginRepositories(List<Repository> pluginRepositories) {
    throw new UnsupportedOperationException();
  }

  public Properties getProperties() {
    Element properties = jdomElement.getChild("properties", jdomElement.getNamespace());

    if (properties == null) {
      return null;
    } else {
      return new JDomProperties(properties);
    }
  }

  public void setProperties(Properties properties) {
    throw new UnsupportedOperationException();
  }

  public Reporting getReporting() {
    Element reporting = jdomElement.getChild("reporting", jdomElement.getNamespace());

    if (reporting == null) {
      return null;
    } else {
      return new JDomReporting(reporting);
    }
  }

  public void setReporting(Reporting reporting) {
    throw new UnsupportedOperationException();
  }

  public List<Repository> getRepositories() {
    throw new UnsupportedOperationException();
  }

  public void setRepositories(List<Repository> repositories) {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
