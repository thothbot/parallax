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

package org.parallax3d.parallax.graphics.extras.helpers;

import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix3;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

@ThreejsObject("THREE.VertexNormalsHelper")
public class VertexNormalsHelper extends LineSegments
{
	Mesh object;
	double size;

	public VertexNormalsHelper(GeometryObject object)
	{
		this(object, 1.0);
	}

	public VertexNormalsHelper(GeometryObject object, double size )
	{
		this(object, size, 0xff0000);
	}

	public VertexNormalsHelper(GeometryObject object, double size, int hex )
	{
		this(object, size, hex, 1);
	}

	public VertexNormalsHelper(GeometryObject object, double size, int hex, int linewidth )
	{
		super(intDefaultGeometry(object), new LineBasicMaterial().setColor(hex).setLinewidth(linewidth));

		this.setMatrixAutoUpdate(false);

		this.update();

	}

	private static BufferGeometry intDefaultGeometry(GeometryObject object) {

		int nNormals = 0;

		AbstractGeometry objGeometry = object.getGeometry();

		if ( objGeometry instanceof Geometry ) {

			nNormals = ((Geometry) objGeometry).getFaces().size() * 3;

		} else if ( objGeometry instanceof BufferGeometry ) {

			nNormals = ((BufferGeometry) objGeometry).getAttributes().get("normal").getCount();

		}

		BufferGeometry geometry = new BufferGeometry();

		geometry.addAttribute( "position", new BufferAttribute(Float32Array.create(nNormals * 2 * 3), 3));

		return geometry;
	}

	static final Vector3 v1 = new Vector3();
	static final Vector3 v2 = new Vector3();
	static final Matrix3 normalMatrix = new Matrix3();
	public VertexNormalsHelper update()
	{

//		var keys = [ 'a', 'b', 'c' ];

		this.object.updateMatrixWorld( true );

		normalMatrix.getNormalMatrix(this.object.getMatrixWorld());

		Matrix4 matrixWorld = this.object.getMatrixWorld();

		BufferAttribute position = ((BufferGeometry) this.geometry).getAttributes().get("position");

		//

		AbstractGeometry objGeometry = this.object.getGeometry();

		if ( objGeometry instanceof Geometry ) {

			List<Vector3> vertices = ((Geometry) objGeometry).getVertices();

			List<Face3> faces = ((Geometry) objGeometry).getFaces();

			int idx = 0;

			for ( int i = 0, l = faces.size(); i < l; i ++ ) {

				Face3 face = faces.get(i);

				for (int j = 0, jl = face.getVertexNormals().size(); j < jl; j ++ ) {

					Vector3 vertex = vertices.get(face.getFlat()[j]);

					Vector3 normal = face.getVertexNormals().get(j);

					v1.copy( vertex ).apply( matrixWorld );

					v2.copy( normal ).apply( normalMatrix ).normalize().multiply( this.size ).add( v1 );

					position.setXYZ( idx, v1.getX(), v1.getY(), v1.getZ());

					idx = idx + 1;

					position.setXYZ( idx, v2.getX(), v2.getY(), v2.getZ());

					idx = idx + 1;

				}

			}

		} else if ( objGeometry instanceof BufferGeometry ) {

			BufferAttribute objPos = ((BufferGeometry) objGeometry).getAttributes().get("position");

			BufferAttribute objNorm = ((BufferGeometry) objGeometry).getAttributes().get("normal");

			int idx = 0;

			// for simplicity, ignore index and drawcalls, and render every normal

			for ( int j = 0, jl = objPos.getCount(); j < jl; j ++ ) {

				v1.set( objPos.getX( j ), objPos.getY( j ), objPos.getZ( j ) ).apply( matrixWorld );

				v2.set( objNorm.getX( j ), objNorm.getY( j ), objNorm.getZ( j ) );

				v2.apply( normalMatrix ).normalize().multiply( this.size ).add( v1 );

				position.setXYZ( idx, v1.getX(), v1.getY(), v1.getZ());

				idx = idx + 1;

				position.setXYZ( idx, v2.getX(), v2.getY(), v2.getZ());

				idx = idx + 1;

			}

		}

		position.setNeedsUpdate(true);

		return this;

	}
}
