/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * This file is part of Parallax project.
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax.platforms.gwt;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.Animation;
import org.parallax3d.parallax.Input;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.input.InputHandler;
import org.parallax3d.parallax.system.AnimationReadyListener;
import org.parallax3d.parallax.system.ParallaxRuntimeException;
import org.parallax3d.parallax.system.ViewportResizeBus;
import org.parallax3d.parallax.system.gl.GL20;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.webgl.client.WebGLContextAttributes;
import com.google.gwt.webgl.client.WebGLRenderingContext;

public class GwtRenderingContext implements ResizeHandler, RenderingContext, AnimationScheduler.AnimationCallback
{

	/**
	 * The ID of the pending animation request.
	 */
	private AnimationScheduler.AnimationHandle	animationHandle;

	Panel										root;

	CanvasElement								canvas;
	WebGLRenderingContext						context;
	GLRenderer									renderer;
	GL20										gl;

	GwtInput									input;

	Animation									listener;

	List< AnimationReadyListener >				animationReadyListener	= new ArrayList<>();

	int											lastWidth;
	int											lastHeight;

	double										fps						= 0;
	long										lastTimeStamp			= System.currentTimeMillis();
	long										frameId					= -1;
	double										deltaTime				= 0;
	double										time					= 0;
	int											frames;

	GwtRenderingContextConfiguration			config;

	public GwtRenderingContext ( final Panel root ) throws ParallaxRuntimeException
	{
		this( root, new GwtRenderingContextConfiguration() );
	}

	public GwtRenderingContext ( final Panel root, final GwtRenderingContextConfiguration config )
			throws ParallaxRuntimeException
	{
		this.root = root;
		root.clear();

		final Canvas canvasWidget = Canvas.createIfSupported();
		if ( canvasWidget == null )
		{
			throw new ParallaxRuntimeException( "Canvas not supported" );
		}

		final int width = root.getOffsetWidth();
		final int height = root.getOffsetHeight();

		if ( ( width == 0 ) || ( height == 0 ) )
		{
			new ParallaxRuntimeException( "Width or Height of the Panel is 0" );
		}

		this.lastWidth = width;
		this.lastHeight = height;

		this.canvas = canvasWidget.getCanvasElement();
		root.add( canvasWidget );
		this.canvas.setWidth( width );
		this.canvas.setHeight( height );
		this.config = config;

		final WebGLContextAttributes attributes = WebGLContextAttributes.create();
		attributes.setAntialias( config.isAntialiasing() );
		attributes.setStencil( config.isStencil() );
		attributes.setAlpha( config.isAlpha() );
		attributes.setPremultipliedAlpha( config.isPremultipliedAlpha() );
		attributes.setPreserveDrawingBuffer( config.isPreserveDrawingBuffer() );

		this.context = WebGLRenderingContext.getContext( this.canvas, attributes );
		this.context.viewport( 0, 0, width, height );

		this.gl = new GwtGL20( this.context );

		this.renderer = new GLRenderer( this.gl, width, height );

		this.input = new GwtInput( this.canvas );

		this.addEventListeners();

		Window.addResizeHandler( this );
	}

	@Override
	public void setAnimation ( final Animation animation )
	{
		this.listener = animation;
		// tell listener about app creation
		try
		{
			this.renderer.setDefaultGLState();

			if ( this.listener instanceof InputHandler )
			{
				this.input.addInputHandler( (InputHandler)this.listener );
			}
			this.listener.onStart( this );
			this.listener.onResize( this );

			for ( final AnimationReadyListener ready : this.animationReadyListener )
			{
				ready.onAnimationReady( this.listener );
			}

		}
		catch ( final Throwable t )
		{
			Log.error( "GwtRendering: exception: " + t.getMessage(), t );
			t.printStackTrace();
			throw new ParallaxRuntimeException( t );
		}

		this.run();
	}

	private void refresh ()
	{
		final long currTimeStamp = System.currentTimeMillis();
		this.deltaTime = ( currTimeStamp - this.lastTimeStamp ) / 1000.0;
		this.lastTimeStamp = currTimeStamp;
		this.time += this.deltaTime;
		this.frames++;
		if ( this.time > 1 )
		{
			this.fps = this.frames;
			this.time = 0;
			this.frames = 0;
		}
	}

