package ch.ivyteam.ivy.addons.filemanager.restricted.database.sql;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;

/**
 * NOT PUBLIC API
 * @author ec
 *
 */
public class FolderOnServerTranslationHelper {
	
	public static String getTranslatedPathForFolderOnServer(FolderOnServer fos, BasicConfigurationController config) throws Exception {
		IFolderOnServerPersistence dirPers = PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(config);
		String translatedPath = fos.getDisplayName();
		if(!fos.getPath().contains("/")) {
			return (!StringUtils.isEmpty(fos.getDisplayName()))?fos.getDisplayName():fos.getName();
		}
		String path = fos.getPath().substring(0, fos.getPath().lastIndexOf("/"));
		while(path.contains("/")) {
			fos = dirPers.get(path);
			
			if(StringUtils.isEmpty(fos.getDisplayName())) {
				translatedPath = fos.getName()+"/"+translatedPath;
			} else {
				translatedPath = fos.getDisplayName()+"/"+translatedPath;
			}
			
			path = fos.getPath().substring(0, fos.getPath().lastIndexOf("/"));
		}
		if(!path.trim().isEmpty()) {
			fos = dirPers.get(path);
			translatedPath = fos.getDisplayName()+"/"+translatedPath;
		}
		return translatedPath;
	}
	
	public static String getTranslatedPathForFolderOnServer(String directoryPath, BasicConfigurationController config) throws Exception {
		if(StringUtils.isEmpty(directoryPath)) {
			return "";
		}
		IFolderOnServerPersistence dirPersistence = PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(config);
		FolderOnServer fos = dirPersistence.get(directoryPath);
		
		if(fos == null) {
			return "";
		}
		if(!fos.getPath().contains("/")) {
			return (!StringUtils.isEmpty(fos.getDisplayName()))?fos.getDisplayName():fos.getName();
		}
		String translatedPath = (!StringUtils.isEmpty(fos.getDisplayName()))?fos.getDisplayName():fos.getName();
		
		String path = fos.getPath().substring(0, fos.getPath().lastIndexOf("/"));
		while(path.contains("/")) {
			fos = dirPersistence.get(path);
			
			if(StringUtils.isEmpty(fos.getDisplayName())) {
				translatedPath = fos.getName()+"/"+translatedPath;
			} else {
				translatedPath = fos.getDisplayName()+"/"+translatedPath;
			}
			
			path = fos.getPath().substring(0, fos.getPath().lastIndexOf("/"));
		}
		if(!path.trim().isEmpty()) {
			fos = dirPersistence.get(path);
			translatedPath = fos.getDisplayName()+"/"+translatedPath;
		}
		return translatedPath;
	}

}
