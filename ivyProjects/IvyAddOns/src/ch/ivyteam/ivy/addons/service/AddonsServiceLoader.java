package ch.ivyteam.ivy.addons.service;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.application.ReleaseState;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * This class contains a loadService static method allowing loading services (plugins) dynamically.<br>
 * The basic contract is to match to the java.util.ServiceLoader conventions.
 */
public class AddonsServiceLoader {

	/**
	 * Where the plugin jars have to be located in the project.
	 */
	public static String PLUGIN_JAR_DIR="/lib";

	/**
	 * This method allows loading services (Plugin) with a the Ivyaddons ProjectClassLoader.<br>
	 * See java.util.ServiceLoader documentation.
	 * @param serviceClass The Interface class Type that has to be loaded
	 * @param loadServiceFromOtherProjects : if true the plugins are looked up in all the workspace's projects PLUGIN_JAR_DIR directories.<br>
	 * The plugins jars are going to be added to the project's classpath if they are not already in it.
	 * @param optionalFileFilter : an optional FileFilter to be able to filter the jars to add
	 * @return the set of instantiated plugin classes.
	 * @throws Exception
	 */
	public static <T> Set<T> loadServiceWithTheIvyAddonsProjectClassLoader(Class<T> serviceClass, boolean loadServiceFromOtherProjects, FileFilter optionalFileFilter)
			throws Exception {
		return loadServiceWithUrlClassLoader(serviceClass, (URLClassLoader) com.aspose.words.Document.class.getClassLoader(), 
				loadServiceFromOtherProjects, optionalFileFilter);

	}
	
	/**
	 * This method allows loading services (Plugin) with a given UrlClassLoader.<br>
	 * See java.util.ServiceLoader documentation.
	 * @param serviceClass The Interface class Type that has to be loaded
	 * @param classLoader The URLClassLoader that should load the services
	 * @param loadServiceFromOtherProjects : if true the plugins are looked 
	 * up in all the workspace's projects PLUGIN_JAR_DIR directories.<br>
	 * The plugins jars are going to be added to the project's classpath if they are not already in it.
	 * @param optionalFileFilter an optional FileFilter to be able to filter the jars to add
	 * @return the set of instantiated plugin classes.
	 * @throws Exception
	 */
	public static <T> Set<T> loadServiceWithUrlClassLoader(
			Class<T> serviceClass, URLClassLoader classLoader,
			boolean loadServiceFromOtherProjects, FileFilter optionalFileFilter)
			throws Exception {
		Set<T> result = new HashSet<T>();
		addInternLibsToClassLoaderIfNotIncludedYet(classLoader,
				optionalFileFilter);
		if (loadServiceFromOtherProjects) {
			addExtLibsToClassLoader(classLoader, optionalFileFilter);
		}

		ServiceLoader<T> loader = ServiceLoader.load(serviceClass, classLoader);
		Iterator<T> iter = loader.iterator();
		while (iter.hasNext()) {
			result.add(iter.next());
		}

		return result;
	}

	/**
	 * Used internally to add jars located recursively under "/lib" and that are not in the project's classloader yet.
	 * @param urlClassLoader
	 * @param optionalFileFilter
	 * @throws Exception
	 */
	private static void addInternLibsToClassLoaderIfNotIncludedYet(URLClassLoader urlClassLoader, FileFilter optionalFileFilter)
			throws Exception {
			List<IProcessModelVersion> pmvs = Ivy.request().getProcessModel().getProcessModelVersions();
			addReleasedProcessModelVersionsLibsToClassPath(pmvs, urlClassLoader, optionalFileFilter);
	}
	
