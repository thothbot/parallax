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
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_test_memory2")
public final class MiscMemoryTestShaders extends ParallaxTest
{
	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../../resources/shaders/misc_memory_test.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/misc_memory_test.fs.glsl")
		SourceTextResource getFragmentShader();
	}
	
	private static final int N = 100;

	Scene scene;
	PerspectiveCamera camera;
	List<Mesh> meshes;

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
		
		camera.getPosition().setZ(2000);

		SphereGeometry geometry = new SphereGeometry( 15, 64, 32 );
		meshes = new ArrayList<Mesh>();

		for ( int i = 0; i < N; i ++ ) 
		{
			ShaderMaterial material = new ShaderMaterial(Resources.INSTANCE);
			generateFragmentShader(material.getShader());

			Mesh mesh = new Mesh( geometry, material );

			mesh.getPosition().setX( ( 0.5 - Math.random() ) * 1000 );
			mesh.getPosition().setY( ( 0.5 - Math.random() ) * 1000 );
			mesh.getPosition().setZ( ( 0.5 - Math.random() ) * 1000 );

			scene.add( mesh );

			meshes.add( mesh );
		}

		context.getRenderer().setClearColor(0xeeeeee);
	}

	private void generateFragmentShader(Shader shader) 
	{
		String vector = Math.random() + "," + Math.random() + "," + Math.random();
		shader.setFragmentSource(Shader.updateShaderSource(shader.getFragmentSource(), vector));
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		for ( int i = 0; i < N; i ++ ) 
		{
			Mesh mesh = meshes.get( i );
			 mesh.getMaterial().deallocate( context.getRenderer() );
		}
		
		for ( int i = 0; i < N; i ++ ) 
		{
			Mesh mesh = meshes.get( i );
			ShaderMaterial material = new ShaderMaterial(Resources.INSTANCE);
			generateFragmentShader(material.getShader());
			mesh.setMaterial(material);
		}
		
		context.getRenderer().render(scene, camera);
	}
		
	@Override
	public String getName() {
		return "Memory test: shaders";
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
