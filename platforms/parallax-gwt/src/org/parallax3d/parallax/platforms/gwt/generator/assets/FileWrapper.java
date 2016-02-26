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

package org.parallax3d.parallax.platforms.gwt.generator.assets;

import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.system.StreamUtils;

import java.io.*;

/** Used in PreloaderBundleGenerator to ease my pain. Since we emulate the original FileHandle, i have to make a copy...
 * @author mzechner
 * @author Nathan Sweet */
public class FileWrapper {
	protected File file;

	protected FileWrapper () {}

	public FileWrapper (String fileName) {
		this.file = new File(fileName);
	}

	protected FileWrapper (File file) {
		this.file = file;
	}

	public String path () {
		return file.getPath();
	}

	public String name () {
		return file.getName();
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

	public String ext () {
		int dotIndex = name().lastIndexOf('.');
		if (dotIndex == -1) return "";
		return name().toLowerCase().substring(dotIndex + 1);
	}

	public InputStream read () {
		try {
			return new FileInputStream(file());
		} catch (Exception ex) {
			if (file().isDirectory())
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file, ex);
			throw new ParallaxRuntimeException("Error reading file: " + file, ex);
		}
	}

	public OutputStream write (boolean append) {
		parent().mkdirs();
		try {
			return new FileOutputStream(file(), append);
		} catch (Exception ex) {
			if (file().isDirectory())
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file, ex);
			throw new ParallaxRuntimeException("Error writing file: " + file, ex);
		}
	}

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
			throw new ParallaxRuntimeException("Error stream writing to file: " + file, ex);
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

	public Writer writer (boolean append, String charset) {
		parent().mkdirs();
		try {
			FileOutputStream output = new FileOutputStream(file(), append);
			if (charset == null)
				return new OutputStreamWriter(output);
			else
				return new OutputStreamWriter(output, charset);
		} catch (IOException ex) {
			if (file().isDirectory())
				throw new ParallaxRuntimeException("Cannot open a stream to a directory: " + file, ex);
			throw new ParallaxRuntimeException("Error writing file: " + file, ex);
		}
	}

	public void writeString (String string, boolean append) {
		writeString(string, append, null);
	}
	public void writeString (String string, boolean append, String charset) {
		Writer writer = null;
		try {
			writer = writer(append, charset);
			writer.write(string);
		} catch (Exception ex) {
			throw new ParallaxRuntimeException("Error writing file: " + file, ex);
		} finally {
			StreamUtils.closeQuietly(writer);
		}
	}

	public FileWrapper[] list () {
		String[] relativePaths = file().list();
		if (relativePaths == null) return new FileWrapper[0];
		FileWrapper[] handles = new FileWrapper[relativePaths.length];
		for (int i = 0, n = relativePaths.length; i < n; i++)
			handles[i] = child(relativePaths[i]);
		return handles;
	}

	public boolean isDirectory () {
		return file().isDirectory();
	}

	public FileWrapper child (String name) {
		if (file.getPath().length() == 0) return new FileWrapper(new File(name));
		return new FileWrapper(new File(file, name));
	}

	public FileWrapper parent () {
		File parent = file.getParentFile();
		if (parent == null) {
			parent = new File("/");
		}
		return new FileWrapper(parent);
	}
	public boolean mkdirs () {
		return file().mkdirs();
	}

	public boolean exists () {
		return file().exists();
	}

	public boolean delete () {
		return file().delete();
	}

	public boolean deleteDirectory () {
		return deleteDirectory(file());
	}

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

	public void moveTo (FileWrapper dest) {
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
			throw new ParallaxRuntimeException("Error copying source file: " + source.file + "\n" //
				+ "To destination: " + dest.file, ex);
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
