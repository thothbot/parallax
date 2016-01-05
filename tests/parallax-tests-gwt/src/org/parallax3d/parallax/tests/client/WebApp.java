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
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.parallax3d.parallax.App;
import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.resources.DemoResources;
import org.parallax3d.parallax.platforms.gwt.GwtApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WebApp extends GwtApp
{
	/**
	 * The static resources used throughout the Demo.
	 */
	public static final DemoResources resources = GWT.create(DemoResources.class);

	private PanelExample panelExample;
	
	private PanelExamples panelExamples;
	
	private LayoutMain layoutMain;

	public void onInit()
	{
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable throwable) {
				App.app.error("WebApp", "Uncaught exception", throwable);
				if (!GWT.isScript()) {
					String text = "Uncaught exception: ";
					while (throwable != null) {
						StackTraceElement[] stackTraceElements = throwable.getStackTrace();
						text += throwable.toString() + "\n";

						for (int i = 0; i < stackTraceElements.length; i++)
							text += "    at " + stackTraceElements[i] + "\n";

						throwable = throwable.getCause();
						if (throwable != null)
							text += "Caused by: ";
					}

					DialogBox dialogBox = new DialogBox(true);
					DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
					text = text.replaceAll(" ", "&nbsp;");
					dialogBox.setHTML("<pre>" + text + "</pre>");
					dialogBox.center();
				}
			}
		});

		resources.css().ensureInjected();

		// Create the application panelExample.
		final SingleSelectionModel<TestAnimation> selectionModel = new SingleSelectionModel<>();
		final DataModel treeModel = new DataModel(selectionModel);
		Set<TestAnimation> contentWidgets = treeModel.getAllContentWidgets();
		
		layoutMain = new LayoutMain();
		// Hide loading panel
		RootPanel.get("loading").getElement().getStyle().setVisibility(Visibility.HIDDEN);
		// Attach layoutMain panel
		RootLayoutPanel.get().add(layoutMain);
		
		layoutMain.getLinkAllExamples().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event)
			{
				displayIndex();
			}
		});

		panelExamples = new PanelExamples(treeModel);
		panelExample = new PanelExample(treeModel);

		// Prefetch examples when opening the Category tree nodes.
		final List<DataModel.Category> prefetched = new ArrayList<DataModel.Category>();
		final CellTree mainMenu = panelExample.getMenu();

		// Change the history token when a main menu item is selected.
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event)
			{
				TestAnimation selected = selectionModel.getSelectedObject();
				if (selected != null) 
				{
					layoutMain.setContentWidget(panelExample);
					History.newItem("!"+selected.getContentWidgetToken(), true);
				}
			}
		});

		// Setup a history handler to reselect the associate menu item.
		final ValueChangeHandler<String> historyHandler = new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event)
			{
				// Get the content widget associated with the history token.
				TestAnimation contentWidget = treeModel.getContentWidgetForToken(event.getValue().replaceFirst("!", ""));

				if (contentWidget == null)
					return;

				// Expand the tree node associated with the content.
				DataModel.Category category = treeModel.getCategoryForContentWidget(contentWidget);
				TreeNode node = mainMenu.getRootTreeNode();
				int childCount = node.getChildCount();
				for (int i = 0; i < childCount; i++) 
				{
					App.app.log("", Boolean.toString(node.getChildValue(i) == category));
					if (node.getChildValue(i) == category) 
					{
						node.setChildOpen(i, true, true);
						break;
					}
				}

				// Display the content widget.
				displayContentWidget(contentWidget);

				// Select the node in the tree.
				selectionModel.setSelected(contentWidget, true);
			}
		};

		History.addValueChangeHandler(historyHandler);

		// Show the initial example.
		if (History.getToken().length() > 0) 
			History.fireCurrentHistoryState();

		// Use the first token available.
		else
			displayIndex();
	}

	private void displayContentWidget(final TestAnimation content)
	{
		if (content == null)
			return;

		panelExample.setAnimation(content);
		Window.setTitle("Parallax tests: " + content.getName());
	}

	private void displayIndex()
	{
		History.newItem("", true);
		layoutMain.setContentWidget(panelExamples);
		Window.setTitle("Parallax: All Examples");
	}
}
