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
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.SceneUtils;
import org.parallax3d.parallax.graphics.extras.core.Path;
import org.parallax3d.parallax.graphics.extras.core.Shape;
import org.parallax3d.parallax.graphics.extras.curves.SplineCurve3;
import org.parallax3d.parallax.graphics.extras.curves.SplineCurve3Closed;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ThreejsExample("webgl_geometry_extrude_shapes")
public class GeometryExtrudeShapes extends ParallaxTest
{
	Scene scene;
	PerspectiveCamera camera;
	Object3D parentObject;
	
	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 50,
				context.getAspectRation(),
				1, 
				1000 
			);
		camera.getPosition().set(0, 150, 150);
		
		DirectionalLight light = new DirectionalLight( 0xffffff );
		light.getPosition().set( 0, 0, 1 );
		scene.add( light );

		this.parentObject = new Object3D();
		this.parentObject.getPosition().setY(50);
		scene.add( this.parentObject );
		
		//Closed
		SplineCurve3 extrudeBend = new SplineCurve3(Arrays.asList(
				new Vector3( 30, 12, 83),
				new Vector3( 40, 20, 67),
				new Vector3( 60, 40, 99),
				new Vector3( 10, 60, 49),
				new Vector3( 25, 80, 40)));

		SplineCurve3 pipeSpline = new SplineCurve3(Arrays.asList(
				new Vector3(0, 10, -10), 
				new Vector3(10, 0, -10), 
				new Vector3(20, 0, 0), 
				new Vector3(30, 0, 10), 
				new Vector3(30, 0, 20), 
				new Vector3(20, 0, 30), 
				new Vector3(10, 0, 30), 
				new Vector3(0, 0, 30), 
				new Vector3(-10, 10, 30), 
				new Vector3(-10, 20, 30), 
				new Vector3(0, 30, 30), 
				new Vector3(10, 30, 30), 
				new Vector3(20, 30, 15), 
				new Vector3(10, 30, 10), 
				new Vector3(0, 30, 10), 
				new Vector3(-10, 20, 10), 
				new Vector3(-10, 10, 10), 
				new Vector3(0, 0, 10), 
				new Vector3(10, -10, 10), 
				new Vector3(20, -15, 10), 
				new Vector3(30, -15, 10), 
				new Vector3(40, -15, 10), 
				new Vector3(50, -15, 10), 
				new Vector3(60, 0, 10), 
				new Vector3(70, 0, 0), 
				new Vector3(80, 0, 0), 
				new Vector3(90, 0, 0),
				new Vector3(100, 0, 0)));

		SplineCurve3Closed sampleClosedSpline = new SplineCurve3Closed(Arrays.asList(
				new Vector3(0, -40, -40),
				new Vector3(0, 40, -40),
				new Vector3(0, 140, -40),
				new Vector3(0, 40, 40),
				new Vector3(0, -40, 40)));

//			List<Vector3> randomPoints = new ArrayList<Vector3>();
//
//			for (int i=0; i<10; i++)
//				randomPoints.add(new Vector3((double)Math.random() * 200.0f, (double)Math.random() * 200.0f, (double)Math.random() * 200.0f ));
//
//			SplineCurve3 randomSpline =  new SplineCurve3(randomPoints);
		
		SplineCurve3 randomSpline = new SplineCurve3(Arrays.asList( 
				new Vector3(-40, -40, 0),
				new Vector3(40, -40, 0),
				new Vector3( 140, -40, 0),
				new Vector3(40, 40, 0),
				new Vector3(-40, 40, 20)));

