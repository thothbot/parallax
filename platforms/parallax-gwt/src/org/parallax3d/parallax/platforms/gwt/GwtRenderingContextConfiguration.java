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

public class GwtRenderingContextConfiguration {

    private boolean stencil = false;

    private boolean antialiasing = false;

    private boolean preserveDrawingBuffer = false;

    private boolean alpha = false;

    private boolean premultipliedAlpha = false;

    /**
     * whether to use a stencil buffer
     */
    public boolean isStencil() {
        return stencil;
    }

    public GwtRenderingContextConfiguration setStencil(boolean stencil) {
        this.stencil = stencil;
        return this;
    }

    /**
     * whether to enable antialiasing
     */
    public boolean isAntialiasing() {
        return antialiasing;
    }

    public GwtRenderingContextConfiguration setAntialiasing(boolean antialiasing) {
        this.antialiasing = antialiasing;
        return this;
    }

    /**
     * preserve the back buffer, needed if you fetch a screenshot via canvas#toDataUrl, may have performance impact
     */
    public boolean isPreserveDrawingBuffer() {
        return preserveDrawingBuffer;
    }

    public GwtRenderingContextConfiguration setPreserveDrawingBuffer(boolean preserveDrawingBuffer) {
        this.preserveDrawingBuffer = preserveDrawingBuffer;
        return this;
    }

    /**
     * whether to include an alpha channel in the color buffer to combine the color buffer with the rest of the
     * webpage effectively allows transparent backgrounds in GWT, at a performance cost.
     */
    public boolean isAlpha() {
        return alpha;
    }

    public GwtRenderingContextConfiguration setAlpha(boolean alpha) {
        this.alpha = alpha;
        return this;
    }

    /**
     * whether to use premultipliedalpha, may have performance impact
     */
    public boolean isPremultipliedAlpha() {
        return premultipliedAlpha;
    }

    public GwtRenderingContextConfiguration setPremultipliedAlpha(boolean premultipliedAlpha) {
        this.premultipliedAlpha = premultipliedAlpha;
        return this;
    }
}
