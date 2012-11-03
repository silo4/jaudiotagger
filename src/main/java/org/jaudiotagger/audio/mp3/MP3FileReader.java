package org.jaudiotagger.audio.mp3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * Read Mp3 Info (retrofitted to entagged ,done differently to entagged which is why some methods throw
 * RuntimeException) because done elsewhere
 */
public class MP3FileReader extends AudioFileReader {
	@Override
	protected GenericAudioHeader getEncodingInfo(final RandomAccessFile raf) throws CannotReadException, IOException {
		throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
	}

	@Override
	protected Tag getTag(final RandomAccessFile raf) throws CannotReadException, IOException {
		throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
	}

	/**
	 * @param f
	 * @return
	 */
	// Override because we read mp3s differently to the entagged code
	@Override
	public AudioFile read(final Path f) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		final MP3File mp3File = new MP3File(f, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG, true);
		return mp3File;
	}

	/**
	 * Read
	 * 
	 * @param f
	 * @return
	 * @throws ReadOnlyFileException
	 *             thrown if the file is not writable
	 * @throws org.jaudiotagger.tag.TagException
	 * @throws java.io.IOException
	 * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
	 */
	public AudioFile readMustBeWritable(final Path f) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		final MP3File mp3File = new MP3File(f, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG, false);
		return mp3File;
	}

}
