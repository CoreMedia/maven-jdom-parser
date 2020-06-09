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

import org.apache.maven.model.PluginExecution;
import org.jdom2.Element;
import org.jdom2.Text;

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_CONFIGURATION;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.resetIndentations;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

public class JDomPluginExecution extends PluginExecution implements JDomBacked {

  private static final long serialVersionUID = -6973299112773078102L;

  private final Element jdomElement;

  JDomPluginExecution(Element jdomElement) {
    this.jdomElement = jdomElement;
  }

  @Override
  public Element getJDomElement() {
    return jdomElement;
  }

  @Override
  public Object getConfiguration() {
    Element elm = getChildElement(POM_ELEMENT_CONFIGURATION, jdomElement);
    if (elm == null) {
      return null;
    } else {
      return new JDomConfiguration(elm);
    }
  }

  @Override
  public void setConfiguration(Object configuration) {
    if (configuration == null) {
      rewriteElement(POM_ELEMENT_CONFIGURATION, null, jdomElement);
    } else if (configuration instanceof JDomConfiguration) {
      Element newJDomConfigurationElement = ((JDomConfiguration) configuration).getJDomElement().clone();

      JDomConfiguration oldJDomConfiguration = (JDomConfiguration) getConfiguration();
      if (oldJDomConfiguration == null) {
        jdomElement.addContent(
                jdomElement.getContentSize() - 1,
                asList(
                        new Text("\n" + detectIndentation(jdomElement)),
                        newJDomConfigurationElement));
      } else {
        int replaceIndex = jdomElement.indexOf(oldJDomConfiguration.getJDomElement());
        jdomElement.removeContent(replaceIndex);
        jdomElement.addContent(replaceIndex, newJDomConfigurationElement);
      }

      resetIndentations(jdomElement, detectIndentation(jdomElement));
      resetIndentations(newJDomConfigurationElement, detectIndentation(jdomElement) + "  ");
    }
  }

}
