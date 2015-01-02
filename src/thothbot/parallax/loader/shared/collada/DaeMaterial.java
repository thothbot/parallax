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

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeMaterial extends DaeIdElement 
{

	private String instanceEffect;
	
	public DaeMaterial(Node node) {
		super(node);
		
		Log.debug("DaeMaterial() " + toString());
	}
	
	public String getInstanceEffect() {
		return this.instanceEffect;
	}
	
	public static Map<String, DaeMaterial> parse(DaeDocument document)
	{
		Map<String, DaeMaterial> retval = new HashMap<String, DaeMaterial>();
		
		Node lib = document.getDocument().getElementsByTagName("library_materials").item(0);
		NodeList list = ((Element)lib).getElementsByTagName("material"); 
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);

			DaeMaterial material = new DaeMaterial(child);
			if (material.getID() != null) 
			{
				retval.put(material.getID(), material);
			}
		}

		return retval;
	}
	
	@Override
	public void read() {
		super.read();
		
		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("instance_effect") == 0) 
			{
				DaeElement effect = new DaeElement(child) {		
					@Override
					public void read() {}
				};
				this.instanceEffect = effect.readAttribute("url", true);
			}
		}
	}

	public String toString()
	{
		return super.toString() + ", instanceEffect=" + this.instanceEffect;
	}
}
