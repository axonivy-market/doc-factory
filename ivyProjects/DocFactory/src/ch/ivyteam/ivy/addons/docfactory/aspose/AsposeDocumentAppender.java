package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.util.List;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentAppendingStart;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;

import com.aspose.words.Document;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Node;
import com.aspose.words.Section;
import com.aspose.words.SectionStart;

public class AsposeDocumentAppender {
	
	private static final int SECOND_DOCUMENT_INDEX = 1;

	/**
	 * Appends the given list of Documents together. 
	 * The first Document is the first part of the produced document, the second one the second part and do on ...
	 * @param documents the Document Objects that should be appended together. Cannot be null or empty.
	 * @param FileAppenderOptions Options for fine tuning the way the documents will be appended. See {@link FileAppenderOptions#getDocumentAppendingStart()},
	 * {@link FileAppenderOptions#isUseHeadersFootersFromLeadingPage()} and {@link FileAppenderOptions#isRestartPageNumbering()}.
	 * Cannot be null.
	 * @return The document containing all the Documents appended together
	 * @throws Exception
	 */
	public static Document appendDocuments(List<Document> documents, FileAppenderOptions fileAppenderOptions) throws Exception {
		API.checkNotEmpty(documents, "List<Document> documents");
		API.checkNotNull(fileAppenderOptions, "FileAppenderOptions fileAppenderOptions");
		
		// deep clone the leading document because of Issue https://jira.axonivy.com/jira/browse/AIPROD-189
		Document leadingDocument = (Document) documents.get(0).deepClone(true);
		int sectionStart = DocumentAppendingStart.CONTINUOUS.equals(fileAppenderOptions.getDocumentAppendingStart()) ? SectionStart.CONTINUOUS : SectionStart.NEW_PAGE;
		for(int i = SECOND_DOCUMENT_INDEX; i < documents.size(); i++) {
			Document doc = documents.get(i);
			doc.getFirstSection().getPageSetup().setSectionStart(sectionStart);
			doc.getFirstSection().getPageSetup().setPageWidth(leadingDocument.getLastSection().getPageSetup().getPageWidth());
			doc.getFirstSection().getPageSetup().setPageHeight(leadingDocument.getLastSection().getPageSetup().getPageHeight());
			doc.getFirstSection().getPageSetup().setOrientation(leadingDocument.getLastSection().getPageSetup().getOrientation());
			doc.getFirstSection().getHeadersFooters().linkToPrevious(fileAppenderOptions.isUseHeadersFootersFromLeadingPage());
			doc.getFirstSection().getPageSetup().setRestartPageNumbering(fileAppenderOptions.isRestartPageNumbering());
			appendDoc(leadingDocument, doc);
		}
		return leadingDocument;
	}
	
	/**
	 * A useful function that you can use to easily append one document to another.
	 * @param dstDoc : The destination document where to append to. This will be the leading document. Cannot be null.
	 * @param srcDoc : The source document. This document will be appended to the dstDoc. Cannot be null.
	 */
	public static void appendDoc(Document dstDoc, Document srcDoc) throws Exception {
		API.checkNotNull(dstDoc, "Document dstDoc");
		API.checkNotNull(srcDoc, "Document srcDoc");
		// Loop through all sections in the source document.
		// Section nodes are immediate children of the Document node so we can just enumerate the Document.
		int x = srcDoc.getSections().getCount();
		for (int i = 0; i < x; i++) {
			// Because we are copying a section from one document to another,
			// it is required to import the Section node into the destination document.
			// This adjusts any document-specific references to styles, lists, etc.
			//
			// Importing a node creates a copy of the original node, but the copy
			// is ready to be inserted into the destination document.
			Section srcSection = srcDoc.getSections().get(i);
			Node dstSection = dstDoc.importNode(srcSection, true, ImportFormatMode.KEEP_SOURCE_FORMATTING);

			// Now the new section node can be appended to the destination document.
			dstDoc.appendChild(dstSection);
		}
	}

}
