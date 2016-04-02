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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.core.geometry.MorphTarget;
import org.parallax3d.parallax.graphics.materials.HasSkinning;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MultiMaterial;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.system.gl.enums.BeginMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Mesh objects.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Mesh")
public class Mesh extends GeometryObject
{
	BeginMode drawMode = BeginMode.TRIANGLES;

	private static MeshBasicMaterial defaultMaterial = new MeshBasicMaterial();
	static {
		defaultMaterial.setColor( new Color((int)(Math.random() * 0xffffff)) );
		defaultMaterial.setWireframe( true );
	};

	Integer morphTargetBase = null;
	List<Double> morphTargetInfluences;
	FastMap<Integer> morphTargetDictionary;

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

	public Integer getMorphTargetBase() {
		return morphTargetBase;
	}

	public List<Double> getMorphTargetInfluences() {
		return morphTargetInfluences;
	}

	public FastMap<Integer> getMorphTargetDictionary() {
		return morphTargetDictionary;
	}

	public void setDrawMode(BeginMode drawMode) {
		this.drawMode = drawMode;
	}

	public BeginMode getDrawMode() {
		return drawMode;
	}

	public void updateMorphTargets() {

		if(this.getGeometry() instanceof BufferGeometry)
			return;

		if ( ((Geometry)this.getGeometry()).getMorphTargets() != null && ((Geometry)this.getGeometry()).getMorphTargets().size() > 0 ) {

			this.morphTargetBase = -1;
			this.morphTargetInfluences = new ArrayList<>();
			this.morphTargetDictionary = new FastMap<>();

			List<MorphTarget> morphTargets = ((Geometry)this.getGeometry()).getMorphTargets();
			for ( int m = 0, ml = ((Geometry)this.getGeometry()).getMorphTargets().size(); m < ml; m ++ ) {

				this.morphTargetInfluences.add( 0.0 );
				this.morphTargetDictionary.put(morphTargets.get(m).getName(), m);

			}

		}

	}

	public int getMorphTargetIndexByName( String name ) {

		if ( this.morphTargetDictionary.containsKey( name )) {

			return this.morphTargetDictionary.get( name );

		}

		Log.warn( "Mesh.getMorphTargetIndexByName: morph target " + name + " does not exist. Returning 0." );

		return 0;

	}

	static Matrix4 inverseMatrix = new Matrix4();
	static Ray ray = new Ray();
	static Sphere sphere = new Sphere();

	static Vector3 vA = new Vector3();
	static Vector3 vB = new Vector3();
	static Vector3 vC = new Vector3();

	static Vector3 tempA = new Vector3();
	static Vector3 tempB = new Vector3();
	static Vector3 tempC = new Vector3();

	static Vector2 uvA = new Vector2();
	static Vector2 uvB = new Vector2();
	static Vector2 uvC = new Vector2();

	static Vector3 barycoord = new Vector3();

	static Vector3 intersectionPoint = new Vector3();
	static Vector3 intersectionPointWorld = new Vector3();

