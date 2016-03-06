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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_materials2")
public final class MaterialsTextures extends ParallaxTest 
{
	
	private static final String img = "textures/lava/lavatile.jpg";
	private static final String img2 = "textures/planets/moon_1024.jpg";

	Scene scene;
	PerspectiveCamera camera;
	List<Mesh> objects;
	Mesh particleLight;

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
		
		camera.getPosition().setY(200);
		
		objects = new ArrayList<Mesh>();

		Texture imgTexture2 = new Texture( img2 );
		imgTexture2.setWrapS(TextureWrapMode.REPEAT);
		imgTexture2.setWrapT(TextureWrapMode.REPEAT);
		imgTexture2.setAnisotropy(16);

		Texture imgTexture = new Texture( img );
		imgTexture.setRepeat(new Vector2(4, 2));
		imgTexture.setWrapS(TextureWrapMode.REPEAT);
		imgTexture.setWrapT(TextureWrapMode.REPEAT);
		imgTexture.setAnisotropy(16);

		double shininess = 50;
		Color specular = new Color(0x333333);
		int bumpScale = 1;
		Material.SHADING shading = Material.SHADING.SMOOTH;
		
		List<Material> materials = new ArrayList<Material>();

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture)
				.setBumpMap(imgTexture)
				.setBumpScale(bumpScale)
				.setColor(0xffffff)
				.setAmbient(0x777777)
				.setSpecular(specular)
				.setShininess(shininess)
				.setShading(shading));

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture)
				.setBumpMap(imgTexture)
				.setBumpScale(bumpScale)
				.setColor(0x00ff00)
				.setAmbient(0x777777)
				.setSpecular(specular)
				.setShininess(shininess)
				.setShading(shading));

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture)
				.setBumpMap(imgTexture)
				.setBumpScale(bumpScale)
				.setColor(0x00ff00)
				.setAmbient(0x007700)
				.setSpecular(specular)
				.setShininess(shininess)
				.setShading(shading));

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture)
				.setBumpMap(imgTexture)
				.setBumpScale(bumpScale)
				.setColor(0x000000)
				.setAmbient(0x00ff00)
				.setSpecular(specular)
				.setShininess(shininess)
				.setShading(shading));

		materials.add( new MeshLambertMaterial()
				.setMap(imgTexture)
				.setColor(0xffffff)
				.setAmbient(0x777777)
				.setShading(shading));

		materials.add( new MeshLambertMaterial()
				.setMap(imgTexture)
				.setColor(0xff0000)
				.setAmbient(0x777777)
				.setShading(shading));

		materials.add( new MeshLambertMaterial()
				.setMap(imgTexture)
				.setColor(0xff0000)
				.setAmbient(0x770000)
				.setShading(shading));

		materials.add( new MeshLambertMaterial()
				.setMap(imgTexture)
				.setColor(0x000000)
				.setAmbient(0xff0000)
				.setShading(shading));

		shininess = 15;

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture2)
				.setBumpMap(imgTexture2)
				.setBumpScale(bumpScale)
				.setColor(0x000000)
				.setAmbient(0x000000)
				.setSpecular(0xffaa00)
				.setShininess(shininess)
				.setMetal(true)
				.setShading(shading));

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture2)
				.setBumpMap(imgTexture2)
				.setBumpScale(bumpScale)
				.setColor(0x000000)
				.setAmbient(0x000000)
				.setSpecular(0xaaff00)
				.setShininess(shininess)
				.setMetal(true)
				.setShading(shading));

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture2)
				.setBumpMap(imgTexture2)
				.setBumpScale(bumpScale)
				.setColor(0x000000)
				.setAmbient(0x000000)
				.setSpecular(0x00ffaa)
				.setShininess(shininess)
				.setMetal(true)
				.setShading(shading));

		materials.add( new MeshPhongMaterial()
				.setMap(imgTexture2)
				.setBumpMap(imgTexture2)
				.setBumpScale(bumpScale)
				.setColor(0x000000)
				.setAmbient(0x000000)
				.setSpecular(0x00aaff)
				.setShininess(shininess)
				.setMetal(true)
				.setShading(shading));

		// Spheres geometry

		SphereGeometry geometry_smooth = new SphereGeometry( 70, 32, 16 );
		SphereGeometry geometry_flat = new SphereGeometry( 70, 32, 16 );

		for ( int i = 0, l = materials.size(); i < l; i ++ ) 
		{
			Material material = materials.get( i );
			SphereGeometry geometry = (((HasShading)material).getShading() == Material.SHADING.FLAT) ? geometry_flat : geometry_smooth;
			Mesh sphere = new Mesh( geometry, material );

			sphere.getPosition().setX( ( i % 4 ) * 200 - 200 );
			sphere.getPosition().setZ( Math.floor( i / 4 ) * 200 - 200 );

			objects.add( sphere );

			scene.add( sphere );
		}

		MeshBasicMaterial particleLightMaterial = new MeshBasicMaterial();
		particleLightMaterial.setColor(new Color(0xffffff));
		particleLight = new Mesh( new SphereGeometry( 4, 8, 8 ), particleLightMaterial );
		scene.add( particleLight );

		// Lights

		scene.add( new AmbientLight( 0x444444 ) );

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 1 );
		directionalLight.getPosition().set( 1 ).normalize();
		scene.add( directionalLight );

		PointLight pointLight = new PointLight( 0xffffff, 2, 800 );
		scene.add( pointLight );

		((MeshBasicMaterial)particleLight.getMaterial()).setColor( pointLight.getColor() );
		pointLight.setPosition( particleLight.getPosition() );

		//
		context.getRenderer().setClearColor(0x0a0a0a);
		context.getRenderer().setSortObjects(true);
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double timer = context.getDeltaTime() * 0.00025;

		camera.getPosition().setX( Math.cos( timer ) * 800 );
		camera.getPosition().setZ( Math.sin( timer ) * 800 );

		camera.lookAt( scene.getPosition() );

		for ( int i = 0, l = objects.size(); i < l; i ++ ) 
		{
			Mesh object = objects.get( i );

			object.getRotation().addY( 0.005 );
		}

		particleLight.getPosition().setX( Math.sin( timer * 7 ) * 300 );
		particleLight.getPosition().setY( Math.cos( timer * 5 ) * 400 );
		particleLight.getPosition().setZ( Math.cos( timer * 3 ) * 300 );
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Textures";
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
