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

package thothbot.parallax.core.shared.curves;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.core.Vector3f;

/*
 * For computing of Frenet frames, exposing the tangents, normals and binormals the spline
 */
public class FrenetFrames
{
	private static double epsilon = 0.0001f;

	// expose internals
	private List<Vector3f> tangents;
	private List<Vector3f> normals;
	private List<Vector3f> binormals;

	private Curve path;

	public FrenetFrames(Curve path, int segments, boolean closed)
	{
		this.path = path;

		this.tangents = new ArrayList<Vector3f>();
		this.normals = new ArrayList<Vector3f>();
		this.binormals =new ArrayList<Vector3f>();

		Matrix4f mat = new Matrix4f();

		int numpoints = segments + 1;

		// compute the tangent vectors for each segment on the path
		for ( int i = 0; i < numpoints; i++ ) 
		{
			double u = i / ( numpoints - 1.0 );

			Vector3f vec = (Vector3f) path.getTangentAt( u ); 
			tangents.add(vec.normalize());
		}


		initialNormal3();

		Vector3f vec = new Vector3f();

		// compute the slowly-varying normal and binormal vectors for each segment on the path
		for ( int i = 1; i < numpoints; i++ ) 
		{
			normals.add( i, normals.get( i - 1 ).clone());

			binormals.add( i, binormals.get( i - 1 ).clone());
			vec.cross( tangents.get( i - 1 ), tangents.get( i ) );

			if ( vec.length() > epsilon ) 
			{
				vec.normalize();
				double aCos =  tangents.get( i - 1 ).dot( tangents.get( i ) );
				double theta = Math.acos( aCos > 1 ? 1.0 : aCos );
			
				mat.makeRotationAxis( vec, theta ).multiplyVector3( normals.get( i ) );
			}

			binormals.get( i ).cross( tangents.get( i ), normals.get( i ) );

		}

		// if the curve is closed, postprocess the vectors so the first and last normal vectors are the same
		if ( closed ) 
		{
			double theta = Math.acos( normals.get( 0 ).dot( normals.get( numpoints - 1 ) ) );
			theta /= ( numpoints - 1.0 );

			if ( tangents.get( 0 ).dot( vec.cross( normals.get( 0 ), normals.get( numpoints - 1 ) ) ) > 0 )
				theta = -theta;

			for ( int i = 1; i < numpoints; i++ ) 
			{
				// twist a little...
				mat.makeRotationAxis( tangents.get( i ), theta * i ).multiplyVector3( normals.get( i ) );
				binormals.get( i ).cross( tangents.get( i ), normals.get( i ) );
			}
		}
	}

	public List<Vector3f> getTangents()
	{
		return tangents;
	}

	public List<Vector3f> getNormals()
	{
		return normals;
	}

	public List<Vector3f> getBinormals()
	{
		return binormals;
	}

	private void initialNormal1()
	{
		initialNormal1(new Vector3f( 0, 0, 1 ));
	}

	private void initialNormal1(Vector3f lastBinormal) 
	{
		// fixed start binormal. Has dangers of 0 vectors
		normals.add( 0, new Vector3f());
		binormals.add( 0, new Vector3f());

		normals.get( 0 ).cross( lastBinormal, tangents.get( 0 ) ).normalize();
		binormals.get( 0 ).cross( tangents.get( 0 ), normals.get( 0 ) ).normalize();
	}

	private void initialNormal2() 
	{
		// This uses the Frenet-Serret formula for deriving binormal
		Vector3f t2 = (Vector3f) path.getTangentAt( epsilon );

		normals.add( 0, new Vector3f().sub( t2, tangents.get( 0 ) ).normalize() );
		binormals.add( 0, new Vector3f().cross( tangents.get( 0 ), normals.get( 0 ) ) );

		normals.get( 0 ).cross( binormals.get( 0 ), tangents.get( 0 ) ).normalize(); // last binormal x tangent
		binormals.get( 0 ).cross( tangents.get( 0 ), normals.get( 0 ) ).normalize();

	}

	/*
	 * select an initial normal vector perpenicular to the first tangent vector,
	 * and in the direction of the smallest tangent xyz component
	 */
	private void initialNormal3() 
	{
		normals.add( 0, new Vector3f());
		binormals.add( 0, new Vector3f());
		double smallest = Double.MAX_VALUE;

		double tx = Math.abs( tangents.get( 0 ).getX() );
		double ty = Math.abs( tangents.get( 0 ).getY() );
		double tz = Math.abs( tangents.get( 0 ).getZ() );

		Vector3f normal = new Vector3f();
		if ( tx <= smallest ) 
		{
			smallest = tx;
			normal.set( 1, 0, 0 );
		}

		if ( ty <= smallest ) {
			smallest = ty;
			normal.set( 0, 1, 0 );
		}

		if ( tz <= smallest ) {
			normal.set( 0, 0, 1 );
		}

		Vector3f vec = new Vector3f();
		vec.cross( tangents.get( 0 ), normal ).normalize();

		normals.get( 0 ).cross( tangents.get( 0 ), vec );
		binormals.get( 0 ).cross( tangents.get( 0 ), normals.get( 0 ) );
	}
}
