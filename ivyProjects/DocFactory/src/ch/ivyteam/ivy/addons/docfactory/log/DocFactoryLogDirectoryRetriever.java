package ch.ivyteam.ivy.addons.docfactory.log;

import java.io.File;

public interface DocFactoryLogDirectoryRetriever {

	/**
	 * returns the DocFactory log directory. This is used for example by aspose for converting PDF to PDF/A.
	 * @return the Log directory where the docFactory can put ist own logs.
	 */
	File getLogDirectory();
}
