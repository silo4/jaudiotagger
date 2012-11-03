package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp3.MPEGFrameHeader;

/**
 * Test reading Version 2 Layer III file correctly
 */
public class Issue028Test extends AbstractTestCase {
	public void testReadV2L3Stereo() {
		final Path orig = AbstractTestCase.dataPath.resolve("test97.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception exceptionCaught = null;
		final Path testFile = AbstractTestCase.copyAudioToTmp("test97.mp3");
		MP3AudioHeader mp3AudioHeader = null;
		try {
			mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
		assertEquals("22050", mp3AudioHeader.getSampleRate());
		assertEquals("04:03", mp3AudioHeader.getTrackLengthAsString());
		assertFalse(mp3AudioHeader.isVariableBitRate());
		assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2)), mp3AudioHeader.getMpegVersion());
		assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
		assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_JOINT_STEREO)), mp3AudioHeader.getChannels());
		assertFalse(mp3AudioHeader.isOriginal());
		assertFalse(mp3AudioHeader.isCopyrighted());
		assertFalse(mp3AudioHeader.isPrivate());
		assertFalse(mp3AudioHeader.isProtected());
		assertEquals("32", mp3AudioHeader.getBitRate());
		assertEquals("mp3", mp3AudioHeader.getEncodingType());

	}
}
