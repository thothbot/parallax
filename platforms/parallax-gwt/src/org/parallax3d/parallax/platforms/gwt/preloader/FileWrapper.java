/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.parallax3d.parallax.platforms.gwt.preloader;

import org.parallax3d.parallax.App;
import org.parallax3d.parallax.Files;
import org.parallax3d.parallax.Files.FileType;
import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.system.StreamUtils;

import java.io.*;

/** Used in PreloaderBundleGenerator to ease my pain. Since we emulate the original FileHandle, i have to make a copy...
 * @author mzechner
 * @author Nathan Sweet */
public class FileWrapper {
	protected File file;
	protected FileType type;

	protected FileWrapper () {
	}

	/** Creates a new absolute FileHandle for the file name. Use this for tools on the desktop that don't need any of the backends.
	 * Do not use this constructor in case you write something cross-platform. Use the {@link Files} interface instead.
	 * @param fileName the filename. */
	public FileWrapper (String fileName) {
		this.file = new File(fileName);
		this.type = FileType.Absolute;
	}

	protected FileWrapper (File file, FileType type) {
		this.file = file;
		this.type = type;
	}

	public String path () {
		return file.getPath();
	}

	public String name () {
		return file.getName();
	}

	public FileType type () {
		return type;
	}

	/** Returns a java.io.File that represents this file handle. Note the returned file will only be usable for
	 * {@link FileType#Absolute} and {@link FileType#External} file handles. */
	public File file () {
		if (type == FileType.External) return new File(App.files.getExternalStoragePath(), file.getPath());
		return file;
	}

