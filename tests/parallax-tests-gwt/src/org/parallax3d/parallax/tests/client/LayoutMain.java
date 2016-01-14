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
import com.google.gwt.view.client.TreeViewModel;
import org.parallax3d.parallax.tests.resources.DemoResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

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
	@UiField(provided = true)
	CellTree menu;

	@UiField(provided = true)
	LayoutDock docked;

	/**
	 * Main panel where will be RenderingPanel located
	 */
	@UiField(provided = true)
	SimpleLayoutPanel content;
	
	public LayoutMain(TreeViewModel treeModel)
	{
		// Create the cell tree.
		menu = new CellTree(treeModel, null);
		menu.setAnimationEnabled(true);
		menu.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
		menu.ensureDebugId("menu");

		content = new SimpleLayoutPanel();

		docked = new LayoutDock();

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");
	}

	/**
	 * Get the main menu used to select examples.
	 *
	 * @return the main menu
	 */
	public CellTree getMenu()
	{
		return menu;
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
