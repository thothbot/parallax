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
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Int16Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.math.Box3;
import thothbot.parallax.core.shared.math.Sphere;
import thothbot.parallax.core.shared.math.Vector3;

public class GeometryBuffer implements Geometric
{
	public static class Offset 
	{
		public int start;
		public int count;
		public int index;	
	}

	public static int Counter = 0;
	private int id = 0;
	
	// the intermediate typearrays will be deleted when set to false
	private boolean isDynamic = true; 
	
	// Bounding box.		
	protected Box3 boundingBox = null;

	// Bounding sphere.
	protected Sphere boundingSphere = null;
	
	// True if geometry has tangents. Set in Geometry.computeTangents.
	private Boolean hasTangents = false;

	private boolean isNormalsNeedUpdate;
	private boolean isTangentsNeedUpdate;
	private boolean isElementsNeedUpdate;
	private boolean isVerticesNeedUpdate;
	private boolean isUvsNeedUpdate;
	private boolean isColorsNeedUpdate;
	
	private boolean isBuffersNeedUpdate;

	public List<GeometryBuffer.Offset> offsets;
	
	private boolean isArrayInitialized;
	
	private Uint16Array webGlIndexArray;
	private Uint16Array webGlFaceArray;
	private Uint16Array webGlLineArray;
	
	private Float32Array webGlColorArray;
	private Float32Array webGlVertexArray;
	private Float32Array webGlNormalArray;
	private Float32Array webGlTangentArray;
	private Float32Array webGlUvArray;
	private Float32Array webGlUv2Array;
	
	public WebGLBuffer __webglIndexBuffer;
	public WebGLBuffer __webglFaceBuffer;
	public WebGLBuffer __webglLineBuffer;
	
	public WebGLBuffer __webglColorBuffer;
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public WebGLBuffer __webglTangentBuffer;
	public WebGLBuffer __webglUVBuffer;
	public WebGLBuffer __webglUV2Buffer;
	
	public WebGLBuffer __webglSkinIndicesBuffer;
	public WebGLBuffer __webglSkinWeightsBuffer;
		
	public int numMorphTargets;
	public List<WebGLBuffer> __webglMorphTargetsBuffers;
	
	public int numMorphNormals;
	public List<WebGLBuffer> __webglMorphNormalsBuffers;
	
	public List<Attribute> __webglCustomAttributesList;
	
	public int __webglParticleCount;
	public int __webglLineCount;
	public int __webglVertexCount;
	public int __webglFaceCount;
		
	public GeometryBuffer() 
	{
		this.id = GeometryBuffer.Counter++;
		
		this.boundingBox = null;
		this.boundingSphere = null;
	}
		
	public void setId(int id) 
	{
		this.id = id;
	}

	/**
	 * Gets the Unique number of this geometry instance
	 */
	public int getId() 
	{
		return id;
	}
	
	public boolean isDynamic() {
		return this.isDynamic;
	}
	
	/**
	 * Set to true if attribute buffers will need to change in runtime (using "dirty" flags).
	 * Unless set to true internal typed arrays corresponding to buffers will be deleted once sent to GPU.
	 */
	public void setDynamic(boolean dynamic) {
		this.isDynamic = dynamic;
	}
	
	/**
	 * Gets True if geometry has tangents. {@link Geometry#computeTangents()} 
	 */
	public Boolean hasTangents() {
		return hasTangents;
	}
	
	public void setHasTangents(Boolean hasTangents) {
		this.hasTangents = hasTangents;
	}
	
	public boolean isElementsNeedUpdate() {
		return isElementsNeedUpdate;
	}

	public void setElementsNeedUpdate(boolean isElementsNeedUpdate) {
		this.isElementsNeedUpdate = isElementsNeedUpdate;
	}
	
	public boolean isNormalsNeedUpdate() {
		return isNormalsNeedUpdate;
	}

	public void setNormalsNeedUpdate(boolean isNormalsNeedUpdate) {
		this.isNormalsNeedUpdate = isNormalsNeedUpdate;
	}
	
	public boolean isTangentsNeedUpdate() {
		return isTangentsNeedUpdate;
	}

