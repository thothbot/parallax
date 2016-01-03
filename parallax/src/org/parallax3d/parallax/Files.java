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

/** Provides standard access to the filesystem, classpath, Android SD card, and Android assets directory.
 * @author mzechner
 * @author Nathan Sweet */
public interface Files {
	/** Indicates how to resolve a path to a file.
	 * @author mzechner
	 * @author Nathan Sweet */
	public enum FileType {
		/** Path relative to the root of the classpath. Classpath files are always readonly. Note that classpath files are not
		 * compatible with some functionality on Android
		 */
		Classpath,

		/** Path relative to the asset directory on Android and to the application's root directory on the desktop. On the desktop,
		 * if the file is not found, then the classpath is checked. This enables files to be found when using JWS or applets.
		 * Internal files are always readonly. */
		Internal,

		/** Path relative to the root of the SD card on Android and to the home directory of the current user on the desktop. */
		External,

		/** Path that is a fully qualified, absolute filesystem path. To ensure portability across platforms use absolute files only
		 * when absolutely (heh) necessary. */
		Absolute,

		/** Path relative to the private files directory on Android and to the application's root directory on the desktop. */
		Local, FileType;
	}

	/** Returns a handle representing a file or directory.
	 * @param type Determines how the path is resolved.
	 * @see FileType */
	public FileHandle getFileHandle(String path, FileType type);

	/** Convenience method that returns a {@link FileType#Classpath} file handle. */
	public FileHandle classpath(String path);

	/** Convenience method that returns a {@link FileType#Internal} file handle. */
	public FileHandle internal(String path);

	/** Convenience method that returns a {@link FileType#External} file handle. */
	public FileHandle external(String path);

	/** Convenience method that returns a {@link FileType#Absolute} file handle. */
	public FileHandle absolute(String path);

	/** Convenience method that returns a {@link FileType#Local} file handle. */
	public FileHandle local(String path);

	/** Returns the external storage path directory. This is the SD card on Android and the home directory of the current user on
	 * the desktop. */
	public String getExternalStoragePath();

	/** Returns true if the external storage is ready for file IO. Eg, on Android, the SD card is not available when mounted for use
	 * with a PC. */
	public boolean isExternalStorageAvailable();

	/** Returns the local storage path directory. This is the private files directory on Android and the directory of the jar on the
	 * desktop. */
	public String getLocalStoragePath();

	/** Returns true if the local storage is ready for file IO. */
	public boolean isLocalStorageAvailable();
}
