package org.jaudiotagger.issues;

import java.nio.file.Path;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Test GetAll functionality for mp4
 */
public class Issue423Test extends AbstractTestCase {
	public void testGetAllMp4() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTag();
		assertTrue(tag.hasField(FieldKey.TRACK));
		assertEquals("1", tag.getFirst(FieldKey.TRACK));
		assertEquals(1, tag.getAll(FieldKey.TRACK).size());
		assertTrue(tag.hasField(FieldKey.TITLE));
		assertEquals("title", tag.getFirst(FieldKey.TITLE));
		assertEquals(1, tag.getAll(FieldKey.TITLE).size());
		assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
		;
	}

	public void testGetAllMp3() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTagOrCreateDefault();
		tag.setField(FieldKey.TRACK, "1");
		tag.setField(FieldKey.TITLE, "title");
		assertTrue(tag.hasField(FieldKey.TRACK));
		assertEquals("1", tag.getFirst(FieldKey.TRACK));
		assertEquals(1, tag.getAll(FieldKey.TRACK).size());
		assertTrue(tag.hasField(FieldKey.TITLE));
		assertEquals("title", tag.getFirst(FieldKey.TITLE));
		assertEquals(1, tag.getAll(FieldKey.TITLE).size());
		assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
		;
	}

	public void testGetAllOgg() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTagOrCreateDefault();
		tag.setField(FieldKey.TRACK, "1");
		tag.setField(FieldKey.TITLE, "title");
		assertTrue(tag.hasField(FieldKey.TRACK));
		assertEquals("1", tag.getFirst(FieldKey.TRACK));
		assertEquals(1, tag.getAll(FieldKey.TRACK).size());
		assertTrue(tag.hasField(FieldKey.TITLE));
		assertEquals("title", tag.getFirst(FieldKey.TITLE));
		assertEquals(1, tag.getAll(FieldKey.TITLE).size());
		assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
		;
	}

	public void testGetAllFlac() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("test.flac");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTagOrCreateDefault();
		tag.setField(FieldKey.TRACK, "1");
		tag.setField(FieldKey.TITLE, "title");
		assertTrue(tag.hasField(FieldKey.TRACK));
		assertEquals("1", tag.getFirst(FieldKey.TRACK));
		assertEquals(1, tag.getAll(FieldKey.TRACK).size());
		assertTrue(tag.hasField(FieldKey.TITLE));
		assertEquals("title", tag.getFirst(FieldKey.TITLE));
		assertEquals(1, tag.getAll(FieldKey.TITLE).size());
		assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
		;
	}

	public void testGetAllWma() throws Exception {
		final Path testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
		final AudioFile f = AudioFileIO.read(testFile);
		final Tag tag = f.getTagOrCreateDefault();
		tag.setField(FieldKey.TRACK, "1");
		tag.setField(FieldKey.TITLE, "title");
		assertTrue(tag.hasField(FieldKey.TRACK));
		assertEquals("1", tag.getFirst(FieldKey.TRACK));
		assertEquals(1, tag.getAll(FieldKey.TRACK).size());
		assertTrue(tag.hasField(FieldKey.TITLE));
		assertEquals("title", tag.getFirst(FieldKey.TITLE));
		assertEquals(1, tag.getAll(FieldKey.TITLE).size());
		assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
		;
	}
}
