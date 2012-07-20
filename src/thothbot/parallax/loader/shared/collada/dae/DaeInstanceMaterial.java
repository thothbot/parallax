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

public class DaeInstanceMaterial extends DaeElement 
{
	private String symbol;
	private String target;

	public DaeInstanceMaterial(DaeDocument document) 
	{
		super(document);
	}

	public DaeInstanceMaterial(DaeDocument document, Node node) 
	{
		super(document, node);
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);
		symbol = readAttribute(node, "symbol", true);
		target = readAttribute(node, "target", true);
	}

	public String getSymbol() {
		return symbol;
	}

	public String getTarget() {
		return target;
	}
}
