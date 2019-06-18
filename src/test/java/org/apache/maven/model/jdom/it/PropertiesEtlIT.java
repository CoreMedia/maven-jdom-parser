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

import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Tests transformations of {@code properties}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PropertiesEtlIT extends AbstractJDomModelEtlIT {

  @Test
  public void addProperty() throws IOException {
    subjectModel.addProperty("property.new", "value-new");
    assertTransformation();
  }

  @Test
  public void addPropertyWhenEmpty() throws IOException {
    Properties properties = new Properties();
    properties.setProperty("property.new", "value-new");
    subjectModel.setProperties(properties);
    assertTransformation();
  }

  @Test
  public void changePropertyValue() throws IOException {
    for (Map.Entry<Object, Object> property : subjectModel.getProperties().entrySet()) {
      if (property.getValue().equals("1.0-SNAPSHOT")) {
        property.setValue("${project.version}");
      }
    }
    assertTransformation();
  }

  @Test
  public void removeProperty() throws IOException {
    subjectModel.getProperties().remove("property.a");
    subjectModel.getProperties().remove("property.c");
    assertTransformation();
  }
}
