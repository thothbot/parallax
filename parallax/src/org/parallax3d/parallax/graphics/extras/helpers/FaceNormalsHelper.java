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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.math.Matrix3;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.List;

@ThreejsObject("THREE.FaceNormalsHelper")
public class FaceNormalsHelper extends LineSegments
{

	Mesh object;
	double size;

	public FaceNormalsHelper(Mesh object)
	{
		this(object, 1.0);
	}

	public FaceNormalsHelper(Mesh object, double size )
	{
		this(object, size, 0xffff00);
	}

	public FaceNormalsHelper(Mesh object, double size, int hex )
	{
		this(object, size, hex, 1);
	}

	public FaceNormalsHelper(Mesh object, double size, int hex, int linewidth )
	{
		super(intDefaultGeometry(object.getGeometry()), new LineBasicMaterial().setColor(hex).setLinewidth(linewidth));
		//

		this.setMatrixAutoUpdate(false);
		this.update();

	}

	private static BufferGeometry intDefaultGeometry(AbstractGeometry objGeometry) {

		int nNormals = 0;

		if ( objGeometry instanceof Geometry ) {

			nNormals = ((Geometry) objGeometry).getFaces().size();

		} else {

			Log.warn( "FaceNormalsHelper: only Geometry is supported. Use VertexNormalsHelper, instead." );

		}

		BufferGeometry geometry = new BufferGeometry();
		geometry.addAttribute( "position", new BufferAttribute(Float32Array.create( nNormals * 2 * 3), 3) );

		return geometry;
	}

	static final Vector3 v1 = new Vector3();
	static final Vector3 v2 = new Vector3();
	static final Matrix3 normalMatrix = new Matrix3();
	public FaceNormalsHelper update()
	{

		this.object.updateMatrixWorld( true );

		normalMatrix.getNormalMatrix(this.object.getMatrixWorld());

		Matrix4 matrixWorld = this.object.getMatrixWorld();

		BufferAttribute position = ((BufferGeometry) this.geometry).getAttributes().get("position");

		//

		Geometry objGeometry = (Geometry) this.object.getGeometry();

		List<Vector3> vertices = objGeometry.getVertices();

		List<Face3> faces = objGeometry.getFaces();

		int idx = 0;

		for ( int i = 0, l = faces.size(); i < l; i ++ ) {

			Face3 face = faces.get(i);

			Vector3 normal = face.getNormal();

			v1.copy(vertices.get(face.getA()))
					.add(vertices.get(face.getB()))
					.add(vertices.get(face.getC()))
					.divide( 3. )
					.apply( matrixWorld );

			v2.copy( normal ).apply( normalMatrix ).normalize().multiply( this.size ).add( v1 );

			position.setXYZ( idx, v1.getX(), v1.getY(), v1.getZ());

			idx = idx + 1;

			position.setXYZ( idx, v2.getX(), v2.getY(), v2.getZ());

			idx = idx + 1;

		}

		position.setNeedsUpdate(true);

		return this;
	}
}
