package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class AiffFile extends AudioFile {

	/**
	 * A static DateFormat object for generating ISO date strings
	 */
	public final static SimpleDateFormat ISO_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	/**
	 * Creates a new empty AiffFile that is not associated with a specific file.
	 */
	public AiffFile() {

	}

	/**
	 * Creates a new MP3File datatype and parse the tag from the given filename.
	 * 
	 * @param filename
	 *            AIFF file
	 * @throws IOException
	 *             on any I/O error
	 * @throws TagException
	 *             on any exception generated by this library.
	 * @throws org.jaudiotagger.audio.exceptions.ReadOnlyFileException
	 * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
	 */
	public AiffFile(final String filename) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		this(Paths.get(filename));
	}

	/**
	 * Creates a new MP3File datatype and parse the tag from the given file Object.
	 * 
	 * @param file
	 *            MP3 file
	 * @throws IOException
	 *             on any I/O error
	 * @throws TagException
	 *             on any exception generated by this library.
	 * @throws org.jaudiotagger.audio.exceptions.ReadOnlyFileException
	 * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
	 */
	public AiffFile(final Path file) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		this(file, true);
	}

	public AiffFile(final Path file, final boolean readOnly) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		RandomAccessFile newFile = null;
		try {
			logger.setLevel(Level.FINEST);
			logger.fine("Called AiffFile constructor on " + file.toAbsolutePath());
			this.file = file;

			// Check File accessibility
			newFile = checkFilePermissions(file, readOnly);
			audioHeader = new AiffAudioHeader();
			// readTag();

		} finally {
			if (newFile != null)
				newFile.close();
		}
	}

	public AiffAudioHeader getAiffAudioHeader() {
		return (AiffAudioHeader) audioHeader;
	}

}
