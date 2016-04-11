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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.geometry.DrawRange;
import org.parallax3d.parallax.graphics.core.geometry.Group;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.Points;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an efficient alternative to {@link Geometry}, because it stores all data, including vertex positions, 
 * face indices, normals, colors, UVs, and custom attributes within buffers; this reduces the cost of passing 
 * all this data to the GPU. This also makes BufferGeometry harder to work with than {@link Geometry}; rather than 
 * accessing position data as {@link Vector3} objects, color data as {@link Color} objects, and so on, you have to access 
 * the raw data from the appropriate attribute buffer {@link BufferAttribute}.  
 *
 *<pre>
 * BufferGeometry geometry = new BufferGeometry();
 *
 * int chunkSize = 20000;
 *
 * // three components per vertex
 * int triangles = 160000;
 * Uint16Array indices = Uint16Array.create( triangles * 3 );
 *
 * for ( int i = 0; i < indices.getLength(); i ++ ) 
 * {
 *		indices.put( i, i % ( 3 * chunkSize ));
 * }
 *
 * Float32Array positions = Float32Array.create( triangles * 3 * 3 );
 *
 * //itemSize = 3 because there are 3 values (components) per vertex
 * geometry.addAttribute( "position", new BufferAttribute( positions, 3 ) );
 *</pre>
 */
@ThreejsObject("BufferGeometry")
public class BufferGeometry extends AbstractGeometry
{
	public static long MaxIndex = 65535;

	AttributeData index;
	FastMap<BufferAttribute> attributes = new FastMap<>();
	FastMap<List<BufferAttribute>> morphAttributes = new FastMap<>();

	List<Group> groups = new ArrayList<>();

	DrawRange drawRange = new DrawRange(0, Integer.MAX_VALUE);

	public BufferGeometry() {
	}

	public AttributeData getIndex() {

		return this.index;

	}

	public void setIndex( AttributeData index ) {

		this.index = index;

	}

	public FastMap<BufferAttribute> getAttributes() {
		return this.attributes;
	}

	public FastMap<List<BufferAttribute>> getMorphAttributes() {
		return this.morphAttributes;
	}

	/**
	 * WebGL stores data associated with individual vertices of a geometry in attributes.
	 * Examples include the position of the vertex, the normal vector for the vertex, the vertex color, and so on.
	 *  When using {@link Geometry}, the renderer takes care of wrapping up this information into typed array buffers and sending this
	 *  data to the shader. With BufferGeometry, all of this data is stored in buffers associated with an individual attributes.
	 *  This means that to get the position data associated with a vertex (for instance), you must call .getAttribute() to access
	 *  the "position" attribute, then access the individual x, y, and z coordinates of the position.
	 *  <p>
	 *  The following attributes are set by various members of this class:
	 *
	 *  <ul>
	 *  	<li>position - Stores the x, y, and z coordinates of each vertex in this geometry. Set by .fromGeometry().</li>
	 *  	<li>normal - Stores the x, y, and z components of the face or vertex normal vector of each vertex in this geometry. Set by .fromGeometry().</li>
	 *  	<li>color - Stores the red, green, and blue channels of vertex color of each vertex in this geometry. Set by .fromGeometry().</li>
	 *      <li>tangent - Stores the x, y, and z components of the tangent vector of each vertex in this geometry. Set by .computeTangents().</li>
	 *  </ul>
	 * @param name Attribute name
	 * @param attribute
	 */
	public void addAttribute( String name, BufferAttribute attribute ) {

		this.attributes.put( name, attribute );

	}

	public void removeAttribute( String name ) {

		this.attributes.remove( name );

	}

	public BufferAttribute getAttribute( String name ) {

		return this.attributes.get( name );

	}

	public List<Group> getGroups() {
		return groups;
	}

	public BufferGeometry addGroup( int start, int count ) {
		return addGroup( start, count, 0);
	}

	public BufferGeometry addGroup( int start, int count, int materialIndex )
	{
		this.groups.add( new Group(start, count, materialIndex));
		return this;
	}

	public BufferGeometry clearGroups()
	{
		this.groups = new ArrayList<>();
		return this;
	}

