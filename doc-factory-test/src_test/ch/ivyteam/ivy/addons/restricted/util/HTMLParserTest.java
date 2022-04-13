package ch.ivyteam.ivy.addons.restricted.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.restricted.parser.HTMLParser;

public class HTMLParserTest {

  @Test
  public void null_argument_is_not_HTML() {
    assertFalse(HTMLParser.isHTML(null));
  }

  @Test
  public void empty_string_is_not_HTML() {
    assertFalse(HTMLParser.isHTML(" "));
  }

  @Test
  public void stringWithoutTag_is_not_HTML() {
    assertFalse(
            HTMLParser.isHTML("Lupsem test in the dvbaid /n sdgh < //ggt < /> << >*& $ \\\\ ygbdfsdkj @me"));
  }

  @Test
  public void stringEclosedInHTML_is_HTML() {
    assertTrue(HTMLParser.isHTML("<b>Lupsem test</b>"));
  }

  @Test
  public void stringWithSelfEnclosingTag_is_HTML() {
    assertTrue(HTMLParser
            .isHTML("Lupsem test in the <br /> dvbaid /n sdgh < //ggt < /> << >*& $ \\\\ ygbdfsdkj @me"));
  }

  @Test
  public void stringWithNoEnclosingTag_is_not_HTML() {
    assertFalse(HTMLParser
            .isHTML("Lupsem test in the <br> dvbaid /n sdgh < //ggt < /> << >*& $ \\\\ ygbdfsdkj @me"));
  }

  @Test
  public void stringWithHTMLList_is_HTML() {
    assertTrue(HTMLParser.isHTML("Lupsem test "
            + "<ul> "
            + "<li>one"
            + "<li>two"
            + "<li>three"
            + "<ul/>"
            + "@me"));
  }

  @Test
  public void stringWithWrongFormatedHTMLList_has_no_HTML_tags() {
    assertFalse(HTMLParser.isHTML("Lupsem test "
            + "<ul> "
            + "<li>one"
            + "<li>two"
            + "<li>three"
            + "<ul>"
            + "@me"));
  }

}
