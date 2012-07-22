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

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeDocument  
{
	private Document document;
	private DaeNode rootNode;
	private DaeAsset asset;
	
	private Map<String, DaeGeometry> geometries;

	public DaeDocument(Document document) 
	{
		this.document = document;
		
		this.asset = DaeAsset.parse(this);
		this.geometries = DaeGeometry.parse(this);	
	}
	
	public Document getDocument() {
		return document;
	}

	public DaeNode getRootNode() {
		return rootNode;
	}
	
	/**
	 * Gets a geometry by its ID.
	 * 
	 * @param id
	 */
	public DaeGeometry getGeometryByID(String id) 
	{
		if (geometries.containsKey(id)) 
		{
			return geometries.get(id);
		} 
		else 
		{
//			Node node = XPath.evaluateSingle(document, "//geometry[@id='"+id+"']");
//			if (node != null) 
//			{
//				DaeGeometry geometry = new DaeGeometry(this, node);
//				if (geometry.getID() != null) 
//				{
//					geometries.put(geometry.getID(), geometry);
//					return geometry;
//				}
//			}
		}
		return null;
	}

	/**
	 * Gets a source by its ID.
	 * 
	 * @param id
	 */
	public static DaeSource getSourceByID(Node node, String id) 
	{
		NodeList list = ((Element)node).getElementsByTagName("source");
		for (int i = 0; i < list.getLength(); i++) 
		{
			if(((Element)list.item(i)).getAttribute("id").compareTo(id) == 0)
			{
				return new DaeSource(list.item(i));
			}
		}
		
		Log.error("Could not find source with id=" + id + " !");

		return null;
	}

//	public void readScene() 
//	{
////		Node node = XPath.evaluateSingle(document, ".//scene/instance_visual_scene/@url");
////
////		if (node != null) 
////		{
////			readVisualScene(node.getNodeValue());
////		} 
////		else 
////		{
////			Log.warn("Could not find a scene!");
////		}
//
////		List<Node> list = XPath.evaluate(document, "//effect");
////		for (Node effectNode: list) 
////		{
////			DaeEffect effect = new DaeEffect(this, effectNode);
////		}
//	}

//	public void readVisualScene(String url) 
//	{
////		if (url.startsWith("#")) 
////		{
////			url = url.substring(1);
////		}
////		Node node = XPath.evaluateSingle(document, "//visual_scene[@id='"+ url +"']");
////
////		if (node != null) 
////		{
////			rootNode = new DaeNode(this, node);
////		}
//	}
}
