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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.Raycaster;
import org.parallax3d.parallax.graphics.materials.HasVertexColors;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.math.Ray;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector4;

import org.parallax3d.parallax.graphics.materials.HasSkinning;
import org.parallax3d.parallax.graphics.materials.HasWireframe;
import org.parallax3d.parallax.graphics.materials.MeshFaceMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;
import org.parallax3d.parallax.system.gl.enums.BeginMode;
import org.parallax3d.parallax.system.gl.enums.BufferTarget;
import org.parallax3d.parallax.system.gl.enums.BufferUsage;
import org.parallax3d.parallax.system.gl.enums.DrawElementsType;

/**
 * Base class for Mesh objects.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Mesh")
public class Mesh extends GeometryObject
{
	//	private Boolean overdraw;
	public Integer morphTargetBase = null;
	public List<Double> morphTargetInfluences;
	public List<Integer> morphTargetForcedOrder;
	private FastMap<Integer> morphTargetDictionary;

	public Float32Array __webglMorphTargetInfluences;

	private static MeshBasicMaterial defaultMaterial = new MeshBasicMaterial();
	static {
		defaultMaterial.setColor( new Color((int)(Math.random() * 0xffffff)) );
		defaultMaterial.setWireframe( true );
	};

	// Temporary variables
	static Matrix4 _inverseMatrix = new Matrix4();
	static Ray _ray = new Ray();
	static Sphere _sphere = new Sphere();

	static Vector3 _vA = new Vector3();
	static Vector3 _vB = new Vector3();
	static Vector3 _vC = new Vector3();

	public Mesh() {
		this(new Geometry());
	}

	public Mesh(AbstractGeometry geometry)
	{
		this(geometry, Mesh.defaultMaterial);
	}

	public Mesh(AbstractGeometry geometry, Material material)
	{
		super(geometry, material);

		this.updateMorphTargets();
	}

	public void updateMorphTargets() {

		if(this.getGeometry() instanceof BufferGeometry)
			return;

		if ( ((Geometry)this.getGeometry()).getMorphTargets() != null && !((Geometry)this.getGeometry()).getMorphTargets().isEmpty() ) {

			this.morphTargetBase = -1;
			this.morphTargetForcedOrder = new ArrayList<Integer>();
			this.morphTargetInfluences = new ArrayList<Double>();
			this.morphTargetDictionary = new FastMap<Integer>();

			List<Geometry.MorphTarget> morphTargets = ((Geometry)this.getGeometry()).getMorphTargets();
			for ( int m = 0, ml = ((Geometry)this.getGeometry()).getMorphTargets().size(); m < ml; m ++ ) {

				this.morphTargetInfluences.add( 0.0 );
				this.morphTargetDictionary.put(morphTargets.get(m).name, m);

			}

		}

	}

	public void raycast( Raycaster raycaster, List<Raycaster.Intersect> intersects) {

		// Checking boundingSphere distance to ray
		AbstractGeometry geometry = this.getGeometry();

		if ( geometry.getBoundingSphere() == null )
			geometry.computeBoundingSphere();

		_sphere.copy( geometry.getBoundingSphere() );
		_sphere.apply( this.matrixWorld );

		if ( !raycaster.getRay().isIntersectionSphere( _sphere ) )
		{
			return;
		}

		// Check boundingBox before continuing

		_inverseMatrix.getInverse( this.matrixWorld );
		_ray.copy( raycaster.getRay() ).apply( _inverseMatrix );

		if ( geometry.getBoundingBox() != null && !_ray.isIntersectionBox( geometry.getBoundingBox() ) )
		{
			return;
		}

		double precision = Raycaster.PRECISION;

		if ( geometry instanceof BufferGeometry )
		{
			Material material = this.getMaterial();

			if ( material == null ) return;

			BufferGeometry bGeometry = (BufferGeometry) geometry;

			if ( bGeometry.getAttribute("index") != null ) {

				Uint16Array indices = (Uint16Array)bGeometry.getAttribute("index").getArray();
				Float32Array positions = (Float32Array)bGeometry.getAttribute("position").getArray();
				List<BufferGeometry.DrawCall> offsets = bGeometry.getDrawcalls();

				if ( offsets.isEmpty() )
				{
					offsets.add(new BufferGeometry.DrawCall(0, indices.getLength(), 0));
				}

				for ( int oi = 0, ol = offsets.size(); oi < ol; ++oi )
				{
					int start = offsets.get( oi ).start;
					int count = offsets.get( oi ).count;
					int index = offsets.get( oi ).index;

					for ( int i = start, il = start + count; i < il; i += 3 )
					{

						int a = index + indices.get( i );
						int b = index + indices.get( i + 1 );
						int c = index + indices.get( i + 2 );

						_vA.fromArray( positions, a * 3 );
						_vB.fromArray( positions, b * 3 );
						_vC.fromArray( positions, c * 3 );

						Vector3 intersectionPoint;

						if ( material.getSides() == Material.SIDE.BACK )
						{
							intersectionPoint = _ray.intersectTriangle( _vC, _vB, _vA, true);
						}
						else
						{
							intersectionPoint = _ray.intersectTriangle( _vA, _vB, _vC, material.getSides() != Material.SIDE.DOUBLE );
						}

						if ( intersectionPoint == null ) continue;

						intersectionPoint.apply( this.matrixWorld );

						double distance = raycaster.getRay().getOrigin().distanceTo( intersectionPoint );

						if ( distance < precision || distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

						Raycaster.Intersect intersect = new Raycaster.Intersect();
						intersect.distance = distance;
						intersect.point = intersectionPoint;
						intersect.face = new Face3( a, b, c, Triangle.normal( _vA, _vB, _vC ) );
						intersect.object = this;
						intersects.add( intersect );

					}

				}

			} else {

				Float32Array positions = (Float32Array)bGeometry.getAttribute("position").getArray();

				for ( int i = 0, j = 0, il = positions.getLength(); i < il; i += 3, j += 9 ) {

					int a = i;
					int b = i + 1;
					int c = i + 2;

					_vA.fromArray( positions, j );
					_vB.fromArray( positions, j + 3 );
					_vC.fromArray( positions, j + 6 );

					Vector3 intersectionPoint;

					if ( material.getSides() == Material.SIDE.BACK ) {

						intersectionPoint = _ray.intersectTriangle( _vC, _vB, _vA, true, null );

					} else {

						intersectionPoint = _ray.intersectTriangle( _vA, _vB, _vC, material.getSides() != Material.SIDE.DOUBLE, null );

					}

					if ( intersectionPoint == null ) continue;

					intersectionPoint.apply( this.matrixWorld );

					double distance = raycaster.getRay().getOrigin().distanceTo( intersectionPoint );

					if ( distance < precision || distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

					Raycaster.Intersect intersect = new Raycaster.Intersect();
					intersect.distance = distance;
					intersect.point = intersectionPoint;
					intersect.face = new Face3( a, b, c, Triangle.normal( _vA, _vB, _vC ) );
					intersect.object = this;
					intersects.add( intersect );

				}

			}

		} else if ( geometry instanceof Geometry ) {

			boolean isFaceMaterial = this.getMaterial() instanceof MeshFaceMaterial;
			List<Material> objectMaterials = isFaceMaterial ? ((MeshFaceMaterial)this.getMaterial()).getMaterials() : null;

			Geometry aGeometry = (Geometry) geometry;

			List<Vector3> vertices = aGeometry.getVertices();

			for ( int f = 0, fl = aGeometry.getFaces().size(); f < fl; f ++ ) {

				Face3 face = aGeometry.getFaces().get( f );

				Material material = isFaceMaterial ? objectMaterials.get( face.getMaterialIndex() ) : this.getMaterial();

				if ( material == null ) continue;

				Vector3 a = vertices.get( face.getA() );
				Vector3 b = vertices.get( face.getB() );
				Vector3 c = vertices.get( face.getC() );

				if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets() == true ) {

					List<Geometry.MorphTarget> morphTargets = aGeometry.getMorphTargets();

					_vA.set( 0, 0, 0 );
					_vB.set( 0, 0, 0 );
					_vC.set( 0, 0, 0 );

					for ( int t = 0, tl = morphTargets.size(); t < tl; t ++ ) {

						double influence = this.morphTargetInfluences.get( t );

						if ( influence == 0 ) continue;

						List<Vector3> targets = morphTargets.get( t ).vertices;

						_vA.setX( _vA.getX() + ( targets.get( face.getA() ).getX() - a.getX() ) * influence );
						_vA.setY( _vA.getY() + ( targets.get( face.getA() ).getY() - a.getY() ) * influence );
						_vA.setZ( _vA.getZ() + ( targets.get( face.getA() ).getZ() - a.getZ() ) * influence );

						_vB.setX( _vB.getX() + ( targets.get( face.getB() ).getX() - b.getX() ) * influence );
						_vB.setY( _vB.getY() + ( targets.get( face.getB() ).getY() - b.getY() ) * influence );
						_vB.setZ( _vB.getZ() + ( targets.get( face.getB() ).getZ() - b.getZ() ) * influence );

						_vC.setX( _vC.getX() + ( targets.get( face.getC() ).getX() - c.getX() ) * influence );
						_vC.setY( _vC.getY() + ( targets.get( face.getC() ).getY() - c.getY() ) * influence );
						_vC.setZ( _vC.getZ() + ( targets.get( face.getC() ).getZ() - c.getZ() ) * influence );

					}

					_vA.add( a );
					_vB.add( b );
					_vC.add( c );

					a = _vA;
					b = _vB;
					c = _vC;

				}

				Vector3 intersectionPoint;

				if ( material.getSides() == Material.SIDE.BACK ) {

					intersectionPoint = _ray.intersectTriangle( c, b, a, true );

				} else {

					intersectionPoint = _ray.intersectTriangle( a, b, c, material.getSides() != Material.SIDE.DOUBLE );

				}

				if ( intersectionPoint == null ) continue;

				intersectionPoint.apply( this.matrixWorld );

				double distance = raycaster.getRay().getOrigin().distanceTo( intersectionPoint );

				if ( distance < precision || distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

				Raycaster.Intersect intersect = new Raycaster.Intersect();
				intersect.distance = distance;
				intersect.point = intersectionPoint;
				intersect.face = face;
				intersect.faceIndex = f;
				intersect.object = this;
				intersects.add( intersect );

			}

		}

	}

	public Mesh clone() {
		return clone(false);
	}

	public Mesh clone( boolean recursive ) {
		return clone(new Mesh( this.getGeometry(), this.getMaterial() ), recursive);
	}

	public Mesh clone( Mesh object, boolean recursive ) {

		super.clone(object, recursive);

		return object;

	}

//	public boolean getOverdraw()
//	{
//		return this.overdraw;
//	}
//
//	public void setOverdraw(boolean overdraw)
//	{
//		this.overdraw = overdraw;
//	}
//
//	/**
//	 * Get Morph Target Index by Name
//	 */
//	public int getMorphTargetIndexByName(String name)
//	{
//		if (this.morphTargetDictionary.containsKey(name))
//			return this.morphTargetDictionary.get(name);
//
//		Log.debug("Mesh.getMorphTargetIndexByName: morph target " + name
//				+ " does not exist. Returning 0.");
//		return 0;
//	}
//
//	public Integer getMorphTargetBase() {
//		return morphTargetBase;
//	}
//
//	public List<Double> getMorphTargetInfluences() {
//		return this.morphTargetInfluences;
//	}
//
//	public List<Integer> getMorphTargetForcedOrder() {
//		return this.morphTargetForcedOrder;
//	}

	@Override
	public void renderBuffer(GLRenderer renderer, GLGeometry geometryGroup, boolean updateBuffers)
	{
		GLRendererInfo info = renderer.getInfo();

		// wireframe
		if ( getMaterial() instanceof HasWireframe && ((HasWireframe)getMaterial()).isWireframe() )
		{
			setLineWidth(renderer.gl, ((HasWireframe) getMaterial()).getWireframeLineWidth());

			if ( updateBuffers )
				renderer.gl.glBindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.__webglLineBuffer);

			renderer.gl.glDrawElements(BeginMode.LINES.getValue(), geometryGroup.__webglLineCount, DrawElementsType.UNSIGNED_SHORT.getValue(), 0);

			// triangles

		}
		else
		{
			if ( updateBuffers )
				renderer.gl.glBindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.__webglFaceBuffer);

			renderer.gl.glDrawElements(BeginMode.TRIANGLES.getValue(), geometryGroup.__webglFaceCount, DrawElementsType.UNSIGNED_SHORT.getValue(), 0);
		}

		info.getRender().calls ++;
		info.getRender().vertices += geometryGroup.__webglFaceCount;
		info.getRender().faces += geometryGroup.__webglFaceCount / 3;
	}

	//	/*