	/** Returns a stream for reading this file as bytes.
	 * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public InputStream read () {
		if (type == FileType.Classpath || (type == FileType.Internal && !file.exists())
			|| (type == FileType.Local && !file.exists())) {
			InputStream input = FileWrapper.class.getResourceAsStream("/" + file.getPath().replace('\\', '/'));
			if (input == null) throw new ParallaxRuntimeException("File not found: " + file + " (" + type + ")");
			return input;
		}
		try {
			return new FileInputStream(file());
		} catch (Exception ex) {
			if (file().isDirectory())
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file + " (" + type + ")", ex);
			throw new ParallaxRuntimeException("Error reading file: " + file + " (" + type + ")", ex);
		}
	}

	/** Returns a stream for writing to this file. Parent directories will be created if necessary.
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throw GdxRuntimeException if this file handle represents a directory, if it is a {@link FileType#Classpath} or
	 *        {@link FileType#Internal} file, or if it could not be written. */
	public OutputStream write (boolean append) {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot write to a classpath file: " + file);
		if (type == FileType.Internal) throw new ParallaxRuntimeException("Cannot write to an internal file: " + file);
		parent().mkdirs();
		try {
			return new FileOutputStream(file(), append);
		} catch (Exception ex) {
			if (file().isDirectory())
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file + " (" + type + ")", ex);
			throw new ParallaxRuntimeException("Error writing file: " + file + " (" + type + ")", ex);
		}
	}

	/** Reads the remaining bytes from the specified stream and writes them to this file. The stream is closed. Parent directories
	 * will be created if necessary.
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throw GdxRuntimeException if this file handle represents a directory, if it is a {@link FileType#Classpath} or
	 *        {@link FileType#Internal} file, or if it could not be written. */
	public void write (InputStream input, boolean append) {
		OutputStream output = null;
		try {
			output = write(append);
			byte[] buffer = new byte[4096];
			while (true) {
				int length = input.read(buffer);
				if (length == -1) break;
				output.write(buffer, 0, length);
			}
		} catch (Exception ex) {
			throw new ParallaxRuntimeException("Error stream writing to file: " + file + " (" + type + ")", ex);
		} finally {
			try {
				if (input != null) input.close();
			} catch (Exception ignored) {
			}
			try {
				if (output != null) output.close();
			} catch (Exception ignored) {
			}
		}

	}

	/** Returns a writer for writing to this file. Parent directories will be created if necessary.
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @param charset May be null to use the default charset.
	 * @throw GdxRuntimeException if this file handle represents a directory, if it is a {@link FileType#Classpath} or
	 *        {@link FileType#Internal} file, or if it could not be written. */
	public Writer writer (boolean append, String charset) {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot write to a classpath file: " + file);
		if (type == FileType.Internal) throw new ParallaxRuntimeException("Cannot write to an internal file: " + file);
		parent().mkdirs();
		try {
			FileOutputStream output = new FileOutputStream(file(), append);
			if (charset == null)
				return new OutputStreamWriter(output);
			else
				return new OutputStreamWriter(output, charset);
		} catch (IOException ex) {
			if (file().isDirectory())
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file + " (" + type + ")", ex);
			throw new ParallaxRuntimeException("Error writing file: " + file + " (" + type + ")", ex);
		}
	}

	/** Writes the specified string to the file using the default charset. Parent directories will be created if necessary.
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throw GdxRuntimeException if this file handle represents a directory, if it is a {@link FileType#Classpath} or
	 *        {@link FileType#Internal} file, or if it could not be written. */
	public void writeString (String string, boolean append) {
		writeString(string, append, null);
	}

	/** Writes the specified string to the file as UTF-8. Parent directories will be created if necessary.
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @param charset May be null to use the default charset.
	 * @throw GdxRuntimeException if this file handle represents a directory, if it is a {@link FileType#Classpath} or
	 *        {@link FileType#Internal} file, or if it could not be written. */
	public void writeString (String string, boolean append, String charset) {
		Writer writer = null;
		try {
			writer = writer(append, charset);
			writer.write(string);
		} catch (Exception ex) {
			throw new ParallaxRuntimeException("Error writing file: " + file + " (" + type + ")", ex);
		} finally {
			StreamUtils.closeQuietly(writer);
		}
	}

	/** Returns the paths to the children of this directory. Returns an empty list if this file handle represents a file and not a
	 * directory. On the desktop, an {@link FileType#Internal} handle to a directory on the classpath will return a zero length
	 * array.
	 * @throw GdxRuntimeException if this file is an {@link FileType#Classpath} file. */
	public FileWrapper[] list () {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot list a classpath directory: " + file);
		String[] relativePaths = file().list();
		if (relativePaths == null) return new FileWrapper[0];
		FileWrapper[] handles = new FileWrapper[relativePaths.length];
		for (int i = 0, n = relativePaths.length; i < n; i++)
			handles[i] = child(relativePaths[i]);
		return handles;
	}

	/** Returns true if this file is a directory. Always returns false for classpath files. On Android, an {@link FileType#Internal}
	 * handle to an empty directory will return false. On the desktop, an {@link FileType#Internal} handle to a directory on the
	 * classpath will return false. */
	public boolean isDirectory () {
		if (type == FileType.Classpath) return false;
		return file().isDirectory();
	}

	/** Returns a handle to the child with the specified name.
	 * @throw GdxRuntimeException if this file handle is a {@link FileType#Classpath} or {@link FileType#Internal} and the child
	 *        doesn't exist. */
	public FileWrapper child (String name) {
		if (file.getPath().length() == 0) return new FileWrapper(new File(name), type);
		return new FileWrapper(new File(file, name), type);
	}

	public FileWrapper parent () {
		File parent = file.getParentFile();
		if (parent == null) {
			if (type == FileType.Absolute)
				parent = new File("/");
			else
				parent = new File("");
		}
		return new FileWrapper(parent, type);
	}

	/** @throw GdxRuntimeException if this file handle is a {@link FileType#Classpath} or {@link FileType#Internal} file. */
	public boolean mkdirs () {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot mkdirs with a classpath file: " + file);
		if (type == FileType.Internal) throw new ParallaxRuntimeException("Cannot mkdirs with an internal file: " + file);
		return file().mkdirs();
	}

	/** Returns true if the file exists. On Android, a {@link FileType#Classpath} or {@link FileType#Internal} handle to a directory
	 * will always return false. */
	public boolean exists () {
		switch (type) {
		case Internal:
			if (file.exists()) return true;
			// Fall through.
		case Classpath:
			return FileWrapper.class.getResource("/" + file.getPath().replace('\\', '/')) != null;
		}
		return file().exists();
	}

	/** Deletes this file or empty directory and returns success. Will not delete a directory that has children.
	 * @throw GdxRuntimeException if this file handle is a {@link FileType#Classpath} or {@link FileType#Internal} file. */
	public boolean delete () {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot delete a classpath file: " + file);
		if (type == FileType.Internal) throw new ParallaxRuntimeException("Cannot delete an internal file: " + file);
		return file().delete();
	}

	/** Deletes this file or directory and all children, recursively.
	 * @throw GdxRuntimeException if this file handle is a {@link FileType#Classpath} or {@link FileType#Internal} file. */
	public boolean deleteDirectory () {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot delete a classpath file: " + file);
		if (type == FileType.Internal) throw new ParallaxRuntimeException("Cannot delete an internal file: " + file);
		return deleteDirectory(file());
	}

	/** Copies this file or directory to the specified file or directory. If this handle is a file, then 1) if the destination is a
	 * file, it is overwritten, or 2) if the destination is a directory, this file is copied into it, or 3) if the destination
	 * doesn't exist, {@link #mkdirs()} is called on the destination's parent and this file is copied into it with a new name. If
	 * this handle is a directory, then 1) if the destination is a file, GdxRuntimeException is thrown, or 2) if the destination is
	 * a directory, this directory is copied into it recursively, overwriting existing files, or 3) if the destination doesn't
	 * exist, {@link #mkdirs()} is called on the destination and this directory is copied into it recursively.
	 * @throw GdxRuntimeException if the destination file handle is a {@link FileType#Classpath} or {@link FileType#Internal} file,
	 *        or copying failed. */
	public void copyTo (FileWrapper dest) {
		boolean sourceDir = isDirectory();
		if (!sourceDir) {
			if (dest.isDirectory()) dest = dest.child(name());
			copyFile(this, dest);
			return;
		}
		if (dest.exists()) {
			if (!dest.isDirectory()) throw new ParallaxRuntimeException("Destination exists but is not a directory: " + dest);
		} else {
			dest.mkdirs();
			if (!dest.isDirectory()) throw new ParallaxRuntimeException("Destination directory cannot be created: " + dest);
		}
		if (!sourceDir) dest = dest.child(name());
		copyDirectory(this, dest);
	}

	/** Moves this file to the specified file, overwriting the file if it already exists.
	 * @throw GdxRuntimeException if the source or destination file handle is a {@link FileType#Classpath} or
	 *        {@link FileType#Internal} file. */
	public void moveTo (FileWrapper dest) {
		if (type == FileType.Classpath) throw new ParallaxRuntimeException("Cannot move a classpath file: " + file);
		if (type == FileType.Internal) throw new ParallaxRuntimeException("Cannot move an internal file: " + file);
		copyTo(dest);
		delete();
	}

	/** Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
	 * determined. */
	public long length () {
		return file().length();
	}

	public String toString () {
		return file.getPath();
	}

	static private boolean deleteDirectory (File file) {
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0, n = files.length; i < n; i++) {
					if (files[i].isDirectory())
						deleteDirectory(files[i]);
					else
						files[i].delete();
				}
			}
		}
		return file.delete();
	}

	static private void copyFile (FileWrapper source, FileWrapper dest) {
		try {
			dest.write(source.read(), false);
		} catch (Exception ex) {
			throw new ParallaxRuntimeException("Error copying source file: " + source.file + " (" + source.type + ")\n" //
				+ "To destination: " + dest.file + " (" + dest.type + ")", ex);
		}
	}

	static private void copyDirectory (FileWrapper sourceDir, FileWrapper destDir) {
		destDir.mkdirs();
		FileWrapper[] files = sourceDir.list();
		for (int i = 0, n = files.length; i < n; i++) {
			FileWrapper srcFile = files[i];
			FileWrapper destFile = destDir.child(srcFile.name());
			if (srcFile.isDirectory())
				copyDirectory(srcFile, destFile);
			else
				copyFile(srcFile, destFile);
		}
	}
}
