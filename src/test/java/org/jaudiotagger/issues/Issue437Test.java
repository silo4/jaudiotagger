package org.jaudiotagger.issues;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Adding 'getBitsPerSample()' accessor to AudioHeader
 */
public class Issue437Test extends AbstractTestCase {
	public void testGetBitsPerSampleFlac() {
		Throwable e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.flac");
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals(16, af.getAudioHeader().getBitsPerSample());
		} catch (final Exception ex) {
			e = ex;
		}
		assertNull(e);
	}

	public void testGetBitsPerSampleMp4() {
		Throwable e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals(16, af.getAudioHeader().getBitsPerSample());
		} catch (final Exception ex) {
			e = ex;
		}
		assertNull(e);
	}

	public void testGetBitsPerSampleOgg() {
		Throwable e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals(16, af.getAudioHeader().getBitsPerSample());
		} catch (final Exception ex) {
			e = ex;
		}
		assertNull(e);
	}

	public void testGetBitsPerSampleWma() {
		Throwable e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals(16, af.getAudioHeader().getBitsPerSample());
		} catch (final Exception ex) {
			e = ex;
		}
		assertNull(e);
	}

	public void testGetBitsPerSampleMp3() {
		Throwable e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			assertEquals(16, af.getAudioHeader().getBitsPerSample());
		} catch (final Exception ex) {
			e = ex;
		}
		assertNull(e);
	}
}
