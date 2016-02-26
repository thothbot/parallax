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
import org.parallax3d.parallax.system.ParallaxRuntimeException;

public abstract class Parallax {

    public interface ParallaxListener {
        void onParallaxApplicationReady(Parallax instance);
    }

    public enum Platform {
        Android, Desktop, WebGL
    }

    public static Parallax instance;

    public abstract FileHandle getAsset(String path);
    public abstract FileHandle getAsset(String path, FileListener<? extends FileHandle> listener);

    public abstract Logger getLogger();
    public abstract Parallax.Platform getType();

    public static Parallax getInstance() {
        isInit();
        return instance;
    }

    public static FileHandle asset( String path ) {
        isInit();
        return instance.getAsset(path);
    }

    public static FileHandle asset(String path, FileListener<FileHandle> listener) {
        isInit();
        return instance.getAsset( path, listener );
    }

    public static Logger logger() {
        isInit();
        return instance.getLogger();
    }

    private static void isInit() {
        if(instance == null)
            throw new ParallaxRuntimeException("Parallax application is not initialized");
    }
}
