package org.apache.maven.model.jdom.it;

/*
 * Copyright 2018 CoreMedia AG, Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginContainer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Tests transformations of {@code plugins}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PluginsEtlIT extends AbstractJDomModelEtlIT {

  @Test
  public void addPlugin() throws IOException {
    Plugin plugin = new Plugin();
    plugin.setGroupId("org.apache.maven.plugins");
    plugin.setArtifactId("maven-antrun-plugin");
    plugin.setVersion("1.8");
    getPluginPluginContainer().addPlugin(plugin);
    assertTransformation();
  }

  @Test
  public void changeConfigurationValue() throws IOException {
    ((Xpp3Dom) getPluginPluginContainer().getPlugins().get(0).getConfiguration()).getChild("skipDeploy").setValue("true");
    assertTransformation();
  }

  @Test
  public void changePluginVersion() throws IOException {
    for (Plugin plugin : getPluginPluginContainer().getPlugins().toArray(new Plugin[]{})) {
      if (plugin.getArtifactId().equals("maven-site-plugin")) {
        plugin.setVersion("3.7.1");
      }
    }
    assertTransformation();
  }

  @Test
  public void removePlugin() throws IOException {
    Plugin plugin = new Plugin();
    plugin.setGroupId("org.apache.maven.plugins");
    plugin.setArtifactId("maven-site-plugin");
    getPluginPluginContainer().removePlugin(plugin);
    assertTransformation();
  }

  @Test
  public void resetJDomConfiguration() throws IOException {
    Object configuration = getSourceModel().getBuild().getPlugins().get(0).getConfiguration();
    getPluginPluginContainer().getPlugins().get(0).setConfiguration(configuration);
    assertTransformation();
  }

  @Test
  public void setJDomConfiguration() throws IOException {
    Object configuration = getSourceModel().getBuild().getPlugins().get(0).getConfiguration();
    getPluginPluginContainer().getPlugins().get(0).setConfiguration(configuration);
    assertTransformation();
  }

  @Test
  public void setJDomDependencies() throws IOException {
    List<Dependency> dependencies = getSourceModel().getBuild().getPlugins().get(0).getDependencies();
    getPluginPluginContainer().getPlugins().get(0).setDependencies(dependencies);
    assertTransformation();
  }

  protected PluginContainer getPluginPluginContainer() {
    return subjectModel.getBuild();
  }
}
