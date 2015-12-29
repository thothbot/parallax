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

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;

public class GwtAppConfiguration {

    /**
     * Width of the drawing area in pixels
     */
    public int width;

    /**
     * Height of the drawing area in pixels
     */
    public int height;

    /**
     * whether to use a stencil buffer
     */
    public boolean stencil = false;

    /**
     * whether to enable antialiasing
     */
    public boolean antialiasing = false;

    /**
     * the Panel to add the WebGL canvas to, can be null in which case a Panel is added automatically to the body
     * element of the DOM
     */
    public Panel rootPanel;

    /**
     * the id of a canvas element to be used as the drawing area, can be null in which case a Panel and Canvas
     * are added to the body element of the DOM
     */
    public String canvasId;

    /**
     * a TextArea to log messages to, can be null in which case a TextArea will be added to the body element
     * of the DOM.
     */
    public TextArea log;

    /**
     * whether SoundManager2 should prefer to use flash instead of html5 audio (it should fall back if not available)
     */
    public boolean preferFlash = true;

    /**
     * preserve the back buffer, needed if you fetch a screenshot via canvas#toDataUrl, may have performance impact
     */
    public boolean preserveDrawingBuffer = false;

    /**
     * whether to include an alpha channel in the color buffer to combine the color buffer with the rest of the
     * webpage effectively allows transparent backgrounds in GWT, at a performance cost.
     */
    public boolean alpha = false;

    /**
     * whether to use premultipliedalpha, may have performance impact
     */
    public boolean premultipliedAlpha = false;

}
