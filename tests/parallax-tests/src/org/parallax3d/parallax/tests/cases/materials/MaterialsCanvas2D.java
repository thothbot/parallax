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
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.textures.TextureData;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_materials")
public final class MaterialsCanvas2D extends ParallaxTest 
{
	Scene scene;
	PerspectiveCamera camera;
	private List<Material> materials;
	private PointLight pointLight;
	private Mesh particleLight;
	private List<Mesh> objects;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				45, // fov
				context.getAspectRation(), // aspect
				1, // near
				2000 // far 
		); 

		camera.getPosition().set(0, 200, 800);
		
		// Grid

		Geometry geometry = new Geometry();
		
		double floor = -75, step = 25;

		for ( int i = 0; i <= 40; i ++ ) 
		{
			geometry.getVertices().add( new Vector3( - 500, floor, i * step - 500 ) );
			geometry.getVertices().add( new Vector3(   500, floor, i * step - 500 ) );

			geometry.getVertices().add( new Vector3( i * step - 500, floor, -500 ) );
			geometry.getVertices().add( new Vector3( i * step - 500, floor,  500 ) );
		}

		LineBasicMaterial line_material = new LineBasicMaterial()
			.setColor( 0x303030 );

		Line line = new Line( geometry, line_material, Line.MODE.PIECES );
		scene.add( line );

		// Materials

		Texture texture = new Texture( generateTexture() );
		texture.setNeedsUpdate(true);

		this.materials = new ArrayList<>();

		materials.add( new MeshLambertMaterial()
				.setMap(texture)
				.setTransparent(true));

		materials.add( new MeshLambertMaterial()
				.setColor( 0xdddddd )
				.setShading( Material.SHADING.FLAT ));

		materials.add( new MeshPhongMaterial()
				.setAmbient( 0x030303 )
				.setColor( 0xdddddd )
				.setSpecular( 0x009900 )
				.setShininess( 30 )
				.setShading( Material.SHADING.FLAT ));
		
		materials.add( new MeshNormalMaterial() );

		materials.add( new MeshBasicMaterial()
				.setColor( 0xffaa00 )
				.setTransparent( true )
				.setBlending( Material.BLENDING.ADDITIVE ));

		materials.add( new MeshLambertMaterial()
				.setColor( 0xdddddd )
				.setShading( Material.SHADING.SMOOTH ));

		materials.add( new MeshPhongMaterial()
				.setMap( texture )
				.setTransparent( true ));

		materials.add( new MeshNormalMaterial()
				.setShading( Material.SHADING.SMOOTH ));

		materials.add( new MeshBasicMaterial()
				.setColor( 0x00ffaa )
				.setWireframe(true));

		materials.add( new MeshDepthMaterial() );

		materials.add( new MeshLambertMaterial()
				.setColor( 0x666666 )
				.setEmissive( 0xff0000 )
				.setAmbient( 0x000000 )
				.setShading( Material.SHADING.SMOOTH ));

		materials.add( new MeshPhongMaterial()
				.setAmbient( 0x000000 )
				.setEmissive( 0xffff00 )
				.setColor( 0x000000 )
				.setSpecular( 0x666666 )
				.setShininess( 10 )
				.setShading( Material.SHADING.SMOOTH )
				.setOpacity( 0.9 )
				.setTransparent(true));

		MeshBasicMaterial mbOpt2 = new MeshBasicMaterial();
		mbOpt2.setMap( texture );
		mbOpt2.setTransparent( true );
		materials.add( mbOpt2 );

		// Spheres geometry

		SphereGeometry geometry_smooth = new SphereGeometry( 70, 32, 16 );
		SphereGeometry geometry_flat = new SphereGeometry( 70, 32, 16 );
		SphereGeometry geometry_pieces = new SphereGeometry( 70, 32, 16 ); // Extra geometry to be broken down for MeshFaceMaterial

		for ( int i = 0, l = geometry_pieces.getFaces().size(); i < l; i ++ ) 
		{
			Face3 face = geometry_pieces.getFaces().get( i );

			if ( Math.random() > 0.7 )
				face.setMaterialIndex( (int)( Math.random() * materials.size()) );

			else
				face.setMaterialIndex( 0 );

		}

