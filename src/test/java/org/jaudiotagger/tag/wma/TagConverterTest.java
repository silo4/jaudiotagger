package org.jaudiotagger.tag.wma;

import java.io.IOException;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContainerType;
import org.jaudiotagger.audio.asf.data.MetadataContainer;
import org.jaudiotagger.audio.asf.data.MetadataContainerUtils;
import org.jaudiotagger.audio.asf.io.AsfHeaderReader;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.tag.asf.AsfTag;

/**
 * @author Christian Laireiter
 * 
 */
public class TagConverterTest extends WmaTestCase {

	public final static String TEST_FILE = "test6.wma";

	/**
	 * @param name
	 */
	public TagConverterTest(final String name) {
		super(TEST_FILE, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test method for
	 * {@link org.jaudiotagger.audio.asf.util.TagConverter#distributeMetadata(org.jaudiotagger.tag.asf.AsfTag)} .
	 */
	public void testDistributeMetadata() throws IOException {
		final AsfHeader header = AsfHeaderReader.readHeader(prepareTestFile(null));
		final MetadataContainer contentDesc = header.findMetadataContainer(ContainerType.CONTENT_DESCRIPTION);
		assertNotNull(contentDesc);
		final MetadataContainer extContentDesc = header.findMetadataContainer(ContainerType.EXTENDED_CONTENT);
		assertNotNull(extContentDesc);
		final AsfTag createTagOf = TagConverter.createTagOf(header);
		final MetadataContainer[] distributeMetadata = TagConverter.distributeMetadata(createTagOf);
		assertTrue(MetadataContainerUtils.equals(contentDesc, distributeMetadata[0]));
		assertTrue(MetadataContainerUtils.equals(extContentDesc, distributeMetadata[2]));
	}

}
