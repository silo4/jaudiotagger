package org.jaudiotagger.issues;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;

/**
 * Test Writing to new urls with common interface
 */
public class Issue255Test extends AbstractTestCase {
	/**
	 * Test Mp4 with padding after last atom
	 */
	public void testReadMp4FileWithPaddingAfterLastAtom() {
		final Path orig = AbstractTestCase.dataPath.resolve("test35.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test35.m4a");

			// Read File
			final AudioFile af = AudioFileIO.read(testFile);

			// Print Out Tree

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);

		try {
			// Now just createField tree
			final Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile.toFile(), "r"));
			atomTree.printAtomTree();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

	/**
	 * Test to write all data to a m4p which has a padding but no MDAT Dat aso fails on read
	 * <p/>
	 */
	public void testReadFileWithInvalidPadding() {
		final Path orig = AbstractTestCase.dataPath.resolve("test28.m4p");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test28.m4p", Paths.get("WriteFileWithInvalidFreeAtom.m4p"));

			final AudioFile f = AudioFileIO.read(testFile);
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof CannotReadException);
	}

	/**
	 * Test Mp4 with padding after last atom
	 */
	public void testWriteMp4FileWithPaddingAfterLastAtom() {
		final Path orig = AbstractTestCase.dataPath.resolve("test35.m4a");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		Path testFile = null;
		Exception exceptionCaught = null;
		try {
			testFile = AbstractTestCase.copyAudioToTmp("test35.m4a");

			// Add a v24Tag
			final AudioFile af = AudioFileIO.read(testFile);
			af.getTag().setField(FieldKey.ALBUM, "NewValue");
			af.commit();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		// Ensure temp file deleted
		try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(testFile.getParent())) {
			final Iterator<Path> files = fileStream.iterator();
			while (files.hasNext()) {
				final Path file = files.next();
				System.out.println("Checking " + file.getFileName());
				assertFalse(file.getFileName().toString().matches(".*test35.*.tmp"));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		assertNull(exceptionCaught);
	}
}
