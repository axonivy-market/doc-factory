package ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog;

import java.util.List;

/**
 * This is the parameters of containers that uses a RGridBagLayoutPane and a separator.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 03.08.2011
 */
class SeparatorPaneParameters extends ContainerParameters
{
  private static final long serialVersionUID = 571495192938471214L;

  protected SeparatorPaneParameters(List<String> cmsContexts, String name, String fullName,
          ComplexComponentParameters parentContainerParameters, Integer position, Class<?> clazz)
  {
    super(cmsContexts, name, fullName, parentContainerParameters, position, clazz);
  }

  @Override
  public ComponentType getComponentType()
  {
    return ComponentType.SEPARATOR_PANE;
  }
}
