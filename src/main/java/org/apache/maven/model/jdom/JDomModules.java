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

import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static java.util.Collections.emptyList;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_MODULE;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_MODULES;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.removeChildElement;

/**
 * JDOM implementation of the {@link ArrayList<String>} class, holding the child elements of the Maven POMs
 * {@code modules} element.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomModules extends ArrayList<String> implements JDomBacked {

  private static final long serialVersionUID = -4351000399917342663L;

  private Element jdomElement;

  private final JDomBacked parent;

  @SuppressWarnings("WeakerAccess")
  public JDomModules(Element jdomElement, JDomBacked parent) {
    super(transformModuleElementsToModuleNameList(jdomElement));
    this.jdomElement = jdomElement;
    this.parent = parent;
  }

  private static List<String> transformModuleElementsToModuleNameList(Element jdomElement) {
    if (jdomElement == null) {
      return emptyList();
    } else {
      List<Element> moduleElements = jdomElement.getContent(new ElementFilter(POM_ELEMENT_MODULE, jdomElement.getNamespace()));
      List<String> moduleNameList = new ArrayList<>(moduleElements.size());
      for (Element moduleElement : moduleElements) {
        moduleNameList.add(moduleElement.getTextTrim());
      }
      return moduleNameList;
    }
  }

  @Override
  public boolean add(String module) {
    assert module != null;

    if (jdomElement == null || jdomElement.getParent() == null) {
      jdomElement = insertNewElement(POM_ELEMENT_MODULES, parent.getJDomElement());
    }

    Element newElement = insertNewElement(POM_ELEMENT_MODULE, jdomElement);
    newElement.removeContent(0); // Remove line break that is created with the new element.
    newElement.addContent(module);

    return super.add(module);
  }

  @Override
  public boolean remove(final Object module) {
    List<Element> removeElements = jdomElement.getContent(new ElementFilter() {
      private static final long serialVersionUID = 4738814199213496535L;

      @Override
      public Element filter(Object content) {
        Element element = super.filter(content);
        return element == null || !module.equals(element.getTextTrim()) ? null : element;
      }
    });

    for (Element removeElement : removeElements) {
      removeChildElement(jdomElement, removeElement);
    }

    boolean remove = super.remove(module);
    if (super.isEmpty()) {
      removeChildElement(parent.getJDomElement(), jdomElement);
    }
    return remove;
  }

  @Override
  public boolean addAll(Collection<? extends String> modules) {
    boolean added = false;
    for (String module : modules) {
      added |= this.add(module);
    }
    return added;
  }

  @Override
  public boolean addAll(int index, Collection<? extends String> modules) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> modules) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> modules) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    while (size() > 0) {
      remove(0);
    }
  }

  @Override
  public String set(int index, String module) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int index, String module) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String remove(int index) {
    String module = get(index);
    remove(module);
    return module;
  }

  @Override
  public int lastIndexOf(Object module) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<String> listIterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<String> listIterator(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Object clone() {
    throw new UnsupportedOperationException();
  }

  /** {@inheritDoc} */
  @Override
  public Element getJDomElement() {
    return jdomElement;
  }
}
