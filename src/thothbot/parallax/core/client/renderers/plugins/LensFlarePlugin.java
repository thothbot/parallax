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

package thothbot.parallax.core.client.renderers.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLProgram;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLShader;
import thothbot.parallax.core.client.gl2.WebGLTexture;
import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.ShaderLensFlare;
import thothbot.parallax.core.client.shader.ShaderLensFlareVertexTexture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Vector2f;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.objects.LensFlare;
import thothbot.parallax.core.shared.scenes.Scene;

public final class LensFlarePlugin extends Plugin
{
	private WebGLRenderer renderer;
	private class LensFlareGeometry 
	{
		Float32Array vertices;
		Uint16Array faces;
		
		WebGLBuffer vertexBuffer;
		WebGLBuffer elementBuffer;
		
		WebGLTexture tempTexture;
		WebGLTexture occlusionTexture;
		
		WebGLProgram program;
		Map<String, Integer> attributes;
		Map<String, WebGLUniformLocation> uniforms;
		
		boolean hasVertexTexture;
		boolean attributesEnabled;
	}
	
	private LensFlareGeometry lensFlare;
	
	@Override
	public void init(WebGLRenderer webGLRenderer) 
	{
		this.renderer = webGLRenderer;
		this.lensFlare = new LensFlareGeometry();
		
		WebGLRenderingContext gl = this.renderer.getGL();
		

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

		lensFlare.vertexBuffer     = gl.createBuffer();
		lensFlare.elementBuffer    = gl.createBuffer();

		gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), lensFlare.vertexBuffer );
		gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), lensFlare.vertices, GLenum.STATIC_DRAW.getValue() );

		gl.bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.elementBuffer );
		gl.bufferData( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.faces, GLenum.STATIC_DRAW.getValue() );

		// textures

		lensFlare.tempTexture      = gl.createTexture();
		lensFlare.occlusionTexture = gl.createTexture();

		gl.bindTexture( GLenum.TEXTURE_2D.getValue(), lensFlare.tempTexture );
		gl.texImage2D( GLenum.TEXTURE_2D.getValue(), 0, GLenum.RGB.getValue(), 16, 16, 0, GLenum.RGB.getValue(), GLenum.UNSIGNED_BYTE.getValue(), null );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_WRAP_S.getValue(), GLenum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_WRAP_T.getValue(), GLenum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_MAG_FILTER.getValue(), GLenum.NEAREST.getValue() );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_MIN_FILTER.getValue(), GLenum.NEAREST.getValue() );

		gl.bindTexture( GLenum.TEXTURE_2D.getValue(), lensFlare.occlusionTexture );
		gl.texImage2D( GLenum.TEXTURE_2D.getValue(), 0, GLenum.RGBA.getValue(), 16, 16, 0, GLenum.RGBA.getValue(), GLenum.UNSIGNED_BYTE.getValue(), null );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_WRAP_S.getValue(), GLenum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_WRAP_T.getValue(), GLenum.CLAMP_TO_EDGE.getValue() );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_MAG_FILTER.getValue(), GLenum.NEAREST.getValue() );
		gl.texParameteri( GLenum.TEXTURE_2D.getValue(), GLenum.TEXTURE_MIN_FILTER.getValue(), GLenum.NEAREST.getValue() );

		if ( gl.getParameteri( GLenum.MAX_VERTEX_TEXTURE_IMAGE_UNITS.getValue() ) <= 0 ) 
		{
			lensFlare.hasVertexTexture = false;
			lensFlare.program = createProgram( new ShaderLensFlare() );
		} 
		else 
		{
			lensFlare.hasVertexTexture = true;
			lensFlare.program = createProgram( new ShaderLensFlareVertexTexture() );
		}

		lensFlare.attributes = new HashMap<String, Integer>();
		lensFlare.uniforms = new HashMap<String, WebGLUniformLocation>();

		lensFlare.attributes.put("vertex", gl.getAttribLocation ( lensFlare.program, "position" ) );
		lensFlare.attributes.put("uv",     gl.getAttribLocation ( lensFlare.program, "uv" ) );

		lensFlare.uniforms.put("renderType", gl.getUniformLocation( lensFlare.program, "renderType" ) );
		lensFlare.uniforms.put("map",        gl.getUniformLocation( lensFlare.program, "map" ) );
		lensFlare.uniforms.put("occlusionMap", gl.getUniformLocation( lensFlare.program, "occlusionMap" ) );
		lensFlare.uniforms.put("opacity",    gl.getUniformLocation( lensFlare.program, "opacity" ) );
		lensFlare.uniforms.put("color",      gl.getUniformLocation( lensFlare.program, "color" ) );
		lensFlare.uniforms.put("scale",      gl.getUniformLocation( lensFlare.program, "scale" ) );
		lensFlare.uniforms.put("rotation",   gl.getUniformLocation( lensFlare.program, "rotation" ) );
		lensFlare.uniforms.put("screenPosition", gl.getUniformLocation( lensFlare.program, "screenPosition" ) );

		lensFlare.attributesEnabled = false;
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
	public void render(Scene scene, Camera camera, int viewportWidth, int viewportHeight) 
	{
		List<LensFlare> flares = scene.__webglFlares;
		int nFlares = flares.size();

		if ( nFlares == 0 ) return;

		WebGLRenderingContext gl = this.renderer.getGL();

		Vector3f tempPosition = new Vector3f();

		float invAspect = (float)viewportHeight / viewportWidth;
		float halfViewportWidth = viewportWidth * 0.5f;
		float halfViewportHeight = viewportHeight * 0.5f;

		float size = 16f / viewportHeight;
		Vector2f scale = new Vector2f( size * invAspect, size );

		Vector3f screenPosition = new Vector3f( 1, 1, 0 );
		Vector2f screenPositionPixels = new Vector2f( 1, 1 );

		Map<String, WebGLUniformLocation> uniforms = this.lensFlare.uniforms;
		Map<String, Integer> attributes = this.lensFlare.attributes;

		// set _lensFlare program and reset blending

		gl.useProgram( lensFlare.program );

		if ( ! lensFlare.attributesEnabled ) 
		{
			gl.enableVertexAttribArray( lensFlare.attributes.get("vertex") );
			gl.enableVertexAttribArray( lensFlare.attributes.get("uv") );

			lensFlare.attributesEnabled = true;
		}

		// loop through all lens flares to update their occlusion and positions
		// setup gl and common used attribs/unforms

		gl.uniform1i( uniforms.get("occlusionMap"), 0 );
		gl.uniform1i( uniforms.get("map"), 1 );

		gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), lensFlare.vertexBuffer );
		gl.vertexAttribPointer( attributes.get("vertex"), 2, GLenum.FLOAT.getValue(), false, 2 * 8, 0 );
		gl.vertexAttribPointer( attributes.get("uv"), 2, GLenum.FLOAT.getValue(), false, 2 * 8, 8 );

		gl.bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER.getValue(), lensFlare.elementBuffer );

		gl.disable( GLenum.CULL_FACE.getValue() );
		gl.depthMask( false );

		for ( int i = 0; i < nFlares; i ++ ) 
		{
			size = 16f / viewportHeight;
			scale.set( size * invAspect, size );

			// calc object screen position

			LensFlare flare = flares.get( i );

			tempPosition.set( 
					flare.getMatrixWorld().getArray().get(12), 
					flare.getMatrixWorld().getArray().get(13), 
					flare.getMatrixWorld().getArray().get(14) );

			camera.getMatrixWorldInverse().multiplyVector3( tempPosition );
			camera.getProjectionMatrix().multiplyVector3( tempPosition );

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

				gl.activeTexture( GLenum.TEXTURE1.getValue() );
				gl.bindTexture( GLenum.TEXTURE_2D.getValue(), lensFlare.tempTexture );
				gl.copyTexImage2D( GLenum.TEXTURE_2D.getValue(), 0, GLenum.RGB.getValue(), (int)screenPositionPixels.getX() - 8, (int)screenPositionPixels.getY() - 8, 16, 16, 0 );

				// render pink quad

				gl.uniform1i( uniforms.get("renderType"), 0 );
				gl.uniform2f( uniforms.get("scale"), scale.getX(), scale.getY() );
				gl.uniform3f( uniforms.get("screenPosition"), screenPosition.getX(), screenPosition.getY(), screenPosition.getZ() );

				gl.disable( GLenum.BLEND.getValue() );
				gl.enable( GLenum.DEPTH_TEST.getValue() );

				gl.drawElements( GLenum.TRIANGLES.getValue(), 6, GLenum.UNSIGNED_SHORT.getValue(), 0 );

				// copy result to occlusionMap

				gl.activeTexture( GLenum.TEXTURE0.getValue() );
				gl.bindTexture( GLenum.TEXTURE_2D.getValue(), lensFlare.occlusionTexture );
				gl.copyTexImage2D( GLenum.TEXTURE_2D.getValue(), 0, GLenum.RGBA.getValue(), (int)screenPositionPixels.getX() - 8, (int)screenPositionPixels.getY() - 8, 16, 16, 0 );

				// restore graphics

				gl.uniform1i( uniforms.get("renderType"), 1 );
				gl.disable( GLenum.DEPTH_TEST.getValue() );

				gl.activeTexture( GLenum.TEXTURE1.getValue() );
				gl.bindTexture( GLenum.TEXTURE_2D.getValue(), lensFlare.tempTexture );
				gl.drawElements( GLenum.TRIANGLES.getValue(), 6, GLenum.UNSIGNED_SHORT.getValue(), 0 );

				// update object positions

				flare.getPositionScreen().copy( screenPosition );

				flare.getUpdateCallback().update();

				// render flares

				gl.uniform1i( uniforms.get("renderType"), 2 );
				gl.enable( GLenum.BLEND.getValue() );

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

						gl.uniform3f( uniforms.get("screenPosition"), screenPosition.getX(), screenPosition.getY(), screenPosition.getZ() );
						gl.uniform2f( uniforms.get("scale"), scale.getX(), scale.getY() );
						gl.uniform1f( uniforms.get("rotation"), sprite.rotation );

						gl.uniform1f( uniforms.get("opacity"), sprite.opacity );
						gl.uniform3f( uniforms.get("color"), sprite.color.getR(), sprite.color.getG(), sprite.color.getB() );

//						renderer.setBlending( sprite.blending, sprite.blendEquation, sprite.blendSrc, sprite.blendDst );
						renderer.setBlending( sprite.blending );
						renderer.setTexture( sprite.texture, 1 );

						gl.drawElements( GLenum.TRIANGLES.getValue(), 6, GLenum.UNSIGNED_SHORT.getValue(), 0 );
					}
				}
			}
		}

		// restore gl

		gl.enable( GLenum.CULL_FACE.getValue() );
		gl.enable( GLenum.DEPTH_TEST.getValue() );
		gl.depthMask( true );

	}
	
	private WebGLProgram createProgram ( Shader shader ) 
	{
		WebGLRenderingContext gl = this.renderer.getGL();
		WebGLProgram program = gl.createProgram();

		WebGLShader fragmentShader = gl.createShader( GLenum.FRAGMENT_SHADER.getValue() );
		WebGLShader vertexShader = gl.createShader( GLenum.VERTEX_SHADER.getValue() );

		gl.shaderSource( fragmentShader, shader.getFragmentSource() );
		gl.shaderSource( vertexShader, shader.getVertexSource() );

		gl.compileShader( fragmentShader );
		gl.compileShader( vertexShader );

		gl.attachShader( program, fragmentShader );
		gl.attachShader( program, vertexShader );

		gl.linkProgram( program );

		return program;
	}
}
