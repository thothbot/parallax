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

import java.util.ArrayList;
import java.util.Arrays;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;

public class DaeElement 
{
	private Node node;
	private String id;
	private String name;
	private String sid;

	public DaeElement()
	{
		
	}

	public DaeElement(Node node) 
	{
		setNode( node );
	}

	public void destroy() 
	{
		id = name = sid = null;
		node = null;
	}
	
	public Node getNode() 
	{
		return node;
	}
	
	public void setNode(Node node)
	{
		this.node = node;
		read();
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSID() {
		return sid;
	}

	public void read() 
	{
		id = readAttribute(getNode(), "id");
		sid = readAttribute(getNode(), "sid");
		name = readAttribute(getNode(), "name");
	}

	public String readAttribute(Node node, String name) 
	{
		return (node.getAttributes().getNamedItem(name) != null ? 
				node.getAttributes().getNamedItem(name).getNodeValue() : null);
	}

	public String readAttribute(Node node, String name, Boolean stripHash) 
	{
		String attr = readAttribute(node, name);
		if (stripHash && attr.startsWith("#"))
			attr = attr.substring(1);

		return attr;
	}

	public int readIntAttribute(Node node, String name, int defaultValue) 
	{
		String attr = readAttribute(node, name);
		if (attr != null) 
		{
			return Integer.parseInt(attr, 10);
		} 
		else 
		{
			return defaultValue;
		}
	}

	public float[] readFloatArray(Node node) 
	{
		String[] parts = readStringArray(node);
		Log.debug("DaeElement() readFloatArray: " + parts);
		if (parts != null && parts.length > 0) 
		{
			Log.debug(" [Float]-> " + parts.length);
			
			float[] data = new float[parts.length];
			for (int i = 0; i < parts.length; i++) 
			{
				data[i] = Float.parseFloat(parts[i]);
			}
			return data;
		}
		return null;
	}

	public int[] readIntArray(Node node) 
	{
		String[] parts = readStringArray(node);
		Log.debug("DaeElement() readIntArray: " + parts);
		if (parts != null && parts.length > 0) 
		{
			Log.debug(" [Int]-> " + parts.length);
			
			int[] data = new int[parts.length];
			for (int i = 0; i < parts.length; i++) 
			{
				data[i] = Integer.parseInt(parts[i], 10);
			}
			return data;
		}
		return null;
	}

	public String[] readStringArray(Node node) 
	{
		if (node.getChildNodes().getLength() > 0) 
		{
			String raw = "";
			for (int i = 0; i < node.getChildNodes().getLength(); i++) 
			{
				Node child = node.getChildNodes().item(i);
				if (child.getNodeType() == Node.TEXT_NODE) 
				{
					raw += child.getNodeValue();
				}
			}

			String[] parts = raw.trim().split("\\s+");
			return parts;
		} 
		else 
		{
			Log.error("readStringArray failed! " + getNode());
		}
		return null;
	}

	public void write() {

	}
	
	public String toString()
	{
		return "{id=" + this.id + ", name=" + this.name + ", sid=" + this.sid + "}";
	}
}
