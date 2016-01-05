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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

public class LayoutExample extends ResizeComposite
{
	interface PanelUiBinder extends UiBinder<Widget, LayoutExample> {
	}

	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	/**
	 * The panel that holds the content.
	 */
	@UiField
	SimpleLayoutPanel content;

	/**
	 * The main menu used to navigate to examples.
	 */
	@UiField(provided = true)
	CellTree menu;

	/**
	 * Construct the ShowcaseShell.
	 *
	 * @param treeModel
	 *            the treeModel that backs the main menu
	 */
	public LayoutExample(TreeViewModel treeModel)
	{
		// Create the cell tree.
		menu = new CellTree(treeModel, null);
		menu.setAnimationEnabled(true);
		menu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		menu.ensureDebugId("menu");

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");

		setContent(null);
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

	/**
	 * Set the content to display.
	 *
	 * @param content
	 *            the content
	 */
	public void setContent(final PanelExample content)
	{
		if (content == null)
		{
			this.content.setWidget(null);
			return;
		}
	}

}
