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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeMesh extends DaeElement 
{
	private Map<String, DaeSource> sources;
	private DaeSource vertices;
	private Vector<DaePrimitive> primitives;
	private String verticesID;

	public DaeMesh(Node node) 
	{
		super(node);
	}

	@Override
	public void destroy() 
	{
		super.destroy();

		if (sources != null) 
		{
			sources.clear();
			sources = null;
		}

		if (primitives != null) 
		{
			for (DaePrimitive primitive: primitives) 
			{
				primitive.destroy();
			}
			primitives.clear();
			primitives = null;
		}

		vertices = null;
		verticesID = null;
	}

	@Override
	public void read() 
	{
		super.read();

		sources = new HashMap<String, DaeSource>();
		primitives = new Vector<DaePrimitive>();
		vertices = null;
		verticesID = null;

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("source") == 0) 
			{
				readSource(readAttribute(child, "id"));
			} 
			else if (nodeName.compareTo("vertices") == 0) 
			{
				readVertices(child);
			}
			else if (nodeName.compareTo("triangles") == 0) 
			{
				primitives.add(new DaeTriangles(child, this));
			}
		}
	}

	private DaeSource readSource(String id) 
	{
		if (id != null) 
		{
			DaeSource source = DaeDocument.getSourceByID(getNode(), id);
			if (source != null && source.getID() != null) 
			{
				if (sources.get(id) == null) 
				{
					sources.put(source.getID(), source);
				}
				return source;
			}
		} 
		return null;
	}

	private void readVertices(Node node) 
	{
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("input") == 0) 
			{
				DaeInput input = new DaeInput(child);
				if (input.getSemantic().compareTo("POSITION") == 0) 
				{
					vertices = readSource(input.getSource());
					verticesID = readAttribute(node, "id");
				}
			}
		}
	}

	public Vector<DaePrimitive> getPrimitives() {
		return primitives;
	}

	public Map<String, DaeSource> getSources() {
		return sources;
	}

	public DaeSource getVertices() {
		return vertices;
	}

	public String getVerticesID() {
		return verticesID;
	}
}
