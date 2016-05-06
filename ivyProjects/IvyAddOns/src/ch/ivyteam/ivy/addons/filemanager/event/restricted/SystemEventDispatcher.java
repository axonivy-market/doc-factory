package ch.ivyteam.ivy.addons.filemanager.event.restricted;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.event.SystemEvent;
import ch.ivyteam.ivy.event.SystemEventCategory;
import ch.ivyteam.ivy.event.ISystemEventDispatcher.DispatchStatus;

public class SystemEventDispatcher {

	public static DispatchStatus sendSystemEvent (String eventName, Object parameter) {
		DispatchStatus status = Ivy.wf().getApplication().sendSystemEvent(
				new SystemEvent<>(SystemEventCategory.THIRD_PARTY, eventName, parameter)
			);
		return status;
	}
}
