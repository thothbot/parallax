/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.parallax3d.parallax.platforms.gwt.system.preloader;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.typedarrays.shared.Int8Array;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.gwt.xhr.client.XMLHttpRequest.ResponseType;
import org.parallax3d.parallax.files.AssetFilter;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

public class AssetDownloader {

	public AssetDownloader () {
		useInlineBase64 = false;
	}

	public void setUseInlineBase64 (boolean useInlineBase64) {
		this.useInlineBase64 = useInlineBase64;
	}

	public boolean isUseInlineBase64 () {
		return useInlineBase64;
	}

	public void load (String url, AssetFilter.AssetType type, String mimeType, FileListener<?> listener) {
		switch (type) {
		case Text:
			loadText(url, (FileListener<String>)listener);
			break;
		case Image:
			loadImage(url, mimeType, (FileListener<ImageElement>)listener);
			break;
		case Binary:
			loadBinary(url, (FileListener<Blob>)listener);
			break;
		case Audio:
			loadAudio(url, (FileListener<Void>)listener);
			break;
		case Directory:
			listener.onSuccess(null);
			break;
		default:
			throw new ParallaxRuntimeException("Unsupported asset type " + type);
		}
	}

	public void loadText (String url, final FileListener<String> listener) {
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
		request.open("GET", url);
		request.setRequestHeader("Content-Type", "text/plain; charset=utf-8");
		request.send();
	}

	public void loadBinary (final String url, final FileListener<Blob> listener) {
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
		request.open("GET", url);
		request.setResponseType(ResponseType.ArrayBuffer);
		request.send();
	}

	public void loadAudio (String url, final FileListener<Void> listener) {
		if (useBrowserCache) {
			loadBinary(url, new FileListener<Blob>() {
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

	public void loadImage (final String url, final String mimeType, final FileListener<ImageElement> listener) {
		if (useBrowserCache || useInlineBase64) {
			loadBinary(url, new FileListener<Blob>() {
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
					if (isUseInlineBase64()) {
						image.setSrc("data:" + mimeType + ";base64," + result.toBase64());
					} else {
						image.setSrc(url);
					}
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
			image.setSrc(url);
		}
	}

	private interface ImgEventListener {
		void onEvent (NativeEvent event);
	}

	static native void hookImgListener (ImageElement img, ImgEventListener h) /*-{
		img.addEventListener(
			'load',
			function(e) {
				h.@org.parallax3d.parallax.platforms.gwt.system.preloader.AssetDownloader.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
			}, false);
		img.addEventListener(
			'error',
			function(e) {
				h.@org.parallax3d.parallax.platforms.gwt.system.preloader.AssetDownloader.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
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

	private boolean useBrowserCache;

	private boolean useInlineBase64;

}
