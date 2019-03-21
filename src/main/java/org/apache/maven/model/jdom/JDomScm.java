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

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms SCM element
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomScm extends Scm {

  private Element scm;

  public JDomScm(Element scm) {
    this.scm = scm;
  }

  @Override
  public String getConnection() {
    return getChildElementTextTrim("connection", scm);
  }

  @Override
  public void setConnection(String connection) {
    rewriteElement("connection", connection, scm, scm.getNamespace());
  }

  @Override
  public String getDeveloperConnection() {
    return getChildElementTextTrim("developerConnection", scm);
  }

  @Override
  public void setDeveloperConnection(String developerConnection) {
    rewriteElement("developerConnection", developerConnection, scm, scm.getNamespace());
  }

  @Override
  public String getTag() {
    return getChildElementTextTrim("tag", scm);
  }

  @Override
  public void setTag(String tag) {
    rewriteElement("tag", tag, scm, scm.getNamespace());
  }

  @Override
  public String getUrl() {
    return getChildElementTextTrim("url", scm);
  }

  @Override
  public void setUrl(String url) {
    rewriteElement("url", url, scm, scm.getNamespace());
  }
}
