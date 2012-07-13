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

package thothbot.squirrel.core.shared.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.squirrel.core.client.gl2.WebGLBuffer;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.objects.Bone;
import thothbot.squirrel.core.shared.objects.DimensionalObject;


public class Geometry extends GeometryBuffer
{
	// Bounding box.
	// 		{ min: new Vector3f(), max: new Vector3f() }		
	protected BoundingBox boundingBox = null;

	// Bounding sphere.
	// 		{ radius: float }
	protected BoundingSphere boundingSphere = null;
	
	// Array of morph targets. Each morph target is JS object:
	//  		{ name: "targetName", vertices: [ new Vector3f(), ... ] }
	// Morph vertices match number and order of primary vertices.
	protected List<DimensionalObject> morphTargets;

	// Array of vertices.
	protected List<Vector3f> vertices;
	
	protected ArrayList<Vector3f> tempVerticles;

	// Array of vertex colors, matching number and order of vertices.
	// Used in ParticleSystem, Line and Ribbon.
	// Meshes use per-face-use-of-vertex colors embedded directly in faces.
	protected List<Color3f> colors; // one-to-one vertex colors, used in
								// ParticleSystem, Line and Ribbon

	// Array of triangles or/and quads.
	protected List<Face3> faces;

	// Array of face UV layers.
	// Each UV layer is an array of UV matching order and number of vertices in faces.
	protected List<List<UVf>> faceUvs;
	
	// Array of face UV layers.
	// Each UV layer is an array of UV matching order and number of vertices in faces.
	protected List<List<List<UVf>>> faceVertexUvs;
	
	// True if geometry has tangents. Set in Geometry.computeTangents.
	protected Boolean hasTangents = false;
	
	// Array of materials.
	protected List<Material> materials;


	/////////////////////////////////////////////////////
	// TODO: Check vars
	// Array of morph colors. Morph colors have similar structure as morph targets, each color set is JS object:
	//		morphColor = { name: "colorName", colors: [ new Color3f(), ... ] }
	// Morph colors can match either number and order of faces (face colors) or number of vertices (vertex colors).
	public List<MorphColor> morphColors;

	// 		morphNormals = { faceNormals: [ new Vector3f(), ... ],  vertexNormals: [ new Vector3f(), ... ]}
	private List<MorphNormal> morphNormals;

	// Array of skinning weights, matching number and order of vertices.
	public List<Vector4f> skinWeights;

	// Array of skinning indices, matching number and order of vertices.
	public List<Vector4f> skinIndices;
	
	public List<Vector3f> skinVerticesA;
	public List<Vector3f> skinVerticesB;

	public List<Bone> bones;

	// Set to true if attribute buffers will need to change in runtime (using "dirty" flags).
	// Unless set to true internal typed arrays corresponding to buffers will be deleted once sent to GPU.
	// unless set to true the *Arrays will be deleted once sent to a buffer.
	public boolean dynamic = false; 

	public List<List<Integer>> __sortArray;
	
	public boolean verticesNeedUpdate;
	public boolean morphTargetsNeedUpdate;
	public boolean uvsNeedUpdate;
	public boolean normalsNeedUpdate;
	public boolean tangetsNeedUpdate;
	public boolean elementsNeedUpdate;
	public boolean colorsNeedUpdate;
	
	public Map<String, GeometryGroup> geometryGroups;
	public List<GeometryGroup> geometryGroupsList;
	
	public Geometry() {
		super();

		this.vertices = new ArrayList<Vector3f>();
		this.colors = new ArrayList<Color3f>(); // one-to-one vertex colors, used in ParticleSystem, Line and Ribbon

		this.faces = new ArrayList<Face3>();

		this.faceUvs = new ArrayList<List<UVf>>();
		this.faceVertexUvs = new ArrayList<List<List<UVf>>>();
		ArrayList<List<UVf>> firstChild = new ArrayList<List<UVf>>();
		this.faceVertexUvs.add(firstChild);

		this.morphTargets = new ArrayList<DimensionalObject>();
		this.morphNormals = new ArrayList<MorphNormal>();
		this.morphColors = new ArrayList<MorphColor>();

		this.skinWeights = new ArrayList<Vector4f>();
		this.skinIndices = new ArrayList<Vector4f>();

		this.boundingBox = null;
		this.boundingSphere = null;

		this.hasTangents = false;
		
		this.dynamic = false; 
	}

