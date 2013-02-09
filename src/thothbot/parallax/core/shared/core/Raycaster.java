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

package thothbot.parallax.core.shared.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Plane;
import thothbot.parallax.core.shared.math.Ray;
import thothbot.parallax.core.shared.math.Sphere;
import thothbot.parallax.core.shared.math.Triangle;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.DimensionalObject;
import thothbot.parallax.core.shared.objects.GeometryObject;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Particle;

public class Raycaster 
{
	private static final double precision = 0.0001;
	
	public static class Intersect implements Comparable
	{
		// this works because the original ray was normalized, and the transformed localRay wasn't
		public double distance;	
		public Vector3 point;
		public Face3 face;
		public int faceIndex;
		public DimensionalObject object;
		
		@Override
		public int compareTo(Object arg0) 
		{
			Intersect b = (Intersect)arg0;
			return (this.distance == b.distance) 
					? 0 : this.distance < b.distance ? -1 : 1;
		}
	}
	
	private Ray ray;
	private double near;
	private double far;
	
	private Sphere sphere = new Sphere();
	private Ray localRay = new Ray();
	private Plane facePlane = new Plane();
	private Vector3 intersectPoint = new Vector3();
	private Matrix4 inverseMatrix = new Matrix4();

	public Raycaster( Vector3 origin, Vector3 direction)
	{
		this(origin, direction, 0, Double.POSITIVE_INFINITY);
	}
	
	public Raycaster( Vector3 origin, Vector3 direction, double near, double far ) 
	{

		this.ray = new Ray( origin, direction );
		
		// normalized ray.direction required for accurate distance calculations
		if( this.ray.getDirection().length() > 0 ) 
		{
			this.ray.getDirection().normalize();
		}

		this.near = near;
		this.far = far;
	}
	
//	var descSort = function ( a, b ) {
//
//		return a.distance - b.distance;
//
//	};

