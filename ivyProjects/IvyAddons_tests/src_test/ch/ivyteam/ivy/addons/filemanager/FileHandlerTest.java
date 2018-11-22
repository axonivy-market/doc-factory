package ch.ivyteam.ivy.addons.filemanager;


import org.junit.Test;

public class FileHandlerTest {

	@Test
	public void testGetFileSize() {
		java.io.File f = new java.io.File("F:/Support/filemanager/EPD/XEPD.jpg");
		
		String s = FileHandler.getFileSize(f);
		System.out.println(s);
	}
	
	@Test
	public void testGetFileSize2() {
		java.io.File f = new java.io.File("F:/Support/filemanager/DesktopClassTest.zip");
		
		String s = FileHandler.getFileSize(f);
		System.out.println(s);
	}
	
	@Test
	public void testGetFileSize3() {
		java.text.NumberFormat formatter = new java.text.DecimalFormat("#.##");
		float s = 1048576/1048576;
		String nb =formatter.format(s);
		System.out.println(nb);
	}
	

}
