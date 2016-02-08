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

package org.parallax3d.parallax.platforms.gwt;

import com.google.gwt.storage.client.Storage;
import org.parallax3d.parallax.Files;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.files.FileType;
import org.parallax3d.parallax.platforms.gwt.preloader.Preloader;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

public class GwtFiles implements Files {

    public static final Storage LocalStorage = Storage.getLocalStorageIfSupported();

    final Preloader preloader;

    public GwtFiles (Preloader preloader) {
        this.preloader = preloader;
    }

    @Override
    public FileHandle getFileHandle (String path, FileType type) {
        return getFileHandle(path, type, null);
    }

    @Override
    public FileHandle getFileHandle(String path, FileType type, FileListener<?> listener) {
        if (type != FileType.Internal) throw new ParallaxRuntimeException("FileType '" + type + "' not supported in GWT backend");
        return new GwtFileHandle(preloader, path, type, listener);
    }

    @Override
    public FileHandle classpath (String path) {
        return classpath(path, null);
    }

    @Override
    public FileHandle classpath(String path, FileListener<?> listener) {
        return new GwtFileHandle(preloader, path, FileType.Classpath);
    }

    @Override
    public FileHandle internal (String path) {
        return internal(path, null);
    }

    @Override
    public FileHandle internal(String path, FileListener<?> listener) {
        return new GwtFileHandle(preloader, path, FileType.Internal);
    }

    @Override
    public FileHandle external (String path) {
        return external(path, null);
    }

    @Override
    public FileHandle external(String path, FileListener<?> listener) {
        throw new ParallaxRuntimeException("Not supported in GWT backend");
    }

    @Override
    public FileHandle absolute (String path) {
        return absolute(path, null);
    }

    @Override
    public FileHandle absolute(String path, FileListener<?> listener) {
        throw new ParallaxRuntimeException("Not supported in GWT backend");
    }

    @Override
    public FileHandle local (String path) {
        return local(path, null);
    }

    @Override
    public FileHandle local(String path, FileListener<?> listener) {
        throw new ParallaxRuntimeException("Not supported in GWT backend");
    }

    @Override
    public String getExternalStoragePath () {
        return null;
    }

    @Override
    public boolean isExternalStorageAvailable () {
        return false;
    }

    @Override
    public String getLocalStoragePath () {
        return null;
    }

    @Override
    public boolean isLocalStorageAvailable () {
        return false;
    }
}
