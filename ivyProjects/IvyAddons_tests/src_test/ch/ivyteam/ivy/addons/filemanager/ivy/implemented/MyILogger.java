package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import org.apache.log4j.Priority;

import ch.ivyteam.api.IvyScriptVisibility;
import ch.ivyteam.api.PublicAPI;
import ch.ivyteam.log.Logger;

public class MyILogger extends Logger {

	public MyILogger(String _name) {
		super(_name);
		// TODO Auto-generated constructor stub
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void debug(String formatedMessage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void debug(String formatedMessage, Throwable throwable,
			Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void error(Object message) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void error(String formatedMessage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void error(String formatedMessage, Throwable throwable,
			Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void fatal(String formatedMessage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void fatal(String formatedMessage, Throwable throwable,
			Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void info(Object message) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void info(String formatedMessage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void info(String formatedMessage, Throwable throwable,
			Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void log(Priority level, String formatedMessage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void log(Priority level, String formatedMessage,
			Throwable throwable, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void warn(Object message) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public void warn(String formatedMessage, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void warn(String formatedMessage, Throwable throwable,
			Object... args) {
		// TODO Auto-generated method stub

	}

}
