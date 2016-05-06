package ch.ivyteam.ivy.addons.filemanager.database.sql;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import ch.ivyteam.ivy.scripting.objects.File;

public final class FileExtractor {
	
	public static final int DEFAULT_BUFFER_SIZE = 512;
	
	private FileExtractor() {}
	
	public static java.io.File extractBlobToTemporaryFile(java.sql.Blob blob, String fileName) throws IOException, SQLException {
		return extractInputStreamToTemporaryFile(blob.getBinaryStream(), fileName);
	}
	
	public static java.io.File extractInputStreamToTemporaryFile(InputStream inputStream, String fileName) throws IOException, SQLException {
		return extractInputStreamToTemporaryIvyFile(inputStream, fileName).getJavaFile();
	}
	
	public static File extractInputStreamToTemporaryIvyFile(InputStream inputStream, String fileName) throws IOException, SQLException {
		String tmpPath="tmp/"+System.nanoTime()+"/"+fileName;
		File ivyFile = new File(tmpPath, true);
		ivyFile.createNewFile();
		
		java.io.File javaFile = ivyFile.getJavaFile();
		
		FileOutputStream fos = null;
		try {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int read = 0;
			fos = new FileOutputStream(javaFile.getPath());
			while((read = inputStream.read(buffer)) != -1) {
				fos.write(buffer, 0, read);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
            }
            if (fos != null) {
                fos.close();
            }
		}
		
		return ivyFile;
	}
	

}
