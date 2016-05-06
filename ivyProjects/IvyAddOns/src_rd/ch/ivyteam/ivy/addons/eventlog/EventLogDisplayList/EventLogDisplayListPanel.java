package ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ivyteam.anything.Anything;
import ch.ivyteam.ivy.addons.eventlog.EventLogProperty;
import ch.ivyteam.ivy.addons.widgets.RTableWithExcelExport;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.richdialog.rdpanels.RichDialogBorderPanel;
import ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane;

import com.ulcjava.base.application.ULCTable;

/**
 * RichDialog panel implementation for EventLogDisplayListPanel.
 * @author <%=author%>
 * @since <%=date%>
 */
public class EventLogDisplayListPanel extends RichDialogBorderPanel 
implements IRichDialogPanel 
{ 
  /** Serial version id */
  private static final long serialVersionUID = 1L;
private RScrollPane eventLogsScrollPane = null;
private RTableWithExcelExport eventLogsTable = null;
private String modelConfiguration;
  
  /**
   * Create a new instance of EventLogDisplayListPanel
   */
  public EventLogDisplayListPanel()
  {
    super();
    initialize();
  }
  
  /**
   * This method initializes EventLogDisplayListPanel
   * @return void
   */
  private void initialize()
  {
        this.setPreferredSize(new com.ulcjava.base.application.util.Dimension(554,368));
        this.add(getEventLogsScrollPane(), com.ulcjava.base.application.ULCBorderLayoutPane.CENTER);
  }

/**
 * This method initializes eventLogsScrollPane	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.containers.RScrollPane	
 */
private RScrollPane getEventLogsScrollPane() {
	if (eventLogsScrollPane == null) {
		eventLogsScrollPane = new RScrollPane();
		eventLogsScrollPane.setName("eventLogsScrollPane");
		eventLogsScrollPane.setStyleProperties("{/horizontalScrollBarPolicy \"AS_NEEDED\"}");
		eventLogsScrollPane.setViewPortView(getEventLogsTable());
	}
	return eventLogsScrollPane;
}

/**
 * This method initializes eventLogsTable	
 * 	
 * @return ch.ivyteam.ivy.richdialog.widgets.components.RTable	
 */
private RTableWithExcelExport getEventLogsTable() {
	if (eventLogsTable == null) {
		eventLogsTable = new RTableWithExcelExport();
		eventLogsTable.setName("eventLogsTable");
		eventLogsTable.setSortable(true);
		eventLogsTable.setName("eventLogsTable");
		eventLogsTable.setRowHeight(20);
		eventLogsTable.setAutoResizeMode(ULCTable.AUTO_RESIZE_ALL_COLUMNS);
		eventLogsTable.setModelConfiguration("{/emptyTableText \"\"/version \"3.0\"/showTableheader true /autoTableheader false /showtooltip false /showIcons true /columns {{/result \"result=#value is initialized ? value.format((ivy.var.xivy_addons_restricted_dateFormatPattern)) : null\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventDate\\\")\"/field \"data.eventDate\"/editable \"\"/condition \"\"/columnWidth \"60\"/cellWidget \"\"}{/result \"result=#value is initialized ? value.format() : null\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventTime\\\")\"/field \"data.eventTime\"/editable \"\"/condition \"\"/columnWidth \"60\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventType\\\")\"/field \"data.eventType\"/editable \"\"/condition \"\"/columnWidth \"100\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"eventSubType\\\")\"/field \"data.eventSubType\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"objectId\\\")\"/field \"data.objectId\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"message\\\")\"/field \"data.message\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value is initialized ? value.toString() : null\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"severity\\\")\"/field \"data.severity\"/editable \"\"/condition \"\"/columnWidth \"60\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"groupId\\\")\"/field \"data.groupId\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=null\"/version \"3.0\"/tooltip \"\"/icon \"result=#value is initialized && value ? ivy.cms.cr(\\\"/ch/ivyteam/ivy/addons/eventlog/EventLogDisplayTree/icons/True16\\\") : null\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"isBusinessEvent\\\")\"/field \"data.isBusinessEvent\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"initiator\\\")\"/field \"data.initiator\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"userName\\\")\"/field \"data.userName\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"source\\\")\"/field \"data.source\"/editable \"\"/condition \"\"/cellWidget \"\"}{/result \"result=#value\"/version \"3.0\"/tooltip \"\"/icon \"\"/header \"ch.ivyteam.ivy.addons.cmscontext.Cms.co(in.cmsContext, \\\"status\\\")\"/field \"status\"/editable \"\"/condition \"\"/columnWidth \"100\"/cellWidget \"\"}}}");
	}
	return eventLogsTable;
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
		modelConfiguration = getEventLogsTable().getModelConfiguration();
	}
	model = Anything.read(modelConfiguration);
	columnDefinitions = model.get("columns");
	
	for (Anything columnDefinition : columnDefinitions.elements()) {
		fieldDefinition = columnDefinition.get(
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
		} else if (fieldDefinition.equals("status")) { 
			currentColumn = EventLogProperty.STATUS;
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
	getEventLogsTable().setModelConfiguration(model.toString());
}
}  //  @jve:decl-index=0:visual-constraint="10,10"