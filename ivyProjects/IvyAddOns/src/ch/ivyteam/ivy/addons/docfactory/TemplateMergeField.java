/**
 * 
 */
package ch.ivyteam.ivy.addons.docfactory;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.addons.docfactory.mergefield.TemplateMergeFieldType;


/**
 * This Class represents a MergeField in a document Template<br>
 * like in a Microsoft Office dot document.
 * @author ec<br>
 * @param <T> this Type has been added in June 2016 for specifying the type of the TemplateMergeField.
 * @since 27.10.2009<br>
 * @see TemplateMergeFieldType ch.ivyteam.ivy.addons.docfactory.TemplateMergeFieldType
 */
public class TemplateMergeField {
	
	/** The MergeField Name*/
	private String mergeFieldName="";
	/** The MergeField value that should be inserted into the Merge field*/
	private Object value;
	
	private TemplateMergeFieldType type = TemplateMergeFieldType.TEXT;
	
	private Locale locale = DocFactoryConstants.DEFAULT_LOCALE;
	
	private DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.YEAR_FIELD, locale);
	
	private NumberFormat numberFormat = NumberFormat.getInstance(locale);
	
	/**
	 * Constructor with two parameters : the mergeFieldName and its value.
	 * @param _mergeFieldName String
	 * @param _mergeFieldValue String
	 */
	public TemplateMergeField (String _mergeFieldName, Object _mergeFieldValue) {
		this.mergeFieldName = _mergeFieldName;
		this.value = _mergeFieldValue;
		setType();
		
	}
	
	public static TemplateMergeField withName(String mergeFieldName) {
		if(StringUtils.isBlank(mergeFieldName)) {
			throw new IllegalArgumentException("The mergeFieldName cannot be blank");
		}
		return new TemplateMergeField(mergeFieldName, "");
	}
	
	/**
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public TemplateMergeField withValueAs(TemplateMergeFieldType type, Object value) {
		setValue(value);
		this.type = type;
		return this;
	}
	
	public TemplateMergeField withValue(Object value) {
		setValue(value);
		setType();
		return this;
	}
	
	public TemplateMergeField useLocaleAndResetNumberFormatAndDateFormat(Locale locale) {
		if(locale != null) {
			this.locale = locale;
			this.dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, locale);
			this.numberFormat = NumberFormat.getInstance(locale);
		}
		return this;
	}
	
	public TemplateMergeField useDateFormat(DateFormat dateFormat) {
		if(dateFormat != null) {
			this.dateFormat = dateFormat;
		}
		return this;
	}
	
	public TemplateMergeField useNumberFormat(NumberFormat numberFormat) {
		if(numberFormat != null) {
			this.numberFormat = numberFormat;
		}
		return this;
	}

	private void setValue(Object value) {
		if(value == null) {
			throw new IllegalArgumentException("The value of a merge field cannot be null");
		}
		this.value = value;
	}

	private void setType() {
		if(value instanceof String) {
			this.type = TemplateMergeFieldType.TEXT;
		} else if (value instanceof byte[]) {
			this.type = TemplateMergeFieldType.BYTES;
		} else if (value instanceof java.io.File) {
			this.type = TemplateMergeFieldType.FILE;
		} else if (value instanceof Number) {
			this.type = TemplateMergeFieldType.NUMBER;
		} else if (value instanceof Date || value instanceof java.sql.Date) {
			this.type = TemplateMergeFieldType.DATE;
		} else {
			this.type = TemplateMergeFieldType.OBJECT;
		}
	}
	
	public TemplateMergeFieldType getType() {
		return this.type;
	}
	
	/**
	 * Get the Merge field name
	 * @return the mergeFieldName
	 */
	public String getMergeFieldName() {
		return mergeFieldName;
	}
	
	/**
	 * set the Merge field name.
	 * @param mergeFieldName the mergeFieldName to set as String
	 */
	public void setMergeFieldName(String mergeFieldName) {
		this.mergeFieldName = mergeFieldName;
	}
	
	/**
	 * Get the merge field value.
	 * @return the mergeField Value as String. 
	 * If the value is null, it returns an empty String.
	 * In case of Date or Number type, it returns the formatted String of the Date or Number.
	 * If the value is an object it uses the object toString() method.
	 */
	public String getMergeFieldValue() {
		if(this.value == null) {
			return "";
		}
		try {
			if(this.type.equals(TemplateMergeFieldType.DATE)) {
				return this.dateFormat.format(this.value);
			}
			if(this.type.equals(TemplateMergeFieldType.NUMBER)) {
				return this.numberFormat.format(value);
			}
		} catch (IllegalArgumentException e) {
			
		}
		return this.value.toString();
	}
	
	/**
	 * returns the value to be used for mail merging.
	 * In case of Date or Number type, it returns the formatted String of the Date or Number.
	 * Else it returns the original Object.
	 * @return
	 */
	public Object getValueForMailMerging() {
		if(this.type.equals(TemplateMergeFieldType.BYTES) || this.type.equals(TemplateMergeFieldType.FILE) || this.type.equals(TemplateMergeFieldType.OBJECT)) {
			return this.value;
		}
		return getMergeFieldValue();
	}
	
	/**
	 * Get the Value stored
	 * @return
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Set the merge field Value
	 * @param mergeFieldValue the mergeFieldValue to set as string
	 */
	public void setMergeFieldValue(String mergeFieldValue) {
		this.value = mergeFieldValue;
		this.type = TemplateMergeFieldType.TEXT;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mergeFieldName == null) ? 0 : mergeFieldName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		boolean result = true;
		TemplateMergeField other = (TemplateMergeField) obj;
		if (this.mergeFieldName == null) {
			if (other.mergeFieldName != null)
				return false;
		} else if (!this.mergeFieldName.equals(other.mergeFieldName)) {
			return false;
		}
		
		if (type != other.type) {
			if(shouldCompareDateAndNumber(other.type)) {
				return areNumberAndDateSame(other);
			}
			return false;
		}
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (type.equals(TemplateMergeFieldType.NUMBER)) {
			result =  String.valueOf(value).equals(String.valueOf(other.value));
		} else if (!value.equals(other.value)) {
			result =  false;
		}
		
		return result;
	}
	
	private boolean shouldCompareDateAndNumber(TemplateMergeFieldType otherType) {
		return (type.equals(TemplateMergeFieldType.DATE) && otherType.equals(TemplateMergeFieldType.NUMBER)) 
				|| 
				(type.equals(TemplateMergeFieldType.NUMBER) && otherType.equals(TemplateMergeFieldType.DATE));
	}
	
	private boolean areNumberAndDateSame(TemplateMergeField other) {
		if(type.equals(TemplateMergeFieldType.NUMBER) && other.type.equals(TemplateMergeFieldType.DATE)) {
			return new Date((long) this.value).equals(other.value);
		}
		return new Date((long) other.value).equals(this.value);
	}

	@Override
	public String toString() {
		return this.mergeFieldName + " " + this.value + " " + this.type;
	}
	
	

}