	/**
	 * For geometries that use indexed triangles, this Array can be used to split the object into multiple WebGL draw calls.
	 * Each draw call will draw some subset of the vertices in this geometry using the configured shader.
	 * This may be necessary if, for instance, you have more than 65535 vertices in your object.
	 * @return
	 */
	public DrawRange getDrawRange() {
		return drawRange;
	}

	public void setDrawRange( int start, int count ) {
		this.drawRange = new DrawRange(start, count);
	}
	/**
	 * @param drawRange the drawcalls to set
	 */
	public void setDrawRange(DrawRange drawRange) {
		this.drawRange = drawRange;
	}

	/**
	 * Bakes matrix transform directly into vertex coordinates.
	 * @param matrix
	 */
	public BufferGeometry applyMatrix( Matrix4 matrix ) {

		BufferAttribute position = getAttribute("position");

		if ( position != null ) {

			matrix.applyToVector3Array( (Float32Array) position.getArray() );
			position.setNeedsUpdate( true );

		}

		BufferAttribute normal =  getAttribute("normal");

		if ( normal != null ) {

			Matrix3 normalMatrix = new Matrix3().getNormalMatrix( matrix );

			normalMatrix.applyToVector3Array( (Float32Array) normal.getArray() );
			normal.setNeedsUpdate( true );

		}

		if ( this.boundingBox != null ) {

			this.computeBoundingBox();

		}

		if ( this.boundingSphere != null ) {

			this.computeBoundingSphere();

		}

		return this;
	}

	/**
	 * rotate geometry around world x-axis
	 * @param angle
	 * @return
     */
    private static final Matrix4 m1 = new Matrix4();
    public BufferGeometry rotateX( double angle ) {

		m1.makeRotationX( angle );

		this.applyMatrix( m1 );

		return this;

	}

	/**
	 * rotate geometry around world y-axis
	 * @param angle
	 * @return
     */
    private static final Matrix4 m2 = new Matrix4();
	public BufferGeometry rotateY( double angle ) {

		m2.makeRotationY( angle );

		this.applyMatrix( m2 );

		return this;

	}

	/**
	 * rotate geometry around world z-axis
	 * @param angle
	 * @return
     */
    private static final Matrix4 m3 = new Matrix4();
	public BufferGeometry rotateZ( double angle ) {

		m3.makeRotationZ( angle );

		this.applyMatrix( m3 );

		return this;

	}

    private static final Matrix4 m4 = new Matrix4();
	public BufferGeometry translate( double x, double y, double z ) {

		m4.makeTranslation( x, y, z );

		this.applyMatrix( m4 );

		return this;

	}

    private static final Matrix4 m5 = new Matrix4();
	public BufferGeometry scale( double x, double y, double z ) {

		m5.makeScale( x, y, z );
	
		this.applyMatrix( m5 );
	
		return this;
	
	}
	
	static final Object3D obj1 = new Object3D();
	public BufferGeometry lookAt( Vector3 vector ) {
		
		obj1.lookAt( vector );

		obj1.updateMatrix();

		this.applyMatrix( obj1.matrix );

		return this;
	}

	public Vector3 center() {

		this.computeBoundingBox();
	
		Vector3 offset = this.boundingBox.center().negate();
	
		this.translate( offset.getX(), offset.getY(), offset.getZ());
	
		return offset;
	
	}

