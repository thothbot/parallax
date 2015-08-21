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

public class VertexNormalsHelper extends Line
{
	Mesh object;
	double size;
	Matrix3 normalMatrix;
	
	public VertexNormalsHelper(Mesh object) 
	{
		this(object, 1.0);
	}
	
	public VertexNormalsHelper(Mesh object, double size ) 
	{
		this(object, size, 0xff0000);
	}
	
	public VertexNormalsHelper(Mesh object, double size, int hex ) 
	{
		this(object, size, hex, 1);
	}
	
	public VertexNormalsHelper(Mesh object, double size, int hex, int linewidth ) 
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

			Face3 face = faces.get( i );

			for ( int j = 0, jl = face.getVertexNormals().size(); j < jl; j ++ ) 
			{
				
				geometry.getVertices().addAll( Arrays.asList( new Vector3(), new Vector3() ) );

			}

		}

		setMatrixAutoUpdate(false);

		this.normalMatrix = new Matrix3();

		this.update();

	}
	
	Vector3 v1 = new Vector3();
	public void update() 
	{

		this.object.updateMatrixWorld( true );

		this.normalMatrix.getNormalMatrix( this.object.getMatrixWorld() );

		List<Vector3> vertices = ((Geometry)this.getGeometry()).getVertices();
		
		List<Vector3> verts = ((Geometry)object.getGeometry()).getVertices();
		List<Face3> faces = ((Geometry)object.getGeometry()).getFaces();
		Matrix4 worldMatrix = object.getMatrixWorld();

		int idx = 0;
		int vsize = vertices.size();

		for ( int i = 0, l = faces.size(); i < l; i ++ ) {

			Face3 face = faces.get( i );

			for ( int j = 0, jl = face.getVertexNormals().size(); j < jl; j ++ ) {

				int vertexId = face.getFlat()[ j ];
				Vector3 vertex = verts.get( vertexId );

				Vector3 normal = face.getVertexNormals().get( j );

				if(vsize > idx)
				{
					vertices.get( idx ).copy( vertex ).apply( worldMatrix );
		
					v1.copy( normal ).apply( this.normalMatrix ).normalize().multiply( this.size );
		
					v1.add( vertices.get( idx ) );
					idx = idx + 1;
		
					vertices.get( idx ).copy( v1 );
					idx = idx + 1;
				}

			}

		}

		this.geometry.setVerticesNeedUpdate(true);

	}
}
