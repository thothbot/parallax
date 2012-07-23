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

public abstract class DaeElement 
{
	private Node node;

	public DaeElement()
	{
		
	}
	
	public DaeElement(Node node) 
	{
		setNode( node );
	}

	public void destroy() 
	{
		node = null;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node;
		read();
	}

	public abstract void read(); 

	public String readAttribute(String name) 
	{
		return (node.getAttributes().getNamedItem(name) != null ? 
				node.getAttributes().getNamedItem(name).getNodeValue() : null);
	}

	public String readAttribute(String name, Boolean stripHash) 
	{
		String attr = readAttribute(name);
		if (stripHash && attr.startsWith("#"))
			attr = attr.substring(1);

		return attr;
	}

	public int readIntAttribute(String name, int defaultValue) 
	{
		String attr = readAttribute(name);
		if (attr != null) 
		{
			return Integer.parseInt(attr, 10);
		} 
		else 
		{
			return defaultValue;
		}
	}
	
	public abstract String toString();
}
