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
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.core;

import thothbot.parallax.core.client.gl2.arrays.ArrayBuffer;

import com.google.gwt.xhr.client.XMLHttpRequest;

public final class XMLHttpRequestBinary extends XMLHttpRequest 
{
	protected XMLHttpRequestBinary() {
		
	}

	/**
	 * Get the response as an {@link ArrayBuffer}.
	 * 
	 * @return an {@link ArrayBuffer} containing the response, or null if the
	 *     request is in progress or failed
	 */
	public final native ArrayBuffer getResponseArrayBuffer() /*-{
	    return this.response;
	}-*/;
	
	/**
	 * Sets response type like to "arraybuffer" or "blob" for binary response
	 * @param value
	 */ 
    public native void setResponseType(String value) /*-{
       this.responseType = value;
    }-*/;
}
