package org.jaudiotagger.issues;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

/**
 * Test APIC Frame with no PictureType Field
 */
public class Issue224Test extends AbstractTestCase {

	public void testReadInvalidPicture() {
		final String genre = null;

		final Path orig = AbstractTestCase.dataPath.resolve("test31.mp3");
		if (!Files.isRegularFile(orig))
			return;

		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test31.mp3");
			final AudioFile f = AudioFileIO.read(testFile);
			final Tag tag = f.getTag();
			assertEquals(10, tag.getFieldCount());
			assertTrue(tag instanceof ID3v23Tag);
			final ID3v23Tag id3v23Tag = (ID3v23Tag) tag;
			final TagField coverArtField = id3v23Tag.getFirstField(org.jaudiotagger.tag.id3.ID3v23FieldKey.COVER_ART.getFieldName());
			assertTrue(coverArtField instanceof ID3v23Frame);
			assertTrue(((ID3v23Frame) coverArtField).getBody() instanceof FrameBodyAPIC);
			final FrameBodyAPIC body = (FrameBodyAPIC) ((ID3v23Frame) coverArtField).getBody();
			final byte[] imageRawData = body.getImageData();
			final BufferedImage bi = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(imageRawData)));
			assertEquals(953, bi.getWidth());
			assertEquals(953, bi.getHeight());

			assertEquals("image/png", body.getMimeType());
			assertEquals("", body.getDescription());
			assertEquals("", body.getImageUrl());

			// This is an invalid value (probably first value of PictureType)
			assertEquals(208, body.getPictureType());

			assertFalse(body.isImageUrl());

			// SetDescription
			body.setDescription("FREDDY");
			assertEquals("FREDDY", body.getDescription());
			f.commit();

		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
		assertNull(genre);

	}
}