	public void raycast( Raycaster raycaster, List<Raycaster.Intersect> intersects)
	{
		AbstractGeometry geometry = this.geometry;
		Material material = this.material;
		Matrix4 matrixWorld = this.getMatrixWorld();

		if ( material == null ) return;

		// Checking boundingSphere distance to ray

		if ( geometry.getBoundingSphere() == null ) geometry.computeBoundingSphere();

		sphere.copy( geometry.getBoundingSphere() );
		sphere.apply( matrixWorld );

		if (!raycaster.getRay().intersectsSphere(sphere)) return;

		//

		inverseMatrix.getInverse( matrixWorld );
		ray.copy( raycaster.getRay() ).apply( inverseMatrix );

		// Check boundingBox before continuing

		if ( geometry.getBoundingBox() != null ) {

			if (!ray.intersectsBox(geometry.getBoundingBox())) return;

		}

		Float32Array uvs = null;
		Raycaster.Intersect intersection;

		if ( geometry instanceof BufferGeometry )
		{

			AttributeData index = ((BufferGeometry) geometry).getIndex();
			FastMap<BufferAttribute> attributes = ((BufferGeometry) geometry).getAttributes();
			Float32Array positions = (Float32Array) attributes.get("position").getArray();

			if ( attributes.get("uv") != null ) {

				uvs = (Float32Array) attributes.get("uv").getArray();

			}

			if ( index != null ) {

				Int32Array indices = (Int32Array) index.getArray();

				for ( int i = 0, l = indices.getLength(); i < l; i += 3 ) {

					int a = indices.get( i );
					int b = indices.get( i + 1 );
					int c = indices.get( i + 2 );

					intersection = checkBufferGeometryIntersection( this, raycaster, ray, positions, uvs, a, b, c );

					if ( intersection != null ) {

						intersection.faceIndex = (int) Math.floor( i / 3 ); // triangle number in indices buffer semantics
						intersects.add( intersection );

					}

				}

			} else {

				for ( int i = 0, l = positions.getLength(); i < l; i += 9 ) {

					int a = i / 3;
					int b = a + 1;
					int c = a + 2;

					intersection = checkBufferGeometryIntersection( this, raycaster, ray, positions, uvs, a, b, c );

					if ( intersection != null ) {

						intersection.index = a; // triangle number in positions buffer semantics
						intersects.add( intersection );

					}

				}

			}

		} else if ( geometry instanceof Geometry ) {

			boolean isFaceMaterial = material instanceof MultiMaterial;
			List<Material> materials = isFaceMaterial ? ((MultiMaterial)material).getMaterials() : null;

			List<Vector3> vertices = ((Geometry) geometry).getVertices();
			List<Face3> faces =  ((Geometry) geometry).getFaces();
			List<List<Vector2>> faceVertexUvs = ((Geometry) geometry).getFaceVertexUvs().get(0);
			List<List<Vector2>> uvs2 = null;
			if ( faceVertexUvs.size() > 0 )
				uvs2 = faceVertexUvs;

			for ( int f = 0, fl = faces.size(); f < fl; f ++ )
			{

				Face3 face = faces.get( f );
				Material faceMaterial = isFaceMaterial ? materials.get(face.getMaterialIndex()) : material;

				if ( faceMaterial == null ) continue;

				Vector3 fvA = vertices.get(face.getA());
				Vector3 fvB = vertices.get(face.getB());
				Vector3 fvC = vertices.get(face.getC());

				if ( faceMaterial instanceof HasSkinning && ((HasSkinning)faceMaterial).isMorphTargets() ) {

					List<MorphTarget> morphTargets = ((Geometry) geometry).getMorphTargets();
					List<Double> morphInfluences = this.morphTargetInfluences;

					vA.set( 0, 0, 0 );
					vB.set( 0, 0, 0 );
					vC.set( 0, 0, 0 );

					for ( int t = 0, tl = morphTargets.size(); t < tl; t ++ ) {

						Double influence = morphInfluences.get(t);

						if ( influence == 0 ) continue;

						List<Vector3> targets = morphTargets.get(t).getVertices();

						vA.add( tempA.sub(targets.get(face.getA()), fvA ), influence );
						vB.add( tempB.sub(targets.get(face.getB()), fvB ), influence );
						vC.add( tempC.sub(targets.get(face.getC()), fvC ), influence );

					}

					vA.add( fvA );
					vB.add( fvB );
					vC.add( fvC );

					fvA = vA;
					fvB = vB;
					fvC = vC;

				}

				intersection = checkIntersection( this, raycaster, ray, fvA, fvB, fvC, intersectionPoint );

				if ( intersection!= null ) {

					if ( uvs2 != null ) {

						List<Vector2> uvs_f = uvs2.get(f);
						uvA.copy(uvs_f.get(0));
						uvB.copy(uvs_f.get(1));
						uvC.copy(uvs_f.get(2));

						intersection.uv = uvIntersection( intersectionPoint, fvA, fvB, fvC, uvA, uvB, uvC );

					}

					intersection.face = face;
					intersection.faceIndex = f;
					intersects.add( intersection );

				}

			}

		}

	}

	public Mesh clone() {

		return (Mesh) new Mesh( this.geometry, this.material ).copy( this );

	}

	private Raycaster.Intersect checkBufferGeometryIntersection( Mesh object, Raycaster raycaster, Ray ray,
																 Float32Array positions, Float32Array uvs, int a, int b, int c)
	{

		vA.fromArray( positions, a * 3 );
		vB.fromArray( positions, b * 3 );
		vC.fromArray( positions, c * 3 );

		Raycaster.Intersect intersection = checkIntersection( object, raycaster, ray, vA, vB, vC, intersectionPoint );

		if ( intersection != null ) {

			if ( uvs != null ) {

				uvA.fromArray( uvs, a * 2 );
				uvB.fromArray( uvs, b * 2 );
				uvC.fromArray( uvs, c * 2 );

				intersection.uv = uvIntersection( intersectionPoint,  vA, vB, vC,  uvA, uvB, uvC );

			}

			intersection.face = new Face3( a, b, c, Triangle.normal( vA, vB, vC ) );
			intersection.faceIndex = a;

		}

		return intersection;

	}

	private Vector2 uvIntersection( Vector3 point, Vector3 p1, Vector3 p2, Vector3 p3, Vector2 uv1, Vector2 uv2, Vector2 uv3 ) {

		Triangle.barycoordFromPoint( point, p1, p2, p3, barycoord );

		uv1.multiply(barycoord.getX());
		uv2.multiply(barycoord.getY());
		uv3.multiply(barycoord.getZ());

		uv1.add( uv2 ).add( uv3 );

		return uv1.clone();

	}

	private Raycaster.Intersect checkIntersection( Mesh object, Raycaster raycaster, Ray ray, Vector3 pA,
												   Vector3 pB, Vector3 pC, Vector3 point )
	{

		Vector3 intersect;
		Material material = object.material;

		if ( material.getSides() == Material.SIDE.BACK) {

			intersect = ray.intersectTriangle( pC, pB, pA, true, point );

		} else {

			intersect = ray.intersectTriangle( pA, pB, pC, material.getSides() != Material.SIDE.DOUBLE, point );

		}

		if ( intersect == null ) return null;

		intersectionPointWorld.copy( point );
		intersectionPointWorld.apply( object.getMatrixWorld() );

		double distance = raycaster.getRay().getOrigin().distanceTo( intersectionPointWorld );

		if ( distance < raycaster.getNear() || distance > raycaster.getFar()) return null;

		Raycaster.Intersect retval = new Raycaster.Intersect();
		retval.distance = distance;
		retval.point = intersectionPointWorld.clone();
		retval.object = object;

		return retval;
	}
}
