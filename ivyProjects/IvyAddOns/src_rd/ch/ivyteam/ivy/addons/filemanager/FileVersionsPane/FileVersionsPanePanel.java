package ch.ivyteam.ivy.addons.filemanager.FileVersionsPane;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTable;
import com.ulcjava.base.application.ULCTable;
import ch.ivyteam.ivy.addons.filemanager.DesktopHandler.DesktopHandlerPanel;
import ch.ivyteam.ivy.richdialog.exec.panel.EmbeddedRichDialog;
import ch.ivyteam.ivy.richdialog.exec.panel.RichDialogPanelFactory;
import com.ulcjava.base.application.ULCContainer;
import com.ulcjava.base.application.util.Font;

/**
 * <p>FileVersionsPanePanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class FileVersionsPanePanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RLabel titleLabel = null;
private RScrollPane ScrollPane = null;
private RTable Table = null;
private @EmbeddedRichDialog(DesktopHandlerPanel.class) ULCContainer desktopHandlerPanel = null;
  
  /**
   * Create a new instance of FileVersionsPanePanel
   */
  public FileVersionsPanePanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes FileVersionsPanePanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(682,343));
        this.add(getTitleLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getScrollPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getDesktopHandlerPanel(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes titleLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getTitleLabel() {
	if (titleLabel == null) {
		titleLabel = new RLabel();
		titleLabel.setName("titleLabel");
		titleLabel.setStyleProperties("{/font {/name \"Dialog\"/size \"14\"/style \"BOLD\"}}");
	}
	return titleLabel;
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
		ScrollPane.setStyleProperties("{/fill \"BOTH\"/weightY \"1\"/weightX \"1\"}");
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
		Table.setSortable(true);
		Table.setModelConfiguration("{/showTableheader true /autoTableheader false /showtooltip false /showIcons false /version \"3.0\"/emptyTableText \"No versions / Keine Versionen vorhanden / Aucune version\"/columns {{/result \"result=entry.versionNumber\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileVersioning/plainStrings/versionnumber\\\")\"/field \"\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=entry.filename\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/fileName\\\")\"/field \"\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=entry.user\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileVersioning/plainStrings/creationUser\\\")\"/field \"\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=entry.date.format(\\\"dd.MM.yyyy\\\")\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/creationDate\\\")\"/field \"\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=entry.time.format(\\\"HH:mm:ss\\\")\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ivy.cms.co(\\\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/tableStrings/creationTime\\\")\"/field \"\"/editable \"\"/condition \"\"/cellWidget \"\"}}}");
		Table.setFont(new Font("Dialog", 0, 12));
		Table.setStyleProperties("{/font {/name \"Dialog\"/size \"14\"/style \"PLAIN\"}}");
		Table.setRowHeight(18);
		Table.setAutoResizeMode(ULCTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	}
	return Table;
}

/**
 * This method initializes desktopHandlerPanel, an embedded RichDialog.
 * The created object might have a different type than the declared
 * class due to overriding.
 * @returns com.ulcjava.base.application.ULCContainer 
 */
private ULCContainer getDesktopHandlerPanel() {
	if (desktopHandlerPanel == null) {
		desktopHandlerPanel = RichDialogPanelFactory
				.create(DesktopHandlerPanel.class);
		desktopHandlerPanel.setName("desktopHandlerPanel");
	}
	return desktopHandlerPanel;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"