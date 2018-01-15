package ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.util.RichDialogUtil;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.components.RTextField;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import ch.ivyteam.ivy.system.IProperty;

import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.ULCAbstractButton;
import com.ulcjava.base.application.util.Color;

/**
 * <p>FileTagWriterPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileTagWriterPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
	/** Serial version id */
	private static final long serialVersionUID = 1L;
	private RLabel Label = null;
	private RLabel TagLabel = null;
	private RTextField TextField = null;
	private RFlowLayoutPane FlowLayoutPane = null;
	private RButton okButton = null;
	private RButton cancelButton = null;

	private boolean xpertlineLF=false;

	/**
	 * Create a new instance of FileTagWriterPanel
	 */
	public FileTagWriterPanel()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes FileTagWriterPanel
	 * @return void
	 */
	private void initialize()
	{
		if(!RichDialogUtil.isRunningInVisualEditor()) {
			IProperty prop = Ivy.wf().getApplication().getConfigurationProperty("ria.lookandfeel");
			if(prop!=null) {
				if(prop.getValue()!=null && prop.getValue().toLowerCase().contains("xpertlinestandardlookandfeel")) {
					xpertlineLF = true;
				}
			}
		}
		this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(550,120));
		this.add(getLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 3, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		this.add(getTagLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		this.add(getTextField(), new com.ulcjava.base.application.GridBagConstraints(1, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		this.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 2, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
	}

	/**
	 * This method initializes Label	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
	 */
	private RLabel getLabel() {
		if (Label == null) {
			Label = new RLabel();
			Label.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsBottom \"10\"/insetsTop \"10\"/fill \"BOTH\"/insetsRight \"10\"/insetsLeft \"10\"}");
			Label.setName("Label");
		}
		return Label;
	}

	/**
	 * This method initializes TagLabel	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
	 */
	private RLabel getTagLabel() {
		if (TagLabel == null) {
			TagLabel = new RLabel();
			TagLabel.setText("Tag");
			TagLabel.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsLeft \"10\"}");
			TagLabel.setName("TagLabel");
		}
		return TagLabel;
	}

	/**
	 * This method initializes TextField	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTextField	
	 */
	private RTextField getTextField() {
		if (TextField == null) {
			TextField = new RTextField();
			TextField.setText("");
			TextField.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsBottom \"5\"/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"5\"/weightX \"1\"}");
			TextField.setMandatory(true);
			TextField.setName("TextField");
		}
		return TextField;
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
			FlowLayoutPane.setStyleProperties("{/insetsTop \"10\"/insetsBottom \"10\"/fill \"HORIZONTAL\"/insetsRight \"10\"/insetsLeft \"10\"/alignment \"RIGHT\"}");
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
			okButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/ok/24\")%>");
			okButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok\")%>");
			okButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			okButton.setEnabler(getTextField());
			okButton.setName("okButton");
			this.fixXpertlineLookAndFeel(okButton);
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
			cancelButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/cancel/24\")%>");
			cancelButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/cancel\")%>");
			cancelButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			cancelButton.setName("cancelButton");
			this.fixXpertlineLookAndFeel(cancelButton);
		}
		return cancelButton;
	}

	private void fixXpertlineLookAndFeel(ULCAbstractButton co){
		if(this.xpertlineLF) {
			co.setForeground(Color.white);
		}
	}
}  //  @jve:decl-index=0:visual-constraint="8,4"