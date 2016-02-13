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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.platforms.gwt.GwtFileHandle;
import org.parallax3d.parallax.platforms.gwt.GwtGL20;
import org.parallax3d.parallax.platforms.gwt.preloader.AssetDownloader;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.PixelType;

public class PixmapTextureData implements TextureData {

    private Element image;

    public PixmapTextureData() {
        image = AssetDownloader.createImage();
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
    public void glTexImage2D(GL20 gl, int target) {
        ((GwtGL20)gl).glTexImage2D(target, 0, PixelFormat.RGBA.getValue(), PixelFormat.RGBA.getValue(), PixelType.UNSIGNED_BYTE.getValue(), (ImageElement)image);
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
    public TextureData createScaledCopy(int width, int height) {
        return null;
    }

    @Override
    public void recycle() {

    }
}
