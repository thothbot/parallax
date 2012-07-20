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

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeBindMaterial extends DaeElement 
{

	List<DaeInstanceMaterial> materials;

	public DaeBindMaterial(DaeDocument document) 
	{
		super(document);
	}

	public DaeBindMaterial(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node)
	{
		super.read(node);

		materials = new ArrayList<DaeInstanceMaterial>();
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("technique_common") == 0) 
			{
				readTechniqueCommon(child);
			}
		}
	}

	private void readTechniqueCommon(Node node) 
	{
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("instance_material") == 0) 
			{
				DaeInstanceMaterial material = new DaeInstanceMaterial(getDocument(), child);
				materials.add(material);
			}
		}
	}

	public List<DaeInstanceMaterial> getMaterials() {
		return materials;
	}
}