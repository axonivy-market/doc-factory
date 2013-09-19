/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.Language;

/**
 * @author ec
 *
 */
public interface ILanguagePersistence extends IItemPersistence<Language> {
	
	/**
	 * Returns the list of the existing Language objects.
	 * @return java.util.List of Language objects.
	 * @throws Exception 
	 */
	public List<Language> getList() throws Exception;

}
