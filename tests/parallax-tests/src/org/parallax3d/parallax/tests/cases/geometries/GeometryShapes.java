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
import org.parallax3d.parallax.graphics.extras.core.Path;
import org.parallax3d.parallax.graphics.extras.core.Shape;
import org.parallax3d.parallax.graphics.extras.geometries.ExtrudeGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.ShapeGeometry;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.PointsMaterial;
import org.parallax3d.parallax.graphics.objects.Group;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.Points;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_geometry_shapes")
public final class GeometryShapes extends ParallaxTest implements TouchMoveHandler
{
	static final String image = "textures/UV_Grid_Sm.jpg";

	Scene scene;
	PerspectiveCamera camera ;

	Group group;

	int width = 0;
	int mouseX = 0;

	Texture texture;
	ExtrudeGeometry.ExtrudeGeometryParameters extrudeSettings;
			
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
		
		camera.getPosition().set( 0, 150, 500 );

		PointLight light = new PointLight( 0xffffff, 0.8 );
		camera.add( light );

		group = new Group();
		group.getPosition().setY(50);
		scene.add( group );

		texture = new Texture(image)
			.setWrapS(TextureWrapMode.REPEAT)
			.setWrapT(TextureWrapMode.REPEAT);
		texture.getRepeat().set( 0.008, 0.008 );

		extrudeSettings = new ExtrudeGeometry.ExtrudeGeometryParameters();
		extrudeSettings.amount = 8;
		extrudeSettings.bevelEnabled = true;
		extrudeSettings.bevelSegments = 2;
		extrudeSettings.steps = 2;
		extrudeSettings.bevelSize = 1;
		extrudeSettings.bevelThickness = 1;

        california();
		triangle();
		square();
		circle();
		arcCircle();
		heart();
		roundedRectangle();
		fish();
		smile();
		splineShape();

