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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.controllers.TrackballControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.helpers.ArrowHelper;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Group;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_geometry_normals")
public class GeometryNormals extends ParallaxTest 
{
	Scene scene;
	PerspectiveCamera camera;
	
	private TrackballControls control;
	
	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				70, // fov
				context.getAspectRation(), // aspect
				1, // near
				1000 // far 
		);
		camera.getPosition().setZ(500);
		
		this.control = new TrackballControls( camera, context );
		this.control.setPanSpeed(0.2);
		this.control.setDynamicDampingFactor(0.3);
		
		PointLight light = new PointLight( 0xffffff, 1.5, 0.0 );
		light.getPosition().set( 1000, 1000, 2000 );
		scene.add( light );

		Geometry geometry = new BoxGeometry(200, 200, 200, 2, 2, 2);
		MeshBasicMaterial material = new MeshBasicMaterial()
				.setColor( (int)(Math.random() * 0xffffff))
				.setWireframe(false);

		geometry.computeBoundingSphere();
		
		double scaleFactor = 160.0 / geometry.getBoundingSphere().getRadius();
		geometry.applyMatrix( new Matrix4().makeScale( scaleFactor, scaleFactor, scaleFactor ) );

		Geometry originalGeometry = geometry.clone();
		originalGeometry.computeFaceNormals();
		originalGeometry.computeVertexNormals( true );

		// in case of duplicated vertices
		geometry.mergeVertices();
		geometry.computeFaceNormals();
		geometry.computeVertexNormals( true );

		for ( int i = 0; i < geometry.getFaces().size(); i ++ ) {

			Face3 f  = geometry.getFaces().get( i );
			int n = 3;

			for( int j = 0; j < n; j++ ) {
				int vertexIndex = f.getFlat()[j];
				Vector3 p = geometry.getVertices().get( vertexIndex );

				Color color = new Color( 0xffffff );
				color.setHSL( ( p.getY() ) / 400.0 + 0.5, 1.0, 0.5 );

				f.getVertexColors().add( j, color);

			}

		}
		
		Group group = new Group();
		scene.add( group );
		
		MeshBasicMaterial meshMaterial = new MeshBasicMaterial()
				.setColor( 0xfefefe )
				.setWireframe(true)
				.setOpacity(0.5);
		Mesh mesh = new Mesh( geometry, meshMaterial );
		group.add( mesh );

		int normalLength = 15;
		
		for( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) {
			Face3 face = geometry.getFaces().get( f );

			Vector3 centroid = new Vector3()
				.add( geometry.getVertices().get( face.getA() ) )
				.add( geometry.getVertices().get( face.getB() ) )
				.add( geometry.getVertices().get( face.getC() ) )
				.divide( 3.0 );

			ArrowHelper arrow = new ArrowHelper(
					face.getNormal(),
					centroid,
					normalLength,
					0x3333FF );
			mesh.add( arrow );
		}
		
		for( int f = 0, fl = originalGeometry.getFaces().size(); f < fl; f ++ ) {
			Face3 face = originalGeometry.getFaces().get( f );
			if( face.getVertexNormals() == null ) {
				continue;
			}
			Log.error(face.getVertexNormals().size());
			for( int v = 0, vl = face.getVertexNormals().size(); v < vl; v ++ ) {
				ArrowHelper arrow = new ArrowHelper( 
						face.getVertexNormals().get( v ),
						originalGeometry.getVertices().get( face.getFlat()[ v ] ),
						normalLength,
						0xFF3333 );
				mesh.add( arrow );
			}
		}

		for( int f = 0, fl = ((Geometry)mesh.getGeometry()).getFaces().size(); f < fl; f ++ ) {
			Face3 face =((Geometry)mesh.getGeometry()).getFaces().get( f );
			if( face.getVertexNormals() == null ) {
				continue;
			}
			for( int v = 0, vl = face.getVertexNormals().size(); v < vl; v ++ ) {
				ArrowHelper arrow = new ArrowHelper( 
						face.getVertexNormals().get( v ),
						((Geometry)mesh.getGeometry()).getVertices().get( face.getFlat()[ v ] ),
						normalLength,
						0x000000 );
				mesh.add( arrow );
			}
		}
		
		context.getRenderer().setClearColor( 0xf0f0f0 );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		this.control.update();
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Normals";
	}

	@Override
	public String getDescription() {
		return "Blue Arrows: Face Normals. Red Arrows: Vertex Normals before Geometry.mergeVertices. Black Arrows: Vertex Normals after Geometry.mergeVertices.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

}
