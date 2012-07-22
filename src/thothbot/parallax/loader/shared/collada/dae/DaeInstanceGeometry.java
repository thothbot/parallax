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

public class DaeInstanceGeometry extends DaeElement {

	private String url;
	private DaeGeometry geometry;
	private List<DaeBindMaterial> boundMaterials;

	public DaeInstanceGeometry(Node node) 
	{
		super(node);
	}

	@Override
	public void read() 
	{
		super.read();

		url = readAttribute(getNode(), "url", true);

		boundMaterials = new ArrayList<DaeBindMaterial>();

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("bind_material") == 0) 
			{
				boundMaterials.add(new DaeBindMaterial(child));
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