	private void intersectObject( DimensionalObject object, Raycaster raycaster, List<Raycaster.Intersect> intersects ) 
	{
		if ( object instanceof Particle ) 
		{
			double distance = raycaster.ray.distanceToPoint( object.getMatrixWorld().getPosition() );

			if ( distance > object.getScale().getX() ) 
			{
				return ;
			}

			Raycaster.Intersect intersect = new Raycaster.Intersect();
			intersect.distance = distance;
			intersect.point = object.getPosition();
			intersect.object = object;
			intersects.add(intersect);
		} 
		else if ( object instanceof Mesh ) 
		{

			// Checking boundingSphere distance to ray
			sphere.set(
				object.getMatrixWorld().getPosition(),
				((Mesh) object).getGeometry().getBoundingSphere().getRadius() * object.getMatrixWorld().getMaxScaleOnAxis() );

			if ( ! raycaster.ray.isIntersectionSphere( sphere ) ) 
			{
				return ;
			}

			// Checking faces

			Geometry geometry = ((Mesh) object).getGeometry();
			List<Vector3> vertices = geometry.getVertices();

			boolean isFaceMaterial = ((Mesh) object).getMaterial() instanceof MeshFaceMaterial;
			List<Material> objectMaterials = (isFaceMaterial == true)
					? ((MeshFaceMaterial)((Mesh) object).getMaterial()).getMaterials() : null;

			Material.SIDE side = ((Mesh) object).getMaterial().getSides();

			Vector3 a, b, c, d;

			object.getMatrixRotationWorld().extractRotation( object.getMatrixWorld() );

			inverseMatrix.getInverse( object.getMatrixWorld() );

			localRay.copy( raycaster.ray ).transform( inverseMatrix );
	
			for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) 
			{
				Face3 face = geometry.getFaces().get( f );

				Material material = isFaceMaterial == true 
						? objectMaterials.get( face.getMaterialIndex() ) : ((Mesh)object).getMaterial();

				if ( material == null ) continue;
				
				facePlane.setFromNormalAndCoplanarPoint( face.normal, vertices.get(face.getA()) );

				double planeDistance = localRay.distanceToPlane( facePlane );
	
				// bail if raycaster and plane are parallel
				if ( Math.abs( planeDistance ) < Raycaster.precision ) continue;
	
				// if negative distance, then plane is behind raycaster
				if ( planeDistance < 0 ) continue;

				// check if we hit the wrong side of a single sided face
				side = material.getSides();
				if( side != Material.SIDE.DOUBLE ) 
				{
					double planeSign = localRay.getDirection().dot( facePlane.getNormal() );

					if( ! ( (side == Material.SIDE.FRONT) ? planeSign < 0 : planeSign > 0 ) ) continue;
				}

				// this can be done using the planeDistance from localRay because localRay wasn't normalized, but ray was
				if ( planeDistance < raycaster.near || planeDistance > raycaster.far ) continue;
				
				intersectPoint = localRay.at( planeDistance, intersectPoint ); // passing in intersectPoint avoids a copy

				if ( face instanceof Face3 ) 
				{
					a = vertices.get( face.getA() );
					b = vertices.get( face.getB() );
					c = vertices.get( face.getC() );

					if ( ! Triangle.containsPoint( intersectPoint, a, b, c ) ) continue;

				}
				else if ( face instanceof Face4 ) 
				{

					a = vertices.get( face.getA() );
					b = vertices.get( face.getB() );
					c = vertices.get( face.getC() );
					d = vertices.get( ((Face4) face).getD() );

					if ( ( ! Triangle.containsPoint( intersectPoint, a, b, d ) ) &&
						 ( ! Triangle.containsPoint( intersectPoint, b, c, d ) ) ) continue;
				} 
//				else 
//				{
//					// This is added because if we call out of this if/else group when none of the cases
//					//    match it will add a point to the intersection list erroneously.
//					throw Error( "face type not supported" );
//				}

				Raycaster.Intersect intersect = new Raycaster.Intersect();
				intersect.distance = planeDistance;	// this works because the original ray was normalized, and the transformed localRay wasn't
				intersect.point = raycaster.ray.at( planeDistance );
				intersect.face = face;
				intersect.faceIndex = f;
				intersect.object = object;
				intersects.add(intersect);
			}
		}
	}

	private void intersectDescendants( DimensionalObject object, Raycaster raycaster, List<Raycaster.Intersect> intersects ) 
	{
		List<DimensionalObject> descendants = object.getDescendants();

		for ( int i = 0, l = descendants.size(); i < l; i ++ ) 
		{
			intersectObject( descendants.get( i ), raycaster, intersects );
		}
	}

	//

	public void set( Vector3 origin, Vector3 direction ) 
	{
		this.ray.set( origin, direction );

		// normalized ray.direction required for accurate distance calculations
		if( this.ray.getDirection().length() > 0 ) 
		{
			this.ray.getDirection().normalize();
		}
	}

	public List<Raycaster.Intersect> intersectObject( DimensionalObject object ) 
	{
		return intersectObject(object, false);
	}
	
	public List<Raycaster.Intersect> intersectObject( DimensionalObject object, boolean recursive ) 
	{
		List<Raycaster.Intersect> intersects = new ArrayList<Raycaster.Intersect>();

		if ( recursive == true ) 
		{
			intersectDescendants( object, this, intersects );
		}

		intersectObject( object, this, intersects );

		Collections.sort(intersects);

		return intersects;
	}

	public List<Raycaster.Intersect> intersectObjects( List<? extends DimensionalObject> objects ) 
	{
		return intersectObjects(objects, false);
	}
	
	public List<Raycaster.Intersect> intersectObjects( List<? extends DimensionalObject> objects, boolean recursive ) 
	{
		List<Raycaster.Intersect> intersects = new ArrayList<Raycaster.Intersect>();
		
		for ( int i = 0, l = objects.size(); i < l; i ++ ) 
		{
			intersectObject( objects.get( i ), this, intersects );

			if ( recursive == true ) 
			{
				intersectDescendants( objects.get( i ), this, intersects );
			}
		}
		Collections.sort(intersects);

		return intersects;
	}
}
