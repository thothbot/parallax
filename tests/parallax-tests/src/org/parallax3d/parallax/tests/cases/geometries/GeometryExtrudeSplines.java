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

import java.util.Arrays;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.SceneUtils;
import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.graphics.extras.curves.SplineCurve3;
import org.parallax3d.parallax.graphics.extras.curves.SplineCurve3Closed;
import org.parallax3d.parallax.graphics.extras.curves.parametric.*;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.TubeGeometry;
import org.parallax3d.parallax.graphics.extras.helpers.CameraHelper;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_geometry_extrude_splines")
public final class GeometryExtrudeSplines extends ParallaxTest
{

	Scene scene;
	Object3D parent;
	Object3D tubeMesh;
	TubeGeometry tubeGeometry;
	Mesh cameraEye;

	PerspectiveCamera mainCamera;
	PerspectiveCamera splineCamera;
	CameraHelper cameraHelper;

	// Default
	Curve extrudePath = splines().get("GrannyKnot");
	int extrusionSegments = 100;
	int radiusSegments = 3;
	double scale = 4;
	boolean isAnimation = false;
	boolean isClosed = true;
	boolean isDebug = false;
	boolean isLookAhead = false;
	boolean isShowCameraHelper = false;

	Vector3 binormal = new Vector3();
	Vector3 normal = new Vector3();

	double targetRotation = 0;
	double targetRotationOnMouseDown = 0;

	int mouseX = 0;
	int mouseXOnMouseDown = 0;

	public FastMap<Curve> splines()
	{
		FastMap<Curve> retval = new FastMap<>();
		retval.put("GrannyKnot", new CurveGrannyKnot());
		retval.put("HeartCurve", new CurveHeart(3.5)); // ?
		retval.put("VivianiCurve", new CurveViviani(70));
		retval.put("KnotCurve", new CurveKnot());
		retval.put("HelixCurve", new CurveHelix());
		retval.put("TrefoilKnot", new CurveTrefoilKnot());
		retval.put("TorusKnotGeometry", new CurveTorusKnot(20));
		retval.put("CinquefoilKnot", new CurveCinquefoilKnot(20));
		retval.put("TrefoilPolynomialKnot", new CurveTrefoilPolynomialKnot(14));
		retval.put("FigureEightPolynomialKnot", new CurveFigureEightPolynomialKnot());
		retval.put("DecoratedTorusKnot4a", new CurveDecoratedTorusKnot4a());
		retval.put("DecoratedTorusKnot4b", new CurveDecoratedTorusKnot4b());
		retval.put("DecoratedTorusKnot5a", new CurveDecoratedTorusKnot5a());
		retval.put("DecoratedTorusKnot5c", new CurveDecoratedTorusKnot5c());
		retval.put("PipeSpline", new SplineCurve3(Arrays.asList(
				 new Vector3(0, 10, -10),  new Vector3(10, 0, -10),  new Vector3(20, 0, 0),
				 new Vector3(30, 0, 10),   new Vector3(30, 0, 20),   new Vector3(20, 0, 30),
				 new Vector3(10, 0, 30),   new Vector3(0, 0, 30),    new Vector3(-10, 10, 30),
				 new Vector3(-10, 20, 30), new Vector3(0, 30, 30),   new Vector3(10, 30, 30),
				 new Vector3(20, 30, 15),  new Vector3(10, 30, 10),  new Vector3(0, 30, 10),
				 new Vector3(-10, 20, 10), new Vector3(-10, 10, 10), new Vector3(0, 0, 10),
				 new Vector3(10, -10, 10), new Vector3(20, -15, 10), new Vector3(30, -15, 10),
				 new Vector3(40, -15, 10), new Vector3(50, -15, 10), new Vector3(60, 0, 10),
				 new Vector3(70, 0, 0),    new Vector3(80, 0, 0),    new Vector3(90, 0, 0),
				 new Vector3(100, 0, 0))));
		retval.put("SampleClosedSpline",  new SplineCurve3Closed(Arrays.asList( //?
				 new Vector3(0, -40, -40), new Vector3(0, 40, -40), new Vector3(0, 140, -40),
				 new Vector3(0, 40, 40),   new Vector3(0, -40, 40) )));

		return retval;
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		mainCamera = new PerspectiveCamera(
				50, // fov
				context.getAspectRation(), // aspect
				0.01, // near
				1000 // far
		);

		mainCamera.getPosition().set(0, 50, 500);

		splineCamera = new PerspectiveCamera(
				84, // fov
				context.getAspectRation(), // aspect
				0.01, // near
				1000 // far
		);

		DirectionalLight light = new DirectionalLight(0xffffff);
		light.getPosition().set(0, 0, 1);
		scene.add(light);

		parent = new Object3D();
		parent.getPosition().setY(100);
		parent.add(splineCamera);

		scene.add(parent);

		cameraHelper = new CameraHelper(splineCamera);
		scene.add(cameraHelper);

		addTube();

		// Debug point
		MeshBasicMaterial pMaterial = new MeshBasicMaterial().setColor(0xdddddd);
		cameraEye = new Mesh(new SphereGeometry(5), pMaterial);
		parent.add(cameraEye);

		animateCamera();

		context.getRenderer().setClearColor(0xf0f0f0);
	}

