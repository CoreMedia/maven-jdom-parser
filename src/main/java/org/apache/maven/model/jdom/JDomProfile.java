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

import org.apache.maven.model.Activation;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.jdom2.Element;

import java.util.List;
import java.util.Properties;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ACTIVATION;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_BUILD;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ID;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDOM implementation of the {@link Profile} class. It holds the child elements of the Maven POMs {@code profile}
 * element that are not also definable in the root ({@code project}) element.
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomProfile extends Profile implements JDomBacked {

  private static final long serialVersionUID = 1381285942042666375L;

  private final Element jdomElement;

  private final JDomModelBase modelBase;

  @SuppressWarnings("WeakerAccess")
  public JDomProfile(Element jdomElement) {
    this.jdomElement = jdomElement;
    this.modelBase = new JDomModelBase(jdomElement);

    super.setId(getChildElementTextTrim(POM_ELEMENT_ID, this.jdomElement));

    Element activationElement = getChildElement(POM_ELEMENT_ACTIVATION, this.jdomElement);
    if (activationElement != null) {
      super.setActivation(new JDomActivation(activationElement));
    }

    Element buildElement = getChildElement(POM_ELEMENT_BUILD, this.jdomElement);
    if (buildElement != null) {
      super.setBuild(new JDomBuild(buildElement));
    }
  }

  @Override
  public void setActivation(Activation activation) {
    //noinspection IfStatementWithTooManyBranches
    if (activation == null) {
      rewriteElement(POM_ELEMENT_ACTIVATION, null, jdomElement);
      super.setActivation(null);
    } else if (activation instanceof JDomActivation) {
      rewriteElement(((JDomActivation) activation).getJDomElement(), jdomElement);
      super.setActivation(activation);
    } else {
      JDomActivation jdomActivation = new JDomActivation(insertNewElement(POM_ELEMENT_ACTIVATION, jdomElement), activation);
      super.setActivation(jdomActivation);
    }
  }

  @Override
  public void setBuild(BuildBase build) {
    //noinspection IfStatementWithTooManyBranches
    if (build == null) {
      rewriteElement(POM_ELEMENT_BUILD, null, jdomElement);
      super.setBuild(null);
    } else if (build instanceof JDomBuild) {
      // TODO Introduce and use JDomBuildBase
      rewriteElement(((JDomBuild) build).getJDomElement(), jdomElement);
      super.setBuild(build);
    } else {
      // TODO Introduce and use JDomBuildBase
      JDomBuild jdomBuild = new JDomBuild(insertNewElement(POM_ELEMENT_BUILD, jdomElement), (Build) build);
      super.setBuild(jdomBuild);
    }
  }

  @Override
  public List<Dependency> getDependencies() {
    // TODO Implement support for setting ModelBase#dependencies (in JDomModelBase#JDomModelBase and JDomModelBase#setDependencies) and remove this method override
    return modelBase.getDependencies();
  }

  @Override
  public void setDependencies(List<Dependency> dependencies) {
    modelBase.setDependencies(dependencies);
  }

  @Override
  public DependencyManagement getDependencyManagement() {
    // TODO Implement support for setting ModelBase#dependenyManagement (in JDomModelBase#JDomModelBase and JDomModelBase#setDependenyManagement) and remove this method override
    return modelBase.getDependencyManagement();
  }

  @Override
  public void setDependencyManagement(DependencyManagement dependencyManagement) {
    modelBase.setDependencyManagement(dependencyManagement);
  }

  @Override
  public DistributionManagement getDistributionManagement() {
    // TODO Implement support for setting ModelBase#distributionManagement (in JDomModelBase#JDomModelBase and JDomModelBase#setDistributionManagement) and remove this method override
    return modelBase.getDistributionManagement();
  }

  @Override
  public void setDistributionManagement(DistributionManagement distributionManagement) {
    modelBase.setDistributionManagement(distributionManagement);
  }

  @Override
  public void setId(String id) {
    rewriteElement(POM_ELEMENT_ID, id, jdomElement);
    super.setId(id);
  }

  @Override
  public List<String> getModules() {
    // TODO Implement support for setting ModelBase#modules (in JDomModelBase#JDomModelBase and JDomModelBase#setModules) and remove this method override
    return modelBase.getModules();
  }

  @Override
  public void setModules(List<String> modules) {
    modelBase.setModules(modules);
  }

  @Override
  public List<Repository> getPluginRepositories() {
    // TODO Implement support for setting ModelBase#pluginRepositories (in JDomModelBase#JDomModelBase and JDomModelBase#setPluginRepositories) and remove this method override
    return modelBase.getPluginRepositories();
  }

  @Override
  public void setPluginRepositories(List<Repository> pluginRepositories) {
    modelBase.setPluginRepositories(pluginRepositories);
  }

  @Override
  public Properties getProperties() {
    // TODO Implement support for setting ModelBase#properties (in JDomModelBase#JDomModelBase and JDomModelBase#setProperties) and remove this method overrid
    return modelBase.getProperties();
  }

  @Override
  public void setProperties(Properties properties) {
    modelBase.setProperties(properties);
  }

  @Override
  public Reporting getReporting() {
    // TODO Implement support for setting ModelBase#reporting (in JDomModelBase#JDomModelBase and JDomModelBase#setReporting) and remove this method override
    return modelBase.getReporting();
  }

  @Override
  public void setReporting(Reporting reporting) {
    modelBase.setReporting(reporting);
  }

  @Override
  public List<Repository> getRepositories() {
    // TODO Implement support for setting ModelBase#repositories (in JDomModelBase#JDomModelBase and JDomModelBase#setRepositories) and remove this method override
    return modelBase.getRepositories();
  }

  @Override
  public void setRepositories(List<Repository> repositories) {
    modelBase.setRepositories(repositories);
  }

  /** {@inheritDoc} */
  @Override
  public Profile clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
