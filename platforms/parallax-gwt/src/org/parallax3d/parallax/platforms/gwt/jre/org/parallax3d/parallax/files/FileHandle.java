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

package org.parallax3d.parallax.files;

import org.parallax3d.parallax.system.ParallaxRuntimeException;

import java.io.*;

public class FileHandle {

	public String path () {
		throw new ParallaxRuntimeException("Stub");
	}

	public String name () {
		throw new ParallaxRuntimeException("Stub");
	}

	public String extension () {
		throw new ParallaxRuntimeException("Stub");
	}

	public String nameWithoutExtension () {
		throw new ParallaxRuntimeException("Stub");
	}

	public String pathWithoutExtension () {
		throw new ParallaxRuntimeException("Stub");
	}

	public boolean isText() {
		throw new ParallaxRuntimeException("Stub");
	}

	public InputStream read () {
		throw new ParallaxRuntimeException("Stub");
	}

	public BufferedInputStream read (int bufferSize) {
		throw new ParallaxRuntimeException("Stub");
	}

	public Reader reader () {
		throw new ParallaxRuntimeException("Stub");
	}

	public Reader reader (String charset) {
		throw new ParallaxRuntimeException("Stub");
	}

	public BufferedReader reader (int bufferSize) {
		throw new ParallaxRuntimeException("Stub");
	}

	public BufferedReader reader (int bufferSize, String charset) {
		throw new ParallaxRuntimeException("Stub");
	}

	public String readString () {
		throw new ParallaxRuntimeException("Stub");
	}

	public String readString (String charset) {
		throw new ParallaxRuntimeException("Stub");
	}

	public byte[] readBytes () {
		throw new ParallaxRuntimeException("Stub");
	}

	public int readBytes (byte[] bytes, int offset, int size) {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle[] list (FileFilter filter) {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle[] list (FilenameFilter filter) {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle[] list () {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle[] list (String suffix) {
		throw new ParallaxRuntimeException("Stub");
	}

	public boolean isDirectory () {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle child (String name) {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle parent () {
		throw new ParallaxRuntimeException("Stub");
	}

	public FileHandle sibling (String name) {
		throw new ParallaxRuntimeException("Stub");
	}

	public boolean exists () {
		throw new ParallaxRuntimeException("Stub");
	}

	/** Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
	 * determined. */
	public long length () {
		throw new ParallaxRuntimeException("Stub");
	}

	public long lastModified () {
		throw new ParallaxRuntimeException("Stub");
	}

	public String toString () {
		throw new ParallaxRuntimeException("Stub");
	}
}
