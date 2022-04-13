package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("hiding")
public class Person implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String firstname;
	private Address address;
	private Date birthday;
	private BigDecimal id;
	private InsuranceBasket insuranceBasket;
	private boolean acceptToBeContacted;

	private Person() { }

	public static Person withNameFirstname(String name, String firstname) {
		Person p = new Person();
		p.setName(name);
		p.setFirstname(firstname);
		return p;
	}

	public static Person emptyPerson() {
		return new Person();
	}

	public Person withAddress(Address address) {
		this.setAddress(address);
		return this;
	}

	public Person withInsuranceBasket(InsuranceBasket insuranceBasket) {
		this.setInsuranceBasket(insuranceBasket);
		return this;
	}

	public Person withBirthday(Date birthday) {
		this.setBirthday(birthday);
		return this;
	}

	public Person withId(BigDecimal id) {
		this.setId(id);
		return this;
	}

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public InsuranceBasket getInsuranceBasket() {
		return insuranceBasket;
	}

	public void setInsuranceBasket(InsuranceBasket insuranceBasket) {
		this.insuranceBasket = insuranceBasket;
	}

	public boolean isAcceptToBeContacted() {
		return acceptToBeContacted;
	}

	public void setAcceptToBeContacted(boolean acceptToBeContacted) {
		this.acceptToBeContacted = acceptToBeContacted;
	}

	public Person acceptToBeContacted(boolean b) {
		this.setAcceptToBeContacted(b);
		return this;
	}
}