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
import java.util.List;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeVertices extends DaeIdElement 
{
	DaeInput input;
	
	public DaeVertices(Node node) 
	{
		super(node);
	}
	
	public DaeInput getInput() {
		return this.input;
	}

	@Override
	public void read() 
	{
		super.read();

		List<DaeInput> inputs = new ArrayList<DaeInput>();
		NodeList list = getNode().getChildNodes();

		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("input") == 0) 
			{
				input = new DaeInput(child);
//				if (getMesh().getVerticesID().compareTo(input.getSource()) == 0) 
//				{
//					input.setSource(getMesh().getVertices().getID());
//				}
//				inputs.add(input);
			} 
		}
	}
}
