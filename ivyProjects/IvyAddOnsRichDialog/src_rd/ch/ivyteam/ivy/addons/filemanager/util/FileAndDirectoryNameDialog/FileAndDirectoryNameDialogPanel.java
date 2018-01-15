package ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog;

import com.ulcjava.base.shared.UlcEventCategories;
import com.ulcjava.base.shared.UlcEventConstants;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.components.RTextField;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RFiller;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;

/**
 * <p>FileAndDirectoryNameDialogPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileAndDirectoryNameDialogPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel iconLabel = null;
private RLabel textLabel = null;
private RTextField nameTextField = null;
private RFlowLayoutPane buttonsFlowLayoutPane = null;
private RLabel messageLabel = null;
private RButton okButton = null;
private RButton cancelButton = null;
  
  /**
   * Create a new instance of FileAndDirectoryNameDialogPanel
   */
  public FileAndDirectoryNameDialogPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FileAndDirectoryNameDialogPanel
   * @return void
   */
  private void initialize()
  {
        RFiller Filler1 = new RFiller();
        Filler1.setStyleProperties("{/fill \"VERTICAL\"/weightY \"1\"}");
        RFiller Filler = new RFiller();
        Filler.setStyleProperties("{/preferredSizeWidth \"1\"/preferredSizeHeight \"30\"}");
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(570,200));
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(658,177));
        this.setStyleProperties("{/fill \"HORIZONTAL\"}");
        this.add(getIconLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 3, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getTextLabel(), new com.ulcjava.base.application.GridBagConstraints(1, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getNameTextField(), new com.ulcjava.base.application.GridBagConstraints(1, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getButtonsFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 4, 4, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getMessageLabel(), new com.ulcjava.base.application.GridBagConstraints(1, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(Filler, new com.ulcjava.base.application.GridBagConstraints(2, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(Filler1, new com.ulcjava.base.application.GridBagConstraints(1, 3, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes iconLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getIconLabel() {
	if (iconLabel == null) {
		iconLabel = new RLabel();
		iconLabel.setStyleProperties("{/horizontalAlignment \"CENTER\"/verticalAlignment \"CENTER\"/insetsTop \"10\"/fill \"VERTICAL\"/insetsLeft \"10\"}");
		iconLabel.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/folder/48\")%>");
		iconLabel.setName("iconLabel");
	}
	return iconLabel;
}

/**
 * This method initializes textLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getTextLabel() {
	if (textLabel == null) {
		textLabel = new RLabel();
		textLabel.setName("textLabel");
		textLabel.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsTop \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"}");
	}
	return textLabel;
}

/**
 * This method initializes nameTextField	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTextField	
 */
private RTextField getNameTextField() {
	if (nameTextField == null) {
		nameTextField = new RTextField();
		nameTextField.setText("");
		nameTextField.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsBottom \"20\"/insetsTop \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"/weightX \"1\"}");
		nameTextField.setName("nameTextField");
		nameTextField.setEventDeliveryMode(UlcEventCategories.VALUE_CHANGED_EVENT_CATEGORY, UlcEventConstants.ASYNCHRONOUS_MODE);
		nameTextField.setEventDeliveryMode(UlcEventCategories.KEY_EVENT_CATEGORY, UlcEventConstants.ASYNCHRONOUS_MODE);
	}
	return nameTextField;
}

/**
 * This method initializes buttonsFlowLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane	
 */
private RFlowLayoutPane getButtonsFlowLayoutPane() {
	if (buttonsFlowLayoutPane == null) {
		buttonsFlowLayoutPane = new RFlowLayoutPane();
		buttonsFlowLayoutPane.setName("buttonsFlowLayoutPane");
		buttonsFlowLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"/alignment \"RIGHT\"/weightX \"1\"}");
		buttonsFlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		buttonsFlowLayoutPane.add(getOkButton());
		buttonsFlowLayoutPane.add(getCancelButton());
	}
	return buttonsFlowLayoutPane;
}

/**
 * This method initializes messageLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getMessageLabel() {
	if (messageLabel == null) {
		messageLabel = new RLabel();
		messageLabel.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"}");
		messageLabel.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/warning/24\")%>");
		messageLabel.setName("messageLabel");
	}
	return messageLabel;
}

/**
 * This method initializes okButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getOkButton() {
	if (okButton == null) {
		okButton = new RButton();
		okButton.setName("okButton");
		okButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		okButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok\")%>");
	}
	return okButton;
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
		cancelButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		cancelButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/cancel\")%>");
	}
	return cancelButton;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"