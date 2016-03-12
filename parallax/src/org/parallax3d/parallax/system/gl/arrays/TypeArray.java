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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Wraps Java's ByteBuffer for Parallax3D's API.
 *
 * @author h@realh.co.uk
 */
public abstract class TypeArray  {

    protected ByteBuffer buffer;

	protected TypeArray(int capacity) {
        createBuffer(capacity);
	}

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public Buffer getTypedBuffer() {
        return buffer;
    }

    public int getBytesPerElement() {
        return 1;
    }

    /**
     * The subarray() method returns a new TypedArray on the same ArrayBuffer
     * store and with the same element types as for this TypedArray object.
     * The begin offset is inclusive and the end offset is exclusive. TypedArray is one of the typed array types.
     *
     * @param begin Element to begin at. The offset is inclusive.
     * @param end Element to end at. The offset is exclusive. If not specified, all elements
     *            from the one specified by begin to the end of the array are included in the new view.
     * @return
     */
    public abstract TypeArray getSubarray(int begin, int end  );

    /**
     * Support creation of empty Float32Buffers which can be resized dynamically.
     */
    protected TypeArray() {
        buffer = null;
    }

    protected void createBuffer(int capacity) {
        buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }

    /**
     * Set multiple values, reading input values from the array.
     *
     * @param array
    */
    public final void set(TypeArray array) {
        ByteBuffer b = array.buffer;
        int l = b.limit();
        b.rewind();
        if (l > buffer.capacity()) {
            createBuffer(l);
            createTypedBuffer();
        } else {
            buffer.rewind();
        }
        buffer.put(b);
	}

    /**
     * Set multiple values, reading input values from the array.
     *
     * @param array
     * @param offset indicates the index in the current array where values are
     * 				written, in subclass' element size.
     */
    public void set(TypeArray array, int offset) {
        if (this.getClass() != array.getClass()) {
            throw new Error("Type mismatch for array copy");
        }
        ByteBuffer b = array.buffer;
        int l = b.limit() + offset;
        b.rewind();
        if (l > buffer.capacity()) {
            ByteBuffer old = buffer;
            createBuffer(l);
            old.limit(offset);
            buffer.put(old);
            createTypedBuffer();
        } else {
            buffer.position(offset);
        }
        buffer.put(b);
    }

    /**
     * Gets the length of this array in bytes.
     */
    public int getByteLength() {
        return buffer.limit();
    }

    /**
     * Gets the length of this array in elements of subclass' type.
     */
    public abstract int getLength();

    /**
     * Disused reverse() method elided.
     */

    protected abstract void createTypedBuffer();

    /**
     * @return  The type of each element eg GL_BYTE, GL_FLOAT etc.
     */
    public abstract int getElementType();

    /**
     * @return  The size of each element in bytes.
     */
    public abstract int getElementSize();
}
