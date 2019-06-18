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

import org.apache.maven.model.Model;
import org.apache.maven.model.jdom.etl.JDomModelETL;
import org.apache.maven.model.jdom.etl.JDomModelETLFactory;
import org.apache.maven.model.jdom.etl.ModelETLRequest;
import org.codehaus.plexus.util.FileUtils;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.apache.maven.model.jdom.etl.ModelETLRequest.UNIX_LS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This abstract class offers the base implementation that allows to add tests consisting of the transformation code and
 * some test resources:
 * <ul>
 * <li><u>*_input-pom.xml</u> file containing the input XML file (mandatory</li>
 * <li><u>*_expected-pom.xml</u> file containing the expected XML after transformation (mandatory)</li>
 * <li><u>*_source-pom.xml</u> file containing model definitions that can be copied by the test (optional)</li>
 * </ul>
 * The file names must follow the pattern {@code [TEST_CLASS]_[TEST_METHOD]_[input|expected]-pom.xml}. The
 * transformation output is written to a file {@code [TEST_CLASS]_[TEST_METHOD]_output-pom.xml}. It is placed into a
 * folder that can be configured using the system property the {@code test.output.directory}. If that property is not
 * set the output will be written to a temporary file that is deleted after the test.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public abstract class AbstractJDomModelEtlIT {

  @Rule
  public TestName testName = new TestName();

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  protected Model subjectModel;

  private Model sourceModel;

  private JDomModelETL subjectModelETL;

  private File expectedPomFile;
  private File outputPomFile;

  @Before
  public void setUp() throws IOException, JDOMException, URISyntaxException {
    String testResourceNamePrefix = getTestResourceNamePrefix();
    expectedPomFile = getTestResource(testResourceNamePrefix + "_expected-pom.xml");
    outputPomFile = getOutputFile(testResourceNamePrefix + "_output-pom.xml");

    ModelETLRequest modelETLRequest = new ModelETLRequest();
    modelETLRequest.setLineSeparator(UNIX_LS);

    File inputPomFile = getTestResource(testResourceNamePrefix + "_input-pom.xml");
    subjectModelETL = new JDomModelETLFactory().newInstance(modelETLRequest);
    subjectModelETL.extract(inputPomFile);
    subjectModel = subjectModelETL.getModel();

    try {
      File sourcePomFile = getTestResource(testResourceNamePrefix + "_source-pom.xml");
      JDomModelETL sourceModelETL = new JDomModelETLFactory().newInstance(modelETLRequest);
      sourceModelETL.extract(sourcePomFile);
      sourceModel = sourceModelETL.getModel();
    } catch (FileNotFoundException ignored) {
      // Can be ignored, because the sourceModel must only exist when the test actually uses it.
    }
  }

  @SuppressWarnings("WeakerAccess")
  protected void assertTransformation() throws IOException {
    subjectModelETL.load(outputPomFile);

    String actualXml = FileUtils.fileRead(outputPomFile);
    String expectedXml = FileUtils.fileRead(expectedPomFile);
    String message = "Unexpected contents in output file " + outputPomFile + System.getProperty("line.separator");
    assertEquals(message, expectedXml, actualXml);
  }

  @SuppressWarnings("WeakerAccess")
  protected void assertTransformationWithCleanup() throws IOException {
    subjectModelETL.doGlobalCleanup();
    subjectModelETL.load(outputPomFile);

    String actualXml = FileUtils.fileRead(outputPomFile);
    String expectedXml = FileUtils.fileRead(expectedPomFile);
    String message = "Unexpected contents in output file " + outputPomFile + System.getProperty("line.separator");
    assertEquals(message, expectedXml, actualXml);
  }

  @SuppressWarnings("WeakerAccess")
  protected Model getSourceModel() {
    if (sourceModel == null) {
      fail("Test resource not found: " + getTestResourceNamePrefix() + "_source-pom.xml");
    }
    return sourceModel;
  }

  private File getOutputFile(String filename) throws IOException {
    String outputDirectory = System.getProperty("test.output.directory");
    if (outputDirectory == null) {
      // Write the output to a tmp file - applies when tests are executed in the IDE.
      return folder.newFile(filename);
    } else {
      // Write the output to a file in the output dir - applies when tests are executed by the Maven build.
      File outputDir = new File(outputDirectory);
      outputDir.mkdirs();
      return new File(outputDirectory, this.getClass().getPackage().getName() + "." + filename);
    }
  }

  private File getTestResource(String filename) throws FileNotFoundException, URISyntaxException {
    URL resource = this.getClass().getResource(filename);
    if (resource == null) {
      throw new FileNotFoundException("Test resource not found: " + filename);
    } else {
      return new File(resource.toURI());
    }
  }

  private String getTestResourceNamePrefix() {
    return this.getClass().getSimpleName() + "_" + testName.getMethodName();
  }
}
