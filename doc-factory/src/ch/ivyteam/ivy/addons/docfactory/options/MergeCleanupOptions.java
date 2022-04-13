package ch.ivyteam.ivy.addons.docfactory.options;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("hiding")
public class MergeCleanupOptions extends SimpleMergeCleanupOptions {

  private boolean removesUnusedRegions = true;
  private boolean removesUnusedFields = true;
  private boolean removesContainingFields = true;

  public static MergeCleanupOptions getRecommendedMergeCleanupOptionsForMergingWithRegions() {
    return new MergeCleanupOptions();
  }

  public MergeCleanupOptions removingContainingFields(boolean removesContainingFields) {
    this.removesContainingFields = removesContainingFields;
    return this;
  }

  public MergeCleanupOptions removingUnusedRegions(boolean removesUnusedRegions) {
    this.removesUnusedRegions = removesUnusedRegions;
    return this;
  }

  public MergeCleanupOptions removingUnusedFields(boolean removesUnusedFields) {
    this.removesUnusedFields = removesUnusedFields;
    return this;
  }

  @Override
  public MergeCleanupOptions removingBlankLines(boolean removesBlankFields) {
    super.removingBlankLines(removesBlankFields);
    return this;
  }

  @Override
  public MergeCleanupOptions removingEmptyParagraphs(boolean removesEmptyParagraphs) {
    super.removingEmptyParagraphs(removesEmptyParagraphs);
    return this;
  }

  public boolean isRemovesUnusedRegions() {
    return removesUnusedRegions;
  }

  public boolean isRemovesContainingFields() {
    return removesContainingFields;
  }

  public boolean isRemovesUnusedFields() {
    return removesUnusedFields;
  }

  public boolean isCleanUpAll() {
    return isRemovesEmptyParagraphs() && isRemovesContainingFields() && isRemovesUnusedFields()
            && isRemovesUnusedRegions();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
            .append(isRemovesBlankLines())
            .append(isRemovesEmptyParagraphs())
            .append(isRemovesUnusedFields())
            .append(isRemovesUnusedRegions())
            .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof MergeCleanupOptions)) {
      return false;
    }
    MergeCleanupOptions other = (MergeCleanupOptions) obj;
    return this.isRemovesBlankLines() == other.isRemovesBlankLines() &&
            this.isRemovesContainingFields() == other.isRemovesContainingFields() &&
            this.isRemovesEmptyParagraphs() == other.isRemovesEmptyParagraphs() &&
            this.isRemovesUnusedFields() == other.isRemovesUnusedFields() &&
            this.isRemovesUnusedRegions() == other.isRemovesUnusedRegions();
  }

}
