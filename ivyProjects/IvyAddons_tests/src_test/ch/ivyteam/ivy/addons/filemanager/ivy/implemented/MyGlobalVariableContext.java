package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.util.Set;

import ch.ivyteam.ivy.globalvars.IGlobalVariableContext;
import ch.ivyteam.ivy.vars.Variable;


public class MyGlobalVariableContext implements IGlobalVariableContext {

  @Override
  public String get(String varName)
  {
    return null;
  }

  @Override
  public Set<String> getVariableNames()
  {
    return null;
  }

  @Override
  public String set(String name, String value)
  {
    return null;
  }

  @Override
  public String reset(String name)
  {
    return null;
  }

  @Override
  public String getForEnvironment(String environment, String name)
  {
    return null;
  }

  @Override
  public String setForEnvironment(String environment, String name, String value)
  {
    return null;
  }

  @Override
  public String resetForEnvironment(String environment, String name)
  {
    return null;
  }

  @Override
  public Set<String> names()
  {
    return null;
  }

  @Override
  public Variable variable(String name)
  {
    return null;
  }

}
