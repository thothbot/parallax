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

package thothbot.squirrel.core.shared.core;

import thothbot.squirrel.core.shared.cameras.Camera;

public class Projector
{
	private Matrix4f _projScreenMatrix;
			
	public Projector()
	{
		this._projScreenMatrix = new Matrix4f();
	}
	
	public Vector3f unprojectVector ( Vector3f vector, Camera camera ) 
	{
		camera.getProjectionMatrixInverse().getInverse( camera.getProjectionMatrix() );

		this._projScreenMatrix.multiply( camera.getMatrixWorld(), camera.getProjectionMatrixInverse() );
		this._projScreenMatrix.multiplyVector3( vector );

		return vector;
	}
}
