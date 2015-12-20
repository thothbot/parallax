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

package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;
import java.nio.Buffer;
import java.nio.FloatBuffer;

@ThreeJsObject("THREE.BufferAttribute")
public class BufferAttribute {
	
	private FloatBuffer array;
	private int itemSize;
	
	// TODO: Fix it (BufferGeometry)
	private int numItems;
	
	private boolean needsUpdate = false;
	private int buffer; //WebGLBuffer
	
	public BufferAttribute(FloatBuffer array, int itemSize) {
		this.array = array;
		this.itemSize = itemSize;
	}
	
	public int 	getLength () {

		return this.array.arrayOffset();

	}
	
	public int getItemSize() {
		return this.itemSize;
	}
	
	public Buffer getArray() {
		return this.array;
	}
	
	public void setArray(FloatBuffer array) {
		this.array = array;
	}
	
	/**
	 * @return the numItems
	 */
	public int getNumItems() {
		return numItems;
	}

	/**
	 * @param numItems the numItems to set
	 */
	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}

	/**
	 * @return the needsUpdate
	 */
	public boolean isNeedsUpdate() {
		return needsUpdate;
	}
	
	public void setNeedsUpdate(boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}

	/**
	 * @return the buffer
	 */
	public Integer  getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}
		
	public void copyAt( int index1, BufferAttribute attribute, int index2 ) {

		index1 *= this.itemSize;
		index2 *= attribute.itemSize;

		for ( int i = 0, l = this.itemSize; i < l; i ++ ) {

			((FloatBuffer)this.array).put(index1 + i, ((FloatBuffer) attribute.array).get(index2 + i));

		}

	}

	public BufferAttribute set( FloatBuffer value ) {

		this.array.put(value);

		return this;

	}
	
	public BufferAttribute setX( int index, float x ) {

		((FloatBuffer)this.array).put(index * this.itemSize, x);

		return this;

	}

	public BufferAttribute setY( int index, float y ) {

		((FloatBuffer)this.array).put(index * this.itemSize + 1, y);

		return this;

	}

	public BufferAttribute setZ( int index, float z ) {

		((FloatBuffer)this.array).put(index * this.itemSize + 2, z);

		return this;

	}

	public BufferAttribute setXY( int index, float x, float y ) {

		index *= this.itemSize;

		((FloatBuffer)this.array).put(index, x);
		((FloatBuffer)this.array).put(index + 1, y);

		return this;

	}

	public BufferAttribute setXYZ ( int index, float x, float y, float z ) {

		index *= this.itemSize;

		((FloatBuffer)this.array).put(index, x);	
		((FloatBuffer)this.array).put(index + 1, y);
		((FloatBuffer)this.array).put(index + 2, z);

		return this;

	}

	public BufferAttribute setXYZW ( int index, float x, float y, float z, float w ) {

		index *= this.itemSize;

		((FloatBuffer)this.array).put(index, x);
		((FloatBuffer)this.array).put(index + 1, y);
		((FloatBuffer)this.array).put(index + 2, z);
		((FloatBuffer)this.array).put(index + 3, w);

		return this;

	}

//	public BufferAttribute clone() {
//
//		return new BufferAttribute( FloatBuffer.create(this.array),  this.itemSize );
//
//	}

	public String toString() {
		return "{array: " + this.array.arrayOffset() 
				+ ", itemSize: " + this.itemSize 
				+ ", needsUpdate: " + this.isNeedsUpdate() 
				+ ", buffer: " + this.getBuffer() + "}";
	}
}
