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

package thothbot.parallax.core.client.shader;

import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.textures.Texture;

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
		F,  // single float
		V2, // single Vector2f
		V3, // single Vector3f
		V4, // single Vector4f
		C,  // single Color3f
		FV1,// flat array of floats (JS or typed array)
		FV, // flat array of floats with 3 x N size (JS or typed array)
		V2V,// array of Vector2f
		V3V,// array of Vector3f
		V4V,// array of Vector4f
		M4, // single Matrix4f
		M4V,// array of Matrix4f
		T,  // single Texture (2d or cube)
		TV // array of Texture (2d)
	};

	private Uniform.TYPE type;
	private Object value;
	private Texture texture;
	private Float32Array cache_array;
	
	public Uniform(Uniform.TYPE type, Object value) 
	{
		this.type = type;
		this.value = value;
	}
	
	public Uniform(Uniform.TYPE type, Object value, Texture texture) 
	{
		this.type = type;
		this.value = value;
		this.texture = texture;
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
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Float32Array getCacheArray() {
		return this.cache_array;
	}
	
	public void setCacheArray(Float32Array array) {
		this.cache_array = array;
	}
	
	public String toString()
	{
		return "{type=" + type.name() + ", value=" + value + ", texture=" + texture + "}";
	}
	
	public Uniform clone()
	{
		Uniform result = new Uniform(this.type, this.value, this.texture);
		result.cache_array = this.cache_array;

		return result;
	}
}
