package ch.ivyteam.ivy.addons.docfactory;

import java.io.File;

import ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment;

public class MyIvyScriptObjectEnvironment implements
		IIvyScriptObjectEnvironment {

	@Override
	public String format(String arg0, Object arg1) {
		return String.format(arg0, arg1);
	}

	@Override
	public String getApplicationName() {
		return "DocFactory";
	}

	@Override
	public File getFileArea() {
		return new File("test");
	}

	@Override
	public File getTempFileArea() {
		return new File("test");
	}

}
