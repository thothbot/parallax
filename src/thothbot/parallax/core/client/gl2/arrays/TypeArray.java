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

/**
 * The typed array view types represent a view of an {@link ArrayBuffer} that 
 * allows for indexing and manipulation.
 * 
 * @author hao1300@gmail.com
 */
public abstract class TypeArray extends ArrayBufferView {
	
	protected TypeArray() {
		
	}
	
	/**
   * Set multiple values, reading input values from the array. 
   * 
   * The input array and this array may use the same underlying 
   * {@link ArrayBuffer}. In this situation, setting the values takes place as 
   * if all the data is first copied into a temporary buffer that does not 
   * overlap either of the arrays, and then the data from the temporary buffer 
   * is copied into the current array.
   * 
   * @param array
   */
  public final native void set(TypeArray array) /*-{
		this.set(array);
	}-*/;
  
  /**
   * Set multiple values, reading input values from the array. 
   * 
   * The input array and this array may use the same underlying 
   * {@link ArrayBuffer}. In this situation, setting the values takes place as 
   * if all the data is first copied into a temporary buffer that does not 
   * overlap either of the arrays, and then the data from the temporary buffer 
   * is copied into the current array.
   * 
   * @param array
   * @param offset indicates the index in the current array where values are 
   * 				written.
   */
  public final native void set(TypeArray array, int offset) /*-{
  	this.set(array, offset);
  }-*/;
  
  /**
   * Gets the length of this array in elements.
   */
  public final native int getLength() /*-{
  	return this.length;
  }-*/;
  
  public final native int reverse() /*-{
	return this.reverse;
  }-*/;
}
