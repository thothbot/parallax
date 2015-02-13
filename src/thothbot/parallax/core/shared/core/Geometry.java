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

package thothbot.parallax.core.shared.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.shared.math.Box3;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix3;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Sphere;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;
import thothbot.parallax.core.shared.objects.Bone;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.PointCloud;

import com.google.gwt.core.client.GWT;

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
public class Geometry extends AbstractGeometry
{
	public class MorphColor
	{
		public String name;
		public List<Color> colors;
	}
	
	public class MorphNormal
	{
		public List<Vector3> faceNormals;
		public List<VertextNormal> vertexNormals;
	}
	
	public class VertextNormal
	{
		public Vector3 a;
		public Vector3 b;
		public Vector3 c;
	}
	
	public class MorphTarget
	{
		public String name;
		public List<Vector3> vertices;
	}
		
	// Array of vertices.
	private List<Vector3> vertices;
	// one-to-one vertex colors, used in ParticleSystem, Line and Ribbon
	private List<Color> colors;
	
	private List<Face3> faces;
	
	private List<List<List<Vector2>>> faceVertexUvs;

	private List<MorphTarget> morphTargets;
	private List<MorphColor> morphColors;
	private List<MorphNormal> morphNormals;

	private List<Vector4> skinWeights;
	private List<Vector4> skinIndices;

	/*
	 * An array containing distances between vertices for Line geometries. 
	 * This is required for LinePieces/LineDashedMaterial to render correctly.
	 * Line distances can also be generated with computeLineDistances.
	 */
	private List<Double> lineDistances;
	
	/*
	 * True if geometry has tangents. Set in Geometry.computeTangents.
	 */
	private boolean hasTangents = false;

	/*
	 * The intermediate typed arrays will be deleted when set to false
	 */
	private boolean dynamic = true; 

	private List<Bone> bones;
	
	public Geometry() 
	{
		super();
		
		this.vertices = new ArrayList<Vector3>();
		this.colors = new ArrayList<Color>(); // one-to-one vertex colors, used in ParticleSystem, Line and Ribbon

		this.faces = new ArrayList<Face3>();
		
		this.faceVertexUvs = new ArrayList<List<List<Vector2>>>();
		this.faceVertexUvs.add(new ArrayList<List<Vector2>>());

		this.morphTargets = new ArrayList<MorphTarget>();
		this.morphNormals = new ArrayList<MorphNormal>();
		this.morphColors = new ArrayList<MorphColor>();

		this.skinWeights = new ArrayList<Vector4>();
		this.skinIndices = new ArrayList<Vector4>();
		
		this.lineDistances = new ArrayList<Double>();
	}
	
	/**
	 * Set to true if attribute buffers will need to change in runtime (using "dirty" flags).
	 * Unless set to true internal typed arrays corresponding to buffers will be deleted once sent to GPU.
	 * Defaults to true.
	 * @return
	 */
	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
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
	 * Used in {@link PointCloud}, {@link Line}.<br>
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
	 * Gets the List of {@link Geometry.MorphTarget}. 
	 * Morph vertices match number and order of primary vertices.
	 */
	public List<MorphTarget> getMorphTargets() {
		return morphTargets;
	}

	/**
	 * Get the List of {@link Geometry.MorphNormal}.
	 * Morph normals have similar structure as morph targets 
	 * @return
	 */
	public List<MorphNormal> getMorphNormals() {
		return morphNormals;
	}
	
	/**
	 * Gets the List of {@link Geometry.MorphColor}. Morph colors have similar structure as {@link Geometry.MorphTarget}.
	 * Morph colors can match either number and order of faces (face colors) or number of vertices (vertex colors).
	 */
	public List<MorphColor> getMorphColors() {
		return this.morphColors;
	}
	
	/**
	 * Gets the List of face {@link UV} layers.
	 * Each UV layer is an array of UVs matching the order and number of vertices in faces.
	 * To signal an update in this array, Geometry.uvsNeedUpdate needs to be set to true.
	 */
	public List<List<List<Vector2>>> getFaceVertexUvs(){
		return this.faceVertexUvs;
	}
	
	public void setFaceVertexUvs(List<List<List<Vector2>>> faceVertexUvs) {
		this.faceVertexUvs = faceVertexUvs;
	}

	public List<Bone> getBones() {
		return bones;
	}

	public void setBones(List<Bone> bones) {
		this.bones = bones;
	}
	
	/**
	 * @return the hasTangents
	 */
	public boolean isHasTangents() {
		return hasTangents;
	}

	/**
	 * @param hasTangents the hasTangents to set
	 */
	public void setHasTangents(boolean hasTangents) {
		this.hasTangents = hasTangents;
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

		this.computeBoundingBox();

		this.computeBoundingSphere();
	}
	
