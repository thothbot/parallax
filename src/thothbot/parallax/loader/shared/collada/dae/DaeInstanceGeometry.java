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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeInstanceGeometry extends DaeElement {

	private String url;
	private DaeGeometry geometry;
	private List<DaeBindMaterial> boundMaterials;

	public DaeInstanceGeometry(DaeDocument document) 
	{
		super(document);
	}

	public DaeInstanceGeometry(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);

		url = readAttribute(node, "url", true);

		boundMaterials = new ArrayList<DaeBindMaterial>();

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("bind_material") == 0) 
			{
				boundMaterials.add(new DaeBindMaterial(getDocument(), child));
			}
		}
	}

	public List<DaeBindMaterial> getBoundMaterials() 
	{
		return boundMaterials;
	}

	public DaeGeometry getGeometry() {
		return geometry;
	}

	public String getURL() {
		return url;
	}

	public void setGeometry(DaeGeometry value) {
		geometry = value;
	}
}
