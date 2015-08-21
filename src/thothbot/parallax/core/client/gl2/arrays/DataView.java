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

package thothbot.parallax.core.client.gl2.arrays;

import com.google.gwt.core.client.JavaScriptObject;

public final class DataView extends JavaScriptObject
{
	protected DataView() {
		
	}
	
	/**
	   * @param buffer
	   * @return a {@link DataView} instance
	   */
	  public static native DataView create(ArrayBuffer buffer) /*-{
	    return new $wnd.DataView(buffer);
	  }-*/;
	  
	  /**
	   * @param buffer
	   * @param byteOffset
	   * @return a {@link DataView} instance
	   */
	  public static native DataView create(ArrayBuffer buffer, int byteOffset) /*-{
	    return new $wnd.DataView(buffer, byteOffset);
	  }-*/;

	  /**
	   * @param buffer
	   * @param byteOffset
	   * @param byteLength
	   * @return a {@link DataView} instance
	   */
	  public static native DataView create(ArrayBuffer buffer, int byteOffset,
	      int byteLength) /*-{
	    return new $wnd.DataView(buffer, byteOffset, byteLength);
	  }-*/;
	  
	  public native int byteLength() /*-{
	    return this.byteLength;
	  }-*/;

	  public native float getFloat32(int byteOffset) /*-{
	    return this.getFloat32(byteOffset);
	  }-*/;

	  public native float getFloat32(int byteOffset, boolean littleEndian) /*-{
	    return this.getFloat32(byteOffset, littleEndian);
	  }-*/;

	  public native double getFloat64(int byteOffset) /*-{
	    return this.getFloat64(byteOffset);
	  }-*/;

	  public native double getFloat64(int byteOffset, boolean littleEndian) /*-{
	    return this.getFloat64(byteOffset, littleEndian);
	  }-*/;

	  public native short getInt16(int byteOffset) /*-{
	    return this.getInt16(byteOffset);
	  }-*/;

	  public native short getInt16(int byteOffset, boolean littleEndian) /*-{
	    return this.getInt16(byteOffset, littleEndian);
	  }-*/;

	  public native int getInt32(int byteOffset) /*-{
	    return this.getInt32(byteOffset);
	  }-*/;

	  public native int getInt32(int byteOffset, boolean littleEndian) /*-{
	    return this.getInt32(byteOffset, littleEndian);
	  }-*/;

	  public native byte getInt8(int byteOffset) /*-{
	    return this.getInt8(byteOffset);
	  }-*/;

	  public native int getUint16(int byteOffset) /*-{
	    return this.getUint16(byteOffset);
	  }-*/;

	  public native int getUint16(int byteOffset, boolean littleEndian) /*-{
	    return this.getUint16(byteOffset, littleEndian);
	  }-*/;

	  public int getUint32(int byteOffset) {
	    return (int) getUint32AsDouble(byteOffset);
	  }

	  public int getUint32(int byteOffset, boolean littleEndian) {
	    return (int) getUint32AsDouble(byteOffset, littleEndian);
	  }

	  public native double getUint32AsDouble(int byteOffset) /*-{
	    return this.getUint32(byteOffset);
	  }-*/;

	  public native double getUint32AsDouble(int byteOffset, boolean littleEndian) /*-{
	    return this.getUint32(byteOffset, littleEndian);
	  }-*/;

	  public native short getUint8(int byteOffset) /*-{
	    return this.getUint8(byteOffset);
	  }-*/;

	  public native void setFloat32(int byteOffset, float value) /*-{
	    this.setFloat32(byteOffset, value);
	  }-*/;

	  public native void setFloat32(int byteOffset, float value, boolean littleEndian) /*-{
	    this.setFloat32(byteOffset, value, littleEndian);
	  }-*/;

	  public native void setFloat64(int byteOffset, double value) /*-{
	    this.setFloat64(byteOffset, value);
	  }-*/;

	  public native void setFloat64(int byteOffset, double value, boolean littleEndian) /*-{
	    this.setFloat64(byteOffset, value, littleEndian);
	  }-*/;

	  public native void setInt16(int byteOffset, int value) /*-{
	    this.setInt16(byteOffset, value);
	  }-*/;

	  public native void setInt16(int byteOffset, int value, boolean littleEndian) /*-{
	    this.setInt16(byteOffset, value, littleEndian);
	  }-*/;

	  public native void setInt32(int byteOffset, int value) /*-{
	    this.setInt32(byteOffset, value);
	  }-*/;

	  public native void setInt32(int byteOffset, int value, boolean littleEndian) /*-{
	    this.setInt32(byteOffset, value, littleEndian);
	  }-*/;

	  public native void setInt8(int byteOffset, int value) /*-{
	    this.setInt8(byteOffset, value);
	  }-*/;

	  public native void setUint16(int byteOffset, int value) /*-{
	    this.setUint16(byteOffset, value);
	  }-*/;

	  public native void setUint16(int byteOffset, int value, boolean littleEndian) /*-{
	    this.setUint16(byteOffset, value, littleEndian);
	  }-*/;

	  public void setUint32(int byteOffset, long value) {
	    setUint32FromDouble(byteOffset, value);
	  }

	  public void setUint32(int byteOffset, long value, boolean littleEndian) {
	    setUint32FromDouble(byteOffset, value, littleEndian);
	  }

	  public native void setUint32FromDouble(int byteOffset, double value) /*-{
	    this.setUint32(byteOffset, value);
	  }-*/;

	  public native void setUint32FromDouble(int byteOffset, double value, boolean littleEndian) /*-{
	    this.setUint32(byteOffset, value, littleEndian);
	  }-*/;

	  public native void setUint8(int byteOffset, int value) /*-{
	    this.setUint8(byteOffset, value);
	  }-*/;
}

