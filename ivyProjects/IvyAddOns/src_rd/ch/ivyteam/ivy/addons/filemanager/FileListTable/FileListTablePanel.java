package ch.ivyteam.ivy.addons.filemanager.FileListTable;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTable;

import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCTable;
import ch.ivyteam.ivy.addons.filemanager.DesktopHandler.DesktopHandlerPanel;
import ch.ivyteam.ivy.addons.util.RDCallbackMethodHandler;
import ch.ivyteam.ivy.richdialog.widgets.menus.RPopupMenu;
import ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import ch.ivyteam.ivy.richdialog.widgets.components.RTextField;
import ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane;
import com.ulcjava.base.application.ULCFlowLayoutPane;
import ch.ivyteam.ivy.richdialog.exec.panel.EmbeddedRichDialog;
import com.ulcjava.base.application.ULCContainer;
import com.ulcjava.base.application.dnd.DataFlavor;
import com.ulcjava.base.application.dnd.DnDFileListData;
import com.ulcjava.base.application.dnd.TransferHandler;
import com.ulcjava.base.application.dnd.Transferable;

import ch.ivyteam.ivy.richdialog.exec.panel.RichDialogPanelFactory;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * RichDialog panel implementation for FileListTablePanel.
 * @author <%=author%>
 * @since <%=date%>
 */
