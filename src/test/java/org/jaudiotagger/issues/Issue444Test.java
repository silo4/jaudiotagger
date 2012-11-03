package org.jaudiotagger.issues;

import java.nio.file.Path;
import java.util.Iterator;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.TyerTdatAggregatedFrame;
import org.jaudiotagger.tag.reference.ID3V2Version;

/**
 * ID3v23 Date needs splitting into frames
 */
public class Issue444Test extends AbstractTestCase {
	public void testFullDateWrittenToID3v24() {
		try {
			TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault();
			af.getTag().setField(FieldKey.YEAR, "2004-10-12");
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("2004-10-12", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v24Tag) af.getTag()).getFrame("TYER"));
			assertNotNull(((ID3v24Tag) af.getTag()).getFrame("TDRC"));

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testFullDateWrittenToID3v23NeedsToBeSplitIntoFrames() {
		try {
			TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault();
			af.getTag().setField(FieldKey.YEAR, "2004-10-12");
			assertEquals("2004-10-12", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDAT"));
			assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

			TyerTdatAggregatedFrame aggframe = (TyerTdatAggregatedFrame) (((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
			Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
			assertEquals("2004", i.next().getContent());
			assertEquals("1210", i.next().getContent());
			assertEquals("2004-10-12", aggframe.getContent());
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("2004-10-12", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDAT"));
			assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
			aggframe = (TyerTdatAggregatedFrame) (((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
			i = aggframe.getFrames().iterator();
			assertEquals("2004", i.next().getContent());
			assertEquals("1210", i.next().getContent());

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testYearAndMonthWrittenToID3v23NeedsToBeSplitIntoFrames() {
		try {
			TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault();
			af.getTag().setField(FieldKey.YEAR, "2004-10");
			assertEquals("2004-10-01", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDAT"));
			assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

			TyerTdatAggregatedFrame aggframe = (TyerTdatAggregatedFrame) (((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
			Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
			assertEquals("2004", i.next().getContent());
			assertEquals("0110", i.next().getContent());
			assertEquals("2004-10-01", aggframe.getContent());
			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("2004-10-01", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDAT"));
			assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
			aggframe = (TyerTdatAggregatedFrame) (((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
			i = aggframe.getFrames().iterator();
			assertEquals("2004", i.next().getContent());
			assertEquals("0110", i.next().getContent());

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testYearWrittenToID3v23NeedsToBePutInTyerFrame() {
		try {
			TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
			AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault();
			af.getTag().setField(FieldKey.YEAR, "2004");
			assertEquals("2004", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));
			assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDAT"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

			af.commit();
			af = AudioFileIO.read(testFile);
			assertEquals("2004", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));
			assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDAT"));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testInvalidYearNotWrittenToID3v23() {
		Exception e = null;
		try {
			TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
			final Path testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
			final AudioFile af = AudioFileIO.read(testFile);
			af.getTagOrCreateAndSetDefault();
			af.getTag().setField(FieldKey.YEAR, "20");
			assertEquals("", af.getTag().getFirst(FieldKey.YEAR));
			assertNull(((ID3v23Tag) af.getTag()).getFrame("TDRC"));

		} catch (final Exception ex) {
			e = ex;
		}
		assertNotNull(e);
		assertTrue(e instanceof FieldDataInvalidException);
	}

	public void testDuplicates() {
		Exception e = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test106.mp3");
			final AudioFile af = AudioFileIO.read(testFile);

		} catch (final Exception ex) {
			ex.printStackTrace();
			e = ex;
		}
		assertNull(e);
	}
}
