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

import org.codehaus.plexus.util.StringUtils;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;
import org.jdom2.util.IteratorIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROFILE;
import static org.apache.maven.model.jdom.util.JDomCfg.POM_ELEMENT_PROFILES;

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
        JDomUtils.removeChildAndItsCommentFromContent(profilesElement.getParentElement(), profilesElement);
      }
    }
  }

  /**
   * Remove empty element tags and their empty child tags.<br>
   * Empty tags may contain comments which will be removed as well.
   *
   * @param rootElement the root element.
   * @param tag         Tag to check.
   */
  public static void cleanupEmptyElements(Element rootElement, String tag) {
    IteratorIterable<Element> filteredElements = rootElement.getDescendants(new ElementFilter(tag));
    List<Element> elementsToRemoveIfEmpty = new ArrayList<>();
    for (Element element : filteredElements) {
      elementsToRemoveIfEmpty.add(element);
    }
    for (Element elementToRemove : elementsToRemoveIfEmpty) {
      List<Element> children = elementToRemove.getChildren();
      if (children.size() == 0) {
        JDomUtils.removeChildAndItsCommentFromContent(elementToRemove.getParentElement(), elementToRemove);
      }
    }
  }

  /**
   * Replace all multilines with more than two multilines with a double newline.<br>
   * Indentations are preserved.
   *
   * @param rootElement the root element.
   */
  public static void squashMultilines(Element rootElement) {
    Set<Content> contentsToRemove = new HashSet<>();
    for (Content descendant : rootElement.getDescendants()) {
      if (JDomContentHelper.isMultiNewLine(descendant) && StringUtils.countMatches(descendant.getValue(), "\n") > 2) {
        Text multiLineTest = (Text) descendant;
        String newText = multiLineTest.getText().replaceAll("\n", "");

        int count = 0;
        Element parent = descendant.getParentElement();
        int index = parent.indexOf(descendant);
        Content predecessor = JDomContentHelper.getPredecessorOfContentWithIndex(index, parent);
        while (JDomContentHelper.hasNewlines(predecessor)) {
          count++;
          contentsToRemove.add(predecessor);
          index--;
          predecessor = JDomContentHelper.getPredecessorOfContentWithIndex(index, parent);
        }
        if (count == 0) {
          newText = "\n\n" + newText;
        } else {
          newText = StringUtils.repeat("\n", count) + newText;
        }
        multiLineTest.setText(newText);
      }
    }
    for (Content content : contentsToRemove) {
      JDomUtils.simpleRemoveAtIndex(content);
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
      JDomUtils.removeChildAndItsCommentFromContent(parent, elementToBeRemoved);
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
