package ch.ivyteam.ivy.addons.filemanager.thumbnailer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import ch.ivyteam.ivy.addons.filemanager.thumbnailer.Thumbnailer;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.enums.ImageType;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.enums.LicenseType;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.mime.MimeTypeDetector;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.util.ChainedHashMap;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.util.IOUtil;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.util.StringUtil;
import ch.ivyteam.ivy.environment.Ivy;

public class ThumbnailCreator implements Thumbnailer, ThumbnailerConstants {
	/** Folder under which new thumbnails should be filed */
	private File thumbnailFolder;
	private int thumbWidth;
	private int thumbHeight;
	private double thumbOptions = 0;
	private ImageType imgType;
	/** MIME Type for "all MIME" within thumbnailers Hash */
	private static final String ALL_MIME_WILDCARD = "*/*";
	private static final int DEFAULT_NB_MIME_TYPES = 40;
	private static final int DEFAULT_NB_THUMBNAILERS_PER_MIME = 5;
	private static final ImageType DEFAULT_IMAGE_TYPE = ImageType.JPEG;

	private AsposeLicense asposeLicense;

	/**
	 * Thumbnailers per MIME-Type they accept (ALL_MIME_WILDCARD for all)
	 */
	private ChainedHashMap<String, Thumbnailer> thumbnailers;

	/**
	 * All Thumbnailers.
	 */
	private Queue<Thumbnailer> allThumbnailers;

	/**
	 * Magic Mime Detection ... a wrapper class to Aperature's Mime thingies.
	 */
	private MimeTypeDetector mimeTypeDetector;

