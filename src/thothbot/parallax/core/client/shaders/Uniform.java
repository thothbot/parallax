/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.shaders;

import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;

/**
 * Shader's uniforms.
 * 
 * @author thothbot
 *
 */
public class Uniform
{
	public static enum TYPE {
		I,  // single integer
		F,  // single double
		V2, // single Vector2
		V3, // single Vector3
		V4, // single Vector4
		C,  // single Color
		FV1,// flat array of floats (JS or typed array)
		FV, // flat array of floats with 3 x N size (JS or typed array)
		V2V,// array of Vector2
		V3V,// array of Vector3
		V4V,// array of Vector4
		M4, // single Matrix4
		M4V,// array of Matrix4
		T,  // single Texture (2d or cube)
		TV // array of Texture (2d)
	};

	private Uniform.TYPE type;
	private Object value;
	private Float32Array cache_array;
	private WebGLUniformLocation location;

	public Uniform(Uniform.TYPE type) 
	{
		this(type, null);
	}

	public Uniform(Uniform.TYPE type, Object value) 
	{
		this.type = type;
		this.value = value;
	}
		
	public Uniform.TYPE getType() {
		return this.type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Float32Array getCacheArray() {
		return this.cache_array;
	}
	
	public void setCacheArray(Float32Array array) {
		this.cache_array = array;
	}
	
	public WebGLUniformLocation getLocation() {
		return this.location;
	}
	
	public void setLocation(WebGLUniformLocation location) {
		this.location = location;
	}
	
	public String toString()
	{
		return "{type=" + type.name() + ", value=" + value + "}";
	}
	
	public Uniform clone()
	{
		Uniform result = new Uniform(this.type, this.value);
		result.cache_array = this.cache_array;

		return result;
	}
}