	public void setHasTangents(Boolean hasTangents) {
		this.hasTangents = hasTangents;
	}

	public Boolean getHasTangents() {
		return hasTangents;
	}

	public void setFaceUvs(List<List<UVf>> faceUvs) {
		this.faceUvs = faceUvs;
	}

	public List<List<UVf>> getFaceUvs() {
		return faceUvs;
	}

	public void setColors(ArrayList<Color3f> colors) {
		this.colors = colors;
	}

	public List<Color3f> getColors() {
		return colors;
	}

	public void setFaces(ArrayList<Face3> faces) {
		this.faces = faces;
	}

	/*
	 * Can be Face3 and Face4
	 */
	public List<Face3> getFaces() {
		return faces;
	}
	
	public List<Material> getMaterials() 
	{
		return this.materials;
	}
	
	public void setMaterials(List<Material> materials) 
	{
		this.materials = materials;
	}
	
	public void setVertices(List<Vector3f> vertices) 
	{
		this.vertices = vertices;
	}

	public List<Vector3f> getVertices() 
	{
		return vertices;
	}

	public void setBoundingSphere(BoundingSphere boundingSphere) 
	{
		this.boundingSphere = boundingSphere;
	}

	public BoundingSphere getBoundingSphere() {
		return boundingSphere;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setMorphTargets(List<DimensionalObject> morphTargets) {
		this.morphTargets = morphTargets;
	}

	public List<DimensionalObject> getMorphTargets() {
		return morphTargets;
	}
	
	public void setMorphNormals(List<MorphNormal> morphNormals) {
		this.morphNormals = morphNormals;
	}

	public List<MorphNormal> getMorphNormals() {
		return morphNormals;
	}
	
	public List<List<List<UVf>>> getFaceVertexUvs(){
		return this.faceVertexUvs;
	}

	public void computeCentroids(){
		for (Face3 face: this.faces) {
			face.getCentroid().set(0,0,0);

			if (face.getClass() == Face3.class) {
				Face3 face3 = (Face3)face;
				face3.getCentroid().add(this.vertices.get(face3.getA()));
				face3.getCentroid().add(this.vertices.get(face3.getB()));
				face3.getCentroid().add(this.vertices.get(face3.getC()));
				face3.getCentroid().divide(3);

			} else if (face.getClass() == Face4.class) {
				Face4 face4 = (Face4)face;
				face4.getCentroid().add(this.vertices.get(face4.getA()));
				face4.getCentroid().add(this.vertices.get(face4.getB()));
				face4.getCentroid().add(this.vertices.get(face4.getC()));
				face4.getCentroid().add(this.vertices.get(face4.getD()));
				face4.getCentroid().divide(4);
			}

		}
	}

	public void computeVertexNormals(){
		// create internal buffers for reuse when calling this method repeatedly
		// (otherwise memory allocation / deallocation every frame is big resource hog)

		if (this.tempVerticles == null) {

			this.tempVerticles = new ArrayList<Vector3f>(this.vertices.size());

			for (int v = 0, vl = this.vertices.size(); v < vl; v++){
				this.tempVerticles.add(v, new Vector3f());
			}

			for (Face3 face : this.faces) {

				if (face.getClass() == Face3.class){
					List<Vector3f> normals = face.getVertexNormals();
					normals.clear();
					normals.add(new Vector3f());
					normals.add(new Vector3f());
					normals.add(new Vector3f());
				} else if (face.getClass() == Face4.class) {
					List<Vector3f> normals = face.getVertexNormals();
					normals.clear();
					normals.add(new Vector3f());
					normals.add(new Vector3f());
					normals.add(new Vector3f());
					normals.add(new Vector3f());
				}
			}
		} else {
			for (int v = 0, vl = this.vertices.size(); v < vl; v++) {
				this.tempVerticles.get(v).set(0,0,0);
			}
		}

		for (Face3 face : this.faces) {
			if (face.getClass() == Face3.class) {
				Face3 face3 = face;
				this.tempVerticles.get(face3.getA()).add(face3.getNormal());
				this.tempVerticles.get(face3.getB()).add(face3.getNormal());
				this.tempVerticles.get(face3.getC()).add(face3.getNormal());

			} else if (face.getClass() == Face4.class) {
				Face4 face4 = (Face4)face;
				this.tempVerticles.get(face4.getA()).add(face4.getNormal());
				this.tempVerticles.get(face4.getB()).add(face4.getNormal());
				this.tempVerticles.get(face4.getC()).add(face4.getNormal());
				this.tempVerticles.get(face4.getD()).add(face4.getNormal());
			}
		}

		for (int v = 0, vl = this.vertices.size(); v < vl; v ++ ) {
			this.tempVerticles.get(v).normalize();
		}

		for (Face3 face : this.faces) {
			if (face.getClass() == Face3.class) {
				Face3 face3 = face;
				face3.getVertexNormals().get(0).copy(this.tempVerticles.get(face3.getA()));
				face3.getVertexNormals().get(1).copy(this.tempVerticles.get(face3.getB()));
				face3.getVertexNormals().get(2).copy(this.tempVerticles.get(face3.getC()));

			} else if (face.getClass() == Face4.class) {
				Face4 face4 = (Face4)face;
				face4.getVertexNormals().get(0).copy(this.tempVerticles.get(face4.getA()));
				face4.getVertexNormals().get(1).copy(this.tempVerticles.get(face4.getB()));
				face4.getVertexNormals().get(2).copy(this.tempVerticles.get(face4.getC()));
				face4.getVertexNormals().get(3).copy(this.tempVerticles.get(face4.getD()));
			}

		}
	}
	
	public void computeFaceNormals(Boolean useVertexNormals){
		Vector3f cb = new Vector3f(), ab = new Vector3f();

		for (Face3 face: this.faces) {
			if (useVertexNormals && face.getVertexNormals().size() > 0) {
				cb.set(0,0,0);
				for(Vector3f vertexNormal: face.getVertexNormals())
					cb.add(vertexNormal);

				cb.divide(3);
				if (!cb.isZero())
					cb.normalize();

				face.getNormal().copy(cb);
			} else {
				Vector3f vA = this.vertices.get(face.getA());
				Vector3f vB = this.vertices.get(face.getB());
				Vector3f vC = this.vertices.get(face.getC());

				cb.sub(vC, vB);
				ab.sub(vA, vB);
				cb.cross(ab);

				if (!cb.isZero())
					cb.normalize();

				face.getNormal().copy(cb);
			}
		}
	}
	
	public void computeTangents(){
		Face3 face;
		UVf[] uv = new UVf[0];
		int v, vl, f, fl, i, vertexIndex;
		List<Vector3f> tan1 = new ArrayList<Vector3f>(), 
				tan2 = new ArrayList<Vector3f>();
		Vector3f tmp = new Vector3f(), tmp2 = new Vector3f();

		for (v = 0,vl = this.vertices.size(); v<vl; v++) {
			tan1.add(v, new Vector3f());
			tan2.add(v, new Vector3f());
		}
		
		for (f = 0, fl = this.faces.size(); f < fl; f++) {

			face = this.faces.get(f);
			uv = this.faceVertexUvs.get(0).get(f).toArray(uv); // use UV layer 0 for tangents

			if (face.getClass() == Face3.class) {
				handleTriangle(face.getA(), face.getB(), face.getC(), 0, 1, 2, uv, tan1, tan2);

			} else if (face.getClass() == Face4.class) {
				Face4 face4 = (Face4)face;
				handleTriangle(face4.getA(), face4.getB(), face4.getC(), 0, 1, 2, uv, tan1, tan2);
				handleTriangle(face4.getA(), face4.getB(), face4.getD(), 0, 1, 3, uv, tan1, tan2);

			}
		}

		for (f = 0, fl = this.faces.size(); f < fl; f ++ ) {

			face = this.faces.get(f);

			for (i = 0; i < face.getVertexNormals().size(); i++) {

				Vector3f n = new Vector3f();
				n.copy(face.getVertexNormals().get(i));

				vertexIndex = face.getFlat()[i];

				Vector3f t = tan1.get(vertexIndex);

				// Gram-Schmidt orthogonalize

				tmp.copy(t);
				n.multiply(n.dot(t));
				tmp.sub(n);
				tmp.normalize();

				// Calculate handedness

				tmp2.cross(face.getVertexNormals().get(i), t);
				float test = tmp2.dot(tan2.get(vertexIndex));
				float w = (test < 0.0f) ? -1.0f : 1.0f;
				
				face.getVertexTangents().set(i, new Vector4f(tmp.x,tmp.y,tmp.z,w));
			}
		}

		this.hasTangents = true;
	}

	public void computeBoundingBox() {

		if ( this.boundingBox == null )
			this.boundingBox = new BoundingBox();

		if(this.vertices.size() == 0 )
		{
			this.boundingBox.min.set( 0, 0, 0 );
			this.boundingBox.max.set( 0, 0, 0 );
			return;
		}
		
			Vector3f firstPosition = this.vertices.get( 0 );

			this.boundingBox.min.copy( firstPosition );
			this.boundingBox.max.copy( firstPosition );

			Vector3f min = this.boundingBox.min;
			Vector3f max = this.boundingBox.max;

			for(Vector3f position: this.vertices) {
				if ( position.x < min.x ) {
					min.x = position.x;

				} else if ( position.x > max.x ) {
					max.x = position.x;
				}

				if ( position.y < min.y ) {
					min.y = position.y;
				} else if ( position.y > max.y ) {
					max.y = position.y;
				}

				if ( position.z < min.z ) {
					min.z = position.z;
				} else if ( position.z > max.z ) {
					max.z = position.z;
				}

			}

	}

	public void computeBoundingSphere(){
		float radius = this.boundingSphere == null ? 0 : this.boundingSphere.radius;
		for (int v = 0, vl = this.vertices.size(); v < vl; v++) {
			radius = Math.max(radius, this.vertices.get(v).length());
		}
		this.boundingSphere = new BoundingSphere(radius);
	}
	
	private String getHash(int a, int b){
		return Math.min(a, b) + "_" + Math.max(a, b);
	}
	
	private void handleTriangle(int a, int b, int c, int ua, int ub, int uc, UVf[] uv, List<Vector3f> tan1, List<Vector3f> tan2){
		Vector3f vA = this.vertices.get(a);
		Vector3f vB = this.vertices.get(b);
		Vector3f vC = this.vertices.get(c);
		
		UVf uvA = uv[ua];
		UVf uvB = uv[ub];
		UVf uvC = uv[uc];
		
		float x1 = vB.x - vA.x;
		float x2 = vC.x - vA.x;
		float y1 = vB.y - vA.y;
		float y2 = vC.y - vA.y;
		float z1 = vB.z - vA.z;
		float z2 = vC.z - vA.z;
		
		float s1 = uvB.getU() - uvA.getU();
		float s2 = uvC.getU() - uvA.getU();
		float t1 = uvB.getV() - uvA.getV();
		float t2 = uvC.getV() - uvA.getV();
		
		float r = 1.0f/(s1*t2-s2*t1);
		
		Vector3f sdir = new Vector3f();
		sdir.set((t2*x1-t1*x2)*r,
				  (t2*y1-t1*y2)*r,
				  (t2*z1-t1*z2)*r);
		
		Vector3f tdir = new Vector3f();
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


	/////////////////////////////////////////////////////
	// TODO: Check methods
	
	public void applyMatrix(Matrix4f matrix)
	{
		Matrix4f matrixRotation = new Matrix4f();
		matrixRotation.extractRotation(matrix);

		for(Vector3f verticle: this.vertices)
			matrix.multiplyVector3(verticle);

		for(Face3 face: this.faces) {
			matrixRotation.multiplyVector3(face.normal);
			for(Vector3f vertexNormal: face.vertexNormals)
				matrixRotation.multiplyVector3(vertexNormal);
			
			matrix.multiplyVector3(face.centroid);
		}		
	}




	// TODO: FIX ALL
	public void computeMorphNormals() {
		// save original normals
		// - create temp variables on first access
		//   otherwise just copy (for faster repeated calls)
//		for (Face3 face: this.faces) {		
//			if ( face.__originalFaceNormal == null ) {
//				face.__originalFaceNormal = face.normal.clone();
//			} else {
//				face.__originalFaceNormal.copy( face.normal );
//			}
//
//			if ( face.__originalVertexNormals == null ) 
//				face.__originalVertexNormals = new ArrayList<Vector3f>();
//
//			for (int i = 0; i < face.vertexNormals.size(); i++) {
//				if ( face.__originalVertexNormals.get( i ) == null) {
//					face.__originalVertexNormals.set( i, face.vertexNormals.get( i ).clone());
//				} else {
//					face.__originalVertexNormals.get( i ).copy( face.vertexNormals.get( i ) );
//				}
//			}
//		}
//
//		// use temp geometry to compute face and vertex normals for each morph
//		Geometry tmpGeo = new Geometry();
//		tmpGeo.faces = this.faces;

		// TODO: Fix this
//		for (int j = 0; j < this.morphTargets.size(); j++) {
//			// create on first access
//
//			if ( this.morphNormals.get( j ) == null ) {
//
//				this.morphNormals.set( j ,  new MorphNormal());
//				this.morphNormals.get( j ).faceNormals = new ArrayList<Vector3f>();
//				this.morphNormals.get( j ).vertexNormals = new ArrayList<Vector3f>();
//
//				List<Vector3f> dstNormalsFace = this.morphNormals.get( j ).faceNormals;
//				List<Vector3f> dstNormalsVertex = this.morphNormals.get(j).vertexNormals;
//
//				for (Face face: this.faces) {		
//					Vector3f faceNormal = new Vector3f();
//
//					if ( face instanceof Face3 ) {
//						vertexNormals = { a: new THREE.Vector3(), b: new THREE.Vector3(), c: new THREE.Vector3() };
//					} else {
//						vertexNormals = { a: new THREE.Vector3(), b: new THREE.Vector3(), c: new THREE.Vector3(), d: new THREE.Vector3() };
//					}
//
//					dstNormalsFace.push( faceNormal );
//					dstNormalsVertex.push( vertexNormals );
//
//				}
//
//			}
//
//			MorphNormal morphNormals = this.morphNormals.get(j);
//
//			// set vertices to morph target
//			tmpGeo.vertices = this.morphTargets[ i ].vertices;
//
//			// compute morph normals
//			tmpGeo.computeFaceNormals();
//			tmpGeo.computeVertexNormals();
//
//			// store morph normals
//
//			var faceNormal, vertexNormals;
//
//			for ( int f = 0, fl = this.faces.length; f < fl; f ++ ) {
//
//				Face face = this.faces[ f ];
//
//				faceNormal = morphNormals.faceNormals[ f ];
//				vertexNormals = morphNormals.vertexNormals[ f ];
//
//				faceNormal.copy( face.normal );
//
//				if ( face instanceof Face3 ) {
//
//					vertexNormals.a.copy( face.vertexNormals[ 0 ] );
//					vertexNormals.b.copy( face.vertexNormals[ 1 ] );
//					vertexNormals.c.copy( face.vertexNormals[ 2 ] );
//
//				} else {
//
//					vertexNormals.a.copy( face.vertexNormals[ 0 ] );
//					vertexNormals.b.copy( face.vertexNormals[ 1 ] );
//					vertexNormals.c.copy( face.vertexNormals[ 2 ] );
//					vertexNormals.d.copy( face.vertexNormals[ 3 ] );
//
//				}
//
//			}
//
//		}
//
//		// restore original normals
//		for ( int f = 0, fl = this.faces.length; f < fl; f ++ ) {
//			Face face = this.faces[ f ];
//			face.normal = face.__originalFaceNormal;
//			face.vertexNormals = face.__originalVertexNormals;
//		}

	}




	/*
	 * Checks for duplicate vertices with hashmap.
	 * Duplicated vertices are removed
	 * and faces' vertices are updated.
	 */
	public int mergeVertices() 
	{
		// Hashmap for looking up vertice by position coordinates (and making sure they are unique)
		Map<String, Integer> verticesMap = new HashMap<String, Integer>();
		List<Vector3f> unique = new ArrayList<Vector3f>();
		List<Integer> changes = new ArrayList<Integer>();

		// number of decimal points, eg. 4 for epsilon of 0.0001
		float precisionPoints = 4f; 
		float precision = (float) Math.pow( 10, precisionPoints );

		for ( int i = 0; i < this.vertices.size(); i ++ ) {
			Vector3f v = this.vertices.get( i );
			String key = Math.round( v.x * precision ) + "_" + Math.round( v.y * precision ) + "_"  + Math.round( v.z * precision );

			if ( !verticesMap.containsKey(key)) {
				verticesMap.put(key, i);
				unique.add(v);
				changes.add( i , unique.size() - 1);

			} else {
				//console.log('Duplicate vertex found. ', i, ' could be using ', verticesMap[key]);
				changes.add( i , changes.get( verticesMap.get( key ) ));
			}
		}


		// Start to patch face indices
		for ( int i = 0; i < this.faces.size(); i ++ ) {
			Face3 face = this.faces.get( i );

			if ( face.getClass() == Face3.class ) 
			{
				Face3 face3 = (Face3)face;
				face3.setA(changes.get( face3.getA() ));
				face3.setB(changes.get( face3.getB() ));
				face3.setC(changes.get( face3.getC() ));

			} else if ( face.getClass() == Face4.class ) 
			{
				Face4 face4 = (Face4)face;

				face4.setA(changes.get( face4.getA() ));
				face4.setB(changes.get( face4.getB() ));
				face4.setC(changes.get( face4.getC()));
				face4.setD(changes.get( face4.getD() ));
 
				// check dups in (a, b, c, d) and convert to -> face3
				List<Integer> o = Arrays.asList(face4.getA(), face4.getB(), face4.getC(), face4.getD());
				List<Integer> a = Arrays.asList(face4.getA(), face4.getB(), face4.getC(), face4.getD()); 

				for (int k=3; k>0; k--) 
				{
					if ( o.indexOf(a.get(k)) != k ) 
					{
						// console.log('faces', face.a, face.b, face.c, face.d, 'dup at', k);
						o.remove(k);
						this.faces.set( i, new Face3(o.get(0), o.get(1), o.get(2)));
						
						for (int j=0,jl = this.faceVertexUvs.size(); j<jl; j++) 
						{
							List<UVf> u = this.faceVertexUvs.get(j).get(i);
							if (u != null) 
								u.remove(k);
						}
						
						break;
					}
				}
			}
		}

		// Use unique set of vertices
		int diff = this.vertices.size() - unique.size();
		this.vertices = unique;
		return diff;
	}

}
