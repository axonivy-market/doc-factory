/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

import java.util.HashMap;

/**
 * @author ec <br>
 * This class represents a translation for an Item that is stored in a database and that can be uniquely identified with an id (long).<br>
 * The translations are stored in an HashMap of String/String key/value pairs. <br>
 * The key is the language ISO 639 code (DE,EN,FR...) and the value is the corresponding translation..
 *
 */
public class ItemTranslation {
	
	
	private long translatedItemId;
	private HashMap<String,String> translations;
	
	
	@SuppressWarnings("unused")
	private ItemTranslation() {}
	
	public ItemTranslation(long itemId, HashMap<String,String> translations) {
		this.translatedItemId = itemId;
		this.translations = translations==null? new HashMap<String,String>():translations;
	}

	/**
	 * @return the itemId
	 */
	public long getTranslatedItemId() {
		return translatedItemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setTranslatedItemId(long itemId) {
		this.translatedItemId = itemId;
	}

	/**
	 * The translations are an HashMap of String/String key/value pairs. <br>
	 * The key is the language ISO 639 code (DE,EN,FR...) and the value is the corresponding translation.
	 * @return the translations
	 */
	public HashMap<String, String> getTranslations() {
		return translations;
	}

	/**
	 * The translations are an HashMap of String/String key/value pairs. <br>
	 * The key is the language ISO 639 code (DE,EN,FR...) and the value is the corresponding translation.
	 * @param translations the translations to set
	 */
	public void setTranslations(HashMap<String, String> translations) {
		this.translations = translations==null? new HashMap<String,String>():translations;
	}

	
	
	

}
