package ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog;

import ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog.internal.VisualDebugFiller;
import ch.ivyteam.ivy.addons.dynamicrd.DynamicDialog.internal.VisualDebugGridBagLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RFiller;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane;

import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.GridBagConstraints;
import com.ulcjava.base.application.ULCContainer;
import com.ulcjava.base.application.util.Color;

/**
 * This is the implementation of containers that uses a RGridBagLayoutPane with a separator on the top.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 03.08.2011
 */
public class SeparatorPane extends ComplexComponent
{

  private RGridBagLayoutPane gridBag = null;

  private RGridBagLayoutPane separatorGridBag = null;

  private RLabel separatorLabel = null;

  private RFiller filler = null;

  /**
   * Constructs a new SeparatourLayoutPane object.
   * 
   * @param panel dynamic dialog panel
   * @param parentContainer parent container
   * @param parameters parameters
   * @param index position when component is in a list
   */
  protected SeparatorPane(DynamicDialogPanel panel, ComplexComponent container,
          SeparatorPaneParameters parameters, int index)
  {
    super(panel, container, parameters, index);
  }

  @Override
  protected final void applyComponentStyle()
  {
    getGridBag().setStyle(getParameters().getContainerStyle());
    getSeparatorGridBag().setStyle(getParameters().getInsideContainerStyle());
    getHorizontalFiller().setStyle(getParameters().getSeparatorLabelStyle());
    getHorizontalFiller()
            .setStyleProperties(
                    "{/anchor \"CENTER\"/maximumSizeHeight \"1\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/minimumSizeHeight \"1\"/preferredSizeHeight \"1\"/weightX \"1\"}");
    getSeparatorLabel().setStyle(getParameters().getSeparatorLabelStyle());

    setWeightX(gridBag);
  }

  private RGridBagLayoutPane getGridBag()
  {
    if (gridBag == null)
    {
      gridBag = new VisualDebugGridBagLayoutPane();
      gridBag.setName(getParameters().getName());

      getUlcComponents().add(gridBag);
    }
    return gridBag;
  }

  @Override
  public final RGridBagLayoutPane getLastMainComponent()
  {
    return getMainComponent();
  }

  @Override
  public final RGridBagLayoutPane getMainComponent()
  {
    return gridBag;
  }

  @Override
  public final SeparatorPaneParameters getParameters()
  {
    return (SeparatorPaneParameters) getComponentParameters();
  }

  @Override
  public final ULCContainer getUlcContainer()
  {
    return gridBag;
  }

  @Override
  protected final void initialize(final Position pos, ComplexComponent previousContainer)
  {
    GridBagConstraints constraints;

    constraints = new GridBagConstraints();
    constraints.setGridX(pos.getPosX() + 0);
    constraints.setGridY(pos.getPosY() + 0);
    constraints.setGridWidth(getParameters().getGridWidth() * GRID_BAG_COLUMN_WIDTH);
    getParentContainer().add(getGridBag(), constraints);

    super.initialize(pos);

    getGridBag().add(
            getSeparatorGridBag(),
            new com.ulcjava.base.application.GridBagConstraints(0, 0, GRID_BAG_COLUMN_WIDTH, 1, 1, -1,
                    com.ulcjava.base.application.GridBagConstraints.CENTER,
                    com.ulcjava.base.application.GridBagConstraints.HORIZONTAL,
                    new com.ulcjava.base.application.util.Insets(0, 0, 0, 0), 0, 0));
    pos.setPosY(pos.getPosY() + 1);
  }

  private RGridBagLayoutPane getSeparatorGridBag()
  {
    if (separatorGridBag == null)
    {
      separatorGridBag = new VisualDebugGridBagLayoutPane();
      separatorGridBag.setName(getParameters().getName() + "SeparatorGridBag");

      getUlcComponents().add(separatorGridBag);

      separatorGridBag.add(getSeparatorLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1,
              1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER,
              com.ulcjava.base.application.GridBagConstraints.NONE,
              new com.ulcjava.base.application.util.Insets(0, 0, 0, 0), 0, 0));
      separatorGridBag.add(getHorizontalFiller(), new com.ulcjava.base.application.GridBagConstraints(1, 0,
              GRID_BAG_COLUMN_WIDTH - 1, 1, 1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER,
              com.ulcjava.base.application.GridBagConstraints.NONE,
              new com.ulcjava.base.application.util.Insets(0, 0, 0, 0), 0, 0));
    }
    return separatorGridBag;
  }

  @Override
  public final boolean isFocusable()
  {
    return false;
  }

  @Override
  public final void setFocusable(boolean b)
  {
    // Nothing to do
  }

  @Override
  public final String getLabel()
  {
    return getSeparatorLabel().getText();
  }

  @Override
  public final void setLabel(String value)
  {
    getSeparatorLabel().setText(value);
  }

  @Override
  protected final boolean useParentContainer()
  {
    return false;
  }

  @Override
  protected final Position getStartPos(Position pos)
  {
    return new Position(0, 1);
  }

  /**
   * This method initializes separatorLabel
   * 
   * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel
   */
  private RLabel getSeparatorLabel()
  {
    if (separatorLabel == null)
    {
      separatorLabel = new RLabel();
      separatorLabel.setName(getParameters().getName() + "SeparatorLabel");
      separatorLabel.setText(getParameters().getTitle());
      getUlcComponents().add(separatorLabel);
    }
    return separatorLabel;
  }

  private RFiller getHorizontalFiller()
  {
    if (filler == null)
    {
      filler = new VisualDebugFiller();
      filler.setName(getParameters().getName() + "SeparatorFiller");

      filler.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

      getUlcComponents().add(filler);
    }
    return filler;
  }
}
