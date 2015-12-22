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

import org.parallax3d.parallax.system.ThreeJsObject;
//import org.parallax3d.parallax.backends.gwt.client.events.ViewportResizeEvent;
import org.parallax3d.parallax.math.Mathematics;

/**
 * Camera with perspective projection.
 * <pre>
 * {@code
 * PerspectiveCamera camera = new PerspectiveCamera( 45, width / height, 1, 1000 );
 * }
 * </pre>
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.PerspectiveCamera")
public class PerspectiveCamera extends Camera implements HasNearFar
{
	public float zoom = 1.0f;
	
	protected float fov;
	protected float aspect;
	protected float near;
	protected float far;
	
	protected int fullWidth;
	protected int fullHeight;
	protected float x;
	protected float y;
	protected int width;
	protected int height;

	/**
	 * Perspective Camera default constructor. It uses the following defaults:<br>
	 * <ul>
	 * <li>fieldOfView - 50</li>
	 * <li>aspectRatio - 1</li>
	 * <li>near - 0.1</li>
	 * <li>far - 2000</li>
	 * </ul>
	 * 
	 */
	public PerspectiveCamera() 
	{
		this(50, 1, 0.1f, 2000);
	}

	/**
	 * Perspective Camera constructor
	 * @param fieldOfView Camera frustum vertical field of view.
	 * @param aspectRatio Camera frustum aspect ratio.
	 * @param near        Camera frustum near plane.
	 * @param far         Camera frustum far plane.
	 */
	public PerspectiveCamera(float fieldOfView, float aspectRatio, float near, float far) 
	{
		super();
		this.fov = fieldOfView;
		this.aspect = aspectRatio;
		this.near = near;
		this.far = far;

		updateProjectionMatrix();
	}
	
