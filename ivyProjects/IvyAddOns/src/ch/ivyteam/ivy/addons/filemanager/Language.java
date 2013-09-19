/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

/**
 * @author ec
 *
 */
public class Language {
	
	private String isoName;
	private long id;
	
	@SuppressWarnings("unused")
	private Language() { }
	
	public Language(long id, String isoName) {
		this.id = id;
		this.isoName = isoName.trim().toUpperCase();
	}
	
	public void setIsoName(String isoName) {
		this.isoName = isoName.trim().toUpperCase();
	}
	
	public String getIsoName() {
		return this.isoName;
	}
	
	public void setId(long id)  {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
}
