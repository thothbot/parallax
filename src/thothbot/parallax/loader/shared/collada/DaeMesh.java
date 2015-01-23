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

package thothbot.parallax.loader.shared.collada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeMesh extends DaeElement 
{
	private Map<String, DaeSource> sources;
	private DaeVertices vertices;
	private List<DaePrimitive> primitives;
	private Geometry geometry;
	//	private String verticesID;

	public DaeMesh(Node node) 
	{
		super(node);
		
		Log.debug("DaeMesh() " + toString());
	}

	@Override
	public void destroy() 
	{
		super.destroy();

		if (sources != null) 
		{
			sources.clear();
			sources = null;
		}

		if (primitives != null) 
		{
			for (DaePrimitive primitive: primitives) 
				primitive.destroy();

			primitives.clear();
			primitives = null;
		}

		vertices = null;
//		verticesID = null;
	}

	@Override
	public void read() 
	{
		sources = new HashMap<String, DaeSource>();
		primitives = new ArrayList<DaePrimitive>();
		vertices = null;
//		verticesID = null;

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("source") == 0) 
			{
				DaeSource source = new DaeSource(child);
				sources.put(source.getID(), source);
			} 
			else if (nodeName.compareTo("vertices") == 0) 
			{
				vertices = new DaeVertices(child);
			}
			else if (nodeName.compareTo("triangles") == 0) 
			{
				primitives.add(new DaeTriangles(child, this));
			}
		}
	}
	
	public Geometry getGeometry()
	{
		if(this.geometry == null)
		{
			this.geometry = new Geometry();
			DaeInput input = this.vertices.getInput();
			if(input.getSemantic().compareTo("POSITION") == 0)
			{
				double[] vertexData = sources.get(input.getSource()).getData().getData();
				for ( int i = 0; i < vertexData.length; i += 3 ) 
				{
					this.geometry.getVertices().add( new Vector3(
							vertexData[ i ], vertexData[ 1 + i ], vertexData[ 2 + i ]) );
				}
			}
		}
		
		for ( int i = 0; i < this.primitives.size(); i ++ ) 
		{
			DaePrimitive primitive = this.primitives.get( i );
//			primitive.setVertices( this.vertices );
			handlePrimitive( (DaeTriangles) primitive);

		}

//		this.geometry.computeCentroids();
		this.geometry.computeFaceNormals();
		
//		if ( this.geometry.calcNormals )
//			this.geometry.computeVertexNormals();		
		
		this.geometry.computeBoundingBox();
		
		return this.geometry;
	}
	
	private void handlePrimitive(DaeTriangles primitive)
	{
		List<DaeInput> inputs = primitive.getInput();
		
		int maxOffset = 0;
		List<Integer> texture_sets = new ArrayList<Integer>(); 
		for ( int j = 0; j < inputs.size(); j ++ ) 
		{
			DaeInput input = inputs.get(j);
			int offset = input.getOffset() + 1;
			maxOffset = (maxOffset < offset) ? offset : maxOffset;

			if ( input.getSemantic().compareTo("TEXCOORD") ==0 ) 
				texture_sets.add(input.getSet());
		}
		
//		for ( int pCount = 0; pCount < pList.length; ++pCount ) 
//		{
//			var p = pList[ pCount ], i = 0;
//
//			while ( i < p.length ) {
//
//				var vs = [];
//				var ns = [];
//				var ts = null;
//				var cs = [];
//
//				if ( primitive.vcount ) {
//
//					vcount = primitive.vcount.length ? primitive.vcount[ vcIndex ++ ] : primitive.vcount;
//
//				} else {
//
//					vcount = p.length / maxOffset;
//
//				}
//
//
//				for ( j = 0; j < vcount; j ++ ) {
//
//					for ( k = 0; k < inputs.length; k ++ ) {
//
//						input = inputs[ k ];
//						source = sources[ input.source ];
//
//						index = p[ i + ( j * maxOffset ) + input.offset ];
//						numParams = source.accessor.params.length;
//						idx32 = index * numParams;
//
//						switch ( input.semantic ) {
//
//							case 'VERTEX':
//
//								vs.push( index );
//
//								break;
//
//							case 'NORMAL':
//
//								ns.push( getConvertedVec3( source.data, idx32 ) );
//
//								break;
//
//							case 'TEXCOORD':
//
//								ts = ts || { };
//								if ( ts[ input.set ] === undefined ) ts[ input.set ] = [];
//								// invert the V
//								ts[ input.set ].push( new THREE.UV( source.data[ idx32 ], 1.0 - source.data[ idx32 + 1 ] ) );
//
//								break;
//
//							case 'COLOR':
//
//								cs.push( new THREE.Color().setRGB( source.data[ idx32 ], source.data[ idx32 + 1 ], source.data[ idx32 + 2 ] ) );
//
//								break;
//
//							default:
//							
//								break;
//
//						}
//
//					}
//
//				}
//
//				if ( ns.length == 0 ) {
//
//					// check the vertices inputs
//					input = this.vertices.input.NORMAL;
//
//					if ( input ) {
//
//						source = sources[ input.source ];
//						numParams = source.accessor.params.length;
//
//						for ( var ndx = 0, len = vs.length; ndx < len; ndx++ ) {
//
//							ns.push( getConvertedVec3( source.data, vs[ ndx ] * numParams ) );
//
//						}
//
//					} else {
//
//						geom.calcNormals = true;
//
//					}
//
//				}
//
//				if ( !ts ) {
//
//					ts = { };
//					// check the vertices inputs
//					input = this.vertices.input.TEXCOORD;
//
//					if ( input ) {
//
//						texture_sets.push( input.set );
//						source = sources[ input.source ];
//						numParams = source.accessor.params.length;
//
//						for ( var ndx = 0, len = vs.length; ndx < len; ndx++ ) {
//
//							idx32 = vs[ ndx ] * numParams;
//							if ( ts[ input.set ] === undefined ) ts[ input.set ] = [ ];
//							// invert the V
//							ts[ input.set ].push( new THREE.UV( source.data[ idx32 ], 1.0 - source.data[ idx32 + 1 ] ) );
//
//						}
//
//					}
//
//				}
//
//				if ( cs.length == 0 ) {
//
//					// check the vertices inputs
//					input = this.vertices.input.COLOR;
//
//					if ( input ) {
//
//						source = sources[ input.source ];
//						numParams = source.accessor.params.length;
//
//						for ( var ndx = 0, len = vs.length; ndx < len; ndx++ ) {
//
//							idx32 = vs[ ndx ] * numParams;
//							cs.push( new THREE.Color().setRGB( source.data[ idx32 ], source.data[ idx32 + 1 ], source.data[ idx32 + 2 ] ) );
//
//						}
//
//					}
//
//				}
//
//				var face = null, faces = [], uv, uvArr;
//
//				if ( vcount === 3 ) {
//
//					faces.push( new THREE.Face3( vs[0], vs[1], vs[2], ns, cs.length ? cs : new THREE.Color() ) );
//
//				} else if ( vcount === 4 ) {
//					faces.push( new THREE.Face4( vs[0], vs[1], vs[2], vs[3], ns, cs.length ? cs : new THREE.Color() ) );
//
//				} else if ( vcount > 4 && options.subdivideFaces ) {
//
//					var clr = cs.length ? cs : new THREE.Color(),
//						vec1, vec2, vec3, v1, v2, norm;
//
//					// subdivide into multiple Face3s
//					for ( k = 1; k < vcount-1; ) {
//
//						// FIXME: normals don't seem to be quite right
//						faces.push( new THREE.Face3( vs[0], vs[k], vs[k+1], [ ns[0], ns[k++], ns[k] ],  clr ) );
//
//					}
//
//				}
//
//				if ( faces.length ) {
//
//					for (var ndx = 0, len = faces.length; ndx < len; ndx++) {
//
//						face = faces[ndx];
//						face.daeMaterial = primitive.material;
//						geom.faces.push( face );
//
//						for ( k = 0; k < texture_sets.length; k++ ) {
//
//							uv = ts[ texture_sets[k] ];
//
//							if ( vcount > 4 ) {
//
//								// Grab the right UVs for the vertices in this face
//								uvArr = [ uv[0], uv[ndx+1], uv[ndx+2] ];
//
//							} else if ( vcount === 4 ) {
//
//								uvArr = [ uv[0], uv[1], uv[2], uv[3] ];
//
//							} else {
//
//								uvArr = [ uv[0], uv[1], uv[2] ];
//
//							}
//
//							if ( !geom.faceVertexUvs[k] ) {
//
//								geom.faceVertexUvs[k] = [];
//
//							}
//
//							geom.faceVertexUvs[k].push( uvArr );
//
//						}
//
//					}
//
//				} else {
//
//					console.log( 'dropped face with vcount ' + vcount + ' for geometry with id: ' + geom.id );
//
//				}
//
//				i += maxOffset * vcount;
//
//			}
//		}
	}

