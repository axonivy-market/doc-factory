package ch.ivyteam.ivy.addons.filemanager.util;

public class MethodArgumentsChecker {
	
	private static String DEFAULT_IAE_MESSAGE = "An IllegalArgumentException has been thrown.";
	
	/**
	 * Throws an illegalArgumentException if one of the String parameter is null or empty.<br>
	 * If no String parameter has been provided, the method returns.
	 * @param message a message for the IllegalArgumentException, if null a default one is provided.
	 * @param args an array of String arguments to be checked.
	 * @throws IllegalArgumentException
	 */
	public static void throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty(String message, String ... args)
			throws IllegalArgumentException{
		if(args==null) {
			return;
		}
		for(String s: args) {
			if(s==null || s.trim().isEmpty()) {
				throw new IllegalArgumentException(message==null?DEFAULT_IAE_MESSAGE:message);
			}
		}
	}
	
	/**
	 * Throws an illegalArgumentException if one of the parameter is null.<br>
	 * If no parameter has been provided, the method returns.
	 * @param message a message for the IllegalArgumentException, if null a default one is provided.
	 * @param args an array of arguments to be checked.
	 * @throws IllegalArgumentException
	 */
	public static void throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull(String message, Object ... args)
			throws IllegalArgumentException{
		if(args==null) {
			return;
		}
		for(Object o: args) {
			if(o==null) {
				throw new IllegalArgumentException(message==null?DEFAULT_IAE_MESSAGE:message);
			}
		}
	}

}
