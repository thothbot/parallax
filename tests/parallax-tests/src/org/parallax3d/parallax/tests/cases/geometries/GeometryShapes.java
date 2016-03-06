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
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.SceneUtils;
import org.parallax3d.parallax.graphics.extras.core.ExtrudeGeometry;
import org.parallax3d.parallax.graphics.extras.core.Path;
import org.parallax3d.parallax.graphics.extras.core.Shape;
import org.parallax3d.parallax.graphics.extras.curves.SplineCurve3;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.PointCloudMaterial;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ThreejsExample("webgl_geometry_shapes")
public final class GeometryShapes extends ParallaxTest implements TouchMoveHandler
{
	Scene scene;
	PerspectiveCamera camera ;
	
	Object3D parent;

	int width = 0;
	int mouseX = 0;

	@Override
	public void onResize(RenderingContext context) {
		width = context.getWidth();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				50, // fov
				context.getAspectRation(), // aspect
				1, // near
				1000 // far 
		);
		
		camera.getPosition().set( 0, 180, 500 );
		
		DirectionalLight light = new DirectionalLight( 0xffffff );
		light.getPosition().set( 0, 0, 1 );
		scene.add( light );

		this.parent = new Object3D();
		this.parent.getPosition().setY( 50 );
		scene.add( parent );
		
		ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings = new ExtrudeGeometry.ExtrudeGeometryParameters();
		extrudeSettings.amount = 20;
		extrudeSettings.bevelEnabled = true;
		extrudeSettings.bevelSegments = 2;
		extrudeSettings.steps = 2;
		
		triangle(extrudeSettings);
		square(extrudeSettings);
		circle(extrudeSettings);
		arcCircle(extrudeSettings);
		heart(extrudeSettings);
		roundedRectangle(extrudeSettings);
		fish(extrudeSettings);
		smile(extrudeSettings);
		splineShape(extrudeSettings);
		california(extrudeSettings);

