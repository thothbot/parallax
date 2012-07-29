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

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.geometries.Plane;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;

import thothbot.parallax.postprocessing.client.shader.ShaderScreen;

public class EffectComposer
{
	private WebGLRenderer renderer;
	
	private RenderTargetTexture renderTarget1;
	private RenderTargetTexture renderTarget2;
	
	private RenderTargetTexture writeBuffer;
	private RenderTargetTexture readBuffer;
	
	private List<Pass> passes;
	private ShaderPass copyPass;
	
	// shared ortho camera

	public static OrthographicCamera camera;
	
	// shared fullscreen quad scene

	public static Plane geometry;
	public static Mesh quad;
	public static Scene scene;

	public EffectComposer( WebGLRenderer renderer )
	{
		this(renderer, new RenderTargetTexture(
				renderer.getCanvas().getWidth(), 
				renderer.getCanvas().getHeight()));
		
		this.renderTarget1.setMinFilter(TextureMinFilter.LINEAR);
		this.renderTarget1.setMagFilter(TextureMagFilter.LINEAR);
		this.renderTarget1.setFormat(PixelFormat.RGB);
		this.renderTarget1.setStencilBuffer(true);
		
		this.renderTarget2 = this.renderTarget1.clone();
	}
	
	public EffectComposer( WebGLRenderer renderer, RenderTargetTexture renderTarget ) 
	{
		this.renderer = renderer;
		
		this.renderTarget1 = renderTarget;
		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		this.passes = new ArrayList<Pass>();

		this.copyPass = new ShaderPass( new ShaderScreen() );
		
		Canvas3d canvas = renderer.getCanvas();
		this.camera = new OrthographicCamera( 
			canvas.getWidth() / -2f, canvas.getWidth() / 2f, 
			canvas.getHeight() / 2f, canvas.getHeight() / -2f, 
			-10000f, 10000f
		);
		
		this.geometry = new Plane( 1, 1 );
		this.quad = new Mesh( geometry, null );
		this.scene = new Scene();
		
		geometry.applyMatrix( new Matrix4f().makeRotationX( (float) (Math.PI / 2.0) ) );
		
		quad.getPosition().setZ(-100);
		quad.getScale().set( canvas.getWidth(), canvas.getHeight(), 1 );

		scene.addChild( quad );
		scene.addChild( camera );
	}
	
	private void swapBuffers() 
	{
		RenderTargetTexture tmp = this.readBuffer;
		this.readBuffer = this.writeBuffer;
		this.writeBuffer = tmp;
	}
	
	private void addPass( Pass pass ) 
	{
		this.passes.add( pass );
	}
	
	private void render( WebGLRenderer renderer, float delta ) 
	{
		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		boolean maskActive = false;

		for ( int i = 0; i < this.passes.size(); i ++ ) 
		{
			Pass pass = this.passes.get( i );

			if ( !pass.isEnabled() ) continue;

			pass.render( renderer, this.writeBuffer, this.readBuffer, delta, maskActive );

			if ( pass.isNeedsSwap() ) 
			{
				if ( maskActive ) 
				{
					renderer.getGL().stencilFunc( GLenum.NOTEQUAL.getValue(), 1, 0xffffffff );

					this.copyPass.render( renderer, this.writeBuffer, this.readBuffer, delta, true );

					renderer.getGL().stencilFunc( GLenum.EQUAL.getValue(), 1, 0xffffffff );
				}

				this.swapBuffers();

			}

			maskActive = pass.isMaskActive();
		}
	}

	private void reset( RenderTargetTexture renderTarget ) 
	{
		this.renderTarget1 = renderTarget;

		if ( this.renderTarget1 == null )
		{
			this.renderTarget1 = new RenderTargetTexture(
					renderer.getCanvas().getWidth(), 
					renderer.getCanvas().getHeight());
			
			this.renderTarget1.setMinFilter(TextureMinFilter.LINEAR);
			this.renderTarget1.setMagFilter(TextureMagFilter.LINEAR);
			this.renderTarget1.setFormat(PixelFormat.RGB);
			this.renderTarget1.setStencilBuffer(true);
		}

		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		Canvas3d canvas = this.renderer.getCanvas();
		this.quad.getScale().set( canvas.getWidth(), canvas.getHeight(), 1 );

		this.camera.setLeft(canvas.getWidth() / -2f);
		this.camera.setRight(canvas.getWidth() / 2f);
		this.camera.setTop(canvas.getHeight() / 2f);
		this.camera.setBottom(canvas.getHeight() / -2f);

		this.camera.updateProjectionMatrix();
	}
}
