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
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.CubeShader;
import org.parallax3d.parallax.graphics.renderers.shaders.FresnelShader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_materials_shaders_fresnel")
public final class MaterialsCubemapFresnel extends ParallaxTest implements TouchMoveHandler
{

	private static final String textures = "textures/cube/park2/*.jpg";

	Scene scene;
	PerspectiveCamera camera;
	Scene sceneCube;
	PerspectiveCamera cameraCube;
	
	List<Mesh> spheres;

	int width = 0, height = 0;
	int mouseX = 0;
	int mouseY = 0;

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
		textureCube.setFormat(PixelFormat.RGB);

		FresnelShader shader = new FresnelShader();
		shader.getUniforms().get("tCube").setValue(textureCube);

		ShaderMaterial material = new ShaderMaterial( shader );

		spheres = new ArrayList<Mesh>();
		for ( int i = 0; i < 500; i ++ ) 
		{
			Mesh mesh = new Mesh( geometry, material );

			mesh.getPosition().setX( Math.random() * 10000 - 5000 );
			mesh.getPosition().setY( Math.random() * 10000 - 5000 );
			mesh.getPosition().setZ( Math.random() * 10000 - 5000 );

			mesh.getScale().set( Math.random() * 3 + 1 );

			scene.add( mesh );

			spheres.add( mesh );
		}

		scene.setMatrixAutoUpdate(false);

		// Skybox
		CubeShader shaderCube = new CubeShader();
		shaderCube.getUniforms().get("tCube").setValue(textureCube);

		ShaderMaterial sMaterial = new ShaderMaterial(shaderCube)
			.setSide(Material.SIDE.BACK);

		Mesh mesh = new Mesh( new BoxGeometry( 100000, 100000, 100000 ), sMaterial );
		sceneCube.add( mesh );

		//

		context.getRenderer().setAutoClear(false);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double timer = 0.0001 * context.getDeltaTime();

		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * .05 );
		camera.getPosition().addY( ( - mouseY - camera.getPosition().getY() ) * .05 );

		camera.lookAt( scene.getPosition() );

		cameraCube.getRotation().copy( camera.getRotation() );

		for ( int i = 0, il = spheres.size(); i < il; i ++ ) 
		{
			Mesh sphere = spheres.get( i );

			sphere.getPosition().setX( 5000 * Math.cos( timer + i ) );
			sphere.getPosition().setY( 5000 * Math.sin( timer + i * 1.1 ) );
		}

		context.getRenderer().clear();
		context.getRenderer().render( sceneCube, cameraCube );
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 ) * 10;
		mouseY = (screenY - height / 2) * 10;
	}
	
	@Override
	public String getName() {
		return "Cube map Fresnel shader";
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
