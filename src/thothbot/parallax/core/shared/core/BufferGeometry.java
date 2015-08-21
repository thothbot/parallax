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
import java.util.Set;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Int32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Box3;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix3;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Sphere;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.core.client.GWT;

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
 *		indices.set( i, i % ( 3 * chunkSize ));
 * }
 *
 * Float32Array positions = Float32Array.create( triangles * 3 * 3 );
 *
 * //itemSize = 3 because there are 3 values (components) per vertex
 * geometry.addAttribute( "position", new BufferAttribute( positions, 3 ) );
 *</pre>
 */
public class BufferGeometry extends AbstractGeometry
{
	public static class DrawCall 
	{
		public int start;
		public int count;
		public int index;	
		
		public DrawCall(int start, int count, int index) {
			this.start = start;
			this.count = count;
			this.index = index;
		}
	}
			
	private Map<String, BufferAttribute> attributes;
	private Set<String> attributesKeys;
	
	private List<BufferGeometry.DrawCall> drawcalls;

	public BufferGeometry() 
	{
		super();
		
		this.attributes  = GWT.isScript() ? 
				new FastMap<BufferAttribute>() : new HashMap<String, BufferAttribute>();
					
		this.setDrawcalls(new ArrayList<BufferGeometry.DrawCall>());

		this.boundingBox = null;
		this.boundingSphere = null;
	}
		
