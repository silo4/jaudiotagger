package org.jaudiotagger.tag.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.reference.PictureTypes;

/**
 * Represents artwork in a format independent way
 */
public class StandardArtwork implements Artwork {
	private byte[] binaryData;
	private String mimeType = "";
	private String description = "";
	private boolean isLinked = false;
	private String imageUrl = "";
	private int pictureType = -1;
	private int width;
	private int height;

	public StandardArtwork() {

	}

	@Override
	public byte[] getBinaryData() {
		return binaryData;
	}

	@Override
	public void setBinaryData(final byte[] binaryData) {
		this.binaryData = binaryData;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Should be called when you wish to prime the artwork for saving
	 * 
	 * @return
	 */
	@Override
	public boolean setImageFromData() {
		try {
			final BufferedImage image = (BufferedImage) getImage();
			setWidth(image.getWidth());
			setHeight(image.getHeight());
		} catch (final IOException ioe) {
			return false;
		}
		return true;
	}

	@Override
	public Object getImage() throws IOException {
		final ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(getBinaryData()));
		final BufferedImage bi = ImageIO.read(iis);
		return bi;
	}

	@Override
	public boolean isLinked() {
		return isLinked;
	}

	@Override
	public void setLinked(final boolean linked) {
		isLinked = linked;
	}

	@Override
	public String getImageUrl() {
		return imageUrl;
	}

	@Override
	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public int getPictureType() {
		return pictureType;
	}

	@Override
	public void setPictureType(final int pictureType) {
		this.pictureType = pictureType;
	}

	/**
	 * Create Artwork from File
	 * 
	 * @param file
	 * @throws java.io.IOException
	 */
	@Override
	public void setFromFile(final Path file) throws IOException {
		final RandomAccessFile imageFile = new RandomAccessFile(file.toFile(), "r");
		final byte[] imagedata = new byte[(int) imageFile.length()];
		imageFile.read(imagedata);
		imageFile.close();

		setBinaryData(imagedata);
		setMimeType(ImageFormats.getMimeTypeForBinarySignature(imagedata));
		setDescription("");
		setPictureType(PictureTypes.DEFAULT_ID);
	}

	/**
	 * Create Linked Artwork from URL
	 * 
	 * @param url
	 * @throws java.io.IOException
	 */
	public void setLinkedFromURL(final String url) throws IOException {
		setLinked(true);
		setImageUrl(url);
	}

	/**
	 * Create Artwork from File
	 * 
	 * @param file
	 * @return
	 * @throws java.io.IOException
	 */
	public static StandardArtwork createArtworkFromFile(final Path file) throws IOException {
		final StandardArtwork artwork = new StandardArtwork();
		artwork.setFromFile(file);
		return artwork;
	}

	public static StandardArtwork createLinkedArtworkFromURL(final String url) throws IOException {
		final StandardArtwork artwork = new StandardArtwork();
		artwork.setLinkedFromURL(url);
		return artwork;
	}

	/**
	 * Populate Artwork from MetadataBlockDataPicture as used by Flac and VorbisComment
	 * 
	 * @param coverArt
	 */
	@Override
	public void setFromMetadataBlockDataPicture(final MetadataBlockDataPicture coverArt) {
		setMimeType(coverArt.getMimeType());
		setDescription(coverArt.getDescription());
		setPictureType(coverArt.getPictureType());
		if (coverArt.isImageUrl()) {
			setLinked(coverArt.isImageUrl());
			setImageUrl(coverArt.getImageUrl());
		} else
			setBinaryData(coverArt.getImageData());
		setWidth(coverArt.getWidth());
		setHeight(coverArt.getHeight());
	}

	/**
	 * Create artwork from Flac block
	 * 
	 * @param coverArt
	 * @return
	 */
	public static StandardArtwork createArtworkFromMetadataBlockDataPicture(final MetadataBlockDataPicture coverArt) {
		final StandardArtwork artwork = new StandardArtwork();
		artwork.setFromMetadataBlockDataPicture(coverArt);
		return artwork;
	}

	@Override
	public void setWidth(final int width) {
		this.width = width;
	}

	@Override
	public void setHeight(final int height) {
		this.height = height;
	}
}
