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

package org.parallax3d.parallax.loaders;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;

public abstract class ModelLoader extends Loader {

    ModelLoadHandler handler;

    String texturePath;

    public ModelLoader(final String url, final ModelLoadHandler handler) {

        this.handler = handler;
        this.texturePath = extractUrlBase(url);

        Parallax.asset(url, new FileListener<FileHandle>() {
            @Override
            public void onProgress(double amount) {

            }

            @Override
            public void onFailure() {
                Log.error("An error occurred while loading model: " + url);
            }

            @Override
            public void onSuccess(FileHandle result) {
                Log.info("Loaded model: " + url);

                parse(result);
                onReady();
            }
        });

    }

    protected void onReady() {
        if(handler != null)
            handler.onModelLoaded(ModelLoader.this, getGeometry());
    }

    public abstract AbstractGeometry getGeometry();

    public String getTexturePath() {
        return this.texturePath;
    }

    private String extractUrlBase( String url )
    {
        int i = url.lastIndexOf('/');
        return (i >= 0) ? url.substring(0, i) + '/' : "";
    }
}
