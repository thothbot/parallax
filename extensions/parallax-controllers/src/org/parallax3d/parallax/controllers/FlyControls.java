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

package org.parallax3d.parallax.controllers;

import org.parallax3d.parallax.Input;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.input.KeyCodes;
import org.parallax3d.parallax.input.KeyDownHandler;
import org.parallax3d.parallax.input.KeyUpHandler;
import org.parallax3d.parallax.input.TouchDownHandler;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.input.TouchUpHandler;
import org.parallax3d.parallax.math.Quaternion;
import org.parallax3d.parallax.math.Vector3;

public final class FlyControls extends Controls
		implements TouchMoveHandler, TouchDownHandler, TouchUpHandler, KeyDownHandler, KeyUpHandler
{

	private double	movementSpeed	= 1.0;
	private double	rollSpeed		= 0.005;

	private boolean	isDragToLook	= false;
	private boolean	isAutoForward	= false;

	private class MoveState
	{
		public boolean	up;
		public boolean	down;
		public boolean	left;
		public boolean	right;
		public boolean	forward;
		public boolean	back;
		public boolean	pitchUp;
		public double	pitchDown;
		public double	yawLeft;
		public boolean	yawRight;
		public boolean	rollLeft;
		public boolean	rollRight;
	}

	// internals
	private int					mouseStatus;

	private final MoveState		moveState;
	private final Quaternion	tmpQuaternion;
	private final Vector3		moveVector;
	private final Vector3		rotationVector;

	private final double		viewHalfX;
	private final double		viewHalfY;

	public FlyControls ( final Object3D object, final RenderingContext context )
	{
		super( object, context );

		// if(getWidget().getClass() != RootPanel.class)
		// getWidget().getElement().setAttribute( "tabindex", "-1" );

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

	public void setMovementSpeed ( final double speed )
	{
		this.movementSpeed = speed;
	}

	public void setRollSpeed ( final double speed )
	{
		this.rollSpeed = speed;
	}

	public void setDragToLook ( final boolean isDragToLook )
	{
		this.isDragToLook = isDragToLook;
	}

	public void setAutoForward ( final boolean isAutoForward )
	{
		this.isAutoForward = isAutoForward;
	}

	public void update ( double delta )
	{
		delta *= 0.0001;
		final double moveMult = delta * this.movementSpeed;
		final double rotMult = delta * this.rollSpeed;

		this.getObject().translateX( this.moveVector.getX() * moveMult );
		this.getObject().translateY( this.moveVector.getY() * moveMult );
		this.getObject().translateZ( this.moveVector.getZ() * moveMult );

		this.tmpQuaternion.set( this.rotationVector.getX() * rotMult, this.rotationVector.getY() * rotMult,
				this.rotationVector.getZ() * rotMult, 1.0 ).normalize();

		this.getObject().getQuaternion().multiply( this.tmpQuaternion );

		// expose the rotation vector for convenience
		this.getObject().getRotation().setFromQuaternion( this.getObject().getQuaternion(),
				this.getObject().getRotation().getOrder() );
	}

	@Override
	public void onKeyUp ( final int keycode )
	{
		switch ( keycode )
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

		}

		this.updateMovementVector();
		this.updateRotationVector();

	}

	@Override
	public void onKeyDown ( final int keycode )
	{
		if ( keycode == KeyCodes.KEY_ALT )
		{
			return;
		}

		switch ( keycode )
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
		}

		this.updateMovementVector();
		this.updateRotationVector();

	}

	@Override
	public void onTouchUp ( final int screenX, final int screenY, final int pointer, final int button )
	{

		if ( this.isDragToLook )
		{
			this.mouseStatus--;

			this.moveState.yawLeft = this.moveState.pitchDown = 0;
		}
		else
		{
			switch ( button )
			{
				case Input.Buttons.LEFT:
					this.moveState.forward = false;
					break;
				case Input.Buttons.RIGHT:
					this.moveState.forward = false;
					break;
			}
		}

		this.updateRotationVector();
	}

	@Override
	public void onTouchDown ( final int screenX, final int screenY, final int pointer, final int button )
	{

		if ( this.isDragToLook )
		{
			this.mouseStatus++;
		}
		else
		{
			switch ( button )
			{
				case Input.Buttons.LEFT:
					this.moveState.forward = true;
					break;
				case Input.Buttons.RIGHT:
					this.moveState.forward = false;
					break;
			}
		}
	}

	@Override
	public void onTouchMove ( final int screenX, final int screenY, final int pointer )
	{
		if ( !this.isDragToLook || ( this.mouseStatus > 0 ) )
		{
			this.moveState.yawLeft = - ( screenX - this.viewHalfX ) / this.viewHalfX;
			this.moveState.pitchDown = ( screenY - this.viewHalfY ) / this.viewHalfY;

			this.updateRotationVector();
		}
	}

	private void updateMovementVector ()
	{
		final int forward = ( this.moveState.forward || ( this.isAutoForward && !this.moveState.back ) ) ? 1 : 0;

		this.moveVector.setX( - ( ( this.moveState.left ) ? 1 : 0 ) + ( ( this.moveState.right ) ? 1 : 0 ) );
		this.moveVector.setY( - ( ( this.moveState.down ) ? 1 : 0 ) + ( ( this.moveState.up ) ? 1 : 0 ) );
		this.moveVector.setZ( -forward + ( ( this.moveState.back ) ? 1 : 0 ) );
	}

	private void updateRotationVector ()
	{
		this.rotationVector.setX( -this.moveState.pitchDown + ( ( this.moveState.pitchUp ) ? 1 : 0 ) );
		this.rotationVector.setY( - ( ( this.moveState.yawRight ) ? 1 : 0 ) + this.moveState.yawLeft );
		this.rotationVector
				.setZ( - ( ( this.moveState.rollRight ) ? 1 : 0 ) + ( ( this.moveState.rollLeft ) ? 1 : 0 ) );
	}
}
