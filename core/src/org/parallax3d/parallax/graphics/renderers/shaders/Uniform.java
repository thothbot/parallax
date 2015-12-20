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

package org.parallax3d.parallax.graphics.renderers.shaders;

import java.nio.FloatBuffer;

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

	private TYPE type;
	private Object value;
	private FloatBuffer cache_array;
	private Integer location; //WebGLUniformLocation

	public Uniform(TYPE type)
	{
		this(type, null);
	}

	public Uniform(TYPE type, Object value)
	{
		this.type = type;
		this.value = value;
	}
		
	public TYPE getType() {
		return this.type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public FloatBuffer getCacheArray() {
		return this.cache_array;
	}
	
	public void setCacheArray(FloatBuffer array) {
		this.cache_array = array;
	}
	
	public Integer getLocation() {
		return this.location;
	}
	
	public void setLocation(int location) {
		this.location = location;
	}
	
	public String toString()
	{
		return "{type=" + type.name() 
				+ ", value=" + value 
				+ ", location=" + location + "}\n";
	}
	
	public Uniform clone()
	{
		Uniform result = new Uniform(this.type, this.value);
		result.cache_array = this.cache_array;

		return result;
	}
}
