package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makeFile;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makePerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.options.SimpleMergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class DocumentTemplateWithCleanupOptionsTest {

  @Test
  public void documentTemplate_has_default_cleanupOptions_byDefault() throws URISyntaxException {
    var template = new File(this.getClass().getResource(DocFactoryTest.TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());
    var documentTemplate = DocumentTemplate.withTemplate(template);
    var simpleMergeCleanupOptions = documentTemplate.getDocumentFactory().getSimpleMergeCleanupOptions();
    assertThat(simpleMergeCleanupOptions).isEqualTo(SimpleMergeCleanupOptions.getRecommendedMergeCleanupOptionsForSimpleMerging());
    assertThat(simpleMergeCleanupOptions.isRemovesBlankLines()).isTrue();
    assertThat(simpleMergeCleanupOptions.isRemovesEmptyParagraphs()).isTrue();

    var mergeCleanupOptions = documentTemplate.getDocumentFactory().getRegionsMergeCleanupOptions();
    assertThat(mergeCleanupOptions).isEqualTo(MergeCleanupOptions.getRecommendedMergeCleanupOptionsForMergingWithRegions());
    assertThat(mergeCleanupOptions.isRemovesBlankLines()).isTrue();
    assertThat(mergeCleanupOptions.isRemovesContainingFields()).isTrue();
    assertThat(mergeCleanupOptions.isRemovesEmptyParagraphs()).isTrue();
    assertThat(mergeCleanupOptions.isRemovesUnusedFields()).isTrue();
    assertThat(mergeCleanupOptions.isRemovesUnusedRegions()).isTrue();
  }

  @Test
  public void withSimpleMergeCleanupOptions_changes_the_cleanupOptions_for_simple_merging() throws URISyntaxException {
    var template = new File(this.getClass().getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());

    var changedSimpleMergeCleanupOptions = new SimpleMergeCleanupOptions().removingBlankLines(false).removingEmptyParagraphs(false);
    var documentTemplate = DocumentTemplate.withTemplate(template).withSimpleMergeCleanupOptions(changedSimpleMergeCleanupOptions);
    var simpleMergeCleanupOptions = documentTemplate.getDocumentFactory().getSimpleMergeCleanupOptions();
    assertThat(simpleMergeCleanupOptions).isNotEqualTo(SimpleMergeCleanupOptions.getRecommendedMergeCleanupOptionsForSimpleMerging());
    assertThat(simpleMergeCleanupOptions.isRemovesBlankLines()).isFalse();
    assertThat(simpleMergeCleanupOptions.isRemovesEmptyParagraphs()).isFalse();
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
    
    assertThat(mergeCleanupOptions).isNotEqualTo(MergeCleanupOptions.getRecommendedMergeCleanupOptionsForMergingWithRegions());
    assertThat(mergeCleanupOptions.isRemovesBlankLines()).isFalse();
    assertThat(mergeCleanupOptions.isRemovesContainingFields()).isFalse();
    assertThat(mergeCleanupOptions.isRemovesEmptyParagraphs()).isFalse();
    assertThat(mergeCleanupOptions.isRemovesUnusedFields()).isFalse();
    assertThat(mergeCleanupOptions.isRemovesUnusedRegions()).isFalse();
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

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
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

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
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

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
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

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
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

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
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

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
  }
}
