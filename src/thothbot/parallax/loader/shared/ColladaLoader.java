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

package thothbot.parallax.loader.shared;

import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.loader.shared.collada.DaeDocument;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class ColladaLoader extends XHRLoader
{
	private DaeDocument daeDocument;
	private Document document;
	
	public ColladaLoader(String url, ModelLoadHandler modelLoadHandler) 
	{
		super(url, modelLoadHandler);
		
		load();
	} 

	protected AbstractGeometry parse(String xmlString) 
	{
		document = XMLParser.parse(xmlString);
		daeDocument = new DaeDocument(document);
		
		return null;
	}

	public DaeDocument getDaeDocument() {
		return daeDocument;
	}

	public Document getDocument() {
		return document;
	}
}
