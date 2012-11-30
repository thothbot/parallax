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

package thothbot.parallax.core.client.shaders;

import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;

public class Attribute
{
	public static enum TYPE {
		V2,
		V3,
		V4,
		C,
		F
	};
	
	public static enum BOUND_TO {
		FACES,
		VERTICES,
		FACE_VERTICES
	};

	public WebGLBuffer buffer;
	public boolean createUniqueBuffers;
	public Attribute.TYPE type;
	// TODO: remove = change to type (initCustomAttributes)
	public int size;
	private List<?> value;
	private BOUND_TO boundTo;
	
	public Float32Array array;
	public boolean needsUpdate;
	public String belongsToAttribute;
	public Attribute __original;

	public boolean __webglInitialized;
	
	public Attribute()
	{
		//
	}

	public Attribute(Attribute.TYPE type, List<?> value)
	{
		this.type = type;
		this.value = value;
	}
	
	public List<?> getValue() {
		return value;
	}

	public void setValue(List<Object> value) {
		this.value = value;
	}
	
	public BOUND_TO getBoundTo() {
		return this.boundTo;
	}
	
	public void setBoundTo(BOUND_TO boundTo) {
		this.boundTo = boundTo;
	}
	
	public Attribute clone() 
	{
		Attribute att = new Attribute(this.type, this.value);
		att.buffer = this.buffer;
		att.createUniqueBuffers = this.createUniqueBuffers;
		att.size = this.size;
		att.array = this.array;
		att.needsUpdate = this.needsUpdate;
		att.belongsToAttribute = this.belongsToAttribute;
		att.boundTo = this.boundTo;
		att.__webglInitialized = this.__webglInitialized;
		att.__original = this;
		
		return att;
	}
	
	public String toString()
	{
		return "{ type=" + this.type.name() 
				+ ", boundTo=" + this.boundTo
				+ ", needsUpdate=" + this.needsUpdate 
				+ ", belongsToAttribute=" + this.belongsToAttribute
				+ ", valueSize=" + (this.value == null ? "null" : this.value.size() ) 
				+ ", value=" + this.value + "}";
	}
}
