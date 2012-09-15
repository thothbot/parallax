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

package thothbot.parallax.plugin.postprocessing.client;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.geometries.PlaneGeometry;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugin.postprocessing.client.shaders.ScreenShader;

public class Postprocessing extends Plugin
{
	
	private RenderTargetTexture renderTarget1;
	private RenderTargetTexture renderTarget2;
	
	private List<Pass> passes;
	private ShaderPass copyPass;
	
	private RenderTargetTexture writeBuffer;
	private RenderTargetTexture readBuffer;

	// shared ortho camera
	private OrthographicCamera camera;
	
	// shared fullscreen quad scene
	private PlaneGeometry geometry;
	private Mesh quad;

	public Postprocessing( WebGLRenderer renderer, Scene scene)
	{
		this(renderer, scene, new RenderTargetTexture(
				renderer.getCanvas().getWidth(), 
				renderer.getCanvas().getHeight()));
			
		this.renderTarget1.setMinFilter(TextureMinFilter.LINEAR);
		this.renderTarget1.setMagFilter(TextureMagFilter.LINEAR);
		this.renderTarget1.setFormat(PixelFormat.RGB);
		this.renderTarget1.setStencilBuffer(true);
		
		this.renderTarget2 = this.renderTarget1.clone();
	}
		
	public Postprocessing( WebGLRenderer renderer, Scene scene, RenderTargetTexture renderTarget ) 
	{
		super(renderer, new Scene());

		this.renderTarget1 = renderTarget;
		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		this.passes = new ArrayList<Pass>();

		this.copyPass = new ShaderPass( new ScreenShader() );
		
		Canvas3d canvas = renderer.getCanvas();

		this.camera = new OrthographicCamera( 
			canvas.getWidth() / -2.0, canvas.getWidth() / 2.0, 
			canvas.getHeight() / 2.0, canvas.getHeight() / -2.0, 
			-10000, 10000
		);
		
		this.geometry = new PlaneGeometry( 1, 1 );
		this.quad = new Mesh( geometry, null );
		
		geometry.applyMatrix( new Matrix4().makeRotationX( Math.PI / 2.0) );
		
		quad.getPosition().setZ(-100);
		quad.getScale().set( canvas.getWidth(), canvas.getHeight(), 1 );

		getScene().add( quad );
		getScene().add( camera );
	}
	
	public Plugin.TYPE getType() {
		return Plugin.TYPE.POST_RENDER;
	}
	
	public OrthographicCamera getCamera() {
		return this.camera;
	}
	
	public PlaneGeometry getGeometry() {
		return this.geometry;
	}
	
	public Mesh getQuad() {
		return this.quad;
	}

	public RenderTargetTexture getWriteBuffer() {
		return this.writeBuffer;
	}
	
	public RenderTargetTexture getReadBuffer() {
		return this.readBuffer;
	}
	
	public void addPass( Pass pass ) 
	{
		this.passes.add( pass );
	}

	public void render( Scene scene, Camera camera, int currentWidth, int currentHeight ) 
	{
		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		boolean maskActive = false;
		
		updateSizes();
		// TODO: check
		double delta = 0;
		WebGLRenderingContext gl = getRenderer().getGL();
		
		for ( int i = 0; i < this.passes.size(); i ++ ) 
		{
			Pass pass = this.passes.get( i );

			if ( !pass.isEnabled() ) continue;

			pass.render( this, delta, maskActive );

			if ( pass.isNeedsSwap() ) 
			{
				if ( maskActive ) 
				{
					gl.stencilFunc( GLenum.NOTEQUAL.getValue(), 1, 0xffffffff );

					this.copyPass.render( this, delta, true );

					gl.stencilFunc( GLenum.EQUAL.getValue(), 1, 0xffffffff );
				}

				this.swapBuffers();

			}

			maskActive = pass.isMaskActive();
		}
	}

	public void reset( RenderTargetTexture renderTarget ) 
	{
		this.renderTarget1 = renderTarget;

		if ( this.renderTarget1 == null )
		{
			this.renderTarget1 = new RenderTargetTexture(
					getRenderer().getCanvas().getWidth(), 
					getRenderer().getCanvas().getHeight());
			
			this.renderTarget1.setMinFilter(TextureMinFilter.LINEAR);
			this.renderTarget1.setMagFilter(TextureMagFilter.LINEAR);
			this.renderTarget1.setFormat(PixelFormat.RGB);
			this.renderTarget1.setStencilBuffer(true);
		}

		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		Canvas3d canvas = this.getRenderer().getCanvas();
		this.quad.getScale().set( canvas.getWidth(), canvas.getHeight(), 1 );

		this.camera.setLeft(canvas.getWidth() / -2.0);
		this.camera.setRight(canvas.getWidth() / 2.0);
		this.camera.setTop(canvas.getHeight() / 2.0);
		this.camera.setBottom(canvas.getHeight() / -2.0);

		this.camera.updateProjectionMatrix();
	}
	
	private void swapBuffers() 
	{
		RenderTargetTexture tmp = this.readBuffer;
		this.readBuffer = this.writeBuffer;
		this.writeBuffer = tmp;
	}
	
	private void updateSizes() 
	{
		Canvas3d canvas = getRenderer().getCanvas();

		double oldWidth = this.renderTarget1.getWidth();
		double oldHeight = this.renderTarget1.getHeight();
		
		if(oldWidth == canvas.getWidth() && oldHeight == canvas.getWidth())
			return;
		
		this.camera.setLeft(canvas.getWidth() / -2.0);
		this.camera.setRight(canvas.getWidth() / 2.0);
		this.camera.setTop(canvas.getHeight() / 2.0);
		this.camera.setBottom(canvas.getHeight() / -2.0); 
		this.camera.updateProjectionMatrix();
		
		quad.getScale().set( canvas.getWidth(), canvas.getHeight(), 1 );
		
		this.renderTarget1.setWidth(canvas.getWidth());
		this.renderTarget1.setHeight(canvas.getHeight());
		
		this.renderTarget2.setWidth(canvas.getWidth());
		this.renderTarget2.setHeight(canvas.getHeight());
	}
}
