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
import org.parallax3d.parallax.controllers.FlyControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.PointsMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.Points;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.tests.NeedImprovement;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.Arrays;
import java.util.List;

@NeedImprovement("Postprocessing, FlyControls")
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

	static final double cloudsScale = 1.005;
	static final double moonScale = 0.23;

	Scene scene;
	PerspectiveCamera camera;
	
	Mesh meshPlanet;
	Mesh meshClouds;
	Mesh meshMoon;
	
	FlyControls controls;
	double oldTime;

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

		scene.setFog( new FogExp2( 0x000000, 0.00000025 ) );

		controls = new FlyControls( camera, context );
		controls.setMovementSpeed(1000);
		controls.setRollSpeed(Math.PI / 24);
//		controls.autoForward = false;
//		controls.dragToLook = false;

		DirectionalLight dirLight = new DirectionalLight( 0xFFFFFF );
		dirLight.getPosition().set( -1, 0, 1 ).normalize();
		scene.add( dirLight );

		Texture cloudsTexture   = new Texture( earthClouds );
		Texture moonTexture     = new Texture( moon );

		MeshPhongMaterial materialNormalMap = new MeshPhongMaterial()
				.setSpecular(0x333333)
				.setShininess(15)
				.setMap(new Texture( earthAtmos ))
				.setSpecularMap(new Texture( earthSpecular ))
				.setNormalMap(new Texture( earthNormal ))
				.setNormalScale(new Vector2( 0.85, 0.85 ));

		// planet

		SphereGeometry geometry = new SphereGeometry( radius, 100, 50 );

		this.meshPlanet = new Mesh( geometry, materialNormalMap );
		meshPlanet.getRotation().setY( 0 );
		meshPlanet.getRotation().setZ( tilt );
		scene.add( meshPlanet );

		// clouds

		MeshLambertMaterial materialClouds = new MeshLambertMaterial()
				.setMap( cloudsTexture )
				.setTransparent(true);

		this.meshClouds = new Mesh( geometry, materialClouds );
		meshClouds.getScale().set( cloudsScale );
		meshClouds.getRotation().setZ( tilt );
		scene.add( meshClouds );


		// moon
		MeshPhongMaterial materialMoon = new MeshPhongMaterial()
				.setMap( moonTexture );

		this.meshMoon = new Mesh( geometry, materialMoon );
		meshMoon.getPosition().set( radius * 5.0, 0, 0 );
		meshMoon.getScale().set( moonScale );
		scene.add( meshMoon );


		// stars

		Geometry starsGeometry1 = new Geometry(),
				starsGeometry2 = new Geometry();

		for ( int i = 0; i < 250; i ++ ) {

			Vector3 vertex = new Vector3();
			vertex.setX(Math.random() * 2 - 1);
			vertex.setY(Math.random() * 2 - 1);
			vertex.setZ(Math.random() * 2 - 1);
			vertex.multiplyScalar( radius );

			starsGeometry1.getVertices().add( vertex );

		}

		for ( int i = 0; i < 1500; i ++ ) {

			Vector3 vertex = new Vector3();
			vertex.setX(Math.random() * 2 - 1);
			vertex.setY(Math.random() * 2 - 1);
			vertex.setZ(Math.random() * 2 - 1);
			vertex.multiplyScalar( radius );

			starsGeometry2.getVertices().add( vertex );

		}

		List<PointsMaterial> starsMaterials = Arrays.asList(
			new PointsMaterial().setColor(0x555555).setSize(2).setSizeAttenuation(false),
			new PointsMaterial().setColor(0x555555).setSize(1).setSizeAttenuation(false),
			new PointsMaterial().setColor(0x333333).setSize(2).setSizeAttenuation(false),
			new PointsMaterial().setColor(0x3a3a3a).setSize(1).setSizeAttenuation(false),
			new PointsMaterial().setColor(0x1a1a1a).setSize(2).setSizeAttenuation(false),
			new PointsMaterial().setColor(0x1a1a1a).setSize(1).setSizeAttenuation(false)
		);

		for ( int i = 10; i < 30; i ++ ) 
		{
			Points stars = new Points( i % 2 == 0 ? starsGeometry1 : starsGeometry2, starsMaterials.get( i % 6 ) );

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

	static final Vector3 dMoonVec = new Vector3();
	@Override
	public void onUpdate(RenderingContext context)
	{
		// slow down as we approach the surface

		double dPlanet = camera.getPosition().length();

		dMoonVec.subVectors(camera.getPosition(), meshMoon.getPosition());
		double dMoon = dMoonVec.length();

		double d = ( dMoon < dPlanet ) ? ( dMoon - radius * moonScale * 1.01 ) : ( dPlanet - radius * 1.01 );

		controls.setMovementSpeed(0.33 * d);
		controls.update( context.getDeltaTime() );

		context.getRenderer().clear();
//		composer.render( delta );
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
