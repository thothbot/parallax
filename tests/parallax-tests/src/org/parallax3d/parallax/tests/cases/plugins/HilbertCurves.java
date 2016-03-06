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

package org.parallax3d.parallax.tests.cases.plugins;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.BloomPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.Postprocessing;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.RenderPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.ShaderPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders.CopyShader;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HilbertCurves extends ParallaxTest implements TouchMoveHandler
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
				33, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		); 
		
		camera.getPosition().setZ(700);

		Geometry geometry = new Geometry();
		Geometry geometry2 = new Geometry();
		Geometry geometry3 = new Geometry();
		List<Vector3> points = hilbert3D( new Vector3( 0,0,0 ), 200.0, 2, 0, 1, 2, 3, 4, 5, 6, 7 );

		List<Color> colors = new ArrayList<Color>(); 
		List<Color> colors2 = new ArrayList<Color>();
		List<Color> colors3 = new ArrayList<Color>();

		for ( int i = 0; i < points.size(); i ++ ) 
		{
			geometry.getVertices().add( points.get( i ) );

			colors.add( new Color( 0xffffff ) );
			colors.get( i ).setHSL( 0.6, 1.0, Math.max( 0, ( 200 - points.get( i ).getX() ) / 400.0 ) * 0.5 + 0.5);

			colors2.add( new Color( 0xffffff ) );
			colors2.get( i ).setHSL( 0.3, 1.0, Math.max( 0, ( 200 + points.get( i ).getX() ) / 400.0 ) * 0.5 );

			colors3.add( new Color( 0xffffff ) );
			colors3.get( i ).setHSL( i / (double)points.size(), 1.0, 0.5 );
		}

		geometry2.setVertices(geometry.getVertices()); 
		geometry3.setVertices(geometry.getVertices());

		geometry.setColors(colors);
		geometry2.setColors(colors2);
		geometry3.setColors(colors3);

		// lines

		LineBasicMaterial material = new LineBasicMaterial()
				.setColor( 0xffffff )
				.setLinewidth(3)
				.setVertexColors(Material.COLORS.VERTEX)		
				.setOpacity(1.0);

		double scale = 0.3;
		double d = 225;

		material.setVertexColors(Material.COLORS.VERTEX);

		Line line = new Line( geometry,  material );
		line.getScale().set(scale * 1.5);
		line.getPosition().set(-d, 0, 0);
		scene.add( line );
		
		Line line2 = new Line( geometry2,  material );
		line2.getScale().set(scale * 1.5);
		line2.getPosition().set(0, 0, 0);
		scene.add( line2 );
		
		Line line3 = new Line( geometry3,  material );
		line3.getScale().set(scale * 1.5);
		line3.getPosition().set(d, 0, 0);
		scene.add( line3 );
		
		//

		RenderPass renderModel = new RenderPass( scene, camera );
		BloomPass effectBloom = new BloomPass( 1.3 );

		ShaderPass effectCopy = new ShaderPass( new CopyShader() );
		effectCopy.setRenderToScreen(true);

		Postprocessing composer = new Postprocessing( context.getRenderer(), scene );

		composer.addPass( renderModel );
		composer.addPass( effectBloom );
		composer.addPass( effectCopy );

		context.getRenderer().setAutoClear(false);
	}

	/**
	 * Port of Processing Java code by Thomas Diewald
	 * <a href="http://www.openprocessing.org/visuals/?visualID=15599">openprocessing.org</a>
	 */
	private List<Vector3> hilbert3D( Vector3 center, double side, int iterations, 
			int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7 ) 
	{

		double half = side / 2.0;

		List<Vector3> vec_s = Arrays.asList(
				new Vector3( center.getX() - half, center.getY() + half, center.getZ() - half ),
				new Vector3( center.getX() - half, center.getY() + half, center.getZ() + half ),
				new Vector3( center.getX() - half, center.getY() - half, center.getZ() + half ),
				new Vector3( center.getX() - half, center.getY() - half, center.getZ() - half ),
				new Vector3( center.getX() + half, center.getY() - half, center.getZ() - half ),
				new Vector3( center.getX() + half, center.getY() - half, center.getZ() + half ),
				new Vector3( center.getX() + half, center.getY() + half, center.getZ() + half ),
				new Vector3( center.getX() + half, center.getY() + half, center.getZ() - half )
		);

		List<Vector3> vec = Arrays.asList( 
				vec_s.get( v0 ), vec_s.get( v1 ), vec_s.get( v2 ), vec_s.get( v3 ), 
				vec_s.get( v4 ), vec_s.get( v5 ), vec_s.get( v6 ), vec_s.get( v7 ) );

		if( --iterations >= 0 ) 
		{
			List<Vector3> tmp = new ArrayList<Vector3>();
			tmp.addAll(hilbert3D ( vec.get( 0 ), half, iterations, v0, v3, v4, v7, v6, v5, v2, v1 ));
			tmp.addAll(hilbert3D ( vec.get( 1 ), half, iterations, v0, v7, v6, v1, v2, v5, v4, v3 ) );
			tmp.addAll(hilbert3D ( vec.get( 2 ), half, iterations, v0, v7, v6, v1, v2, v5, v4, v3 ) );
			tmp.addAll(hilbert3D ( vec.get( 3 ), half, iterations, v2, v3, v0, v1, v6, v7, v4, v5 ) );
			tmp.addAll(hilbert3D ( vec.get( 4 ), half, iterations, v2, v3, v0, v1, v6, v7, v4, v5 ) );
			tmp.addAll(hilbert3D ( vec.get( 5 ), half, iterations, v4, v3, v2, v5, v6, v1, v0, v7 ) );
			tmp.addAll(hilbert3D ( vec.get( 6 ), half, iterations, v4, v3, v2, v5, v6, v1, v0, v7 ) );
			tmp.addAll(hilbert3D ( vec.get( 7 ), half, iterations, v6, v5, v2, v1, v0, v3, v4, v7 ) );

			return tmp;
		}

		return vec;
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * .05 );
		camera.getPosition().addY( ( - mouseY + 200.0 - camera.getPosition().getY() ) * .05 );

		camera.lookAt( scene.getPosition() );

		for ( int i = 0; i < scene.getChildren().size(); i ++ ) 
		{
			Object3D object = scene.getChildren().get(i);
			if ( object instanceof Line ) 
				object.getRotation().setY( context.getDeltaTime() * 0.0005 * ( (i % 2 > 0) ? 1.0 : -1.0 ) );
		}

		context.getRenderer().clear();
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 ) * 3;
		mouseY = (screenY - height / 2) * 3;
	}

	@Override
	public String getName() {
		return "Hilbert curves";
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
