package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;

/**
 * Test tag Equality (specifically PartOfSet)
 */
public class Issue320Test extends AbstractTestCase {
	/*
	 * Test File Equality
	 * 
	 * @throws Exception
	 */
	public void testTagEquality() throws Exception {
		final Path orig = AbstractTestCase.dataPath.resolve("test26.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		final Path file1 = AbstractTestCase.dataPath.resolve("test26.mp3");
		final Path file2 = AbstractTestCase.dataPath.resolve("test26.mp3");

		final MP3File audioFile1 = (MP3File) AudioFileIO.read(file1);
		final Tag tag1 = audioFile1.getTag();

		final MP3File audioFile2 = (MP3File) AudioFileIO.read(file2);
		final Tag tag2 = audioFile2.getTag();

		assertTrue(tag1.equals(tag2));

	}
}
