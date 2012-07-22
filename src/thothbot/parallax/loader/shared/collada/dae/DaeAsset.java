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
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.loader.shared.collada.dae;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeAsset 
{
	public enum AXIS {
		X,
		Y,
		Z
	};
	
	private float unit = 1.0f;
	private AXIS upAxis = AXIS.Z;
	
	public static DaeAsset parse(DaeDocument document)
	{
		DaeAsset asset = new DaeAsset();
		
		Node node = document.getDocument().getElementsByTagName("asset").item(0);

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			if (child.getNodeName().compareTo("unit") == 0) 
			{
				asset.unit = Float.parseFloat( ((Element)child).getAttribute("meter") );
			}
			else if (child.getNodeName().compareTo("up_axis") == 0) 
			{ 
				switch(child.getFirstChild().getNodeValue().charAt(0))
				{
				case 'X': asset.upAxis = AXIS.X; break;
				case 'Y': asset.upAxis = AXIS.Y; break;
				case 'Z': asset.upAxis = AXIS.Z; break;
				}
			}
		}
		
		Log.debug("DaeAsset(): " + asset.toString());
		return asset;
	}
	
	public float getUnit() {
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
