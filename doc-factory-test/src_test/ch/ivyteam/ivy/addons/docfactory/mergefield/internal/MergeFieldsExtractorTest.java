package ch.ivyteam.ivy.addons.docfactory.mergefield.internal;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

import com.aspose.words.Document;

import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.addons.docfactory.aspose.MailMergeDataSourceGenerator;

public class MergeFieldsExtractorTest {

	@Test
	public void getMergeFields_returns_empty_Collection_if_bean_null() {

		Collection<TemplateMergeField> result = MergeFieldsExtractor.getMergeFields(null);

		assertTrue(result.isEmpty());
	}

	@Test
	public void getMergeFields_returns_empty_Collection_if_bean_has_no_property() {

		Collection<TemplateMergeField> result = MergeFieldsExtractor.getMergeFields(new MyEmptyBean());

		assertTrue(result.isEmpty());
	}

	@Test
	public void getMergeFields_returns_templateMergeFields_if_bean_not_empty() {
		SimplePerson person = new SimplePerson();
		person.setFirstname("Emmanuel");
		person.setName("Comba");

		Collection<TemplateMergeField> result = MergeFieldsExtractor.getMergeFields(person);
		assertThat(
				result,
				containsInAnyOrder(
						TemplateMergeField.withName("simplePerson.name").withValue("Comba"),
						TemplateMergeField.withName("simplePerson.firstname").withValue("Emmanuel")
				)
		);
	}

	@Test
	public void getMergeFields_returns_templateMergeFields_with_embedded_beans_values() {
		Person person = makePerson();
		java.io.File paySlip = new java.io.File("path/to/payslip/payslip.pdf");
		person.setPaySlip(paySlip);

		Collection<TemplateMergeField> result = MergeFieldsExtractor.getMergeFields(person);

		assertThat(
				result,
				containsInAnyOrder(
						TemplateMergeField.withName("person.name").withValue("Comba"),
						TemplateMergeField.withName("person.firstname").withValue("Emmanuel"),
						TemplateMergeField.withName("person.address.street").withValue("Wellington avenue"),
						TemplateMergeField.withName("person.address.zipCode").withValue("C5F0B5"),
						TemplateMergeField.withName("person.id").withValue(885),
						TemplateMergeField.withName("person.birthday").withValue(person.getBirthday()),
						TemplateMergeField.withName("person.address").withValue(person.getAddress()),
						TemplateMergeField.withName("person.paySlip").withValue(paySlip)
				)
		);
	}

	@Test
	public void getMergeFields_withOneCollection() {
		BeanWithCollection bean = new BeanWithCollection();
		bean.age = 10;
		bean.UID ="dgag465h215ht";
		bean.hobbies = new ArrayList<>();
		bean.hobbies.add("fishing");
		bean.hobbies.add("cooking");

		Collection<TemplateMergeField> result = MergeFieldsExtractor.getMergeFields(bean);
		assertThat(
				result,
				containsInAnyOrder(
						TemplateMergeField.withName("beanWithCollection.age").withValue(10),
						TemplateMergeField.withName("beanWithCollection.UID").withValue("dgag465h215ht"),
						TemplateMergeField.withName("beanWithCollection.hobbies").withValue(bean.hobbies)
				)
		);
		assertTrue(hasGivenNumberOfCollectionTemplateMergeFields(1, result));
	}

	@Test
	public void getMergeFields_withTwoCollection() {
		BeanWithCollection bean = new BeanWithCollection();
		bean.age = 10;
		bean.UID ="dgag465h215ht";
		bean.hobbies = new ArrayList<>();
		bean.hobbies.add("fishing");
		bean.hobbies.add("cooking");

		bean.addresses = new ArrayList<>();
		Address address = new Address();
		address.street = "Main Street 44";
		address.zipCode = "2132";
		bean.addresses.add(address);
		address = new Address();
		address.street = "Holydays Street 44";
		address.zipCode = "456";
		bean.addresses.add(address);

		Collection<TemplateMergeField> result = MergeFieldsExtractor.getMergeFields(bean);
		for(TemplateMergeField tmf: result) {
			System.out.println(tmf.getMergeFieldName());
		}
		assertThat(
				result,
				containsInAnyOrder(
						TemplateMergeField.withName("beanWithCollection.age").withValue(10),
						TemplateMergeField.withName("beanWithCollection.UID").withValue("dgag465h215ht"),
						TemplateMergeField.withName("beanWithCollection.hobbies").withValue(bean.hobbies),
						TemplateMergeField.withName("beanWithCollection.addresses").withValue(bean.addresses)
				)
		);
		assertTrue(hasGivenNumberOfCollectionTemplateMergeFields(2, result));

	}

