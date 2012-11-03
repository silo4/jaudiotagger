package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v22Frame;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyIPLS;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;

/**
 * Test Writing to new urls with common interface
 */
public class Issue249Test extends AbstractTestCase {
	/**
	 * Test New Urls ID3v24
	 */
	public void testConvertv22tagWithIPLSToV24() {
		ID3v24Tag v24tag = null;
		ID3v22Tag v22tag = null;
		final Path orig = AbstractTestCase.dataPath.resolve("test34.mp3");
		if (!Files.isRegularFile(orig))
			return;

		Exception exceptionCaught = null;
		try {
			// This is a V2 File with an IPL frame
			final Path testFile = AbstractTestCase.copyAudioToTmp("test34.mp3");

			// Convert to v24Tag
			final AudioFile af = AudioFileIO.read(testFile);
			final MP3File mp3File = (MP3File) af;
			v24tag = mp3File.getID3v2TagAsv24();
			v22tag = (ID3v22Tag) mp3File.getID3v2Tag();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
		final ID3v24Frame frame = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
		final ID3v22Frame framev2 = (ID3v22Frame) v22tag.getFrame(ID3v22Frames.FRAME_ID_V2_IPLS);

		assertNotNull(frame);
		assertNotNull(framev2);

		// Original
		final FrameBodyIPLS framebodyv2 = (FrameBodyIPLS) framev2.getBody();
		assertEquals(1, framebodyv2.getNumberOfPairs());
		assertEquals("", framebodyv2.getKeyAtIndex(0));
		assertEquals("PRAISE J.R. \"BOB\" DOBBS!!!", framebodyv2.getValueAtIndex(0));

		// Converted to v24
		final FrameBodyTIPL framebody = (FrameBodyTIPL) frame.getBody();
		assertEquals(1, framebody.getNumberOfPairs());
		assertEquals("", framebody.getKeyAtIndex(0));
		assertEquals("PRAISE J.R. \"BOB\" DOBBS!!!", framebody.getValueAtIndex(0));
	}

}