	private void animateCamera()
	{
		cameraHelper.setVisible( this.isShowCameraHelper );
		cameraEye.setVisible( this.isShowCameraHelper );
	}

	private void setScale()
	{
		tubeMesh.getScale().set(this.scale);
	}

	private void addTube()
	{
		if (tubeMesh != null)
			parent.remove(tubeMesh);

		tubeGeometry = new TubeGeometry(this.extrudePath, this.extrusionSegments, 2.0, this.radiusSegments, this.isClosed, this.isDebug);

		addGeometry(tubeGeometry, new Color(0xff00ff));
		setScale();
	}

	private void addGeometry(Geometry geometry, Color color)
	{
		MeshLambertMaterial material1 = new MeshLambertMaterial()
				.setColor(color)
				.setOpacity(this.isDebug ? 0.2 : 0.8)
				.setTransparent(true);

		MeshBasicMaterial material2 = new MeshBasicMaterial()
				.setColor( 0x000000)
				.setWireframe(true)
				.setOpacity(0.5);

		// 3d shape
		this.tubeMesh = (Object3D) SceneUtils.createMultiMaterialObject(geometry, Arrays.asList(material1, material2));

//    		this.tubeMesh.add(geometry.getDebug());

		this.parent.add(this.tubeMesh);
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		// Try Animate Camera Along Spline
		double looptime = 20 * 1000;
		double t = ((context.getDeltaTime() % looptime) / looptime);

		Vector3 pos = (Vector3) this.tubeGeometry.getPath().getPointAt(t);
		pos.multiply( this.scale );

		// interpolation
		int segments = this.tubeGeometry.getTangents().size();
		double pickt = t * segments;
		int pick = (int) Math.floor(pickt);
		int pickNext = (pick + 1) % segments;

		this.binormal.sub( this.tubeGeometry.getBinormals().get( pickNext ), this.tubeGeometry.getBinormals().get( pick ) );
		this.binormal.multiply( pickt - (double)pick ).add( this.tubeGeometry.getBinormals().get(pick) );

		Vector3 dir = (Vector3) this.tubeGeometry.getPath().getTangentAt(t);

		double offset = 15;

		this.normal.copy( this.binormal ).cross( dir );

		// We move on a offset on its binormal
		pos.add( this.normal.clone().multiply( offset ) );

		this.splineCamera.setPosition( pos );
		this.cameraEye.setPosition( pos );

		// Using arclength for stabilization in look ahead.
		Vector3 lookAt = (Vector3) this.tubeGeometry.getPath().getPointAt(
				( t + 30 / this.tubeGeometry.getPath().getLength()) % 1 ).multiply(this.scale);

		// Camera Orientation 2 - up orientation via normal
		if ( !this.isLookAhead )
			lookAt.copy( pos ).add( dir );

		this.splineCamera.getMatrix().lookAt( this.splineCamera.getPosition(), lookAt, this.normal );
		this.splineCamera.getRotation().setFromRotationMatrix(splineCamera.getMatrix(), splineCamera.getRotation().getOrder());

		this.cameraHelper.update();

		this.parent.getRotation().addY( ( this.targetRotation - this.parent.getRotation().getY() ) * 0.05 );

		context.getRenderer().render( scene, isAnimation ? splineCamera : mainCamera );
	}

