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

import org.apache.maven.model.Build;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Resource;
import org.apache.maven.model.jdom.util.JDomUtils;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms BUILD element
 *
 * @author Robert Scholte (for <a href="https://github.com/apache/maven-release/">Maven Release projct</a>, version 3.0)
 */
public class JDomBuild extends Build {

  private final Element build;

  public JDomBuild(Element build) {
    this.build = build;
  }

  @Override
  public String getDefaultGoal() {
    return getChildElementTextTrim("defaultGoal", build);
  }

  @Override
  public void setDefaultGoal(String defaultGoal) {
    rewriteElement("defaultGoal", defaultGoal, build, build.getNamespace());
  }

  @Override
  public String getDirectory() {
    return getChildElementTextTrim("directory", build);
  }

  @Override
  public void setDirectory(String directory) {
    rewriteElement("directory", directory, build, build.getNamespace());
  }

  @Override
  public List<Extension> getExtensions() {
    Element extensionsElm = build.getChild("extensions", build.getNamespace());
    if (extensionsElm == null) {
      return Collections.emptyList();
    } else {
      List<Element> extensionElms = extensionsElm.getChildren("extension", build.getNamespace());
      List<Extension> extensions = new ArrayList<>(extensionElms.size());
      for (Element extensionElm : extensionElms) {
        extensions.add(new JDomExtension(extensionElm));
      }
      return extensions;
    }
  }

  @Override
  public void setExtensions(List<Extension> extensions) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getFilters() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFilters(List<String> filters) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getFinalName() {
    return getChildElementTextTrim("finalName", build);
  }

  @Override
  public void setFinalName(String finalName) {
    rewriteElement("finalName", finalName, build, build.getNamespace());
  }

  @Override
  public String getOutputDirectory() {
    return getChildElementTextTrim("outputDirectory", build);
  }

  @Override
  public void setOutputDirectory(String outputDirectory) {
    rewriteElement("outputDirectory", outputDirectory, build, build.getNamespace());
  }

  @Override
  public PluginManagement getPluginManagement() {
    Element pluginManagementElm = build.getChild("pluginManagement", build.getNamespace());
    if (pluginManagementElm == null) {
      return null;
    } else {
      return new JDomPluginManagement(pluginManagementElm);
    }
  }

  @Override
  public void setPluginManagement(PluginManagement pluginManagement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Plugin> getPlugins() {
    Element pluginsElm = build.getChild("plugins", build.getNamespace());
    if (pluginsElm == null) {
      return Collections.emptyList();
    } else {
      return new JDomPlugins(pluginsElm);
    }
  }

  @Override
  public void setPlugins(List<Plugin> plugins) {
    if (plugins == null) {
      JDomUtils.rewriteElement("plugins", null, build, build.getNamespace());
    } else {
      new JDomPlugins(insertNewElement("plugins", build)).addAll(plugins);
    }

  }

  @Override
  public List<Resource> getResources() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setResources(List<Resource> resources) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getScriptSourceDirectory() {
    return getChildElementTextTrim("scriptSourceDirectory", build);
  }

  @Override
  public void setScriptSourceDirectory(String scriptSourceDirectory) {
    rewriteElement("scriptSourceDirectory", scriptSourceDirectory, build, build.getNamespace());
  }

  @Override
  public String getSourceDirectory() {
    return getChildElementTextTrim("sourceDirectory", build);
  }

  @Override
  public void setSourceDirectory(String sourceDirectory) {
    rewriteElement("sourceDirectory", sourceDirectory, build, build.getNamespace());
  }

  @Override
  public String getTestOutputDirectory() {
    return getChildElementTextTrim("testOutputDirectory", build);
  }

  @Override
  public void setTestOutputDirectory(String testOutputDirectory) {
    rewriteElement("testOutputDirectory", testOutputDirectory, build, build.getNamespace());
  }

  @Override
  public List<Resource> getTestResources() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTestResources(List<Resource> testResources) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getTestSourceDirectory() {
    return getChildElementTextTrim("testSourceDirectory", build);
  }

  @Override
  public void setTestSourceDirectory(String testSourceDirectory) {
    rewriteElement("testSourceDirectory", testSourceDirectory, build, build.getNamespace());
  }

  @Override
  public void flushPluginMap() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, Plugin> getPluginsAsMap() {
    throw new UnsupportedOperationException();
  }
}
