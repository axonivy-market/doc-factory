package ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog;

import java.util.List;

/**
 * This is parameter class of fields that use a RLookupTextField.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 03.08.2011
 */
class LabelParameters extends FieldComponentParameters
{
  private static final long serialVersionUID = 9131561307998999761L;

  protected LabelParameters(List<String> cmsContexts, String name, String fullName,
          ComplexComponentParameters parentContainerParameters, Integer position, Class<?> clazz)
  {
    super(cmsContexts, name, fullName, parentContainerParameters, position, clazz);
  }

  @Override
  public ComponentType getComponentType()
  {
    return ComponentType.LABEL;
  }

  @Override
  protected boolean isEditableByDefault()
  {
    return false;
  }
}
