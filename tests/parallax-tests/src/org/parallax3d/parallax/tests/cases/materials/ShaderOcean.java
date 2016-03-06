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

package org.parallax3d.parallax.tests.cases.materials;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.controllers.FirstPersonControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.IcosahedronGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.CubeShader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;
import org.parallax3d.parallax.tests.resources.Water;

@ThreejsExample("webgl_shaders_ocean")
public final class ShaderOcean extends ParallaxTest {

	static final String waternormals = "textures/waternormals.jpg";
	static final String textures = "textures/cube/skybox/*.jpg";
	
	public int width = 2000;
	public int height = 2000;
	public int widthSegments = 250;
	public int heightSegments = 250;
	public int depth = 1500;
	public int param = 4;
	public int filterparam = 1;

	Scene scene;
	PerspectiveCamera camera;
	
	FirstPersonControls controls;
	
	Water water;
	Mesh sphere;
	
	private double oldTime;
	
	@Override
	public void onResize(RenderingContext context) 
	{		
	}
	
	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		
		camera = new PerspectiveCamera(
				55, // fov
				context.getAspectRation(), // aspect
				0.5, // near
				3000000 // far 
		); 
		
		camera.getPosition().set( 2000, 750, 2000 );
		
		this.controls = new FirstPersonControls( camera, context );
		controls.setMovementSpeed(500);
		controls.setLookSpeed(0.1);
		
		HemisphereLight light = new HemisphereLight( 0xffffbb, 0x080820, 1 );
		light.getPosition().set( - 1, 1, - 1 );
		scene.add( light );
		
		water = new Water( context.getRenderer(), camera, scene);
		water.width = 512;
		water.height = 512;
		water.alpha = 1.0;
		water.sunDirection = light.getPosition().clone().normalize();
		water.sunColor = new Color(0xffffff);
		water.waterColor = new Color(0x001e0f);

		Texture waterNormals = new Texture( waternormals, new Texture.ImageLoadHandler() {

			@Override
			public void onImageLoad(Texture texture) {
				water.normalSampler = texture;
				water.updateUniforms();
			}
		});
		
		waterNormals.setWrapS(TextureWrapMode.REPEAT);
		waterNormals.setWrapT(TextureWrapMode.REPEAT);

		Mesh mirrorMesh = new Mesh(
				new PlaneBufferGeometry( this.width * 500, this.height * 500 ),
				water.material
		);
					
		mirrorMesh.add( water );
		mirrorMesh.getRotation().setX( - Math.PI * 0.5 );
		scene.add( mirrorMesh );
		
		CubeTexture textureCube = new CubeTexture( textures );
		
		MeshBasicMaterial material = new MeshBasicMaterial()
				.setColor( 0xffffff )
				.setEnvMap( textureCube );
		
		// Skybox

		ShaderMaterial sMaterial = new ShaderMaterial( new CubeShader() )
				.setDepthWrite( false )
				.setSide(Material.SIDE.BACK);
		sMaterial.getShader().getUniforms().get("tCube").setValue( textureCube ); 

		Mesh mesh = new Mesh( new BoxGeometry( 1000000, 1000000, 1000000 ), sMaterial );
		scene.add( mesh );

		// Sphere
		IcosahedronGeometry geometry = new IcosahedronGeometry( 400, 4 );

		for ( int i = 0, j = geometry.getFaces().size(); i < j; i ++ ) {

			geometry.getFaces().get(i).getColor().setHex( Mathematics.randInt(0x111111, 0xffffff) );

		}

		MeshPhongMaterial sphereMaterial = new MeshPhongMaterial()
				.setVertexColors(Material.COLORS.FACE)
				.setEnvMap(textureCube)
				.setShininess(100.0);
		
		sphere = new Mesh( geometry, sphereMaterial );
		scene.add( sphere );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = Duration.currentTimeMillis() * 0.001;

		sphere.getPosition().setY( Math.sin( time ) * 500 + 250 );
		sphere.getRotation().setY( time * 0.5 );
		sphere.getRotation().setZ( time * 0.51 );

		water.material.getShader().getUniforms().get("time").setValue(  (Double)water.material.getShader().getUniforms().get("time").getValue() + 1.0 / 60.0 );
		water.render();
		controls.update( context.getDeltaTime() );
		this.oldTime = Duration.currentTimeMillis();
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Ocean shader";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
