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
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.Arrays;
import java.util.List;

@ThreejsExample("webgl_materials_wireframe")
public final class MaterialsWireframe extends ParallaxTest 
{

	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../../resources/shaders/materials_wireframe.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/materials_wireframe.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	Scene scene;
	PerspectiveCamera camera;
	
	Mesh meshLines;
	Mesh meshTris;
	Mesh meshMixed;

	@Override
	public void onStart(RenderingContext context)
	{
		double size = 150;

		scene = new Scene();
		camera = new PerspectiveCamera(
				40, // fov
				context.getAspectRation(), // aspect
				1, // near
				2000 // far 
		);
		
		camera.getPosition().setZ(800);

		MeshBasicMaterial material = new MeshBasicMaterial().setWireframe(true);

		Mesh mesh = new Mesh( new BufferGeometry().fromGeometry( new BoxGeometry( size, size, size ) ), material );
		mesh.getPosition().setX(-150);
		scene.add( mesh );

		//

		BufferGeometry geometry = new BufferGeometry().fromGeometry( new BoxGeometry( size, size, size ) );

		setupAttributes( geometry );

		// wireframe using gl.TRIANGLES (interpreted as triangles)

		ShaderMaterial materialMixed = new ShaderMaterial( Resources.INSTANCE );

		meshMixed = new Mesh( geometry, materialMixed );
		meshMixed.getPosition().setX(-150);
		scene.add( meshMixed );

	}

	private void setupAttributes(BufferGeometry geometry)
	{
		List<Vector3> vectors = Arrays.asList(
			new Vector3( 1, 0, 0 ),
			new Vector3( 0, 1, 0 ),
			new Vector3( 0, 0, 1 )
		);

		BufferAttribute position = geometry.getAttributes().get("position");
		Float32Array centers = Float32Array.create( position.getCount() * 3 );

		for ( int i = 0, l = position.getCount(); i < l; i ++ ) {

			vectors.get(i % 3).toArray( centers, i * 3 );

		}

		geometry.addAttribute( "center", new BufferAttribute( centers, 3 ) );
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		for (int i = 0; i < scene.getChildren().size(); i ++ ) {

			Object3D object = scene.getChildren().get(i);
			object.getRotation().addX( 0.005 );
			object.getRotation().addY( 0.01 );

		}

		context.getRenderer().render( scene, camera );
	}
	
	@Override
	public String getName() {
		return "Wireframe material";
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
