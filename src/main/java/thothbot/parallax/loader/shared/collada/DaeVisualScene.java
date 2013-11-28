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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeVisualScene extends DaeIdElement 
{

	List<DaeNode> nodes;
	
	public DaeVisualScene(Node node) 
	{
		super(node);
		
		Log.debug("DaeVisualScene() " + toString()); 
	}
	
	public static Map<String, DaeVisualScene> parse(DaeDocument document)
	{
		Map<String, DaeVisualScene> retval = new HashMap<String, DaeVisualScene>();
		
		Node lib = document.getDocument().getElementsByTagName("library_visual_scenes").item(0);
		NodeList list = ((Element)lib).getElementsByTagName("visual_scene"); 
		for (int i = 0; i < list.getLength(); i++) 
		{
			DaeVisualScene visualScene = new DaeVisualScene(list.item(i));
			if (visualScene.getID() != null) 
			{
				retval.put(visualScene.getID(), visualScene);
			}
		}
		
		return retval;
	}
	
	@Override
	public void read()
	{
		super.read();

		this.nodes = new ArrayList<DaeNode>();
		
		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("node") == 0) 
			{
				this.nodes.add( new DaeNode(child) );
			}
		}
	}
	
	public List<DaeNode> getNodes()
	{
		return this.nodes;
	}

}
