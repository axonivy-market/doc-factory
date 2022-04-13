package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.options.SimpleMergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;

public class DocumentTemplateWithCleanupOptionsTest extends DocFactoryTest {

  @Test
  public void documentTemplate_has_default_cleanupOptions_byDefault() throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());
    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template);

    SimpleMergeCleanupOptions simpleMergeCleanupOptions = documentTemplate.getDocumentFactory()
            .getSimpleMergeCleanupOptions();

    assertThat(simpleMergeCleanupOptions,
            equalTo(SimpleMergeCleanupOptions.getRecommendedMergeCleanupOptionsForSimpleMerging()));
    assertThat(simpleMergeCleanupOptions.isRemovesBlankLines(), is(true));
    assertThat(simpleMergeCleanupOptions.isRemovesEmptyParagraphs(), is(true));

    MergeCleanupOptions mergeCleanupOptions = documentTemplate.getDocumentFactory()
            .getRegionsMergeCleanupOptions();

    assertThat(mergeCleanupOptions,
            equalTo(MergeCleanupOptions.getRecommendedMergeCleanupOptionsForMergingWithRegions()));
    assertThat(mergeCleanupOptions.isRemovesBlankLines(), is(true));
    assertThat(mergeCleanupOptions.isRemovesContainingFields(), is(true));
    assertThat(mergeCleanupOptions.isRemovesEmptyParagraphs(), is(true));
    assertThat(mergeCleanupOptions.isRemovesUnusedFields(), is(true));
    assertThat(mergeCleanupOptions.isRemovesUnusedRegions(), is(true));
  }

  @Test
  public void withSimpleMergeCleanupOptions_changes_the_cleanupOptions_for_simple_merging()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    SimpleMergeCleanupOptions changedSimpleMergeCleanupOptions = new SimpleMergeCleanupOptions()
            .removingBlankLines(false).removingEmptyParagraphs(false);

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .withSimpleMergeCleanupOptions(changedSimpleMergeCleanupOptions);

    SimpleMergeCleanupOptions simpleMergeCleanupOptions = documentTemplate.getDocumentFactory()
            .getSimpleMergeCleanupOptions();

    assertThat(simpleMergeCleanupOptions,
            not(SimpleMergeCleanupOptions.getRecommendedMergeCleanupOptionsForSimpleMerging()));
    assertThat(simpleMergeCleanupOptions.isRemovesBlankLines(), is(false));
    assertThat(simpleMergeCleanupOptions.isRemovesEmptyParagraphs(), is(false));
  }

  @Test
  public void withRegionsMergeCleanupOptions_changes_the_cleanupOptions_for_simple_merging()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    MergeCleanupOptions changedMergeCleanupOptions = new MergeCleanupOptions().removingBlankLines(false)
            .removingEmptyParagraphs(false).removingUnusedRegions(false).removingContainingFields(false)
            .removingUnusedFields(false);

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .withRegionsMergeCleanupOptions(changedMergeCleanupOptions);

    MergeCleanupOptions mergeCleanupOptions = documentTemplate.getDocumentFactory()
            .getRegionsMergeCleanupOptions();

    assertThat(mergeCleanupOptions,
            not(MergeCleanupOptions.getRecommendedMergeCleanupOptionsForMergingWithRegions()));
    assertThat(mergeCleanupOptions.isRemovesBlankLines(), is(false));
    assertThat(mergeCleanupOptions.isRemovesContainingFields(), is(false));
    assertThat(mergeCleanupOptions.isRemovesEmptyParagraphs(), is(false));
    assertThat(mergeCleanupOptions.isRemovesUnusedFields(), is(false));
    assertThat(mergeCleanupOptions.isRemovesUnusedRegions(), is(false));
  }

  @Test
  public void produceDocument_by_default_removes_lines_with_only_empty_string_values()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    Person p = makePerson();
    // the name and firstname are in the same line, if they are both empty the
    // line should be removed
    p.setFirstname("");
    p.setName("");

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(p).useLocale(Locale.forLanguageTag("de-CH"));

    File resultFile = makeFile("test/cleanup/remove_lines_with_only_empty_strings.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_by_default_keeps_lines_with_at_least_one_not_empty_string_values()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    Person p = makePerson();
    // the name and firstname are in the same line, if they are both empty the
    // line should be removed
    p.setFirstname("");
    p.setName("Tuchella");

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(p).useLocale(Locale.forLanguageTag("de-CH"));

    File resultFile = makeFile("test/cleanup/keep_lines_with_at_least_one_not_empty_string.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_by_default_removes_lines_with_only_null_values() throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    Person p = makePerson();
    // the name and firstname are in the same line, if they are both null the
    // line should be removed
    p.setFirstname(null);
    p.setName(null);

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(p).useLocale(Locale.forLanguageTag("de-CH"));

    File resultFile = makeFile("test/cleanup/remove_lines_with_only_null_values.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_by_default_removes_blank_lines_resulting_from_mailMerge()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    Person p = makePerson();
    // the name and firstname are in the same line, if they are both null the
    // line should be removed
    p.setFirstname("");
    p.setName(null);

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(p).useLocale(Locale.forLanguageTag("de-CH"));

    File resultFile = makeFile("test/cleanup/remove_blank_lines_resulting_from_mailMerge.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_withSimpleMergeCleanupOptions_notRemovingBlankLines_keeps_blank_lines_resulting_from_mailMerge()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    Person p = makePerson();
    p.setFirstname(null);
    p.setName("");

    SimpleMergeCleanupOptions changedSimpleMergeCleanupOptions = new SimpleMergeCleanupOptions()
            .removingBlankLines(false);

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(p).useLocale(Locale.forLanguageTag("de-CH"))
            .withSimpleMergeCleanupOptions(changedSimpleMergeCleanupOptions);

    File resultFile = makeFile("test/cleanup/keep_blank_lines_resulting_from_mailMerge.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_withSimpleMergeCleanupOptions_notRemovingBlankLines_keeps_lines_with_null_values()
          throws URISyntaxException {
    File template = new File(
            this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    Person p = makePerson();
    p.setFirstname(null);
    p.setName(null);

    SimpleMergeCleanupOptions changedSimpleMergeCleanupOptions = new SimpleMergeCleanupOptions()
            .removingBlankLines(false);

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(p).useLocale(Locale.forLanguageTag("de-CH"))
            .withSimpleMergeCleanupOptions(changedSimpleMergeCleanupOptions);

    File resultFile = makeFile("test/cleanup/keep_null_lines.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

}
