package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;
import java.util.List;

import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.FileAppender;
import ch.ivyteam.ivy.addons.docfactory.FileUtil;
import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;

public class AsposeFileAppender extends FileAppender {

	@Override
	public File appendPdfFiles(List<File> filesToAppendTogether,
			FileAppenderOptions fileAppenderOptions)
			throws Exception {
		String filePath = FileUtil.formatPathWithEndSeparator(fileAppenderOptions.getAppendedFileParentDirectoryPath()) + 
				fileAppenderOptions.getAppendedSingleFileName() + DocFactoryConstants.PDF_EXTENSION;
		return PdfFactory.get().appendPdfFiles(filePath, 
				filterPdfFiles(filesToAppendTogether)).getJavaFile();
	}		

	@Override
	public File appendOfficeWordFiles(List<File> filesToAppendTogether,
			FileAppenderOptions fileAppenderOptions)
			throws Exception {
		return AsposeOfficeFileAppender.getInstance().
				appendFiles(filterOfficeWordFiles(filesToAppendTogether), fileAppenderOptions);
	}
	
}
