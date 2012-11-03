package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v22Frame;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;

/**
 * Converting FrameBodyUnsupported with known identifier to FrameBodyIPLS (v23) causing NoSuchMethodException. Not
 * really sure why this is happening but we should check and take action instead of failing as we currently do
 */
public class Issue284Test extends AbstractTestCase {
	public void testConvertv23v24() {
		final Path orig = AbstractTestCase.dataPath.resolve("testV1.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
			final MP3File af = (MP3File) AudioFileIO.read(testFile);

			final ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
			final FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
			frame.setBody(fb);
			assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
			final ID3v23Frame framev23 = new ID3v23Frame(frame);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testConvertv22v24() {
		final Path orig = AbstractTestCase.dataPath.resolve("testV1.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
			final MP3File af = (MP3File) AudioFileIO.read(testFile);

			final ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
			final FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
			frame.setBody(fb);
			assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
			final ID3v22Frame framev22 = new ID3v22Frame(frame);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testConvertv24v23() {
		final Path orig = AbstractTestCase.dataPath.resolve("testV1.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
			final MP3File af = (MP3File) AudioFileIO.read(testFile);

			final ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_IPLS);
			final FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v23Frames.FRAME_ID_V3_IPLS);
			frame.setBody(fb);
			assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
			final ID3v24Frame framev24 = new ID3v24Frame(frame);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testConvertv24v22() {
		final Path orig = AbstractTestCase.dataPath.resolve("testV1.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
			final MP3File af = (MP3File) AudioFileIO.read(testFile);

			final ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_TITLE);
			final FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v22Frames.FRAME_ID_V2_TITLE);
			frame.setBody(fb);
			assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
			final ID3v24Frame framev24 = new ID3v24Frame(frame);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testConvertv22v23() {
		final Path orig = AbstractTestCase.dataPath.resolve("testV1.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
			final MP3File af = (MP3File) AudioFileIO.read(testFile);

			final ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TITLE);
			final FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v23Frames.FRAME_ID_V3_TITLE);
			frame.setBody(fb);
			assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
			final ID3v22Frame framev22 = new ID3v22Frame(frame);

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}
}
