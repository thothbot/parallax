/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Base on libgdx FileHandle.java file
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

package org.parallax3d.parallax.files;

import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.system.StreamUtils;

import java.io.*;

/** Represents a file or directory on the filesystem, classpath, Android SD card, or Android assets directory.
 * 
 * Because some of the file types are backed by composite files and may be compressed (for example, if they are in an Android .apk
 * or are found via the classpath), the methods for extracting a {@link #path()} or {@link #file()} may not be appropriate for all
 * types. Use the Reader or Stream methods here to hide these dependencies from your platforms independent code.
 * 
 * @author mzechner
 * @author Nathan Sweet */
public class FileHandle {
	private static final String ERROR_READING_FILE = "Error reading file: ";
	protected File file;

	protected FileHandle () {
	}

	/** Creates a new absolute FileHandle for the file name. Use this for tools on the desktop that don't need any of the platforms.
	 * Do not use this constructor in case you write something cross-platforms.
	 * @param fileName the filename. */
	public FileHandle (String fileName) {
		this(new File(fileName));
	}

	protected FileHandle (File file) {
		this.file = file;
	}

	/** @return the path of the file as specified on construction, e.g. Gdx.files.internal("dir/file.png") -> dir/file.png. backward
	 *         slashes will be replaced by forward slashes. */
	public String path () {
		return file.getPath().replace('\\', '/');
	}

	/** @return the name of the file, without any parent paths. */
	public String name () {
		return file.getName();
	}

