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
import org.parallax3d.parallax.graphics.extras.SceneUtils;
import org.parallax3d.parallax.graphics.extras.geometries.parametric.KleinParametricGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.parametric.MobiusParametricGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.parametric.PlaneParametricGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_geometries2")
public class GeometriesParametric extends ParallaxTest
{

	static final String image = "textures/UV_Grid_Sm.jpg";

	PerspectiveCamera camera;
	Scene scene;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();

		camera = new PerspectiveCamera( 45,
				context.getAspectRation(),
				1,
				2000
		);

		camera.getPosition().setY(400);

		scene.add( new AmbientLight( 0x404040 ) );

		DirectionalLight light = new DirectionalLight( 0xffffff );
		light.getPosition().set( 0, 0, 1 );
		scene.add( light );

		Texture texture = new Texture(image);
		texture.setWrapS(TextureWrapMode.REPEAT);
		texture.setWrapT(TextureWrapMode.REPEAT);
		texture.setAnisotropy(16);

		List<Material> materials = new ArrayList<Material>();
		MeshLambertMaterial lmaterial = new MeshLambertMaterial()
				.setMap( texture )
				.setAmbient( 0xbbbbbb )
				.setSide(Material.SIDE.DOUBLE);
		materials.add(lmaterial);


		MeshBasicMaterial bmaterial = new MeshBasicMaterial()
				.setColor( 0xffffff )
				.setWireframe( true )
				.setTransparent(true)
				.setOpacity( 0.1 )
				.setSide(Material.SIDE.DOUBLE);
		materials.add(bmaterial);

		// KleinParametricGeometry Bottle
		Object3D object1 = SceneUtils.createMultiMaterialObject(new KleinParametricGeometry(20, 20), materials );
		object1.getPosition().set( 0, 0, 0 );
		object1.getScale().multiply(20);
		scene.add( object1 );

		// MobiusParametricGeometry Strip
		Object3D object2 = SceneUtils.createMultiMaterialObject( new MobiusParametricGeometry(20, 20), materials );
		object2.getPosition().set( 10, 0, 0 );
		object2.getScale().multiply(100);
		scene.add( object2 );

		Object3D object3 = SceneUtils.createMultiMaterialObject( new PlaneParametricGeometry(200, 200, 10, 20), materials );
		object3.getPosition().set( 20, 0, 0 );
		scene.add( object3 );

//			DimensionalObject object4 = SceneUtils.createMultiMaterialObject( new Mobius3dParametricGeometry(20,20), materials );
//			object4.getPosition().set( 10, 0, 0 );
//			object4.getScale().multiply(100);
//			scene.addChild( object4 );
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().setX(Math.cos( context.getDeltaTime() * 0.0001 ) * 800.0);
		camera.getPosition().setZ(Math.sin( context.getDeltaTime() * 0.0001 ) * 800.0);

		camera.lookAt( scene.getPosition() );

		for ( int i = 0, l = scene.getChildren().size(); i < l; i ++ )
		{
			Object3D object = scene.getChildren().get( i );

			object.getRotation().addX(0.01);
			object.getRotation().addY(0.005);
		}

		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Parametric geometry";
	}

	@Override
	public String getDescription() {
		return "Here are show how to generate geometric objects by custom function.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
