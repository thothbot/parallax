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

import java.util.List;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeSource extends DaeElement 
{
	public enum Type {
		INVALID,
		FLOAT,
		INT,
		NAME,
		BOOLEAN
	}

	private DaeAccessor accessor;
	private Type type;
	private DaeArrayData data;

	public DaeSource(DaeDocument document) 
	{
		super(document);
	}
	public DaeSource(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);

		this.type = Type.INVALID;

		NodeList list = node.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("float_array") == 0) 
			{
				this.type = Type.FLOAT;
				data = new DaeArrayFloat(getDocument(), child);
			} 
			else if (nodeName.compareTo("technique_common") == 0) 
			{
				readTechniqueCommon(child);
			}
		}
	}

	public DaeAccessor getAccessor() {
		return accessor;
	}

	public float[] getFloats() 
	{
		if (data instanceof DaeArrayFloat) 
		{
			return ((DaeArrayFloat) data).getData();
		} 
		else 
		{
			return null;
		}
	}

	public DaeArrayData getData() {
		return data;
	}

	public Type getType() {
		return type;
	}

	public float[] readFloats(int index) 
	{
		float[] floats = getFloats();
		if (floats != null && floats.length > 0 && accessor != null) 
		{
			List<DaeParam> params = accessor.getParams();
			float[] result = new float[params.size()];
			for (int i = 0; i < params.size(); i++) 
			{
				result[i] = floats[index + i];
			}
			return result;
		}
		return null;
	}

	private void readTechniqueCommon(Node node) 
	{
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("accessor") == 0) 
			{
				accessor = new DaeAccessor(getDocument(), child);
			}
		}
	}
}
