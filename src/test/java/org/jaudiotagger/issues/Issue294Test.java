package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp3.MPEGFrameHeader;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.images.Images;

/**
 * Jpg added to this mp3 fails on read back
 */
public class Issue294Test extends AbstractTestCase {
	public void testSavingArtworkToMp3File() {

		final Path orig = AbstractTestCase.dataPath.resolve("test70.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		final Path testFile = AbstractTestCase.copyAudioToTmp("test70.mp3");
		final Path testPix = AbstractTestCase.copyAudioToTmp("test70.jpg");

		final Path originalFileBackup = null;

		Exception exceptionCaught = null;
		try {
			TagOptionSingleton.getInstance().setUnsyncTags(false);
			// Read and save changes
			MP3File af = (MP3File) AudioFileIO.read(testFile);

			// File is corrupt
			assertEquals(1, af.getTag().getArtworkList().size());
			Artwork artwork = af.getTag().getFirstArtwork();
			assertEquals("image/jpeg", artwork.getMimeType());
			assertTrue(ImageFormats.isPortableFormat(artwork.getBinaryData()));

			// Delete and commit
			// af.getTag().deleteArtworkField();
			// af.commit();

			// af.getID3v2TagAsv24().removeFrame("APIC");

			final List multiFrames = new ArrayList();
			multiFrames.add(af.getID3v2Tag().createField(ArtworkFactory.createArtworkFromFile(testPix)));
			af.getID3v2Tag().setFrame("APIC", multiFrames);
			af.commit();

			// Can we read this image file
			artwork = ArtworkFactory.createArtworkFromFile(testPix);
			assertEquals(118145, artwork.getBinaryData().length);
			assertEquals(0xFF, artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals(0xD8, artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals(0xFF, artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals(0xE0, artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals("image/jpeg", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(500, Images.getImage(artwork).getHeight());
			assertEquals(500, Images.getImage(artwork).getWidth());

			final byte[] origData = artwork.getBinaryData();

			af = (MP3File) AudioFileIO.read(testFile);
			assertEquals(1, af.getTag().getArtworkList().size());
			artwork = af.getTag().getArtworkList().get(0);
			assertEquals(118145, artwork.getBinaryData().length);
			assertEquals(0xFF, artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals(0xD8, artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals(0xFF, artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals(0xE0, artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1);
			assertEquals("image/jpeg", artwork.getMimeType());

			for (int i = 0; i < origData.length; i++)
				if (origData[i] != artwork.getBinaryData()[i]) {
					System.out.println("Index " + i + ":" + Hex.asHex(i & MPEGFrameHeader.SYNC_BYTE1) + ":" + Hex.asHex(origData[i] & MPEGFrameHeader.SYNC_BYTE1) + ":" + Hex.asHex(artwork.getBinaryData()[i] & MPEGFrameHeader.SYNC_BYTE1));
					break;
				}
			assertTrue(Arrays.equals(origData, artwork.getBinaryData()));
			assertNotNull(artwork.getImage());
			assertEquals(500, Images.getImage(artwork).getHeight());
			assertEquals(500, Images.getImage(artwork).getWidth());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		} finally {

		}
		assertNull(exceptionCaught);
	}
}
