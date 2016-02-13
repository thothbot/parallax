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
 * The typed array that holds unsigned short (16-bit unsigned integer) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Uint16Array extends IndexTypeArray {
	public static final int BYTES_PER_ELEMENT = 2;
	public static final int SIGN_OFFSET = 0x10000;
	public static final int MAX_POSITIVE = 0x7fff;
	public static final int MAX_NEGATIVE = 0xffff;

	private ShortBuffer shortBuffer;

	/**
	 * @param capacity	In bytes.
	 */
	protected Uint16Array(int capacity) {
		super(capacity);
		createTypedBuffer();
	}

	@Override
	protected void createTypedBuffer() {
		shortBuffer = getBuffer().asShortBuffer();
	}

	@Override
	public ShortBuffer getTypedBuffer() {
		return shortBuffer;
	}

	@Override
	public int getElementType() {
		return GL20.GL_UNSIGNED_SHORT;
	}

	@Override
	public int getElementSize() {
		return 2;
	}

	/**
	 * Create a new {@link java.nio.ByteBuffer} with enough bytes to hold length
	 * elements of this typed array.
	 * 
	 * @param length
	 */
	public static Uint16Array create(int length) {
		return new Uint16Array(length * BYTES_PER_ELEMENT);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Uint16Array create(TypeArray array) {
		Uint16Array result = create(array.getLength());
		result.set(array);
		return result;
	}

	private static short coerce(int n) {
		return (short) (n > MAX_POSITIVE & n <= MAX_POSITIVE ?
				(n - SIGN_OFFSET) : n);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Uint16Array create(int[] array) {
		short[] shorts = new short[array.length];
		for (int i = 0; i < array.length; ++i) {
            shorts[i] = coerce(array[i]);
		}
		return create(shorts);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Uint16Array create(short[] array) {
		Uint16Array result = create(array.length);
		result.shortBuffer.put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public int get(int index) {
		return shortBuffer.get(index);
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	@Override
	public int getUnsigned(int index) {
		int n = shortBuffer.get(index);
		return n < 0 ? n + SIGN_OFFSET : n;
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, short value) {
		shortBuffer.put(index, value);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, int value) {
		shortBuffer.put(index, coerce(value));
	}

	public void set(Uint16Array array) {
        super.set(array);
    }

	public void set(Uint16Array array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

    public void set(TypeArray array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

	@Override
	public int getLength()
	{
		return shortBuffer.limit();
	}

	/**
	 * slice methods were not used.
     */
}