	public Geometry fromBufferGeometry( BufferGeometry geometry ) 
	{

		Float32Array vertices = (Float32Array)geometry.getAttribute("position").getArray();
		Uint16Array indices = geometry.getAttribute("index") != null 
				? (Uint16Array)geometry.getAttribute("index").getArray() : null;
		Float32Array normals = geometry.getAttribute("normal") != null 
				? (Float32Array)geometry.getAttribute("normal").getArray() : null;
		Float32Array colors = geometry.getAttribute("color") != null 
				? (Float32Array)geometry.getAttribute("color").getArray() : null;
		Float32Array uvs = geometry.getAttribute("uv") != null
				? (Float32Array)geometry.getAttribute("uv").getArray() : null;

		List<Vector3> tempNormals = new ArrayList<Vector3>();
		List<Vector2> tempUVs = new ArrayList<Vector2>();

		for ( int i = 0, j = 0; i < vertices.getLength(); i += 3, j += 2 ) {

			this.vertices.add( new Vector3( vertices.get( i ), vertices.get( i + 1 ), vertices.get( i + 2 ) ) );

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

		}

		if ( indices != null ) {

			for ( int i = 0; i < indices.getLength(); i += 3 ) {

				addFace( normals, colors, tempNormals, tempUVs, (int)indices.get( i ), (int)indices.get( i + 1 ), (int)indices.get( i + 2 ) );

			}

		} else {

			for ( int i = 0; i < vertices.getLength() / 3; i += 3 ) {

				addFace( normals, colors, tempNormals, tempUVs, i, i + 1, i + 2 );

			}

		}
		
		this.computeFaceNormals();

		if(geometry.boundingBox != null)
			this.boundingBox = geometry.boundingBox.clone();
		
		if(geometry.boundingSphere != null)
			this.boundingSphere = geometry.boundingSphere.clone();

		return this;
	}
	
	private void addFace(Float32Array normals, Float32Array colors, List<Vector3> tempNormals, List<Vector2> tempUVs, int a, int b, int c ) 
	{
		List<Vector3> vertexNormals = normals != null 
				? Arrays.asList( tempNormals.get( a ).clone(), tempNormals.get( b ).clone(), tempNormals.get( c ).clone() ) 
						: new ArrayList<Vector3>();
		List<Color> vertexColors = colors != null 
				? Arrays.asList( this.colors.get( a ).clone(), this.colors.get( b ).clone(), this.colors.get( c ).clone() ) 
						: new ArrayList<Color>();

		this.faces.add( new Face3( a, b, c, vertexNormals, vertexColors, 0 ) );
		this.faceVertexUvs.get( 0 ).add( Arrays.asList( tempUVs.get( a ), tempUVs.get( b ), tempUVs.get( c ) ) );

	}
	
	public Vector3 center() 
	{

		this.computeBoundingBox();

		Vector3 offset = new Vector3();

		offset.add( this.boundingBox.getMin(), this.boundingBox.getMax() );
		offset.multiply( - 0.5 );

		this.applyMatrix( new Matrix4().makeTranslation( offset.getX(), offset.getY(), offset.getZ() ) );
		this.computeBoundingBox();

		return offset;

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

				Geometry.MorphNormal morphNormal = new MorphNormal();
				morphNormal.faceNormals = new ArrayList<Vector3>();
				morphNormal.vertexNormals = new ArrayList<Geometry.VertextNormal>();

				List<Vector3> dstNormalsFace = morphNormal.faceNormals;
				List<VertextNormal> dstNormalsVertex = morphNormal.vertexNormals;

				for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

					VertextNormal vertexNormals = new VertextNormal();
					vertexNormals.a  = new Vector3();
					vertexNormals.b  = new Vector3();
					vertexNormals.c  = new Vector3();

					dstNormalsFace.add( new Vector3() );
					dstNormalsVertex.add( vertexNormals );

				}
				
