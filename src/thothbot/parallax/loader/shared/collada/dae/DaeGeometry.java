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

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeGeometry extends DaeIdElement
{
	DaeMesh mesh;
	
	public DaeGeometry(Node node)
	{
		super(node);
		
		Log.debug("DaeGeometry() " + toString()); 
	}

	public static Map<String, DaeGeometry> parse(DaeDocument document)
	{
		Map<String, DaeGeometry> retval = new HashMap<String, DaeGeometry>();
		
		NodeList list = document.getDocument().getElementsByTagName("geometry");
		for (int i = 0; i < list.getLength(); i++) 
		{
			DaeGeometry geometry = new DaeGeometry(list.item(i));
			if (geometry.getID() != null) 
			{
				retval.put(geometry.getID(), geometry);
			}
		}
		
		return retval;
	}

	@Override
	public void read()
	{
		super.read();

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("mesh") == 0) 
			{
				mesh = new DaeMesh(child);
			}
		}
	}
	
	public DaeMesh getMesh()
	{
		return this.mesh;
	}
}
