package org.jaudiotagger.issues;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;

/**
 * Unable to write, offsets do not match
 */
public class Issue291Test extends AbstractTestCase {
	public void testSavingFile() {
		final Path orig = AbstractTestCase.dataPath.resolve("test83.mp4");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test83.mp4");
			AudioFile af = AudioFileIO.read(testFile);
			System.out.println("Tag is" + af.getTag().toString());
			af.getTag().setField(af.getTag().createField(FieldKey.ARTIST, "Kenny Rankin1"));
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("Kenny Rankin1", af.getTag().getFirst(FieldKey.ARTIST));
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}

	public void testPrintAtomTree() {
		final Path orig = AbstractTestCase.dataPath.resolve("test83.mp4");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test83.mp4");
			final Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile.toFile(), "r"));
			atomTree.printAtomTree();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}
}
