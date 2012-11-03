package org.jaudiotagger.tag.id3;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTCMP;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTCMPTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TCMP Frame
 */
public class FrameTCMPTest extends AbstractTestCase {
	public static ID3v24Frame getInitialisedFrame() {
		final ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
		final FrameBodyTCMP fb = FrameBodyTCMPTest.getInitialisedBody();
		frame.setBody(fb);
		return frame;
	}

	public void testCreateID3v24Frame() {
		Exception exceptionCaught = null;
		ID3v24Frame frame = null;
		FrameBodyTCMP fb = null;
		try {
			frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
			fb = FrameBodyTCMPTest.getInitialisedBody();
			frame.setBody(fb);
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
		assertEquals(ID3v24Frames.FRAME_ID_IS_COMPILATION, frame.getIdentifier());
		assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
		assertTrue(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
		assertFalse(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));

	}

	public void testCreateID3v23Frame() {
		Exception exceptionCaught = null;
		ID3v23Frame frame = null;
		FrameBodyTCMP fb = null;
		try {
			frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_IS_COMPILATION);
			fb = FrameBodyTCMPTest.getInitialisedBody();
			frame.setBody(fb);
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
		assertEquals(ID3v23Frames.FRAME_ID_V3_IS_COMPILATION, frame.getIdentifier());
		assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
		assertTrue(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
		assertFalse(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));

	}

	public void testCreateID3v22Frame() {
		Exception exceptionCaught = null;
		ID3v22Frame frame = null;
		FrameBodyTCMP fb = null;
		try {
			frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_IS_COMPILATION);
			fb = FrameBodyTCMPTest.getInitialisedBody();
			frame.setBody(fb);
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
		assertEquals(ID3v22Frames.FRAME_ID_V2_IS_COMPILATION, frame.getIdentifier());
		assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
		assertTrue(ID3v22Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
		assertFalse(ID3v22Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));

	}

	public void testSaveToFile() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", Paths.get("test1000.mp3"));
		MP3File mp3File = new MP3File(testFile);

		// Create and Save
		final ID3v24Tag tag = new ID3v24Tag();
		tag.setFrame(FrameTCMPTest.getInitialisedFrame());
		mp3File.setID3v2Tag(tag);
		mp3File.save();

		// Reload
		mp3File = new MP3File(testFile);
		final ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
		final FrameBodyTCMP body = (FrameBodyTCMP) frame.getBody();
		assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
	}

	public void testSaveEmptyFrameToFile() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", Paths.get("test1001.mp3"));
		MP3File mp3File = new MP3File(testFile);

		ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
		frame.setBody(new FrameBodyTCMP());

		// Create and Save
		final ID3v24Tag tag = new ID3v24Tag();
		tag.setFrame(frame);
		mp3File.setID3v2Tag(tag);
		mp3File.save();

		// Reload
		mp3File = new MP3File(testFile);
		frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
		final FrameBodyTCMP body = (FrameBodyTCMP) frame.getBody();
		assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
	}

	public void testConvertV24ToV23() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", Paths.get("test1002.mp3"));
		MP3File mp3File = new MP3File(testFile);

		// Create and Save
		final ID3v24Tag tag = new ID3v24Tag();
		tag.setFrame(FrameTCMPTest.getInitialisedFrame());
		mp3File.setID3v2Tag(tag);
		mp3File.save();

		// Reload and convert to v23 and save
		mp3File = new MP3File(testFile);
		mp3File.setID3v2TagOnly(new ID3v23Tag(mp3File.getID3v2TagAsv24()));
		mp3File.save();

		// Reload
		mp3File = new MP3File(testFile);
		final ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_IS_COMPILATION);
		final FrameBodyTCMP body = (FrameBodyTCMP) frame.getBody();
		assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
	}

	public void testConvertV22ToV24() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", Paths.get("test1003.mp3"));
		MP3File mp3File = new MP3File(testFile);

		// Create and Save
		final ID3v22Tag tag = new ID3v22Tag();
		final ID3v22Frame id3v22frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_IS_COMPILATION);
		tag.setFrame(id3v22frame);
		mp3File.setID3v2TagOnly(tag);
		mp3File.save();

		// Reload and convert to v23 and save
		mp3File = new MP3File(testFile);
		mp3File.setID3v2TagOnly(new ID3v24Tag(mp3File.getID3v2Tag()));
		mp3File.save();

		// Reload
		mp3File = new MP3File(testFile);
		final ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
		final FrameBodyTCMP body = (FrameBodyTCMP) frame.getBody();
		assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
	}
}
