package ch.ivyteam.ivy.docFactoryExamples;

import java.io.File;
import java.nio.file.Paths;

/*
 * Loader for files from resources folder.
 */
public class ResourceLoader {

	public File getResource(String relativFilePath) {
		try {
			return Paths.get(getClass().getClassLoader().getResource(relativFilePath).toURI()).toFile();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
 