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

package org.parallax3d.parallax.tests.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RenderablePanel;
import com.google.gwt.user.client.ui.Widget;

/** 
 * Common information panel used in the {@link RenderablePanel} widget.
 * 
 * @author thothbot
 *
 */
public abstract class Alert extends LayoutPanel
{
	AbsolutePanel popupPanel;
	boolean isVisible;

	public abstract Widget getContent();

	public Alert()
	{   
		this.popupPanel = new AbsolutePanel();

		this.popupPanel.setStyleName("common-panel", true);
		
		this.add(this.popupPanel);
		
		this.popupPanel.add(getContent());
	}

	/**
	 * Show the panel.
	 */
	public void show()
	{
		if(isVisible) return;

		this.popupPanel.setVisible(true);
		isVisible = true;
	}
	
	/**
	 * Hide the panel.
	 */
	public void hide()
	{
		if(!isVisible) return;
		
		this.popupPanel.setVisible(false);
		isVisible = false;
	}
}