//	 * Returns geometry quantities
//	 */
//	@Override
//	public void initBuffer(WebGLRenderer renderer)
//	{
//		WebGlRendererInfo info = renderer.getInfo();
//
//		Geometry geometry = this.getGeometry();
//
//		if(geometryBuffer != null)
//		{
//			createBuffers(renderer, geometryBuffer );
//		}
//		else if(geometry instanceof Geometry)
//		{
//			Log.debug("addObject() geometry.geometryGroups is null: " + ( geometry.getGeometryGroups() == null ));
//			if ( geometry.getGeometryGroups() == null )
//				sortFacesByMaterial( this.getGeometry() );
//
//			// create separate VBOs per geometry chunk
//			for ( GeometryGroup geometryGroup : geometry.getGeometryGroups() )
//			{
//				// initialise VBO on the first access
//				if ( geometryGroup.__webglVertexBuffer == null )
//				{
//					createBuffers(renderer, geometryGroup );
//					initBuffers(renderer.getGL(), geometryGroup );
//					info.getMemory().geometries++;
//
//					geometry.setVerticesNeedUpdate(true);
//					geometry.setMorphTargetsNeedUpdate(true);
//					geometry.setElementsNeedUpdate(true);
//					geometry.setUvsNeedUpdate(true);
//					geometry.setNormalsNeedUpdate(true);
//					geometry.setTangentsNeedUpdate(true);
//					geometry.setColorsNeedUpdate(true);
//				}
//			}
//		}
//
//	}
//
	// initMeshBuffers
	public void initBuffers(GL20 gl, GeometryGroup geometryGroup)
	{
		Geometry geometry = (Geometry) this.getGeometry();

		List<Integer> faces3 = geometryGroup.getFaces3();

		int nvertices = faces3.size() * 3 ;
		int ntris = faces3.size() * 1;
		int nlines = faces3.size() * 3;

		Material material = Material.getBufferMaterial(this, geometryGroup);

		geometryGroup.__vertexArray = Float32Array.create(nvertices * 3);
		geometryGroup.__normalArray =  Float32Array.create(nvertices * 3);
		geometryGroup.__colorArray = Float32Array.create(nvertices * 3);
		geometryGroup.__uvArray = Float32Array.create(nvertices * 2);

		if ( geometry.getFaceVertexUvs().size() > 1 ) {

			geometryGroup.__uv2Array = Float32Array.create(nvertices * 2);

		}

		if ( geometry.isHasTangents() ) {

			geometryGroup.__tangentArray = Float32Array.create(nvertices * 4);

		}

		if ( !geometry.getSkinWeights().isEmpty() && !geometry.getSkinIndices().isEmpty() ) {

			geometryGroup.__skinIndexArray = Float32Array.create(nvertices * 4);
			geometryGroup.__skinWeightArray = Float32Array.create(nvertices * 4);

		}

//		Class UintArray = WebGLExtensions.get( gl, WebGLExtensions.Id.OES_element_index_uint ) != null && ntris > 21845
//				? Uint32Array.class : Uint16Array; // 65535 / 3

//		geometryGroup.__typeArray = UintArray;
		geometryGroup.__faceArray = Uint16Array.create(ntris * 3);
		geometryGroup.__lineArray = Uint16Array.create(nlines * 2);

		if ( geometryGroup.getNumMorphTargets() > 0 ) {

			geometryGroup.__morphTargetsArrays = new ArrayList<Float32Array>();

			for ( int m = 0, ml = geometryGroup.getNumMorphTargets(); m < ml; m ++ ) {

				geometryGroup.__morphTargetsArrays.add( Float32Array.create(nvertices * 3) );

			}

		}

		if ( geometryGroup.getNumMorphNormals() > 0 ) {

			geometryGroup.__morphNormalsArrays  = new ArrayList<Float32Array>();

			for ( int m = 0, ml = geometryGroup.getNumMorphNormals(); m < ml; m ++ ) {

				geometryGroup.__morphNormalsArrays.add( Float32Array.create(nvertices * 3) );

			}

		}

		geometryGroup.__webglFaceCount = ntris * 3;
		geometryGroup.__webglLineCount = nlines * 2;


		// custom attributes

		FastMap<Attribute> attributes = material.getShader().getAttributes();

		if (attributes != null)
		{

			if (geometryGroup.__webglCustomAttributesList == null)
				geometryGroup.__webglCustomAttributesList = new ArrayList<Attribute>();


			for (String a : attributes.keySet())
			{
				Attribute originalAttribute = attributes.get(a);

				// Do a shallow copy of the attribute object so different geometryGroup chunks use different
				// attribute buffers which are correctly indexed in the setMeshBuffers function

				Attribute attribute = originalAttribute.clone();

				if (!attribute.__webglInitialized || attribute.createUniqueBuffers)
				{
					attribute.__webglInitialized = true;

					int size = 1; // "f" and "i"

					if (attribute.type == Attribute.TYPE.V2)
						size = 2;
					else if (attribute.type == Attribute.TYPE.V3)
						size = 3;
					else if (attribute.type == Attribute.TYPE.V4)
						size = 4;
					else if (attribute.type == Attribute.TYPE.C)
						size = 3;

					attribute.size = size;

					attribute.array = Float32Array.create(nvertices * size);

					attribute.buffer = gl.glGenBuffer();
					attribute.belongsToAttribute = a;

					originalAttribute.needsUpdate = true;
				}

				geometryGroup.__webglCustomAttributesList.add(attribute);
			}

		}

		geometryGroup.__inittedArrays = true;
	}

	// createMeshBuffers
	public void createBuffers(GLRenderer renderer, GeometryGroup geometryGroup)
	{
		GLRendererInfo info = renderer.getInfo();

		geometryGroup.__webglVertexBuffer = renderer.gl.glGenBuffer();

		geometryGroup.__webglNormalBuffer = renderer.gl.glGenBuffer();
		geometryGroup.__webglTangentBuffer = renderer.gl.glGenBuffer();
		geometryGroup.__webglColorBuffer = renderer.gl.glGenBuffer();
		geometryGroup.__webglUVBuffer = renderer.gl.glGenBuffer();
		geometryGroup.__webglUV2Buffer = renderer.gl.glGenBuffer();

		geometryGroup.__webglSkinIndicesBuffer = renderer.gl.glGenBuffer();
		geometryGroup.__webglSkinWeightsBuffer = renderer.gl.glGenBuffer();

		geometryGroup.__webglFaceBuffer = renderer.gl.glGenBuffer();
		geometryGroup.__webglLineBuffer = renderer.gl.glGenBuffer();

		if (geometryGroup.getNumMorphTargets() != 0) {
			geometryGroup.__webglMorphTargetsBuffers = new ArrayList<Integer>();

			for (int m = 0; m < geometryGroup.getNumMorphTargets(); m++) {
				geometryGroup.__webglMorphTargetsBuffers.add(renderer.gl.glGenBuffer());
			}
		}

		if (geometryGroup.getNumMorphNormals() != 0) {
			geometryGroup.__webglMorphNormalsBuffers = new ArrayList<Integer>();

			for (int m = 0; m < geometryGroup.getNumMorphNormals(); m++) {
				geometryGroup.__webglMorphNormalsBuffers.add(renderer.gl.glGenBuffer());
			}
		}

		info.getMemory().geometries ++;
	}

