package org.apache.maven.model.jdom;

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

import org.apache.maven.model.Activation;
import org.apache.maven.model.ActivationFile;
import org.apache.maven.model.ActivationOS;
import org.apache.maven.model.ActivationProperty;
import org.jdom2.Element;

import static java.lang.Boolean.parseBoolean;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDOM implementation of POMs {@code activation} element.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomActivation extends Activation {

  private static final String ELEMENT_ACTIVE_BY_DEFAULT = "activeByDefault";
  private static final String ELEMENT_JDK = "jdk";
  private static final String ELEMENT_PROPERTY = "property";

  private Element jdomElement;

  public JDomActivation(Element element) {
    jdomElement = element;

    super.setActiveByDefault(parseBoolean(getChildElementTextTrim(ELEMENT_ACTIVE_BY_DEFAULT, jdomElement)));
    super.setJdk(getChildElementTextTrim(ELEMENT_JDK, jdomElement));

    Element propertyElement = getChildElement(ELEMENT_PROPERTY, jdomElement);
    if (propertyElement != null) {
      super.setProperty(new JDomActivationProperty(propertyElement));
    }
  }

  @Override
  public void setActiveByDefault(boolean activeByDefault) {
    if (activeByDefault || super.isActiveByDefault()) {
      // Don't touch if original was 'true' and isn't actually changed. Otherwise remove on 'false'.
      rewriteElement(ELEMENT_ACTIVE_BY_DEFAULT, activeByDefault ? Boolean.TRUE.toString() : null, jdomElement);
    }
    super.setActiveByDefault(activeByDefault);
  }

  @Override
  public ActivationFile getFile() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFile(ActivationFile file) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setJdk(String jdk) {
    rewriteElement(ELEMENT_JDK, jdk, jdomElement);
    super.setJdk(jdk);
  }

  @Override
  public ActivationOS getOs() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setOs(ActivationOS os) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setProperty(ActivationProperty property) {
    if (property == null) {
      rewriteElement(ELEMENT_PROPERTY, null, jdomElement);
    } else {
      JDomActivationProperty jdomProperty = (JDomActivationProperty) super.getProperty();
      if (jdomProperty == null) {
        Element element = insertNewElement(ELEMENT_PROPERTY, jdomElement);
        jdomProperty = new JDomActivationProperty(element);
      }

      String name = property.getName();
      if (name != null || jdomProperty.getName() != null) {
        jdomProperty.setName(name);
      }

      String value = property.getValue();
      if (value != null || jdomProperty.getValue() != null) {
        jdomProperty.setValue(value);
      }
    }

    super.setProperty(property);
  }

  public Element getJDomElement() {
    return jdomElement;
  }
}
