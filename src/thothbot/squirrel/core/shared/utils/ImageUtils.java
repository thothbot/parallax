/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.utils;

import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.textures.Texture;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/**
 * This class implements some Image-related helper methods.
 * 
 * @author thothbot
 *
 */
public final class ImageUtils 
{
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
	 * @param mapping  the mapping mode {@link Texture.MAPPING_MODE}. Not necessary.
	 * @param callback the {@link ImageUtils.Callback}. Not necessary.
	 * 
	 * @return the new instance of {@link Texture}
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
	 * @param mapping        the mapping mode {@link Texture.MAPPING_MODE}. Not necessary.
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
	 * @param mapping  the mapping mode {@link Texture.MAPPING_MODE}. Not necessary.
	 * @param callback the {@link ImageUtils.Callback}. Not necessary.
	 * 
	 * @return the new instance of {@link Texture}
	 */
	public static Texture loadTexture(Image image, Texture.MAPPING_MODE mapping, final Callback callback)
	{
		final Texture texture = new Texture(image, mapping);
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
				if (callback != null){
					callback.run((Image)event.getSource());
				}
			}
		});

//		RootPanel.get().add(image);

		return texture;
	}
	
//	public static Texture loadTextureCube(List<String> paths, Texture.MAPPING_MODE mapping, final Callback callback)
//	{
//		List<Image> images = new ArrayList<Image>();
//		final Texture texture = new Texture(images, mapping);
//		
//		LoadHandler loadHandler = new LoadHandler() {
//			int loaded = 0;
//			@Override
//			public void onLoad(LoadEvent event) {
//				loaded++;
//				if (loaded == 6){
//					texture.setNeedsUpdate(true);
//				}
//				if (callback != null){
//					callback.run((Image)event.getSource());
//				}
//			}
//		};
//		
//		for(String path : paths)
//		{
//			Image img = new Image(); 
//			images.add(img);
//			img.addLoadHandler(loadHandler);
//			img.setUrl(path);
//		}
//		
//		return texture;
//	}
}
