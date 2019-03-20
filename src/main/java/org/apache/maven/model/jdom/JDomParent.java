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

import org.apache.maven.model.Parent;
import org.jdom2.Element;

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms PARENT element
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomParent extends Parent implements MavenCoordinate {

  private Element parent;

  private final MavenCoordinate coordinate;

  public JDomParent(Element parent) {
    this.parent = parent;
    this.coordinate = new JDomMavenCoordinate(parent);
  }

  @Override
  public String getArtifactId() {
    return this.coordinate.getArtifactId();
  }

  @Override
  public void setArtifactId(String artifactId) {
    this.coordinate.setArtifactId(artifactId);
  }

  @Override
  public String getGroupId() {
    return this.coordinate.getGroupId();
  }

  @Override
  public void setGroupId(String groupId) {
    this.coordinate.setGroupId(groupId);
  }

  @Override
  public String getRelativePath() {
    return getChildElementTextTrim("relativePath", parent);
  }

  @Override
  public void setRelativePath(String relativePath) {
    rewriteElement("relativePath", relativePath, parent, parent.getNamespace());
  }

  @Override
  public String getVersion() {
    return this.coordinate.getVersion();
  }

  @Override
  public void setVersion(String version) {
    this.coordinate.setVersion(version);
  }

  @Override
  public String getName() {
    return "parent";
  }
}