	public String extension () {
		String name = file.getName();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1) {
			return "";
		}
		return name.substring(dotIndex + 1);
	}

	/** @return the name of the file, without parent paths or the extension. */
	public String nameWithoutExtension () {
		String name = file.getName();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1) {
			return name;
		}
		return name.substring(0, dotIndex);
	}

	/** @return the path and filename without the extension, e.g. dir/dir2/file.png -> dir/dir2/file. backward slashes will be
	 *         returned as forward slashes. */
	public String pathWithoutExtension () {
		String path = file.getPath().replace('\\', '/');
		int dotIndex = path.lastIndexOf('.');
		if (dotIndex == -1) {
			return path;
		}
		return path.substring(0, dotIndex);
	}

	public File file () {
		return file;
	}

	public boolean isText() {
		boolean result = false;

		FileReader inputStream = null;

		try {
			inputStream = new FileReader(file);

			int c;
			while ((c = inputStream.read()) != -1) {

				Character.UnicodeBlock block = Character.UnicodeBlock.of(c);

				if (block == Character.UnicodeBlock.BASIC_LATIN || block == Character.UnicodeBlock.GREEK) {
					// (9)Horizontal Tab (10)Line feed  (11)Vertical tab (13)Carriage return (32)Space (126)tilde
					if (c==9 || c == 10 || c == 11 || c == 13 || (c >= 32 && c <= 126)) {
						result = true;

						// (153)Superscript two (160)ϊ  (255) No break space
					} else if (c == 153 || c >= 160 && c <= 255) {
						result = true;

					} else {
						result = false;
						break;
					}
				}
			}
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
				}
			}
		}
		return result;
	}

	/** Returns a stream for reading this file as bytes.*/
	public InputStream read () {
		try {
			return new FileInputStream(file());
		} catch (Exception ex) {
			if (file().isDirectory()) {
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file , ex);
			}	
			throw new ParallaxRuntimeException(ERROR_READING_FILE + file, ex);
		}
	}

	/** Returns a buffered stream for reading this file as bytes.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public BufferedInputStream read (int bufferSize) {
		return new BufferedInputStream(read(), bufferSize);
	}

	/** Returns a reader for reading this file as characters.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public Reader reader () {
		return new InputStreamReader(read());
	}

	/** Returns a reader for reading this file as characters.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public Reader reader (String charset) {
		InputStream stream = read();
		try {
			return new InputStreamReader(stream, charset);
		} catch (UnsupportedEncodingException ex) {
			StreamUtils.closeQuietly(stream);
			throw new ParallaxRuntimeException(ERROR_READING_FILE + this, ex);
		}
	}

	/** Returns a buffered reader for reading this file as characters.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public BufferedReader reader (int bufferSize) {
		return new BufferedReader(new InputStreamReader(read()), bufferSize);
	}

	/** Returns a buffered reader for reading this file as characters.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public BufferedReader reader (int bufferSize, String charset) {
		try {
			return new BufferedReader(new InputStreamReader(read(), charset), bufferSize);
		} catch (UnsupportedEncodingException ex) {
			throw new ParallaxRuntimeException(ERROR_READING_FILE + this, ex);
		}
	}

	/** Reads the entire file into a string using the platforms's default charset.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public String readString () {
		return readString(null);
	}

	/** Reads the entire file into a string using the specified charset.
	 * @param charset If null the default charset is used.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public String readString (String charset) {
		StringBuilder output = new StringBuilder(estimateLength());
		InputStreamReader reader = null;
		try {
			if (charset == null)
				reader = new InputStreamReader(read());
			else
				reader = new InputStreamReader(read(), charset);
			char[] buffer = new char[256];
			while (true) {
				int length = reader.read(buffer);
				if (length == -1) {
					break;
				}
				output.append(buffer, 0, length);
			}
		} catch (IOException ex) {
			throw new ParallaxRuntimeException("Error reading layout file: " + this, ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
		return output.toString();
	}

	/** Reads the entire file into a byte array.
	 * @throws ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read. */
	public byte[] readBytes () {
		InputStream input = read();
		try {
			return StreamUtils.copyStreamToByteArray(input, estimateLength());
		} catch (IOException ex) {
			throw new ParallaxRuntimeException(ERROR_READING_FILE + this, ex);
		} finally {
			StreamUtils.closeQuietly(input);
		}
	}

	private int estimateLength () {
		int length = (int)length();
		return length != 0 ? length : 512;
	}

	/** Reads the entire file into the byte array. The byte array must be big enough to hold the file's data.
	 * @param bytes the array to load the file into
	 * @param offset the offset to start writing bytes
	 * @param size the number of bytes to read, see {@link #length()}
	 * @return the number of read bytes */
	public int readBytes (byte[] bytes, int offset, int size) {
		InputStream input = read();
		int position = 0;
		try {
			while (true) {
				int count = input.read(bytes, offset + position, size - position);
				if (count <= 0) {
					break;
				}
				position += count;
			}
		} catch (IOException ex) {
			throw new ParallaxRuntimeException(ERROR_READING_FILE + this, ex);
		} finally {
			StreamUtils.closeQuietly(input);
		}
		return position - offset;
	}

	public FileHandle[] list () {
		String[] relativePaths = file().list();
		if (relativePaths == null) {
			return new FileHandle[0];
		}
		FileHandle[] handles = new FileHandle[relativePaths.length];
		for (int i = 0, n = relativePaths.length; i < n; i++)
			handles[i] = child(relativePaths[i]);
		return handles;
	}

	public FileHandle[] list (FileFilter filter) {
		File file = file();
		String[] relativePaths = file.list();
		if (relativePaths == null) {
			return new FileHandle[0];
		}
		FileHandle[] handles = new FileHandle[relativePaths.length];
		int count = 0;
		for (int i = 0, n = relativePaths.length; i < n; i++) {
			String path = relativePaths[i];
			FileHandle child = child(path);
			if (!filter.accept(child.file())) {
				continue;
			}
			handles[count] = child;
			count++;
		}
		if (count < relativePaths.length) {
			FileHandle[] newHandles = new FileHandle[count];
			System.arraycopy(handles, 0, newHandles, 0, count);
			handles = newHandles;
		}
		return handles;
	}

	public FileHandle[] list (FilenameFilter filter) {
		File file = file();
		String[] relativePaths = file.list();
		if (relativePaths == null) {
			return new FileHandle[0];
		}
		FileHandle[] handles = new FileHandle[relativePaths.length];
		int count = 0;
		for (int i = 0, n = relativePaths.length; i < n; i++) {
			String path = relativePaths[i];
			if (!filter.accept(file, path)) {
				continue;
			}
			handles[count] = child(path);
			count++;
		}
		if (count < relativePaths.length) {
			FileHandle[] newHandles = new FileHandle[count];
			System.arraycopy(handles, 0, newHandles, 0, count);
			handles = newHandles;
		}
		return handles;
	}

	public FileHandle[] list (String suffix) {
		String[] relativePaths = file().list();
		if (relativePaths == null) {
			return new FileHandle[0];
		}
		FileHandle[] handles = new FileHandle[relativePaths.length];
		int count = 0;
		for (int i = 0, n = relativePaths.length; i < n; i++) {
			String path = relativePaths[i];
			if (!path.endsWith(suffix)) {
				continue;
			}
			handles[count] = child(path);
			count++;
		}
		if (count < relativePaths.length) {
			FileHandle[] newHandles = new FileHandle[count];
			System.arraycopy(handles, 0, newHandles, 0, count);
			handles = newHandles;
		}
		return handles;
	}

	public boolean isDirectory () {
		return file().isDirectory();
	}

	/** Returns a handle to the child with the specified name. */
	public FileHandle child (String name) {
		if (file.getPath().length() == 0) {
			return new FileHandle(new File(name));
		}
		return new FileHandle(new File(file, name));
	}

	/** Returns a handle to the sibling with the specified name.
	 * @throws ParallaxRuntimeException if this file is the root. */
	public FileHandle sibling (String name) {
		if (file.getPath().length() == 0) {
			throw new ParallaxRuntimeException("Cannot get the sibling of the root.");
		}
		return new FileHandle(new File(file.getParent(), name));
	}

	public FileHandle parent () {
		File parent = file.getParentFile();
		if (parent == null) {
				parent = new File("");
		}
		return new FileHandle(parent);
	}

	public boolean exists () {
		return file().exists();
	}

	/** Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
	 * determined. */
	public long length () {
		if (!file.exists()) {
			InputStream input = read();
			try {
				return input.available();
			} catch (Exception ignored) {
			} finally {
				StreamUtils.closeQuietly(input);
			}
			return 0;
		}
		return file().length();
	}

	public long lastModified () {
		return file().lastModified();
	}

	@Override
	public boolean equals (Object obj) {
		if (!(obj instanceof FileHandle)) {
			return false;
		}
		FileHandle other = (FileHandle)obj;
		return path().equals(other.path());
	}

	@Override
	public int hashCode () {
		int hash = 1;
		hash = hash * 67 + path().hashCode();
		return hash;
	}

	public String toString () {
		return file.getPath().replace('\\', '/');
	}
}
