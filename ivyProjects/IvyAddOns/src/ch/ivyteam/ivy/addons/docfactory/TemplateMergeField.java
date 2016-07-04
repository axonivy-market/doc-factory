/**
 * 
 */
package ch.ivyteam.ivy.addons.docfactory;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.addons.docfactory.mergefield.TemplateMergeFieldType;
import ch.ivyteam.ivy.addons.docfactory.mergefield.internal.MergeFieldsExtractor;


/**
 * This Class represents a MergeField in a document Template<br>
 * like in a Microsoft Office dot document.
 * @author ec<br>
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
	
	private Collection<TemplateMergeField> children = new ArrayList<>();
	
	private Collection<TemplateMergeField> nestedCollections = new ArrayList<>();
	
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
	
	/**
	 * Creates a new TemplateMergeField with the given name. The Name will be used in the mail merging operation for filling the corresponding mail merge field in the template.
	 * @param mergeFieldName the name. Cannot be blank.
	 * @return the TemplateMergeField with the name set.
	 */
	public static TemplateMergeField withName(String mergeFieldName) {
		if(StringUtils.isBlank(mergeFieldName)) {
			throw new IllegalArgumentException("The mergeFieldName cannot be blank");
		}
		return new TemplateMergeField(mergeFieldName, "");
	}
	
	/**
	 * Set the Type {@link TemplateMergeFieldType} and the value of this TemplateMergeField.<br>
	 * If you don't want to care about setting the type yourself, use {@link TemplateMergeField#withValue(Object)}.
	 * @param type the TemplateMergeFieldType reflecting the type of this field value.
	 * @param value the value of the field. 
	 * @return
	 */
	public TemplateMergeField withValueAs(TemplateMergeFieldType type, Object value) {
		setValue(value);
		this.type = type;
		makeChildren();
		return this;
	}
	
	/**
	 * Set the value of the TemplateMergeField and return it.<br>
	 * The {@link TemplateMergeFieldType} will be automatically computed.
	 * 
	 * @param value the value to set, cannot be null.
	 * @return
	 */
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
		if(value == null) {
			return;
		}
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
		} else if (value instanceof Collection || value instanceof Map<?,?>) {
			this.type = TemplateMergeFieldType.COLLECTION;
			makeChildren();
		} else {
			this.type = TemplateMergeFieldType.OBJECT;
			makeChildren();
		}
	}
	
	/**
	 * gets the type of this TemplateMergeField. <br>
	 * @see {@link TemplateMergeFieldType}
	 * @return the TemplateMergeFieldType of this field
	 */
	public TemplateMergeFieldType getType() {
		return this.type;
	}
	
	/**
	 * Checks if this TemplateMergeField is a Collection Type (List, Set, Map ....)
	 * @return true if the TemplateMergeField value is from type {@link TemplateMergeFieldType#COLLECTION}
	 */
	public boolean isCollection() {
		return this.type.is(TemplateMergeFieldType.COLLECTION);
	}
	
	public boolean hasCollection() {
		return !getNestedCollections().isEmpty();
	}
	
	public boolean hasChildren() {
		return !getChildren().isEmpty();
	}
	
	public Collection<TemplateMergeField> getChildren() {
		return this.children;
	}
	
	public Collection<TemplateMergeField> getNestedCollections() {
		return this.nestedCollections;
	}
	
	private void makeChildren() {
		this.children.clear();
		this.nestedCollections.clear();
		if(this.type.is(TemplateMergeFieldType.OBJECT)) {
			this.children.addAll(MergeFieldsExtractor.getChildrenMergeFieldsForObjectMergeField(this));
			for(TemplateMergeField tmf : this.children) {
				if(tmf.isCollection()) {
					this.nestedCollections.add(tmf);
				}
			}
		}
		if(this.type.is(TemplateMergeFieldType.COLLECTION)) {
			/*Collection<?> col = null;
			if(this.getValue() instanceof Map<?, ?>) {
				col = ((Map<?, ?>) this.getValue()).values();
			} else {
				col = (Collection<?>) this.getValue();
			}*/
			for(Collection<TemplateMergeField> result : MergeFieldsExtractor.getMergeFieldsForCollectionTemplateMergeField(this.getMergeFieldName(), this)) {
				this.children.addAll(result);
			}
		}
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
			if(this.type.is(TemplateMergeFieldType.DATE)) {
				return this.dateFormat.format(this.value);
			}
			if(this.type.is(TemplateMergeFieldType.NUMBER)) {
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
		if(this.type.is(TemplateMergeFieldType.BYTES) || this.type.is(TemplateMergeFieldType.FILE) || 
				this.type.is(TemplateMergeFieldType.OBJECT) || this.type.is(TemplateMergeFieldType.COLLECTION)) {
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
	
	public Locale getLocale() {
		return this.locale;
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
		} else if (type.is(TemplateMergeFieldType.NUMBER)) {
			result =  String.valueOf(value).equals(String.valueOf(other.value));
		} else if (!value.equals(other.value)) {
			result =  false;
		}
		
		return result;
	}
	
	private boolean shouldCompareDateAndNumber(TemplateMergeFieldType otherType) {
		return (type.is(TemplateMergeFieldType.DATE) && otherType.is(TemplateMergeFieldType.NUMBER)) 
				|| 
				(type.is(TemplateMergeFieldType.NUMBER) && otherType.is(TemplateMergeFieldType.DATE));
	}
	
	private boolean areNumberAndDateSame(TemplateMergeField other) {
		if(type.is(TemplateMergeFieldType.NUMBER) && other.type.is(TemplateMergeFieldType.DATE)) {
			return new Date((long) this.value).equals(other.value);
		}
		return new Date((long) other.value).equals(this.value);
	}

	@Override
	public String toString() {
		return this.mergeFieldName + " " + this.value + " " + this.type;
	}
	
	

}
