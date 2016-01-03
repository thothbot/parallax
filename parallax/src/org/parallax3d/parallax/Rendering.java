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

import org.parallax3d.parallax.events.AnimationReadyListener;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.system.gl.GL20;

public interface Rendering {

    boolean isGL30Available();

    GL20 getGL20();

    GLRenderer getRenderer();

    int getWidth();

    int getHeight();

    long getFrameId();

    float getDeltaTime();

    float getRawDeltaTime();

    int getFramesPerSecond();

    void setAnimation(Animation animation);

    /** @return the pixels per inch on the x-axis */
    float getPpiX();

    /** @return the pixels per inch on the y-axis */
    float getPpiY();

    boolean supportsDisplayModeChange();

    /** Whether the app is fullscreen or not */
    boolean isFullscreen();

    void setAnimationReadyListener(AnimationReadyListener animationReadyListener);

    void pause();

    void resume();

}
