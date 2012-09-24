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

package thothbot.parallax.core.shared.core;

import thothbot.parallax.core.shared.cameras.Camera;

/**
 * Projects points between spaces.
 * <p>
 * Based on three.js code
 * 
 * @author thothbot
 *
 */
public class Projector
{
	private Matrix4 _projScreenMatrix;
			
	public Projector()
	{
		this._projScreenMatrix = new Matrix4();
	}
	
	public Vector3 unprojectVector ( Vector3 vector, Camera camera ) 
	{
		camera.getProjectionMatrixInverse().getInverse( camera.getProjectionMatrix() );

		this._projScreenMatrix.multiply( camera.getMatrixWorld(), camera.getProjectionMatrixInverse() );
		this._projScreenMatrix.multiplyVector3( vector );

		return vector;
	}
	
	/**
	 * Translates a 2D point from NDC (Normalized Device Coordinates) 
	 * to a Ray that can be used for picking.
	 * <p> 
	 * NDC range from [-1..1] in x (left to right) and [1.0 .. -1.0] in y (top to bottom).
	 */
	public Ray pickingRay( Vector3 vector, Camera camera ) 
	{
		// set two vectors with opposing z values
		vector.setZ( -1.0 );
		Vector3 end = new Vector3( vector.getX(), vector.getY(), 1.0 );

		this.unprojectVector( vector, camera );
		this.unprojectVector( end, camera );

		// find direction from vector to end
		end.sub( vector ).normalize();

		return new Ray( vector, end );
	}
}
