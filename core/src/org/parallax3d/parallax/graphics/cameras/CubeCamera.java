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

package org.parallax3d.parallax.graphics.cameras;

import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.renderers.WebGLRenderer;
import org.parallax3d.parallax.graphics.renderers.RenderTargetCubeTexture;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;

/**
 * Camera for rendering cube maps
 * - renders scene into axis-aligned cube
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.CubeCamera")
public final class CubeCamera extends Object3D
{
	private float fov = 90.0f;
	private float aspect = 1.0f;

	private PerspectiveCamera cameraPX;
	private PerspectiveCamera cameraNX;

	private PerspectiveCamera cameraPY;
	private PerspectiveCamera cameraNY;

	private PerspectiveCamera cameraPZ;
	private PerspectiveCamera cameraNZ;

	private RenderTargetCubeTexture renderTarget;

	/**
	 * Constructs a CubeCamera that contains 6 {@link PerspectiveCamera}s that then render to a {@link RenderTargetCubeTexture}
	 * @param near The near clipping distance. 
	 * @param far The far clipping distance 
	 * @param cubeResolution  Sets the width of the cube.
	 */
	public CubeCamera(float near, float far, int cubeResolution)
	{
		this.cameraPX = new PerspectiveCamera( fov, aspect, near, far );
		cameraPX.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraPX.lookAt( new Vector3( 1.0f, 0.0f, 0.0f ) );
		this.add( cameraPX );

		this.cameraNX = new PerspectiveCamera( fov, aspect, near, far );
		cameraNX.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraNX.lookAt( new Vector3( -1.0f, 0.0f, 0.0f ) );
		this.add( cameraNX );

		this.cameraPY = new PerspectiveCamera( fov, aspect, near, far );
		cameraPY.getUp().set( 0.0f, 0.0f, 1.0f );
		cameraPY.lookAt( new Vector3( 0.0f, 1.0f, 0.0f ) );
		this.add( cameraPY );

		this.cameraNY = new PerspectiveCamera( fov, aspect, near, far );
		cameraNY.getUp().set( 0.0f, 0.0f, -1.0f );
		cameraNY.lookAt( new Vector3( 0.0f, -1.0f, 0.0f ) );
		this.add( cameraNY );

		this.cameraPZ = new PerspectiveCamera( fov, aspect, near, far );
		cameraPZ.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraPZ.lookAt( new Vector3( 0.0f, 0.0f, 1.0f ) );
		this.add( cameraPZ );

		this.cameraNZ = new PerspectiveCamera( fov, aspect, near, far );
		cameraNZ.getUp().set( 0.0f, -1.0f, 0.0f );
		cameraNZ.lookAt( new Vector3( 0.0f, 0.0f, -1.0f ) );
		this.add( cameraNZ );

		this.renderTarget = new RenderTargetCubeTexture( cubeResolution, cubeResolution );
		this.renderTarget.setFormat(PixelFormat.RGB);
		this.renderTarget.setMagFilter(TextureMagFilter.LINEAR);
		this.renderTarget.setMinFilter(TextureMinFilter.LINEAR);
	}
	
	/**
	 * The cube texture that gets generated.
	 * @return
	 */
	public RenderTargetCubeTexture getRenderTarget()
	{
		return this.renderTarget;
	}
	
	/**
	 * Call this to update the renderTarget.
	 * @param renderer The current WebGL renderer 
	 * @param scene The current scene
	 */
	public void updateCubeMap( WebGLRenderer renderer, Scene scene )
	{
		RenderTargetCubeTexture renderTarget = this.renderTarget;
		boolean generateMipmaps = renderTarget.isGenerateMipmaps();

		renderTarget.setGenerateMipmaps( false );

		renderTarget.setActiveCubeFace(0);
		renderer.render( scene, cameraPX, renderTarget );

		renderTarget.setActiveCubeFace(1);
		renderer.render( scene, cameraNX, renderTarget );

		renderTarget.setActiveCubeFace(2);
		renderer.render( scene, cameraPY, renderTarget );

		renderTarget.setActiveCubeFace(3);
		renderer.render( scene, cameraNY, renderTarget );

		renderTarget.setActiveCubeFace(4);
		renderer.render( scene, cameraPZ, renderTarget );

		renderTarget.setGenerateMipmaps( generateMipmaps );

		renderTarget.setActiveCubeFace(5);
		renderer.render( scene, cameraNZ, renderTarget );
	}
}