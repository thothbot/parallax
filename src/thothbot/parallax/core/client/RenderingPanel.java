/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client;

import java.beans.Beans;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.context.Canvas3dAttributes;
import thothbot.parallax.core.client.context.Canvas3dException;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.widget.BadCanvasPanel;
import thothbot.parallax.core.client.widget.Debugger;
import thothbot.parallax.core.client.widget.LoadingPanel;
import thothbot.parallax.core.resources.CoreResources;
import thothbot.parallax.core.shared.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * A widget where {@link AnimatedScene} will be rendered. 
 * This is complex widget which used for display {@link LoadingPanel} and {@link Debugger}.
 * 
 * @author thothbot
 * 
 */
public class RenderingPanel extends LayoutPanel implements IsWidget, HasWidgets, HasHandlers
{	
    // Sets the background color for the {@link Canvas3d}. Default: black (#000000).
	private int clearColor = 0x000000;

	// Sets the background alpha value for the {@link Canvas3d}. Default: opaque (1.0).
	private double clearAlpha = 1.0;
	
	private Canvas3dAttributes canvas3dAttributes;
	
	private AnimatedScene animatedScene;
	private HandlerManager handlerManager;
	private boolean isLoaded = false;
		
	// Debug panel
	private boolean isDebugEnabled;
	private Debugger debugger;
	
	// Loading info panel
	private LoadingPanel loadingPanal; 

	private Canvas3d canvas;
	private WebGLRenderer renderer;

	/**
	 * This constructor will create new instance of the widget.
	 */
	public RenderingPanel() 
	{
		this.handlerManager = new HandlerManager(this);
		
		this.ensureDebugId("renderingPanel");
		this.getElement().getStyle().setPosition(Position.RELATIVE);
		this.canvas3dAttributes = new Canvas3dAttributes();
		this.canvas3dAttributes.setStencilEnable(true);

		updateBackground();

		// Loading specific styles
		CoreResources.INSTANCE.css().ensureInjected();
		
		// Add loading panel
		this.loadingPanal = new LoadingPanel();
		add(this.loadingPanal);
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
	
	/**
	 * Sets the {@link AnimatedScene} to the widget.
	 * @param animatedScene
	 */
	public void setAnimatedScene(AnimatedScene animatedScene)
	{
		this.animatedScene = animatedScene;
	}
	
	public void enableDebug(boolean enabled)
	{
		this.isDebugEnabled = enabled;
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
			this.renderer.setClearColorHex(this.clearColor, this.clearAlpha);
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
	 * Load Debugger
	 */
	private void loadDebuger()
	{
		if(this.isDebugEnabled && this.debugger == null)
		{
			this.debugger = new Debugger(getRenderer().getInfo());
			this.add(this.debugger);
			this.setWidgetRightWidth(this.debugger, 1, Unit.PX, 17, Unit.EM);
			this.setWidgetTopHeight(this.debugger, 1, Unit.PX, 2.4, Unit.EM);			
		}
	}

	/**
	 * This method is called when a widget is attached to the browser's document. 
	 * {@link Canvas3d} should be initialized here.
	 */
	@Override
	public void onLoad()
	{
		Log.debug("RenderingPanel: onLoad()");
		
		this.loadingPanal.show();

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
					loadingPanal.hide();
					add(new BadCanvasPanel(ex.getMessage()));
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
			this.animatedScene.init(getRenderer(), new AnimatedScene.AnimatedSceneCallback() {
				
				@Override
				public void onUpdate()
				{
					// Remove loading panel
					loadingPanal.hide();

					loadDebuger();
					
					// Update debugger
					if(debugger != null)
						debugger.update();
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

				if(animatedScene != null)
					animatedScene.onResize();
			}
		});
	}
	
	public HandlerRegistration addAnimationReadyEventHandler(AnimationReadyHandler handler) 
	{
		Log.debug("RenderingPanel: Registered event for class " + handler.getClass().getName());

		return handlerManager.addHandler(AnimationReadyEvent.TYPE, handler);
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
			this.renderer.setClearColorHex(this.clearColor, this.clearAlpha);
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
}
