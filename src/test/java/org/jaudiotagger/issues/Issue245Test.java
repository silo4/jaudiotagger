package org.jaudiotagger.issues;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.asf.AsfTagCoverField;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.images.Images;

/**
 * Support For Common Interface for reading and writing coverart
 */
public class Issue245Test extends AbstractTestCase {
	/**
	 * Test writing Artwork to Mp3 ID3v24
	 */
	public void testWriteArtworkFieldsToMp3ID3v24() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			af.setTag(new ID3v24Tag());
			Tag tag = af.getTag();

			assertEquals(0, tag.getArtworkList().size());

			// Now addField the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			newartwork.setPictureType(5);
			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			final Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(5, artwork.getPictureType());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test writing Artwork to Mp3 ID3v23
	 */
	public void testWriteArtworkFieldsToMp3ID3v23() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault();
			Tag tag = af.getTag();

			assertEquals(0, tag.getArtworkList().size());

			// Now addField the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			newartwork.setPictureType(11);
			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			final Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(11, artwork.getPictureType());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test writing Artwork to Mp3 ID3v22
	 */
	public void testWriteArtworkFieldsToMp3ID3v22() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			af.setTag(new ID3v22Tag());
			Tag tag = af.getTag();

			assertEquals(0, tag.getArtworkList().size());

			// Now addField the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			newartwork.setPictureType(5);
			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			final Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(5, artwork.getPictureType());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test reading/writing artwork to Ogg
	 */
	public void testReadWriteArtworkFieldsToOggVorbis() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test3.ogg");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			Tag tag = af.getTag();

			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());

			// Now replace the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test reading/writing artwork to Flac
	 */
	public void testReadWriteArtworkFieldsToFlac() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test.flac", Paths.get("testwriteartwork.flac"));

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			Tag tag = af.getTag();

			assertEquals(2, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(3, artwork.getPictureType());
			// Now replace the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			newartwork.setDescription("freddy");
			newartwork.setPictureType(7);
			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(2, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(7, artwork.getPictureType());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test reading/writing artwork to Wma
	 */
	public void testReadWriteArtworkFieldsToWma() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test5.wma");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			Tag tag = af.getTag();

			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(3, artwork.getPictureType());
			// Now replace the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			newartwork.setDescription("freddy");
			newartwork.setPictureType(8);
			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertTrue(tag.getFirstField(FieldKey.COVER_ART) instanceof AsfTagCoverField);
			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());
			assertEquals(8, artwork.getPictureType());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test reading/writing artwork to Mp4
	 */
	public void testReadWriteArtworkFieldsToMp4() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			Tag tag = af.getTag();

			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			Artwork artwork = tag.getFirstArtwork();
			assertEquals("image/jpeg", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(159, Images.getImage(artwork).getWidth());

			// Now replace the image
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			tag.setField(newartwork);
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(1, tag.getArtworkList().size());
			assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
			artwork = tag.getFirstArtwork();
			assertEquals("image/png", artwork.getMimeType());
			assertNotNull(artwork.getImage());
			assertEquals(200, Images.getImage(artwork).getWidth());

			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();
			af = AudioFileIO.read(testFile);
			tag = af.getTag();
			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test Artwork cannot be written to Wav
	 */
	public void testReadWriteArtworkFieldsToWav() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test.wav");

			// Read File okay
			final AudioFile af = AudioFileIO.read(testFile);
			final Tag tag = af.getTag();

			assertEquals(0, tag.getArtworkList().size());

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);

		try {
			// Now try and addField image
			final AudioFile af = AudioFileIO.read(testFile);
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			final Tag tag = af.getTag();
			tag.setField(newartwork);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNotNull(exceptionCaught);
		assertTrue(exceptionCaught instanceof UnsupportedOperationException);

		// Not Supported
		try {
			// Now try and addField image
			final AudioFile af = AudioFileIO.read(testFile);
			final Tag tag = af.getTag();
			tag.deleteArtworkField();
			assertEquals(0, tag.getArtworkList().size());
			af.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNotNull(exceptionCaught);
		assertTrue(exceptionCaught instanceof UnsupportedOperationException);
	}

	/**
	 * Test Artwork cannot be written to Real
	 */
	public void testReadWriteArtworkFieldsToReal() {
		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test01.ra");

			// Read File okay
			final AudioFile af = AudioFileIO.read(testFile);
			final Tag tag = af.getTag();

			assertEquals(0, tag.getArtworkList().size());
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);

		try {
			// Now try and addField image
			final AudioFile af = AudioFileIO.read(testFile);
			final Artwork newartwork = ArtworkFactory.createArtworkFromFile(AbstractTestCase.dataPath.resolve("coverart.png"));
			assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

			final Tag tag = af.getTag();
			tag.setField(newartwork);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNotNull(exceptionCaught);
		assertTrue(exceptionCaught instanceof UnsupportedOperationException);

		// Not supported
		try {

			final AudioFile af = AudioFileIO.read(testFile);
			final Tag tag = af.getTag();
			tag.deleteArtworkField();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNotNull(exceptionCaught);
		assertTrue(exceptionCaught instanceof UnsupportedOperationException);
	}

}