	public Map<String, BufferAttribute> getAttributes() {
		return this.attributes;
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
	 *      <li>index - Allows for vertices to be re-used across multiple triangles; this is called using "indexed triangles," 
	 *      	and works much the same as it does in Geometry: each triangle is associated with the index of three vertices.
	 *      	This attribute therefore stores the index of each vertex for each triangular face. If this attribute is not set, the renderer 
	 *      	assumes that each three contiguous positions represent a single triangle.</li>
	 *  </ul>
	 * @param name Attribute name
	 * @param attribute
	 */
	public void addAttribute( String name, BufferAttribute attribute ) {

		this.attributes.put( name, attribute );
		this.attributesKeys = this.attributes.keySet();

	}

	public BufferAttribute getAttribute( String name ) {

		return this.attributes.get( name );

	}
	
	/**
	 * For geometries that use indexed triangles, this Array can be used to split the object into multiple WebGL draw calls. 
	 * Each draw call will draw some subset of the vertices in this geometry using the configured shader. 
	 * This may be necessary if, for instance, you have more than 65535 vertices in your object.
	 * @return
	 */
	public List<BufferGeometry.DrawCall> getDrawcalls() {
		return drawcalls;
	}

	/**
	 * @param drawcalls the drawcalls to set
	 */
	public void setDrawcalls(List<BufferGeometry.DrawCall> drawcalls) {
		this.drawcalls = drawcalls;
	}

	public void addDrawCall( int start, int count ) {
		addDrawCall(start, count, 0);
	}
	
	public void addDrawCall( int start, int count, int indexOffset ) {

		DrawCall drawCall = new DrawCall(start, count, indexOffset);
		this.getDrawcalls().add(drawCall);
	}
	
	/**
	 * Bakes matrix transform directly into vertex coordinates.
	 * @param matrix
	 */
	public void applyMatrix( Matrix4 matrix ) {

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

	}
	
	/**
	 * Populates this BufferGeometry with data from a {@link Geometry} object.
	 * @param geometry
	 * @return
	 */
	public BufferGeometry fromGeometry( Geometry geometry) {
	
		return fromGeometry(geometry, Material.COLORS.NO);
	}

	public BufferGeometry fromGeometry( Geometry geometry, Material.COLORS vertexColors) {

		List<Vector3> vertices = geometry.getVertices();
		List<Face3> faces = geometry.getFaces();
		List<List<List<Vector2>>> faceVertexUvs = geometry.getFaceVertexUvs();
		boolean hasFaceVertexUv = faceVertexUvs.get( 0 ).size() > 0;
		boolean hasFaceVertexNormals = faces.get( 0 ).getVertexNormals().size() == 3;

		Float32Array positions = Float32Array.create( faces.size() * 3 * 3 );
		this.addAttribute( "position", new BufferAttribute( positions, 3 ) );

		Float32Array normals = Float32Array.create( faces.size() * 3 * 3 );
		this.addAttribute( "normal", new BufferAttribute( normals, 3 ) );

		Float32Array colors = Float32Array.create( faces.size() * 3 * 3 );
		if ( vertexColors != Material.COLORS.NO ) {

			this.addAttribute( "color", new BufferAttribute( colors, 3 ) );

		}

		Float32Array uvs = Float32Array.create( faces.size() * 3 * 2 );
		if ( hasFaceVertexUv == true ) {
			
			this.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );

		}

		for ( int i = 0, i2 = 0, i3 = 0; i < faces.size(); i ++, i2 += 6, i3 += 9 ) {

			Face3 face = faces.get( i );

			Vector3 a = vertices.get( face.getA() );
			Vector3 b = vertices.get( face.getB() );
			Vector3 c = vertices.get( face.getC() );

			positions.set( i3     , a.getX());
			positions.set( i3 + 1 , a.getY());
			positions.set( i3 + 2 , a.getZ());

			positions.set( i3 + 3 , b.getX());
			positions.set( i3 + 4 , b.getY());
			positions.set( i3 + 5 , b.getZ());

			positions.set( i3 + 6 , c.getX());
			positions.set( i3 + 7 , c.getY());
			positions.set( i3 + 8 , c.getZ());

			if ( hasFaceVertexNormals == true ) {

				Vector3 na = face.getVertexNormals().get( 0 );
				Vector3 nb = face.getVertexNormals().get( 1 );
				Vector3 nc = face.getVertexNormals().get( 2 );

				normals.set( i3     , na.getX());
				normals.set( i3 + 1 , na.getY());
				normals.set( i3 + 2 , na.getZ());

				normals.set( i3 + 3 , nb.getX());
				normals.set( i3 + 4 , nb.getY());
				normals.set( i3 + 5 , nb.getZ());

				normals.set( i3 + 6 , nc.getX());
				normals.set( i3 + 7 , nc.getY());
				normals.set( i3 + 8 , nc.getZ());

			} else {

				Vector3 n = face.getNormal();

				normals.set( i3     , n.getX());
				normals.set( i3 + 1 , n.getY());
				normals.set( i3 + 2 , n.getZ());

				normals.set( i3 + 3 , n.getX());
				normals.set( i3 + 4 , n.getY());
				normals.set( i3 + 5 , n.getZ());

				normals.set( i3 + 6 , n.getX());
				normals.set( i3 + 7 , n.getY());
				normals.set( i3 + 8 , n.getZ());

			}

			if ( vertexColors == Material.COLORS.FACE ) {

				Color fc = face.getColor();

				colors.set( i3     , fc.getR());
				colors.set( i3 + 1 , fc.getG());
				colors.set( i3 + 2 , fc.getB());

				colors.set( i3 + 3 , fc.getR());
				colors.set( i3 + 4 , fc.getG());
				colors.set( i3 + 5 , fc.getB());

				colors.set( i3 + 6 , fc.getR());
				colors.set( i3 + 7 , fc.getG());
				colors.set( i3 + 8 , fc.getB());

			} else if ( vertexColors ==  Material.COLORS.VERTEX ) {

				Color vca = face.getVertexColors().get( 0 );
				Color vcb = face.getVertexColors().get( 1 );
				Color vcc = face.getVertexColors().get( 2 );

				colors.set( i3     , vca.getR());
				colors.set( i3 + 1 , vca.getG());
				colors.set( i3 + 2 , vca.getB());

				colors.set( i3 + 3 , vcb.getR());
				colors.set( i3 + 4 , vcb.getG());
				colors.set( i3 + 5 , vcb.getB());

				colors.set( i3 + 6 , vcc.getR());
				colors.set( i3 + 7 , vcc.getG());
				colors.set( i3 + 8 , vcc.getB());

			}

			if ( hasFaceVertexUv == true ) {

				Vector2 uva = faceVertexUvs.get( 0 ).get( i ).get( 0 );
				Vector2 uvb = faceVertexUvs.get( 0 ).get( i ).get( 1 );
				Vector2 uvc = faceVertexUvs.get( 0 ).get( i ).get( 2 );

				uvs.set( i2     , uva.getX());
				uvs.set( i2 + 1 , uva.getY());

				uvs.set( i2 + 2 , uvb.getX());
				uvs.set( i2 + 3 , uvb.getY());

				uvs.set( i2 + 4 , uvc.getX());
				uvs.set( i2 + 5 , uvc.getY());

			}

		}

		this.computeBoundingSphere();

		return this;

	}

