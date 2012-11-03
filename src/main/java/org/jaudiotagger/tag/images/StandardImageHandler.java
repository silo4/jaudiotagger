package org.jaudiotagger.tag.images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import org.jaudiotagger.tag.id3.valuepair.ImageFormats;

/**
 * Image Handling used when running on standard JVM
 */
public class StandardImageHandler implements ImageHandler {
	private static StandardImageHandler instance;

	public static StandardImageHandler getInstanceOf() {
		if (instance == null)
			instance = new StandardImageHandler();
		return instance;
	}

	private StandardImageHandler() {

	}

	/**
	 * Resize the image until the total size require to store the image is less than maxsize
	 * 
	 * @param artwork
	 * @param maxSize
	 * @throws IOException
	 */
	@Override
	public void reduceQuality(final Artwork artwork, final int maxSize) throws IOException {
		while (artwork.getBinaryData().length > maxSize) {
			final Image srcImage = (Image) artwork.getImage();
			final int w = srcImage.getWidth(null);
			final int newSize = w / 2;
			makeSmaller(artwork, newSize);
		}
	}

	/**
	 * Resize image using Java 2D
	 * 
	 * @param artwork
	 * @param size
	 * @throws java.io.IOException
	 */
	@Override
	public void makeSmaller(final Artwork artwork, final int size) throws IOException {
		final Image srcImage = (Image) artwork.getImage();

		final int w = srcImage.getWidth(null);
		final int h = srcImage.getHeight(null);

		// Determine the scaling required to get desired result.
		final float scaleW = (float) size / (float) w;
		final float scaleH = (float) size / (float) h;

		// Create an image buffer in which to paint on, create as an opaque Rgb type image, it doesnt matter what type
		// the original image is we want to convert to the best type for displaying on screen regardless
		final BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		// Set the scale.
		final AffineTransform tx = new AffineTransform();
		tx.scale(scaleW, scaleH);

		// Paint image.
		final Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(srcImage, tx, null);
		g2d.dispose();

		if (artwork.getMimeType() != null && isMimeTypeWritable(artwork.getMimeType()))
			artwork.setBinaryData(writeImage(bi, artwork.getMimeType()));
		else
			artwork.setBinaryData(writeImageAsPng(bi));
	}

	@Override
	public boolean isMimeTypeWritable(final String mimeType) {
		final Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(mimeType);
		return writers.hasNext();
	}

	/**
	 * Write buffered image as required format
	 * 
	 * @param bi
	 * @param mimeType
	 * @return
	 * @throws IOException
	 */
	@Override
	public byte[] writeImage(final BufferedImage bi, final String mimeType) throws IOException {
		final Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(mimeType);
		if (writers.hasNext()) {
			final ImageWriter writer = writers.next();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			writer.setOutput(ImageIO.createImageOutputStream(baos));
			writer.write(bi);
			return baos.toByteArray();
		}
		throw new IOException("Cannot write to this mimetype");
	}

	/**
	 * 
	 * @param bi
	 * @return
	 * @throws IOException
	 */
	@Override
	public byte[] writeImageAsPng(final BufferedImage bi) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, ImageFormats.MIME_TYPE_PNG, baos);
		return baos.toByteArray();
	}

	/**
	 * Show read formats
	 * 
	 * On Windows supports png/jpeg/bmp/gif
	 */
	@Override
	public void showReadFormats() {
		final String[] formats = ImageIO.getReaderMIMETypes();
		for (final String f : formats)
			System.out.println("r" + f);
	}

	/**
	 * Show write formats
	 * 
	 * On Windows supports png/jpeg/bmp
	 */
	@Override
	public void showWriteFormats() {
		final String[] formats = ImageIO.getWriterMIMETypes();
		for (final String f : formats)
			System.out.println(f);
	}
}
