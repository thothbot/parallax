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
import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.math.Ray;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.system.gl.enums.BeginMode;
import org.parallax3d.parallax.system.gl.enums.BufferTarget;
import org.parallax3d.parallax.system.gl.enums.BufferUsage;

/**
 * A line or a series of lines.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Line")
public class Line extends GeometryObject
{
	private static LineBasicMaterial defaultMaterial = new LineBasicMaterial();
	static {
		defaultMaterial.setColor( new Color((int)(Math.random() * 0xffffff)) );
	};

	public Line() {
		this(new Geometry());
	}

	public Line(AbstractGeometry geometry)
	{
		this(geometry, Line.defaultMaterial);
	}

	public Line(AbstractGeometry geometry, LineBasicMaterial material)
	{
		super(geometry, material);
	}

	static Matrix4 inverseMatrix = new Matrix4();
	static Ray ray = new Ray();
	static Sphere sphere = new Sphere();
	public void raycast( Raycaster raycaster, List<Raycaster.Intersect> intersects)
	{
		double precision = Raycaster.LINE_PRECISION;
		double precisionSq = precision * precision;

		AbstractGeometry geometry = this.getGeometry();

		if ( geometry.getBoundingSphere() == null )
			geometry.computeBoundingSphere();

		// Checking boundingSphere distance to ray

		sphere.copy( geometry.getBoundingSphere() );
		sphere.apply( this.getMatrixWorld() );

		if (!raycaster.getRay().intersectsSphere(sphere))
			return;

		inverseMatrix.getInverse( this.getMatrixWorld() );
		ray.copy( raycaster.getRay() ).apply( inverseMatrix );

		Vector3 vStart = new Vector3();
		Vector3 vEnd = new Vector3();
		Vector3 interSegment = new Vector3();
		Vector3 interRay = new Vector3();
		int step = this instanceof LineSegments ? 2 : 1;


		if(geometry instanceof BufferGeometry)
		{
			org.parallax3d.parallax.graphics.renderers.gl.AttributeData index = ((BufferGeometry) geometry).getIndex();
			FastMap<BufferAttribute> attributes = ((BufferGeometry) geometry).getAttributes();
			Float32Array positions = attributes.get("position").getArray();

			if ( index != null )
			{

				Int32Array indices = index.array;

				for ( int i = 0, l = indices.getLength() - 1; i < l; i += step ) {

					int a = indices.get( i );
					int b = indices.get( i + 1 );

					vStart.fromArray( positions, a * 3 );
					vEnd.fromArray( positions, b * 3 );

					double distSq = ray.distanceSqToSegment( vStart, vEnd, interRay, interSegment );

					if ( distSq > precisionSq ) continue;

					interRay.apply( this.getMatrixWorld() ); //Move back to world space for distance calculation

					double distance = raycaster.getRay().getOrigin().distanceTo( interRay );

					if ( distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

					Raycaster.Intersect intersect = new Raycaster.Intersect();
					intersect.distance = distance;
					intersect.point = interSegment.clone().apply(this.getMatrixWorld());
					intersect.index = i;
					intersect.object = this;
					intersects.add( intersect );
				}

			} else {

				for ( int i = 0, l = positions.getLength() / 3 - 1; i < l; i += step ) {

					vStart.fromArray( positions, 3 * i );
					vEnd.fromArray( positions, 3 * i + 3 );

					double distSq = ray.distanceSqToSegment( vStart, vEnd, interRay, interSegment );

					if ( distSq > precisionSq ) continue;

					interRay.apply( this.getMatrixWorld() ); //Move back to world space for distance calculation

					double distance = raycaster.getRay().getOrigin().distanceTo( interRay );

					if ( distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

					Raycaster.Intersect intersect = new Raycaster.Intersect();
					intersect.distance = distance;
					intersect.point = interSegment.clone().apply(this.getMatrixWorld());
					intersect.object = this;
					intersect.index = i;
					intersects.add( intersect );
				}

			}
		}
		else if(geometry instanceof Geometry)
		{
			List<Vector3> vertices = ((Geometry) geometry).getVertices();
			int nbVertices = vertices.size();

			for ( int i = 0; i < nbVertices - 1; i += step ) {

				double distSq = ray.distanceSqToSegment( vertices.get( i ), vertices.get( i + 1 ), interRay, interSegment );

				if ( distSq > precisionSq ) continue;

				interRay.apply( this.getMatrixWorld() ); //Move back to world space for distance calculation

				double distance = raycaster.getRay().getOrigin().distanceTo( interRay );

				if ( distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

				Raycaster.Intersect intersect = new Raycaster.Intersect();
				intersect.distance = distance;
				intersect.point = interSegment.clone().apply(this.getMatrixWorld());
				intersect.object = this;

				intersects.add( intersect );
			}
		}
	}

	public Line clone() {

		return (Line) new Line( this.geometry, (LineBasicMaterial) this.material).copy( this );

	}
}