	public BufferGeometry setFromObject( GeometryObject object ) {
		
		Geometry geometry = (Geometry) object.geometry;
	
		if ( geometry instanceof Geometry && (object instanceof Points || object instanceof Line)) {

			BufferAttribute positions = BufferAttribute.Float32Attribute( geometry.vertices.size() * 3, 3 );
			BufferAttribute colors = BufferAttribute.Float32Attribute( geometry.colors.size() * 3, 3 );
	
			this.addAttribute( "position", positions.copyVector3sArray( geometry.vertices ) );
			this.addAttribute( "color", colors.copyColorsArray( geometry.colors ) );
	
			if ( ((Geometry) geometry).getLineDistances() != null
					&& ((Geometry) geometry).getLineDistances().size() == geometry.vertices.size()  ) {

				BufferAttribute lineDistances = BufferAttribute.Float32Attribute( geometry.getLineDistances().size(), 1 );
	
				this.addAttribute( "lineDistance", lineDistances.copyArray( ((Geometry) geometry).getLineDistances() ) );
	
			}
	
			if ( geometry.boundingSphere != null ) {
	
				this.boundingSphere = geometry.boundingSphere.clone();
	
			}
	
			if ( geometry.boundingBox != null ) {
	
				this.boundingBox = geometry.boundingBox.clone();
	
			}
	
		} else if ( object instanceof Mesh) {
	
			if ( geometry instanceof Geometry ) {
	
				this.fromGeometry( geometry );
	
			}
	
		}
	
		return this;
	
	}

	public BufferGeometry updateFromObject( GeometryObject object ) {

		DirectGeometry geometry = null;

		if ( object instanceof Mesh ) {

			DirectGeometry direct = ((Geometry)object.geometry).__directGeometry;

			if ( direct == null ) {

				return this.fromGeometry( ((Geometry)object.geometry) );

			}

			direct.verticesNeedUpdate = object.geometry.verticesNeedUpdate;
			direct.normalsNeedUpdate = object.geometry.normalsNeedUpdate;
			direct.colorsNeedUpdate = object.geometry.colorsNeedUpdate;
			direct.uvsNeedUpdate = object.geometry.uvsNeedUpdate;
			direct.groupsNeedUpdate = object.geometry.groupsNeedUpdate;

			object.geometry.verticesNeedUpdate = false;
			object.geometry.normalsNeedUpdate = false;
			object.geometry.colorsNeedUpdate = false;
			object.geometry.uvsNeedUpdate = false;
			object.geometry.groupsNeedUpdate = false;

			geometry = direct;

		}

		if (geometry.verticesNeedUpdate) {

			BufferAttribute attribute = this.attributes.get("position");

			if ( attribute != null ) {

				attribute.copyVector3sArray( geometry.vertices );
				attribute.setNeedsUpdate(true);

			}

			geometry.verticesNeedUpdate = false;

		}

		if (geometry.normalsNeedUpdate) {

			BufferAttribute attribute = this.attributes.get("normal");

			if ( attribute != null ) {

				attribute.copyVector3sArray( geometry.normals );
				attribute.setNeedsUpdate(true);

			}

			geometry.normalsNeedUpdate = false;

		}

		if (geometry.colorsNeedUpdate) {

			BufferAttribute attribute = this.attributes.get("color");

			if ( attribute != null ) {

				attribute.copyColorsArray( geometry.colors );
				attribute.setNeedsUpdate(true);

			}

			geometry.colorsNeedUpdate = false;

		}

		if ( geometry.uvsNeedUpdate ) {

			BufferAttribute attribute = this.attributes.get("uv");

			if ( attribute != null ) {

				attribute.copyVector2sArray( geometry.uvs );
				attribute.setNeedsUpdate(true);

			}

			geometry.uvsNeedUpdate = false;

		}

//		if ( geometry.lineDistancesNeedUpdate ) {
//
//			BufferAttribute attribute = this.attributes.get("lineDistance");
//
//			if ( attribute != null ) {
//
//				attribute.copyArray(geometry.getLineDistances());
//				attribute.setNeedsUpdate(true);
//
//			}
//
//			geometry.lineDistancesNeedUpdate = false;
//
//		}

		if ( geometry.groupsNeedUpdate ) {

			geometry.computeGroups((Geometry) object.geometry);
			this.groups = geometry.groups;

			geometry.groupsNeedUpdate = false;

		}

		return this;

	}

	/**
	 * Populates this BufferGeometry with data from a {@link Geometry} object.
	 * @param geometry
	 * @return
	 */
	public BufferGeometry fromGeometry( Geometry geometry ) {

		geometry.__directGeometry = new DirectGeometry().fromGeometry( geometry );

		return this.fromDirectGeometry( geometry.__directGeometry );
	}

