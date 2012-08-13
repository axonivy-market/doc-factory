package ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager;

import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RTree;
import ch.ivyteam.ivy.richdialog.widgets.containers.RGridLayoutPane;
import ch.ivyteam.ivy.richdialog.widgets.components.RFiller;
import ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox;
import com.ulcjava.base.application.BorderFactory;
import com.ulcjava.base.application.util.Color;
import com.ulcjava.base.application.border.ULCTitledBorder;
import com.ulcjava.base.application.util.Font;
import ch.ivyteam.ivy.richdialog.widgets.containers.RSplitPane;
import com.ulcjava.base.application.ULCSplitPane;

/**
 * <p>DirectorySecurityManagerPanel is a rich dialog panel implementation.
 *
 * <p>Please note that a rich dialog panel is not an instance of a Swing 
 * container, but of an ULCContainer. As such it can not be run 
 * or instantiated outside the ULC framework.
 */
@SuppressWarnings("all")
public class DirectorySecurityManagerPanel extends RichDialogGridBagPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RGridBagLayoutPane headGridBagLayoutPane = null;
private RLabel Label = null;
private RButton closeButton = null;
private RLabel ivyRolesLabel = null;
private RScrollPane treeScrollPane = null;
private RTree rolesTree = null;
private RLabel ManagedActionsLabel = null;
private RGridBagLayoutPane actionsGridBagLayoutPane = null;
private RCheckBox codCheckBox = null;
private RCheckBox cudCheckBox = null;
private RCheckBox cddCheckBox = null;
private RCheckBox cwfCheckBox = null;
private RCheckBox cdfCheckBox = null;
private RLabel mainLabel = null;
private RLabel iconLabel = null;
private RSplitPane SplitPane = null;
private RGridBagLayoutPane leftGridBagLayoutPane = null;
private RGridBagLayoutPane rightGridBagLayoutPane = null;
/**
   * Create a new instance of DirectorySecurityManagerPanel
   */
  public DirectorySecurityManagerPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes DirectorySecurityManagerPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(818,502));
        this.setBorder(BorderFactory.createTitledBorder(null, "", ULCTitledBorder.DEFAULT_JUSTIFICATION, ULCTitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(23, 64, 140)));
        this.add(getHeadGridBagLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
        this.add(getSplitPane(), new com.ulcjava.base.application.GridBagConstraints(0, 3, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
  }

/**
 * This method initializes headGridBagLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane	
 */
private RGridBagLayoutPane getHeadGridBagLayoutPane() {
	if (headGridBagLayoutPane == null) {
		headGridBagLayoutPane = new RGridBagLayoutPane();
		headGridBagLayoutPane.setName("headGridBagLayoutPane");
		headGridBagLayoutPane.setStyleProperties("{/fill \"HORIZONTAL\"/weightX \"1\"}");
		headGridBagLayoutPane.add(getCloseButton(), new com.ulcjava.base.application.GridBagConstraints(2, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		headGridBagLayoutPane.add(getMainLabel(), new com.ulcjava.base.application.GridBagConstraints(1, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		headGridBagLayoutPane.add(getIconLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
	}
	return headGridBagLayoutPane;
}

/**
 * This method initializes Label	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getLabel() {
	if (Label == null) {
		Label = new RLabel();
		Label.setStyleProperties("{/font {/name \"Dialog\"/size \"16\"/style \"PLAIN\"}/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/weightX \"1\"}");
		Label.setName("Label");
	}
	return Label;
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
		closeButton.setStyleProperties("{/horizontalAlignment \"CENTER\"/usePreferredSizeAsExactSize \"true\"/insetsTop \"3\"/insetsBottom \"3\"/preferredSizeWidth \"30\"/insetsRight \"3\"/preferredSizeHeight \"30\"}");
		closeButton.setBorderPainted(false);
		closeButton.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/tooltips/closeSecurity\")%>");
		closeButton.setName("closeButton");
	}
	return closeButton;
}

/**
 * This method initializes ivyRolesLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getIvyRolesLabel() {
	if (ivyRolesLabel == null) {
		ivyRolesLabel = new RLabel();
		ivyRolesLabel.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/plainStrings/ivyRoles\")%>");
		ivyRolesLabel.setStyleProperties("{/font {/name \"Dialog\"/size \"16\"/style \"PLAIN\"}/fill \"HORIZONTAL\"}");
		ivyRolesLabel.setName("ivyRolesLabel");
	}
	return ivyRolesLabel;
}

/**
 * This method initializes treeScrollPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane	
 */
private RScrollPane getTreeScrollPane() {
	if (treeScrollPane == null) {
		treeScrollPane = new RScrollPane();
		treeScrollPane.setName("treeScrollPane");
		treeScrollPane.setStyleProperties("{/fill \"BOTH\"/weightY \"1\"/weightX \"1\"}");
		treeScrollPane.setCornerRadius(0);
		treeScrollPane.setViewPortView(getRolesTree());
	}
	return treeScrollPane;
}

/**
 * This method initializes rolesTree	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTree	
 */
private RTree getRolesTree() {
	if (rolesTree == null) {
		rolesTree = new RTree();
		rolesTree.setName("rolesTree");
		rolesTree.setAutoSelectFirstEntry(true);
		rolesTree.setModelConfiguration("{/showTableheader \"true\"/autoTableheader \"false\"/showtooltip \"false\"/showIcons \"false\"/dynamicTreeLoadMode \"LOAD_FOR_RENDER_PARENT\"/version \"3.0\"/columns {{/patterns {{/result \"result=value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"\"/field \"\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=IF(entry.getName().trim().equals(\\\"\\\"),entry.getDisplayName(),entry.getName())\"/version \"3.0\"/icon \"result=ivy.cms.cr(\\\"/ch/ivyteam/ivy/addons/icons/roles/24\\\")\"/field \"\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.security.IRole\"}}/version \"3.0\"}}}");
	}
	return rolesTree;
}

/**
 * This method initializes ManagedActionsLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getManagedActionsLabel() {
	if (ManagedActionsLabel == null) {
		ManagedActionsLabel = new RLabel();
		ManagedActionsLabel.setStyleProperties("{/font {/name \"Dialog\"/size \"16\"/style \"PLAIN\"}/anchor \"WEST\"/fill \"NONE\"/insetsLeft \"20\"/weightX \"0\"}");
		ManagedActionsLabel.setName("ManagedActionsLabel");
	}
	return ManagedActionsLabel;
}

/**
 * This method initializes actionsGridBagLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane	
 */
private RGridBagLayoutPane getActionsGridBagLayoutPane() {
	if (actionsGridBagLayoutPane == null) {
		RFiller Filler1 = new RFiller();
		Filler1.setStyleProperties("{/fill \"VERTICAL\"/weightY \"1\"}");
		actionsGridBagLayoutPane = new RGridBagLayoutPane();
		actionsGridBagLayoutPane.setName("actionsGridBagLayoutPane");
		actionsGridBagLayoutPane.setStyleProperties("{/fill \"BOTH\"/weightY \"1\"/weightX \"1\"}");
		actionsGridBagLayoutPane.setCornerRadius(0);
		actionsGridBagLayoutPane.setBorder(BorderFactory.createTitledBorder(null, "", ULCTitledBorder.DEFAULT_JUSTIFICATION, ULCTitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(23, 64, 140)));
		actionsGridBagLayoutPane.add(Filler1, new com.ulcjava.base.application.GridBagConstraints(1, 6, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		actionsGridBagLayoutPane.add(getCodCheckBox(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		actionsGridBagLayoutPane.add(getCudCheckBox(), new com.ulcjava.base.application.GridBagConstraints(0, 2, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		actionsGridBagLayoutPane.add(getCddCheckBox(), new com.ulcjava.base.application.GridBagConstraints(0, 3, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		actionsGridBagLayoutPane.add(getCwfCheckBox(), new com.ulcjava.base.application.GridBagConstraints(0, 4, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		actionsGridBagLayoutPane.add(getCdfCheckBox(), new com.ulcjava.base.application.GridBagConstraints(0, 5, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		actionsGridBagLayoutPane.add(getLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
	}
	return actionsGridBagLayoutPane;
}

/**
 * This method initializes codCheckBox	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox	
 */
private RCheckBox getCodCheckBox() {
	if (codCheckBox == null) {
		codCheckBox = new RCheckBox();
		codCheckBox.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/openDirectoryRight\")%>");
		codCheckBox.setToolTipText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/tooltips/openDirectory\")%>");
		codCheckBox.setStyleProperties("{/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/weightX \"1\"}");
		codCheckBox.setName("codCheckBox");
	}
	return codCheckBox;
}

/**
 * This method initializes cudCheckBox	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox	
 */
private RCheckBox getCudCheckBox() {
	if (cudCheckBox == null) {
		cudCheckBox = new RCheckBox();
		cudCheckBox.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/updateDirectoryRight\")%>");
		cudCheckBox.setStyleProperties("{/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/weightX \"1\"}");
		cudCheckBox.setName("cudCheckBox");
	}
	return cudCheckBox;
}

/**
 * This method initializes cddCheckBox	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox	
 */
private RCheckBox getCddCheckBox() {
	if (cddCheckBox == null) {
		cddCheckBox = new RCheckBox();
		cddCheckBox.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/deleteDirectoryRight\")%>");
		cddCheckBox.setStyleProperties("{/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/weightX \"1\"}");
		cddCheckBox.setName("cddCheckBox");
	}
	return cddCheckBox;
}

/**
 * This method initializes cwfCheckBox	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox	
 */
private RCheckBox getCwfCheckBox() {
	if (cwfCheckBox == null) {
		cwfCheckBox = new RCheckBox();
		cwfCheckBox.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/writeFiles\")%>");
		cwfCheckBox.setStyleProperties("{/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"/weightX \"1\"}");
		cwfCheckBox.setName("cwfCheckBox");
	}
	return cwfCheckBox;
}

/**
 * This method initializes cdfCheckBox	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox	
 */
private RCheckBox getCdfCheckBox() {
	if (cdfCheckBox == null) {
		cdfCheckBox = new RCheckBox();
		cdfCheckBox.setText("<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/deleteFiles\")%>");
		cdfCheckBox.setStyleProperties("{/insetsTop \"5\"/fill \"HORIZONTAL\"/insetsLeft \"5\"}");
		cdfCheckBox.setName("cdfCheckBox");
	}
	return cdfCheckBox;
}

/**
 * This method initializes mainLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getMainLabel() {
	if (mainLabel == null) {
		mainLabel = new RLabel();
		mainLabel.setText("<%= ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/security/selectLabel\") %>");
		mainLabel.setStyleProperties("{/font {/name \"Dialog\"/size \"16\"/style \"PLAIN\"}/fill \"HORIZONTAL\"/weightX \"1\"}");
		mainLabel.setName("mainLabel");
	}
	return mainLabel;
}

/**
 * This method initializes iconLabel	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RLabel	
 */
private RLabel getIconLabel() {
	if (iconLabel == null) {
		iconLabel = new RLabel();
		iconLabel.setName("iconLabel");
		iconLabel.setIconUri("<%=ivy.cms.cr(\"/ch/ivyteam/ivy/addons/icons/folder/48\")%>");
	}
	return iconLabel;
}

/**
 * This method initializes SplitPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RSplitPane	
 */
private RSplitPane getSplitPane() {
	if (SplitPane == null) {
		SplitPane = new RSplitPane();
		SplitPane.setName("SplitPane");
		SplitPane.setStyleProperties("{/dividerLocation \"0.5\"/orientation \"HORIZONTAL_SPLIT\"/fill \"BOTH\"}");
		SplitPane.setDividerLocation(300);
		SplitPane.setRightComponent(getRightGridBagLayoutPane());
		SplitPane.setLeftComponent(getLeftGridBagLayoutPane());
	}
	return SplitPane;
}

/**
 * This method initializes leftGridBagLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane	
 */
private RGridBagLayoutPane getLeftGridBagLayoutPane() {
	if (leftGridBagLayoutPane == null) {
		leftGridBagLayoutPane = new RGridBagLayoutPane();
		leftGridBagLayoutPane.setName("leftGridBagLayoutPane");
		leftGridBagLayoutPane.add(getIvyRolesLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		leftGridBagLayoutPane.add(getTreeScrollPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
	}
	return leftGridBagLayoutPane;
}

/**
 * This method initializes rightGridBagLayoutPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RGridBagLayoutPane	
 */
private RGridBagLayoutPane getRightGridBagLayoutPane() {
	if (rightGridBagLayoutPane == null) {
		rightGridBagLayoutPane = new RGridBagLayoutPane();
		rightGridBagLayoutPane.setName("rightGridBagLayoutPane");
		rightGridBagLayoutPane.add(getManagedActionsLabel(), new com.ulcjava.base.application.GridBagConstraints(0, 0, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
		rightGridBagLayoutPane.add(getActionsGridBagLayoutPane(), new com.ulcjava.base.application.GridBagConstraints(0, 1, 1, 1, -1, -1, com.ulcjava.base.application.GridBagConstraints.CENTER, com.ulcjava.base.application.GridBagConstraints.NONE, new com.ulcjava.base.application.util.Insets(0,0,0,0), 0, 0));
	}
	return rightGridBagLayoutPane;
}
}  //  @jve:decl-index=0:visual-constraint="10,10"