public class FileListTablePanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RTable fileListTable = null;
private RScrollPane ScrollPane = null;
@EmbeddedRichDialog(DesktopHandlerPanel.class) private ULCContainer desktopHandlerPanel = null;
private RPopupMenu tablePopupMenu = null;  //  @jve:decl-index=0:visual-constraint="254,322"
private RMenuItem openMenuItem = null;
private RMenuItem uploadMenuItem = null;
private RMenuItem deleteMenuItem = null;
private RButton uploadButton = null;
private RButton openButton = null;
private RButton deleteButton = null;
private RTextField enablerTextField = null;
private RFlowLayoutPane ToolbarFlowLayoutPane = null;
private TransferHandler filesTableTransferHandler;  //  @jve:decl-index=0:
private RButton unlockButton = null;
private RMenuItem unockMenuItem = null;
private RButton refrehButton = null;
private RMenuItem refreshMenuItem = null;
/**
   * Create a new instance of FileListTablePanel
   */
  public FileListTablePanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FileListTablePanel
   * @return void
   */
  private void initialize()
  {
this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(679,255));
this.setStyleProperties("{/fill \"BOTH\"/weightY \"1\"/weightX \"1\"}");
        this.add(getScrollPane(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, 1.0D, 1.0D, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.BOTH, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getDesktopHandlerPanel(), new com.ulcjava.base.application.GridBagConstraints(0, 3, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getEnablerTextField(), new com.ulcjava.base.application.GridBagConstraints(0, 4, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getToolbarFlowLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes fileListTable	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTable	
 */
private RTable getFileListTable() {
	if (fileListTable == null) {
		fileListTable = new RTable();
		fileListTable.setName("fileListTable");
		fileListTable.setAutoResizeMode(ULCTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		fileListTable.setSortable(true);
		fileListTable.setModelConfiguration("{/showTableheader true /autoTableheader false /showtooltip false /showIcons true /version \"3.0\"/emptyTableText \"Keine Datei / Aucun Fichier / No File\"/columns {{/result \"result=entry.filename\"/version \"3.0\"/tooltip \"\"/icon \"result=IF(!entry.locked.trim().equals(\\\"1\\\"),null,\\r\\nivy.cms.cr(\\\"/ch/ivyteam/ivy/addons/icons/lock/16\\\"))\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/fileName\\\")\"/field \"\"/condition \"\"/columnWidth \"250\"}{/result \"result=entry.creationDate.toIvyDate().format(\\\"dd-MM-yyyy\\\")\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/creationDate\\\")\"/field \"\"/condition \"\"/columnWidth \"150\"}{/result \"result=entry.userID\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/createdBy\\\")\"/field \"\"/columnWidth \"80\"}}}");
		fileListTable.setPreferredSize(new com.ulcjava.base.application.util.Dimension(0,0));
		fileListTable.setStyle("field");
		fileListTable.setRowHeight(20);
		fileListTable.setDragEnabled(true);
		fileListTable.setTransferHandler(getFilesTableTransferHandler());
		fileListTable.setComponentPopupMenu(getTablePopupMenu());
		fileListTable.setStyleProperties("{/fill \"BOTH\"/weightY \"1\"/weightX \"1\"}");
	}
	return fileListTable;
}

/**
 * 
 * @return the transferHandler for the files table
 */
private TransferHandler getFilesTableTransferHandler(){
	final IRichDialogPanel panel = this;

	if(filesTableTransferHandler==null){
		filesTableTransferHandler = new TransferHandler(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 2947690723197732172L;

			@Override
			public void exportDone(ULCComponent sourceComponent,
					Transferable _transferable, int dropAction) {
				//Do nothing
			}

			@Override
			public boolean importData(ULCComponent targetComponent,
					Transferable _transferable) {

				Object sourceObj = _transferable.getTransferData(DataFlavor.DRAG_FLAVOR);
				boolean b = false;
				if(sourceObj instanceof DnDFileListData)
				{
					DnDFileListData listData = (DnDFileListData) sourceObj;
					List<String> droppedFiles = List.create(String.class);
					String[] l = listData.getFileNames();
					for(int i =0; i<l.length; i++){
						droppedFiles.add(l[i].replace("\\", "/"));
					}
					Object [] param = new Object[1];
					param[0]= droppedFiles;
					RDCallbackMethodHandler.callRDMethod(panel, "_uploadFilesFromOSDnD", param);
					b=true;
				}
				return b;
			}

		
		};
	}
	return filesTableTransferHandler;
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
		ScrollPane.setStyleProperties("{/weightY \"1\"/weightX \"1\"}");
		ScrollPane.setName("ScrollPane");
		ScrollPane.setViewPortView(getFileListTable());
	}
	return ScrollPane;
}

/**
 * This method initializes desktopHandlerPanel	
 * 	
 * @return ch.xpertline.common.ria.component.DesktopHandler.DesktopHandlerPanel	
 */
private ULCContainer getDesktopHandlerPanel()  {
	if (desktopHandlerPanel == null) {
		desktopHandlerPanel = RichDialogPanelFactory.create(DesktopHandlerPanel.class);
		desktopHandlerPanel.setName("desktopHandlerPanel");
		desktopHandlerPanel.setVisible(false);
	}
	return desktopHandlerPanel;
}

/**
 * This method initializes tablePopupMenu	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RPopupMenu	
 */
private RPopupMenu getTablePopupMenu() {
	if (tablePopupMenu == null) {
		tablePopupMenu = new RPopupMenu();
		tablePopupMenu.setName("tablePopupMenu");
		tablePopupMenu.add(getRefreshMenuItem());
		tablePopupMenu.setName("tablePopupMenu");
		tablePopupMenu.add(getOpenMenuItem());
		tablePopupMenu.add(getUploadMenuItem());
		tablePopupMenu.add(getDeleteMenuItem());
		tablePopupMenu.add(getUnockMenuItem());
	}
	return tablePopupMenu;
}

/**
 * This method initializes openMenuItem	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem	
 */
private RMenuItem getOpenMenuItem() {
	if (openMenuItem == null) {
		openMenuItem = new RMenuItem();
		openMenuItem.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/openFile\")%>");
		openMenuItem.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/file/16\")%>");
		openMenuItem.setEnabler(getEnablerTextField());
		openMenuItem.setName("openMenuItem");
	}
	return openMenuItem;
}

/**
 * This method initializes uploadMenuItem	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem	
 */
private RMenuItem getUploadMenuItem() {
	if (uploadMenuItem == null) {
		uploadMenuItem = new RMenuItem();
		uploadMenuItem.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/upload\")%>");
		uploadMenuItem.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/up/16\")%>");
		uploadMenuItem.setName("uploadMenuItem");
	}
	return uploadMenuItem;
}

/**
 * This method initializes deleteMenuItem	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem	
 */
private RMenuItem getDeleteMenuItem() {
	if (deleteMenuItem == null) {
		deleteMenuItem = new RMenuItem();
		deleteMenuItem.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/deletedFile\")%>");
		deleteMenuItem.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/delete/16\")%>");
		deleteMenuItem.setEnabler(getEnablerTextField());
		deleteMenuItem.setName("deleteMenuItem");
	}
	return deleteMenuItem;
}

