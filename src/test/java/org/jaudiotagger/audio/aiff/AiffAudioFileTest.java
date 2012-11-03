package org.jaudiotagger.audio.aiff;

import java.nio.file.Path;
import java.util.List;

import junit.framework.TestCase;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;

public class AiffAudioFileTest extends TestCase {

	public void testReadAiff() {
		Exception exceptionCaught = null;
		final Path testFile0 = AbstractTestCase.copyAudioToTmp("M1F1-int8C-AFsp.aif");
		try {
			final AudioFile f = AudioFileIO.read(testFile0);
			final AudioHeader ah = f.getAudioHeader();
			assertTrue(ah instanceof AiffAudioHeader);
		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);

		final Path testFile1 = AbstractTestCase.copyAudioToTmp("M1F1-int8-AFsp.aif");
		try {
			final AudioFile f = AudioFileIO.read(testFile1);
			final AudioHeader ah = f.getAudioHeader();
			assertTrue(ah instanceof AiffAudioHeader);
			final AiffAudioHeader aah = (AiffAudioHeader) ah;
			final List<String> appIdentifiers = aah.getApplicationIdentifiers();
			final String ident = appIdentifiers.get(0);
			assertTrue(ident.indexOf("CAPELLA") > 0);
		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);

		final Path testFile2 = AbstractTestCase.copyAudioToTmp("ExportedFromItunes.aif");
		try {
			final AudioFile f = AudioFileIO.read(testFile2);
			final AudioHeader ah = f.getAudioHeader();
			assertTrue(ah instanceof AiffAudioHeader);
			final Tag tag = f.getTag();
			assertNotNull(tag);
			assertTrue(tag instanceof AiffTag);
			assertTrue(tag.getFieldCount() == 10);
			assertEquals("Gary McGath", tag.getFirst(FieldKey.ARTIST));
			assertEquals("None", tag.getFirst(FieldKey.ALBUM));
			assertTrue(tag.getFirst(FieldKey.TITLE).indexOf("Short sample") == 0);
			assertEquals("This is actually a comment.", tag.getFirst(FieldKey.COMMENT));
			assertEquals("2012", tag.getFirst(FieldKey.YEAR));
			assertEquals("1", tag.getFirst(FieldKey.TRACK));

		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

}
