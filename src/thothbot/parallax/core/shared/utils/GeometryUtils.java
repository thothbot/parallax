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

package thothbot.parallax.core.shared.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.objects.Mesh;

/**
 * This class implements some helpers methods for geometry.
 * 
 * This code is based on js-code written by mrdoob and alteredq.
 * 
 * @author thothbot
 *
 */
public class GeometryUtils
{
	/**
	 * Merge geometric object and geometry from Mesh object into geometry object.
	 * 
	 * @param geometry1 the geometry object
	 * @param object    the Mesh object
	 */
	public static void merge( Geometry geometry1, Mesh object )
	{
		if(object.isMatrixAutoUpdate())
			object.updateMatrix();

		Matrix4 matrix = object.getMatrix();
		Matrix4 matrixRotation = new Matrix4();
		matrixRotation.extractRotation( matrix, object.getScale() );

		GeometryUtils.merge(geometry1, object.getGeometry(), matrix, matrixRotation);
	}

	/**
	 * Merge two geometric object: first and second into the first geometry object.
	 * 
	 * @param geometry1 the first geometry object
	 * @param geometry2 the second geometry object
	 */
	public static void merge( Geometry geometry1, Geometry geometry2)
	{
		GeometryUtils.merge(geometry1, geometry2, null, null);
	}

	/**
	 * Merge two geometric object: first and second into the first geometry object.
	 * 
	 * @param geometry1 the first geometry object
	 * @param geometry2 the second geometry object
	 * @param matrix    
	 * @param matrixRotation
	 */
	private static void merge( Geometry geometry1, Geometry geometry2, Matrix4 matrix, Matrix4 matrixRotation ) 
	{
		int vertexOffset = geometry1.getVertices().size();
		int uvPosition = geometry1.getFaceVertexUvs().get( 0 ).size();

		List<Vector3> vertices1 = geometry1.getVertices();
		List<Vector3> vertices2 = geometry2.getVertices();
		List<Face3> faces1 = geometry1.getFaces();
		List<Face3> faces2 = geometry2.getFaces();
		List<List<UV>> uvs1 = geometry1.getFaceVertexUvs().get( 0 );
		List<List<UV>> uvs2 = geometry2.getFaceVertexUvs().get( 0 );

		Map<Integer, Integer> geo1MaterialsMap = new HashMap<Integer, Integer>();

		if(geometry1.getMaterials() != null)
		{
			for ( int i = 0; i < geometry1.getMaterials().size(); i ++ ) 
			{
				int id = geometry1.getMaterials().get( i ).getId();

				geo1MaterialsMap.put(id, i);
			}
		}

		// vertices

		for ( int i = 0, il = vertices2.size(); i < il; i ++ ) 
		{
			Vector3 vertex = vertices2.get( i );

			Vector3 vertexCopy = vertex.clone();

			if ( matrix != null ) 
				matrix.multiplyVector3( vertexCopy );

			vertices1.add( vertexCopy );
		}

		// faces

		for ( int i = 0, il = faces2.size(); i < il; i ++ ) 
		{
			Face3 face = faces2.get( i );//, faceCopy, normal, color,
			Face3 faceCopy = null;
			List<Vector3> faceVertexNormals = face.getVertexNormals();
			List<Color> faceVertexColors = face.getVertexColors();

			if ( face.getClass() == Face3.class ) 
			{
				faceCopy = new Face3( face.getA() + vertexOffset, face.getB() + vertexOffset, face.getC() + vertexOffset );

			} 
			else if ( face.getClass() == Face4.class ) 
			{
				faceCopy = new Face4( face.getA() + vertexOffset, face.getB() + vertexOffset, face.getC() + vertexOffset, ((Face4)face).getD() + vertexOffset );
			}

			faceCopy.getNormal().copy( face.getNormal() );

			if ( matrixRotation != null ) 
				matrixRotation.multiplyVector3( faceCopy.getNormal() );

			for ( int j = 0, jl = faceVertexNormals.size(); j < jl; j ++ ) 
			{
				Vector3 normal = faceVertexNormals.get( j ).clone();

				if ( matrixRotation != null ) 
					matrixRotation.multiplyVector3( normal );

				faceCopy.getVertexNormals().add( normal );
			}

			faceCopy.getColor().copy( face.getColor() );

			for ( int j = 0, jl = faceVertexColors.size(); j < jl; j ++ ) 
			{
				Color color = faceVertexColors.get( j );
				faceCopy.getVertexColors().add( color.clone() );
			}

			if ( face.getMaterialIndex() >= 0 && geometry2.getMaterials().size() > 0) 
			{
				Material material2 = geometry2.getMaterials().get( face.getMaterialIndex() );
				int materialId2 = material2.getId();

				int materialIndex;

				if (  geo1MaterialsMap.containsKey( materialId2 ) ) 
				{

					materialIndex = geometry1.getMaterials().size();
					geo1MaterialsMap.put( materialId2, materialIndex);

					geometry1.getMaterials().add( material2 );

					faceCopy.setMaterialIndex(materialIndex);
				}
			}

			faceCopy.getCentroid().copy( face.getCentroid() );
			if ( matrix != null ) 
				matrix.multiplyVector3( faceCopy.getCentroid() );

			faces1.add( faceCopy );

		}

		// uvs

		for ( int i = 0, il = uvs2.size(); i < il; i ++ ) 
		{
			List<UV> uv = uvs2.get( i );
			List<UV> uvCopy = new ArrayList<UV>();

			for ( int j = 0, jl = uv.size(); j < jl; j ++ ) 
				uvCopy.add( new UV( uv.get( j ).getU(), uv.get( j ).getV() ) );

			uvs1.add( uvCopy );

		}
	}
	
