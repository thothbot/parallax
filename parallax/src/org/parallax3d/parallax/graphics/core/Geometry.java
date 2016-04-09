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

package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.graphics.core.geometry.Group;
import org.parallax3d.parallax.graphics.core.geometry.MorphNormal;
import org.parallax3d.parallax.graphics.core.geometry.MorphTarget;
import org.parallax3d.parallax.graphics.core.geometry.VertextNormal;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.Points;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

import java.util.*;

/**
 * Base class for geometries. A geometry holds all data necessary to describe a 3D model.
 * 
 * <pre>
 * {@code
 * Geometry geometry = new Geometry();
 * 
 * geometry.getVertices().add( new Vector3( -10, 10, 0 ) ); 
 * geometry.getVertices().add( new Vector3( -10, -10, 0 ) );
 * geometry.getVertices().add( new Vector3( 10, -10, 0 ) );  
 * geometry.getFaces().add( new Face3( 0, 1, 2 ) );  
 * 
 * geometry.computeBoundingSphere();
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Geometry")
public class Geometry extends AbstractGeometry
{
	// Array of vertices.
	List<Vector3> vertices = new ArrayList<>();
	// one-to-one vertex colors, used in ParticleSystem, Line and Ribbon
	List<Color> colors = new ArrayList<>();

	List<Face3> faces = new ArrayList<>();

	List<List<List<Vector2>>> faceVertexUvs = new ArrayList<>();

	List<MorphTarget> morphTargets = new ArrayList<>();
	List<MorphNormal> morphNormals = new ArrayList<>();

	List<Vector4> skinWeights = new ArrayList<>();
	List<Vector4> skinIndices = new ArrayList<>();

	/*
	 * An array containing distances between vertices for Line geometries. 
	 * This is required for LinePieces/LineDashedMaterial to render correctly.
	 * Line distances can also be generated with computeLineDistances.
	 */
	List<Double> lineDistances = new ArrayList<>();

	List<GBone> bones;

	public DirectGeometry __directGeometry;

	public Geometry() {
		this.faceVertexUvs.add(new ArrayList<List<Vector2>>());
	}

	/**
	 * Gets the List of skinning weights, matching number and order of vertices.
	 */
	public List<Vector4> getSkinWeights() {
		return this.skinWeights;
	}

	/**
	 * Gets the List of skinning indices, matching number and order of vertices.
	 */
	public List<Vector4> getSkinIndices() {
		return this.skinIndices;
	}

	public List<Double> getLineDistances() {
		return lineDistances;
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	/**
	 * Gets List of vertex {@link Color}s, matching number and order of vertices.
	 * <p>
	 * Used in {@link Points}, {@link Line}.<br>
	 * {@link Mesh}es use per-face-use-of-vertex colors embedded directly in faces.
	 * To signal an update in this array, Geometry.colorsNeedUpdate needs to be set to true.
	 */
	public List<Color> getColors() {
		return colors;
	}

	public void setFaces(List<Face3> faces) {
		this.faces = faces;
	}

	/**
	 * Gets the List of triangles: {@link Face3}. The array of faces describe how each vertex in the model is connected with each other.
	 * To signal an update in this array, Geometry.elementsNeedUpdate needs to be set to true.
	 */
	public List<Face3> getFaces() {
		return faces;
	}

	public void setVertices(List<Vector3> vertices)
	{
		this.vertices = vertices;
	}

	/**
	 * Gets List of {@link Vector3}. The array of vertices holds every position of points in the model.
	 * To signal an update in this array, Geometry.verticesNeedUpdate needs to be set to true.
	 */
	public List<Vector3> getVertices()
	{
		return vertices;
	}

	/**
	 * Gets the List of {@link MorphTarget}.
	 * Morph vertices match number and order of primary vertices.
	 */
	public List<MorphTarget> getMorphTargets() {
		return morphTargets;
	}

	/**
	 * Get the List of {@link MorphNormal}.
	 * Morph normals have similar structure as morph targets
	 * @return
	 */
	public List<MorphNormal> getMorphNormals() {
		return morphNormals;
	}

	/**
	 * Gets the List of face UVMapping layers.
	 * Each UVMapping layer is an array of UVs matching the order and number of vertices in faces.
	 * To signal an update in this array, Geometry.uvsNeedUpdate needs to be set to true.
	 */
	public List<List<List<Vector2>>> getFaceVertexUvs(){
		return this.faceVertexUvs;
	}

	public void setFaceVertexUvs(List<List<List<Vector2>>> faceVertexUvs) {
		this.faceVertexUvs = faceVertexUvs;
	}

	public static class GBone {
		public Float32Array pos;
		public Float32Array rotq;
		public Float32Array scl;
		public int parent = -1;
		public String name;
	}

	public List<GBone> getBones() {
		return bones;
	}

	public void setBones(List<GBone> bones) {
		this.bones = bones;
	}

	/**
	 * Bakes matrix transform directly into vertex coordinates.
	 */
	public void applyMatrix(Matrix4 matrix)
	{
		Matrix3 normalMatrix = new Matrix3().getNormalMatrix( matrix );

		for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) {

			Vector3 vertex = this.vertices.get( i );
			vertex.apply( matrix );

		}

		for ( int i = 0, il = this.faces.size(); i < il; i ++ ) {

			Face3 face = this.faces.get( i );
			face.normal.apply( normalMatrix ).normalize();

			for ( int j = 0, jl = face.vertexNormals.size(); j < jl; j ++ ) {

				face.vertexNormals.get( j ).apply( normalMatrix ).normalize();

			}

		}

		if(this.boundingBox != null)
			this.computeBoundingBox();

		if(this.boundingSphere != null)
			this.computeBoundingSphere();
	}

	static final Matrix4 m1 = new Matrix4();
	public Geometry rotateX( double angle ) {

		m1.makeRotationX( angle );

		this.applyMatrix( m1 );

		return this;

	}

	public Geometry rotateY( double angle ) {

		m1.makeRotationY( angle );

		this.applyMatrix( m1 );

		return this;

	}

	public Geometry rotateZ( double angle ) {

		m1.makeRotationZ( angle );

		this.applyMatrix( m1 );

		return this;

	}

	public Geometry translate( double x, double y, double z )
	{
		m1.makeTranslation( x, y, z );

		this.applyMatrix( m1 );

		return this;

	}

	public Geometry scale( double x, double y, double z )
	{

		m1.makeScale( x, y, z );

		this.applyMatrix( m1 );

		return this;

	}

	static final Object3D obj = new Object3D();
	public Geometry lookAt( Vector3 vector ) {

		obj.lookAt( vector );

		obj.updateMatrix();

		this.applyMatrix( obj.matrix );

		return this;
	}

	public Geometry fromBufferGeometry( BufferGeometry geometry )
	{
		Uint32Array indices = geometry.getIndex() != null
				? (Uint32Array) geometry.getIndex().getArray() : null;

		Float32Array positions = (Float32Array)geometry.getAttribute("position").getArray();
		Float32Array normals = geometry.getAttribute("normal") != null
				? (Float32Array)geometry.getAttribute("normal").getArray() : null;
		Float32Array colors = geometry.getAttribute("color") != null
				? (Float32Array)geometry.getAttribute("color").getArray() : null;
		Float32Array uvs = geometry.getAttribute("uv") != null
				? (Float32Array)geometry.getAttribute("uv").getArray() : null;
		Float32Array uvs2 = geometry.getAttribute("uvs2") != null
				? (Float32Array)geometry.getAttribute("uvs2").getArray() : null;

		if ( uvs2 != null ) this.faceVertexUvs.add(1, new ArrayList<List<Vector2>>());

		List<Vector3> tempNormals = new ArrayList<>();
		List<Vector2> tempUVs = new ArrayList<>();
		List<Vector2> tempUVs2 = new ArrayList<>();

		for ( int i = 0, j = 0; i < positions.getLength(); i += 3, j += 2 ) {

			this.vertices.add( new Vector3( positions.get( i ), positions.get( i + 1 ), positions.get( i + 2 ) ) );

			if ( normals != null ) {

				tempNormals.add( new Vector3( normals.get( i ), normals.get( i + 1 ), normals.get( i + 2 ) ) );

			}

			if ( colors != null ) {

				Color color = new Color();
				this.colors.add( color.setRGB( colors.get( i ), colors.get( i + 1 ), colors.get( i + 2 ) ) );

			}

			if ( uvs != null ) {

				tempUVs.add( new Vector2( uvs.get( j ), uvs.get( j + 1 ) ) );

			}

			if ( uvs2 != null ) {

				tempUVs2.add( new Vector2( uvs2.get( j ), uvs2.get( j + 1 ) ) );

			}
		}

		if ( indices != null ) {

			List<Group> groups = geometry.getGroups();

			if(groups.size() > 0)
			{
				for ( int i = 0; i < groups.size(); i ++ ) {

					Group group = groups.get(i);

					int start = group.getStart();
					int count = group.getCount();

					for ( int j = start, jl = start + count; j < jl; j += 3 ) {

						addFace( normals, colors, uvs, uvs2, tempNormals, tempUVs,tempUVs2,
								indices.get( j ), indices.get( j + 1 ), indices.get( j + 2 ), group.getMaterialIndex());

					}

				}
			}
			else
			{
				for ( int i = 0; i < indices.getLength(); i += 3 ) {

					addFace( normals, colors, uvs, uvs2, tempNormals, tempUVs,tempUVs2,
							indices.get( i ), indices.get( i + 1 ), indices.get( i + 2 ), 0 );

				}

			}

		} else {

			for ( int i = 0; i < positions.getLength() / 3; i += 3 ) {

				addFace(normals, colors, uvs, uvs2, tempNormals, tempUVs,tempUVs2,
						i, i + 1, i + 2, 0 );

			}

		}

		this.computeFaceNormals();

		if(geometry.boundingBox != null)
			this.boundingBox = geometry.boundingBox.clone();

		if(geometry.boundingSphere != null)
			this.boundingSphere = geometry.boundingSphere.clone();

		return this;
	}

	private void addFace(Float32Array normals, Float32Array colors, Float32Array uvs, Float32Array uvs2,
						 List<Vector3> tempNormals, List<Vector2> tempUVs, List<Vector2> tempUVs2,
						 int a, int b, int c, int materialIndex )
	{
		List<Vector3> vertexNormals = normals != null
				? Arrays.asList( tempNormals.get( a ).clone(), tempNormals.get( b ).clone(), tempNormals.get( c ).clone() )
				: new ArrayList<Vector3>();
		List<Color> vertexColors = colors != null
				? Arrays.asList( this.colors.get( a ).clone(), this.colors.get( b ).clone(), this.colors.get( c ).clone() )
				: new ArrayList<Color>();

		this.faces.add( new Face3( a, b, c, vertexNormals, vertexColors, materialIndex ) );

		if ( uvs != null ) {

			this.faceVertexUvs.get(0).add( Arrays.asList( tempUVs.get( a ).clone(), tempUVs.get( b ).clone(), tempUVs.get( c ).clone() ) );

		}

		if ( uvs2 != null ) {

			this.faceVertexUvs.get(1).add( Arrays.asList(tempUVs2.get( a ).clone(), tempUVs2.get( b ).clone(), tempUVs2.get( c ).clone() ) );

		}

		this.faceVertexUvs.get( 0 ).add( Arrays.asList( tempUVs.get( a ), tempUVs.get( b ), tempUVs.get( c ) ) );

	}

	public Vector3 center()
	{

		this.computeBoundingBox();

		Vector3 offset = new Vector3();

		this.translate(offset.getX(), offset.getY(), offset.getZ());

		return offset;

	}

	public Geometry normalize() {

		this.computeBoundingSphere();

		Vector3 center = this.boundingSphere.getCenter();
		double radius = this.boundingSphere.getRadius();

			double s = radius == 0 ? 1 : 1.0 / radius;

			Matrix4 matrix = new Matrix4();
		matrix.set(
				s, 0, 0, - s * center.getX(),
				0, s, 0, - s * center.getY(),
				0, 0, s, - s * center.getZ(),
				0, 0, 0, 1
		);

		this.applyMatrix( matrix );

		return this;

	}

	/**
	 * Computes face normals.
	 */
	public void computeFaceNormals() {

		Vector3 cb = new Vector3(), ab = new Vector3();

		for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

			Face3 face = this.faces.get( f );

			Vector3 vA = this.vertices.get( face.getA() );
			Vector3 vB = this.vertices.get( face.getB() );
			Vector3 vC = this.vertices.get( face.getC() );

			cb.sub( vC, vB );
			ab.sub( vA, vB );
			cb.cross( ab );

			cb.normalize();

			face.normal.copy( cb );

		}

	}

	public void computeVertexNormals()
	{
		computeVertexNormals(false);
	}

	/**
	 * Computes vertex normals by averaging face normals.
	 * Face normals must be existing / computed beforehand.
	 */
	public void computeVertexNormals(boolean areaWeighted)
	{
		Vector3[] vertices = new Vector3[this.vertices.size()];
		Face3 face;

		for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) {

			vertices[ v ] = new Vector3();

		}

		if ( areaWeighted ) {

			// vertex normals weighted by triangle areas
			// http://www.iquilezles.org/www/articles/normals/normals.htm

			Vector3 vA, vB, vC;
			Vector3 cb = new Vector3(), ab = new Vector3();

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

				face = this.faces.get( f );

				vA = this.vertices.get( face.a );
				vB = this.vertices.get( face.b );
				vC = this.vertices.get( face.c );

				cb.sub( vC, vB );
				ab.sub( vA, vB );
				cb.cross( ab );

				vertices[ face.a ].add( cb );
				vertices[ face.b ].add( cb );
				vertices[ face.c ].add( cb );

			}

		} else {

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

				face = this.faces.get( f );

				vertices[ face.a ].add( face.normal );
				vertices[ face.b ].add( face.normal );
				vertices[ face.c ].add( face.normal );

			}

		}

		for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) {

			vertices[ v ].normalize();

		}

		for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

			face = this.faces.get( f );

			if(face.getVertexNormals().size() > 0) {
				face.getVertexNormals().set(0, vertices[ face.a ].clone() );
				face.getVertexNormals().set(1, vertices[ face.b ].clone() );
				face.getVertexNormals().set(2, vertices[ face.c ].clone() );
			} else {
				face.getVertexNormals().add(0, vertices[ face.a ].clone() );
				face.getVertexNormals().add(1, vertices[ face.b ].clone() );
				face.getVertexNormals().add(2, vertices[ face.c ].clone() );
			}

		}

		if ( this.faces.size() > 0 ) {

			this.normalsNeedUpdate = true;

		}
	}

	/**
	 * Computes morph normals.
	 */
	public void computeMorphNormals()
	{

		Face3 face;

		// save original normals
		// - create temp variables on first access
		//   otherwise just copy (for faster repeated calls)

		for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

			face = this.faces.get( f );

			if ( face.__originalFaceNormal == null ) {

				face.__originalFaceNormal = face.normal.clone();

			} else {

				face.__originalFaceNormal.copy( face.normal );

			}

			if ( face.__originalVertexNormals == null )
				face.__originalVertexNormals = new ArrayList<Vector3>();

			for ( int i = 0, il = face.vertexNormals.size(); i < il; i ++ ) {

				if ( face.__originalVertexNormals.size() <= i || face.__originalVertexNormals.get( i ) == null ) {

					face.__originalVertexNormals.add( i , face.vertexNormals.get( i ).clone() );

				} else {

					face.__originalVertexNormals.get( i ).copy( face.vertexNormals.get( i ) );

				}

			}

		}

		// use temp geometry to compute face and vertex normals for each morph

		Geometry tmpGeo = new Geometry();
		tmpGeo.faces = this.faces;

		for ( int i = 0, il = this.morphTargets.size(); i < il; i ++ ) {

			// create on first access

			if ( this.morphNormals.size() <= i || this.morphNormals.get( i ) == null ) {

				MorphNormal morphNormal = new MorphNormal();

				List<Vector3> dstNormalsFace = morphNormal.getFaceNormals();
				List<VertextNormal> dstNormalsVertex = morphNormal.getVertexNormals();

				for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

					VertextNormal vertexNormals = new VertextNormal();

					dstNormalsFace.add( new Vector3() );
					dstNormalsVertex.add( vertexNormals );

				}

				this.morphNormals.add( i, morphNormal);
			}

			MorphNormal morphNormals = this.morphNormals.get( i );

			// set vertices to morph target

			tmpGeo.vertices = this.morphTargets.get(i).getVertices();

			// compute morph normals

			tmpGeo.computeFaceNormals();
			tmpGeo.computeVertexNormals();

			// store morph normals

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

				face = this.faces.get( f );

				Vector3 faceNormal = morphNormals.getFaceNormals().get( f );
				VertextNormal vertexNormals = morphNormals.getVertexNormals().get( f );

				faceNormal.copy( face.normal );

				vertexNormals.getA().copy( face.vertexNormals.get( 0 ) );
				vertexNormals.getB().copy( face.vertexNormals.get( 1 ) );
				vertexNormals.getC().copy( face.vertexNormals.get( 2 ) );

			}

		}

		// restore original normals

		for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

			face = this.faces.get( f );

			face.normal = face.__originalFaceNormal;
			face.vertexNormals = face.__originalVertexNormals;

		}

	}

	/**
	 * Compute distances between vertices for Line geometries.
	 */
	public void computeLineDistances( )
	{
		double d = 0;

		for ( int i = 0, il = vertices.size(); i < il; i ++ )
		{
			if ( i > 0 )
			{
				d += this.vertices.get( i ).distanceTo( this.vertices.get( i - 1 ) );
			}

			this.lineDistances.add( i, d );
		}
	}

	/**
	 * Computes bounding box of the geometry.
	 */
	@Override
	public void computeBoundingBox()
	{
		if ( this.boundingBox == null )
		{
			this.boundingBox = new Box3();
		}

		this.boundingBox.setFromPoints( this.vertices );
	}

	/**
	 * Computes bounding sphere of the geometry.
	 * <p>
	 * Neither bounding boxes or bounding spheres are computed by default.
	 * They need to be explicitly computed, otherwise they are null.
	 */
	@Override
	public void computeBoundingSphere()
	{
		if ( this.boundingSphere == null ) {

			this.boundingSphere = new Sphere();

		}

		this.boundingSphere.setFromPoints( this.vertices, null );

	}

	public void merge( Geometry geometry, Matrix4 matrix )
	{

		merge(geometry, matrix, 0);

	}

	/**
	 * Merge two geometries or geometry and geometry from object (using object's transform)
	 *
	 * @param geometry
	 * @param matrix
	 * @param materialIndexOffset
	 */
	public void merge( Geometry geometry, Matrix4 matrix, int materialIndexOffset )
	{

		Matrix3 normalMatrix = null;

		int vertexOffset = this.vertices.size();
		List<Vector3> vertices1 = this.vertices;
		List<Vector3> vertices2 = geometry.vertices;
		List<Face3> faces1 = this.faces;
		List<Face3> faces2 = geometry.faces;

		List<List<Vector2>>uvs1 = this.faceVertexUvs.get( 0 );
		List<List<Vector2>>uvs2 = geometry.faceVertexUvs.get( 0 );

		if ( matrix != null ) {

			normalMatrix = new Matrix3().getNormalMatrix( matrix );

		}

		// vertices

		for ( int i = 0, il = vertices2.size(); i < il; i ++ ) {

			Vector3 vertex = vertices2.get( i );

			Vector3 vertexCopy = vertex.clone();

			if ( matrix != null ) vertexCopy.apply( matrix );

			vertices1.add( vertexCopy );

		}

		// faces

		for ( int i = 0, il = faces2.size(); i < il; i ++ ) {

			Face3 face = faces2.get( i );

			List<Vector3> faceVertexNormals = face.vertexNormals;
			List<Color> faceVertexColors = face.vertexColors;

			Face3 faceCopy = new Face3( face.a + vertexOffset, face.b + vertexOffset, face.c + vertexOffset );
			faceCopy.getNormal().copy( face.normal );

			if ( normalMatrix != null ) {

				faceCopy.getNormal().apply( normalMatrix ).normalize();

			}

			for ( int j = 0, jl = faceVertexNormals.size(); j < jl; j ++ ) {

				Vector3 normal = faceVertexNormals.get( j ).clone();

				if ( normalMatrix != null ) {

					normal.apply( normalMatrix ).normalize();

				}

				faceCopy.vertexNormals.add( normal );

			}

			faceCopy.color.copy( face.color );

			for ( int j = 0, jl = faceVertexColors.size(); j < jl; j ++ ) {

				Color color = faceVertexColors.get( j );
				faceCopy.vertexColors.add( color.clone() );

			}

			faceCopy.materialIndex = face.materialIndex + materialIndexOffset;

			faces1.add( faceCopy );

		}

		// uvs

		for ( int i = 0, il = uvs2.size(); i < il; i ++ ) {

			List<Vector2> uv = uvs2.get( i );
			List<Vector2> uvCopy = new ArrayList<Vector2>();

			if ( uv == null ) {

				continue;

			}

			for ( int j = 0, jl = uv.size(); j < jl; j ++ ) {

				uvCopy.add( new Vector2( uv.get( j ).getX(), uv.get( j ).getY() ) );

			}

			uvs1.add( uvCopy );

		}

	}

	public void mergeMesh ( Mesh mesh ) {

		if(mesh.isMatrixAutoUpdate())
			mesh.updateMatrix();

		this.merge((Geometry) mesh.geometry, mesh.getMatrix());

	}

	/**
	 * Checks for duplicate vertices with hashmap.
	 * Duplicated vertices are removed and faces' vertices are updated.
	 */
	public int mergeVertices()
	{
		// Hashmap for looking up vertice by position coordinates (and making sure they are unique)
		FastMap<Integer> verticesMap = new FastMap<>();
		List<Vector3> unique = new ArrayList<>();
		List<Integer> changes = new ArrayList<>();

		int precisionPoints = 4; // number of decimal points, eg. 4 for epsilon of 0.0001
		double precision = Math.pow( 10, precisionPoints );

		for ( int i = 0, il = this.vertices.size(); i < il; i ++ )
		{
			Vector3 v = this.vertices.get( i );
			String key = Math.round( v.getX() * precision ) + "_" + Math.round( v.getY() * precision ) + "_"  + Math.round( v.getZ() * precision );

			if ( !verticesMap.containsKey(key))
			{
				verticesMap.put(key, i);
				unique.add(v);
				changes.add( i , unique.size() - 1);
			}
			else
			{
				changes.add( i , changes.get( verticesMap.get( key ) ));
			}
		}


		// if faces are completely degenerate after merging vertices, we
		// have to remove them from the geometry.
		List<Integer> faceIndicesToRemove = new ArrayList<Integer>();

		for( int i = 0, il = this.faces.size(); i < il; i ++ )
		{
			Face3 face = this.faces.get( i );

			face.setA( changes.get( face.getA() ) );
			face.setB( changes.get( face.getB() ) );
			face.setC( changes.get( face.getC() ) );

			int[] indices = { face.getA(), face.getB(), face.getC() };

			// if any duplicate vertices are found in a Face3
			// we have to remove the face as nothing can be saved
			for ( int n = 0; n < 3; n ++ )
			{
				if ( indices[ n ] == indices[ ( n + 1 ) % 3 ] )
				{
					faceIndicesToRemove.add( i );
					break;
				}
			}
		}

		for ( int i = faceIndicesToRemove.size() - 1; i >= 0; i -- )
		{
			this.faces.remove( i );

			for ( int j = 0, jl = this.faceVertexUvs.size(); j < jl; j ++ )
			{
				this.faceVertexUvs.get( j ).remove( i );
			}
		}

		// Use unique set of vertices

		int diff = this.vertices.size() - unique.size();
		this.vertices = unique;
		return diff;
	}

	public void sortFacesByMaterialIndex() {

		// tag faces
		class FaceTag {
			public int id;
			public int materialIndex;
			public FaceTag(int id, int materialIndex) {
				this.id = id;
				this.materialIndex = materialIndex;
			}
		}

		List<FaceTag> faces = new ArrayList<>();
		int length = this.faces.size();

		for ( int i = 0; i < length; i ++ ) {

			faces.add(new FaceTag(i, this.faces.get(i).materialIndex));

		}

		// sort faces
		Collections.sort(faces, new Comparator<FaceTag>() {
			@Override
			public int compare(FaceTag a, FaceTag b) {
				return a.materialIndex - b.materialIndex;
			}
		});

		// sort uvs

		List<List<Vector2>> uvs1 = this.faceVertexUvs.get(0);
		List<List<Vector2>> uvs2 = this.faceVertexUvs.get(1);

		List<List<Vector2>> newUvs1 = null, newUvs2 = null;

		if ( uvs1 != null && uvs1.size() == length ) newUvs1 = new ArrayList<>();
		if ( uvs2 != null && uvs2.size() == length ) newUvs2 = new ArrayList<>();

		for ( int i = 0; i < length; i ++ ) {

			int id = faces.get(i).id;

			if ( newUvs1 != null ) newUvs1.add( uvs1.get( id ) );
			if ( newUvs2 != null ) newUvs2.add( uvs2.get( id ) );

		}

		if ( newUvs1 != null) this.faceVertexUvs.set(0, newUvs1);
		if ( newUvs2 != null) this.faceVertexUvs.set(1, newUvs2);

	}

	public Geometry clone() {
		return new Geometry().copy( this );
	}
	/**
	 * Creates a new clone of the Geometry.
	 */
	public Geometry copy(Geometry source) {

		this.vertices = new ArrayList<>();
		this.faces = new ArrayList<>();
		this.faceVertexUvs = new ArrayList<>();

		List<Vector3> vertices = source.vertices;

		for ( int i = 0, il = vertices.size(); i < il; i ++ ) {

			vertices.add( vertices.get( i ).clone() );

		}

		List<Face3> faces = source.faces;

		for ( int i = 0, il = faces.size(); i < il; i ++ ) {

			faces.add( faces.get( i ).clone() );
		}

		for ( int i = 0, il = source.faceVertexUvs.size(); i < il; i ++ ) {

			List<List<Vector2>> faceVertexUvs = source.faceVertexUvs.get(i);

			if ( this.faceVertexUvs.get(i) == null  ) {

				this.faceVertexUvs.add(i, new ArrayList<List<Vector2>>());

			}

			for ( int j = 0, jl = faceVertexUvs.size(); j < jl; j ++ ) {

				List<Vector2> uvs = faceVertexUvs.get(j);
				List<Vector2> uvsCopy = new ArrayList<>();

				for ( int k = 0, kl = uvs.size(); k < kl; k ++ ) {

					Vector2 uv = uvs.get(k);

					uvsCopy.add( uv.clone() );

				}

				this.faceVertexUvs.get(i).add( uvsCopy );

			}

		}

		return this;

	}
}
