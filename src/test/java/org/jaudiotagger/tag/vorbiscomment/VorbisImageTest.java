package org.jaudiotagger.tag.vorbiscomment;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;

/**
 */
public class VorbisImageTest extends AbstractTestCase {
	/**
	 * Test can read file with base64 encoded image from ogg
	 * <p/>
	 * Works
	 */
	public void testReadFileWithSmallImageTag() {
		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("testsmallimage.ogg");
			final AudioFile f = AudioFileIO.read(testFile);
			final String mimeType = ((VorbisCommentTag) f.getTag()).getFirst(VorbisCommentFieldKey.COVERARTMIME);
			assertEquals("image/jpeg", mimeType);
			if (mimeType != null & mimeType.length() > 0) {
				final String imageRawData = ((VorbisCommentTag) f.getTag()).getFirst(VorbisCommentFieldKey.COVERART);
				assertEquals(22972, imageRawData.length());
			}
		} catch (final Exception e) {
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

	/**
	 * Test can read file with base64 encoded image thats spans multiple ogg pages
	 * <p/>
	 * Fails:Doesnt give error but doesnt read image
	 */
	public void testReadFileWithLargeImageTag() {
		Exception exceptionCaught = null;
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg");
			final AudioFile f = AudioFileIO.read(testFile);
			final String mimeType = ((VorbisCommentTag) f.getTag()).getFirst(VorbisCommentFieldKey.COVERARTMIME);
			assertEquals("image/jpeg", mimeType);
			if (mimeType != null & mimeType.length() > 0) {
				final String imageRawData = ((VorbisCommentTag) f.getTag()).getFirst(VorbisCommentFieldKey.COVERART);
				assertEquals(1013576, imageRawData.length());
			}
		} catch (final Exception e) {
			e.printStackTrace();
			exceptionCaught = e;
		}
		assertNull(exceptionCaught);
	}

	/**
	 * Write and read image using lowest level methods
	 */
	public void testWriteImage1() {
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.ogg", Paths.get("testWriteImage1.ogg"));
			AudioFile f = AudioFileIO.read(testFile);
			VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

			// Add new image, requires two fields in oggVorbis with data in base64 encoded form
			final RandomAccessFile imageFile = new RandomAccessFile(AbstractTestCase.dataPath.resolve("coverart.png").toFile(), "r");
			final byte[] imagedata = new byte[(int) imageFile.length()];
			imageFile.read(imagedata);
			final char[] testdata = Base64Coder.encode(imagedata);
			final String base64image = new String(testdata);
			tag.setField(tag.createField(VorbisCommentFieldKey.COVERART, base64image));
			tag.setField(tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));
			f.commit();

			f = AudioFileIO.read(testFile);
			tag = (VorbisCommentTag) f.getTag();

			// VorbisImage base64 image, and reconstruct
			assertEquals(base64image, tag.getFirst(VorbisCommentFieldKey.COVERART));
			assertEquals("image/png", tag.getFirst(VorbisCommentFieldKey.COVERARTMIME));
			final BufferedImage bi = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(Base64Coder.decode(tag.getFirst(VorbisCommentFieldKey.COVERART).toCharArray()))));
			assertNotNull(bi);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write Image using new method, read using lowlevel
	 */
	public void testWriteImage2() {
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.ogg", Paths.get("testWriteImage2.ogg"));
			AudioFile f = AudioFileIO.read(testFile);
			VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

			// Add new image using purpose built method
			final RandomAccessFile imageFile = new RandomAccessFile(AbstractTestCase.dataPath.resolve("coverart.png").toFile(), "r");
			final byte[] imagedata = new byte[(int) imageFile.length()];
			imageFile.read(imagedata);

			tag.setArtworkField(imagedata, "image/png");
			f.commit();

			f = AudioFileIO.read(testFile);
			tag = (VorbisCommentTag) f.getTag();

			// VorbisImage base64 image, and reconstruct
			final char[] testdata = Base64Coder.encode(imagedata);
			final String base64image = new String(testdata);
			assertEquals(base64image, tag.getFirst(VorbisCommentFieldKey.COVERART));
			assertEquals("image/png", tag.getFirst(VorbisCommentFieldKey.COVERARTMIME));
			final BufferedImage bi = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(Base64Coder.decode(tag.getFirst(VorbisCommentFieldKey.COVERART).toCharArray()))));
			assertNotNull(bi);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write Image using lowlevel , read using new method
	 */
	public void testWriteImage3() {
		try {
			final Path testFile = AbstractTestCase.copyAudioToTmp("test.ogg", Paths.get("testWriteImage3.ogg"));
			AudioFile f = AudioFileIO.read(testFile);
			VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

			// Add new image, requires two fields in oggVorbis with data in base64 encoded form
			final RandomAccessFile imageFile = new RandomAccessFile(AbstractTestCase.dataPath.resolve("coverart.png").toFile(), "r");
			final byte[] imagedata = new byte[(int) imageFile.length()];
			imageFile.read(imagedata);
			final char[] testdata = Base64Coder.encode(imagedata);
			final String base64image = new String(testdata);
			tag.setField(tag.createField(VorbisCommentFieldKey.COVERART, base64image));
			tag.setField(tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));
			f.commit();

			f = AudioFileIO.read(testFile);
			tag = (VorbisCommentTag) f.getTag();

			// VorbisImage base64 image, and reconstruct
			assertEquals("image/png", tag.getArtworkMimeType());
			final byte[] newImageData = tag.getArtworkBinaryData();
			final BufferedImage bi = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(newImageData)));
			assertNotNull(bi);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
