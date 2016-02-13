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

package org.parallax3d.parallax.graphics.renderers.plugins.lensflare;

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.lights.Light;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.renderers.Plugin;
import org.parallax3d.parallax.graphics.renderers.plugins.lensflare.shaders.LensFlareShader;
import org.parallax3d.parallax.graphics.renderers.plugins.lensflare.shaders.LensFlareVertexTextureShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;
import org.parallax3d.parallax.system.gl.enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LensFlarePlugin extends Plugin
{

	public class LensFlareGeometry 
	{
		Float32Array vertices;
		Uint16Array faces;
		
		int vertexBuffer; //WebGLBuffer
		int elementBuffer; //WebGLBuffer
		
		int tempTexture; //WebGLTexture
		int occlusionTexture; //WebGLTexture
		
		LensFlareShader shader;
		
		boolean hasVertexTexture;
		boolean attributesEnabled;
	}

	private LensFlareGeometry lensFlare;
	private List<LensFlare> objects;
	
	public LensFlarePlugin(GLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);
		
		this.lensFlare = new LensFlareGeometry();
		
		GL20 gl = getRenderer().gl;

		lensFlare.vertices = Float32Array.create( 8 + 8 );
		lensFlare.faces = Uint16Array.create( 6 );

		int i = 0;
		lensFlare.vertices.set( i++, -1); lensFlare.vertices.set( i++, -1);	// vertex
		lensFlare.vertices.set( i++, 0);  lensFlare.vertices.set( i++, 0);	// uv... etc.

		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, -1);
		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, 0);

		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, 1);
		lensFlare.vertices.set( i++, 1);  lensFlare.vertices.set( i++, 1);

		lensFlare.vertices.set( i++, -1); lensFlare.vertices.set( i++, 1);
		lensFlare.vertices.set( i++, 0);  lensFlare.vertices.set( i++, 1);

		i = 0;
		lensFlare.faces.set( i++, 0); lensFlare.faces.set( i++, 1); lensFlare.faces.set( i++, 2);
		lensFlare.faces.set( i++, 0); lensFlare.faces.set( i++, 2); lensFlare.faces.set( i++, 3);

		// buffers

		lensFlare.vertexBuffer     = gl.glGenBuffer();
		lensFlare.elementBuffer    = gl.glGenBuffer();

		gl.glBindBuffer( BufferTarget.ARRAY_BUFFER.getValue(), lensFlare.vertexBuffer );
		gl.glBufferData( BufferTarget.ARRAY_BUFFER.getValue(), lensFlare.vertices.getByteLength(), lensFlare.vertices.getTypedBuffer(), BufferUsage.STATIC_DRAW.getValue() );

		gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.elementBuffer );
		gl.glBufferData( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.faces.getByteLength(), lensFlare.faces.getTypedBuffer(), BufferUsage.STATIC_DRAW.getValue() );

		// textures

		lensFlare.tempTexture      = gl.glGenTexture();
		lensFlare.occlusionTexture = gl.glGenTexture();

		gl.glBindTexture( TextureTarget.TEXTURE_2D.getValue(), lensFlare.tempTexture );
		gl.glTexImage2D( TextureTarget.TEXTURE_2D.getValue(), 0, 16, 16, 0, PixelFormat.RGB.getValue(), PixelType.UNSIGNED_BYTE.getValue(), 0, null );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_WRAP_S.getValue(), GL20.GL_CLAMP_TO_EDGE );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_WRAP_T.getValue(), GL20.GL_CLAMP_TO_EDGE );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_MAG_FILTER.getValue(), GL20.GL_NEAREST );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_MIN_FILTER.getValue(), GL20.GL_NEAREST );

		gl.glBindTexture( TextureTarget.TEXTURE_2D.getValue(), lensFlare.occlusionTexture );
		gl.glTexImage2D( TextureTarget.TEXTURE_2D.getValue(), 0, 16, 16, 0, PixelFormat.RGBA.getValue(), PixelType.UNSIGNED_BYTE.getValue(), 0, null );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_WRAP_S.getValue(), GL20.GL_CLAMP_TO_EDGE );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_WRAP_T.getValue(), GL20.GL_CLAMP_TO_EDGE );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_MAG_FILTER.getValue(), GL20.GL_NEAREST );
		gl.glTexParameteri( TextureTarget.TEXTURE_2D.getValue(), TextureParameterName.TEXTURE_MIN_FILTER.getValue(), GL20.GL_NEAREST );

