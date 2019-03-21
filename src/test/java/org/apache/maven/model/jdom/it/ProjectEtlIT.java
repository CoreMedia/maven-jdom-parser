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

import org.apache.maven.model.Parent;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests transformations of (root) {@code project} elements.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class ProjectEtlIT extends AbstractJDomModelEtlIT {

  @Test
  public void changeArtifactId() throws IOException {
    subjectModel.setArtifactId("my-renamed-test-project");
    assertTransformation();
  }

  @Test
  public void changeParentVersion() throws IOException {
    subjectModel.getParent().setVersion("20");
    assertTransformation();
  }

  @Test
  public void changeProjectVersionSimple() throws IOException {
    subjectModel.setVersion("1.2.3.4.5.6.7.8.9-SNAPSHOT");
    assertTransformation();
  }

  @Test
  public void changeProjectVersionParent() throws IOException {
    subjectModel.setVersion("9.8.7.6.5.4.3.2.1-SNAPSHOT");
    assertTransformation();
  }

  @Test
  public void removeParent() throws IOException {
    subjectModel.setParent(null);
    assertTransformation();
  }

  @Test
  public void setParent() throws IOException {
    Parent parent = new Parent();
    parent.setGroupId("org.apache");
    parent.setArtifactId("apache");
    parent.setVersion("21");
    subjectModel.setParent(parent);
    assertTransformation();
  }
}
