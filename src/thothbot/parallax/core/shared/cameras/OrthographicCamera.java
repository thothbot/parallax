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

/**
 * Implementation of Orthographic Camera.
 * 
 * @author thothbot
 *
 */
public class OrthographicCamera extends Camera
{

	protected double left;
	protected double right;
	protected double top;
	protected double bottom;

	protected double near;
	protected double far;

	public OrthographicCamera(double left, double right, double top, double bottom) 
	{
		this(left, right, top, bottom, 0.1, 2000);
	}

	public OrthographicCamera(double left, double right, double top, double bottom, double near, double far) 
	{
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;

		this.near = near;
		this.far = far;
		
		updateProjectionMatrix();
	}

	public double getLeft()
	{
		return left;
	}

	public void setLeft(double left)
	{
		this.left = left;
	}

	public double getRight()
	{
		return right;
	}

	public void setRight(double right)
	{
		this.right = right;
	}

	public double getTop()
	{
		return top;
	}

	public void setTop(double top)
	{
		this.top = top;
	}

	public double getBottom()
	{
		return bottom;
	}

	public void setBottom(double bottom)
	{
		this.bottom = bottom;
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
	
	public void updateProjectionMatrix()
	{
		this.projectionMatrix.makeOrthographic( getLeft(), getRight(), getTop(), getBottom(), getNear(), getFar() );
	}
}
