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

package thothbot.parallax.core.shared.helpers;

import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix3;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;

public class FaceNormalsHelper extends Line
{
	
	Mesh object;
	double size;
	Matrix3 normalMatrix;
	
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
		super(new Geometry(), new LineBasicMaterial(), Line.MODE.PIECES);
		
		this.object = object;
		this.size = size;
		
		LineBasicMaterial material = (LineBasicMaterial) getMaterial();
		material.setColor(new Color(hex));
		material.setLinewidth(linewidth);
		
		Geometry geometry = (Geometry) getGeometry();

		List<Face3> faces = ((Geometry)this.object.getGeometry()).getFaces();

		for ( int i = 0, l = faces.size(); i < l; i ++ ) {

			geometry.getVertices().addAll( Arrays.asList( new Vector3(), new Vector3() ) );

		}

		setMatrixAutoUpdate(false);

		this.normalMatrix = new Matrix3();

		this.update();

	}
	
	public void update() 
	{

		List<Vector3> vertices = ((Geometry)this.getGeometry()).getVertices();

		List<Vector3> objectVertices = ((Geometry)object.getGeometry()).getVertices();
		List<Face3> objectFaces = ((Geometry)object.getGeometry()).getFaces();
		Matrix4 objectWorldMatrix = object.getMatrixWorld();

		object.updateMatrixWorld( true );

		this.normalMatrix.getNormalMatrix( objectWorldMatrix );

		for ( int i = 0, i2 = 0, l = objectFaces.size(); i < l; i ++, i2 += 2 ) {

			Face3 face = objectFaces.get( i );

			vertices.get( i2 ).copy( objectVertices.get( face.getA() ) )
				.add( objectVertices.get( face.getB() ) )
				.add( objectVertices.get( face.getC() ) )
				.divide( 3 )
				.apply( objectWorldMatrix );

			vertices.get( i2 + 1 ).copy( face.getNormal() )
				.apply( this.normalMatrix )
				.normalize()
				.multiply( this.size )
				.add( vertices.get( i2 ) );

		}

		this.geometry.setVerticesNeedUpdate(true);

	}
}
