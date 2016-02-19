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

package org.parallax3d.parallax;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.renderers.CSS3DRenderer;

/**
 * Widget which manages {@link CSS3DAnimatedScene} and {@link CSS3DRenderer}
 *
 * rebuild from old RenderingPanel
 *
 * @author svv2014
 * 
 */
public class CSS3DRenderingPanel extends HTMLPanel implements IsWidget, HasWidgets, HasHandlers
{
	public interface AnimationUpdateHandler
	{
		public void onUpdate(double duration);
	}

	private CSS3DAnimatedScene animatedScene;
	private HandlerManager handlerManager;

	// Loading info panel
	private boolean isSceneLoaded;

	private AnimationUpdateHandler animationUpdateHandler;

	/**
	 * This constructor will create new instance of the widget.
	 */
	public CSS3DRenderingPanel()
	{
		super("");
		this.handlerManager = new HandlerManager(this);
		
		this.ensureDebugId("css3DRenderingPanel");
		this.getElement().getStyle().setPosition(Position.RELATIVE);
		this.setWidth("100%");
		this.setHeight("100%");

	}
	
	public Widget getCanvas()
	{
		return animatedScene.getCanvas();
	}

	/**
	 * Gets {@link CSS3DRenderer}. Use {@link CSS3DAnimatedScene#getRenderer()} instead.
	 *  
	 * @return {@link CSS3DRenderer}
	 */
	public CSS3DRenderer getRenderer()
	{
		return animatedScene.getRenderer();
	}

	/**
	 * Gets {@link CSS3DAnimatedScene} instance associated with the widget.
	 * 
	 * @return the {@link CSS3DAnimatedScene}
	 */
	public CSS3DAnimatedScene getAnimatedScene()
	{
		return this.animatedScene;
	}
	
	public void setAnimationUpdateHandler(AnimationUpdateHandler animationUpdateHandler) 
	{
		this.animationUpdateHandler = animationUpdateHandler;
	}
	
	/**
	 * Sets the {@link CSS3DAnimatedScene} to the widget.
	 * @param animatedScene
	 */
	public void setAnimatedScene(CSS3DAnimatedScene animatedScene)
	{
		this.animatedScene = animatedScene;
		this.add(animatedScene.getRenderer());
//		handlerManager.fireEvent(new SceneLoadingEvent());
	}

	/**
	 * This method is called when a widget is attached to the browser's document.
	 */
	@Override
	public void onLoad()
	{
		Log.debug("RenderingPanel: onLoad()");
		
		super.onLoad();

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				onLoaded();
			}
		});
	}
	
	/**
	 * This method is called when a widget is detached from the browser's document.
	 * Here is called  Animation#stop() method.
	 */
	@Override
	public void onUnload()
	{
//		if(getAnimatedScene() != null)
//			getAnimatedScene().stop();
		
		super.onUnload();
	}
	
	/**
	 * This method is called when a widget is fully initialized.
	 */
	public void onLoaded()
	{
		setSize(this.getOffsetWidth(), this.getOffsetHeight());

		if(getAnimatedScene() != null)
		{
			this.animatedScene.init(new CSS3DAnimatedScene.AnimationUpdateHandler() {
				
				@Override
				public void onUpdate(double duration)
				{

					if(!isSceneLoaded)
					{
//						handlerManager.fireEvent(new SceneLoadingEvent(true));
						isSceneLoaded = true;
					}
					
					if(animationUpdateHandler != null)
						animationUpdateHandler.onUpdate(duration);
				}
			});

//			handlerManager.fireEvent(new AnimationReadyEvent());
//
//			getAnimatedScene().run();
		}
	}

	/**
	 * This method is called when the implementor's size has been modified.
	 */
	public void onResize() 
	{
		Log.debug("RenderingPanel: onResize()");

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() 
			{
				setSize(CSS3DRenderingPanel.this.getOffsetWidth(), CSS3DRenderingPanel.this.getOffsetHeight());
			}
		});
	}

//	public HandlerRegistration addAnimationReadyHandler(AnimationReadyHandler handler) {
//		return addHandler(AnimationReadyEvent.TYPE, handler);
//	}
//
//	public HandlerRegistration addSceneLoadingHandler(SceneLoadingHandler handler) {
//		return addHandler(SceneLoadingEvent.TYPE, handler);
//	}
//
//	public HandlerRegistration addCanvas3dErrorHandler(Context3dErrorHandler handler) {
//		return addHandler(Context3dErrorEvent.TYPE, handler);
//	}

	protected <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, H handler) {
		Log.debug("RenderingPanel: Registered event for class " + handler.getClass().getName());
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * Resizes the {@link CSS3DRenderer} viewport
	 * to (width, height), and also sets the viewport to fit that size, 
	 * starting in (0, 0).
	 * 
	 * @param width  the new width of the {@link CSS3DRenderer}.
	 * @param height the new height of the {@link CSS3DRenderer}.
	 */
	private void setSize(int width, int height) {
		Log.debug("CSS3DRenderingPanel: set size: W=" + width + ", H=" + height);

		getCanvas().setSize(width+"px", height+"px");
		getRenderer().setSize(width, height);
	}

	
	public void toFullScreen() {
		CSS3DRenderingPanel.toFullScreen(this.getElement());
	}
	
	private static native void toFullScreen(Element element) /*-{
  	   if(element.requestFullscreen) {
		    element.requestFullscreen();
	   } else if(element.webkitRequestFullscreen) {
		    element.webkitRequestFullscreen();
	   } else if(element.mozRequestFullscreen) {
		    element.mozRequestFullScreen();
	   }
	  
	  	var rect = element.getBoundingClientRect();
     	element.width = rect.width;
     	element.height = rect.height;
	}-*/;

	public boolean isSupportFullScreen() {
		return CSS3DRenderingPanel.isSupportFullScreen(this.getElement());
	}

	private static native boolean isSupportFullScreen(Element element) /*-{
		return (element.requestFullscreen || element.webkitRequestFullscreen || element.mozRequestFullscreen) ? true : false;
	}-*/;
}
