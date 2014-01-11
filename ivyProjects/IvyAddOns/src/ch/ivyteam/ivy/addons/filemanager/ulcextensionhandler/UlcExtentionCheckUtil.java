package ch.ivyteam.ivy.addons.filemanager.ulcextensionhandler;

import java.lang.reflect.Method;

import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author Emmanuel Comba, ecomba@soreco.ch
 * Utility Class to check if some classes are present in the ulc extensions and to introspect them.
 */
public class UlcExtentionCheckUtil {
	
	/**
	 * private constructor for this utility class that just contains static methods.
	 */
	private UlcExtentionCheckUtil(){
		
	}
	
	/**
	 * Checks if a given method with the given arguments types is implemented by the given class
	 * @param className the class name, if this class cannot be found in the classpath, the method returns false
	 * @param methodName the method name, if no such a method with the given arguments types can be found, returns false, else true.
	 * @param args the arguments Class types. If the method has no arguments, please give null.
	 * @return returns true if the given method with the arguments types from the specified class can be found, else false.
	 */
	@SuppressWarnings("restriction")
	public static boolean ulcMethodExist(String className, String methodName, Class<?>... args){
		boolean result = false;
		try{
			Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
			
			c.getDeclaredMethod(methodName, args);
			result = true;
		}catch(Exception ex){
			Ivy.log().debug("The method {0} with the args {1} for the Class {2} does not exist.", className, args, methodName);
		}
		if(!result) {
			try {
				Ivy.log().info("calling ulcMethodExist {0} {1} {2}",className,methodName,Ivy.request().getProject().getProjectClassLoader());
				Class<?> c = Ivy.request().getProject().getProjectClassLoader().loadClass(className);
				c.getDeclaredMethod(methodName, args);
				result = true;
			}catch(Exception ex){
				Ivy.log().debug("The method {0} with the args {1} for the Class {2} does not exist.", className, args, methodName);
			}finally {
				
			}
		}
		return result;
	}
	
	/**
	 * Invokes a specified method on the given object.
	 * @param o The Object that declares the method to be invoked
	 * @param methodName The method name
	 * @param arguments The arguments to pass to the methods as Object[]
	 * @param args The Class arguments corresponding to the method arguments
	 * @return null if no such method could be found, or the Object returned by the method, else if the method is void returns true.
	 */
	public static Object invokeMethod(Object o, String methodName, Object[] arguments, Class<?>... args){
		Object result = null;
		try{
			Class<?> c = o.getClass();
			Method m = c.getDeclaredMethod(methodName, args);
			result = m.invoke(o, arguments);
			if(result == null){
				result = true;
			}
		}catch(Exception ex){
			Ivy.log().error("The method {0} with the args {1} for the Object {2} does not exist.", o, args, methodName);
		}
		return result;
	}

}
