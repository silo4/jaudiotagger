package org.jaudiotagger.audio;

import java.nio.file.Path;

import junit.framework.TestCase;

import org.jaudiotagger.AbstractTestCase;

/**
 * Generic tests
 */
public class GenericTest extends TestCase {
	/**
	 * Test File filter, positive and negative tests
	 */
	public void testReadFileUnsupportedFormat() {
		final Path nonAudioFile = AbstractTestCase.dataPath.resolve("coverart.bmp");
		final AudioFileFilter aff = new AudioFileFilter();
		aff.accept(nonAudioFile);
		assertFalse(aff.accept(nonAudioFile));

		final Path audioFile0 = AbstractTestCase.dataPath.resolve("test.m4a");
		aff.accept(audioFile0);
		assertTrue(aff.accept(audioFile0));

		final Path audioFile1 = AbstractTestCase.dataPath.resolve("test.flac");
		aff.accept(audioFile1);
		assertTrue(aff.accept(audioFile1));

		final Path audioFile2 = AbstractTestCase.dataPath.resolve("test.ogg");
		aff.accept(audioFile2);
		assertTrue(aff.accept(audioFile2));

		final Path audioFile3 = AbstractTestCase.dataPath.resolve("testV1.mp3");
		aff.accept(audioFile3);
		assertTrue(aff.accept(audioFile3));
	}
}
