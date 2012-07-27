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
import thothbot.parallax.core.shared.core.Geometry.MorphColor;
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.core.Vector4f;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.utils.ColorUtils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class Json extends Loader 
{

	private JSONObject json;
	private Geometry geometry;
	private Mesh mesh;
	private Material material;
	private MorphAnimation animation;
	
	private List<Material> materials;
	private boolean isParsed = false;
	
	@Override
	public void parse(String string) 
	{
		if(!isThisJsonStringValid(string))
			return;
		
		Log.debug("JSON parse()");
		
		geometry = new Geometry();
		
		parseMaterials();
		parseVertices();
		parseFaces();
		parseSkin();
		parseMorphing();

		geometry.computeCentroids();
		geometry.computeFaceNormals(false);

		if ( hasNormals() ) 
			geometry.computeTangents();
		
		geometry.computeMorphNormals();
		
		getAnimation().init(getMesh(), getGeometry());
	}

	public MorphAnimation getAnimation()
	{
		if(this.animation == null)
		{
			this.animation = new MorphAnimation();
		}
		
		return this.animation;
	}
	
	public Geometry getGeometry() 
	{
		return this.geometry;
	}
	
	public Material getMaterial() 
	{
		if(this.material == null)
		{
			MeshPhongMaterial material = new MeshPhongMaterial();
			material.setColor( new Color3f(0xffffff) );
			material.setSpecular( new Color3f(0xffffff) );
			material.setShininess(20);
			material.setMorphTargets( true );
			material.setMorphNormals( true );
			material.setVertexColors(Material.COLORS.FACE);
			material.setShading(Material.SHADING.SMOOTH); 
			material.setPerPixel(false);
			setMaterial(material);
			
			morphColorsToFaceColors();
		}
		
		return this.material;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	public Mesh getMesh() 
	{
		if(this.mesh == null)
		{
			this.mesh = new Mesh(getGeometry(), getMaterial());
		}
		
		return this.mesh;
	}
	
	private boolean isThisJsonStringValid(String iJSonString) 
	{ 
		try 
		{   
			json = JSONParser.parseLenient(iJSonString).isObject();
		} 
		catch ( JSONException e) 
		{
			Log.error("Could not parser JSON data");
			return false;
		}  

		return true;
	}
		
	private void parseMaterials()
	{
		if(! json.containsKey("materials")) 
			return;
		
		Log.debug("JSON parseMaterials()");
		
		JSONArray materials = json.get("materials").isArray();
		
		this.materials= new ArrayList<Material>(); 
		for ( int i = 0; i < materials.size(); ++ i )
			this.materials.add( createMaterial( materials.get(i).isObject()) );
	}
	
	private Material createMaterial(JSONObject jsonMaterial)
	{
		// defaults
		Material material = new MeshLambertMaterial();
		material.setOpacity(1.0f);
		((MeshLambertMaterial)material).setColor(new Color3f(0xeeeeee));
		if(jsonMaterial.containsKey("wireframe"))
			((MeshLambertMaterial)material).setWireframe(true);
		
		if(jsonMaterial.containsKey("shading"))
		{
			if(jsonMaterial.get("shading").isString().stringValue().compareToIgnoreCase("phong") == 0)
			{
				material = new MeshPhongMaterial();
			}
			else if(jsonMaterial.get("shading").isString().stringValue().compareToIgnoreCase("basic") == 0)
			{
				material = new MeshBasicMaterial();
			}
		}
		
		// parameters from model file

//		if ( m.blending !== undefined && THREE[ m.blending ] !== undefined ) {
//
//			mpars.blending = THREE[ m.blending ];
//
//		}
//
//		if ( m.transparent !== undefined || m.opacity < 1.0 ) {
//
//			mpars.transparent = m.transparent;
//
//		}
//
//		if ( m.depthTest !== undefined ) {
//
//			mpars.depthTest = m.depthTest;
//
//		}
//
//		if ( m.depthWrite !== undefined ) {
//
//			mpars.depthWrite = m.depthWrite;
//
//		}
//
//		if ( m.vertexColors !== undefined ) {
//
//			if ( m.vertexColors == "face" ) {
//
//				mpars.vertexColors = THREE.FaceColors;
//
//			} else if ( m.vertexColors ) {
//
//				mpars.vertexColors = THREE.VertexColors;
//
//			}
//
//		}
//
//		// colors
//
//		if ( m.colorDiffuse ) {
//
//			mpars.color = rgb2hex( m.colorDiffuse );
//
//		} else if ( m.DbgColor ) {
//
//			mpars.color = m.DbgColor;
//
//		}
//
//		if ( m.colorSpecular ) {
//
//			mpars.specular = rgb2hex( m.colorSpecular );
//
//		}
//
//		if ( m.colorAmbient ) {
//
//			mpars.ambient = rgb2hex( m.colorAmbient );
//
//		}
//
//		// modifiers
//
//		if ( m.transparency ) {
//
//			mpars.opacity = m.transparency;
//
//		}
//
//		if ( m.specularCoef ) {
//
//			mpars.shininess = m.specularCoef;
//
//		}
//
//		// textures
//
//		if ( m.mapDiffuse && texturePath ) {
//
//			create_texture( mpars, "map", m.mapDiffuse, m.mapDiffuseRepeat, m.mapDiffuseOffset, m.mapDiffuseWrap );
//
//		}
//
//		if ( m.mapLight && texturePath ) {
//
//			create_texture( mpars, "lightMap", m.mapLight, m.mapLightRepeat, m.mapLightOffset, m.mapLightWrap );
//
//		}
//
//		if ( m.mapNormal && texturePath ) {
//
//			create_texture( mpars, "normalMap", m.mapNormal, m.mapNormalRepeat, m.mapNormalOffset, m.mapNormalWrap );
//
//		}
//
//		if ( m.mapSpecular && texturePath ) {
//
//			create_texture( mpars, "specularMap", m.mapSpecular, m.mapSpecularRepeat, m.mapSpecularOffset, m.mapSpecularWrap );
//
//		}
//
//		// special case for normal mapped material
//
//		if ( m.mapNormal ) {
//
//			var shader = THREE.ShaderUtils.lib[ "normal" ];
//			var uniforms = THREE.UniformsUtils.clone( shader.uniforms );
//
//			uniforms[ "tNormal" ].texture = mpars.normalMap;
//
//			if ( m.mapNormalFactor ) {
//
//				uniforms[ "uNormalScale" ].value = m.mapNormalFactor;
//
//			}
//
//			if ( mpars.map ) {
//
//				uniforms[ "tDiffuse" ].texture = mpars.map;
//				uniforms[ "enableDiffuse" ].value = true;
//
//			}
//
//			if ( mpars.specularMap ) {
//
//				uniforms[ "tSpecular" ].texture = mpars.specularMap;
//				uniforms[ "enableSpecular" ].value = true;
//
//			}
//
//			if ( mpars.lightMap ) {
//
//				uniforms[ "tAO" ].texture = mpars.lightMap;
//				uniforms[ "enableAO" ].value = true;
//
//			}
//
//			// for the moment don't handle displacement texture
//
//			uniforms[ "uDiffuseColor" ].value.setHex( mpars.color );
//			uniforms[ "uSpecularColor" ].value.setHex( mpars.specular );
//			uniforms[ "uAmbientColor" ].value.setHex( mpars.ambient );
//
//			uniforms[ "uShininess" ].value = mpars.shininess;
//
//			if ( mpars.opacity !== undefined ) {
//
//				uniforms[ "uOpacity" ].value = mpars.opacity;
//
//			}
//
//			var parameters = { fragmentShader: shader.fragmentShader, vertexShader: shader.vertexShader, uniforms: uniforms, lights: true, fog: true };
//			var material = new THREE.ShaderMaterial( parameters );
//
//		} else {
//
//			var material = new THREE[ mtype ]( mpars );
//
//		}
//
//		if ( m.DbgName != undefined ) material.name = m.DbgName;

		return material;
	}

	private void parseVertices()
	{
		if(! json.containsKey("vertices")) 
			return;
		
		Log.debug("JSON parseVertices()");
		
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
		
		Log.debug("JSON parseFaces()");
		
		JSONArray faces = json.get("faces").isArray();

		JSONArray uvs = json.get("uvs").isArray();
		int nUvLayers = 0;
		
		// disregard empty arrays
		for ( int i = 0; i < uvs.size(); i++ )
			if ( uvs.get( i ).isArray().size() > 0) nUvLayers ++;
		
		JSONArray normals = json.get("normals").isArray();
		JSONArray colors = json.get("colors").isArray();
				
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
	
	private void parseSkin() 
	{
		Log.debug("JSON parseSkin()");
		
		if ( json.containsKey("skinWeights") ) 
		{
			JSONArray skinWeights = json.get("skinWeights").isArray();
			for ( int i = 0, l = skinWeights.size(); i < l; i += 2 ) 
			{
				geometry.getSkinWeights().add( new Vector4f( 
						(float)value( skinWeights, i ),
						(float)value( skinWeights, i + 1 ), 
						0, 0 ) );
			}

		}

		if ( json.containsKey("skinIndices") ) 
		{
			JSONArray skinIndices = json.get("skinIndices").isArray();
			for ( int i = 0, l = skinIndices.size(); i < l; i += 2 ) 
			{
				geometry.getSkinIndices().add( new Vector4f(
						(float)value( skinIndices, i ),
						(float)value( skinIndices, i + 1 ), 
						0, 0) );
			}
		}

//		geometry.bones = json.bones;
//		geometry.animation = json.animation;
	}

	private void parseMorphing() 
	{
		Log.debug("JSON parseMorphing()");
		
		double scale = getScale();
		
		if ( json.containsKey("morphTargets")) 
		{
			JSONArray morphTargets = json.get("morphTargets").isArray();
			for ( int i = 0, l = morphTargets.size(); i < l; i ++ ) 
			{
				Geometry.MorphTarget morphTarget = geometry.new MorphTarget();
				morphTarget.name = morphTargets.get(i).isObject().get("name").isString().stringValue();
				morphTarget.vertices = new ArrayList<Vector3f>();
				
				JSONArray srcVertices = morphTargets.get(i).isObject().get("vertices").isArray();
				for( int v = 0, vl = srcVertices.size(); v < vl; v += 3 ) 
				{
					morphTarget.vertices.add( new Vector3f(
						(float)(value( srcVertices, v ) * scale),
						(float)(value( srcVertices, v + 1 ) * scale),
						(float)(value( srcVertices, v + 2 ) * scale)
					) );
				}

				geometry.getMorphTargets().add(morphTarget);
			}
		}

		if ( json.containsKey("morphColors") ) 
		{
			JSONArray morphColors = json.get("morphColors").isArray();
			for ( int i = 0, l = morphColors.size(); i < l; i++ ) 
			{
				Geometry.MorphColor morphColor = geometry.new MorphColor();
				morphColor.name = morphColors.get(i).isObject().get("name").isString().stringValue();
				morphColor.colors = new ArrayList<Color3f>();
								
				JSONArray srcColors = morphColors.get(i).isObject().get("colors").isArray();
				for ( int c = 0, cl = srcColors.size(); c < cl; c += 3 ) 
				{
					Color3f color = new Color3f( 0xffaa00 );
					color.setRGB( 
						(float)value(srcColors, c ), 
						(float)value(srcColors, c + 1 ), 
						(float)value(srcColors, c + 2 ) );
					morphColor.colors.add(color);
				}
				
				geometry.getMorphColors().add(morphColor);
			}
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
	
	private boolean is_pow2( int n ) 
	{
		double l = Math.log( n ) / Math.log(2);
		return Math.floor( l ) == l;
	}

	private int nearest_pow2( int n ) 
	{
		double l = Math.log( n ) / Math.log(2);
		return (int) Math.pow( 2, Math.round(  l ) );
	}

//	private void load_image( where, url ) 
//	{
//		var image = new Image();
//		image.onload = function () {
//
//			if ( !is_pow2( this.width ) || !is_pow2( this.height ) ) {
//
//				var width = nearest_pow2( this.width );
//				var height = nearest_pow2( this.height );
//
//				where.image.width = width;
//				where.image.height = height;
//				where.image.getContext( '2d' ).drawImage( this, 0, 0, width, height );
//
//			} else {
//
//				where.image = this;
//
//			}
//
//			where.needsUpdate = true;
//
//		}
//
//		image.crossOrigin = _this.crossOrigin;
//		image.src = url;
//	}
//	
//	function create_texture( where, name, sourceFile, repeat, offset, wrap ) {
//
//		var texture = document.createElement( 'canvas' );
//
//		where[ name ] = new THREE.Texture( texture );
//		where[ name ].sourceFile = sourceFile;
//
//		if( repeat ) {
//
//			where[ name ].repeat.set( repeat[ 0 ], repeat[ 1 ] );
//
//			if ( repeat[ 0 ] != 1 ) where[ name ].wrapS = THREE.RepeatWrapping;
//			if ( repeat[ 1 ] != 1 ) where[ name ].wrapT = THREE.RepeatWrapping;
//
//		}
//
//		if ( offset ) {
//
//			where[ name ].offset.set( offset[ 0 ], offset[ 1 ] );
//
//		}
//
//		if ( wrap ) {
//
//			var wrapMap = {
//				"repeat": THREE.RepeatWrapping,
//				"mirror": THREE.MirroredRepeatWrapping
//			}
//
//			if ( wrapMap[ wrap[ 0 ] ] !== undefined ) where[ name ].wrapS = wrapMap[ wrap[ 0 ] ];
//			if ( wrapMap[ wrap[ 1 ] ] !== undefined ) where[ name ].wrapT = wrapMap[ wrap[ 1 ] ];
//
//		}
//
//		load_image( where[ name ], texturePath + "/" + sourceFile );
//	}
	
	private boolean hasNormals() 
	{
		for( int i = 0; i < this.materials.size(); i ++ ) 
			if (  this.materials.get(i) instanceof ShaderMaterial ) 
				return true;

		return false;
	}
	
	private void morphColorsToFaceColors() 
	{
		if ( geometry.getMorphColors() != null && geometry.getMorphColors().size() > 0 ) 
		{
			MorphColor colorMap = geometry.getMorphColors().get( 0 );

			for ( int i = 0; i < colorMap.colors.size(); i ++ ) 
			{
				geometry.getFaces().get(i).setColor( colorMap.colors.get(i) );
				ColorUtils.adjustHSV( geometry.getFaces().get(i).getColor(), 0f, 0.125f, 0f );
			}
		}
	}
}
