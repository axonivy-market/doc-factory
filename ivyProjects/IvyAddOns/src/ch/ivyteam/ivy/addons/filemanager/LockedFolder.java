/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

/**
 * @author ec
 *
 */
public class LockedFolder extends FolderOnServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1328281674840323964L;

	/**
	 * 
	 */
	public LockedFolder() {
		super();
	}
	
	public LockedFolder(FolderOnServer fos) {
		super();
		this.setCcd(fos.getCcd());
		this.setCcf(fos.getCcf());
		this.setCdd(fos.getCdd());
		this.setCdf(fos.getCdf());
		this.setCmrd(fos.getCmrd());
		this.setCod(fos.getCod());
		this.setCrd(fos.getCrd());
		this.setCtd(fos.getCtd());
		this.setCud(fos.getCud());
		this.setCuf(fos.getCuf());
		this.setCwf(fos.getCwf());
		this.setCreationDate(fos.getCreationDate());
		this.setCreationTime(fos.getCreationTime());
		this.setCreationUser(fos.getCreationUser());
		this.setId(fos.getId());
		this.setName(fos.getName());
		this.setPath(fos.getPath());
		this.setIconPath(fos.getIconPath());
		this.setIs_protected(fos.getIs_protected());
		this.setIsRoot(fos.getIsRoot());
		this.setTranslation(fos.getTranslation());
	}

}
