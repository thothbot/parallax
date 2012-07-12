/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.controls;

import thothbot.squirrel.core.shared.core.Mathematics;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.objects.Object3D;

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

public class FirstPersonControl extends Control 
		implements MouseMoveHandler, MouseDownHandler, MouseUpHandler, 
		KeyDownHandler, KeyUpHandler, ContextMenuHandler
{
	private Vector3f target;
	private float movementSpeed = 1.0f;
	private float lookSpeed = 0.005f;

	private boolean noFly = false;
	private boolean lookVertical = true;
	private boolean autoForward = false;

	private boolean activeLook = true;

	private boolean heightSpeed = false;
	private float heightCoef = 1.0f;
	private float heightMin = 0.0f;
	// TODO: Check
	private float heightMax = 0.0f;

	private boolean constrainVertical = false;
	private float verticalMin = 0f;
	private float verticalMax = (float)Math.PI;

	private float autoSpeedFactor = 0.0f;

	private int mouseX = 0;
	private int mouseY = 0;

	private float lat = 0f;
	private float lon = 0f;
	private float phi = 0f;
	private float theta = 0f;

	private boolean moveForward = false;
	private boolean moveBackward = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean freeze = false;

	private boolean mouseDragOn = false;
	
	private int viewHalfX;
	private int viewHalfY;
	
	public FirstPersonControl(Object3D object, Widget widget)
	{
		super(object, widget);
		
		this.viewHalfX = widget.getOffsetWidth() / 2;
		this.viewHalfY = widget.getOffsetHeight() / 2;
		
		if(getWidget().getClass() != RootPanel.class)
			getWidget().getElement().setAttribute( "tabindex", "-1" );
		
		this.target = new Vector3f();
		
		getWidget().addDomHandler(this, ContextMenuEvent.getType());

		getWidget().addDomHandler(this, MouseMoveEvent.getType());
		getWidget().addDomHandler(this, MouseDownEvent.getType());
		getWidget().addDomHandler(this, MouseUpEvent.getType());
		RootPanel.get().addDomHandler(this, KeyDownEvent.getType());
		RootPanel.get().addDomHandler(this, KeyUpEvent.getType());		
	}
	
	public void setMovementSpeed(float movementSpeed)
	{
		this.movementSpeed = movementSpeed;
	}
	
	public void setLookSpeed(float lookSpeed)
	{
		this.lookSpeed = lookSpeed;
	}
		
	public void update( float delta ) 
	{
		float actualMoveSpeed = 0;
		float actualLookSpeed;
		
		if ( this.freeze ) 
		{	
			return;	
		} 
		else 
		{
			if ( this.heightSpeed ) 
			{
				float y = Mathematics.clamp( getObject().getPosition().getY(), this.heightMin, this.heightMax );
				float heightDelta = y - this.heightMin;

				this.autoSpeedFactor = delta * ( heightDelta * this.heightCoef );

			} 
			else 
			{
				this.autoSpeedFactor = 0.0f;
			}

			actualMoveSpeed = delta * this.movementSpeed;

			if ( this.moveForward || ( this.autoForward && !this.moveBackward ) ) 
				getObject().translateZ( - ( actualMoveSpeed + this.autoSpeedFactor ) );

			if ( this.moveBackward ) 
				getObject().translateZ( actualMoveSpeed );

			if ( this.moveLeft ) 
				getObject().translateX( - actualMoveSpeed );
			
			if ( this.moveRight ) 
				getObject().translateX( actualMoveSpeed );

			if ( this.moveUp ) 
				getObject().translateY( actualMoveSpeed );
			
			if ( this.moveDown ) 
				getObject().translateY( - actualMoveSpeed );

			actualLookSpeed = delta * this.lookSpeed;

			if ( !this.activeLook )
				actualLookSpeed = 0.0f;

			this.lon += this.mouseX * actualLookSpeed;
			if( this.lookVertical ) 
				this.lat -= this.mouseY * actualLookSpeed; // * this.invertVertical?-1:1;

			this.lat = Math.max( - 85f, Math.min( 85f, this.lat ) );
			this.phi = (float) (( 90.0 - this.lat ) * Math.PI / 180.0);
			this.theta = (float) (this.lon * Math.PI / 180.0);

			Vector3f targetPosition = this.target;
			Vector3f position = getObject().getPosition();

			targetPosition.setX((float) (position.getX() + 100.0 * Math.sin( this.phi ) * Math.cos( this.theta )));
			targetPosition.setY((float) (position.getY() + 100.0 * Math.cos( this.phi )));
			targetPosition.setZ((float) (position.getZ() + 100.0 * Math.sin( this.phi ) * Math.sin( this.theta )));

		}

		float verticalLookRatio = 1.0f;

		if ( this.constrainVertical )
			verticalLookRatio = (float) (Math.PI / ( this.verticalMax - this.verticalMin ));

		this.lon += this.mouseX * actualLookSpeed;
		if( this.lookVertical ) this.lat -= this.mouseY * actualLookSpeed * verticalLookRatio;

		this.lat = Math.max( - 85f, Math.min( 85f, this.lat ) );
		this.phi = (float) (( 90.0 - this.lat ) * Math.PI / 180.0);

		this.theta = (float) (this.lon * Math.PI / 180.0);

		if ( this.constrainVertical )
			this.phi = Mathematics.mapLinear( this.phi, 0.0f, (float)Math.PI, this.verticalMin, this.verticalMax );

		Vector3f targetPosition = this.target;
		Vector3f position = getObject().getPosition();

		targetPosition.setX((float) (position.getX() + 100.0 * Math.sin( this.phi ) * Math.cos( this.theta )));
		targetPosition.setY((float) (position.getY() + 100.0 * Math.cos( this.phi )));
		targetPosition.setZ((float) (position.getZ() + 100.0 * Math.sin( this.phi ) * Math.sin( this.theta )));
		
		this.getObject().lookAt( targetPosition );
	}
	
	public void onMouseMove(MouseMoveEvent event)
	{
		if ( getWidget().getClass() == RootPanel.class ) 
		{
			this.mouseX = event.getX() - this.viewHalfX;
			this.mouseY = event.getY() - this.viewHalfY;
		} 
		else 
		{
			this.mouseX = event.getX() - getWidget().getAbsoluteLeft() - this.viewHalfX;
			this.mouseY = event.getY() - getWidget().getAbsoluteTop() - this.viewHalfY;
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event)
	{
//		if (getWidget().getClass() != RootPanel.class)
//			getWidget().setFocus(true);

		event.preventDefault();
		event.stopPropagation();

		if ( this.activeLook ) 
		{
			switch ( event.getNativeButton() ) 
			{
			case NativeEvent.BUTTON_LEFT: this.moveForward = true; break;
			case NativeEvent.BUTTON_RIGHT: this.moveBackward = true; break;
			}
		}

		this.mouseDragOn = true;
	}

	@Override
	public void onMouseUp(MouseUpEvent event)
	{
		event.preventDefault();
		event.stopPropagation();

		if ( this.activeLook ) 
		{
			switch ( event.getNativeButton() ) 
			{
			case NativeEvent.BUTTON_LEFT: this.moveForward = false; break;
			case NativeEvent.BUTTON_RIGHT: this.moveBackward = false; break;
			}
		}

		this.mouseDragOn = false;
	}

	@Override
	public void onKeyDown(KeyDownEvent event)
	{
		switch( event.getNativeEvent().getKeyCode() ) 
		{
		case 38: /*up*/
		case 87: /*W*/ this.moveForward = true; break;

		case 37: /*left*/
		case 65: /*A*/ this.moveLeft = true; break;

		case 40: /*down*/
		case 83: /*S*/ this.moveBackward = true; break;

		case 39: /*right*/
		case 68: /*D*/ this.moveRight = true; break;

		case 82: /*R*/ this.moveUp = true; break;
		case 70: /*F*/ this.moveDown = true; break;

		case 81: /*Q*/ this.freeze = !this.freeze; break;
		}
	}

	@Override
	public void onKeyUp(KeyUpEvent event)
	{
		switch( event.getNativeEvent().getKeyCode() ) 
		{
		case 38: /*up*/
		case 87: /*W*/ this.moveForward = false; break;

		case 37: /*left*/
		case 65: /*A*/ this.moveLeft = false; break;

		case 40: /*down*/
		case 83: /*S*/ this.moveBackward = false; break;

		case 39: /*right*/
		case 68: /*D*/ this.moveRight = false; break;

		case 82: /*R*/ this.moveUp = false; break;
		case 70: /*F*/ this.moveDown = false; break;
		}
	}

	@Override
	public void onContextMenu(ContextMenuEvent event)
	{
		event.preventDefault();
	}	
}
