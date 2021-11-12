package ch.ivyteam.ivy.addons.docfactory.aspose.options;

import com.aspose.words.MailMergeCleanupOptions;

import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;

public class AsposeMergeCleanupOptions {
	
	public static int getCleanupOptionIntValues(MergeCleanupOptions mergeCleanupOptions) {
		return (mergeCleanupOptions.isRemovesContainingFields()? MailMergeCleanupOptions.REMOVE_CONTAINING_FIELDS : MailMergeCleanupOptions.NONE) |
				(mergeCleanupOptions.isRemovesEmptyParagraphs()? MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS : MailMergeCleanupOptions.NONE) |
				(mergeCleanupOptions.isRemovesUnusedFields()? MailMergeCleanupOptions.REMOVE_UNUSED_FIELDS : MailMergeCleanupOptions.NONE) |
				(mergeCleanupOptions.isRemovesUnusedRegions()? MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS : MailMergeCleanupOptions.NONE);
	}

}
