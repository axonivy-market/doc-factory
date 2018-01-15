package ch.ivyteam.ivy.addons.filemanager.util.MessageDialog;

import java.awt.event.MouseAdapter;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.triggers.MouseTrigger;
import org.jdesktop.animation.timing.triggers.MouseTriggerEvent;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;
import ch.ivyteam.ivy.richdialog.widgets.components.RFiller;
import ch.ivyteam.ivy.richdialog.widgets.containers.RGridLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButtonGroup;
import ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox;
import ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.containers.RTabbedPane;

/**
 * <p>MessageDialogPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class MessageDialogPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel iconLabel = null;
private RLabel messageLabel = null;
private RFlowLayoutPane FlowLayoutPane = null;
private RButton okButton = null;
private RGridLayoutPane checkBoxesGridLayoutPane = null;
private RButtonGroup ButtonGroup = null;  //  @jve:decl-index=0:visual-constraint="310,252"
private RCheckBox optionalCheckBox = null;
private RGridBagLayoutPane GridBagLayoutPane = null;
/**
   * Create a new instance of MessageDialogPanel
   */
  public MessageDialogPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes MessageDialogPanel
   * @return void
   */
  private void initialize()
  {
        RFiller Filler = new RFiller();
        Filler.setStyleProperties("{/fill \"VERTICAL\"/weightY \"1\"}");
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(530,189));
        this.add(getIconLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getMessageLabel(), new com.ulcjava.base.application.GridBagConstraints(1, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(Filler, new com.ulcjava.base.application.GridBagConstraints(1, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getCheckBoxesGridLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 2, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getGridBagLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 4, 3, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes iconLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getIconLabel() {
	if (iconLabel == null) {
		iconLabel = new RLabel();
		iconLabel.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/info/48\")%>");
		iconLabel.setStyleProperties("{/horizontalAlignment \"CENTER\"/verticalAlignment \"CENTER\"/insetsTop \"10\"/insetsLeft \"10\"}");
		iconLabel.setName("iconLabel");
	}
	return iconLabel;
}

/**
 * This method initializes messageLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getMessageLabel() {
	if (messageLabel == null) {
		messageLabel = new RLabel();
		messageLabel.setText("messageLabel");
		messageLabel.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsTop \"10\"/fill \"BOTH\"/insetsRight \"10\"/insetsLeft \"10\"/weightX \"1\"}");
		messageLabel.setName("messageLabel");
	}
	return messageLabel;
}

/**
 * This method initializes FlowLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane	
 */
private RFlowLayoutPane getFlowLayoutPane() {
	if (FlowLayoutPane == null) {
		FlowLayoutPane = new RFlowLayoutPane();
		FlowLayoutPane.setName("FlowLayoutPane");
		FlowLayoutPane.setStyleProperties("{/fill \"HORIZONTAL\"/alignment \"RIGHT\"/weightX \"1\"}");
		FlowLayoutPane.setBackground(new Color(244, 247, 255));
		FlowLayoutPane.add(getOkButton());
	}
	return FlowLayoutPane;
}

/**
 * This method initializes okButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getOkButton() {
	if (okButton == null) {
		okButton = new RButton();
		okButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok\")%>");
		okButton.setStyleProperties("{/font \"name=Verdana\\nsize=11\\nstyle=PLAIN\"/horizontalTextPosition \"LEFT\"}");
		okButton.setSelected(false);
		okButton.setName("okButton");
	}
	return okButton;
}

/**
 * This method initializes checkBoxesGridLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RGridLayoutPane	
 */
private RGridLayoutPane getCheckBoxesGridLayoutPane() {
	if (checkBoxesGridLayoutPane == null) {
		checkBoxesGridLayoutPane = new RGridLayoutPane();
		checkBoxesGridLayoutPane.setName("checkBoxesGridLayoutPane");
		checkBoxesGridLayoutPane.setStyleProperties("{/columns \"1\"/insetsBottom \"10\"/insetsTop \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"/weightX \"1\"}");
	}
	return checkBoxesGridLayoutPane;
}

/**
 * This method initializes ButtonGroup	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButtonGroup	
 */
public RButtonGroup getButtonGroup() {
	if (ButtonGroup == null) {
		ButtonGroup = new RButtonGroup();
	}
	return ButtonGroup;
}

/**
 * This method initializes optionalCheckBox	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox	
 */
private RCheckBox getOptionalCheckBox() {
	if (optionalCheckBox == null) {
		optionalCheckBox = new RCheckBox();
		optionalCheckBox.setText("optionalCheckBox");
		optionalCheckBox.setStyleProperties("{/fill \"HORIZONTAL\"}");
		optionalCheckBox.setName("optionalCheckBox");
	}
	return optionalCheckBox;
}

/**
 * This method initializes GridBagLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane	
 */
private RGridBagLayoutPane getGridBagLayoutPane() {
	if (GridBagLayoutPane == null) {
		GridBagLayoutPane = new RGridBagLayoutPane();
		GridBagLayoutPane.setName("GridBagLayoutPane");
		GridBagLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/fill \"HORIZONTAL\"/insetsLeft \"10\"}");
		GridBagLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		GridBagLayoutPane.add(getOptionalCheckBox(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		GridBagLayoutPane.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(1, 0, 1, 1, 0.0D, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
	}
	return GridBagLayoutPane;
}

}  //  @jve:decl-index=0:visual-constraint="10,10"