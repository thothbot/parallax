/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * This file is based on libgdx sources.
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

package org.parallax3d.parallax.platforms.gwt;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.platforms.gwt.system.assets.Asset;
import org.parallax3d.parallax.platforms.gwt.system.assets.AssetDirectory;
import org.parallax3d.parallax.platforms.gwt.system.assets.AssetFile;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GwtFileHandle extends FileHandle {

    Asset asset;

    public GwtFileHandle( String fileName ) {

        asset = GwtParallax.assets.get(fixSlashes(fileName));
        if(asset == null)
            throw new ParallaxRuntimeException("File not found: " + fileName);
    }

    private static String fixSlashes(String path) {
        path = path.replace('\\', '/');
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public GwtFileHandle load(final FileListener<GwtFileHandle> callback) {
        if (asset == null || !(asset instanceof AssetFile)) {
            if(callback != null) callback.onFailure();
            return this;
        }

        final AssetFile assetFile = (AssetFile)asset;

        if (assetFile.isLoaded && assetFile.data != null) {
            if(callback != null) callback.onSuccess(this);
            return this;
        }

        assetFile.load(new FileListener<Object>() {

            public void onProgress(double amount) {
                assetFile.loaded = (long) amount;
                if(callback != null) callback.onProgress(amount);
            }

            public void onFailure() {
                assetFile.isFailed = true;
                if(callback != null) callback.onFailure();
            }

            public void onSuccess(Object result) {
                assetFile.data = result;
                assetFile.isLoaded = true;
                if(callback != null) callback.onSuccess(GwtFileHandle.this);
            }
        });

        return this;
    }

    public boolean isDirectory() {
        return this.asset instanceof AssetDirectory;
    }

    public String path() {
        return asset.getPath();
    }

    public String name() {
        int index = path().lastIndexOf('/');
        if (index < 0) return path();
        return path().substring(index + 1);
    }

    public String extension() {
        String name = name();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return name.substring(dotIndex + 1);
    }

    public String nameWithoutExtension() {
        String name = name();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) return name;
        return name.substring(0, dotIndex);
    }

    /**
     * @return the path and filename without the extension, e.g. dir/dir2/file.png -> dir/dir2/file
     */
    public String pathWithoutExtension() {
        String path = asset.getPath();
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1) return path;
        return path.substring(0, dotIndex);
    }

    public Object file() {
        if(isDirectory()) return null;

        return ((AssetFile)asset).data;
    }

    public boolean isText() {
        if(isDirectory()) return false;

        return ((AssetFile)asset).isText();
    }

    /**
     * Returns a stream for reading this file as bytes.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public InputStream read() {
        if(isDirectory()) return null;

        InputStream in = ((AssetFile)asset).read();
        if (in == null) throw new ParallaxRuntimeException(path() + " does not exist");
        return in;
    }

    /**
     * Returns a buffered stream for reading this file as bytes.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public BufferedInputStream read(int bufferSize) {
        return new BufferedInputStream(read(), bufferSize);
    }

    /**
     * Returns a reader for reading this file as characters.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public Reader reader() {
        return new InputStreamReader(read());
    }

    /**
     * Returns a reader for reading this file as characters.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public Reader reader(String charset) {
        try {
            return new InputStreamReader(read(), charset);
        } catch (UnsupportedEncodingException e) {
            throw new ParallaxRuntimeException("Encoding '" + charset + "' not supported", e);
        }
    }

    /**
     * Returns a buffered reader for reading this file as characters.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public BufferedReader reader(int bufferSize) {
        return new BufferedReader(reader(), bufferSize);
    }

    /**
     * Returns a buffered reader for reading this file as characters.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public BufferedReader reader(int bufferSize, String charset) {
        return new BufferedReader(reader(charset), bufferSize);
    }

    /**
     * Reads the entire file into a string using the platform's default charset.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public String readString() {
        return readString(null);
    }

    /**
     * Reads the entire file into a string using the specified charset.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public String readString(String charset) {
        if(isDirectory()) return null;

        if (((AssetFile)asset).isText())
            return (String) ((AssetFile)asset).data;
        try {
            return new String(readBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Reads the entire file into a byte array.
     *
     * @throw ParallaxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    public byte[] readBytes() {
        int length = (int) length();
        if (length == 0) length = 512;
        byte[] buffer = new byte[length];
        int position = 0;
        InputStream input = read();
        try {
            while (true) {
                int count = input.read(buffer, position, buffer.length - position);
                if (count == -1) break;
                position += count;
                if (position == buffer.length) {
                    // Grow buffer.
                    byte[] newBuffer = new byte[buffer.length * 2];
                    System.arraycopy(buffer, 0, newBuffer, 0, position);
                    buffer = newBuffer;
                }
            }
        } catch (IOException ex) {
            throw new ParallaxRuntimeException("Error reading file: " + this, ex);
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }
        if (position < buffer.length) {
            // Shrink buffer.
            byte[] newBuffer = new byte[position];
            System.arraycopy(buffer, 0, newBuffer, 0, position);
            buffer = newBuffer;
        }
        return buffer;
    }

    /**
     * Reads the entire file into the byte array. The byte array must be big enough to hold the file's data.
     *
     * @param bytes  the array to load the file into
     * @param offset the offset to start writing bytes
     * @param size   the number of bytes to read, see {@link #length()}
     * @return the number of read bytes
     */
    public int readBytes(byte[] bytes, int offset, int size) {
        InputStream input = read();
        int position = 0;
        try {
            while (true) {
                int count = input.read(bytes, offset + position, size - position);
                if (count <= 0) break;
                position += count;
            }
        } catch (IOException ex) {
            throw new ParallaxRuntimeException("Error reading file: " + this, ex);
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }
        return position - offset;
    }

    public FileHandle[] list() {
        String url = asset.getPath();
        List<FileHandle> files = new ArrayList<>();
        for (Asset asset : GwtParallax.assets.getAll()) {
            if (asset instanceof AssetFile && ((AssetFile)asset).isChild(url)) {
                files.add(new GwtFileHandle(asset.getPath()));
            }
        }
        FileHandle[] list = new FileHandle[files.size()];
        System.arraycopy(files.toArray(), 0, list, 0, list.length);
        return list;
    }

    public FileHandle[] list (FileFilter filter) {
        String url = asset.getPath();
        List<FileHandle> files = new ArrayList<FileHandle>();
        for (Asset asset : GwtParallax.assets.getAll()) {
            if (asset instanceof AssetFile && ((AssetFile)asset).isChild(url)  && filter.accept(new File(asset.getPath())) ) {
                files.add(new GwtFileHandle(asset.getPath()));
            }
        }
        FileHandle[] list = new FileHandle[files.size()];
        System.arraycopy(files.toArray(), 0, list, 0, list.length);
        return list;
    }

    public FileHandle[] list (FilenameFilter filter) {
        String url = asset.getPath();
        List<FileHandle> files = new ArrayList<FileHandle>();
        for (Asset asset : GwtParallax.assets.getAll()) {
            if (asset instanceof AssetFile && ((AssetFile)asset).isChild(url) && filter.accept(new File(url), asset.getPath().substring(url.length() + 1))) {
                files.add(new GwtFileHandle(asset.getPath()));
            }
        }
        FileHandle[] list = new FileHandle[files.size()];
        System.arraycopy(files.toArray(), 0, list, 0, list.length);
        return list;
    }

    public FileHandle[] list (String suffix) {
        String url = asset.getPath();
        List<FileHandle> files = new ArrayList<FileHandle>();
        for (Asset asset : GwtParallax.assets.getAll()) {
            if (asset instanceof AssetFile && ((AssetFile)asset).isChild(url) && asset.getPath().endsWith(suffix)) {
                files.add(new GwtFileHandle(asset.getPath()));
            }
        }
        FileHandle[] list = new FileHandle[files.size()];
        System.arraycopy(files.toArray(), 0, list, 0, list.length);
        return list;
    }

    public FileHandle child(String name) {
        return new GwtFileHandle((asset.getPath().isEmpty() ? "" : (asset.getPath() + (asset.getPath().endsWith("/") ? "" : "/"))) + name);
    }

    public FileHandle parent() {
        int index = asset.getPath().lastIndexOf("/");
        String dir = "";
        if (index > 0) dir = asset.getPath().substring(0, index);
        return new GwtFileHandle( dir );
    }

    public FileHandle sibling(String name) {
        return parent().child(fixSlashes(name));
    }

    public boolean exists() {
        return GwtParallax.assets.contains(asset.getPath());
    }

    /**
     * Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
     * determined.
     */
    public long length() {
        if(isDirectory()) return 0;

        return ((AssetFile)asset).getSize();
    }

    public long lastModified() {
        return 0;
    }

    public String toString() {
        return asset.getPath();
    }
}
