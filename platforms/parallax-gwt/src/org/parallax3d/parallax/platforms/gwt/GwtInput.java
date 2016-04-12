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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.parallax3d.parallax.Input;
import org.parallax3d.parallax.input.InputHandler;
import org.parallax3d.parallax.input.KeyDownHandler;
import org.parallax3d.parallax.input.KeyTypedHandler;
import org.parallax3d.parallax.input.KeyUpHandler;
import org.parallax3d.parallax.input.ScrolledHandler;
import org.parallax3d.parallax.input.TouchDownHandler;
import org.parallax3d.parallax.input.TouchDraggedHandler;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.input.TouchUpHandler;
import org.parallax3d.parallax.system.Duration;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;

public class GwtInput implements Input
{

	static final int						MAX_TOUCHES		= 20;

	Set< Integer >							pressedButtons	= new HashSet<>();

	private final Map< Integer, Integer >	touchMap		= new HashMap<>();

	boolean									justTouched		= false;
	boolean									keyJustPressed	= false;
	boolean[]								justPressedKeys	= new boolean[ 256 ];

	int										pressedKeyCount	= 0;
	boolean[]								pressedKeys		= new boolean[ 256 ];

	private final boolean[]					touched			= new boolean[ GwtInput.MAX_TOUCHES ];

	private final int[]						touchX			= new int[ GwtInput.MAX_TOUCHES ];
	private final int[]						touchY			= new int[ GwtInput.MAX_TOUCHES ];
	private final int[]						deltaX			= new int[ GwtInput.MAX_TOUCHES ];
	private final int[]						deltaY			= new int[ GwtInput.MAX_TOUCHES ];

	long									currentEventTimeStamp;
	final CanvasElement						canvas;
	boolean									hasFocus		= true;

	// Handlers
	Set< KeyDownHandler >					keyDownHandler      = new HashSet<>();
	Set< KeyTypedHandler >					keyTypedHandler     = new HashSet<>();
	Set< KeyUpHandler >					    keyUpHandler        = new HashSet<>();
	Set< ScrolledHandler >					scrolledHandler     = new HashSet<>();
	Set< TouchDownHandler >				    touchDownHandler    = new HashSet<>();
	Set< TouchDraggedHandler >				touchDraggedHandler = new HashSet<>();
	Set< TouchUpHandler >					touchUpHandler      = new HashSet<>();
	Set< TouchMoveHandler >				    touchMoveHandler    = new HashSet<>();

	public GwtInput ( final CanvasElement canvas )
	{
		this.canvas = canvas;
		this.hookEvents();
	}

	void reset ()
	{
		this.justTouched = false;
		if ( this.keyJustPressed )
		{
			this.keyJustPressed = false;
			for ( int i = 0 ; i < this.justPressedKeys.length ; i++ )
			{
				this.justPressedKeys[ i ] = false;
			}
		}
	}

	@Override
	public void addInputHandler ( final InputHandler handler )
	{
		if ( handler instanceof KeyDownHandler )
		{
			this.keyDownHandler.add( (KeyDownHandler)handler );
		}
		if ( handler instanceof KeyTypedHandler )
		{
			this.keyTypedHandler.add( (KeyTypedHandler)handler );
		}
		if ( handler instanceof KeyUpHandler )
		{
			this.keyUpHandler.add( (KeyUpHandler)handler );
		}
		if ( handler instanceof ScrolledHandler )
		{
			this.scrolledHandler.add( (ScrolledHandler)handler );
		}
		if ( handler instanceof TouchDownHandler )
		{
			this.touchDownHandler.add( (TouchDownHandler)handler );
		}
		if ( handler instanceof TouchDraggedHandler )
		{
			this.touchDraggedHandler.add( (TouchDraggedHandler)handler );
		}
		if ( handler instanceof TouchUpHandler )
		{
			this.touchUpHandler.add( (TouchUpHandler)handler );
		}
		if ( handler instanceof TouchMoveHandler )
		{
			this.touchMoveHandler.add( (TouchMoveHandler)handler );
		}
	}

