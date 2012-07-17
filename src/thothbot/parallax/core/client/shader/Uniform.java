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

	public Uniform.TYPE type;
	public Object value;
	public WebGLUniformLocation location;
	public Texture texture;
	public Float32Array _array;
	
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
	
	public String toString()
	{
		return "{type=" + type.name() + ", value=" + value + ", texture=" + texture + "}";
	}
	
	// TODO: check if needed to clone value and Location
	public Uniform clone()
	{
		Uniform result = new Uniform(this.type, this.value, this.texture);
		result.location = this.location;
		result._array = this._array;

		return result;
	}

}
