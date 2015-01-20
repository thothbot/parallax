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

package thothbot.parallax.core.client.controls;

import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public final class FlyControls extends Controls implements 
	MouseMoveHandler, MouseDownHandler, MouseUpHandler, 
	KeyDownHandler, KeyUpHandler, ContextMenuHandler
{
	
	private double movementSpeed = 1.0;
	private double rollSpeed = 0.005;
	
	private boolean isDragToLook = false;
	private boolean isAutoForward = false;
	
	private class MoveState
	{ 
		public boolean up;
		public boolean down;
		public boolean left;
		public boolean right;
		public boolean forward;
		public boolean back;
		public boolean pitchUp;
		public double pitchDown;
		public double yawLeft;
		public boolean yawRight;
		public boolean rollLeft;
		public boolean rollRight;
	}

	// internals
	private int mouseStatus;
	
	private MoveState moveState;
	private Quaternion tmpQuaternion;
	private Vector3 moveVector;
	private Vector3 rotationVector;
	
	private double viewHalfX;
	private double viewHalfY;
		
	public FlyControls(Object3D object, Widget widget)
	{
		super(object, widget);
			
		if(getWidget().getClass() != RootPanel.class)
			getWidget().getElement().setAttribute( "tabindex", "-1" );
		
		this.viewHalfX = widget.getOffsetWidth() / 2.0;
		this.viewHalfY = widget.getOffsetHeight() / 2.0;

		// disable default target object behavior
		this.tmpQuaternion = new Quaternion();

		this.mouseStatus = 0;
		this.moveState = new MoveState();

		this.moveVector = new Vector3( 0, 0, 0 );
		this.rotationVector = new Vector3( 0, 0, 0 );
		
		getWidget().addDomHandler(this, ContextMenuEvent.getType());

		getWidget().addDomHandler(this, MouseMoveEvent.getType());
		getWidget().addDomHandler(this, MouseDownEvent.getType());
		getWidget().addDomHandler(this, MouseUpEvent.getType());
		RootPanel.get().addDomHandler(this, KeyDownEvent.getType());
		RootPanel.get().addDomHandler(this, KeyUpEvent.getType());		
	}
	
	public void setMovementSpeed(double speed) {
		this.movementSpeed = speed;
	}
	
	public void setRollSpeed(double speed) {
		this.rollSpeed = speed;
	}
	
	public void setDragToLook(boolean isDragToLook) {
		this.isDragToLook = isDragToLook;
	}
	
	public void setAutoForward(boolean isAutoForward) {
		this.isAutoForward = isAutoForward;
	}

	public void update( double delta ) 
	{
		double moveMult = delta * this.movementSpeed;
		double rotMult = delta * this.rollSpeed;

		getObject().translateX( this.moveVector.getX() * moveMult );
		getObject().translateY( this.moveVector.getY() * moveMult );
		getObject().translateZ( this.moveVector.getZ() * moveMult );

		this.tmpQuaternion.set( 
				this.rotationVector.getX() * rotMult, 
				this.rotationVector.getY() * rotMult, 
				this.rotationVector.getZ() * rotMult, 
				1.0).normalize();

		getObject().getQuaternion().multiply( this.tmpQuaternion );
		
		// expose the rotation vector for convenience
		getObject().getRotation().setFromQuaternion( getObject().getQuaternion(), getObject().getRotation().getOrder() );
	}

	@Override
	public void onContextMenu(ContextMenuEvent event) 
	{
		event.preventDefault();	
	}

	@Override
	public void onKeyUp(KeyUpEvent event) 
	{
		switch( event.getNativeEvent().getKeyCode() ) 
		{

		case 87: /*W*/ this.moveState.forward = false; break;
		case 83: /*S*/ this.moveState.back = false; break;

		case 65: /*A*/ this.moveState.left = false; break;
		case 68: /*D*/ this.moveState.right = false; break;

		case 82: /*R*/ this.moveState.up = false; break;
		case 70: /*F*/ this.moveState.down = false; break;

		case 38: /*up*/ this.moveState.pitchUp = false; break;
		case 40: /*down*/ this.moveState.pitchDown = 0; break;

		case 37: /*left*/ this.moveState.yawLeft = 0; break;
		case 39: /*right*/ this.moveState.yawRight = false; break;

		case 81: /*Q*/ this.moveState.rollLeft = false; break;
		case 69: /*E*/ this.moveState.rollRight = false; break;

		}

		this.updateMovementVector();
		this.updateRotationVector();

	}

	@Override
	public void onKeyDown(KeyDownEvent event) 
	{
		if ( event.isAltKeyDown() )
			return;

		switch( event.getNativeEvent().getKeyCode() ) 
		{
			case 87: /*W*/ this.moveState.forward = true; break;
			case 83: /*S*/ this.moveState.back = true; break;

			case 65: /*A*/ this.moveState.left = true; break;
			case 68: /*D*/ this.moveState.right = true; break;

			case 82: /*R*/ this.moveState.up = true; break;
			case 70: /*F*/ this.moveState.down = true; break;

			case 38: /*up*/ this.moveState.pitchUp = true; break;
			case 40: /*down*/ this.moveState.pitchDown = 1; break;

			case 37: /*left*/ this.moveState.yawLeft = 1; break;
			case 39: /*right*/ this.moveState.yawRight = true; break;

			case 81: /*Q*/ this.moveState.rollLeft = true; break;
			case 69: /*E*/ this.moveState.rollRight = true; break;

		}

		this.updateMovementVector();
		this.updateRotationVector();
		
	}

	@Override
	public void onMouseUp(MouseUpEvent event) 
	{
		event.preventDefault();
		event.stopPropagation();

		if ( this.isDragToLook ) 
		{
			this.mouseStatus --;

			this.moveState.yawLeft = this.moveState.pitchDown = 0;
		} 
		else 
		{
			switch ( event.getNativeButton() ) 
			{
			case NativeEvent.BUTTON_LEFT: this.moveState.forward = false; break;
			case NativeEvent.BUTTON_RIGHT: this.moveState.forward = false; break;
			}
		}

		this.updateRotationVector();	
	}

	@Override
	public void onMouseDown(MouseDownEvent event) 
	{
		event.preventDefault();
		event.stopPropagation();

		if ( this.isDragToLook ) 
		{
			this.mouseStatus ++;
		} 
		else 
		{
			switch ( event.getNativeButton() )
			{
			case NativeEvent.BUTTON_LEFT: this.moveState.forward = true; break;
			case NativeEvent.BUTTON_RIGHT: this.moveState.forward = false; break;
			}
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) 
	{
		if ( !this.isDragToLook || this.mouseStatus > 0 ) 
		{
			this.moveState.yawLeft   = - ( event.getX() - viewHalfX ) / viewHalfX;
			this.moveState.pitchDown =   ( event.getY() - viewHalfY ) / viewHalfY;

			this.updateRotationVector();
		}
	}
	
	private void updateMovementVector()
	{
		int forward = ( this.moveState.forward || ( this.isAutoForward && !this.moveState.back ) ) ? 1 : 0;

		this.moveVector.setX( - ((this.moveState.left) ? 1 : 0)    + ((this.moveState.right) ? 1 : 0) );
		this.moveVector.setY( - ((this.moveState.down) ? 1 : 0)    + ((this.moveState.up) ? 1 : 0) );
		this.moveVector.setZ( - forward + ((this.moveState.back) ? 1 : 0) );
	}

	private void updateRotationVector()
	{
		this.rotationVector.setX( - this.moveState.pitchDown + ((this.moveState.pitchUp) ? 1 : 0) );
		this.rotationVector.setY( - ((this.moveState.yawRight) ? 1 : 0)  + this.moveState.yawLeft );
		this.rotationVector.setZ( - ((this.moveState.rollRight) ? 1 : 0) + ((this.moveState.rollLeft) ? 1 : 0) );
	}
}
