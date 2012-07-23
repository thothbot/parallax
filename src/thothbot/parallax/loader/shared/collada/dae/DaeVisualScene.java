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
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.loader.shared.collada.dae;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

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
		
		NodeList list = document.getDocument().getElementsByTagName("visual_scene");
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
