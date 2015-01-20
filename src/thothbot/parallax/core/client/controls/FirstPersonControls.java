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

import thothbot.parallax.core.client.AnimatedScene;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.math.Mathematics;
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

/**
 * The control implements control from a first person.
 * There are disabled the context menu for the {@link #getWidget()} 
 * and implemented the following controls:
 * <p>
 * Mouse move - rotate the {@link #getObject()}.
 * <p>
 * Mouse down - translate the {@link #getObject()} forward.
 * <p>
 * Mouse up - translate the  {@link #getObject()} backward.
 * <p>
 * Keyboard: <br>
 * [W or *up*] - translate the  {@link #getObject()} forward.<br>
 * [S or *down*] - translate the  {@link #getObject()} backward.<br>
 * [A or *left*] - translate the {@link #getObject()} to the left.<br>
 * [D or *right*] - translate the {@link #getObject()} to the right.<br>
 * [R] - translate the {@link #getObject()} up.<br>
 * [F] - translate the {@link #getObject()} down.<br>
 * [Q] - freez the {@link #getObject()}.
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public class FirstPersonControls extends Controls 
		implements MouseMoveHandler, MouseDownHandler, MouseUpHandler, 
		KeyDownHandler, KeyUpHandler, ContextMenuHandler
{
	private Vector3 target;
	private double movementSpeed = 1.0;
	private double lookSpeed = 0.005;

	private boolean lookVertical = true;
	private boolean autoForward = false;

	private boolean activeLook = true;

	private boolean heightSpeed = false;
	private double heightCoef = 1.0;
	private double heightMin = 0.0;
	// TODO: Check
	private double heightMax = 0.0;

	private boolean constrainVertical = false;
	private double verticalMin = 0.0;
	private double verticalMax = Math.PI;

	private double autoSpeedFactor = 0.0;

	private int mouseX = 0;
	private int mouseY = 0;

	private double lat = 0.0;
	private double lon = 0.0;
	private double phi = 0.0;
	private double theta = 0.0;

	private boolean moveForward = false;
	private boolean moveBackward = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean freeze = false;
	
	private int viewHalfX;
	private int viewHalfY;
	
	public FirstPersonControls(Object3D object, Widget widget)
	{
		super(object, widget);
		
		this.viewHalfX = widget.getOffsetWidth() / 2;
		this.viewHalfY = widget.getOffsetHeight() / 2;
		
		if(getWidget().getClass() != RootPanel.class)
			getWidget().getElement().setAttribute( "tabindex", "-1" );
		
		this.target = new Vector3();
		
		getWidget().addDomHandler(this, ContextMenuEvent.getType());

		getWidget().addDomHandler(this, MouseMoveEvent.getType());
		getWidget().addDomHandler(this, MouseDownEvent.getType());
		getWidget().addDomHandler(this, MouseUpEvent.getType());
		RootPanel.get().addDomHandler(this, KeyDownEvent.getType());
		RootPanel.get().addDomHandler(this, KeyUpEvent.getType());		
	}
	
	/**
	 * Sets the movement speed. Default 1.0
	 * 
	 * @param movementSpeed the movement speed.
	 */
	public void setMovementSpeed(double movementSpeed)
	{
		this.movementSpeed = movementSpeed;
	}
	
	/**
	 * Sets look speed. Default 0.005.
	 * 
	 * @param lookSpeed the look speed.
	 */
	public void setLookSpeed(double lookSpeed)
	{
		this.lookSpeed = lookSpeed;
	}
		
	/**
	 * The method must be called in the {@link AnimatedScene}} onUpdate method.
	 * 
	 * @param delta the time in milliseconds needed to render one frame. 
	 */
	public void update( double delta ) 
	{
		double actualMoveSpeed = 0;
		double actualLookSpeed;
		
		if ( this.freeze ) 
		{	
			return;	
		} 
		else 
		{
			if ( this.heightSpeed ) 
			{
				double y = Mathematics.clamp( getObject().getPosition().getY(), this.heightMin, this.heightMax );
				double heightDelta = y - this.heightMin;

				this.autoSpeedFactor = delta * ( heightDelta * this.heightCoef );
			} 
			else 
			{
				this.autoSpeedFactor = 0.0;
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
				actualLookSpeed = 0.0;

			this.lon += this.mouseX * actualLookSpeed;
			if( this.lookVertical ) 
				this.lat -= this.mouseY * actualLookSpeed; // * this.invertVertical?-1:1;

			this.lat = Math.max( - 85.0, Math.min( 85.0, this.lat ) );
			this.phi = ( 90.0 - this.lat ) * Math.PI / 180.0;
			this.theta = this.lon * Math.PI / 180.0;

			Vector3 targetPosition = this.target;
			Vector3 position = getObject().getPosition();

			targetPosition.setX(position.getX() + 100.0 * Math.sin( this.phi ) * Math.cos( this.theta ));
			targetPosition.setY(position.getY() + 100.0 * Math.cos( this.phi ));
			targetPosition.setZ(position.getZ() + 100.0 * Math.sin( this.phi ) * Math.sin( this.theta ));

		}

		double verticalLookRatio = 1.0;

		if ( this.constrainVertical )
			verticalLookRatio = Math.PI / ( this.verticalMax - this.verticalMin );

		this.lon += this.mouseX * actualLookSpeed;
		if( this.lookVertical ) this.lat -= this.mouseY * actualLookSpeed * verticalLookRatio;

		this.lat = Math.max( - 85.0, Math.min( 85.0, this.lat ) );
		this.phi = ( 90.0 - this.lat ) * Math.PI / 180.0;

		this.theta = this.lon * Math.PI / 180.0;

		if ( this.constrainVertical )
			this.phi = Mathematics.mapLinear( this.phi, 0.0, Math.PI, this.verticalMin, this.verticalMax );

		Vector3 targetPosition = this.target;
		Vector3 position = getObject().getPosition();

		targetPosition.setX(position.getX() + 100.0 * Math.sin( this.phi ) * Math.cos( this.theta ));
		targetPosition.setY(position.getY() + 100.0 * Math.cos( this.phi ));
		targetPosition.setZ(position.getZ() + 100.0 * Math.sin( this.phi ) * Math.sin( this.theta ));
		
		this.getObject().lookAt( targetPosition );
	}
	
	@Override
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
