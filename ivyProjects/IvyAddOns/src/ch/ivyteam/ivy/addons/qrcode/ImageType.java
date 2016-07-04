package ch.ivyteam.ivy.addons.qrcode;

import ch.ivyteam.api.API;

public enum ImageType {
	PNG("png"), 
	JPG("jpg"), 
	JPEG("jpeg");
	
	private String value;
	
	private ImageType(String value) {
		API.checkParameterNotEmpty(value, "Image type value");
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns true if the given type is an ImageType (image type extension without leading ".").
	 * @param value
	 * @return
	 */
	public static boolean isAcceptedImageType(String value) {
		for(ImageType img : ImageType.values()) {
			if(img.getValue().equalsIgnoreCase(value))  {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns the list of the image types as String. For the moment "png jpg jpeg".
	 * @return image types as String. For the moment "png jpg jpeg".
	 */
	public static String getImageTypesListAsString() {
		String result = "";
		for(ImageType img : ImageType.values()) {
			result += img.getValue()+" ";
		}
		return result.toString();
	}
}
