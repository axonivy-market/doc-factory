package ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.components.RTextField;

/**
 * <p>FolderOnServerTranslationUnitPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class ItemTranslationUnitPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel langLabel = null;
private RTextField translationTextField = null;
  
  /**
   * Create a new instance of FolderOnServerTranslationUnitPanel
   */
  public ItemTranslationUnitPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FolderOnServerTranslationUnitPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(512,75));
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(513,71));
        this.add(getLangLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getTranslationTextField(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes langLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getLangLabel() {
	if (langLabel == null) {
		langLabel = new RLabel();
		langLabel.setStyleProperties("{/insetsBottom \"5\"/insetsTop \"5\"/insetsRight \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/weightX \"1\"}");
		langLabel.setName("langLabel");
	}
	return langLabel;
}

/**
 * This method initializes translationTextField	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTextField	
 */
private RTextField getTranslationTextField() {
	if (translationTextField == null) {
		translationTextField = new RTextField();
		translationTextField.setText("");
		translationTextField.setStyleProperties("{/insetsBottom \"5\"/insetsRight \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"}");
		translationTextField.setName("translationTextField");
	}
	return translationTextField;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"