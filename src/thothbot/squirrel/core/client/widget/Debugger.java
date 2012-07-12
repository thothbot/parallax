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

package thothbot.squirrel.core.client.widget;

import thothbot.squirrel.core.client.renderers.WebGLRenderInfo;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class Debugger extends FlowPanel
{
	private WebGLRenderInfo info;
	
	FlowPanel frameRate;
	FlowPanel renderingInfo;
	
	private Label frames;
	private Label duration;
	
	private Label string1;
	private Label string2;
	
	public Debugger(WebGLRenderInfo info)
	{
		super();
		this.setStyleName("common-panel", true);
		this.setStyleName("debug-panel", true);
		this.info = info;
		
		this.frameRate = new FlowPanel();
		this.frameRate.setStyleName("debug-panel-element");
		this.frameRate.getElement().getStyle().setProperty("borderRight", "1px solid #999999");
		this.add(this.frameRate);
		
		this.renderingInfo = new FlowPanel();
		this.renderingInfo.setStyleName("debug-panel-element");
		this.add(this.renderingInfo);
		
		this.frames = new Label();
		this.duration = new Label();
		this.string1 = new Label();
		this.string2 = new Label();
		
		this.frameRate.add(this.frames);
		this.frameRate.add(this.duration);
		
		this.renderingInfo.add(this.string1);
		this.renderingInfo.add(this.string2);
	}
	
	public void update()
	{
		long elapsedTime = this.info.getTimer().render.elapsedMillis();
		double frames = 1.0 / elapsedTime * 1000.0;
		
		String fs = NumberFormat.getFormat("0.0").format((frames > 60) ? 60 : frames);
		String ms = NumberFormat.getDecimalFormat().format((elapsedTime < 0) ? 0 : elapsedTime);

		this.frames.setText(fs + " fs");
		this.duration.setText(ms + " ms");
		
		this.string1.setText(
				"[R] C:" + this.info.getRender().calls
				+", F:" + this.info.getRender().faces
				+", P:" + this.info.getRender().points
				+", V:" + this.info.getRender().vertices
		);

		this.string2.setText(
				"[M] G:" + this.info.getMemory().geometries
				+", P:" + this.info.getMemory().programs
				+", T:" + this.info.getMemory().textures
		);
	}
}
