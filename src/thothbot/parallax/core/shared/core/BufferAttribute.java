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

package thothbot.parallax.core.shared.core;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.TypeArray;

public class BufferAttribute {
	
	private TypeArray array;
	private int itemSize;
	
	// TODO: Fix it (BufferGeometry)
	private int numItems;
	
	private boolean needsUpdate = false;
	private WebGLBuffer buffer;
	
	public BufferAttribute(TypeArray array, int itemSize) {
		this.array = array;
		this.itemSize = itemSize;
	}
	
	public int 	getLength () {

		return this.array.getLength();

	}
	
	public int getItemSize() {
		return this.itemSize;
	}
	
	public TypeArray getArray() {
		return this.array;
	}
	
	public void setArray(Float32Array array) {
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
	public WebGLBuffer getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(WebGLBuffer buffer) {
		this.buffer = buffer;
	}
		
	public void copyAt( int index1, BufferAttribute attribute, int index2 ) {

		index1 *= this.itemSize;
		index2 *= attribute.itemSize;

		for ( int i = 0, l = this.itemSize; i < l; i ++ ) {

			((Float32Array)this.array).set( index1 + i , ((Float32Array)attribute.array).get( index2 + i ) );

		}

	}

	public BufferAttribute set( Float32Array value ) {

		this.array.set( value );

		return this;

	}
	
	public BufferAttribute setX( int index, double x ) {

		((Float32Array)this.array).set( index * this.itemSize , x );

		return this;

	}

	public BufferAttribute setY( int index, double y ) {

		((Float32Array)this.array).set( index * this.itemSize + 1 , y );

		return this;

	}

	public BufferAttribute setZ( int index, double z ) {

		((Float32Array)this.array).set( index * this.itemSize + 2 , z );

		return this;

	}

	public BufferAttribute setXY( int index, double x, double y ) {

		index *= this.itemSize;

		((Float32Array)this.array).set( index     , x);
		((Float32Array)this.array).set( index + 1 , y );

		return this;

	}

	public BufferAttribute setXYZ ( int index, double x, double y, double z ) {

		index *= this.itemSize;

		((Float32Array)this.array).set( index     , x );	
		((Float32Array)this.array).set( index + 1 , y );
		((Float32Array)this.array).set( index + 2 , z );

		return this;

	}

	public BufferAttribute setXYZW ( int index, double x, double y, double z, double w ) {

		index *= this.itemSize;

		((Float32Array)this.array).set( index     , x );
		((Float32Array)this.array).set( index + 1 , y );
		((Float32Array)this.array).set( index + 2 , z );
		((Float32Array)this.array).set( index + 3 , w );

		return this;

	}

	public BufferAttribute clone() {

		return new BufferAttribute( Float32Array.create(this.array),  this.itemSize );

	}

	public String toString() {
		return "{array: " + this.array.getLength() 
				+ ", itemSize: " + this.itemSize 
				+ ", needsUpdate: " + this.isNeedsUpdate() 
				+ ", buffer: " + this.getBuffer() + "}";
	}
}
