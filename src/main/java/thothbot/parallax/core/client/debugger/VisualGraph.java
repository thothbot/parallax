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

import thothbot.parallax.core.resources.CoreResources;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.FocusWidget;

public class VisualGraph extends FocusWidget implements MouseDownHandler
{
	private final DivElement container;
	private VisualGraphAbstract visualGraphFps, visualGraphMs;
	
	public VisualGraph()
	{
		container = Document.get().createElement("div").cast();
		container.setId("graph");
		container.setClassName("debugger-graph-container");
		setElement(container);
		
		// Loading specific styles
		CoreResources.INSTANCE.debuggerCss().ensureInjected();
		addMouseDownHandler(this);
		
		visualGraphFps = new VisualGraphFps();
		container.appendChild(visualGraphFps.getContainer());
		visualGraphMs = new VisualGraphMs();
		visualGraphMs.hide();
		container.appendChild(visualGraphMs.getContainer());
	}

	@Override
	public void onMouseDown(MouseDownEvent event) 
	{
		event.preventDefault(); 
		if(visualGraphFps.isVisible()) 
		{
			visualGraphFps.hide();
			visualGraphMs.show();
		}
		else
		{
			visualGraphMs.hide();
			visualGraphFps.show();
		}
	}

	public void update() 
	{
		double time = Duration.currentTimeMillis();
		visualGraphFps.update(time);
		visualGraphMs.update(time);
	}
}
