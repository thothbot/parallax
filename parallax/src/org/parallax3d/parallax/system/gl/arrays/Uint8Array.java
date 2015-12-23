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

/**
 * The typed array that holds unsigned byte (8-bit unsigned integer) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Uint8Array extends IndexTypeArray {
	public static final int SIGN_OFFSET = 0x10000;
	public static final int MAX_POSITIVE = 0x7fff;
	public static final int MAX_NEGATIVE = 0xffff;

	/**
	 * @param capacity	In bytes.
	 */
	protected Uint8Array(int capacity) {
		super(capacity);
	}

	@Override
	public int getLength()
	{
		return buffer.limit();
	}

	@Override
	protected void createTypedBuffer() {
	}

	@Override
	public int getElementType() {
		return GL20.GL_UNSIGNED_BYTE;
	}

	@Override
	public int getElementSize() {
		return 1;
	}

	/**
	 * Create a new {@link java.nio.ByteBuffer} with enough bytes to hold length
	 * elements of this typed array.
	 * 
	 * @param length
	 */
	public static Uint8Array create(int length) {
		return new Uint8Array(length);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Uint8Array create(TypeArray array) {
		Uint8Array result = create(array.getLength());
		result.set(array);
		return result;
	}

	private static byte coerce(int n) {
		return (byte) (n > MAX_POSITIVE & n <= MAX_POSITIVE ?
				(n - SIGN_OFFSET) : n);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Uint8Array create(int[] array) {
		byte[] bytes = new byte[array.length];
		for (int i = 0; i < array.length; ++i) {
            bytes[i] = coerce(array[i]);
		}
		return create(bytes);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Uint8Array create(byte[] array) {
		Uint8Array result = create(array.length);
		result.getBuffer().put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public int get(int index) {
		return getBuffer().get(index);
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	@Override
	public int getUnsigned(int index) {
		int n = getBuffer().get(index);
		return n < 0 ? (int) n + SIGN_OFFSET : (int) n;
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, byte value) {
		getBuffer().put(index, value);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, int value) {
		getBuffer().put(index, coerce(value));
	}

	public void set(Uint8Array array) {
        super.set(array);
    }

	public void set(Uint8Array array, int offset) {
        super.set(array, offset);
    }

	/**
	 * slice methods were not used.
     */
}
