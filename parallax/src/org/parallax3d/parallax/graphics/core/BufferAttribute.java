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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.*;

import java.util.List;

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

	public BufferAttribute copy( BufferAttribute source ) {

		for ( int i = 0, l = source.itemSize; i < l; i ++ ) {

			((Float32Array)this.array).set(i, ((Float32Array) source.array).get(i));

		}

		this.itemSize = source.itemSize;

		this.dynamic = source.dynamic;

		return this;

	}

	public void copyAt( int index1, BufferAttribute attribute, int index2 ) {

		index1 *= this.itemSize;
		index2 *= attribute.itemSize;

		for ( int i = 0, l = this.itemSize; i < l; i ++ ) {

			((Float32Array)this.array).set(index1 + i, ((Float32Array) attribute.array).get(index2 + i));

		}

	}

	public BufferAttribute copyArray( List<Double> array ) {

		int offset = 0;

		for ( int i = 0, l = array.size(); i < l; i ++ ) {
			((Float32Array)this.array).set( offset ++ , array.get(i));
		}

		return this;

	}

	public BufferAttribute copyColorsArray( List<Color> colors ) {

		Float32Array array = (Float32Array) this.array;
		int offset = 0;

		for ( int i = 0, l = colors.size(); i < l; i ++ ) {

			Color color = colors.get(i);

			if ( color == null ) {

				Log.warn( "BufferAttribute.copyColorsArray(): color is undefined: " + i );
				color = new Color();

			}

			array.set( offset ++ , color.getR());
			array.set( offset ++ , color.getG());
			array.set( offset ++ , color.getB());

		}

		return this;

	}

	public BufferAttribute  copyIndicesArray( List<Face3> indices ) {

		Float32Array array = (Float32Array) this.array;
		int offset = 0;

		for ( int i = 0, l = indices.size(); i < l; i ++ ) {

			Face3 index = indices.get(i);

			array.set( offset ++ , index.a);
			array.set( offset ++ , index.b);
			array.set( offset ++ , index.c);

		}

		return this;

	}

	public BufferAttribute copyVector2sArray( List<Vector2> vectors ) {

		Float32Array array = (Float32Array) this.array;
		int offset = 0;

		for ( int i = 0, l = vectors.size(); i < l; i ++ ) {

			Vector2 vector = vectors.get(i);

			if ( vector == null ) {

				Log.warn( "BufferAttribute.copyVector2sArray(): vector is undefined " + i );
				vector = new Vector2();

			}

			array.set( offset ++ , vector.getX());
			array.set( offset ++ , vector.getY());

		}

		return this;

	}

	public BufferAttribute copyVector3sArray( List<Vector3> vectors ) {

		Float32Array array = (Float32Array) this.array;
		int offset = 0;

		for ( int i = 0, l = vectors.size(); i < l; i ++ ) {

			Vector3 vector = vectors.get(i);

			if ( vector == null ) {

				Log.warn( "BufferAttribute.copyVector3sArray(): vector is undefined " + i );
				vector = new Vector3();

			}

			array.set( offset ++ , vector.getX());
			array.set( offset ++ , vector.getY());
			array.set( offset ++ , vector.getZ());

		}

		return this;

	}

	public BufferAttribute copyVector4sArray ( List<Vector4> vectors ) {

		Float32Array array = (Float32Array) this.array;
		int offset = 0;

		for ( int i = 0, l = vectors.size(); i < l; i ++ ) {

			Vector4 vector = vectors.get(i);

			if ( vector == null ) {

				Log.warn( "BufferAttribute.copyVector4sArray(): vector is undefined " + i );
				vector = new Vector4();

			}

			array.set( offset ++ , vector.getX());
			array.set( offset ++ , vector.getY());
			array.set( offset ++ , vector.getZ());
			array.set( offset ++ , vector.getW());

		}

		return this;

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

	public static BufferAttribute Int8Attribute( int len, int itemSize )
	{
		return new BufferAttribute(Int8Array.create(len), itemSize );
	};

	public static BufferAttribute Uint8Attribute( byte[] array, int itemSize ) {

		return new BufferAttribute(Uint8Array.create(array), itemSize );

	};

	public static BufferAttribute Uint8Attribute( int len, int itemSize ) {

		return new BufferAttribute(Uint8Array.create(len), itemSize );

	};

	public static BufferAttribute Int16Attribute( short[] array, int itemSize ) {

		return new BufferAttribute(Int16Array.create(array), itemSize );

	};

	public static BufferAttribute Int16Attribute( int len, int itemSize ) {

		return new BufferAttribute(Int16Array.create(len), itemSize );

	};


	public static BufferAttribute Uint16Attribute( short[] array, int itemSize ) {

		return new BufferAttribute( Uint16Array.create( array ), itemSize );

	};

	public static BufferAttribute Uint16Attribute( int len, int itemSize ) {

		return new BufferAttribute( Uint16Array.create( len ), itemSize );

	};

	public static BufferAttribute Int32Attribute( int[] array, int itemSize ) {

		return new BufferAttribute( Int32Array.create( array ), itemSize );

	};

	public static BufferAttribute Int32Attribute( int len, int itemSize ) {

		return new BufferAttribute( Int32Array.create( len ), itemSize );

	};

	public static BufferAttribute Uint32Attribute(List<Integer> array, int itemSize ) {

		return new BufferAttribute( Uint32Array.create( array ), itemSize );

	}

	public static BufferAttribute Uint32Attribute(int[] array, int itemSize ) {

		return new BufferAttribute( Uint32Array.create( array ), itemSize );

	}

	public static BufferAttribute Uint32Attribute(int len, int itemSize ) {

		return new BufferAttribute( Uint32Array.create( len ), itemSize );

	};

	public static BufferAttribute Float32Attribute( double[] array, int itemSize ) {

		return new BufferAttribute( Float32Array.create( array ), itemSize );

	};

	public static BufferAttribute Float32Attribute( int len, int itemSize ) {

		return new BufferAttribute( Float32Array.create( len ), itemSize );

	};

	public static BufferAttribute Float64Attribute( double[] array, int itemSize ) {

		return new BufferAttribute( Float64Array.create( array ), itemSize );

	};

	public static BufferAttribute Float64Attribute( int len, int itemSize ) {

		return new BufferAttribute( Float64Array.create( len ), itemSize );

	};
}
