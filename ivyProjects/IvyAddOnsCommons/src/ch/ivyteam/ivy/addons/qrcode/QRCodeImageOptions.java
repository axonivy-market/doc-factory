package ch.ivyteam.ivy.addons.qrcode;

import java.awt.Color;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.scripting.objects.util.IvyScriptObjectEnvironment;

/**
 * Bean holding options for QRCode Images like the width and height, the image file export...
 *
 */
public class QRCodeImageOptions implements Serializable {

	private static final long serialVersionUID = 4192856473084689278L;
	private static final String DATE_FORMAT = "yyyy-MM-dd HH-mm-ss-SSS";
    private static final String DOT = ".";
	private static final ImageType DEFAULT_IMAGE_TYPE = ImageType.PNG;
	
	private int widthInPixels;
	private int heightInPixels;
	private java.io.File imageExportFile;
	
	private Color QRCodeBackgroundColor = Color.WHITE;
	private Color QRCodeColor = Color.BLACK;
	
	
	private QRCodeImageOptions() {}
	
	/**
	 * Returns an QRCodeImageOptions representing a square with the given size. The name of the 
	 * @param sizeInPixels size in pixel, cannot be less than 1.
	 * @return QRCodeImageOptions representing a square with the given size.
	 */
	public static QRCodeImageOptions forSquare(int sizeInPixels) {
		API.checkLowerBorder(sizeInPixels, "size of the square in pixels", 1);
		
		QRCodeImageOptions options = new QRCodeImageOptions();
		options.heightInPixels = sizeInPixels;
		options.widthInPixels = sizeInPixels;
		
		return options;
	}
	
	/**
	 * Set the file where the QRCode has to be exported. <br>
	 * If you don't set the export file by using this function, a default exportFile will be used:<br>
	 * This file is located in the Ivy Temp FileArea directory and it's name is made with the current date formatted with "yyyy-MM-dd HH-mm-ss-SSS" + .png
	 * @param exportFile java.io.File. Cannot be null and must be of one accepted type. See: {@link ImageType#getImageTypesListAsString()}
	 * @return the QRCodeImageOptions with the file where the QRCode has to be exported.
	 */
	public QRCodeImageOptions withExportFile(java.io.File exportFile) {
		setExportFile(exportFile);
		return this;
	}
	
	/**
	 * Set the QRCode image colors and return the QRCodeImageOptions. 
	 * If you don't specify the colors by calling this function, by default the colors are white for the background and black for the Code.
	 * @param backgroundColor the background color. Cannot be null.
	 * @param QRCodeColor the QRCode color. Cannot be null.
	 * @return the QRCodeImageOptions
	 */
	public QRCodeImageOptions withQRCodeImageColors(Color backgroundColor, Color QRCodeColor) {
		API.checkNotNull(backgroundColor, "backgroundColor");
		API.checkNotNull(QRCodeColor, "QRCodeColor");
		
		this.QRCodeBackgroundColor = backgroundColor;
		this.QRCodeColor = QRCodeColor;
		return this;
	}

	public int getWidthInPixels() {
		return widthInPixels;
	}

	public void setWidthInPixels(int widthInPixels) {
		API.checkLowerBorder(widthInPixels, "width in pixels", 1);
		this.widthInPixels = widthInPixels;
	}

	public int getHeightInPixels() {
		return heightInPixels;
	}

	public void setHeightInPixels(int heightInPixels) {
		API.checkLowerBorder(heightInPixels, "height in pixels", 1);
		this.heightInPixels = heightInPixels;
	}

	public java.io.File getExporFile() {
		if(imageExportFile == null) {
			imageExportFile = new java.io.File(IvyScriptObjectEnvironment
					.getIvyScriptObjectEnvironment().getTempFileArea(), makeDefaultFileName());
		}
		return imageExportFile;
	}
	
	public Color getQRCodeBackgroundColor() {
		return QRCodeBackgroundColor;
	}

	public void setQRCodeBackgroundColor(Color qRCodeBackgroundColor) {
		QRCodeBackgroundColor = qRCodeBackgroundColor;
	}

	public Color getQRCodeColor() {
		return QRCodeColor;
	}

	public void setQRCodeColor(Color qRCodeColor) {
		QRCodeColor = qRCodeColor;
	}

	public String getImageType() {
		return FilenameUtils.getExtension(getExporFile().getName());
	}

	private String makeDefaultFileName() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT) ;
		Date date = new Date();
		return dateFormat.format(date) + DOT + DEFAULT_IMAGE_TYPE.getValue();
	}

	protected void setExportFile(java.io.File exportFile) {
		API.checkNotNull(exportFile, "export file");
		if(!isFileTypeSupported(exportFile)) {
			throw new IllegalArgumentException("the file type is not supported. "
					+ "Only the following file types are accepted: " + ImageType.getImageTypesListAsString());
		}
		this.imageExportFile = exportFile;
	}
	
	private boolean isFileTypeSupported(java.io.File exportFile) {
		String extension = FilenameUtils.getExtension(exportFile.getName());
		return ImageType.isAcceptedImageType(extension);
	}
	
}
