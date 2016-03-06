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
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.PointCloudMaterial;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_trails")
public final class ParticlesTrails extends ParallaxTest implements TouchMoveHandler
{
	Scene scene;
	PerspectiveCamera camera;

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
				10000 // far 
		); 
		
		camera.getPosition().set(100000, 0, 3200 );

		int[] colors = {0x000000, 0xff0080, 0x8000ff, 0xffffff};
		Geometry geometry = new Geometry();

		for ( int i = 0; i < 2000; i ++ ) 
		{
			Vector3 vertex = new Vector3();
			vertex.setX(Math.random() * 4000.0 - 2000.0);
			vertex.setY(Math.random() * 4000.0 - 2000.0);
			vertex.setZ(Math.random() * 4000.0 - 2000.0);
			geometry.getVertices().add( vertex );

			geometry.getColors().add( new Color( colors[ (int) Math.floor( Math.random() * colors.length ) ] ) );

		}

		PointCloudMaterial material = new PointCloudMaterial()
				.setSize( 1.0 )
				.setVertexColors( Material.COLORS.VERTEX )
				.setSizeAttenuation(false)
				.setDepthTest( false )
				.setOpacity( 0.5 )
				.setTransparent(true);

		PointCloud mesh = new PointCloud( geometry, material );
		scene.add( mesh );

		context.getRenderer().setSortObjects(false);
		context.getRenderer().setAutoClear(false);
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
		mouseX = (screenX - width / 2) * 10;
		mouseY = (screenY - height / 2) * 10;
	}

	@Override
	public String getName() {
		return "Particle Trails";
	}

	@Override
	public String getDescription() {
		return "Use mouse.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

//	@Override
//	protected void loadRenderingPanelAttributes(RenderingPanel renderingPanel) 
//	{
//		super.loadRenderingPanelAttributes(renderingPanel);
//		renderingPanel.getCanvas3dAttributes().setPreserveDrawingBufferEnable(true);
//	}
	
}
