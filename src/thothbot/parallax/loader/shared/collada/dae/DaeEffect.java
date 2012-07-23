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

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeEffect extends DaeIdElement 
{

	private DaeShader shader;
	
	public DaeEffect(Node node) 
	{
		super(node);
		
		Log.debug("DaeEffect() " + toString()); 
	}
	
	public DaeShader getShader() {
		return this.shader;
	}
	
	public static Map<String, DaeEffect> parse(DaeDocument document)
	{
		Map<String, DaeEffect> retval = new HashMap<String, DaeEffect>();
		
		Node lib = document.getDocument().getElementsByTagName("library_effects").item(0);
		NodeList list = ((Element)lib).getElementsByTagName("effect"); 
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);

			DaeEffect effect = new DaeEffect(child);
			if (effect.getID() != null) 
			{
				retval.put(effect.getID(), effect);
			}
		}

		return retval;
	}

	@Override
	public void read() 
	{
		super.read();

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("profile_COMMON") == 0) 
			{
				readProfileCommon(child);
			}
		}
	}

	private void readProfileCommon(Node node)
	{
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("technique") == 0) 
			{
				readTechnique(child);
			}
		}
	}

	private void readTechnique(Node node) 
	{
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("constant") == 0
					|| nodeName.compareTo("lambert") == 0
					|| nodeName.compareTo("blinn") == 0
					|| nodeName.compareTo("phong") == 0
			) {
				this.shader = new DaeShader(child);
			}
		}
	}
}
