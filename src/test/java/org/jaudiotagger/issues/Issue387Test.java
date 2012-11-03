package org.jaudiotagger.issues;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;

/**
 * Test writing mp4
 */
public class Issue387Test extends AbstractTestCase {
	public void testIssue() throws Exception {
		Exception caught = null;
		try {
			final Path orig = AbstractTestCase.dataPath.resolve("test100.mp4");
			if (!Files.isRegularFile(orig)) {
				System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
				return;
			}

			final Path testFile = AbstractTestCase.copyAudioToTmp("test100.mp4");
			final AudioFile af = AudioFileIO.read(testFile);
			System.out.println(af.getAudioHeader());
			af.getTagOrCreateAndSetDefault();
			af.commit();

			final Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile.toFile(), "r"));
			atomTree.printAtomTree();

		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}
}
