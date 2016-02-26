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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.Parallax.ParallaxListener;
import org.parallax3d.parallax.platforms.gwt.GwtRenderingContext;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ParallaxTestCases;
import org.parallax3d.parallax.tests.client.widgets.Alert;
import org.parallax3d.parallax.tests.resources.Resources;
import org.parallax3d.parallax.platforms.gwt.GwtParallax;

public class WebApp implements EntryPoint, ParallaxListener {

    /**
     * The static resources used throughout the Demo.
     */
    public static final Resources resources = GWT.create(Resources.class);

    private PageIndex pageIndex;
    private PageExample pageExample;

    public void onModuleLoad()
    {
        resources.css().ensureInjected();

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            public void onUncaughtException(Throwable throwable) {
                Log.error("Uncaught exception ", throwable);
//                if (!GWT.isScript()) {
                String text = "Uncaught exception: ";
                while (throwable != null) {
                    StackTraceElement[] stackTraceElements = throwable.getStackTrace();
                    text += throwable.toString() + "\n";

                    for (StackTraceElement stackTraceElement : stackTraceElements)
                        text += "    at " + stackTraceElement + "\n";

                    throwable = throwable.getCause();
                    if (throwable != null)
                        text += "Caused by: ";
                }

                History.newItem("", true);
                text = text.replaceAll("\n", "<br/>");
                RootLayoutPanel.get().add(new Alert(new HTMLPanel(text)));
//                }
            }
        });

        GwtParallax.init( this );
    }

    @Override
    public void onParallaxApplicationReady(Parallax instance)
    {
        final ParallaxTestCases testCases = GWT.create(ParallaxTestCases.class);
        pageIndex = new PageIndex(testCases);
        pageExample = new PageExample(testCases);

        // Setup a history handler to reselect the associate menu item.
        final ValueChangeHandler<String> historyHandler = new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                final ParallaxTest test = testCases.getContentWidgetForToken(event.getValue().replaceFirst("!", ""));

                RootLayoutPanel.get().clear();
                if (test != null) {
                    pageExample.addGwtReadyListener(new PageExample.PanelReady() {
                        @Override
                        public void onRenderingReady(GwtRenderingContext rendering) {
                            rendering.setAnimation(test);
                        }
                    });
                    RootLayoutPanel.get().add(pageExample);

                    Window.setTitle("Parallax: " + test.getName());
                } else {
                    RootLayoutPanel.get().add(pageIndex);

                    History.newItem("", true);
                    Window.setTitle("Parallax: Cross-platform Java 3D library");
                }
            }
        };

        History.addValueChangeHandler(historyHandler);
        History.fireCurrentHistoryState();

        // Remove loading panel
        RootPanel.get("loading").getElement().removeFromParent();
    }
}
