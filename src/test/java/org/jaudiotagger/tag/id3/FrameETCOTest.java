package org.jaudiotagger.tag.id3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Testing Etco (Issue 187)
 */
public class FrameETCOTest extends AbstractTestCase {
	/**
	 * This tests reading a file that contains an ETCO frame
	 * 
	 * @throws Exception
	 */
	public void testReadFile() throws Exception {
		final File orig = new File("testdata", "test20.mp3");
		if (!orig.isFile())
			return;
		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test20.mp3");
			final AudioFile f = AudioFileIO.read(testFile);
		} catch (final IOException e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}
}
