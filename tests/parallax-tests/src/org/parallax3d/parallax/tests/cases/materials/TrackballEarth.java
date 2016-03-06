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
import org.parallax3d.parallax.controllers.TrackballControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.PointCloudMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.renderers.shaders.NormalMapShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("misc_controls_fly")
public final class TrackballEarth extends ParallaxTest 
{

	static final String earthAtmos    = "textures/planets/earth_atmos_2048.jpg";
	static final String earthClouds   = "textures/planets/earth_clouds_1024.png";
	static final String earthNormal   = "textures/planets/earth_normal_2048.jpg";
	static final String earthSpecular = "textures/planets/earth_specular_2048.jpg";
	static final String moon          = "textures/planets/moon_1024.jpg";
	
	static final int radius = 6371;
	static final double tilt = 0.41;
	static final double rotationSpeed = 0.1;

	static final double cloudsScale = 1.005;
	static final double moonScale = 0.23;

	Scene scene;
	PerspectiveCamera camera;
	
	Mesh meshPlanet;
	Mesh meshClouds;
	Mesh meshMoon;
	
	private TrackballControls control;
	private double oldTime;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				25, // fov
				context.getAspectRation(), // aspect
				50, // near
				1e7f // far 
		); 
		
		camera.getPosition().setZ(radius * 5);
		
		this.control = new TrackballControls( camera, context );
		this.control.setPanSpeed(0.2);
		this.control.setDynamicDampingFactor(0.3);
		this.control.setMinDistance(radius * 1.1);
		this.control.setMaxDistance(radius * 100);

		DirectionalLight dirLight = new DirectionalLight( 0xFFFFFF );
		dirLight.getPosition().set( -1, 0, 1 ).normalize();
		scene.add( dirLight );
		
		AmbientLight ambientLight = new AmbientLight( 0x000000 );
		scene.add( ambientLight );

		Texture planetTexture   = new Texture( earthAtmos );
		Texture cloudsTexture   = new Texture( earthClouds );
		Texture normalTexture   = new Texture( earthNormal );
		Texture specularTexture = new Texture( earthSpecular );
		Texture moonTexture     = new Texture( moon );

		ShaderMaterial materialNormalMap = new ShaderMaterial( new NormalMapShader() )
			.setLights( true );
		
		FastMap<Uniform> uniforms = materialNormalMap.getShader().getUniforms();

		uniforms.get("tNormal").setValue( normalTexture );
		((Vector2)uniforms.get("uNormalScale").getValue()).set( 0.85, 0.85 );

		uniforms.get("tDiffuse").setValue( planetTexture );
		uniforms.get("tSpecular").setValue( specularTexture );

		uniforms.get("enableAO").setValue( false );
		uniforms.get("enableDiffuse").setValue( true );
		uniforms.get("enableSpecular").setValue( true );

		((Color)uniforms.get("diffuse").getValue()).setHex( 0xffffff );
		((Color)uniforms.get("specular").getValue()).setHex( 0x666666 );
		((Color)uniforms.get("ambient").getValue()).setHex( 0x000000 );

		uniforms.get("shininess").setValue( 15.0 );

		// planet

		SphereGeometry geometry = new SphereGeometry( radius, 100, 50 );
		geometry.computeTangents();

		this.meshPlanet = new Mesh( geometry, materialNormalMap );
		meshPlanet.getRotation().setY( 0 );
		meshPlanet.getRotation().setZ( tilt );
		scene.add( meshPlanet );

		// clouds

		MeshLambertMaterial materialClouds = new MeshLambertMaterial()
				.setColor( 0xffffff )
				.setMap( cloudsTexture )
				.setTransparent(true);

		this.meshClouds = new Mesh( geometry, materialClouds );
		meshClouds.getScale().set( cloudsScale );
		meshClouds.getRotation().setZ( tilt );
		scene.add( meshClouds );


		// moon
		MeshPhongMaterial materialMoon = new MeshPhongMaterial()
				.setColor( 0xffffff )
				.setMap( moonTexture );
		

		this.meshMoon = new Mesh( geometry, materialMoon );
		meshMoon.getPosition().set( radius * 5.0, 0, 0 );
		meshMoon.getScale().set( moonScale );
		scene.add( meshMoon );


		// stars

		Geometry starsGeometry = new Geometry();

		for ( int i = 0; i < 1500; i ++ ) 
		{

			Vector3 vertex = new Vector3();
			vertex.setX( Math.random() * 2.0 - 1.0 );
			vertex.setY( Math.random() * 2.0 - 1.0 );
			vertex.setZ( Math.random() * 2.0 - 1.0 );
			vertex.multiply( radius );

			starsGeometry.getVertices().add( vertex );

		}

		PointCloudMaterial pbOpt = new PointCloudMaterial();
		pbOpt.setColor( new Color(0x555555) );
		pbOpt.setSize( 2 );
		pbOpt.setSizeAttenuation(false);
		
		List<PointCloudMaterial> starsMaterials = new ArrayList<PointCloudMaterial>();
		starsMaterials.add(pbOpt);

		starsMaterials.add(new PointCloudMaterial()
				.setColor( 0x555555 )
				.setSize( 1 )
				.setSizeAttenuation(false));

		starsMaterials.add(new PointCloudMaterial()
				.setColor( 0x333333 )
				.setSize( 2 )
				.setSizeAttenuation(false));

		starsMaterials.add(new PointCloudMaterial()
				.setColor( 0x3a3a3a )
				.setSize( 1 )
				.setSizeAttenuation(false));

		starsMaterials.add(new PointCloudMaterial()
				.setColor( 0x1a1a1a )
				.setSize( 2 )
				.setSizeAttenuation(false));

		starsMaterials.add( new PointCloudMaterial()
				.setColor( 0x1a1a1a )
				.setSize( 1 )
				.setSizeAttenuation(false));

		for ( int i = 10; i < 30; i ++ ) 
		{
			PointCloud stars = new PointCloud( starsGeometry, starsMaterials.get( i % 6 ) );

			stars.getRotation().setX( Math.random() * 6.0 );
			stars.getRotation().setY( Math.random() * 6.0 );
			stars.getRotation().setZ( Math.random() * 6.0 );

			double s = i * 10.0;
			stars.getScale().set( s );

			stars.setMatrixAutoUpdate(false);
			stars.updateMatrix();

			scene.add( stars );
		}
		
		context.getRenderer().setSortObjects(false);
		context.getRenderer().setAutoClear(false);
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
		
		this.oldTime = Duration.currentTimeMillis();
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double delta = (Duration.currentTimeMillis() - this.oldTime) * 0.001;

		meshPlanet.getRotation().addY( rotationSpeed * delta );
		meshClouds.getRotation().addY( 1.25 * rotationSpeed * delta );

		double angle = delta * rotationSpeed;

		meshMoon.setPosition( new Vector3(
			Math.cos( angle ) * meshMoon.getPosition().getX() - Math.sin( angle ) * meshMoon.getPosition().getZ(),
			0,
			Math.sin( angle ) * meshMoon.getPosition().getX() + Math.cos( angle ) * meshMoon.getPosition().getZ()
		));
		meshMoon.getRotation().addY( - angle );

		this.control.update();

		context.getRenderer().clear();
		
		this.oldTime = Duration.currentTimeMillis();
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Earth (trackball camera)";
	}

	@Override
	public String getDescription() {
		return "MOVE: mouse and press, LEFT/A: rotate, MIDDLE/S: zoom, RIGHT/D: pan.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
