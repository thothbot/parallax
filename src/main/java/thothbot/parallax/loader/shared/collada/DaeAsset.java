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

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeAsset extends DaeElement
{
	public enum AXIS {
		X,
		Y,
		Z
	};
	
	private double unit = 1.0;
	private AXIS upAxis = AXIS.Z;
	
	public DaeAsset(Node node)
	{
		super(node);

		Log.debug("DaeAsset(): " + toString());
	}
	
	public static DaeAsset parse(DaeDocument document)
	{
		return new DaeAsset(document.getDocument().getElementsByTagName("asset").item(0));
	}

	@Override
	public void read()
	{
		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			if (child.getNodeName().compareTo("unit") == 0) 
			{
				this.unit = Float.parseFloat( ((Element)child).getAttribute("meter") );
			}
			else if (child.getNodeName().compareTo("up_axis") == 0) 
			{ 
				switch(child.getFirstChild().getNodeValue().charAt(0))
				{
				case 'X': this.upAxis = AXIS.X; break;
				case 'Y': this.upAxis = AXIS.Y; break;
				case 'Z': this.upAxis = AXIS.Z; break;
				}
			}
		}
	}
	
	public double getUnit() {
		return this.unit;
	}
	
	public AXIS getUpAxis() {
		return this.upAxis;
	}
	
	public String toString()
	{
		return "{unit="+ this.unit + ", up="+ this.upAxis +"}";
	}
}
