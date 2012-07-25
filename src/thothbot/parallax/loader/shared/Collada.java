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

package thothbot.parallax.loader.shared;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.loader.shared.dae.DaeDocument;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class Collada 
{
	public interface Callback 
	{
		public void onLoaded();
	}

	private DaeDocument daeDocument;
	private Document document;

//	public Element getSourceElementById(String id) 
//	{
//		return (Element) XPath.evaluateSingle(document, "//source[@id='"+id+"']");
//	}

	public void load(String url, final Callback callback) throws RequestException 
	{
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url);
		rb.sendRequest(null, new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) 
			{
				document = parseXML(response.getText());
				callback.onLoaded();
			}

			@Override
			public void onError(Request request, Throwable exception) 
			{
				Log.error("Error while loading COLLADA file.");
			}
		});
	}

	public Document parseXML(String xmlString) 
	{
		document = XMLParser.parse(xmlString);

		daeDocument = new DaeDocument(document);

		return document;
	}

	public Document getDocument() {
		return document;
	}
}