//		if ( gl.getParameteri( WebGLConstants.MAX_VERTEX_TEXTURE_IMAGE_UNITS ) <= 0 )
//		{
//			lensFlare.hasVertexTexture = false;
//			lensFlare.shader = new LensFlareShader();
//		}
//		else
//		{
			lensFlare.hasVertexTexture = true;
			lensFlare.shader = new LensFlareVertexTextureShader();
//		}

		FastMap<Attribute> attributes = new FastMap<>();
		attributes.put("position", new Attribute(Attribute.TYPE.V3, null));
		attributes.put("uv", new Attribute(Attribute.TYPE.V3, null));
		lensFlare.shader.setAttributes(attributes);
		lensFlare.shader.buildProgram(gl);
	}
	
	@Override
	public Plugin.TYPE getType()
	{
		return Plugin.TYPE.POST_RENDER;
	}

	public List<LensFlare> getObjects() 
	{
		if(this.objects == null)
		{
			this.objects = (List<LensFlare>)(ArrayList)getScene().getChildrenByClass(LensFlare.class, true);
		}
		
		return (List<LensFlare>)(ArrayList)this.objects;
	}

	/**
	 * Render lens flares
	 * Method: renders 16x16 0xff00ff-colored points scattered over the light source area,
	 *         reads these back and calculates occlusion.
	 *         Then _lensFlare.update_lensFlares() is called to re-position and
	 *         update transparency of flares. Then they are rendered.
	 *
	 */
	@Override
	public void render(GL20 gl, Camera camera, List<Light> lights, int viewportWidth, int viewportHeight) 
	{
		List<LensFlare> flares = getObjects();
		int nFlares = flares.size();

		if ( nFlares == 0 ) return;

		Vector3 tempPosition = new Vector3();

		double invAspect = (double)viewportHeight / viewportWidth;
		double halfViewportWidth = viewportWidth * 0.5;
		double halfViewportHeight = viewportHeight * 0.5;

		double size = 16.0 / viewportHeight;
		Vector2 scale = new Vector2( size * invAspect, size );

		Vector3 screenPosition = new Vector3( 1, 1, 0 );
		Vector2 screenPositionPixels = new Vector2( 1, 1 );

		FastMap<Uniform> uniforms = this.lensFlare.shader.getUniforms();
		FastMap<Integer> attributesLocation = this.lensFlare.shader.getAttributesLocations();

		// set _lensFlare program and reset blending

		gl.glUseProgram( lensFlare.shader.getProgram() );

		if ( ! lensFlare.attributesEnabled ) 
		{
			gl.glEnableVertexAttribArray( attributesLocation.get("position") );
			gl.glEnableVertexAttribArray( attributesLocation.get("uv") );

			lensFlare.attributesEnabled = true;
		}

		// loop through all lens flares to update their occlusion and positions
		// setup gl and common used attribs/unforms

		gl.glUniform1i( uniforms.get("occlusionMap").getLocation(), 0 );
		gl.glUniform1i( uniforms.get("map").getLocation(), 1 );

		gl.glBindBuffer( BufferTarget.ARRAY_BUFFER.getValue(), lensFlare.vertexBuffer );
		gl.glVertexAttribPointer( attributesLocation.get("position"), 2, DataType.FLOAT.getValue(), false, 2 * 8, 0 );
		gl.glVertexAttribPointer( attributesLocation.get("uv"), 2, DataType.FLOAT.getValue(), false, 2 * 8, 8 );

		gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.elementBuffer );

		gl.glDisable( EnableCap.CULL_FACE.getValue() );
		gl.glDepthMask( false );

		for ( int i = 0; i < nFlares; i ++ ) 
		{
			size = 16.0 / viewportHeight;
			scale.set( size * invAspect, size );

			// calc object screen position

			LensFlare flare = flares.get( i );

			tempPosition.set( 
					flare.getMatrixWorld().getArray().get(12), 
					flare.getMatrixWorld().getArray().get(13), 
					flare.getMatrixWorld().getArray().get(14) );

			tempPosition.apply( camera.getMatrixWorldInverse() );
			tempPosition.applyProjection( camera.getProjectionMatrix() );

			// setup arrays for gl programs

			screenPosition.copy( tempPosition );

			screenPositionPixels.setX( screenPosition.getX() * halfViewportWidth + halfViewportWidth);
			screenPositionPixels.setY( screenPosition.getY() * halfViewportHeight + halfViewportHeight);

			// screen cull

			if ( lensFlare.hasVertexTexture || (
					screenPositionPixels.getX() > 0 &&
					screenPositionPixels.getX() < viewportWidth &&
					screenPositionPixels.getY() > 0 &&
					screenPositionPixels.getY() < viewportHeight ) 
			) {

				// save current RGB to temp texture

				gl.glActiveTexture( TextureUnit.TEXTURE1.getValue() );
				gl.glBindTexture( TextureTarget.TEXTURE_2D.getValue(), lensFlare.tempTexture );
				gl.glCopyTexImage2D( TextureTarget.TEXTURE_2D.getValue(), 0, PixelFormat.RGB.getValue(), (int)screenPositionPixels.getX() - 8, (int)screenPositionPixels.getY() - 8, 16, 16, 0 );

				// render pink quad

				gl.glUniform1i( uniforms.get("renderType").getLocation(), 0 );
				gl.glUniform2f( uniforms.get("scale").getLocation(), (float)scale.getX(), (float)scale.getY() );
				gl.glUniform3f( uniforms.get("screenPosition").getLocation(), (float)screenPosition.getX(), (float)screenPosition.getY(), (float)screenPosition.getZ() );

				gl.glDisable( EnableCap.BLEND.getValue() );
				gl.glEnable( EnableCap.DEPTH_TEST.getValue() );

				gl.glDrawElements( BeginMode.TRIANGLES.getValue(), 6, DrawElementsType.UNSIGNED_SHORT.getValue(), 0 );

				// copy result to occlusionMap

				gl.glActiveTexture( TextureUnit.TEXTURE0.getValue() );
				gl.glBindTexture( TextureTarget.TEXTURE_2D.getValue(), lensFlare.occlusionTexture );
				gl.glCopyTexImage2D( TextureTarget.TEXTURE_2D.getValue(), 0, PixelFormat.RGBA.getValue(), (int)screenPositionPixels.getX() - 8, (int)screenPositionPixels.getY() - 8, 16, 16, 0 );

				// restore graphics

				gl.glUniform1i( uniforms.get("renderType").getLocation(), 1 );
				gl.glDisable( EnableCap.DEPTH_TEST.getValue() );

				gl.glActiveTexture( TextureUnit.TEXTURE1.getValue() );
				gl.glBindTexture( TextureTarget.TEXTURE_2D.getValue(), lensFlare.tempTexture );
				gl.glDrawElements( BeginMode.TRIANGLES.getValue(), 6, DrawElementsType.UNSIGNED_SHORT.getValue(), 0 );

				// update object positions

				flare.getPositionScreen().copy( screenPosition );

				flare.getUpdateCallback().update();

				// render flares

				gl.glUniform1i( uniforms.get("renderType").getLocation(), 2 );
				gl.glEnable( EnableCap.BLEND.getValue() );

				for ( int j = 0, jl = flare.getLensFlares().size(); j < jl; j ++ ) 
				{
					LensFlare.LensSprite sprite = flare.getLensFlares().get( j );

					if ( sprite.opacity > 0.001 && sprite.scale > 0.001 ) 
					{
						screenPosition.setX( sprite.x );
						screenPosition.setY( sprite.y );
						screenPosition.setZ( sprite.z );

						size = sprite.size * sprite.scale / viewportHeight;

						scale.setX( size * invAspect );
						scale.setY( size );

						gl.glUniform3f( uniforms.get("screenPosition").getLocation(), (float)screenPosition.getX(), (float)screenPosition.getY(), (float)screenPosition.getZ() );
						gl.glUniform2f( uniforms.get("scale").getLocation(), (float)scale.getX(), (float)scale.getY() );
						gl.glUniform1f( uniforms.get("rotation").getLocation(), (float)sprite.rotation );

						gl.glUniform1f( uniforms.get("opacity").getLocation(), (float)sprite.opacity );
						gl.glUniform3f( uniforms.get("color").getLocation(), (float)sprite.color.getR(), (float)sprite.color.getG(), (float)sprite.color.getB() );

//						getRenderer().setBlending( sprite.blending, sprite.blendEquation, sprite.blendSrc, sprite.blendDst );
						getRenderer().setBlending( sprite.blending );
						getRenderer().setTexture( sprite.texture, 1 );

						gl.glDrawElements( BeginMode.TRIANGLES.getValue(), 6, DrawElementsType.UNSIGNED_SHORT.getValue(), 0 );
					}
				}
			}
		}

		// restore gl

		gl.glEnable( EnableCap.CULL_FACE.getValue() );
		gl.glEnable( EnableCap.DEPTH_TEST.getValue() );
		gl.glDepthMask( true );
		
		getRenderer().resetGLState();

	}
}
