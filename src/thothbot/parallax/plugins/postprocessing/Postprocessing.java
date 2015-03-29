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

package thothbot.parallax.plugins.postprocessing;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.events.ViewportResizeEvent;
import thothbot.parallax.core.client.events.ViewportResizeHandler;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.StencilFunction;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.geometries.PlaneGeometry;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugins.postprocessing.shaders.CopyShader;

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
	private Mesh quad;

	public Postprocessing( WebGLRenderer renderer, Scene scene)
	{
		this(renderer, scene, new RenderTargetTexture( renderer.getAbsoluteWidth(), renderer.getAbsoluteHeight() ));

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

		this.copyPass = new ShaderPass( new CopyShader() );
		
		this.camera = new OrthographicCamera( 2, 2, 0, 1 );
		this.camera.addViewportResizeHandler(new ViewportResizeHandler() {
			
			@Override
			public void onResize(ViewportResizeEvent event) {
				camera.setSize(2, 2);
			}
		});

		this.quad = new Mesh( new PlaneGeometry( 2, 2 ), null );
		
		getScene().add( quad );
		getScene().add( camera );
	}
	
	@Override
	public boolean isMulty() {
		return true;
	}
	
	public Plugin.TYPE getType() {
		return Plugin.TYPE.POST_RENDER;
	}
	
	public RenderTargetTexture getRenderTarget1() {
		return renderTarget1;
	}

	public RenderTargetTexture getRenderTarget2() {
		return renderTarget2;
	}
	
	public OrthographicCamera getCamera() {
		return this.camera;
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

	@Override
	public void render( Camera camera, List<Light> lights, int currentWidth, int currentHeight ) 
	{
		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;

		boolean maskActive = false;

		double delta = 0;
		WebGLRenderingContext gl = getRenderer().getGL();

		for ( Pass pass : this.passes ) 
		{	
			if ( !pass.isEnabled() ) continue;
			
			Log.info(" ----> Postprocessing.render(): pass " + pass.getClass().getSimpleName() 
					+ (pass.getClass().equals(ShaderPass.class) ? 
							"(" + ((ShaderPass)pass).getMaterial().getShader().getClass().getSimpleName() + ")" : "") );

			pass.render( this, delta, maskActive );

			if ( pass.isNeedsSwap() ) 
			{
				if ( maskActive ) 
				{
					gl.stencilFunc( StencilFunction.NOTEQUAL, 1, 0xffffffff );

					this.copyPass.render( this, delta, true );

					gl.stencilFunc( StencilFunction.EQUAL, 1, 0xffffffff );
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
			this.renderTarget1 = new RenderTargetTexture(getRenderer().getAbsoluteWidth(), getRenderer().getAbsoluteHeight());
			
			this.renderTarget1.setMinFilter(TextureMinFilter.LINEAR);
			this.renderTarget1.setMagFilter(TextureMagFilter.LINEAR);
			this.renderTarget1.setFormat(PixelFormat.RGB);
			this.renderTarget1.setStencilBuffer(true);
		}

		this.renderTarget2 = this.renderTarget1.clone();

		this.writeBuffer = this.renderTarget1;
		this.readBuffer = this.renderTarget2;
	}
	
	private void swapBuffers() 
	{
		RenderTargetTexture tmp = this.readBuffer;
		this.readBuffer = this.writeBuffer;
		this.writeBuffer = tmp;
	}
}
