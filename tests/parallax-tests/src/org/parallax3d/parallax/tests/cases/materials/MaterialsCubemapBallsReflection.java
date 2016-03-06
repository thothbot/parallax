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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.CubeShader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_materials_cubemap_balls_reflection")
public final class MaterialsCubemapBallsReflection extends ParallaxTest implements TouchMoveHandler
{
	static final String textures = "textures/cube/pisa/*.png";

	Scene scene;
	PerspectiveCamera camera;

	int width = 0, height = 0;
	int mouseX = 0;
	int mouseY = 0;
	
	private List<Mesh> sphere;
	
	private Scene sceneCube;
	private PerspectiveCamera cameraCube;

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
				60, // fov
				context.getAspectRation(), // aspect
				1, // near
				100000 // far 
		);
		camera.getPosition().setZ(3200);		

		this.cameraCube = new PerspectiveCamera(
				60, // fov
				context.getAspectRation(), // aspect
				1, // near
				100000 // far 
		);
		
		this.sceneCube = new Scene();

		SphereGeometry geometry = new SphereGeometry( 100, 32, 16 );

		CubeTexture textureCube = new CubeTexture( textures );
		
		MeshBasicMaterial material = new MeshBasicMaterial()
				.setColor( 0xffffff )
				.setEnvMap( textureCube );
		
		this.sphere = new ArrayList<Mesh>();
		
		for ( int i = 0; i < 500; i ++ ) 
		{
			Mesh mesh = new Mesh( geometry, material );

			mesh.getPosition().setX( Math.random() * 10000.0 - 5000.0 );
			mesh.getPosition().setY( Math.random() * 10000.0 - 5000.0 );
			mesh.getPosition().setZ( Math.random() * 10000.0 - 5000.0 );

			double scale = Math.random() * 3.0 + 1.0;
			mesh.getScale().set(scale);

			scene.add( mesh );

			this.sphere.add( mesh );
		}

		// Skybox

		ShaderMaterial sMaterial = new ShaderMaterial( new CubeShader() )
				.setDepthWrite( false )
				.setSide(Material.SIDE.BACK);
		sMaterial.getShader().getUniforms().get("tCube").setValue( textureCube );

		Mesh mesh = new Mesh( new BoxGeometry( 100, 100, 100 ), sMaterial );
		sceneCube.add( mesh );
		
		context.getRenderer().setAutoClear(false);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double timer = 0.0001 * context.getDeltaTime();

		for ( int i = 0, il = this.sphere.size(); i < il; i ++ ) 
		{
			this.sphere.get(i).getPosition().setX( 5000.0 * Math.cos( timer + i ) );
			this.sphere.get(i).getPosition().setY( 5000.0 * Math.sin( timer + i * 1.1 ) );
		}

		camera.getPosition().addX(( mouseX - camera.getPosition().getX() ) * 0.05 );
		camera.getPosition().addY(( - mouseY - camera.getPosition().getY() ) * 0.05 );

		camera.lookAt( scene.getPosition() );

		this.cameraCube.getRotation().copy( camera.getRotation() );
		
		context.getRenderer().render( this.sceneCube, this.cameraCube);
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 ) * 10;
		mouseY = (screenY - height / 2) * 10;
	}

	@Override
	public String getName() {
		return "Cube map reflection";
	}

	@Override
	public String getDescription() {
		return "Drag mouse to move.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
