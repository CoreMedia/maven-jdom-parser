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
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.jdom2.Element;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEPENDENCIES;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEPENDENCY_MANAGEMENT;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_MODULES;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROPERTIES;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_REPORTING;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.newDetachedElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDOM implementation of the {@link ModelBase} class. It holds the child elements of the Maven POMs root
 * ({@code project}) element that are also definable in a {@code profile}.
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomModelBase extends ModelBase implements JDomBacked {

  private final Element jdomElement;

  @SuppressWarnings("WeakerAccess")
  public JDomModelBase(Element jdomElement) {
    this.jdomElement = jdomElement;

    Element dependenciesElement = getChildElement(POM_ELEMENT_DEPENDENCIES, jdomElement);
    if (dependenciesElement == null) {
      dependenciesElement = newDetachedElement(POM_ELEMENT_DEPENDENCIES, jdomElement);
    }
    super.setDependencies(new JDomDependencies(dependenciesElement, this));

    Element dependencyManagementElement = getChildElement(POM_ELEMENT_DEPENDENCY_MANAGEMENT, jdomElement);
    if (dependencyManagementElement == null) {
      dependencyManagementElement = newDetachedElement(POM_ELEMENT_DEPENDENCY_MANAGEMENT, jdomElement);
      insertNewElement(POM_ELEMENT_DEPENDENCIES, dependencyManagementElement);
    }
    super.setDependencyManagement(new JDomDependencyManagement(dependencyManagementElement, this));

    Element modulesElement = getChildElement(POM_ELEMENT_MODULES, jdomElement);
    if (modulesElement != null) {
      super.setModules(new JDomModules(modulesElement));
    }
  }

  public DistributionManagement getDistributionManagement() {
    // TODO Implement support for setting ModelBase#distributionManagement (in #JDomModelBase and #setDistributionManagement) and remove this method override
    throw new UnsupportedOperationException();
  }

  public void setDistributionManagement(DistributionManagement distributionManagement) {
    throw new UnsupportedOperationException();
  }

  public void setDependencies(List<Dependency> dependencies) {
    if (dependencies == null) {
      rewriteElement(POM_ELEMENT_DEPENDENCIES, null, jdomElement);
    } else {
      new JDomDependencies(insertNewElement(POM_ELEMENT_DEPENDENCIES, jdomElement), this).addAll(dependencies);
    }
  }

  @Override
  public void setDependencyManagement(DependencyManagement dependencyManagement) {
    if (dependencyManagement == null) {
      rewriteElement(POM_ELEMENT_DEPENDENCY_MANAGEMENT, null, jdomElement);
      super.setDependencyManagement(null);
    } else if (dependencyManagement instanceof JDomDependencyManagement) {
      rewriteElement(((JDomDependencyManagement) dependencyManagement).getJDomElement(), jdomElement);
      super.setDependencyManagement(dependencyManagement);
    } else {
      DependencyManagement jdomDependencyManagement = getDependencyManagement();
      if (jdomDependencyManagement == null) {
        Element dependencyManagementRoot = insertNewElement(POM_ELEMENT_DEPENDENCY_MANAGEMENT, jdomElement);
        jdomDependencyManagement = new JDomDependencyManagement(dependencyManagementRoot, this);
      }
      jdomDependencyManagement.setDependencies(dependencyManagement.getDependencies());
    }
  }

  public List<String> getModules() {
    Element modulesElement = getChildElement(POM_ELEMENT_MODULES, jdomElement);
    if (modulesElement == null) {
      return emptyList();
    } else {
      return new JDomModules(modulesElement);
    }
  }

  public void setModules(List<String> modules) {
    if (modules == null) {
      rewriteElement(POM_ELEMENT_MODULES, null, jdomElement);
    } else {
      List<String> jDomModules = getModules();
      if (jDomModules instanceof JDomModules) {
        jDomModules.clear();
      } else {
        jDomModules = new JDomModules(insertNewElement(POM_ELEMENT_MODULES, jdomElement));
      }
      jDomModules.addAll(modules);
    }
  }

  public List<Repository> getPluginRepositories() {
    // TODO Implement support for setting ModelBase#pluginRepositories (in #JDomModelBase and #setPluginRepositories) and remove this method override
    throw new UnsupportedOperationException();
  }

  public void setPluginRepositories(List<Repository> pluginRepositories) {
    throw new UnsupportedOperationException();
  }

  public Properties getProperties() {
    Element properties = jdomElement.getChild(POM_ELEMENT_PROPERTIES, jdomElement.getNamespace());

    if (properties == null) {
      return null;
    } else {
      return new JDomProperties(properties);
    }
  }

  public void setProperties(Properties properties) {
    if (properties == null) {
      rewriteElement(POM_ELEMENT_PROPERTIES, null, jdomElement);
    } else {
      Properties jDomProperties = getProperties();
      if (jDomProperties != null) {
        jDomProperties.clear();
      } else {
        jDomProperties = new JDomProperties(insertNewElement(POM_ELEMENT_PROPERTIES, jdomElement));
      }
      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        jDomProperties.setProperty((String) entry.getKey(), (String) entry.getValue());
      }
    }
  }

  public Reporting getReporting() {
    Element reporting = jdomElement.getChild(POM_ELEMENT_REPORTING, jdomElement.getNamespace());

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
  public ModelBase clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