	@Override
	public String getName() {
		return "Spline Extrusion";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

//	@Override
//	public void onAnimationReady(AnimationReadyEvent event)
//	{
//		super.onAnimationReady(event);
//
//		final DemoScene rs = (DemoScene) this.renderingPanel.getAnimatedScene();
//
//		this.renderingPanel.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
//		      @Override
//		      public void onMouseMove(MouseMoveEvent event)
//		      {
//		    	  	rs.mouseX = (event.getX() - renderingPanel.context.getRenderer().getAbsoluteWidth() / 2 );
//		    	  	rs.targetRotation = rs.targetRotationOnMouseDown + (rs.mouseX - rs.mouseXOnMouseDown) * 0.02;
//		      }
//		});
//
//		this.renderingPanel.getCanvas().addMouseDownHandler(new MouseDownHandler() {
//
//			@Override
//			public void onMouseDown(MouseDownEvent event) {
//				rs.mouseXOnMouseDown = event.getX() - renderingPanel.context.getRenderer().getAbsoluteWidth() / 2;
//				rs.targetRotationOnMouseDown = rs.targetRotation;
//
//			}
//		});
//
//		FlowPanel panel = new FlowPanel();
//		panel.setStyleName("common-panel", true);
//		panel.setStyleName("corner-panel", true);
//		this.renderingPanel.add(panel);
//		this.renderingPanel.setWidgetLeftWidth(panel, 1, Unit.PX, 40, Unit.EM);
//		this.renderingPanel.setWidgetTopHeight(panel, 1, Unit.PX, 19, Unit.EM);
//
//
//		// Splines
//		panel.add(new InlineLabel("Spline:"));
//
//		final ListBox splines = new ListBox();
//		splines.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				rs.extrudePath = rs.splines().get(splines.getValue(splines.getSelectedIndex()));
//				rs.addTube();
//			}
//		});
//
//		for(String key: rs.splines().keySet())
//			splines.addItem(key);
//
//		panel.add(splines);
//
//		panel.add(new InlineHTML("<br/>"));
//
//		// Scale
//		panel.add(new InlineLabel("Scale:"));
//
//		final ListBox scale = new ListBox();
//		scale.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				rs.scale = Integer.parseInt( scale.getValue(scale.getSelectedIndex()));
//				rs.setScale();
//			}
//		});
//
//		for(String key: Arrays.asList("1", "2", "4", "6", "10"))
//			scale.addItem(key, key);
//		scale.setItemSelected(2, true);
//
//		panel.add(scale);
//
//		panel.add(new InlineHTML("<br/>"));
//
//		// Extrusion Segments
//		panel.add(new InlineLabel("Extrusion Segments:"));
//
//		final ListBox extrusionSegments = new ListBox();
//		extrusionSegments.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				rs.extrusionSegments = Integer.parseInt( extrusionSegments.getValue(extrusionSegments.getSelectedIndex()) );
//				rs.addTube();
//			}
//		});
//
//		for(String key: Arrays.asList("50", "100", "200", "400"))
//			extrusionSegments.addItem(key);
//		extrusionSegments.setItemSelected(1, true);
//
//		panel.add(extrusionSegments);
//
//		panel.add(new InlineHTML("<br/>"));
//
//		// Radius Segments
//		panel.add(new InlineLabel("Radius Segments:"));
//
//		final ListBox radiusSegments = new ListBox();
//		radiusSegments.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				rs.radiusSegments = Integer.parseInt( radiusSegments.getValue(radiusSegments.getSelectedIndex()) );
//				rs.addTube();
//			}
//		});
//
//		for(String key: Arrays.asList("1", "2", "3", "4", "5", "6", "8", "12"))
//			radiusSegments.addItem(key, key);
//		radiusSegments.setItemSelected(2, true);
//
//		panel.add(radiusSegments);
//
//		panel.add(new InlineHTML("<br/>"));
//
//		// Debug normals
//		panel.add(new InlineLabel("Debug normals:"));
//		final CheckBox isDebugNormals = new CheckBox();
//		isDebugNormals.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				rs.isDebug = isDebugNormals.getValue();
//				rs.addTube();
//			}
//		});
//
//		panel.add(isDebugNormals);
//
//		// Closed
//		panel.add(new InlineLabel("Closed:"));
//		final CheckBox isClosed = new CheckBox();
//		isClosed.setValue(true);
//		isClosed.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				rs.isClosed = isClosed.getValue();
//				rs.addTube();
//			}
//		});
//
//		panel.add(isClosed);
//
//		panel.add(new InlineHTML("<br/>"));
//
//		final Button animation = new Button("Camera Spline Animation View: OFF");
//		animation.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				rs.isAnimation = !rs.isAnimation;
//				animation.setText("Camera Spline Animation View: " + ((rs.isAnimation) ? "ON" : "OFF"));
//			}
//		});
//
//		// Camera Spline Animation View
//		panel.add(animation);
//
//		panel.add(new InlineHTML("<br/>"));
//
//		// Look Ahead
//		panel.add(new InlineLabel("Look Ahead:"));
//		final CheckBox isLookAhead = new CheckBox();
//		isLookAhead.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				rs.isLookAhead = isLookAhead.getValue();
//			}
//		});
//
//		panel.add(isLookAhead);
//
//		// Camera Helper
//		panel.add(new InlineLabel("Camera Helper:"));
//		final CheckBox isCameraHelper = new CheckBox();
//		isCameraHelper.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				rs.isShowCameraHelper = isCameraHelper.getValue();
//				rs.animateCamera();
//			}
//		});
//		panel.add(isCameraHelper);
//	}

}
