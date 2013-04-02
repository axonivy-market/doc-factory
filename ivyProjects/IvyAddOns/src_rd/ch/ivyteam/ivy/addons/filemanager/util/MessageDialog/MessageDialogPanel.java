package ch.ivyteam.ivy.addons.filemanager.util.MessageDialog;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;
import ch.ivyteam.ivy.richdialog.widgets.components.RFiller;

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
        this.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 2, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(Filler, new com.ulcjava.base.application.GridBagConstraints(1, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
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
		FlowLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"/alignment \"RIGHT\"}");
		FlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
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
		okButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		okButton.setSelected(false);
		okButton.setName("okButton");
	}
	return okButton;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"