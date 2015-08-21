/*
 * Copyright 2009-2011 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package thothbot.parallax.core.client.gl2.arrays;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The ArrayBuffer type describes a buffer used to store data for the 
 * TypedArray interface and its subclasses
 * 
 * @author hao1300@gmail.com
 */
public final class ArrayBuffer extends JavaScriptObject {

	protected ArrayBuffer() {
		
	}
	
	/**
	 * Creates a new ArrayBuffer of the given length in bytes. The contents of 
	 * the ArrayBuffer are initialized to 0.
	 * 
	 * @param length number of bytes
	 * @return the new ArrayBuffer
	 */
	public static native ArrayBuffer create(int length) /*-{
		return new $wnd.ArrayBuffer(length);		
	}-*/;
	
	public static native ArrayBuffer copy(com.google.gwt.typedarrays.shared.ArrayBuffer origin) /*-{
		if (!ArrayBuffer.prototype.slice)
  		{
			ArrayBuffer.prototype.slice = function (start, end) {
		        var that = new Uint8Array(this);
		        if (end == undefined) end = that.length;
		        var result = new ArrayBuffer(end - start);
		        var resultArray = new Uint8Array(result);
		        for (var i = 0; i < resultArray.length; i++)
		           resultArray[i] = that[i + start];
		        return result;
		    }
  		}

		return origin.slice(0);
	}-*/;
	
	public static native ArrayBuffer slice(int start, int end) /*-{
		if (!ArrayBuffer.prototype.slice)
		{
			ArrayBuffer.prototype.slice = function (start, end) {
		        var that = new Uint8Array(this);
		        if (end == undefined) end = that.length;
		        var result = new ArrayBuffer(end - start);
		        var resultArray = new Uint8Array(result);
		        for (var i = 0; i < resultArray.length; i++)
		           resultArray[i] = that[i + start];
		        return result;
		    }
		}

		return this.slice(start, end);
	}-*/;
	
	/**
	 * The length of the ArrayBuffer in bytes, as fixed at construction time.
	 */
	public native int getByteLength() /*-{
		return this.byteLength;
	}-*/;
	
    public native byte get(int index) /*-{
	  	return this[index];
	}-*/;
}