				this.morphNormals.add( i, morphNormal);
			}

			MorphNormal morphNormals = this.morphNormals.get( i );

			// set vertices to morph target

			tmpGeo.vertices = this.morphTargets.get( i ).vertices;

			// compute morph normals

			tmpGeo.computeFaceNormals();
			tmpGeo.computeVertexNormals();

			// store morph normals

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) {

				face = this.faces.get( f );

				Vector3 faceNormal = morphNormals.faceNormals.get( f );
				VertextNormal vertexNormals = morphNormals.vertexNormals.get( f );

				faceNormal.copy( face.normal );

				vertexNormals.a.copy( face.vertexNormals.get( 0 ) );
				vertexNormals.b.copy( face.vertexNormals.get( 1 ) );
				vertexNormals.c.copy( face.vertexNormals.get( 2 ) );

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
	 * Computes vertex tangents.<br>
	 * Based on <a href="http://www.terathon.com/code/tangent.html">terathon.com</a>
	 * <p>
	 * Geometry must have vertex {@link UV}s (layer 0 will be used).
	 */
	@Override
	public void computeTangents()
	{
		Face3 face;
		Vector2[] uv = new Vector2[0];
		int v, vl, f, fl, i, vertexIndex;
		List<Vector3> tan1 = new ArrayList<Vector3>(), 
				tan2 = new ArrayList<Vector3>();
		Vector3 tmp = new Vector3(), tmp2 = new Vector3();

		for (v = 0,vl = this.vertices.size(); v<vl; v++) 
		{
			tan1.add(v, new Vector3());
			tan2.add(v, new Vector3());
		}
		
		for (f = 0, fl = this.faces.size(); f < fl; f++) 
		{

			face = this.faces.get(f);
			uv = this.faceVertexUvs.get(0).get(f).toArray(uv); // use UV layer 0 for tangents

			handleTriangle(face.getA(), face.getB(), face.getC(), 0, 1, 2, uv, tan1, tan2);
		}

		for (f = 0, fl = this.faces.size(); f < fl; f ++ ) 
		{

			face = this.faces.get(f);

			for (i = 0; i < face.getVertexNormals().size(); i++) {

				Vector3 n = new Vector3();
				n.copy(face.getVertexNormals().get(i));

				vertexIndex = face.getFlat()[i];

				Vector3 t = tan1.get(vertexIndex);

				// Gram-Schmidt orthogonalize

				tmp.copy(t);
				tmp.sub( n.multiply( n.dot( t ) ) ).normalize();

				// Calculate handedness

				tmp2.cross(face.getVertexNormals().get(i), t);
				double test = tmp2.dot(tan2.get(vertexIndex));
				double w = (test < 0.0) ? -1.0 : 1.0;
				
				face.getVertexTangents().add(i, new Vector4(tmp.getX(),tmp.getY(),tmp.getZ(), w));
			}
		}

		this.setHasTangents(true);
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
//				faceCopy.vertexColors.add( color );

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
	
	/**
	 * Checks for duplicate vertices with hashmap.
	 * Duplicated vertices are removed and faces' vertices are updated.
	 */
	public int mergeVertices() 
	{
		// Hashmap for looking up vertice by position coordinates (and making sure they are unique)
		Map<String, Integer> verticesMap = GWT.isScript() ? 
				new FastMap<Integer>() : new HashMap<String, Integer>();
		List<Vector3> unique = new ArrayList<Vector3>();
		List<Integer> changes = new ArrayList<Integer>();

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
	
	/**
	 * Creates a new clone of the Geometry.
	 */
	public Geometry clone() {

		Geometry geometry = new Geometry();


		for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) {

			geometry.vertices.add( this.vertices.get( i ).clone() );

		}

		for ( int i = 0, il = this.faces.size(); i < il; i ++ ) {

			geometry.faces.add( this.faces.get( i ).clone() );
		}

		List<List<Vector2>> uvs = this.faceVertexUvs.get( 0 );

		for ( int i = 0, il = uvs.size(); i < il; i ++ ) {

			List<Vector2> uv = uvs.get( i );
			List<Vector2> uvCopy = new ArrayList<Vector2>();

			for ( int j = 0, jl = uv.size(); j < jl; j ++ ) {

				uvCopy.add( new Vector2( uv.get( j ).getX(), uv.get( j ).getY() ) );

			}

			geometry.faceVertexUvs.get( 0 ).add( uvCopy );

		}

		return geometry;
		
	}

	private void handleTriangle(int a, int b, int c, int ua, int ub, int uc, Vector2[] uv, List<Vector3> tan1, List<Vector3> tan2)
	{
		Vector3 vA = this.vertices.get(a);
		Vector3 vB = this.vertices.get(b);
		Vector3 vC = this.vertices.get(c);
		
		Vector2 uvA = uv[ua];
		Vector2 uvB = uv[ub];
		Vector2 uvC = uv[uc];
		
		double x1 = vB.getX() - vA.getX();
		double x2 = vC.getX() - vA.getX();
		double y1 = vB.getY() - vA.getY();
		double y2 = vC.getY() - vA.getY();
		double z1 = vB.getZ() - vA.getZ();
		double z2 = vC.getZ() - vA.getZ();
		
		double s1 = uvB.getX() - uvA.getX();
		double s2 = uvC.getX() - uvA.getX();
		double t1 = uvB.getY() - uvA.getY();
		double t2 = uvC.getY() - uvA.getY();
		
		double r = 1.0 / (s1 * t2 - s2 * t1);
		
		Vector3 sdir = new Vector3();
		sdir.set((t2*x1-t1*x2)*r,
				  (t2*y1-t1*y2)*r,
				  (t2*z1-t1*z2)*r);
		
		Vector3 tdir = new Vector3();
		tdir.set((s1*x2 - s2*x1)*r,
				  (s1*y2 - s2*y1)*r,
				  (s1*z2 - s2*z1)*r);
		
		tan1.get(a).add(sdir);
		tan1.get(b).add(sdir);
		tan1.get(c).add(sdir);

		tan2.get(a).add(tdir);
		tan2.get(b).add(tdir);
		tan2.get(c).add(tdir);
	}

}
