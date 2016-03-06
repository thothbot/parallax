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

package org.parallax3d.parallax.tests.cases.interactivity;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.controllers.TrackballControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.SpotLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Uint8Array;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.PixelType;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_interactive_cubes_gpu")
public final class InteractiveCubesGpu extends ParallaxTest implements TouchMoveHandler
{
	class Picking
	{
		public Vector3 position;
		public Euler rotation;
		public Vector3 scale;
	}
	
	Scene scene;
	PerspectiveCamera camera;
	
	Vector3 offset = new Vector3(10, 10, 10);
	int mouseX = 0, mouseY = 0;

	Scene pickingScene;
	TrackballControls controls;
	RenderTargetTexture pickingTexture;
	
	Mesh highlightBox;
	List<Picking> pickingData;

	@Override
	public void onResize(RenderingContext context) 
	{
		pickingTexture.setWidth( context.getWidth() );
		pickingTexture.setHeight(  context.getHeight() );
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				70, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		);
		camera.getPosition().setZ(1000);
		
		controls = new TrackballControls( camera, context  );
		controls.setRotateSpeed(1.0);
		controls.setZoomSpeed(1.2);
		controls.setPanSpeed(0.8);
		controls.setZoom(true);
		controls.setPan(true);
		controls.setStaticMoving(true);
		controls.setDynamicDampingFactor(0.3);

		pickingScene = new Scene();

		pickingTexture = new RenderTargetTexture(context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight());
		pickingTexture.setGenerateMipmaps(false);

		scene.add( new AmbientLight( 0x555555 ) );

		SpotLight light = new SpotLight( 0xffffff, 1.5 );
		light.getPosition().set( 0, 500, 2000 );
		scene.add( light );

		Geometry geometry = new Geometry();
		Geometry pickingGeometry = new Geometry();
		MeshBasicMaterial pickingMaterial = new MeshBasicMaterial().setVertexColors(Material.COLORS.VERTEX);

		MeshLambertMaterial defaultMaterial = new MeshLambertMaterial()
				.setColor( 0xffffff )
				.setShading(Material.SHADING.FLAT)
				.setVertexColors(Material.COLORS.VERTEX);

		pickingData = new ArrayList<Picking>();
		
		BoxGeometry geom = new BoxGeometry( 1, 1, 1 );
		Color color = new Color();

		Matrix4 matrix = new Matrix4();
		Quaternion quaternion = new Quaternion();

		for ( int i = 0; i < 500; i ++ ) 
		{
			Vector3 position = new Vector3();
			position.setX( Math.random() * 10000 - 5000 );
			position.setY( Math.random() * 6000 - 3000 );
			position.setZ( Math.random() * 8000 - 4000 );

			Euler rotation = new Euler();
			rotation.setX( ( Math.random() * 2 * Math.PI) );
			rotation.setY( ( Math.random() * 2 * Math.PI) );
			rotation.setZ( ( Math.random() * 2 * Math.PI) );

			Vector3 scale = new Vector3();
			scale.setX( Math.random() * 200 + 100 );
			scale.setY( Math.random() * 200 + 100 );
			scale.setZ( Math.random() * 200 + 100 );
			
			quaternion.setFromEuler( rotation, false );
			matrix.compose( position, quaternion, scale );


			//give the geom's vertices a random color, to be displayed
			applyVertexColors( geom, color.setHex( (int)Math.random() * 0xffffff ) );
			
			geometry.merge( geom, matrix );

			// give the geom's vertices a color corresponding to the "id"

			applyVertexColors( geom, color.setHex( i ) );

			pickingGeometry.merge( geom, matrix );
			
			Picking picking = new Picking();
			picking.position = position;
			picking.rotation = rotation;
			picking.scale = scale;

			pickingData.add(picking);
		}
		
		Mesh drawnObject = new Mesh(geometry, defaultMaterial);
		scene.add(drawnObject);

		pickingScene.add(new Mesh(pickingGeometry, pickingMaterial));

		highlightBox = new Mesh( new BoxGeometry(1, 1, 1), new MeshLambertMaterial().setColor( 0xffff00 ) );
		scene.add( highlightBox );

		context.getRenderer().setSortObjects(false);
		context.getRenderer().setClearColor(0xffffff);
	}
	
	private void applyVertexColors(Geometry g, Color c) 
	{
		for(Face3 f: g.getFaces())
		{
			int n = (f.getClass() == Face3.class) ? 3 : 4;
			for(int j = 0; j < n; j++)
			{
				f.getVertexColors().add( c );
			}
		}
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		controls.update();

		pick(context);
		
		context.getRenderer().render(scene, camera);
	}
	
	private void pick(RenderingContext context)
	{
		//render the picking scene off-screen
		GL20 gl = context.getRenderer().gl;
		context.getRenderer().render(pickingScene, camera, pickingTexture);
		Uint8Array pixelBuffer = Uint8Array.create(4);

		//read the pixel under the mouse from the texture
		gl.glReadPixels(mouseX, pickingTexture.getHeight() - mouseY, 1, 1, PixelFormat.RGBA.getValue(), PixelType.UNSIGNED_BYTE.getValue(), pixelBuffer.getBuffer());

		//interpret the pixel as an ID

		int id = ( pixelBuffer.get(0) << 16 ) | (  pixelBuffer.get(1) << 8 ) | ( pixelBuffer.get(2) );
		if( pickingData.size() > id && pickingData.get(id) != null )
		{
			Picking data = pickingData.get(id);
			//move our highlightBox so that it surrounds the picked object
			if(data.position != null && data.rotation != null && data.scale != null)
			{
				highlightBox.getPosition().copy(data.position);
				highlightBox.getRotation().copy(data.rotation);
				highlightBox.getScale().copy(data.scale).add(offset);
				highlightBox.setVisible(true);
			}
		} 
		else 
		{
			highlightBox.setVisible(false);
		}
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = screenX;
		mouseY = screenY;
	}

	@Override
	public String getName() {
		return "GPU picking";
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
