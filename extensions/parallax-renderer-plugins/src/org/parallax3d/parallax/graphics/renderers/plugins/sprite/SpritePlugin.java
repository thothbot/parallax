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

package org.parallax3d.parallax.graphics.renderers.plugins.sprite;

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.lights.Light;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.renderers.Plugin;
import org.parallax3d.parallax.graphics.renderers.plugins.sprite.shaders.SpriteShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.AbstractFog;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Quaternion;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;
import org.parallax3d.parallax.system.gl.enums.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SpritePlugin extends Plugin
{
	Float32Array vertices;
	Uint16Array faces;
	
	int vertexBuffer; //WebGLBuffer
	int elementBuffer; //WebGLBuffer
	
	SpriteShader shader;
	
	// decompose matrixWorld

	Vector3 spritePosition = new Vector3();
	Quaternion spriteRotation = new Quaternion();
	Vector3 spriteScale = new Vector3();
	
	private List<Sprite> objects;

	private static final String FOG_TYPE = "fogType";

	public SpritePlugin(GLRenderer renderer, Scene scene)
	{
		super(renderer, scene);

		GL20 gl = getRenderer().gl;
		
		vertices = Float32Array.create( new double[]{
			-0.5, -0.5, 0, 0,
			0.5, -0.5, 1, 0,
			0.5, 0.5, 1, 1,
			-0.5, 0.5, 0, 1
		});
		faces = Uint16Array.create( new int[]{
				0, 1, 2,
				0, 2, 3
		});
		
		vertexBuffer  = gl.glGenBuffer();
		elementBuffer = gl.glGenBuffer();

		gl.glBindBuffer( BufferTarget.ARRAY_BUFFER.getValue(), vertexBuffer );
		gl.glBufferData( BufferTarget.ARRAY_BUFFER.getValue(), vertices.getByteLength(), vertices.getTypedBuffer(), BufferUsage.STATIC_DRAW.getValue() );

		gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), elementBuffer );
		gl.glBufferData( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), faces.getByteLength(), faces.getTypedBuffer(), BufferUsage.STATIC_DRAW.getValue() );

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
		if(this.objects == null || this.objects.isEmpty())
		{
			this.objects = (List<Sprite>)(ArrayList)getScene().getChildrenByClass(Sprite.class, true);
		}
		
		return this.objects;
	}

	@Override
	public void render(GL20 gl, Camera camera, List<Light> lights, int viewportWidth, int viewportHeight)
	{
		List<Sprite> sprites = getObjects();
		int nSprites = sprites.size();

		if ( nSprites == 0 ) return;

		FastMap<Uniform> uniforms = this.shader.getUniforms();
		FastMap<Integer> attributesLocations = this.shader.getAttributesLocations();

		// setup gl

		gl.glUseProgram( this.shader.getProgram() );

		gl.glEnableVertexAttribArray( attributesLocations.get("position") );
		gl.glEnableVertexAttribArray( attributesLocations.get("uv") );

		gl.glDisable( EnableCap.CULL_FACE.getValue() );
		gl.glEnable( EnableCap.BLEND.getValue() );

		gl.glBindBuffer( BufferTarget.ARRAY_BUFFER.getValue(), vertexBuffer );
		gl.glVertexAttribPointer( attributesLocations.get("position"), 2, DataType.FLOAT.getValue(), false, 2 * 8, 0 );
		gl.glVertexAttribPointer( attributesLocations.get("uv"), 2, DataType.FLOAT.getValue(), false, 2 * 8, 8 );

		gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), elementBuffer );

		gl.glUniformMatrix4fv( uniforms.get("projectionMatrix").getLocation(), 1, false, camera.getProjectionMatrix().getArray().getTypedBuffer() );

		gl.glActiveTexture( TextureUnit.TEXTURE0.getValue() );
		gl.glUniform1i( uniforms.get("map").getLocation(), 0 );
		
		int oldFogType = 0;
		int sceneFogType = 0;
		AbstractFog fog = scene.getFog();

		if ( fog != null ) {

			gl.glUniform3f( uniforms.get("fogColor").getLocation(), (float)fog.getColor().getR(), (float)fog.getColor().getG(), (float)fog.getColor().getB() );

			if ( fog instanceof Fog) {

				gl.glUniform1f( uniforms.get("fogNear").getLocation(), (float) ((Fog)fog).getNear() );
				gl.glUniform1f( uniforms.get("fogFar").getLocation(), (float) ((Fog)fog).getFar() );

				gl.glUniform1i( uniforms.get(FOG_TYPE).getLocation(), 1 );
				oldFogType = 1;
				sceneFogType = 1;

			} else if ( fog instanceof FogExp2) {

				gl.glUniform1f( uniforms.get("fogDensity").getLocation(), (float)((FogExp2)fog).getDensity() );

				gl.glUniform1i( uniforms.get(FOG_TYPE).getLocation(), 2 );
				oldFogType = 2;
				sceneFogType = 2;

			}

		} else {

			gl.glUniform1i( uniforms.get(FOG_TYPE).getLocation(), 0 );
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

			gl.glUniform1f( uniforms.get("alphaTest").getLocation(), (float) material.getAlphaTest() );
			gl.glUniformMatrix4fv( uniforms.get("modelViewMatrix").getLocation(), 1, false, sprite._modelViewMatrix.getArray().getTypedBuffer());
			
			sprite.getMatrixWorld().decompose( spritePosition, spriteRotation, spriteScale );
			
			int fogType = 0;

			if ( scene.getFog() != null && material.isFog() ) {

				fogType = sceneFogType;

			}

			if ( oldFogType != fogType ) {

				gl.glUniform1i( uniforms.get(FOG_TYPE).getLocation(), fogType );
				oldFogType = fogType;

			}

			if (  material.getMap() != null 
					&& material.getMap().getImage() != null 
					&& material.getMap().getImage().getWidth() > 0 ) {

				gl.glUniform2f( uniforms.get("uvOffset").getLocation(), (float)material.getMap().getOffset().getX(), (float)material.getMap().getOffset().getY() );
				gl.glUniform2f( uniforms.get("uvScale").getLocation(), (float)material.getMap().getRepeat().getX(), (float)material.getMap().getRepeat().getY() );

			} else {

				gl.glUniform2f( uniforms.get("uvOffset").getLocation(), 0.0f, 0.0f );
				gl.glUniform2f( uniforms.get("uvScale").getLocation(), 1.0f, 1.0f );

			}
			
			gl.glUniform1f( uniforms.get("opacity").getLocation(), (float)material.getOpacity() );
			gl.glUniform3f( uniforms.get("color").getLocation(),
					(float)material.getColor().getR(), (float)material.getColor().getG(), (float)material.getColor().getB() );

			gl.glUniform1f( uniforms.get("rotation").getLocation(), (float)material.getRotation() );
			gl.glUniform2fv( uniforms.get("scale").getLocation(), 2, new float[]{(float)spriteScale.getX(), (float)spriteScale.getY()}, 0 );

			getRenderer().setBlending( material.getBlending() );
			getRenderer().setDepthTest( material.isDepthTest() );
			getRenderer().setDepthWrite( material.isDepthWrite() );

			
			if (  material.getMap() != null 
					&& material.getMap().getImage() != null 
					&& material.getMap().getImage().getWidth() > 0 ) {
			
				getRenderer().setTexture( material.getMap(), 0 );
			}

			gl.glDrawElements( BeginMode.TRIANGLES.getValue(), 6, DrawElementsType.UNSIGNED_SHORT.getValue(), 0 );
		}

		// restore gl

		gl.glEnable( EnableCap.CULL_FACE.getValue() );

		getRenderer().resetGLState();
	}
}
