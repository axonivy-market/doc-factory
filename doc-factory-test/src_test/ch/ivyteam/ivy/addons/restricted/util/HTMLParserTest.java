package ch.ivyteam.ivy.addons.restricted.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.restricted.parser.HTMLParser;

public class HTMLParserTest {

  @Test
  public void null_argument_is_not_HTML() {
    assertThat(HTMLParser.isHTML(null)).isFalse();
  }

  @Test
  public void empty_string_is_not_HTML() {
    assertThat(HTMLParser.isHTML(" ")).isFalse();
  }

  @Test
  public void stringWithoutTag_is_not_HTML() {
    assertThat(HTMLParser.isHTML("Lupsem test in the dvbaid /n sdgh < //ggt < /> << >*& $ \\\\ ygbdfsdkj @me")).isFalse();
  }

  @Test
  public void stringEclosedInHTML_is_HTML() {
    assertThat(HTMLParser.isHTML("<b>Lupsem test</b>")).isTrue();
  }

  @Test
  public void stringWithSelfEnclosingTag_is_HTML() {
    assertThat(HTMLParser
	.isHTML("Lupsem test in the <br /> dvbaid /n sdgh < //ggt < /> << >*& $ \\\\ ygbdfsdkj @me")).isTrue();
  }

  @Test
  public void stringWithNoEnclosingTag_is_not_HTML() {
    assertThat(HTMLParser
	.isHTML("Lupsem test in the <br> dvbaid /n sdgh < //ggt < /> << >*& $ \\\\ ygbdfsdkj @me")).isFalse();
  }

  @Test
  public void stringWithHTMLList_is_HTML() {
    assertThat(HTMLParser.isHTML("Lupsem test "
	+ "<ul> "
	+ "<li>one"
	+ "<li>two"
	+ "<li>three"
	+ "<ul/>"
	+ "@me")).isTrue();
  }

  @Test
  public void stringWithWrongFormatedHTMLList_has_no_HTML_tags() {
    assertThat(HTMLParser.isHTML("Lupsem test "
	+ "<ul> "
	+ "<li>one"
	+ "<li>two"
	+ "<li>three"
	+ "<ul>"
	+ "@me")).isFalse();
  }
}
