package ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.util.RichDialogUtil;
import ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTable;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import ch.ivyteam.ivy.system.IProperty;

import com.ulcjava.base.application.ULCAbstractButton;
import com.ulcjava.base.application.ULCListSelectionModel;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;

/**
 * <p>FileTypesDialogPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileTypesDialogPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
	/** Serial version id */
	private static final long serialVersionUID = 1L;
	private RScrollPane ScrollPane = null;
	private RTable fileTypesTable = null;
	private RFlowLayoutPane managerFlowLayoutPane = null;
	private RButton newButton = null;
	private RButton modifyButton = null;
	private RButton closeButton = null;
	private RFlowLayoutPane chooserFlowLayoutPane = null;
	private RButton selectButton = null;
	private RButton cancelButton = null;
	private RButton deleteButton = null;
	private RButton translateButton = null;

	private boolean xpertlineLF=false;

	/**
	 * Create a new instance of FileTypesDialogPanel
	 */
	public FileTypesDialogPanel()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes FileTypesDialogPanel
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
		this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(555,304));
		this.add(getScrollPane(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		this.add(getManagerFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		this.add(getChooserFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
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
			ScrollPane.setStyleProperties("{/insetsTop \"10\"/insetsRight \"10\"/fill \"BOTH\"/insetsLeft \"10\"/weightY \"1\"/weightX \"1\"}");
			ScrollPane.setViewPortView(getFileTypesTable());
		}
		return ScrollPane;
	}

	/**
	 * This method initializes fileTypesTable	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTable	
	 */
	private RTable getFileTypesTable() {
		if (fileTypesTable == null) {
			fileTypesTable = new RTable();
			fileTypesTable.setName("fileTypesTable");
			fileTypesTable.setStyleProperties("{/font {/name \"Tahoma\"/size \"12\"/style \"PLAIN\"}}");
			fileTypesTable.setSortable(true);
			fileTypesTable.setModelConfiguration("{/showTableheader true /autoTableheader false /showtooltip false /showIcons false /version \"3.0\"/emptyTableText \"No FileTypes / Keine Dateintypen / Aucun type de documents\"/columns {{/result \"result=entry.fileTypeName\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/name\\\")\"/field \"\"/editable \"\"/condition \"\"/columnStyle \"default\"/cellWidget \"\"}{/result \"result=IF(entry.#applicationName==null,\\\"\\\",entry.applicationName)\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/applicationName\\\")\"/field \"\"/editable \"\"/condition \"\"/columnStyle \"default\"/cellWidget \"\"}}}");
			fileTypesTable.setAutoResizeMode(ULCTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			fileTypesTable.setRowHeight(20);
			fileTypesTable.setRowMargin(2);
			fileTypesTable.setSelectionMode(ULCListSelectionModel.SINGLE_SELECTION);
		}
		return fileTypesTable;
	}

	/**
	 * This method initializes managerFlowLayoutPane	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane	
	 */
	private RFlowLayoutPane getManagerFlowLayoutPane() {
		if (managerFlowLayoutPane == null) {
			managerFlowLayoutPane = new RFlowLayoutPane();
			managerFlowLayoutPane.setName("managerFlowLayoutPane");
			managerFlowLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/fill \"BOTH\"/insetsLeft \"10\"/alignment \"RIGHT\"/weightX \"1\"}");
			managerFlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
			managerFlowLayoutPane.add(getNewButton());
			managerFlowLayoutPane.add(getModifyButton());
			managerFlowLayoutPane.add(getTranslateButton());
			managerFlowLayoutPane.add(getDeleteButton());
			managerFlowLayoutPane.add(getCloseButton());
		}
		return managerFlowLayoutPane;
	}

	/**
	 * This method initializes newButton	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
	 */
	private RButton getNewButton() {
		if (newButton == null) {
			newButton = new RButton();
			newButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/plainStrings/new\")%>");
			newButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/file/24\")%>");
			newButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			newButton.setName("newButton");
			this.fixXpertlineLookAndFeel(newButton);
		}
		return newButton;
	}

	/**
	 * This method initializes modifyButton	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
	 */
	private RButton getModifyButton() {
		if (modifyButton == null) {
			modifyButton = new RButton();
			modifyButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/plainStrings/modify\")%>");
			modifyButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/write/24\")%>");
			modifyButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			modifyButton.setName("modifyButton");
			this.fixXpertlineLookAndFeel(modifyButton);
		}
		return modifyButton;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
	 */
	private RButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new RButton();
			closeButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/close\")%>");
			closeButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/close/24\")%>");
			closeButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			closeButton.setName("closeButton");
			this.fixXpertlineLookAndFeel(closeButton);
		}
		return closeButton;
	}

	/**
	 * This method initializes chooserFlowLayoutPane	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane	
	 */
	private RFlowLayoutPane getChooserFlowLayoutPane() {
		if (chooserFlowLayoutPane == null) {
			chooserFlowLayoutPane = new RFlowLayoutPane();
			chooserFlowLayoutPane.setName("chooserFlowLayoutPane");
			chooserFlowLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/fill \"BOTH\"/insetsLeft \"10\"/alignment \"RIGHT\"/weightX \"1\"}");
			chooserFlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
			chooserFlowLayoutPane.add(getSelectButton());
			chooserFlowLayoutPane.add(getCancelButton());
		}
		return chooserFlowLayoutPane;
	}

	/**
	 * This method initializes selectButton	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
	 */
	private RButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new RButton();
			selectButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/select\")%>");
			selectButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/ok/24\")%>");
			selectButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			selectButton.setName("selectButton");
			this.fixXpertlineLookAndFeel(selectButton);
		}
		return selectButton;
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
			cancelButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/cancel/24\")%>");
			cancelButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			cancelButton.setName("cancelButton");
			this.fixXpertlineLookAndFeel(cancelButton);
		}
		return cancelButton;
	}

	/**
	 * This method initializes deleteButton	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
	 */
	private RButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new RButton();
			deleteButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/delete\")%>");
			deleteButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/cancel/24\")%>");
			deleteButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
			deleteButton.setName("deleteButton");
			this.fixXpertlineLookAndFeel(deleteButton);
		}
		return deleteButton;
	}

	/**
	 * This method initializes translateButton	
	 * 	
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
	 */
	private RButton getTranslateButton() {
		if (translateButton == null) {
			translateButton = new RButton();
			translateButton.setText("<%= ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/translateDir\") %>");
			translateButton.setStyleProperties("{/font \"name=Verdana\\nsize=11\\nstyle=PLAIN\"}");
			translateButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/filetypeadmin/24\")%>");
			translateButton.setName("translateButton");
			this.fixXpertlineLookAndFeel(translateButton);
		}
		return translateButton;
	}

	private void fixXpertlineLookAndFeel(ULCAbstractButton co){
		if(this.xpertlineLF) {
			co.setForeground(Color.white);
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"