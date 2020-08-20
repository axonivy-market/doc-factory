package ch.ivyteam.ivy.addons.filemanager.thumbnailer;

import ch.ivyteam.ivy.scripting.objects.util.IvyScriptObjectEnvironmentContext;

public class ThumbnailConstants {
	public static final String DEFAULT_THUMB_FOLDER_NAME = "thumb/";
	public static final String DEFAULT_THUMB_FOLDER = IvyScriptObjectEnvironmentContext
		    .getIvyScriptObjectEnvironment().getFileArea()
		    .getAbsolutePath()
		    + "/"+DEFAULT_THUMB_FOLDER_NAME;
	public static final String DEFAULT_THUMBNAIL_IMAGE = "unknownFile.jpg";
	public static final String DEFAULT_THUMBNAIL_TYPE = ".jpg";
	public static final String DEFAULT_THUMBNAIL_NO_IMAGE = "no_image.jpg";
}
