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

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.StringReader;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ARTIFACT_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_GROUP_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_VERSION;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JDomExtensionTest {

  private SAXBuilder builder = new SAXBuilder();

  @Test
  public void testGetArtifactId() throws Exception {
    String content = "<extension></extension>";
    Element extensionElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomExtension(extensionElm).getArtifactId());

    content = "<extension><artifactId>ARTIFACTID</artifactId></extension>";
    extensionElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("ARTIFACTID", new JDomExtension(extensionElm).getArtifactId());
  }

  @Test
  public void testGetGroupId() throws Exception {
    String content = "<extension></extension>";
    Element extensionElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomExtension(extensionElm).getGroupId());

    content = "<extension><groupId>GROUPID</groupId></extension>";
    extensionElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("GROUPID", new JDomExtension(extensionElm).getGroupId());
  }

  @Test
  public void testGetVersion() throws Exception {
    String content = "<extension></extension>";
    Element extensionElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomExtension(extensionElm).getVersion());

    content = "<extension><version>VERSION</version></extension>";
    extensionElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("VERSION", new JDomExtension(extensionElm).getVersion());
  }

  @Test
  public void testSetArtifactIdString() throws Exception {
    String content = "<extension><artifactId>OLD_ARTIFACTID</artifactId></extension>";
    Element extensionElm = builder.build(new StringReader(content)).getRootElement();
    new JDomExtension(extensionElm).setArtifactId("NEW_ARTIFACTID");
    assertEquals("NEW_ARTIFACTID", getChildElementTextTrim(POM_ELEMENT_ARTIFACT_ID, extensionElm));
  }

  @Test
  public void testSetGroupId() throws Exception {
    String content = "<extension><groupId>OLD_GROUPID</groupId></extension>";
    Element extensionElm = builder.build(new StringReader(content)).getRootElement();
    new JDomExtension(extensionElm).setGroupId("NEW_GROUPID");
    assertEquals("NEW_GROUPID", getChildElementTextTrim(POM_ELEMENT_GROUP_ID, extensionElm));
  }

  @Test
  public void testSetVersion() throws Exception {
    String content = "<extension><version>OLD_VERSION</version></extension>";
    Element extensionElm = builder.build(new StringReader(content)).getRootElement();
    new JDomExtension(extensionElm).setVersion("NEW_VERSION");
    assertEquals("NEW_VERSION", getChildElementTextTrim(POM_ELEMENT_VERSION, extensionElm));
  }
}
