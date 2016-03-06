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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_lines_sphere")
public final class LinesSphere extends ParallaxTest implements TouchMoveHandler
{

	class ExampleData 
	{
		double scale;
		Color color;
		double opasity;
		double lineWidth;
		
		public ExampleData(double scale, int color, double opasity, double lineWidth)
		{
			this.scale = scale;
			this.color = new Color(color);
			this.opasity = opasity;
			this.lineWidth = lineWidth;
		}
	}

	Scene scene;
	PerspectiveCamera camera;

	int width = 0, height = 0;
	int mouseX;
	int mouseY;
	
	Map<Line, Double> originalScale;

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
				80, // fov
				context.getAspectRation(), // aspect
				1, // near
				3000 // far 
		);
		
		camera.getPosition().setZ(1000);

		List<ExampleData> parameters = new ArrayList<ExampleData>();
		parameters.add(new ExampleData( 0.25, 0xff7700, 1.00, 2));
		parameters.add(new ExampleData( 0.50, 0xff9900, 1.00, 1));
		parameters.add(new ExampleData( 0.75, 0xffaa00, 0.75, 1));
		parameters.add(new ExampleData( 1.00, 0xffaa00, 0.50, 1));
		parameters.add(new ExampleData( 1.25, 0x000833, 0.80, 1));
		parameters.add(new ExampleData( 3.00, 0xaaaaaa, 0.75, 2));
		parameters.add(new ExampleData( 3.50, 0xffffff, 0.50, 1));
		parameters.add(new ExampleData( 4.50, 0xffffff, 0.25, 1));
		parameters.add(new ExampleData( 5.50, 0xffffff, 0.125, 1 ));

		Geometry geometry = new Geometry();


		for ( int i = 0; i < 1500; i ++ ) 
		{
			Vector3 vertex1 = new Vector3();
			vertex1.setX(Math.random() * 2.0 - 1.0);
			vertex1.setY(Math.random() * 2.0 - 1.0);
			vertex1.setZ(Math.random() * 2.0 - 1.0);
			vertex1.normalize();
			vertex1.multiply( 450 );

			Vector3 vertex2 = vertex1.clone();
			vertex2.multiply( Math.random() * 0.09 + 1.0 );

			geometry.getVertices().add( vertex1 );
			geometry.getVertices().add( vertex2 );
		}

		this.originalScale = new HashMap<Line, Double>();
		
		for( int i = 0; i < parameters.size(); ++i ) 
		{
			ExampleData p = parameters.get(i);

			LineBasicMaterial material = new LineBasicMaterial()
					.setColor(p.color)
					.setLinewidth(p.lineWidth)
					.setOpacity(p.opasity);

			Line line = new Line( geometry, material, Line.MODE.PIECES );
			line.getScale().set( p.scale );
			this.originalScale.put(line, p.scale);

			line.getRotation().setY( Math.random() * Math.PI );
			line.updateMatrix();
			scene.add( line );
		}
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().addY( ( - mouseY + 200.0 - camera.getPosition().getY() ) * .05 );
		camera.lookAt( scene.getPosition() );

		double time = context.getDeltaTime() * 0.0001;

		for ( int i = 0; i < scene.getChildren().size(); i ++ ) 
		{
			Object3D object = scene.getChildren().get(i);

			if ( object instanceof Line ) 
			{
				object.getRotation().setY( time * ( i < 4 ? ( i + 1.0 ) : - ( i + 1.0 ) ) );

				if ( i < 5 ) 
					object.getScale().set(originalScale.get(object) * (i / 5.0 + 1.0) * (1.0 + 0.5 * Math.sin( 7.0 * time ) ));
			}
		}
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 );
		mouseY = (screenY - height / 2);
	}

	@Override
	public String getName() {
		return "Spheres in lines";
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
