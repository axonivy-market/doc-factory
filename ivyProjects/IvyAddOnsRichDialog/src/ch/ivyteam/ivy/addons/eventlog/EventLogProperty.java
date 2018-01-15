/**
 * 
 */
package ch.ivyteam.ivy.addons.eventlog;

import ch.ivyteam.ivy.workflow.IProperty;

/**
 * @author nhvu
 *
 */
public enum EventLogProperty implements IProperty {

	ID("EventLogId"), 

	TIMESTAMP("Timestamp"), 

	SERVER("Server"), 

	SYSTEM("System"), 

	SUBSYSTEM("Subsystem"), 

	APPLICATION_NAME("ApplicationName"), 

	ENVIRONMENT("Environment"), 

	GROUPID("GroupId"), 

	EVENT_DATE("EventDate"), 

	EVENT_TIME("EventTime"), 

	INITIATOR("Initiator"), 

	USER_NAME("UserName"), 

	SOURCE("Source"), 

	SEVERITY("Severity"), 

	OBJECT_ID("ObjectId"), 

	CONTEXT("Context"), 

	IS_BUSINESS_EVENT("IsBusinessEvent"), 

	EVENT_TYPE("EventType"), 

	EVENT_SUB_TYPE("EventSubType"), 

	USER_COMMENT("UserComment"), 

	ERROR_CODE("ErrorCode"), 

	MESSAGE("Message"),
	
	STATUS("Status");

	private String name;

	private EventLogProperty(String _name)
	{
		this.name = _name;
	}


	@Override
	public String getName() {		
		return this.name;
	}

}
