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
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
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
		scene = new Scene();
		camera = new PerspectiveCamera(
				40, // fov
				context.getAspectRation(), // aspect
				1, // near
				2000 // far 
		);
		
		camera.getPosition().setZ(800);
		
		double size = 150;

		BoxGeometry geometryLines = new BoxGeometry( size, size, size );
		BoxGeometry geometryTris = new BoxGeometry( size, size, size );

		// wireframe using gl.LINES

		MeshBasicMaterial materialLines = new MeshBasicMaterial()
			.setWireframe(true);

		meshLines = new Mesh( geometryLines, materialLines );
		meshLines.getPosition().setX(-150);
		scene.add( meshLines );

		// wireframe using gl.TRIANGLES (interpreted as triangles)

		Attribute attributesTris = new Attribute(Attribute.TYPE.V3, setupAttributes( geometryTris ));
		attributesTris.setBoundTo( Attribute.BOUND_TO.FACE_VERTICES );
		
		ShaderMaterial materialTris = new ShaderMaterial( Resources.INSTANCE );
		materialTris.getShader().addAttributes("center", attributesTris);

		meshTris = new Mesh( geometryTris, materialTris );
		meshTris.getPosition().setX(150);
		scene.add( meshTris );

		// wireframe using gl.TRIANGLES (mixed triangles and quads)

		SphereGeometry mixedGeometry = new SphereGeometry( size / 2.0, 32, 16 );

		Attribute attributesMixed = new Attribute(Attribute.TYPE.V3, setupAttributes( mixedGeometry ));
		attributesMixed.setBoundTo( Attribute.BOUND_TO.FACE_VERTICES );

		ShaderMaterial materialMixed = new ShaderMaterial( Resources.INSTANCE );
		materialMixed.getShader().addAttributes("center", attributesMixed);

		meshMixed = new Mesh( mixedGeometry, materialMixed );
		meshMixed.getPosition().setX(-150);
		scene.add( meshMixed );

	}
	
	private List<List<Vector3>> setupAttributes(Geometry geometry)
	{
		List<List<Vector3>> values = new ArrayList<List<Vector3>>();
		
		for( int f = 0; f < geometry.getFaces().size(); f ++ ) 
		{
			Face3 face = geometry.getFaces().get( f );
			values.add(f, Arrays.asList(
					new Vector3( 1, 0, 0 ), 
					new Vector3( 0, 1, 0 ), 
					new Vector3( 0, 0, 1 ) ));
		}

		return values;
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		meshLines.getRotation().addX(0.005);
		meshLines.getRotation().addY(0.01);

		meshTris.getRotation().addX(0.005);
		meshTris.getRotation().addY(0.01);

		if ( meshMixed != null) 
		{
			meshMixed.getRotation().addX(0.005);
			meshMixed.getRotation().addY(0.01);
		}
		
		context.getRenderer().render(scene, camera);
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
