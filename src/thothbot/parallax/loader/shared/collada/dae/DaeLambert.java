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

public class DaeLambert extends DaeConstant 
{

	private DaeColorOrTexture ambient;
	private DaeColorOrTexture diffuse;

	public DaeLambert(DaeDocument document) 
	{
		super(document);
	}

	public DaeLambert(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);
	}

	public DaeColorOrTexture getAmbient() {
		return ambient;
	}

	public DaeColorOrTexture getDiffuse() {
		return diffuse;
	}

	public void setAmbient(DaeColorOrTexture value) {
		ambient = value;
	}

	public void setDiffuse(DaeColorOrTexture value) {
		diffuse = value;
	}
}