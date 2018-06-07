package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.test.data.Address;
import ch.ivyteam.ivy.addons.docfactory.test.data.Insurance;
import ch.ivyteam.ivy.addons.docfactory.test.data.InsuranceBasket;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

import com.aspose.words.DataRow;
import com.aspose.words.DataTable;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class})
public class DocumentTemplateTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws Exception {
		Logger mockLogger = mock(Logger.class);
		doNothing().when(mockLogger).error(any(String.class));
		doNothing().when(mockLogger).info(any(String.class));
		doNothing().when(mockLogger).debug(any(String.class));

		IContentManagementSystem mockedCms = mock(IContentManagementSystem.class);
		when(mockedCms.co(any(String.class))).thenReturn("");

		mockStatic(ThirdPartyLicenses.class);
		mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mockLogger);
		when(Ivy.cms()).thenReturn(mockedCms);
		when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
	}

	@Test
	public void withTemplate_null_throws_IAE() {
		thrown.expect(IllegalArgumentException.class);
		DocumentTemplate.withTemplate(null);
	}

	@Test
	public void withTemplate_returns_DocumentTemplate_if_template_not_null() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(new File("path/to/template.doc"));

		assertNotNull(documentTemplate);
	}

	@Test
	public void putDataAsSourceForMailMerge_throws_IAE_if_data_null() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(new File("path/to/template.doc"));

		thrown.expect(IllegalArgumentException.class);
		documentTemplate.putDataAsSourceForMailMerge(null);
	}

	@Test
	public void putDataAsSourceForMailMerge_with_data_fills_mergeFields() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(new File("path/to/template.doc"));

		documentTemplate.putDataAsSourceForSimpleMailMerge(makePerson());
		for(TemplateMergeField tmf: documentTemplate.getMergeFields()) {
			System.out.println(tmf.getMergeFieldName() + " " + tmf.getMergeFieldValue());
		}
		assertThat(documentTemplate.getMergeFields(), hasSize(11));
	}

	@Test
	public void produceDocument_with_serializable_produces_document() throws URISyntaxException {
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH"));

		FileOperationMessage result = null;
		File resultFile = new File("test/test.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_with_some_HTML_formatted_text() throws URISyntaxException {
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePersonWithHTML()).
				useLocale(Locale.forLanguageTag("de-CH"));

		FileOperationMessage result = null;
		File resultFile = new File("test/testHtml.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_with_some_CSS_formatted_text() throws URISyntaxException {
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePersonWithHTMLFormattedInCssFile()).
				useLocale(Locale.forLanguageTag("de-CH"));

		FileOperationMessage result = null;
		File resultFile = new File("test/testHtmlCSS.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_file_to_insert() throws URISyntaxException {
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());
		File otherDocumentToInject = new File(this.getClass().getResource("another_doc.doc").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				addMergeField("document_person.paySlip", otherDocumentToInject).
				useLocale(Locale.GERMAN);

		FileOperationMessage result = null;
		File resultFile = new File("test/test2.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_DataTable() throws Exception {
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				putTableDataForMergingATableInDocument(makeDataTable()).
				useLocale(Locale.GERMAN);

		FileOperationMessage result = null;
		File resultFile = new File("test/test3.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_with_DataTable_as_object_ignored() throws Exception {
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				putTableDataForMergingATableInDocument(Person.emptyPerson()).
				useLocale(Locale.GERMAN);

		FileOperationMessage result = null;
		File resultFile = new File("test/test4.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}


	private Person makePerson() {
		return Person.withNameFirstname("Comba", "Emmanuel")
				.withAddress(Address.withStreetZipCodeCityCountry("Muristrasse 4", "3005", "Bern", "CH"))
				.withBirthday(Calendar.getInstance().getTime())
				.withInsuranceBasket(
						InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
						.putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
						.putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
				.withId(new BigDecimal(213546));
	}
	
	private Person makePersonWithHTML() {
		String htmlStreet = "Hey dude! This is my address:<br>"
				+ "<b>My Street in bold</b>"
				+ "<br /><font color='blue'>I am blue</font>"
				+ "<br /><i><font color='red'>I am red and italic</font></i>"
				+ "<br /><div style='color:green; font-family:courier;'>I am green and courier</div>"
				+ "<a href='www.axonivy.com'>Come and visit us!</a><br />"
				+ "<br /><ul><b>And a list:</b>"
				+ "<li>one"
				+ "<li>two"
				+ "<li>three"
				+ "</ul>";
		return Person.withNameFirstname("Comba", "Emmanuel")
				.withAddress(Address.withStreetZipCodeCityCountry(htmlStreet, "3005", "Bern", "CH"))
				.withBirthday(Calendar.getInstance().getTime())
				.withInsuranceBasket(
						InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
						.putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
						.putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
				.withId(new BigDecimal(213546));
	}
	
	private Person makePersonWithHTMLFormattedInCssFile() {
		String htmlStreet = 
				"<html>"
				+ "<head>"
				+ "<link rel='stylesheet' href='styles.css'>"
				+ "</head>"
				+ "<body>"
				+ "<h1>The style is defined in a css file.</h1>"
				+ "<div>This is a div with a background and a border.</div>"
				+ "</body>"
				+ "</html>";
		return Person.withNameFirstname("Comba", "Emmanuel")
				.withAddress(Address.withStreetZipCodeCityCountry(htmlStreet, "3005", "Bern", "CH"))
				.withBirthday(Calendar.getInstance().getTime())
				.withInsuranceBasket(
						InsuranceBasket.withInsurance(Insurance.withName("AXA").withContractNumber("sdfh735"))
						.putInsurance(Insurance.withName("GENERALI").withContractNumber("fsghdg6666"))
						.putInsurance(Insurance.withName("AB").withContractNumber("23445656")))
				.withId(new BigDecimal(213546));
	}

	private DataTable makeDataTable() {
		//The name of the DataTable must be the same as the name of the merge field region (TableStart:itemPrices)
		com.aspose.words.DataTable data = new DataTable("itemPrices");
		// add the columns which names are the same as the merge fields in the data table region
		data.getColumns().add("Item", String.class);
		data.getColumns().add("Price", Number.class);
		data.getColumns().add("Currency", String.class);
		// add the rows
		DataRow dr = data.newRow();
		dr.set("Item", "T-Shirt");
		dr.set("Price", 22.56);
		dr.set("Currency", "$");
		data.getRows().add(dr);
		dr = data.newRow();
		dr.set("Item", "Porsche");
		dr.set("Price", 435345);
		dr.set("Currency", "CHF");
		data.getRows().add(dr);
		dr = data.newRow();
		dr.set("Item", "EM-Ticket");
		dr.set("Price", 45);
		dr.set("Currency", "CHF");
		data.getRows().add(dr);
		dr = data.newRow();
		dr.set("Item", "Super PC");
		dr.set("Price", 1500.00);
		dr.set("Currency", "Euro");
		data.getRows().add(dr);
		
		return data;
	}

}
