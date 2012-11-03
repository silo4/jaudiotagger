package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

/**
 * Writing to Ogg file
 */
public class Issue308Test extends AbstractTestCase {
	public static int countExceptions = 0;

	public void testAddingLargeImageToOgg() throws Exception {
		final Path orig = AbstractTestCase.dataPath.resolve("test72.ogg");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test72.ogg");
			if (!Files.isRegularFile(testFile)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}
			AudioFile af = AudioFileIO.read(testFile);
			final Artwork artwork = ArtworkFactory.getNew();
			artwork.setFromFile(AbstractTestCase.dataPath.resolve("coverart_large.jpg"));

			af.getTag().setField(artwork);
			af.commit();

			// Reread
			System.out.println("Read Audio");
			af = AudioFileIO.read(testFile);
			System.out.println("Rewrite Audio");
			af.commit();

			// Resave
			af.getTag().addField(FieldKey.TITLE, "TESTdddddddddddddddddddddddd");
			af.commit();
		} catch (final Exception ex) {
			e = ex;
			ex.printStackTrace();
		}
		assertNull(e);
	}
}
