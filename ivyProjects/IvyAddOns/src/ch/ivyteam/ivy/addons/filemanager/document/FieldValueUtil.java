package ch.ivyteam.ivy.addons.filemanager.document;

public class FieldValueUtil {

	/**
	 * returns the long value of the given String id.<br>
	 * If the id is null, empty or NaN then -1 is returned.
	 * 
	 * @param id the id as String.
	 * @return the long value of the given String id.
	 */
	public static long getLongValueForStringId(String id) {
		if(id == null || id.trim().isEmpty()) {
			return -1;
		}
		try {
			return Long.parseLong(id);
		} catch(NumberFormatException ex) {
			return -1;
		}
	}
	
	/**
	 * Returns true if the given String id represents a number greater than zero.
	 * returns false if this String is null, empty, NaN or represents a negative or zero number.
	 * @param id
	 * @return
	 */
	public static boolean isStringValidLongId(String id) {
		return getLongValueForStringId(id) > 0;
	}

	/**
	 * returns true if the given String is null or empty.
	 * @param s the String to check.
	 * @return true if the given String is null or empty. Else false.
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

}
