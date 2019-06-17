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

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for {@link Content}.
 *
 * @author https://github.com/eva-mueller-coremedia  (for <a href="https://github.com/CoreMedia/maven-jdom-parser">Maven JDom Parser</a>, version 3.0)
 */
class JDomContentHelper {

  static String contentAsString(Content content) {
    if (content instanceof Element) {
      return elementToString((Element) content);
    }
    if (content instanceof Text) {
      return textToString((Text) content);
    }
    return content.getCType() + " => " + content.getValue().trim();
  }

  private static String elementToString(Element element) {
    return element.getCType() + " => <" + element.getName() + "> : " + element.getValue().trim().replaceAll("\n", "\\\\n");
  }

  private static String textToString(Text text) {
    String value = text.getValue().replaceAll("\n", "\\\\n");
    if (isNewline(text)) {
      return "New Line: '" + value + "'";
    }
    if (isMultiNewLine(text)) {
      return "Multi New Line: '" + value + "'";
    }
    return text.getCType() + " => " + value;
  }

  /**
   * Check if content represent newlines.
   *
   * @param content the content to check
   * @return boolean
   */
  static boolean hasNewlines(Content content) {
    if (content instanceof Text) {
      Text text = (Text) content;
      return StringUtils.countMatches(text.getValue(), "\n") >= 1;
    }
    return false;
  }

  /**
   * Check if content is one newline, i.e. \n.<br>
   * Maybe followed by an empty string.
   *
   * @param content the content to check
   * @return boolean
   */
  static boolean isNewline(Content content) {
    if (content instanceof Text) {
      Text text = (Text) content;
      return StringUtils.countMatches(text.getValue(), "\n") == 1;
    }
    return false;
  }

  /**
   * Check if content is a multiline text, i.e. \n\n (or more).<br>
   * * Maybe followed by an empty string.
   *
   * @param content the content to check
   * @return boolean
   */
  static boolean isMultiNewLine(Content content) {
    if (content instanceof Text) {
      Text text = (Text) content;
      return StringUtils.countMatches(text.getValue(), "\n") > 1;
    }
    return false;
  }

  static boolean isComment(Content content) {
    return content instanceof Comment;
  }
}
