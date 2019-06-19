package org.apache.maven.model.jdom.util;

import org.jdom2.Text;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JDomContentHelperTest {

  @Test
  public void hasNewlines() {
    assertTrue(JDomContentHelper.hasNewlines(new Text("\n    ")));
    assertTrue(JDomContentHelper.hasNewlines(new Text("\n\n\n    ")));
    assertTrue(JDomContentHelper.hasNewlines(new Text("\n     \n\n    ")));
    assertFalse(JDomContentHelper.hasNewlines(new Text("\ntext    ")));
    assertFalse(JDomContentHelper.hasNewlines(new Text("\ntext\n    ")));
  }

  @Test
  public void isNewline() {
    assertTrue(JDomContentHelper.isNewline(new Text("\n    ")));
    assertFalse(JDomContentHelper.isNewline(new Text("\n\n\n    ")));
    assertFalse(JDomContentHelper.isNewline(new Text("\ntext    ")));
    assertFalse(JDomContentHelper.isNewline(new Text("\ntext\n\n\n    ")));
  }

  @Test
  public void isMultiNewLine() {
    assertFalse(JDomContentHelper.isMultiNewLine(new Text("\n    ")));
    assertTrue(JDomContentHelper.isMultiNewLine(new Text("\n\n\n")));
    assertTrue(JDomContentHelper.isMultiNewLine(new Text("\n\n\n      ")));
    assertFalse(JDomContentHelper.isMultiNewLine(new Text("\ntext\n\n    ")));
  }
}