//	@Override
//	public void onResize(ViewportResizeEvent event)
//	{
//		setAspect(event.getRenderer().getAbsoluteAspectRation());	
//	}

	/**
	 * Gets Camera frustum vertical field of view.
	 */
	public float getFov()
	{
		return fov;
	}

	/**
	 * Sets Camera frustum vertical field of view.
	 */
	public void setFov(float fov)
	{
		this.fov = fov;
	}

	/**
	 * Gets Camera frustum aspect ratio.
	 */
	public float getAspect()
	{
		return aspect;
	}

	/**
	 * Sets Camera frustum aspect ratio.
	 */
	public void setAspect(float aspect)
	{
		this.aspect = aspect;
		this.updateProjectionMatrix();
	}

	/**
	 * Gets Camera frustum near plane.
	 */
	public float getNear()
	{
		return near;
	}

	/**
	 * Sets Camera frustum near plane.
	 */
	public void setNear(float near)
	{
		this.near = near;
	}

	/**
	 * Gets Camera frustum far plane.
	 */
	public float getFar()
	{
		return far;
	}

	/**
	 * Sets Camera frustum far plane.
	 */
	public void setFar(float far)
	{
		this.far = far;
	}

	public int getFullWidth()
	{
		return fullWidth;
	}

	public void setFullWidth(int fullWidth)
	{
		this.fullWidth = fullWidth;
	}

	public int getFullHeight()
	{
		return fullHeight;
	}

	public void setFullHeight(int fullHeight)
	{
		this.fullHeight = fullHeight;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @see #setLens(int, int)
	 * 
	 * @param focalLength the focal length
	 */
	public void setLens(int focalLength)
	{
		setLens(focalLength, 24);
	}

	/**
	 * Uses Focal Length (in mm) to estimate and set FOV 35mm (fullframe) camera
	 * is used if frame size is not specified.<br>
	 * Formula based on <a href="http://www.bobatkins.com/photography/technical/field_of_view.html">bobatkins.com</a>
	 * 
	 * @param focalLength the focal length
	 * @param frameHeight the frame size
	 */
	public void setLens(int focalLength, int frameHeight)
	{
		this.fov = 2.0f * Mathematics.radToDeg((float)Math.atan(frameHeight / (focalLength * 2.0f)));
		this.updateProjectionMatrix();
	}
	
	/**
	 * Sets an offset in a larger frustum. This is useful for multi-window or
	 * multi-monitor/multi-machine setups.
	 * <p>
	 * For example, if you have 3x2 monitors and each monitor is 1920x1080 and
	 * the monitors are in grid like this
	 *
	 *<pre>
	 *{@code
	 *
	 *   +---+---+---+
	 *   | A | B | C |
	 *   +---+---+---+
	 *   | D | E | F |
	 *   +---+---+---+
	 *}
	 *</pre>
	 * then for each monitor you would call it like this
	 *
	 *<pre>
	 *{@code
	 *
	 *   float w = 1920;
	 *   float h = 1080;
	 *   float fullWidth = w * 3;
	 *   float fullHeight = h * 2;
	 *
	 *   --A--
	 *   camera.setOffset( fullWidth, fullHeight, w * 0, h * 0, w, h );
	 *   --B--
	 *   camera.setOffset( fullWidth, fullHeight, w * 1, h * 0, w, h );
	 *   --C--
	 *   camera.setOffset( fullWidth, fullHeight, w * 2, h * 0, w, h );
	 *   --D--
	 *   camera.setOffset( fullWidth, fullHeight, w * 0, h * 1, w, h );
	 *   --E--
	 *   camera.setOffset( fullWidth, fullHeight, w * 1, h * 1, w, h );
	 *   --F--
	 *   camera.setOffset( fullWidth, fullHeight, w * 2, h * 1, w, h );
	 *}
	 *</pre>
	 *
	 *   Note there is no reason monitors have to be the same size or in a grid.
	 * 
	 * @param fullWidth  the full width of multiview setup
	 * @param fullHeight the full height of multiview setup
	 * @param x          the horizontal offset of subcamera
	 * @param y          the vertical offset of subcamera
	 * @param width      the width of subcamera
	 * @param height     the height of subcamera
	 */
	public void setViewOffset( int fullWidth, int fullHeight, float x, float y, int width, int height ) 
	{
		this.fullWidth = fullWidth;
		this.fullHeight = fullHeight;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.updateProjectionMatrix();
	}
	
	/**
	 * Updates the camera projection matrix.
	 * <p>
	 * Must be called after change of parameters.
	 */
	public void updateProjectionMatrix() 
	{
		float fov = Mathematics.radToDeg( 2 * (float)Math.atan( Math.tan( Mathematics.degToRad( this.fov ) * 0.5f ) / this.zoom ) );

		if ( this.fullWidth > 0 ) {

			float aspect = (float)this.fullWidth / (float)this.fullHeight;
			float top = (float)Math.tan( Mathematics.degToRad( fov * 0.5f ) ) * this.near;
			float bottom = - top;
			float left = aspect * bottom;
			float right = aspect * top;
			float width = Math.abs( right - left );
			float height = Math.abs( top - bottom );

			this.projectionMatrix.makeFrustum(
				left + this.x * width / this.fullWidth,
				left + ( this.x + this.width ) * width / this.fullWidth,
				top - ( this.y + this.height ) * height / this.fullHeight,
				top - this.y * height / this.fullHeight,
				this.near,
				this.far
			);

		} else {

			this.projectionMatrix.makePerspective( fov, this.aspect, this.near, this.far );

		}

	}
	
	public PerspectiveCamera clone () {

		PerspectiveCamera camera = new PerspectiveCamera();
		
		super.clone(camera);

		camera.zoom = this.zoom;

		camera.fov = this.fov;
		camera.aspect = this.aspect;
		camera.near = this.near;
		camera.far = this.far;

		camera.projectionMatrix.copy( this.projectionMatrix );

		return camera;

	}
}
