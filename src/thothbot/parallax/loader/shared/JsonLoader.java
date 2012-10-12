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
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.loader.shared;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Geometry.MorphColor;
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.core.Vector4;
import thothbot.parallax.core.shared.materials.HasAmbientEmissiveColor;
import thothbot.parallax.core.shared.materials.HasColor;
import thothbot.parallax.core.shared.materials.HasSpecularMap;
import thothbot.parallax.core.shared.materials.HasVertexColors;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.materials.Material.COLORS;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.utils.ColorUtils;
import thothbot.parallax.loader.shared.json.JsoMaterial;
import thothbot.parallax.loader.shared.json.JsoMorphColors;
import thothbot.parallax.loader.shared.json.JsoMorphTargets;
import thothbot.parallax.loader.shared.json.JsoObjectFactory;
import thothbot.parallax.loader.shared.json.JsoObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class JsonLoader extends Loader 
{

	private JsoObject object;
	private Geometry geometry;
	private Mesh mesh;
	private Material material;
	private MorphAnimation animation;
	
	private List<Material> materials;
	
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
		geometry.computeFaceNormals();

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
			material.setColor( new Color(0xffffff) );
			material.setSpecular( new Color(0xffffff) );
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
		JsoObjectFactory factory = GWT.create(JsoObjectFactory.class);

		try 
		{
			AutoBean<JsoObject> bean = AutoBeanCodex.decode(factory, JsoObject.class, iJSonString);
			object = bean.as();
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
		if(object.getMaterials() == null) 
			return;
		
		Log.debug("JSON parseMaterials()");
		
		this.materials = new ArrayList<Material>(); 
		for ( JsoMaterial material: object.getMaterials() )
			this.materials.add( createMaterial( material ) );
	}
	
	private Material createMaterial(JsoMaterial jsonMaterial)
	{
		// defaults
		Material material = new MeshLambertMaterial();
		material.setOpacity(1.0);
		((MeshLambertMaterial)material).setColor(new Color(0xeeeeee));
		
		if(jsonMaterial.getShading() != null)
		{
			if(jsonMaterial.getShading().compareToIgnoreCase("phong") == 0)
			{
				material = new MeshPhongMaterial();
			}
			else if(jsonMaterial.getShading().compareToIgnoreCase("basic") == 0)
			{
				material = new MeshBasicMaterial();
			}
		}
		
		// parameters from model file

		if ( jsonMaterial.getBlending() != null ) 
		{
			material.setBlending(jsonMaterial.getBlending().getValue());
		}

		material.setTransparent(jsonMaterial.getTransparent());
		material.setDepthTest(jsonMaterial.getDepthTest());
		material.setDepthWrite(jsonMaterial.getDepthWrite());
		
		if(jsonMaterial.getVertexColors() && material instanceof HasVertexColors)
		{
			((HasVertexColors) material).setVertexColors(COLORS.VERTEX);
		}
			
		if(material instanceof HasColor)
		{
			if(jsonMaterial.getColorDiffuse() != null)
			{
				((HasColor) material).setColor(getColor(jsonMaterial.getColorDiffuse()));
			}
			else if(jsonMaterial.getDbgColor() > 0)
			{
				((HasColor) material).setColor(new Color(jsonMaterial.getDbgColor()));
			}
		}
		
		if(jsonMaterial.getColorSpecular() != null && material instanceof MeshPhongMaterial)
		{
			((MeshPhongMaterial)material).setSpecular(getColor(jsonMaterial.getColorSpecular()));
		}

		if(jsonMaterial.getColorAmbient() != null && material instanceof HasAmbientEmissiveColor)
		{
			((HasAmbientEmissiveColor)material).setAmbient(getColor(jsonMaterial.getColorAmbient()));
		}
		
		if(jsonMaterial.getTransparent())
		{
			material.setOpacity(jsonMaterial.getTransparency());
		}
		
		if(jsonMaterial.getSpecularCoef() > 0 && material instanceof MeshPhongMaterial)
		{
			((MeshPhongMaterial)material).setShininess(jsonMaterial.getSpecularCoef());
		}
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
		if(object.getVertices() == null) 
			return;
		
		Log.debug("JSON parseVertices()");
		
		List<Double> vertices = object.getVertices();
		
		double scale = getScale();
		int offset = 0;
		int zLength = vertices.size();

		while ( offset < zLength ) 
		{
			Vector3 vertex = new Vector3();

			vertex.setX( vertices.get(offset++) * scale );
			vertex.setY( vertices.get(offset++) * scale );
			vertex.setZ( vertices.get(offset++) * scale );

			this.geometry.getVertices().add( vertex );
		}
	}
	
	private void parseFaces()
	{
		if(object.getFaces() == null) 
			return;
		
		Log.debug("JSON parseFaces()");
		
		List<Integer> faces = object.getFaces();

		List<List<Double>> uvs = object.getUvs();
		int nUvLayers = 0;
		
		// disregard empty arrays
		for ( int i = 0; i < uvs.size(); i++ )
			if ( uvs.get( i ).size() > 0) nUvLayers ++;
		
		List<Double> normals = object.getNormals();
		List<Integer> colors = object.getColors();
				
		int offset = 0;
		int zLength = faces.size();

		while ( offset < zLength ) 
		{
			int type = faces.get(offset++);

			boolean isQuad          	= isBitSet( type, 0 );
			boolean hasMaterial         = isBitSet( type, 1 );
			boolean hasFaceUv           = isBitSet( type, 2 );
			boolean hasFaceVertexUv     = isBitSet( type, 3 );
			boolean hasFaceNormal       = isBitSet( type, 4 );
			boolean hasFaceVertexNormal = isBitSet( type, 5 );
			boolean hasFaceColor	    = isBitSet( type, 6 );
			boolean hasFaceVertexColor  = isBitSet( type, 7 );


			Face3 face;
			int nVertices;
			if ( isQuad ) 
			{
				nVertices = 4;
				face = new Face4(faces.get(offset++), faces.get(offset++), faces.get(offset++),faces.get(offset++));
			} 
			else 
			{
				nVertices = 3;
				face = new Face3(faces.get(offset++), faces.get(offset++), faces.get(offset++));
			}

			if ( hasMaterial ) 
				face.setMaterialIndex(faces.get(offset++));

			// to get face <=> uv index correspondence
//			int fi = geometry.getFaces().size();

			if ( hasFaceUv ) 
			{
				for ( int i = 0; i < nUvLayers; i++ ) 
				{
					List<Double> uvLayer = uvs.get(i);

					int uvIndex = faces.get(offset++);

					UV UV = new UV( uvLayer.get(uvIndex * 2), uvLayer.get(uvIndex * 2 + 1));

					this.geometry.getFaceUvs().get(i).add(UV);
				}
			}

			if ( hasFaceVertexUv ) 
			{
				for ( int i = 0; i < nUvLayers; i++ ) 
				{
					List<Double> uvLayer = uvs.get(i);

					List<UV> UVs = new ArrayList<UV>();

					for ( int j = 0; j < nVertices; j ++ ) 
					{
						int uvIndex = faces.get(offset++);
						UVs.add( new UV( uvLayer.get(uvIndex * 2), uvLayer.get(uvIndex * 2 + 1) ) );
					}

					geometry.getFaceVertexUvs().get(i).add(UVs);
				}
			}

			if ( hasFaceNormal ) 
			{
				int normalIndex = faces.get(offset++) * 3;

				Vector3 normal = new Vector3();

				normal.setX( normals.get( normalIndex ++ ) );
				normal.setY( normals.get( normalIndex ++ ) );
				normal.setZ( normals.get( normalIndex ) );

				face.setNormal(normal);
			}

			if ( hasFaceVertexNormal ) 
			{
				for ( int i = 0; i < nVertices; i++ ) 
				{
					int normalIndex = faces.get(offset++) * 3;
					Vector3 normal = new Vector3();
					
					normal.setX( normals.get( normalIndex ++ ) );
					normal.setY( normals.get( normalIndex ++ ) );
					normal.setZ( normals.get( normalIndex ) );

					face.getVertexNormals().add( normal );
				}
			}


			if ( hasFaceColor ) 
			{
				int colorIndex = faces.get(offset++);
				face.setColor(new Color(colors.get(colorIndex)));
			}

			if ( hasFaceVertexColor ) 
			{
				for ( int i = 0; i < nVertices; i++ ) 
				{
					int colorIndex = faces.get(offset++);
					face.getVertexColors().add(new Color(colors.get(colorIndex)));
				}
			}

			this.geometry.getFaces().add( face );
		}
	}
	
	private void parseSkin() 
	{
		Log.debug("JSON parseSkin()");
		
		if ( object.getSkinWeights() != null ) 
		{
			List<Double> skinWeights = object.getSkinWeights();
			for ( int i = 0, l = skinWeights.size(); i < l; i += 2 ) 
			{
				geometry.getSkinWeights().add( new Vector4(skinWeights.get(i), skinWeights.get(i + 1), 0, 0 ) );
			}

		}

		if ( object.getSkinIndices() != null) 
		{
			List<Integer> skinIndices = object.getSkinIndices();

			for ( int i = 0, l = skinIndices.size(); i < l; i += 2 ) 
			{
				geometry.getSkinIndices().add( new Vector4(skinIndices.get(i), skinIndices.get(i + 1), 0, 0) );
			}
		}

//		geometry.bones = json.bones;
//		geometry.animation = json.animation;
	}

	private void parseMorphing() 
	{
		Log.debug("JSON parseMorphing()");
		
		double scale = getScale();
		
		if ( object.getMorphTargets() != null) 
		{
			List<JsoMorphTargets> morphTargets = object.getMorphTargets();
			
			for ( int i = 0, l = morphTargets.size(); i < l; i ++ ) 
			{
				Geometry.MorphTarget morphTarget = geometry.new MorphTarget();
				morphTarget.name = morphTargets.get(i).getName();
				morphTarget.vertices = new ArrayList<Vector3>();
				
				List<Double> srcVertices = morphTargets.get(i).getVertices();
				for( int v = 0, vl = srcVertices.size(); v < vl; v += 3 ) 
				{
					morphTarget.vertices.add( 
							new Vector3(
									srcVertices.get(v) * scale, 
									srcVertices.get(v + 1) * scale,
									srcVertices.get(v + 2) * scale));
				}

				geometry.getMorphTargets().add(morphTarget);
			}
		}

		if ( object.getMorphColors() != null ) 
		{
			List<JsoMorphColors> morphColors = object.getMorphColors();
			
			for ( int i = 0, l = morphColors.size(); i < l; i++ ) 
			{
				Geometry.MorphColor morphColor = geometry.new MorphColor();
				morphColor.name = morphColors.get(i).getName();
				morphColor.colors = new ArrayList<Color>();
								
				List<Double> srcColors = morphColors.get(i).getColors();
				for ( int c = 0, cl = srcColors.size(); c < cl; c += 3 ) 
				{
					Color color = new Color( 0xffaa00 );
					color.setRGB(srcColors.get(c), srcColors.get(c + 1), srcColors.get(c + 2));
					morphColor.colors.add(color);
				}
				
				geometry.getMorphColors().add(morphColor);
			}
		}
	}
	
	private double getScale()
	{
		return ( object.getScale() > 0 ) 
				? 1.0 / object.getScale() : 1.0;
	}
	
	private boolean isBitSet( int value, int position ) 
	{
		return (value & ( 1 << position )) > 0;
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
	

	private Color getColor( List<Double> rgb ) 
	{
		return new Color(
				  ((int)(rgb.get(0) * 255) << 16 ) 
				+ ((int)(rgb.get(1) * 255) << 8 ) 
				+  (int)(rgb.get(2) * 255));
	}

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
				ColorUtils.adjustHSV( geometry.getFaces().get(i).getColor(), 0.0, 0.125, 0.0 );
			}
		}
	}
}
