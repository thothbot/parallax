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

package org.parallax3d.parallax.tests.cases;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_loader_collada")
public final class LoaderCollada extends ParallaxTest 
{

	static final String model = "collada/monster/monster.dae";

	Scene scene;
	PerspectiveCamera camera;
	Mesh particleLight;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 45,
				context.getAspectRation(),
				1, 
				2000 
		);
		camera.getPosition().set(2, 2, 3);

//		new ColladaLoader(model, new XHRLoader.ModelLoadHandler() {
//
//			@Override
//			public void onModelLoaded(XHRLoader loader, AbstractGeometry geometry) {
////						assert(false);
//				// Add the COLLADA
////						scene.addChild( dae );
//			}
//		});
		
		// Grid

		LineBasicMaterial line_material = new LineBasicMaterial()
				.setColor( 0xcccccc )
				.setOpacity(0.2);
		
		Geometry geometry = new Geometry();
		double floor = -0.04, step = 1.0, size = 14.0;

		for ( int i = 0; i <= size / step * 2; i ++ ) 
		{

			geometry.getVertices().add( new Vector3( - size, floor, i * step - size ) );
			geometry.getVertices().add( new Vector3(   size, floor, i * step - size ) );

			geometry.getVertices().add( new Vector3( i * step - size, floor, -size ) );
			geometry.getVertices().add( new Vector3( i * step - size, floor,  size ) );

		}

		Line line = new Line( geometry, line_material, Line.MODE.PIECES);
		scene.add( line );

		MeshBasicMaterial sMaterial = new MeshBasicMaterial()
				.setColor( 0xffffff );
		
		this.particleLight = new Mesh( new SphereGeometry( 4, 8, 8 ), sMaterial );
		scene.add( this.particleLight );

		// Lights

		scene.add( new AmbientLight( 0xcccccc ) );

		DirectionalLight directionalLight = new DirectionalLight(0xeeeeee );
		directionalLight.getPosition().setX( Math.random() - 0.5 );
		directionalLight.getPosition().setY( Math.random() - 0.5 );
		directionalLight.getPosition().setZ( Math.random() - 0.5 );

		PointLight pointLight = new PointLight( 0xffffff, 4, 0 );
		pointLight.setPosition( this.particleLight.getPosition() );
		scene.add( pointLight );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double timer = context.getDeltaTime() * 0.0005;

		camera.getPosition().setX( Math.cos( timer ) * 10.0 );
		camera.getPosition().setY( 2.0 );
		camera.getPosition().setZ( Math.sin( timer ) * 10.0 );

		camera.lookAt( scene.getPosition() );

		this.particleLight.getPosition().setX( Math.sin( timer * 4 ) * 3009.0 );
		this.particleLight.getPosition().setY( Math.cos( timer * 5 ) * 4000.0 );
		this.particleLight.getPosition().setZ( Math.cos( timer * 4 ) * 3009.0 );
		
		context.getRenderer().render(scene, camera);
	}
		
	@Override
	public String getName() {
		return "ColladaLoader";
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
