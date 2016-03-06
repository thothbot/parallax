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
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.objects.MorphAnimMesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
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
	Scene scene, scene2;
	
	List<MorphAnimMesh> morphs;
	
	private double oldTime;
	Vector3 target = new Vector3( 0, 150, 0 );

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		scene2 = new Scene();
		
		camera = new PerspectiveCamera(
				40, // fov
				0.5 * context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		);
		
		camera.getPosition().setY(300);
//		camera.addViewportResizeHandler(new ViewportResizeHandler() {
//			
//			@Override
//			public void onResize(ViewportResizeEvent event) {
//				camera.setAspect(0.5 * event.context.getAspectRation());
//				
//			}
//		});
		
		DirectionalLight light = new DirectionalLight( 0xffffff, 1.3 );
		light.getPosition().set( 1, 1, 1 );
		scene.add( light );

		
		DirectionalLight light2 = new DirectionalLight( 0xffffff, 0.1 );
		light2.getPosition().set( 0.25, -1, 0 );
		scene.add( light2 );
		
		DirectionalLight light12 = new DirectionalLight( 0xffffff, 1.3 );
		light12.getPosition().set( 1, 1, 1 );
		scene2.add( light12 );

		
		DirectionalLight light22 = new DirectionalLight( 0xffffff, 0.1 );
		light22.getPosition().set( 0.25, -1, 0 );
		scene2.add( light22 );

		morphs = new ArrayList<MorphAnimMesh>();
		new JsonLoader(model, new ModelLoadHandler() {

				@Override
				public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

					((JsonLoader)loader).morphColorsToFaceColors( (Geometry) geometry );
					((Geometry)geometry).computeMorphNormals();

					MeshLambertMaterial material = new MeshLambertMaterial();
					material.setColor(new Color(0xffffff));
					material.setMorphTargets(true);
					material.setMorphNormals(true);
					material.setVertexColors(Material.COLORS.FACE);
					material.setShading(Material.SHADING.FLAT);

					MorphAnimMesh meshAnim = new MorphAnimMesh( (Geometry) geometry, material );

					meshAnim.setDuration(5000);

					meshAnim.getScale().set( 1.5 );
					meshAnim.getPosition().setY( 150 );

					scene.add( meshAnim );
					morphs.add( meshAnim );
				}

		});

		new JsonLoader(model, new ModelLoadHandler() {

				@Override
				public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

					((JsonLoader)loader).morphColorsToFaceColors( (Geometry) geometry );
					((Geometry)geometry).computeMorphNormals();

					MeshPhongMaterial material = new MeshPhongMaterial();
					material.setColor(new Color(0xffffff));
					material.setSpecular(new Color(0xffffff));
					material.setShininess(20);
					material.setMorphTargets(true);
					material.setMorphNormals(true);
					material.setVertexColors(Material.COLORS.FACE);
					material.setShading(Material.SHADING.SMOOTH);

					MorphAnimMesh meshAnim = new MorphAnimMesh( (Geometry) geometry, material );

					meshAnim.setDuration(5000);

					meshAnim.getScale().set( 1.5 );
					meshAnim.getPosition().setY( 150 );

					scene2.add( meshAnim );
					morphs.add( meshAnim );
				}
		});
		
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
		context.getRenderer().setSortObjects(false);
		context.getRenderer().setAutoClear(false);
		
		this.oldTime = Duration.currentTimeMillis();

		context.getRenderer().setClearColor(0x222222);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double theta = context.getDeltaTime() * 0.01;

		camera.getPosition().setX( radius * Math.sin( theta * Math.PI / 360.0 ) );
		camera.getPosition().setZ( radius * Math.cos( theta * Math.PI / 360.0 ) );

		camera.lookAt( target );

		for ( int i = 0; i < morphs.size(); i ++ ) 
		{
			MorphAnimMesh morph = morphs.get( i );
			morph.updateAnimation( Duration.currentTimeMillis() - this.oldTime );
		}

		context.getRenderer().clear();

		context.getRenderer().setViewport( 0, 0, context.getRenderer().getAbsoluteWidth()/2, context.getRenderer().getAbsoluteHeight() );
		context.getRenderer().render( scene, camera );

		context.getRenderer().setViewport( context.getRenderer().getAbsoluteWidth()/2, 0, context.getRenderer().getAbsoluteWidth()/2, context.getRenderer().getAbsoluteHeight() );
		context.getRenderer().render( scene2, camera );

		this.oldTime = Duration.currentTimeMillis();
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
