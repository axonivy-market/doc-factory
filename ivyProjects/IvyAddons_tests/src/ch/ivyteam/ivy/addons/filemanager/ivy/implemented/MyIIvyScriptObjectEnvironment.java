/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.io.File;

import ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment;

/**
 * @author ec
 *
 */
public class MyIIvyScriptObjectEnvironment implements
		IIvyScriptObjectEnvironment {

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment#format(java.lang.String, java.lang.Object)
	 */
	@Override
	public String format(String arg0, Object arg1) {
		return arg0.concat(arg1.toString());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment#getApplicationName()
	 */
	@Override
	public String getApplicationName() {
		return "myApp";
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment#getFileArea()
	 */
	@Override
	public File getFileArea() {
		return new java.io.File("test/test");
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment#getTempFileArea()
	 */
	@Override
	public File getTempFileArea() {
		return new java.io.File("test/tmp");
	}

}
