package ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput;

import com.ulcjava.base.shared.UlcEventCategories;
import com.ulcjava.base.shared.UlcEventConstants;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTextArea;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;

/**
 * <p>FileDescriptionInputPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileDescriptionInputPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel titelLabel = null;
private RScrollPane ScrollPane = null;
private RTextArea TextArea = null;
private RLabel validationLabel = null;
private RFlowLayoutPane FlowLayoutPane = null;
private RButton okButton = null;
private RButton cancelButton = null;
  
  /**
   * Create a new instance of FileDescriptionInputPanel
   */
  public FileDescriptionInputPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FileDescriptionInputPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(503,287));
        this.add(getTitelLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 2, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getScrollPane(), new com.ulcjava.base.application.GridBagConstraints(1, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getValidationLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 2, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 3, 2, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes titelLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getTitelLabel() {
	if (titelLabel == null) {
		titelLabel = new RLabel();
		titelLabel.setName("titelLabel");
		titelLabel.setText("");
		titelLabel.setStyleProperties("{/font {/name \"Dialog\"/size \"14\"/style \"PLAIN\"}/fill \"HORIZONTAL\"/weightX \"1\"}");
	}
	return titelLabel;
}

/**
 * This method initializes ScrollPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane	
 */
private RScrollPane getScrollPane() {
	if (ScrollPane == null) {
		ScrollPane = new RScrollPane();
		ScrollPane.setName("ScrollPane");
		ScrollPane.setStyleProperties("{/insetsRight \"10\"/fill \"BOTH\"/insetsLeft \"10\"/weightY \"1\"/weightX \"1\"}");
		ScrollPane.setViewPortView(getTextArea());
	}
	return ScrollPane;
}

/**
 * This method initializes TextArea	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTextArea	
 */
private RTextArea getTextArea() {
	if (TextArea == null) {
		TextArea = new RTextArea();
		TextArea.setText("");
		TextArea.setLineWrap(true);
		TextArea.setWrapStyleWord(true);
		TextArea.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		TextArea.setEventDeliveryMode(UlcEventCategories.VALUE_CHANGED_EVENT_CATEGORY, UlcEventConstants. ASYNCHRONOUS_MODE);
		TextArea.setEventDeliveryMode(UlcEventCategories.KEY_EVENT_CATEGORY, UlcEventConstants. ASYNCHRONOUS_MODE);
		TextArea.setName("TextArea");
	}
	return TextArea;
}

/**
 * This method initializes validationLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getValidationLabel() {
	if (validationLabel == null) {
		validationLabel = new RLabel();
		validationLabel.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/enteredTextToolong\")%>");
		validationLabel.setStyleProperties("{/foregroundColor {/b \"0\"/r \"255\"/g \"153\"}/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsRight \"10\"/fill \"HORIZONTAL\"/insetsLeft \"10\"}");
		validationLabel.setName("validationLabel");
	}
	return validationLabel;
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
		FlowLayoutPane.setStyleProperties("{/insetsTop \"10\"/fill \"BOTH\"/insetsRight \"10\"/insetsLeft \"10\"/alignment \"RIGHT\"}");
		FlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		FlowLayoutPane.add(getOkButton());
		FlowLayoutPane.add(getCancelButton());
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
		okButton.setName("okButton");
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
		cancelButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/cancel\")%>");
		cancelButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		cancelButton.setName("cancelButton");
	}
	return cancelButton;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"