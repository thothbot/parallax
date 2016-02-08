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

package org.parallax3d.parallax.graphics.lights;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.math.Vector3;

public final class VirtualLight extends DirectionalLight 
{

	private Camera originalCamera;

	private List<Vector3> pointsWorld;
	private List<Vector3> pointsFrustum;

	public VirtualLight(int hex)
	{
		super(hex);

		pointsWorld = new ArrayList<Vector3>();
		pointsFrustum = new ArrayList<Vector3>();

		for ( int i = 0; i < 8; i ++ )
		{
			getPointsWorld().set( i, new Vector3() );
			getPointsFrustum().set( i, new Vector3() );
		}
	}

	public Camera getOriginalCamera() {
		return originalCamera;
	}

	public VirtualLight setOriginalCamera(Camera originalCamera) {
		this.originalCamera = originalCamera;
		return this;
	}

	public List<Vector3> getPointsWorld() {
		return pointsWorld;
	}

	public VirtualLight setPointsWorld(List<Vector3> pointsWorld) {
		this.pointsWorld = pointsWorld;
		return this;
	}

	public List<Vector3> getPointsFrustum() {
		return pointsFrustum;
	}

	public VirtualLight setPointsFrustum(List<Vector3> pointsFrustum) {
		this.pointsFrustum = pointsFrustum;
		return this;
	}

}