	@Override
	public void execute ( final double timestamp )
	{
		try
		{
			this.mainLoop();
		}
		catch ( final Throwable t )
		{
			Log.error( "GwtApplication: exception: " + t.getMessage(), t );
			throw new ParallaxRuntimeException( t );
		}

		this.animationHandle = AnimationScheduler.get().requestAnimationFrame( this, this.canvas );
	}

	private void mainLoop ()
	{

		this.refresh();

		this.frameId++;
		this.listener.onUpdate( this );

	}

	@Override
	public void onResize ( final ResizeEvent resizeEvent )
	{
		Scheduler.get().scheduleDeferred( new Scheduler.ScheduledCommand()
		{
			@Override
			public void execute ()
			{

				GwtRenderingContext.this.lastWidth = GwtRenderingContext.this.getWidth();
				GwtRenderingContext.this.lastHeight = GwtRenderingContext.this.getHeight();

				if ( ( GwtRenderingContext.this.lastWidth > 0 ) && ( GwtRenderingContext.this.lastHeight > 0 ) )
				{

					if ( !GwtRenderingContext.this.isRun() )
					{
						GwtRenderingContext.this.run();
					}

					ViewportResizeBus.onViewportResize( GwtRenderingContext.this.lastWidth,
							GwtRenderingContext.this.lastHeight );

					GwtRenderingContext.this.canvas.setWidth( GwtRenderingContext.this.lastWidth );
					GwtRenderingContext.this.canvas.setHeight( GwtRenderingContext.this.lastHeight );
					GwtRenderingContext.this.renderer.setSize( GwtRenderingContext.this.lastWidth,
							GwtRenderingContext.this.lastHeight );
					GwtRenderingContext.this.listener.onResize( GwtRenderingContext.this );

				}
				else
				{
					GwtRenderingContext.this.stop();
				}
			}
		} );
	}

	@Override
	public void stop ()
	{
		// Cancel the animation request.
		if ( this.animationHandle != null )
		{
			this.animationHandle.cancel();
			this.animationHandle = null;
		}
	}

	@Override
	public void run ()
	{

		this.stop();
		// Execute the first callback.
		AnimationScheduler.get().requestAnimationFrame( this, this.canvas );
	}

	@Override
	public boolean isRun ()
	{
		return ( this.animationHandle != null );
	}

	private native void addEventListeners () /*-{
												var self = this;
												$doc.addEventListener('visibilitychange', function (e) {
												self.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::onVisibilityChange(Z)($doc['hidden'] !== true);
												});
												}-*/;

	private void onVisibilityChange ( final boolean visible )
	{
		if ( visible )
		{
			this.run();
		}
		else
		{
			this.stop();
		}
	}

	@Override
	public void addAnimationReadyListener ( final AnimationReadyListener animationReadyListener )
	{
		this.animationReadyListener.add( animationReadyListener );
	}

	public WebGLRenderingContext getContext ()
	{
		return this.context;
	}

	@Override
	public GLRenderer getRenderer ()
	{
		return this.renderer;
	}

	@Override
	public Input getInput ()
	{
		return this.input;
	}

	@Override
	public GL20 getGL20 ()
	{
		return this.gl;
	}

	@Override
	public int getWidth ()
	{
		return this.root.getOffsetWidth();
	}

	@Override
	public int getHeight ()
	{
		return this.root.getOffsetHeight();
	}

	@Override
	public double getAspectRation ()
	{
		return this.getWidth() / (double)this.getHeight();
	}

	@Override
	public long getFrameId ()
	{
		return this.frameId;
	}

	@Override
	public double getDeltaTime ()
	{
		return this.deltaTime;
	}

	@Override
	public int getFramesPerSecond ()
	{
		return (int)this.fps;
	}

	@Override
	public float getPpiX ()
	{
		return 96;
	}

	@Override
	public float getPpiY ()
	{
		return 96;
	}

	@Override
	public float getDensity ()
	{
		return 96.0f / 160;
	}

	@Override
	public double getRawDeltaTime ()
	{
		return this.getDeltaTime();
	}

	@Override
	public boolean supportsDisplayModeChange ()
	{
		return this.supportsFullscreenJSNI();
	}

