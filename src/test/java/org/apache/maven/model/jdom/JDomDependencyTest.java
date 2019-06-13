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
import org.junit.Test;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_ARTIFACT_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_CLASSIFIER;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEPENDENCY;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_GROUP_ID;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_OPTIONAL;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SCOPE;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SYSTEM_PATH;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_TYPE;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for the {@link JDomDependency} class.
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomDependencyTest {

  @Test
  public void testArtifactId() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getArtifactId());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_ARTIFACT_ID).setText(" an-artifact ")));
    assertEquals("an-artifact", model.getArtifactId());

    model.setArtifactId(" another-artifact ");
    assertEquals("another-artifact", model.getArtifactId());
  }

  @Test
  public void testClassifier() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getClassifier());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_CLASSIFIER).setText(" a-classifier ")));
    assertEquals("a-classifier", model.getClassifier());

    model.setClassifier(" another-classifier ");
    assertEquals("another-classifier", model.getClassifier());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddExclusion() {
    new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)).addExclusion(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetExclusions() {
    new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)).getExclusions();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveExclusion() {
    new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)).removeExclusion(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetExclusions() {
    new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)).setExclusions(null);
  }

  @Test
  public void testGroupId() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getGroupId());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_GROUP_ID).setText(" a.group.id ")));
    assertEquals("a.group.id", model.getGroupId());

    model.setGroupId(" another.group.id ");
    assertEquals("another.group.id", model.getGroupId());
  }

  @Test
  public void testOptional() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getOptional());
    assertFalse(model.isOptional());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_OPTIONAL).setText(" true ")));
    assertEquals("true", model.getOptional());
    assertTrue(model.isOptional());

    model.setOptional(" false ");
    assertEquals("false", model.getOptional());
    assertFalse(model.isOptional());

    model.setOptional(true);
    assertTrue(model.isOptional());
  }

  @Test
  public void testScope() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getScope());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_SCOPE).setText(" provided ")));
    assertEquals("provided", model.getScope());

    model.setScope(" runtime ");
    assertEquals("runtime", model.getScope());
  }

  @Test
  public void testSystemPath() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getSystemPath());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_SYSTEM_PATH).setText(" /a/path ")));
    assertEquals("/a/path", model.getSystemPath());

    model.setSystemPath(" /another/path ");
    assertEquals("/another/path", model.getSystemPath());
  }

  @Test
  public void testType() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertEquals("jar", model.getType());
    assertNull(model.getJDomElement().getChildText(POM_ELEMENT_TYPE));

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_TYPE).setText(" ear ")));
    assertEquals("ear", model.getType());

    model.setType(null);
    assertEquals("jar", model.getType());
    assertNull(model.getJDomElement().getChildText(POM_ELEMENT_TYPE));

    model.setType(" jar ");
    assertEquals("jar", model.getType());
    assertEquals(" jar ", model.getJDomElement().getChildText(POM_ELEMENT_TYPE));
  }

  @Test
  public void testVersion() {
    JDomDependency model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY));
    assertNull(model.getVersion());

    model = new JDomDependency(new Element(POM_ELEMENT_DEPENDENCY)
            .addContent(new Element(POM_ELEMENT_VERSION).setText(" 1-SNAPSHOT ")));
    assertEquals("1-SNAPSHOT", model.getVersion());

    model.setVersion(" 1.2.3 ");
    assertEquals("1.2.3", model.getVersion());
  }
}