//			((GeometryObject)geometry_pieces).setmmaterials = materials;

		materials.add( new MeshFaceMaterial() );

		this.objects = new ArrayList<Mesh>();

		for ( int i = 0, l = materials.size(); i < l; i ++ ) 
		{
			Material material = materials.get( i );

			Geometry geometryMesh = material.getClass() == MeshFaceMaterial.class && 1==2
						? geometry_pieces 
						: ( material instanceof HasShading && ((HasShading)material).getShading() == Material.SHADING.FLAT 
							? geometry_flat 
							: geometry_smooth );

			Mesh sphere = new Mesh( geometryMesh, material );

			sphere.getPosition().setX(( i % 4 ) * 200.0 - 400.0);
			sphere.getPosition().setZ(Math.floor( i / 4.0 ) * 200.0 - 200.0);

			sphere.getRotation().setX(Math.random() * 200.0 - 100.0);
			sphere.getRotation().setY(Math.random() * 200.0 - 100.0);
			sphere.getRotation().setZ(Math.random() * 200.0 - 100.0);

			this.objects.add( sphere );

			scene.add( sphere );

		}

		this.particleLight = new Mesh( new SphereGeometry( 4, 8, 8 ), new MeshBasicMaterial().setColor( 0xffffff ) );
		scene.add( this.particleLight );

		// Lights

		scene.add( new AmbientLight( 0x111111 ) );

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 0.125 );

		directionalLight.getPosition().setX( Math.random() - 0.5 );
		directionalLight.getPosition().setY( Math.random() - 0.5 );
		directionalLight.getPosition().setZ( Math.random() - 0.5 );

		directionalLight.getPosition().normalize();

		scene.add( directionalLight );

		this.pointLight = new PointLight( 0xffffff );
		scene.add( pointLight );
	}
	
	private TextureData generateTexture()
	{
//		CanvasElement canvas = Document.get().createElement("canvas").cast();
//		canvas.setWidth(256);
//		canvas.setHeight(256);
//
//		Context2d context = canvas.getContext2d();
//		ImageData image = context.getImageData( 0, 0, 256, 256 );
//
//		int x = 0, y = 0;
//		for ( int i = 0, j = 0, l = image.getData().getLength(); i < l; i += 4, j ++ )
//		{
//			x = j % 64;
//			y = x == 0 ? y + 1 : y;
//
//			image.getData().set( i, 255);
//			image.getData().set( i + 1, 255);
//			image.getData().set( i + 2, 255);
//			image.getData().set( i + 3, (int)Math.floor( x ^ y ));
//		}
//
//		context.putImageData( image, 0, 0 );
//
//		return canvas;
		return null;
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double timer = 0.0001 * context.getDeltaTime();

		camera.getPosition().setX( Math.cos( timer ) * 1000.0 );
		camera.getPosition().setZ( Math.sin( timer ) * 1000.0 );

		camera.lookAt( scene.getPosition() );

		for ( int i = 0, l = this.objects.size(); i < l; i ++ ) 
		{
			Mesh object = this.objects.get(i);

			object.getRotation().addX(0.01);
			object.getRotation().addY(0.005);

			Material material = this.materials.get( i ); 
			if(i > 9 && material instanceof MeshPhongMaterial)
			{
				((MeshPhongMaterial)material).getEmissive()
					.setHSL( 0.54, 1.0, 0.7 * ( 0.5 + 0.5 * Math.sin( 35 * timer ) ) );	
			}
			else if(i > 9 && material instanceof MeshLambertMaterial)
			{
				((MeshLambertMaterial)material).getEmissive()
					.setHSL( 0.04, 1.0, 0.7 * ( 0.5 + 0.5 * Math.cos( 35 * timer ) ) );	
			}
		}
		
		this.particleLight.getPosition().setX(Math.sin( timer * 7 ) * 300.0 );
		this.particleLight.getPosition().setY(Math.cos( timer * 5 ) * 400.0 );
		this.particleLight.getPosition().setZ(Math.cos( timer * 3 ) * 300.0 );

		this.pointLight.getPosition().setX( particleLight.getPosition().getX() );
		this.pointLight.getPosition().setY( particleLight.getPosition().getY() );
		this.pointLight.getPosition().setZ( particleLight.getPosition().getZ() );
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Canvas 2D texture";
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
