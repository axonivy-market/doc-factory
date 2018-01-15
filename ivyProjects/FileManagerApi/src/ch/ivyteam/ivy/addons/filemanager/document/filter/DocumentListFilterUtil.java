package ch.ivyteam.ivy.addons.filemanager.document.filter;

import java.util.ArrayList;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

/**
 * Utility Class for DocumentOnServer objects List filtering.
 * @author ec
 *
 */
public class DocumentListFilterUtil {
	
	private DocumentListFilterUtil() {}
	
	/**
	 * Filters the given List of DocumentOnServer objects, if the given DocumentListFilter is not null.
	 * @param docs 
	 * @param filter
	 * @return
	 */
	public static java.util.List<DocumentOnServer> filterDocumentListIfFilterNotNull(java.util.List<DocumentOnServer> docs, 
			DocumentListFilter filter) {
		if(filter==null) {
			return docs;
		}
		java.util.List<DocumentOnServer> documents = new ArrayList<>();
		for(DocumentOnServer doc: docs) {
			if(filter.accept(doc)) {
				documents.add(doc);
			}
		}
		
		return documents;
	}

}
