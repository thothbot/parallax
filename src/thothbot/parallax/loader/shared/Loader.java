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

import thothbot.parallax.core.client.gl2.arrays.ArrayBuffer;
import thothbot.parallax.core.shared.Log;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.google.gwt.xhr.client.XMLHttpRequest.ResponseType;

public abstract class Loader 
{
	public interface ModelLoadHandler 
	{
		public void onModelLoaded();
	}
	
	private String texturePath;
	private ResponseType responseType = ResponseType.Default;
	
	public void load(final String url, final ModelLoadHandler modelLoadHandler) throws RequestException 
	{
		texturePath = extractUrlBase(url);
		
		final XMLHttpRequest request = XMLHttpRequest.create();
		request.open( "GET", url );
		request.setResponseType(this.responseType);
		request.send( null );

		request.setOnReadyStateChange(new ReadyStateChangeHandler() {
			
			@Override
			public void onReadyStateChange(XMLHttpRequest xhr) {
				if(xhr.getReadyState() == XMLHttpRequest.DONE) 
				{
					if (xhr.getStatus() >= 400) {
						Log.error("Error while loading file: " + url);	
					}
					else
					{
						if(xhr.getResponseType().equals("arraybuffer")) {
							com.google.gwt.typedarrays.shared.ArrayBuffer origin = xhr.getResponseArrayBuffer(); 

							parse(ArrayBuffer.copy(origin));
						} else
							parse(xhr.getResponseText());

						modelLoadHandler.onModelLoaded();						
					}
					
					request.clearOnReadyStateChange();
					request.abort();
				}
			}
		});
	}
	
	public abstract void parse(String string);
	public void parse(ArrayBuffer buffer) {
		
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
