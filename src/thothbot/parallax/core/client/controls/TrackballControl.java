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

package thothbot.parallax.core.client.controls;

import thothbot.parallax.core.client.AnimatedScene;
import thothbot.parallax.core.shared.core.Quaternion;
import thothbot.parallax.core.shared.core.Vector2f;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.objects.Object3D;

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

/**
 * The control implements Trackball Control.
 * There is disabled the context menu for the {@link #getWidget()}.
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class TrackballControl extends Control implements MouseMoveHandler, MouseDownHandler, MouseUpHandler, 
KeyDownHandler, KeyUpHandler, ContextMenuHandler
{
	private boolean isEnabled = true;
	private float rotateSpeed = 1.0f;
	private float zoomSpeed = 1.2f;
	private float panSpeed = 0.3f;

	private boolean isRotate = true;
	private boolean isZoom = true;
	private boolean isPan = true;

	private boolean isStaticMoving = false;
	private float dynamicDampingFactor = 0.2f;

	private float minDistance = 0f;
	private float maxDistance = Float.MAX_VALUE;
	
	private int keyRotate = 65; // A
	private int keyZoom = 83; // S
	private int keyPan = 68; // D
		
	private float radius;
	
	private enum STATE {
		 NONE,
		 ROTATE, 
		 ZOOM, 
		 PAN
	};

	private Vector3f target;

	private Vector3f lastPosition;

	private boolean isKeyPressed = false;
	private STATE state = STATE.NONE;

	private Vector3f eye;

	private Vector3f rotateStart;
	private Vector3f rotateEnd;

	private Vector2f zoomStart;
	private Vector2f zoomEnd;

	private Vector2f panStart;
	private Vector2f panEnd;
	
	public TrackballControl(Object3D object, Widget widget) 
	{
		super(object, widget);
		
		this.radius =  ( widget.getOffsetWidth() + widget.getOffsetHeight() ) / 4.0f;
		
		if(getWidget().getClass() != RootPanel.class)
			getWidget().getElement().setAttribute( "tabindex", "-1" );
		
		this.target = new Vector3f();
		lastPosition = new Vector3f();
		eye = new Vector3f();
		rotateStart = new Vector3f();
		rotateEnd = new Vector3f();
		zoomStart = new Vector2f();
		zoomEnd = new Vector2f();
		panStart = new Vector2f();
		panEnd = new Vector2f();
		
		getWidget().addDomHandler(this, ContextMenuEvent.getType());

		getWidget().addDomHandler(this, MouseMoveEvent.getType());
		getWidget().addDomHandler(this, MouseDownEvent.getType());
		getWidget().addDomHandler(this, MouseUpEvent.getType());
		RootPanel.get().addDomHandler(this, KeyDownEvent.getType());
		RootPanel.get().addDomHandler(this, KeyUpEvent.getType());	
	}
	
	/**
	 * Enable/Disable this control. Default true.
	 */
	public void isEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * Sets rotation speed. Default 1.0
	 */
	public void setRotateSpeed(float rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}

	/**
	 * Sets zoom speed. Default 1.2
	 */
	public void setZoomSpeed(float zoomSpeed) {
		this.zoomSpeed = zoomSpeed;
	}

	/**
	 * Sets pan speed. Default 0.3 
	 */
	public void setPanSpeed(float panSpeed) {
		this.panSpeed = panSpeed;
	}

	/**
	 * Enable/Disable rotation. Default true.
	 */
	public void setRotate(boolean isRotate) {
		this.isRotate = isRotate;
	}

	/**
	 * Enable/Disable zoom. Default true.
	 * @param isZoom
	 */
	public void setZoom(boolean isZoom) {
		this.isZoom = isZoom;
	}

	/**
	 * Enable/Disable static moving. Default true.
	 */
	public void setStaticMoving(boolean isStaticMoving) {
		this.isStaticMoving = isStaticMoving;
	}

	/**
	 * Sets dynamic damping factor. Default 0.2
	 */
	public void setDynamicDampingFactor(float dynamicDampingFactor) {
		this.dynamicDampingFactor = dynamicDampingFactor;
	}

	/**
	 * Sets minimum distance to the object. Default 0
	 */
	public void setMinDistance(float minDistance) {
		this.minDistance = minDistance;
	}

	/**
	 * Sets maximum distance to the object. Default Infinity.
	 */
	public void setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
	}

	/**
	 * Sets key to rotate. Default [A]
	 */
	public void setKeyRotate(int keyRotate) {
		this.keyRotate = keyRotate;
	}

	/**
	 * Sets key to zoom. Default [S]
	 */
	public void setKeyZoom(int keyZoom) {
		this.keyZoom = keyZoom;
	}

	/**
	 * sets key to pan. Default [D]
	 */
	public void setKeyPan(int keyPan) {
		this.keyPan = keyPan;
	}

	/**
	 * The method must be called in the {@link AnimatedScene}} onUpdate method.
	 */
	public void update() 
	{
		eye.copy( this.getObject().getPosition() ).sub( this.target );

		if ( this.isRotate )
			rotateCamera();
		
		if ( this.isZoom )
			zoomCamera();

		if ( this.isPan )
			panCamera();

		getObject().getPosition().add( this.target, this.eye );

		checkDistances();

		getObject().lookAt( this.target );

		if ( lastPosition.distanceTo( getObject().getPosition() ) > 0 ) 
		{	
			lastPosition.copy( getObject().getPosition() );
		}
	}

	@Override
	public void onContextMenu(ContextMenuEvent event) 
	{
		event.preventDefault();
	}

	@Override
	public void onKeyUp(KeyUpEvent event) 
	{
		if ( ! this.isEnabled ) return;

		state = STATE.NONE;
	}

	@Override
	public void onKeyDown(KeyDownEvent event) 
	{
		if ( ! this.isEnabled ) return;

		if ( state != STATE.NONE ) 
		{
			return;
		} 
		else if ( event.getNativeEvent().getKeyCode() == this.keyRotate && this.isRotate ) 
		{
			state = STATE.ROTATE;
		} 
		else if ( event.getNativeEvent().getKeyCode() == this.keyZoom && this.isZoom ) 
		{
			state = STATE.ZOOM;
		} 
		else if ( event.getNativeEvent().getKeyCode() == this.keyPan && this.isPan ) 
		{
			state = STATE.PAN;
		}

		if ( state != STATE.NONE )
			this.isKeyPressed = true;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) 
	{
		if ( ! this.isEnabled ) return;

		event.preventDefault();
		event.stopPropagation();

		this.state = STATE.NONE;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) 
	{
		if ( ! this.isEnabled ) return;

		event.preventDefault();
		event.stopPropagation();

		if ( state == STATE.NONE ) 
		{
			if ( event.getNativeButton() == NativeEvent.BUTTON_LEFT && this.isRotate ) 
			{
				rotateStart = rotateEnd = getMouseProjectionOnBall( event.getX(), event.getY() );
				state = STATE.ROTATE;
			} 
			else if ( event.getNativeButton() == NativeEvent.BUTTON_MIDDLE && this.isZoom ) 
			{
				zoomStart = zoomEnd = getMouseOnScreen( event.getX(), event.getY() );
				state = STATE.ZOOM;
			} 
			else if ( event.getNativeButton() == NativeEvent.BUTTON_RIGHT && this.isPan ) 
			{
				panStart = panEnd = getMouseOnScreen( event.getX(), event.getY() );
			}
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) 
	{
		if ( ! this.isEnabled ) return;

		if ( this.isKeyPressed ) 
		{
			rotateStart = rotateEnd = getMouseProjectionOnBall( event.getX(),  event.getY() );
			zoomStart = zoomEnd = getMouseOnScreen( event.getX(),  event.getY() );
			panStart = panEnd = getMouseOnScreen( event.getX(),  event.getY() );

			isKeyPressed = false;
		}

		if ( this.state == STATE.NONE ) 
		{
			return;
		} 
		else if ( this.state == STATE.ROTATE && this.isRotate ) 
		{
			rotateEnd = getMouseProjectionOnBall( event.getX(), event.getY() );
		} 
		else if ( this.state == STATE.ZOOM && this.isZoom ) 
		{
			zoomEnd = getMouseOnScreen( event.getX(), event.getY() );
		} 
		else if ( this.state == STATE.PAN && this.isPan ) 
		{
			panEnd = getMouseOnScreen( event.getX(), event.getY() );
		}
	}
	
	private Vector2f getMouseOnScreen( int clientX, int clientY ) 
	{
		return new Vector2f(
			( clientX - getWidget().getAbsoluteLeft() ) / this.radius * 0.5f,
			( clientY - getWidget().getAbsoluteTop() ) / this.radius * 0.5f
		);
	}

	private Vector3f getMouseProjectionOnBall ( int clientX, int clientY ) 
	{
		Vector3f mouseOnBall = new Vector3f(
			( clientX - getWidget().getOffsetWidth() * 0.5f - getWidget().getAbsoluteLeft() ) / this.radius,
			( getWidget().getOffsetHeight() * 0.5f + getWidget().getAbsoluteTop() - clientY ) / this.radius,
			0.0f
		);

		float length = mouseOnBall.length();

		if ( length > 1.0f )
			mouseOnBall.normalize();

		else
			mouseOnBall.setZ( (float) Math.sqrt( 1.0 - length * length ) );

		eye.copy( getObject().getPosition() ).sub( this.target );

		Vector3f projection = getObject().getUp().clone().setLength( mouseOnBall.getY() );
		projection.add( getObject().getUp().clone().cross( eye ).setLength( mouseOnBall.getX() ) );
		projection.add( eye.setLength( mouseOnBall.getZ() ) );

		return projection;
	}
	
	private void checkDistances()
	{
		if ( this.isZoom || this.isPan ) 
		{
			if ( getObject().getPosition().lengthSq() > this.maxDistance * this.maxDistance ) 
				getObject().getPosition().setLength( this.maxDistance );

			if ( this.eye.lengthSq() < this.minDistance * this.minDistance ) 
				getObject().getPosition().add( this.target, eye.setLength( this.minDistance ) );
		}
	}

	private void panCamera() 
	{

		Vector2f mouseChange = panEnd.clone().sub( panStart );

		if ( mouseChange.lengthSq() > 0 ) 
		{
			mouseChange.multiply( eye.length() * this.panSpeed );

			Vector3f pan = eye.clone().cross( getObject().getUp() ).setLength( mouseChange.getX() );
			pan.add( getObject().getUp().clone().setLength( mouseChange.getY() ) );

			getObject().getPosition().add( pan );
			this.target.add( pan );

			if ( this.isStaticMoving )
				panStart = panEnd;

			else
				panStart.add( mouseChange.sub( panEnd, panStart ).multiply( this.dynamicDampingFactor ) );

		}
	}
	
	private void zoomCamera() 
	{
		float factor = 1.0f + ( zoomEnd.getY() - zoomStart.getY() ) * this.zoomSpeed;

		if ( factor != 1.0f && factor > 0.0f ) 
		{
			eye.multiply( factor );

			if ( this.isStaticMoving )
				zoomStart = zoomEnd;

			else
				zoomStart.addY( ( zoomEnd.getY() - zoomStart.getY() ) * this.dynamicDampingFactor );
		}
	}
	
	private void rotateCamera() 
	{
		float angle = (float) Math.acos( rotateStart.dot( rotateEnd ) / rotateStart.length() / rotateEnd.length() );

		if ( angle > 0 ) 
		{
			Vector3f axis = ( new Vector3f() ).cross( rotateStart, rotateEnd ).normalize();
			Quaternion quaternion = new Quaternion();

			angle *= this.rotateSpeed;

			quaternion.setFromAxisAngle( axis, -angle );

			quaternion.multiply( eye );
			quaternion.multiply( getObject().getUp() );

			quaternion.multiply( rotateEnd );

			if ( this.isStaticMoving ) 
			{
				rotateStart = rotateEnd;
			} 
			else 
			{
				quaternion.setFromAxisAngle( axis, angle * ( this.dynamicDampingFactor - 1.0f ) );
				quaternion.multiply( rotateStart );
			}
		}
	}
}
