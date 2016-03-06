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
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.textures.TextureData;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_test_memory")
public final class MiscMemoryTestGeometries extends ParallaxTest 
{

	Scene scene;
	PerspectiveCamera camera;
	Mesh mesh;
	Texture texture;
	
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
		
		camera.getPosition().setZ(200);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{			
		SphereGeometry geometry = new SphereGeometry( 50, (int)(Math.random() * 64), (int)(Math.random() * 32) );

		texture = new Texture( generateTexture() );
		texture.setNeedsUpdate(true);

		MeshBasicMaterial material = new MeshBasicMaterial()
				.setMap(texture)
				.setWireframe(true);

		mesh = new Mesh( geometry, material );

		scene.add( mesh );
		
		context.getRenderer().render(scene, camera);
		
		scene.remove( mesh );
		
		// clean up

		geometry.dispose();
//			material.dispose();
//			texture.dispose();
	}
	
	private TextureData generateTexture()
	{
//		CanvasElement canvas = Document.get().createElement("canvas").cast();
//		canvas.setWidth(256);
//		canvas.setHeight(256);
//
//		Context2d context = canvas.getContext2d();
//		context.setFillStyle("rgb(" + Math.floor( Math.random() * 256 )
//				+ "," + Math.floor( Math.random() * 256 )
//				+ "," + Math.floor( Math.random() * 256 ) + ")");
//		context.fillRect( 0, 0, 256, 256 );

		return null;
	}

	@Override
	public String getName() {
		return "Memory test: geometries";
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
