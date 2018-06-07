package ch.ivyteam.ivy.addons.filemanager.configuration;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.globalvars.IGlobalVariableContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class})
public class IvyGlobalVariableHandlerTest {
	
	@Rule
	ExpectedException thrown = ExpectedException.none();
	
	@Mock
	IGlobalVariableContext var;
	
	private static final String PROPOSED_VALUE = "proposed value";
	
	private static final String GLOBAL_VARIABLE_NAME = "global_variable";
	private static final String GLOBAL_VARIABLE_VALUE = "global variable value";
	
	@Before
	public void setUp() throws Exception {
		var = mock(IGlobalVariableContext.class);
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.var()).thenReturn(var);
	}

	@Test
	public void returnsGlobalVariableValueIfProposedValueIsBlank_throw_IAE_if_globalVariableName_is_null() {
		thrown.expect(IllegalArgumentException.class);
		IvyGlobalVariableHandler.returnsGlobalVariableValueIfProposedValueIsBlank(PROPOSED_VALUE, null);
	}
	
	@Test
	public void returnsGlobalVariableValueIfProposedValueIsBlank_throw_IAE_if_globalVariableName_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		IvyGlobalVariableHandler.returnsGlobalVariableValueIfProposedValueIsBlank(PROPOSED_VALUE, " ");
	}
	
	@Test
	public void returnsGlobalVariableValueIfProposedValueIsBlank_returns_global_variable_value_if_proposedValue_is_null() {
		when(var.get(GLOBAL_VARIABLE_NAME)).thenReturn(GLOBAL_VARIABLE_VALUE);
		
		String returnedValue = IvyGlobalVariableHandler.returnsGlobalVariableValueIfProposedValueIsBlank(null, GLOBAL_VARIABLE_NAME);
		
		assertTrue(returnedValue.equals(GLOBAL_VARIABLE_VALUE));
	}
	
	@Test
	public void returnsGlobalVariableValueIfProposedValueIsBlank_returns_global_variable_value_if_proposedValue_is_empty() {
		when(var.get(GLOBAL_VARIABLE_NAME)).thenReturn(GLOBAL_VARIABLE_VALUE);
		
		String returnedValue = IvyGlobalVariableHandler.returnsGlobalVariableValueIfProposedValueIsBlank(" ", GLOBAL_VARIABLE_NAME);
		
		assertTrue(returnedValue.equals(GLOBAL_VARIABLE_VALUE));
	}
	
	@Test
	public void returnsGlobalVariableValueIfProposedValueIsBlank_returns_proposed_value_if_is_notEmpty() {
		when(var.get(GLOBAL_VARIABLE_NAME)).thenReturn(GLOBAL_VARIABLE_VALUE);
		
		String returnedValue = IvyGlobalVariableHandler.returnsGlobalVariableValueIfProposedValueIsBlank(PROPOSED_VALUE, GLOBAL_VARIABLE_NAME);
		
		assertTrue(returnedValue.equals(PROPOSED_VALUE));
	}

}