	private native boolean supportsFullscreenJSNI () /*-{
														if ("fullscreenEnabled" in $doc) {
														return $doc.fullscreenEnabled;
														}
														if ("webkitFullscreenEnabled" in $doc) {
														return $doc.webkitFullscreenEnabled;
														}
														if ("mozFullScreenEnabled" in $doc) {
														return $doc.mozFullScreenEnabled;
														}
														if ("msFullscreenEnabled" in $doc) {
														return $doc.msFullscreenEnabled;
														}
														return false;
														}-*/;

	private native int getScreenWidthJSNI () /*-{
												return $wnd.screen.width;
												}-*/;

	private native int getScreenHeightJSNI () /*-{
												return $wnd.screen.height;
												}-*/;

	private native void exitFullscreen () /*-{
											if ($doc.exitFullscreen)
											$doc.exitFullscreen();
											if ($doc.msExitFullscreen)
											$doc.msExitFullscreen();
											if ($doc.webkitExitFullscreen)
											$doc.webkitExitFullscreen();
											if ($doc.mozExitFullscreen)
											$doc.mozExitFullscreen();
											if ($doc.webkitCancelFullScreen) // Old WebKit
											$doc.webkitCancelFullScreen();
											}-*/;

	private void fullscreenChanged ()
	{
		if ( !this.isFullscreen() )
		{
			this.renderer.setSize( this.lastWidth, this.lastHeight );
		}
	}

	@Override
	public void setFullscreen ()
	{
		if ( this.isFullscreenJSNI() )
		{
			this.exitFullscreen();
		}
		else
		{
			this.setFullscreenJSNI( this, this.canvas );
		}
	}

	private native boolean setFullscreenJSNI ( GwtRenderingContext graphics, CanvasElement element ) /*-{
																										// Attempt to use the non-prefixed standard API (https://fullscreen.spec.whatwg.org)
																										if (element.requestFullscreen) {
																										element.width = $wnd.screen.width;
																										element.height = $wnd.screen.height;
																										element.requestFullscreen();
																										$doc.addEventListener(
																										"fullscreenchange",
																										function () {
																										graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
																										}, false);
																										return true;
																										}
																										// Attempt to the vendor specific variants of the API
																										if (element.webkitRequestFullScreen) {
																										element.width = $wnd.screen.width;
																										element.height = $wnd.screen.height;
																										element.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
																										$doc.addEventListener(
																										"webkitfullscreenchange",
																										function () {
																										graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
																										}, false);
																										return true;
																										}
																										if (element.mozRequestFullScreen) {
																										element.width = $wnd.screen.width;
																										element.height = $wnd.screen.height;
																										element.mozRequestFullScreen();
																										$doc.addEventListener(
																										"mozfullscreenchange",
																										function () {
																										graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
																										}, false);
																										return true;
																										}
																										if (element.msRequestFullscreen) {
																										element.width = $wnd.screen.width;
																										element.height = $wnd.screen.height;
																										element.msRequestFullscreen();
																										$doc.addEventListener(
																										"msfullscreenchange",
																										function () {
																										graphics.@org.parallax3d.parallax.platforms.gwt.GwtRenderingContext::fullscreenChanged()();
																										}, false);
																										return true;
																										}
																										
																										return false;
																										}-*/;

	@Override
	public boolean isFullscreen ()
	{
		return this.isFullscreenJSNI();
	}

	private native boolean isFullscreenJSNI () /*-{
												// Standards compliant check for fullscreen
												if ("fullscreenElement" in $doc) {
												return $doc.fullscreenElement != null;
												}
												// Vendor prefixed versions of standard check
												if ("msFullscreenElement" in $doc) {
												return $doc.msFullscreenElement != null;
												}
												if ("webkitFullscreenElement" in $doc) {
												return $doc.webkitFullscreenElement != null;
												}
												if ("mozFullScreenElement" in $doc) { // Yes, with a capital 'S'
												return $doc.mozFullScreenElement != null;
												}
												// Older, non-standard ways of checking for fullscreen
												if ("webkitIsFullScreen" in $doc) {
												return $doc.webkitIsFullScreen;
												}
												if ("mozFullScreen" in $doc) {
												return $doc.mozFullScreen;
												}
												return false
												}-*/;
}
