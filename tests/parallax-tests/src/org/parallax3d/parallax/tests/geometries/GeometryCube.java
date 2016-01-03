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

package org.parallax3d.parallax.tests.geometries;

import org.parallax3d.parallax.App;
import org.parallax3d.parallax.tests.DemoAnimation;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;

public class GeometryCube extends DemoAnimation
{
	private static final String texture = "./static/textures/crate.gif";
	PerspectiveCamera camera;

	private Mesh mesh;
	private Scene scene;

	public GeometryCube()
	{
		super("Cube and texture", "Here are used cube geometry and mesh basic material with simple texture. This example based on the three.js example.");
	}

	@Override
	public void onStart()
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				70, // fov
				App.app.getRendering().getRenderer().getAbsoluteAspectRation(), // aspect
				1, // near
				1000 // far
		);
		camera.getPosition().setZ(400);

		BoxGeometry geometry = new BoxGeometry( 200, 200, 200 );

		MeshBasicMaterial material = new MeshBasicMaterial();
//		material.setMap(new Texture(texture));

		this.mesh = new Mesh(geometry, material);
		scene.add(mesh);
	}

	@Override
	public void onUpdate()
	{
		this.mesh.getRotation().addX(0.005);
		this.mesh.getRotation().addY(0.01);

		App.app.getRendering().getRenderer().render(scene, camera);
	}
}
