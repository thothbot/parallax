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

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeSource extends DaeIdElement 
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
	private DaeArrayFloat data;

	public DaeSource(Node node) 
	{
		super(node);
	}

	public DaeAccessor getAccessor() {
		return accessor;
	}
	
	public DaeArrayFloat getData() {
		return data;
	}

	public Type getType() {
		return type;
	}
	
	@Override
	public void read() 
	{
		super.read();

		this.type = Type.INVALID;

		NodeList list = getNode().getChildNodes();

		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("float_array") == 0) 
			{
				this.type = Type.FLOAT;
				data = new DaeArrayFloat(child);
			} 
			else if (nodeName.compareTo("technique_common") == 0) 
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
			if (nodeName.compareTo("accessor") == 0) 
			{
				accessor = new DaeAccessor(child);
			}
		}
	}

//	public float[] getFloats() 
//	{
//		if (data instanceof DaeArrayFloat) 
//		{
//			return ((DaeArrayFloat) data).getData();
//		} 
//		else 
//		{
//			return null;
//		}
//	}
//
//	public float[] readFloats(int index) 
//	{
//		float[] floats = getFloats();
//		if (floats != null && floats.length > 0 && accessor != null) 
//		{
//			List<DaeParam> params = accessor.getParams();
//			float[] result = new float[params.size()];
//			for (int i = 0; i < params.size(); i++) 
//			{
//				result[i] = floats[index + i];
//			}
//			return result;
//		}
//		return null;
//	}
}
