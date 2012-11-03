package org.jaudiotagger.tag.mp4;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

public class ConcurrentWritesTest extends TestCase {

	private static final int THREADS = 100;
	private final Path[] files = new Path[THREADS];

	@Override
	public void setUp() {
		for (int counter = 0; counter < THREADS; counter++)
			files[counter] = AbstractTestCase.copyAudioToTmp("test5.m4a", Paths.get(ConcurrentWritesTest.class.getSimpleName() + "-" + counter + ".mp4"));
	}

	@Override
	public void tearDown() {
		for (final Path file : files)
			file.toFile().delete();
	}

	public void testConcurrentWrites() throws Exception {

		final ExecutorService executor = Executors.newCachedThreadPool();
		final List<Future<Boolean>> results = new ArrayList<Future<Boolean>>(files.length);
		for (final Path file : files)
			results.add(executor.submit(new WriteFileCallable(file)));

		for (final Future<Boolean> result : results)
			assertTrue(result.get());
	}

	private static class WriteFileCallable implements Callable<Boolean> {
		private final Path file;

		public WriteFileCallable(final Path file) {
			this.file = file;
		}

		@Override
		public Boolean call() throws Exception {
			AudioFile audiofile = AudioFileIO.read(file);
			audiofile.getTagOrCreateAndSetDefault().setField(FieldKey.CUSTOM1, file.getFileName().toString());
			audiofile.commit();
			audiofile = AudioFileIO.read(file);
			assertEquals(file.getFileName().toString(), audiofile.getTag().getFirst(FieldKey.CUSTOM1));
			return true;
		}
	}
}
