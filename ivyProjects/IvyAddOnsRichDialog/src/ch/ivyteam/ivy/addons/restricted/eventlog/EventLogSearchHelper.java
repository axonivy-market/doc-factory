package ch.ivyteam.ivy.addons.restricted.eventlog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog;
import ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogCase;
import ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchCriteria;
import ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogTask;
import ch.ivyteam.ivy.addons.eventlog.data.technical.EventLogSeverity;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.util.IvyDefaultValues;
import ch.ivyteam.ivy.workflow.IPropertyFilter;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.PropertyOrder;
import ch.ivyteam.ivy.workflow.eventlog.EventLogProperty;
import ch.ivyteam.ivy.workflow.eventlog.IEventLog;
import ch.ivyteam.logicalexpression.RelationalOperator;

/**
 * Helper class that searches into the event log tables.
 * 
 * @author Patrick Joly, TI-Informatique
 * @since 13.12.2010
 */
public class EventLogSearchHelper
{
  /**
   * Searches the event log tables and order the result on the case id to easily permit grouping the result by
   * case.
   * 
   * @param criteria Criteria used to search the event log tables
   * @return event list found
   * @throws PersistencyException
   */
  public static List<EventLog> searchByCase(EventLogSearchCriteria criteria, IWorkflowContext wf)
          throws PersistencyException
  {
    List<EventLog> result;
    List<EventLog> temp;
    List<PropertyOrder<EventLogProperty>> propertyOrder;
    long caseId;
    Set<Long> usedIds;

    result = new ArrayList<EventLog>();
    propertyOrder = new ArrayList<PropertyOrder<EventLogProperty>>();
    propertyOrder.add(new PropertyOrder<EventLogProperty>(EventLogProperty.TIMESTAMP));

    usedIds = new HashSet<Long>();

    for (IEventLog event : wf.findEventLogs(prepareCriteria(criteria), propertyOrder, 0, -1, true)
            .getResultList())
    {
      if (event.getCaseHistory() != null)
      {
        caseId = event.getTaskHistory().getCaseId();

        if (!usedIds.contains(caseId))
        {
          temp = EventLogHelper.readAllByCase(caseId, wf);
          if (temp != null)
          {
            result.addAll(temp);
            for (EventLog e : temp)
            {
              e.setEventLogCase(new EventLogCase());
              e.getEventLogCase().setCaseId((int)caseId);
              e.setEventLogTask(new EventLogTask());
              e.getEventLogTask().setCaseId((int)caseId);
            }
          }
          usedIds.add(caseId);
        }
      }
    }

    return result;
  }

  /**
   * Searches the event log tables and order the result on the group id to easily permit grouping the result
   * by group.
   * 
   * @param criteria Criteria used to search the event log tables
   * @return event list found
   * @throws PersistencyException
   */
  public static List<EventLog> searchByGroupId(EventLogSearchCriteria criteria) throws PersistencyException
  {
    List<EventLog> result;
    List<PropertyOrder<EventLogProperty>> propertyOrder;

    result = new ArrayList<EventLog>();
    propertyOrder = new ArrayList<PropertyOrder<EventLogProperty>>();
    propertyOrder.add(new PropertyOrder<EventLogProperty>(EventLogProperty.TIMESTAMP));
    Set<String> usedIds;

    usedIds = new HashSet<String>();
    for (IEventLog event : Ivy.wf().findEventLogs(prepareCriteria(criteria), propertyOrder, 0, -1, true)
            .getResultList())
    {
      if (event.getGroupId() != null && !event.getGroupId().equals("")
              && !usedIds.contains(event.getGroupId()))
      {
        result.addAll(EventLogHelper.getEventLogs(Ivy.wf().findEventLogs(
                Ivy.wf().createEventLogPropertyFilter(EventLogProperty.GROUPID, RelationalOperator.EQUAL,
                        event.getGroupId()), propertyOrder, 0, -1, true).getResultList()));
        usedIds.add(event.getGroupId());
      }
    }

    return result;
  }

  /**
   * Searches the event log tables and order the result by creation date.
   * 
   * @param criteria Criteria used to search the event log tables
   * @return event list found
   * @throws PersistencyException
   */
  public static List<EventLog> search(EventLogSearchCriteria criteria) throws PersistencyException
  {
    List<PropertyOrder<EventLogProperty>> propertyOrder;

    propertyOrder = new ArrayList<PropertyOrder<EventLogProperty>>();
    propertyOrder.add(new PropertyOrder<EventLogProperty>(EventLogProperty.TIMESTAMP));

    return EventLogHelper.getEventLogs(Ivy.wf().findEventLogs(prepareCriteria(criteria), propertyOrder, 0,
            -1, true).getResultList());
  }

