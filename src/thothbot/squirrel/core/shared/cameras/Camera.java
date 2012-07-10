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

import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.shared.core.Matrix4f;
import thothbot.squirrel.core.shared.core.Object3D;
import thothbot.squirrel.core.shared.core.Vector3f;

public class Camera extends Object3D
{

	protected Matrix4f matrixWorldInverse;
	protected Matrix4f projectionMatrix;
	protected Matrix4f projectionMatrixInverse;

	public Float32Array _viewMatrixArray;
	public Float32Array _projectionMatrixArray;

	public Camera() {
		super();
		this.matrixWorldInverse = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		this.projectionMatrixInverse = new Matrix4f();

	}

	public Matrix4f getMatrixWorldInverse()
	{
		return this.matrixWorldInverse;
	}

	public void setMatrixWorldInverse(Matrix4f matrixWorldInverse)
	{
		this.matrixWorldInverse = matrixWorldInverse;
	}

	public Matrix4f getProjectionMatrix()
	{
		return this.projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4f getProjectionMatrixInverse()
	{
		return this.projectionMatrixInverse;
	}

	public void setProjectionMatrixInverse(Matrix4f projectionMatrix)
	{
		this.projectionMatrixInverse = projectionMatrix;
	}

	// TODO: Add hierarchy support.
	@Override
	public void lookAt(Vector3f vector)
	{
		this.matrix.lookAt(this.position, vector, this.up);

		if (this.rotationAutoUpdate)
			this.rotation.getRotationFromMatrix(this.matrix);
	}
}
