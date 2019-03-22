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

import org.apache.maven.model.Scm;
import org.jdom2.Element;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_CONNECTION;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEVELOPER_CONNECTION;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_TAG;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_URL;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDOM implementation of the {@link Scm} class. It holds the child elements of the Maven POMs {@code scm} element.
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomScm extends Scm implements JDomBacked {

  private static final long serialVersionUID = -1073839677237527269L;

  private Element jdomElement;

  @SuppressWarnings("WeakerAccess")
  public JDomScm(Element jdomElement) {
    this.jdomElement = jdomElement;

    super.setConnection(getChildElementTextTrim(POM_ELEMENT_CONNECTION, this.jdomElement));
    super.setDeveloperConnection(getChildElementTextTrim(POM_ELEMENT_DEVELOPER_CONNECTION, this.jdomElement));
    super.setTag(getChildElementTextTrim(POM_ELEMENT_TAG, this.jdomElement));
    super.setUrl(getChildElementTextTrim(POM_ELEMENT_URL, this.jdomElement));
  }

  @SuppressWarnings("WeakerAccess")
  public JDomScm(Element jdomElement, Scm scm) {
    this.jdomElement = jdomElement;

    setConnection(scm.getConnection());
    setDeveloperConnection(scm.getDeveloperConnection());
    setTag(scm.getTag());
    setUrl(scm.getUrl());
  }

  @Override
  public void setConnection(String connection) {
    rewriteElement(POM_ELEMENT_CONNECTION, connection, jdomElement);
    super.setConnection(connection);
  }

  @Override
  public void setDeveloperConnection(String developerConnection) {
    rewriteElement(POM_ELEMENT_DEVELOPER_CONNECTION, developerConnection, jdomElement);
    super.setDeveloperConnection(developerConnection);
  }

  @Override
  public void setTag(String tag) {
    rewriteElement(POM_ELEMENT_TAG, tag, jdomElement);
    super.setTag(tag);
  }

  @Override
  public void setUrl(String url) {
    rewriteElement(POM_ELEMENT_URL, url, jdomElement);
    super.setUrl(url);
  }

  /** {@inheritDoc} */
  @Override
  public Scm clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
