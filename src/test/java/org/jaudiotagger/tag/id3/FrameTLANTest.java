package org.jaudiotagger.tag.id3;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;

/**
 * Test TLANFrame
 */
public class FrameTLANTest extends AbstractTestCase {
	public void testWriteFileContainingTLANFrame() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("Issue116.id3", "testV1.mp3");

		final MP3File mp3File = new MP3File(testFile);
		mp3File.save();
	}
}
