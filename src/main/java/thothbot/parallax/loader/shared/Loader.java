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

import thothbot.parallax.core.shared.Log;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public abstract class Loader 
{
	public interface ModelLoadHandler 
	{
		public void onModelLoaded();
	}
	
	private String texturePath;
	
	public void load(final String url, final ModelLoadHandler modelLoadHandler) throws RequestException 
	{
		texturePath = extractUrlBase(url);
		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url);
		rb.sendRequest(null, new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) 
			{
				parse(response.getText());
				modelLoadHandler.onModelLoaded();
			}

			@Override
			public void onError(Request request, Throwable exception) 
			{
				Log.error("Error while loading file: " + url);
			}
		});
	}
	
	public abstract void parse(String string); 
	
	public String getTexturePath() {
		return this.texturePath;
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
