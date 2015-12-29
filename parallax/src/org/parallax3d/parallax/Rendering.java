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

public abstract class Rendering {

    public abstract boolean isGL30Available();

    public abstract GL20 getGL20();

    public abstract GL30 getGL30();

    public abstract int getWidth();

    public abstract int getHeight();

    public double getAbsoluteAspectRation() {
        return getWidth() / (double)getHeight();
    }

    public abstract long getFrameId();

    public abstract float getDeltaTime();

    public abstract float getRawDeltaTime();

    public abstract int getFramesPerSecond();

    /** @return the pixels per inch on the x-axis */
    public abstract float getPpiX();

    /** @return the pixels per inch on the y-axis */
    public abstract float getPpiY();

    /** @param extension the extension name
     * @return whether the extension is supported */
    public abstract boolean supportsExtension(String extension);

    public abstract boolean supportsDisplayModeChange();

    /** Whether the app is fullscreen or not */
    public abstract boolean isFullscreen();
}
