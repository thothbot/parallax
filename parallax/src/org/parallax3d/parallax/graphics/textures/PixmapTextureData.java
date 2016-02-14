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

package org.parallax3d.parallax.graphics.textures;

import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.PixelType;

public class PixmapTextureData implements TextureData {

    public PixmapTextureData () {

    }

    @Override
    public void load(FileHandle file, TextureLoadHandler textureLoadHandler) {
        textureLoadHandler.onLoaded( true );
    }

    @Override
    public void glTexImage2D(GL20 gl, int target, PixelFormat pixelFormat, PixelType pixelType) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public PixmapTextureData clampToMaxSize(int maxSize) {
        return this;
    }

    @Override
    public boolean isPowerOfTwo() {
        return false;
    }

    @Override
    public PixmapTextureData toPowerOfTwo() {
        return this;
    }

    @Override
    public void recycle() {

    }
}
