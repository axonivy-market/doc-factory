/**
 * 
 */
package ch.ivyteam.ivy.addons.service;

import static org.junit.Assert.*;

import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ec
 *
 */
public class AddonsServiceLoaderTest {

	
	@Test
	public void testIfUrlPointsToJarAddJarNameToStringSet_validJarUrl_urlAdded() {
		Set<String> jarsInClassPath = new HashSet<String>();
		try {
			URL url = new URL("file:///your/file/here/cool.jar");
			AddonsServiceLoader.ifUrlPointsToJarAddJarNameToStringSet(jarsInClassPath, url);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		assertTrue(jarsInClassPath.contains("cool.jar"));
	}
	
	@Test
	public void testIfUrlPointsToJarAddJarNameToStringSet_JarFile_urlAdded() {
		Set<String> jarsInClassPath = new HashSet<String>();
		try {
			java.io.File f = new java.io.File("/tmp/cool.jar");
			AddonsServiceLoader.ifUrlPointsToJarAddJarNameToStringSet(jarsInClassPath, f.toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		assertTrue(jarsInClassPath.contains("cool.jar"));
	}
	
	@Test
	public void testIfUrlPointsToJarAddJarNameToStringSet_UrlNotAJar_ResourceNotAdded() {
		Set<String> jarsInClassPath = new HashSet<String>();
		try {
			URL url = new URL("file:///your/file/here/cool.properties");
			AddonsServiceLoader.ifUrlPointsToJarAddJarNameToStringSet(jarsInClassPath, url);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		assertTrue(jarsInClassPath.isEmpty());
	}
	
	@Test
	public void acceptFileAsJarNotAlreadyInPathTest_NoFilter_JarAccepted() {
		java.io.File jar = mock(java.io.File.class);
		when(jar.getName()).thenReturn("thumbnail.jar");
		when(jar.getPath()).thenReturn("test/cool/thumbnail.jar");
		when(jar.isFile()).thenReturn(true);
		boolean b =AddonsServiceLoader.acceptFileAsJarNotAlreadyInPath(jar, generateJarNamesSet(),null);
		assertTrue(b);
	}
	
	@Test
	public void acceptFileAsJarNotAlreadyInPathTest_NoFilter_JarNotAccepted_CauseAlreadyInPath() {
		java.io.File jar = mock(java.io.File.class);
		when(jar.getName()).thenReturn("cool.jar");
		when(jar.getPath()).thenReturn("test/cool/cool.jar");
		when(jar.isFile()).thenReturn(true);
		boolean b =AddonsServiceLoader.acceptFileAsJarNotAlreadyInPath(jar, generateJarNamesSet(),null);
		assertFalse(b);
	}
	
	@Test
	public void acceptFileAsJarNotAlreadyInPathTest_ThumbnailerPluginFilter_JarAccepted() {
		java.io.File jar = mock(java.io.File.class);
		when(jar.getName()).thenReturn("zzz_plugin_thumbnail.jar");
		when(jar.getPath()).thenReturn("test/cool/zzz_plugin_thumbnail.jar");
		when(jar.isFile()).thenReturn(true);
		FileFilter filter =new ServiceLoaderPluginFilter(null,null);
		boolean b =AddonsServiceLoader.acceptFileAsJarNotAlreadyInPath(jar, generateJarNamesSet(),filter);
		assertTrue(b);
	}
	
	@Test
	public void acceptFileAsJarNotAlreadyInPathTest_ThumbnailerPluginFilter_JarNotAccepted_CauseNotAcceptedByFilter() {
		java.io.File jar = mock(java.io.File.class);
		when(jar.getName()).thenReturn("zzz_thumbnail.jar");
		when(jar.getPath()).thenReturn("test/cool/zzz_thumbnail.jar");
		when(jar.isFile()).thenReturn(true);
		FileFilter filter =new ServiceLoaderPluginFilter(null,"plugin");
		boolean b =AddonsServiceLoader.acceptFileAsJarNotAlreadyInPath(jar, generateJarNamesSet(),filter);
		assertFalse(b);
	}
	
	private Set<String> generateJarNamesSet() {
		Set<String> set = new HashSet<String>();
		set.add("cool.jar");
		set.add("log4j.jar");
		return set;
	}

}
