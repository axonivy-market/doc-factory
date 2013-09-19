package ch.ivyteam.ivy.addons.filemanager.util.ActionDisplay;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;

/**
 * <p>ActionDisplayPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class ActionDisplayPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
  
  /**
   * Create a new instance of ActionDisplayPanel
   */
  public ActionDisplayPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes ActionDisplayPanel
   * @return void
   */
  private void initialize()
  {
  }
}