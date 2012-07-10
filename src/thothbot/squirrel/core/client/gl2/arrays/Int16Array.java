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
package thothbot.squirrel.core.client.gl2.arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayInteger;

/**
 * The typed array that holds short (16-bit 2's complement signed integer) as its element.
 * 
 * @author hao1300@gmail.com
 */
public final class Int16Array extends TypeArray {
	public static final int BYTES_PER_ELEMENT = 2;
	
	protected Int16Array() {
		
	}
	
	/**
	 * Create a new {@link ArrayBuffer} with enough bytes to hold length elements 
	 * of this typed array, then creates a typed array view referring to the full 
	 * buffer.
	 * 
	 * @param length
	 */
	public static native Int16Array create(int length) /*-{
		return new $wnd.Int16Array(length);
	}-*/;
	
	/**
	 * Create a new {@link ArrayBuffer} with enough bytes to hold array.length 
	 * elements of this typed array, then creates a typed array view referring 
	 * to the full buffer. The contents of the new view are initialized to the 
	 * contents of the given typed array or sequence, with each element converted 
	 * to the appropriate typed array type.
	 * 
	 * @param array
	 */
	public static native Int16Array create(TypeArray array) /*-{
		return new $wnd.Int16Array(array);
	}-*/;
	
	/**
	 * Create a new {@link ArrayBuffer} with enough bytes to hold array.length 
	 * elements of this typed array, then creates a typed array view referring 
	 * to the full buffer. The contents of the new view are initialized to the 
	 * contents of the given typed array or sequence, with each element converted 
	 * to the appropriate typed array type.
	 * 
	 * @param array
	 */
	public static Int16Array create(int... array) {
		if (GWT.isScript()) {
			return createCompiled(array);
		}
		return create(JsArrayUtil.toJsArrayInteger(array));
	}
	
	private static native Int16Array createCompiled(int[] array) /*-{
		return new $wnd.Int16Array(array);
	}-*/;
	
	/**
	 * Create a new {@link ArrayBuffer} with enough bytes to hold array.length 
	 * elements of this typed array, then creates a typed array view referring 
	 * to the full buffer. The contents of the new view are initialized to the 
	 * contents of the given typed array or sequence, with each element converted 
	 * to the appropriate typed array type.
	 * 
	 * @param array
	 */
	public static native Int16Array create(JsArrayInteger array) /*-{
		return new $wnd.Int16Array(array);
	}-*/;
	
	/**
	 * Create a new Int16Array object using the passed {@link ArrayBuffer} for 
	 * its storage. The Int16Array spans the entire {@link ArrayBuffer} range. 
	 * 
	 * @param buffer
	 */
	public static native Int16Array create(ArrayBuffer buffer) /*-{
		return new $wnd.Int16Array(buffer);
	}-*/;
	
	/**
	 * Create a new Int16Array object using the passed {@link ArrayBuffer} for 
	 * its storage. The Int16Array extends from the given byteOffset until the 
	 * end of the {@link ArrayBuffer}.
	 * 
	 * The given byteOffset must be a multiple of the element size of the 
	 * specific type, otherwise an INDEX_SIZE_ERR exception is raised.
	 * 
	 * If a given byteOffset references an area beyond the end of the 
	 * {@link ArrayBuffer} an INDEX_SIZE_ERR exception is raised.
	 * 
	 * The length of the {@link ArrayBuffer} minus the byteOffset must be a 
	 * multiple of the element size of the specific type, or an INDEX_SIZE_ERR 
	 * exception is raised.
	 * 
	 * @param buffer
	 * @param byteOffset indicates the offset in bytes from the start of the 
	 * 				{@link ArrayBuffer} 
	 */
	public static native Int16Array create(ArrayBuffer buffer, int byteOffset) /*-{
		return new $wnd.Int16Array(buffer, byteOffset);
	}-*/;
	
	/**
	 * Create a new Int16Array object using the passed {@link ArrayBuffer} for 
	 * its storage. 
	 * 
	 * The given byteOffset must be a multiple of the element size of the 
	 * specific type, otherwise an INDEX_SIZE_ERR exception is raised.
	 * 
	 * If a given byteOffset and length references an area beyond the end of the 
	 * {@link ArrayBuffer} an INDEX_SIZE_ERR exception is raised.
	 * 
	 * @param buffer
	 * @param byteOffset indicates the offset in bytes from the start of the 
	 * 				{@link ArrayBuffer} 
	 * @param length the count of elements from the offset that this 
	 * 				Int16Array will reference
	 */
	public static native Int16Array create(ArrayBuffer buffer, int byteOffset,
			int length) /*-{
		return new $wnd.Int16Array(buffer, byteOffset, length);
	}-*/;
  
	/**
	 * Returns the element at the given numeric index.
	 * 
	 * @param index
	 */
  public native int get(int index) /*-{
  	return this[index];
  }-*/;
  
  /**
   * Sets the element at the given numeric index to the given value.
   * 
   * @param index
   * @param value
   */
  public native void set(int index, int value) /*-{
  	 this[index] = value;
  }-*/;
  
  /**
   * Set multiple values, reading input values from the array. 
   */
  public native void set(JsArrayInteger array) /*-{
  	this.set(array);
  }-*/;
  
  /**
   * /**
   * Set multiple values, reading input values from the array.
   * 
   * @param array
   * @param offset indicates the index in the current array where values are 
   * 				written.
   */
  public native void set(JsArrayInteger array, int offset) /*-{
  	this.set(array, offset);
  }-*/;
  
  /**
   * Returns a new Int16Array view of the {@link ArrayBuffer} store for this 
   * Int16Array, referencing the elements at begin, inclusive, up to end, 
   * exclusive. If either begin or end is negative, it refers to an index from 
   * the end of the array, as opposed to from the beginning.
   * 
   * The slice contains all elements from begin to the end of the Int16Array.
   * 
   * The range specified by the begin and end values is clamped to the valid 
   * index range for the current array. If the computed length of the new 
   * Int16Array would be negative, it is clamped to zero.
   * 
   * The returned Int16Array will be of the same type as the array on which this 
   * method is invoked.
   * 
   * @param begin
   */
  public native Int16Array slice(int begin) /*-{
		return this.slice(begin);
	}-*/;
  
  /**
   * Returns a new Int16Array view of the {@link ArrayBuffer} store for this 
   * Int16Array, referencing the elements at begin, inclusive, up to end, 
   * exclusive. If either begin or end is negative, it refers to an index from 
   * the end of the array, as opposed to from the beginning.
   * 
   * The slice contains all elements from begin to the end of the Int16Array.
   * 
   * The range specified by the begin and end values is clamped to the valid 
   * index range for the current array. If the computed length of the new 
   * Int16Array would be negative, it is clamped to zero.
   * 
   * The returned Int16Array will be of the same type as the array on which this 
   * method is invoked.
   * 
   * @param begin
   * @param end
   */
  public native Int16Array slice(int begin, int end) /*-{
  	return this.slice(begin, end);
  }-*/;
}
