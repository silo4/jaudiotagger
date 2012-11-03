/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.generic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.ModifyVetoException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;

/**
 * This abstract class is the skeleton for tag writers.
 * <p/>
 * <p/>
 * It handles the creation/closing of the randomaccessfile objects and then call the subclass method writeTag or
 * deleteTag. These two method have to be implemented in the subclass.
 * 
 * @author Raphael Slinckx
 * @version $Id: AudioFileWriter.java,v 1.21 2009/05/05 15:59:14 paultaylor Exp $
 * @since v0.02
 */
public abstract class AudioFileWriter {
	private static final String TEMP_FILENAME_SUFFIX = ".tmp";
	private static final String WRITE_MODE = "rws";
	private static final int MINIMUM_FILESIZE = 150;

	// Logger Object
	public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.generic");

	// If filename too long try recreating it with length no longer than 50 that
	// should be safe on all operating
	// systems
	private static final String FILE_NAME_TOO_LONG = "File name too long";
	private static final String FILE_NAME_TOO_LONG2 = "The filename, directory name, or volume label syntax is incorrect";
	private static final int FILE_NAME_TOO_LONG_SAFE_LIMIT = 50;

	/**
	 * If not <code>null</code>, this listener is used to notify the listener about modification events.<br>
	 */
	private AudioFileModificationListener modificationListener = null;

	/**
	 * Delete the tag (if any) present in the given file
	 * 
	 * @param af
	 *            The file to process
	 * @throws CannotWriteException
	 *             if anything went wrong
	 * @throws org.jaudiotagger.audio.exceptions.CannotReadException
	 */
	public void delete(final AudioFile af) throws CannotReadException, CannotWriteException {
		if (!Files.isWritable(af.getFile()))
			throw new CannotWriteException(ErrorMessage.GENERAL_DELETE_FAILED.getMsg(af.getFile()));

		if (af.getFile().toFile().length() <= MINIMUM_FILESIZE)
			throw new CannotWriteException(ErrorMessage.GENERAL_DELETE_FAILED.getMsg(af.getFile()));

		RandomAccessFile raf = null;
		RandomAccessFile rafTemp = null;
		Path tempF = null;

		// Will be set to true on VetoException, causing the finally block to
		// discard the tempfile.
		boolean revert = false;

		try {

			tempF = Files.createTempFile(af.getFile().getParent(), af.getFile().getFileName().toString().replace('.', '_'), TEMP_FILENAME_SUFFIX);

			rafTemp = new RandomAccessFile(tempF.toFile(), WRITE_MODE);
			raf = new RandomAccessFile(af.getFile().toFile(), WRITE_MODE);
			raf.seek(0);
			rafTemp.seek(0);

			try {
				if (this.modificationListener != null)
					this.modificationListener.fileWillBeModified(af, true);
				deleteTag(raf, rafTemp);
				if (this.modificationListener != null)
					this.modificationListener.fileModified(af, tempF);
			} catch (final ModifyVetoException veto) {
				throw new CannotWriteException(veto);
			}

		} catch (final Exception e) {
			revert = true;
			throw new CannotWriteException("\"" + af.getFile().toAbsolutePath() + "\" :" + e, e);
		} finally {
			// will be set to the remaining file.
			Path result = af.getFile();
			try {
				if (raf != null)
					raf.close();
				if (rafTemp != null)
					rafTemp.close();

				if (tempF.toFile().length() > 0 && !revert) {
					final boolean deleteResult = Files.exists(af.getFile());
					if (!deleteResult) {
						logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_ORIGINAL_FILE.getMsg(af.getFile(), tempF));
						throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_ORIGINAL_FILE.getMsg(af.getFile(), tempF));
					}
					try {
						Files.move(tempF, af.getFile());
					} catch (final IOException e) {
						logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(af.getFile(), tempF));
						throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(af.getFile(), tempF));
					}

					result = tempF;

