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

package thothbot.parallax.loader.shared.dae;

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeController extends DaeIdElement 
{
	private DaeSkin skin;

	public DaeController(Node node) 
	{
		super(node);
		
		Log.debug("DaeController() " + toString());
	}
	
	public DaeSkin getSkin() {
		return this.skin;
	}

	public static Map<String, DaeController> parse(DaeDocument document)
	{
		Map<String, DaeController> retval = new HashMap<String, DaeController>();
		
		Node lib = document.getDocument().getElementsByTagName("library_controllers").item(0);
		NodeList list = ((Element)lib).getElementsByTagName("controller"); 
		for (int i = 0; i < list.getLength(); i++) 
		{
			DaeController controller = new DaeController(list.item(i));
			if (controller.getID() != null) 
			{
				retval.put(controller.getID(), controller);
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

			if (nodeName.compareTo("skin") == 0) 
			{
				skin = new DaeSkin(child);
			}
		}
	}
}
