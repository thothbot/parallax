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
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.MeshNormalMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_geometry_hierarchy")
public final class GeometryHierarchy extends ParallaxTest implements TouchMoveHandler
{

	Scene scene;
	PerspectiveCamera camera;
	
	Object3D group;

	int width = 0, height = 0;
	int mouseX = 0, mouseY = 0;

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
				10000 // far 
		);
		
		camera.getPosition().setZ(500);

		scene.setFog(new Fog( 0xffffff, 1, 10000));

		BoxGeometry geometry = new BoxGeometry( 100, 100, 100 );
		MeshNormalMaterial material = new MeshNormalMaterial();

		this.group = new Object3D();

		for ( int i = 0; i < 1000; i ++ ) 
		{
			Mesh mesh = new Mesh( geometry, material );
			mesh.getPosition().setX( Math.random() * 2000.0 - 1000.0 );
			mesh.getPosition().setY( Math.random() * 2000.0 - 1000.0 );
			mesh.getPosition().setZ( Math.random() * 2000.0 - 1000.0 );

			mesh.getRotation().setX( Math.random() * 360.0 * ( Math.PI / 180.0 ) );
			mesh.getRotation().setY( Math.random() * 360.0 * ( Math.PI / 180.0 ) );

			mesh.setMatrixAutoUpdate(false);
			mesh.updateMatrix();

			group.add( mesh );
		}

		scene.add( group );

		context.getRenderer().setSortObjects(false);
		context.getRenderer().setClearColor(0xeeeeee);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getDeltaTime() * 0.001;

		double rx = Math.sin( time * 0.7 ) * 0.5;
		double ry = Math.sin( time * 0.3 ) * 0.5;
		double rz = Math.sin( time * 0.2 ) * 0.5;

		camera.getPosition().addX(( mouseX - camera.getPosition().getX() ) * .05);
		camera.getPosition().addY(( - mouseY - camera.getPosition().getY() ) * .05);

		camera.lookAt( scene.getPosition() );

		this.group.getRotation().setX( rx );
		this.group.getRotation().setY( ry );
		this.group.getRotation().setZ( rz );
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 ) * 10;
		mouseY = (screenY - height / 2) * 10;
	}

	@Override
	public String getName() {
		return "Geometry hierarchy";
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
