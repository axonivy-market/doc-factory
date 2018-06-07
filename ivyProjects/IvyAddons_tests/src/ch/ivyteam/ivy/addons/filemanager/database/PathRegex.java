package ch.ivyteam.ivy.addons.filemanager.database;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class PathRegex {

	@Test
	public void test() {
		String path = "root/test/test_1/a directory(1)/great-thing";
		String pattern = Pattern.quote("root/test/test_1/a directory(1)/");
		System.out.println(pattern);
		path = path.replaceFirst(pattern, "root/new destination/");
		assertTrue(path.equals("root/new destination/great-thing"));
	}
	
	@Test
	public void test2() {
		String path = "root/test/test_1/a directory(1)/root/test/test_1/a directory(1)/great-thing";
		
		path = path.replaceFirst(Pattern.quote("root/test/test_1/a directory(1)/"), "root/new destination/");
		assertTrue(path.equals("root/new destination/root/test/test_1/a directory(1)/great-thing"));
	}

}
