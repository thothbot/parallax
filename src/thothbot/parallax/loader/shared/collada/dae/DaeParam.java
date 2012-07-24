/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.loader.shared.collada.dae;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;

public class DaeParam extends DaeElement
{
	private String name;
	private String type;
	
	public DaeParam(Node node)
	{
		super(node);
		Log.debug("DaeParam() " + toString());
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public void read() 
	{
		name = readAttribute("name");
		type = readAttribute("type");
	}
	
	@Override
	public String toString() 
	{
		return "{name=" + this.name + ", type=" + this.type + "}";
	}
}