package ch.ivyteam.ivy.addons.docfactory.mergefield;

/**
 * Enum of the TemplateMergeField types
 * @author ec
 *
 */
public enum TemplateMergeFieldType {
	
	TEXT, FILE, BYTES, OBJECT, DATE, NUMBER, COLLECTION;

	public boolean is(TemplateMergeFieldType type) {
		return this.equals(type);
	}

}
