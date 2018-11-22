package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.util.Set;

import ch.ivyteam.api.IvyScriptVisibility;
import ch.ivyteam.api.PublicAPI;
import ch.ivyteam.ivy.globalvars.IGlobalVariableContext;


public class MyGlobalVariableContext implements IGlobalVariableContext {

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public String get(String varName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getVariableNames() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
