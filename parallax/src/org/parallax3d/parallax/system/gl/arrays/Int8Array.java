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
 * The typed array that holds byte (8-bit signed integer) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Int8Array extends TypeArray {

	/**
	 * @param capacity	In bytes.
	 */
	protected Int8Array(int capacity) {
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
		return GL20.GL_BYTE;
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
	public static Int8Array create(int length) {
		return new Int8Array(length);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Int8Array create(TypeArray array) {
		Int8Array result = create(array.getLength());
		result.set(array);
		return result;
	}
	
	/**
	 * Create an array.
	 *
	 * @param array
	 */
	public static Int8Array create(byte[] array) {
		Int8Array result = create(array.length);
		result.getBuffer().put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public byte get(int index) {
		return getBuffer().get(index);
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

	public void set(Int8Array array) {
        super.set(array);
    }

	public void set(Int8Array array, int offset) {
        super.set(array, offset);
    }

	/**
	 * slice methods were not used.
     */
}
