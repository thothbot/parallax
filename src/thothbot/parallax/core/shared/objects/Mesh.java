/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.WebGLRenderInfo;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Color3;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.GeometryGroup;
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.core.Vector4;
import thothbot.parallax.core.shared.core.WebGLCustomAttribute;
import thothbot.parallax.core.shared.materials.HasSkinning;
import thothbot.parallax.core.shared.materials.HasWireframe;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;

public class Mesh  extends GeometryObject implements HasSides
{
	private Boolean flipSided;
	private Boolean doubleSided;
	private Boolean overdraw;
	private Integer morphTargetBase = null;
	private List<Double> morphTargetInfluences;
	private List<Integer> morphTargetForcedOrder;
	private HashMap<String, Integer> morphTargetDictionary;
	public Float32Array __webglMorphTargetInfluences;
	
	private static int _geometryGroupCounter = 0;

	private static MeshBasicMaterial defaultMaterial = new MeshBasicMaterial();
	static {
		defaultMaterial.setColor( new Color3((int) Math.random() * 0xffffff) );
		defaultMaterial.setWireframe( true );
	};

	public Mesh(Geometry geometry) 
	{
		this(geometry, Mesh.defaultMaterial);
	}
	
	public Mesh(Geometry geometry, Material material) 
	{
		super();

		this.geometry = geometry;
		this.material = material;
		this.flipSided = false;
		this.doubleSided = false;
		this.overdraw = false;

		if (this.geometry != null) 
		{
			// calc bound radius
			if (this.geometry.getBoundingSphere() == null)
				this.geometry.computeBoundingSphere();

			this.boundRadius = this.geometry.getBoundingSphere().radius;

			// setup morph targets
			if (this.geometry.getMorphTargets().size() != 0) 
			{
				this.morphTargetBase = -1;
				this.morphTargetForcedOrder = new ArrayList<Integer>();
				this.morphTargetInfluences = new ArrayList<Double>();
				this.morphTargetDictionary = new HashMap<String, Integer>();

				List<Geometry.MorphTarget> morphTargets = this.geometry.getMorphTargets();
				for (int m = 0; m < morphTargets.size(); m++) 
				{
					this.morphTargetInfluences.add(0.0);
					this.morphTargetDictionary.put(morphTargets.get(m).name, m);
				}
			}
		}
	}

	public boolean isFlipSided()
	{
		return this.flipSided;
	}

	public void setFlipSided(boolean flipSided)
	{
		this.flipSided = flipSided;
	}

	public boolean isDoubleSided()
	{
		return this.doubleSided;
	}

	public void setDoubleSided(boolean doubleSided)
	{
		this.doubleSided = doubleSided;
	}

	public boolean getOverdraw()
	{
		return this.overdraw;
	}

	public void setOverdraw(boolean overdraw)
	{
		this.overdraw = overdraw;
	}

	/**
	 * Get Morph Target Index by Name
	 */
	public int getMorphTargetIndexByName(String name)
	{
		if (this.morphTargetDictionary.containsKey(name))
			return this.morphTargetDictionary.get(name);

		Log.debug("Mesh.getMorphTargetIndexByName: morph target " + name
				+ " does not exist. Returning 0.");
		return 0;
	}

	public Integer getMorphTargetBase() {
		return morphTargetBase;
	}
	
	public List<Double> getMorphTargetInfluences() {
		return this.morphTargetInfluences;
	}
	
	public List<Integer> getMorphTargetForcedOrder() {
		return this.morphTargetForcedOrder;
	}

	@Override
	public void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();

		// wireframe
		if ( getMaterial() instanceof HasWireframe && ((HasWireframe)getMaterial()).isWireframe() ) 
		{
			setLineWidth( gl, ((HasWireframe)getMaterial()).getWireframeLineWidth() );

			if ( updateBuffers ) 
				gl.bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), geometryBuffer.__webglLineBuffer );
			
			gl.drawElements( GLenum.LINES.getValue(), geometryBuffer.__webglLineCount, GLenum.UNSIGNED_SHORT.getValue(), 0 );

			// triangles

		}
		else 
		{
			if ( updateBuffers ) 
				gl.bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), geometryBuffer.__webglFaceBuffer );
			
			gl.drawElements( GLenum.TRIANGLES.getValue(), geometryBuffer.__webglFaceCount, GLenum.UNSIGNED_SHORT.getValue(), 0 );
		}
		
		info.getRender().calls ++;
		info.getRender().vertices += geometryBuffer.__webglFaceCount;
		info.getRender().faces += geometryBuffer.__webglFaceCount / 3;
	}

	/*
	 * Returns geometry quantities
	 */
	@Override
	public void initBuffer(WebGLRenderer renderer) 
	{
		WebGLRenderInfo info = renderer.getInfo();
		int geometries = 0;
		
		Geometry geometry = this.getGeometry();

		if(geometry instanceof Geometry) 
		{
			Log.debug("addObject() geometry.geometryGroups is null: " + ( this.getGeometry().getGeometryGroups() == null ));
			if ( geometry.getGeometryGroups() == null )
				sortFacesByMaterial( this.getGeometry() );

			// create separate VBOs per geometry chunk
			for ( GeometryGroup geometryGroup : geometry.getGeometryGroups().values() ) 
			{
				// initialise VBO on the first access
				if ( geometryGroup.__webglVertexBuffer == null ) 
				{
					createBuffers(renderer, geometryGroup );
					initBuffers(renderer.getGL(), geometryGroup );
					info.getMemory().geometries++;

					geometry.verticesNeedUpdate = true;
					geometry.morphTargetsNeedUpdate = true;
					geometry.elementsNeedUpdate = true;
					geometry.uvsNeedUpdate = true;
					geometry.normalsNeedUpdate = true;
					geometry.tangetsNeedUpdate = true;
					geometry.colorsNeedUpdate = true;
				}
			}
		}
	}

	// initMeshBuffers
	private void initBuffers(WebGLRenderingContext gl, GeometryGroup geometryGroup)
	{
		Geometry geometry = this.geometry;

		List<Integer> faces3 = geometryGroup.faces3;
		List<Integer> faces4 = geometryGroup.faces4;

		int nvertices = faces3.size() * 3 + faces4.size() * 4;
		int ntris = faces3.size() * 1 + faces4.size() * 2;
		int nlines = faces3.size() * 3 + faces4.size() * 4;

		Material material = Material.getBufferMaterial(this, geometryGroup);

		boolean uvType = material.bufferGuessUVType();
		Material.SHADING normalType = material.bufferGuessNormalType();
		Material.COLORS vertexColorType = material.bufferGuessVertexColorType();

		geometryGroup.setWebGlVertexArray( Float32Array.create(nvertices * 3) );

		if (normalType != null)
			geometryGroup.setWebGlNormalArray( Float32Array.create(nvertices * 3) );

		if (geometry.getHasTangents())
			geometryGroup.setWebGlTangentArray( Float32Array.create(nvertices * 4) );

		if (vertexColorType != null)
			geometryGroup.setWebGlColorArray( Float32Array.create(nvertices * 3) );

		if (uvType) 
		{
			if (geometry.getFaceUvs().size() > 0 || geometry.getFaceVertexUvs().size() > 0)
				geometryGroup.setWebGlUvArray( Float32Array.create(nvertices * 2) );

			if (geometry.getFaceUvs().size() > 1 || geometry.getFaceVertexUvs().size() > 1)
				geometryGroup.setWebGlUv2Array( Float32Array.create(nvertices * 2) );
		}

		if (this.geometry.getSkinWeights().size() > 0 && this.geometry.getSkinIndices().size() > 0) 
		{
			geometryGroup.setWebGlSkinVertexAArray( Float32Array.create(nvertices * 4) );
			geometryGroup.setWebGlSkinVertexBArray( Float32Array.create(nvertices * 4) );
			geometryGroup.setWebGlSkinIndexArray  ( Float32Array.create(nvertices * 4) );
			geometryGroup.setWebGlSkinWeightArray ( Float32Array.create(nvertices * 4) );
		}

		geometryGroup.setWebGlFaceArray( Uint16Array.create(ntris * 3) );
		geometryGroup.setWebGlLineArray( Uint16Array.create(nlines * 2) );

		if (geometryGroup.numMorphTargets > 0) 
		{
			geometryGroup.__morphTargetsArrays = new ArrayList<Float32Array>();

			for (int m = 0; m < geometryGroup.numMorphTargets; m++)
				geometryGroup.__morphTargetsArrays.add(Float32Array.create(nvertices * 3));
		}

		if (geometryGroup.numMorphNormals > 0) 
		{
			geometryGroup.__morphNormalsArrays = new ArrayList<Float32Array>();

			for (int m = 0; m < geometryGroup.numMorphNormals; m++)
				geometryGroup.__morphNormalsArrays.add(Float32Array.create(nvertices * 3));
		}

		geometryGroup.__webglFaceCount = ntris * 3;
		geometryGroup.__webglLineCount = nlines * 2;

		// custom attributes

		if (material.getAttributes() != null) 
		{
			if (geometryGroup.__webglCustomAttributesList == null)
				geometryGroup.__webglCustomAttributesList = new ArrayList<WebGLCustomAttribute>();

			for (String a : material.getAttributes().keySet()) 
			{
				WebGLCustomAttribute originalAttribute = material.getAttributes().get(a);

				// Do a shallow copy of the attribute object so different
				// geometryGroup chunks use different
				// attribute buffers which are correctly indexed in the
				// setMeshBuffers function
				WebGLCustomAttribute attribute = originalAttribute.clone();

				if (!attribute.__webglInitialized || attribute.createUniqueBuffers) 
				{
					attribute.__webglInitialized = true;

					int size = 1; // "f" and "i"

					if (attribute.type == WebGLCustomAttribute.TYPE.V2)
						size = 2;
					else if (attribute.type == WebGLCustomAttribute.TYPE.V3)
						size = 3;
					else if (attribute.type == WebGLCustomAttribute.TYPE.V4)
						size = 4;
					else if (attribute.type == WebGLCustomAttribute.TYPE.C)
						size = 3;

					attribute.size = size;

					attribute.array = Float32Array.create(nvertices * size);

					attribute.buffer = gl.createBuffer();
					attribute.belongsToAttribute = a;

					originalAttribute.needsUpdate = true;
				}

				geometryGroup.__webglCustomAttributesList.add(attribute);
			}
		}

		geometryGroup.setArrayInitialized(true);
	}

	// createMeshBuffers
	private void createBuffers(WebGLRenderer renderer, GeometryGroup geometryGroup)
	{
		WebGLRenderingContext gl = renderer.getGL();
		
		geometryGroup.__webglVertexBuffer = gl.createBuffer();
		geometryGroup.__webglNormalBuffer = gl.createBuffer();
		geometryGroup.__webglTangentBuffer = gl.createBuffer();
		geometryGroup.__webglColorBuffer = gl.createBuffer();
		geometryGroup.__webglUVBuffer = gl.createBuffer();
		geometryGroup.__webglUV2Buffer = gl.createBuffer();

		geometryGroup.__webglSkinVertexABuffer = gl.createBuffer();
		geometryGroup.__webglSkinVertexBBuffer = gl.createBuffer();
		geometryGroup.__webglSkinIndicesBuffer = gl.createBuffer();
		geometryGroup.__webglSkinWeightsBuffer = gl.createBuffer();

		geometryGroup.__webglFaceBuffer = gl.createBuffer();
		geometryGroup.__webglLineBuffer = gl.createBuffer();

		if (geometryGroup.numMorphTargets != 0) {
			geometryGroup.__webglMorphTargetsBuffers = new ArrayList<WebGLBuffer>();

			for (int m = 0; m < geometryGroup.numMorphTargets; m++) {
				geometryGroup.__webglMorphTargetsBuffers.add(gl.createBuffer());
			}
		}

		if (geometryGroup.numMorphNormals != 0) {
			geometryGroup.__webglMorphNormalsBuffers = new ArrayList<WebGLBuffer>();

			for (int m = 0; m < geometryGroup.numMorphNormals; m++) {
				geometryGroup.__webglMorphNormalsBuffers.add(gl.createBuffer());
			}
		}

	}

	public void setBuffer(WebGLRenderer renderer) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		// TODO: Fix BufferGeometry
