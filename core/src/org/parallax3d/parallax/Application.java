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

import org.parallax3d.parallax.files.Files;

public interface Application {
    /** Enumeration of possible {@link Application} types
     *
     * @author mzechner */
    public enum ApplicationType {
        Android, Desktop, HeadlessDesktop, Applet, WebGL, iOS
    }

    public static final int LOG_NONE = 0;
    public static final int LOG_DEBUG = 3;
    public static final int LOG_INFO = 2;
    public static final int LOG_ERROR = 1;

    /** @return the {@link ApplicationListener} instance */
    public ApplicationListener getApplicationListener ();

    /** @return the {@link Graphics} instance */
    public Graphics getGraphics ();

//    /** @return the {@link Audio} instance */
//    public Audio getAudio ();
//
//    /** @return the {@link Input} instance */
//    public Input getInput ();

    /** @return the {@link Files} instance */
    public Files getFiles ();

//    /** @return the {@link Net} instance */
//    public Net getNet ();

    /** Logs a message to the console or logcat */
    public void log (String tag, String message);

    /** Logs a message to the console or logcat */
    public void log (String tag, String message, Throwable exception);

    /** Logs an error message to the console or logcat */
    public void error (String tag, String message);

    /** Logs an error message to the console or logcat */
    public void error (String tag, String message, Throwable exception);

    /** Logs a debug message to the console or logcat */
    public void debug (String tag, String message);

    /** Logs a debug message to the console or logcat */
    public void debug (String tag, String message, Throwable exception);

    /** Sets the log level. {@link #LOG_NONE} will mute all log output. {@link #LOG_ERROR} will only let error messages through.
     * {@link #LOG_INFO} will let all non-debug messages through, and {@link #LOG_DEBUG} will let all messages through.
     * @param logLevel {@link #LOG_NONE}, {@link #LOG_ERROR}, {@link #LOG_INFO}, {@link #LOG_DEBUG}. */
    public void setLogLevel (int logLevel);

    /** Gets the log level. */
    public int getLogLevel ();

    /** @return what {@link ApplicationType} this application has, e.g. Android or Desktop */
    public ApplicationType getType ();

    /** @return the Android API level on Android, the major OS version on iOS (5, 6, 7, ..), or 0 on the desktop. */
    public int getVersion ();

    /** @return the Java heap memory use in bytes */
    public long getJavaHeap ();

    /** @return the Native heap memory use in bytes */
    public long getNativeHeap ();

    /** Returns the {@link Preferences} instance of this Application. It can be used to store application settings across runs.
     * @param name the name of the preferences, must be useable as a file name.
     * @return the preferences. */
    public Preferences getPreferences (String name);

//    public Clipboard getClipboard ();

    /** Posts a {@link Runnable} on the main loop thread.
     *
     * @param runnable the runnable. */
    public void postRunnable (Runnable runnable);

    /** Schedule an exit from the application. On android, this will cause a call to pause() and dispose() some time in the future,
     * it will not immediately finish your application.
     * On iOS this should be avoided in production as it breaks Apples guidelines*/
    public void exit ();

    /** Adds a new {@link LifecycleListener} to the application. This can be used by extensions to hook into the lifecycle more
     * easily. The {@link ApplicationListener} methods are sufficient for application level development.
     * @param listener */
    public void addLifecycleListener (LifecycleListener listener);

    /** Removes the {@link LifecycleListener}.
     * @param listener */
    public void removeLifecycleListener (LifecycleListener listener);
}

