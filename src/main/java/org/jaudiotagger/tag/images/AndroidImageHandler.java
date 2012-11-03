package org.jaudiotagger.tag.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Image Handling to to use when running on Android
 * 
 * TODO need to provide Android compatible implementations
 */
public class AndroidImageHandler implements ImageHandler {
	private static AndroidImageHandler instance;

	public static AndroidImageHandler getInstanceOf() {
		if (instance == null)
			instance = new AndroidImageHandler();
		return instance;
	}

	private AndroidImageHandler() {

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
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isMimeTypeWritable(final String mimeType) {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param bi
	 * @return
	 * @throws IOException
	 */
	@Override
	public byte[] writeImageAsPng(final BufferedImage bi) throws IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Show read formats
	 * 
	 * On Windows supports png/jpeg/bmp/gif
	 */
	@Override
	public void showReadFormats() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Show write formats
	 * 
	 * On Windows supports png/jpeg/bmp
	 */
	@Override
	public void showWriteFormats() {
		throw new UnsupportedOperationException();
	}
}