	private boolean hasGivenNumberOfCollectionTemplateMergeFields(int expectedNumber, Collection<TemplateMergeField> result) {
		int nb = 0;
		for(TemplateMergeField tmf: result) {
			if(tmf.isCollection()) {
				MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
				nb++;
			}
		}
		return nb == expectedNumber;
	}

	@Test
	public void getMergeFields_doesNot_get_class_properties_children_fields() {
		Person person = makePerson();
		Collection<AdditionalInformation> infos = new ArrayList<>();
		infos.add(new AdditionalInformation().withDate(new Date())
				.withType(Person.class).withUsername("ec"));
		infos.add(new AdditionalInformation().withDate(new Date())
				.withType(Document.class).withUsername("test"));
		infos.add(new AdditionalInformation().withDate(new Date())
				.withType(String.class).withUsername("lt"));
		person.setPersonalInformations(infos);

		Collection<TemplateMergeField> personMergeFields = MergeFieldsExtractor.getMergeFields(person);


		Optional<TemplateMergeField> personAdditionalInformationsTemplateMergeField = personMergeFields.stream().
				filter(tm -> tm.getMergeFieldName().equals("person.personalInformations")).findFirst();

		assertTrue(personAdditionalInformationsTemplateMergeField.get().isCollection());

		Collection<TemplateMergeField> additionalTemplateTypeMergeFields =
				personAdditionalInformationsTemplateMergeField.get().getChildren().
					stream().filter(tm -> tm.getMergeFieldName().equals("additionalinformation.type")).
					collect(Collectors.toList());

		assertThat(additionalTemplateTypeMergeFields, hasSize(infos.size()));

		assertTrue(haveAllNoChildrenMergeFields(additionalTemplateTypeMergeFields));
	}

	private boolean haveAllNoChildrenMergeFields(Collection<TemplateMergeField> mergeFields) {
		return mergeFields.stream().allMatch(tm -> tm.getChildren().isEmpty());
	}

	private Person makePerson() {
		Person person = new Person();
		Calendar cal = Calendar.getInstance();
		cal.set(1972, 9, 19);
		Date birthday = cal.getTime();

		person.setFirstname("Emmanuel");
		person.setName("Comba");
		person.setId(885);
		person.setBirthday(birthday);
		Address address = new Address();
		address.setStreet("Wellington avenue");
		address.setZipCode("C5F0B5");
		person.setAddress(address);
		return person;
	}


	private class MyEmptyBean implements Serializable {
		private static final long serialVersionUID = -1788146613602936894L;

	}

	private class SimplePerson implements Serializable {
		private static final long serialVersionUID = -1788146613602936894L;
		private String name;
		private String firstname;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

	}

	public class Person extends SimplePerson {
		private static final long serialVersionUID = 1L;
		Address address;
		private long id;
		private Date birthday;
		private File paySlip;
		private Collection<AdditionalInformation> personalInformations;

		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}
		public File getPaySlip() {
			return paySlip;
		}
		public void setPaySlip(File paySlip) {
			this.paySlip = paySlip;
		}
		public Collection<AdditionalInformation> getPersonalInformations() {
			return personalInformations;
		}
		public void setPersonalInformations(
				Collection<AdditionalInformation> personalInformations) {
			this.personalInformations = personalInformations;
		}


	}

	public class Address implements Serializable {
		private static final long serialVersionUID = 1L;
		private String street;
		private String zipCode;

		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getZipCode() {
			return zipCode;
		}
		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
	}

	@SuppressWarnings("hiding")
	class AdditionalInformation {

		private Class<?> type;
		private Date date;
		private String username;

		AdditionalInformation withType(Class<?> type){
			this.type = type;
			return this;
		}

		AdditionalInformation withDate(Date date){
			this.date = date;
			return this;
		}

		AdditionalInformation withUsername(String username){
			this.username = username;
			return this;
		}

		public Class<?> getType() {
			return type;
		}

		public void setType(Class<?> type) {
			this.type = type;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}

	public class BeanWithCollection implements Serializable {
		private static final long serialVersionUID = 1L;

		private String UID;
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

	}

}
