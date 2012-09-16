/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.utils;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.Uint8Array;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.textures.CubeTexture;
import thothbot.parallax.core.client.textures.DataTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Color;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This class implements some Image-related helper methods.
 * 
 * @author thothbot
 *
 */
public final class ImageUtils 
{
	private static FlowPanel loadingArea = new FlowPanel();
	static {
		loadingArea.getElement().getStyle().setProperty("visibility", "hidden");
        loadingArea.getElement().getStyle().setProperty("position", "absolute");
        loadingArea.getElement().getStyle().setProperty("width", "1px");
        loadingArea.getElement().getStyle().setProperty("height", "1px");
        loadingArea.getElement().getStyle().setProperty("overflow", "hidden");
        RootPanel.get().add(loadingArea);
	}
	
	/**
	 * This callback will be called when the image has been loaded.
	 */
	public static interface ImageLoadHandler 
	{
		void onImageLoad(Texture texture);
	}
	
	private static int loadedCount;
	private static interface Loader 
	{
		void onLoad();
	}
	
	public static Texture loadTexture(ImageResource imageResource)
	{
		return ImageUtils.loadTexture(imageResource.getSafeUri().asString());
	}
	
	public static Texture loadTexture(ImageResource imageResource, Texture.MAPPING_MODE mapping)
	{
		return ImageUtils.loadTexture(imageResource.getSafeUri().asString(), mapping);
	}
	
	public static Texture loadTexture(String url)
	{
		return ImageUtils.loadTexture(url, Texture.MAPPING_MODE.UV);
	}
	
	public static Texture loadTexture(String url, Texture.MAPPING_MODE mapping)
	{
		return ImageUtils.loadTexture(url, mapping, null);
	}

	public static Texture loadTexture(String url, Texture.MAPPING_MODE mapping, ImageLoadHandler imageLoadHandler)
	{
		Image image = new Image();
		image.setUrl(url);

		return ImageUtils.loadTexture(image, mapping, imageLoadHandler);
	}
	
	/**
	 * Loading of texture.
	 * 
	 * @param image          the Image
	 * @param mapping        the mapping mode {@link thothbot.parallax.core.client.textures.Texture.MAPPING_MODE}. Not necessary.
	 * @param imageLoadHandler       the {@link ImageUtils.ImageLoadHandler}. Not necessary.
	 * 
	 * @return the new instance of {@link thothbot.parallax.core.client.textures.Texture}
	 */
	public static Texture loadTexture(Image image, Texture.MAPPING_MODE mapping, final ImageLoadHandler imageLoadHandler)
	{	
		final Texture texture = new Texture(image.getElement(), mapping);

		loadImage(image, texture, new Loader() {
			
			@Override
			public void onLoad() {
				
				texture.setNeedsUpdate(true);
				if (imageLoadHandler != null)
					imageLoadHandler.onImageLoad(texture);
			}
		});

		return texture;
	}
	
	public static CubeTexture loadTextureCube(String url)
	{
		return ImageUtils.loadTextureCube(url, Texture.MAPPING_MODE.UV);
	}
	
	public static CubeTexture loadTextureCube(String url, Texture.MAPPING_MODE mapping)
	{
		return ImageUtils.loadTextureCube(url, mapping, null);
	}

	public static CubeTexture loadTextureCube(String url, Texture.MAPPING_MODE mapping, ImageLoadHandler imageLoadHandler)
	{
		List<Image> images = new ArrayList<Image>();
		
		String[] parts = {"px", "nx", "py", "ny", "pz", "nz"};
		String urlStart = url.substring(0, url.indexOf("*"));
		String urlEnd = url.substring(url.indexOf("*") + 1, url.length());
		
		for(String part: parts)
		{
			Image image = new Image();
			image.setUrl(urlStart + part + urlEnd);
			images.add(image);
		}

		return ImageUtils.loadTextureCube(images, mapping, imageLoadHandler);
	}
	
	public static CubeTexture loadTextureCube(List<Image> images, Texture.MAPPING_MODE mapping, final ImageLoadHandler imageLoadHandler)
	{
		List<Element> elements = new ArrayList<Element>();
		final CubeTexture texture = new CubeTexture(elements, mapping);
		texture.setFlipY(false);
	
		loadedCount = 0;
		for(Image image: images)
		{
			loadImage(image, texture, new Loader() {
				
				@Override
				public void onLoad() {
					if(++loadedCount == 6)
					{
						texture.setNeedsUpdate(true);
						if (imageLoadHandler != null)
							imageLoadHandler.onImageLoad(texture);
					}
				}
			});
			elements.add(image.getElement());
		}

		return texture;
	}
	
	private static void loadImage(final Image image, final Texture texture, final Loader loader)
	{
		loadingArea.add(image);
		
	    // Hook up an error handler, so that we can be informed if the image fails
	    // to load.
		image.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event)
			{
				Log.error("An error occurred while loading image: " + image.getUrl());
			}
		});

		image.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) 
			{			
				Log.info("Loaded image: " + image.getUrl());
				loader.onLoad();
			}
		});
	}
	
	public static DataTexture generateDataTexture( int width, int height, Color color ) 
	{
		int size = width * height;
		Uint8Array data = Uint8Array.create( 3 * size );

		int r = (int)Math.floor( color.getR() * 255 );
		int g = (int)Math.floor( color.getG() * 255 );
		int b = (int)Math.floor( color.getB() * 255 );

		for ( int i = 0; i < size; i ++ ) 
		{
			data.set( i * 3, r);
			data.set( i * 3 + 1, g);
			data.set( i * 3 + 2, b);

		}

		DataTexture texture = new DataTexture( data, width, height, PixelFormat.RGB );
		texture.setNeedsUpdate(true);

		return texture;
	}
}
