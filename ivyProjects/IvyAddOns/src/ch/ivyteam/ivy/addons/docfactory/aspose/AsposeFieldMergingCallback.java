/**
 * 
 */
package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.ivyteam.ivy.environment.Ivy;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;

/**
 * This class was developed to be able to handle merge fields that should be filled up with images.<br> 
 * This is used in the AsposeDocFactory class to insert the image when the mail merge engine encounters some fields named as "Image: + name of
 * imageMailField".<br>
 * In such a case the merge-field-value corresponding to this field can be a String (path of the image file) or the image content as ByteArray.<br><br>
 * 
 * You can extend this class and provide your own implementation if your mail
 * merging has to behave dependently of the merge field's name or value.<br>
 * An example can be found at {@link http://www.aspose.com/community/forums/thread/380671/html-text-with-merge-field.aspx} <br><br>
 * Two methods can be overwritten:<br>
 * {@code public void fieldMerging(FieldMergingArgs arg0) throws Exception }, see the example linked above, the AsposeFieldMergingCallback class provides an empty implementation for this method,<br>
 * {@code public void imageFieldMerging(ImageFieldMergingArgs e) throws Exception }, overwrites it only in the case you need some adaptation in the way the images fields are handled. <br><br>
 * Use the {@code AsposeDocFactory.setAsposeFieldMergingCallback(AsposeFieldMergingCallback fieldMergingCallback)} method to pass your own AsposeFieldMergingCallback to the docFactory.
 */
public class AsposeFieldMergingCallback implements IFieldMergingCallback {

	
	/**
	 * You can define the behavior of the field merging by extending 
	 * this class and providing you own implementation of this method.
	 */
	@Override
	public void fieldMerging(FieldMergingArgs arg0) throws Exception { }

	
	@Override
	public void imageFieldMerging(ImageFieldMergingArgs e) throws Exception {
		InputStream imageStream = null;
		if(e.getFieldValue() != null){
			// The field value is a byte array, just cast it and create a stream on it.
			if(e.getFieldValue().getClass().getComponentType()!= null && 
					e.getClass().getComponentType().getName().equalsIgnoreCase("java.lang.Byte")){
				imageStream = new ByteArrayInputStream((byte[])e.getFieldValue());
				e.setImageStream(imageStream);
			}else if(e.getFieldValue().getClass().getName().equalsIgnoreCase("java.lang.String")){
				try {
					imageStream = new FileInputStream(new java.io.File((String) e.getFieldValue()));
					e.setImageFileName((String) e.getFieldValue());
				} catch (FileNotFoundException ex) {
					Ivy.log().error("FileNotFoundException occurred while getting an imageStream for mail merging.",ex);
					return;
				}
			}
		}

	}
}
