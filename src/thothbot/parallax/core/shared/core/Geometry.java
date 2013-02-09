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

import org.apache.commons.lang.ArrayUtils;

import thothbot.parallax.core.shared.materials.Material;
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
import thothbot.parallax.core.shared.objects.Object3D;
import thothbot.parallax.core.shared.objects.ParticleSystem;
import thothbot.parallax.core.shared.objects.Ribbon;

import com.google.gwt.core.client.GWT;

/**
 * Base class for geometries
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
public class Geometry extends GeometryBuffer implements Geometric
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
		public Vector3 d;
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
	// one-to-one vertex normals, used in Ribbon
	private List<Vector3> normals;

	private List<Face3> faces;
	
	private List<List<Vector2>> faceUvs;	
	private List<List<List<Vector2>>> faceVertexUvs;

	private List<MorphTarget> morphTargets;
	private List<MorphColor> morphColors;
	private List<MorphNormal> morphNormals;

	private List<Vector4> skinWeights;
	private List<Vector4> skinIndices;

	private List<Double> lineDistances;
	private boolean isLineDistancesNeedUpdate;
	
	// Array of materials.
	private List<Material> materials;
	
	private List<Vector3> skinVerticesA;
	private List<Vector3> skinVerticesB;

	private List<Bone> bones;

	public List<List<Integer>> sortArray;
	
	private boolean isMorphTargetsNeedUpdate;
	
	private List<GeometryGroup> geometryGroups;
	private Map<String, GeometryGroup> geometryGroupsCache;
		
	private Object3D debug;
	
	private ArrayList<Vector3> __tmpVertices;
	
	public Geometry() 
	{
		super();

		this.vertices = new ArrayList<Vector3>();
		this.colors = new ArrayList<Color>(); // one-to-one vertex colors, used in ParticleSystem, Line and Ribbon

		this.faces = new ArrayList<Face3>();

		this.faceUvs = new ArrayList<List<Vector2>>();
		this.faceVertexUvs = new ArrayList<List<List<Vector2>>>();
		this.faceVertexUvs.add(new ArrayList<List<Vector2>>());

		this.morphTargets = new ArrayList<MorphTarget>();
		this.morphNormals = new ArrayList<MorphNormal>();
		this.morphColors = new ArrayList<MorphColor>();

		this.skinWeights = new ArrayList<Vector4>();
		this.skinIndices = new ArrayList<Vector4>();

		this.debug = new Object3D();
	}
	
	public Map<String, GeometryGroup> getGeometryGroupsCache() {
		return this.geometryGroupsCache;
	}
	
	public void setGeometryGroupsCache(Map<String, GeometryGroup> geometryGroups) {
		this.geometryGroupsCache = geometryGroups;
	}
	
	public List<GeometryGroup> getGeometryGroups() {
		return this.geometryGroups;
	}
	
	public void setGeometryGroups(List<GeometryGroup> geometryGroupsList) {
		this.geometryGroups = geometryGroupsList;
	}
	
	public Object3D getDebug() {
		return this.debug;
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
	
	public List<Vector3> getSkinVerticesA() {
		return this.skinVerticesA;
	}
	
	public void setSkinVerticesA(List<Vector3> skinVerticesA) {
		this.skinVerticesA = skinVerticesA;
	}
	
	public List<Vector3> getSkinVerticesB() {
		return this.skinVerticesB;
	}
	
	public void setSkinVerticesB(List<Vector3>  skinVerticesB) {
		this.skinVerticesB = skinVerticesB;
	}

	public boolean isMorphTargetsNeedUpdate() {
		return isMorphTargetsNeedUpdate;
	}

	public void setMorphTargetsNeedUpdate(boolean isMorphTargetsNeedUpdate) {
		this.isMorphTargetsNeedUpdate = isMorphTargetsNeedUpdate;
	}

	public void setFaceUvs(List<List<Vector2>> faceUvs) {
		this.faceUvs = faceUvs;
	}

	/**
	 * Gets the List of face {@link UV} layers.
	 * Each UV layer is an List of {@link UV} matching order and number of faces.
	 */
	public List<List<Vector2>> getFaceUvs() {
		return faceUvs;
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	/**
	 * Gets List of vertex {@link Color}s, matching number and order of vertices.
	 * <p>
	 * Used in {@link ParticleSystem}, {@link Line} and {@link Ribbon}.<br>
	 * {@link Mesh}es use per-face-use-of-vertex colors embedded directly in faces.
	 */
	public List<Color> getColors() {
		return colors;
	}

	public void setFaces(List<Face3> faces) {
		this.faces = faces;
	}

	/**
	 * Gets the List of triangles: {@link Face3} or/and quads: {@link Face4}
	 */
	public List<Face3> getFaces() {
		return faces;
	}
	
	/**
	 * Gets the List of {@link Material}s.
	 */
	public List<Material> getMaterials() 
	{
		return this.materials;
	}
	
	public void setMaterials(List<Material> materials) 
	{
		this.materials = materials;
	}
	
	public void setVertices(List<Vector3> vertices) 
	{
		this.vertices = vertices;
	}

	/**
	 * Gets List of {@link Vector3}.
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
	 * Each UV layer is an List of UV matching order and number of vertices in faces.
	 */
	public List<List<List<Vector2>>> getFaceVertexUvs(){
		return this.faceVertexUvs;
	}
	
	public void setFaceVertexUvs(List<List<List<Vector2>>> faceVertexUvs) {
		this.faceVertexUvs = faceVertexUvs;
	}


	/**
	 * Makes matrix transform directly into vertex coordinates.	
	 */
	public void applyMatrix(Matrix4 matrix)
	{
		Matrix3 normalMatrix = new Matrix3().getInverse( matrix ).transpose();

		for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) 
		{
			Vector3 vertex = this.vertices.get( i );
			vertex.apply( matrix );
		}

		for ( int i = 0, il = this.faces.size(); i < il; i ++ ) 
		{
			Face3 face = this.faces.get( i );
			face.getNormal().apply( normalMatrix ).normalize();

			for ( int j = 0, jl = face.vertexNormals.size(); j < jl; j ++ ) 
			{
				face.getVertexNormals().get( j ).apply( normalMatrix ).normalize();
			}

			face.centroid.apply( matrix );
		}
	}
	
	/**
	 * Computes centroids for all faces.
	 */
	public void computeCentroids()
	{
		for (Face3 face: this.faces) 
		{
			face.getCentroid().set(0,0,0);

			if (face.getClass() == Face3.class) 
			{
				Face3 face3 = (Face3)face;
				face3.getCentroid().add(this.vertices.get(face3.getA()));
				face3.getCentroid().add(this.vertices.get(face3.getB()));
				face3.getCentroid().add(this.vertices.get(face3.getC()));
				face3.getCentroid().divide(3);

			} 
			else if (face.getClass() == Face4.class) 
			{
				Face4 face4 = (Face4)face;
				face4.getCentroid().add(this.vertices.get(face4.getA()));
				face4.getCentroid().add(this.vertices.get(face4.getB()));
				face4.getCentroid().add(this.vertices.get(face4.getC()));
				face4.getCentroid().add(this.vertices.get(face4.getD()));
				face4.getCentroid().divide(4);
			}

		}
	}
	
	/**
	 * Computes face normals.
	 */
	public void computeFaceNormals()
	{
		computeFaceNormals(false);
	}

	public void computeFaceNormals(Boolean useVertexNormals)
	{
		Vector3 cb = new Vector3(), ab = new Vector3();

		for (int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
		{
			Face3 face = this.faces.get( f );

			Vector3 vA = this.vertices.get(face.getA());
			Vector3 vB = this.vertices.get(face.getB());
			Vector3 vC = this.vertices.get(face.getC());

			cb.sub(vC, vB);
			ab.sub(vA, vB);
			cb.cross(ab);

			cb.normalize();

			face.getNormal().copy(cb);
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
		List<Vector3> vertices;
		Face3 face;
		
		// create internal buffers for reuse when calling this method repeatedly
		// (otherwise memory allocation / deallocation every frame is big resource hog)
		if ( this.__tmpVertices == null ) 
		{
			this.__tmpVertices = new ArrayList<Vector3>( this.vertices.size() );
			vertices = this.__tmpVertices;

			for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) 
			{
				vertices.add( v, new Vector3());
			}

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
			{
				face = this.faces.get( f );

				if ( face.getClass() == Face3.class ) 
				{
					face.setVertexNormals(Arrays.asList( new Vector3(), new Vector3(), new Vector3() ));
				}
				else if ( face.getClass() == Face4.class ) 
				{
					face.setVertexNormals( Arrays.asList( new Vector3(), new Vector3(), new Vector3(), new Vector3() ));
				}
			}
		} 
		else 
		{
			vertices = this.__tmpVertices;

			for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) 
			{
				vertices.get( v ).set( 0, 0, 0 );
			}
		}

		if ( areaWeighted ) 
		{
			// vertex normals weighted by triangle areas
			// http://www.iquilezles.org/www/articles/normals/normals.htm

			Vector3 vA, vB, vC, vD;
			Vector3 cb = new Vector3(), ab = new Vector3(),
				db = new Vector3(), dc = new Vector3(), bc = new Vector3();

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
			{
				face = this.faces.get( f );

				if ( face.getClass() == Face3.class ) 
				{

					vA = this.vertices.get( face.getA() );
					vB = this.vertices.get( face.getB() );
					vC = this.vertices.get( face.getC() );

					cb.sub( vC, vB );
					ab.sub( vA, vB );
					cb.cross( ab );

					vertices.get( face.getA() ).add( cb );
					vertices.get( face.getB() ).add( cb );
					vertices.get( face.getC() ).add( cb );

				} 
				else if ( face.getClass() == Face4.class ) 
				{
					vA = this.vertices.get( face.getA() );
					vB = this.vertices.get( face.getB() );
					vC = this.vertices.get( face.getC() );
					vD = this.vertices.get( ((Face4)face).getD() );

					// abd

					db.sub( vD, vB );
					ab.sub( vA, vB );
					db.cross( ab );

					vertices.get( face.getA() ).add( db );
					vertices.get( face.getB() ).add( db );
					vertices.get( ((Face4)face).getD() ).add( db );

					// bcd

					dc.sub( vD, vC );
					bc.sub( vB, vC );
					dc.cross( bc );

					vertices.get( face.getB() ).add( dc );
					vertices.get( face.getC() ).add( dc );
					vertices.get( ((Face4)face).getD() ).add( dc );
				}
			}
		} 
		else 
		{
			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
			{
				face = this.faces.get( f );

				if ( face.getClass() == Face3.class ) 
				{
					vertices.get( face.getA() ).add( face.normal );
					vertices.get( face.getB() ).add( face.normal );
					vertices.get( face.getC() ).add( face.normal );
				} 
				else if ( face.getClass() == Face4.class ) 
				{
					vertices.get( face.getA() ).add( face.normal );
					vertices.get( face.getB() ).add( face.normal );
					vertices.get( face.getC() ).add( face.normal );
					vertices.get( ((Face4)face).getD() ).add( face.normal );
				}
			}
		}

		for ( int v = 0, vl = this.vertices.size(); v < vl; v ++ ) 
		{
			vertices.get( v ).normalize();
		}

		for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
		{
			face = this.faces.get( f );

			if ( face.getClass() == Face3.class ) 
			{
				face.vertexNormals.get( 0 ).copy( vertices.get( face.getA() ) );
				face.vertexNormals.get( 1 ).copy( vertices.get( face.getB() ) );
				face.vertexNormals.get( 2 ).copy( vertices.get( face.getC() ) );
			} 
			else if ( face.getClass() == Face4.class ) 
			{
				face.vertexNormals.get( 0 ).copy( vertices.get( face.getA() ) );
				face.vertexNormals.get( 1 ).copy( vertices.get( face.getB() ) );
				face.vertexNormals.get( 2 ).copy( vertices.get( face.getC() ) );
				face.vertexNormals.get( 3 ).copy( vertices.get( ((Face4)face).getD() ) );
			}
		}
	}
	
	public void computeMorphNormals() 
	{
		Face3 face;
		
		// save original normals
		// - create temp variables on first access
		//   otherwise just copy (for faster repeated calls)
		for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
		{
			face = this.faces.get( f );

			if ( face.__originalFaceNormal == null ) 
			{
				face.__originalFaceNormal = face.getNormal().clone();
			} 
			else 
			{
				face.__originalFaceNormal.copy( face.getNormal() );
			}

			for ( int i = 0, il = face.vertexNormals.size(); i < il; i ++ ) 
			{
				if ( face.__originalVertexNormals.size() <= i 
						|| face.__originalVertexNormals.get( i ) == null)
				{
					face.__originalVertexNormals.add( i, face.getVertexNormals().get( i ).clone());
				}
				else
				{
					face.__originalVertexNormals.get( i ).copy( face.getVertexNormals().get( i ) );
				}
			}
		}

		// use temp geometry to compute face and vertex normals for each morph

		Geometry tmpGeo = new Geometry();
		tmpGeo.faces = this.faces;

		for ( int i = 0, il = this.morphTargets.size(); i < il; i ++ ) 
		{
			// create on first access

			if ( this.morphNormals.size() == i ) 
			{
				MorphNormal morphNormal = new MorphNormal();
				morphNormal.faceNormals = new ArrayList<Vector3>();
				morphNormal.vertexNormals = new ArrayList<VertextNormal>();
				
				List<Vector3> dstNormalsFace = this.morphNormals.get( i ).faceNormals;
				List<VertextNormal> dstNormalsVertex = this.morphNormals.get( i ).vertexNormals;

				for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
				{
					face = this.faces.get( f );

					Vector3 faceNormal = new Vector3();
					VertextNormal vertexNormals = new VertextNormal();
					
					if ( face.getClass() == Face3.class ) 
					{
						vertexNormals.a = new Vector3();
						vertexNormals.b = new Vector3();
						vertexNormals.c = new Vector3();
					} 
					else 
					{
						vertexNormals.a = new Vector3();
						vertexNormals.b = new Vector3();
						vertexNormals.c = new Vector3();
						vertexNormals.c = new Vector3();
					}

					dstNormalsFace.add( faceNormal );
					dstNormalsVertex.add( vertexNormals );
				}
			}

			MorphNormal morphNormals = this.morphNormals.get( i );

			// set vertices to morph target

			tmpGeo.vertices = this.morphTargets.get( i ).vertices;

			// compute morph normals

			tmpGeo.computeFaceNormals();
			tmpGeo.computeVertexNormals();

			// store morph normals

			for ( int f = 0, fl = this.faces.size(); f < fl; f ++ ) 
			{
				face = this.faces.get( f );

				Vector3 faceNormal = morphNormals.faceNormals.get(f);
				VertextNormal vertexNormals = morphNormals.vertexNormals.get(f);

				faceNormal.copy( face.normal );

				if ( face.getClass() == Face3.class ) 
				{
					vertexNormals.a.copy( face.getVertexNormals().get(0) );
					vertexNormals.b.copy( face.getVertexNormals().get(1) );
					vertexNormals.c.copy( face.getVertexNormals().get(2) );
				} 
				else 
				{
					vertexNormals.a.copy( face.getVertexNormals().get(0) );
					vertexNormals.b.copy( face.getVertexNormals().get(1) );
					vertexNormals.c.copy( face.getVertexNormals().get(2) );
					vertexNormals.d.copy( face.getVertexNormals().get(3) );
				}

			}
		}

		// restore original normals
		for ( int f = 0, fl = getFaces().size(); f < fl; f ++ ) 
		{
			face = getFaces().get(f);
			face.setNormal( face.__originalFaceNormal );
			face.setVertexNormals( face.__originalVertexNormals );
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

			if (face.getClass() == Face3.class) {
				handleTriangle(face.getA(), face.getB(), face.getC(), 0, 1, 2, uv, tan1, tan2);

			} else if (face.getClass() == Face4.class) {
				Face4 face4 = (Face4)face;
				handleTriangle(face4.getA(), face4.getB(), face4.getC(), 0, 1, 3, uv, tan1, tan2);
				handleTriangle(face4.getA(), face4.getB(), face4.getD(), 1, 2, 3, uv, tan1, tan2);

			}
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

		setHasTangents(true);
	}

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

		this.boundingSphere.setFromCenterAndPoints( this.boundingSphere.getCenter(), this.vertices );
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

		// reset cache of vertices as it now will be changing.
		this.__tmpVertices = null;

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

			if ( face.getClass() == Face3.class ) 
			{
				face.setA( changes.get( face.getA() ) );
				face.setB( changes.get( face.getB() ) );
				face.setC( changes.get( face.getC() ) );

				int[] indices = { face.getA(), face.getB(), face.getC() };

				int dupIndex = -1;

				// if any duplicate vertices are found in a Face3
				// we have to remove the face as nothing can be saved
				for ( int n = 0; n < 3; n ++ ) 
				{
					if ( indices[ n ] == indices[ ( n + 1 ) % 3 ] ) 
					{
						dupIndex = n;
						faceIndicesToRemove.add( i );
						break;
					}
				}

			}
			else if ( face.getClass() == Face4.class ) 
			{

				face.setA( changes.get( face.getA() ) );
				face.setB( changes.get( face.getB() ) );
				face.setC( changes.get( face.getC() ) );
				((Face4)face).setD( changes.get( ((Face4)face).getD() ) );

				// check dups in (a, b, c, d) and convert to -> face3

				List<Integer> indices = Arrays.asList( face.getA(), face.getB(), face.getC(), ((Face4)face).getD() );

				int dupIndex = -1;

				for ( int n = 0; n < 4; n ++ ) 
				{
					if ( indices.get( n ) == indices.get( ( n + 1 ) % 4 ) ) 
					{
						// if more than one duplicated vertex is found
						// we can't generate any valid Face3's, thus
						// we need to remove this face complete.
						if ( dupIndex >= 0 ) 
						{
							faceIndicesToRemove.add( i );
						}

						dupIndex = n;
					}
				}

				if ( dupIndex >= 0 ) 
				{
					indices.remove( dupIndex );

					Face3 newFace = new Face3( indices.get(0), indices.get(1), indices.get(2), face.normal, face.color, face.materialIndex );

					for ( int j = 0, jl = this.faceVertexUvs.size(); j < jl; j ++ ) 
					{
						List<Vector2> u = this.faceVertexUvs.get( j ).get( i );

						if ( u != null ) {
							u.remove( dupIndex );
						}
					}

					if( face.vertexNormals != null && face.vertexNormals.size() > 0) 
					{
						newFace.vertexNormals = face.vertexNormals;
						newFace.vertexNormals.remove( dupIndex);
					}

					if( face.vertexColors != null && face.vertexColors.size() > 0 ) 
					{
						newFace.vertexColors = face.vertexColors;
						newFace.vertexColors.remove( dupIndex );
					}

					this.faces.set( i, newFace );
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
	
	public Geometry clone() 
	{
		Geometry geometry = new Geometry();

		for ( int i = 0, il = vertices.size(); i < il; i ++ ) 
		{
			geometry.vertices.add( this.vertices.get( i ).clone() );
		}

		for ( int i = 0, il = faces.size(); i < il; i ++ ) 
		{
			geometry.faces.add( this.faces.get( i ).clone() );
		}

		List<List<Vector2>> uvs = this.faceVertexUvs.get( 0 );

		for ( int i = 0, il = uvs.size(); i < il; i ++ ) 
		{
			List<Vector2> uv = uvs.get( i ), uvCopy = new ArrayList<Vector2>();

			for ( int j = 0, jl = uv.size(); j < jl; j ++ ) 
			{
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
