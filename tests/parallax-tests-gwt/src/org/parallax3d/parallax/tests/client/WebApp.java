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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.TestList;
import org.parallax3d.parallax.tests.client.widgets.WidgetIndex;
import org.parallax3d.parallax.tests.resources.DemoResources;
import org.parallax3d.parallax.platforms.gwt.GwtApp;

public class WebApp extends GwtApp
{
	/**
	 * The static resources used throughout the Demo.
	 */
	public static final DemoResources resources = GWT.create(DemoResources.class);

	private WidgetIndex indexWidget;

	private LayoutMain layoutMain;

	public void onInit()
	{
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable throwable) {
				Log.error("Uncaught exception", throwable);
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

		indexWidget = new WidgetIndex(TestList.DATA);
		layoutMain = new LayoutMain();

		// Hide loading panel
		RootPanel.get("loading").getElement().getStyle().setVisibility(Visibility.HIDDEN);

		// Setup a history handler to reselect the associate menu item.
		final ValueChangeHandler<String> historyHandler = new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event)
			{
				TestAnimation contentWidget = TestList.getContentWidgetForToken(event.getValue().replaceFirst("!", ""));

				if (contentWidget != null)
				{
					// Display the content widget.
					displayContentWidget(contentWidget);

				}
				else
				{
					displayIndex();
				}
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

		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(layoutMain);

		PanelExample panelExample = new PanelExample();
		layoutMain.setContentWidget(panelExample);

		panelExample.setAnimation(content);
		Window.setTitle("Parallax tests: " + content.getName());
	}

	private void displayIndex()
	{
		History.newItem("", true);

		// Attach layoutMain panel
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(indexWidget);

		Window.setTitle("Parallax: All Examples");
	}
}
