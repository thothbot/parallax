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

package thothbot.parallax.core.client.debugger;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;

public abstract class VisualGraphAbstract
{
	private boolean isVisible = true;
	private DivElement container;
	protected DivElement text, textMin, textMax;
	protected DivElement graph;
	
	public VisualGraphAbstract()
	{
		container = Document.get().createElement("div").cast();
		container.setId(getType());
		container.setClassName("debugger-" + getType());

		text = Document.get().createElement("div").cast();
		text.setId(getType() + "Text");
		text.setClassName("debugger-" + getType() + "-text");
		text.setInnerHTML(getType().toUpperCase());
		container.appendChild( text );

		graph = Document.get().createElement("div").cast();
		graph.setId(getType() + "Graph");
		graph.setClassName("debugger-" + getType() + "-graph");		
		container.appendChild( graph );
		
		DivElement fpsGraphContainer = Document.get().createElement("div").cast();
		fpsGraphContainer.setClassName("debugger-graph-text");
		container.appendChild( fpsGraphContainer );
		textMax = Document.get().createElement("div").cast();
		textMax.setInnerHTML("max");
		textMax.setClassName("debugger-" + getType() + "-text-max");
		fpsGraphContainer.appendChild( textMax );

		textMin = Document.get().createElement("div").cast();
		textMin.setInnerHTML("min");
		textMin.setClassName("debugger-" + getType() + "-text-min");
		fpsGraphContainer.appendChild( textMin );

		while ( graph.getChildNodes().getLength() < 74 ) 
		{
			SpanElement bar = Document.get().createElement("span").cast();
			graph.appendChild( bar );
		}
	}
	
	protected abstract String getType();
	protected abstract void update(double time);
	
	public DivElement getContainer() {
		return this.container;
	}

	public boolean isVisible() {
		return this.isVisible;
	}

	public void show() 
	{
		container.getStyle().setDisplay(Display.BLOCK);
		this.isVisible = true;
	}
	
	public void hide() 
	{
		container.getStyle().setDisplay(Display.NONE);
		this.isVisible = false;
	}
	
	protected void updateGraph( DivElement dom, double value ) 
	{
		DivElement child = (DivElement) dom.appendChild( dom.getFirstChild() );
		child.getStyle().setHeight((int)value, Unit.PX);
	}
}
