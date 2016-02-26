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

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.platforms.gwt.GwtFileHandle;
import org.parallax3d.parallax.platforms.gwt.GwtGL20;
import org.parallax3d.parallax.platforms.gwt.system.assets.AssetFile;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.PixelType;

public class PixmapTextureData implements TextureData {

    private Element image;

    public PixmapTextureData() {
        image = AssetFile.createImage();
    }

    @Override
    public void load(final FileHandle file, final TextureLoadHandler textureLoadHandler) {
        ((GwtFileHandle)file).load(new FileListener<GwtFileHandle>() {
            @Override
            public void onSuccess(GwtFileHandle result) {
                Log.info("Loaded texture: " + file.path());
                image = (ImageElement)result.file();
                textureLoadHandler.onLoaded( true );
            }

            @Override
            public void onProgress(double amount) {

            }

            @Override
            public void onFailure() {
                Log.error("An error occurred while loading texture: " + file.path());
                textureLoadHandler.onLoaded( false );
            }
        });
    }

    @Override
    public void glTexImage2D(GL20 gl, int target, PixelFormat pixelFormat, PixelType pixelType) {
        ((GwtGL20)gl).glTexImage2D(target, 0, pixelFormat.getValue(), pixelFormat.getValue(), pixelType.getValue(), image);
    }

    @Override
    public int getWidth() {
        return image.getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return image.getOffsetHeight();
    }

    /**
     * Warning: Scaling through the canvas will only work with images that use
     * premultiplied alpha.
     *
     * @param maxSize  the max size of absoluteWidth or absoluteHeight
     *
     * @return a new Image, or the same one if no clamping was necessary
     */
    public PixmapTextureData clampToMaxSize ( int maxSize )
    {
        int imgWidth = image.getOffsetWidth();
        int imgHeight = image.getOffsetHeight();

        if ( imgWidth <= maxSize && imgHeight <= maxSize )
            return this;

        int maxDimension = Math.max( imgWidth, imgHeight );
        int newWidth = (int) Math.floor( imgWidth * maxSize / maxDimension );
        int newHeight = (int) Math.floor( imgHeight * maxSize / maxDimension );

        CanvasElement canvas = Document.get().createElement("canvas").cast();
        canvas.setWidth(newWidth);
        canvas.setHeight(newHeight);

        Context2d context = canvas.getContext2d();
        context.drawImage((ImageElement)image, 0, 0, imgWidth, imgHeight, 0, 0, newWidth, newHeight );

        image.getParentElement().appendChild(canvas);
        image.removeFromParent();
        image = canvas;

        return this;
    }

    @Override
    public boolean isPowerOfTwo() {
        return  Mathematics.isPowerOfTwo( this.getWidth() )
                && Mathematics.isPowerOfTwo( this.getHeight() );
    }

    @Override
    public PixmapTextureData toPowerOfTwo() {
        if(isPowerOfTwo())
            return this;

        int width = image.getOffsetWidth();
        int height = image.getOffsetHeight();

        CanvasElement canvas = Document.get().createElement("canvas").cast();

        // Scale up the texture to the next highest power of two dimensions.
        canvas.setWidth( Mathematics.getNextHighestPowerOfTwo( width ) );
        canvas.setHeight( Mathematics.getNextHighestPowerOfTwo( height ) );

        Context2d context = canvas.getContext2d();
        context.drawImage((ImageElement)image, 0, 0, width, height);

        image = canvas;
        return this;
    }

    @Override
    public void recycle() {

    }
}
