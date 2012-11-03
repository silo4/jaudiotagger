package org.jaudiotagger.tag.images;

import java.io.IOException;
import java.nio.file.Path;

import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.TagOptionSingleton;

/**
 * Get appropriate Artwork class
 */
public class ArtworkFactory {

	public static Artwork getNew() {
		// Normal
		if (!TagOptionSingleton.getInstance().isAndroid())
			return new StandardArtwork();
		else
			return new AndroidArtwork();
	}

	/**
	 * Create Artwork instance from A Flac Metadata Block
	 * 
	 * @param coverArt
	 * @return
	 */
	public static Artwork createArtworkFromMetadataBlockDataPicture(final MetadataBlockDataPicture coverArt) {
		// Normal
		if (!TagOptionSingleton.getInstance().isAndroid())
			return StandardArtwork.createArtworkFromMetadataBlockDataPicture(coverArt);
		else
			return AndroidArtwork.createArtworkFromMetadataBlockDataPicture(coverArt);
	}

	/**
	 * Create Artwork instance from an image file
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Artwork createArtworkFromFile(final Path file) throws IOException {
		// Normal
		if (!TagOptionSingleton.getInstance().isAndroid())
			return StandardArtwork.createArtworkFromFile(file);
		else
			return AndroidArtwork.createArtworkFromFile(file);
	}

	/**
	 * Create Artwork instance from an image file
	 * 
	 * @param link
	 * @return
	 * @throws IOException
	 */
	public static Artwork createLinkedArtworkFromURL(final String link) throws IOException {
		// Normal
		if (!TagOptionSingleton.getInstance().isAndroid())
			return StandardArtwork.createLinkedArtworkFromURL(link);
		else
			return AndroidArtwork.createLinkedArtworkFromURL(link);
	}
}
