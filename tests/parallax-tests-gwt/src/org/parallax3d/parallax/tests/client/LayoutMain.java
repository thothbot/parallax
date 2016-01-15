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

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.TreeViewModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

/**
 * Main view of the application
 */
public class LayoutMain extends ResizeComposite
{
	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	interface PanelUiBinder extends UiBinder<Widget, LayoutMain> {
	}

	/**
	 * The main menu used to navigate to examples.
	 */
	@UiField
	ScrollPanel menu;

	@UiField(provided = true)
	LayoutDock docked;

	/**
	 * Main panel where will be RenderingPanel located
	 */
	@UiField(provided = true)
	SimpleLayoutPanel content;
	
	public LayoutMain()
	{
		content = new SimpleLayoutPanel();

		docked = new LayoutDock();

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");
	}

	public LayoutDock getDock() {
		return docked;
	}

	public void setContentWidget(SimplePanel content)
	{
		this.content.setWidget(content);
	}

	public void setContentWidget(PanelExample content)
	{
		this.content.setWidget(content);
	}	
}
