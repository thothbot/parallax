/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.loader.shared;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class Json extends Loader 
{

	private JSONObject json;
	private Geometry geometry;
	
	@Override
	public void parse(String string) 
	{
		if(!isThisJsonStringValid(string))
			return;
		
		geometry = new Geometry();
		
		parseVertices();
		parseFaces();
	}
	
	public Geometry getGeometry() {
		return this.geometry;
	}
	
	private boolean isThisJsonStringValid(String iJSonString) 
	{ 
		try 
		{   
			json = JSONParser.parseLenient(iJSonString).isObject();
		} 
		catch ( JSONException e) 
		{
			Log.error("Couldbot parser JSON data");
			return false;
		}  

		return true;
	}
		
	private void parseVertices()
	{
		if(! json.containsKey("vertices")) 
			return;
		
		JSONArray vertices = json.get("vertices").isArray();
		
		double scale = getScale();
		int offset = 0;
		int zLength = vertices.size();

		while ( offset < zLength ) 
		{
			Vector3f vertex = new Vector3f();

			vertex.setX( (float) ( value( vertices, offset ++ ) * scale) );
			vertex.setY( (float) ( value( vertices, offset ++ ) * scale) );
			vertex.setZ( (float) ( value( vertices, offset ++ ) * scale) );

			this.geometry.getVertices().add( vertex );
		}
	}
	
	private void parseFaces()
	{
		if(! json.containsKey("faces")) 
			return;
		
		JSONArray faces = json.get("faces").isArray();

		JSONArray uvs = json.get("uvs").isArray();
		int nUvLayers = 0;
		
		// disregard empty arrays
		for ( int i = 0; i < uvs.size(); i++ )
			if ( uvs.get( i ).isArray().size() > 0) nUvLayers ++;

//		for ( int i = 0; i < nUvLayers; i++ ) 
//		{
//			this.geometry.getFaceUvs().add(i, new ArrayList<UVf>());
//			this.geometry.getFaceVertexUvs().add(i, new ArrayList<List<UVf>>());
//		}
		
		JSONArray normals = json.get("normals").isArray();
		JSONArray colors = json.get("colors").isArray();
				
		double scale = getScale();
		int offset = 0;
		int zLength = faces.size();

		while ( offset < zLength ) 
		{
			int type = (int) value( faces, offset ++ );

			boolean isQuad          	= isBitSet( type, 0 );
			boolean hasMaterial         = isBitSet( type, 1 );
			boolean hasFaceUv           = isBitSet( type, 2 );
			boolean hasFaceVertexUv     = isBitSet( type, 3 );
			boolean hasFaceNormal       = isBitSet( type, 4 );
			boolean hasFaceVertexNormal = isBitSet( type, 5 );
			boolean hasFaceColor	    = isBitSet( type, 6 );
			boolean hasFaceVertexColor  = isBitSet( type, 7 );

//			Log.debug("parseFaces() type " + type + ", bits={" 
//					+ isQuad + ", " + hasMaterial + ", " + hasFaceUv + ", " + hasFaceVertexUv + ", " 
//					+ hasFaceNormal + ", " + hasFaceVertexNormal + ", " + hasFaceColor + ", " + hasFaceVertexColor + "}");

			Face3 face;
			int nVertices;
			if ( isQuad ) 
			{
				nVertices = 4;
				face = new Face4(
					(int)value( faces, offset ++ ),
					(int)value( faces, offset ++ ),
					(int)value( faces, offset ++ ),
					(int)value( faces, offset ++ ));
			} 
			else 
			{
				nVertices = 3;
				face = new Face3(
					(int)value( faces, offset ++ ),
					(int)value( faces, offset ++ ),
					(int)value( faces, offset ++ ));
			}

			if ( hasMaterial ) 
				face.setMaterialIndex((int)value( faces, offset ++ ));

			// to get face <=> uv index correspondence
//			int fi = geometry.getFaces().size();

			if ( hasFaceUv ) 
			{
				for ( int i = 0; i < nUvLayers; i++ ) 
				{
					JSONArray uvLayer = uvs.get(i).isArray();

					int uvIndex = (int)value( faces, offset ++ );

					UVf UVf = new UVf( 
						(float) value( uvLayer, uvIndex * 2), 
						(float) value( uvLayer, uvIndex * 2 + 1));

					this.geometry.getFaceUvs().get(i).add(UVf);
				}
			}

			if ( hasFaceVertexUv ) 
			{
				for ( int i = 0; i < nUvLayers; i++ ) 
				{
					JSONArray uvLayer = uvs.get(i).isArray();

					List<UVf> UVs = new ArrayList<UVf>();

					for ( int j = 0; j < nVertices; j ++ ) 
					{
						int uvIndex = (int)value( faces, offset ++ );
						UVs.add( new UVf( 
							(float) value( uvLayer, uvIndex * 2), 
							(float) value( uvLayer, uvIndex * 2 + 1)));
					}

					geometry.getFaceVertexUvs().get(i).add(UVs);
				}
			}

			if ( hasFaceNormal ) 
			{
				int normalIndex = (int)value( faces, offset ++ ) * 3;

				Vector3f normal = new Vector3f();

				normal.setX( (float) value( normals, normalIndex ++ ) );
				normal.setY( (float) value( normals, normalIndex ++ ) );
				normal.setZ( (float) value( normals, normalIndex ) );

				face.setNormal(normal);
			}

			if ( hasFaceVertexNormal ) 
			{
				for ( int i = 0; i < nVertices; i++ ) 
				{
					int normalIndex = (int)value( faces, offset ++ ) * 3;
					Vector3f normal = new Vector3f();
					
					normal.setX( (float) value( normals, normalIndex ++ ) );
					normal.setY( (float) value( normals, normalIndex ++ ) );
					normal.setZ( (float) value( normals, normalIndex ) );

					face.getVertexNormals().add( normal );
				}
			}


			if ( hasFaceColor ) 
			{
				int colorIndex = (int)value( faces, offset ++ );
				face.setColor(new Color3f((int)value(colors, colorIndex)));
			}

			if ( hasFaceVertexColor ) 
			{
				for ( int i = 0; i < nVertices; i++ ) 
				{
					int colorIndex = (int)value( faces, offset ++ );
					face.getVertexColors().add( new Color3f((int)value(colors, colorIndex) ));
				}
			}

			this.geometry.getFaces().add( face );
		}
	}
	
	private double getScale()
	{
		return ( json.containsKey("scale") ) 
				? 1.0 / json.get("scale").isNumber().doubleValue() : 1.0;
	}
	
	private boolean isBitSet( int value, int position ) 
	{
		return (value & ( 1 << position )) > 0;
	}
	
	private double value(JSONArray array, int offset) 
	{
		return array.get( offset ).isNumber().doubleValue();
	}
}
