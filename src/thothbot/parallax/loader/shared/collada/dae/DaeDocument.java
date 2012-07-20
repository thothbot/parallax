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
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.mouchel.gwt.xpath.client.XPath;

public class DaeDocument  {

	private Document document;
	private DaeNode rootNode;
	private Map<String, DaeGeometry> geometries;
	private Map<String, DaeSource> sources;

	public DaeDocument() 
	{
		this.document = null;
		geometries = new HashMap<String, DaeGeometry>();
		sources = new HashMap<String, DaeSource>();
	}

	public DaeDocument(Document document) 
	{
		this();
		this.document = document;
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
			Node node = XPath.evaluateSingle(document, "//geometry[@id='"+id+"']");
			if (node != null) 
			{
				DaeGeometry geometry = new DaeGeometry(this, node);
				if (geometry.getID() != null) 
				{
					geometries.put(geometry.getID(), geometry);
					return geometry;
				}
			}
		}
		return null;
	}

	/**
	 * Gets a source by its ID.
	 * 
	 * @param id
	 */
	public DaeSource getSourceByID(String id) 
	{
		if (sources.containsKey(id)) 
		{
			return sources.get(id);
		} 
		else 
		{
			Node node = XPath.evaluateSingle(document, "//source[@id='"+id+"']");
			if (node != null) 
			{
				DaeSource source = new DaeSource(this, node);
				if (source.getID() != null) 
				{
					sources.put(source.getID(), source);
					return source;
				}
			} 
			else 
			{
				Log.error("Could not find source with id=" + id + " !");
			}
		}
		return null;
	}

	public void readScene() 
	{
		Node node = XPath.evaluateSingle(document, ".//dae:scene/dae:instance_visual_scene/@url");

		if (node != null) 
		{
			readVisualScene(node.getNodeValue());
		} 
		else 
		{
			Log.warn("Could not find a scene!");
		}

		List<Node> list = XPath.evaluate(document, "//effect");
		for (Node effectNode: list) 
		{
			DaeEffect effect = new DaeEffect(this, effectNode);
		}
	}

	public void readVisualScene(String url) 
	{
		if (url.startsWith("#")) 
		{
			url = url.substring(1);
		}
		Node node = XPath.evaluateSingle(document, "//visual_scene[@id='"+ url +"']");

		if (node != null) 
		{
			rootNode = new DaeNode(this, node);
		}
	}

	public Document getDocument() {
		return document;
	}

	public DaeNode getRootNode() {
		return rootNode;
	}
}
