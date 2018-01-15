/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */

package ch.ivyteam.ivy.addons.filemanager.thumbnailer.mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.ivyteam.ivy.addons.filemanager.thumbnailer.util.IOUtil;
import ch.ivyteam.ivy.addons.service.AddonsServiceLoader;
import ch.ivyteam.ivy.addons.service.ServiceLoaderPluginFilter;
import ch.ivyteam.ivy.environment.Ivy;


/**
 * Wrapper class for MIME Identification of Files.
 * 
 * Depends:
 * <li>Aperture (for MIME-Detection)
 */
public class MimeTypeDetector {


	private List<MimeTypeIdentifier> extraIdentifiers;
	
	
	private int minArrayLength;

	/**
	 * Create a MimeType Detector and init it.
	 */
	public MimeTypeDetector() {
		extraIdentifiers = new ArrayList<MimeTypeIdentifier>();
		
		addMimeTypeIdentifier(new ScratchFileIdentifier());
		
		Set<String> allowedExt = new HashSet<String>();
		allowedExt.add("jar");
		Set<MimeTypeIdentifier> mimeTypeIdentifiers;
		try {
			mimeTypeIdentifiers = AddonsServiceLoader.loadServiceWithTheIvyAddonsProjectClassLoader(MimeTypeIdentifier.class,true, 
					new ServiceLoaderPluginFilter(allowedExt,""));
			for(MimeTypeIdentifier mti : mimeTypeIdentifiers) {
				this.addMimeTypeIdentifier(mti);
				try{
					Method m = mti.getClass().getDeclaredMethod("getMinArrayLength");
					if(m!=null) {
						this.minArrayLength = (Integer) m.invoke(mti);
					}
				}catch(Exception e){
		    		Ivy.log().warn(e.getMessage());
		    	}
			}
		} catch(Exception e){
    		Ivy.log().error(e.getMessage());
    	}
		
	}

	/**
	 * Add a new MimeTypeIdentifier to this Detector.
	 * MimeTypeIdentifier may override the decision of the detector.
	 * The order the identifiers are added will also be the order they will be executed
	 * (i.e., the last identifiers may override all others.)
	 * 
	 * @param identifier	a new MimeTypeIdentifier
	 */
	private void addMimeTypeIdentifier(MimeTypeIdentifier identifier) {
		extraIdentifiers.add(identifier);
	}
	
	/**
	 * Detect MIME-Type for this file.
	 * 
	 * @param file	File to analyse
	 * @return	String of MIME-Type, or null if no detection was possible (or unknown MIME Type)
	 */
	public String getMimeType(File file) {
		byte[] bytes = new byte[(int) (this.minArrayLength>0?this.minArrayLength:file.length())];
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
			fis = null;
		} catch (IOException e) {
			return null; // File does not exist or other I/O Error
		} finally {
			IOUtil.quietlyClose(fis);
		}
		String mimeType = "";
		
		// Identifiers may re-write MIME.
		for (MimeTypeIdentifier identifier : extraIdentifiers) {
			try{
				mimeType = identifier.identify(mimeType, bytes, file);
			}catch(Exception ex){
				
			}
			if(!mimeType.trim().isEmpty()) {
				Ivy.log().info("MimeTypeDetector has detected following mime {0} with {1}",mimeType,identifier);
				break;
			}
		}
		
		return mimeType;
	}
	
	/**
	 * Return the standard extension of a specific MIME-Type.
	 * What are these files "normally" called?
	 * 
	 * @param mimeType	MIME-Type, e.g. "text/plain"
	 * @return	Extension, e.g. "txt"
	 */
	public String getStandardExtensionForMimeType(String mimeType) {
		List<String> extensions = getExtensionsCached(mimeType);
		
		if (extensions == null)
			return null;
		
		try {
			return extensions.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	private Map<String, List<String>> extensionsCache = new HashMap<String, List<String>>(); 
	
	protected List<String> getExtensionsCached(String mimeType) {
		List<String> extensions = extensionsCache.get(mimeType);
		if (extensions != null)
			return extensions;
		
		for (MimeTypeIdentifier identifier : this.extraIdentifiers) {
			if (extensions != null)
				return extensions;
			
			extensions = identifier.getExtensionsFor(mimeType);
		}
		
		extensionsCache.put(mimeType, extensions);
		return extensions;
	}
	
}
