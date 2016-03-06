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
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshFaceMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_materials_lightmap")
public final class MaterialsLightmap extends ParallaxTest 
{

	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../../resources/shaders/skydome.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/skydome.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	static final String model = "models/obj/lightmap/lightmap.js";

	Scene scene;
	PerspectiveCamera camera;
	
	TrackballControls controls;

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
		
		camera.getPosition().set( 700, 180, -500 );

		scene.setFog( new Fog( 0xfafafa, 1000, 10000 ) );

		// CONTROLS

		controls = new TrackballControls( camera, context );
		controls.getTarget().setZ( 150 );

		// LIGHTS

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 1.475 );
		directionalLight.getPosition().set( 100, 100, -100 );
		scene.add( directionalLight );


		HemisphereLight hemiLight = new HemisphereLight( 0xffffff, 0xffffff, 1.25 );
		hemiLight.getColor().setHSL( 0.6, 1.0, 0.75 );
		hemiLight.getGroundColor().setHSL( 0.1, 0.8, 0.7 );
		hemiLight.getPosition().setY( 500 );
		scene.add( hemiLight );

		// SKYDOME

		ShaderMaterial skyMat = new ShaderMaterial(Resources.INSTANCE)
			.setSide(Material.SIDE.BACK);

		skyMat.getShader().addUniform("topColor", new Uniform(Uniform.TYPE.C, new Color(0x0077ff)));
		skyMat.getShader().addUniform("bottomColor", new Uniform(Uniform.TYPE.C, new Color(0xffffff)));
		skyMat.getShader().addUniform("topColor", new Uniform(Uniform.TYPE.C, hemiLight.getColor().clone()));
		skyMat.getShader().addUniform("offset", new Uniform(Uniform.TYPE.F, 400.0 ));
		skyMat.getShader().addUniform("exponent", new Uniform(Uniform.TYPE.F, 0.6 ));

		scene.getFog().setColor( ((Color)skyMat.getShader().getUniforms().get("bottomColor").getValue()).clone() );

		SphereGeometry skyGeo = new SphereGeometry( 4000, 32, 15 );
		Mesh sky = new Mesh( skyGeo, skyMat );
		scene.add( sky );

		// RENDERER

		context.getRenderer().setClearColor( scene.getFog().getColor(), 1 );
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);

		// MODEL

		new JsonLoader(model, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {
				Mesh mesh = new Mesh( geometry, new MeshFaceMaterial(((JsonLoader)loader).getMaterials()) );
				mesh.getPosition().set( 0 );
				mesh.getScale().set( 100 );
				scene.add( mesh );
			}
		});

	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		controls.update();
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Lightmap";
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