//	@Override
//	public void setBuffer(WebGLRenderer renderer)
//	{
//		WebGLRenderingContext gl = renderer.getGL();
//
//		if ( geometryBuffer != null )
//		{
//			if ( geometryBuffer.isVerticesNeedUpdate() || geometryBuffer.isElementsNeedUpdate() ||
//				geometryBuffer.isUvsNeedUpdate() || geometryBuffer.isNormalsNeedUpdate() ||
//				geometryBuffer.isColorsNeedUpdate() || geometryBuffer.isTangentsNeedUpdate() )
//			{
//				((BufferGeometry)geometryBuffer).setDirectBuffers( renderer.getGL(), BufferUsage.DYNAMIC_DRAW, !geometryBuffer.isDynamic() );
//			}
//
//			geometryBuffer.setVerticesNeedUpdate(false);
//			geometryBuffer.setElementsNeedUpdate(false);
//			geometryBuffer.setUvsNeedUpdate(false);
//			geometryBuffer.setNormalsNeedUpdate(false);
//			geometryBuffer.setColorsNeedUpdate(false);
//			geometryBuffer.setTangentsNeedUpdate(false);
//		}
//		else
//		{
//			// check all geometry groups
//
//			for( GeometryGroup geometryGroup : geometry.getGeometryGroups() )
//			{
//				// TODO: try to make object's material
//				Material material = Material.getBufferMaterial( this, geometryGroup );
//
//				boolean areCustomAttributesDirty = material.getShader().areCustomAttributesDirty();
//				if ( geometry.isVerticesNeedUpdate()
//						|| geometry.isMorphTargetsNeedUpdate()
//						|| geometry.isElementsNeedUpdate()
//						|| geometry.isUvsNeedUpdate()
//						|| geometry.isNormalsNeedUpdate()
//						|| geometry.isColorsNeedUpdate()
//						|| geometry.isTangentsNeedUpdate()
//						|| areCustomAttributesDirty
//				) {
//					setBuffers( gl, geometryGroup, BufferUsage.DYNAMIC_DRAW, material);
//					material.getShader().clearCustomAttributes();
//				}
//			}
//
//			geometry.setVerticesNeedUpdate(false);
//			geometry.setMorphTargetsNeedUpdate(false);
//			geometry.setElementsNeedUpdate(false);
//			geometry.setUvsNeedUpdate(false);
//			geometry.setNormalsNeedUpdate(false);
//			geometry.setColorsNeedUpdate(false);
//			geometry.setTangentsNeedUpdate(false);
//		}
//	}

	// setMeshBuffers
	public void setBuffers(GL20 gl, GeometryGroup geometryGroup, BufferUsage hint, boolean dispose, Material material)
	{
		//Log.debug("Called Mesh.setBuffers() - material=" + material.getId() + ", " + material.getClass().getName());

		if ( ! geometryGroup.__inittedArrays )
			return;

		boolean needsSmoothNormals = material.materialNeedsSmoothNormals();

		int vertexIndex = 0,

				offset = 0,
				offset_uv = 0,
				offset_uv2 = 0,
				offset_face = 0,
				offset_normal = 0,
				offset_tangent = 0,
				offset_line = 0,
				offset_color = 0,
				offset_skin = 0,
				offset_morphTarget,
				offset_custom;

		Float32Array vertexArray = geometryGroup.__vertexArray,
				uvArray = geometryGroup.__uvArray,
				uv2Array = geometryGroup.__uv2Array,
				normalArray = geometryGroup.__normalArray,
				tangentArray = geometryGroup.__tangentArray,
				colorArray = geometryGroup.__colorArray,

				skinIndexArray = geometryGroup.__skinIndexArray,
				skinWeightArray = geometryGroup.__skinWeightArray;

		List<Float32Array> morphTargetsArrays = geometryGroup.__morphTargetsArrays,
				morphNormalsArrays = geometryGroup.__morphNormalsArrays;

		List<Attribute> customAttributes = geometryGroup.__webglCustomAttributesList;

		Uint16Array faceArray = geometryGroup.__faceArray,
				lineArray = geometryGroup.__lineArray;

		Geometry geometry = (Geometry)this.getGeometry(); // this is shared for all chunks

		boolean dirtyVertices = geometry.isVerticesNeedUpdate(),
				dirtyElements = geometry.isElementsNeedUpdate(),
				dirtyUvs = geometry.isUvsNeedUpdate(),
				dirtyNormals = geometry.isNormalsNeedUpdate(),
				dirtyTangents = geometry.isTangentsNeedUpdate(),
				dirtyColors = geometry.isColorsNeedUpdate(),
				dirtyMorphTargets = geometry.isMorphTargetsNeedUpdate();

		List<Vector3> vertices = geometry.getVertices();
		List<Integer> chunk_faces3 = geometryGroup.getFaces3();
		List<Face3> obj_faces = geometry.getFaces();

		List<List<Vector2>> obj_uvs  = geometry.getFaceVertexUvs().get( 0 );

		List<List<Vector2>>	obj_uvs2 = null;
		if(geometry.getFaceVertexUvs().size() > 1)
			obj_uvs2 = geometry.getFaceVertexUvs().get( 1 );

		List<Vector4> obj_skinIndices = geometry.getSkinIndices(),
				obj_skinWeights = geometry.getSkinWeights();

		List<Geometry.MorphTarget> morphTargets = geometry.getMorphTargets();
		List<Geometry.MorphNormal> morphNormals = geometry.getMorphNormals();

		if ( dirtyVertices )
		{
			for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
			{
				Face3 face = obj_faces.get( chunk_faces3.get( f ) );

				Vector3 v1 = vertices.get( face.getA() );
				Vector3 v2 = vertices.get( face.getB() );
				Vector3 v3 = vertices.get( face.getC() );

				vertexArray.set(offset, v1.getX());
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

			 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglVertexBuffer);
			 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), vertexArray.getByteLength(), vertexArray.getTypedBuffer(), hint.getValue() );
		}

		if ( dirtyMorphTargets )
		{

			for ( int vk = 0, vkl = morphTargets.size(); vk < vkl; vk ++ )
			{
				offset_morphTarget = 0;

				for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
				{
					int chf = chunk_faces3.get( f );
					Face3 face = obj_faces.get( chf );

					// morph positions

					Vector3 v1 = morphTargets.get( vk ).vertices.get( face.getA() );
					Vector3 v2 = morphTargets.get( vk ).vertices.get( face.getB() );
					Vector3 v3 = morphTargets.get( vk ).vertices.get( face.getC() );

					Float32Array vka = morphTargetsArrays.get(vk);

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

						Float32Array nka = morphNormalsArrays.get( vk );

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

				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphTargetsBuffers.get(vk));
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), morphTargetsArrays.get(vk).getByteLength(), morphTargetsArrays.get( vk ).getTypedBuffer(), hint.getValue() );

				 if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() )
				 {
					 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphNormalsBuffers.get(vk));
					 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), morphNormalsArrays.get(vk).getByteLength(), morphNormalsArrays.get( vk ).getTypedBuffer(), hint.getValue() );
				 }
			 }
		}

		if ( !obj_skinWeights.isEmpty() )
		{
			for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
			{
				Face3 face = obj_faces.get( chunk_faces3.get( f ) );

				// weights

				Vector4 sw1 = obj_skinWeights.get( face.getA() );
				Vector4 sw2 = obj_skinWeights.get( face.getB() );
				Vector4 sw3 = obj_skinWeights.get( face.getC() );

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

				Vector4 si1 = obj_skinIndices.get(face.getA());
				Vector4 si2 = obj_skinIndices.get(face.getB());
				Vector4 si3 = obj_skinIndices.get(face.getC());

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

				offset_skin += 12;

			}

			 if ( offset_skin > 0 )
			 {
				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglSkinIndicesBuffer);
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), skinIndexArray.getByteLength(), skinIndexArray.getTypedBuffer(), hint.getValue());

				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglSkinWeightsBuffer);
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), skinWeightArray.getByteLength(), skinWeightArray.getTypedBuffer(), hint.getValue() );
			 }
		 }

		if ( dirtyColors )
		{
			for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
			{
				Face3 face = obj_faces.get(chunk_faces3.get(f));

				List<Color> vertexColors = face.getVertexColors();
				Color faceColor = face.getColor();
				Color c1, c2, c3;

				if ( vertexColors.size() == 3 && ((HasVertexColors)material).isVertexColors() == Material.COLORS.VERTEX)
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

			 if ( offset_color > 0 )
			 {
				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglColorBuffer);
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), colorArray.getByteLength(),  colorArray.getTypedBuffer(), hint.getValue() );
			 }
		 }

		if ( dirtyTangents && geometry.isHasTangents())
		{
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

			 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglTangentBuffer);
			 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), tangentArray.getByteLength(), tangentArray.getTypedBuffer(), hint.getValue() );

		}

		if ( dirtyNormals )
		{

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

						normalArray.set(offset_normal, vn.getX());
						normalArray.set(offset_normal + 1, vn.getY());
						normalArray.set(offset_normal + 2, vn.getZ());

						offset_normal += 3;
					}

				}
				else
				{

					for ( int i = 0; i < 3; i ++ )
					{

						normalArray.set(offset_normal, faceNormal.getX());
						normalArray.set(offset_normal + 1, faceNormal.getY());
						normalArray.set(offset_normal + 2, faceNormal.getZ());

						offset_normal += 3;
					}
				}
			}

			 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglNormalBuffer);
			 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), normalArray.getByteLength(),  normalArray.getTypedBuffer(), hint.getValue() );

		}

		if ( dirtyUvs && obj_uvs != null && !obj_uvs.isEmpty() )
		{
			for (int  f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
			{

				int fi = chunk_faces3.get(f);

				if(obj_uvs.size() <= fi) {
					continue;
				}

				List<Vector2> uv = obj_uvs.get(fi);

				if ( uv == null ) {
					continue;
				}

				for ( int i = 0; i < 3; i ++ ) {

					Vector2 uvi = uv.get(i);

					uvArray.set(offset_uv, uvi.getX());
					uvArray.set(offset_uv + 1, uvi.getY());

					offset_uv += 2;
				}
			}

			 if ( offset_uv > 0 )
			 {
				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglUVBuffer);
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), uvArray.getByteLength(), uvArray.getTypedBuffer(), hint.getValue() );
			 }
		 }

		if (dirtyUvs && obj_uvs2 != null && !obj_uvs2.isEmpty() )
		{

			for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
			{
				int fi = chunk_faces3.get(f);

				List<Vector2> uv2 = obj_uvs2.get(fi);

				if ( uv2 == null ) {
					continue;
				}

				for ( int i = 0; i < 3; i ++ )
				{
					Vector2 uv2i = uv2.get(i);

					uv2Array.set(offset_uv2, uv2i.getX());
					uv2Array.set(offset_uv2 + 1, uv2i.getY());

					offset_uv2 += 2;
				}
			}

			 if ( offset_uv2 > 0 )
			 {
				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryGroup.__webglUV2Buffer);
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), uv2Array.getByteLength(), uv2Array.getTypedBuffer(), hint.getValue() );
			 }
		 }

		if (  dirtyElements )
		{
			for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
			{
				faceArray.set(offset_face, vertexIndex);
				faceArray.set(offset_face + 1, vertexIndex + 1);
				faceArray.set(offset_face + 2, vertexIndex + 2);

				offset_face += 3;

				lineArray.set(offset_line, vertexIndex);
				lineArray.set(offset_line + 1, vertexIndex + 1);

				lineArray.set(offset_line + 2, vertexIndex);
				lineArray.set(offset_line + 3, vertexIndex + 2);

				lineArray.set(offset_line + 4, vertexIndex + 1);
				lineArray.set(offset_line + 5, vertexIndex + 2);

				offset_line += 6;

				 vertexIndex += 3;
			 }

			 gl.glBindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.__webglFaceBuffer);
			 gl.glBufferData(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), faceArray.getByteLength(), faceArray.getTypedBuffer(), hint.getValue());

			 gl.glBindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), geometryGroup.__webglLineBuffer);
			 gl.glBufferData(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), lineArray.getByteLength(), lineArray.getTypedBuffer(), hint.getValue() );

		}

		if ( customAttributes != null )
		{
			for ( int i = 0, il = customAttributes.size(); i < il; i ++ )
			{
				Attribute customAttribute = geometryGroup.__webglCustomAttributesList.get(i);

				if ( ! customAttribute.__original.needsUpdate ) {
					continue;
				}

				offset_custom = 0;

				if ( customAttribute.size == 1 )
				{
					if ( customAttribute.getBoundTo() == null
							|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES )
					{
						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
							Face3 face = obj_faces.get(chunk_faces3.get(f));

							customAttribute.array.set(offset_custom, (Double) customAttribute.getValue().get(face.getA()));
							customAttribute.array.set(offset_custom + 1, (Double) customAttribute.getValue().get(face.getB()));
							customAttribute.array.set(offset_custom + 2, (Double) customAttribute.getValue().get(face.getC()));

							offset_custom += 3;
						}
					}
					else if ( customAttribute.getBoundTo() == Attribute.BOUND_TO.FACES )
					{
						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
							double value = (Double) customAttribute.getValue().get(chunk_faces3.get(f));

							customAttribute.array.set(offset_custom, value);
							customAttribute.array.set(offset_custom + 1, value);
							customAttribute.array.set(offset_custom + 2, value);

							offset_custom += 3;

						}
					}
				}
				else if ( customAttribute.size == 2 )
				{
					if ( customAttribute.getBoundTo() == null
							|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES )
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
					}
					else if ( customAttribute.getBoundTo() == Attribute.BOUND_TO.FACES )
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

					}

				}
				else if ( customAttribute.size == 3 )
				{
					if ( customAttribute.getBoundTo() == null
							|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES )
					{

						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ ) {

							Face3 face = obj_faces.get(chunk_faces3.get(f));

							if(customAttribute.type == Attribute.TYPE.C) {
								Color v1 = (Color) customAttribute.getValue().get(face.getA());
								Color v2 = (Color) customAttribute.getValue().get(face.getB());
								Color v3 = (Color) customAttribute.getValue().get(face.getC());

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

					}
					else if ( customAttribute.getBoundTo() == Attribute.BOUND_TO.FACES )
					{

						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
							if(customAttribute.type == Attribute.TYPE.C)
							{
								Color value = (Color) customAttribute.getValue().get(chunk_faces3.get(f));
								Color v1 = value;
								Color v2 = value;
								Color v3 = value;

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
					}
					else if ( customAttribute.getBoundTo() == Attribute.BOUND_TO.FACE_VERTICES)
					{
						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
							if(customAttribute.type == Attribute.TYPE.C)
							{
								List<Color> value = (List<Color>) customAttribute.getValue().get(chunk_faces3.get(f));
								Color v1 = value.get(0);
								Color v2 = value.get(1);
								Color v3 = value.get(2);

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
								List<Vector3> value = (List<Vector3>) customAttribute.getValue().get(chunk_faces3.get(f));
								Vector3 v1 = value.get(0);
								Vector3 v2 = value.get(1);
								Vector3 v3 = value.get(2);

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
					}
				}
				else if ( customAttribute.size == 4 )
				{
					if ( customAttribute.getBoundTo() == null
							|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES )
					{

						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
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

					}
					else if ( customAttribute.getBoundTo() == Attribute.BOUND_TO.FACES)
					{
						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
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
					}
					else if ( customAttribute.getBoundTo() == Attribute.BOUND_TO.FACE_VERTICES )
					{
						for ( int f = 0, fl = chunk_faces3.size(); f < fl; f ++ )
						{
							List<Vector4> value = (List<Vector4>) customAttribute.getValue().get(chunk_faces3.get(f));

							Vector4 v1 = value.get(0);
							Vector4 v2 = value.get(1);
							Vector4 v3 = value.get(2);

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
					}
				}

				 gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), customAttribute.buffer);
				 gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), customAttribute.array.getByteLength(), customAttribute.array.getTypedBuffer(), hint.getValue() );
			 }
		 }

		if ( dispose )
			geometryGroup.dispose();
	}

	@Override
	public void deleteBuffers(GLRenderer renderer)
	{
//		for ( GeometryGroup geometryGroup : ((Geometry)getGeometry()).getGeometryGroupsCache().values() )
//		{
//			renderer.getGL().deleteBuffer( geometryGroup.__webglVertexBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglNormalBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglTangentBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglColorBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglUVBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglUV2Buffer );
//
//			renderer.getGL().deleteBuffer( geometryGroup.__webglSkinIndicesBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglSkinWeightsBuffer );
//
//			renderer.getGL().deleteBuffer( geometryGroup.__webglFaceBuffer );
//			renderer.getGL().deleteBuffer( geometryGroup.__webglLineBuffer );
//
//			if ( geometryGroup.numMorphTargets != 0)
//			{
//				for ( int m = 0; m < geometryGroup.numMorphTargets; m ++ )
//				{
//					renderer.getGL().deleteBuffer( geometryGroup.__webglMorphTargetsBuffers.get( m ) );
//				}
//			}
//
//			if ( geometryGroup.numMorphNormals != 0 )
//			{
//				for ( int m = 0; m <  geometryGroup.numMorphNormals; m ++ )
//				{
//					renderer.getGL().deleteBuffer( geometryGroup.__webglMorphNormalsBuffers.get( m ) );
//				}
//			}
//
//
//			if ( geometryGroup.__webglCustomAttributesList != null)
//			{
//				for ( Attribute att : geometryGroup.__webglCustomAttributesList )
//				{
//					renderer.getGL().deleteBuffer( att.buffer );
//				}
//			}
//
//			renderer.getInfo().getMemory().geometries --;
//		}
	}
