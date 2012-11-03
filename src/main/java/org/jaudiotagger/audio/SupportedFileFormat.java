package org.jaudiotagger.audio;

import java.util.ArrayList;

import org.jaudiotagger.audio.aiff.AiffTag;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.audio.real.RealTag;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

/**
 * Files formats currently supported by Library. Each enum value is associated with a file suffix (extension).
 */
public enum SupportedFileFormat {
	OGG("ogg") {
		@Override
		public Tag getDefaultTag() {
			return new Mp4Tag();
		}
	},
	MP3("mp3") {
		@Override
		public Tag getDefaultTag() {
			throw new RuntimeException("Unable to create default tag for this file format");
		}
	},
	FLAC("flac") {
		@Override
		public Tag getDefaultTag() {
			return new FlacTag(VorbisCommentTag.createNewTag(), new ArrayList<MetadataBlockDataPicture>());
		}
	},
	MP4("mp4") {
		@Override
		public Tag getDefaultTag() {
			return new Mp4Tag();
		}
	},
	M4A("m4a") {
		@Override
		public Tag getDefaultTag() {
			return new Mp4Tag();
		}
	},
	M4P("m4p") {
		@Override
		public Tag getDefaultTag() {
			return new Mp4Tag();
		}
	},
	WMA("wma") {
		@Override
		public Tag getDefaultTag() {
			return new AsfTag();
		}
	},
	WAV("wav") {
		@Override
		public Tag getDefaultTag() {
			return new WavTag();
		}
	},
	RA("ra") {
		@Override
		public Tag getDefaultTag() {
			return new RealTag();
		}
	},
	RM("rm") {
		@Override
		public Tag getDefaultTag() {
			return new RealTag();
		}
	},
	M4B("m4b") {
		@Override
		public Tag getDefaultTag() {
			throw new RuntimeException("Unable to create default tag for this file format");
		}
	},
	AIF("aif") {
		@Override
		public Tag getDefaultTag() {
			return new AiffTag();
		}
	};

	private String filesuffix;

	/**
	 * Constructor for internal use by this enum.
	 */
	SupportedFileFormat(final String filesuffix) {
		this.filesuffix = filesuffix;
	}

	/**
	 * Returns the file suffix (lower case without initial .) associated with the format.
	 */
	public String getFilesuffix() {
		return filesuffix;
	}

	public abstract Tag getDefaultTag();
}