	public BufferGeometry fromDirectGeometry ( DirectGeometry geometry ) {

		Float32Array positions = Float32Array.create( geometry.vertices.size() * 3 );
		this.addAttribute( "position", new BufferAttribute( positions, 3 ).copyVector3sArray( geometry.vertices ) );
	
		if ( geometry.normals.size() > 0 ) {

			Float32Array normals = Float32Array.create( geometry.normals.size() * 3 );
			this.addAttribute( "normal", new BufferAttribute( normals, 3 ).copyVector3sArray( geometry.normals ) );
	
		}
	
		if ( geometry.colors.size() > 0 ) {

			Float32Array colors = Float32Array.create( geometry.colors.size() * 3 );
			this.addAttribute( "color", new BufferAttribute( colors, 3 ).copyColorsArray( geometry.colors ) );
	
		}
	
		if ( geometry.uvs.size() > 0 ) {

			Float32Array uvs = Float32Array.create( geometry.uvs.size() * 2 );
			this.addAttribute( "uv", new BufferAttribute( uvs, 2 ).copyVector2sArray( geometry.uvs ) );
	
		}
	
		if ( geometry.uvs2.size() > 0 ) {

			Float32Array uvs2 = Float32Array.create( geometry.uvs2.size() * 2 );
			this.addAttribute( "uv2", new BufferAttribute( uvs2, 2 ).copyVector2sArray( geometry.uvs2 ) );
	
		}
	
		if ( geometry.indices.size() > 0 ) {

			Uint32Array indices = Uint32Array.create(geometry.indices.size() * 3);
			this.setIndex( new BufferAttribute( indices, 1 ).copyIndicesArray( geometry.indices ) );
	
		}
	
		// groups
	
		this.groups = geometry.groups;
	
		// morphs
	
		for ( String name : geometry.morphTargets.keySet() ) {
	
			List<BufferAttribute> array = new ArrayList<>();
			List<List<Vector3>> morphTargets = geometry.morphTargets.get( name );
	
			for ( int i = 0, l = morphTargets.size(); i < l; i ++ ) {
	
				List<Vector3> morphTarget = morphTargets.get( i );

				BufferAttribute attribute = BufferAttribute.Float32Attribute( morphTarget.size() * 3, 3 );
	
				array.add( attribute.copyVector3sArray( morphTarget ) );
	
			}
	
			this.morphAttributes.put( name , array);
	
		}
	
		// skinning
	
		if ( geometry.skinIndices.size() > 0 ) {

			BufferAttribute skinIndices = BufferAttribute.Float32Attribute( geometry.skinIndices.size() * 4, 4 );
			this.addAttribute( "skinIndex", skinIndices.copyVector4sArray( geometry.skinIndices ) );
	
		}
	
		if ( geometry.skinWeights.size() > 0 ) {

			BufferAttribute skinWeights = BufferAttribute.Float32Attribute( geometry.skinWeights.size() * 4, 4 );
			this.addAttribute( "skinWeight", skinWeights.copyVector4sArray( geometry.skinWeights ) );
	
		}
	
		//
	
		if ( geometry.boundingSphere != null ) {
	
			this.boundingSphere = geometry.boundingSphere.clone();
	
		}
	
		if ( geometry.boundingBox != null ) {
	
			this.boundingBox = geometry.boundingBox.clone();
	
		}
	
		return this;
	
	}

	/**
	 * Computes bounding box of the geometry, updating Geometry.boundingBox attribute.
	 * Bounding boxes aren"t computed by default. They need to be explicitly computed, otherwise they are null.
	 */
	@Override
	public void computeBoundingBox() {

		if ( this.boundingBox == null ) {

			this.boundingBox = new Box3();

		}

		Float32Array positions = (Float32Array) getAttribute("position").getArray();

		if ( positions != null ) {

			this.boundingBox.setFromArray( positions );

		}

		if ( positions == null || positions.getLength() == 0 ) {

			this.boundingBox.getMin().set( 0, 0, 0 );
			this.boundingBox.getMax().set( 0, 0, 0 );

		}

	}