//	private DaeSource readSource(String id) 
//	{
//		if (id != null) 
//		{
//			DaeSource source = DaeDocument.getSourceByID(getNode(), id);
//			if (source != null && source.getID() != null) 
//			{
//				if (sources.get(id) == null) 
//				{
//					sources.put(source.getID(), source);
//				}
//				return source;
//			}
//		} 
//		return null;
//	}

//	private void readVertices(Node node) 
//	{
//		NodeList list = node.getChildNodes();
//		for (int i = 0; i < list.getLength(); i++) 
//		{
//			Node child = list.item(i);
//			String nodeName = child.getNodeName();
//
//			if (nodeName.compareTo("input") == 0) 
//			{
//				DaeInput input = new DaeInput(child);
//				if (input.getSemantic().compareTo("POSITION") == 0) 
//				{
//					vertices = readSource(input.getSource());
//					verticesID = readAttribute(node, "id");
//				}
//			}
//		}
//	}

	public List<DaePrimitive> getPrimitives() {
		return primitives;
	}

	public Map<String, DaeSource> getSources() {
		return sources;
	}

	public DaeVertices getVertices() {
		return vertices;
	}

//	public String getVerticesID() {
//		return verticesID;
//	}
	
	@Override
	public String toString()
	{
		return "Mesh";
	}
}
