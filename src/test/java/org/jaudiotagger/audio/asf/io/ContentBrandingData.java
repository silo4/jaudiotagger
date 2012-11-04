/**
 * 
 */
package org.jaudiotagger.audio.asf.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.jaudiotagger.audio.asf.data.ContentBranding;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.MetadataContainerUtils;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * @author Christian Laireiter
 * 
 */
public class ContentBrandingData extends TestCase {

	public void testContentBrandingWriteRead() throws IOException {
		final ContentBranding cb = new ContentBranding();
		cb.setCopyRightURL("CP URL");
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		cb.writeInto(bos);
		assertEquals(cb.getCurrentAsfChunkSize(), bos.toByteArray().length);
		final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		assertEquals(GUID.GUID_CONTENT_BRANDING, Utils.readGUID(bis));
		final ContentBranding read = (ContentBranding) new ContentBrandingReader().read(GUID.GUID_CONTENT_BRANDING, bis, 0);
		MetadataContainerUtils.equals(cb, read);
	}

}