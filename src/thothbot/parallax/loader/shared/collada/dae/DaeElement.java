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

public class DaeElement 
{
	private DaeDocument document;
	private String id;
	private String name;
	private String sid;

	public DaeElement(DaeDocument document) 
	{
		id = name = sid = null;
		this.document = document;
	}

	public DaeElement(DaeDocument document, Node node) 
	{
		this(document);
		if (node != null) 
		{
			read(node);
		}
	}

	public void destroy() 
	{
		id = name = sid = null;
		document = null;
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

	public void read(Node node) 
	{
		id = readAttribute(node, "id");
		sid = readAttribute(node, "sid");
		name = readAttribute(node, "name");
	}

	public String readAttribute(Node node, String name) 
	{
		return (node.getAttributes().getNamedItem(name) != null ? node
				.getAttributes().getNamedItem(name).getNodeValue() : null);
	}

	public String readAttribute(Node node, String name, Boolean stripHash) 
	{
		String attr = readAttribute(node, name);
		if (stripHash && attr.startsWith("#")) {
			attr = attr.substring(1);
		}
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
		if (parts != null && parts.length > 0) 
		{
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
		Log.debug("readIntArray: " + parts);
		if (parts != null && parts.length > 0) 
		{
			Log.debug(" -> " + parts.length);
			
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
			Log.debug("readStringArray failed! " + node);
		}
		return null;
	}

	public void write() {

	}

	public DaeDocument getDocument() 
	{
		return document;
	}
}
