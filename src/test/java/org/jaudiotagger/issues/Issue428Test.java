package org.jaudiotagger.issues;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.FlacAudioHeader;

/**
 * Reading MD5 Integrity Checksum
 */
public class Issue428Test extends AbstractTestCase {
	public void testGetMD5ForFlac() {
		Throwable e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.flac");
			final AudioFile af = AudioFileIO.read(testFile);
			assertTrue(af.getAudioHeader() instanceof FlacAudioHeader);
			assertEquals("4d285826d15a2d38b4d02b4dc2d3f4e1", ((FlacAudioHeader) af.getAudioHeader()).getMd5());

		} catch (final Exception ex) {
			e = ex;
		}
		assertNull(e);
	}
}
