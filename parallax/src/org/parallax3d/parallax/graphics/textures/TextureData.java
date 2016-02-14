/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 * 
 * This file is part of the realh fork of the Parallax project.
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

/**
 * Representation of an image that can be uploaded as a texture.
 */
public interface TextureData
{
    /**
     * This callback will be called when the image has been loaded.
     */
    interface TextureLoadHandler
    {
        void onLoaded(boolean success);
    }

    void load(FileHandle file, TextureLoadHandler textureLoadHandler);

    /**
     * Upload the texture to the GPU.
     *
     * @param target    eg GL_TEXTURE_2D.
     */
    void glTexImage2D(GL20 gl, int target, PixelFormat pixelFormat, PixelType pixelType);

    /**
     *
     * @return  width of image in pixels.
     */
    int getWidth();

    /**
     *
     * @return  height of image in pixels.
     */
    int getHeight();

    /**
     * Warning: Scaling through the canvas will only work with images that use
     * premultiplied alpha.
     *
     * @param maxSize  the max size of absoluteWidth or absoluteHeight
     *
     * @return a new Image, or the same one if no clamping was necessary
     */
    TextureData clampToMaxSize ( int maxSize );

    boolean isPowerOfTwo();
    TextureData toPowerOfTwo();

    /**
     * Explicitly free the Image's internal resources.
     */
    void recycle();
}
