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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.geometries.*;
import org.parallax3d.parallax.graphics.extras.helpers.ArrowHelper;
import org.parallax3d.parallax.graphics.extras.helpers.AxisHelper;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_geometries")
public class Geometries extends ParallaxTest
{
	static final String image = "textures/UV_Grid_Sm.jpg";

	Scene scene;
	PerspectiveCamera camera;

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
		light.getPosition().set( 0, 1, 0 );
		scene.add( light );

		Texture texture = new Texture(image);
		texture.setWrapS(TextureWrapMode.REPEAT);
		texture.setWrapT(TextureWrapMode.REPEAT);
		texture.setAnisotropy(16);

		MeshLambertMaterial material = new MeshLambertMaterial()
				.setMap( texture )
				.setAmbient( 0xbbbbbb )
				.setSide(Material.SIDE.DOUBLE);

		Object3D object1 = new Mesh( new SphereGeometry( 75, 20, 10 ), material );
		object1.getPosition().set( -400, 0, 200 );
		scene.add( object1 );

		Object3D object2 = new Mesh(  new IcosahedronGeometry( 75, 1 ), material );
		object2.getPosition().set( -200, 0, 200 );
		scene.add( object2 );

		Object3D object3 = new Mesh( new OctahedronGeometry( 75, 2 ), material );
		object3.getPosition().set( 0, 0, 200 );
		scene.add( object3 );

		Object3D object4 = new Mesh( new TetrahedronGeometry( 75, 0 ), material );
		object4.getPosition().set( 200, 0, 200 );
		scene.add( object4 );

		//

		Object3D object5 = new Mesh( new PlaneGeometry( 100, 100, 4, 4 ), material );
		object5.getPosition().set( -400, 0, 0 );
		scene.add( object5 );

		Object3D object6 = new Mesh( new BoxGeometry( 100, 100, 100, 4, 4, 4 ), material );
		object6.getPosition().set( -200, 0, 0 );
		scene.add( object6 );

		Object3D object7 = new Mesh( new CircleGeometry( 50, 20, 0, Math.PI * 2 ), material );
		object7.getPosition().set( 0, 0, 0 );
		scene.add( object7 );

		Object3D object8 = new Mesh( new RingGeometry( 10, 50, 20, 5, 0, Math.PI * 2 ), material );
		object8.getPosition().set( 200, 0, 0 );
		scene.add( object8 );

		Object3D object9 = new Mesh( new CylinderGeometry( 25, 75, 100, 40, 5 ), material );
		object9.getPosition().set( 400, 0, 0 );
		scene.add( object9 );

		List<Vector3> points = new ArrayList<Vector3>();

		for ( int i = 0; i < 50; i ++ )
		{
			points.add( new Vector3( Math.sin( i * 0.2 ) * Math.sin( i * 0.1 ) * 15.0 + 50.0, 0.0, ( i - 5.0 ) * 2.0 )  );
		}

		Object3D object10 = new Mesh( new LatheGeometry( points, 20 ), material );
		object10.getPosition().set( -400, 0, -200 );
		scene.add( object10 );

		Object3D object11 = new Mesh( new TorusGeometry( 50, 20, 20, 20 ), material );
		object11.getPosition().set( -200, 0, -200 );
		scene.add( object11 );

		Object3D object12 = new Mesh( new TorusKnotGeometry( 50, 10, 50, 20 ), material );
		object12.getPosition().set( 0, 0, -200 );
		scene.add( object12 );

		AxisHelper object13 = new AxisHelper();
		object13.getPosition().set( 200, 0, -200 );
		scene.add( object13 );

		ArrowHelper object14 = new ArrowHelper( new Vector3( 0, 1, 0 ), new Vector3( 0, 0, 0 ), 50 );
		object14.getPosition().set( 400, 0, -200 );
		scene.add( object14 );
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
		return "Different geometries";
	}

	@Override
	public String getDescription() {
		return "Here are used pull of some geometric objects and two materials: mesh basic and lambert. ";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
