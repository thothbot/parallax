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

package org.parallax3d.parallax.loaders;

import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STLLoader extends ModelLoader
{
	FileHandle file;

	AbstractGeometry geometry;

	boolean hasColors;
	double alpha;
	
	public STLLoader(String url, ModelLoadHandler modelLoadHandler)
	{
		super(url, modelLoadHandler);
	}
	
	public boolean isHasColors() {
		return hasColors;
	}

	public double getAlpha() {
		return alpha;
	}
	
	@Override
	protected void parse(FileHandle result)
	{
		file = result;

		geometry = file.isText() ? parseASCII() : parseBinary();
	}

	@Override
	public AbstractGeometry getGeometry() {
		return geometry;
	}

	private AbstractGeometry parseBinary()
	{
		byte [] arr = file.readBytes();
		ByteBuffer reader = ByteBuffer.wrap(arr);
		reader.order(ByteOrder.LITTLE_ENDIAN);

		int faces = reader.getInt(80);
		boolean hasColors = false;
		Float32Array colors = null;

		double r = 0, g = 0, b = 0;
		double defaultR = 0, defaultG = 0, defaultB = 0, alpha = 0;
		// process STL header
		// check for default color in header ("COLOR=rgba" sequence).

		for ( int index = 0; index < 80 - 10; index ++ ) {

			if ((reader.getInt(index) == 0x434F4C4F /*COLO*/) &&
				(reader.getShort(index + 4) == 0x52 /*'R'*/) &&
				(reader.getShort(index + 5) == 0x3D /*'='*/)) {

					hasColors = true;
					colors = Float32Array.create( faces * 3 * 3);

					defaultR = (double)reader.getShort(index + 6) / 255.0;
					defaultG = (double)reader.getShort(index + 7) / 255.0;
					defaultB = (double)reader.getShort(index + 8) / 255.0;
					alpha = (double)reader.getShort(index + 9) / 255.0;
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

			double normalX = reader.getFloat(start);
			double normalY = reader.getFloat(start + 4);
			double normalZ = reader.getFloat(start + 8);

			if (hasColors) {

				int packedColor = reader.getShort(start + 48);

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

				vertices.set( offset     , reader.getFloat( vertexstart ));
				vertices.set( offset + 1 , reader.getFloat( vertexstart + 4 ));
				vertices.set( offset + 2 , reader.getFloat( vertexstart + 8 ));

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

		geometry.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
		geometry.addAttribute( "normal", new BufferAttribute( normals, 3 ) );

		if (hasColors) {
			geometry.addAttribute( "color", new BufferAttribute( colors, 3 ) );
			this.hasColors = true;
			this.alpha = alpha;
		}

		return geometry;
	}

	private AbstractGeometry parseASCII()
	{
		Geometry geometry = new Geometry();

		Vector3 normal = null;
		Pattern patternFace = Pattern.compile("facet([\\s\\S]*?)endfacet");

		String data = file.readString();
		Matcher result = patternFace.matcher(data);
		while (result.find())
		{
			String text = result.group(0);
			Pattern patternNormal = Pattern.compile("normal[\\s]+([\\-+]?[0-9]+\\.?[0-9]*([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+");

			Matcher result2 = patternNormal.matcher(text);
			while (result2.find()) {
				normal = new Vector3(  Double.parseDouble( result2.group(1) ),  Double.parseDouble( result2.group(3) ),  Double.parseDouble( result2.group(5) ) );
			}

			Pattern patternVertex = Pattern.compile("vertex[\\s]+([\\-+]?[0-9]+\\.?[0-9]*([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+[\\s]+([\\-+]?[0-9]*\\.?[0-9]+([eE][\\-+]?[0-9]+)?)+");
			result2 = patternVertex.matcher(text);
			while (result2.find()) {
				geometry.getVertices().add( new Vector3(  Double.parseDouble( result2.group(1) ),  Double.parseDouble( result2.group(3) ),  Double.parseDouble( result2.group(5) ) ) );
			}

			int length = geometry.getVertices().size();
			geometry.getFaces().add( new Face3( length - 3, length - 2, length - 1, normal ) );
		}

		geometry.computeBoundingBox();
		geometry.computeBoundingSphere();

		return geometry;
	}
}