//
//	private void sortFacesByMaterial ( Geometry geometry )
//	{
//		Log.debug("Called sortFacesByMaterial() for geometry: " + geometry.getClass().getName());
//
//		int numMorphTargets = geometry.getMorphTargets().size();
//		int numMorphNormals = geometry.getMorphNormals().size();
//
//		geometry.setGeometryGroupsCache( new HashMap<String, GeometryGroup>() );
//
//		Map<String, Integer> hash_map = GWT.isScript() ?
//				new FastMap<Integer>() : new HashMap<String, Integer>();
//
//		Log.debug("sortFacesByMaterial() geometry faces count: " + geometry.getFaces().size());
//
//		for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ )
//		{
//			Face3 face = geometry.getFaces().get(f);
//
//			int materialIndex = face.getMaterialIndex();
//
//			String materialHash = ( materialIndex != -1 ) ? String.valueOf(materialIndex) : "-1";
//
//			if(!hash_map.containsKey(materialHash))
//				hash_map.put(materialHash, 0);
//
//			String groupHash = materialHash + '_' + hash_map.get(materialHash);
//
//			if( ! geometry.getGeometryGroupsCache().containsKey(groupHash))
//				geometry.getGeometryGroupsCache().put(groupHash, new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals));
//
//			int vertices = face.getClass() == Face3.class ? 3 : 4;
//
//			if ( geometry.getGeometryGroupsCache().get(groupHash).vertices + vertices > 65535 )
//			{
//				hash_map.put(materialHash, hash_map.get(materialHash) + 1);
//				groupHash = materialHash + '_' + hash_map.get( materialHash );
//
//				if ( ! geometry.getGeometryGroupsCache().containsKey(groupHash))
//					geometry.getGeometryGroupsCache().put(groupHash, new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals));
//			}
//
//			if ( face.getClass() == Face3.class )
//				geometry.getGeometryGroupsCache().get(groupHash).faces3.add( f );
//
//			else
//				geometry.getGeometryGroupsCache().get(groupHash).faces4.add( f );
//
//			geometry.getGeometryGroupsCache().get(groupHash).vertices += vertices;
//		}
//
//		geometry.setGeometryGroups( new ArrayList<GeometryGroup>() );
//
//		for ( GeometryGroup g : geometry.getGeometryGroupsCache().values() )
//		{
//			geometry.getGeometryGroups().add( g );
//		}
//	}
}
