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

package org.parallax3d.parallax.tests.cases.plugins;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.controllers.FlyControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.plugins.lensflare.LensFlare;
import org.parallax3d.parallax.graphics.renderers.plugins.lensflare.LensFlarePlugin;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_lensflares")
public final class EffectsLensFlares extends ParallaxTest
{
		
	private Texture textureFlare0 = new Texture( "textures/lensflare/lensflare0.png" );
	private Texture textureFlare2 = new Texture( "textures/lensflare/lensflare2.png" );
	private Texture textureFlare3 = new Texture( "textures/lensflare/lensflare3.png" );

	Scene scene;
	PerspectiveCamera camera;
	
	private FlyControls controls;
	
	private double oldTime;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				40, // fov
				context.getAspectRation(), // aspect
				1, // near
				15000 // far 
		);
		
		camera.getPosition().setZ(250);
		
		new LensFlarePlugin(context.getRenderer(), scene);

		controls = new FlyControls( camera, context );

		controls.setMovementSpeed( 2500 );
		controls.setRollSpeed( Math.PI / 6.0 );
		controls.setAutoForward( false );
		controls.setDragToLook( false );
		
		Fog fog = new Fog( 0x000000, 3500, 15000 );
		fog.getColor().setHSL( 0.51, 0.4, 0.01 );
		scene.setFog(fog);

		// world

		int s = 250;
		BoxGeometry cubeGeometry = new BoxGeometry( s, s, s );
		MeshPhongMaterial material = new MeshPhongMaterial()
				.setColor(0xffffff)
				.setAmbient(0x333333)
				.setSpecular(0xffffff)
				.setShininess(50);
		
		for ( int i = 0; i < 3000; i ++ ) 
		{
			Mesh mesh = new Mesh( cubeGeometry, material );

			mesh.getPosition().setX( 8000 * ( 2.0 * Math.random() - 1.0 ) );
			mesh.getPosition().setY( 8000 * ( 2.0 * Math.random() - 1.0 ) );
			mesh.getPosition().setZ( 8000 * ( 2.0 * Math.random() - 1.0 ) );

			mesh.getRotation().setX( Math.random() * Math.PI );
			mesh.getRotation().setY( Math.random() * Math.PI );
			mesh.getRotation().setZ( Math.random() * Math.PI );

			mesh.setMatrixAutoUpdate(false);
			mesh.updateMatrix();

			scene.add( mesh );
		}

		// lights

		AmbientLight ambient = new AmbientLight( 0xffffff );
		ambient.getColor().setHSL( 0.1, 0.3, 0.2 );
		scene.add( ambient );


		DirectionalLight dirLight = new DirectionalLight( 0xffffff, 0.125 );
		dirLight.getPosition().set( 0, -1, 0 ).normalize();
		scene.add( dirLight );

		dirLight.getColor().setHSL( 0.1, 0.7, 0.5 );

		// lens flares

		addLight( 0.55, 0.9, 0.5, 5000, 0, -1000 );
		addLight( 0.08, 0.8, 0.5,    0, 0, -1000 );
		addLight( 0.995, 0.5, 0.9, 5000, 5000, -1000 );

		// renderer
		context.getRenderer().setClearColor( scene.getFog().getColor(), 1 );
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
		
		this.oldTime = Duration.currentTimeMillis();
	}
	
	private void addLight( double h, double s, double l, double x, double y, double z ) 
	{
		PointLight light = new PointLight( 0xffffff, 1.5, 4500 );
		light.getColor().setHSL( h, s, l );
		light.getPosition().set( x, y, z );
		scene.add( light );
		
		Color flareColor = new Color( 0xffffff );
		flareColor.setHSL( h, s, l + 0.5 );
		
		final LensFlare lensFlare = new LensFlare( textureFlare0, 700, 0.0, Material.BLENDING.ADDITIVE, flareColor );

		lensFlare.add( textureFlare2, 512, 0.0, Material.BLENDING.ADDITIVE );
		lensFlare.add( textureFlare2, 512, 0.0, Material.BLENDING.ADDITIVE );
		lensFlare.add( textureFlare2, 512, 0.0, Material.BLENDING.ADDITIVE );

		lensFlare.add( textureFlare3, 60,  0.6, Material.BLENDING.ADDITIVE );
		lensFlare.add( textureFlare3, 70,  0.7, Material.BLENDING.ADDITIVE );
		lensFlare.add( textureFlare3, 120, 0.9, Material.BLENDING.ADDITIVE );
		lensFlare.add( textureFlare3, 70,  1.0, Material.BLENDING.ADDITIVE );

		lensFlare.setUpdateCallback(new LensFlare.Callback() {

			@Override
			public void update() {

				double vecX = -lensFlare.getPositionScreen().getX() * 2.0;
				double vecY = -lensFlare.getPositionScreen().getY() * 2.0;

				for( int f = 0; f < lensFlare.getLensFlares().size(); f++ ) 
				{
					LensFlare.LensSprite flare = lensFlare.getLensFlares().get( f );

					flare.x = lensFlare.getPositionScreen().getX() + vecX * flare.distance;
					flare.y = lensFlare.getPositionScreen().getY() + vecY * flare.distance;

					flare.rotation = 0;
				}

				lensFlare.getLensFlares().get( 2 ).y += 0.025;
				lensFlare.getLensFlares().get( 3 ).rotation = lensFlare.getPositionScreen().getX() * 0.5 + Mathematics.degToRad(45.0);
			}
		});

		lensFlare.setPosition(light.getPosition());

		scene.add( lensFlare );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		controls.update( context.getDeltaTime() );
		this.oldTime = Duration.currentTimeMillis();
		
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Lens Flares";
	}

	@Override
	public String getDescription() {
		return "Fly with WASD/RF/QE + mouse.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