	/**
	 * Initialise Thumbnail Manager
	 */
	public ThumbnailCreator() {
		// Execute close() when JVM shuts down (if it wasn't executed before).
		final ThumbnailCreator self = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				IOUtil.quietlyClose(self);
			}
		});

		thumbnailers = new ChainedHashMap<String, Thumbnailer>(
				DEFAULT_NB_MIME_TYPES, DEFAULT_NB_THUMBNAILERS_PER_MIME);
		allThumbnailers = new LinkedList<Thumbnailer>();

		mimeTypeDetector = new MimeTypeDetector();

		thumbHeight = ThumbnailerConstants.THUMBNAIL_DEFAULT_HEIGHT;
		thumbWidth = ThumbnailerConstants.THUMBNAIL_DEFAULT_WIDTH;
		this.imgType = DEFAULT_IMAGE_TYPE;
		asposeLicense = AsposeLicense.getInstance();
	}

	/**
	 * put the license of aspose to system
	 * 
	 * @param type
	 * @param license
	 */
	public void putAsposeLicense(LicenseType type, Object license) {
		this.asposeLicense.putLicense(type.getName(), license);
	}

	/**
	 * create thumbnail (this method should be called when user want to make
	 * thumbnail).
	 * 
	 * @param input
	 * @param outputFileNameWithoutExtension
	 * @return
	 * @throws ThumbnailerException
	 * @throws Exception
	 */
	public File createThumbnail(File input) throws ThumbnailerException,
			Exception {
		File output = chooseThumbnailFilename(input, true);
		generateThumbnail(input, output);
		return output;
	}

	/**
	 * Calculate a thumbnail filename (via hashing).
	 * 
	 * @param input
	 *            Input file
	 * @param checkExist
	 *            If true: guarantuee that such a filename doesn't exist yet
	 * @return The chosen filename
	 */
	private File chooseThumbnailFilename(File input, boolean checkExist) {
		if (thumbnailFolder == null)
			throw new RuntimeException(
					"chooseThumbnailFilename cannot be run before a first call to setThumbnailFolder()");
		if (input == null)
			throw new NullPointerException("Input file may not be null");

		String hash = ""; // "_" + generate_hash(input.getAbsolutePath());
		String prefix = input.getName().replace('.', '_');

		int tries = 0;
		String suffix = "";
		File output;
		do {
			if (tries > 0) {
				int suffix_length = tries / 4 + 1; // Simple (i.e. guessed)
													// heuristic to add
													// randomness if many files
													// have the same name
				suffix = "-" + StringUtil.randomString(suffix_length);
			}

			String name = prefix + hash + suffix + "." + this.imgType.getName();
			output = new File(thumbnailFolder, name);

			tries++;
		} while (checkExist && output.exists());

		return output;
	}

	public File getThumbnailFolder() {
		return thumbnailFolder;
	}

	public int getThumbWidth() {
		return thumbWidth;
	}

	public void setThumbWidth(int thumbWidth) {
		this.thumbWidth = thumbWidth;
	}

	public int getThumbHeight() {
		return thumbHeight;
	}

	public void setThumbHeight(int thumbHeight) {
		this.thumbHeight = thumbHeight;
	}

	public void setThumbnailFolder(File thumbnailFolder) {
		this.thumbnailFolder = thumbnailFolder;
	}

	/**
	 * Add a AbstractThumbnailer-Class to the list of available
	 * AbstractThumbnailer Note that the order you add AbstractThumbnailer may
	 * make a difference: First added AbstractThumbnailer are tried first, if
	 * one fails, the next (that claims to be able to treat such a document) is
	 * tried. (AbstractThumbnailer that claim to treat all MIME Types are tried
	 * last, though.)
	 * 
	 * @param thumbnailer
	 *            AbstractThumbnailer to add.
	 */
	public void registerThumbnailer(Thumbnailer thumbnailer)
			throws Exception {
		Set<String> acceptMIME = thumbnailer.getAcceptedMIMETypes();
		if (acceptMIME == null)
			thumbnailers.put(ALL_MIME_WILDCARD, thumbnailer);
		else {
			for (String mime : acceptMIME)
				thumbnailers.put(mime, thumbnailer);
		}
		allThumbnailers.add(thumbnailer);

		thumbnailer.setImageSize(thumbWidth, thumbHeight, thumbOptions);
		thumbnailer.setImageOutputType(this.imgType);

	}

	/**
	 * Set the folder where the thumbnails should be generated by default (if no
	 * output file is given).
	 * 
	 * @param thumbnailPath
	 *            Path where the future thumbnails will be written to
	 * @throws FileDoesNotExistException
	 *             If the given path is not writeable
	 */
	public void setThumbnailFolder(String thumbnailPath)
			throws FileDoesNotExistException {
		File f = new File(thumbnailPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		setThumbnailFolder(f);
	}

	public void generateThumbnail(File input, File output, String mimeType)
			throws IOException, ThumbnailerException {

		FileDoesNotExistException.check(input, "The input file");
		boolean generated = false;
		// MIME might be known already (in case of recursive thumbnail managers)
		if (mimeType == null) {
			mimeType = mimeTypeDetector.getMimeType(input);
			
		}
		Ivy.log().info("Mime type detected in ThumbnailCreator generateThumbnail(File input, File output, String mimeType): {0}",mimeType);
		
		if (mimeType != null)
			generated = executeThumbnailers(mimeType, input, output, mimeType);

		// Try again using wildcard thumbnailers
		if (!generated)
			generated = executeThumbnailers(ALL_MIME_WILDCARD, input, output,
					mimeType);

		if (!generated)
			throw new ThumbnailerException(
					"No suitable Thumbnailer has been found. (File: "
							+ input.getName() + " ; Detected MIME: " + mimeType
							+ ")");
		Ivy.log().info("Thumbnail has been generated in ThumbnailCreator generateThumbnail(File input, File output, String mimeType): {0}",output);
	}

	/**
	 * Helper function for Thumbnail generation: execute all thumbnailers of a
	 * given MimeType.
	 * 
	 * 
	 * @param useMimeType
	 *            Which MIME Type the thumbnailers should be taken from
	 * @param input
	 *            Input File that should be processed
	 * @param output
	 *            Output file where the image shall be written.
	 * @param detectedMimeType
	 *            MIME Type that was returned by automatic MIME Detection
	 * @return True on success (1 thumbnailer could generate the output file).
	 * @throws IOException
	 *             Input file cannot be read, or output file cannot be written,
	 *             or necessary temporary files could not be created.
	 */
	private boolean executeThumbnailers(String useMimeType, File input,
			File output, String detectedMimeType) throws IOException {
		for (Thumbnailer thumbnailer : thumbnailers
				.getIterable(useMimeType)) {
			try {
				Ivy.log().info("1. executeThumbnailers in ThumbnailCreator: {0}",thumbnailer);
				thumbnailer.generateThumbnail(input, output, detectedMimeType);
				Ivy.log().info("2. executeThumbnailers in ThumbnailCreator: {0}",thumbnailer);
				return true;
			} catch (Exception e) {
				Ivy.log().warn("executeThumbnailers in ThumbnailCreator: ",e);
				// This Thumbnailer apparently wasn't suitable, so try next
				System.out.println("Warning: "
						+ thumbnailer.getClass().getName()
						+ " could not handle the file " + input.getName()
						+ " (trying next)");
			}
		}
		return false;
	}

	public void generateThumbnail(File input, File output) throws IOException,
			ThumbnailerException {
		generateThumbnail(input, output, null);

	}

	public void close() {
		if (allThumbnailers == null)
			return; // Already closed

		for (Thumbnailer thumbnailer : allThumbnailers) {
			try {
				thumbnailer.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		thumbnailers = null;
		allThumbnailers = null;
	}

	public void setImageOutputType(ImageType _imgType) {
		if (_imgType == null) {
			this.imgType = DEFAULT_IMAGE_TYPE;
		}
		this.imgType = _imgType;

		for (Thumbnailer thumbnailer : allThumbnailers)
			thumbnailer.setImageOutputType(imgType);

	}

	public void setImageSize(int width, int height, double imageResizeOptions) {
		this.thumbWidth = width;
		this.thumbHeight = height;
		this.thumbOptions = imageResizeOptions;
		if (thumbWidth < 0)
			thumbWidth = 0;
		if (thumbHeight < 0)
			thumbHeight = 0;

		for (Thumbnailer thumbnailer : allThumbnailers)
			thumbnailer.setImageSize(thumbWidth, thumbHeight, thumbOptions);

	}

	@Override
	public int getCurrentImageWidth() {
		return this.thumbWidth;
	}

	@Override
	public int getCurrentImageHeight() {
		return this.thumbHeight;
	}

	@Override
	public Set<String> getAcceptedMIMETypes() {
		if (thumbnailers.containsKey(ALL_MIME_WILDCARD))
			return null; // All MIME Types
		else
			return thumbnailers.keySet();
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public OutputStream[] generateFullSizeImages(File input, String mimeType, int totalPagesForGenerating)
			throws IOException, ch.ivyteam.ivy.addons.filemanager.thumbnailer.exception.ThumbnailerException {
		FileDoesNotExistException.check(input, "The input file");
		OutputStream[] generated = null;
		// MIME might be known already (in case of recursive thumbnail managers)
		if (mimeType == null) {
			mimeType = mimeTypeDetector.getMimeType(input);
		}
		
		if (mimeType != null)
			generated = executeFullSizeThumbnailers(mimeType, input, mimeType, totalPagesForGenerating);
		
		// Try again using wildcard thumbnailers
		if (generated == null)
			generated = executeFullSizeThumbnailers(ALL_MIME_WILDCARD, input,
					mimeType, totalPagesForGenerating);

		if (generated == null) 
			throw new ch.ivyteam.ivy.addons.filemanager.thumbnailer.exception.ThumbnailerException(
					"No suitable Thumbnailer has been found. (File: "
							+ input.getName() + " ; Detected MIME: " + mimeType
							+ ")");
		
		
		return generated;
	}
	
	/**
	 * Helper function for Thumbnail generation: execute all thumbnailers of a
	 * given MimeType.
	 * @param useMimeType
	 * @param input
	 * @param detectedMimeType
	 * @return
	 * @throws IOException
	 * @author: tctruc
	 * @Date: Jul 28, 2014
	 */
	private OutputStream[] executeFullSizeThumbnailers(String useMimeType, File input,
			String detectedMimeType, int totalPagesForGenerating) throws IOException {
		OutputStream[] result = null;
		for (Thumbnailer thumbnailer : thumbnailers
				.getIterable(useMimeType)) {
			try {
				result = thumbnailer.generateFullSizeImages(input, detectedMimeType, totalPagesForGenerating);
			} catch (Exception e) {
				// This Thumbnailer apparently wasn't suitable, so try next
				System.out.println("Warning: "
						+ thumbnailer.getClass().getName()
						+ " could not handle the file " + input.getName()
						+ " (trying next)");
			}
		}
		return result;
	}

	@Override
	public int getNumberOfPages(File input, String mimeType) throws IOException {
		FileDoesNotExistException.check(input, "The input file");
		int result = 0;
		
		if (mimeType == null) {
			mimeType = mimeTypeDetector.getMimeType(input);
		}
		
		if (mimeType != null)
			result = executeGetNumberOfPages(mimeType, input, mimeType);
		
		if (result == 0)
			result = executeGetNumberOfPages(ALL_MIME_WILDCARD, input,
					mimeType);
		
		if (result == 0) 
			Ivy.log().warn("No suitable Thumbnailer has been found. (File: "
							+ input.getName() + " ; Detected MIME: " + mimeType
							+ ")");
		
		return result;
	}
	
	/**
	 * Helper function for getting number of pages
	 * given MimeType.
	 * @param useMimeType
	 * @param input
	 * @param detectedMimeType
	 * @return
	 * @throws IOException
	 * @author: tctruc
	 * @Date: Jul 28, 2014
	 */
	private int executeGetNumberOfPages(String useMimeType, File input,
			String detectedMimeType) throws IOException {
		int result = 0;
		for (Thumbnailer thumbnailer : thumbnailers
				.getIterable(useMimeType)) {
			try {
				result = thumbnailer.getNumberOfPages(input, detectedMimeType);
			} catch (Exception e) {
				result = 0;
				// This Thumbnailer apparently wasn't suitable, so try next
				Ivy.log().warn("Warning: "
						+ thumbnailer.getClass().getName()
						+ " could not handle the file " + input.getName()
						+ " (trying next)");
			}
		}
		Ivy.log().info("Total Pages: " + result);
		return result;
	}

}
