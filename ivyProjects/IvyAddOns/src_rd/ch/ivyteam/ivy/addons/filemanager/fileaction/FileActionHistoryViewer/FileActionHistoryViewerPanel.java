package ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTable;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import com.ulcjava.base.application.ULCTable;
import ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem;
import ch.ivyteam.ivy.richdialog.widgets.menus.RPopupMenu;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;

/**
 * <p>FileActionHistoryViewerPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileActionHistoryViewerPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel tileLabel = null;
private RScrollPane ScrollPane = null;
private RTable Table = null;
private RFlowLayoutPane FlowLayoutPane = null;
private RButton closeButton = null;
private RMenuItem refreshMenuItem = null;
private RPopupMenu PopupMenu = null;  //  @jve:decl-index=0:visual-constraint="612,208"
  
  /**
   * Create a new instance of FileActionHistoryViewerPanel
   */
  public FileActionHistoryViewerPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FileActionHistoryViewerPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(452,299));
        this.add(getTileLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getScrollPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes tileLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getTileLabel() {
	if (tileLabel == null) {
		tileLabel = new RLabel();
		tileLabel.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/file/24\")%>");
		tileLabel.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsTop \"10\"/insetsRight \"10\"/fill \"HORIZONTAL\"/insetsLeft \"10\"}");
		tileLabel.setName("tileLabel");
	}
	return tileLabel;
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
		ScrollPane.setViewPortView(getTable());
	}
	return ScrollPane;
}

/**
 * This method initializes Table	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTable	
 */
private RTable getTable() {
	if (Table == null) {
	Table = new RTable();
		Table.setName("Table");
		Table.setModelConfiguration("{/showTableheader true /autoTableheader false /showtooltip false /showIcons false /version \"3.0\"/columns {{/result \"result=\\\"<html><b>\\\"+entry.desc+\\\"</b><br>\\\"\\r\\n+entry.date.format(\\\"dd.MM.yyyy\\\")+\\\", \\\"+entry.time\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileActionHistory/label/name\\\")\"/headerAlignment \"default\"/field \"\"/editable \"\"/condition \"\"/columnWidth \"120\"/columnStyle \"default\"/cellWidget \"\"}{/result \"result=IF(entry.fullUsername.trim().length()>0,\\r\\n\\tentry.fullUsername.trim()+IF(entry.username.trim().length()>0,\\\" (\\\"+entry.username.trim()+\\\")\\\",\\\"\\\"),\\r\\n\\tentry.username.trim()\\r\\n)\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileActionHistory/label/user\\\")\"/headerAlignment \"default\"/field \"\"/editable \"\"/condition \"\"/columnWidth \"150\"/columnStyle \"default\"/cellWidget \"\"}{/result \"result=entry.info\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileActionHistory/label/otherInfos\\\")\"/headerAlignment \"default\"/field \"\"/editable \"\"/condition \"\"/columnWidth \"400\"/columnStyle \"default\"/cellWidget \"\"}}}");
		Table.setAutoResizeMode(ULCTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		Table.setRowHeight(34);
		Table.setStyleProperties("{/font {/name \"Dialog\"/size \"12\"/style \"PLAIN\"}}");
		Table.setComponentPopupMenu(getPopupMenu());
		Table.setSortable(true);
	}
	return Table;
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
		FlowLayoutPane.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}/insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/fill \"HORIZONTAL\"/insetsLeft \"10\"/alignment \"RIGHT\"}");
		FlowLayoutPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		FlowLayoutPane.add(getCloseButton());
	}
	return FlowLayoutPane;
}

/**
 * This method initializes closeButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getCloseButton() {
	if (closeButton == null) {
		closeButton = new RButton();
		closeButton.setName("closeButton");
		closeButton.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/close\")%>");
		closeButton.setStyleProperties("{/font {/name \"Verdana\"/size \"11\"/style \"PLAIN\"}}");
		closeButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/close/24\")%>");
	}
	return closeButton;
}

/**
 * This method initializes refreshMenuItem	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem	
 */
private RMenuItem getRefreshMenuItem() {
	if (refreshMenuItem == null) {
		refreshMenuItem = new RMenuItem();
		refreshMenuItem.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/refresh\")%>");
		refreshMenuItem.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/refresh/16\")%>");
		refreshMenuItem.setName("refreshMenuItem");
	}
	return refreshMenuItem;
}

/**
 * This method initializes PopupMenu	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RPopupMenu	
 */
private RPopupMenu getPopupMenu() {
	if (PopupMenu == null) {
		PopupMenu = new RPopupMenu();
		PopupMenu.setName("PopupMenu");
		PopupMenu.add(getRefreshMenuItem());
	}
	return PopupMenu;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"