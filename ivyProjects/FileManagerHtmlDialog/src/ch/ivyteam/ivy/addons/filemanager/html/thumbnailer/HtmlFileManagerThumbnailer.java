package ch.ivyteam.ivy.addons.filemanager.html.thumbnailer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ch.ivyteam.ivy.addons.filemanager.thumbnailer.Thumbnailer;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.enums.ImageType;
import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.ThumbnailCreator;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.enums.LicenseType;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.mime.MimeTypeDetector;
import ch.ivyteam.ivy.addons.service.AddonsServiceLoader;
import ch.ivyteam.ivy.addons.service.ServiceLoaderPluginFilter;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Binary;
import ch.ivyteam.ivy.scripting.objects.util.IvyScriptObjectEnvironment;

/**
 * 
 * @author ec
 *
 */
public class HtmlFileManagerThumbnailer {
	private ThumbnailCreator thumbnailer;
	private MimeTypeDetector mimeTypeDetector;
	private com.aspose.words.License wordLicense = null;
	private com.aspose.cells.License excelLicense = null;
	private com.aspose.slides.License pptLicense = null;
	
    public HtmlFileManagerThumbnailer() throws Exception {
    	//init param to create thumbnail
		initParams();
		//load thumbnailer
		loadExistingThumbnailers();
	}
	
    private void loadLicense(){
    	wordLicense = new com.aspose.words.License();
    	excelLicense = new com.aspose.cells.License();
    	pptLicense = new com.aspose.slides.License();
		try {
			InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();
			wordLicense.setLicense(in);
			in.close();
			InputStream in2 = ThirdPartyLicenses.getDocumentFactoryLicense();
			excelLicense.setLicense(in2);
			in2.close();
			InputStream in3 = ThirdPartyLicenses.getDocumentFactoryLicense();
			pptLicense.setLicense(in3);
			in3.close();
		} catch (Exception e) {
			Ivy.log().error("Aspose Licence error "+e);
		}catch (Throwable t) {
			Ivy.log().error("Aspose Licence error "+t);
		}
    }
    private void putLicense(){
    	if(thumbnailer != null){
    		//license for Word
    		if(wordLicense != null){
    			thumbnailer.putAsposeLicense(LicenseType.WORD, wordLicense);
    		}
    		//license for Excel
    		if(excelLicense != null){
    			thumbnailer.putAsposeLicense(LicenseType.EXCEL,excelLicense);
    		}
    		//license for PPT
    		if(pptLicense != null){
    			thumbnailer.putAsposeLicense(LicenseType.SLIDE, pptLicense);
    		}
    	}
    }
    /**
     * create thumbnail from input
     * 
     * @param input
     * @return
     */
	public ReturnedMessage createThumbnail(File input) 
	{
		ReturnedMessage message = new ReturnedMessage();
		if(!checkValidMIMEtype(input)){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setFile(null);
			message.setText("invalid file type to create thubmnail");
			return message;
			
		}
		File output = null;
		try{
			Ivy.log().info("Trying to create Thumbnail {0}",input);
			output = thumbnailer.createThumbnail(input);
			if(output != null && output.exists()) {
				message.setType(FileHandler.SUCCESS_MESSAGE);
				message.setFile(output);
			} else {
				message.setText("can not create thubmnail");
			}
		}catch(Exception e){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setFile(null);
    		Ivy.log().error("Exception in createThumbnail():" + e.getMessage());
    		return message;
    	}
		
		return message;
	}
	
	/**
	 * check mimeType is valid to create thumbnail
	 * 
	 * @param input
	 * @return
	 */
	private boolean checkValidMIMEtype(File input) {
		Set<String> mimeTypes = thumbnailer.getAcceptedMIMETypes();
		String mimeType = getMimeType(input);
		Iterator<String> iter = mimeTypes.iterator();
		while(iter.hasNext()) {
			String s = iter.next();
			if(s.equalsIgnoreCase(mimeType)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * return MIMEType of file
	 * 
	 * @param input
	 * @return
	 */
	private String getMimeType(File input) {
		return mimeTypeDetector.getMimeType(input);
	}
	/**
	 * init param to create thumbnail
	 * including: size and folder
	 */
	private void initParams()  {
		try{
			thumbnailer = new ThumbnailCreator();
			String rootPath = IvyScriptObjectEnvironment
					.getIvyScriptObjectEnvironment().getTempFileArea()
					.getAbsolutePath()
					+ "/thumbnailers/";
			Ivy.log().info("Input params for thubmnails: {0}",rootPath );
			
			thumbnailer.setThumbnailFolder(rootPath);
			thumbnailer.setImageOutputType(ImageType.JPEG);
			mimeTypeDetector = new MimeTypeDetector();
			//load license
			loadLicense();
			//put license for thumbnailer
			putLicense();
		}catch(Exception e){
    		Ivy.log().error(e.getMessage());
    	}
	}
	/**
	 * set image size for thumbnailer
	 * @param width
	 * @param height
	 * @param resizeOption
	 */
	public void setImageSize(int width, int height, int resizeOption) {
		thumbnailer.setImageSize(width,height, resizeOption);
	}

	/**
	 * load file Type that can be create thumbnail
	 * @author: trung do
	 */
	protected void loadExistingThumbnailers()  {
		
		try{
			Set<String> allowedExt = new HashSet<String>();
			allowedExt.add("jar");
			Set<Thumbnailer> thumbnailers = AddonsServiceLoader.loadServiceWithTheIvyAddonsProjectClassLoader(Thumbnailer.class,true, 
					new ServiceLoaderPluginFilter(allowedExt,""));
			for(Thumbnailer th : thumbnailers) {
				thumbnailer.registerThumbnailer(th);
			}
		}catch(Exception e){
    		Ivy.log().error(e.getMessage());
    	}
	}
	
	/**
	 * Generate full size images from byte array
	 * @author tctruc
	 * @param input
	 * @return OutputStream[]
	 */
	public OutputStream[] createFullSizeImages(byte[] input, String filename, int totalPagesForGenerating) {
		OutputStream[] result = null;
		try{
			ch.ivyteam.ivy.scripting.objects.File tempFile = new ch.ivyteam.ivy.scripting.objects.File(filename, true);
			//tempFile.makePersistent(false);
			tempFile.writeBinary(new Binary(input));
			
			if(checkValidMIMEtype(tempFile.getJavaFile())){
				result = thumbnailer.generateFullSizeImages(tempFile.getJavaFile(), null, totalPagesForGenerating);
			}
		}
		catch(Exception e) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Get number of pages
	 * @param input
	 * @param mimeType
	 * @return total pages
	 * @throws IOException
	 * @author: tctruc
	 * @Date: Aug 5, 2014
	 */
	public int getNumberOfPages(File input, String mimeType) throws IOException {
		int result = 0;
		try {
			if(checkValidMIMEtype(input)) {
				return thumbnailer.getNumberOfPages(input, mimeType);
			}
		} catch(Exception ex) {
			result = 0;
		}
		return result;
	}
	

}
