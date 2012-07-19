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

import thothbot.parallax.core.client.textures.CubeTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

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
        RootLayoutPanel.get().add(loadingArea);
	}
	
	/**
	 * This callback will be called when the image has been loaded.
	 */
	public static interface Callback 
	{
		void run(Image image);
	}
	
	/**
	 * Loading of texture.
	 * 
	 * @param path     the string path to the image
	 * @param mapping  the mapping mode {@link thothbot.parallax.core.client.textures.Texture.MAPPING_MODE}. Not necessary.
	 * @param callback the {@link ImageUtils.Callback}. Not necessary.
	 * 
	 * @return the new instance of {@link thothbot.parallax.core.client.textures.Texture}
	 */
	public static Texture loadTexture(String path, Texture.MAPPING_MODE mapping, final Callback callback)
	{
		Image image = new Image();
		image.setUrl(path);

		return ImageUtils.loadTexture(image, mapping, callback);
	}
	
	/**
	 * Loading of texture.
	 * 
	 * @param imageResource  the {@link ImageResource} instance
	 * @param mapping        the mapping mode {@link thothbot.parallax.core.client.textures.Texture.MAPPING_MODE}. Not necessary.
	 * @param callback       the {@link ImageUtils.Callback}. Not necessary.
	 * 
	 * @return the new instance of {@link Texture}
	 */
	public static Texture loadTexture(ImageResource imageResource, Texture.MAPPING_MODE mapping, final Callback callback)
	{
		Image image = new Image();
		image.setUrl(imageResource.getSafeUri());
		return ImageUtils.loadTexture(image, mapping, callback);
	}

	/**
	 * Loading of texture.
	 * 
	 * @param image    the instance of {@link Image}
	 * @param mapping  the mapping mode {@link thothbot.parallax.core.client.textures.Texture.MAPPING_MODE}. Not necessary.
	 * @param callback the {@link ImageUtils.Callback}. Not necessary.
	 * 
	 * @return the new instance of {@link thothbot.parallax.core.client.textures.Texture}
	 */
	public static Texture loadTexture(final Image image, Texture.MAPPING_MODE mapping, final Callback callback)
	{
		loadingArea.add(image);
		Texture texture = new Texture(image.getElement(), mapping);
		texture.setNeedsUpdate(true);

	    // Hook up an error handler, so that we can be informed if the image fails
	    // to load.
		image.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event)
			{
				Log.error("An error occurred while loading image.");
			}
		});

		image.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) 
			{			
				if (callback != null)
					callback.run((Image)event.getSource());
			}
		});

		return texture;
	}
	
	public static CubeTexture loadTextureCube(List<ImageResource> imageResources, Texture.MAPPING_MODE mapping, final Callback callback)
	{
		List<Element> images = new ArrayList<Element>();
		for(ImageResource ir: imageResources)
		{
			final Image image = new Image();
			loadingArea.add(image);

			image.setUrl(ir.getSafeUri());
			image.addErrorHandler(new ErrorHandler() {
				
				@Override
				public void onError(ErrorEvent event)
				{
					Log.error("An error occurred while loading image.");
				}
			});
			
			image.addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(LoadEvent event) 
				{		
					if (callback != null)
						callback.run((Image)event.getSource());
				}
			});
			
			images.add(image.getElement());
		}
		
		CubeTexture texture = new CubeTexture(images, mapping);
		texture.setNeedsUpdate(true);
				
		return texture;
	}
}
