/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.objects.DimensionalObject;
import thothbot.parallax.core.shared.objects.GeometryObject;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Particle;

public class Ray 
{
	private Vector3 origin;
	private Vector3 direction;
	private double near;
	private double far;

	private double precision = 0.0001;

	class Intersect implements Comparable<Intersect>
	{
		public double distance;
		public Vector3 point;
		public Face3 face;
		public int faceIndex;
		public DimensionalObject object;

		@Override
		public int compareTo(Intersect o) 
		{
			double result = o.distance - this.distance; 
			return (result == 0) ? 0 
					: (result > 0) ? 1 : -1;
		}
	}

	public Ray()
	{
		this(new Vector3(), new Vector3());
	}

	public Ray( Vector3 origin, Vector3 direction )
	{
		this(origin, direction, 0, Double.MAX_VALUE);
	}

	public Ray( Vector3 origin, Vector3 direction, double near, double far ) 
	{
		this.origin = origin;
		this.direction = direction;
		this.near = near;
		this.far = far;

		//

		//		var a = new THREE.Vector3();
		//		var b = new THREE.Vector3();
		//		var c = new THREE.Vector3();
		//		var d = new THREE.Vector3();
		//
		//		var originCopy = new THREE.Vector3();
		//		var directionCopy = new THREE.Vector3();
		//
		//		var vector = new THREE.Vector3();
		//		var normal = new THREE.Vector3();
		//		var intersectPoint = new THREE.Vector3();
	}

	//

	private double distanceFromIntersection( Vector3 origin, Vector3 direction, Vector3 position ) 
	{
		Vector3 v0 = new Vector3(), v1 = new Vector3(), v2 = new Vector3();
		v0.sub( position, origin );
		double dot = v0.dot( direction );

		Vector3 intersect = v1.add( origin, v2.copy( direction ).multiply( dot ) );
		double distance = position.distanceTo( intersect );

		return distance;
	}

