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

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeAccessor extends DaeElement 
{
	private String source;
	private int count;
	private int stride;
	
	private List<DaeParam> params;

	public DaeAccessor(Node node) 
	{
		super(node);
		
		Log.debug("DaeAccessor() " + toString()); 
	}

	@Override
	public void read() 
	{
		params = new ArrayList<DaeParam>();
		source = readAttribute("source", true);
		count = readIntAttribute("count", 0);
		stride = readIntAttribute("stride", 0);

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("param") == 0)
			{
				params.add(new DaeParam(child));
			}
		}
	}

	public String getSource() {
		return source;
	}

	public int getCount() {
		return count;
	}

	public int getStride() {
		return stride;
	}
	
	public List<DaeParam> getParams( ) {
		return params;
	}
	
	@Override
	public String toString()
	{
		return "{source=" + this.source + ", count=" + this.count + ", stride=" + this.stride + "}";
	}
}
