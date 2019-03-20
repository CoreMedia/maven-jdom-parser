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

/**
 * Tests transformations of {@code properties}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class PropertiesEtlIT extends AbstractJDomModelEtlIT {

  @Test
  public void addProperty() throws IOException {
    jDomModelETL.getModel().addProperty("property.new", "value-new");
    assertTransformation();
  }

  @Test
  public void changePropertyValue() throws IOException {
    for (Map.Entry<Object, Object> property : jDomModelETL.getModel().getProperties().entrySet()) {
      if (property.getValue().equals("1.0-SNAPSHOT")) {
        property.setValue("${project.version}");
      }
    }
    assertTransformation();
  }

  @Test
  public void removeProperty() throws IOException {
    jDomModelETL.getModel().getProperties().remove("property.b");
    assertTransformation();
  }
}
