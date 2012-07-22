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
import java.util.List;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeNode extends DaeElement 
{
	private List<DaeNode> nodes;
	private List<DaeInstanceGeometry> geometries;
	private List<DaeTransform> transforms;

	public DaeNode(Element element) 
	{
		super(element);
	}

	public DaeNode(DaeDocument document, Node node) 
	{
		super(node);
	}

	public DaeInstanceGeometry getGeometryByID(String id) 
	{
		for (DaeInstanceGeometry geometry: geometries) 
		{
			if (geometry.getURL() != null && geometry.getURL().compareTo(id) == 0) 
			{
				return geometry;
			}
		}
		return null;
	}

	public DaeNode getNodeByID(String id) 
	{
		for (DaeNode node: nodes) 
		{
			if (node.getID().compareTo(id) == 0) 
			{
				return node;
			}
		}
		return null;
	}

	public List<DaeInstanceGeometry> getGeometries() {
		return geometries;
	}

	public List<DaeNode> getNodes() {
		return nodes;
	}

	@Override
	public void read() 
	{
		super.read();

		nodes = new ArrayList<DaeNode>();
		geometries = new ArrayList<DaeInstanceGeometry>();
		transforms = new ArrayList<DaeTransform>();

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("node") == 0) 
			{
				DaeNode n = new DaeNode((Element) child);
				if (n.getID() != null) 
				{
					nodes.add(n);
				}
			} 
			else if (nodeName.compareTo("matrix") == 0 || 
					   nodeName.compareTo("rotate") == 0 || 
					   nodeName.compareTo("translate") == 0 ||
					   nodeName.compareTo("skew") == 0 ||
					   nodeName.compareTo("lookat") == 0 ||
					   nodeName.compareTo("scale") == 0) 
			{
				readTransform(child);
			} 
			else if (nodeName.compareTo("instance_geometry") == 0) 
			{
				String geomId = readAttribute(child, "url", true);
				if (geomId != null) 
				{
//					DaeGeometry geometry = getDocument().getGeometryByID(geomId);
//					if (geometry != null && geometry.getID() != null) 
//					{
//						DaeInstanceGeometry instanceGeometry = new DaeInstanceGeometry(child);
//						instanceGeometry.setGeometry(geometry);
//						geometries.add(instanceGeometry);
//					}
				}
			}
		}
	}

	private void readTransform(Node node) 
	{
		DaeTransform transform = new DaeTransform(node);
		if (transform.getData() != null && transform.getData().length > 0) 
		{
			transforms.add(transform);
		}
	}
}
