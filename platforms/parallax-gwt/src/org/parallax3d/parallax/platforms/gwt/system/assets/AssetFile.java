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

package org.parallax3d.parallax.platforms.gwt.system.assets;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.typedarrays.shared.Int8Array;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class AssetFile implements Asset {
    long size;
    boolean isText;
    String mimeType;
    String path;

    public long loaded;
    public boolean isLoaded;
    public boolean isFailed;
    public Object data;

    // false for now
    boolean useBrowserCache;
    boolean useInlineBase64;

    public AssetFile(String path, long size, boolean isText, String mimeType) {
        this.path = path;
        this.size = size;
        this.isText = isText;
        this.mimeType = mimeType != null ? mimeType : "application/unknown";
    }

    public String getPath() {
        return path;
    }

    public String getMime() {
        return mimeType;
    }

    public long getSize() {
        return size;
    }

    public boolean isChild(String url) {
        return getPath().startsWith(url) && (getPath().indexOf('/', url.length() + 1) < 0);
    }

    @Override
    public String toString() {
        return "{path: " + getPath() + ", size: " + getSize() +", MIME: " + getMime() + "}";
    }

    public InputStream read ()
    {
		if (isText()) {
			try {
				return new ByteArrayInputStream(((String)data).getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
        else if(isBinary())
        {
            return ((Blob)data).read();
        }
        else
        {
            return new ByteArrayInputStream(new byte[1]);
        }
    }

    public void load (FileListener<?> listener) {
        if(isText())
            loadText((FileListener<String>)listener);
        else if(isImage())
            loadImage((FileListener<ImageElement>)listener);
        else if(isAudio())
            loadAudio((FileListener<Void>)listener);
        else if(isBinary())
            loadBinary((FileListener<Blob>)listener);
        else
            throw new ParallaxRuntimeException("Unsupported asset type");
    }

    public String getUrl() {
        return Assets.baseUrl + this.getPath();
    }

    public boolean isText() {
        return isText;
    }

    public boolean isImage() {
        return Arrays.asList("image/bmp", "image/gif", "image/jpeg", "image/png", "image/tiff").contains(getMime());
    }

    public boolean isAudio() {
        return mimeType.startsWith("audio");
    }

    public boolean isBinary() {
        return !isText() && !isImage() && !isAudio();
    }

    public void loadText (final FileListener<String> listener) {
        XMLHttpRequest request = XMLHttpRequest.create();
        request.setOnReadyStateChange(new ReadyStateChangeHandler() {
            @Override
            public void onReadyStateChange (XMLHttpRequest xhr) {
                if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                    if (xhr.getStatus() != 200) {
                        listener.onFailure();
                    } else {
                        listener.onSuccess(xhr.getResponseText());
                    }
                }
            }
        });
        setOnProgress(request, listener);
        request.open("GET", getUrl());
        request.setRequestHeader("Content-Type", "text/plain; charset=utf-8");
        request.send();
    }

    public void loadBinary (final FileListener<Blob> listener) {
        XMLHttpRequest request = XMLHttpRequest.create();
        request.setOnReadyStateChange(new ReadyStateChangeHandler() {
            @Override
            public void onReadyStateChange (XMLHttpRequest xhr) {
                if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                    if (xhr.getStatus() != 200) {
                        listener.onFailure();
                    } else {
                        Int8Array data = TypedArrays.createInt8Array(xhr.getResponseArrayBuffer());
                        listener.onSuccess(new Blob(data));
                    }
                }
            }
        });
        setOnProgress(request, listener);
        request.open("GET", getUrl());
        request.setResponseType(XMLHttpRequest.ResponseType.ArrayBuffer);
        request.send();
    }

    public void loadAudio (final FileListener<Void> listener) {
        if (useBrowserCache) {
            loadBinary(new FileListener<Blob>() {
                @Override
                public void onProgress (double amount) {
                    listener.onProgress(amount);
                }

                @Override
                public void onFailure () {
                    listener.onFailure();
                }

                @Override
                public void onSuccess (Blob result) {
                    listener.onSuccess(null);
                }

            });
        } else {
            listener.onSuccess(null);
        }
    }

    public void loadImage (final FileListener<ImageElement> listener) {
        if (useBrowserCache || useInlineBase64) {
            loadBinary(new FileListener<Blob>() {
                @Override
                public void onProgress(double amount) {
                    listener.onProgress(amount);
                }

                @Override
                public void onFailure() {
                    listener.onFailure();
                }

                @Override
                public void onSuccess(Blob result) {
                    final ImageElement image = createImage();
                    hookImgListener(image, new ImgEventListener() {
                        @Override
                        public void onEvent(NativeEvent event) {
                            if (event.getType().equals("error"))
                                listener.onFailure();
                            else
                                listener.onSuccess(image);
                        }
                    });
                    image.setSrc(getUrl());
                }

            });
        } else {
            final ImageElement image = createImage();
            hookImgListener(image, new ImgEventListener() {
                @Override
                public void onEvent(NativeEvent event) {
                    if (event.getType().equals("error"))
                        listener.onFailure();
                    else
                        listener.onSuccess(image);
                }
            });
            image.setSrc(getUrl());
        }
    }

    private interface ImgEventListener {
        void onEvent (NativeEvent event);
    }

    static native void hookImgListener (ImageElement img, ImgEventListener h) /*-{
        img.addEventListener(
            'load',
            function(e) {
                h.@org.parallax3d.parallax.platforms.gwt.system.assets.AssetFile.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
            }, false);
        img.addEventListener(
            'error',
            function(e) {
                h.@org.parallax3d.parallax.platforms.gwt.system.assets.AssetFile.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
            }, false);
    }-*/;

    // Need to get width and height values
    public static native ImageElement createImage () /*-{
        var imagestore = document.getElementById("imagestore-wrapper");
        if(!imagestore)
        {
            imagestore = document.createElement('div');
            imagestore.src= 'http://127.0.0.1:8080/parallax-tests-gwt/assets/textures/lensflare/lensflare3.png';
            imagestore.id = 'imagestore-wrapper';
            imagestore.style.visibility =  'hidden';
            imagestore.style.position =  'absolute';
            imagestore.style.width =  '1px';
            imagestore.style.height =  '1px';
            imagestore.style.overflow =  'hidden';
            document.body.appendChild(imagestore)
        }
        var image = new Image();
        imagestore.appendChild(image);
        return image;
    }-*/;

    private native static void setOnProgress (XMLHttpRequest req, FileListener listener) /*-{
        var _this = this;
        this.onprogress = $entry(function(evt) {
            listener.@org.parallax3d.parallax.files.FileListener::onProgress(D)(evt.loaded);
        });
    }-*/;

}
