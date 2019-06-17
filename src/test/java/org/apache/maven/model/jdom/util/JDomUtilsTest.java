package org.apache.maven.model.jdom.util;

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
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.jdom2.filter.Filters.element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Unit test for the {@link JDomUtils} class.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomUtilsTest {

  @Test
  public void testDetectIndentation() throws JDOMException, IOException {
    Element root = new SAXBuilder()
            .build(this.getClass().getResource("JDomUtilsTest_testDetectIndentation.xml"))
            .getRootElement();
    assertIndentation(4, detectIndentation(root.getContent(element("detectByParent")).get(0)));
    assertIndentation(6, detectIndentation(root.getContent(element("detectByEndTag")).get(0)));
    assertIndentation(8, detectIndentation(root.getContent(element("detectByChild")).get(0)));
  }

  private static void assertIndentation(int expectedIndentSpaces, String actualIndentation) {
    assertTrue(actualIndentation.matches(" *"));
    assertEquals(expectedIndentSpaces, actualIndentation.length());
  }
}