		context.getRenderer().setClearColor(0xf0f0f0);
	}
	
	private void splineShape()
	{
		List<Vector2> splinepts = new ArrayList<>();
        splinepts.add( new Vector2 ( 70, 20 ) );
        splinepts.add( new Vector2 ( 80, 90 ) );
        splinepts.add( new Vector2 ( -30, 70 ) );
        splinepts.add( new Vector2 ( 0, 0 ) );

		Shape splineShape = new Shape();
		splineShape.moveTo( 0, 0 );
		splineShape.splineThru( splinepts );

		addShape(splineShape, 0x888888, -50, -100, -50, 0, 0, 0, 0.2 );
	}
	
	private void california()
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

		addShape( californiaShape, 0xffaa00, -300, -100, 0, 0, 0, 0, 0.25 );
	}

	private void triangle()
	{
		Shape triangleShape = new Shape();
		triangleShape.moveTo(  80, 20 );
		triangleShape.lineTo(  40, 80 );
		triangleShape.lineTo( 120, 80 );
		triangleShape.lineTo(  80, 20 ); // close path

		addShape(triangleShape, 0xffee00, -180, 0, 0, 0, 0, 0, 1 );
	}

	private void square()
	{
		int sqLength = 80;

		Shape squareShape = new Shape();
		squareShape.moveTo( 0,0 );
		squareShape.lineTo( 0, sqLength );
		squareShape.lineTo( sqLength, sqLength );
		squareShape.lineTo( sqLength, 0 );
		squareShape.lineTo( 0, 0 );
		
		addShape(squareShape, 0x0055ff, 150, 100, 0, 0, 0, 0, 1 );
	}

	private void circle()
	{
		int circleRadius = 40;
		Shape circleShape = new Shape();
		circleShape.moveTo( 0, circleRadius );
		circleShape.quadraticCurveTo( circleRadius, circleRadius, circleRadius, 0 );
		circleShape.quadraticCurveTo( circleRadius, -circleRadius, 0, -circleRadius );
		circleShape.quadraticCurveTo( -circleRadius, -circleRadius, -circleRadius, 0 );
		circleShape.quadraticCurveTo( -circleRadius, circleRadius, 0, circleRadius );
		
		addShape(circleShape, 0x00ff11, 120, 250, 0, 0, 0, 0, 1 );
	}

	private void arcCircle()
	{
		Shape arcShape = new Shape();
		arcShape.moveTo( 50, 10 );
		arcShape.absarc( 10, 10, 40, 0, Math.PI*2, false );

		Path holePath = new Path();
		holePath.moveTo( 20, 10 );
		holePath.absarc( 10, 10, 10, 0, Math.PI*2, true );
		arcShape.getHoles().add( holePath );
		
		addShape(arcShape, 0xbb4422, 150, 0, 0, 0, 0, 0, 1 );
	}

	private void heart()
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

		addShape( heartShape, 0xff1100, 0, 100, 0, Math.PI, 0, 0, 1 );
	}

	private void fish()
	{
		int x = 0, y = 0;
		
		Shape fishShape = new Shape();

		fishShape.moveTo(x, y);
		fishShape.quadraticCurveTo(x + 50, y - 80, x + 90, y - 10);
		fishShape.quadraticCurveTo(x + 100, y - 10, x + 115, y - 40);
		fishShape.quadraticCurveTo(x + 115, y, x + 115, y + 40);
		fishShape.quadraticCurveTo(x + 100, y + 10, x + 90, y + 10);
		fishShape.quadraticCurveTo(x + 50, y + 80, x, y);
		
		addShape( fishShape, 0x222222, -60, 200, 0, 0, 0, 0, 1 );
	}
	
	private void smile()
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
		
		addShape( smileyShape, 0xee00ff, -270, 250, 0, Math.PI, 0, 0, 1 );
	}
	
	private void roundedRectangle()
	{
		Shape roundedRectShape = new Shape();
		roundedRect( roundedRectShape, 0, 0, 50, 50, 20 );

		addShape( roundedRectShape, 0x005500, -150, 150, 0, 0, 0, 0, 1 );
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
	
	private void addShape(Shape shape, int color,
						  double x, double y, double z, double rx, double ry, double rz, double s
	) {

        // flat shape with texture
        // note: default UVs generated by ShapeGemoetry are simply the x- and y-coordinates of the vertices

        ShapeGeometry geometry = new ShapeGeometry( shape );

        Mesh mesh = new Mesh( geometry, new MeshPhongMaterial( ).setMap(texture).setSide(Material.SIDE.DOUBLE) );
        mesh.getPosition().set( x, y, z - 175 );
        mesh.getRotation().set( rx, ry, rz );
        mesh.getScale().set( s, s, s );
        group.add( mesh );

        // flat shape

        geometry = new ShapeGeometry( shape );

        Mesh mesh1 = new Mesh( geometry, new MeshPhongMaterial().setColor(color).setSide(Material.SIDE.DOUBLE) );
        mesh1.getPosition().set( x, y, z - 125 );
        mesh1.getRotation().set( rx, ry, rz );
        mesh1.getScale().set( s, s, s );
        group.add( mesh1 );

        // extruded shape

        ExtrudeGeometry geometry2 = new ExtrudeGeometry( shape, extrudeSettings );

        Mesh mesh2 = new Mesh( geometry2, new MeshPhongMaterial( ).setColor(color) );
        mesh2.getPosition().set( x, y, z - 75 );
        mesh2.getRotation().set( rx, ry, rz );
        mesh2.getScale().set( s, s, s );
        group.add( mesh2 );

        // lines

        shape.setAutoClose(true);
        Geometry points = shape.createPointsGeometry();
        Geometry spacedPoints = shape.createSpacedPointsGeometry( 50 );

        // solid line

        Line line = new Line( points, new LineBasicMaterial().setColor(color).setLinewidth(3) );
        line.getPosition().set( x, y, z - 25 );
        line.getRotation().set( rx, ry, rz );
        line.getScale().set( s, s, s );
        group.add( line );

        // line from equidistance sampled points

        Line line2 = new Line( spacedPoints, new LineBasicMaterial().setColor(color).setLinewidth(3) );
        line2.getPosition().set( x, y, z + 25 );
        line2.getRotation().set( rx, ry, rz );
        line2.getScale().set( s, s, s );
        group.add( line2 );

        // vertices from real points

        Points particles = new Points( points, new PointsMaterial().setColor(color).setSize(4) );
        particles.getPosition().set( x, y, z + 75 );
        particles.getRotation().set( rx, ry, rz );
        particles.getScale().set( s, s, s );
        group.add( particles );

        // equidistance sampled points

        Points particles2= new Points( spacedPoints, new PointsMaterial().setColor(color).setSize(4) );
        particles2.getPosition().set( x, y, z + 125 );
        particles2.getRotation().set( rx, ry, rz );
        particles2.getScale().set( s, s, s );
        group.add( particles2 );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
        group.getRotation().addY( ( this.mouseX - group.getRotation().getY() ) * 0.00001 );
		
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
