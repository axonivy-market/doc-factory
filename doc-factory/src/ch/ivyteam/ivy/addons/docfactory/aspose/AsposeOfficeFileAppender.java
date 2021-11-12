package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.exception.DocumentGenerationException;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;
import ch.ivyteam.ivy.environment.Ivy;

import com.aspose.words.Document;

public class AsposeOfficeFileAppender {
	
	private AsposeOfficeFileAppender() {
		init();
	}
	
	static AsposeOfficeFileAppender getInstance() {
		return new AsposeOfficeFileAppender();
	}
	
	private void init() {
		try {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);
		} catch (Exception e) {
			Ivy.log().error("An error occurred while loading the Aspose WORDS license.", e);
		}
	}

	File appendFiles(List<File> filesToAppendTogether, FileAppenderOptions fileAppenderOptions) throws Exception {
		API.checkNotNull(fileAppenderOptions, "MultipleDocumentsCreationOptions multipleDocumentsCreationOptions");
		
		List<Document> documents = new ArrayList<>();
		for(File file: filesToAppendTogether) {
			documents.add(new Document(file.getAbsolutePath()));
		}
		Document document = AsposeDocumentAppender.appendDocuments(documents, fileAppenderOptions);
		FileOperationMessage result = AsposeDocFactoryFileGenerator.getInstance().exportDocumentToFile(document, 
				fileAppenderOptions.getAppendedFileAbsolutePathWithoutFormatEnding(), 
				fileAppenderOptions.getAppendedFileOutputFormat());
		
		if(!result.isSuccess()) {
			throw new DocumentGenerationException(result);
		}
		
		return result.getFiles().get(0);
	}

}
