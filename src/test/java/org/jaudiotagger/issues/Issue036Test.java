package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Frame;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * Test frame and Tag Equality
 */
public class Issue036Test extends AbstractTestCase {
	public void testIDv24Frame() throws Exception {
		final ID3v24Frame frame1 = new ID3v24Frame();
		final ID3v24Frame frame2 = new ID3v24Frame();
		final ID3v24Frame frame3 = new ID3v24Frame("TPE1");
		final ID3v24Frame frame4 = new ID3v24Frame("TPE1");
		final ID3v24Frame frame5 = new ID3v24Frame("TPE1");
		frame5.getBody().setTextEncoding((byte) 1);

		assertTrue(frame1.equals(frame1));
		assertTrue(frame1.equals(frame2));
		assertFalse(frame1.equals(frame3));

		assertTrue(frame3.equals(frame3));
		assertTrue(frame3.equals(frame4));
		assertFalse(frame3.equals(frame5));
	}

	public void testAllID3v24Frames() throws Exception {
		for (final String frameId : ID3v24Frames.getInstanceOf().getSupportedFrames()) {
			// System.out.println("Testing:"+frameId);
			final ID3v24Frame frame1 = new ID3v24Frame(frameId);
			final ID3v24Frame frame2 = new ID3v24Frame(frameId);
			assertTrue(frame1.equals(frame2));
		}
	}

	public void testIDv24Tag() throws Exception {
		final ID3v24Tag tag1 = new ID3v24Tag();
		final ID3v24Tag tag2 = new ID3v24Tag();
		final ID3v24Tag tag3 = new ID3v24Tag();
		final ID3v24Tag tag4 = new ID3v24Tag();
		final ID3v24Tag tag5 = new ID3v24Tag();
		tag3.addField(FieldKey.ALBUM, "Porcupine");
		tag4.addField(FieldKey.ALBUM, "Porcupine");
		tag5.addField(FieldKey.ALBUM, "Porcupine");
		tag5.addField(FieldKey.ARTIST, "Echo & the Bunnymen");

		assertTrue(tag1.equals(tag1));
		assertTrue(tag1.equals(tag2));
		assertFalse(tag1.equals(tag3));

		assertTrue(tag3.equals(tag3));
		assertTrue(tag3.equals(tag4));
		assertFalse(tag3.equals(tag5));
	}

	public void testIDv23Frame() throws Exception {
		final ID3v23Frame frame1 = new ID3v23Frame();
		final ID3v23Frame frame2 = new ID3v23Frame();
		final ID3v23Frame frame3 = new ID3v23Frame("TPE1");
		final ID3v23Frame frame4 = new ID3v23Frame("TPE1");
		final ID3v23Frame frame5 = new ID3v23Frame("TPE1");
		frame5.getBody().setTextEncoding((byte) 1);

		assertTrue(frame1.equals(frame1));
		assertTrue(frame1.equals(frame2));
		assertFalse(frame1.equals(frame3));

		assertTrue(frame3.equals(frame3));
		assertTrue(frame3.equals(frame4));
		assertFalse(frame3.equals(frame5));
	}

	public void testAllID3v23Frames() throws Exception {
		for (final String frameId : ID3v23Frames.getInstanceOf().getSupportedFrames()) {
			// System.out.println("Testing:"+frameId);
			final ID3v23Frame frame1 = new ID3v23Frame(frameId);
			final ID3v23Frame frame2 = new ID3v23Frame(frameId);
			assertTrue(frame1.equals(frame2));
		}
	}

	public void testIDv23Tag() throws Exception {
		final ID3v23Tag tag1 = new ID3v23Tag();
		final ID3v23Tag tag2 = new ID3v23Tag();
		final ID3v23Tag tag3 = new ID3v23Tag();
		final ID3v23Tag tag4 = new ID3v23Tag();
		final ID3v23Tag tag5 = new ID3v23Tag();
		tag3.addField(FieldKey.ALBUM, "Porcupine");
		tag4.addField(FieldKey.ALBUM, "Porcupine");
		tag5.addField(FieldKey.ALBUM, "Porcupine");
		tag5.addField(FieldKey.ARTIST, "Echo & the Bunnymen");

		assertTrue(tag1.equals(tag1));
		assertTrue(tag1.equals(tag2));
		assertFalse(tag1.equals(tag3));

		assertTrue(tag3.equals(tag3));
		assertTrue(tag3.equals(tag4));
		assertFalse(tag3.equals(tag5));
	}

	public void testIDv22Frame() throws Exception {
		final ID3v22Frame frame1 = new ID3v22Frame();
		final ID3v22Frame frame2 = new ID3v22Frame();
		final ID3v22Frame frame3 = new ID3v22Frame("TP1");
		final ID3v22Frame frame4 = new ID3v22Frame("TP1");
		final ID3v22Frame frame5 = new ID3v22Frame("TP1");
		frame5.getBody().setTextEncoding((byte) 1);

		assertTrue(frame1.equals(frame1));
		assertTrue(frame1.equals(frame2));
		assertFalse(frame1.equals(frame3));

		assertTrue(frame3.equals(frame3));
		assertTrue(frame3.equals(frame4));
		assertFalse(frame3.equals(frame5));
	}

	public void testAllID3v22Frames() throws Exception {
		for (final String frameId : ID3v22Frames.getInstanceOf().getSupportedFrames()) {
			final ID3v22Frame frame1 = new ID3v22Frame(frameId);
			final ID3v22Frame frame2 = new ID3v22Frame(frameId);
			assertTrue(frame1.equals(frame2));
		}
	}

	public void testIDv22Tag() throws Exception {
		final ID3v22Tag tag1 = new ID3v22Tag();
		final ID3v22Tag tag2 = new ID3v22Tag();
		final ID3v22Tag tag3 = new ID3v22Tag();
		final ID3v22Tag tag4 = new ID3v22Tag();
		final ID3v22Tag tag5 = new ID3v22Tag();
		tag3.addField(FieldKey.ALBUM, "Porcupine");
		tag4.addField(FieldKey.ALBUM, "Porcupine");
		tag5.addField(FieldKey.ALBUM, "Porcupine");
		tag5.addField(FieldKey.ARTIST, "Echo & the Bunnymen");

		assertTrue(tag1.equals(tag1));
		assertTrue(tag1.equals(tag2));
		assertFalse(tag1.equals(tag3));

		assertTrue(tag3.equals(tag3));
		assertTrue(tag3.equals(tag4));
		assertFalse(tag3.equals(tag5));
	}
}
