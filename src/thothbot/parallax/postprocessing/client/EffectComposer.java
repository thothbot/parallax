/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.postprocessing.client;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.renderers.WebGLRenderTarget;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.geometries.Plane;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.core.shared.textures.Texture;

import thothbot.parallax.postprocessing.client.shader.ShaderScreen;

public class EffectComposer
{
	private WebGLRenderTarget renderTarget1;
	private WebGLRenderTarget renderTarget2;
	
	private WebGLRenderTarget writeBuffer;
	private WebGLRenderTarget readBuffer;
	
	private List<Pass> passes;
	private ShaderPass copyPass;
	
	// shared ortho camera

	public static OrthographicCamera camera = new OrthographicCamera( 
			Pass.getRenderer().getCanvas().getWidth() / -2f, 
			Pass.getRenderer().getCanvas().getWidth() / 2f, 
			Pass.getRenderer().getCanvas().getHeight() / 2f, 
			Pass.getRenderer().getCanvas().getHeight() / -2f, 
			-10000f, 10000f
		);
	
	// shared fullscreen quad scene

	public static Plane geometry = new Plane( 1, 1 );
	public static Mesh quad = new Mesh( EffectComposer.geometry, null );
	public static Scene scene = new Scene();

	private static WebGLRenderTarget.WebGLRenderTargetOptions defaultRenderTargetOptions = new WebGLRenderTarget.WebGLRenderTargetOptions();
	
	static {
		defaultRenderTargetOptions.minFilter =  Texture.FILTER.LINEAR;
		defaultRenderTargetOptions.magFilter =  Texture.FILTER.LINEAR;
		defaultRenderTargetOptions.format =  Texture.FORMAT.RGB;
		defaultRenderTargetOptions.stencilBuffer =  false;
		
		geometry.applyMatrix( new Matrix4f().makeRotationX( (float) (Math.PI / 2.0) ) );
		
		quad.getPosition().setZ(-100);
		quad.getScale().set( Pass.getRenderer().getCanvas().getWidth(), Pass.getRenderer().getCanvas().getHeight(), 1 );

		scene.addChild( quad );
		scene.addChild( camera );
	}

	public EffectComposer( int width, int height )
	{
		this(new WebGLRenderTarget(width, height, EffectComposer.defaultRenderTargetOptions));
	}

	public EffectComposer( WebGLRenderTarget renderTarget ) 
	{
		this.renderTarget1 = renderTarget;

		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		this.passes = new ArrayList<Pass>();

		this.copyPass = new ShaderPass( new ShaderScreen() );
	}
	
	private void swapBuffers() 
	{
		WebGLRenderTarget tmp = this.readBuffer;
		this.readBuffer = this.writeBuffer;
		this.writeBuffer = tmp;
	}
	
	private void addPass( Pass pass ) 
	{
		this.passes.add( pass );
	}
	
	private void render( float delta ) 
	{
		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		boolean maskActive = false;

		for ( int i = 0; i < this.passes.size(); i ++ ) 
		{
			Pass pass = this.passes.get( i );

			if ( !pass.isEnabled() ) continue;

			pass.render( this.writeBuffer, this.readBuffer, delta, maskActive );

			if ( pass.isNeedsSwap() ) 
			{
				if ( maskActive ) 
				{
					WebGLRenderer.getInstance().getGL().stencilFunc( WebGLRenderingContext.NOTEQUAL, 1, 0xffffffff );

					this.copyPass.render( this.writeBuffer, this.readBuffer, delta, true );

					WebGLRenderer.getInstance().getGL().stencilFunc( WebGLRenderingContext.EQUAL, 1, 0xffffffff );
				}

				this.swapBuffers();

			}

			maskActive = pass.isMaskActive();
		}
	}

	private void reset( WebGLRenderTarget renderTarget ) 
	{
		this.renderTarget1 = renderTarget;

		if ( this.renderTarget1 == null )
			this.renderTarget1 = new WebGLRenderTarget( 
					Pass.getRenderer().getCanvas().getWidth(), 
					Pass.getRenderer().getCanvas().getHeight(), 
					EffectComposer.defaultRenderTargetOptions 
			);

		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		EffectComposer.quad.getScale().set( Pass.getRenderer().getCanvas().getWidth(), Pass.getRenderer().getCanvas().getHeight(), 1 );

		EffectComposer.camera.setLeft(Pass.getRenderer().getCanvas().getWidth() / -2f);
		EffectComposer.camera.setRight(Pass.getRenderer().getCanvas().getWidth() / 2f);
		EffectComposer.camera.setTop(Pass.getRenderer().getCanvas().getHeight() / 2f);
		EffectComposer.camera.setBottom(Pass.getRenderer().getCanvas().getHeight() / -2f);

		EffectComposer.camera.updateProjectionMatrix();

	}
}
