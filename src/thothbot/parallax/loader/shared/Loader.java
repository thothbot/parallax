/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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
 * Squirrel. If not, see http://www.gnu.org/licenses/.
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
	public interface Callback 
	{
		public void onLoaded();
	}
	
	private String texturePath;
	
	public void load(final String url, final Callback callback) throws RequestException 
	{
		texturePath = extractUrlBase(url);
		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url);
		rb.sendRequest(null, new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) 
			{
				parse(response.getText());
				callback.onLoaded();
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
		String part = url.substring(0, url.lastIndexOf('/'));
		return ( part.length() < 1 ? "." : part ) + '/';
	}
}