	/**
	 * Computes bounding sphere of the geometry, updating Geometry.boundingSphere attribute.
	 * Bounding spheres aren"t computed by default. They need to be explicitly computed, otherwise they are null.
	 */
	static final Box3 box = new Box3();
	static final Vector3 vector = new Vector3();
	public void computeBoundingSphere() {

		if ( this.boundingSphere == null ) {

			this.boundingSphere = new Sphere();

		}

		Float32Array positions = (Float32Array) getAttribute("position").getArray();

		if ( positions != null ) {

			Vector3 center = this.boundingSphere.getCenter();

			box.setFromArray( positions );
			box.center( center );

			// hoping to find a boundingSphere with a radius smaller than the
			// boundingSphere of the boundingBox: sqrt(3) smaller in the best case

			double maxRadiusSq = 0;

			for ( int i = 0, il = positions.getLength(); i < il; i += 3 ) {

				vector.fromArray( positions, i );
				maxRadiusSq = Math.max( maxRadiusSq, center.distanceToSquared( vector ) );

			}

			this.boundingSphere.setRadius(Math.sqrt(maxRadiusSq));

		}

	}

	public void computeFaceNormals() {

	}

	/**
	 * Computes vertex normals by averaging face normals.
	 */
	public void computeVertexNormals() {

		AttributeData index = this.index;
		FastMap<BufferAttribute> attributes = this.attributes;
		List<Group> groups = this.groups;

		if ( attributes.containsKey("position") ) {

			Float32Array positions = (Float32Array) attributes.get("position").getArray();

			if ( !attributes.containsKey("normal")) {

				this.addAttribute( "normal", new BufferAttribute( Float32Array.create( positions.getLength() ), 3 ) );

			} else {

				// reset existing normals to zero

				Float32Array array = (Float32Array) attributes.get("normal").getArray();

				for ( int i = 0, il = array.getLength(); i < il; i ++ ) {

					array.set( i , 0);

				}

			}

			Float32Array normals = (Float32Array)attributes.get("normal").getArray();

			Vector3 pA = new Vector3(),
					pB = new Vector3(),
					pC = new Vector3(),

					cb = new Vector3(),
					ab = new Vector3();

			// indexed elements

			if ( index != null ) {

                Uint32Array indices = (Uint32Array) index.array;

				if ( groups.size() == 0 ) {

					this.addGroup( 0, indices.getLength() );

				}

				for ( int j = 0, jl = groups.size(); j < jl; ++ j ) {

					Group group = groups.get(j);

					int start = group.getStart();
					int count = group.getCount();

					for ( int i = start, il = start + count; i < il; i += 3 ) {

						int vA = indices.get( i + 0 ) * 3;
						int vB = indices.get( i + 1 ) * 3;
						int vC = indices.get( i + 2 ) * 3;

						pA.fromArray( positions, vA );
						pB.fromArray( positions, vB );
						pC.fromArray( positions, vC );

						cb.sub( pC, pB );
						ab.sub( pA, pB );
						cb.cross( ab );

						normals.set( vA , normals.get( vA ) + cb.getX() );
						normals.set( vA + 1 , normals.get( vA + 1 ) + cb.getY() );
						normals.set( vA + 2 , normals.get( vA + 2 ) + cb.getZ() );

						normals.set( vB , normals.get( vB ) + cb.getX() );
						normals.set( vB + 1 , normals.get( vB + 1 ) + cb.getY() );
						normals.set( vB + 2 , normals.get( vB + 2 ) + cb.getZ() );

						normals.set( vC , normals.get( vC ) + cb.getX() );
						normals.set( vC + 1 , normals.get( vC + 1 ) + cb.getY() );
						normals.set( vC + 2 , normals.get( vC + 2 ) + cb.getZ() );

					}

				}

			} else {

				// non-indexed elements (unconnected triangle soup)
				for ( int i = 0, il = positions.getLength(); i < il; i += 9 ) {

					pA.fromArray( positions, i );
					pB.fromArray( positions, i + 3 );
					pC.fromArray( positions, i + 6 );

					cb.sub( pC, pB );
					ab.sub( pA, pB );
					cb.cross( ab );

					normals.set( i , cb.getX() );
					normals.set( i + 1 , cb.getY() );
					normals.set( i + 2 , cb.getZ() );

					normals.set( i + 3 , cb.getX() );
					normals.set( i + 4 , cb.getY() );
					normals.set( i + 5 , cb.getZ() );

					normals.set( i + 6 , cb.getX() );
					normals.set( i + 7 , cb.getY() );
					normals.set( i + 8 , cb.getZ() );

				}

			}

			this.normalizeNormals();

			attributes.get("normal").setNeedsUpdate(true);

		}

	}

