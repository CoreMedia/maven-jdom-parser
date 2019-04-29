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

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static java.util.Arrays.asList;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_DEPENDENCY;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.removeChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.resetIndentations;
import static org.codehaus.plexus.util.StringUtils.defaultString;

/**
 * JDOM implementation of POMs {@code dependencies} element.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class JDomDependencies extends ArrayList<Dependency> implements JDomBacked {

  private final Element jdomElement;

  public JDomDependencies(Element jdomElement) {
    super(transformToJDomDependencyList(getDependencyElements(jdomElement)));
    this.jdomElement = jdomElement;
  }

  private static List<Element> getDependencyElements(Element dependencies) {
    return dependencies.getContent(new ElementFilter(POM_ELEMENT_DEPENDENCY, dependencies.getNamespace()));
  }

  private static List<JDomDependency> transformToJDomDependencyList(List<Element> dependencyElements) {
    List<JDomDependency> jDomDependencyList = new ArrayList<>(dependencyElements.size());
    for (Element dependencyElement : dependencyElements) {
      jDomDependencyList.add(new JDomDependency(dependencyElement));
    }
    return jDomDependencyList;
  }

  @Override
  public boolean add(Dependency dependency) {
    Element newElement;
    if (dependency instanceof JDomDependency) {
      newElement = ((JDomDependency) dependency).getJDomElement().clone();
      jdomElement.addContent(
              jdomElement.getContentSize() - 1,
              asList(
                      new Text("\n" + detectIndentation(jdomElement)),
                      newElement));
      resetIndentations(jdomElement, detectIndentation(jdomElement));
      resetIndentations(newElement, detectIndentation(jdomElement) + "  ");
    } else {
      newElement = insertNewElement(POM_ELEMENT_DEPENDENCY, jdomElement);
      JDomDependency jDomDependency = new JDomDependency(newElement);

      jDomDependency.setGroupId(dependency.getGroupId());
      jDomDependency.setArtifactId(dependency.getArtifactId());
      jDomDependency.setVersion(dependency.getVersion());

      String classifier = dependency.getClassifier();
      if (classifier != null) {
        jDomDependency.setClassifier(classifier);
      }

      List<Exclusion> exclusions = dependency.getExclusions();
      if (!exclusions.isEmpty()) {
        jDomDependency.setExclusions(exclusions);
      }

      if (dependency.isOptional()) {
        jDomDependency.setOptional(true);
      }

      String scope = dependency.getScope();
      if (!"compile".equals(scope)) {
        jDomDependency.setScope(scope);
      }


      String systemPath = dependency.getSystemPath();
      if (systemPath != null) {
        jDomDependency.setSystemPath(systemPath);
      }

      String type = dependency.getType();
      if (!"jar".equals(type)) {
        jDomDependency.setType(type);
      }
    }

    return super.add(dependency);
  }

  @Override
  public boolean remove(final Object dependency) {
    Dependency removeDependency = (Dependency) dependency;
    for (Dependency candidate : this) {
      if (candidate.getGroupId().equals(removeDependency.getGroupId())
              && candidate.getArtifactId().equals(removeDependency.getArtifactId())
              && defaultString(candidate.getType(), "jar").equals(defaultString(removeDependency.getType(), "jar"))) {
        removeChildElement(jdomElement, ((JDomDependency) candidate).getJDomElement());
        return super.remove(removeDependency);
      }
    }
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends Dependency> dependencies) {
    boolean added = false;
    for (Dependency dependency : dependencies) {
      added |= this.add(dependency);
    }
    return added;
  }

  @Override
  public boolean addAll(int index, Collection<? extends Dependency> dependencies) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> dependencies) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> dependencies) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    while (size() > 0) {
      remove(0);
    }
  }

  @Override
  public Dependency set(int index, Dependency dependency) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int index, Dependency dependency) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Dependency remove(int index) {
    Dependency dependency = get(index);
    remove(dependency);
    return dependency;
  }

  @Override
  public int lastIndexOf(Object dependency) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<Dependency> listIterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<Dependency> listIterator(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Dependency> subList(int fromIndex, int toIndex) {
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
