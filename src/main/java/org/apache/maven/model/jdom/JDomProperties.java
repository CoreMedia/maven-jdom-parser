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

import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.apache.maven.model.jdom.util.JDomUtils.rewriteElement;

/**
 * JDom implementation of poms PROPERTIES element
 *
 * @author Robert Scholte
 * @since 3.0
 */
public class JDomProperties extends Properties {

  private final Element properties;

  public JDomProperties(Element properties) {
    this.properties = properties;
  }

  @Override
  public Set<Map.Entry<Object, Object>> entrySet() {
    JDomPropertiesSet entrySet = new JDomPropertiesSet();

    for (Element property : properties.getContent(new ElementFilter(properties.getNamespace()))) {
      entrySet.addProperty(new JDomProperty(property));
    }

    return entrySet;
  }

  @Override
  public synchronized Object put(Object key, Object value) {
    String previousValue = getChildElementTextTrim((String) key, properties);
    rewriteElement((String) key, (String) value, properties, properties.getNamespace());
    return previousValue;
  }

  @Override
  public synchronized Object remove(Object key) {
    String previousValue = getChildElementTextTrim((String) key, properties);
    rewriteElement((String) key, null, properties, properties.getNamespace());
    return previousValue;
  }

  @Override
  public synchronized void load(Reader reader)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public synchronized void load(InputStream inStream)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void save(OutputStream out, String comments) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void store(Writer writer, String comments)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void store(OutputStream out, String comments)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public synchronized void loadFromXML(InputStream in)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void storeToXML(OutputStream os, String comment)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void storeToXML(OutputStream os, String comment, String encoding)
          throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getProperty(String key) {
    Element property = properties.getChild(key, properties.getNamespace());

    if (property == null) {
      return null;
    } else {
      return property.getTextTrim();
    }
  }

  @Override
  public String getProperty(String key, String defaultValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Enumeration<?> propertyNames() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> stringPropertyNames() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void list(PrintStream out) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void list(PrintWriter out) {
    throw new UnsupportedOperationException();
  }

  private class JDomPropertiesSet extends HashSet<Map.Entry<Object, Object>> {
    private void addProperty(JDomProperty jDomProperty) {
      // The 'add' method can only be called internally.
      // Adding a property from the outside can currently only be supported using the JDomProperties.put() method.
      super.add(jDomProperty);
    }

    @Override
    public boolean add(Map.Entry<Object, Object> objectObjectEntry) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      throw new UnsupportedOperationException();
    }


    @Override
    public boolean addAll(Collection<? extends Map.Entry<Object, Object>> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }
  }

  private class JDomProperty implements Map.Entry<Object, Object> {
    private Element property;

    private JDomProperty(Element property) {
      this.property = property;
    }

    @Override
    public Object getKey() {
      return property.getName();
    }

    @Override
    public Object getValue() {
      return property.getTextTrim();
    }

    @Override
    public Object setValue(Object value) {
      String previousValue = property.getTextTrim();
      property.setText((String) value);
      return previousValue;
    }
  }
}
