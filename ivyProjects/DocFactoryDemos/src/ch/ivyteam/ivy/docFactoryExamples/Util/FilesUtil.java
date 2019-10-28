package ch.ivyteam.ivy.docFactoryExamples.Util;

import java.io.IOException;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class FilesUtil {

	/*
	 * set file reference into the session
	*/
	public static void setFileRef(File ivyFile)
	{
		Ivy.session().setAttribute("docRef", Ivy.html().fileref(ivyFile));
		Ivy.session().setAttribute("docFilename", ivyFile.getName());

	}

	/*
	 * set file reference into the session
	 */
	public static void setFileRef(java.io.File file) throws IOException
	{
		Ivy.session().setAttribute("docRef", Ivy.html().fileref(new File(file.getParentFile().getName()+"/"+file.getName())));
		Ivy.session().setAttribute("docFilename", file.getName());
	}		

}
