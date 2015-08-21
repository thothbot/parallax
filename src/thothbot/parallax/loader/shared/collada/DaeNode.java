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

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeNode extends DaeIdElement 
{
	private List<DaeNode> nodes;
	private List<DaeTransform> transforms;
	//	private List<DaeInstanceGeometry> geometries;

	public DaeNode(Node node) 
	{
		super(node);
		
		Log.debug("DaeNode() " + toString()); 
	}
	
	public List<DaeNode> getNodes() {
		return nodes;
	}

	@Override
	public void read() 
	{
		super.read();

		nodes = new ArrayList<DaeNode>();
		transforms = new ArrayList<DaeTransform>();
//		geometries = new ArrayList<DaeInstanceGeometry>();

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("node") == 0) 
			{
				DaeNode n = new DaeNode(child);
				if (n.getID() != null) 
				{
					nodes.add(n);
				}
			} 
			else if (nodeName.compareTo("matrix") == 0)
			{
				transforms.add(new DaeTransformMatrix(child));
			}
			else if (nodeName.compareTo("rotate") == 0)
			{
				transforms.add(new DaeTransformRotate(child));
			}
			else if (nodeName.compareTo("translate") == 0 ||
					nodeName.compareTo("scale") == 0)
			{
				transforms.add(new DaeTransformVector(child));
			}
			else if(nodeName.compareTo("skew") == 0 ||
					nodeName.compareTo("lookat") == 0)
			{
				Log.error("Can not convert Transform of type " + nodeName );
			}
//			else if (nodeName.compareTo("instance_geometry") == 0) 
//			{
//				String geomId = readAttribute(child, "url", true);
//				if (geomId != null) 
//				{
//					DaeGeometry geometry = getDocument().getGeometryByID(geomId);
//					if (geometry != null && geometry.getID() != null) 
//					{
//						DaeInstanceGeometry instanceGeometry = new DaeInstanceGeometry(child);
//						instanceGeometry.setGeometry(geometry);
//						geometries.add(instanceGeometry);
//					}
//				}
//			}
		}
	}

//	public DaeInstanceGeometry getGeometryByID(String id) 
//	{
//		for (DaeInstanceGeometry geometry: geometries) 
//		{
//			if (geometry.getURL() != null && geometry.getURL().compareTo(id) == 0) 
//			{
//				return geometry;
//			}
//		}
//		return null;
//	}

//	public DaeNode getNodeByID(String id) 
//	{
//		for (DaeNode node: nodes) 
//		{
//			if (node.getID().compareTo(id) == 0) 
//			{
//				return node;
//			}
//		}
//		return null;
//	}

//	public List<DaeInstanceGeometry> getGeometries() {
//		return geometries;
//	}

//	private void readTransform(Node node) 
//	{
//		DaeTransform transform = new DaeTransform(node);
//		if (transform.getData() != null && transform.getData().length > 0) 
//		{
//			transforms.add(transform);
//		}
//	}
}
