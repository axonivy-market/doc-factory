package ch.ivyteam.ivy.addons.qrcode;

import java.io.File;

public interface QRCodeGenerator {

	/**
	 * Generates a java.io.File with the QRCode representation of the given data.
	 * @param data the data which has to be encoded in QRCode. Cannot be blank.
	 * @param imageOptions QRCodeImageOptions object containing informations like the size and the type of the image of the code.
	 * @return the generated File with the QRCode representation
	 */
	File generateFileWithGeneratedQRCode(String data, QRCodeImageOptions imageOptions);
	
	/**
	 * Generates a java.io.File with the DataMatrix QRCode representation of the given data.
	 * @param data the data which has to be encoded in DataMatrix QRCode. Cannot be blank.
	 * @param imageOptions QRCodeImageOptions object containing informations like the size and the type of the image of the code.
	 * @return the generated File with the DataMatrix QRCode representation
	 */
	File generateFileWithGeneratedDataMatrixQRCode(String data, QRCodeImageOptions imageOptions);
}
