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

import com.google.gwt.xml.client.Node;

public class DaeIdElement extends DaeElement 
{
	private String id;
	private String name;
	private String sid;
	
	public DaeIdElement(Node node) 
	{
		super( node );
	}
	
	public void destroy() 
	{
		super.destroy();
		id = name = sid = null;
	}
	
	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSID() {
		return sid;
	}
	
	public void read() 
	{
		id   = readAttribute("id"  );
		sid  = readAttribute("sid" );
		name = readAttribute("name");
	}

	public String toString()
	{
		return "id=" + this.id + ", name=" + this.name + ", sid=" + this.sid;
	}
}
