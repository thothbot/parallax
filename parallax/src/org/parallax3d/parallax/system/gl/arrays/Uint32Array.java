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

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * The typed array that holds unsigned int (32-bit unsigned integer) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Uint32Array extends TypeArray {
	public static final int BYTES_PER_ELEMENT = 4;
	public static final long SIGN_OFFSET = 0x100000000l;
	public static final long MAX_POSITIVE = 0x7fffffffl;
	public static final long MAX_NEGATIVE = 0xffffffffl;

	private IntBuffer intBuffer;

	/**
	 * @param capacity	In bytes.
	 */
	protected Uint32Array(int capacity) {
		super(capacity);
		createTypedBuffer();
	}

	@Override
	protected void createTypedBuffer() {
		intBuffer = getBuffer().asIntBuffer();
	}

	@Override
	public IntBuffer getTypedBuffer() {
		return intBuffer;
	}

	@Override
	public int getElementType() {
		return GL20.GL_UNSIGNED_INT;
	}

    @Override
    public int getElementSize() {
        return 4;
    }

    /**
	 * Create a new {@link java.nio.ByteBuffer} with enough bytes to hold length
	 * elements of this typed array.
	 * 
	 * @param length
	 */
	public static Uint32Array create(int length) {
		return new Uint32Array(length * BYTES_PER_ELEMENT);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Uint32Array create(TypeArray array) {
		Uint32Array result = create(array.getLength());
		result.set(array);
		return result;
	}

	private static int coerce(long n) {
		return (int) (n > MAX_POSITIVE & n <= MAX_POSITIVE ?
				(n - SIGN_OFFSET) : n);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Uint32Array create(long[] array) {
		int[] ints = new int[array.length];
		for (int i = 0; i < array.length; ++i) {
            ints[i] = coerce(array[i]);
		}
		return create(ints);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Uint32Array create(int[] array) {
		Uint32Array result = create(array.length);
		result.intBuffer.put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public int get(int index) {
		return intBuffer.get(index);
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public long getUnsigned(int index) {
		int n = intBuffer.get(index);
		return n < 0 ? (long) n + SIGN_OFFSET : (long) n;
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, int value) {
		intBuffer.put(index, value);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, long value) {
		intBuffer.put(index, coerce(value));
	}

	public void set(Uint32Array array) {
        super.set(array);
    }

	public void set(Uint32Array array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

    public void set(TypeArray array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

	@Override
	public int getLength()
	{
		return intBuffer.limit();
	}

	/**
	 * slice methods were not used.
     */
}
