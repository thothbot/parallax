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
import com.google.gwt.xml.client.NodeList;

public class DaeConstant extends DaeElement 
{

	private DaeColorOrTexture emission;
	private DaeColorOrTexture reflective;
	private float reflectivity = 0.0f;
	private DaeColorOrTexture transparent;
	private float transparency = 0.0f;
	private float index_of_refraction = 0.0f;

	public DaeConstant(DaeDocument document) 
	{
		super(document);
	}

	public DaeConstant(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node)
	{
		super.read(node);

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("emission") == 0) 
			{
				emission = new DaeColorOrTexture(getDocument(), child);
			} 
			else if (nodeName.compareTo("reflective") == 0) 
			{
				reflective = new DaeColorOrTexture(getDocument(), child);
			} 
			else if (nodeName.compareTo("transparent") == 0) 
			{
				transparent = new DaeColorOrTexture(getDocument(), child);
			} 
			else if (nodeName.compareTo("reflectivity") == 0) 
			{
				DaeColorOrTexture cotr = new DaeColorOrTexture(getDocument(), child);
			} 
			else if (nodeName.compareTo("transparency") == 0) 
			{
				DaeColorOrTexture cott = new DaeColorOrTexture(getDocument(), child);
			}
			else if (nodeName.compareTo("index_of_refraction") == 0) 
			{
				DaeColorOrTexture coti = new DaeColorOrTexture(getDocument(), child);
			}  
		}
	}

	public DaeColorOrTexture getEmission() {
		return emission;
	}

	public float getIndexOfRefraction() {
		return index_of_refraction;
	}

	public DaeColorOrTexture getReflective() {
		return reflective;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public float getTransparency() {
		return transparency;
	}

	public DaeColorOrTexture getTransparent() {
		return transparent;
	}

	public void setEmission(DaeColorOrTexture value) {
		emission = value;
	}

	public void setIndexOfRefraction(float value) {
		index_of_refraction = value;
	}

	public void setReflective(DaeColorOrTexture value) {
		reflective = value;
	}

	public void setReflectivity(float value) {
		reflectivity = value;
	}

	public void setTransparency(float value) {
		transparency = value;
	}

	public void setTransparent(DaeColorOrTexture value) {
		transparent = value;
	}
}