	/**
	 * http://www.blackpawn.com/texts/pointinpoly/default.html
	 * @param p
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public boolean pointInFace3( Vector3 p, Vector3 a, Vector3 b, Vector3 c ) 
	{
		Vector3 v0 = new Vector3(), v1 = new Vector3(), v2 = new Vector3();
		v0.sub( c, a );
		v1.sub( b, a );
		v2.sub( p, a );

		double dot00 = v0.dot( v0 );
		double dot01 = v0.dot( v1 );
		double dot02 = v0.dot( v2 );
		double dot11 = v1.dot( v1 );
		double dot12 = v1.dot( v2 );

		double invDenom = 1.0 / ( dot00 * dot11 - dot01 * dot01 );
		double u = ( dot11 * dot02 - dot01 * dot12 ) * invDenom;
		double v = ( dot00 * dot12 - dot01 * dot02 ) * invDenom;

		return ( u >= 0 ) && ( v >= 0 ) && ( u + v < 1 );
	}

	//
	public void setPrecision( double value ) 
	{
		this.precision = value;
	}

	public List<Ray.Intersect> intersectObject( DimensionalObject object, boolean recursive ) 
	{
		//			var intersect, 
		List<Ray.Intersect> intersects = new ArrayList<Ray.Intersect>();

		if ( recursive == true ) 
		{
			for ( int i = 0, l = object.getChildren().size(); i < l; i ++ ) 
			{
				intersects.addAll( intersectObject( object.getChildren().get( i ), recursive ) );
			}
		}

		if ( object instanceof Particle ) 
		{
			double distance = distanceFromIntersection( this.origin, this.direction, object.getMatrixWorld().getPosition() );
			if ( distance > object.getScale().getX() ) 
			{
				//					return [];
				return null;
			}

			Ray.Intersect intersect = new Ray.Intersect();
			intersect.distance = distance;
			intersect.point = object.getPosition();
			intersect.object = object;

			intersects.add( intersect );
		} 
		else if ( object instanceof Mesh ) 
		{
			// Checking boundingSphere
			Vector3 scale = Frustum.__v1.set( 
					object.getMatrixWorld().getColumnX().length(),
					object.getMatrixWorld().getColumnY().length(),
					object.getMatrixWorld().getColumnZ().length() );

			double scaledRadius = ((Mesh)object).getGeometry().getBoundingSphere().radius 
					* Math.max( scale.x, Math.max( scale.y, scale.z ) );

			// Checking distance to ray
			double distance = distanceFromIntersection( this.origin, this.direction, object.getMatrixWorld().getPosition() );
			if ( distance > scaledRadius) 
			{
				return intersects;
			}

			// Checking faces
			Geometry geometry = ((Mesh) object).getGeometry();
			List<Vector3> vertices = geometry.getVertices();
			//				objMatrix,
			//				isFaceMaterial, material, side;

			List<Material> geometryMaterials = ((Mesh) object).getGeometry().getMaterials();
			boolean isFaceMaterial = ((Mesh) object).getMaterial() instanceof MeshFaceMaterial;
			Material.SIDE side = ((Mesh) object).getMaterial().getSides();

			object.getMatrixRotationWorld().extractRotation( object.getMatrixWorld() );

			for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) 
			{
				Face3 face = geometry.getFaces().get( f );

				Material material = ( isFaceMaterial == true ) ? geometryMaterials.get( face.getMaterialIndex() ) : ((Mesh) object).getMaterial();

				if ( material == null ) 
					continue;

				side = material.getSides();

				Vector3 originCopy = this.origin.clone();
				Vector3 directionCopy = this.direction.clone();

				Matrix4 objMatrix = object.getMatrixWorld();

				// determine if ray intersects the plane of the face
				// note: this works regardless of the direction of the face normal

				Vector4 vector = objMatrix.multiplyVector3( vector.copy( face.centroid ) ).sub( originCopy );
				Vector4 normal = object.getMatrixRotationWorld().multiplyVector3( normal.copy( face.normal ) );
				double dot = directionCopy.dot( normal );

				// bail if ray and plane are parallel

				if ( Math.abs( dot ) < this.precision ) 
					continue;

				// calc distance to plane

				double scalar = normal.dot( vector ) / dot;

				// if negative distance, then plane is behind ray

				if ( scalar < 0 ) continue;

				if ( side == Material.SIDE.DOUBLE || ( side == Material.SIDE.FRONT ? dot < 0 : dot > 0 ) ) 
				{
					Vector3 intersectPoint = new Vector3();
					intersectPoint.add( originCopy, directionCopy.multiply( scalar ) );

					distance = originCopy.distanceTo( intersectPoint );

					if ( distance < this.near ) continue;
					if ( distance > this.far ) continue;

					if ( face.getClass() == Face3.class ) 
					{
						Vector3 a = objMatrix.multiplyVector3( a.copy( vertices.get( face.getA() ) ) );
						Vector3 b = objMatrix.multiplyVector3( b.copy( vertices.get( face.getB() ) ) );
						Vector3 c = objMatrix.multiplyVector3( c.copy( vertices.get( face.getC() ) ) );

						if ( pointInFace3( intersectPoint, a, b, c ) ) 
						{

							Ray.Intersect intersect = new Ray.Intersect();
							intersect.distance = distance;
							intersect.point = intersectPoint.clone();
							intersect.face = face;
							intersect.faceIndex = f;
							intersect.object = object;

							intersects.add( intersect );
						}
					} 
					else if ( face.getClass() == Face4.class ) 
					{
						Vector3 a = objMatrix.multiplyVector3( a.copy( vertices.get( face.getA() ) ) );
						Vector3 b = objMatrix.multiplyVector3( b.copy( vertices.get( face.getB() ) ) );
						Vector3 c = objMatrix.multiplyVector3( c.copy( vertices.get( face.getC() ) ) );
						Vector3 d = objMatrix.multiplyVector3( d.copy( vertices.get( ((Face4)face).getD() ) ) );

						if ( pointInFace3( intersectPoint, a, b, d ) || pointInFace3( intersectPoint, b, c, d ) ) 
						{
							Ray.Intersect intersect = new Ray.Intersect();
							intersect.distance = distance;
							intersect.point = intersectPoint.clone();
							intersect.face = face;
							intersect.faceIndex = f;
							intersect.object = object;

							intersects.add( intersect );
						}
					}
				}
			}
		}

		Collections.sort(intersects);

		return intersects;
	}

	public List<Ray.Intersect> intersectObjects(List<DimensionalObject> objects, boolean recursive ) 
	{
		List<Ray.Intersect> intersects = new ArrayList<Ray.Intersect>();

		for ( int i = 0, l = objects.size(); i < l; i ++ )
		{
			intersects.addAll( intersectObject( objects.get( i ), recursive ) );
		}

		Collections.sort( intersects );

		return intersects;
	}
}
