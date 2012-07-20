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

public class DaeTransform extends DaeElement 
{
	private String type;
	private float[] data;

	public DaeTransform(DaeDocument document) 
	{
		super(document);
	}
	public DaeTransform(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);

		type = node.getNodeName();

		if (node.getChildNodes().getLength() == 1 && node.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE) 
		{
			String raw = node.getChildNodes().item(0).getNodeValue();
			String[] parts = raw.trim().split("\\s");

			data = new float[parts.length];
			for (int i = 0; i < parts.length; i++) 
			{
				data[i] = Float.parseFloat(parts[i]);
			}
		}
	}

	public float[] getData() 
	{
		return data;
	}

	public String getType() 
	{
		return type;
	}
}
