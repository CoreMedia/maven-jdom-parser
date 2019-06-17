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
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.jdom2.Element;
import org.jdom2.Text;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_CONFIGURATION;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEPENDENCIES;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_EXTENSIONS;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_INHERITED;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.resetIndentations;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms PLUGIN element
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomPlugin extends Plugin implements JDomBacked, MavenCoordinate {

  private final Element jdomElement;

  private final MavenCoordinate coordinate;

  public JDomPlugin(Element jdomElement) {
    this.jdomElement = jdomElement;
    this.coordinate = new JDomMavenCoordinate(jdomElement);
  }

  @Override
  public String getArtifactId() {
    return coordinate.getArtifactId();
  }

  @Override
  public void setArtifactId(String artifactId) {
    coordinate.setArtifactId(artifactId);
  }

  @Override
  public Object getConfiguration() {
    Element elm = getChildElement(POM_ELEMENT_CONFIGURATION, jdomElement);
    if (elm == null) {
      return null;
    } else {
      return new JDomConfiguration(elm);
    }
  }

  @Override
  public void setConfiguration(Object configuration) {
    if (configuration == null) {
      rewriteElement(POM_ELEMENT_CONFIGURATION, null, jdomElement);
    } else if (configuration instanceof JDomConfiguration) {
      Element newJDomConfigurationElement = ((JDomConfiguration) configuration).getJDomElement().clone();

      JDomConfiguration oldJDomConfiguration = (JDomConfiguration) getConfiguration();
      if (oldJDomConfiguration == null) {
        jdomElement.addContent(
                jdomElement.getContentSize() - 1,
                asList(
                        new Text("\n" + detectIndentation(jdomElement)),
                        newJDomConfigurationElement));
      } else {
        int replaceIndex = jdomElement.indexOf(oldJDomConfiguration.getJDomElement());
        jdomElement.removeContent(replaceIndex);
        jdomElement.addContent(replaceIndex, newJDomConfigurationElement);
      }

      resetIndentations(jdomElement, detectIndentation(jdomElement));
      resetIndentations(newJDomConfigurationElement, detectIndentation(jdomElement) + "  ");
    }
  }

  @Override
  public List<Dependency> getDependencies() {
    Element dependenciesElm = jdomElement.getChild(POM_ELEMENT_DEPENDENCIES, jdomElement.getNamespace());
    return new JDomDependencies(dependenciesElm, this);
  }

  @Override
  public void setDependencies(List<Dependency> dependencies) {
    if (dependencies == null) {
      rewriteElement(POM_ELEMENT_DEPENDENCIES, null, jdomElement);
    } else {
      new JDomDependencies(insertNewElement(POM_ELEMENT_DEPENDENCIES, jdomElement), this).addAll(dependencies);
    }
  }

  @Override
  public List<PluginExecution> getExecutions() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setExecutions(List<PluginExecution> executions) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getExtensions() {
    return getChildElementTextTrim(POM_ELEMENT_EXTENSIONS, jdomElement);
  }

  @Override
  public void setExtensions(String extensions) {
    rewriteElement(POM_ELEMENT_EXTENSIONS, extensions, jdomElement);
  }

  @Override
  public boolean isExtensions() {
    return Boolean.parseBoolean(getExtensions());
  }

  @Override
  public void setExtensions(boolean extensions) {
    setExtensions(Boolean.toString(extensions));
  }

  @Override
  public Object getGoals() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setGoals(Object goals) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getGroupId() {
    return coordinate.getGroupId();
  }

  @Override
  public void setGroupId(String groupId) {
    coordinate.setGroupId(groupId);
  }

  @Override
  public String getInherited() {
    return getChildElementTextTrim(POM_ELEMENT_INHERITED, jdomElement);
  }

  @Override
  public void setInherited(String inherited) {
    rewriteElement(POM_ELEMENT_INHERITED, inherited, jdomElement);
  }

  @Override
  public boolean isInherited() {
    return Boolean.parseBoolean(getInherited());
  }

  @Override
  public void setInherited(boolean inherited) {
    setInherited(Boolean.toString(inherited));
  }

  @Override
  public String getVersion() {
    return coordinate.getVersion();
  }

  @Override
  public void setVersion(String version) {
    coordinate.setVersion(version);
  }

  @Override
  public void flushExecutionMap() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, PluginExecution> getExecutionsAsMap() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getKey() {
    return constructKey(getGroupId(), getArtifactId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Plugin clone() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