/**
 * This method initializes uploadButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getUploadButton() {
	if (uploadButton == null) {
		uploadButton = new RButton();
		uploadButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/up/16\")%>");
		uploadButton.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/upload\")%>");
		uploadButton.setName("uploadButton");
		uploadButton.setBorderPainted(false);
	}
	return uploadButton;
}

/**
 * This method initializes openButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getOpenButton() {
	if (openButton == null) {
		openButton = new RButton();
		openButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/file/16\")%>");
		openButton.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/openFile\")%>");
		openButton.setEnabler(getEnablerTextField());
		openButton.setName("openButton");
		openButton.setBorderPainted(false);
	}
	return openButton;
}

/**
 * This method initializes deleteButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getDeleteButton() {
	if (deleteButton == null) {
		deleteButton = new RButton();
		deleteButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/delete/16\")%>");
		deleteButton.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/deletedFile\")%>");
		deleteButton.setEnabler(getEnablerTextField());
		deleteButton.setName("deleteButton");
		deleteButton.setBorderPainted(false);
	}
	return deleteButton;
}

/**
 * This method initializes enablerTextField	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTextField	
 */
private RTextField getEnablerTextField() {
	if (enablerTextField == null) {
		enablerTextField = new RTextField();
		enablerTextField.setText("");
		enablerTextField.setMandatory(true);
		enablerTextField.setValidation("StringOneWord");
		enablerTextField.setName("enablerTextField");
	}
	return enablerTextField;
}

/**
 * This method initializes ToolbarFlowLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RFlowLayoutPane	
 */
private RFlowLayoutPane getToolbarFlowLayoutPane() {
	if (ToolbarFlowLayoutPane == null) {
		ToolbarFlowLayoutPane = new RFlowLayoutPane();
		ToolbarFlowLayoutPane.setName("ToolbarFlowLayoutPane");
		ToolbarFlowLayoutPane.setAlignment(ULCFlowLayoutPane.LEFT);
		ToolbarFlowLayoutPane.setStyleProperties("{/fill \"HORIZONTAL\"/weightX \"1\"}");
		ToolbarFlowLayoutPane.add(getRefrehButton());
		ToolbarFlowLayoutPane.add(getUploadButton());
		ToolbarFlowLayoutPane.add(getOpenButton());
		ToolbarFlowLayoutPane.add(getDeleteButton());
		ToolbarFlowLayoutPane.add(getUnlockButton());
	}
	return ToolbarFlowLayoutPane;
}

/**
 * This method initializes unlockButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getUnlockButton() {
	if (unlockButton == null) {
		unlockButton = new RButton();
		unlockButton.setName("unlockButton");
		unlockButton.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/unlockSelectedFiles\")%>");
		unlockButton.setBorderPainted(false);
		unlockButton.setEnabler(getEnablerTextField());
		unlockButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/unlock/16\")%>");
	}
	return unlockButton;
}

/**
 * This method initializes unockMenuItem	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem	
 */
private RMenuItem getUnockMenuItem() {
	if (unockMenuItem == null) {
		unockMenuItem = new RMenuItem();
		unockMenuItem.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/unlockSelectedFiles\")%>");
		unockMenuItem.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/unlock/16\")%>");
		unockMenuItem.setName("unockMenuItem");
	}
	return unockMenuItem;
}

/**
 * This method initializes refrehButton	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RButton	
 */
private RButton getRefrehButton() {
	if (refrehButton == null) {
		refrehButton = new RButton();
		refrehButton.setName("refrehButton");
		refrehButton.setBorderPainted(false);
		refrehButton.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/refresh/16\")%>");
		refrehButton.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/refresh\")%>");
	}
	return refrehButton;
}

/**
 * This method initializes refreshMenuItem	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.menus.RMenuItem	
 */
private RMenuItem getRefreshMenuItem() {
	if (refreshMenuItem == null) {
		refreshMenuItem = new RMenuItem();
		refreshMenuItem.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/refresh\")%>");
		refreshMenuItem.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/refresh/16\")%>");
		refreshMenuItem.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/FileListTable/buttonsLabel/refresh\")%>");
		refreshMenuItem.setName("refreshMenuItem");
	}
	return refreshMenuItem;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"