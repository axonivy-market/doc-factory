package ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.displays.RListDisplay;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;

/**
 * <p>FolderOnServerTranslationAdminPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class ItemTranslationAdminPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel foldernameLabel = null;
private RListDisplay ListDisplay = null;
private RFlowLayoutPane FlowLayoutPane = null;
private RButton cancelButton = null;
private RButton saveButton = null;
  
  /**
   * Create a new instance of FolderOnServerTranslationAdminPanel
   */
  public ItemTranslationAdminPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FolderOnServerTranslationAdminPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(660,299));
        this.add(getFoldernameLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getListDisplay(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes foldernameLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getFoldernameLabel() {
	if (foldernameLabel == null) {
		foldernameLabel = new RLabel();
		foldernameLabel.setText("foldernameLabel");
		foldernameLabel.setStyleProperties("{/insetsTop \"10\"/insetsBottom \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"}");
		foldernameLabel.setName("foldernameLabel");
	}
	return foldernameLabel;
}

/**
 * This method initializes ListDisplay	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.displays.RListDisplay	
 */
private RListDisplay getListDisplay() {
	if (ListDisplay == null) {
		ListDisplay = new RListDisplay();
		ListDisplay.setName("ListDisplay");
		ListDisplay.setStyleProperties("{/fill \"BOTH\"/insetsLeft \"1\"/weightX \"1\"}");
		ListDisplay.setStyleProperties("{/fill \"BOTH\"/insetsRight \"10\"/insetsLeft \"10\"/weightY \"1\"/weightX \"1\"}");
	}
	return ListDisplay;
}

/**
 * This method initializes FlowLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane	
 */
private RFlowLayoutPane getFlowLayoutPane() {
	if (FlowLayoutPane == null) {
		FlowLayoutPane = new RFlowLayoutPane();
		FlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		FlowLayoutPane.setName("FlowLayoutPane");
		FlowLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"/weightX \"1\"/alignment \"RIGHT\"}");
		FlowLayoutPane.add(getSaveButton());
		FlowLayoutPane.add(getCancelButton());
	}
	return FlowLayoutPane;
}

/**
 * This method initializes cancelButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getCancelButton() {
	if (cancelButton == null) {
		cancelButton = new RButton();
		cancelButton.setName("cancelButton");
		cancelButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/cancel\")%>");
		cancelButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		cancelButton.setSelected(false);
	}
	return cancelButton;
}

/**
 * This method initializes saveButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getSaveButton() {
	if (saveButton == null) {
		saveButton = new RButton();
		saveButton.setName("saveButton");
		saveButton.setStyleProperties("{/font \"name=Verdana\\nsize=11\\nstyle=PLAIN\"}");
		saveButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/save\")%>");
	}
	return saveButton;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"