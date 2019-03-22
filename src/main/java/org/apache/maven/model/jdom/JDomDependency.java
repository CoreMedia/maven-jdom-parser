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
import org.apache.maven.model.Exclusion;
import org.jdom2.Element;

import java.util.List;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_CLASSIFIER;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_OPTIONAL;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SCOPE;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SYSTEM_PATH;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_TYPE;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms DEPENDENCY element
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomDependency extends Dependency implements JDomBacked, MavenCoordinate {

  private final Element jdomElement;

  private final MavenCoordinate coordinate;

  public JDomDependency(Element jdomElement) {
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
  public String getClassifier() {
    return getChildElementTextTrim(POM_ELEMENT_CLASSIFIER, jdomElement);
  }

  @Override
  public void setClassifier(String classifier) {
    rewriteElement(POM_ELEMENT_CLASSIFIER, classifier, jdomElement, jdomElement.getNamespace());
  }

  @Override
  public List<Exclusion> getExclusions() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setExclusions(List<Exclusion> exclusions) {
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
  public String getOptional() {
    return getChildElementTextTrim(POM_ELEMENT_OPTIONAL, jdomElement);
  }

  @Override
  public void setOptional(String optional) {
    rewriteElement(POM_ELEMENT_OPTIONAL, optional, jdomElement, jdomElement.getNamespace());
  }

  @Override
  public boolean isOptional() {
    return Boolean.parseBoolean(getOptional());
  }

  @Override
  public void setOptional(boolean optional) {
    setOptional(Boolean.toString(optional));
  }

  @Override
  public String getScope() {
    return getChildElementTextTrim(POM_ELEMENT_SCOPE, jdomElement);
  }

  @Override
  public void setScope(String scope) {
    rewriteElement(POM_ELEMENT_SCOPE, scope, jdomElement, jdomElement.getNamespace());
  }

  @Override
  public String getSystemPath() {
    return getChildElementTextTrim(POM_ELEMENT_SYSTEM_PATH, jdomElement);
  }

  @Override
  public void setSystemPath(String systemPath) {
    rewriteElement(POM_ELEMENT_SYSTEM_PATH, systemPath, jdomElement, jdomElement.getNamespace());
  }

  @Override
  public String getType() {
    return getChildElementTextTrim(POM_ELEMENT_TYPE, jdomElement);
  }

  @Override
  public void setType(String type) {
    rewriteElement(POM_ELEMENT_TYPE, type, jdomElement, jdomElement.getNamespace());
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
  public String getManagementKey() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
