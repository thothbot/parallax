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

package thothbot.parallax.core.client.widget;

import thothbot.parallax.core.resources.CoreResources;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FocusWidget;

public class VisualGraph extends FocusWidget implements MouseDownHandler
{
	private final DivElement container;
	private DivElement fpsDiv, msDiv;
	private DivElement fpsText, msText, fpsTextMax, fpsTextMin;
	private DivElement fpsGraph, msGraph, msTextMax, msTextMin;
	
	private double startTime = Duration.currentTimeMillis();
	private double prevTime = startTime;
	
	private double ms = 0, msMin = Double.POSITIVE_INFINITY, msMax = 0;
	private double fps = 0, fpsMin = Double.POSITIVE_INFINITY, fpsMax = 0;
	private double frames = 0;
	private int mode = 0;
	
	public VisualGraph()
	{
		container = Document.get().createElement("div").cast();
		container.setId("graph");
		container.setClassName("debugger-graph-container");
		setElement(container);
		
		// Loading specific styles
		CoreResources.INSTANCE.graphCss().ensureInjected();
		addMouseDownHandler(this);
		
		createFpsGraph();
		createMsGraph();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) 
	{
		event.preventDefault(); 
		setMode( ++ mode % 2 );
	}

	public void update() 
	{
		double time = Duration.currentTimeMillis();

		ms = time - startTime;
		msMin = Math.min( msMin, ms );
		msMax = Math.max( msMax, ms );

		msText.setInnerText( ms + "ms");
		msTextMin.setInnerText(msMin + "");
		msTextMax.setInnerText(msMax + "");
		updateGraph( msGraph, Math.min( 30, 30 - ( ms / 200 ) * 30 ) );

		frames ++;

		if ( time > prevTime + 1000 ) {

			fps = Math.round( ( frames * 1000 ) / ( time - prevTime ) );
			fpsMin = Math.min( fpsMin, fps );
			fpsMax = Math.max( fpsMax, fps );

			fpsText.setInnerText( fps + "fps" );
			fpsTextMin.setInnerText(fpsMin + "");
			fpsTextMax.setInnerText(fpsMax + "");

			updateGraph( fpsGraph, Math.min( 30, 30 - ( fps / 100 ) * 30 ) );

			prevTime = time;
			frames = 0;
		}

		startTime = time;
	}

	public void setMode( int value ) 
	{
		mode = value;

		switch ( mode ) 
		{
		case 0:
			fpsDiv.getStyle().setDisplay(Display.BLOCK);
			msDiv.getStyle().setDisplay(Display.NONE);
			break;
		case 1:

			fpsDiv.getStyle().setDisplay(Display.NONE);
			msDiv.getStyle().setDisplay(Display.BLOCK);
			break;
		}
	}
	
	private void updateGraph( DivElement dom, double value ) 
	{
		DivElement child = (DivElement) dom.appendChild( dom.getFirstChild() );
		child.getStyle().setHeight((int)value, Unit.PX);
	}

	private void createFpsGraph()
	{
		fpsDiv = Document.get().createElement("div").cast();
		fpsDiv.setId("fps");
		fpsDiv.setClassName("debugger-fps");
		container.appendChild( fpsDiv );

		fpsText = Document.get().createElement("div").cast();
		fpsText.setId("fpsText");
		fpsText.setClassName("debugger-fps-text");
		fpsText.setInnerHTML("FPS");
		fpsDiv.appendChild( fpsText );

		fpsGraph = Document.get().createElement("div").cast();
		fpsGraph.setId("fpsGraph");
		fpsGraph.setClassName("debugger-fps-graph");		
		fpsDiv.appendChild( fpsGraph );
		
		DivElement fpsGraphContainer = Document.get().createElement("div").cast();
		fpsGraphContainer.setClassName("debugger-graph-text");
		fpsDiv.appendChild( fpsGraphContainer );
		fpsTextMax = Document.get().createElement("div").cast();
		fpsTextMax.setInnerHTML("max");
		fpsTextMax.setClassName("debugger-fps-text-max");
		fpsGraphContainer.appendChild( fpsTextMax );

		fpsTextMin = Document.get().createElement("div").cast();
		fpsTextMin.setInnerHTML("min");
		fpsTextMin.setClassName("debugger-fps-text-min");
		fpsGraphContainer.appendChild( fpsTextMin );

		while ( fpsGraph.getChildNodes().getLength() < 74 ) 
		{
			SpanElement bar = Document.get().createElement("span").cast();
			fpsGraph.appendChild( bar );
		}
	}
	
	private void createMsGraph()
	{
		msDiv = Document.get().createElement("div").cast();
		msDiv.setId("ms");
		msDiv.setClassName("debugger-ms");
		container.appendChild( msDiv );
		
		msText = Document.get().createElement("div").cast();
		msText.setId("msText");
		msText.setClassName("debugger-ms-text");
		msText.setInnerHTML("MS");
		msDiv.appendChild( msText );
		
		msGraph = Document.get().createElement("div").cast();
		msGraph.setId("msGraph");
		msGraph.setClassName("debugger-ms-graph");		
		msDiv.appendChild( msGraph );
		
		DivElement msGraphContainer = Document.get().createElement("div").cast();
		msGraphContainer.setClassName("debugger-graph-text");
		msDiv.appendChild( msGraphContainer );
		msTextMax = Document.get().createElement("div").cast();
		msTextMax.setInnerHTML("max");
		msTextMax.setClassName("debugger-ms-text-max");
		msGraphContainer.appendChild( msTextMax );

		msTextMin = Document.get().createElement("div").cast();
		msTextMin.setInnerHTML("min");
		msTextMin.setClassName("debugger-ms-text-min");
		msGraphContainer.appendChild( msTextMin );
		
		while ( msGraph.getChildNodes().getLength() < 74 ) 
		{
			SpanElement bar = Document.get().createElement("span").cast();
			msGraph.appendChild( bar );
		}
	}
}
