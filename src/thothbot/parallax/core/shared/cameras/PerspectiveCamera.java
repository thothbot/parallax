/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.core.shared.cameras;

import thothbot.parallax.core.shared.Log;

/**
 * Implementation of Perspective Camera.
 * 
 * @author thothbot
 *
 */
public class PerspectiveCamera extends Camera
{
	protected double fieldOfView;
	protected double aspectRatio;
	protected double near;
	protected double far;
	
	protected int fullWidth;
	protected int fullHeight;
	protected double x;
	protected double y;
	protected int width;
	protected int height;

	public PerspectiveCamera() 
	{
		this(50, 1, 0.1, 2000);
	}

	public PerspectiveCamera(double fieldOfView, double aspectRatio, double near, double far) 
	{
		super();
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;

		updateProjectionMatrix();
	}

	public double getFieldOfView()
	{
		return fieldOfView;
	}

	public void setFieldOfView(double fov)
	{
		this.fieldOfView = fov;
	}

	public double getAspectRation()
	{
		return aspectRatio;
	}

	public void setAspectRatio(double aspect)
	{
		this.aspectRatio = aspect;
		this.updateProjectionMatrix();
	}

	public double getNear()
	{
		return near;
	}

	public void setNear(double near)
	{
		this.near = near;
	}

	public double getFar()
	{
		return far;
	}

	public void setFar(double far)
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

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
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
		this.fieldOfView = 2.0 * Math.atan( frameHeight / ( focalLength * 2.0 ) ) * ( 180.0 / Math.PI );
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
	public void setViewOffset( int fullWidth, int fullHeight, double x, double y, int width, int height ) 
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
			double aspect = this.fullWidth / (double)this.fullHeight;
			double top = Math.tan( this.fieldOfView * Math.PI / 360.0 ) * this.near;
			double bottom = -top;
			double left = aspect * bottom;
			double right = aspect * top;
			double width = Math.abs( right - left );
			double height = Math.abs( top - bottom );

			this.projectionMatrix.makeFrustum
			(
				left + this.x * width / this.fullWidth * 1.0,
				left + ( this.x + this.width ) * width / this.fullWidth * 1.0,
				top - ( this.y + this.height ) * height / this.fullHeight * 1.0,
				top - this.y * height / this.fullHeight * 1.0,
				getNear(),
				getFar()
			);
		}
		else 
		{
			this.projectionMatrix.makePerspective( getFieldOfView(), getAspectRation(), getNear(), getFar() );
		}
Log.error("====" + this.projectionMatrix);
	}
}