	@Override
	public void removeInputHandler ( final InputHandler handler )
	{
		if ( handler instanceof KeyDownHandler )
		{
			this.keyDownHandler.remove( handler );
		}
		if ( handler instanceof KeyTypedHandler )
		{
			this.keyTypedHandler.remove( handler );
		}
		if ( handler instanceof KeyUpHandler )
		{
			this.keyUpHandler.remove( handler );
		}
		if ( handler instanceof ScrolledHandler )
		{
			this.scrolledHandler.remove( handler );
		}
		if ( handler instanceof TouchDownHandler )
		{
			this.touchDownHandler.remove( handler );
		}
		if ( handler instanceof TouchDraggedHandler )
		{
			this.touchDraggedHandler.remove( handler );
		}
		if ( handler instanceof TouchUpHandler )
		{
			this.touchUpHandler.remove( handler );
		}
		if ( handler instanceof TouchMoveHandler )
		{
			this.touchMoveHandler.remove( handler );
		}
	}

	@Override
	public float getAccelerometerX ()
	{
		return 0;
	}

	@Override
	public float getAccelerometerY ()
	{
		return 0;
	}

	@Override
	public float getAccelerometerZ ()
	{
		return 0;
	}

	@Override
	public int getX ()
	{
		return this.touchX[ 0 ];
	}

	@Override
	public int getX ( final int pointer )
	{
		return this.touchX[ pointer ];
	}

	@Override
	public int getDeltaX ()
	{
		return this.deltaX[ 0 ];
	}

	@Override
	public int getDeltaX ( final int pointer )
	{
		return this.deltaX[ pointer ];
	}

	@Override
	public int getY ()
	{
		return this.touchY[ 0 ];
	}

	@Override
	public int getY ( final int pointer )
	{
		return this.touchY[ pointer ];
	}

	@Override
	public int getDeltaY ()
	{
		return this.deltaY[ 0 ];
	}

	@Override
	public int getDeltaY ( final int pointer )
	{
		return this.deltaY[ pointer ];
	}

