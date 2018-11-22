/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author ec
 *
 */
public class FileTypePersistence {

	@Test
	public void testSplitWith2() {
		String uniqueDescriptor = "test1*SEPARATE*test2";
		String[] params = uniqueDescriptor.split("\\*SEPARATE\\*");
		String[] params2 = new String[] {"test1","test2"};
		
		assertArrayEquals(params2, params);
	}
	
	@Test
	public void testSplitWith1() {
		String uniqueDescriptor = "test1*SEPARATE*";
		String[] params = uniqueDescriptor.split("\\*SEPARATE\\*");
		String[] params2 = new String[] {"test1"};
		
		assertArrayEquals(params2, params);
	}
	
	@Test
	public void testSplitWith() {
		String uniqueDescriptor = "test1";
		String[] params = uniqueDescriptor.split("\\*SEPARATE\\*");
		String[] params2 = new String[] {"test1"};
		
		assertArrayEquals(params2, params);
	}

}
