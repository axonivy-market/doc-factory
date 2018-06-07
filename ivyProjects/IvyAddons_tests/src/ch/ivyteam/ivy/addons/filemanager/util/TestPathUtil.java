/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.util;

import static org.junit.Assert.*;

import org.junit.Test;

import static ch.ivyteam.ivy.addons.filemanager.util.PathUtil.*;

/**
 * @author ec
 *
 */
public class TestPathUtil {

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#escapeBackSlash(java.lang.String)}.
	 */
	@Test
	public void testEscapeBackSlash() {
		String s = escapeBackSlash("\\\\test\\\\cool\\");
		assertTrue(s.equalsIgnoreCase("//test//cool/"));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#formatPath(java.lang.String)}.
	 */
	@Test
	public void testFormatPath() {
		String s = formatPath("\\\\test\\\\cool\\");
		assertTrue(s.equalsIgnoreCase("//test//cool"));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#formatPathForDirectory(java.lang.String)}.
	 */
	@Test
	public void testFormatPathForDirectory() {
		String s = formatPathForDirectory("\\\\test\\\\cool\\");
		assertTrue(s.equalsIgnoreCase("//test//cool/"));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#formatPathForDirectoryWithoutLastAndFirstSeparator(java.lang.String)}.
	 */
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparator() {
		String s = formatPathForDirectoryWithoutLastAndFirstSeparator("\\\\test\\\\cool\\");
		assertTrue(s.equalsIgnoreCase("test//cool"));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#formatPathForDirectoryWithoutLastSeparator(java.lang.String)}.
	 */
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparatorAllowingSlashesAndBackslashesAtBeginOfPath() {
		String s = formatPathForDirectoryWithoutLastSeparator("\\\\test\\\\cool\\");
		assertTrue(s.equalsIgnoreCase("//test//cool"));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#formatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath(java.lang.String)}.
	 */
	@Test
	public void testFormatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath() {
		String s = formatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath("\\\\test\\\\cool\\");
		assertTrue(s.equalsIgnoreCase("//test//cool/"));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.util.PathUtil#escapeUnderscoreInPath(java.lang.String)}.
	 */
	@Test
	public void testEscapeUnderscoreInPath() {
		String s = escapeUnderscoreInPath("\\\\test_cool\\");
		System.out.println(s);
		assertTrue(s.equalsIgnoreCase("\\\\test\\_cool\\"));
	}
	
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparatorUnformatSeparatorAtBeginOfPathNoBackslashesAtTheBeginning() {
		String path = "gecinaivyd\\IvyDvp\\XIVYServerApplicationsResourcesDev\\wfResources/files/Default/150885/Documents";
		String s = PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		String shouldBe = "gecinaivyd/IvyDvp/XIVYServerApplicationsResourcesDev/wfResources/files/Default/150885/Documents";
		assertTrue(shouldBe.equalsIgnoreCase(s));
	}
	
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparatorUnformatSeparatorAtBeginOfPath1BackslashesAtTheBeginning() {
		String path = "\\gecinaivyd\\IvyDvp\\XIVYServerApplicationsResourcesDev\\wfResources/files/Default/150885/Documents\\\\";
		String s = PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		String shouldBe = "/gecinaivyd/IvyDvp/XIVYServerApplicationsResourcesDev/wfResources/files/Default/150885/Documents";
		assertTrue(shouldBe.equalsIgnoreCase(s));
	}
	
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparatorUnformatSeparatorAtBeginOfPath2BackslashesAtTheBeginning() {
		String path = "\\\\gecinaivyd\\IvyDvp\\XIVYServerApplicationsResourcesDev\\wfResources/files/Default/150885/Documents//";
		String s = PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		String shouldBe = "//gecinaivyd/IvyDvp/XIVYServerApplicationsResourcesDev/wfResources/files/Default/150885/Documents";
		
		assertTrue(shouldBe.equalsIgnoreCase(s));
	}
	
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparatorUnformatSeparatorAtBeginOfPath1slashesAtTheBeginning() {
		String path = "/gecinaivyd\\IvyDvp\\XIVYServerApplicationsResourcesDev\\wfResources/files/Default/150885/Documents/";
		String s = PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		String shouldBe = "/gecinaivyd/IvyDvp/XIVYServerApplicationsResourcesDev/wfResources/files/Default/150885/Documents";
		
		assertTrue(shouldBe.equalsIgnoreCase(s));
	}
	
	@Test
	public void testFormatPathForDirectoryWithoutLastSeparatorUnformatSeparatorAtBeginOfPath2slashesAtTheBeginning() {
		String path = "//gecinaivyd\\IvyDvp\\XIVYServerApplicationsResourcesDev\\wfResources/files/Default/150885/Documents";
		String s = PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		String shouldBe = "//gecinaivyd/IvyDvp/XIVYServerApplicationsResourcesDev/wfResources/files/Default/150885/Documents";
		
		assertTrue(shouldBe.equalsIgnoreCase(s));
	}
	
	@Test
	public void testFormatOnlyBackslashesReturnsEmptyString() {
		String path = "\\\\\\\\";
		String s = PathUtil.formatPathForDirectoryWithoutLastAndFirstSeparator(path);
		assertTrue(s.isEmpty());
	}
	
	@Test
	public void testFormatOnlySlashesReturnsEmptyString() {
		String path = "///////";
		String s = PathUtil.formatPathForDirectoryWithoutLastAndFirstSeparator(path);
		assertTrue(s.isEmpty());
	}
	
	@Test
	public void testFormatXSlashesAtTheEnd() {
		String path = "////cool///";
		String s = PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		
		assertTrue(s.equalsIgnoreCase("////cool"));
	}
	
	@Test
	public void getDirectoryNameForServerShareRootPath(){
		String path = "\\\\COMBA\\";
		String s = PathUtil.getDirectoryNameFromPath(path);
		
		assertEquals("", "//COMBA", s);
	}
	
	@Test
	public void getDirectoryNameForPathInServerShare(){
		String path = "\\\\COMBA\\Shared/tip/";
		String s = PathUtil.getDirectoryNameFromPath(path);
		
		assertEquals("", "tip", s);
	}
	
	@Test
	public void getDirectoryNameForClassicPath(){
		String path = "COMBA\\Shared/tip/";
		String s = PathUtil.getDirectoryNameFromPath(path);
		
		assertEquals("", "tip", s);
	}
	
	@Test
	public void getDirectoryNameForClassicRootPath(){
		String path = "    COMBA\\\\\\   ";
		String s = PathUtil.getDirectoryNameFromPath(path);
		
		assertEquals("", "COMBA", s);
	}
	
	@Test
	public void getDirectoryNameForNull(){
		String path = null;
		String s = PathUtil.getDirectoryNameFromPath(path);
		
		assertEquals("", "", s);
	}
	
	@Test
	public void getDirectoryNameForEmptyPath(){
		String path = "	        ";
		String s = PathUtil.getDirectoryNameFromPath(path);
		
		assertEquals("", "", s);
	}
	
	@Test
	public void getParentDirectoryPathForServerShareRootPath(){
		String path = "\\\\COMBA\\";
		String s = PathUtil.getParentDirectoryPath(path);
		
		assertEquals("", "", s);
	}
	
	@Test
	public void getParentDirectoryPathForDirectoryInServerShare(){
		String path = "\\\\COMBA\\Shared/tip/";
		String s = PathUtil.getParentDirectoryPath(path);
		
		assertEquals("", "//COMBA/Shared", s);
	}
	
	@Test
	public void getParentDirectoryPathForDirectoryPath(){
		String path = "COMBA\\Shared/tip/top////";
		String s = PathUtil.getParentDirectoryPath(path);
		
		assertEquals("", "COMBA/Shared/tip", s);
	}
	
	@Test
	public void getParentDirectoryPathForRootDirectoryPath(){
		String path = "COMBA\\";
		String s = PathUtil.getParentDirectoryPath(path);
		
		assertEquals("", "", s);
	}
	
	@Test
	public void getParentDirectoryPathForEmptyPath(){
		String path = "			";
		String s = PathUtil.getParentDirectoryPath(path);
		
		assertEquals("", "", s);
	}
	
	@Test
	public void getParentDirectoryPathForNullPath(){
		String path = null;
		String s = PathUtil.getParentDirectoryPath(path);
		
		assertEquals("", "", s);
	}
	
	@Test
	public void escapeSpecialSQLSignsInPathNull() {
		String path = null;
		assertNull(PathUtil.escapeSpecialSQLSignsInPath(path));
	}
	
	@Test
	public void escapeSpecialSQLSignsInPathEmpty() {
		String path = " ";
		assertEquals(PathUtil.escapeSpecialSQLSignsInPath(path),path);
	}
	
	@Test
	public void escapeSpecialSQLSignsInPath() {
		String path = "test/Rock n'Roll (tip top) for_you";
		assertEquals(PathUtil.escapeSpecialSQLSignsInPath(path),"test/Rock n''Roll \\(tip top\\) for\\_you");
	}

}
