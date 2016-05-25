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

package org.parallax3d.parallax.controllers;

import org.parallax3d.parallax.Input;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.input.*;
import org.parallax3d.parallax.math.Quaternion;
import org.parallax3d.parallax.math.Vector3;

public final class FlyControls extends Controls implements TouchMoveHandler, TouchDownHandler, TouchUpHandler, KeyDownHandler, KeyUpHandler
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
		
	public FlyControls(Object3D object, RenderingContext context)
	{
		super(object, context);

		this.viewHalfX = context.getWidth() / 2.0;
		this.viewHalfY = context.getHeight() / 2.0;

		// disable default target object behavior
		this.tmpQuaternion = new Quaternion();

		this.mouseStatus = 0;
		this.moveState = new MoveState();

		this.moveVector = new Vector3( 0, 0, 0 );
		this.rotationVector = new Vector3( 0, 0, 0 );

		context.getInput().addInputHandler( this );
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
		delta *= 0.0001;
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
	public void onKeyUp(int keycode) {
		switch( keycode )
		{

			case KeyCodes.KEY_W: 
				this.moveState.forward = false; 
				break;
			case KeyCodes.KEY_S: 
				this.moveState.back = false; 
				break;

			case KeyCodes.KEY_A: 
				this.moveState.left = false; 
				break;
			case KeyCodes.KEY_D: 
				this.moveState.right = false; 
				break;

			case KeyCodes.KEY_R: 
				this.moveState.up = false; 
				break;
			case KeyCodes.KEY_F: 
				this.moveState.down = false; 
				break;

			case KeyCodes.KEY_UP: 
				this.moveState.pitchUp = false; 
				break;
			case KeyCodes.KEY_DOWN: 
				this.moveState.pitchDown = 0; 
				break;

			case KeyCodes.KEY_LEFT: 
				this.moveState.yawLeft = 0; 
				break;
			case KeyCodes.KEY_RIGHT: 
				this.moveState.yawRight = false; 
				break;

			case KeyCodes.KEY_Q: 
				this.moveState.rollLeft = false; 
				break;
			case KeyCodes.KEY_E: 
				this.moveState.rollRight = false; 
				break;
			default:
				break;

		}

		this.updateMovementVector();
		this.updateRotationVector();

	}

	@Override
	public void onKeyDown(int keycode) {
		if ( keycode == KeyCodes.KEY_ALT )
			return;

		switch( keycode )
		{
			case KeyCodes.KEY_W: 
				this.moveState.forward = true; 
				break;
			case KeyCodes.KEY_S: 
				this.moveState.back = true; 
				break;

			case KeyCodes.KEY_A: 
				this.moveState.left = true; 
				break;
			case KeyCodes.KEY_D: 
				this.moveState.right = true; 
				break;

			case KeyCodes.KEY_R: 
				this.moveState.up = true; 
				break;
			case KeyCodes.KEY_F: 
				this.moveState.down = true; 
				break;

			case KeyCodes.KEY_UP: 
				this.moveState.pitchUp = true; 
				break;
			case KeyCodes.KEY_DOWN: 
				this.moveState.pitchDown = 1; 
				break;

			case KeyCodes.KEY_LEFT: 
				this.moveState.yawLeft = 1; 
				break;
			case KeyCodes.KEY_RIGHT: 
				this.moveState.yawRight = true; 
				break;

			case KeyCodes.KEY_Q: 
				this.moveState.rollLeft = true; 
				break;
			case KeyCodes.KEY_E: 
				this.moveState.rollRight = true; 
				break;
			default:
				break;
		}

		this.updateMovementVector();
		this.updateRotationVector();
		
	}

	@Override
	public void onTouchUp(int screenX, int screenY, int pointer, int button) {

		if ( this.isDragToLook ) 
		{
			this.mouseStatus --;

			this.moveState.yawLeft = this.moveState.pitchDown = 0;
		} 
		else 
		{
			if ( button == Input.Buttons.LEFT )
			{
				this.moveState.forward = false;
			} else if( button == Input.Buttons.RIGHT )
			{
				this.moveState.forward = false;
			}
		}

		this.updateRotationVector();	
	}

	@Override
	public void onTouchDown(int screenX, int screenY, int pointer, int button) {

		if ( this.isDragToLook ) 
		{
			this.mouseStatus ++;
		} 
		else 
		{
			if( button == Input.Buttons.LEFT )
			{
				this.moveState.forward = true;
			} else if( button == Input.Buttons.RIGHT )
			{
				this.moveState.forward = false;
			}
		}
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		if (!this.isDragToLook || this.mouseStatus > 0) {
			this.moveState.yawLeft = -(screenX - viewHalfX) / viewHalfX;
			this.moveState.pitchDown = (screenY - viewHalfY) / viewHalfY;

			this.updateRotationVector();
		}
	}
	
	private void updateMovementVector()
	{
		int forward = ( this.moveState.forward || ( this.isAutoForward && !this.moveState.back ) ) ? 1 : 0;

		this.moveVector.setX( - (this.moveState.left ? 1D : 0D)    + (this.moveState.right ? 1D : 0D) );
		this.moveVector.setY( - (this.moveState.down ? 1D : 0D)    + (this.moveState.up ? 1D : 0D) );
		this.moveVector.setZ( - (double)forward + (this.moveState.back ? 1D : 0D) );
	}

	private void updateRotationVector()
	{
		this.rotationVector.setX( - this.moveState.pitchDown + (this.moveState.pitchUp ? 1 : 0) );
		this.rotationVector.setY( - (this.moveState.yawRight ? 1 : 0)  + this.moveState.yawLeft );
		this.rotationVector.setZ( - (this.moveState.rollRight ? 1D : 0D) + (this.moveState.rollLeft ? 1D : 0D) );
	}
}
