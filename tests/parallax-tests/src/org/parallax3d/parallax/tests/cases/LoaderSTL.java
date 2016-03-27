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

package org.parallax3d.parallax.tests.cases;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.gl.ShadowMap;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.loaders.STLLoader;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_loader_stl")
public class LoaderSTL extends ParallaxTest 
{
	private static final String slotted_disk = "models/stl/ascii/slotted_disk.stl";
	private static final String pr2_head_pan = "models/stl/binary/pr2_head_pan.stl";
	private static final String pr2_head_tilt = "models/stl/binary/pr2_head_tilt.stl";
	private static final String colored = "models/stl/binary/colored.stl";

	Scene scene;
	PerspectiveCamera camera;
	Vector3 cameraTarget;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				35, // fov
				context.getAspectRation(), // aspect
				1, // near
				15 // far 
		);
		
		camera.getPosition().set( 3, 0.15, 3 );
		
		cameraTarget = new Vector3( 0, -0.25, 0 );
		
		scene.setFog(new Fog( 0x72645b, 2, 15 ));

		// Ground

		Mesh plane = new Mesh( new PlaneBufferGeometry( 40, 40 ),  new MeshPhongMaterial().setColor( 0x999999 ).setSpecular( 0x101010 ) );
		plane.getRotation().setX( -Math.PI/2 );
		plane.getPosition().setY( -0.5 );
		scene.add( plane );

		plane.setReceiveShadow(true);
		
		// Binary files
		new STLLoader(slotted_disk, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

				MeshPhongMaterial material = new MeshPhongMaterial()
						.setColor( new Color(0xff5533) )
						.setSpecular( new Color(0x111111) )
						.setShininess(200.0);

				Mesh mesh = new Mesh( geometry, material );

				mesh.getPosition().set( 0, - 0.25, 0.6 );
				mesh.getRotation().set( 0, - Math.PI / 2, 0 );
				mesh.getScale().set( 0.5, 0.5, 0.5 );

				mesh.setCastShadow(true);
				mesh.setReceiveShadow(true);

				scene.add( mesh );

			}
		});

		final MeshPhongMaterial material = new MeshPhongMaterial()
				.setColor( 0xAAAAAA )
				.setSpecular( 0x111111 )
				.setShininess(200.0);

		new STLLoader(pr2_head_pan, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

				Mesh mesh = new Mesh( geometry, material );

				mesh.getPosition().set( 0, - 0.37, - 0.6 );
				mesh.getRotation().set( - Math.PI / 2, 0, 0 );
				mesh.getScale().set( 2, 2, 2 );

				mesh.setCastShadow(true);
				mesh.setReceiveShadow(true);

				scene.add( mesh );

			}
		});

		new STLLoader(pr2_head_tilt, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

				Mesh mesh = new Mesh( geometry, material );

				mesh.getPosition().set( 0.136, - 0.37, - 0.6 );
				mesh.getRotation().set( - Math.PI / 2, 0.3, 0 );
				mesh.getScale().set( 2, 2, 2 );

				mesh.setCastShadow(true);
				mesh.setReceiveShadow(true);

				scene.add( mesh );

			}
		});

		new STLLoader(colored, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {


				MeshPhongMaterial meshMaterial = material;

				if(((STLLoader)loader).isHasColors())
					meshMaterial = new MeshPhongMaterial()
						.setVertexColors( Material.COLORS.VERTEX )
						.setOpacity(((STLLoader)loader).getAlpha());

				Mesh mesh = new Mesh( geometry, meshMaterial );

				mesh.getPosition().set( 0.5, 0.2, 0 );
				mesh.getRotation().set( - Math.PI / 2, Math.PI / 2, 0 );
				mesh.getScale().set( 0.3, 0.3, 0.3 );

				mesh.setCastShadow(true);
				mesh.setReceiveShadow(true);

				scene.add( mesh );

			}
		});

		scene.add( new HemisphereLight( 0x443333, 0x111122 ) );
		
		addShadowedLight( 1, 1, 1, 0xffffff, 1.35 );
		addShadowedLight( 0.5, 1, -1, 0xffaa00, 1 );

		new ShadowMap(context.getRenderer(), scene)
				.setCullFrontFaces(false);

		context.getRenderer().setClearColor( scene.getFog().getColor() );
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
	}
	
	private void addShadowedLight( double x, double y, double z, int color, double intensity ) {

		DirectionalLight directionalLight = new DirectionalLight( color, intensity );
		directionalLight.getPosition().set( x, y, z );
		scene.add( directionalLight );

		int d = 1;
		directionalLight.setCastShadow(true);

		directionalLight.getShadow().getCamera().setLeft( -d );
		directionalLight.getShadow().getCamera().setRight( d );
		directionalLight.getShadow().getCamera().setTop( d );
		directionalLight.getShadow().getCamera().setBottom( -d );

		directionalLight.getShadow().getCamera().setNear( 1 );
		directionalLight.getShadow().getCamera().setFar( 4 );

		directionalLight.getShadow().getMap().setWidth( 1024 );
		directionalLight.getShadow().getMap().setHeight( 1024 );

		directionalLight.getShadow().setBias( -0.005 );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double duration = context.getFrameId();

		camera.getPosition().setX( Math.cos( duration * 0.005 ) * 3 );
		camera.getPosition().setZ( Math.sin( duration * 0.005) * 3 );

		camera.lookAt( cameraTarget );

		context.getRenderer().render(scene, camera);
	}
		
	@Override
	public String getName() {
		return "STL loader";
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