	/**
	 * Computes bounding box of the geometry, updating Geometry.boundingBox attribute.
	 * Bounding boxes aren't computed by default. They need to be explicitly computed, otherwise they are null.
	 */
	@Override
	public void computeBoundingBox() {

		Vector3 vector = new Vector3();

		if ( this.boundingBox == null ) {

			this.boundingBox = new Box3();

		}

		Float32Array positions = (Float32Array) getAttribute("position").getArray();

		if ( positions != null) {

			Box3 bb = this.boundingBox;
			bb.makeEmpty();

			for ( int i = 0, il = positions.getLength(); i < il; i += 3 ) {

				vector.set( positions.get( i ), positions.get( i + 1 ), positions.get( i + 2 ) );
				bb.expandByPoint( vector );

			}

		}

		if ( positions == null || positions.getLength() == 0 ) {

			this.boundingBox.getMin().set( 0, 0, 0 );
			this.boundingBox.getMax().set( 0, 0, 0 );

		}
		
	}

	/**
	 * Computes bounding sphere of the geometry, updating Geometry.boundingSphere attribute.
	 * Bounding spheres aren't computed by default. They need to be explicitly computed, otherwise they are null.
	 */
	public void computeBoundingSphere() {

		Box3 box = new Box3();
		Vector3 vector = new Vector3();

		if ( this.boundingSphere == null ) {

			this.boundingSphere = new Sphere();

		}

		Float32Array positions = (Float32Array) getAttribute("position").getArray();

		if ( positions != null ) {

			box.makeEmpty();

			Vector3 center = this.boundingSphere.getCenter();

			for ( int i = 0, il = positions.getLength(); i < il; i += 3 ) {

				vector.set( positions.get( i ), positions.get( i + 1 ), positions.get( i + 2 ) );
				box.expandByPoint( vector );

			}

			box.center( center );

			// hoping to find a boundingSphere with a radius smaller than the
			// boundingSphere of the boundingBox:  sqrt(3) smaller in the best case

			double maxRadiusSq = 0.0;

			for ( int i = 0, il = positions.getLength(); i < il; i += 3 ) {

				vector.set( positions.get( i ), positions.get( i + 1 ), positions.get( i + 2 ) );
				maxRadiusSq = Math.max( maxRadiusSq, center.distanceToSquared( vector ) );

			}

			this.boundingSphere.setRadius( Math.sqrt( maxRadiusSq ) );

		}

	}

