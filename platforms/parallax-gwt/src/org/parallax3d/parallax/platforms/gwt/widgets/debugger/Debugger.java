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

package org.parallax3d.parallax.platforms.gwt.widgets.debugger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;

/**
 * Debugger widget.
 * 
 * @author thothbot
 *
 */
public class Debugger extends FlowPanel
{
	public interface Resources extends ClientBundle
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("debugger.css")
		@CssResource.NotStrict
		CssResource css();
	}

	private GLRendererInfo info;
	
	VisualGraph graph;
	FlowPanel renderingInfo;
	
	private Label string1;
	private Label string2;
	
	public Debugger(GLRendererInfo info)
	{
		super();
		// Loading specific styles
		Resources.INSTANCE.css().ensureInjected();

		this.setStyleName("debug-panel");
		this.info = info;
		
		this.graph = new VisualGraph();
		this.add(graph);
		
		this.renderingInfo = new FlowPanel();
		this.renderingInfo.setStyleName("debug-panel-element");
		this.add(this.renderingInfo);
		
		this.string1 = new Label();
		this.string2 = new Label();
		
		this.renderingInfo.add(this.string1);
		this.renderingInfo.add(this.string2);
	}
	
	/**
	 * This method will refresh debugger information.
	 */
	public void update()
	{
		this.graph.update();
		
		this.string1.setText( 
				"F:" + this.info.getRender().faces
				+", P:" + this.info.getRender().points
				+", V:" + this.info.getRender().vertices
		);

		this.string2.setText(
				"C:" + this.info.getRender().calls
				+ ", G:" + this.info.getMemory().geometries
				+", P:" + this.info.getMemory().programs
				+", T:" + this.info.getMemory().textures
		);
	}
}
