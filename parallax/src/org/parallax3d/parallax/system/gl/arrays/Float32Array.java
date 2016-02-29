/*
 * Copyright 2009-2011 Sönke Sothmann, Steffen Schäfer and others
 * Copyright 2015 Tony Houghton, h@realh.co.uk
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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.gl.GL20;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * The typed array that holds float (32-bit IEEE floating point) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Float32Array extends TypeArray {
	public static final int BYTES_PER_ELEMENT = 4;

	private FloatBuffer floatBuffer;

	/**
	 * Lighting requires arrays of unknown length.
	 */
	private boolean resizable = false;

	protected Float32Array() {
		buffer = null;
		floatBuffer = null;
		resizable = true;
	}

	/**
	 * @param capacity	In bytes.
	 */
	protected Float32Array(int capacity) {
		super(capacity);
		createTypedBuffer();
	}

	@Override
	protected void createTypedBuffer() {
		floatBuffer = getBuffer().asFloatBuffer();
	}

	@Override
	public FloatBuffer getTypedBuffer() {
		return floatBuffer;
	}

    @Override
    public int getElementType() {
        return GL20.GL_FLOAT;
    }

    @Override
    public int getElementSize() {
        return 4;
    }

	public static Float32Array createArray()
	{
		return new Float32Array();
	}

    /**
	 * Create a new {@link java.nio.ByteBuffer} with enough bytes to hold length
	 * elements of this typed array.
	 * 
	 * @param length
	 */
	public static Float32Array create(int length) {
		return new Float32Array(length * BYTES_PER_ELEMENT);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Float32Array create(TypeArray array) {
		Float32Array result = create(array.getLength());
		result.set(array);
		return result;
	}
	
	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Float32Array create(double[] array) {
		float[] floats = new float[array.length];
		for (int i = 0; i < array.length; ++i)
			floats[i] = (float) array[i];
		return create(floats);
	}

	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Float32Array create(float[] array) {
		Float32Array result = create(array.length);
		result.floatBuffer.put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public double get(int index) {
		return floatBuffer.get(index);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, double value) {
		set(index, (float) value);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, float value) {
		if (resizable)
		{
			if (floatBuffer == null || index >= floatBuffer.capacity())
			{
				float val[] = new float[getLength()];
				for(int i = 0, len = getLength(); i < len; i++)
					val[i] =  floatBuffer.get(i);

				// Usually floats are added in groups of 3, so add spare capacity
				createBuffer((index + 4) * 12);
				createTypedBuffer();
				floatBuffer.limit(index + 1);
				floatBuffer.put(val);
			}
			else if (index >= floatBuffer.limit())
			{
				floatBuffer.limit(index + 1);
			}
		}
		floatBuffer.put(index, value);
	}

	public void set(Float32Array array) {
        super.set(array);
    }

	public void set(Float32Array array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

    public void set(TypeArray array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

	@Override
	public int getLength()
	{
		return floatBuffer == null ? 0 : floatBuffer.limit();
	}

	public String toString() {
		float val[] = new float[getLength()];
		for(int i = 0, len = getLength(); i < len; i++)
			val[i] =  floatBuffer.get(i);
		return Arrays.toString(val);
	}
}
