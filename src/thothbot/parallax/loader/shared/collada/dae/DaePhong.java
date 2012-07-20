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

import com.google.gwt.xml.client.Node;

public class DaePhong extends DaeLambert 
{

	private DaeColorOrTexture specular;
	private float shininess = 0.0f;

	public DaePhong(DaeDocument document) 
	{
		super(document);
	}

	public DaePhong(DaeDocument document,  Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);
	}

	public DaeColorOrTexture getSpecular() {
		return specular;
	}

	public float getShininess() {
		return shininess;
	}

	public void setSpecular(DaeColorOrTexture value) {
		specular = value;
	}

	public void setShininess(float value) {
		shininess = value;
	}
}
