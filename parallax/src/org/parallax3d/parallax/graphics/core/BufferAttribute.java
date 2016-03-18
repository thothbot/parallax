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

import org.parallax3d.parallax.graphics.renderers.gl.AttributeData;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.*;

@ThreejsObject("BufferAttribute")
public class BufferAttribute extends AttributeData {

	int itemSize;

	public BufferAttribute(TypeArray array, int itemSize)
	{
		super(array);

		this.itemSize = itemSize;
	}

	public int getItemSize() {
		return this.itemSize;
	}

	public int getCount() {

		return this.array.getLength()/ this.itemSize;

	}

	public void copyAt( int index1, BufferAttribute attribute, int index2 ) {

		index1 *= this.itemSize;
		index2 *= attribute.itemSize;

		for ( int i = 0, l = this.itemSize; i < l; i ++ ) {

			((Float32Array)this.array).set(index1 + i, ((Float32Array) attribute.array).get(index2 + i));

		}

	}

	public double getX( int index ) {

		return ((Float32Array)this.array).get( index * this.itemSize );

	}

	public BufferAttribute setX( int index, double x ) {

		((Float32Array)this.array).set(index * this.itemSize, x);

		return this;

	}

	public double getY( int index ) {

		return ((Float32Array)this.array).get( index * this.itemSize + 1 );

	}

	public BufferAttribute setY( int index, double y ) {

		((Float32Array)this.array).set(index * this.itemSize + 1, y);

		return this;

	}

	public double getZ( int index ) {

		return ((Float32Array)this.array).get( index * this.itemSize + 2 );

	}

	public BufferAttribute setZ( int index, double z ) {

		((Float32Array)this.array).set(index * this.itemSize + 2, z);

		return this;

	}

	public double getW( int index ) {

		return ((Float32Array)this.array).get( index * this.itemSize + 3 );

	}

	public BufferAttribute setW( int index, double w ) {

		((Float32Array)this.array).set(index * this.itemSize + 3, 2);

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
				+ ", int itemSize: " + this.itemSize + "}";
	}

	public static BufferAttribute Int8Attribute( byte[] array, int itemSize )
	{
		return new BufferAttribute(Int8Array.create(array), itemSize );
	};

	public static BufferAttribute Uint8Attribute( byte[] array, int itemSize ) {

		return new BufferAttribute(Uint8Array.create(array), itemSize );

	};

	public static BufferAttribute Int16Attribute( short[] array, int itemSize ) {

		return new BufferAttribute(Int16Array.create(array), itemSize );

	};

	public static BufferAttribute Uint16Attribute( short[] array, int itemSize ) {

		return new BufferAttribute( Uint16Array.create( array ), itemSize );

	};

	public static BufferAttribute Int32Attribute( int[] array, int itemSize ) {

		return new BufferAttribute( Int32Array.create( array ), itemSize );

	};

	public static BufferAttribute Uint32Attribute(Object[] array, int itemSize ) {

		return new BufferAttribute( Uint32Array.create( array ), itemSize );

	};

	public static BufferAttribute Float32Attribute( double[] array, int itemSize ) {

		return new BufferAttribute( Float32Array.create( array ), itemSize );

	};

	public static BufferAttribute Float64Attribute( double[] array, int itemSize ) {

		return new BufferAttribute( Float64Array.create( array ), itemSize );

	};
}
