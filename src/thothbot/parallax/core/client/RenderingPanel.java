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

package thothbot.parallax.core.client;

import java.beans.Beans;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.context.Canvas3dAttributes;
import thothbot.parallax.core.client.context.Canvas3dException;
import thothbot.parallax.core.client.events.AnimationReadyEvent;
import thothbot.parallax.core.client.events.AnimationReadyHandler;
import thothbot.parallax.core.client.events.Context3dErrorEvent;
import thothbot.parallax.core.client.events.Context3dErrorHandler;
import thothbot.parallax.core.client.events.SceneLoadingEvent;
import thothbot.parallax.core.client.events.SceneLoadingHandler;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * Widget which manages {@link WebGLRenderer} and {@link Canvas3d}
 * 
 * @author thothbot
 * 
 */
public class RenderingPanel extends LayoutPanel implements IsWidget, HasWidgets, HasHandlers
{	
	public interface AnimationUpdateHandler 
	{
		public void onUpdate(double duration);
	}
	
    // Sets the background color for the {@link Canvas3d}. Default: black (#000000).
	private int clearColor = 0x000000;

	// Sets the background alpha value for the {@link Canvas3d}. Default: opaque (1.0).
	private double clearAlpha = 1.0;
	
	private Canvas3dAttributes canvas3dAttributes;
	
	private AnimatedScene animatedScene;
	private HandlerManager handlerManager;
	private boolean isLoaded = false;
	
	// Loading info panel
	private boolean isSceneLoaded;

	private Canvas3d canvas;
	private WebGLRenderer renderer;
	
	private AnimationUpdateHandler animationUpdateHandler;

	/**
	 * This constructor will create new instance of the widget.
	 */
	public RenderingPanel() 
	{
		this.handlerManager = new HandlerManager(this);
		
		this.ensureDebugId("renderingPanel");
		this.getElement().getStyle().setPosition(Position.RELATIVE);
		this.setWidth("100%");
		this.setHeight("100%");
		this.canvas3dAttributes = new Canvas3dAttributes();
		this.canvas3dAttributes.setStencilEnable(true);

		updateBackground();
	}
	
	public Canvas3d getCanvas()
	{
		return this.canvas;
	}

	/**
	 * Gets {@link WebGLRenderer}. Use {@link AnimatedScene#getRenderer()} instead. 
	 *  
	 * @return {@link WebGLRenderer}
	 */
	public WebGLRenderer getRenderer()
	{
		return this.renderer;
	}

	/**
	 * Gets {@link AnimatedScene} instance associated with the widget.
	 * 
	 * @return the {@link AnimatedScene}
	 */
	public AnimatedScene getAnimatedScene()
	{
		return this.animatedScene;
	}
	
	public void setAnimationUpdateHandler(AnimationUpdateHandler animationUpdateHandler) 
	{
		this.animationUpdateHandler = animationUpdateHandler;
	}
	
	/**
	 * Sets the {@link AnimatedScene} to the widget.
	 * @param animatedScene
	 */
	public void setAnimatedScene(AnimatedScene animatedScene)
	{
		this.animatedScene = animatedScene;
		handlerManager.fireEvent(new SceneLoadingEvent());
	}
	
	public void setBackground(int color)
	{
		setBackground(color, this.clearAlpha);
	}
	
	public void setBackground(int color, double alpha)
	{
		this.clearColor = color;
		this.clearAlpha = alpha;
		
		updateBackground();
	}
	
	public Canvas3dAttributes getCanvas3dAttributes() 
	{
		return this.canvas3dAttributes;
	}
		
	/**
	 * Load renderer
	 */
	private void loadRenderer() throws Exception
	{
		// Do not create WebGLRenderer instance while design UI (in Eclipse)
		// otherwise you'll see exception in UI builder
		// Also loads renderer when needed
		if (!Beans.isDesignTime() && this.renderer == null) 
		{
			Log.debug("RenderingPanel: initRenderer()");

			canvas = loadCanvas();
			this.renderer = new WebGLRenderer(canvas.getGL(), canvas.getWidth(), canvas.getHeight());
			this.renderer.setClearColor(this.clearColor, this.clearAlpha);
		}
	}
	
	/**
	 * Load canvas widget
	 * 
	 * @throws Canvas3dException
	 */
	private Canvas3d loadCanvas() throws Exception
	{
		Log.debug("RenderingPanel: loadCanvas()");
		
		Canvas3d canvas = new Canvas3d(this.canvas3dAttributes); 
		this.add(canvas);

		return canvas;
	}

