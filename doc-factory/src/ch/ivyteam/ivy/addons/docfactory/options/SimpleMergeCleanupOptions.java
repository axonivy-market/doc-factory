package ch.ivyteam.ivy.addons.docfactory.options;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SimpleMergeCleanupOptions {

	private boolean removesBlankLines = true;
	private boolean removesEmptyParagraphs = true;

	public static SimpleMergeCleanupOptions getRecommendedMergeCleanupOptionsForSimpleMerging() {
		return new SimpleMergeCleanupOptions();
	}

	public SimpleMergeCleanupOptions removingBlankLines(boolean removesBlankFields) {
		this.removesBlankLines = removesBlankFields;
		return this;
	}

	@SuppressWarnings("hiding")
	public SimpleMergeCleanupOptions removingEmptyParagraphs(boolean removesEmptyParagraphs) {
		this.removesEmptyParagraphs = removesEmptyParagraphs;
		return this;
	}

	public boolean isRemovesBlankLines() {
		return removesBlankLines;
	}

	public boolean isRemovesEmptyParagraphs() {
		return removesEmptyParagraphs;
	}

	public boolean removesNone() {
		return !removesBlankLines && !removesEmptyParagraphs;
	}

	public boolean removesAll() {
		return removesBlankLines && removesEmptyParagraphs;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof SimpleMergeCleanupOptions)) {
			return false;
		}
		SimpleMergeCleanupOptions other = (SimpleMergeCleanupOptions) obj;
		return this.removesBlankLines == other.removesBlankLines && this.removesEmptyParagraphs == other.removesEmptyParagraphs;
	}

        @Override
        public int hashCode() {
          return new HashCodeBuilder()
                  .append(removesBlankLines)
                  .append(removesEmptyParagraphs)
                  .toHashCode();
        }

}
