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

package thothbot.squirrel.core.client;

import thothbot.squirrel.core.client.renderers.WebGLRenderInfo;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.i18n.client.NumberFormat;

public class Debugger extends SimplePanel
{
	private WebGLRenderInfo info;
	
	FlowPanel content;
	
	private Label string1;
	private Label string2;
	
	public Debugger(WebGLRenderInfo info)
	{
		super();
		this.setStyleName("debug-panel");
		this.info = info;
		
		this.content = new FlowPanel();
		this.add(this.content);

		this.string1 = new Label();
		this.content.add(this.string1);
		
		this.string2 = new Label();
		this.content.add(this.string2);
	}
	
	public void update()
	{
		long elapsedTime = this.info.getTimer().render.elapsedMillis();
		String ms = NumberFormat.getDecimalFormat().format(elapsedTime);
		this.string1.setText("T: " + ms + "ms"
				+", RC:" + this.info.getRender().calls
				+", RF:" + this.info.getRender().faces
				+", RP:" + this.info.getRender().points
				+", RV:" + this.info.getRender().vertices
		);
		String fs = NumberFormat.getFormat("0.0").format(1.0/elapsedTime * 1000.0);
		this.string2.setText("F: " + fs + "fs"
				+" MG:" + this.info.getMemory().geometries
				+", MP:" + this.info.getMemory().programs
				+", MT:" + this.info.getMemory().textures
		);
	}
}
