package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue220Test extends AbstractTestCase {
	/**
	 * Test read mp4 ok without any udta atom (but does have meta atom under trak)
	 */
	public void testReadMp4WithoutUdta() {
		final Path orig = AbstractTestCase.dataPath.resolve("test41.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test41.m4a");

			// Read File okay
			final AudioFile af = AudioFileIO.read(testFile);
			assertTrue(af.getTag().isEmpty());
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test write mp4 ok without any udta atom (but does have meta atom under trak)
	 */
	public void testWriteMp4WithoutUdta() {
		final Path orig = AbstractTestCase.dataPath.resolve("test41.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test41.m4a");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			assertTrue(af.getTag().isEmpty());

			// Write file
			af.getTag().setField(FieldKey.ARTIST, "FREDDYCOUGAR");
			af.getTag().setField(FieldKey.ALBUM, "album");
			af.getTag().setField(FieldKey.TITLE, "title");
			af.getTag().setField(FieldKey.GENRE, "genre");
			af.getTag().setField(FieldKey.YEAR, "year");
			af.commit();

			// Read file again okay
			af = AudioFileIO.read(testFile);
			assertEquals("FREDDYCOUGAR", af.getTag().getFirst(FieldKey.ARTIST));
			assertEquals("album", af.getTag().getFirst(FieldKey.ALBUM));
			assertEquals("title", af.getTag().getFirst(FieldKey.TITLE));
			assertEquals("genre", af.getTag().getFirst(FieldKey.GENRE));
			assertEquals("year", af.getTag().getFirst(FieldKey.YEAR));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test read mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta
	 * atom before mvhd atom (and still has the meta atom under trak)
	 */
	public void testReadMp4WithUdtaAndMetaHierachy() {
		final Path orig = AbstractTestCase.dataPath.resolve("test42.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test42.m4a");

			// Read File okay
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals("artist", af.getTag().getFirst(FieldKey.ARTIST));
			assertEquals("album", af.getTag().getFirst(FieldKey.ALBUM));
			assertEquals("test42", af.getTag().getFirst(FieldKey.TITLE));
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test write mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta
	 * atom before mvhd atom (and still has the meta atom under trak)
	 */
	public void testWriteMp4WithUdtaAndMetaHierachy() {
		final Path orig = AbstractTestCase.dataPath.resolve("test42.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test42.m4a");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(FieldKey.ALBUM, "KARENTAYLORALBUM");
			af.getTag().setField(FieldKey.TITLE, "KARENTAYLORTITLE");
			af.getTag().setField(FieldKey.GENRE, "KARENTAYLORGENRE");
			af.getTag().setField(af.getTag().createField(FieldKey.AMAZON_ID, "12345678"));

			af.commit();
			System.out.println("All is going well");
			af = AudioFileIO.read(testFile);
			assertEquals("KARENTAYLORALBUM", af.getTag().getFirst(FieldKey.ALBUM));
			assertEquals("KARENTAYLORTITLE", af.getTag().getFirst(FieldKey.TITLE));
			assertEquals("KARENTAYLORGENRE", af.getTag().getFirst(FieldKey.GENRE));
			assertEquals("12345678", af.getTag().getFirst(FieldKey.AMAZON_ID));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test write mp4 ok without any udta atom (but does have meta atom under trak)
	 */
	public void testWriteMp4WithUdtaAfterTrackSmaller() {
		final Path orig = AbstractTestCase.dataPath.resolve("test44.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test44.m4a");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);

			// Write file
			af.getTag().setField(FieldKey.TITLE, "ti");
			af.commit();

			// Read file again okay
			af = AudioFileIO.read(testFile);
			assertEquals("ti", af.getTag().getFirst(FieldKey.TITLE));
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	/**
	 * Test write mp4 ok without any udta atom (but does have meta atom under trak)
	 */
	public void testWriteMp4WithUdtaAfterTrack() {
		final Path orig = AbstractTestCase.dataPath.resolve("test44.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test44.m4a");

			// Read File okay
			AudioFile af = AudioFileIO.read(testFile);

			// Write file
			af.getTag().setField(FieldKey.ARTIST, "FREDDYCOUGAR");
			af.getTag().setField(FieldKey.ALBUM, "album");
			af.getTag().setField(FieldKey.TITLE, "title");
			af.getTag().setField(FieldKey.GENRE, "genre");
			af.getTag().setField(FieldKey.YEAR, "year");
			af.commit();

			// Read file again okay
			af = AudioFileIO.read(testFile);
			assertEquals("FREDDYCOUGAR", af.getTag().getFirst(FieldKey.ARTIST));
			assertEquals("album", af.getTag().getFirst(FieldKey.ALBUM));
			assertEquals("title", af.getTag().getFirst(FieldKey.TITLE));
			assertEquals("genre", af.getTag().getFirst(FieldKey.GENRE));
			assertEquals("year", af.getTag().getFirst(FieldKey.YEAR));

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

}
