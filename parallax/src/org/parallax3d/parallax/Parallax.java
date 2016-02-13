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

public class Parallax {

    public enum ApplicationType {
        Android, Desktop, WebGL
    }

    public interface AppListener {
        void onAppInitialized();
    }

    public interface App {

        FileHandle asset(String path);
        FileHandle asset(String path, FileListener<? extends FileHandle> listener);

        Logger getLogger();
        ApplicationType getType();

    }

    public static App app;

    public static boolean isAppInitialized() {
        return app != null;
    }

    private static void checkAppInitialized() {
        if(!isAppInitialized())
            throw new ParallaxRuntimeException("Parallax application is not initialized");
    }

    public static App app() {
        checkAppInitialized();
        return app;
    }

    public static FileHandle asset( String path ) {
        return asset(path, null);
    }

    public static FileHandle asset(String path, FileListener<FileHandle> listener) {
        checkAppInitialized();
        return app.asset( path, listener );
    }

    public static Logger logger() {
        checkAppInitialized();
        return app.getLogger();
    }
}
