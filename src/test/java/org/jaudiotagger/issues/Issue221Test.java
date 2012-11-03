package org.jaudiotagger.issues;

import java.io.FileOutputStream;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

/**
 * Test Creating Null fields
 */
public class Issue221Test extends AbstractTestCase {

	public void testCreateNullMp4FrameTitle() {
		Exception exceptionCaught = null;
		try {
			final Mp4Tag tag = new Mp4Tag();
			tag.setField(FieldKey.TITLE, null);
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullOggVorbisFrameTitle() {
		Exception exceptionCaught = null;
		try {
			final VorbisCommentTag tag = VorbisCommentTag.createNewTag();
			tag.setField(FieldKey.TITLE, null);
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v23FrameTitle() {
		Exception exceptionCaught = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			tag.setField(FieldKey.TITLE, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v23FrameAlbum() {
		Exception exceptionCaught = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			tag.setField(FieldKey.ALBUM, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v23FrameArtist() {
		Exception exceptionCaught = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			tag.setField(FieldKey.ARTIST, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v23FrameComment() {
		Exception exceptionCaught = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			tag.setField(FieldKey.COMMENT, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v23FrameGenre() {
		Exception exceptionCaught = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			tag.setField(FieldKey.GENRE, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v23FrameTrack() {
		Exception exceptionCaught = null;
		try {
			final ID3v23Tag tag = new ID3v23Tag();
			tag.setField(FieldKey.TRACK, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);

	}

	public void testCreateNullID3v24Frame() {
		Exception exceptionCaught = null;
		try {
			final ID3v24Tag tag = new ID3v24Tag();
			tag.setField(FieldKey.TITLE, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v24.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}

	public void testCreateNullID3v22Frame() {
		Exception exceptionCaught = null;
		try {
			final ID3v22Tag tag = new ID3v22Tag();
			tag.setField(FieldKey.TITLE, null);
			final FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v24.mp3");
			tag.write(os.getChannel());
			os.close();
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}

		assertTrue(exceptionCaught instanceof IllegalArgumentException);
	}
}
