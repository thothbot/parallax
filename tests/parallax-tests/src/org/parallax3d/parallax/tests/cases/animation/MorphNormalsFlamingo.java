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

package org.parallax3d.parallax.tests.cases.animation;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.animation.AnimationMixer;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.extras.objects.MorphBlendMesh;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_morphnormals")
public final class MorphNormalsFlamingo extends ParallaxTest
{
	static final String model = "models/animated/flamingo.js";
	
	static final int radius = 600;
	PerspectiveCamera camera;
	Scene scene;

	List<AnimationMixer> mixers = new ArrayList<>();
	
	private double oldTime;
	Vector3 target = new Vector3( 0, 150, 0 );

	double theta = 0;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		
		camera = new PerspectiveCamera(
				40, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		);
		
		camera.getPosition().setY(300);


		scene.add( new HemisphereLight( 0x443333, 0x222233 ) );

		DirectionalLight light = new DirectionalLight( 0xffffff, 1 );
		light.getPosition().set( 1, 1, 1 );
		scene.add( light );

//		morphs = new ArrayList<MorphBlendMesh>();

		new JsonLoader(model, new ModelLoadHandler() {

				@Override
				public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

					MeshLambertMaterial material = new MeshLambertMaterial()
							.setColor(new Color(0xffffff))
							.setMorphTargets(true)
							.setVertexColors(Material.COLORS.FACE)
							.setShading(Material.SHADING.FLAT);

					Mesh mesh = new Mesh( geometry, material );

					mesh.getPosition().setX(-150);
					mesh.getPosition().setY(150);
					mesh.getScale().set( 1.5, 1.5, 1.5 );

					scene.add( mesh );

					AnimationMixer mixer = new AnimationMixer( mesh );
					mixer.clipAction( geometry.animations[ 0 ] ).setDuration( 1 ).play();

					mixers.add( mixer );
				}

		});

		new JsonLoader(model, new ModelLoadHandler() {

				@Override
				public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

					geometry.computeVertexNormals();
					((Geometry)geometry).computeMorphNormals();

					MeshPhongMaterial material = new MeshPhongMaterial()
							.setColor(new Color(0xffffff))
							.setMorphTargets(true)
							.setMorphNormals(true)
							.setVertexColors(Material.COLORS.FACE)
							.setShading(Material.SHADING.SMOOTH);

					Mesh mesh = new Mesh( geometry, material );

					mesh.getPosition().setX(150);
					mesh.getPosition().setY(150);
					mesh.getScale().set( 1.5, 1.5, 1.5 );

					scene.add( mesh );

					AnimationMixer mixer = new AnimationMixer( mesh );
					mixer.clipAction( geometry.animations[ 0 ] ).setDuration( 1 ).play();

					mixers.add( mixer );
				}
		});

	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		theta += 0.1;

		camera.getPosition().setX(radius * Math.sin(Mathematics.degToRad(theta)));
		camera.getPosition().setZ(radius * Math.cos(Mathematics.degToRad(theta)));

		camera.lookAt( target );

		double delta = context.getDeltaTime();

		for ( int i = 0; i < mixers.size(); i ++ ) {

			mixers.get(i).update( delta );

		}

		context.getRenderer().clear();
		context.getRenderer().render( scene, camera );
	}

	@Override
	public String getName() {
		return "Morph normals: flamingo";
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
