package ch.ivyteam.ivy.addons.docfactory.aspose.options;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.aspose.words.MailMergeCleanupOptions;

import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class AsposeMergeCleanupOptionsTest {

  MergeCleanupOptions cleanUpOptions;

  @Test
  public void getCleanupOptionIntValues_with_reconmmended_cleanUpOptions_returns_all_MailMergeCleanupOptions_addition() {
    cleanUpOptions = MergeCleanupOptions.getRecommendedMergeCleanupOptionsForMergingWithRegions();

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS +
            MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_all_options_to_false_returns_MailMergeCleanupOptions_none_value() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(false)
            .removingUnusedRegions(false).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.NONE;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_returns_MailMergeCleanupOptions_REMOVE_CONTAINING_FIELDS_value() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(false)
            .removingUnusedRegions(false).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingUnusedFields_returns_MailMergeCleanupOptions_REMOVE_UNUSED_FIELDS_value() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(true)
            .removingUnusedRegions(false).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingUnusedRegions_returns_MailMergeCleanupOptions_REMOVE_UNUSED_REGIONS_value() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(false)
            .removingUnusedRegions(true).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingEmptyParagraphs_returns_MailMergeCleanupOptions_REMOVE_EMPTY_PARAGRAPHS_value() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(false)
            .removingUnusedRegions(false).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_and_removingUnusedFields() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(true)
            .removingUnusedRegions(false).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_and_removingUnusedFields_and_removingUnusedRegions() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(true)
            .removingUnusedRegions(true).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_and_removingUnusedFields_and_removingEmptyParagraphs() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(true)
            .removingUnusedRegions(false).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_and_removingUnusedRegions_and_removingEmptyParagraphs() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(false)
            .removingUnusedRegions(true).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_and_removingUnusedRegions() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(false)
            .removingUnusedRegions(true).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingContainingFields_and_removingEmptyParagraphs() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(true).removingUnusedFields(false)
            .removingUnusedRegions(false).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingUnusedFields_and_removingUnusedRegions() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(true)
            .removingUnusedRegions(true).removingEmptyParagraphs(false);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingUnusedFields_and_removingUnusedRegions_and_removingEmptyParagraphs() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(true)
            .removingUnusedRegions(true).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS +
            MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingUnusedFields_and_removingEmptyParagraphs() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(true)
            .removingUnusedRegions(false).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

  @Test
  public void getCleanupOptionIntValues_for_removingUnusedRegions_and_removingEmptyParagraphs() {
    cleanUpOptions = new MergeCleanupOptions().removingContainingFields(false).removingUnusedFields(false)
            .removingUnusedRegions(true).removingEmptyParagraphs(true);

    int cleanupAsIntResult = AsposeMergeCleanupOptions.getCleanupOptionIntValues(cleanUpOptions);

    int expectedResult = MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS +
            MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS;

    assertThat(cleanupAsIntResult).isEqualTo(expectedResult);
  }

}
