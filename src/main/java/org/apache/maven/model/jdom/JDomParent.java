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

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ARTIFACT_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_GROUP_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_RELATIVE_PATH;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_VERSION;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDOM implementation of the {@link Parent} class. It holds the child elements of the Maven POMs {@code parent} element.
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomParent extends Parent implements JDomBacked {

  private static final long serialVersionUID = 2981841470366853811L;

  private static final String DEFAULT_RELATIVE_PATH = "../pom.xml";

  private final Element jdomElement;

  @SuppressWarnings("WeakerAccess")
  public JDomParent(Element jdomElement) {
    this.jdomElement = jdomElement;

    super.setArtifactId(getChildElementTextTrim(POM_ELEMENT_ARTIFACT_ID, this.jdomElement));
    super.setGroupId(getChildElementTextTrim(POM_ELEMENT_GROUP_ID, this.jdomElement));
    super.setRelativePath(getChildElementTextTrim(POM_ELEMENT_RELATIVE_PATH, this.jdomElement));
    super.setVersion(getChildElementTextTrim(POM_ELEMENT_VERSION, this.jdomElement));
  }

  @SuppressWarnings("WeakerAccess")
  public JDomParent(Element jdomElement, Parent parent) {
    this.jdomElement = jdomElement;

    setArtifactId(parent.getArtifactId());
    setGroupId(parent.getGroupId());
    setRelativePath(parent.getRelativePath());
    setVersion(parent.getVersion());
  }

  @Override
  public void setArtifactId(String artifactId) {
    rewriteElement(POM_ELEMENT_ARTIFACT_ID, artifactId, jdomElement);
    super.setArtifactId(artifactId);
  }

  @Override
  public void setGroupId(String groupId) {
    rewriteElement(POM_ELEMENT_GROUP_ID, groupId, jdomElement);
    super.setGroupId(groupId);
  }

  @Override
  public void setRelativePath(String relativePath) {
    if (relativePath == null ||
            getChildElementTextTrim(POM_ELEMENT_RELATIVE_PATH, this.jdomElement) == null
                    && DEFAULT_RELATIVE_PATH.equals(relativePath)
                    && DEFAULT_RELATIVE_PATH.equals(super.getRelativePath())) {
      rewriteElement(POM_ELEMENT_RELATIVE_PATH, null, jdomElement);
      super.setRelativePath(null);
    } else {
      rewriteElement(POM_ELEMENT_RELATIVE_PATH, relativePath, jdomElement);
      super.setRelativePath(relativePath);
    }
  }

  @Override
  public void setVersion(String version) {
    rewriteElement(POM_ELEMENT_VERSION, version, jdomElement);
    super.setVersion(version);
  }

  /** {@inheritDoc} */
  @Override
  public Parent clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
