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

package thothbot.parallax.loader.shared.collada;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;

public class DaeInput extends DaeElement 
{
	private int set;
	private int offset;
	private String semantic;
	private String source;

	public DaeInput(Node node) 
	{
		super(node);
		
		Log.debug("DaeInput() " + toString());
	}

	@Override
	public void read() 
	{
		semantic = readAttribute("semantic");
		source = readAttribute("source", true);
		offset = readIntAttribute("offset", 0);
		set = readIntAttribute("set", 0);
	}

	public String getSemantic() {
		return semantic;
	}

	public String getSource() {
		return source;
	}

	public int getOffset() {
		return offset;
	}

	public int getSet() {
		return set;
	}

	public void setSource(String value) {
		source = value;
	}
	
	public String toString()
	{
		return "{semantic=" +this.semantic + ", offset="+ this.offset + ", set="+ this.set + "}";
	}
}
