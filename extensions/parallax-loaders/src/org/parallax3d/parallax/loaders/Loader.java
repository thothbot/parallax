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

import org.parallax3d.parallax.graphics.core.AbstractGeometry;

import java.util.ArrayList;
import java.util.List;

public abstract class Loader
{
	private static List<ModelLoadHandler> loadHandlers = new ArrayList<ModelLoadHandler>();
	private static LoaderProgressHandler loaderProgressHandler;
	public static void addLoaderProgress(LoaderProgressHandler loaderProgressHandler)
	{
		Loader.loaderProgressHandler = loaderProgressHandler;
	}

	public interface LoaderProgressHandler
	{
		public void onProgressUpdate(int left);
	}

	public interface ModelLoadHandler
	{
		public void onModelLoaded(Loader loader, AbstractGeometry geometry);
	}

	private String url;
	private String texturePath;
	private ModelLoadHandler modelLoadHandler;

	public Loader(String url, ModelLoadHandler modelLoadHandler)
	{
		this.url = url;
		this.texturePath = extractUrlBase(url);
		
		this.modelLoadHandler = modelLoadHandler;
	}

	protected abstract AbstractGeometry parse(String string);

	public String getTexturePath() {
		return this.texturePath;
	}

	private String extractUrlBase( String url ) 
	{
		int i = url.lastIndexOf('/');
		return (i >= 0) ? url.substring(0, i) + '/' : "";
	}
}
