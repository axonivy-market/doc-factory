package ch.ivyteam.ivy.addons.filemanager.thumbnailer;

import java.util.HashMap;
import java.util.Map;

class AsposeLicense {
	private static AsposeLicense asposeLicense;
	private Map<String, Object> licenses;
	private AsposeLicense(){
		licenses = new HashMap<String, Object>();
	
	}
	
	static AsposeLicense getInstance(){
		if( asposeLicense == null) {
			asposeLicense = new AsposeLicense();
		}
			
		return asposeLicense;
	}
	void putLicense(String name, Object license) {
		if(asposeLicense.getLicenses().containsKey(name) == false) {
			asposeLicense.getLicenses().put(name, license);
		}
	}
	
	public Map<String, Object> getLicenses() {
		return licenses;
	}

	public void setLicenses(Map<String, Object> licenses) {
		this.licenses = licenses;
	}
	
}
