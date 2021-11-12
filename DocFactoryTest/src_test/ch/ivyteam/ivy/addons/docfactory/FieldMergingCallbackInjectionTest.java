package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.Paragraph;

public class FieldMergingCallbackInjectionTest extends DocFactoryTest {
	
	
	@Test
	public void inject_fieldMergingCallback_test() throws Exception {
		File template = new File(this.getClass().getResource(TEMPLATE_WITH_FIELDS_FORM_DOCX).toURI().getPath());
		
		//prepare the data that will be injected in the merge-fields
		Person person = Person
				.withNameFirstname("Comba", null) // The first name gets a null value
				.withBirthday(Calendar.getInstance().getTime());
		
		//We instantiate the docFactory 
		BaseDocFactory docFactory = BaseDocFactory.getInstance()
				.withFieldMergingCallBack(new MyFieldMergingCallback()); // and inject our FieldMergingCallback
		
		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(person).
				useLocale(Locale.forLanguageTag("de-CH")).
				withDocFactory(docFactory); //we use our docFactory instance containing the adapted FieldMergingCallback

		File resultFile = makeResultFile();
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);
		
		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	private File makeResultFile() {
		File resultFile = new File("test/test_firstname_null.pdf");
		if(resultFile.isFile()) {
			resultFile.delete();
		}
		return resultFile;
	}
	
	protected class MyFieldMergingCallback extends AsposeFieldMergingCallback {

		@Override
		public void fieldMerging(FieldMergingArgs e) throws Exception {
			if(e.getFieldValue() == null) {
				removeBlankLine(e); // here we add our logic for dealing with null values
			}
			super.fieldMerging(e);
		}
		
		private void removeBlankLine(FieldMergingArgs args) throws Exception {
			DocumentBuilder builder = new DocumentBuilder(args.getDocument());
			builder.moveToMergeField(args.getFieldName());
			Paragraph paragraph = builder.getCurrentParagraph();
			if (StringUtils.isBlank(paragraph.getText())) {
				paragraph.remove();
			}
		}
		
	}

}
