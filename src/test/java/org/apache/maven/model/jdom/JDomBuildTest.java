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

import org.apache.maven.model.Plugin;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEFAULT_GOAL;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DIRECTORY;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_FINAL_NAME;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_OUTPUT_DIRECTORY;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SCRIPT_SOURCE_DIRECTORY;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_SOURCE_DIRECTORY;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_TEST_OUTPUT_DIRECTORY;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_TEST_SOURCE_DIRECTORY;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JDomBuildTest {

  private SAXBuilder builder = new SAXBuilder();

  @Test
  public void testGetExtensions() throws Exception {
    String content = "<build></build>";
    Document document = builder.build(new StringReader(content));
    assertNotNull(new JDomBuild(document.getRootElement()).getExtensions());
    assertEquals(0, new JDomBuild(document.getRootElement()).getExtensions().size());

    content = "<build><extensions/></build>";
    document = builder.build(new StringReader(content));
    assertEquals(0, new JDomBuild(document.getRootElement()).getExtensions().size());

    content = "<build><extensions><extension/></extensions></build>";
    document = builder.build(new StringReader(content));
    assertEquals(1, new JDomBuild(document.getRootElement()).getExtensions().size());

  }

  @Test
  public void testGetPluginManagement() throws Exception {
    String content = "<build></build>";
    Document document = builder.build(new StringReader(content));
    assertNull(new JDomBuild(document.getRootElement()).getPluginManagement());

    content = "<build><pluginManagement/></build>";
    document = builder.build(new StringReader(content));
    assertNotNull(new JDomBuild(document.getRootElement()).getPluginManagement());
  }

  @Test
  public void testGetPlugins() throws Exception {
    String content = "<build></build>";
    Document document = builder.build(new StringReader(content));
    assertNotNull(new JDomBuild(document.getRootElement()).getPlugins());
    assertEquals(0, new JDomBuild(document.getRootElement()).getPlugins().size());

    content = "<build><plugins/></build>";
    document = builder.build(new StringReader(content));
    assertEquals(0, new JDomBuild(document.getRootElement()).getPlugins().size());

    content = "<build><plugins><plugin/></plugins></build>";
    document = builder.build(new StringReader(content));
    assertEquals(1, new JDomBuild(document.getRootElement()).getPlugins().size());
  }

  // All other methods throw UnsupportedOperationException

  @Test
  public void testGetOutputDirectory() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getOutputDirectory());

    content = "<build><outputDirectory>OUTPUTDIRECTORY</outputDirectory></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("OUTPUTDIRECTORY", new JDomBuild(buildElm).getOutputDirectory());
  }

  @Test
  public void testGetScriptSourceDirectory() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getScriptSourceDirectory());

    content = "<build><scriptSourceDirectory>SCRIPTSOURCEDIRECTORY</scriptSourceDirectory></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("SCRIPTSOURCEDIRECTORY", new JDomBuild(buildElm).getScriptSourceDirectory());
  }

  @Test
  public void testGetSourceDirectory() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getSourceDirectory());

    content = "<build><sourceDirectory>SOURCEDIRECTORY</sourceDirectory></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("SOURCEDIRECTORY", new JDomBuild(buildElm).getSourceDirectory());
  }

  @Test
  public void testGetTestOutputDirectory() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getTestOutputDirectory());

    content = "<build><testOutputDirectory>TESTOUTPUTDIRECTORY</testOutputDirectory></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("TESTOUTPUTDIRECTORY", new JDomBuild(buildElm).getTestOutputDirectory());
  }

  @Test
  public void testGetTestSourceDirectory() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getTestSourceDirectory());

    content = "<build><testSourceDirectory>TESTSOURCEDIRECTORY</testSourceDirectory></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("TESTSOURCEDIRECTORY", new JDomBuild(buildElm).getTestSourceDirectory());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetExtensions() {
    new JDomBuild(new Element("build")).setExtensions(null);
  }

  @Test
  public void testSetOutputDirectory() throws Exception {
    String content = "<build><outputDirectory>OLD_OUTPUTDIRECTORY</outputDirectory></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setOutputDirectory("NEW_OUTPUTDIRECTORY");
    assertEquals("NEW_OUTPUTDIRECTORY", getChildElementTextTrim(POM_ELEMENT_OUTPUT_DIRECTORY, buildElm));
  }

  @Test
  public void testSetScriptSourceDirectory() throws Exception {
    String content = "<build><scriptSourceDirectory>OLD_SCRIPTSOURCEDIRECTORY</scriptSourceDirectory></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setScriptSourceDirectory("NEW_SCRIPTSOURCEDIRECTORY");
    assertEquals("NEW_SCRIPTSOURCEDIRECTORY", getChildElementTextTrim(POM_ELEMENT_SCRIPT_SOURCE_DIRECTORY, buildElm));
  }

  @Test
  public void testSetSourceDirectory() throws Exception {
    String content = "<build><sourceDirectory>OLD_SOURCEDIRECTORY</sourceDirectory></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setSourceDirectory("NEW_SOURCEDIRECTORY");
    assertEquals("NEW_SOURCEDIRECTORY", getChildElementTextTrim(POM_ELEMENT_SOURCE_DIRECTORY, buildElm));
  }

  @Test
  public void testSetTestOutputDirectoryString() throws Exception {
    String content = "<build><testOutputDirectory>OLD_TESTOUTPUTDIRECTORY</testOutputDirectory></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setTestOutputDirectory("NEW_TESTOUTPUTDIRECTORY");
    assertEquals("NEW_TESTOUTPUTDIRECTORY", getChildElementTextTrim(POM_ELEMENT_TEST_OUTPUT_DIRECTORY, buildElm));
  }

  @Test
  public void testSetTestSourceDirectory() throws Exception {
    String content = "<build><testSourceDirectory>OLD_TESTSOURCEDIRECTORY</testSourceDirectory></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setTestSourceDirectory("NEW_TESTSOURCEDIRECTORY");
    assertEquals("NEW_TESTSOURCEDIRECTORY", getChildElementTextTrim(POM_ELEMENT_TEST_SOURCE_DIRECTORY, buildElm));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddFilter() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).addFilter(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddResource() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).addResource(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddTestResource() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).addTestResource(null);
  }

  @Test
  public void testGetDefaultGoal() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getDefaultGoal());

    content = "<build><defaultGoal>DEFAULTGOAL</defaultGoal></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("DEFAULTGOAL", new JDomBuild(buildElm).getDefaultGoal());
  }

  @Test
  public void testGetDirectory() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getDirectory());

    content = "<build><directory>DIRECTORY</directory></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("DIRECTORY", new JDomBuild(buildElm).getDirectory());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetFilters() throws JDOMException, IOException {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).getFilters();
  }

  @Test
  public void testGetFinalName() throws Exception {
    String content = "<build></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    assertNull(new JDomBuild(buildElm).getFinalName());

    content = "<build><finalName>FINALNAME</finalName></build>";
    buildElm = builder.build(new StringReader(content)).getRootElement();
    assertEquals("FINALNAME", new JDomBuild(buildElm).getFinalName());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetResources() {
    new JDomBuild(new Element("build")).getResources();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTestResources() {
    new JDomBuild(new Element("build")).getTestResources();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveFilter() {
    new JDomBuild(new Element("build")).removeFilter(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveResource() {
    new JDomBuild(new Element("build")).removeResource(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveTestResource() {
    new JDomBuild(new Element("build")).removeTestResource(null);
  }

  @Test
  public void testSetDefaultGoal() throws Exception {
    String content = "<build><defaultGoal>OLD_DEFAULTGOAL</defaultGoal></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setDefaultGoal("NEW_DEFAULTGOAL");
    assertEquals("NEW_DEFAULTGOAL", getChildElementTextTrim(POM_ELEMENT_DEFAULT_GOAL, buildElm));
  }

  @Test
  public void testSetDirectory() throws Exception {
    String content = "<build><directory>OLD_DIRECTORY</directory></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setDirectory("NEW_DIRECTORY");
    assertEquals("NEW_DIRECTORY", getChildElementTextTrim(POM_ELEMENT_DIRECTORY, buildElm));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetFilters() {
    new JDomBuild(new Element("build")).setFilters(null);
  }

  @Test
  public void testSetFinalName() throws Exception {
    String content = "<build><finalName>OLD_FINALNAME</finalName></build>";
    Element buildElm = builder.build(new StringReader(content)).getRootElement();
    new JDomBuild(buildElm).setFinalName("NEW_FINALNAME");
    assertEquals("NEW_FINALNAME", getChildElementTextTrim(POM_ELEMENT_FINAL_NAME, buildElm));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetResources() {
    new JDomBuild(new Element("build")).setResources(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetTestResources() {
    new JDomBuild(new Element("build")).setTestResources(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetPluginManagement() {
    new JDomBuild(new Element("build")).setPluginManagement(null);
  }

  @Test
  public void testAddPlugin() throws Exception {
    Plugin plugin = new Plugin();
    plugin.setGroupId("a");
    plugin.setArtifactId("b");

    String content = "<build><plugins> </plugins></build>";
    Document document = builder.build(new StringReader(content));
    JDomBuild build = new JDomBuild(document.getRootElement());
    assertEquals(0, build.getPlugins().size());
    build.addPlugin(plugin);
    assertEquals(1, build.getPlugins().size());
  }

  @Test
  public void testRemovePlugin() throws Exception {
    String contentPlugin = "<plugin><groupId>a</groupId><artifactId>b</artifactId></plugin>";
    String contentPluginMgmt = "<build><plugins>" + contentPlugin + "</plugins></build>";
    Document documentPlugin = builder.build(new StringReader(contentPlugin));
    Document documentPluginMgmt = builder.build(new StringReader(contentPluginMgmt));
    JDomBuild build = new JDomBuild(documentPluginMgmt.getRootElement());
    assertEquals(1, build.getPlugins().size());
    build.removePlugin(new JDomPlugin(documentPlugin.getRootElement()));
    assertEquals(0, build.getPlugins().size());
  }

  @Test
  public void testSetPlugins() throws Exception {
    Plugin plugin1 = new Plugin();
    plugin1.setGroupId("a");
    plugin1.setArtifactId("b");
    Plugin plugin2 = new Plugin();
    plugin2.setGroupId("c");
    plugin2.setArtifactId("d");

    String content = "<build><plugins> </plugins></build>";
    Document document = builder.build(new StringReader(content));
    JDomBuild build = new JDomBuild(document.getRootElement());
    assertEquals(0, build.getPlugins().size());
    build.setPlugins(asList(plugin1, plugin2));
    assertEquals(2, build.getPlugins().size());
  }
}
