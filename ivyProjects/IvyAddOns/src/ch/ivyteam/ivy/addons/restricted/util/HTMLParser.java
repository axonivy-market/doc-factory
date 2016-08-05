package ch.ivyteam.ivy.addons.restricted.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * For internal use only. NOT PUBLIC API
 * @see http://ideone.com/HakdHo
 *
 */
public class HTMLParser {
	
	
	// adapted from post by Phil Haack and modified to match better
    private final static String TAG_START = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
    private final static String TAG_END = "\\</\\w+\\>";
    private final static String TAG_SELF_CLOSING = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
    private final static String HTML_ENTITY = "&[a-zA-Z][a-zA-Z0-9]+;";
    private final static Pattern HTML_PATTERN = Pattern.compile(
      "("+TAG_START+".*"+TAG_END+")|("+TAG_SELF_CLOSING+")|("+HTML_ENTITY+")",
      Pattern.DOTALL
    );
	
	private HTMLParser() {}
	
	/**
     * Will return true if s contains HTML markup tags or entities.
     *
     * @param s String to test
     * @return true if string contains HTML
     */
	public static boolean isHTML(String stringtoCheck) {
		if(StringUtils.isBlank(stringtoCheck)) {
			return false;
		}
		Matcher matcher = HTML_PATTERN.matcher(stringtoCheck);
		return matcher.find();
	}

}