	public static void triangulateQuads(Geometry geometry ) 
	{
		List<Face3> faces = new ArrayList<Face3>();
		List<List<UV>> faceUvs = new ArrayList<List<UV>>();
		List<List<List<UV>>> faceVertexUvs = new ArrayList<List<List<UV>>>();

		for ( int i = 0, il = geometry.getFaceUvs().size(); i < il; i ++ ) 
		{
			faceUvs.add(new ArrayList<UV>());
		}

		for ( int i = 0, il = geometry.getFaceVertexUvs().size(); i < il; i ++ ) 
		{
			faceVertexUvs.add(new ArrayList<List<UV>>());
		}

		for ( int i = 0, il = geometry.getFaces().size(); i < il; i ++ ) 
		{
			Face3 face = geometry.getFaces().get(i);

			if ( face.getClass() == Face4.class ) 
			{
				int a = face.getA();
				int b = face.getB();
				int c = face.getC();
				int d = ((Face4) face).getD();

				Face3 triA = new Face3(0, 0, 0);
				Face3 triB = new Face3(0, 0, 0);

				triA.getColor().copy( face.getColor() );
				triB.getColor().copy( face.getColor() );

				triA.setMaterialIndex(face.getMaterialIndex());
				triB.setMaterialIndex(face.getMaterialIndex());

				triA.setA(a);
				triA.setB(b);
				triA.setC(d);

				triB.setA(b);
				triB.setB(c);
				triB.setC(d);

				if ( face.getVertexColors().size() == 4 ) 
				{

					triA.getVertexColors().set( 0, face.getVertexColors().get( 0 ).clone() );
					triA.getVertexColors().set( 1, face.getVertexColors().get( 1 ).clone() );
					triA.getVertexColors().set( 2, face.getVertexColors().get( 3 ).clone() );

					triB.getVertexColors().set( 0, face.getVertexColors().get( 1 ).clone() );
					triB.getVertexColors().set( 1, face.getVertexColors().get( 2 ).clone() );
					triB.getVertexColors().set( 2, face.getVertexColors().get( 3 ).clone() );

				}

				faces.add( triA );
				faces.add( triB );

				for ( int j = 0, jl = geometry.getFaceVertexUvs().size(); j < jl; j ++ ) 
				{

					if ( geometry.getFaceVertexUvs().get( j ).size() > 0 ) 
					{
						List<UV> uvs = geometry.getFaceVertexUvs().get( j ).get( i );

						UV uvA = uvs.get( 0 );
						UV uvB = uvs.get( 1 );
						UV uvC = uvs.get( 2 );
						UV uvD = uvs.get( 3 );

						List<UV> uvsTriA = Arrays.asList( uvA.clone(), uvB.clone(), uvD.clone() );
						List<UV> uvsTriB = Arrays.asList( uvB.clone(), uvC.clone(), uvD.clone() );

						faceVertexUvs.get( j ).add( uvsTriA );
						faceVertexUvs.get( j ).add( uvsTriB );
					}
				}

				for ( int j = 0, jl = geometry.getFaceUvs().size(); j < jl; j ++ ) 
				{

					if ( geometry.getFaceUvs().get( j ).size() > 0 ) 
					{

						UV faceUv = geometry.getFaceUvs().get( j ).get( i );

						faceUvs.get( j ).add( faceUv );
						faceUvs.get( j ).add( faceUv );
					}
				}
			} 
			else 
			{
				faces.add( face );

				for ( int j = 0, jl = geometry.getFaceUvs().size(); j < jl; j ++ ) 
				{
					faceUvs.get( j ).add( geometry.getFaceUvs().get( j ).get( i ) );
				}

				for ( int j = 0, jl = geometry.getFaceVertexUvs().size(); j < jl; j ++ ) 
				{
					faceVertexUvs.get( j ).add( geometry.getFaceVertexUvs().get( j ).get( i )  );
				}
			}
		}

		geometry.setFaces(faces);
		geometry.setFaceUvs(faceUvs);
		geometry.setFaceVertexUvs(faceVertexUvs);

		geometry.computeCentroids();
		geometry.computeFaceNormals();
		geometry.computeVertexNormals();

		if ( geometry.hasTangents() ) 
			geometry.computeTangents();

	}
}