//			ExtrudeGeometry.ExtrudeGeometryParameters extrudeParameters = new ExtrudeGeometry.ExtrudeGeometryParameters();
//			extrudeParameters.amount = 200;
//			extrudeParameters.bevelEnabled = true;
//			extrudeParameters.bevelSegments = 2;
//			extrudeParameters.steps = 150;
//			extrudeParameters.extrudePath = randomSpline;

		// CircleGeometry

		double circleRadius = 4.0;
		Shape circleShape = new Shape();
		circleShape.moveTo( 0, circleRadius );
		circleShape.quadraticCurveTo( circleRadius, circleRadius, circleRadius, 0 );
		circleShape.quadraticCurveTo( circleRadius, -circleRadius, 0, -circleRadius );
		circleShape.quadraticCurveTo( -circleRadius, -circleRadius, -circleRadius, 0 );
		circleShape.quadraticCurveTo( -circleRadius, circleRadius, 0, circleRadius);

		double rectLength = 12.0;
		double rectWidth = 4.0;

		Shape rectShape = new Shape();

		rectShape.moveTo( -rectLength/2, -rectWidth/2 );
		rectShape.lineTo( -rectLength/2, rectWidth/2 );
		rectShape.lineTo( rectLength/2, rectWidth/2 );
		rectShape.lineTo( rectLength/2, -rectLength/2 );
		rectShape.lineTo( -rectLength/2, -rectLength/2 );

		// Smiley

		Shape smileyShape = new Shape();
		smileyShape.moveTo( 80, 40 );
		smileyShape.arc( 40, 40, 40, 0.0, Math.PI * 2.0, false );

		Path smileyEye1Path = new Path();
		smileyEye1Path.moveTo( 35, 20 );
		smileyEye1Path.arc( 25, 20, 10, 0.0, Math.PI * 2.0, true );
		smileyShape.getHoles().add( smileyEye1Path );

		Path smileyEye2Path = new Path();
		smileyEye2Path.moveTo( 65, 20 );
		smileyEye2Path.arc( 55, 20, 10, 0.0, Math.PI * 2.0, true );
		smileyShape.getHoles().add( smileyEye2Path );

		Path smileyMouthPath = new Path();

		smileyMouthPath.moveTo( 20, 40 );
		smileyMouthPath.quadraticCurveTo( 40, 60, 60, 40 );
		smileyMouthPath.bezierCurveTo( 70, 45, 70, 50, 60, 60 );
		smileyMouthPath.quadraticCurveTo( 40, 80, 20, 60 );
		smileyMouthPath.quadraticCurveTo( 5, 50, 20, 40 );

		smileyShape.getHoles().add( smileyMouthPath );

		List<Vector2> pts = new ArrayList<Vector2>();
		int starPoints = 5;
		double l;
		for (int i = 0; i < starPoints * 2; i++) 
		{
			l = (Mathematics.isEven(i)) ? 5.0 : 10.0;
			double a = i / starPoints * Math.PI;

			pts.add(new Vector2(Math.cos(a) * l, Math.sin(a) * l ));
		}

		Shape starShape = new Shape(pts);
//			ExtrudeGeometry circle3d = starShape.extrude( extrudeParameters ); //circleShape rectShape smileyShape starShape

//			TubeGeometry tubeGeometry = new TubeGeometry((CurvePath) extrudeParameters.extrudePath, 150, 4.0, 5, false, true);     

//			addGeometry( circle3d, new Color(0xff1111),  
//					-100f, 0, 0,     
//					0, 0, 0, 
//					1);

//			addGeometry( tubeGeometry, new Color(0x00ff11),  
//					0, 0, 0,     
//					0, 0, 0, 
//					1);

		context.getRenderer().setClearColor(0xCCCCCC);
	}
	
	private void addGeometry(GLRenderer renderer, Geometry geometry, Color color, double x, double y, double z, double rx, double ry, double rz, double s )
	{
		// 3d shape
		List<Material> materials= new ArrayList<Material>();
		materials.add(new MeshLambertMaterial()
				.setColor(color)
				.setOpacity( 0.2 )
				.setTransparent(true));
		materials.add(new MeshBasicMaterial()
				.setColor( 0x000000 )
				.setWireframe( true )
				.setOpacity( 0.3 ));
		Object3D mesh = SceneUtils.createMultiMaterialObject( geometry, materials );

		mesh.getPosition().set( x, y, z - 75.0 );

		mesh.getScale().set( s );

//			if (geometry.debug) 
//				mesh.add(geometry.debug);

		this.parentObject.add( mesh );
		renderer.render(scene, camera);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
	}

	@Override
	public String getName() {
		return "Geometry - extrude shapes";
	}

	@Override
	public String getDescription() {
		return "Shapes Extrusion via Spline path. (Drag to spin)";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

}
