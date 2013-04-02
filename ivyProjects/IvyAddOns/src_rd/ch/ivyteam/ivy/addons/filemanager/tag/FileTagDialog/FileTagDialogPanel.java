package ch.ivyteam.ivy.addons.filemanager.tag.FileTagDialog;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTable;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.ULCListSelectionModel;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;

/**
 * <p>FileTagDialogPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileTagDialogPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel titelLabel = null;
private RScrollPane ScrollPane = null;
private RTable tagsTable = null;
private RFlowLayoutPane FlowLayoutPane = null;
private RButton editButton = null;
private RButton deleteButton = null;
private RButton closeButton = null;
private RButton refreshButton = null;
private RButton newButton = null;
  
  /**
   * Create a new instance of FileTagDialogPanel
   */
  public FileTagDialogPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FileTagDialogPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(600,348));
        this.add(getTitelLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getScrollPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes titelLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getTitelLabel() {
	if (titelLabel == null) {
		titelLabel = new RLabel();
		titelLabel.setText("titelLabel");
		titelLabel.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/fill \"BOTH\"/insetsLeft \"10\"}");
		titelLabel.setName("titelLabel");
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
		ScrollPane.setViewPortView(getTagsTable());
	}
	return ScrollPane;
}

/**
 * This method initializes tagsTable	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTable	
 */
private RTable getTagsTable() {
	if (tagsTable == null) {
		tagsTable = new RTable();
		tagsTable.setName("tagsTable");
		tagsTable.setSelectionMode(ULCListSelectionModel.SINGLE_SELECTION);
		tagsTable.setStyleProperties("{/font {/name \"Tahoma\"/size \"12\"/style \"PLAIN\"}}");
		tagsTable.setSortable(true);
		tagsTable.setModelConfiguration("{/showTableheader true /autoTableheader false /showtooltip false /showIcons false /version \"3.0\"/columns {{/result \"result=entry.tag\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"\\\"Tag\\\"\"/field \"\"/editable \"\"/condition \"\"/columnStyle \"default\"/cellWidget \"\"}}}");
		tagsTable.setRowHeight(20);
		tagsTable.setRowMargin(2);
		tagsTable.setAutoResizeMode(ULCTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	}
	return tagsTable;
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
		FlowLayoutPane.setStyleProperties("{/insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/fill \"HORIZONTAL\"/insetsLeft \"10\"/alignment \"RIGHT\"/weightX \"1\"}");
		FlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		FlowLayoutPane.add(getNewButton());
		FlowLayoutPane.add(getEditButton());
		FlowLayoutPane.add(getDeleteButton());
		FlowLayoutPane.add(getRefreshButton());
		FlowLayoutPane.add(getCloseButton());
	}
	return FlowLayoutPane;
}

/**
 * This method initializes editButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getEditButton() {
	if (editButton == null) {
		editButton = new RButton();
		editButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/edit\")%>");
		editButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/write/24\")%>");
		editButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		editButton.setName("editButton");
	}
	return editButton;
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
		deleteButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/delete/24\")%>");
		deleteButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		deleteButton.setName("deleteButton");
	}
	return deleteButton;
}

/**
 * This method initializes closeButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getCloseButton() {
	if (closeButton == null) {
		closeButton = new RButton();
		closeButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/close/24\")%>");
		closeButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/close\")%>");
		closeButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		closeButton.setName("closeButton");
	}
	return closeButton;
}

/**
 * This method initializes refreshButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getRefreshButton() {
	if (refreshButton == null) {
		refreshButton = new RButton();
		refreshButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/refresh\")%>");
		refreshButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/refresh/24\")%>");
		refreshButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		refreshButton.setName("refreshButton");
	}
	return refreshButton;
}

/**
 * This method initializes newButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getNewButton() {
	if (newButton == null) {
		newButton = new RButton();
		newButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/new\")%>");
		newButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/add/24\")%>");
		newButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		newButton.setName("newButton");
	}
	return newButton;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"