/**
 * 
 */
package ch.ivyteam.ivy.addons.service;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * @author ec
 *
 */
public class ServiceLoaderPluginFilterTest {

	
	@Test
	public void testAccept_illegalFileWithNoNameRejected() {
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter();
		java.io.File f = new java.io.File("");
		assertFalse(filter.accept(f));
	}
	
	@Test
	public void testAccept_nullFileRejected() {
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter();
		assertFalse(filter.accept(null));
	}
	
	@Test
	public void testAccept_ValidFileAccepted() {
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter();
		assertTrue(filter.accept(new java.io.File("test.jar")));
	}
	
	@Test
	public void testAccept_filterWithPathConvention_FileAccepted() {
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(null,"/lib/");
		assertTrue(filter.accept(new java.io.File("c:\\test\\lib\\test.jar")));
	}
	
	@Test
	public void testAccept_filterWithExtension_FileAccepted() {
		Set<String> exts = new HashSet<String>();
		exts.add("jar");
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(exts,null);
		assertTrue(filter.accept(new java.io.File("c:\\test\\lib\\test.jar")));
	}
	
	@Test
	public void testAccept_filterWithExtensionAndPathConvention_FileAccepted() {
		Set<String> exts = new HashSet<String>();
		exts.add("jar");
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(exts,"/lib/");
		assertTrue(filter.accept(new java.io.File("c:\\test\\lib\\test.jar")));
	}
	
	@Test
	public void testAccept_filterWithExtension_FileRejected_CauseExtensionNotAccepted() {
		Set<String> exts = new HashSet<String>();
		exts.add("doc");
		exts.add("docx");
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(exts,null);
		assertFalse(filter.accept(new java.io.File("c:\\test\\lib\\test.jar")));
	}
	
	@Test
	public void testAccept_filterWithPathConvention_FileRejected_CausePathDoesNotContainConvention() {
		Set<String> exts = new HashSet<String>();
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(exts,"lib/thumbnails\\");
		assertFalse(filter.accept(new java.io.File("c:\\test\\lib\\test.jar")));
	}
	
	@Test
	public void testAccept_filterWithExtensionAndPathConvention_FileRejected_CausePathDoesNotContainConvention() {
		Set<String> exts = new HashSet<String>();
		exts.add("jar");
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(exts,"xzcvz");
		assertFalse(filter.accept(new java.io.File("test.jar")));
	}
	
	@Test
	public void testAccept_filterWithExtensionAndPathConvention_FileRejected_CauseExtensionNotAccepted() {
		Set<String> exts = new HashSet<String>();
		exts.add("doc");
		exts.add("docx");
		ServiceLoaderPluginFilter filter = new ServiceLoaderPluginFilter(exts,"st");
		assertFalse(filter.accept(new java.io.File("test.jar")));
	}

}