	@Override
	public boolean isTouched ()
	{
		for ( int pointer = 0 ; pointer < GwtInput.MAX_TOUCHES ; pointer++ )
		{
			if ( this.touched[ pointer ] )
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isButtonPressed ( final int button )
	{
		return ( button == Buttons.LEFT ) && this.touched[ 0 ];
	}

	@Override
	public boolean isKeyPressed ( final int key )
	{
		if ( ( key < 0 ) || ( key > 255 ) )
		{
			return false;
		}
		return this.pressedKeys[ key ];
	}

	@Override
	public Orientation getNativeOrientation ()
	{
		return Orientation.Landscape;
	}

	@Override
	public boolean isPeripheralAvailable ( final Peripheral peripheral )
	{
		if ( peripheral == Peripheral.Accelerometer )
		{
			return false;
		}
		if ( peripheral == Peripheral.Compass )
		{
			return false;
		}
		if ( peripheral == Peripheral.HardwareKeyboard )
		{
			return true;
		}
		if ( peripheral == Peripheral.MultitouchScreen )
		{
			return GwtInput.isTouchScreen();
		}
		if ( peripheral == Peripheral.OnscreenKeyboard )
		{
			return false;
		}
		if ( peripheral == Peripheral.Vibrator )
		{
			return false;
		}
		return false;
	}

	@Override
	public boolean isCursorCatched ()
	{
		return this.isCursorCatchedJSNI();
	}

	/** Kindly borrowed from PlayN. **/
	protected int getRelativeX ( final NativeEvent e, final CanvasElement target )
	{
		final float xScaleRatio = ( target.getWidth() * 1f ) / target.getClientWidth(); // Correct
																						 // for
																						 // canvas
																						 // CSS
																						 // scaling
		return Math.round( xScaleRatio * ( ( e.getClientX() - target.getAbsoluteLeft() ) + target.getScrollLeft()
				+ target.getOwnerDocument().getScrollLeft() ) );
	}

	/** Kindly borrowed from PlayN. **/
	protected int getRelativeY ( final NativeEvent e, final CanvasElement target )
	{
		final float yScaleRatio = ( target.getHeight() * 1f ) / target.getClientHeight(); // Correct
																							 // for
																							 // canvas
																							 // CSS
																							 // scaling
		return Math.round( yScaleRatio * ( ( e.getClientY() - target.getAbsoluteTop() ) + target.getScrollTop()
				+ target.getOwnerDocument().getScrollTop() ) );
	}

	protected int getRelativeX ( final Touch touch, final CanvasElement target )
	{
		final float xScaleRatio = ( target.getWidth() * 1f ) / target.getClientWidth(); // Correct
																						 // for
																						 // canvas
																						 // CSS
																						 // scaling
		return Math.round( xScaleRatio * touch.getRelativeX( target ) );
	}

	protected int getRelativeY ( final Touch touch, final CanvasElement target )
	{
		final float yScaleRatio = ( target.getHeight() * 1f ) / target.getClientHeight(); // Correct
																							 // for
																							 // canvas
																							 // CSS
																							 // scaling
		return Math.round( yScaleRatio * touch.getRelativeY( target ) );
	}

	private void hookEvents ()
	{
		GwtInput.addEventListener( this.canvas, "mousedown", this, true );
		GwtInput.addEventListener( Document.get(), "mousedown", this, true );
		GwtInput.addEventListener( this.canvas, "mouseup", this, true );
		GwtInput.addEventListener( Document.get(), "mouseup", this, true );
		GwtInput.addEventListener( this.canvas, "mousemove", this, true );
		GwtInput.addEventListener( Document.get(), "mousemove", this, true );
		GwtInput.addEventListener( this.canvas, GwtInput.getMouseWheelEvent(), this, true );
		GwtInput.addEventListener( Document.get(), "keydown", this, false );
		GwtInput.addEventListener( Document.get(), "keyup", this, false );
		GwtInput.addEventListener( Document.get(), "keypress", this, false );

		GwtInput.addEventListener( this.canvas, "touchstart", this, true );
		GwtInput.addEventListener( this.canvas, "touchmove", this, true );
		GwtInput.addEventListener( this.canvas, "touchcancel", this, true );
		GwtInput.addEventListener( this.canvas, "touchend", this, true );

	}

	private int getButton ( final int button )
	{
		if ( button == NativeEvent.BUTTON_LEFT )
		{
			return Buttons.LEFT;
		}
		if ( button == NativeEvent.BUTTON_RIGHT )
		{
			return Buttons.RIGHT;
		}
		if ( button == NativeEvent.BUTTON_MIDDLE )
		{
			return Buttons.MIDDLE;
		}
		return Buttons.LEFT;
	}

	private void handleEvent ( final NativeEvent e )
	{
		if ( e.getType().equals( "mousedown" ) )
		{
			if ( !e.getEventTarget().equals( this.canvas ) || this.touched[ 0 ] )
			{
				final float mouseX = this.getRelativeX( e, this.canvas );
				final float mouseY = this.getRelativeY( e, this.canvas );
				if ( ( mouseX < 0 ) || ( mouseX > this.canvas.getWidth() ) || ( mouseY < 0 )
						|| ( mouseY > this.canvas.getHeight() ) )
				{
					this.hasFocus = false;
				}
				return;
			}
			this.hasFocus = true;
			this.justTouched = true;
			this.touched[ 0 ] = true;
			this.pressedButtons.add( this.getButton( e.getButton() ) );
			this.deltaX[ 0 ] = 0;
			this.deltaY[ 0 ] = 0;
			if ( this.isCursorCatched() )
			{
				this.touchX[ 0 ] += this.getMovementXJSNI( e );
				this.touchY[ 0 ] += this.getMovementYJSNI( e );
			}
			else
			{
				this.touchX[ 0 ] = this.getRelativeX( e, this.canvas );
				this.touchY[ 0 ] = this.getRelativeY( e, this.canvas );
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			for ( final TouchDownHandler handler : this.touchDownHandler )
			{
				handler.onTouchDown( this.touchX[ 0 ], this.touchY[ 0 ], 0, this.getButton( e.getButton() ) );
			}
		}

		if ( e.getType().equals( "mousemove" ) )
		{
			if ( this.isCursorCatched() )
			{
				this.deltaX[ 0 ] = (int)this.getMovementXJSNI( e );
				this.deltaY[ 0 ] = (int)this.getMovementYJSNI( e );
				this.touchX[ 0 ] += this.getMovementXJSNI( e );
				this.touchY[ 0 ] += this.getMovementYJSNI( e );
			}
			else
			{
				this.deltaX[ 0 ] = this.getRelativeX( e, this.canvas ) - this.touchX[ 0 ];
				this.deltaY[ 0 ] = this.getRelativeY( e, this.canvas ) - this.touchY[ 0 ];
				this.touchX[ 0 ] = this.getRelativeX( e, this.canvas );
				this.touchY[ 0 ] = this.getRelativeY( e, this.canvas );
			}
			this.currentEventTimeStamp = Duration.nanoTime();

			for ( final TouchMoveHandler handler : this.touchMoveHandler )
			{
				handler.onTouchMove( this.touchX[ 0 ], this.touchY[ 0 ], 0 );
			}
			if ( this.touched[ 0 ] )
			{
				for ( final TouchDraggedHandler handler : this.touchDraggedHandler )
				{
					handler.onTouchDragged( this.touchX[ 0 ], this.touchY[ 0 ], 0 );
				}
			}
		}

		if ( e.getType().equals( "mouseup" ) )
		{
			if ( !this.touched[ 0 ] )
			{
				return;
			}
			this.pressedButtons.remove( this.getButton( e.getButton() ) );
			this.touched[ 0 ] = this.pressedButtons.size() > 0;
			if ( this.isCursorCatched() )
			{
				this.deltaX[ 0 ] = (int)this.getMovementXJSNI( e );
				this.deltaY[ 0 ] = (int)this.getMovementYJSNI( e );
				this.touchX[ 0 ] += this.getMovementXJSNI( e );
				this.touchY[ 0 ] += this.getMovementYJSNI( e );
			}
			else
			{
				this.deltaX[ 0 ] = this.getRelativeX( e, this.canvas ) - this.touchX[ 0 ];
				this.deltaY[ 0 ] = this.getRelativeY( e, this.canvas ) - this.touchY[ 0 ];
				this.touchX[ 0 ] = this.getRelativeX( e, this.canvas );
				this.touchY[ 0 ] = this.getRelativeY( e, this.canvas );
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			this.touched[ 0 ] = false;
			for ( final TouchUpHandler handler : this.touchUpHandler )
			{
				handler.onTouchUp( this.touchX[ 0 ], this.touchY[ 0 ], 0, this.getButton( e.getButton() ) );
			}
		}
		if ( e.getType().equals( GwtInput.getMouseWheelEvent() ) )
		{
			for ( final ScrolledHandler handler : this.scrolledHandler )
			{
				handler.onScrolled( (int)GwtInput.getMouseWheelVelocity( e ) );
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			e.preventDefault();
		}
		if ( e.getType().equals( "keydown" ) && this.hasFocus )
		{
			final int code = e.getKeyCode();
			if ( !this.pressedKeys[ code ] )
			{
				this.pressedKeyCount++;
				this.pressedKeys[ code ] = true;
				this.keyJustPressed = true;
				this.justPressedKeys[ code ] = true;
				for ( final KeyDownHandler handler : this.keyDownHandler )
				{
					handler.onKeyDown( code );
				}
			}
		}

		if ( e.getType().equals( "keypress" ) && this.hasFocus )
		{
			// System.out.println("keypress");
			final char c = (char)e.getCharCode();
			for ( final KeyTypedHandler handler : this.keyTypedHandler )
			{
				handler.onKeyTyped( c );
			}
		}

		if ( e.getType().equals( "keyup" ) && this.hasFocus )
		{
			// System.out.println("keyup");
			final int code = e.getKeyCode();
			if ( this.pressedKeys[ code ] )
			{
				this.pressedKeyCount--;
				this.pressedKeys[ code ] = false;
			}

			for ( final KeyUpHandler handler : this.keyUpHandler )
			{
				handler.onKeyUp( code );
			}
		}

		if ( e.getType().equals( "touchstart" ) )
		{
			this.justTouched = true;
			final JsArray< Touch > touches = e.getChangedTouches();
			for ( int i = 0, j = touches.length() ; i < j ; i++ )
			{
				final Touch touch = touches.get( i );
				final int real = touch.getIdentifier();
				int touchId;
				this.touchMap.put( real, touchId = this.getAvailablePointer() );
				this.touched[ touchId ] = true;
				this.touchX[ touchId ] = this.getRelativeX( touch, this.canvas );
				this.touchY[ touchId ] = this.getRelativeY( touch, this.canvas );
				this.deltaX[ touchId ] = 0;
				this.deltaY[ touchId ] = 0;

				for ( final TouchDownHandler handler : this.touchDownHandler )
				{
					handler.onTouchDown( this.touchX[ touchId ], this.touchY[ touchId ], touchId, Buttons.LEFT );
				}
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			e.preventDefault();
		}
		if ( e.getType().equals( "touchmove" ) )
		{
			final JsArray< Touch > touches = e.getChangedTouches();
			for ( int i = 0, j = touches.length() ; i < j ; i++ )
			{
				final Touch touch = touches.get( i );
				final int real = touch.getIdentifier();
				final int touchId = this.touchMap.get( real );
				this.deltaX[ touchId ] = this.getRelativeX( touch, this.canvas ) - this.touchX[ touchId ];
				this.deltaY[ touchId ] = this.getRelativeY( touch, this.canvas ) - this.touchY[ touchId ];
				this.touchX[ touchId ] = this.getRelativeX( touch, this.canvas );
				this.touchY[ touchId ] = this.getRelativeY( touch, this.canvas );
				for ( final TouchDraggedHandler handler : this.touchDraggedHandler )
				{
					handler.onTouchDragged( this.touchX[ touchId ], this.touchY[ touchId ], touchId );
				}
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			e.preventDefault();
		}
		if ( e.getType().equals( "touchcancel" ) )
		{
			final JsArray< Touch > touches = e.getChangedTouches();
			for ( int i = 0, j = touches.length() ; i < j ; i++ )
			{
				final Touch touch = touches.get( i );
				final int real = touch.getIdentifier();
				final int touchId = this.touchMap.get( real );
				this.touchMap.remove( real );
				this.touched[ touchId ] = false;
				this.deltaX[ touchId ] = this.getRelativeX( touch, this.canvas ) - this.touchX[ touchId ];
				this.deltaY[ touchId ] = this.getRelativeY( touch, this.canvas ) - this.touchY[ touchId ];
				this.touchX[ touchId ] = this.getRelativeX( touch, this.canvas );
				this.touchY[ touchId ] = this.getRelativeY( touch, this.canvas );
				for ( final TouchUpHandler handler : this.touchUpHandler )
				{
					handler.onTouchUp( this.touchX[ touchId ], this.touchY[ touchId ], touchId, Buttons.LEFT );
				}
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			e.preventDefault();
		}
		if ( e.getType().equals( "touchend" ) )
		{
			final JsArray< Touch > touches = e.getChangedTouches();
			for ( int i = 0, j = touches.length() ; i < j ; i++ )
			{
				final Touch touch = touches.get( i );
				final int real = touch.getIdentifier();
				final int touchId = this.touchMap.get( real );
				this.touchMap.remove( real );
				this.touched[ touchId ] = false;
				this.deltaX[ touchId ] = this.getRelativeX( touch, this.canvas ) - this.touchX[ touchId ];
				this.deltaY[ touchId ] = this.getRelativeY( touch, this.canvas ) - this.touchY[ touchId ];
				this.touchX[ touchId ] = this.getRelativeX( touch, this.canvas );
				this.touchY[ touchId ] = this.getRelativeY( touch, this.canvas );
				for ( final TouchUpHandler handler : this.touchUpHandler )
				{
					handler.onTouchUp( this.touchX[ touchId ], this.touchY[ touchId ], touchId, Buttons.LEFT );
				}
			}
			this.currentEventTimeStamp = Duration.nanoTime();
			e.preventDefault();
		}
		// if(hasFocus) e.preventDefault();
	}

	private int getAvailablePointer ()
	{
		for ( int i = 0 ; i < GwtInput.MAX_TOUCHES ; i++ )
		{
			if ( !this.touchMap.containsValue( i ) )
			{
				return i;
			}
		}
		return -1;
	}

	/** Kindly borrowed from PlayN. **/
	protected static native String getMouseWheelEvent () /*-{
															if (navigator.userAgent.toLowerCase().indexOf('firefox') != -1) {
															return "DOMMouseScroll";
															} else {
															return "mousewheel";
															}
															}-*/;

	static native void addEventListener ( JavaScriptObject target, String name, GwtInput handler,
			boolean capture ) /*-{
								target
								.addEventListener(
								name,
								function(e) {
								handler.@org.parallax3d.parallax.platforms.gwt.GwtInput::handleEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
								}, capture);
								}-*/;

	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * 
	 * @return is Cursor catched
	 */
	private native boolean isCursorCatchedJSNI () /*-{
													if (!navigator.pointer) {
													navigator.pointer = navigator.webkitPointer || navigator.mozPointer;
													}
													if (navigator.pointer) {
													if (typeof (navigator.pointer.isLocked) === "boolean") {
													// Chrome initially launched with this interface
													return navigator.pointer.isLocked;
													} else if (typeof (navigator.pointer.isLocked) === "function") {
													// Some older builds might provide isLocked as a function
													return navigator.pointer.isLocked();
													} else if (typeof (navigator.pointer.islocked) === "function") {
													// For compatibility with early Firefox build
													return navigator.pointer.islocked();
													}
													}
													return false;
													}-*/;

	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * 
	 * @param event
	 * @return
	 */
	private native float getMovementXJSNI ( NativeEvent event ) /*-{
																return event.movementX || event.webkitMovementX || 0;
																}-*/;

	/**
	 * from https://github.com/toji/game-shim/blob/master/game-shim.js
	 * 
	 * @param event
	 * @return
	 */
	private native float getMovementYJSNI ( NativeEvent event ) /*-{
																return event.movementY || event.webkitMovementY || 0;
																}-*/;

	private static native boolean isTouchScreen () /*-{
													return (('ontouchstart' in window) || (navigator.msMaxTouchPoints > 0));
													}-*/;

	private static native float getMouseWheelVelocity ( NativeEvent evt ) /*-{
																			var delta = 0.0;
																			var agentInfo = @org.parallax3d.parallax.platforms.gwt.GwtParallax::agentInfo()();
																			
																			if (agentInfo.isFirefox) {
																			if (agentInfo.isMacOS) {
																			delta = 1.0 * evt.detail;
																			} else {
																			delta = 1.0 * evt.detail / 3;
																			}
																			} else if (agentInfo.isOpera) {
																			if (agentInfo.isLinux) {
																			delta = -1.0 * evt.wheelDelta / 80;
																			} else {
																			// on mac
																			delta = -1.0 * evt.wheelDelta / 40;
																			}
																			} else if (agentInfo.isChrome || agentInfo.isSafari) {
																			delta = -1.0 * evt.wheelDelta / 120;
																			// handle touchpad for chrome
																			if (Math.abs(delta) < 1) {
																			if (agentInfo.isWindows) {
																			delta = -1.0 * evt.wheelDelta;
																			} else if (agentInfo.isMacOS) {
																			delta = -1.0 * evt.wheelDelta / 3;
																			}
																			}
																			}
																			return delta;
																			}-*/;
}
