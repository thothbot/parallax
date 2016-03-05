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

package org.parallax3d.parallax.graphics.cameras;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.ViewportResizeBus;
import org.parallax3d.parallax.system.ViewportResizeListener;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Quaternion;

/**
 * Abstract base class for cameras.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Camera")
public class Camera extends Object3D implements ViewportResizeListener
{
	protected Matrix4 matrixWorldInverse;
	protected Matrix4 projectionMatrix;

	/**
	 * This constructor sets the following properties to the correct type: matrixWorldInverse and projectionMatrix.
	 */
	public Camera()
	{
		super();

		ViewportResizeBus.addViewportResizeListener(this);

		this.matrixWorldInverse = new Matrix4();
		this.projectionMatrix = new Matrix4();
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

		return optionalTarget.set( 0, 0, - 1 ).apply(quaternion);

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

		this.quaternion.setFromRotationMatrix(m1);
	}

	public Camera clone() {
		return clone(new Camera());
	}

	public Camera clone ( Camera camera ) {

		super.clone(camera);

		camera.matrixWorldInverse.copy(this.matrixWorldInverse);
		camera.projectionMatrix.copy(this.projectionMatrix);

		return camera;
	};

	@Override
	public void onViewportResize(int newWidth, int newHeight) {

	}

	@Override
	public void finalize() {
		ViewportResizeBus.removeViewportResizeListener(this);
		try
		{
			super.finalize();
		}
		catch (Throwable throwable)
		{
			Log.error("Exception in Camera.finalize:", throwable);
		}
	}
}
