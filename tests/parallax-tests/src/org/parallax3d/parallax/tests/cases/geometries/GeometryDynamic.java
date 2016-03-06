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
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import org.parallax3d.parallax.controllers.FirstPersonControls;

@ThreejsExample("webgl_geometry_dynamic")
public class GeometryDynamic extends ParallaxTest
{

	static final String img = "textures/water.jpg";

	Scene scene;
	PerspectiveCamera camera;

	FirstPersonControls controls;
	PlaneGeometry geometry;
	Mesh mesh;

	int worldWidth = 32;
	int worldDepth = 32;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				60, // fov
				context.getAspectRation(), // aspect
				1, // near
				20000 // far
		);

		camera.getPosition().setY(200);

		scene.setFog(new FogExp2( 0xAACCFF, 0.0007 ));

		this.controls = new FirstPersonControls( camera, context );
		controls.setMovementSpeed(500);
		controls.setLookSpeed(0.1);

		this.geometry = new PlaneGeometry( 20000, 20000, worldWidth - 1, worldDepth - 1 );
		this.geometry.applyMatrix(new Matrix4().makeRotationX( - Math.PI / 2.0 ));

		for ( int i = 0, il = this.geometry.getVertices().size(); i < il; i ++ )
			this.geometry.getVertices().get( i ).setY(35.0 * Math.sin( i/2.0 ));

		this.geometry.computeFaceNormals();
		this.geometry.computeVertexNormals();

		Texture texture = new Texture(img);
		texture.setWrapS(TextureWrapMode.REPEAT);
		texture.setWrapT(TextureWrapMode.REPEAT);
		texture.getRepeat().set( 5.0, 5.0 );

		MeshBasicMaterial material = new MeshBasicMaterial()
				.setColor( 0x0044ff )
				.setMap( texture );

		this.mesh = new Mesh( this.geometry, material );
		scene.add( this.mesh );

		context.getRenderer().setClearColor(0xaaccff);
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		for ( int i = 0, l = this.geometry.getVertices().size(); i < l; i ++ )
			this.geometry.getVertices().get( i ).setY(35.0 * Math.sin( i / 5.0 + ( context.getDeltaTime() * 0.01 + i ) / 7.0 ));

		this.mesh.getGeometry().setVerticesNeedUpdate( true );

		this.controls.update( context.getDeltaTime() );

		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Vertices moving";
	}

	@Override
	public String getDescription() {
		return "Here are shown vertices moving on single surface and using dense fog. (left click: forward, right click: backward). ";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

}
