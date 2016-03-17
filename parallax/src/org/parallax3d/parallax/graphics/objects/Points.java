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

package org.parallax3d.parallax.graphics.objects;

import java.util.List;

import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.materials.PointsMaterial;
import org.parallax3d.parallax.math.Ray;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.graphics.renderers.gl.AttributeData;

@ThreejsObject("THREE.Points")
public class Points extends GeometryObject
{
	public static double RAYCASTER_THRESHOLD = 1.0;

	private static PointsMaterial defaultMaterial = new PointsMaterial();
	static {
		defaultMaterial.setColor( new Color((int)(Math.random() * 0xffffff)) );
	};

	public Points() {
		this(new Geometry());
	}

	public Points(AbstractGeometry geometry)
	{
		this(geometry, Points.defaultMaterial);
	}

	public Points(AbstractGeometry geometry, Material material)
	{
		super(geometry, material);
	}

	static Matrix4 inverseMatrix = new Matrix4();
	static Ray ray = new Ray();
	static Sphere sphere = new Sphere();

	@Override
	public void raycast(Raycaster raycaster, List<Raycaster.Intersect> intersects)
	{
		Points object = this;
		AbstractGeometry geometry = this.geometry;
		Matrix4 matrixWorld = this.getMatrixWorld();

		// Checking boundingSphere distance to ray

		if ( geometry.getBoundingSphere() == null ) geometry.computeBoundingSphere();

		sphere.copy(geometry.getBoundingSphere());
		sphere.apply( matrixWorld );

		if (!raycaster.getRay().intersectsSphere(sphere)) return;

		//

		inverseMatrix.getInverse( matrixWorld );
		ray.copy(raycaster.getRay()).apply( inverseMatrix );

		double localThreshold = RAYCASTER_THRESHOLD / ( ( this.getScale().getX() + this.getScale().getY() + this.getScale().getZ()) / 3 );
		double localThresholdSq = localThreshold * localThreshold;
		Vector3 position = new Vector3();

		if ( geometry instanceof BufferGeometry ) {

			AttributeData index = ((BufferGeometry)geometry).getIndex();
			FastMap<BufferAttribute> attributes = ((BufferGeometry) geometry).getAttributes();
			Float32Array positions = attributes.get("position").getArray();

			if ( index != null ) {

				Int32Array indices = index.getArray();

				for ( int i = 0, il = indices.getLength(); i < il; i ++ ) {

					int a = indices.get( i );

					position.fromArray( positions, a * 3 );

					testPoint( raycaster, intersects, position, a, localThreshold );

				}

			} else {

				for ( int i = 0, l = positions.getLength() / 3; i < l; i ++ ) {

					position.fromArray( positions, i * 3 );

					testPoint( raycaster, intersects, position, i, localThreshold);

				}

			}

		} else {

			List<Vector3> vertices = ((Geometry) geometry).getVertices();

			for ( int i = 0, l = vertices.size(); i < l; i ++ ) {

				testPoint(raycaster, intersects, vertices.get(i), i, localThreshold);

			}

		}
	}

	public Points clone()
	{
		return (Points) new Points( this.geometry, this.material ).copy( this );
	}


	private void testPoint(Raycaster raycaster, List<Raycaster.Intersect> intersects, Vector3 point, int index, double localThresholdSq)
	{
		double rayPointDistanceSq = ray.distanceSqToPoint( point );
		if ( rayPointDistanceSq < localThresholdSq ) {

			Vector3 intersectPoint = ray.closestPointToPoint( point );
			intersectPoint.apply(getMatrixWorld());

			double distance = raycaster.getRay().getOrigin().distanceTo( intersectPoint );

			if ( distance < raycaster.getNear() || distance > raycaster.getFar()) return;

			Raycaster.Intersect intersect = new Raycaster.Intersect();
			intersect.distance = distance;
			intersect.distanceToRay = Math.sqrt( rayPointDistanceSq );
			intersect.point = intersectPoint.clone();
			intersect.index = index;
			intersect.object = this;

			intersects.add( intersect );

		}
	}
}