//		if ( geometry.getClass() == BufferGeometry.class ) {
//
//			/*
//			if ( geometry.verticesNeedUpdate || geometry.elementsNeedUpdate ||
//				 geometry.uvsNeedUpdate || geometry.normalsNeedUpdate ||
//				 geometry.colorsNeedUpdate  ) {
//
//				// TODO
//				// set buffers from typed arrays
//
//			}
//			*/
//
//			geometry.verticesNeedUpdate = false;
//			geometry.elementsNeedUpdate = false;
//			geometry.uvsNeedUpdate = false;
//			geometry.normalsNeedUpdate = false;
//			geometry.colorsNeedUpdate = false;
//
//		} else {

			// check all geometry groups
			for( int i = 0, il = geometry.getGeometryGroupsList().size(); i < il; i ++ ) 
			{

				GeometryGroup geometryGroup = geometry.getGeometryGroupsList().get( i );
				material = Material.getBufferMaterial( this, geometryGroup );

				boolean customAttributesDirty = (material.getAttributes() != null); // && areCustomAttributesDirty( material );

				if ( geometry.verticesNeedUpdate || geometry.morphTargetsNeedUpdate || geometry.elementsNeedUpdate ||
					 geometry.uvsNeedUpdate      || geometry.normalsNeedUpdate      ||
					 geometry.colorsNeedUpdate   || geometry.tangetsNeedUpdate      || customAttributesDirty ) 
				{
					setBuffers(gl, geometryGroup, GLenum.DYNAMIC_DRAW.getValue(), !geometry.isDynamic(), material );
				}
			}

			geometry.verticesNeedUpdate = false;
			geometry.morphTargetsNeedUpdate = false;
			geometry.elementsNeedUpdate = false;
			geometry.uvsNeedUpdate = false;
			geometry.normalsNeedUpdate = false;
			geometry.colorsNeedUpdate = false;
			geometry.tangetsNeedUpdate = false;

			material.clearCustomAttributes();
