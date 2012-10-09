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

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.objects.Object3D;

/**
 * Abstract base class for cameras.
 * 
 * @author thothbot
 *
 */
public class Camera extends Object3D
{
	protected Matrix4 matrixWorldInverse;
	protected Matrix4 projectionMatrix;
	protected Matrix4 projectionMatrixInverse;

	public Float32Array _viewMatrixArray;
	public Float32Array _projectionMatrixArray;
	
	public Camera() 
	{
		super();
		this.matrixWorldInverse = new Matrix4();
		this.projectionMatrix = new Matrix4();
		this.projectionMatrixInverse = new Matrix4();
		
		this._viewMatrixArray = Float32Array.create( 16 );
		this._projectionMatrixArray = Float32Array.create( 16 );
	}

	public Matrix4 getMatrixWorldInverse()
	{
		return this.matrixWorldInverse;
	}

	public void setMatrixWorldInverse(Matrix4 matrixWorldInverse)
	{
		this.matrixWorldInverse = matrixWorldInverse;
	}

	public Matrix4 getProjectionMatrix()
	{
		return this.projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4 projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4 getProjectionMatrixInverse()
	{
		return this.projectionMatrixInverse;
	}

	public void setProjectionMatrixInverse(Matrix4 projectionMatrix)
	{
		this.projectionMatrixInverse = projectionMatrix;
	}

	@Override
	public void lookAt(Vector3 vector)
	{
		this.matrix.lookAt(this.position, vector, this.up);

		if (this.rotationAutoUpdate)
			this.rotation.setEulerFromRotationMatrix( this.matrix, this.eulerOrder );
	}
}
