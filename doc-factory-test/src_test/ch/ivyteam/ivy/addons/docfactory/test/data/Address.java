package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;

public class Address implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Street;
	private String zipCode;
	private String city;
	private String country;

	private Address() {}
	
	public static Address withStreetZipCodeCityCountry(String street, String zipCode, String city, String country) {
		Address add = new Address();
		add.setCity(city);
		add.setCountry(country);
		add.setStreet(street);
		add.setZipCode(zipCode);
		return add;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}