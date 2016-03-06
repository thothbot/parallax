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
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.SceneUtils;
import org.parallax3d.parallax.graphics.extras.geometries.IcosahedronGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_geometry_colors")
public class GeometryColors extends ParallaxTest implements TouchMoveHandler
{

	static final String texture = "textures/shadow.png";

	Scene scene;

	PerspectiveCamera camera;
	
	int mouseX;
	int mouseY;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 20,
				context.getAspectRation(),
				1, 
				10000 
		);
		
		camera.getPosition().setZ(1800);
		
		DirectionalLight light = new DirectionalLight( 0xffffff );
		light.getPosition().set( 0, 0, 1 );
		scene.add( light );
		
		MeshBasicMaterial shadowMaterial = new MeshBasicMaterial().setMap( new Texture(texture) );
		PlaneBufferGeometry shadowGeo = new PlaneBufferGeometry( 300, 300, 1, 1 );
		
		Mesh mesh1 = new Mesh( shadowGeo, shadowMaterial );
		mesh1.getPosition().setY(-250);
		mesh1.getRotation().setX(- Math.PI / 2.0);
		scene.add( mesh1 );

		Mesh mesh2 = new Mesh( shadowGeo, shadowMaterial );
		mesh2.getPosition().setY(-250);
		mesh2.getPosition().setX(-400);
		mesh2.getRotation().setX(- Math.PI / 2.0);
		scene.add( mesh2 );

		Mesh mesh3 = new Mesh( shadowGeo, shadowMaterial );
		mesh3.getPosition().setY(-250);
		mesh3.getPosition().setX(400);
		mesh3.getRotation().setX(- Math.PI / 2.0);
		scene.add( mesh3 );
		
		int radius = 200;
		
		Geometry geometry  = new IcosahedronGeometry( radius, 1 );
		Geometry geometry2 = new IcosahedronGeometry( radius, 1 );
		Geometry geometry3 = new IcosahedronGeometry( radius, 1 );
		
		for ( int i = 0; i < geometry.getFaces().size(); i ++ ) 
		{
			Face3 f  = geometry.getFaces().get( i );

			int n = 3;

			for( int j = 0; j < n; j++ ) 
			{
				int vertexIndex = (j == 0 ) ? f.getA()
						: (j == 1 ) ? f.getB()
						: (j == 2 ) ? f.getC() : 0;

				Vector3 p = geometry.getVertices().get( vertexIndex );

				Color color = new Color( 0xffffff );
				color.setHSL( ( p.getY() / (double)radius + 1.0 ) / 2.0, 1.0, 0.5 );

				geometry.getFaces().get( i ).getVertexColors().add(color);

				Color color2 = new Color( 0xffffff );
				color2.setHSL( 0.0, ( p.getY() / (double)radius + 1.0 ) / 2.0, 0.5 );

				geometry2.getFaces().get( i ).getVertexColors().add(color2);

				Color color3 = new Color( 0xffffff );
				color3.setHSL( (0.125 * vertexIndex / (double)geometry.getVertices().size()), 1.0, 0.5 );

				geometry3.getFaces().get( i ).getVertexColors().add(color3);
			}
		}

		List<Material> materials = new ArrayList<Material>();
		materials.add(new MeshLambertMaterial()
				.setColor( 0xffffff )
				.setShading( Material.SHADING.FLAT )
				.setVertexColors( Material.COLORS.VERTEX ));

		materials.add(new MeshBasicMaterial()
				.setColor( 0x000000 )
				.setShading( Material.SHADING.FLAT )
				.setWireframe(true)
				.setTransparent( true ));

		Object3D group1 = SceneUtils.createMultiMaterialObject( geometry, materials );
		group1.getPosition().setX(-400);
		group1.getRotation().setX(-1.87);
		scene.add( group1 );

		Object3D group2 = SceneUtils.createMultiMaterialObject( geometry2, materials );
		group2.getPosition().setX(400);
		group2.getRotation().setX(0);
		scene.add( group2 );

		Object3D group3 = SceneUtils.createMultiMaterialObject( geometry3, materials );
		group3.getPosition().setX(0);
		group3.getRotation().setX(0);
		scene.add( group3 );

		context.getRenderer().setClearColor(0xDDDDDD);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().addX(( - mouseX - camera.getPosition().getX()) * 0.05 );
		camera.getPosition().addY(( mouseY - camera.getPosition().getY()) * 0.05 );

		camera.lookAt( scene.getPosition());
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = screenX;
		mouseY = screenY;
	}

	@Override
	public String getName() {
		return "Vertices colors";
	}

	@Override
	public String getDescription() {
		return "Here are shown Icosahedrons and different vertex colors. At the bottom located shadow texture. Drag mouse to move. ";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
	
}
