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

package thothbot.parallax.plugins.sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.DrawElementsType;
import thothbot.parallax.core.client.gl2.enums.EnableCap;
import thothbot.parallax.core.client.gl2.enums.TextureUnit;
import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugins.sprite.shaders.SpriteShader;

public final class SpritePlugin extends Plugin 
{
	private class SpriteGeometry 
	{
		Float32Array vertices;
		Uint16Array faces;
		
		WebGLBuffer vertexBuffer;
		WebGLBuffer elementBuffer;
		
		SpriteShader shader;
		
		boolean attributesEnabled;
	}
	
	private SpriteGeometry sprite;
	private List<Sprite> objects;
	
	public SpritePlugin(WebGLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);
		
		this.sprite = new SpriteGeometry();
		
		WebGLRenderingContext gl = getRenderer().getGL();
		
		sprite.vertices = Float32Array.create( 8 + 8 );
		sprite.faces = Uint16Array.create( 6 );
		
		int i = 0;

		sprite.vertices.set( i++,  -1); sprite.vertices.set( i++,  -1);	// vertex 0
		sprite.vertices.set( i++,  0);  sprite.vertices.set( i++,  1);	// uv 0

		sprite.vertices.set( i++,  1);  sprite.vertices.set( i++,  -1);	// vertex 1
		sprite.vertices.set( i++,  1);  sprite.vertices.set( i++,  1);	// uv 1

		sprite.vertices.set( i++,  1);  sprite.vertices.set( i++,  1);	// vertex 2
		sprite.vertices.set( i++,  1);  sprite.vertices.set( i++,  0);	// uv 2

		sprite.vertices.set( i++,  -1); sprite.vertices.set( i++,  1);	// vertex 3
		sprite.vertices.set( i++,  0);  sprite.vertices.set( i++,  0);	// uv 3

		i = 0;

		sprite.faces.set( i++,  0); sprite.faces.set( i++,  1); sprite.faces.set( i++,  2);
		sprite.faces.set( i++,  0); sprite.faces.set( i++,  2); sprite.faces.set( i++,  3);

		sprite.vertexBuffer  = gl.createBuffer();
		sprite.elementBuffer = gl.createBuffer();

