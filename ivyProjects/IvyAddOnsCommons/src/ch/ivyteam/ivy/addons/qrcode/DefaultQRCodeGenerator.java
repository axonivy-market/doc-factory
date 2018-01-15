package ch.ivyteam.ivy.addons.qrcode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.qrcode.exception.QRCodeGenerationException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.QRCodeWriter;

public class DefaultQRCodeGenerator implements QRCodeGenerator {

	private QRCodeImageOptions imageOptions;

	@Override
	public File generateFileWithGeneratedQRCode(String data, QRCodeImageOptions imageOptions) {
		API.checkParameterNotEmpty(data, "data");
		API.checkNotNull(imageOptions, "QRCodeImageOptions");
		this.imageOptions = imageOptions;
		try {
			createExportFile();
			BufferedImage image = createQRImage(data, imageOptions.getHeightInPixels());
			ImageIO.write(image, imageOptions.getImageType(), imageOptions.getExporFile());
			return imageOptions.getExporFile();

		} catch (WriterException e) {
			throw new QRCodeGenerationException("WriterException occurred while generating the QRCode", e);
		} catch (IOException e) {
			throw new QRCodeGenerationException("IOException occurred while generating the QRCode", e);
		}
	}

	private void createExportFile() throws IOException {
		if(!this.imageOptions.getExporFile().getParentFile().isDirectory()) {
			this.imageOptions.getExporFile().getParentFile().mkdirs();
		}
		if(!this.imageOptions.getExporFile().isFile()) {
			this.imageOptions.getExporFile().createNewFile();
		}
	}

	@Override
	public File generateFileWithGeneratedDataMatrixQRCode(String data, QRCodeImageOptions imageOptions) {
		API.checkParameterNotEmpty(data, "data");
		API.checkNotNull(imageOptions, "QRCodeImageOptions");
		this.imageOptions = imageOptions;

		try {
			createExportFile();
			BufferedImage image = createDataMatrixImage(data, imageOptions.getHeightInPixels());
			ImageIO.write(image, imageOptions.getImageType(), imageOptions.getExporFile());
			return imageOptions.getExporFile();

		} catch (IOException e) {
			throw new QRCodeGenerationException("IOException occurred while generating the QRCode", e);
		} catch (WriterException e) {
			throw new QRCodeGenerationException("WriterException occurred while generating the QRCode", e);
		}
	}

	private BufferedImage createDataMatrixImage(String qrCodeText, int size) throws WriterException {
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
		//This don't work anymore: EncodeHintType.MIN_SIZE deprecated and there is a bug in zxing lib...
		// see http://stackoverflow.com/questions/22766017/datamatrix-encoding-with-zxing-only-generates-14px-bitmap
		//hints.put(EncodeHintType.MIN_SIZE, new Dimension(size, size));

		Writer dataMatrixCodeWriter = new DataMatrixWriter();
		BitMatrix byteMatrix = dataMatrixCodeWriter.encode(qrCodeText, BarcodeFormat.DATA_MATRIX , size, size, hints);
		int width = byteMatrix.getWidth();
		int height = byteMatrix.getHeight();

		// calculating the scaling factor
		int pixelsize = size/width;
		int[] pixels = new int[size * size];
		for (int y = 0; y < height; y++) {
			int offset = y * size * pixelsize;
			// scaling pixel height
			for (int pixelsizeHeight = 0; pixelsizeHeight < pixelsize; pixelsizeHeight++, offset+=size) {
				for (int x = 0; x < width; x++) {
					int color = byteMatrix.get(x, y) ? imageOptions.getQRCodeColor().getRGB() : imageOptions.getQRCodeBackgroundColor().getRGB();
					// scaling pixel width
					for (int pixelsizeWidth = 0; pixelsizeWidth < pixelsize; pixelsizeWidth++) {
						pixels[offset + x * pixelsize + pixelsizeWidth] = color;
					}
				}
			}
		}
		
		BufferedImage bitmap = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		bitmap.getRaster().setDataElements(0, 0, size, size, pixels);
		return bitmap;
	}

	private BufferedImage createQRImage(String qrCodeText, int size) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size);
		// Make the BufferedImage that are to hold the QRCode
		BufferedImage image = bitMatrixToBufferedImage(byteMatrix);
		return image;
	}

	private BufferedImage bitMatrixToBufferedImage(BitMatrix byteMatrix) {
		int matrixWidth = byteMatrix.getWidth();
		int matrixHeight = byteMatrix.getHeight();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(this.imageOptions.getQRCodeBackgroundColor());
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);

		// Paint and save the image using the ByteMatrix
		graphics.setColor(this.imageOptions.getQRCodeColor());

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixHeight; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		return image;
	}

}
