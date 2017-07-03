/**
 * 
 */
package ch.ivyteam.ivy.docfactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	public static Server get() {
		return new Server();
	}
	
	public boolean isRunning() {
		try {
			Class<?> ivyServer = Class.forName("ch.ivyteam.ivy.server.IServer");
			Class<?> diCore = Class.forName("ch.ivyteam.di.restricted.DiCore");
			Object injector = diCore.getMethod("getGlobalInjector").invoke(null);
			Object server = injector.getClass().getMethod("getInstance", Class.class).invoke(injector, ivyServer);
			Boolean isRunning = (Boolean) ivyServer.getMethod("isRunning").invoke(server);
			return isRunning.booleanValue();
		}
		catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Exception occurred while checking if the Ivy Server is running", ex);
			return false;
		}
	}

}
