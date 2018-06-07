package ch.ivyteam.ivy.addons.qrcode;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;


public class DefaultQRCodeGeneratorTest {
	
	private static java.io.File DEFAULT_TEMP_DIR = new java.io.File("temp");
	
	
	@Test
	public void generateFileWithGeneratedQRCode_withExportFile() {
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(200).withExportFile(new java.io.File(DEFAULT_TEMP_DIR, "test.jpg"));
		
		java.io.File result = new DefaultQRCodeGenerator().generateFileWithGeneratedQRCode("Data to be encoded", options);
		
		assertTrue(result.isFile());
	}
	
	@Test
	public void generateFileWithGeneratedQRCode_withColors() {
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(200).
				withExportFile(new java.io.File(DEFAULT_TEMP_DIR, "test_blue.jpg")).
				withQRCodeImageColors(Color.WHITE, Color.BLUE);
		
		QRCodeGenerator qRCodeGenerator = new DefaultQRCodeGenerator();
		
		java.io.File result = qRCodeGenerator.generateFileWithGeneratedQRCode("Data to be encoded", options);
		
		assertTrue(result.isFile());
	}
	
	
	@Test
	public void generateFileWithGeneratedDataMatrixQRCode_withExportFile() {
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(200).withExportFile(new java.io.File(DEFAULT_TEMP_DIR, "DataMatrix.jpg"));
		
		java.io.File result = new DefaultQRCodeGenerator().generateFileWithGeneratedDataMatrixQRCode(makeText(), options);
		
		assertTrue(result.isFile());
	}
	
	@Test
	public void generateFileWithGeneratedDataMatrixQRCode_withColors() {
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(200).
				withExportFile(new java.io.File(DEFAULT_TEMP_DIR, "DataMatrix_blue.jpg")).
				withQRCodeImageColors(Color.WHITE, Color.BLUE);
		
		QRCodeGenerator qRCodeGenerator = new DefaultQRCodeGenerator();
		
		java.io.File result = qRCodeGenerator.generateFileWithGeneratedDataMatrixQRCode(makeText(), options);
		
		assertTrue(result.isFile());
	}
	
	private String makeText() {
		return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
				+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
				+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
				+ "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in r"
				+ "eprehenderit in voluptate velit esse cillum dolore eu fugiat nulla "
				+ "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in "
				+ "culpa qui officia deserunt mollit anim id est laborum." +
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
				+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
				+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
				+ "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in r"
				+ "eprehenderit in voluptate velit esse cillum dolore eu fugiat nulla "
				+ "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in "
				+ "culpa qui officia deserunt mollit anim id est laborum.";
	}

}
