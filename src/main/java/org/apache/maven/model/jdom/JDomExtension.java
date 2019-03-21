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

import org.apache.maven.model.Extension;
import org.jdom2.Element;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ARTIFACT_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_GROUP_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_VERSION;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms EXTENSION element
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomExtension extends Extension implements JDomBacked {

  private final Element jdomElement;

  public JDomExtension(Element jdomElement) {
    this.jdomElement = jdomElement;

    super.setArtifactId(getChildElementTextTrim(POM_ELEMENT_ARTIFACT_ID, this.jdomElement));
    super.setGroupId(getChildElementTextTrim(POM_ELEMENT_GROUP_ID, this.jdomElement));
    super.setVersion(getChildElementTextTrim(POM_ELEMENT_VERSION, this.jdomElement));
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
  public void setVersion(String version) {
    rewriteElement(POM_ELEMENT_VERSION, version, jdomElement);
    super.setVersion(version);
  }

  /** {@inheritDoc} */
  @Override
  public Extension clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
