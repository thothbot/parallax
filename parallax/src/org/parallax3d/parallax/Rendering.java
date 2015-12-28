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
import org.parallax3d.parallax.system.gl.GL30;

public interface Rendering {

    public boolean isGL30Available ();

    public GL20 getGL20 ();

    public GL30 getGL30 ();

    public int getWidth ();

    public int getHeight ();

    public long getFrameId ();

    public float getDeltaTime ();

    public float getRawDeltaTime ();

    public int getFramesPerSecond ();

    /** @return the pixels per inch on the x-axis */
    public float getPpiX ();

    /** @return the pixels per inch on the y-axis */
    public float getPpiY ();

    /** @param extension the extension name
     * @return whether the extension is supported */
    public boolean supportsExtension (String extension);

    public boolean supportsDisplayModeChange ();

    /** Whether the app is fullscreen or not */
    public boolean isFullscreen ();
}
