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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import org.parallax3d.parallax.Animation;
import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.AnimationReadyListener;
import org.parallax3d.parallax.platforms.gwt.GwtParallax;
import org.parallax3d.parallax.platforms.gwt.GwtRenderingContext;
import org.parallax3d.parallax.platforms.gwt.widgets.debugger.Debugger;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ParallaxTestCases;
import org.parallax3d.parallax.tests.client.widgets.AlertBadCanvas;
import org.parallax3d.parallax.tests.client.widgets.ItemInfo;
import org.parallax3d.parallax.tests.client.widgets.ItemSmall;
import org.parallax3d.parallax.tests.client.widgets.Logo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main view of the application
 */
public class PageExample extends ResizeComposite implements AnimationReadyListener
{
	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	interface PanelUiBinder extends UiBinder<Widget, PageExample> {
	}

	public interface PanelReady {
		void onRenderingReady(GwtRenderingContext rendering);
	}

	/**
	 * The main menu used to navigate to examples.
	 */
	@UiField
	FlowPanel menu;

	@UiField
	Logo logo;

	@UiField
	AbsolutePanel debuggerPanel;

	/**
	 * Main panel where will be RenderingPanel located
	 */
	@UiField
	SimplePanel content;

	GwtRenderingContext rendering;

	PanelReady renderingReady;

	Debugger debugger;

	public PageExample(ParallaxTestCases testCases)
	{
		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");
		menu.ensureDebugId("menu");

		for(Map.Entry<String, List<ParallaxTest>> entry: testCases.getAllTests().entrySet())
			for (ParallaxTest test : entry.getValue())
				this.menu.add(new ItemSmall(entry.getKey(), test));
	}

	@Override
	protected void onLoad() {

		if(rendering == null)
		{
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute()
				{
					try {

						rendering = new GwtRenderingContext(PageExample.this.content);

						debugger = new Debugger(rendering.getRenderer().getInfo());
						debuggerPanel.add(debugger);
						rendering.addAnimationReadyListener(PageExample.this);

						if(renderingReady != null)
							renderingReady.onRenderingReady(rendering);

					}
					catch (Throwable e)
					{
						String msg = "Sorry, your browser doesn't seem to support WebGL";
						Log.error("setRendering: " + msg, e);
						RootLayoutPanel.get().add(new AlertBadCanvas(msg));
					}
				}
			});
		}
		else
		{
			if(renderingReady != null)
				renderingReady.onRenderingReady(rendering);
		}

		super.onLoad();
	}

	public void addGwtReadyListener(PanelReady gwtReady)
	{
		this.renderingReady = gwtReady;
	}

	@Override
	public void onAnimationReady(Animation animation)
	{
		final Button switchAnimation = new Button("Pause");
		final Button switchFullScreen = new Button("Fullscreen");

		switchAnimation.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (rendering.isRun()) {
					rendering.stop();
					switchAnimation.setText("Resume");
				}
				else {
					rendering.run();
					switchAnimation.setText("Pause");
				}
			}
		});

		switchFullScreen.setEnabled(rendering.supportsDisplayModeChange());
		switchFullScreen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendering.setFullscreen();
			}
		});

		logo.setInfoPanel(new ItemInfo((ParallaxTest) animation, Arrays.asList(switchAnimation, switchFullScreen)));
	}

}