					// If still exists we can now delete
					if (Files.exists(tempF))
						try {
							Files.delete(tempF);
						} catch (final IOException e) {
							// Non critical failed deletion
							logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(tempF));
						}
				} else
					// It was created but never used
					try {
						Files.delete(tempF);
					} catch (final IOException e) {
						// Non critical failed deletion
						logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(tempF));
					}
			} catch (final Exception ex) {
				logger.severe("AudioFileWriter exception cleaning up delete:" + af.getFile() + " or" + tempF.toAbsolutePath() + ":" + ex);
			}
			// Notify listener
			if (this.modificationListener != null)
				this.modificationListener.fileOperationFinished(result);
		}
	}

	/**
	 * Delete the tag (if any) present in the given randomaccessfile, and do not close it at the end.
	 * 
	 * @param raf
	 *            The source file, already opened in r-write mode
	 * @param tempRaf
	 *            The temporary file opened in r-write mode
	 * @throws CannotWriteException
	 *             if anything went wrong
	 * @throws org.jaudiotagger.audio.exceptions.CannotReadException
	 * @throws java.io.IOException
	 */
	public void delete(final RandomAccessFile raf, final RandomAccessFile tempRaf) throws CannotReadException, CannotWriteException, IOException {
		raf.seek(0);
		tempRaf.seek(0);
		deleteTag(raf, tempRaf);
	}

	/**
	 * Same as above, but delete tag in the file.
	 * 
	 * @param raf
	 * @param tempRaf
	 * @throws IOException
	 *             is thrown when the RandomAccessFile operations throw it (you should never throw them manually)
	 * @throws CannotWriteException
	 *             when an error occured during the deletion of the tag
	 * @throws org.jaudiotagger.audio.exceptions.CannotReadException
	 */
	protected abstract void deleteTag(RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotReadException, CannotWriteException, IOException;

	/**
	 * This method sets the {@link AudioFileModificationListener}.<br>
	 * There is only one listener allowed, if you want more instances to be supported, use the
	 * {@link ModificationHandler} to broadcast those events.<br>
	 * 
	 * @param listener
	 *            The listener. <code>null</code> allowed to deregister.
	 */
	public void setAudioFileModificationListener(final AudioFileModificationListener listener) {
		this.modificationListener = listener;
	}

	/**
	 * Prechecks before normal write
	 * <p/>
	 * <ul>
	 * <li>If the tag is actually empty, remove the tag</li>
	 * <li>if the file is not writable, throw exception
	 * <li>
	 * <li>If the file is too small to be a valid file, throw exception
	 * <li>
	 * </ul>
	 * 
	 * @param af
	 * @throws CannotWriteException
	 */
	private void precheckWrite(final AudioFile af) throws CannotWriteException {
		// Preliminary checks
		try {
			if (af.getTag().isEmpty()) {
				delete(af);
				return;
			}
		} catch (final CannotReadException re) {
			throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(af.getFile()));
		}

		if (!Files.isWritable(af.getFile())) {
			logger.severe(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(af.getFile()));
			throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(af.getFile()));
		}

		if (af.getFile().toFile().length() <= MINIMUM_FILESIZE) {
			logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(af.getFile()));
			throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(af.getFile()));
		}
	}

	/**
	 * Write the tag (if not empty) present in the AudioFile in the associated File
	 * 
	 * @param af
	 *            The file we want to process
	 * @throws CannotWriteException
	 *             if anything went wrong
	 */
	// TODO Creates temp file in same folder as the original file, this is safe
	// but would impose a performance overhead if the original file is on a
	// networked drive
	public void write(final AudioFile af) throws CannotWriteException {
		logger.config("Started writing tag data for file:" + af.getFile().getFileName());

		// Prechecks
		precheckWrite(af);

		// mp3's use a different mechanism to the other formats
		if (af instanceof MP3File) {
			af.commit();
			return;
		}

		RandomAccessFile raf = null;
		RandomAccessFile rafTemp = null;
		Path newFile;
		Path result;

		// Create temporary File
		try {
			newFile = Files.createTempFile(af.getFile().getParent(), af.getFile().getFileName().toString().replace('.', '_'), TEMP_FILENAME_SUFFIX);
		}
		// Unable to create temporary file, can happen in Vista if have Create
		// Files/Write Data set to Deny
		catch (final IOException ioe) {
			if (ioe.getMessage().equals(FILE_NAME_TOO_LONG) && (af.getFile().toFile().getName().length() > FILE_NAME_TOO_LONG_SAFE_LIMIT))
				try {

					newFile = Files.createTempFile(af.getFile().getParent(), af.getFile().toFile().getName().substring(0, FILE_NAME_TOO_LONG_SAFE_LIMIT).replace('.', '_'), TEMP_FILENAME_SUFFIX);

				} catch (final IOException ioe2) {
					logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(af.getFile().getFileName(), af.getFile().getParent().toAbsolutePath()), ioe2);
					throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(af.getFile().getFileName(), af.getFile().getParent().toAbsolutePath()));
				}
			else {
				logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(af.getFile().getFileName(), af.getFile().getParent().toAbsolutePath()), ioe);
				throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(af.getFile().getFileName(), af.getFile().getParent().toAbsolutePath()));
			}
		}

		// Open temporary file and actual file for editing
		try {
			rafTemp = new RandomAccessFile(newFile.toFile(), WRITE_MODE);
			raf = new RandomAccessFile(af.getFile().toFile(), WRITE_MODE);

		}
		// Unable to write to writable file, can happen in Vista if have Create
		// Folders/Append Data set to Deny
		catch (final IOException ioe) {
			logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(af.getFile().toAbsolutePath()), ioe);

			// If we managed to open either file, delete it.
			try {
				if (raf != null)
					raf.close();
				if (rafTemp != null)
					rafTemp.close();
			} catch (final IOException ioe2) {
				// Warn but assume has worked okay
				logger.log(Level.WARNING, ErrorMessage.GENERAL_WRITE_PROBLEM_CLOSING_FILE_HANDLE.getMsg(af.getFile(), ioe.getMessage()), ioe2);
			}

			// Delete the temp file ( we cannot delete until closed
			// corresponding
			// rafTemp)
			try {
				Files.delete(newFile);
			} catch (final IOException e) {
				// Non critical failed deletion
				logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(newFile.toAbsolutePath()));
			}

			throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(af.getFile().toAbsolutePath()));
		}

		// Write data to File
		try {

			raf.seek(0);
			rafTemp.seek(0);
			try {
				if (this.modificationListener != null)
					this.modificationListener.fileWillBeModified(af, false);
				writeTag(af.getTag(), raf, rafTemp);
				if (this.modificationListener != null)
					this.modificationListener.fileModified(af, newFile);
			} catch (final ModifyVetoException veto) {
				throw new CannotWriteException(veto);
			}
		} catch (final Exception e) {
			logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(af.getFile(), e.getMessage()), e);

			try {
				if (raf != null)
					raf.close();
				if (rafTemp != null)
					rafTemp.close();
			} catch (final IOException ioe) {
				// Warn but assume has worked okay
				logger.log(Level.WARNING, ErrorMessage.GENERAL_WRITE_PROBLEM_CLOSING_FILE_HANDLE.getMsg(af.getFile().toAbsolutePath(), ioe.getMessage()), ioe);
			}

			// Delete the temporary file because either it was never used so
			// lets just tidy up or we did start writing to it but
			// the write failed and we havent renamed it back to the original
			// file so we can just delete it.
			try {
				Files.delete(newFile);
			} catch (final IOException ex) {
				// Non critical failed deletion
				logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(newFile.toAbsolutePath()));
			}
			throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(af.getFile(), e.getMessage()));
		} finally {
			try {
				if (raf != null)
					raf.close();
				if (rafTemp != null)
					rafTemp.close();
			} catch (final IOException ioe) {
				// Warn but assume has worked okay
				logger.log(Level.WARNING, ErrorMessage.GENERAL_WRITE_PROBLEM_CLOSING_FILE_HANDLE.getMsg(af.getFile().toAbsolutePath(), ioe.getMessage()), ioe);
			}
		}

		// Result held in this file
		result = af.getFile();

		// If the temporary file was used
		if (newFile.toFile().length() > 0) {

			// Rename Original File
			// Can fail on Vista if have Special Permission 'Delete' set Deny
			Path originalFileBackup = af.getFile().toAbsolutePath().getParent().resolve(AudioFile.getBaseFilename(af.getFile()) + ".old");

			// If already exists modify the suffix
			int count = 1;
			while (Files.exists(originalFileBackup)) {
				originalFileBackup = af.getFile().toAbsolutePath().getParent().resolve(AudioFile.getBaseFilename(af.getFile()) + ".old" + count);
				count++;
			}

			boolean renameResult = Utils.rename(af.getFile(), originalFileBackup);
			if (!renameResult) {
				logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP.getMsg(af.getFile().toAbsolutePath(), originalFileBackup.getFileName()));
				// Delete the temp file because write has failed
				if (newFile != null)
					try {
						Files.delete(newFile);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP.getMsg(af.getFile(), originalFileBackup.getFileName()));
			}

			// Rename Temp File to Original File
			renameResult = Utils.rename(newFile, af.getFile());
			if (!renameResult) {
				// Renamed failed so lets do some checks rename the backup back
				// to the original file
				// New File doesnt exist
				if (!Files.exists(newFile))
					logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_NEW_FILE_DOESNT_EXIST.getMsg(newFile.toAbsolutePath()));

				// Rename the backup back to the original
				if (!originalFileBackup.toFile().renameTo(af.getFile().toFile()))
					// TODO now if this happens we are left with testfile.old
					// instead of testfile.mp4
					logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_BACKUP_TO_ORIGINAL.getMsg(originalFileBackup.toAbsolutePath(), af.getFile().getFileName()));

				logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(af.getFile().toAbsolutePath(), newFile.getFileName()));
				throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(af.getFile().toAbsolutePath(), newFile.getFileName()));
			} else {
				// Rename was okay so we can now delete the backup of the
				// original
				final boolean deleteResult = originalFileBackup.toFile().delete();
				if (!deleteResult)
					// Not a disaster but can't delete the backup so make a
					// warning
					logger.warning(ErrorMessage.GENERAL_WRITE_WARNING_UNABLE_TO_DELETE_BACKUP_FILE.getMsg(originalFileBackup.toAbsolutePath()));
			}

			// Delete the temporary file if still exists
			if (Files.exists(newFile))
				if (!newFile.toFile().delete())
					// Non critical failed deletion
					logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(newFile));
		} else // Delete the temporary file that wasn't ever used
		if (!newFile.toFile().delete())
			// Non critical failed deletion
			logger.warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE.getMsg(newFile));

		if (this.modificationListener != null)
			this.modificationListener.fileOperationFinished(result);
	}

	/**
	 * This is called when a tag has to be written in a file. Three parameters are provided, the tag to write (not
	 * empty) Two randomaccessfiles, the first points to the file where we want to write the given tag, and the second
	 * is an empty temporary file that can be used if e.g. the file has to be bigger than the original.
	 * <p/>
	 * If something has been written in the temporary file, when this method returns, the original file is deleted, and
	 * the temporary file is renamed the the original name
	 * <p/>
	 * If nothing has been written to it, it is simply deleted.
	 * <p/>
	 * This method can assume the raf, rafTemp are pointing to the first byte of the file. The subclass must not close
	 * these two files when the method returns.
	 * 
	 * @param tag
	 * @param raf
	 * @param rafTemp
	 * @throws IOException
	 *             is thrown when the RandomAccessFile operations throw it (you should never throw them manually)
	 * @throws CannotWriteException
	 *             when an error occured during the generation of the tag
	 * @throws org.jaudiotagger.audio.exceptions.CannotReadException
	 */
	protected abstract void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException;
}