		gl.bindBuffer( BufferTarget.ARRAY_BUFFER, sprite.vertexBuffer );
		gl.bufferData( BufferTarget.ARRAY_BUFFER, sprite.vertices, BufferUsage.STATIC_DRAW );

		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, sprite.elementBuffer );
		gl.bufferData( BufferTarget.ELEMENT_ARRAY_BUFFER, sprite.faces, BufferUsage.STATIC_DRAW );

		sprite.shader = new SpriteShader();
		sprite.shader.buildProgram(gl);
	}
	
	@Override
	public Plugin.TYPE getType()
	{
		return Plugin.TYPE.POST_RENDER;
	}
	
	public List<Sprite> getObjects() 
	{
		if(this.objects == null)
		{
			this.objects = (List<Sprite>)(ArrayList)getScene().getChildrenByClass(Sprite.class, true);
		}
		
		return this.objects;
	}

	@Override
	public void render( Camera camera, int viewportWidth, int viewportHeight) 
	{
		List<Sprite> sprites = getObjects();
		int nSprites = sprites.size();

		if ( nSprites == 0 ) return;

		WebGLRenderingContext gl = getRenderer().getGL();

		Map<String, Uniform> uniforms = this.sprite.shader.getUniforms();
		Map<String, Integer> attributesLocations = this.sprite.shader.getAttributesLocations();

		double invAspect = (double)viewportHeight / viewportWidth;

		double halfViewportWidth = viewportWidth * 0.5;
		double halfViewportHeight = viewportHeight * 0.5;

		boolean mergeWith3D = true;

		// setup gl

		gl.useProgram( this.sprite.shader.getProgram() );

		if ( ! sprite.attributesEnabled ) 
		{
			gl.enableVertexAttribArray( attributesLocations.get("position") );
			gl.enableVertexAttribArray( attributesLocations.get("uv") );

			sprite.attributesEnabled = true;
		}

		gl.disable( EnableCap.CULL_FACE );
		gl.enable( EnableCap.BLEND );
		gl.depthMask( true );

		gl.bindBuffer( BufferTarget.ARRAY_BUFFER, sprite.vertexBuffer );
		gl.vertexAttribPointer( attributesLocations.get("position"), 2, DataType.FLOAT, false, 2 * 8, 0 );
		gl.vertexAttribPointer( attributesLocations.get("uv"), 2, DataType.FLOAT, false, 2 * 8, 8 );

		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, sprite.elementBuffer );

		gl.uniformMatrix4fv( uniforms.get("projectionMatrix").getLocation(), false, camera._projectionMatrixArray );

		gl.activeTexture( TextureUnit.TEXTURE0 );
		gl.uniform1i( uniforms.get("map").getLocation(), 0 );

		// update positions and sort

		for( int i = 0; i < nSprites; i ++ ) 
		{
			Sprite sprite = sprites.get( i );

			if ( ! sprite.isVisible() || sprite.getOpacity() == 0 ) continue;

			if( ! sprite.isUseScreenCoordinates() ) 
			{
				sprite._modelViewMatrix.multiply( camera.getMatrixWorldInverse(), sprite.getMatrixWorld());
				sprite.setZ( - sprite._modelViewMatrix.getArray().get(14) );
			} 
			else 
			{
				sprite.setZ( - sprite.getPosition().getZ() );
			}
		}

		Collections.sort((List<Sprite>)(ArrayList)sprites);

		// render all sprites

		for( int i = 0; i < nSprites; i ++ ) 
		{
			Sprite sprite = sprites.get( i );

			if ( ! sprite.isVisible() || sprite.getOpacity() == 0 ) continue;

			if ( sprite.getMap() != null 
					&& sprite.getMap().getImage() != null 
					&& sprite.getMap().getImage().getOffsetWidth() > 0 ) 
			{

				if ( sprite.isUseScreenCoordinates() ) 
				{
					gl.uniform1i( uniforms.get("useScreenCoordinates").getLocation(), 1 );
					gl.uniform3f( uniforms.get("screenPosition").getLocation(), ( sprite.getPosition().getX() - halfViewportWidth  ) / halfViewportWidth,
							( halfViewportHeight - sprite.getPosition().getY() ) / halfViewportHeight,
							Math.max( 0, Math.min( 1, sprite.getPosition().getZ() ) ) );
				} 
				else 
				{
					gl.uniform1i( uniforms.get("useScreenCoordinates").getLocation(), 0 );
					gl.uniform1i( uniforms.get("affectedByDistance").getLocation(), sprite.isAffectedByDistance() ? 1 : 0 );
					gl.uniformMatrix4fv( uniforms.get("modelViewMatrix").getLocation(), false, sprite._modelViewMatrix.getArray());
				}

				double size = sprite.getMap().getImage().getOffsetWidth() 
						/ ( sprite.isScaleByViewport() ? viewportHeight : 1.0 );

				double[] scale = { 
						size * invAspect * sprite.getScale().getX(),
						size * sprite.getScale().getY() };

				gl.uniform2f( uniforms.get("uvScale").getLocation(), sprite.getUvScale().getX(), sprite.getUvScale().getY() );
				gl.uniform2f( uniforms.get("uvOffset").getLocation(), sprite.getUvOffset().getX(), sprite.getUvOffset().getY() );
				gl.uniform2f( uniforms.get("alignment").getLocation(), sprite.getAlignment().get().getX(), sprite.getAlignment().get().getY() );

				gl.uniform1f( uniforms.get("opacity").getLocation(), sprite.getOpacity() );
				gl.uniform3f( uniforms.get("color").getLocation(), 
						sprite.getColor().getR(), 
						sprite.getColor().getG(), 
						sprite.getColor().getB() );

				gl.uniform1f( uniforms.get("rotation").getLocation(), sprite.getRotationFactor() );
				gl.uniform2fv( uniforms.get("scale").getLocation(), scale );

				if ( sprite.isMergeWith3D() && !mergeWith3D ) 
				{
					gl.enable( EnableCap.DEPTH_TEST );
					mergeWith3D = true;
				} 
				else if ( ! sprite.isMergeWith3D() && mergeWith3D ) 
				{
					gl.disable( EnableCap.DEPTH_TEST );
					mergeWith3D = false;
				}

				//	renderer.setBlending( sprite.blending, sprite.blendEquation, sprite.blendSrc, sprite.blendDst );
				getRenderer().setBlending( sprite.getBlending() );
				getRenderer().setTexture( sprite.getMap(), 0 );

				gl.drawElements( BeginMode.TRIANGLES, 6, DrawElementsType.UNSIGNED_SHORT, 0 );
			}
		}

		// restore gl

		gl.enable( EnableCap.CULL_FACE );
		gl.enable( EnableCap.DEPTH_TEST );
		gl.depthMask( true );
	}
}
