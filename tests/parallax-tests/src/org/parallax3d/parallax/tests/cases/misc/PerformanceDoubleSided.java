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

package org.parallax3d.parallax.tests.cases.misc;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_performance_doublesided")
public final class PerformanceDoubleSided extends ParallaxTest implements TouchMoveHandler
{

	private static final String textures = "textures/cube/swedishRoyalCastle/*.jpg";

	Scene scene;
	PerspectiveCamera camera;

	int width = 0, height = 0;
	int mouseX;
	int mouseY;

	@Override
	public void onResize(RenderingContext context) {
		width = context.getWidth();
		height = context.getHeight();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				50, // fov
				context.getAspectRation(), // aspect
				1, // near
				20000 // far 
		); 
		
		camera.getPosition().setZ(3200);
		
		PointLight light1 = new PointLight( 0x0011ff, 1, 5500 );
		light1.getPosition().set( 4000, 0, 0 );
		scene.add( light1 );

		PointLight light2 = new PointLight( 0xff1100, 1, 5500 );
		light2.getPosition().set( -4000, 0, 0 );
		scene.add( light2 );

		PointLight light3 = new PointLight( 0xffaa00, 2, 3000 );
		light3.getPosition().set( 0, 0, 0 );
		scene.add( light3 );

		CubeTexture reflectionCube = new CubeTexture( textures );
		reflectionCube.setFormat(PixelFormat.RGB);

		MeshPhongMaterial material = new MeshPhongMaterial()
				.setSpecular( 0xffffff )
				.setShininess( 100 )
				.setEnvMap( reflectionCube )
				.setCombine( Texture.OPERATIONS.MIX )
				.setReflectivity( 0.1 )
				.setWrapAround(true)
				.setSide(Material.SIDE.DOUBLE);
		material.getWrapRGB().set( 0.5, 0.5, 0.5 );


		SphereGeometry geometry = new SphereGeometry( 1, 32, 16, 0, (double)Math.PI );

		for ( int i = 0; i < 5000; i ++ )
		{
			Mesh mesh = new Mesh( geometry, material );

			mesh.getPosition().setX( Math.random() * 10000.0 - 5000.0 );
			mesh.getPosition().setY( Math.random() * 10000.0 - 5000.0 );
			mesh.getPosition().setZ( Math.random() * 10000.0 - 5000.0 );

			mesh.getRotation().setX( Math.random() * 360.0 * ( Math.PI / 180.0 ) );
			mesh.getRotation().setY( Math.random() * 360.0 * ( Math.PI / 180.0 ) );
			
			double scale =  Math.random() * 50.0 + 100.0;
			mesh.getScale().set( scale );

			mesh.setMatrixAutoUpdate(false);
			mesh.updateMatrix();

			scene.add( mesh );
		}

		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
		context.getRenderer().setClearColor(0x050505);
	}
			
	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * .05 );
		camera.getPosition().addY( ( - mouseY - camera.getPosition().getY() ) * .05 );

		camera.lookAt( scene.getPosition() );
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 ) * 10;
		mouseY = (screenY - height / 2) * 10;
	}

	@Override
	public String getName() {
		return "Performance: Double sided";
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