	/**
	 * Computes vertex normals by averaging face normals.
	 */
	public void computeVertexNormals() {

		BufferAttribute positionAttribute = getAttribute("position");
		
		if ( positionAttribute != null ) {
		
			Float32Array positions =  (Float32Array) positionAttribute.getArray();

			BufferAttribute normalAttribute = getAttribute("normal");

			if ( normalAttribute == null ) {

				this.addAttribute( "normal", new BufferAttribute( Float32Array.create( positions.getLength() ), 3 ) );

			} else {

				// reset existing normals to zero

				Float32Array normals = (Float32Array) normalAttribute.getArray();

				for ( int i = 0, il = normals.getLength(); i < il; i ++ ) {

					normals.set( i , 0.0 );

				}

			}

			Float32Array normals = (Float32Array) normalAttribute.getArray();

			int vA, vB, vC;

			Vector3 pA = new Vector3(),
			pB = new Vector3(),
			pC = new Vector3(),

			cb = new Vector3(),
			ab = new Vector3();

			// indexed elements

			if ( getAttribute("index") != null ) {

				Uint16Array indices = (Uint16Array) getAttribute("normal").getArray();

				List<BufferGeometry.DrawCall> offsets = this.drawcalls.size() > 0 
						? this.drawcalls 
						: Arrays.asList( new BufferGeometry.DrawCall(0, indices.getLength(), 0 ) ) ;

				for ( int j = 0, jl = offsets.size(); j < jl; ++ j ) {

					int start = offsets.get( j ).start;
					int count = offsets.get( j ).count;
					int index = offsets.get( j ).index;

					for ( int i = start, il = start + count; i < il; i += 3 ) {

						vA = ( index + (int)indices.get( i     ) ) * 3;
						vB = ( index + (int)indices.get( i + 1 ) ) * 3;
						vC = ( index + (int)indices.get( i + 2 ) ) * 3;

						pA.fromArray( positions, vA );
						pB.fromArray( positions, vB );
						pC.fromArray( positions, vC );

						cb.sub( pC, pB );
						ab.sub( pA, pB );
						cb.cross( ab );

						normals.set( vA     , normals.get( vA     ) + cb.getX());
						normals.set( vA + 1 , normals.get( vA + 1 ) + cb.getY());
						normals.set( vA + 2 , normals.get( vA + 2 ) + cb.getZ());

						normals.set( vB     , normals.get( vB     ) + cb.getX());
						normals.set( vB + 1 , normals.get( vB + 1 ) + cb.getY());
						normals.set( vB + 2 , normals.get( vB + 2 ) + cb.getZ());

						normals.set( vC     , normals.get( vC     ) + cb.getX());
						normals.set( vC + 1 , normals.get( vC + 1 ) + cb.getY());
						normals.set( vC + 2 , normals.get( vC + 2 ) + cb.getZ());

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

					normals.set( i     , cb.getX());
					normals.set( i + 1 , cb.getY());
					normals.set( i + 2 , cb.getZ());

					normals.set( i + 3 , cb.getX());
					normals.set( i + 4 , cb.getY());
					normals.set( i + 5 , cb.getZ());

					normals.set( i + 6 , cb.getX());
					normals.set( i + 7 , cb.getY());
					normals.set( i + 8 , cb.getZ());

				}

			}

			this.normalizeNormals();

			getAttribute("normal").setNeedsUpdate( true );

		}

	}
	
	/**
	 * Computes vertex tangents.
	 * Based on http://www.terathon.com/code/tangent.html
	 * Geometry must have vertex UVs (layer 0 will be used).
	 */
	public void computeTangents() {

		// based on http://www.terathon.com/code/tangent.html
		// (per vertex tangents)

		if ( getAttribute("index") == null ||
			 getAttribute("position") == null ||
			 getAttribute("normal") == null ||
			 getAttribute("uv") == null ) {

			Log.warn( "Missing required attributes (index, position, normal or uv) in BufferGeometry.computeTangents()" );
			return;

		}

		Uint16Array indices = (Uint16Array)getAttribute("index").getArray();
		Float32Array positions = (Float32Array)getAttribute("position").getArray();
		Float32Array normals = (Float32Array)getAttribute("normal").getArray();
		Float32Array uvs = (Float32Array)getAttribute("uv").getArray();

		int nVertices = positions.getLength() / 3;

		if ( getAttribute("tangent") == null ) {

			this.addAttribute( "tangent", new BufferAttribute( Float32Array.create( 4 * nVertices ), 4 ) );

		}
		
		Vector3[] tan1 = new Vector3[nVertices], tan2 = new Vector3[nVertices];

		for ( int k = 0; k < nVertices; k ++ ) {

			tan1[ k ] = new Vector3();
			tan2[ k ] = new Vector3();

		}

		Float32Array tangents = (Float32Array)getAttribute("tangent").getArray();

		if ( this.getDrawcalls().size() == 0 ) {

			this.addDrawCall( 0, indices.getLength(), 0 );

		}

		List<DrawCall> drawcalls = this.getDrawcalls();

		for ( int j = 0, jl = drawcalls.size(); j < jl; ++ j ) {

			int start = drawcalls.get( j ).start;
			int count = drawcalls.get( j ).count;
			int index = drawcalls.get( j ).index;

			for ( int i = start, il = start + count; i < il; i += 3 ) {

				int iA = index + (int)indices.get( i );
				int iB = index + (int)indices.get( i + 1 );
				int iC = index + (int)indices.get( i + 2 );

				handleTriangle( tan1, tan2, positions, uvs, iA, iB, iC );

			}

		}

		for ( int j = 0, jl = drawcalls.size(); j < jl; ++ j ) {

			int start = drawcalls.get( j ).start;
			int count = drawcalls.get( j ).count;
			int index = drawcalls.get( j ).index;

			for ( int i = start, il = start + count; i < il; i += 3 ) {

				int iA = index + (int)indices.get( i );
				int iB = index + (int)indices.get( i + 1 );
				int iC = index + (int)indices.get( i + 2 );

				handleVertex( tan1, tan2, normals, tangents, iA );
				handleVertex( tan1, tan2, normals, tangents, iB );
				handleVertex( tan1, tan2, normals, tangents, iC );

			}

		}

	}
	
	public List<BufferGeometry.DrawCall> computeOffsets() {
	    //WebGL limits type of index buffer values to 16-bit.
		return computeOffsets(65535);
	}

	/**
	 * Compute the draw offset for large models by chunking the index buffer into chunks of 65k addressable vertices.
	 * This method will effectively rewrite the index buffer and remap all attributes to match the new indices.
	 * WARNING: This method will also expand the vertex count to prevent sprawled triangles across draw offsets
	 * @param size Defaults to 65535, but allows for larger or smaller chunks.
	 * @return
	 */
	public List<BufferGeometry.DrawCall> computeOffsets( int size /* indexBufferSize */ ) {

//		var s = Date.now();

		Uint16Array indices = (Uint16Array)getAttribute("index").getArray();
		Float32Array vertices = (Float32Array)getAttribute("position").getArray();

		int verticesCount = ( vertices.getLength() / 3 );
		int facesCount = ( indices.getLength() / 3 );

		Float32Array sortedIndices = Float32Array.create( indices.getLength() ); //16-bit buffers
		int indexPtr = 0;
		int vertexPtr = 0;

		List<BufferGeometry.DrawCall> offsets = Arrays.asList(new BufferGeometry.DrawCall(0, 0, 0));
		BufferGeometry.DrawCall offset = offsets.get( 0 );

		int duplicatedVertices = 0;
		int newVerticeMaps = 0;
		Int32Array faceVertices = Int32Array.create( 6 );
		Int32Array vertexMap = Int32Array.create( vertices.getLength() );
		Int32Array revVertexMap = Int32Array.create( vertices.getLength() );
		for ( int j = 0; j < vertices.getLength(); j ++ ) { 
			vertexMap.set( j , - 1 ); 
			revVertexMap.set( j , - 1); 
		}

		/*
			Traverse every face and reorder vertices in the proper offsets of 65k.
			We can have more than 65k entries in the index buffer per offset, but only reference 65k values.
		*/
		for ( int findex = 0; findex < facesCount; findex ++ ) {
			newVerticeMaps = 0;

			for ( int vo = 0; vo < 3; vo ++ ) {
				int vid = (int)indices.get( findex * 3 + vo );
				if ( vertexMap.get( vid ) == - 1 ) {
					//Unmapped vertice
					faceVertices.set( vo * 2 , vid);
					faceVertices.set( vo * 2 + 1 , - 1);
					newVerticeMaps ++;
				} else if ( vertexMap.get( vid ) < offset.index ) {
					//Reused vertices from previous block (duplicate)
					faceVertices.set( vo * 2 , vid);
					faceVertices.set( vo * 2 + 1 , - 1);
					duplicatedVertices ++;
				} else {
					//Reused vertice in the current block
					faceVertices.set( vo * 2 , vid);
					faceVertices.set( vo * 2 + 1 , vertexMap.get( vid ));
				}
			}

			int faceMax = vertexPtr + newVerticeMaps;
			if ( faceMax > ( offset.index + size ) ) {
				BufferGeometry.DrawCall new_offset = new BufferGeometry.DrawCall(indexPtr, 0, vertexPtr );
				offsets.add( new_offset );
				offset = new_offset;

				//Re-evaluate reused vertices in light of new offset.
				for ( int v = 0; v < 6; v += 2 ) {
					int new_vid = faceVertices.get( v + 1 );
					if ( new_vid > - 1 && new_vid < offset.index )
						faceVertices.set( v + 1 , - 1);
				}
			}

			//Reindex the face.
			for ( int v = 0; v < 6; v += 2 ) {
				int vid = faceVertices.get( v );
				int new_vid = faceVertices.get( v + 1 );

				if ( new_vid == - 1 )
					new_vid = vertexPtr ++;

				vertexMap.set( vid , new_vid);
				revVertexMap.set( new_vid , vid);
				sortedIndices.set( indexPtr ++ , new_vid - offset.index); //XXX overflows at 16bit
				offset.count ++;
			}
		}

		/* Move all attribute values to map to the new computed indices , also expand the vertice stack to match our new vertexPtr. */
		this.reorderBuffers( sortedIndices, revVertexMap, vertexPtr );
		this.drawcalls = offsets;

		/*
		var orderTime = Date.now();
		console.log("Reorder time: "+(orderTime-s)+"ms");
		console.log("Duplicated "+duplicatedVertices+" vertices.");
		console.log("Compute Buffers time: "+(Date.now()-s)+"ms");
		console.log("Draw offsets: "+offsets.length);
		*/

		return offsets;
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

	/*
		reoderBuffers:
		Reorder attributes based on a new indexBuffer and indexMap.
		indexBuffer - Uint16Array of the new ordered indices.
		indexMap - Int32Array where the position is the new vertex ID and the value the old vertex ID for each vertex.
		vertexCount - Amount of total vertices considered in this reordering (in case you want to grow the vertice stack).
	*/
	public void reorderBuffers( Float32Array indexBuffer, Int32Array indexMap, int vertexCount ) {

		/* Create a copy of all attributes for reordering. */
		Map <String, Float32Array> sortedAttributes  = GWT.isScript() ? 
				new FastMap<Float32Array>() : new HashMap<String, Float32Array>();
				
		for(String attr : this.attributes.keySet()) { 
			if ( attr.equals("index" ) )
				continue;
				
			BufferAttribute attribute = getAttribute(attr); 
			Float32Array sourceArray = (Float32Array)attribute.getArray();
			sortedAttributes.put( attr,Float32Array.create(  attribute.getItemSize() * vertexCount ));
		}

		/* Move attribute positions based on the new index map */
		for ( int new_vid = 0; new_vid < vertexCount; new_vid ++ ) {
			int vid = indexMap.get( new_vid );
			for(String attr : this.attributes.keySet()) {
				if ( attr.equals("index" ) )
					continue;
			
				BufferAttribute attribute = getAttribute(attr);
				Float32Array attrArray = (Float32Array)attribute.getArray();
				int attrSize = attribute.getItemSize();
				 		
				Float32Array sortedAttr = sortedAttributes.get( attr );
				for ( int k = 0; k < attrSize; k ++ )
					sortedAttr.set( new_vid * attrSize + k , attrArray.get( vid * attrSize + k ) );
			}
		}

		/* Carry the new sorted buffers locally */
		getAttribute("index").setArray(indexBuffer);

		for(String attr : this.attributes.keySet()) {
			if ( attr.equals("index" ) )
				continue;

			this.attributes.get( attr ).setArray( sortedAttributes.get( attr ) );
			this.attributes.get( attr ).setNumItems(this.attributes.get( attr ).getItemSize() * vertexCount);
		}
	}
	
	public BufferGeometry clone() {

		BufferGeometry geometry = new BufferGeometry();
		
		for(String attr : this.attributes.keySet()) {
			BufferAttribute sourceAttr = getAttribute( attr );
			geometry.addAttribute( attr, sourceAttr.clone() );
		}

		for ( int i = 0, il = this.drawcalls.size(); i < il; i ++ ) {

			DrawCall offset = this.drawcalls.get( i );

			geometry.drawcalls.add( new DrawCall(

				offset.start,
				offset.index,
				offset.count

			) );

		}

		return geometry;

	}
	
	
	private void handleVertex( Vector3[] tan1, Vector3[] tan2, Float32Array normals, Float32Array tangents, int v ) {

		Vector3 tmp = new Vector3(), tmp2 = new Vector3();
		Vector3 n = new Vector3(), n2 = new Vector3();
		
		n.fromArray( normals, v * 3 );
		n2.copy( n );

		Vector3 t = tan1[ v ];

		// Gram-Schmidt orthogonalize

		tmp.copy( t );
		tmp.sub( n.multiply( n.dot( t ) ) ).normalize();

		// Calculate handedness

		tmp2.cross( n2, t );
		double test = tmp2.dot( tan2[ v ] );
		double w = ( test < 0.0 ) ? - 1.0 : 1.0;

		tangents.set( v * 4     , tmp.getX());
		tangents.set( v * 4 + 1 , tmp.getY());
		tangents.set( v * 4 + 2 , tmp.getZ());
		tangents.set( v * 4 + 3 , w);

	}

	private void handleTriangle(Vector3[] tan1, Vector3[] tan2, Float32Array positions, Float32Array uvs, int a, int b, int c ) {

		Vector3 vA = new Vector3(),
			vB = new Vector3(),
			vC = new Vector3();

		vA.fromArray( positions, a * 3 );
		vB.fromArray( positions, b * 3 );
		vC.fromArray( positions, c * 3 );

		Vector2 uvA = new Vector2(),
			uvB = new Vector2(),
			uvC = new Vector2();

		uvA.fromArray( uvs, a * 2 );
		uvB.fromArray( uvs, b * 2 );
		uvC.fromArray( uvs, c * 2 );

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

		double r = 1.0 / ( s1 * t2 - s2 * t1 );

		Vector3 sdir = new Vector3(), tdir = new Vector3();
		
		sdir.set(
			( t2 * x1 - t1 * x2 ) * r,
			( t2 * y1 - t1 * y2 ) * r,
			( t2 * z1 - t1 * z2 ) * r
		);

		tdir.set(
			( s1 * x2 - s2 * x1 ) * r,
			( s1 * y2 - s2 * y1 ) * r,
			( s1 * z2 - s2 * z1 ) * r
		);

		tan1[ a ].add( sdir );
		tan1[ b ].add( sdir );
		tan1[ c ].add( sdir );

		tan2[ a ].add( tdir );
		tan2[ b ].add( tdir );
		tan2[ c ].add( tdir );

	}
	
	public void setDirectBuffers( WebGLRenderingContext gl ) {

		for ( int i = 0, l = this.attributesKeys.size(); i < l; i ++ ) {

			String key = (String) this.attributesKeys.toArray()[ i ];
			BufferAttribute attribute = this.attributes.get( key );

			if ( attribute.getBuffer() == null ) {

				attribute.setBuffer(gl.createBuffer());
				attribute.setNeedsUpdate(true);

			}

			if ( attribute.isNeedsUpdate() == true ) {

				BufferTarget bufferType = ( key == "index" ) ? BufferTarget.ELEMENT_ARRAY_BUFFER : BufferTarget.ARRAY_BUFFER;

				gl.bindBuffer( bufferType, attribute.getBuffer() );
				gl.bufferData( bufferType, attribute.getArray(), BufferUsage.STATIC_DRAW );

				attribute.setNeedsUpdate(false);

			}

		}

	}
	
	public String toString() {
		return getClass().getSimpleName() 
				+ "{id: " + getId() 
				+ ", offsets: " + this.drawcalls.size()
				+ ", drawcalls: " + this.getDrawcalls().size() + "}";
	}
}