  private static void addCriteriaList(
          ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchCriteria criteria,
          List<IPropertyFilter<EventLogProperty>> mainFilters, EventLogProperty property,
          RelationalOperator operator, List<EventLogSeverity> enums)
  {
    List<IPropertyFilter<EventLogProperty>> filters;
    IPropertyFilter<EventLogProperty> orFilter;
    ch.ivyteam.ivy.workflow.eventlog.EventLogSeverity severity;

    if (enums != null)
    {
      filters = new ArrayList<IPropertyFilter<EventLogProperty>>();

      for (EventLogSeverity object : enums)
      {
        switch (object)
        {
          case ERROR:
            severity = ch.ivyteam.ivy.workflow.eventlog.EventLogSeverity.ERROR;
            break;
          case FATAL:
            severity = ch.ivyteam.ivy.workflow.eventlog.EventLogSeverity.FATAL;
            break;
          case INFO:
            severity = ch.ivyteam.ivy.workflow.eventlog.EventLogSeverity.INFO;
            break;
          case WARNING:
            severity = ch.ivyteam.ivy.workflow.eventlog.EventLogSeverity.WARNING;
            break;
          default:
            continue;
        }
        filters.add(Ivy.wf().createEventLogPropertyFilter(property, operator, severity.intValue()));
      }

      orFilter = null;
      for (IPropertyFilter<EventLogProperty> filter : filters)
      {
        if (orFilter == null)
        {
          orFilter = filter;
        }
        else
        {
          orFilter = orFilter.or(filter);
        }
      }
      if (orFilter != null)
      {
        mainFilters.add(orFilter);
      }
    }
  }

  private static IPropertyFilter<EventLogProperty> prepareCriteria(
          ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchCriteria criteria)
  {
    List<IPropertyFilter<EventLogProperty>> filters;
    IPropertyFilter<EventLogProperty> andFilter;

    filters = new ArrayList<IPropertyFilter<EventLogProperty>>();

    addStringCriteria(criteria, filters, EventLogProperty.SERVER, RelationalOperator.EQUAL, criteria
            .getServer());
    addStringCriteria(criteria, filters, EventLogProperty.APPLICATION_NAME, RelationalOperator.EQUAL,
            criteria.getApplication());
    addStringCriteria(criteria, filters, EventLogProperty.SYSTEM, RelationalOperator.EQUAL, criteria
            .getTower());
    addStringCriteria(criteria, filters, EventLogProperty.SUBSYSTEM, RelationalOperator.EQUAL, criteria
            .getModule());
    addStringCriteria(criteria, filters, EventLogProperty.ENVIRONMENT, RelationalOperator.EQUAL, criteria
            .getEnvironment());
    addStringCriteria(criteria, filters, EventLogProperty.GROUPID, RelationalOperator.EQUAL, criteria
            .getGroupId());
    addStringCriteria(criteria, filters, EventLogProperty.INITIATOR, RelationalOperator.EQUAL, criteria
            .getInitiator());
    addStringCriteria(criteria, filters, EventLogProperty.USER_NAME, RelationalOperator.EQUAL, criteria
            .getUserName());
    addStringCriteria(criteria, filters, EventLogProperty.SOURCE, RelationalOperator.EQUAL, criteria
            .getSource());
    addStringCriteria(criteria, filters, EventLogProperty.OBJECT_ID, RelationalOperator.EQUAL, criteria
            .getObjectId());
    addStringCriteria(criteria, filters, EventLogProperty.CONTEXT, RelationalOperator.EQUAL, criteria
            .getContext());
    addBooleanCriteria(criteria, filters, EventLogProperty.IS_BUSINESS_EVENT, RelationalOperator.EQUAL,
            (criteria.getIsBusinessEvent() != null && criteria.getIsBusinessEvent())? new Integer(1): new Integer(0));
    addStringCriteria(criteria, filters, EventLogProperty.EVENT_TYPE, RelationalOperator.EQUAL, criteria
            .getEventType());
    addStringCriteria(criteria, filters, EventLogProperty.EVENT_SUB_TYPE, RelationalOperator.EQUAL, criteria
            .getEventSubType());
    addStringCriteria(criteria, filters, EventLogProperty.USER_COMMENT, RelationalOperator.EQUAL, criteria
            .getUserComment());
    addStringCriteria(criteria, filters, EventLogProperty.ERROR_CODE, RelationalOperator.EQUAL, criteria
            .getErrorCode());
    addCriteriaList(criteria, filters, EventLogProperty.SEVERITY, RelationalOperator.EQUAL, criteria
            .getSeverities());
    addDateCriteria(criteria, filters, EventLogProperty.EVENT_DATE, RelationalOperator.EQUAL_OR_LARGER,
            criteria.getEventDateFrom());
    addDateCriteria(criteria, filters, EventLogProperty.EVENT_DATE, RelationalOperator.EQUAL_OR_SMALLER,
            criteria.getEventDateTo());

    andFilter = null;
    for (IPropertyFilter<EventLogProperty> filter : filters)
    {
      if (andFilter == null)
      {
        andFilter = filter;
      }
      else
      {
        andFilter = andFilter.and(filter);
      }
    }

    return andFilter;
  }

  private static void addStringCriteria(
          ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchCriteria criteria,
          List<IPropertyFilter<EventLogProperty>> filters, EventLogProperty property,
          RelationalOperator operator, String string)
  {
    if (!IvyDefaultValues.isEqualToDefaultObject(string) && !"".equals(string))
    {
      filters.add(Ivy.wf().createEventLogPropertyFilter(property, operator, string));
    }
  }

  private static void addDateCriteria(
          ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchCriteria criteria,
          List<IPropertyFilter<EventLogProperty>> filters, EventLogProperty property,
          RelationalOperator operator, Date date)
  {
    if (!IvyDefaultValues.isEqualToDefaultObject(date))
    {
      filters.add(Ivy.wf().createEventLogPropertyFilter(property, operator, date));
    }
  }
  
  private static void addBooleanCriteria(EventLogSearchCriteria criteria,
          List<IPropertyFilter<EventLogProperty>> filters, EventLogProperty property,
          RelationalOperator operator, Integer value)
  {
    if (value != null)
    {
      filters.add(Ivy.wf().createEventLogPropertyFilter(property, operator, value));
    } 
    
  }
  
}