	public void setTangentsNeedUpdate(boolean isTangentsNeedUpdate) {
		this.isTangentsNeedUpdate = isTangentsNeedUpdate;
	}
	
	public boolean isVerticesNeedUpdate() {
		return isVerticesNeedUpdate;
	}

	public void setVerticesNeedUpdate(boolean isVerticesNeedUpdate) {
		this.isVerticesNeedUpdate = isVerticesNeedUpdate;
	}
	
	public boolean isUvsNeedUpdate() {
		return isUvsNeedUpdate;
	}

	public void setUvsNeedUpdate(boolean isUvsNeedUpdate) {
		this.isUvsNeedUpdate = isUvsNeedUpdate;
	}

	public boolean isColorsNeedUpdate() {
		return isColorsNeedUpdate;
	}

	public void setColorsNeedUpdate(boolean isColorsNeedUpdate) {
		this.isColorsNeedUpdate = isColorsNeedUpdate;
	}

	public void setBoundingSphere(Sphere boundingSphere) 
	{
		this.boundingSphere = boundingSphere;
	}

	public Sphere getBoundingSphere() {
		return boundingSphere;
	}

	public void setBoundingBox(Box3 boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Box3 getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Check if WebGl arrays initialized
	 */
	public boolean isArrayInitialized() 
	{
		return isArrayInitialized;
	}

	public void setArrayInitialized(boolean isArrayInitialized) 
	{
		this.isArrayInitialized = isArrayInitialized;
	}
	
	public Float32Array getWebGlVertexArray()
	{
		return this.webGlVertexArray;
	}
	
	public Float32Array getWebGlColorArray()
	{
		return this.webGlColorArray;
	}
	
	public void setWebGlColorArray(Float32Array a)
	{
		this.webGlColorArray = a;
	}
	
	public void setWebGlVertexArray(Float32Array a)
	{
		this.webGlVertexArray = a;
	}
	
	public void setWebGlNormalArray(Float32Array a)
	{
		this.webGlNormalArray = a;
	}
	
	public void setWebGlTangentArray(Float32Array a)
	{
		this.webGlTangentArray = a;
	}
	
	public void setWebGlUvArray(Float32Array a)
	{
		this.webGlUvArray = a;
	}
	
	public void setWebGlUv2Array(Float32Array a)
	{
		this.webGlUv2Array = a;
	}
	
	public Float32Array getWebGlNormalArray() 
	{
		return webGlNormalArray;
	}

	public Float32Array getWebGlTangentArray() 
	{
		return webGlTangentArray;
	}

	public Float32Array getWebGlUvArray() 
	{
		return webGlUvArray;
	}

	public Float32Array getWebGlUv2Array() 
	{
		return webGlUv2Array;
	}
	
	public Uint16Array getWebGlIndexArray() 
	{
		return webGlIndexArray;
	}
	
	public void setWebGlIndexArray(Uint16Array a)
	{
		this.webGlIndexArray = a;
	}
	
	public Uint16Array getWebGlFaceArray() 
	{
		return webGlFaceArray;
	}

	public Uint16Array getWebGlLineArray() 
	{
		return webGlLineArray;
	}
	
	public void setWebGlFaceArray(Uint16Array a)
	{
		this.webGlFaceArray = a;
	}
	
	public void setWebGlLineArray(Uint16Array a)
	{
		this.webGlLineArray = a;
	}
	
	protected void dispose() 
	{
		setArrayInitialized(false);
		
		setWebGlIndexArray( null );
		setWebGlFaceArray( null );
		setWebGlLineArray( null );
		

		setWebGlColorArray ( null );
		setWebGlVertexArray( null );

		setWebGlNormalArray( null );
		setWebGlTangentArray( null );
		setWebGlUvArray( null );
		setWebGlUv2Array( null );		
	}
	
	@Override
	public void computeBoundingBox() 
	{
		if ( getBoundingBox() == null) 
		{
			this.boundingBox = new Box3();
		}
		
		Float32Array positions = getWebGlVertexArray();

		if ( positions != null) 
		{
			Box3 bb = this.boundingBox;
			if( positions.getLength() >= 3 ) 
			{
				bb.getMin().setX(positions.get( 0 )); 
				bb.getMax().setX(positions.get( 0 ));
				bb.getMin().setY(positions.get( 1 )); 		
				bb.getMax().setY(positions.get( 1 ));
				bb.getMin().setZ(positions.get( 2 ));
				bb.getMax().setZ(positions.get( 2 ));
			}
			
			for ( int i = 3, il = positions.getLength(); i < il; i += 3  ) 
			{
				double x = positions.get( i );
				double y = positions.get( i + 1 );
				double z = positions.get( i + 2 );

				// bounding box

				if ( x < bb.getMin().getX() ) {
					bb.getMin().setX( x );

				} else if ( x > bb.getMax().getX() ) {
					bb.getMax().setX( x );
				}

				if ( y < bb.getMin().getY() ) {
					bb.getMin().setY( y );

				} else if ( y > bb.getMax().getY() ) {
					bb.getMax().setY( y );
				}

				if ( z < bb.getMin().getZ() ) {
					bb.getMin().setZ( z );

				} else if ( z > bb.getMax().getZ() ) {
					bb.getMax().setZ( z );
				}
			}
		}

		if ( positions == null || positions.getLength() == 0 ) 
		{
			this.boundingBox.getMin().set( 0, 0, 0 );
			this.boundingBox.getMax().set( 0, 0, 0 );
		}
	}
	
	@Override
	public void computeBoundingSphere() 
	{
		if ( this.boundingSphere == null ) 
		{
			this.boundingSphere = new Sphere();
		}

		Float32Array positions = getWebGlVertexArray();

		if ( positions != null) 
		{
			double maxRadiusSq = 0;

			for ( int i = 0, il = positions.getLength(); i < il; i += 3 ) 
			{
				double x = positions.get( i );
				double y = positions.get( i + 1 );
				double z = positions.get( i + 2 );

				double radiusSq =  x * x + y * y + z * z;
				if ( radiusSq > maxRadiusSq ) maxRadiusSq = radiusSq;

			}

			this.boundingSphere.setRadius( Math.sqrt( maxRadiusSq ) );
		}
	}
	
	@Override
	public void computeVertexNormals() 
	{
		if ( getWebGlVertexArray() != null && getWebGlIndexArray() != null ) 
		{
			int nVertexElements = getWebGlVertexArray().getLength();

			if ( getWebGlNormalArray() == null ) 
			{
				setWebGlNormalArray(Float32Array.create(nVertexElements));
			}
			else 
			{
				// reset existing normals to zero

				for ( int i = 0, il = getWebGlNormalArray().getLength(); i < il; i ++ ) 
				{
					getWebGlNormalArray().set( i, 0 );
				}
			}

			List<GeometryBuffer.Offset> offsets = this.offsets;

			Uint16Array indices = getWebGlIndexArray();
			Float32Array positions = getWebGlVertexArray();
			Float32Array normals = getWebGlNormalArray();

			Vector3 pA = new Vector3();
			Vector3 pB = new Vector3();
			Vector3 pC = new Vector3();

			Vector3 cb = new Vector3();
			Vector3 ab = new Vector3();

			for ( int j = 0, jl = offsets.size(); j < jl; ++ j ) 
			{
				int start = offsets.get( j ).start;
				int count = offsets.get( j ).count;
				int index = offsets.get( j ).index;

				for ( int i = start, il = start + count; i < il; i += 3 ) 
				{

					int vA = index + indices.get( i );
					int vB = index + indices.get( i + 1 );
					int vC = index + indices.get( i + 2 );

					
					pA.set(
							positions.get( vA * 3 ),
							positions.get( vA * 3 + 1 ),
							positions.get( vA * 3 + 2 ));

					pB.set(
							positions.get( vB * 3 ),
							positions.get( vB * 3 + 1 ),
							positions.get( vB * 3 + 2 ));

					pC.set(
							positions.get( vC * 3 ),
							positions.get( vC * 3 + 1 ),
							positions.get( vC * 3 + 2 ));
					
					cb.sub( pC, pB );
					ab.sub( pA, pB );
					cb.cross( ab );

					normals.set( vA * 3, normals.get( vA * 3) + cb.getX());
					normals.set( vA * 3 + 1,  normals.get( vA * 3 + 1) + cb.getY());
					normals.set( vA * 3 + 2,  normals.get( vA * 3 + 2) + cb.getZ());

					normals.set( vB * 3,  normals.get( vB * 3) + cb.getX());
					normals.set( vB * 3 + 1,  normals.get( vB * 3 + 1) + cb.getY());
					normals.set( vB * 3 + 2,  normals.get( vB * 3 + 2) + cb.getZ());

					normals.set( vC * 3,  normals.get( vC * 3) + cb.getX());
					normals.set( vC * 3 + 1,  normals.get( vC * 3 + 1) + cb.getY());
					normals.set( vC * 3 + 2,  normals.get( vC * 3 + 2) + cb.getZ());
				}
			}

			// normalize normals

			for ( int i = 0, il = normals.getLength(); i < il; i += 3 ) 
			{
				double x = normals.get( i );
				double y = normals.get( i + 1 );
				double z = normals.get( i + 2 );

				double  n = 1.0 / Math.sqrt( x * x + y * y + z * z );

				normals.set( i , normals.get(i) *  n );
				normals.set( i + 1 , normals.get(i + 1) *  n );
				normals.set( i + 2 , normals.get(i + 2) *  n );
			}

			setNormalsNeedUpdate(true);
		}
	}
	
	/**
	 * 	Based on < href="http://www.terathon.com/code/tangent.html">terathon.com</a>
	 * (per vertex tangents)
	 */
	@Override
	public void computeTangents() 
	{
		if ( getWebGlIndexArray() == null ||
				getWebGlVertexArray() == null ||
			 getWebGlNormalArray() == null ||
			 getWebGlUvArray() == null) 
		{
			Log.warn( "Missing required attributes (index, position, normal or uv) in BufferGeometry.computeTangents()" );
			return;
		}

		Uint16Array indices = getWebGlIndexArray();
		Float32Array positions = getWebGlVertexArray();
		Float32Array normals = getWebGlNormalArray();
		Float32Array uvs = getWebGlUvArray();

		int nVertices = positions.getLength() / 3;

		if ( getWebGlTangentArray() == null ) 
		{
			int nTangentElements = 4 * nVertices;

			setWebGlTangentArray(Float32Array.create(nTangentElements));
		}

		Float32Array tangents = getWebGlTangentArray();

		List<Vector3> tan1 = new ArrayList<Vector3>();
		List<Vector3> tan2 = new ArrayList<Vector3>();

		for ( int k = 0; k < nVertices; k ++ ) 
		{
			tan1.add( new Vector3() );
			tan2.add( new Vector3() );
		}

		List<GeometryBuffer.Offset> offsets = this.offsets;

		for ( int j = 0, jl = offsets.size(); j < jl; ++ j ) 
		{
			int start = offsets.get( j ).start;
			int count = offsets.get( j ).count;
			int index = offsets.get( j ).index;

			for ( int i = start, il = start + count; i < il; i += 3 ) 
			{
				int iA = index + indices.get( i );
				int iB = index + indices.get( i + 1 );
				int iC = index + indices.get( i + 2 );

				handleTriangle( tan1, tan2, iA, iB, iC );
			}
		}

		for ( int j = 0, jl = offsets.size(); j < jl; ++ j ) 
		{
			int start = offsets.get( j ).start;
			int count = offsets.get( j ).count;
			int index = offsets.get( j ).index;

			for ( int i = start, il = start + count; i < il; i += 3 ) 
			{
				int iA = index + indices.get( i );
				int iB = index + indices.get( i + 1 );
				int iC = index + indices.get( i + 2 );

				handleVertex( tan1, tan2, iA );
				handleVertex( tan1, tan2, iB );
				handleVertex( tan1, tan2, iC );
			}
		}

		this.setHasTangents(true);
		this.setTangentsNeedUpdate(true);

	}
	
	private void handleTriangle( List<Vector3> tan1, List<Vector3> tan2, int a, int b, int c ) 
	{
		Float32Array positions = getWebGlVertexArray();
		Float32Array uvs = getWebGlUvArray();
		
		double xA = positions.get( a * 3 );
		double yA = positions.get( a * 3 + 1 );
		double zA = positions.get( a * 3 + 2 );

		double xB = positions.get( b * 3 );
		double yB = positions.get( b * 3 + 1 );
		double zB = positions.get( b * 3 + 2 );

		double xC = positions.get( c * 3 );
		double yC = positions.get( c * 3 + 1 );
		double zC = positions.get( c * 3 + 2 );

		double uA = uvs.get( a * 2 );
		double vA = uvs.get( a * 2 + 1 );

		double uB = uvs.get( b * 2 );
		double vB = uvs.get( b * 2 + 1 );

		double uC = uvs.get( c * 2 );
		double vC = uvs.get( c * 2 + 1 );

		double x1 = xB - xA;
		double x2 = xC - xA;

		double y1 = yB - yA;
		double y2 = yC - yA;

		double z1 = zB - zA;
		double z2 = zC - zA;

		double s1 = uB - uA;
		double s2 = uC - uA;

		double t1 = vB - vA;
		double t2 = vC - vA;

		double r = 1.0 / ( s1 * t2 - s2 * t1 );

		Vector3 sdir = new Vector3(
				( t2 * x1 - t1 * x2 ) * r,
				( t2 * y1 - t1 * y2 ) * r,
				( t2 * z1 - t1 * z2 ) * r);
		
		Vector3 tdir = new Vector3(
				( s1 * x2 - s2 * x1 ) * r,
				( s1 * y2 - s2 * y1 ) * r,
				( s1 * z2 - s2 * z1 ) * r);

		tan1.get( a ).add( sdir );
		tan1.get( b ).add( sdir );
		tan1.get( c ).add( sdir );

		tan2.get( a ).add( tdir );
		tan2.get( b ).add( tdir );
		tan2.get( c ).add( tdir );
	}
	
	private void handleVertex( List<Vector3> tan1, List<Vector3> tan2, int v ) 
	{
		Float32Array normals = getWebGlNormalArray();
		Float32Array tangents = getWebGlTangentArray();
		
		Vector3 n = new Vector3(
				normals.get( v * 3 ),
				normals.get( v * 3 + 1 ),
				normals.get( v * 3 + 2 ));
		
		Vector3 n2 = n.clone();

		Vector3 t = tan1.get( v );

		// Gram-Schmidt orthogonalize

		Vector3 tmp = t.clone();
		tmp.sub( n.multiply( n.dot( t ) ) ).normalize();

		// Calculate handedness

		Vector3 tmp2 = new Vector3();
		tmp2.cross( n2, t );
		double test = tmp2.dot( tan2.get( v ) );
		double w = ( test < 0.0 ) ? -1.0 : 1.0;

		tangents.set( v * 4, tmp.getX());
		tangents.set( v * 4 + 1, tmp.getY());
		tangents.set( v * 4 + 2, tmp.getZ());
		tangents.set( v * 4 + 3, w);
	}
	
	public void setDirectBuffers ( WebGLRenderingContext gl, BufferUsage hint, boolean dispose ) 
	{
		Uint16Array index = getWebGlIndexArray();
		Float32Array position = getWebGlVertexArray();
		Float32Array normal = getWebGlNormalArray();
		Float32Array uv = getWebGlUvArray();
		Float32Array color = getWebGlColorArray();
		Float32Array tangent = getWebGlTangentArray();

		if ( isElementsNeedUpdate() && index != null ) 
		{
			gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, this.__webglIndexBuffer );
			gl.bufferData( BufferTarget.ELEMENT_ARRAY_BUFFER, index, hint );
		}

		if ( isVerticesNeedUpdate() && position != null ) 
		{
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this.__webglVertexBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, position, hint );
		}

		if ( isNormalsNeedUpdate() && normal != null ) 
		{
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this.__webglNormalBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, normal, hint );
		}

		if ( isUvsNeedUpdate() && uv != null ) 
		{
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this.__webglUVBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, uv, hint );
		}

		if ( isColorsNeedUpdate() && color != null ) 
		{
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this.__webglColorBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, color, hint );
		}

		if ( isTangentsNeedUpdate() && tangent != null ) 
		{
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this.__webglTangentBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, tangent, hint );
		}

		if ( dispose ) 
		{
			dispose();
		}
	}

}
