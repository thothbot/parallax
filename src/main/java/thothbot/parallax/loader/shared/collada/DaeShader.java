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

public class DaeShader extends DaeElement 
{
	private String type;
	private Map<String, DaeColorOrTexture> colorOrTeture;
	private float shininess;
	private float reflectivity;
	private float transparency;
	
	public DaeShader(Node node) 
	{
		super(node);
		
		Log.debug("DaeShader()" + toString());
	}
	
	public String getType() {
		return this.type;
	}
	
	public float getShininess() {
		return this.shininess;
	}
	
	public float getReflectivity() {
		return this.reflectivity;
	}
	
	public float getTransparency() {
		return this.transparency;
	}
	
	public Map<String, DaeColorOrTexture> getColorOrTeture() {
		return this.colorOrTeture;
	}

	@Override
	public void read() 
	{
		this.type = getNode().getNodeName();
		
		colorOrTeture = new HashMap<String, DaeColorOrTexture>();
		
		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.compareTo("ambient") == 0
					|| nodeName.compareTo("emission") == 0
					|| nodeName.compareTo("diffuse") == 0
					|| nodeName.compareTo("specular") == 0
					|| nodeName.compareTo("transparent") == 0
			){
				colorOrTeture.put(nodeName, new DaeColorOrTexture(child));
			}
			else if(nodeName.compareTo("shininess") == 0)
			{
				this.shininess = Float.parseFloat(
						((Element)child).getElementsByTagName("float").item(0).getFirstChild().getNodeValue());
			}
			else if(nodeName.compareTo("reflectivity") == 0)
			{
				this.reflectivity = Float.parseFloat(
						((Element)child).getElementsByTagName("float").item(0).getFirstChild().getNodeValue());
			}
			else if(nodeName.compareTo("transparency") == 0)
			{
				this.transparency = Float.parseFloat(
						((Element)child).getElementsByTagName("float").item(0).getFirstChild().getNodeValue());
			}
		}
	}
		
	public String toString()
	{
		return "{type=" + this.type + ", shininess=" + this.shininess + ", reflectivity=" + this.reflectivity + ", transparency=" + this.transparency + "}";
	}

}
