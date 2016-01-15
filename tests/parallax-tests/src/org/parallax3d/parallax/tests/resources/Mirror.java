/*
 * Copyright 2014 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.tests.resources;

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.UniformsUtils;
import org.parallax3d.parallax.graphics.extras.helpers.ArrowHelper;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.*;

import java.util.Map;

public class Mirror extends Object3D {

	/**
	 * options
	 */
	// Texture width
	public int width = 512;
	// Texture height
	public int height = 512;
	public double clipBias = 0.0;
	public Color mirrorColor = new Color(0x7F7F7F);
	public boolean debugMode = false;
	
	
	protected GLRenderer renderer;
	protected PerspectiveCamera camera;
	protected PerspectiveCamera mirrorCamera;
	protected Plane mirrorPlane = new Plane();
	protected Vector3 normal = new Vector3( 0, 0, 1 );
	protected Vector3 mirrorWorldPosition = new Vector3();
	protected Vector3 cameraWorldPosition = new Vector3();
	protected Matrix4 rotationMatrix = new Matrix4();
	protected Vector3 lookAtPosition = new Vector3(0, 0, -1);
	protected Vector4 clipPlane = new Vector4();
	
	protected Matrix4 textureMatrix = new Matrix4();
	
	protected RenderTargetTexture texture;
	protected RenderTargetTexture tempTexture;
	
	public ShaderMaterial material;
	
	protected boolean matrixNeedsUpdate;
	
	public Mirror () {
		this.setName("mirror_ " + this.getId());
	}

	public Mirror(GLRenderer renderer, Camera camera) {
				
		this();
		
		this.renderer = renderer;
		
		if ( debugMode ) {

			ArrowHelper arrow = new ArrowHelper(new Vector3( 0, 0, 1 ), new Vector3( 0, 0, 0 ), 10, 0xffff80 );
			Geometry planeGeometry = new Geometry();
			planeGeometry.getVertices().add( new Vector3( -10, -10, 0 ) );
			planeGeometry.getVertices().add( new Vector3(  10, -10, 0 ) );
			planeGeometry.getVertices().add( new Vector3(  10,  10, 0 ) );
			planeGeometry.getVertices().add( new Vector3( -10,  10, 0 ) );
			planeGeometry.getVertices().add( planeGeometry.getVertices().get(0) );
			LineBasicMaterial lineBasicMaterial = new LineBasicMaterial();
			lineBasicMaterial.setColor(new Color(0xffff80));
			Line plane = new Line( planeGeometry, lineBasicMaterial );

			this.add(arrow);
			this.add(plane);

		}

		if ( camera instanceof PerspectiveCamera ) {

			this.camera = (PerspectiveCamera) camera;

		} else {

			this.camera = new PerspectiveCamera();
//			Log.warn(" -> Mirror() ID " + this.name + ": camera is not a Perspective Camera!");

		}

		this.mirrorCamera = this.camera.clone();

		this.texture = new RenderTargetTexture( width, height );
		this.tempTexture = new RenderTargetTexture( width, height );

		MirrorShader mirrorShader = new MirrorShader();
		Map<String, Uniform> mirrorUniforms = UniformsUtils.clone(mirrorShader.getUniforms());

		this.material = new ShaderMaterial(mirrorShader);
		material.getShader().getUniforms().get("mirrorSampler").setValue(  this.texture ); 
		material.getShader().getUniforms().get("mirrorColor").setValue(  mirrorColor );
		material.getShader().getUniforms().get("textureMatrix").setValue(  this.textureMatrix );

		if ( !Mathematics.isPowerOfTwo(width) || !Mathematics.isPowerOfTwo( height ) ) {

			this.texture.setGenerateMipmaps(false);
			this.tempTexture.setGenerateMipmaps(false);
			
		}

		this.updateTextureMatrix();
		this.render();
		
	}
	
	protected void updateTextureMatrix() {

		this.updateMatrixWorld(false);
		this.camera.updateMatrixWorld(false);

		this.mirrorWorldPosition.setFromMatrixPosition( this.matrixWorld );
		this.cameraWorldPosition.setFromMatrixPosition( this.camera.getMatrixWorld() );

		this.rotationMatrix.extractRotation( this.matrixWorld );

		this.normal.set( 0, 0, 1 );
		this.normal.apply( this.rotationMatrix );

		Vector3 view = this.mirrorWorldPosition.clone().sub( this.cameraWorldPosition );
		view.reflect( this.normal ).negate();
		view.add( this.mirrorWorldPosition );

		this.rotationMatrix.extractRotation( this.camera.getMatrixWorld() );

		this.lookAtPosition.set(0, 0, -1);
		this.lookAtPosition.apply( this.rotationMatrix );
		this.lookAtPosition.add( this.cameraWorldPosition );

		Vector3 target = this.mirrorWorldPosition.clone().sub( this.lookAtPosition );
		target.reflect( this.normal ).negate();
		target.add( this.mirrorWorldPosition );

		this.up.set( 0, -1, 0 );
		this.up.apply( this.rotationMatrix );
		this.up.reflect( this.normal ).negate();

		this.mirrorCamera.getPosition().copy( view );
		this.mirrorCamera.setUp( this.up );
		this.mirrorCamera.lookAt( target );

		this.mirrorCamera.updateProjectionMatrix();
		this.mirrorCamera.updateMatrixWorld(false);
		this.mirrorCamera.getMatrixWorldInverse().getInverse( this.mirrorCamera.getMatrixWorld() );

		// Update the texture matrix
		this.textureMatrix.set( 0.5, 0.0, 0.0, 0.5,
								0.0, 0.5, 0.0, 0.5,
								0.0, 0.0, 0.5, 0.5,
								0.0, 0.0, 0.0, 1.0 );
		this.textureMatrix.multiply( this.mirrorCamera.getProjectionMatrix() );
		this.textureMatrix.multiply( this.mirrorCamera.getMatrixWorldInverse() );

		// Now update projection matrix with new clip plane, implementing code from: http://www.terathon.com/code/oblique.html
		// Paper explaining this technique: http://www.terathon.com/lengyel/Lengyel-Oblique.pdf
		this.mirrorPlane.setFromNormalAndCoplanarPoint( this.normal, this.mirrorWorldPosition );
		this.mirrorPlane.apply( this.mirrorCamera.getMatrixWorldInverse() );

		this.clipPlane.set( this.mirrorPlane.getNormal().getX(), this.mirrorPlane.getNormal().getY(), this.mirrorPlane.getNormal().getZ(), this.mirrorPlane.getConstant() );

		Vector4 q = new Vector4();
		Matrix4 projectionMatrix = this.mirrorCamera.getProjectionMatrix();

		q.setX( ( Mathematics.sign(this.clipPlane.getX()) + projectionMatrix.getArray().get(8) ) / projectionMatrix.getArray().get(0) );
		q.setY( ( Mathematics.sign(this.clipPlane.getY()) + projectionMatrix.getArray().get(9) ) / projectionMatrix.getArray().get(5) );
		q.setZ( - 1.0 );
		q.setW( ( 1.0 + projectionMatrix.getArray().get(10) ) / projectionMatrix.getArray().get(14) );

		// Calculate the scaled plane vector
		Vector4 c = new Vector4();
		c = this.clipPlane.multiply( 2.0 / this.clipPlane.dot(q) );

		// Replacing the third row of the projection matrix
		projectionMatrix.getArray().set(2, c.getX());
		projectionMatrix.getArray().set(6, c.getY());
		projectionMatrix.getArray().set(10, c.getZ() + 1.0 - this.clipBias);
		projectionMatrix.getArray().set(14, c.getW());
	}
	
	public void render() {

		if ( this.matrixNeedsUpdate ) 
			this.updateTextureMatrix();

		this.matrixNeedsUpdate = true;

		// Render the mirrored view of the current scene into the target texture
		Object3D scene = this;

		while ( scene.getParent() != null ) {

			scene = (Object3D) scene.getParent();

		}

		if ( scene != null && scene instanceof Scene) {

			this.renderer.render( (Scene)scene, this.mirrorCamera, this.texture, true );

		}

	}
}