	/**
	 * 
	 * Used internally to add jars located recursively under "/lib" in other projects and that are not in the project's classloader yet.
	 * @param urlClassLoader
	 * @param optionalFileFilter
	 * @throws Exception
	 */
	private static void addExtLibsToClassLoader(URLClassLoader urlClassLoader, FileFilter optionalFileFilter)
			throws Exception {
			List<IProcessModel> pms = Ivy.wf().getApplication().getProcessModels();
			List<IProcessModelVersion> pmvs = new ArrayList<IProcessModelVersion>();
			for (IProcessModel pm : pms) {
				if (Ivy.request().getProcessModel().equals(pm)) 
					continue;

				pmvs.addAll(pm.getProcessModelVersions());
			}
			addReleasedProcessModelVersionsLibsToClassPath(pmvs, urlClassLoader, optionalFileFilter);
	}
	
	private static void addReleasedProcessModelVersionsLibsToClassPath(List<IProcessModelVersion> pmvs, 
			URLClassLoader urlClassLoader, FileFilter optionalFileFilter) throws Exception {
		if (urlClassLoader == null || pmvs ==null || pmvs.isEmpty()) return; 
			
		Set<java.io.File> jarsToAddToClassPath = new HashSet<java.io.File>();
		for (IProcessModelVersion pmv : pmvs) {
			if (pmv.getReleaseState() != ReleaseState.RELEASED)
				continue;

			jarsToAddToClassPath.addAll(getJarsNotAllReadyInClasspathLocatedUnderGivenPath(
					getJarNamesSetAlreadyInGivenClassLoaderPath(urlClassLoader), 
					pmv.getProjectDirectory() + PLUGIN_JAR_DIR, optionalFileFilter));
		}
		addFilesToClassPath(jarsToAddToClassPath, urlClassLoader);
	}

	

	protected static Set<java.io.File> getJarsNotAllReadyInClasspathLocatedUnderGivenPath(Set<String> jarsInClassPath, String path,
			FileFilter optionalFileFilter) {
		Set<java.io.File> files = new HashSet<java.io.File>();
		if(path==null || path.trim().isEmpty()) return files;

		java.io.File dir = new java.io.File(path);
		if(!dir.isDirectory()) return files;

		Iterator<File> jars = FileUtils.iterateFiles(dir, new String[]{"jar"}, true);
		while (jars.hasNext()) {
			File jar = jars.next();
			if (acceptFileAsJarNotAlreadyInPath(jar,jarsInClassPath,optionalFileFilter)) {
				files.add(jar);
			}
		}

		return files;
	}
	
	private static Set<String> getJarNamesSetAlreadyInGivenClassLoaderPath(
			URLClassLoader urlClassLoader) {
		URL [] urls = urlClassLoader.getURLs();
		Set<String> jarsInClassPath = new HashSet<String>();
		for(URL url: urls) {
			ifUrlPointsToJarAddJarNameToStringSet(jarsInClassPath, url);
		}
		return jarsInClassPath;
	}
	
	protected static void ifUrlPointsToJarAddJarNameToStringSet(
			Set<String> jarsInClassPath, URL url) {
		if(url.getFile().toLowerCase().endsWith(".jar")) {
			String s = url.getFile();
			s= s.replaceAll("\\\\", "/");
			if(s.contains("/")) {
				s=s.substring(s.lastIndexOf("/")+1);
			}
			jarsInClassPath.add(s);
		}
	}

	protected static boolean acceptFileAsJarNotAlreadyInPath(java.io.File jar,Set<String> jarsInClassPath,FileFilter optionalFileFilter) {
		return (jar!=null && jar.isFile() && (jarsInClassPath==null || !jarsInClassPath.contains(jar.getName())) 
				&& (optionalFileFilter==null || optionalFileFilter.accept(jar)));
	}
	
	private static void addFilesToClassPath(Set<java.io.File> jars, URLClassLoader urlClassLoader) throws Exception {
		if (urlClassLoader != null && jars !=null && !jars.isEmpty()) {
			final Class<?>[] parameters = new Class[] { URL.class };
			Class<URLClassLoader> sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			for(java.io.File f:jars) {
				method.invoke(urlClassLoader,new Object[] { f.toURI().toURL() });
			}
		}
	}
}
