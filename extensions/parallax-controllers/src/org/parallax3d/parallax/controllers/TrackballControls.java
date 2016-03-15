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
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;

/**
 * The control implements Trackball Controls.
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 */
public final class TrackballControls extends Controls
		implements TouchMoveHandler, TouchDownHandler, TouchUpHandler, KeyDownHandler, KeyUpHandler
{

	private enum STATE
	{
		NONE, ROTATE, ZOOM, PAN
	};

	private boolean			isEnabled				= true;

	private double			rotateSpeed				= 1.0;
	private double			zoomSpeed				= 1.2;
	private double			panSpeed				= 0.3;

	private boolean			isRotate				= true;
	private boolean			isZoom					= true;
	private boolean			isPan					= true;

	private boolean			isStaticMoving			= false;
	private double			dynamicDampingFactor	= 0.2;

	private double			minDistance				= 0.0;
	private double			maxDistance				= Double.MAX_VALUE;

	private int				keyRotate				= KeyCodes.KEY_A;	 // A
	private int				keyZoom					= KeyCodes.KEY_S;	 // S
	private int				keyPan					= KeyCodes.KEY_D;	 // D

	private final double	radius;

	private Vector3			target;

	private final Vector3	lastPosition;

	private boolean			isKeyPressed			= false;
	private STATE			state					= STATE.NONE;

	private final Vector3	eye;

	private Vector3			rotateStart;
	private Vector3			rotateEnd;

	private Vector2			zoomStart;
	private Vector2			zoomEnd;

	private Vector2			panStart;
	private Vector2			panEnd;

	public TrackballControls ( final Object3D object, final RenderingContext context )
	{
		super( object, context );

		this.radius = ( context.getWidth() + context.getHeight() ) / 4.0;

		this.target = new Vector3();
		this.lastPosition = new Vector3();
		this.eye = new Vector3();
		this.rotateStart = new Vector3();
		this.rotateEnd = new Vector3();
		this.zoomStart = new Vector2();
		this.zoomEnd = new Vector2();
		this.panStart = new Vector2();
		this.panEnd = new Vector2();

		context.getInput().addInputHandler( this );
	}

	public boolean isEnabled ()
	{
		return this.isEnabled;
	}

	/**
	 * Enable/Disable this control. Default true.
	 */
	public void setEnabled ( final boolean isEnabled )
	{
		this.isEnabled = isEnabled;
	}

	/**
	 * Sets rotation speed. Default 1.0
	 */
	public void setRotateSpeed ( final double rotateSpeed )
	{
		this.rotateSpeed = rotateSpeed;
	}

	/**
	 * Sets zoom speed. Default 1.2
	 */
	public void setZoomSpeed ( final double zoomSpeed )
	{
		this.zoomSpeed = zoomSpeed;
	}

	/**
	 * Sets pan speed. Default 0.3
	 */
	public void setPanSpeed ( final double panSpeed )
	{
		this.panSpeed = panSpeed;
	}

	/**
	 * Enable/Disable rotation. Default true.
	 */
	public void setRotate ( final boolean isRotate )
	{
		this.isRotate = isRotate;
	}

	/**
	 * Enable/Disable zoom. Default true.
	 * 
	 * @param isZoom
	 */
	public void setZoom ( final boolean isZoom )
	{
		this.isZoom = isZoom;
	}

	/**
	 * Enable/Disable pan. Default true.
	 * 
	 * @param isPan
	 */
	public void setPan ( final boolean isPan )
	{
		this.isPan = isPan;
	}

	/**
	 * Enable/Disable static moving. Default true.
	 */
	public void setStaticMoving ( final boolean isStaticMoving )
	{
		this.isStaticMoving = isStaticMoving;
	}

	/**
	 * Sets dynamic damping factor. Default 0.2
	 */
	public void setDynamicDampingFactor ( final double dynamicDampingFactor )
	{
		this.dynamicDampingFactor = dynamicDampingFactor;
	}

	/**
	 * Sets minimum distance to the object. Default 0
	 */
	public void setMinDistance ( final double minDistance )
	{
		this.minDistance = minDistance;
	}

	/**
	 * Sets maximum distance to the object. Default Infinity.
	 */
	public void setMaxDistance ( final double maxDistance )
	{
		this.maxDistance = maxDistance;
	}

	/**
	 * Sets key to rotate. Default [A]
	 */
	public void setKeyRotate ( final int keyRotate )
	{
		this.keyRotate = keyRotate;
	}

	/**
	 * Sets key to zoom. Default [S]
	 */
	public void setKeyZoom ( final int keyZoom )
	{
		this.keyZoom = keyZoom;
	}

	/**
	 * sets key to pan. Default [D]
	 */
	public void setKeyPan ( final int keyPan )
	{
		this.keyPan = keyPan;
	}

	public Vector3 getTarget ()
	{
		return this.target;
	}

	public void setTarget ( final Vector3 target )
	{
		this.target = target;
	}

	/**
	 * The method must be called in the
	 * {@link org.parallax3d.parallax.Animation#onUpdate(RenderingContext)}}
	 * onUpdate method.
	 */
	public void update ()
	{
		this.eye.copy( this.getObject().getPosition() ).sub( this.target );

		if ( this.isRotate )
		{
			this.rotateCamera();
		}

		if ( this.isZoom )
		{
			this.zoomCamera();
		}

		if ( this.isPan )
		{
			this.panCamera();
		}

		this.getObject().getPosition().add( this.target, this.eye );

		this.checkDistances();

		this.getObject().lookAt( this.target );

		if ( this.lastPosition.distanceTo( this.getObject().getPosition() ) > 0 )
		{
			this.lastPosition.copy( this.getObject().getPosition() );
		}
	}

	@Override
	public void onKeyUp ( final int keycode )
	{

		if ( !this.isEnabled )
		{
			return;
		}

		this.state = STATE.NONE;
	}

	@Override
	public void onKeyDown ( final int keycode )
	{

		if ( !this.isEnabled )
		{
			return;
		}

		if ( this.state != STATE.NONE )
		{
			return;
		}
		else if ( ( keycode == this.keyRotate ) && this.isRotate )
		{
			this.state = STATE.ROTATE;
		}
		else if ( ( keycode == this.keyZoom ) && this.isZoom )
		{
			this.state = STATE.ZOOM;
		}
		else if ( ( keycode == this.keyPan ) && this.isPan )
		{
			this.state = STATE.PAN;
		}

		if ( this.state != STATE.NONE )
		{
			this.isKeyPressed = true;
		}
	}

	@Override
	public void onTouchUp ( final int screenX, final int screenY, final int pointer, final int button )
	{
		if ( !this.isEnabled )
		{
			return;
		}

		this.state = STATE.NONE;
	}

	@Override
	public void onTouchDown ( final int screenX, final int screenY, final int pointer, final int button )
	{
		if ( !this.isEnabled )
		{
			return;
		}

		if ( this.state == STATE.NONE )
		{
			if ( ( button == Input.Buttons.LEFT ) && this.isRotate )
			{
				this.rotateStart = this.rotateEnd = this.getMouseProjectionOnBall( screenX, screenY );
				this.state = STATE.ROTATE;
			}
			else if ( ( button == Input.Buttons.MIDDLE ) && this.isZoom )
			{
				this.zoomStart = this.zoomEnd = this.getMouseOnScreen( screenX, screenY );
				this.state = STATE.ZOOM;
			}
			else if ( ( button == Input.Buttons.RIGHT ) && this.isPan )
			{
				this.panStart = this.panEnd = this.getMouseOnScreen( screenX, screenY );
				this.state = STATE.PAN;
			}
		}
	}

	@Override
	public void onTouchMove ( final int screenX, final int screenY, final int pointer )
	{
		if ( !this.isEnabled )
		{
			return;
		}

		if ( this.isKeyPressed )
		{
			this.rotateStart = this.rotateEnd = this.getMouseProjectionOnBall( screenX, screenY );
			this.zoomStart = this.zoomEnd = this.getMouseOnScreen( screenX, screenY );
			this.panStart = this.panEnd = this.getMouseOnScreen( screenX, screenY );

			this.isKeyPressed = false;
		}

		if ( this.state == STATE.NONE )
		{
			return;
		}
		else if ( ( this.state == STATE.ROTATE ) && this.isRotate )
		{
			this.rotateEnd = this.getMouseProjectionOnBall( screenX, screenY );
		}
		else if ( ( this.state == STATE.ZOOM ) && this.isZoom )
		{
			this.zoomEnd = this.getMouseOnScreen( screenX, screenY );
		}
		else if ( ( this.state == STATE.PAN ) && this.isPan )
		{
			this.panEnd = this.getMouseOnScreen( screenX, screenY );
		}
	}

	private Vector2 getMouseOnScreen ( final int clientX, final int clientY )
	{
		return new Vector2( ( clientX / this.radius ) * 0.5, ( clientY / this.radius ) * 0.5 );
	}

	private Vector3 getMouseProjectionOnBall ( final int clientX, final int clientY )
	{
		final Vector3 mouseOnBall = new Vector3( ( clientX - ( this.getContext().getWidth() * 0.5 ) ) / this.radius,
				( ( this.getContext().getHeight() * 0.5 ) - clientY ) / this.radius, 0.0 );

		final double length = mouseOnBall.length();

		if ( length > 1.0 )
		{
			mouseOnBall.normalize();
		}
		else
		{
			mouseOnBall.setZ( Math.sqrt( 1.0 - ( length * length ) ) );
		}

		this.eye.copy( this.getObject().getPosition() ).sub( this.target );

		final Vector3 projection = this.getObject().getUp().clone().setLength( mouseOnBall.getY() );
		projection.add( this.getObject().getUp().clone().cross( this.eye ).setLength( mouseOnBall.getX() ) );
		projection.add( this.eye.setLength( mouseOnBall.getZ() ) );

		return projection;
	}

	private void checkDistances ()
	{
		if ( this.isZoom || this.isPan )
		{
			if ( this.getObject().getPosition().lengthSq() > ( this.maxDistance * this.maxDistance ) )
			{
				this.getObject().getPosition().setLength( this.maxDistance );
			}

			if ( this.eye.lengthSq() < ( this.minDistance * this.minDistance ) )
			{
				this.getObject().getPosition().add( this.target, this.eye.setLength( this.minDistance ) );
			}
		}
	}

	private void panCamera ()
	{

		final Vector2 mouseChange = this.panEnd.clone().sub( this.panStart );

		if ( mouseChange.lengthSq() > 0 )
		{
			mouseChange.multiply( this.eye.length() * this.panSpeed );

			final Vector3 pan = this.eye.clone().cross( this.getObject().getUp() ).setLength( mouseChange.getX() );
			pan.add( this.getObject().getUp().clone().setLength( mouseChange.getY() ) );

			this.getObject().getPosition().add( pan );
			this.target.add( pan );

			if ( this.isStaticMoving )
			{
				this.panStart = this.panEnd;
			}
			else
			{
				this.panStart
						.add( mouseChange.sub( this.panEnd, this.panStart ).multiply( this.dynamicDampingFactor ) );
			}

		}
	}

	private void zoomCamera ()
	{
		final double factor = 1.0 + ( ( this.zoomEnd.getY() - this.zoomStart.getY() ) * this.zoomSpeed );

		if ( ( factor != 1.0 ) && ( factor > 0.0 ) )
		{
			this.eye.multiply( factor );

			if ( this.isStaticMoving )
			{
				this.zoomStart = this.zoomEnd;
			}
			else
			{
				this.zoomStart.addY( ( this.zoomEnd.getY() - this.zoomStart.getY() ) * this.dynamicDampingFactor );
			}
		}
	}

	private void rotateCamera ()
	{
		double angle = Math
				.acos( this.rotateStart.dot( this.rotateEnd ) / this.rotateStart.length() / this.rotateEnd.length() );

		if ( angle > 0 )
		{
			final Vector3 axis = ( new Vector3() ).cross( this.rotateStart, this.rotateEnd ).normalize();
			final Quaternion quaternion = new Quaternion();

			angle *= this.rotateSpeed;

			quaternion.setFromAxisAngle( axis, -angle );

			quaternion.multiplyVector3( this.eye );
			quaternion.multiplyVector3( this.getObject().getUp() );

			quaternion.multiplyVector3( this.rotateEnd );

			if ( this.isStaticMoving )
			{
				this.rotateStart = this.rotateEnd;
			}
			else
			{
				quaternion.setFromAxisAngle( axis, angle * ( this.dynamicDampingFactor - 1.0 ) );
				quaternion.multiplyVector3( this.rotateStart );
			}
		}
	}
}
