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

package thothbot.parallax.core.client.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.scenes.Scene;

public class GeometryGroup extends WebGLGeometry
{
	public static Map<String, List<GeometryGroup>> geometryGroups = GWT.isScript() ? 
			new FastMap<List<GeometryGroup>>() : new HashMap<String, List<GeometryGroup>>(); 
	
	public static int Counter = 0;
	
	public int id = 0;
	
	public List<Integer> faces3;

	public int materialIndex = -1;

	public int vertices;
	
	public int numMorphTargets;
	public int numMorphNormals;

	public Float32Array __skinIndexArray;
	public Float32Array __skinWeightArray;
	
	public Uint16Array __faceArray;
	public Uint16Array __lineArray;
	
	public List<Float32Array> __morphTargetsArrays;
	public List<Float32Array> __morphNormalsArrays;
	
	public boolean __inittedArrays;

	public GeometryGroup(int materialIndex, int numMorphTargets, int numMorphNormals) 
	{
		super();

		this.id = GeometryGroup.Counter++;
		this.faces3 = new ArrayList<Integer>();
		this.materialIndex = materialIndex;
		this.vertices = 0;
		this.numMorphTargets = numMorphTargets;
		this.numMorphNormals = numMorphNormals;
	}
	
	public static List<GeometryGroup> makeGroups( WebGLRenderingContext gl, Geometry geometry, boolean usesFaceMaterial ) {
		
		long maxVerticesInGroup = WebGLExtensions.get(gl, WebGLExtensions.Id.OES_element_index_uint) != null ? 4294967296L : 65535L;
		
		int numMorphTargets = geometry.getMorphTargets().size();
		int numMorphNormals = geometry.getMorphNormals().size();

		String groupHash;

		Map<String, Integer> hash_map = GWT.isScript() ? 
				new FastMap<Integer>() : new HashMap<String, Integer>(); 

		Map<String, GeometryGroup> groups = GWT.isScript() ? 
				new FastMap<GeometryGroup>() : new HashMap<String, GeometryGroup>();
				
		List<GeometryGroup> groupsList = new ArrayList<GeometryGroup>();
		
		for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) {

			Face3 face = geometry.getFaces().get( f );
			Integer materialIndex = usesFaceMaterial ? face.getMaterialIndex() : 0;

			if ( ! hash_map.containsKey(materialIndex) ) {

				hash_map.put(materialIndex.toString(), 0);

			}

			groupHash = materialIndex + "_" + hash_map.get( materialIndex );

			if ( ! groups.containsKey(groupHash) ) {

				GeometryGroup group = new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals);
				
				groups.put(groupHash, group);
				groupsList.add( group );

			}

			if ( groups.get( groupHash ).vertices + 3 > maxVerticesInGroup ) {

				hash_map.put( materialIndex.toString(), hash_map.get( materialIndex ) + 1 );
				groupHash = materialIndex + "_" + hash_map.get( materialIndex );

				if ( ! groups.containsKey(groupHash) ) {

					GeometryGroup group = new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals);
					groups.put(groupHash, group);
					groupsList.add( group );
					
				}

			}

			groups.get( groupHash ).faces3.add( f );
			groups.get( groupHash ).vertices += 3;

		}

		return groupsList;

	}
	
	public static void initGeometryGroups( Scene scene, Object3D object, Geometry geometry ) {

		var material = object.material, addBuffers = false;

		if ( geometryGroups.get( geometry.getId() ) == null || geometry.groupsNeedUpdate == true ) {

			delete _webglObjects[ object.id ];

			geometryGroups.pub( geometry.getId(), makeGroups( geometry, material instanceof MeshFaceMaterial ));

			geometry.groupsNeedUpdate = false;

		}

		List<GeometryGroup> geometryGroupsList = geometryGroups.get( geometry.getId() );

		// create separate VBOs per geometry chunk

		for ( int i = 0, il = geometryGroupsList.size(); i < il; i ++ ) {

			GeometryGroup geometryGroup = geometryGroupsList.get( i );

			// initialise VBO on the first access

			if ( geometryGroup.__webglVertexBuffer == null ) {

				createMeshBuffers( geometryGroup );
				initMeshBuffers( geometryGroup, object );

				geometry.verticesNeedUpdate = true;
				geometry.morphTargetsNeedUpdate = true;
				geometry.elementsNeedUpdate = true;
				geometry.uvsNeedUpdate = true;
				geometry.normalsNeedUpdate = true;
				geometry.tangentsNeedUpdate = true;
				geometry.colorsNeedUpdate = true;

				addBuffers = true;

			} else {

				addBuffers = false;

			}

			if ( addBuffers || !object.isWebglActive ) {

				addBuffer( _webglObjects, geometryGroup, object );

			}

		}

		object.isWebglActive = true;

	}
	
	@Override
	public void dispose() 
	{
		super.dispose();
		
		__inittedArrays = false;
		__faceArray = null;
		__lineArray = null;
		__skinIndexArray = null;
		__skinWeightArray = null;
	}
}
