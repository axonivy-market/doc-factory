package ch.ivyteam.ivy.addons.filemanager.thumbnailer.enums;

import ch.ivyteam.ivy.addons.filemanager.thumbnailer.ThumbnailerConstants;

public enum LicenseType {
	WORD (1, ThumbnailerConstants.LICENSE_WORD),
	EXCEL(2,ThumbnailerConstants.LICENSE_EXCEL),
	PDF(3,ThumbnailerConstants.LICENSE_PDF),
	SLIDE(4,ThumbnailerConstants.LICENSE_SLIDE),
	;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private int id;
	private String name;
	private LicenseType (int _id, String _name){
		this.id = _id;
		this.name = _name;
	}
	
	
}
