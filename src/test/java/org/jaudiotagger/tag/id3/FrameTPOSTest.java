package org.jaudiotagger.tag.id3;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

/**
 * Test TPOSFrame
 */
public class FrameTPOSTest extends AbstractTestCase {
	public void testMergingMultipleFrames() throws Exception {
		final ID3v24Tag tag = new ID3v24Tag();
		tag.setField(tag.createField(FieldKey.DISC_NO, "1"));
		tag.setField(tag.createField(FieldKey.DISC_TOTAL, "10"));
		assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
		assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
		assertTrue(tag.getFrame("TPOS") instanceof AbstractID3v2Frame);
	}

	public void testDiscNo() {
		Exception exceptionCaught = null;
		final Path orig = AbstractTestCase.dataPath.resolve("test82.mp3");
		if (!Files.isRegularFile(orig)) {
			System.err.println("Unable to test file " + orig.toAbsolutePath() + " - not available");
			return;
		}

		try {
			final AudioFile af = AudioFileIO.read(orig);
			final Tag newTags = af.getTag();
			final Iterator<TagField> i = newTags.getFields();
			while (i.hasNext())
				System.out.println(i.next().getId());
		} catch (final Exception e) {
			exceptionCaught = e;
		}

		assertNull(exceptionCaught);
	}
}
