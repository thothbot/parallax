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
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.PointCloud;

public class Raycaster 
{
	public static final double precision = 0.0001;
	public static final double linePrecision = 1;
	
	public static class Intersect implements Comparable
	{
		// this works because the original ray was normalized, and the transformed localRay wasn't
		public double distance;	
		public Vector3 point;
		public double distanceToRay;
		public Face3 face;
		public int faceIndex;
		public GeometryObject object;
		
		@Override
		public int compareTo(Object arg0) 
		{
			Intersect b = (Intersect)arg0;
//			return this.distance - b.distance;
			return (this.distance == b.distance) 
					? 0 : this.distance < b.distance ? -1 : 1;
		}
	}
	
	private Ray ray;
	private double near;
	private double far;
	
//	private Sphere sphere = new Sphere();
//	private Ray localRay = new Ray();
//	private Plane facePlane = new Plane();
//	private Vector3 intersectPoint = new Vector3();
//	private Matrix4 inverseMatrix = new Matrix4();
	
	public Raycaster()
	{
		this(new Vector3(), new Vector3());
	}

	public Raycaster( Vector3 origin, Vector3 direction)
	{
		this(origin, direction, 0, Double.POSITIVE_INFINITY);
	}
	
	public Raycaster( Vector3 origin, Vector3 direction, double near, double far ) 
	{

		this.ray = new Ray( origin, direction );
		
//		// normalized ray.direction required for accurate distance calculations
//		if( this.ray.getDirection().length() > 0 ) 
//		{
//			this.ray.getDirection().normalize();
//		}

		this.near = near;
		this.far = far;
	}
	
	
	
//	var descSort = function ( a, b ) {
//
//		return a.distance - b.distance;
//
//	};
		
	public Ray getRay() {
		return ray;
	}

	public void setRay(Ray ray) {
		this.ray = ray;
	}

	public double getNear() {
		return near;
	}

	public void setNear(double near) {
		this.near = near;
	}

	public double getFar() {
		return far;
	}

	public void setFar(double far) {
		this.far = far;
	}

	public void set( Vector3 origin, Vector3 direction ) {

		this.ray.set( origin, direction );
		// direction is assumed to be normalized (for accurate distance calculations)

	}
	
	public List<Raycaster.Intersect> intersectObject( GeometryObject object, boolean recursive ) {

		List<Raycaster.Intersect>  intersects = new ArrayList<Raycaster.Intersect>();

		intersectObject( object, this, intersects, recursive );

		Collections.sort(intersects);

		return intersects;

	}
	
	public List<Raycaster.Intersect> intersectObjects ( List<Object3D> objects ) {

		List<Raycaster.Intersect>  intersects = new ArrayList<Raycaster.Intersect>();

		for ( int i = 0, l = objects.size(); i < l; i ++ ) {

			if(objects instanceof GeometryObject)
			intersectObject( (GeometryObject)objects.get( i ), this, intersects, false );

		}

		Collections.sort(intersects);

		return intersects;

	}
		
	public List<Raycaster.Intersect> intersectObjects ( List<GeometryObject> objects, boolean recursive ) {

		List<Raycaster.Intersect>  intersects = new ArrayList<Raycaster.Intersect>();

		for ( int i = 0, l = objects.size(); i < l; i ++ ) {

			intersectObject( objects.get( i ), this, intersects, recursive );

		}

		Collections.sort(intersects);

		return intersects;

	}

	
	private void intersectObject ( GeometryObject object, Raycaster raycaster, List<Intersect> intersects, boolean recursive ) {

		object.raycast( raycaster, intersects );

		if ( recursive == true ) {

			List<Object3D> children = object.children;

			for ( int i = 0, l = children.size(); i < l; i ++ ) {

				intersectObject( (GeometryObject) children.get( i ), raycaster, intersects, true );

			}

		}

	}
}
