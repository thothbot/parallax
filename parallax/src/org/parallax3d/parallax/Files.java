/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 *
 * This file is part of Parallax project.
 *
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 *
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 *
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax;

import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.files.FileType;

/** Provides standard access to the filesystem, classpath, Android SD card, and Android assets directory.
 * @author mzechner
 * @author Nathan Sweet */
public interface Files {

	/** Returns a handle representing a file or directory.
	 * @param type Determines how the path is resolved.
	 * @see FileType */
	FileHandle getFileHandle(String path, FileType type);

	FileHandle getFileHandle(String path, FileType type, FileListener<?> listener);

	/** Convenience method that returns a {@link FileType#Classpath} file handle. */
	FileHandle classpath(String path);

	FileHandle classpath(String path, FileListener<?> listener);

	/** Convenience method that returns a {@link FileType#Internal} file handle. */
	FileHandle internal(String path);

	FileHandle internal(String path, FileListener<?> listener);

	/** Convenience method that returns a {@link FileType#External} file handle. */
	FileHandle external(String path);

	FileHandle external(String path, FileListener<?> listener);

	/** Convenience method that returns a {@link FileType#Absolute} file handle. */
	FileHandle absolute(String path);

	FileHandle absolute(String path, FileListener<?> listener);

	/** Convenience method that returns a {@link FileType#Local} file handle. */
	FileHandle local(String path);

	FileHandle local(String path, FileListener<?> listener);

	/** Returns the external storage path directory. This is the SD card on Android and the home directory of the current user on
	 * the desktop. */
	String getExternalStoragePath();

	/** Returns true if the external storage is ready for file IO. Eg, on Android, the SD card is not available when mounted for use
	 * with a PC. */
	boolean isExternalStorageAvailable();

	/** Returns the local storage path directory. This is the private files directory on Android and the directory of the jar on the
	 * desktop. */
	String getLocalStoragePath();

	/** Returns true if the local storage is ready for file IO. */
	boolean isLocalStorageAvailable();
}
