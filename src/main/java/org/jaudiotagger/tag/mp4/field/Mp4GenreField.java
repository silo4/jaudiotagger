package org.jaudiotagger.tag.mp4.field;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import org.jaudiotagger.tag.reference.GenreTypes;

/**
 * Represents the Genre field , when user has selected from the set list of genres
 * <p/>
 * <p>
 * This class allows you to retrieve either the internal genreid, or the display value
 */
public class Mp4GenreField extends Mp4TagTextNumberField {
	public Mp4GenreField(final String id, final ByteBuffer data) throws UnsupportedEncodingException {
		super(id, data);
	}

	/**
	 * Precheck to see if the value is a valid genre or whether you should use a custom genre.
	 * 
	 * @param genreId
	 * @return
	 */
	public static boolean isValidGenre(final String genreId) {
		// Is it an id (within old id3 range)
		try {
			final short genreVal = Short.parseShort(genreId);
			if ((genreVal - 1) <= GenreTypes.getMaxStandardGenreId())
				return true;
		} catch (final NumberFormatException nfe) {
			// Do Nothing test as String instead
		}

		// Is it the String value ?
		final Integer id3GenreId = GenreTypes.getInstanceOf().getIdForValue(genreId);
		if (id3GenreId != null)
			if (id3GenreId <= GenreTypes.getMaxStandardGenreId())
				return true;
		return false;
	}

	/**
	 * Construct genre, if cant find match just default to first genre
	 * 
	 * @param genreId
	 *            key into ID3v1 list (offset by one) or String value in ID3list
	 */
	public Mp4GenreField(final String genreId) {
		super(Mp4FieldKey.GENRE.getFieldName(), genreId);

		// Is it an id
		try {
			final short genreVal = Short.parseShort(genreId);
			if ((genreVal - 1) <= GenreTypes.getMaxStandardGenreId()) {
				numbers = new ArrayList<Short>();
				numbers.add(genreVal);
				return;
			}
			// Default
			numbers = new ArrayList<Short>();
			numbers.add((short) (1));
			return;
		} catch (final NumberFormatException nfe) {
			// Do Nothing test as String instead
		}

		// Is it the String value ?
		final Integer id3GenreId = GenreTypes.getInstanceOf().getIdForValue(genreId);
		if (id3GenreId != null)
			if (id3GenreId <= GenreTypes.getMaxStandardGenreId()) {
				numbers = new ArrayList<Short>();
				numbers.add((short) (id3GenreId + 1));
				return;
			}
		numbers = new ArrayList<Short>();
		numbers.add((short) (1));
	}

	@Override
	protected void build(final ByteBuffer data) throws UnsupportedEncodingException {
		// Data actually contains a 'Data' Box so process data using this
		final Mp4BoxHeader header = new Mp4BoxHeader(data);
		final Mp4DataBox databox = new Mp4DataBox(header, data);
		dataSize = header.getDataLength();
		numbers = databox.getNumbers();

		if (numbers.size() > 0) {
			final int genreId = numbers.get(0);
			// Get value, we have to adjust index by one because iTunes labels from one instead of zero
			content = GenreTypes.getInstanceOf().getValueForId(genreId - 1);
			// Some apps set genre to invalid value, we dont disguise this by setting content to empty string we leave
			// as null so apps can handle if they wish, but we do display a warning to make them aware.
			if (content == null)
				logger.warning(ErrorMessage.MP4_GENRE_OUT_OF_RANGE.getMsg(genreId));
		} else
			logger.warning(ErrorMessage.MP4_NO_GENREID_FOR_GENRE.getMsg(header.getDataLength()));
	}
}
