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

package thothbot.parallax.loader.shared;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.ArrayBuffer;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.AbstractGeometry;

import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.gwt.xhr.client.XMLHttpRequest.ResponseType;

public abstract class XHRLoader 
{
	private static List<ModelLoadHandler> loadHandlers = new ArrayList<ModelLoadHandler>();
	private static LoaderProgressHandler loaderProgressHandler;
	public static void addLoaderProgress(LoaderProgressHandler loaderProgressHandler) 
	{
		XHRLoader.loaderProgressHandler = loaderProgressHandler;
	}

	public interface LoaderProgressHandler
	{
		public void onProgressUpdate(int left);
	}
	
	public interface ModelLoadHandler 
	{
		public void onModelLoaded(XHRLoader loader, AbstractGeometry geometry);
	}

	private String url;
	private String texturePath;
	private ModelLoadHandler modelLoadHandler;
	private ResponseType responseType = ResponseType.Default;
	
	public XHRLoader(String url, ModelLoadHandler modelLoadHandler) 
	{
		this.url = url;
		this.texturePath = extractUrlBase(url);
		
		this.modelLoadHandler = modelLoadHandler;
	}

	protected void load() 
	{		
		final XMLHttpRequest request = XMLHttpRequest.create();
		request.open( "GET", url );
		request.setResponseType(this.responseType);
		request.send( null );
		
		loadHandlers.add(modelLoadHandler);
		
		if(XHRLoader.loaderProgressHandler != null)
			XHRLoader.loaderProgressHandler.onProgressUpdate(loadHandlers.size());

		request.setOnReadyStateChange(new ReadyStateChangeHandler() {
			
			@Override
			public void onReadyStateChange(XMLHttpRequest xhr) {
				if(xhr.getReadyState() == XMLHttpRequest.DONE) 
				{
					if (xhr.getStatus() >= 400) 
					{
						Log.error("Error while loading file: " + url + ", status: " + xhr.getStatus());	
					}
					else
					{
						AbstractGeometry geometry = null;
						
						if(xhr.getResponseType().equals("arraybuffer")) {
							com.google.gwt.typedarrays.shared.ArrayBuffer origin = xhr.getResponseArrayBuffer(); 

							geometry = parse(ArrayBuffer.copy(origin));
						} else
							geometry = parse(xhr.getResponseText());

						modelLoadHandler.onModelLoaded(XHRLoader.this, geometry);						
					}
					
					request.clearOnReadyStateChange();
					request.abort();
					
					loadHandlers.remove(modelLoadHandler);
					if(XHRLoader.loaderProgressHandler != null)
						XHRLoader.loaderProgressHandler.onProgressUpdate(loadHandlers.size());
				}
			}
		});
	}
	
	protected abstract AbstractGeometry parse(String string);
	protected AbstractGeometry parse(ArrayBuffer buffer) {
		return null;
	}
	
	public String getTexturePath() {
		return this.texturePath;
	}
	
	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}
	
	private String extractUrlBase( String url ) 
	{
		int i = url.lastIndexOf('/');
		if (i >= 0)
		{
			return url.substring(0, i) + '/';
		}
		else
		{
			return "";
		}
	}
}
