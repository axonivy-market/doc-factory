/**
 * 
 */
package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.image.ImageDimensionCalculatorFactory;
import ch.ivyteam.ivy.environment.Ivy;

import com.aspose.words.CompositeNode;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.MergeFieldImageDimension;
import com.aspose.words.Node;
import com.aspose.words.NodeImporter;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Section;

/**
 * This class was developed to be able to handle merge fields that should be filled up with images.<br> 
 * This is used in the AsposeDocFactory class to insert the image when the mail merge engine encounters some fields named as "Image: + name of
 * imageMailField".<br>
 * In such a case the merge-field-value corresponding to this field can be a String (path of the image file) or the image content as ByteArray.<br><br>
 * 
 * You can extend this class and provide your own implementation if your mail
 * merging has to behave dependently of the merge field's name or value.<br>
 * An example can be found at {@link http://www.aspose.com/community/forums/thread/380671/html-text-with-merge-field.aspx} <br><br>
 * Two methods can be overwritten:<br>
 * {@code public void fieldMerging(FieldMergingArgs arg0) throws Exception }, see the example linked above, the AsposeFieldMergingCallback class provides an empty implementation for this method,<br>
 * {@code public void imageFieldMerging(ImageFieldMergingArgs e) throws Exception }, overwrites it only in the case you need some adaptation in the way the images fields are handled. <br><br>
 * Use the {@code AsposeDocFactory.setAsposeFieldMergingCallback(AsposeFieldMergingCallback fieldMergingCallback)} method to pass your own AsposeFieldMergingCallback to the docFactory.
 */
public class AsposeFieldMergingCallback implements IFieldMergingCallback {

	@Override
	public void fieldMerging(FieldMergingArgs e) throws Exception { 
		if(e.getFieldValue() == null) {
			return;
		}
		if(e.getFieldName().toLowerCase().startsWith(DocFactoryConstants.EMBEDDED_DOCUMENT_MERGEFIELD_NAME_START)) {
			handleDocumentInsertion(e);
		}

	}

	@Override
	public void imageFieldMerging(ImageFieldMergingArgs e) throws Exception {
		InputStream imageStream = null;
		InputStream imageStream_dimension = null;
		//e.getDocument().getMailMerge().getFieldNames()
		if(e.getFieldValue() != null) {
			// The field value is a byte array, just cast it and create a stream on it.
			if(e.getFieldValue().getClass().getComponentType()!= null && 
					e.getClass().getComponentType().getName().equalsIgnoreCase("java.lang.Byte")) {
				imageStream = new ByteArrayInputStream((byte[]) e.getFieldValue());
				imageStream_dimension = new ByteArrayInputStream((byte[]) e.getFieldValue());
			} else if (e.getFieldValue().getClass().getName().equalsIgnoreCase("java.lang.String")) {
				try {
					java.io.File image = new java.io.File((String) e.getFieldValue());
					Ivy.log().info("Mergefield image file {0}", image.isFile());
					imageStream = new FileInputStream(image);
					imageStream_dimension = new FileInputStream(image);
				} catch (FileNotFoundException ex) {
					Ivy.log().error("FileNotFoundException occurred while getting an imageStream for mail merging.",ex);
					return;
				}
			} else if(e.getFieldValue().getClass().getName().equalsIgnoreCase("java.io.File")) {
				try {
					imageStream = new FileInputStream((java.io.File) e.getFieldValue());
					imageStream_dimension = new FileInputStream((java.io.File) e.getFieldValue());
				} catch (FileNotFoundException ex) {
					Ivy.log().error("FileNotFoundException occurred while getting an imageStream for mail merging.",ex);
					return;
				}
			}
			if(imageStream == null) {
				return;
			}

			Dimension dim = ImageDimensionCalculatorFactory.getInstance().calculateImageDimensionForMergingInTemplate(imageStream_dimension, e.getFieldName());
			if(dim.width > 0) {
				e.setImageHeight(new MergeFieldImageDimension(dim.getHeight()));
				e.setImageWidth(new MergeFieldImageDimension(dim.getWidth()));
			}
			e.setImageStream(imageStream);
		}
	}

	private void handleDocumentInsertion(FieldMergingArgs e) throws Exception,
	IOException {
		// Use document builder to navigate to the merge field with the specified name.
		DocumentBuilder builder = new DocumentBuilder(e.getDocument());

		builder.moveToMergeField(e.getDocumentFieldName());
		InputStream inStream = null;
		if(e.getFieldValue() instanceof File) {
			File f = (File) e.getFieldValue();
			Path p = Paths.get(f.toURI());
			inStream = new ByteArrayInputStream(Files.readAllBytes(p));
		} else if (e.getFieldValue() instanceof byte[]) {
			inStream = new ByteArrayInputStream((byte[]) e.getFieldValue());
		} else {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bytesOut);

			try {
				oos.writeObject(e.getFieldValue());
			} catch (Exception e1) {
				return;
			}
			oos.flush();
			byte[] bytes = bytesOut.toByteArray();
			inStream = new ByteArrayInputStream(bytes);
		}
		Document subDoc = null;
		try {
			subDoc = new Document(inStream);
		} catch (Exception ex) {
			Ivy.log().error("An Exception occurred while creating the sub document to inject with the InputStream. " + ex.getMessage(), ex);
			return;
		}
		inStream.close();

		// Insert the document.
		insertDocument(builder.getCurrentParagraph(), subDoc);

		// The paragraph that contained the merge field might be empty now and you probably want to delete it.
		if (!builder.getCurrentParagraph().hasChildNodes()) {
			builder.getCurrentParagraph().remove();
		}

		// Indicate to the mail merge engine that we have inserted what we wanted.
		e.setText(null);
	}


	private void insertDocument(Node insertAfterNode, Document documentToInsert) throws Exception {

		if ((insertAfterNode.getNodeType() != NodeType.PARAGRAPH) &
				(insertAfterNode.getNodeType() != NodeType.TABLE))
			throw new IllegalArgumentException("The destination node should be either a paragraph or table.");

		// We will be inserting into the parent of the destination paragraph.
		CompositeNode<?> dstStory = insertAfterNode.getParentNode();

		// This object will be translating styles and lists during the import.
		NodeImporter importer = new NodeImporter(documentToInsert, insertAfterNode.getDocument(), ImportFormatMode.KEEP_SOURCE_FORMATTING);

		// Loop through all sections in the source document.
		for (Section srcSection : documentToInsert.getSections())
		{
			// Loop through all block level nodes (paragraphs and tables) in the body of the section.
			for (Node srcNode : (Iterable<Node>) srcSection.getBody())
			{
				// Let's skip the node if it is a last empty paragraph in a section.
				if (srcNode.getNodeType() == (NodeType.PARAGRAPH))
				{
					Paragraph para = (Paragraph)srcNode;
					if (para.isEndOfSection() && !para.hasChildNodes())
						continue;
				}

				// This creates a clone of the node, suitable for insertion into the destination document.
				Node newNode = importer.importNode(srcNode, true);

				// Insert new node after the reference node.
				dstStory.insertAfter(newNode, insertAfterNode);
				insertAfterNode = newNode;
			}
		}
	}

}
