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

public class BufferAttribute {
	
	private Float32Array array;
	private int itemSize;
	
	// TODO: Fix it (BufferGeometry)
	public int numItems;
	
	public boolean needsUpdate = false;
	public WebGLBuffer buffer;
	
	public BufferAttribute(Float32Array array, int itemSize) {
		this.array = array;
		this.itemSize = itemSize;
	}
	
	public int 	getLength () {

		return this.array.getLength();

	}
	
	public int getItemSize() {
		return this.itemSize;
	}
	
	public Float32Array getArray() {
		return this.array;
	}
	
	public void setArray(Float32Array array) {
		this.array = array;
	}
	
	public void setNeedsUpdate(boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
	}
	
	public void copyAt( int index1, BufferAttribute attribute, int index2 ) {

		index1 *= this.itemSize;
		index2 *= attribute.itemSize;

		for ( int i = 0, l = this.itemSize; i < l; i ++ ) {

			this.array.set( index1 + i , attribute.array.get( index2 + i ) );

		}

	}

	public BufferAttribute set( Float32Array value ) {

		this.array.set( value );

		return this;

	}
	
	public BufferAttribute setX( int index, double x ) {

		this.array.set( index * this.itemSize , x );

		return this;

	}

	public BufferAttribute setY( int index, double y ) {

		this.array.set( index * this.itemSize + 1 , y );

		return this;

	}

	public BufferAttribute setZ( int index, double z ) {

		this.array.set( index * this.itemSize + 2 , z );

		return this;

	}

	public BufferAttribute setXY( int index, double x, double y ) {

		index *= this.itemSize;

		this.array.set( index     , x);
		this.array.set( index + 1 , y );

		return this;

	}

	public BufferAttribute setXYZ ( int index, double x, double y, double z ) {

		index *= this.itemSize;

		this.array.set( index     , x );	
		this.array.set( index + 1 , y );
		this.array.set( index + 2 , z );

		return this;

	}

	public BufferAttribute setXYZW ( int index, double x, double y, double z, double w ) {

		index *= this.itemSize;

		this.array.set( index     , x );
		this.array.set( index + 1 , y );
		this.array.set( index + 2 , z );
		this.array.set( index + 3 , w );

		return this;

	}

	public BufferAttribute clone() {

		return new BufferAttribute( Float32Array.create(this.array),  this.itemSize );

	}


}
