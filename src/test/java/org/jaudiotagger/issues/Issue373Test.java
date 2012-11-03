package org.jaudiotagger.issues;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;

/**
 * Test writing of new files
 */
public class Issue373Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("test94.mp3");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("test94.mp3");

			final AudioFile af = AudioFileIO.read(testFile);
			af.setTag(new ID3v23Tag());
			af.getTag().setField(FieldKey.ARTIST, "artist");

			Thread.sleep(20000);
			// Now open in another program to lock it, cannot reproduce programtically
			// FileChannel channel = new RandomAccessFile(testFile, "rw").getChannel();
			// FileLock lock = channel.lock();

			af.commit();
		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
