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

import org.parallax3d.parallax.system.gl.GL20;

import java.awt.event.ActionListener;
import java.util.logging.Level;

public abstract class App {

    public static App app;
    public static Files files;

    public enum ApplicationType {
        Android, Desktop, WebGL
    }

    public abstract Files getFiles ();

    public abstract Rendering getRendering ();

    public abstract void log (String tag, String message);
    public abstract void log (String tag, String message, Throwable exception);

    public abstract void error (String tag, String message);
    public abstract void error (String tag, String message, Throwable exception);

    public abstract void debug (String tag, String message);
    public abstract void debug (String tag, String message, Throwable exception);

    public abstract void setLogLevel (Level logLevel);
    public abstract Level getLogLevel ();

    public abstract ApplicationType getType ();

//    public abstract Preferences getPreferences (String name);

    public abstract void exit ();
}