	public BufferGeometry merge( BufferGeometry geometry )
	{
		return merge(geometry, 0);
	}

	public BufferGeometry merge( BufferGeometry geometry, int offset ) {

		FastMap<BufferAttribute> attributes = this.attributes;

		for ( String key : attributes.keySet() ) {

			if (!geometry.attributes.containsKey( key ) ) continue;

			BufferAttribute attribute1 = attributes.get( key );
			Float32Array attributeArray1 = (Float32Array) attribute1.array;

			BufferAttribute attribute2 = geometry.attributes.get( key );
			Float32Array attributeArray2 = (Float32Array) attribute2.array;

			int attributeSize = attribute2.itemSize;

			for ( int i = 0, j = attributeSize * offset; i < attributeArray2.getLength(); i ++, j ++ ) {

				attributeArray1.set( j , attributeArray2.get( i ));

			}

		}

		return this;

	}

	/**
	 * Every normal vector in a geometry will have a magnitude of 1. This will correct lighting on the geometry surfaces.
	 */
	public void normalizeNormals() {

		Float32Array normals = (Float32Array)getAttribute("normal").getArray();

		double x, y, z, n;

		for ( int i = 0, il = normals.getLength(); i < il; i += 3 ) {

			x = normals.get( i );
			y = normals.get( i + 1 );
			z = normals.get( i + 2 );

			n = 1.0 / Math.sqrt( x * x + y * y + z * z );

			normals.set( i     , normals.get( i     ) * n);
			normals.set( i + 1 , normals.get( i + 1 ) * n);
			normals.set( i + 2 , normals.get( i + 2 ) * n);

		}

	}

	BufferGeometry toNonIndexed() {

		if (this.index == null) {

			Log.warn("BufferGeometry.toNonIndexed(): Geometry is already non-indexed.");

			return this;
		}

		BufferGeometry geometry2 = new BufferGeometry();

        Uint32Array indices = (Uint32Array) this.index.array;
		FastMap<BufferAttribute> attributes = this.attributes;

		for (String name : attributes.keySet()) {

			BufferAttribute attribute = attributes.get(name);

			Float32Array array = (Float32Array) attribute.array;
			int itemSize = attribute.itemSize;

			Float32Array array2 = Float32Array.create(indices.getLength() * itemSize);

			int index = 0, index2 = 0;

			for (int i = 0, l = indices.getLength(); i < l; i++) {

				index = indices.get(i) * itemSize;

				for (int j = 0; j < itemSize; j++) {

					array2.set(index2++, array.get(index++));

				}

			}

			geometry2.addAttribute(name, new BufferAttribute(array2, itemSize));

		}

		return geometry2;

	}

	public BufferGeometry copy( BufferGeometry source ) {

		AttributeData index = source.index;

		if ( index != null ) {

			this.setIndex( index.clone() );

		}

		FastMap<BufferAttribute> attributes = source.attributes;

		for ( String name : attributes.keySet() ) {

			BufferAttribute attribute = attributes.get( name );
			this.addAttribute( name, attribute.clone() );

		}

		List<Group> groups = source.groups;

		for ( int i = 0, l = groups.size(); i < l; i ++ ) {

			Group group = groups.get(i);
			this.addGroup(group.getStart(), group.getCount());

		}

		return this;

	}

	public BufferGeometry clone() {

		return new BufferGeometry().copy( this );

	}

	public String toString() {
		return getClass().getSimpleName()
				+ "{id: " + getId() + "}";
	}
}
