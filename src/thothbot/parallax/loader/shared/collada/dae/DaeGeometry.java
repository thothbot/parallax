/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.loader.shared.collada.dae;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeGeometry extends DaeElement 
{
	private DaeMesh mesh;

	public DaeGeometry(DaeDocument document) 
	{
		super(document);
	}

	public DaeGeometry(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void destroy() 
	{
		super.destroy();

		if (mesh != null) 
		{
			mesh.destroy();
			mesh = null;
		}
	}

	@Override
	public void read(Node node)
	{
		super.read(node);

		mesh = null;

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("mesh") == 0) 
			{
				mesh = new DaeMesh(getDocument(), child);
			}
		}
	}

	public DaeMesh getMesh() 
	{
		return mesh;
	}
}
