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

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Color3f;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeColorOrTexture extends DaeElement 
{

	private boolean isTexture;
	private Color3f color;

	public DaeColorOrTexture(Node node) 
	{
		super(node);
		
		Log.debug("DaeColorOrTexture() " + toString()); 
	}

	@Override
	public void read() 
	{
		this.color = new Color3f();
		this.color.setRGB( (float)Math.random(), (float)Math.random(), (float)Math.random() );

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("color") == 0) 
			{
				DaeElement color = new DaeDummyElement(child);
				float[] rgba = color.readFloatArray();
				this.color.setRGB( rgba[0], rgba[1], rgba[2] );
			} 
			else if (nodeName.compareTo("texture") == 0) 
			{
				this.isTexture = true;
			}
		}
	}

	public boolean isTexture() {
		return isTexture;
	}
	
	public String toString()
	{
		if(isTexture())
			return "texture";
		else
			return "color: " + this.color;
	}
}