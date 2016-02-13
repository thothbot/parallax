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
 * The typed array that holds short (16-bit signed integer) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Int16Array extends TypeArray {
	public static final int BYTES_PER_ELEMENT = 2;

	private ShortBuffer shortBuffer;

	/**
	 * @param capacity	In bytes.
	 */
	protected Int16Array(int capacity) {
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
		return GL20.GL_SHORT;
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
	public static Int16Array create(int length) {
		return new Int16Array(length * BYTES_PER_ELEMENT);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Int16Array create(TypeArray array) {
		Int16Array result = create(array.getLength());
		result.set(array);
		return result;
	}
	
	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Int16Array create(short[] array) {
		Int16Array result = create(array.length);
		result.shortBuffer.put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public short get(int index) {
		return shortBuffer.get(index);
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

	public void set(Int16Array array) {
        super.set(array);
    }

	public void set(Int16Array array, int offset) {
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