		context.getRenderer().setClearColor(0xf0f0f0);
	}
	
	private void splineShape(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		List<Vector2> splinepts = new ArrayList<Vector2>();
		splinepts.add( new Vector2 ( 350, 100 ) );
		splinepts.add( new Vector2 ( 400, 450 ) );
		splinepts.add( new Vector2 ( -140, 350 ) );
		splinepts.add( new Vector2 ( 0, 0 ) );

		Shape splineShape = new Shape();
		splineShape.moveTo( 0, 0 );
		splineShape.splineThru( splinepts );
	
		SplineCurve3 apath = new SplineCurve3();
		apath.points.add(new Vector3(-50, 150, 10));
		apath.points.add(new Vector3(-20, 180, 20));
		apath.points.add(new Vector3(40, 220, 50));
		apath.points.add(new Vector3(200, 290, 100));

		ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings1 = new ExtrudeGeometry.ExtrudeGeometryParameters(); 
		extrudeSettings1.extrudePath = apath;
		extrudeSettings1.steps = 20;

		addGeometry(
				splineShape.extrude( extrudeSettings1 ), 
				splineShape.createPointsGeometry(), 
				splineShape.createSpacedPointsGeometry(),
				new Color(0x888888), -50, -100, -50, 0, 0, 0, 0.2 );
	}
	
	private void california(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		List<Vector2> californiaPts = new ArrayList<Vector2>();

		californiaPts.add( new Vector2 ( 610, 320 ) );
		californiaPts.add( new Vector2 ( 450, 300 ) );
		californiaPts.add( new Vector2 ( 392, 392 ) );
		californiaPts.add( new Vector2 ( 266, 438 ) );
		californiaPts.add( new Vector2 ( 190, 570 ) );
		californiaPts.add( new Vector2 ( 190, 600 ) );
		californiaPts.add( new Vector2 ( 160, 620 ) );
		californiaPts.add( new Vector2 ( 160, 650 ) );
		californiaPts.add( new Vector2 ( 180, 640 ) );
		californiaPts.add( new Vector2 ( 165, 680 ) );
		californiaPts.add( new Vector2 ( 150, 670 ) );
		californiaPts.add( new Vector2 (  90, 737 ) );
		californiaPts.add( new Vector2 (  80, 795 ) );
		californiaPts.add( new Vector2 (  50, 835 ) );
		californiaPts.add( new Vector2 (  64, 870 ) );
		californiaPts.add( new Vector2 (  60, 945 ) );
		californiaPts.add( new Vector2 ( 300, 945 ) );
		californiaPts.add( new Vector2 ( 300, 743 ) );
		californiaPts.add( new Vector2 ( 600, 473 ) );
		californiaPts.add( new Vector2 ( 626, 425 ) );
		californiaPts.add( new Vector2 ( 600, 370 ) );
		californiaPts.add( new Vector2 ( 610, 320 ) );

		Shape californiaShape = new Shape( californiaPts );

		extrudeSettings.bevelEnabled = false;
		extrudeSettings.steps = 20;
		
		addGeometry( 
				new ExtrudeGeometry( californiaShape, extrudeSettings ), 
				californiaShape.createPointsGeometry(), 
				californiaShape.createSpacedPointsGeometry( 100 ),	
				new Color(0xffaa00), -300, -100, 0, 0, 0, 0, 0.25 );
	}

	private void triangle(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		Shape triangleShape = new Shape();
		triangleShape.moveTo(  80, 20 );
		triangleShape.lineTo(  40, 80 );
		triangleShape.lineTo( 120, 80 );
		triangleShape.lineTo(  80, 20 ); // close path

		addGeometry(
				triangleShape.extrude( extrudeSettings ), 
				triangleShape.createPointsGeometry(), 
				triangleShape.createSpacedPointsGeometry(), 
				new Color(0xffee00), -180, 0, 0, 0, 0, 0, 1 );
	}

	private void square(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		int sqLength = 80;

		Shape squareShape = new Shape();
		squareShape.moveTo( 0,0 );
		squareShape.lineTo( 0, sqLength );
		squareShape.lineTo( sqLength, sqLength );
		squareShape.lineTo( sqLength, 0 );
		squareShape.lineTo( 0, 0 );
		
		addGeometry(
				squareShape.extrude( extrudeSettings ), 
				squareShape.createPointsGeometry(), 
				squareShape.createSpacedPointsGeometry(),
				new Color(0x0055ff), 150, 100, 0, 0, 0, 0, 1 );
	}

	private void circle(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		int circleRadius = 40;
		Shape circleShape = new Shape();
		circleShape.moveTo( 0, circleRadius );
		circleShape.quadraticCurveTo( circleRadius, circleRadius, circleRadius, 0 );
		circleShape.quadraticCurveTo( circleRadius, -circleRadius, 0, -circleRadius );
		circleShape.quadraticCurveTo( -circleRadius, -circleRadius, -circleRadius, 0 );
		circleShape.quadraticCurveTo( -circleRadius, circleRadius, 0, circleRadius );
		
		addGeometry(
				circleShape.extrude( extrudeSettings ), 
				circleShape.createPointsGeometry(), 
				circleShape.createSpacedPointsGeometry(),				
				new Color(0x00ff11), 120, 250, 0, 0, 0, 0, 1 );
	}

	private void arcCircle(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		Shape arcShape = new Shape();
		arcShape.moveTo( 50, 10 );
		arcShape.absarc( 10, 10, 40, 0, Math.PI*2, false );

		Path holePath = new Path();
		holePath.moveTo( 20, 10 );
		holePath.absarc( 10, 10, 10, 0, Math.PI*2, true );
		arcShape.getHoles().add( holePath );
		
		addGeometry( 
				arcShape.extrude( extrudeSettings ), 
				arcShape.createPointsGeometry(), 
				arcShape.createSpacedPointsGeometry(),
				new Color(0xbb4422), 150, 0, 0, 0, 0, 0, 1 );
	}

	private void heart(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		// From http://blog.burlock.org/html5/130-paths

		int x = 0, y = 0;

		Shape heartShape = new Shape(); 

		heartShape.moveTo( x + 25, y + 25 );
		heartShape.bezierCurveTo( x + 25, y + 25, x + 20, y, x, y );
		heartShape.bezierCurveTo( x - 30, y, x - 30, y + 35,x - 30,y + 35 );
		heartShape.bezierCurveTo( x - 30, y + 55, x - 10, y + 77, x + 25, y + 95 );
		heartShape.bezierCurveTo( x + 60, y + 77, x + 80, y + 55, x + 80, y + 35 );
		heartShape.bezierCurveTo( x + 80, y + 35, x + 80, y, x + 50, y );
		heartShape.bezierCurveTo( x + 35, y, x + 25, y + 25, x + 25, y + 25 );

		addGeometry( 
				heartShape.extrude( extrudeSettings ), 
				heartShape.createPointsGeometry(),
				heartShape.createSpacedPointsGeometry(),	
				new Color(0xff1100), 0, 100, 0, Math.PI, 0, 0, 1 );
	}

	private void fish(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		int x = 0, y = 0;
		
		Shape fishShape = new Shape();

		fishShape.moveTo(x, y);
		fishShape.quadraticCurveTo(x + 50, y - 80, x + 90, y - 10);
		fishShape.quadraticCurveTo(x + 100, y - 10, x + 115, y - 40);
		fishShape.quadraticCurveTo(x + 115, y, x + 115, y + 40);
		fishShape.quadraticCurveTo(x + 100, y + 10, x + 90, y + 10);
		fishShape.quadraticCurveTo(x + 50, y + 80, x, y);
		
		addGeometry( 
				fishShape.extrude( extrudeSettings ), 
				fishShape.createPointsGeometry(),
				fishShape.createSpacedPointsGeometry(),
				new Color(0x222222), -60, 200, 0, 0, 0, 0, 1 );
	}
	
	private void smile(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		Shape smileyShape = new Shape();
		smileyShape.moveTo( 80, 40 );
		smileyShape.absarc( 40, 40, 40, 0, Math.PI*2, false );

		Path smileyEye1Path = new Path();
		smileyEye1Path.moveTo( 35, 20 );
		smileyEye1Path.absellipse( 25, 20, 10, 10, 0, Math.PI*2, true );
		smileyShape.getHoles().add( smileyEye1Path );

		Path smileyEye2Path = new Path();
		smileyEye2Path.moveTo( 65, 20 );
		smileyEye2Path.absarc( 55, 20, 10, 0, Math.PI*2, true );
		smileyShape.getHoles().add( smileyEye2Path );

		Path smileyMouthPath = new Path();

		smileyMouthPath.moveTo( 20, 40 );
		smileyMouthPath.quadraticCurveTo( 40, 60, 60, 40 );
		smileyMouthPath.bezierCurveTo( 70, 45, 70, 50, 60, 60 );
		smileyMouthPath.quadraticCurveTo( 40, 80, 20, 60 );
		smileyMouthPath.quadraticCurveTo( 5, 50, 20, 40 );

		smileyShape.getHoles().add( smileyMouthPath );
		
		addGeometry( 
				smileyShape.extrude( extrudeSettings ), 
				smileyShape.createPointsGeometry(), 
				smileyShape.createSpacedPointsGeometry(),
				new Color(0xee00ff), -270, 250, 0, Math.PI, 0, 0, 1 );
	}
	
	private void roundedRectangle(ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings)
	{
		Shape roundedRectShape = new Shape();
		roundedRect( roundedRectShape, 0, 0, 50, 50, 20 );

		addGeometry( 
				roundedRectShape.extrude( extrudeSettings ), 
				roundedRectShape.createPointsGeometry(), 
				roundedRectShape.createSpacedPointsGeometry(),	
				new Color(0x005500), -150, 150, 0, 0, 0, 0, 1 );
	}
	
	private void roundedRect( Shape ctx, double x, double y, double width, double height, double radius )
	{
		ctx.moveTo( x, y + radius );
		ctx.lineTo( x, y + height - radius );
		ctx.quadraticCurveTo( x, y + height, x + radius, y + height );
		ctx.lineTo( x + width - radius, y + height) ;
		ctx.quadraticCurveTo( x + width, y + height, x + width, y + height - radius );
		ctx.lineTo( x + width, y + radius );
		ctx.quadraticCurveTo( x + width, y, x + width - radius, y );
		ctx.lineTo( x + radius, y );
		ctx.quadraticCurveTo( x, y, x, y + radius );
	}
	
	private void addGeometry(Geometry geometry, Geometry points, Geometry spacedPoints, Color color,
							 double x, double y, double z, double rx, double ry, double rz, double s
	) {

		// 3d shape

		MeshLambertMaterial meshMat1 = new MeshLambertMaterial().setColor( color );

		MeshBasicMaterial meshMat2 = new MeshBasicMaterial()
				.setColor(color)
				.setWireframe(true)
				.setTransparent(true);
		
		Object3D mesh = SceneUtils.createMultiMaterialObject( geometry, Arrays.asList( meshMat1 ) );
		
		mesh.getPosition().set( x, y, z - 75.0 );
		mesh.getRotation().set( rx, ry, rz );
		mesh.getScale().set( s );
		this.parent.add( mesh );

		// solid line

		LineBasicMaterial line1Mat = new LineBasicMaterial()
				.setColor(color)
				.setLinewidth(2);

		Line line1 = new Line( points, line1Mat );
		line1.getPosition().set( x, y, z + 25.0 );
		line1.getRotation().set( rx, ry, rz );
		line1.getScale().set( s );
		this.parent.add( line1 );

		// transparent line from real points

		LineBasicMaterial line2Mat = new LineBasicMaterial()
				.setColor(color)
				.setOpacity(0.5);
		
		Line line2 = new Line( points, line2Mat );
		line2.getPosition().set( x, y, z + 75.0 );
		line2.getRotation().set( rx, ry, rz );
		line2.getScale().set( s );
		this.parent.add( line2 );

		// vertices from real points

		PointCloudMaterial particleMat = new PointCloudMaterial()
				.setColor(color)
				.setSize(2)
				.setOpacity(0.75);
		
		Geometry pgeo1 = points.clone();
		PointCloud particles = new PointCloud( pgeo1, particleMat );
		particles.getPosition().set( x, y, z + 75.0 );
		particles.getRotation().set( rx, ry, rz );
		particles.getScale().set( s );
		this.parent.add( particles );

		// transparent line from equidistance sampled points

		LineBasicMaterial line3Mat = new LineBasicMaterial()
				.setColor(color)
				.setOpacity(0.2);
		
		Line line3 = new Line( spacedPoints, line3Mat );
		line3.getPosition().set( x, y, z + 100.0 );
		line3.getRotation().set( rx, ry, rz );
		line3.getScale().set( s );
		this.parent.add( line3 );

		// equidistance sampled points

		PointCloudMaterial particles2Mat = new PointCloudMaterial()
				.setColor(color)
				.setSize(2)
				.setOpacity(0.5);
		
		Geometry pgeo2 = spacedPoints.clone();
		PointCloud particles2 = new PointCloud( pgeo2, particles2Mat );
		particles2.getPosition().set( x, y, z + 100.0 );
		particles2.getRotation().set( rx, ry, rz );
		particles2.getScale().set( s );
		this.parent.add( particles2 );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		this.parent.getRotation().addY( ( this.mouseX - parent.getRotation().getY() ) * 0.00001 );
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 );
	}

	@Override
	public String getName() {
		return "Shapes and curves";
	}

	@Override
	public String getDescription() {
		return "Drag mouse to spin.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

}
