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
 * Camera with orthographic projection
 * <pre>
 * {@code
 * OrthographicCamera camera = new OrthographicCamera( width / - 2, width / 2, height / 2, height / - 2, 1, 1000 ); 
 * getScene().add( camera );
 * }
 * </pre>
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

	/**
	 * Orthographic Camera constructor. It uses the following defaults:<br>
	 * <ul>
	 * <li>near - 0.1</li>
	 * <li>far - 2000</li>
	 * </ul>
	 *  
	 * @param left   Camera frustum left plane.
	 * @param right  Camera frustum right plane.
	 * @param top    Camera frustum top plane.
	 * @param bottom Camera frustum bottom plane.
	 */
	public OrthographicCamera(double left, double right, double top, double bottom) 
	{
		this(left, right, top, bottom, 0.1, 2000);
	}

	/**
	 * Orthographic Camera constructor
	 * 
	 * @param left   Camera frustum left plane.
	 * @param right  Camera frustum right plane.
	 * @param top    Camera frustum top plane.
	 * @param bottom Camera frustum bottom plane.
	 * @param near   Camera frustum near plane
	 * @param far    Camera frustum far plane.
	 */
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

	/**
	 * Gets Camera frustum left plane.
	 */
	public double getLeft()
	{
		return left;
	}

	/**
	 * Sets Camera frustum left plane.
	 */
	public void setLeft(double left)
	{
		this.left = left;
	}

	/**
	 * Gets Camera frustum right plane.
	 */
	public double getRight()
	{
		return right;
	}

	/**
	 * Sets Camera frustum right plane.
	 */
	public void setRight(double right)
	{
		this.right = right;
	}

	/**
	 * Gets Camera frustum top plane.
	 */
	public double getTop()
	{
		return top;
	}

	/**
	 * Sets Camera frustum top plane.
	 */
	public void setTop(double top)
	{
		this.top = top;
	}

	/**
	 * Gets Camera frustum bottom plane.
	 */
	public double getBottom()
	{
		return bottom;
	}

	/**
	 * Sets Camera frustum bottom plane.
	 */
	public void setBottom(double bottom)
	{
		this.bottom = bottom;
	}

	/**
	 * Gets Camera frustum near plane.
	 */
	public double getNear()
	{
		return near;
	}

	/**
	 * Sets Camera frustum near plane.
	 */
	public void setNear(double near)
	{
		this.near = near;
	}

	/**
	 * Gets Camera frustum far plane.
	 */
	public double getFar()
	{
		return far;
	}

	/**
	 * Sets Camera frustum far plane.
	 */
	public void setFar(double far)
	{
		this.far = far;
	}
	
	/**
	 * Updates the camera projection matrix.
	 * <p> 
	 * Must be called after change of parameters.
	 */
	public void updateProjectionMatrix()
	{
		this.projectionMatrix.makeOrthographic( getLeft(), getRight(), getTop(), getBottom(), getNear(), getFar() );
	}
}
