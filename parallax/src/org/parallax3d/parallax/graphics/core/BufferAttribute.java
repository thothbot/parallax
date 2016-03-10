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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;

@ThreejsObject("THREE.BufferAttribute")
public class BufferAttribute {

	public static class UpdateRange {
		public int offset = 0;
		public int count = -1;
	}

	TypeArray array;
	int itemSize;

	boolean dynamic = false;
	int version = 0;

	UpdateRange updateRange;

	public BufferAttribute(TypeArray array, int itemSize)
	{
		this.array = array;
		this.itemSize = itemSize;
		this.updateRange = new UpdateRange();
	}

	public int getItemSize() {
		return this.itemSize;
	}

	public TypeArray getArray() {
		return this.array;
	}

	public void setArray(TypeArray array) {
		this.array = array;
	}

	public int getCount() {

		return this.array.getLength()/ this.itemSize;

	}

	public void setNeedsUpdate( boolean value ) {

		if ( value ) this.version ++;

	}

	public BufferAttribute setDynamic( boolean value ) {

		this.dynamic = value;

		return this;

	}

	public void copyAt( int index1, BufferAttribute attribute, int index2 ) {

		index1 *= this.itemSize;
		index2 *= attribute.itemSize;

		for ( int i = 0, l = this.itemSize; i < l; i ++ ) {

			((Float32Array)this.array).set(index1 + i, ((Float32Array) attribute.array).get(index2 + i));

		}

	}

	public BufferAttribute set( TypeArray value ) {

		this.array.set(value);

		return this;

	}

	public BufferAttribute setX( int index, double x ) {

		((Float32Array)this.array).set(index * this.itemSize, x);

		return this;

	}

	public BufferAttribute setY( int index, double y ) {

		((Float32Array)this.array).set(index * this.itemSize + 1, y);

		return this;

	}

	public BufferAttribute setZ( int index, double z ) {

		((Float32Array)this.array).set(index * this.itemSize + 2, z);

		return this;

	}

	public BufferAttribute setXY( int index, double x, double y ) {

		index *= this.itemSize;

		((Float32Array)this.array).set(index, x);
		((Float32Array)this.array).set(index + 1, y);

		return this;

	}

	public BufferAttribute setXYZ ( int index, double x, double y, double z ) {

		index *= this.itemSize;

		((Float32Array)this.array).set(index, x);
		((Float32Array)this.array).set(index + 1, y);
		((Float32Array)this.array).set(index + 2, z);

		return this;

	}

	public BufferAttribute setXYZW ( int index, double x, double y, double z, double w ) {

		index *= this.itemSize;

		((Float32Array)this.array).set(index, x);
		((Float32Array)this.array).set(index + 1, y);
		((Float32Array)this.array).set(index + 2, z);
		((Float32Array)this.array).set(index + 3, w);

		return this;

	}

	public BufferAttribute clone() {

		return new BufferAttribute( Float32Array.create(this.array),  this.itemSize );

	}

	public String toString() {
		return "{array: " + this.array.getLength()
				+ ", itemSize: " + this.itemSize + "}";
	}
}
