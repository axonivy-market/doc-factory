package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;

import ch.ivyteam.ivy.addons.docfactory.AsposeDocFactory;

import com.aspose.words.Document;

/**
 * A DocumentWorker can be injected in an AsposeDocFactory (
 * {@link AsposeDocFactory#withDocumentWorker(DocumentWorker)}) for applying
 * some custom logic on the com.aspose.words.Document generated by the mail
 * merge with a template. Please read the com.aspose.words.Document API from
 * Aspose (https://www.aspose.com/products/words/java).<br>
 * No support will be granted for your custom logic.
 *
 */
public interface DocumentWorker {

	/**
	 * Will be called before the mail merge logic has been performed on the
	 * document
	 * 
	 * @param document the aspose words Document upon which the mail merge will be performed.
	 */
	default void prepare(Document document) {

	}

	/**
	 * Will be called after the mail merge logic has been performed on the
	 * document
	 * 
	 * @param document the aspose words Document upon which the mail merge has been performed.
	 */
	default void postCreate(Document document) {

	}

	/**
	 * 
	 * You can add any logic which will be performed on the generated file.<br />
	 * Examples: you may want to add some security on the generated file (
	 * {@link File#setReadOnly()}), or protect the file by a password...
	 * 
	 * @param document
	 *            the aspose document upon which the mail merge has been
	 *            performed and that has generated the file. You may need it for
	 *            some special logic but you can ignore it if you don't need it.
	 * @param generatedFile
	 *            the file which has been generated. In this method you will
	 *            have some actions on this file.
	 * @return the File that has been generated. The DocFactory will return this
	 *         File.
	 */

	default File onGeneratedFile(Document document, File generatedFile) {
		return generatedFile;
	}

}
