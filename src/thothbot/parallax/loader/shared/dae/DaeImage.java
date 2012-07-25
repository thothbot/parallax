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

public class DaeImage extends DaeIdElement 
{
	private String initFrom;
	
	public DaeImage(Node node) 
	{
		super(node);
		
		Log.debug("DaeImage() " + toString());
	}
	
	public String getInitFrom() {
		return this.initFrom;
	}
	
	public static Map<String, DaeImage> parse(DaeDocument document)
	{
		Map<String, DaeImage> retval = new HashMap<String, DaeImage>();
		
		Node lib = document.getDocument().getElementsByTagName("library_images").item(0);
		NodeList list = ((Element)lib).getElementsByTagName("image"); 
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);

			DaeImage image = new DaeImage(child);
			if (image.getID() != null) 
			{
				retval.put(image.getID(), image);
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
			if (nodeName.compareTo("init_from") == 0) 
			{
				this.initFrom = child.getFirstChild().getNodeValue();
			}
		}
	}
	
	public String toString()
	{
		return super.toString() + ", initFrom=" + this.initFrom;
	}
}
