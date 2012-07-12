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
 * Implementation of Orthographic Camera.
 * 
 * @author thothbot
 *
 */
public class OrthographicCamera extends Camera
{

	protected float left;
	protected float right;
	protected float top;
	protected float bottom;

	protected float near;
	protected float far;

	public OrthographicCamera(float left, float right, float top, float bottom) 
	{
		this(left, right, top, bottom, 0.1f, 2000f);
	}

	public OrthographicCamera(float left, float right, float top, float bottom, float near,	float far) 
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

	public float getLeft()
	{
		return left;
	}

	public void setLeft(float left)
	{
		this.left = left;
	}

	public float getRight()
	{
		return right;
	}

	public void setRight(float right)
	{
		this.right = right;
	}

	public float getTop()
	{
		return top;
	}

	public void setTop(float top)
	{
		this.top = top;
	}

	public float getBottom()
	{
		return bottom;
	}

	public void setBottom(float bottom)
	{
		this.bottom = bottom;
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
	
	public void updateProjectionMatrix()
	{
		this.projectionMatrix.makeOrthographic( getLeft(), getRight(), getTop(), getBottom(), getNear(), getFar() );
	}
}
