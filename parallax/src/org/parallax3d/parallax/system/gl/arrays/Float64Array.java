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
package org.parallax3d.parallax.system.gl.arrays;

import org.parallax3d.parallax.system.gl.GL20;

import java.nio.DoubleBuffer;
import java.nio.ShortBuffer;

/**
 * The typed array that holds double (64-bit IEEE floating point) as its element.
 * Not really useful for OpenGL.
 * 
 * @author h@realh.co.uk
 */
public final class Float64Array extends TypeArray {
	public static final int BYTES_PER_ELEMENT = 8;

	private DoubleBuffer doubleBuffer;

	/**
	 * @param capacity	In bytes.
	 */
	protected Float64Array(int capacity) {
		super(capacity);
		createTypedBuffer();
	}

	@Override
	protected void createTypedBuffer() {
		doubleBuffer = getBuffer().asDoubleBuffer();
	}

	@Override
	public DoubleBuffer getTypedBuffer() {
		return doubleBuffer;
	}

	@Override
	public int getElementType() {
		return GL20.GL_FLOAT;
	}

	@Override
	public int getElementSize() {
		return 8;
	}

	/**
	 * Create a new {@link java.nio.ByteBuffer} with enough bytes to hold length
	 * elements of this typed array.
	 * 
	 * @param length
	 */
	public static Float64Array create(int length) {
		return new Float64Array(length * BYTES_PER_ELEMENT);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Float64Array create(TypeArray array) {
		Float64Array result = create(array.getLength());
		result.set(array);
		return result;
	}
	
	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Float64Array create(double[] array) {
		Float64Array result = create(array.length);
		result.doubleBuffer.put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public double get(int index) {
		return doubleBuffer.get(index);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, double value) {
		doubleBuffer.put(index, value);
	}

	public void set(Float64Array array) {
        super.set(array);
    }

	public void set(Float64Array array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

    public void set(TypeArray array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

	@Override
	public int getLength()
	{
		return doubleBuffer.limit();
	}

	/**
	 * slice methods were not used.
     */
}
