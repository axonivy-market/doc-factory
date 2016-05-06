/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.observer;

import java.io.File;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * An Observable file action is used in observable objects from the FileManager
 * to be able to communicate the file actions with the observer Objects through
 * the notifyObservers(arg) method, where arg will be such an
 * ObservableFileAction object.
 * 
 * @author ec
 */
public class ObservableFileAction {

	FileActionEnum action;
	List<DocumentOnServer> documents;

	public ObservableFileAction(FileActionEnum action, java.io.File file) {
		this.action = action;
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		if (file != null) {
			DocumentOnServer doc = new DocumentOnServer();
			doc.setFilename(file.getName());
			doc.setPath(file.getPath());
			doc.setFileSize(FileHandler.getFileSize(file));
			doc.setExtension(FileHandler.getFileExtension(file.getName()));
			l.add(doc);
		}
		this.setDocuments(l);
	}

	public ObservableFileAction(FileActionEnum action, String filepath) {
		this.action = action;
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		if (filepath != null && filepath.trim().length() > 0) {
			java.io.File file = new java.io.File(filepath);
			DocumentOnServer doc = new DocumentOnServer();
			doc.setFilename(file.getName());
			doc.setPath(file.getPath());
			doc.setFileSize(FileHandler.getFileSize(file));
			doc.setExtension(FileHandler.getFileExtension(file.getName()));
			l.add(doc);
		}
		this.setDocuments(l);
	}

	public ObservableFileAction(FileActionEnum action, String[] filepaths) {
		this.action = action;
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		if (filepaths != null && filepaths.length > 0) {
			for (int i = 0; i < filepaths.length; i++) {
				String filepath = filepaths[i];
				if (filepath != null && filepath.trim().length() > 0) {
					java.io.File file = new java.io.File(filepath);
					DocumentOnServer doc = new DocumentOnServer();
					doc.setFilename(file.getName());
					doc.setPath(file.getPath());
					doc.setFileSize(FileHandler.getFileSize(file));
					doc.setExtension(FileHandler.getFileExtension(file
							.getName()));
					l.add(doc);
				}
			}

		}
		this.setDocuments(l);
	}

	public ObservableFileAction(FileActionEnum action, DocumentOnServer document) {
		this.action = action;
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		if (document != null) {
			l.add(document);
		}
		this.setDocuments(l);
	}

	/**
	 * constructor
	 * @param action the FileActionEnum denoting the file action that is observed
	 * @param documents an Ivy List (ch.ivyteam.ivy.scripting.objects.List) of DocumentOnServer or java.io.File.
	 * If this List contains other types of Objects, they will be ignored.
	 */
	public ObservableFileAction(FileActionEnum action,
			List<?> documents) {
		this.action = action;
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		if (documents != null && !documents.isEmpty()) {
			for(Object o : documents){
				if(o instanceof DocumentOnServer){
					l.add((DocumentOnServer) o);
				}else if(o instanceof File){
					java.io.File file = (File) o;
					DocumentOnServer doc = new DocumentOnServer();
					doc.setFilename(file.getName());
					doc.setPath(file.getPath());
					doc.setFileSize(FileHandler.getFileSize(file));
					doc.setExtension(FileHandler.getFileExtension(file
							.getName()));
					l.add(doc);
				}
			}
		} 
		this.setDocuments(l);
	}

	public ObservableFileAction(FileActionEnum action, java.util.List<?> documents) {
		this.action = action;
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		if (documents != null && !documents.isEmpty()) {
			for(Object o : documents){
				if(o instanceof DocumentOnServer){
					l.add((DocumentOnServer) o);
				}else if(o instanceof File){
					java.io.File file = (File) o;
					DocumentOnServer doc = new DocumentOnServer();
					doc.setFilename(file.getName());
					doc.setPath(file.getPath());
					doc.setFileSize(FileHandler.getFileSize(file));
					doc.setExtension(FileHandler.getFileExtension(file
							.getName()));
					l.add(doc);
				}
			}
		} 
		this.setDocuments(l);
	}

	public FileActionEnum getAction() {
		return action;
	}

	public void setAction(FileActionEnum action) {
		this.action = action;
	}

	public List<DocumentOnServer> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentOnServer> documents) {
		this.documents = documents;
	}

}