	/**
	 * This method is called when a widget is attached to the browser's document. 
	 * {@link Canvas3d} should be initialized here.
	 */
	@Override
	public void onLoad()
	{
		Log.debug("RenderingPanel: onLoad()");
		
		super.onLoad();

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() 
			{
				try
				{
					loadRenderer();
				}
				catch (Exception ex)
				{
					Log.error(ex.getMessage(), ex.fillInStackTrace());
					handlerManager.fireEvent(new Context3dErrorEvent(ex.getMessage()));
					return;
				}

				onLoaded();
			}
		});
	}
	
	/**
	 * This method is called when a widget is detached from the browser's document.
	 * Here is called {@link Animation#stop()} method.
	 */
	@Override
	public void onUnload()
	{
		if(getAnimatedScene() != null)
			getAnimatedScene().stop();
		
		isLoaded = false;
		super.onUnload();
	}
	
	/**
	 * This method is called when a widget is fully initialized.
	 */
	public void onLoaded()
	{
		if(isLoaded) return;

		Log.debug("RenderingPanel: onLoaded()");

		isLoaded = true;

		setSize(this.getOffsetWidth(), this.getOffsetHeight());

		if(getAnimatedScene() != null)
		{
			this.animatedScene.init(this, new AnimatedScene.AnimationUpdateHandler() {
				
				@Override
				public void onUpdate(double duration)
				{

					if(!isSceneLoaded)
					{
						handlerManager.fireEvent(new SceneLoadingEvent(true));
						isSceneLoaded = true;
					}
					
					if(animationUpdateHandler != null)
						animationUpdateHandler.onUpdate(duration);
				}
			});

			handlerManager.fireEvent(new AnimationReadyEvent());

			getAnimatedScene().run();
		}
	}

	/**
	 * This method is called when the implementor's size has been modified.
	 */
	@Override
	public void onResize() 
	{
		Log.debug("RenderingPanel: onResize()");

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() 
			{
				setSize(RenderingPanel.this.getOffsetWidth(), RenderingPanel.this.getOffsetHeight());
			}
		});
	}

	public HandlerRegistration addAnimationReadyHandler(AnimationReadyHandler handler) {
		return addHandler(AnimationReadyEvent.TYPE, handler);
	}

	public HandlerRegistration addSceneLoadingHandler(SceneLoadingHandler handler) {
		return addHandler(SceneLoadingEvent.TYPE, handler);
	}
	
	public HandlerRegistration addCanvas3dErrorHandler(Context3dErrorHandler handler) {
		return addHandler(Context3dErrorEvent.TYPE, handler);
	}

	protected <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, H handler) 
	{
		Log.debug("RenderingPanel: Registered event for class " + handler.getClass().getName());
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * Resizes the {@link Canvas3d} and {@link WebGLRenderer} viewport 
	 * to (width, height), and also sets the viewport to fit that size, 
	 * starting in (0, 0).
	 * 
	 * @param width  the new width of the {@link Canvas3d}.
	 * @param height the new height of the {@link Canvas3d}.
	 */
	private void setSize(int width, int height) 
	{
		Log.debug("RenderingPanel: set size: W=" + width + ", H=" + height); 

		getCanvas().setSize(width, height);
		getRenderer().setSize(width, height);
	}
	
	private void updateBackground()
	{
		this.getElement().getStyle().setBackgroundColor(cssColor(this.clearColor));
		if(this.clearAlpha < 1.0)
			this.getElement().getStyle().setOpacity(this.clearAlpha);	
		
		if(this.renderer != null)
			this.renderer.setClearColor(this.clearColor, this.clearAlpha);
	}
	
	private static String cssColor(int color)
	{
		String retval = "#";
		if(color == 0)
			retval += "000000";
		else
			retval += Integer.toHexString(color);
		
		return retval;
	}
	
	public void toFullScreen() {
		RenderingPanel.toFullScreen(this.getElement());
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
		return RenderingPanel.isSupportFullScreen(this.getElement());
	}

	private static native boolean isSupportFullScreen(Element element) /*-{
		return (element.requestFullscreen || element.webkitRequestFullscreen || element.mozRequestFullscreen) ? true : false;
	}-*/;
}
