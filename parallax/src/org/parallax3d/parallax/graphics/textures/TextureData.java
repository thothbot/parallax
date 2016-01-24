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

/**
 * Representation of an image that can be uploaded as a texture.
 */
public interface TextureData
{
    /**
     * Upload the texture to the GPU.
     *
     * @param target    eg GL_TEXTURE_2D.
     */
    public void glTexImage2D(int target);

    /**
     *
     * @return  width of image in pixels.
     */
    public int getWidth();

    /**
     *
     * @return  height of image in pixels.
     */
    public int getHeight();

    /**
     * @param width
     * @param height
     * @return  a new Image which is a scaled copy of this one.
     */
    public TextureData createScaledCopy(int width, int height);

    /**
     * Explicitly free the Image's internal resources.
     */
    public void recycle();
}
