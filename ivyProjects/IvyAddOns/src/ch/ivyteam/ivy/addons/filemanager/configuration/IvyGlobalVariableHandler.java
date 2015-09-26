package ch.ivyteam.ivy.addons.filemanager.configuration;

import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.environment.Ivy;

public final class IvyGlobalVariableHandler {
	
	private IvyGlobalVariableHandler() {}
	
	/**
	 * If the proposedValue is blank return the given global variable value. Else the proposedValue.
	 * @param proposedValue the value to check
	 * @param globalVariableName the name of the gloabl variable which content is the default value. Cannot be null or empty.
	 * @return
	 */
	public static String returnsGlobalVariableValueIfProposedValueIsBlank(String proposedValue, String globalVariableName) {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The global variable name cannot be null or empty.", globalVariableName);
		
		if(proposedValue == null || proposedValue.trim().isEmpty()) {
			return Ivy.var().get(globalVariableName);
		}
		return proposedValue;
	}
	
	/**
	 * Returns the global variable value trimmed.
	 * @param globalVariableName the global variable name. Cannot be null or empty.
	 * @return
	 */
	public static String getGlobalVariable(String globalVariableName) {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The global variable name cannot be null or empty.", globalVariableName);
		
		return Ivy.var().get(globalVariableName).trim();
	}
	
	/**
	 * Checks if the given global variable has the expected value.
	 * @param globalVariableName the global variable name. Cannot be null or empty.
	 * @param expectedValue the expected value
	 * @return true if the trimmed global variable value equals the given expected value. Else false.
	 */
	public static boolean isGlobalVariableEqualTo(String globalVariableName, String expectedValue) {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The global variable name cannot be null or empty.", globalVariableName);
		
		return Ivy.var().get(globalVariableName).trim().equals(expectedValue);
	}

}
