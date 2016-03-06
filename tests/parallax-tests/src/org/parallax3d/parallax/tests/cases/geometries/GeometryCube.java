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

package org.parallax3d.parallax.tests.cases.geometries;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_geometry_cube")
public class GeometryCube extends ParallaxTest
{
	static final String texture = "textures/crate.gif";
	PerspectiveCamera camera;

	Mesh mesh;
	Scene scene;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				70, // fov
				context.getAspectRation(), // aspect
				1, // near
				1000 // far
		);
		camera.getPosition().setZ(400);

		this.mesh = new Mesh(new BoxGeometry( 200, 200, 200 ), new MeshBasicMaterial().setMap(new Texture(texture)));
		scene.add(mesh);
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		this.mesh.getRotation().addX(0.005);
		this.mesh.getRotation().addY(0.01);

		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Cube and texture";
	}

	@Override
	public String getDescription() {
		return "Here are used cube geometry and mesh basic material with simple texture.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
