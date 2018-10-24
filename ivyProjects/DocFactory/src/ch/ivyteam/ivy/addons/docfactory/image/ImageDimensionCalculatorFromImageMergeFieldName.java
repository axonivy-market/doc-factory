package ch.ivyteam.ivy.addons.docfactory.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.environment.Ivy;

public class ImageDimensionCalculatorFromImageMergeFieldName implements
		ImageDimensionCalculator {

	@Override
	public Dimension calculateImageDimensionForMergingInTemplate(
			InputStream imageStream, String fieldname, Object... objects) throws IOException {
		if(imageStream == null || StringUtils.isBlank(fieldname)) {
			throw new IllegalArgumentException("The Image Input Stream cannot be null and the fieldName cannot be blank.");
		}
		int width = getImageWithInImageFieldName(fieldname);
		int height = getImageHeightInImageFieldName(fieldname);
		if(width > 0 && height == 0) {
			height = getScaledHeight(imageStream, width);
		}
		if(width == 0 && height > 0) {
			width = getScaledWidth(imageStream, height);
		}
		return new Dimension(width, height);
	}
	
	private int getImageWithInImageFieldName(String imageFieldName) {
		if(imageFieldName.contains("__WIDTH:")) {
			String widthSub = imageFieldName.substring(imageFieldName.indexOf("__WIDTH:") + 8);
			if(widthSub.contains("__HEIGHT:")) {
				widthSub = widthSub.substring(0, widthSub.indexOf("__HEIGHT:"));
			}
			if(widthSub.length() == 0) {
				return 0;
			}
			try {
				return Integer.parseInt(widthSub);
			} catch (Exception ex) {
				
			}
		}
		return 0;
	}
	
	private int getImageHeightInImageFieldName(String imageFieldName) {
		if(imageFieldName.contains("__HEIGHT:")) {
			String heightSub = imageFieldName.substring(imageFieldName.indexOf("__HEIGHT:") + 9);
			if(heightSub.contains("__WIDTH:")) {
				heightSub = heightSub.substring(0, heightSub.indexOf("__WIDTH:"));
			}
			if(heightSub.length() == 0) {
				return 0;
			}
			try {
				return Integer.parseInt(heightSub);
			} catch (Exception ex) {
				
			}
		}
		return 0;
	}
	
	private int getScaledWidth(InputStream imageStream, int resizedHeight) throws IOException {
		BufferedImage bimg = readInputStreamToBufferedImage(imageStream);
		if(bimg == null) {
			return 0;
		}
		int width = bimg.getWidth();
		int height = bimg.getHeight();
		return width * resizedHeight / height;
	}
	
	private int getScaledHeight(InputStream imageStream, int resizedWidth) throws IOException {
		BufferedImage bimg = readInputStreamToBufferedImage(imageStream);
		if(bimg == null) {
			return 0;
		}
		int width = bimg.getWidth();
		int height = bimg.getHeight();
		return height * resizedWidth / width;
	}

	private BufferedImage readInputStreamToBufferedImage(InputStream imageStream) {
		BufferedImage bimg = null;
		try{
			bimg = ImageIO.read(imageStream);
		}catch(Exception ex) {
			Ivy.log().error("Got an error while ImageIO read imageStream", ex);
		}
		return bimg;
	}

}
