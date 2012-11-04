package org.jaudiotagger.issues;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Test hasField() methods
 */
public class NetworkTest extends AbstractTestCase {
	private static AtomicInteger count = new AtomicInteger(0);

	private void loadFiles(final Path dir) throws Exception {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir, new AudioFileFilter())) {
			final Iterator<Path> files = dirStream.iterator();
			while (files.hasNext()) {
				final Path file = files.next();
				if (Files.isDirectory(file))
					loadFiles(file);
				else {
					System.out.println(new Date() + ":Start File:" + file);
					AudioFileIO.read(file);
					// FileChannel fc = new FileInputStream(file).getChannel();
					// ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,0,500000);
					System.out.println(new Date() + ":End File:" + file);
					count.incrementAndGet();
				}
			}

		}
	}

	public void ignoreNetworkSpeed() throws Exception {
		Exception caught = null;
		try {
			System.out.println("Start:" + new Date());
			final Path file = Paths.get("Z:\\Music\\Replay Music Recordings");
			// File file = new File("C:\\Users\\MESH\\Music\\Replay Music Recordings");
			loadFiles(file);
			System.out.println("Loaded:" + count.get());
			System.out.println("End:" + new Date());

		} catch (final Exception e) {
			caught = e;
			e.printStackTrace();
		}
		assertNull(caught);
	}

	/*
	 * public void testDataCopySpeed() throws Exception { File file = new
	 * File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");
	 * 
	 * System.out.println("start:"+new Date()); FileChannel fc = new FileInputStream(file).getChannel(); ByteBuffer bb =
	 * fc.map(FileChannel.MapMode.READ_ONLY,0,500000); fc.close(); System.out.println("end:"+new Date());
	 * 
	 * }
	 * 
	 * public void testDataCopySpeed2() throws Exception { File file = new
	 * File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");
	 * 
	 * System.out.println("start:"+new Date()); FileChannel fc = new FileInputStream(file).getChannel(); ByteBuffer bb =
	 * ByteBuffer.allocate(500000); fc.read(bb); fc.close(); System.out.println("end:"+new Date());
	 * 
	 * }
	 */

	/*
	 * public void testDataCopyBufferedStream() throws Exception {
	 * 
	 * File file = new
	 * File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3"); Date
	 * start = new Date();
	 * 
	 * FileChannel fc = new FileInputStream(file).getChannel(); ByteBuffer bb = ByteBuffer.allocate(500000);
	 * WritableByteChannel wbc = bb. //fc.read(bb); //fc.close(); Date end = new Date();
	 * System.out.println(end.getTime() - start.getTime()); )
	 */
}
