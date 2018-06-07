package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class ProduceDocumentWithSimpleReportTest extends DocFactoryTest {
	
	@Test
	public void produceDocument_with_simple_tables() throws Exception {
		File template = new File(this.getClass().getResource("template_with_simple_tables.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForMailMerge(makeBeanWithCollection()).
				useLocale(Locale.forLanguageTag("de-CH"));
		
		FileOperationMessage result = null;
		File resultFile = new File("test/simpleTables.pdf");
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
	
	private BeanWithCollection makeBeanWithCollection() {
		BeanWithCollection bean = new BeanWithCollection();
		
		bean.age = 10;
		bean.UID = "ADBD-I561F-AS52-32ASF";
		bean.name = "Johnson";
		bean.firstName = "Michael";
		bean.date = Calendar.getInstance().getTime();
		
		bean.hobbies = new ArrayList<>();
		bean.hobbies.add("fishing");
		bean.hobbies.add("cooking");
		bean.hobbies.add("reading");
		
		bean.addresses = new ArrayList<>();
		Address address = new Address();
		address.street = "Bahnhofstrasse 44";
		address.city = new City("Bern", "3000", "CH");
		bean.addresses.add(address);
		address = new Address();
		address.street = "Holydays Street 47";
		address.city = new City("Miami", "J3B6G4", "US");
		bean.addresses.add(address);
		
		return bean;
	}

	class Address implements Serializable {
		private static final long serialVersionUID = 1L;
		private String street;
		private City city;
		
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public City getCity() {
			return city;
		}
		public void setCity(City city) {
			this.city = city;
		}
		
	}
	
	class City implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String zipCode;
		private String country;
		public City(String name, String zip, String country) {
			setName(name);
			setZipCode(zip);
			setCountry(country);
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getZipCode() {
			return zipCode;
		}
		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		
	}
	
	class BeanWithCollection implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String UID;
		private String name = "Pan";
		private String firstName = "Peter";
		private Date date = Calendar.getInstance().getTime();
		private int age;
		private List<String> hobbies;
		private List<Address> addresses;
		
		public String getUID() {
			return UID;
		}
		public void setUID(String uID) {
			UID = uID;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public List<String> getHobbies() {
			return hobbies;
		}
		public void setHobbies(List<String> hobbies) {
			this.hobbies = hobbies;
		}
		public List<Address> getAddresses() {
			return addresses;
		}
		public void setAddresses(List<Address> addresses) {
			this.addresses = addresses;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		
	}

}
