package org.apache.maven.model.jdom.etl;

import org.apache.maven.model.jdom.it.AbstractJDomModelEtlIT;
import org.junit.Test;

import java.io.IOException;

public class JDomModelETLTest extends AbstractJDomModelEtlIT {

  @Test
  public void removeElementWithEmptyChildren() throws IOException {
    assertTransformationWithCleanup();
  }

  @Test
  public void removeElementWithOnlyEmptyChildren() throws IOException {
    assertTransformationWithCleanup();
  }

}
