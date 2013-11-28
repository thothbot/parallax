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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.enums.TextureWrapMode;
import thothbot.parallax.core.client.shaders.NormalMapShader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.client.textures.CompressedTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Geometry.MorphColor;
import thothbot.parallax.core.shared.materials.HasAmbientEmissiveColor;
import thothbot.parallax.core.shared.materials.HasBumpMap;
import thothbot.parallax.core.shared.materials.HasColor;
import thothbot.parallax.core.shared.materials.HasLightMap;
import thothbot.parallax.core.shared.materials.HasMap;
import thothbot.parallax.core.shared.materials.HasNormalMap;
import thothbot.parallax.core.shared.materials.HasSpecularMap;
import thothbot.parallax.core.shared.materials.HasVertexColors;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.Material.COLORS;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;
import thothbot.parallax.loader.shared.json.JsoMaterial;
import thothbot.parallax.loader.shared.json.JsoMorphColors;
import thothbot.parallax.loader.shared.json.JsoMorphTargets;
import thothbot.parallax.loader.shared.json.JsoObject;
import thothbot.parallax.loader.shared.json.JsoObjectFactory;
import thothbot.parallax.loader.shared.json.JsoTextureWrapMode;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.json.client.JSONException;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class JsonLoader extends Loader 
{

	private JsoObject object;
	private Geometry geometry;
	
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
	}
	
	public Geometry getGeometry() 
	{
		return this.geometry;
	}
	
	public void morphColorsToFaceColors() 
	{
		if ( geometry.getMorphColors() != null && geometry.getMorphColors().size() > 0 ) 
		{
			MorphColor colorMap = geometry.getMorphColors().get( 0 );

			for ( int i = 0; i < colorMap.colors.size(); i ++ ) 
			{
				geometry.getFaces().get(i).setColor( colorMap.colors.get(i) );
				geometry.getFaces().get(i).getColor().offsetHSL( 0, 0.3, 0 );
			}
		}
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
		{
			this.materials.add( createMaterial( material ) );
		}
		geometry.setMaterials(this.materials);
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

		// Special case for normal map
		if(jsonMaterial.getMapNormal() != null)
		{
			material = new ShaderMaterial( new NormalMapShader() );
			((ShaderMaterial)material).setLights(true);
			((ShaderMaterial)material).setFog(true);
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
			
		Color diffuseColor = 
				(jsonMaterial.getColorDiffuse() != null) ? getColor(jsonMaterial.getColorDiffuse()) 
			  : (jsonMaterial.getDbgColor() > 0)         ? new Color(jsonMaterial.getDbgColor()) 
			  : null;
						
		if(diffuseColor != null)
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "uDiffuseColor" ).setValue(diffuseColor);	
			}
			else if(material instanceof HasColor)
			{

				((HasColor) material).setColor(diffuseColor);
			}
		}

		if(jsonMaterial.getColorSpecular() != null)
		{
			Color color = getColor(jsonMaterial.getColorSpecular());
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "uSpecularColor" ).setValue(color);
			}
			else if(material instanceof MeshPhongMaterial)
			{
				((MeshPhongMaterial)material).setSpecular(color);
			}
		}

		if(jsonMaterial.getColorAmbient() != null )
		{
			Color color = getColor(jsonMaterial.getColorAmbient());
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "uAmbientColor" ).setValue(color);
			}
			else if( material instanceof HasAmbientEmissiveColor)
			{
				((HasAmbientEmissiveColor)material).setAmbient(color);	
			}
		}
		
		if(jsonMaterial.getTransparent())
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "uOpacity" ).setValue(jsonMaterial.getTransparency());
			}
			else 
			{

				material.setOpacity(jsonMaterial.getTransparency());
			}
		}
		
		if(jsonMaterial.getSpecularCoef() > 0)
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "uShininess" ).setValue(jsonMaterial.getSpecularCoef());
			}
			else if(material instanceof MeshPhongMaterial)
			{
				((MeshPhongMaterial)material).setShininess(jsonMaterial.getSpecularCoef());
			}
		}

		// textures

		if ( jsonMaterial.getMapDiffuse() != null ) 
		{
			Texture texture = create_texture(jsonMaterial.getMapDiffuse(),
					jsonMaterial.getMapDiffuseRepeat(),
					jsonMaterial.getMapDiffuseOffset(),
					jsonMaterial.getMapDiffuseWrap(),
					jsonMaterial.getMapDiffuseAnisotropy());

			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "tDiffuse" ).setValue(texture);
				uniforms.get( "enableDiffuse" ).setValue(true);
			}
			else if(material instanceof HasMap)
			{
				((HasMap)material).setMap(texture);
			}
		}

		if ( jsonMaterial.getMapLight() != null ) 
		{
			Texture texture = create_texture(jsonMaterial.getMapLight(),
					jsonMaterial.getMapLightRepeat(),
					jsonMaterial.getMapLightOffset(),
					jsonMaterial.getMapLightWrap(),
					jsonMaterial.getMapLightAnisotropy());
			
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "tAO" ).setValue(texture);
				uniforms.get( "enableAO" ).setValue(true);
			}
			else if(material instanceof HasLightMap)
			{
				((HasLightMap)material).setLightMap(texture);
			}
		}
		
		if ( jsonMaterial.getMapSpecular() != null) 
		{
			Texture texture = create_texture(jsonMaterial.getMapSpecular(),
					jsonMaterial.getMapSpecularRepeat(),
					jsonMaterial.getMapSpecularOffset(),
					jsonMaterial.getMapSpecularWrap(),
					jsonMaterial.getMapSpecularAnisotropy());
			
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "tSpecular" ).setValue(texture);
				uniforms.get( "enableSpecular" ).setValue(true);
			}
			else if( material instanceof HasSpecularMap )
			{
				((HasSpecularMap)material).setSpecularMap(texture);
			}
		}

		if ( jsonMaterial.getMapBump() != null && material instanceof HasBumpMap) 
		{
			((HasBumpMap)material).setBumpMap(
					create_texture(jsonMaterial.getMapBump(),
							jsonMaterial.getMapBumpRepeat(),
							jsonMaterial.getMapBumpOffset(),
							jsonMaterial.getMapBumpWrap(),
							jsonMaterial.getMapBumpAnisotropy()));
			
			if ( jsonMaterial.getMapBumpScale() > 0) 
			{
				((HasBumpMap)material).setBumpScale(jsonMaterial.getMapBumpScale());
			}
		}
		
		if ( jsonMaterial.getMapNormal() != null && material instanceof HasNormalMap) 
		{
			Map<String, Uniform> uniforms = material.getShader().getUniforms();

			uniforms.get( "tNormal" ).setValue( create_texture(jsonMaterial.getMapNormal(),
					jsonMaterial.getMapNormalRepeat(),
					jsonMaterial.getMapNormalOffset(),
					jsonMaterial.getMapNormalWrap(),
					jsonMaterial.getMapNormalAnisotropy()) );

			if ( jsonMaterial.getMapNormalFactor() > 0 ) 
			{
				((Vector2)uniforms.get( "uNormalScale" ).getValue()).set( 
						jsonMaterial.getMapNormalFactor(), jsonMaterial.getMapNormalFactor() );
			}
		}

		if(jsonMaterial.getDbgName() != null)
		{
			material.setName(jsonMaterial.getDbgName());
		}

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
		{
			if ( uvs.get( i ).size() > 0) 
				nUvLayers ++;
		}

		// 0-index is initialized already
		for ( int i = 0; i < nUvLayers; i++ ) 
		{
			geometry.getFaceUvs().add( i, new ArrayList<Vector2>());
			geometry.getFaceVertexUvs().add( i, new ArrayList<List<Vector2>>());
		}
		
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
			{
				face.setMaterialIndex(faces.get(offset++));
			}

			if ( hasFaceUv ) 
			{
				for ( int i = 0; i < nUvLayers; i++ ) 
				{
					List<Double> uvLayer = uvs.get(i);

					int uvIndex = faces.get(offset++);
					Vector2 UV = new Vector2( uvLayer.get(uvIndex * 2), uvLayer.get(uvIndex * 2 + 1));

					this.geometry.getFaceUvs().get(i).add(UV);
				}
			}

			if ( hasFaceVertexUv ) 
			{
				for ( int i = 0; i < nUvLayers; i++ ) 
				{
					List<Double> uvLayer = uvs.get(i);

					List<Vector2> UVs = new ArrayList<Vector2>();

					for ( int j = 0; j < nVertices; j ++ ) 
					{
						int uvIndex = faces.get(offset++);
						UVs.add( new Vector2( uvLayer.get(uvIndex * 2), uvLayer.get(uvIndex * 2 + 1) ) );
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

	private Texture create_texture( String sourceFile, List<Integer> repeat, List<Double> offset, List<JsoTextureWrapMode> wrap, int anisotropy ) 
	{
		boolean isCompressed = sourceFile.toLowerCase().endsWith(".dds");
		String fullPath =  getTexturePath() + sourceFile;

		final Texture texture;
		
		if ( isCompressed ) 
		{
			texture = new CompressedTexture(fullPath);
		} 
		else 
		{
			texture = new Texture(fullPath, new Texture.ImageLoadHandler() {
				
				@Override
				public void onImageLoad(Texture texture) 
				{
					int oWidth =  texture.getImage().getOffsetWidth();
					int oHeight = texture.getImage().getOffsetHeight();
							
					if ( !Mathematics.isPowerOfTwo( oWidth ) || !Mathematics.isPowerOfTwo( oHeight ) ) 
					{
						CanvasElement canvas = Document.get().createElement("canvas").cast();
						int width = Mathematics.getNextHighestPowerOfTwo(oWidth);
						int height = Mathematics.getNextHighestPowerOfTwo(oHeight);
						canvas.setWidth(width);
						canvas.setHeight(height);

						Context2d context = canvas.getContext2d();
						context.drawImage( (ImageElement)texture.getImage(), 0, 0, width, height );

						texture.setImage(canvas);
					} 

					texture.setNeedsUpdate(true);
				}
			});
			texture.setNeedsUpdate(false);
		}

		if( repeat != null) 
		{
			texture.getRepeat().set(repeat.get(0), repeat.get(1));

			if ( repeat.get( 0 ) != 1 )
				texture.setWrapS(TextureWrapMode.REPEAT);
			if ( repeat.get( 1 ) != 1 )
				texture.setWrapT(TextureWrapMode.REPEAT);
		}

		if ( offset != null) 
		{
			texture.getOffset().set(offset.get(0), offset.get(1));
		}

		if ( wrap != null) 
		{
			if ( wrap.get(0) != null )
				texture.setWrapS(wrap.get(0).getValue());
			if ( wrap.get(1) != null ) 
				texture.setWrapT(wrap.get(1).getValue());
		}

		if ( anisotropy > 0) 
		{
			texture.setAnisotropy(anisotropy);
		}
		Log.error("========", sourceFile, texture.getRepeat(), texture.getWrapS(), texture.getWrapT(), texture.getAnisotropy(), texture.getOffset());
		return texture;
	}

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
}

