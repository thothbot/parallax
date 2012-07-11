/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Face3;
import thothbot.squirrel.core.shared.core.Face4;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.Matrix4f;
import thothbot.squirrel.core.shared.core.UVf;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.objects.Mesh;

/**
 * This class implements some helpers methods for geometry.
 * 
 * This class based on js-code written by mrdoob and alteredq.
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
		if(object.getMatrixAutoUpdate())
			object.updateMatrix();

		Matrix4f matrix = object.getMatrix();
		Matrix4f matrixRotation = new Matrix4f();
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
	private static void merge( Geometry geometry1, Geometry geometry2, Matrix4f matrix, Matrix4f matrixRotation ) 
	{
			int vertexOffset = geometry1.getVertices().size();
			int uvPosition = geometry1.getFaceVertexUvs().get( 0 ).size();
			
			List<Vector3f> vertices1 = geometry1.getVertices();
			List<Vector3f> vertices2 = geometry2.getVertices();
			List<Face3> faces1 = geometry1.getFaces();
			List<Face3> faces2 = geometry2.getFaces();
			List<List<UVf>> uvs1 = geometry1.getFaceVertexUvs().get( 0 );
			List<List<UVf>> uvs2 = geometry2.getFaceVertexUvs().get( 0 );

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
				Vector3f vertex = vertices2.get( i );

				Vector3f vertexCopy = vertex.clone();

				if ( matrix != null ) 
					matrix.multiplyVector3( vertexCopy );

				vertices1.add( vertexCopy );
			}

			// faces

			for ( int i = 0, il = faces2.size(); i < il; i ++ ) 
			{
				Face3 face = faces2.get( i );//, faceCopy, normal, color,
				Face3 faceCopy = null;
				List<Vector3f> faceVertexNormals = face.getVertexNormals();
				List<Color3f> faceVertexColors = face.getVertexColors();

				if ( face instanceof Face3 ) 
				{
					faceCopy = new Face3( face.getA() + vertexOffset, face.getB() + vertexOffset, face.getC() + vertexOffset );

				} else if ( face instanceof Face4 ) 
				{
					Face4 face4 = (Face4) face;
					faceCopy = new Face4( face4.getA() + vertexOffset, face4.getB() + vertexOffset, face4.getC() + vertexOffset, face4.getD() + vertexOffset );
				}

				faceCopy.getNormal().copy( face.getNormal() );

				if ( matrixRotation != null ) 
					matrixRotation.multiplyVector3( faceCopy.getNormal() );

				for ( int j = 0, jl = faceVertexNormals.size(); j < jl; j ++ ) 
				{
					Vector3f normal = faceVertexNormals.get( j ).clone();

					if ( matrixRotation != null ) 
						matrixRotation.multiplyVector3( normal );

					faceCopy.getVertexNormals().add( normal );
				}

				faceCopy.getColor().copy( face.getColor() );

				for ( int j = 0, jl = faceVertexColors.size(); j < jl; j ++ ) 
				{
					Color3f color = faceVertexColors.get( j );
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
				List<UVf> uv = uvs2.get( i );
				List<UVf> uvCopy = new ArrayList<UVf>();

				for ( int j = 0, jl = uv.size(); j < jl; j ++ ) 
					uvCopy.add( new UVf( uv.get( j ).getU(), uv.get( j ).getV() ) );

				uvs1.add( uvCopy );

			}
		}
}
