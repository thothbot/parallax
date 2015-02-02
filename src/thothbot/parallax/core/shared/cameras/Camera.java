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

package thothbot.parallax.core.shared.cameras;

import thothbot.parallax.core.client.events.HasEventBus;
import thothbot.parallax.core.client.events.ViewportResizeEvent;
import thothbot.parallax.core.client.events.ViewportResizeHandler;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract base class for cameras.
 * 
 * @author thothbot
 *
 */
public class Camera extends Object3D implements HasEventBus, ViewportResizeHandler
{
	protected Matrix4 matrixWorldInverse;
	protected Matrix4 projectionMatrix;
	
	/**
	 * This constructor sets the following properties to the correct type: matrixWorldInverse and projectionMatrix.
	 */
	public Camera() 
	{
		super();

		this.matrixWorldInverse = new Matrix4();
		this.projectionMatrix = new Matrix4();

		addViewportResizeHandler(this);
	}
	
	public HandlerRegistration addViewportResizeHandler(ViewportResizeHandler handler) 
	{
		return EVENT_BUS.addHandler(ViewportResizeEvent.TYPE, handler); 
	}
	
	@Override
	public void onResize(ViewportResizeEvent event) {
		//  Empty for capability
	}

	/**
	 * This is the inverse of matrixWorld. MatrixWorld contains the Matrix which has the world transform of the Camera.
	 * @return
	 */
	public Matrix4 getMatrixWorldInverse()
	{
		return this.matrixWorldInverse;
	}

	public void setMatrixWorldInverse(Matrix4 matrixWorldInverse)
	{
		this.matrixWorldInverse = matrixWorldInverse;
	}

	/**
	 * This is the matrix which contains the projection.
	 * @return
	 */
	public Matrix4 getProjectionMatrix()
	{
		return this.projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4 projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}
	
	@Override
	public Vector3 getWorldDirection() {
		return getWorldDirection(new Vector3());
	}

	@Override
	public Vector3 getWorldDirection(Vector3 optionalTarget) {

		Quaternion quaternion = new Quaternion();

		this.getWorldQuaternion( quaternion );

		return optionalTarget.set( 0, 0, - 1 ).apply( quaternion );

	}
	
	/**
	 * This makes the camera look at the vector position in the global space as long as the parent 
	 * of this camera is the scene or at position (0,0,0).
	 */
	@Override
	public void lookAt(Vector3 vector)
	{
		// This routine does not support cameras with rotated and/or translated parent(s)

		Matrix4 m1 = new Matrix4();

		m1.lookAt( this.position, vector, this.up );

		this.quaternion.setFromRotationMatrix( m1 );
	}
	
	public Camera clone() {
		return clone(new Camera());
	}
	
	public Camera clone ( Camera camera ) {

		super.clone(camera);

		camera.matrixWorldInverse.copy( this.matrixWorldInverse );
		camera.projectionMatrix.copy( this.projectionMatrix );

		return camera;
	};
}
