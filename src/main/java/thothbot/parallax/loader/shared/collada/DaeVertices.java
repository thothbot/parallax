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