//		}

	}

	// setMeshBuffers
	private void setBuffers(WebGLRenderingContext gl, GeometryGroup geometryGroup, int hint, boolean dispose, Material material)
	{
		Log.debug("Called Mesh.setBuffers() - material=" + material.getId() + ", " + material.getClass().getName());

		if ( ! geometryGroup.isArrayInitialized() )
			 return;
				
		 Material.SHADING normalType = material.bufferGuessNormalType();
		 Material.COLORS vertexColorType = material.bufferGuessVertexColorType();
		 boolean uvType = material.bufferGuessUVType();
		
		 boolean needsSmoothNormals = ( normalType == Material.SHADING.SMOOTH );
										 
		 List<Integer> chunk_faces3 = geometryGroup.faces3;
		 List<Integer> chunk_faces4 = geometryGroup.faces4;
		 List<Face3> obj_faces = getGeometry().getFaces();

		 List<List<UV>> obj_uvs = (getGeometry().getFaceVertexUvs().size() > 0) 
				 ? getGeometry().getFaceVertexUvs().get(0) : null;

		 List<List<UV>> obj_uvs2 = (getGeometry().getFaceVertexUvs().size() > 1) 
				 ? getGeometry().getFaceVertexUvs().get(1) : null;
						
		 List<Geometry.MorphNormal> morphNormals = getGeometry().getMorphNormals();
		 List<Geometry.MorphTarget> morphTargets = getGeometry().getMorphTargets();
		 
		 if ( getGeometry().verticesNeedUpdate ) 
		 {
			 Float32Array vertexArray = geometryGroup.getWebGlVertexArray();
			 int offset = 0;
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {
				 Face3 face = obj_faces.get( chunk_faces3.get( f ) );
				 
				 Vector3 v1 = getGeometry().getVertices().get( face.getA() );
				 Vector3 v2 = getGeometry().getVertices().get( face.getB() );
				 Vector3 v3 = getGeometry().getVertices().get( face.getC() );

				 vertexArray.set(offset,  v1.getX());
				 vertexArray.set(offset + 1, v1.getY());
				 vertexArray.set(offset + 2, v1.getZ());

				 vertexArray.set(offset + 3, v2.getX());
				 vertexArray.set(offset + 4, v2.getY());
				 vertexArray.set(offset + 5, v2.getZ());

				 vertexArray.set(offset + 6, v3.getX());
				 vertexArray.set(offset + 7, v3.getY());
				 vertexArray.set(offset + 8, v3.getZ());

				 offset += 9;
			 }
			 
			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
			 {
				 Face4 face = (Face4) obj_faces.get( chunk_faces4.get( f ));

				 Vector3 v1 = getGeometry().getVertices().get( face.getA() );
				 Vector3 v2 = getGeometry().getVertices().get( face.getB() );
				 Vector3 v3 = getGeometry().getVertices().get( face.getC() );
				 Vector3 v4 = getGeometry().getVertices().get( face.getD() );

				 vertexArray.set(offset, v1.getX());
				 vertexArray.set(offset + 1, v1.getY());
				 vertexArray.set(offset + 2, v1.getZ());

				 vertexArray.set(offset + 3, v2.getX());
				 vertexArray.set(offset + 4, v2.getY());
				 vertexArray.set(offset + 5, v2.getZ());

				 vertexArray.set(offset + 6, v3.getX());
				 vertexArray.set(offset + 7, v3.getY());
				 vertexArray.set(offset + 8, v3.getZ());

				 vertexArray.set(offset + 9, v4.getX());
				 vertexArray.set(offset + 10, v4.getY());
				 vertexArray.set(offset + 11, v4.getZ());

				 offset += 12;
			 }

			 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglVertexBuffer);
			 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), vertexArray, hint );
		 }
		 
		 if ( getGeometry().morphTargetsNeedUpdate ) 
		 {
			 
			 for ( int vk = 0, vkl = getGeometry().getMorphTargets().size(); vk < vkl; vk ++ ) 
			 {
				 int offset_morphTarget = 0;

				 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
				 {
					 int chf = chunk_faces3.get( f );
					 Face3 face = obj_faces.get( chf );

					 // morph positions

					 Geometry.MorphTarget d1 = morphTargets.get( vk ); 
					 Vector3 v1 = d1.vertices.get( face.getA() );
					 Vector3 v2 = morphTargets.get( vk ).vertices.get( face.getB() );
					 Vector3 v3 = morphTargets.get( vk ).vertices.get( face.getC() );

					 Float32Array vka = geometryGroup.__morphTargetsArrays.get(vk);

					 vka.set(offset_morphTarget, v1.getX());
					 vka.set(offset_morphTarget + 1, v1.getY());
					 vka.set(offset_morphTarget + 2, v1.getZ());

					 vka.set(offset_morphTarget + 3, v2.getX());
					 vka.set(offset_morphTarget + 4, v2.getY());
					 vka.set(offset_morphTarget + 5, v2.getZ());

					 vka.set(offset_morphTarget + 6, v3.getX());
					 vka.set(offset_morphTarget + 7, v3.getY());
					 vka.set(offset_morphTarget + 8, v3.getZ());

					 // morph normals

					 if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() ) 
					 {
						 Vector3 n1, n2, n3;
						 if ( needsSmoothNormals ) 
						 {
							 Geometry.VertextNormal faceVertexNormals = morphNormals.get( vk ).vertexNormals.get( chf );

							 n1 = faceVertexNormals.a;
							 n2 = faceVertexNormals.b;
							 n3 = faceVertexNormals.c;
						 } 
						 else 
						 {
							 n1 = morphNormals.get( vk ).faceNormals.get( chf );
							 n2 = n1;
							 n3 = n1;
						 }

						 Float32Array nka = geometryGroup.__morphNormalsArrays.get( vk );

						 nka.set(offset_morphTarget, n1.getX());
						 nka.set(offset_morphTarget + 1, n1.getY());
						 nka.set(offset_morphTarget + 2, n1.getZ());

						 nka.set(offset_morphTarget + 3, n2.getX());
						 nka.set(offset_morphTarget + 4, n2.getY());
						 nka.set(offset_morphTarget + 5, n2.getZ());

						 nka.set(offset_morphTarget + 6, n3.getX());
						 nka.set(offset_morphTarget + 7, n3.getY());
						 nka.set(offset_morphTarget + 8, n3.getZ());
					 }

					 //

					 offset_morphTarget += 9;

				 }

				 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
				 {
					 int chf = chunk_faces4.get(f);
					 Face4 face = (Face4) obj_faces.get(chf);

					 // morph positions

					 Vector3 v1 = morphTargets.get(vk).vertices.get(face.getA());
					 Vector3 v2 = morphTargets.get(vk).vertices.get(face.getB());
					 Vector3 v3 = morphTargets.get(vk).vertices.get(face.getC());
					 Vector3 v4 = morphTargets.get(vk).vertices.get(face.getD());

					 Float32Array vka = geometryGroup.__morphTargetsArrays.get(vk);

					 vka.set(offset_morphTarget, v1.getX());
					 vka.set(offset_morphTarget + 1, v1.getY());
					 vka.set(offset_morphTarget + 2, v1.getZ());

					 vka.set(offset_morphTarget + 3, v2.getX());
					 vka.set(offset_morphTarget + 4, v2.getY());
					 vka.set(offset_morphTarget + 5, v2.getZ());

					 vka.set(offset_morphTarget + 6, v3.getX());
					 vka.set(offset_morphTarget + 7, v3.getY());
					 vka.set(offset_morphTarget + 8, v3.getZ());

					 vka.set(offset_morphTarget + 9, v4.getX());
					 vka.set(offset_morphTarget + 10, v4.getY());
					 vka.set(offset_morphTarget + 11, v4.getZ());

					 // morph normals

					 if (  material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() ) 
					 {
						 Vector3 n1, n2, n3, n4;
						 if ( needsSmoothNormals ) 
						 {

							 Geometry.VertextNormal faceVertexNormals = morphNormals.get( vk ).vertexNormals.get( chf );

							 n1 = faceVertexNormals.a;
							 n2 = faceVertexNormals.b;
							 n3 = faceVertexNormals.c;
							 n4 = faceVertexNormals.d;

						 } 
						 else 
						 {
							 n1 = morphNormals.get(vk).faceNormals.get(chf);
							 n2 = n1;
							 n3 = n1;
							 n4 = n1;

						 }

						 Float32Array nka = geometryGroup.__morphNormalsArrays.get( vk );

						 nka.set(offset_morphTarget, n1.getX());
						 nka.set(offset_morphTarget + 1, n1.getY());
						 nka.set(offset_morphTarget + 2, n1.getZ());

						 nka.set(offset_morphTarget + 3, n2.getX());
						 nka.set(offset_morphTarget + 4, n2.getY());
						 nka.set(offset_morphTarget + 5, n2.getZ());

						 nka.set(offset_morphTarget + 6, n3.getX());
						 nka.set(offset_morphTarget + 7, n3.getY());
						 nka.set(offset_morphTarget + 8, n3.getZ());

						 nka.set(offset_morphTarget + 9, n4.getX());
						 nka.set(offset_morphTarget + 10, n4.getY());
						 nka.set(offset_morphTarget + 11, n4.getZ());

					 }

					 //

					 offset_morphTarget += 12;

				 }

				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphTargetsBuffers.get( vk ) );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__morphTargetsArrays.get( vk ), hint );

				 if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() ) 
				 {
					 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphNormalsBuffers.get( vk ) );
					 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__morphNormalsArrays.get( vk ), hint );
				 }
			 }
		 }

		 if ( getGeometry().getSkinWeights().size() > 0 ) 
		 {
			 int offset_skin = 0;
			 Float32Array skinVertexAArray = geometryGroup.getWebGlSkinVertexAArray();
			 Float32Array skinVertexBArray = geometryGroup.getWebGlSkinVertexBArray();

			 Float32Array skinIndexArray = geometryGroup.getWebGlSkinIndexArray();
			 Float32Array skinWeightArray = geometryGroup.getWebGlSkinWeightArray();
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {
				 Face3 face = obj_faces.get( chunk_faces3.get( f ) );

				 // weights

				 Vector4 sw1 = getGeometry().getSkinWeights().get( face.getA() );
				 Vector4 sw2 = getGeometry().getSkinWeights().get( face.getB() );
				 Vector4 sw3 = getGeometry().getSkinWeights().get( face.getC() );

				 skinWeightArray.set(offset_skin, sw1.getX());
				 skinWeightArray.set(offset_skin + 1, sw1.getY());
				 skinWeightArray.set(offset_skin + 2, sw1.getZ());
				 skinWeightArray.set(offset_skin + 3, sw1.getW());

				 skinWeightArray.set(offset_skin + 4, sw2.getX());
				 skinWeightArray.set(offset_skin + 5, sw2.getY());
				 skinWeightArray.set(offset_skin + 6, sw2.getZ());
				 skinWeightArray.set(offset_skin + 7, sw2.getW());

				 skinWeightArray.set(offset_skin + 8, sw3.getX());
				 skinWeightArray.set(offset_skin + 9, sw3.getY());
				 skinWeightArray.set(offset_skin + 10, sw3.getZ());
				 skinWeightArray.set(offset_skin + 11, sw3.getW());

				 // indices

				 Vector4 si1 = (Vector4) getGeometry().getSkinIndices().get(face.getA());
				 Vector4 si2 = (Vector4) getGeometry().getSkinIndices().get(face.getB());
				 Vector4 si3 = (Vector4) getGeometry().getSkinIndices().get(face.getC());

				 skinIndexArray.set(offset_skin, si1.getX());
				 skinIndexArray.set(offset_skin + 1, si1.getY());
				 skinIndexArray.set(offset_skin + 2, si1.getZ());
				 skinIndexArray.set(offset_skin + 3, si1.getW());

				 skinIndexArray.set(offset_skin + 4, si2.getX());
				 skinIndexArray.set(offset_skin + 5, si2.getY());
				 skinIndexArray.set(offset_skin + 6, si2.getZ());
				 skinIndexArray.set(offset_skin + 7, si2.getW());

				 skinIndexArray.set(offset_skin + 8, si3.getX());
				 skinIndexArray.set(offset_skin + 9, si3.getY());
				 skinIndexArray.set(offset_skin + 10, si3.getZ());
				 skinIndexArray.set(offset_skin + 11, si3.getW());

				 // vertices A

				 Vector3 sa1 = getGeometry().getSkinVerticesA().get(face.getA());
				 Vector3 sa2 = getGeometry().getSkinVerticesA().get(face.getB());
				 Vector3 sa3 = getGeometry().getSkinVerticesA().get(face.getC());

				 skinVertexAArray.set(offset_skin, sa1.getX());
				 skinVertexAArray.set(offset_skin + 1, sa1.getY());
				 skinVertexAArray.set(offset_skin + 2, sa1.getZ());
				 skinVertexAArray.set(offset_skin + 3, 1.0f); // pad for faster vertex shader

				 skinVertexAArray.set(offset_skin + 4, sa2.getX());
				 skinVertexAArray.set(offset_skin + 5, sa2.getY());
				 skinVertexAArray.set(offset_skin + 6, sa2.getZ());
				 skinVertexAArray.set(offset_skin + 7, 1.0f);

				 skinVertexAArray.set(offset_skin + 8, sa3.getX());
				 skinVertexAArray.set(offset_skin + 9, sa3.getY());
				 skinVertexAArray.set(offset_skin + 10, sa3.getZ());
				 skinVertexAArray.set(offset_skin + 11, 1.0f);

				 // vertices B

				 Vector3 sb1 = getGeometry().getSkinVerticesB().get(face.getA());
				 Vector3 sb2 = getGeometry().getSkinVerticesB().get(face.getB());
				 Vector3 sb3 = getGeometry().getSkinVerticesB().get(face.getC());

				 skinVertexBArray.set(offset_skin, sb1.getX());
				 skinVertexBArray.set(offset_skin + 1, sb1.getY());
				 skinVertexBArray.set(offset_skin + 2, sb1.getZ());
				 skinVertexBArray.set(offset_skin + 3, 1.0f); // pad for faster vertex shader

				 skinVertexBArray.set(offset_skin + 4, sb2.getX());
				 skinVertexBArray.set(offset_skin + 5, sb2.getY());
				 skinVertexBArray.set(offset_skin + 6, sb2.getZ());
				 skinVertexBArray.set(offset_skin + 7, 1.0f);

				 skinVertexBArray.set(offset_skin + 8, sb3.getX());
				 skinVertexBArray.set(offset_skin + 9, sb3.getY());
				 skinVertexBArray.set(offset_skin + 10, sb3.getZ());
				 skinVertexBArray.set(offset_skin + 11, 1.0f);

				 offset_skin += 12;

			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
			 {

				 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

				 // weights

				 Vector4 sw1 = getGeometry().getSkinWeights().get(face.getA());
				 Vector4 sw2 = getGeometry().getSkinWeights().get(face.getB());
				 Vector4 sw3 = getGeometry().getSkinWeights().get(face.getC());
				 Vector4 sw4 = getGeometry().getSkinWeights().get(face.getD());

				 skinWeightArray.set(offset_skin, sw1.getX());
				 skinWeightArray.set(offset_skin + 1, sw1.getY());
				 skinWeightArray.set(offset_skin + 2, sw1.getZ());
				 skinWeightArray.set(offset_skin + 3, sw1.getW());

				 skinWeightArray.set(offset_skin + 4, sw2.getX());
				 skinWeightArray.set(offset_skin + 5, sw2.getY());
				 skinWeightArray.set(offset_skin + 6, sw2.getZ());
				 skinWeightArray.set(offset_skin + 7, sw2.getW());

				 skinWeightArray.set(offset_skin + 8, sw3.getX());
				 skinWeightArray.set(offset_skin + 9, sw3.getY());
				 skinWeightArray.set(offset_skin + 10, sw3.getZ());
				 skinWeightArray.set(offset_skin + 11, sw3.getW());

				 skinWeightArray.set(offset_skin + 12, sw4.getX());
				 skinWeightArray.set(offset_skin + 13, sw4.getY());
				 skinWeightArray.set(offset_skin + 14, sw4.getZ());
				 skinWeightArray.set(offset_skin + 15, sw4.getW());

				 // indices

				 Vector4 si1 = getGeometry().getSkinIndices().get(face.getA());
				 Vector4 si2 = getGeometry().getSkinIndices().get(face.getB());
				 Vector4 si3 = getGeometry().getSkinIndices().get(face.getC());
				 Vector4 si4 = getGeometry().getSkinIndices().get(face.getD());

				 skinIndexArray.set(offset_skin, si1.getX());
				 skinIndexArray.set(offset_skin + 1, si1.getY());
				 skinIndexArray.set(offset_skin + 2, si1.getZ());
				 skinIndexArray.set(offset_skin + 3, si1.getW());

				 skinIndexArray.set(offset_skin + 4, si2.getX());
				 skinIndexArray.set(offset_skin + 5, si2.getY());
				 skinIndexArray.set(offset_skin + 6, si2.getZ());
				 skinIndexArray.set(offset_skin + 7, si2.getW());

				 skinIndexArray.set(offset_skin + 8, si3.getX());
				 skinIndexArray.set(offset_skin + 9, si3.getY());
				 skinIndexArray.set(offset_skin + 10, si3.getZ());
				 skinIndexArray.set(offset_skin + 11, si3.getW());

				 skinIndexArray.set(offset_skin + 12, si4.getX());
				 skinIndexArray.set(offset_skin + 13, si4.getY());
				 skinIndexArray.set(offset_skin + 14, si4.getZ());
				 skinIndexArray.set(offset_skin + 15, si4.getW());

				 // vertices A

				 Vector3 sa1 = getGeometry().getSkinVerticesA().get(face.getA());
				 Vector3 sa2 = getGeometry().getSkinVerticesA().get(face.getB());
				 Vector3 sa3 = getGeometry().getSkinVerticesA().get(face.getC());
				 Vector3 sa4 = getGeometry().getSkinVerticesA().get(face.getD());

				 skinVertexAArray.set(offset_skin, sa1.getX());
				 skinVertexAArray.set(offset_skin + 1, sa1.getY());
				 skinVertexAArray.set(offset_skin + 2, sa1.getZ());
				 skinVertexAArray.set(offset_skin + 3, 1.0f); // pad for faster vertex shader

				 skinVertexAArray.set(offset_skin + 4, sa2.getX());
				 skinVertexAArray.set(offset_skin + 5, sa2.getY());
				 skinVertexAArray.set(offset_skin + 6, sa2.getZ());
				 skinVertexAArray.set(offset_skin + 7, 1.0f);

				 skinVertexAArray.set(offset_skin + 8, sa3.getX());
				 skinVertexAArray.set(offset_skin + 9, sa3.getY());
				 skinVertexAArray.set(offset_skin + 10, sa3.getZ());
				 skinVertexAArray.set(offset_skin + 11, 1.0f);

				 skinVertexAArray.set(offset_skin + 12, sa4.getX());
				 skinVertexAArray.set(offset_skin + 13, sa4.getY());
				 skinVertexAArray.set(offset_skin + 14, sa4.getZ());
				 skinVertexAArray.set(offset_skin + 15, 1.0f);

				 // vertices B

				 Vector3 sb1 = getGeometry().getSkinVerticesB().get(face.getA());
				 Vector3 sb2 = getGeometry().getSkinVerticesB().get(face.getB());
				 Vector3 sb3 = getGeometry().getSkinVerticesB().get(face.getC());
				 Vector3 sb4 = getGeometry().getSkinVerticesB().get(face.getD());

				 skinVertexBArray.set(offset_skin, sb1.getX());
				 skinVertexBArray.set(offset_skin + 1, sb1.getY());
				 skinVertexBArray.set(offset_skin + 2, sb1.getZ());
				 skinVertexBArray.set(offset_skin + 3, 1.0f); // pad for faster vertex shader

				 skinVertexBArray.set(offset_skin + 4, sb2.getX());
				 skinVertexBArray.set(offset_skin + 5, sb2.getY());
				 skinVertexBArray.set(offset_skin + 6, sb2.getZ());
				 skinVertexBArray.set(offset_skin + 7, 1.0f);

				 skinVertexBArray.set(offset_skin + 8, sb3.getX());
				 skinVertexBArray.set(offset_skin + 9, sb3.getY());
				 skinVertexBArray.set(offset_skin + 10, sb3.getZ());
				 skinVertexBArray.set(offset_skin + 11, 1.0f);

				 skinVertexBArray.set(offset_skin + 12, sb4.getX());
				 skinVertexBArray.set(offset_skin + 13, sb4.getY());
				 skinVertexBArray.set(offset_skin + 14, sb4.getZ());
				 skinVertexBArray.set(offset_skin + 15, 1.0f);

				 offset_skin += 16;

			 }

			 if ( offset_skin > 0 ) 
			 {
				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglSkinVertexABuffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), skinVertexAArray, hint );

				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglSkinVertexBBuffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), skinVertexBArray, hint );

				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglSkinIndicesBuffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), skinIndexArray, hint );

				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglSkinWeightsBuffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), skinWeightArray, hint );
			 }
		 }

		 if ( getGeometry().colorsNeedUpdate && (vertexColorType != null )) 
		 {
			 Float32Array colorArray = geometryGroup.getWebGlColorArray();
			 int offset_color = 0;
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {

				 Face3 face = obj_faces.get(chunk_faces3.get(f));

				 List<Color3> vertexColors = face.getVertexColors();
				 Color3 faceColor = face.getColor();
				 Color3 c1, c2, c3;

				 if ( vertexColors.size() == 3 && vertexColorType == Material.COLORS.VERTEX) 
				 {
					 c1 = vertexColors.get(0);
					 c2 = vertexColors.get(1);
					 c3 = vertexColors.get(2);
				 }
				 else 
				 {
					 c1 = faceColor;
					 c2 = faceColor;
					 c3 = faceColor;
				 }

				 colorArray.set(offset_color, c1.getR());
				 colorArray.set(offset_color + 1, c1.getG());
				 colorArray.set(offset_color + 2, c1.getB());

				 colorArray.set(offset_color + 3, c2.getR());
				 colorArray.set(offset_color + 4, c2.getG());
				 colorArray.set(offset_color + 5, c2.getB());

				 colorArray.set(offset_color + 6, c3.getR());
				 colorArray.set(offset_color + 7, c3.getG());
				 colorArray.set(offset_color + 8, c3.getB());

				 offset_color += 9;

			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) {

				 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

				 List<Color3> vertexColors = face.getVertexColors();
				 Color3 faceColor = face.getColor();
				 Color3 c1, c2, c3, c4;

				 if ( vertexColors.size() == 4 && vertexColorType == Material.COLORS.VERTEX) 
				 {
					 c1 = vertexColors.get(0);
					 c2 = vertexColors.get(1);
					 c3 = vertexColors.get(2);
					 c4 = vertexColors.get(3);
				 } 
				 else 
				 {
					 c1 = faceColor;
					 c2 = faceColor;
					 c3 = faceColor;
					 c4 = faceColor;
				 }

				 colorArray.set(offset_color, c1.getR());
				 colorArray.set(offset_color + 1, c1.getG());
				 colorArray.set(offset_color + 2, c1.getB());

				 colorArray.set(offset_color + 3, c2.getR());
				 colorArray.set(offset_color + 4, c2.getG());
				 colorArray.set(offset_color + 5, c2.getB());

				 colorArray.set(offset_color + 6, c3.getR());
				 colorArray.set(offset_color + 7, c3.getG());
				 colorArray.set(offset_color + 8, c3.getB());

				 colorArray.set(offset_color + 9, c4.getR());
				 colorArray.set(offset_color + 10, c4.getG());
				 colorArray.set(offset_color + 11, c4.getB());

				 offset_color += 12;

			 }

			 if ( offset_color > 0 ) 
			 {
				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglColorBuffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), colorArray, hint );
			 }
		 }

		 if ( getGeometry().tangetsNeedUpdate && geometry.getHasTangents()) 
		 {
			 Float32Array tangentArray = geometryGroup.getWebGlTangentArray();
			 int offset_tangent = 0;
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {

				 Face3 face = obj_faces.get(chunk_faces3.get(f));

				 List<Vector4> vertexTangents = face.getVertexTangents();

				 Vector4 t1 = vertexTangents.get(0);
				 Vector4 t2 = vertexTangents.get(1);
				 Vector4 t3 = vertexTangents.get(2);

				 tangentArray.set(offset_tangent, t1.getX());
				 tangentArray.set(offset_tangent + 1, t1.getY());
				 tangentArray.set(offset_tangent + 2, t1.getZ());
				 tangentArray.set(offset_tangent + 3, t1.getW());

				 tangentArray.set(offset_tangent + 4, t2.getX());
				 tangentArray.set(offset_tangent + 5, t2.getY());
				 tangentArray.set(offset_tangent + 6, t2.getZ());
				 tangentArray.set(offset_tangent + 7, t2.getW());

				 tangentArray.set(offset_tangent + 8, t3.getX());
				 tangentArray.set(offset_tangent + 9, t3.getY());
				 tangentArray.set(offset_tangent + 10, t3.getZ());
				 tangentArray.set(offset_tangent + 11, t3.getW());

				 offset_tangent += 12;

			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) {

				 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

				 List<Vector4> vertexTangents = face.getVertexTangents();

				 Vector4 t1 = vertexTangents.get(0);
				 Vector4 t2 = vertexTangents.get(1);
				 Vector4 t3 = vertexTangents.get(2);
				 Vector4 t4 = vertexTangents.get(3);

				 tangentArray.set(offset_tangent, t1.getX());
				 tangentArray.set(offset_tangent + 1, t1.getY());
				 tangentArray.set(offset_tangent + 2, t1.getZ());
				 tangentArray.set(offset_tangent + 3, t1.getW());

				 tangentArray.set(offset_tangent + 4, t2.getX());
				 tangentArray.set(offset_tangent + 5, t2.getY());
				 tangentArray.set(offset_tangent + 6, t2.getZ());
				 tangentArray.set(offset_tangent + 7, t2.getW());

				 tangentArray.set(offset_tangent + 8, t3.getX());
				 tangentArray.set(offset_tangent + 9, t3.getY());
				 tangentArray.set(offset_tangent + 10, t3.getZ());
				 tangentArray.set(offset_tangent + 11, t3.getW());

				 tangentArray.set(offset_tangent + 12, t4.getX());
				 tangentArray.set(offset_tangent + 13, t4.getY());
				 tangentArray.set(offset_tangent + 14, t4.getZ());
				 tangentArray.set(offset_tangent + 15, t4.getW());

				 offset_tangent += 16;

			 }

			 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglTangentBuffer );
			 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), tangentArray, hint );

		 }

		 if ( getGeometry().normalsNeedUpdate && (normalType != null )) 
		 {
			 int offset_normal = 0;
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {

				 Face3 face = obj_faces.get(chunk_faces3.get(f));

				 List<Vector3> vertexNormals = face.getVertexNormals();
				 Vector3 faceNormal = face.getNormal();

				 if ( vertexNormals.size() == 3 && needsSmoothNormals ) 
				 {
					 for ( int i = 0; i < 3; i ++ ) 
					 {

						 Vector3 vn = vertexNormals.get(i);

						 geometryGroup.getWebGlNormalArray().set(offset_normal, vn.getX());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 1, vn.getY());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 2, vn.getZ());

						 offset_normal += 3;
					 }

				 } 
				 else 
				 {

					 for ( int i = 0; i < 3; i ++ ) 
					 {

						 geometryGroup.getWebGlNormalArray().set(offset_normal, faceNormal.getX());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 1, faceNormal.getY());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 2, faceNormal.getZ());

						 offset_normal += 3;
					 }
				 }
			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
			 {

				 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

				 List<Vector3> vertexNormals = face.getVertexNormals();
				 Vector3 faceNormal = face.getNormal();

				 if ( vertexNormals.size() == 4 && needsSmoothNormals ) 
				 {
					 for ( int i = 0; i < 4; i ++ ) 
					 {

						 Vector3 vn = vertexNormals.get(i);

						 geometryGroup.getWebGlNormalArray().set(offset_normal, vn.getX());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 1, vn.getY());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 2, vn.getZ());

						 offset_normal += 3;
					 }

				 } 
				 else 
				 {

					 for ( int i = 0; i < 4; i ++ ) 
					 {

						 geometryGroup.getWebGlNormalArray().set(offset_normal, faceNormal.getX());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 1, faceNormal.getY());
						 geometryGroup.getWebGlNormalArray().set(offset_normal + 2, faceNormal.getZ());

						 offset_normal += 3;
					 }
				 }
			 }

			 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglNormalBuffer);
			 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.getWebGlNormalArray(), hint );

		 }

		 if ( getGeometry().uvsNeedUpdate && (obj_uvs != null) && uvType ) 
		 {
			 Float32Array uvArray = geometryGroup.getWebGlUvArray();
			 int offset_uv = 0;
			 
			 for (int  f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {

				 int fi = chunk_faces3.get(f);

				 Face3 face = obj_faces.get(fi);
				 List<UV> uv = obj_uvs.get(fi);

				 if ( uv == null ) continue;

				 for ( int i = 0; i < 3; i ++ ) {

					 UV uvi = uv.get(i);

					 uvArray.set(offset_uv, uvi.getU());
					 uvArray.set(offset_uv + 1, uvi.getV());

					 offset_uv += 2;
				 }
			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
			 {
				 int fi = chunk_faces4.get(f);

				 Face4 face = (Face4) obj_faces.get(fi);
				 List<UV>uv = obj_uvs.get(fi);

				 if ( uv == null ) continue;

				 for ( int i = 0; i < 4; i ++ ) 
				 {

					 UV uvi = uv.get(i);

					 uvArray.set(offset_uv, uvi.getU());
					 uvArray.set(offset_uv + 1, uvi.getV());

					 offset_uv += 2;
				 }
			 }

			 if ( offset_uv > 0 ) 
			 {
				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglUVBuffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), uvArray, hint );
			 }
		 }

		 if ( getGeometry().uvsNeedUpdate && (obj_uvs2 != null) && uvType ) 
		 {
			 Float32Array uv2Array = geometryGroup.getWebGlUv2Array();
			 int offset_uv2 = 0;
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {
				 int fi = chunk_faces3.get(f);

				 Face3 face = obj_faces.get(fi);
				 List<UV> uv2 = obj_uvs2.get(fi);

				 if ( uv2 == null ) continue;

				 for ( int i = 0; i < 3; i ++ ) 
				 {
					 UV uv2i = uv2.get(i);

					 uv2Array.set(offset_uv2, uv2i.getU());
					 uv2Array.set(offset_uv2 + 1, uv2i.getV());

					 offset_uv2 += 2;
				 }
			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
			 {
				 int fi = chunk_faces4.get(f);

				 Face4 face = (Face4) obj_faces.get(fi);
				 List<UV> uv2 = obj_uvs2.get(fi);

				 if ( uv2 == null ) continue;

				 for ( int i = 0; i < 4; i ++ ) 
				 {
					 UV uv2i = uv2.get(i);

					 uv2Array.set(offset_uv2, uv2i.getU());
					 uv2Array.set(offset_uv2 + 1, uv2i.getV());

					 offset_uv2 += 2;
				 }
			 }

			 if ( offset_uv2 > 0 ) 
			 {
				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglUV2Buffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), uv2Array, hint );
			 }
		 }

		 if (  getGeometry().elementsNeedUpdate ) 
		 {
			 int offset_line = 0;
			 int offset_face = 0;
			 int vertexIndex = 0;
			 
			 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
			 {
				 Face3 face = obj_faces.get(chunk_faces3.get(f));

				 geometryGroup.getWebGlFaceArray().set(offset_face, vertexIndex);
				 geometryGroup.getWebGlFaceArray().set(offset_face + 1, vertexIndex + 1);
				 geometryGroup.getWebGlFaceArray().set(offset_face + 2, vertexIndex + 2);

				 offset_face += 3;

				 geometryGroup.getWebGlLineArray().set(offset_line, vertexIndex);
				 geometryGroup.getWebGlLineArray().set(offset_line + 1, vertexIndex + 1);

				 geometryGroup.getWebGlLineArray().set(offset_line + 2, vertexIndex);
				 geometryGroup.getWebGlLineArray().set(offset_line + 3, vertexIndex + 2);

				 geometryGroup.getWebGlLineArray().set(offset_line + 4, vertexIndex + 1);
				 geometryGroup.getWebGlLineArray().set(offset_line + 5, vertexIndex + 2);

				 offset_line += 6;

				 vertexIndex += 3;
			 }

			 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
			 {
				 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

				 geometryGroup.getWebGlFaceArray().set(offset_face, vertexIndex);
				 geometryGroup.getWebGlFaceArray().set(offset_face + 1, vertexIndex + 1);
				 geometryGroup.getWebGlFaceArray().set(offset_face + 2, vertexIndex + 3);

				 geometryGroup.getWebGlFaceArray().set(offset_face + 3, vertexIndex + 1);
				 geometryGroup.getWebGlFaceArray().set(offset_face + 4, vertexIndex + 2);
				 geometryGroup.getWebGlFaceArray().set(offset_face + 5, vertexIndex + 3);

				 offset_face += 6;

				 geometryGroup.getWebGlLineArray().set(offset_line, vertexIndex);
				 geometryGroup.getWebGlLineArray().set(offset_line + 1, vertexIndex + 1);

				 geometryGroup.getWebGlLineArray().set(offset_line + 2, vertexIndex);
				 geometryGroup.getWebGlLineArray().set(offset_line + 3, vertexIndex + 3);

				 geometryGroup.getWebGlLineArray().set(offset_line + 4, vertexIndex + 1);
				 geometryGroup.getWebGlLineArray().set(offset_line + 5, vertexIndex + 2);

				 geometryGroup.getWebGlLineArray().set(offset_line + 6, vertexIndex + 2);
				 geometryGroup.getWebGlLineArray().set(offset_line + 7, vertexIndex + 3);

				 offset_line += 8;

				 vertexIndex += 4;

			 }
			 
			 gl.bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.__webglFaceBuffer );
			 gl.bufferData( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.getWebGlFaceArray(), hint );

			 gl.bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.__webglLineBuffer );
			 gl.bufferData( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.getWebGlLineArray(), hint );

		 }

		 if ( geometryGroup.__webglCustomAttributesList != null ) 
		 {
			 for ( int i = 0, il = geometryGroup.__webglCustomAttributesList.size(); i < il; i ++ ) 
			 {
				 WebGLCustomAttribute customAttribute = geometryGroup.__webglCustomAttributesList.get(i);

				 if ( ! customAttribute.__original.needsUpdate ) continue;

				 int offset_custom = 0;
				 int offset_customSrc = 0;

				 if ( customAttribute.size == 1 ) 
				 {
					 if ( customAttribute.boundTo == null || customAttribute.boundTo == "vertices" ) 
					 {
						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
						 {
							 Face3 face = obj_faces.get(chunk_faces3.get(f));

							 customAttribute.array.set(offset_custom, (Double)customAttribute.getValue().get(face.getA()));
							 customAttribute.array.set(offset_custom + 1, (Double)customAttribute.getValue().get(face.getB()));
							 customAttribute.array.set(offset_custom + 2, (Double)customAttribute.getValue().get(face.getC()));

							 offset_custom += 3;
						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
						 {
							 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

							 customAttribute.array.set(offset_custom, (Double)customAttribute.getValue().get(face.getA()));
							 customAttribute.array.set(offset_custom + 1, (Double)customAttribute.getValue().get(face.getB()));
							 customAttribute.array.set(offset_custom + 2, (Double)customAttribute.getValue().get(face.getC()));
							 customAttribute.array.set(offset_custom + 3, (Double)customAttribute.getValue().get(face.getD()));

							 offset_custom += 4;
						 }
					 }
					 else if ( customAttribute.boundTo == "faces" ) 
					 {
						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
						 {
							 double value = (Double) customAttribute.getValue().get(chunk_faces3.get(f));

							 customAttribute.array.set(offset_custom, value);
							 customAttribute.array.set(offset_custom + 1, value);
							 customAttribute.array.set(offset_custom + 2, value);

							 offset_custom += 3;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
						 {
							 double value = (Double) customAttribute.getValue().get(chunk_faces4.get(f));

							 customAttribute.array.set(offset_custom, value);
							 customAttribute.array.set(offset_custom + 1, value);
							 customAttribute.array.set(offset_custom + 2, value);
							 customAttribute.array.set(offset_custom + 3, value);

							 offset_custom += 4;
						 }
					 }
				 } 
				 else if ( customAttribute.size == 2 ) 
				 {
					 if ( customAttribute.boundTo == null || customAttribute.boundTo == "vertices" ) 
					 {
						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
						 {
							 Face3 face = obj_faces.get(chunk_faces3.get(f));

							 Vector3 v1 = (Vector3) customAttribute.getValue().get(face.getA());
							 Vector3 v2 = (Vector3) customAttribute.getValue().get(face.getB());
							 Vector3 v3 = (Vector3) customAttribute.getValue().get(face.getC());

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());

							 customAttribute.array.set(offset_custom + 2, v2.getX());
							 customAttribute.array.set(offset_custom + 3, v2.getY());

							 customAttribute.array.set(offset_custom + 4, v3.getX());
							 customAttribute.array.set(offset_custom + 5, v3.getY());

							 offset_custom += 6;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
						 {
							 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

							 Vector3 v1 = (Vector3) customAttribute.getValue().get(face.getA());
							 Vector3 v2 = (Vector3) customAttribute.getValue().get(face.getB());
							 Vector3 v3 = (Vector3) customAttribute.getValue().get(face.getC());
							 Vector3 v4 = (Vector3) customAttribute.getValue().get(face.getD());

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());

							 customAttribute.array.set(offset_custom + 2, v2.getX());
							 customAttribute.array.set(offset_custom + 3, v2.getY());

							 customAttribute.array.set(offset_custom + 4, v3.getX());
							 customAttribute.array.set(offset_custom + 5, v3.getY());

							 customAttribute.array.set(offset_custom + 6, v4.getX());
							 customAttribute.array.set(offset_custom + 7, v4.getY());

							 offset_custom += 8;
						 }
					 } 
					 else if ( customAttribute.boundTo == "faces" ) 
					 {
						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) 
						 {
							 Vector3 value = (Vector3) customAttribute.getValue().get(chunk_faces3.get(f));

							 Vector3 v1 = value;
							 Vector3 v2 = value;
							 Vector3 v3 = value;

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());

							 customAttribute.array.set(offset_custom + 2, v2.getX());
							 customAttribute.array.set(offset_custom + 3, v2.getY());

							 customAttribute.array.set(offset_custom + 4, v3.getX());
							 customAttribute.array.set(offset_custom + 5, v3.getY());

							 offset_custom += 6;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) 
						 {
							 Vector3 value = (Vector3) customAttribute.getValue().get(chunk_faces4.get(f));

							 Vector3 v1 = value;
							 Vector3 v2 = value;
							 Vector3 v3 = value;
							 Vector3 v4 = value;

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());

							 customAttribute.array.set(offset_custom + 2, v2.getX());
							 customAttribute.array.set(offset_custom + 3, v2.getY());

							 customAttribute.array.set(offset_custom + 4, v3.getX());
							 customAttribute.array.set(offset_custom + 5, v3.getY());

							 customAttribute.array.set(offset_custom + 6, v4.getX());
							 customAttribute.array.set(offset_custom + 7, v4.getY());

							 offset_custom += 8;

						 }

					 }

				 } 
				 else if ( customAttribute.size == 3 ) 
				 {
					 if ( customAttribute.boundTo == null || customAttribute.boundTo == "vertices" ) 
					 {

						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) {

							 Face3 face = obj_faces.get(chunk_faces3.get(f));

							 if(customAttribute.type == WebGLCustomAttribute.TYPE.C) {
								 Color3 v1 = (Color3) customAttribute.getValue().get(face.getA());
								 Color3 v2 = (Color3) customAttribute.getValue().get(face.getB());
								 Color3 v3 = (Color3) customAttribute.getValue().get(face.getC());

								 customAttribute.array.set(offset_custom, v1.getR());
								 customAttribute.array.set(offset_custom + 1, v1.getG());
								 customAttribute.array.set(offset_custom + 2, v1.getB());

								 customAttribute.array.set(offset_custom + 3, v2.getR());
								 customAttribute.array.set(offset_custom + 4, v2.getG());
								 customAttribute.array.set(offset_custom + 5, v2.getB());

								 customAttribute.array.set(offset_custom + 6, v3.getR());
								 customAttribute.array.set(offset_custom + 7, v3.getG());
								 customAttribute.array.set(offset_custom + 8, v3.getB());
							 }
							 else
							 {
								 Vector3 v1 = (Vector3) customAttribute.getValue().get(face.getA());
								 Vector3 v2 = (Vector3) customAttribute.getValue().get(face.getB());
								 Vector3 v3 = (Vector3) customAttribute.getValue().get(face.getC());

								 customAttribute.array.set(offset_custom, v1.getX());
								 customAttribute.array.set(offset_custom + 1, v1.getY());
								 customAttribute.array.set(offset_custom + 2, v1.getZ());

								 customAttribute.array.set(offset_custom + 3, v2.getX());
								 customAttribute.array.set(offset_custom + 4, v2.getY());
								 customAttribute.array.set(offset_custom + 5, v2.getZ());

								 customAttribute.array.set(offset_custom + 6, v3.getX());
								 customAttribute.array.set(offset_custom + 7, v3.getY());
								 customAttribute.array.set(offset_custom + 8, v3.getZ());
							 }

							 offset_custom += 9;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) {

							 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

							 if(customAttribute.type == WebGLCustomAttribute.TYPE.C) {
								 Color3 v1 = (Color3) customAttribute.getValue().get(face.getA());
								 Color3 v2 = (Color3) customAttribute.getValue().get(face.getB());
								 Color3 v3 = (Color3) customAttribute.getValue().get(face.getC());
								 Color3 v4 = (Color3) customAttribute.getValue().get(face.getD());

								 customAttribute.array.set(offset_custom, v1.getR());
								 customAttribute.array.set(offset_custom + 1, v1.getG());
								 customAttribute.array.set(offset_custom + 2, v1.getB());

								 customAttribute.array.set(offset_custom + 3, v2.getR());
								 customAttribute.array.set(offset_custom + 4, v2.getG());
								 customAttribute.array.set(offset_custom + 5, v2.getB());

								 customAttribute.array.set(offset_custom + 6, v3.getR());
								 customAttribute.array.set(offset_custom + 7, v3.getG());
								 customAttribute.array.set(offset_custom + 8, v3.getB());

								 customAttribute.array.set(offset_custom + 9, v4.getR());
								 customAttribute.array.set(offset_custom + 10, v4.getG());
								 customAttribute.array.set(offset_custom + 11, v4.getB());
							 }
							 else
							 {
								 Vector3 v1 = (Vector3) customAttribute.getValue().get(face.getA());
								 Vector3 v2 = (Vector3) customAttribute.getValue().get(face.getB());
								 Vector3 v3 = (Vector3) customAttribute.getValue().get(face.getC());
								 Vector3 v4 = (Vector3) customAttribute.getValue().get(face.getD());

								 customAttribute.array.set(offset_custom, v1.getX());
								 customAttribute.array.set(offset_custom + 1, v1.getY());
								 customAttribute.array.set(offset_custom + 2, v1.getZ());

								 customAttribute.array.set(offset_custom + 3, v2.getX());
								 customAttribute.array.set(offset_custom + 4, v2.getY());
								 customAttribute.array.set(offset_custom + 5, v2.getZ());

								 customAttribute.array.set(offset_custom + 6, v3.getX());
								 customAttribute.array.set(offset_custom + 7, v3.getY());
								 customAttribute.array.set(offset_custom + 8, v3.getZ());

								 customAttribute.array.set(offset_custom + 9, v4.getX());
								 customAttribute.array.set(offset_custom + 10, v4.getY());
								 customAttribute.array.set(offset_custom + 11, v4.getZ());
							 }

							 offset_custom += 12;
						 }

					 } else if ( customAttribute.boundTo == "faces" ) {

						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) {
							 if(customAttribute.type == WebGLCustomAttribute.TYPE.C) {
								 Color3 value = (Color3) customAttribute.getValue().get(chunk_faces3.get(f));
								 Color3 v1 = value;
								 Color3 v2 = value;
								 Color3 v3 = value;

								 customAttribute.array.set(offset_custom, v1.getR());
								 customAttribute.array.set(offset_custom + 1, v1.getG());
								 customAttribute.array.set(offset_custom + 2, v1.getB());

								 customAttribute.array.set(offset_custom + 3, v2.getR());
								 customAttribute.array.set(offset_custom + 4, v2.getG());
								 customAttribute.array.set(offset_custom + 5, v2.getB());

								 customAttribute.array.set(offset_custom + 6, v3.getR());
								 customAttribute.array.set(offset_custom + 7, v3.getG());
								 customAttribute.array.set(offset_custom + 8, v3.getB());
							 }
							 else
							 {
								 Vector3 value = (Vector3) customAttribute.getValue().get(chunk_faces3.get(f));
								 Vector3 v1 = value;
								 Vector3 v2 = value;
								 Vector3 v3 = value;

								 customAttribute.array.set(offset_custom, v1.getX());
								 customAttribute.array.set(offset_custom + 1, v1.getY());
								 customAttribute.array.set(offset_custom + 2, v1.getZ());

								 customAttribute.array.set(offset_custom + 3, v2.getX());
								 customAttribute.array.set(offset_custom + 4, v2.getY());
								 customAttribute.array.set(offset_custom + 5, v2.getZ());

								 customAttribute.array.set(offset_custom + 6, v3.getX());
								 customAttribute.array.set(offset_custom + 7, v3.getY());
								 customAttribute.array.set(offset_custom + 8, v3.getZ());
							 }

							 offset_custom += 9;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) {

							 if(customAttribute.type == WebGLCustomAttribute.TYPE.C) {
								 Color3 value = (Color3) customAttribute.getValue().get(chunk_faces4.get(f));
								 Color3 v1 = value;
								 Color3 v2 = value;
								 Color3 v3 = value;
								 Color3 v4 = value;

								 customAttribute.array.set(offset_custom, v1.getR());
								 customAttribute.array.set(offset_custom + 1, v1.getG());
								 customAttribute.array.set(offset_custom + 2, v1.getB());

								 customAttribute.array.set(offset_custom + 3, v2.getR());
								 customAttribute.array.set(offset_custom + 4, v2.getG());
								 customAttribute.array.set(offset_custom + 5, v2.getB());

								 customAttribute.array.set(offset_custom + 6, v3.getR());
								 customAttribute.array.set(offset_custom + 7, v3.getG());
								 customAttribute.array.set(offset_custom + 8, v3.getB());

								 customAttribute.array.set(offset_custom + 9, v4.getR());
								 customAttribute.array.set(offset_custom + 10, v4.getG());
								 customAttribute.array.set(offset_custom + 11, v4.getB());
							 }
							 else
							 {
								 Vector3 value = (Vector3) customAttribute.getValue().get(chunk_faces4.get(f));
								 Vector3 v1 = value;
								 Vector3 v2 = value;
								 Vector3 v3 = value;
								 Vector3 v4 = value;

								 customAttribute.array.set(offset_custom, v1.getX());
								 customAttribute.array.set(offset_custom + 1, v1.getY());
								 customAttribute.array.set(offset_custom + 2, v1.getZ());

								 customAttribute.array.set(offset_custom + 3, v2.getX());
								 customAttribute.array.set(offset_custom + 4, v2.getY());
								 customAttribute.array.set(offset_custom + 5, v2.getZ());

								 customAttribute.array.set(offset_custom + 6, v3.getX());
								 customAttribute.array.set(offset_custom + 7, v3.getY());
								 customAttribute.array.set(offset_custom + 8, v3.getZ());

								 customAttribute.array.set(offset_custom + 9, v4.getX());
								 customAttribute.array.set(offset_custom + 10, v4.getY());
								 customAttribute.array.set(offset_custom + 11, v4.getZ());
							 }

							 offset_custom += 12;

						 }

					 }

				 } 
				 else if ( customAttribute.size == 4 ) 
				 {

					 if ( customAttribute.boundTo == null || customAttribute.boundTo == "vertices" ) 
					 {

						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) {

							 Face3 face = obj_faces.get(chunk_faces3.get(f));

							 Vector4 v1 = (Vector4) customAttribute.getValue().get(face.getA());
							 Vector4 v2 = (Vector4) customAttribute.getValue().get(face.getB());
							 Vector4 v3 = (Vector4) customAttribute.getValue().get(face.getC());

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());
							 customAttribute.array.set(offset_custom + 2, v1.getZ());
							 customAttribute.array.set(offset_custom + 3, v1.getW());

							 customAttribute.array.set(offset_custom + 4, v2.getX());
							 customAttribute.array.set(offset_custom + 5, v2.getY());
							 customAttribute.array.set(offset_custom + 6, v2.getZ());
							 customAttribute.array.set(offset_custom + 7, v2.getW());

							 customAttribute.array.set(offset_custom + 8, v3.getX());
							 customAttribute.array.set(offset_custom + 9, v3.getY());
							 customAttribute.array.set(offset_custom + 10, v3.getZ());
							 customAttribute.array.set(offset_custom + 11, v3.getW());

							 offset_custom += 12;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) {

							 Face4 face = (Face4) obj_faces.get(chunk_faces4.get(f));

							 Vector4 v1 = (Vector4) customAttribute.getValue().get(face.getA());
							 Vector4 v2 = (Vector4) customAttribute.getValue().get(face.getB());
							 Vector4 v3 = (Vector4) customAttribute.getValue().get(face.getC());
							 Vector4 v4 = (Vector4) customAttribute.getValue().get(face.getD());

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());
							 customAttribute.array.set(offset_custom + 2, v1.getZ());
							 customAttribute.array.set(offset_custom + 3, v1.getW());

							 customAttribute.array.set(offset_custom + 4, v2.getX());
							 customAttribute.array.set(offset_custom + 5, v2.getY());
							 customAttribute.array.set(offset_custom + 6, v2.getZ());
							 customAttribute.array.set(offset_custom + 7, v2.getW());

							 customAttribute.array.set(offset_custom + 8, v3.getX());
							 customAttribute.array.set(offset_custom + 9, v3.getY());
							 customAttribute.array.set(offset_custom + 10, v3.getZ());
							 customAttribute.array.set(offset_custom + 11, v3.getW());

							 customAttribute.array.set(offset_custom + 12, v4.getX());
							 customAttribute.array.set(offset_custom + 13, v4.getY());
							 customAttribute.array.set(offset_custom + 14, v4.getZ());
							 customAttribute.array.set(offset_custom + 15, v4.getW());

							 offset_custom += 16;
						 }

					 } else if ( customAttribute.boundTo == "faces" ) {

						 for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) {

							 Vector4 value = (Vector4) customAttribute.getValue().get(chunk_faces3.get(f));

							 Vector4 v1 = value;
							 Vector4 v2 = value;
							 Vector4 v3 = value;

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());
							 customAttribute.array.set(offset_custom + 2, v1.getZ());
							 customAttribute.array.set(offset_custom + 3, v1.getW());

							 customAttribute.array.set(offset_custom + 4, v2.getX());
							 customAttribute.array.set(offset_custom + 5, v2.getY());
							 customAttribute.array.set(offset_custom + 6, v2.getZ());
							 customAttribute.array.set(offset_custom + 7, v2.getW());

							 customAttribute.array.set(offset_custom + 8, v3.getX());
							 customAttribute.array.set(offset_custom + 9, v3.getY());
							 customAttribute.array.set(offset_custom + 10, v3.getZ());
							 customAttribute.array.set(offset_custom + 11, v3.getW());

							 offset_custom += 12;

						 }

						 for ( int f = 0, fl = chunk_faces4.size(); f < fl; f ++ ) {

							 Vector4 value = (Vector4) customAttribute.getValue().get(chunk_faces4.get(f));

							 Vector4 v1 = value;
							 Vector4 v2 = value;
							 Vector4 v3 = value;
							 Vector4 v4 = value;

							 customAttribute.array.set(offset_custom, v1.getX());
							 customAttribute.array.set(offset_custom + 1, v1.getY());
							 customAttribute.array.set(offset_custom + 2, v1.getZ());
							 customAttribute.array.set(offset_custom + 3, v1.getW());

							 customAttribute.array.set(offset_custom + 4, v2.getX());
							 customAttribute.array.set(offset_custom + 5, v2.getY());
							 customAttribute.array.set(offset_custom + 6, v2.getZ());
							 customAttribute.array.set(offset_custom + 7, v2.getW());

							 customAttribute.array.set(offset_custom + 8, v3.getX());
							 customAttribute.array.set(offset_custom + 9, v3.getY());
							 customAttribute.array.set(offset_custom + 10, v3.getZ());
							 customAttribute.array.set(offset_custom + 11, v3.getW());

							 customAttribute.array.set(offset_custom + 12, v4.getX());
							 customAttribute.array.set(offset_custom + 13, v4.getY());
							 customAttribute.array.set(offset_custom + 14, v4.getZ());
							 customAttribute.array.set(offset_custom + 15, v4.getW());

							 offset_custom += 16;

						 }
					 }
				 }

				 gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), customAttribute.buffer );
				 gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), customAttribute.array, hint );
			 }
		 }

		 if ( dispose ) 
			 geometryGroup.dispose();
	}

	private void sortFacesByMaterial ( Geometry geometry ) 
	{
		Log.debug("Called sortFacesByMaterial() for geometry: " + geometry.getClass().getName());

		int numMorphTargets = geometry.getMorphTargets().size();
		int numMorphNormals = geometry.getMorphNormals().size();

		geometry.setGeometryGroups( new HashMap<String, GeometryGroup>() );

		Map<String, Integer> hash_map = new HashMap<String, Integer>();

		Log.debug("sortFacesByMaterial() geometry faces count: " + geometry.getFaces().size());

		for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) 
		{
			Face3 face = geometry.getFaces().get(f);

			int materialIndex = face.getMaterialIndex();

			String materialHash = ( materialIndex != -1 ) ? String.valueOf(materialIndex) : "-1";

			if(!hash_map.containsKey(materialHash))
				hash_map.put(materialHash, 0);

			String groupHash = materialHash + '_' + hash_map.get(materialHash);
			
			if(!geometry.getGeometryGroups().containsKey(groupHash))
				geometry.getGeometryGroups().put(groupHash, new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals));

			int vertices = face.getClass() == Face3.class ? 3 : 4;

			if ( geometry.getGeometryGroups().get(groupHash).vertices + vertices > 65535 ) 
			{
				hash_map.put(materialHash, hash_map.get(materialHash) + 1);
				groupHash = materialHash + '_' + hash_map.get( materialHash );

				if (!geometry.getGeometryGroups().containsKey(groupHash))
					geometry.getGeometryGroups().put(groupHash, new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals));
			}

			if ( face.getClass() == Face3.class )
				geometry.getGeometryGroups().get(groupHash).faces3.add( f );

			else
				geometry.getGeometryGroups().get(groupHash).faces4.add( f );

			geometry.getGeometryGroups().get(groupHash).vertices += vertices;
		}

		geometry.setGeometryGroupsList( new ArrayList<GeometryGroup>() );

		for ( GeometryGroup g : geometry.getGeometryGroups().values() ) 
		{
			g.setId(Mesh._geometryGroupCounter ++);
			geometry.getGeometryGroupsList().add( g );
		}
	}
}
