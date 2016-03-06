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

import org.parallax3d.parallax.system.AnimationReadyListener;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.system.gl.GL20;

public interface RenderingContext {

    GL20 getGL20();

    Input getInput ();

    GLRenderer getRenderer();

    int getWidth();

    int getHeight();

    double getAspectRation();

    long getFrameId();

    double getDeltaTime();

    /** This is a scaling factor for the Density Independent Pixel unit, following the same conventions as
     * android.util.DisplayMetrics#density, where one DIP is one pixel on an approximately 160 dpi screen. Thus on a 160dpi screen
     * this density value will be 1; on a 120 dpi screen it would be .75; etc.
     *
     * @return the logical density of the Display. */
    public float getDensity ();

    double getRawDeltaTime();

    int getFramesPerSecond();

    void setAnimation(Animation animation);

    /** @return the pixels per inch on the x-axis */
    float getPpiX();

    /** @return the pixels per inch on the y-axis */
    float getPpiY();

    boolean supportsDisplayModeChange();

    /** Whether the app is fullscreen or not */
    boolean isFullscreen();

    public void setFullscreen();

    void addAnimationReadyListener(AnimationReadyListener animationReadyListener);

    void stop();

    void run();

    boolean isRun();

}
