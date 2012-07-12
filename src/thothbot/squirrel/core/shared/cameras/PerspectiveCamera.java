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

package thothbot.squirrel.core.shared.cameras;

/**
 * Implementation of Perspective Camera.
 * 
 * @author thothbot
 *
 */
public class PerspectiveCamera extends Camera
{
	protected float fieldOfView;
	protected float aspectRatio;
	protected float near;
	protected float far;
	
	protected int fullWidth;
	protected int fullHeight;
	protected float x;
	protected float y;
	protected int width;
	protected int height;

	public PerspectiveCamera() 
	{
		this(50f, 1f, 0.1f, 2000f);
	}

	public PerspectiveCamera(float fieldOfView, float aspectRatio, float near, float far) 
	{
		super();
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;

		updateProjectionMatrix();
	}

	public float getFieldOfView()
	{
		return fieldOfView;
	}

	public void setFieldOfView(float fov)
	{
		this.fieldOfView = fov;
	}

	public float getAspectRation()
	{
		return aspectRatio;
	}

	public void setAspectRatio(float aspect)
	{
		this.aspectRatio = aspect;
		this.updateProjectionMatrix();
	}

	public float getNear()
	{
		return near;
	}

	public void setNear(float near)
	{
		this.near = near;
	}

	public float getFar()
	{
		return far;
	}

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
	 * Uses Focal Length (in mm) to estimate and set FOV 35mm (fullframe) camera
	 * is used if frame size is not specified; Formula based on
	 * http://www.bobatkins.com/photography/technical/field_of_view.html
	 */

	public void setLens(int focalLength)
	{
		setLens(focalLength, 24);
	}

	public void setLens(int focalLength, int frameHeight)
	{
		this.fieldOfView = (float) (2.0f * Math.atan( frameHeight / ( focalLength * 2.0f ) ) * ( 180.0f / Math.PI ));
		this.updateProjectionMatrix();
	}
	
	/**
	 * Sets an offset in a larger frustum. This is useful for multi-window or
	 * multi-monitor/multi-machine setups.
	 *
	 * For example, if you have 3x2 monitors and each monitor is 1920x1080 and
	 * the monitors are in grid like this
	 *
	 *<pre>
	 *   +---+---+---+
	 *   | A | B | C |
	 *   +---+---+---+
	 *   | D | E | F |
	 *   +---+---+---+
	 *</pre>
	 * then for each monitor you would call it like this
	 *
	 *<pre>
	 *   var w = 1920;
	 *   var h = 1080;
	 *   var fullWidth = w * 3;
	 *   var fullHeight = h * 2;
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
	 *</pre>
	 *
	 *   Note there is no reason monitors have to be the same size or in a grid.
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
	
	public void updateProjectionMatrix() 
	{
		if ( this.fullWidth != 0) 
		{
			float aspect = this.fullWidth / (float)this.fullHeight;
			float top = (float) (Math.tan( this.fieldOfView * Math.PI / 360.0 ) * this.near);
			float bottom = -top;
			float left = aspect * bottom;
			float right = aspect * top;
			float width = Math.abs( right - left );
			float height = Math.abs( top - bottom );

			this.projectionMatrix.makeFrustum
			(
				left + this.x * width / (float)this.fullWidth,
				left + ( this.x + this.width ) * width / (float)this.fullWidth,
				top - ( this.y + this.height ) * height / (float)this.fullHeight,
				top - this.y * height / (float)this.fullHeight,
				getNear(),
				getFar()
			);
		}
		else 
		{
			this.projectionMatrix.makePerspective( getFieldOfView(), getAspectRation(), getNear(), getFar() );
		}

	}
}
