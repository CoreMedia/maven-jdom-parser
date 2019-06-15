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

import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.util.IteratorIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROFILE;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROFILES;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROPERTIES;

/**
 * JDom cleanup methods for:
 * <ol>
 * <li>profiles</li>
 * </ol>
 *
 * @author https://github.com/eva-mueller-coremedia  (for <a href="https://github.com/CoreMedia/maven-jdom-parser">Maven JDom Parser</a>, version 3.0)
 */
public class JDomCleanupHelper {

  /**
   * Remove all empty profiles and profile tags.<br>
   * Empty is defined as
   * <ul>
   * <li>for tag <b>profiles</b>: there are no profile tags</li>
   * <li>for tag <b>profile</b>: Either there are only the tags activation and id present or all other tags are empty</li>
   * </ul>
   * Thus, empty tags may contain comments which will be removed as well.
   *
   * @param rootElement the root element.
   */
  public static void cleanupEmptyProfiles(Element rootElement) {
    IteratorIterable<Element> filteredElements = rootElement.getDescendants(new ElementFilter(POM_ELEMENT_PROFILES));
    List<Element> profiles = new ArrayList<>();
    for (Element profilesElement : filteredElements) {
      profiles.add(profilesElement);
    }
    for (Element profilesElement : profiles) {
      removeElementWithEmptyChildren(profilesElement,
              POM_ELEMENT_PROFILE,
              Arrays.asList(JDomCfg.POM_ELEMENT_ID, JDomCfg.POM_ELEMENT_ACTIVATION)
      );
      if (!profilesElement.getDescendants(new ElementFilter(POM_ELEMENT_PROFILE)).hasNext()) {
        JDomUtils.removeChildElement(rootElement, profilesElement);
      }
    }
  }

  /**
   * Remove all empty children with tag name from parent element.<br>
   * The child is considered as empty if the child has either:
   * <ul>
   * <li>no children itself or</li>
   * <li>only children which are ignored or/and</li>
   * <li>empty children itself which are not ignored</li>
   * </ul>
   *
   * @param parent         the parent element.
   * @param tagName        the tag name to search for.
   * @param ignoreChildren List of children tag names which are ignored when searching for child elements.
   */
  private static void removeElementWithEmptyChildren(Element parent, String tagName, List<String> ignoreChildren) {
    // Example:
    // parent === 'profiles' Element
    // tagName === 'profile'
    // ignoreChildren === ['id', 'activation']

    // filteredElements === direct children of parent which are of type Element and matching 'tagName'
    IteratorIterable<Element> filteredElements = parent.getDescendants(new ElementFilter(tagName));

    List<Content> contentToBeRemoved = new ArrayList<>();
    for (Element filteredElement : filteredElements) {
      boolean empty = true;
      for (Element child : filteredElement.getChildren()) {
        if (!ignoreChildren.contains(child.getName()) && child.getChildren().size() > 0) {
          empty = false;
          break;
        }
      }
      if (empty) {
        contentToBeRemoved.add(filteredElement);
        contentToBeRemoved.addAll(getAttachedComments(filteredElement));
      }
    }
    for (Content elementToBeRemoved : contentToBeRemoved) {
      JDomUtils.removeChildContent(parent, elementToBeRemoved);
    }
  }

  /**
   * Remove childless element and its comments.<br>
   *
   * @param elementToRemove the element to remove.
   */
  private static void removeElementWithEmptyChildren(Element elementToRemove) {
    // filteredElements === direct children of elementToRemove which are of type Element and matching 'tagName'
    List<Content> contentToBeRemoved = new ArrayList<>();
    if (elementToRemove.getChildren().size() == 0) {
      contentToBeRemoved.add(elementToRemove);
      contentToBeRemoved.addAll(getAttachedComments(elementToRemove));
    }
    for (Content elementToBeRemoved : contentToBeRemoved) {
      JDomUtils.removeChildContent(elementToRemove, elementToBeRemoved);
    }
  }

  /**
   * Get all comments attached of the element.
   *
   * @param element the element to consider.
   * @return List of {@link Comment}s
   */
  private static List<Content> getAttachedComments(Element element) {
    List<Content> contents = new ArrayList<>();
    List<Content> siblings = getDirectContents(element.getParentElement());
    int indexOfElement = siblings.indexOf(element);
    for (int i = indexOfElement - 1; i >= 0; i--) {
      if (JDomContentHelper.isNewline(siblings.get(i))) {
        contents.add(siblings.get(i));
        i--;
      }
      if (i >= 0 && siblings.get(i) instanceof Comment) {
        contents.add(siblings.get(i));
        continue;
      }
      if (i >= 0 && JDomContentHelper.isMultiNewLine(siblings.get(i))) {
        contents.add(siblings.get(i));
      }
      break;
    }
    return contents;
  }

  /**
   * Get all direct children of the element.
   *
   * @param element the element to consider.
   * @return List of {@link Content}s
   */
  private static List<Content> getDirectContents(Element element) {
    // get all direct children
    List<Content> children = new ArrayList<>();
    Element parentElement = element.getParentElement();
    if (null == parentElement) {
      return children;
    }
    for (Content descendant : parentElement.getDescendants()) {
      if (!descendant.getParent().equals(element)) {
        // Only consider direct children
        continue;
      }
      children.add(descendant);
    }
    return children;
  }

}
