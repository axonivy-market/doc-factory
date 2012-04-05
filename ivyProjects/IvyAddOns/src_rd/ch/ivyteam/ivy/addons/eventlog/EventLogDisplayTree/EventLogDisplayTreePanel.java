package ch.ivyteam.ivy.addons.eventlog.EventLogDisplayTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ivyteam.anything.Anything;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogGridBagPanel;
import ch.ivyteam.ivy.richdialog.widgets.components.RTableTree;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;
import ch.ivyteam.ivy.workflow.eventlog.EventLogProperty;

import com.ulcjava.base.application.ULCTableTree;

/**
 * RichDialog panel implementation for EventLogDisplayTreePanel.
 * 
 * @author <%=author%>
 * @since <%=date%>
 */
public class EventLogDisplayTreePanel extends RichDialogGridBagPanel implements
		IRichDialogPanel {
	/** Serial version id */
	private static final long serialVersionUID = 1L;
	private RScrollPane ScrollPane = null;
	private RTableTree tableTree = null;
	private String modelConfiguration;

	/**
	 * Create a new instance of EventLogDisplayTreePanel
	 */
	public EventLogDisplayTreePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes EventLogDisplayTreePanel
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(
				695, 395));
		this
				.add(
						getScrollPane(),
						new com.ulcjava.base.application.GridBagConstraints(
								0,
								1,
								1,
								1,
								-1,
								-1,
								com.ulcjava.base.application.GridBagConstraints.CENTER,
								com.ulcjava.base.application.GridBagConstraints.NONE,
								new com.ulcjava.base.application.util.Insets(0,
										0, 0, 0), 0, 0));
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
			ScrollPane
					.setStyleProperties("{/fill \"BOTH\"/weightY \"1\"/weightX \"1\"}");
			ScrollPane.setViewPortView(getTableTree());
		}
		return ScrollPane;
	}

	/**
	 * This method initializes TableTree
	 * 
	 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTableTree
	 */
	private RTableTree getTableTree() {
		if (tableTree == null) {
			tableTree = new RTableTree();
			tableTree.setName("tableTree");
			tableTree.setAutoResizeMode(ULCTableTree.AUTO_RESIZE_OFF);
			tableTree.setRootVisible(false);
			tableTree
					.setModelConfiguration("{/dynamicTreeLoadMode \"LOAD_FOR_RENDER_PARENT\"/version \"3.0\"/showTableheader true /autoTableheader false /showtooltip false /showIcons true /emptyTableText \"\"/columns {{/patterns {{/result \"result=value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventDate\\\")\"/field \"\"/columnWidth \"150\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value is initialized ? value.toJavaDate().format(\\\"dd/MM/yyyy\\\") : null\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"\"/field \"data.eventDate\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventTime\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/header \"\"/field \"data.eventTime\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventType\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/header \"\"/field \"data.eventType\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventSubType\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/header \"\"/field \"data.eventSubType\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"objectId\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/header \"\"/field \"data.objectId\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"message\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/header \"\"/field \"data.message\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"severity\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value is initialized ? value.toString() : null\"/version \"3.0\"/header \"\"/field \"data.severity\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"groupId\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/header \"\"/field \"data.groupId\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"isBusinessEvent\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=null\"/version \"3.0\"/icon \"result=#value is initialized && value ? ivy.cms.cr(\\\"/ch/ivyteam/ivy/addons/eventlog/EventLogDisplayTree/icons/True16\\\") : null\"/header \"\"/field \"data.isBusinessEvent\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"initiator\\\")\"/version \"3.0\"/field \"\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/field \"data.initiator\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"userName\\\")\"/version \"3.0\"/field \"\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/icon \"\"/field \"data.userName\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}{/patterns {{/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"source\\\")\"/version \"3.0\"/patternMode \"ALL\"/patternValue \"default\"}{/result \"result=#value\"/version \"3.0\"/icon \"\"/header \"\"/field \"data.source\"/patternMode \"INSTANCE\"/patternValue \"ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog\"}}/version \"3.0\"}}}");
		}
		return tableTree;
	}

	/**
	 * Updates the model configuration to display only some columns.
	 * 
	 * @param columns
	 *            list of the column to display in the right sequence. Only
	 *            EVENT_DATE, EVENT_TIME, EVENT_TYPE, EVENT_SUB_TYPE, OBJECT_ID
	 *            MESSAGE, SEVERITY, GROUPID, IS_BUSINESS_EVENT, INITIATOR,
	 *            USER_NAME, SOURCE can be used.
	 */
	public void setModel(List<EventLogProperty> columns) {
		Anything model;
		String fieldDefinition;
		EventLogProperty currentColumn;
		Anything columnDefinitions;

		Map<EventLogProperty, Anything> columnDefinitionMap;

		columnDefinitionMap = new HashMap<EventLogProperty, Anything>();

		if (modelConfiguration == null) {
			modelConfiguration = getTableTree().getModelConfiguration();
		}
		model = Anything.read(modelConfiguration);
		columnDefinitions = model.get("columns");

		for (Anything columnDefinition : columnDefinitions.elements()) {
			fieldDefinition = columnDefinition.get("patterns").get(1).get(
					"field").asString("");

			if (fieldDefinition.equals("data.eventDate")) {
				currentColumn = EventLogProperty.EVENT_DATE;
			} else if (fieldDefinition.equals("data.eventTime")) {
				currentColumn = EventLogProperty.EVENT_TIME;
			} else if (fieldDefinition.equals("data.eventType")) {
				currentColumn = EventLogProperty.EVENT_TYPE;
			} else if (fieldDefinition.equals("data.eventSubType")) {
				currentColumn = EventLogProperty.EVENT_SUB_TYPE;
			} else if (fieldDefinition.equals("data.objectId")) {
				currentColumn = EventLogProperty.OBJECT_ID;
			} else if (fieldDefinition.equals("data.message")) {
				currentColumn = EventLogProperty.MESSAGE;
			} else if (fieldDefinition.equals("data.severity")) {
				currentColumn = EventLogProperty.SEVERITY;
			} else if (fieldDefinition.equals("data.groupId")) {
				currentColumn = EventLogProperty.GROUPID;
			} else if (fieldDefinition.equals("data.isBusinessEvent")) {
				currentColumn = EventLogProperty.IS_BUSINESS_EVENT;
			} else if (fieldDefinition.equals("data.initiator")) {
				currentColumn = EventLogProperty.INITIATOR;
			} else if (fieldDefinition.equals("data.userName")) {
				currentColumn = EventLogProperty.USER_NAME;
			} else if (fieldDefinition.equals("data.source")) {
				currentColumn = EventLogProperty.SOURCE;
			} else {
				currentColumn = null;
			}
			if (currentColumn != null) {
				columnDefinitionMap.put(currentColumn, columnDefinition);
			}
		}
		while (columnDefinitions.size() != 0) {
			columnDefinitions.remove(0);
		}
		for (EventLogProperty column : columns) {
			columnDefinitions.append(columnDefinitionMap.get(column));
		}
		getTableTree().setModelConfiguration(model.toString());
	}
} // @jve:decl-index=0:visual-constraint="10,10"