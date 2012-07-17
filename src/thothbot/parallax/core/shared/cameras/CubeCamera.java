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

package thothbot.parallax.core.shared.cameras;

import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetCubeTexture;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.objects.Object3D;
import thothbot.parallax.core.shared.scenes.Scene;

/**
 * Camera for rendering cube maps
 * - renders scene into axis-aligned cube
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
public final class CubeCamera extends Object3D 
{
	protected float fieldOfView = 90.0f;
	protected float aspectRatio = 1.0f;
	
	protected float near;
	protected float far;
	
	PerspectiveCamera cameraPX;
	PerspectiveCamera cameraNX;
	
	PerspectiveCamera cameraPY;
	PerspectiveCamera cameraNY;
	
	PerspectiveCamera cameraPZ;
	PerspectiveCamera cameraNZ;
	
	RenderTargetCubeTexture renderTarget;
	
	public CubeCamera(float near, float far, int cubeResolution)
	{
		this.cameraPX = new PerspectiveCamera( fieldOfView, aspectRatio, near, far );
		cameraPX.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraPX.lookAt( new Vector3f( 1.0f, 0.0f, 0.0f ) );
		this.addChild( cameraPX );

		this.cameraNX = new PerspectiveCamera( fieldOfView, aspectRatio, near, far );
		cameraNX.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraNX.lookAt( new Vector3f( -1.0f, 0.0f, 0.0f ) );
		this.addChild( cameraNX );

		this.cameraPY = new PerspectiveCamera( fieldOfView, aspectRatio, near, far );
		cameraPY.getUp().set( 0.0f, 0.0f, 1.0f );
		cameraPY.lookAt( new Vector3f( 0.0f, 1.0f, 0.0f ) );
		this.addChild( cameraPY );

		this.cameraNY = new PerspectiveCamera( fieldOfView, aspectRatio, near, far );
		cameraNY.getUp().set( 0.0f, 0.0f, -1.0f );
		cameraNY.lookAt( new Vector3f( 0.0f, -1.0f, 0.0f ) );
		this.addChild( cameraNY );

		this.cameraPZ = new PerspectiveCamera( fieldOfView, aspectRatio, near, far );
		cameraPZ.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraPZ.lookAt( new Vector3f( 0.0f, 0.0f, 1.0f ) );
		this.addChild( cameraPZ );

		this.cameraNZ = new PerspectiveCamera( fieldOfView, aspectRatio, near, far );
		cameraNZ.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraNZ.lookAt( new Vector3f( 0.0f, 0.0f, -1.0f ) );
		this.addChild( cameraNZ );

		this.renderTarget = new RenderTargetCubeTexture( cubeResolution, cubeResolution );
		this.renderTarget.setFormat(PixelFormat.RGB);
		this.renderTarget.setMagFilter(TextureMagFilter.LINEAR);
		this.renderTarget.setMinFilter(TextureMinFilter.LINEAR);
	}
	
	public RenderTargetTexture getRenderTarget()
	{
		return this.renderTarget;
	}
	
	public void updateCubeMap( WebGLRenderer renderer, Scene scene ) 
	{
		RenderTargetTexture renderTarget = this.renderTarget;
		boolean generateMipmaps = renderTarget.isGenerateMipmaps();

		renderTarget.setGenerateMipmaps( false );

		renderTarget.activeCubeFace = 0;
		renderer.render( scene, cameraPX, renderTarget );

		renderTarget.activeCubeFace = 1;
		renderer.render( scene, cameraNX, renderTarget );

		renderTarget.activeCubeFace = 2;
		renderer.render( scene, cameraPY, renderTarget );

		renderTarget.activeCubeFace = 3;
		renderer.render( scene, cameraNY, renderTarget );

		renderTarget.activeCubeFace = 4;
		renderer.render( scene, cameraPZ, renderTarget );

		renderTarget.setGenerateMipmaps( generateMipmaps );

		renderTarget.activeCubeFace = 5;
		renderer.render( scene, cameraNZ, renderTarget );
	}
}
