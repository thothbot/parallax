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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;
import org.parallax3d.parallax.App;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.Rendering;
import org.parallax3d.parallax.events.AnimationReadyListener;
import org.parallax3d.parallax.graphics.renderers.Plugin;
import org.parallax3d.parallax.platforms.gwt.GwtApp;
import org.parallax3d.parallax.platforms.gwt.GwtRendering;
import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.tests.TestAnimation;

public class PanelExample extends SimpleLayoutPanel implements AnimationReadyListener
{
	private Plugin effectPlugin;

	private GwtRendering rendering;

	private TestAnimation animation;

	public PanelExample()
	{
	}

	@Override
	protected void onLoad() {

		super.onLoad();
		Timer timer = new Timer() {
			@Override
			public void run() {

				try {

					rendering = new GwtRendering((SimpleLayoutPanel)PanelExample.this, ((GwtApp) App.app).getConfig(), new GwtRendering.RenderingReadyListener() {
						@Override
						public void onRenderingReady(Rendering rendering) {
							rendering.setAnimation(animation);
						}
					});

				}
				catch (Throwable e)
				{
					PanelExample.this.clear();
					String msg = "Sorry, your browser doesn't seem to support WebGL";
					Log.error("setRendering: " + msg, e);
					PanelExample.this.add(new AlertBadCanvas(msg));
				}

				((GwtApp)App.app).setRendering(rendering);
				rendering = (GwtRendering) App.app.getRendering();
				rendering.addAnimationReadyListener(PanelExample.this);
			}
		};
		timer.schedule(100);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public void setAnimation(final TestAnimation animation)
	{
		this.animation = animation;
	}

	public void onAnimationReady()
	{
		final Rendering rendering = App.app.getRendering();

//		buttons.setDebugger(rendering.getRenderer());
////
////		this.renderingPanel.setAnimationUpdateHandler(new RenderingPanel.AnimationUpdateHandler() {
////
////			@Override
////			public void onUpdate(double duration) {
////				buttons.getDebugger().update();
////			}
////		});
//
//		buttons.switchAnimation.setEnabled(true);
//		buttons.switchAnimation.setDown(true);
//		buttons.switchAnimation.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (buttons.switchAnimation.isDown())
//					rendering.resume();
//				else
//					rendering.pause();
//			}
//		});
//
//		buttons.switchFullScreen.setEnabled(rendering.supportsDisplayModeChange());
//		buttons.switchFullScreen.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (buttons.switchFullScreen.isDown()) {
////    				ContentWidget.this.renderingPanel.toFullScreen();
//					buttons.switchFullScreen.setDown(false);
//				}
//			}
//		});
//
////		buttons.setEnableEffectSwitch(this.isEnabledEffectSwitch());
//
//		buttons.switchEffectAnaglyph.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
////    			if (buttons.switchEffectAnaglyph.isDown())
////					ContentWidget.this.effectPlugin = new Anaglyph(
////							ContentWidget.this.renderingPanel.getRenderer(),
////							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
//			}
//		});
//
//		buttons.switchEffectStereo.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
////				if (buttons.switchEffectStereo.isDown())
////					ContentWidget.this.effectPlugin = new Stereo(
////							ContentWidget.this.renderingPanel.getRenderer(),
////							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
//			}
//		});
//
//		buttons.switchEffectParallaxBarrier.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
////				if (buttons.switchEffectParallaxBarrier.isDown())
////					ContentWidget.this.effectPlugin = new ParallaxBarrier(
////							ContentWidget.this.renderingPanel.getRenderer(),
////							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
//			}
//		});
//
//		buttons.switchEffectOculusRift.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
////				if (buttons.switchEffectOculusRift.isDown())
////					ContentWidget.this.effectPlugin = new OculusRift(
////							ContentWidget.this.renderingPanel.getRenderer(),
////							ContentWidget.this.renderingPanel.getAnimatedScene().getScene());
//			}
//		});
//
//		buttons.switchEffectNone.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				rendering.getRenderer().deletePlugin(PanelExample.this.effectPlugin);
//			}
//		});

	}
}
