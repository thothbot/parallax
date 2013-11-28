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
 * The ArrayBufferView type holds information shared among all of the types of 
 * views of {@link ArrayBuffer}s.
 * 
 * @author hao1300@gmail.com
 */
public abstract class ArrayBufferView extends JavaScriptObject {

	protected ArrayBufferView() {
		
	}
	
	/**
	 * @return The {@link ArrayBuffer} that this ArrayBufferView references.
	 */
	public final native ArrayBuffer getBuffer() /*-{
		return this.buffer;
	}-*/;
	
	/**
	 * @return The offset of this ArrayBufferView from the start of its 
	 * 				 {@link ArrayBuffer}, in bytes, as fixed at construction time.
	 */
	public final native int getByteOffset() /*-{
		return this.byteOffset;
	}-*/;
	
	/**
	 * @return The length of the ArrayBufferView in bytes, as fixed at 
	 * 				 construction time.
	 */
	public final native int getByteLength() /*-{
		return this.byteLength;
	}-*/;  
}