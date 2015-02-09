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

package thothbot.parallax.loader.shared;

import thothbot.parallax.core.client.gl2.arrays.ArrayBuffer;
import thothbot.parallax.core.client.gl2.arrays.DataView;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint8Array;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferAttribute;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.xhr.client.XMLHttpRequest.ResponseType;

public class STLLoader extends XHRLoader {

	private ArrayBuffer binData;
	
	private boolean hasColors;
	private double alpha;
	
	public STLLoader(String url, ModelLoadHandler modelLoadHandler) 
	{
		super(url, modelLoadHandler);
		// Load binary data by default
		setResponseType(ResponseType.ArrayBuffer);
		
		load();
	}
	
	public boolean isHasColors() {
		return hasColors;
	}

	public double getAlpha() {
		return alpha;
	}
	
	@Override
	protected AbstractGeometry parse(ArrayBuffer buffer) 
	{
		this.binData = buffer;

		if(isBinary())
			return this.parseBinary();
		else
			return this.parseASCII( ensureString(buffer) );
		
	}

	@Override
	protected AbstractGeometry parse(String string) 
	{
		return null;
	}

	private AbstractGeometry parseBinary()
	{
		DataView reader = DataView.create( this.binData  );
		int faces = (int)reader.getUint32( 80, true );

		boolean hasColors = false;
		Float32Array colors = null;
		
		double r = 0, g = 0, b = 0;
		double defaultR = 0, defaultG = 0, defaultB = 0, alpha = 0;
		// process STL header
		// check for default color in header ("COLOR=rgba" sequence).

		for ( int index = 0; index < 80 - 10; index ++ ) {

			if ((reader.getUint32(index, false) == 0x434F4C4F /*COLO*/) &&
				(reader.getUint8(index + 4) == 0x52 /*'R'*/) &&
				(reader.getUint8(index + 5) == 0x3D /*'='*/)) {

					hasColors = true;
					colors = Float32Array.create( faces * 3 * 3);

					defaultR = (double)reader.getUint8(index + 6) / 255.0;
					defaultG = (double)reader.getUint8(index + 7) / 255.0;
					defaultB = (double)reader.getUint8(index + 8) / 255.0;
					alpha = (double)reader.getUint8(index + 9) / 255.0;
				}
			}

		int dataOffset = 84;
		int faceLength = 12 * 4 + 2;

		int offset = 0;

		BufferGeometry geometry = new BufferGeometry();

		Float32Array vertices = Float32Array.create( faces * 3 * 3 );
		Float32Array normals = Float32Array.create( faces * 3 * 3 );

		for ( int face = 0; face < faces; face ++ ) {

			int start = dataOffset + face * faceLength;

			double normalX = reader.getFloat32(start, true);
			double normalY = reader.getFloat32(start + 4, true);
			double normalZ = reader.getFloat32(start + 8, true);

			if (hasColors) {

				int packedColor = reader.getUint16(start + 48, true);

				if ((packedColor & 0x8000) == 0) { // facet has its own unique color

					r = (double)(packedColor & 0x1F) / 31.0;
					g = (double)((packedColor >> 5) & 0x1F) / 31.0;
					b = (double)((packedColor >> 10) & 0x1F) / 31.0;
				} else {

					r = defaultR;
					g = defaultG;
					b = defaultB;
				}
			}

			for ( int i = 1; i <= 3; i ++ ) {

				int vertexstart = start + i * 12;

				vertices.set( offset     , reader.getFloat32( vertexstart, true ));
				vertices.set( offset + 1 , reader.getFloat32( vertexstart + 4, true ));
				vertices.set( offset + 2 , reader.getFloat32( vertexstart + 8, true ));

				normals.set( offset     , normalX );
				normals.set( offset + 1 , normalY );
				normals.set( offset + 2 , normalZ );

				if (hasColors) {
					colors.set( offset     , r );
					colors.set( offset + 1 , g );
					colors.set( offset + 2 , b );
				}

				offset += 3;

			}

		}

		((BufferGeometry)geometry).addAttribute( "position", new BufferAttribute( vertices, 3 ) );
		((BufferGeometry)geometry).addAttribute( "normal", new BufferAttribute( normals, 3 ) );

		if (hasColors) {
			((BufferGeometry)geometry).addAttribute( "color", new BufferAttribute( colors, 3 ) );
			this.hasColors = true;
			this.alpha = alpha;
		}

		return geometry;
	}
	
	private AbstractGeometry parseASCII( String data ) 
	{
		Geometry geometry = new Geometry();

		Vector3 normal = null;
		RegExp patternFace = RegExp.compile("facet([\\s\\S]*?)endfacet", "g");
		
		for (MatchResult result = patternFace.exec(data); result != null; result = patternFace.exec(data))
		{
			String text = result.getGroup(0);
			RegExp patternNormal = RegExp.compile("normal[\\s]+([\\-+]?[0-9]+\\.?[0-9]*([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+", "g");

			for (MatchResult result2 = patternNormal.exec(text); result2 != null; result2 = patternNormal.exec(text))
			{
				normal = new Vector3(  Double.parseDouble( result2.getGroup(1) ),  Double.parseDouble( result2.getGroup(3) ),  Double.parseDouble( result2.getGroup(5) ) );
			}

			RegExp patternVertex = RegExp.compile("vertex[\\s]+([\\-+]?[0-9]+\\.?[0-9]*([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+", "g");

			for (MatchResult result2 = patternVertex.exec(text); result2 != null; result2 = patternVertex.exec(text))

			{
				((Geometry)geometry).getVertices().add( new Vector3(  Double.parseDouble( result2.getGroup(1) ),  Double.parseDouble( result2.getGroup(3) ),  Double.parseDouble( result2.getGroup(5) ) ) );
			}

			int length = ((Geometry)geometry).getVertices().size();

			((Geometry)geometry).getFaces().add( new Face3( length - 3, length - 2, length - 1, normal ) );

		}

		geometry.computeBoundingBox();
		geometry.computeBoundingSphere();

		return geometry;
	}

	private Uint8Array ensureBinary( String buf ) {

		Uint8Array array_buffer = Uint8Array.create(buf.length());
		
		for(int i = 0; i < buf.length(); i++) 
		{
			array_buffer.set(i, Character.codePointAt(buf, i) & 0xff); // implicitly assumes little-endian
		}
		
		return  array_buffer;
	}
	
	private String ensureString ( ArrayBuffer buf ) {

		Uint8Array array_buffer = Uint8Array.create(buf);
		 StringBuilder builder = new StringBuilder(buf.getByteLength());
		for(int i = 0; i < buf.getByteLength(); i++) {
			builder.append(Character.toChars(array_buffer.get(i))); // implicitly assumes little-endian
		}
		return builder.toString();
	}

	private boolean isBinary() {

		DataView reader = DataView.create( this.binData );
		int face_size = (32 / 8 * 3) + ((32 / 8 * 3) * 3) + (16 / 8);
		int n_faces = (int)reader.getUint32(80, true);
		int expect = 80 + (32 / 8) + (n_faces * face_size);
			
		if ( expect == reader.byteLength() ) {
			
			return true;
			
		}

		// some binary files will have different size from expected,
		// checking characters higher than ASCII to confirm is binary
		int fileLength = reader.byteLength();
		for ( int index = 0; index < fileLength; index ++ ) {

			if ( reader.getUint8(index) > 127 ) {
				
				return true;
				
			}

		}

		return false;
	}

}
