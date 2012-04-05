package ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog;

import java.util.List;

import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.DateTime;
import ch.ivyteam.ivy.scripting.objects.Duration;
import ch.ivyteam.ivy.scripting.objects.Time;

import com.ulcjava.base.application.ULCComponent;

/**
 * This is the implementation of fields that use a RTextField.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 19.11.2008
 */
public class Label extends FieldComponent
{
  private RLabel label = null;

  private Object value = null;

  /**
   * Constructs a new TextField object.
   * 
   * @param panel dynamic dialog panel
   * @param parentContainer parent container
   * @param parameters parameters
   * @param index position when component is in a list
   */
  protected Label(DynamicDialogPanel panel, ComplexComponent parentContainer, LabelParameters parameters,
          int index)
  {
    super(panel, parentContainer, parameters, index);
  }

  @Override
  public final RLabel getLastMainComponent()
  {
    return getMainComponent();
  }

  @Override
  public final RLabel getMainComponent()
  {
    return label;
  }

  @Override
  public final LabelParameters getParameters()
  {
    return (LabelParameters) getComponentParameters();
  }

  @Override
  public final String[] getSelectedRecord()
  {
    return null;
  }

  @Override
  public final String getText()
  {
    return getValueAsString();
  }

  private RLabel getLabelField()
  {
    if (label == null)
    {
      label = new RLabel();
      label.setName(getParameters().getName() + "LabelField");
      getUlcComponents().add(label);
    }
    return label;
  }

  @Override
  public final Object getValue()
  {
    return value;
  }

  @Override
  public final Date getValueAsDate()
  {
    return value instanceof Date ? (Date) value : null;
  }

  @Override
  public final DateTime getValueAsDateTime()
  {
    return value instanceof DateTime ? (DateTime) value : null;
  }

  @Override
  public final Duration getValueAsDuration()
  {
    return value instanceof Duration ? (Duration) value : null;
  }

  @Override
  public final Number getValueAsNumber()
  {
    return value instanceof Number ? (Number) value : null;
  }

  @Override
  public final String getValueAsString()
  {
    return value instanceof String ? (String) value : null;
  }

  @Override
  public final Time getValueAsTime()
  {
    return value instanceof Time ? (Time) value : null;
  }

  @Override
  public final Boolean getValueAsBoolean()
  {
    return value instanceof Boolean ? (Boolean) value : null;
  }

  @Override
  public final void setKeyValue(List<String[]> keyValue)
  {
    // Nothing to do
  }

  @Override
  protected final void applyFieldStyles()
  {
    LabelParameters parameters;

    parameters = getParameters();

    getLabelField().setStyle(parameters.getFieldStyle());
  }

  @Override
  public final void setValue(Object o, String text)
  {
    boolean changed;

    changed = false;
    if (o != value && o != null && !o.equals(value))
    {
      changed = true;
    }
    value = o;
    getLabelField().setText(o == null ? "" : o.toString());

    if (changed)
    {
      valueChanged();
    }
  }

  @Override
  public final void setValueAsDate(Date d, String text)
  {
    setValue(d, text);
  }

  @Override
  public final void setValueAsDateTime(DateTime dt, String text)
  {
    setValue(dt, text);
  }

  @Override
  public final void setValueAsDuration(Duration d, String text)
  {
    setValue(d, text);
  }

  @Override
  public final void setValueAsNumber(Number n, String text)
  {
    setValue(n, text);
  }

  @Override
  public final void setValueAsString(String s, String text)
  {
    setValue(s, text);
  }

  @Override
  public final void setValueAsTime(Time t, String text)
  {
    setValue(t, text);
  }

  @Override
  public final void setValueAsBoolean(Boolean b, String text)
  {
    setValue(b, text);
  }

  @Override
  public final boolean validate()
  {
    return true;
  }

  @Override
  protected final ULCComponent getFieldComponent()
  {
    return getLabelField();
  }

  @Override
  protected boolean isBackgroundColorChangedAllowed()
  {
    return false;
  }

  @Override
  protected boolean isEditable()
  {
    return false;
  }

  @Override
  protected void postInitializeField()
  {
    // Nothing to do
  }

  @Override
  public boolean isFocusable()
  {
    return false;
  }

  @Override
  protected void setFocusable(boolean value)
  {
    // Nothing to do
  }
}