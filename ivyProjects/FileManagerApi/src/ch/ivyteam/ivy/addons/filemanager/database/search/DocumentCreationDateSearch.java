package ch.ivyteam.ivy.addons.filemanager.database.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.Objects;

import ch.ivyteam.api.API;

public class DocumentCreationDateSearch implements Serializable {

	private static final String DOCUMENT_CREATIONDATE_FORMAT = "dd.MM.yyyy";
	private static final SimpleDateFormat DOCUMENT_SIMPLE_DATE_FORMAT = new SimpleDateFormat(DOCUMENT_CREATIONDATE_FORMAT);
	
	private static final long serialVersionUID = -6053138966320692552L;
	
	private String searchQuery;
	
	private DocumentCreationDateSearch() {}
	
	public static DocumentCreationDateSearch forRange(Date from, Date to) {
		API.checkNotNull(from, "date from");
		API.checkNotNull(to, "date to");
		if(from.compareTo(to) > 0 ) {
			throw new IllegalArgumentException("The date from cannot be greater than the date to.");
		}
		
		DocumentCreationDateSearch dds = new DocumentCreationDateSearch();
		dds.searchQuery = " creationdate >= '" + DOCUMENT_SIMPLE_DATE_FORMAT.format(from) + "' AND creationdate <= '" + DOCUMENT_SIMPLE_DATE_FORMAT.format(to) + "' ";
		return dds;
	}
	
	public static DocumentCreationDateSearch greaterOrEqualThan(Date d) {
		API.checkNotNull(d, "date");
		DocumentCreationDateSearch dds = new DocumentCreationDateSearch();
		dds.searchQuery = " creationdate >= '" + DOCUMENT_SIMPLE_DATE_FORMAT.format(d) + "' ";
		return dds;
	}
	
	public static DocumentCreationDateSearch lowerOrEqualThan(Date d) {
		API.checkNotNull(d, "date");
		DocumentCreationDateSearch dds = new DocumentCreationDateSearch();
		dds.searchQuery = " creationdate <= '" + DOCUMENT_SIMPLE_DATE_FORMAT.format(d) + "' ";
		return dds;
	}
	
	public static DocumentCreationDateSearch equalTo(Date d) {
		API.checkNotNull(d, "date");
		DocumentCreationDateSearch dds = new DocumentCreationDateSearch();
		dds.searchQuery = " creationdate = '" + DOCUMENT_SIMPLE_DATE_FORMAT.format(d) + "' ";
		return dds;
	}
	
	public static DocumentCreationDateSearch in(Month month, Year year) {
		DocumentCreationDateSearch dds = null;
		if(Objects.isNull(month) && Objects.isNull(year)) {
			return dds;
		}
		String monthCondition = month == null ? "%" : String.format("%02d", month.getValue());
		String yearCondition = year == null ? "%" : year.toString();
		dds = new DocumentCreationDateSearch();
		dds.searchQuery = " creationdate LIKE '%." + monthCondition + "." + yearCondition + "' "; 
		return dds;
	}
	
	public String getQuery() {
		return this.searchQuery;
	}
}
