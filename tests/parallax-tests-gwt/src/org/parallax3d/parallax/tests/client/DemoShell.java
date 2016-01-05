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

import org.parallax3d.parallax.tests.resources.DemoResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

public class DemoShell extends ResizeComposite
{
	interface DemoShellUiBinder extends UiBinder<Widget, DemoShell> {
	}

	private static DemoShellUiBinder uiBinder = GWT.create(DemoShellUiBinder.class);

	/**
	 * The panel that holds the content.
	 */
	@UiField
	SimpleLayoutPanel contentPanel;

	/**
	 * The main menu used to navigate to examples.
	 */
	@UiField(provided = true)
	CellTree mainMenu;

//	/**
//	 * The current {@link ContentWidget} being displayed.
//	 */
//	private ContentWidget content;

	/**
	 * The handler used to handle user requests to view raw source.
	 */
	private HandlerRegistration contentSourceHandler;

	/**
	 * The html used to show a loading icon.
	 */
	private final String loadingHtml;

	/**
	 * The unique ID assigned to the next callback.
	 */
	private static int nextCallbackId = 0;
	
	/**
	 * Construct the ShowcaseShell.
	 * 
	 * @param treeModel
	 *            the treeModel that backs the main menu
	 */
	public DemoShell(TreeViewModel treeModel, Index index)
	{
		AbstractImagePrototype proto = AbstractImagePrototype.create(WebApp.resources.loading());
		loadingHtml = proto.getHTML();

		// Create the cell tree.
		mainMenu = new CellTree(treeModel, null);
		mainMenu.setAnimationEnabled(true);
		mainMenu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		mainMenu.ensureDebugId("mainMenu");

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		contentPanel.ensureDebugId("contentPanel");

		setContent(null);
	}

//	/**
//	 * Returns the currently displayed content. (Used by tests.)
//	 */
//	public ContentWidget getContent()
//	{
//		return content;
//	}

	/**
	 * Get the main menu used to select examples.
	 * 
	 * @return the main menu
	 */
	public CellTree getMainMenu()
	{
		return mainMenu;
	}

	public void setIndex(Widget content)
	{
		contentPanel.setWidget(content);
	}

	/**
	 * Set the content to display.
	 * 
	 * @param content
	 *            the content
	 */
	public void setContent(final ContentWidget content)
	{
		// Clear the old handler.
		if (contentSourceHandler != null) 
		{
			contentSourceHandler.removeHandler();
			contentSourceHandler = null;
		}

//		this.content = content;
		if (content == null) 
		{
			contentPanel.setWidget(null);
			return;
		}

		// Show the widget.
		showExample();
	}
	
	/**
	 * Show a example.
	 */
	private void showExample()
	{
//		if (content == null)
//			return;

//		contentPanel.setWidget(content);
	}
}
