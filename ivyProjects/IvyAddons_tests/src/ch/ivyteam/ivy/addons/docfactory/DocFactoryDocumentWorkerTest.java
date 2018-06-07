package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.addons.docfactory.test.data.Address;
import ch.ivyteam.ivy.addons.docfactory.test.data.Insurance;
import ch.ivyteam.ivy.addons.docfactory.test.data.InsuranceBasket;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class, File.class, LicenseLoader.class})
public class DocFactoryDocumentWorkerTest {
	
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
		mockStatic(LicenseLoader.class);
		when(Ivy.log()).thenReturn(mockLogger);	
		when(Ivy.cms()).thenReturn(mockedCms);
		when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
	}
	
	@Test
	public void BaseDocFactoryNoDocumentWorker() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance();
		
		assertThat(docFactory.getDocumentWorker(), nullValue());
	}
	
	@Test
	public void BaseDocFactoryWithDocumentWorker(){
		DocumentWorker documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withDocumentWorker(documentWorker);
		
		assertThat(docFactory.getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void AsposeDocFactoryNoDocumentWorker() {
		AsposeDocFactory docFactory = new AsposeDocFactory();
		
		assertThat(docFactory.getDocumentWorker(), nullValue());
	}
	
	@Test
	public void AsposeDocFactoryWithDocumentWorker(){
		DocumentWorker documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
		AsposeDocFactory docFactory = new AsposeDocFactory().withDocumentWorker(documentWorker);
		
		assertThat(docFactory.getDocumentWorker(), is(documentWorker));
	}
	
	@Test
	public void DocFactoryWithDummyObjectAsDocumentWorkerAsposeDoesNotRecognizeIt(){
		Object o = new String("I am a document worker");
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withDocumentWorker(o);
		
		assertThat(docFactory, instanceOf(AsposeDocFactory.class));
		assertThat(docFactory.getDocumentWorker(), nullValue());
	}
	
	@Test
	public void DocumentTemplateWithDocumentWorker() throws Exception {
		java.io.File template = new java.io.File(this.getClass().getResource("template_person.docx").toURI().getPath());
		DocumentWorker documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
		
		DocumentTemplate documentTemplate = DocumentTemplate
				.withTemplate(template)
				.withDocumentWorker(documentWorker);
		
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}
	
	
	@Test
	public void produceDocumentWithDocumentWorker_for_postcreate() throws URISyntaxException {
		java.io.File template = new java.io.File(this.getClass().getResource("template_person.docx").toURI().getPath());
		DocumentWorker documentWorker = new WatermarkTextDocumentWorker("DRAFT");

		DocumentTemplate documentTemplate = DocumentTemplate
				.withTemplate(template)
				.putDataAsSourceForSimpleMailMerge(makePerson())
				.useLocale(Locale.forLanguageTag("de-CH"))
				.withDocumentWorker(documentWorker);

		FileOperationMessage result = null;
		java.io.File resultFile = new java.io.File("test/testwatermark.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}
		System.out.println("resultFile : " + resultFile.getAbsolutePath());
		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocumentWithDocumentWorker_for_prepare() throws URISyntaxException {
		java.io.File template = new java.io.File(this.getClass().getResource("template_person.docx").toURI().getPath());
		DocumentWorker documentWorker = new PageColorDocumentWorker();

		DocumentTemplate documentTemplate = DocumentTemplate
				.withTemplate(template)
				.putDataAsSourceForSimpleMailMerge(makePerson())
				.useLocale(Locale.forLanguageTag("de-CH"))
				.withDocumentWorker(documentWorker);

		FileOperationMessage result = null;
		java.io.File resultFile = new java.io.File("test/testPrepare.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}
		System.out.println("resultFile : " + resultFile.getAbsolutePath());
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

}
