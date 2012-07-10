/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.core;

import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLBuffer;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;

public class WebGLCustomAttribute
{
	public static enum TYPE {
		V2,
		V3,
		V4,
		C,
		F
	};

	public WebGLBuffer buffer;
	public boolean createUniqueBuffers;
	public WebGLCustomAttribute.TYPE type;
	// TODO: remove = change to type (initCustomAttributes)
	public int size;
	private List<?> value;
	public Float32Array array;
	public boolean needsUpdate;
	public String belongsToAttribute;

	public String boundTo;

	public WebGLCustomAttribute __original;

	public boolean __webglInitialized;
	
	public WebGLCustomAttribute()
	{
		//
	}

	public WebGLCustomAttribute(WebGLCustomAttribute.TYPE type, List<?> value)
	{
		this.type = type;
		this.value = value;
	}
	
	public WebGLCustomAttribute clone() {
		WebGLCustomAttribute att = new WebGLCustomAttribute();
		att.buffer = this.buffer;
		att.createUniqueBuffers = this.createUniqueBuffers;
		att.type = this.type;
		att.size = this.size;
		att.array = this.array;
		att.needsUpdate = this.needsUpdate;
		att.__webglInitialized = this.__webglInitialized;
		att.__original = this;
		
		return att;
	}

	public List<?> getValue()
	{
		return value;
	}

	public void setValue(List<Object> value)
	{
		this.value = value;
	}
	
	public String toString()
	{
		return "{ type=" + this.type.name() 
				+ ", needsUpdate=" + this.needsUpdate 
				+ ", belongsToAttribute=" + this.belongsToAttribute
				+ ", valueSize=" + this.value.size() 
				+ ", value=" + this.value + "}";
	}
}
