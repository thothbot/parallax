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
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.scenes.AbstractFog;
import thothbot.parallax.core.shared.scenes.Fog;
import thothbot.parallax.core.shared.scenes.FogExp2;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugins.sprite.shaders.SpriteShader;

public final class SpritePlugin extends Plugin 
{
	private List<Sprite> objects;
	
	Float32Array vertices;
	Uint16Array faces;
	
	WebGLBuffer vertexBuffer;
	WebGLBuffer elementBuffer;
	
	SpriteShader shader;
	
	// decompose matrixWorld

	Vector3 spritePosition = new Vector3();
	Quaternion spriteRotation = new Quaternion();
	Vector3 spriteScale = new Vector3();
		
	public SpritePlugin(WebGLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);

		WebGLRenderingContext gl = getRenderer().getGL();
		
		vertices = Float32Array.create(
				- 0.5, - 0.5,  0, 0,
				  0.5, - 0.5,  1, 0,
				  0.5,   0.5,  1, 1,
				- 0.5,   0.5,  0, 1
		);
		faces = Uint16Array.create(
				0, 1, 2,
				0, 2, 3
		);
		
		vertexBuffer  = gl.createBuffer();
		elementBuffer = gl.createBuffer();

		gl.bindBuffer( BufferTarget.ARRAY_BUFFER, vertexBuffer );
		gl.bufferData( BufferTarget.ARRAY_BUFFER, vertices, BufferUsage.STATIC_DRAW );

		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, elementBuffer );
		gl.bufferData( BufferTarget.ELEMENT_ARRAY_BUFFER, faces, BufferUsage.STATIC_DRAW );

		shader = new SpriteShader();
		shader.buildProgram(gl);
	}
	
	@Override
	public Plugin.TYPE getType()
	{
		return Plugin.TYPE.POST_RENDER;
	}
	
	public List<Sprite> getObjects() 
	{
		if(this.objects == null || this.objects.size() == 0)
		{
			this.objects = (List<Sprite>)(ArrayList)getScene().getChildrenByClass(Sprite.class, true);
		}
		
		return this.objects;
	}

	@Override
	public void render( Camera camera, List<Light> lights, int viewportWidth, int viewportHeight) 
	{
		List<Sprite> sprites = getObjects();
		int nSprites = sprites.size();

		if ( nSprites == 0 ) return;

		WebGLRenderingContext gl = getRenderer().getGL();

		Map<String, Uniform> uniforms = this.shader.getUniforms();
		Map<String, Integer> attributesLocations = this.shader.getAttributesLocations();

		// setup gl

		gl.useProgram( this.shader.getProgram() );

		gl.enableVertexAttribArray( attributesLocations.get("position") );
		gl.enableVertexAttribArray( attributesLocations.get("uv") );

		gl.disable( EnableCap.CULL_FACE );
		gl.enable( EnableCap.BLEND );

		gl.bindBuffer( BufferTarget.ARRAY_BUFFER, vertexBuffer );
		gl.vertexAttribPointer( attributesLocations.get("position"), 2, DataType.FLOAT, false, 2 * 8, 0 );
		gl.vertexAttribPointer( attributesLocations.get("uv"), 2, DataType.FLOAT, false, 2 * 8, 8 );

		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, elementBuffer );

		gl.uniformMatrix4fv( uniforms.get("projectionMatrix").getLocation(), false, camera.getProjectionMatrix().getArray() );

		gl.activeTexture( TextureUnit.TEXTURE0 );
		gl.uniform1i( uniforms.get("map").getLocation(), 0 );
		
		int oldFogType = 0;
		int sceneFogType = 0;
		AbstractFog fog = scene.getFog();

		if ( fog != null ) {

			gl.uniform3f( uniforms.get("fogColor").getLocation(), fog.getColor().getR(), fog.getColor().getG(), fog.getColor().getB() );

			if ( fog instanceof Fog ) {

				gl.uniform1f( uniforms.get("fogNear").getLocation(), ((Fog)fog).getNear() );
				gl.uniform1f( uniforms.get("fogFar").getLocation(), ((Fog)fog).getFar() );

				gl.uniform1i( uniforms.get("fogType").getLocation(), 1 );
				oldFogType = 1;
				sceneFogType = 1;

			} else if ( fog instanceof FogExp2 ) {

				gl.uniform1f( uniforms.get("fogDensity").getLocation(), ((FogExp2)fog).getDensity() );

				gl.uniform1i( uniforms.get("fogType").getLocation(), 2 );
				oldFogType = 2;
				sceneFogType = 2;

			}

		} else {

			gl.uniform1i( uniforms.get("fogType").getLocation(), 0 );
			oldFogType = 0;
			sceneFogType = 0;

		}


		// update positions and sort

		for( int i = 0; i < nSprites; i ++ ) 
		{
			Sprite sprite = sprites.get( i );

			if ( ! sprite.isVisible() ) continue;

			sprite._modelViewMatrix.multiply( camera.getMatrixWorldInverse(), sprite.getMatrixWorld());
			sprite.setZ( - sprite._modelViewMatrix.getArray().get(14) );
		}

		Collections.sort((List<Sprite>)(ArrayList)sprites);

		// render all sprites

		for( int i = 0; i < nSprites; i ++ ) 
		{
			Sprite sprite = sprites.get( i );
			SpriteMaterial material = (SpriteMaterial) sprite.getMaterial();

			if ( ! sprite.isVisible() ) continue;

			gl.uniform1f( uniforms.get("alphaTest").getLocation(), material.getAlphaTest() );
			gl.uniformMatrix4fv( uniforms.get("modelViewMatrix").getLocation(), false, sprite._modelViewMatrix.getArray());
			
			sprite.getMatrixWorld().decompose( spritePosition, spriteRotation, spriteScale );
			
			int fogType = 0;

			if ( scene.getFog() != null && material.isFog() ) {

				fogType = sceneFogType;

			}

			if ( oldFogType != fogType ) {

				gl.uniform1i( uniforms.get("fogType").getLocation(), fogType );
				oldFogType = fogType;

			}

			if (  material.getMap() != null 
					&& material.getMap().getImage() != null 
					&& material.getMap().getImage().getOffsetWidth() > 0 ) {

				gl.uniform2f( uniforms.get("uvOffset").getLocation(), material.getMap().getOffset().getX(), material.getMap().getOffset().getY() );
				gl.uniform2f( uniforms.get("uvScale").getLocation(), material.getMap().getRepeat().getX(), material.getMap().getRepeat().getY() );

			} else {

				gl.uniform2f( uniforms.get("uvOffset").getLocation(), 0.0, 0.0 );
				gl.uniform2f( uniforms.get("uvScale").getLocation(), 1.0, 1.0 );

			}
			
			gl.uniform1f( uniforms.get("opacity").getLocation(), material.getOpacity() );
			gl.uniform3f( uniforms.get("color").getLocation(),
					material.getColor().getR(), material.getColor().getG(), material.getColor().getB() );

			gl.uniform1f( uniforms.get("rotation").getLocation(), material.getRotation() );
			gl.uniform2fv( uniforms.get("scale").getLocation(), new double[]{spriteScale.getX(), spriteScale.getY()} );

			//	renderer.setBlending( sprite.blending, sprite.blendEquation, sprite.blendSrc, sprite.blendDst );
			getRenderer().setBlending( material.getBlending() );
			getRenderer().setDepthTest( material.isDepthTest() );
			getRenderer().setDepthWrite( material.isDepthWrite() );

			
			if (  material.getMap() != null 
					&& material.getMap().getImage() != null 
					&& material.getMap().getImage().getOffsetWidth() > 0 ) {
			
				getRenderer().setTexture( material.getMap(), 0 );
			}

			gl.drawElements( BeginMode.TRIANGLES, 6, DrawElementsType.UNSIGNED_SHORT, 0 );
		}

		// restore gl

		gl.enable( EnableCap.CULL_FACE );

		getRenderer().resetGLState();
	}
}
