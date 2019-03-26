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

import org.apache.maven.model.Profile;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests transformations of {@code modules}.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public class ModulesEtlIT extends AbstractJDomModelEtlIT {

  @Test
  public void addFirstModule() throws IOException {
    subjectModel.addModule("first-module");
    assertTransformation();
  }

  @Test
  public void addModule() throws IOException {
    subjectModel.addModule("new-module");
    assertTransformation();
  }

  @Test
  public void removeLastModule() throws IOException {
    subjectModel.removeModule("last-module");
    assertTransformation();
  }

  @Test
  public void resetModules() throws IOException {
    subjectModel.setModules(Arrays.asList("new-module-1", "new-module-2"));
    assertTransformation();
  }

  @Test
  public void resetProfileModules() throws IOException {
    List<Profile> profiles = subjectModel.getProfiles();
    for (Profile profile : profiles) {
      String s = profile.getId();
      if ("profile-1".equals(s)) {
        profile.setModules(Arrays.asList("new-module-1", "new-module-2", "new-module-3"));
      } else if ("profile-2".equals(s)) {
        profile.setModules(Collections.singletonList("new-module-4"));
      }
    }
    assertTransformation();
  }
}
