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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;
import org.parallax3d.parallax.App;
import org.parallax3d.parallax.Rendering;
import org.parallax3d.parallax.events.AnimationReadyListener;
import org.parallax3d.parallax.graphics.renderers.Plugin;
import org.parallax3d.parallax.platforms.gwt.GwtApp;
import org.parallax3d.parallax.tests.TestAnimation;

public class PanelExample extends ResizeComposite implements AnimationReadyListener
{
	interface PanelUiBinder extends UiBinder<Widget, PanelExample> {
	}

	private static PanelUiBinder uiBinder = GWT.create(PanelUiBinder.class);

	/**
	 * The main menu used to navigate to examples.
	 */
	@UiField(provided = true)
	CellTree menu;

	@UiField(provided = true)
	PanelButtons buttons;

	/**
	 * The panel that holds the content.
	 */
	@UiField
	SimpleLayoutPanel content;


	private Plugin effectPlugin;

	private Rendering rendering;

	/**
	 * Construct the ShowcaseShell.
	 *
	 * @param treeModel
	 *            the treeModel that backs the main menu
	 */
	public PanelExample(TreeViewModel treeModel)
	{
		// Create the cell tree.
		menu = new CellTree(treeModel, null);
		menu.setAnimationEnabled(true);
		menu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		menu.ensureDebugId("menu");

		buttons = new PanelButtons();

		// Initialize the ui binder.
		initWidget(uiBinder.createAndBindUi(this));

		// Default to no content.
		content.ensureDebugId("content");
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		((GwtApp)App.app).setRendering(content, ((GwtApp) App.app).getConfig());
		rendering = App.app.getRendering();
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

	public void setAnimation(final TestAnimation animation)
	{
		App.app.getRendering().setAnimation(animation);
		App.app.getRendering().setAnimationReadyListener(PanelExample.this);
	}

	public void onAnimationReady()
	{
		final Rendering rendering = App.app.getRendering();

		buttons.setDebugger(rendering.getRenderer());
//
//		this.renderingPanel.setAnimationUpdateHandler(new RenderingPanel.AnimationUpdateHandler() {
//
//			@Override
//			public void onUpdate(double duration) {
//				buttons.getDebugger().update();
//			}
//		});

		buttons.switchAnimation.setEnabled(true);
		buttons.switchAnimation.setDown(true);
		buttons.switchAnimation.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (buttons.switchAnimation.isDown())
					rendering.resume();
				else
					rendering.pause();
			}
		});

		buttons.switchFullScreen.setEnabled(rendering.supportsDisplayModeChange());
		buttons.switchFullScreen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (buttons.switchFullScreen.isDown()) {
//    				ContentWidget.this.renderingPanel.toFullScreen();
					buttons.switchFullScreen.setDown(false);
				}
			}
		});

//		buttons.setEnableEffectSwitch(this.isEnabledEffectSwitch());

		buttons.switchEffectAnaglyph.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
//    			if (buttons.switchEffectAnaglyph.isDown())
//					ContentWidget.this.effectPlugin = new Anaglyph(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
			}
		});

		buttons.switchEffectStereo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
//				if (buttons.switchEffectStereo.isDown())
//					ContentWidget.this.effectPlugin = new Stereo(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
			}
		});

		buttons.switchEffectParallaxBarrier.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
//				if (buttons.switchEffectParallaxBarrier.isDown())
//					ContentWidget.this.effectPlugin = new ParallaxBarrier(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
			}
		});

		buttons.switchEffectOculusRift.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
//				if (buttons.switchEffectOculusRift.isDown())
//					ContentWidget.this.effectPlugin = new OculusRift(
//							ContentWidget.this.renderingPanel.getRenderer(),
//							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
			}
		});

		buttons.switchEffectNone.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
			}
		});

	}
}
