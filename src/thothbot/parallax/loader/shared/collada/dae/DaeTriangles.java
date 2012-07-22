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

public class DaeTriangles extends DaePrimitive 
{
	private int[] p;

	public DaeTriangles(Node node, DaeMesh mesh) 
	{
		super(node, mesh);
	}

	@Override
	public void destroy() 
	{
		super.destroy();

		p = null;
	}

	@Override
	public void read() 
	{
		super.read();

		p = null;

		List<DaeInput> inputs = new ArrayList<DaeInput>();
		NodeList list = getNode().getChildNodes();

		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("input") == 0) 
			{
				DaeInput input = new DaeInput(child);
				if (getMesh().getVerticesID().compareTo(input.getSource()) == 0) 
				{
					input.setSource(getMesh().getVertices().getID());
				}
				inputs.add(input);
			} 
			else if (nodeName.compareTo("p") == 0) 
			{
				p = readIntArray(child);
			}
		}

		if (p != null && p.length > 0 && inputs.size() > 0) 
		{
			readTriangles(p, inputs);
		}
	}

	private void readTriangles(int[] p, List<DaeInput> inputs) 
	{
		int current = 0;
		int maxOffset = 0;

		for (DaeInput input: inputs)
		{
			maxOffset = Math.max(maxOffset, input.getOffset());
		}

		while (current < p.length) 
		{
			for (int i = 0; i < inputs.size(); i++) 
			{
				DaeInput input = inputs.get(i);
				DaeSource source = DaeDocument.getSourceByID(getNode().getParentNode(), input.getSource());
				int index = p[current + input.getOffset()];

				addIndex(input, index, source.getAccessor().getParams().size());
			}
			current += (maxOffset + 1);
		}
	}
}
