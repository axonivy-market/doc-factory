package ch.ivyteam.ivy.addons.filemanager.document.filter;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;

/**
 * <b>NOT PUBLIC API</b><br>
 * May change in the future, use at own risk.
 * @author ec
 * 
 */
public class DocumentListFilterForUpload {
	
	public static List<List<String>> getRejectedAndAcceptedFilesForUpload(BasicConfigurationController config, 
			String serverPath, java.util.List<String> filesToUpload) throws Exception {
		
		serverPath = PathUtil.formatPathForDirectoryWithoutLastAndFirstSeparator(serverPath);
		BasicConfigurationController conf = config.cloneConfig();
		DocumentListFilter filter = config.getDocumentListFilter();
		conf.setDocumentListFilter(null);
		
		IDocumentOnServerPersistence docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(conf);
		java.util.List<DocumentOnServer> docs = docPersistence.getList(serverPath, false);
		
		List<String> acceptedFiles = new ArrayList<String>();
		List<String> rejectedFiles = new ArrayList<String>();
		
		for(String s: filesToUpload) {
			DocumentOnServer doc = new DocumentOnServer();
			java.io.File f = new java.io.File(s);
			doc.setFilename(f.getName());
			doc.setExtension(FileHandler.getFileExtension(f.getName()));
			doc.setPath(serverPath+"/"+f.getName());
			if(filter.accept(doc)) {
				acceptedFiles.add(s);
			} else {
				rejectedFiles.add(doc.getPath());
			}
		}
		List<String> tmpFiles = new ArrayList<String>();
		tmpFiles.addAll(acceptedFiles);
		for(DocumentOnServer doc: docs) {
			if(!filter.accept(doc)) {
				//hiden doc if found in the accepted list: we do not accept it
				for(String s: tmpFiles) {
					java.io.File f = new java.io.File(s);
					if(f.getName().equals(doc.getFilename())){
						acceptedFiles.remove(s);
						rejectedFiles.add(doc.getPath());
						break;
					}
				}
			}
		}
		List<List<String>> result = new ArrayList<List<String>>();
		result.add(rejectedFiles);
		result.add(acceptedFiles);
		return result;
	}


}
