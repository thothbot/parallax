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

package org.parallax3d.parallax.demo.client;

import org.parallax3d.parallax.demo.resources.DemoResources;

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
	
	/**
	 * The button used to show the example.
	 */
	Anchor linkExample;

	/**
	 * The button used to show the source code.
	 */
	Anchor linkSource;
		
	/**
	 * The current {@link ContentWidget} being displayed.
	 */
	private ContentWidget content;

	/**
	 * The handler used to handle user requests to view raw source.
	 */
	private HandlerRegistration contentSourceHandler;

	/**
	 * The html used to show a loading icon.
	 */
	private final String loadingHtml;

	/**
	 * The widget that holds CSS or source code for an example.
	 */
	private HTML contentSource = new HTML();

	/**
	 * The callback used when retrieving source code.
	 */
	private class CustomCallback implements ContentWidget.Callback<String>
	{
		private int id;

		public CustomCallback() {
			id = ++nextCallbackId;
		}

		public void onError()
		{
			if (id == nextCallbackId)
				contentSource.setHTML("Cannot find resource");
		}

		public void onSuccess(String value)
		{
			if (id == nextCallbackId) {
				contentSource.setHTML(value);				
			}
			highlightSourcecode(contentSource.getElement().getFirstChildElement().getFirstChildElement());
		}
	}

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

		linkExample = index.linkExample;
		linkSource = index.linkSource;
		
		// Create the cell tree.
		mainMenu = new CellTree(treeModel, null);
		mainMenu.setAnimationEnabled(true);
		mainMenu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		mainMenu.ensureDebugId("mainMenu");

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Handle events from the tabs.
		linkExample.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				showExample();
			}
		});

		linkSource.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				showSourceFile();
			}
		});

		// Default to no content.
		contentPanel.ensureDebugId("contentPanel");

		setContent(null);
	}
	
	public Anchor getTabExample()
	{
		return this.linkExample;
	}
	
	public Anchor getTabSource()
	{
		return this.linkSource;
	}

	/**
	 * Returns the currently displayed content. (Used by tests.)
	 */
	public ContentWidget getContent()
	{
		return content;
	}

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

		this.content = content;
		if (content == null) 
		{
			linkExample.setVisible(false);
			linkSource.setVisible(false);
			contentPanel.setWidget(null);
			return;
		}

		// Setup the options bar.
		linkExample.setVisible(true);
		linkSource.setVisible(true);

		// Show the widget.
		showExample();
	}
	
	/**
	 * Show a example.
	 */
	private void showExample()
	{
		if (content == null)
			return;

		// Set the highlighted tab.
		linkExample.getElement().getStyle().setColor(DemoResources.SELECTED_TAB_COLOR);
		linkSource.getElement().getStyle().clearColor();

		contentPanel.setWidget(content);
	}

	/**
	 * Show a source file based on the selection in the source list.
	 */
	private void showSourceFile()
	{
		if (content == null)
			return;

		// Set the highlighted tab.
		linkExample.getElement().getStyle().clearColor();
		linkSource.getElement().getStyle().setColor(DemoResources.SELECTED_TAB_COLOR);

		contentSource.setHTML(loadingHtml);
		contentPanel.setWidget(new ScrollPanel(contentSource));

		content.getSource(new CustomCallback());
	}
	
	private static native void highlightSourcecode(Element element) /*-{
		$wnd.hljs.highlightBlock(element);
	}-*/;
}
