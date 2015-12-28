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

import java.awt.event.ActionListener;

public interface Application {

    public enum ApplicationType {
        Android, Desktop, WebGL
    }

    public static final int LOG_NONE = 0;
    public static final int LOG_DEBUG = 3;
    public static final int LOG_INFO = 2;
    public static final int LOG_ERROR = 1;

    public Files getFiles ();

    public Rendering getRendering ();

    public void log (String tag, String message);
    public void log (String tag, String message, Throwable exception);

    public void error (String tag, String message);
    public void error (String tag, String message, Throwable exception);

    public void debug (String tag, String message);
    public void debug (String tag, String message, Throwable exception);

    public void setLogLevel (int logLevel);
    public int getLogLevel ();

    public ApplicationType getType ();

    public Preferences getPreferences (String name);

    public void exit ();
}

