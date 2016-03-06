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
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.PointCloudMaterial;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_buffergeometry_points")
public final class BufferGeometryParticles extends ParallaxTest
{
	
	Scene scene;
	PerspectiveCamera camera;
	PointCloud particleSystem;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				27, // fov
				context.getAspectRation(), // aspect
				5, // near
				35000 // far 
		);
		camera.getPosition().setZ(2750);

		
		scene.setFog( new Fog( 0x050505, 2000, 3500 ) );

		//

		int particles = 500000;

		BufferGeometry geometry = new BufferGeometry();

		Float32Array positions = Float32Array.create( particles * 3 );
		Float32Array colors = Float32Array.create( particles * 3 );
		
		Color color = new Color();
		double n = 1000, n2 = n/2;	// particles spread in the cube

		for ( int i = 0; i < positions.getLength(); i += 3 ) 
		{

			// positions
			double x = Math.random() * n - n2;
			double y = Math.random() * n - n2;
			double z = Math.random() * n - n2;

			positions.set( i, x );
			positions.set( i + 1, y );
			positions.set( i + 2, z );
			

			// colors
			double vx = ( x / n ) + 0.5;
			double vy = ( y / n ) + 0.5;
			double vz = ( z / n ) + 0.5;

			color.setRGB( vx, vy, vz );
			
			colors.set( i, color.getR() );
			colors.set( i + 1, color.getG() );
			colors.set( i + 2, color.getB() );
		}

		geometry.addAttribute( "position", new BufferAttribute( positions, 3 ) );
		geometry.addAttribute( "color", new BufferAttribute( colors, 3 ) );

		geometry.computeBoundingSphere();


		//
		PointCloudMaterial material = new PointCloudMaterial()
				.setVertexColors(Material.COLORS.VERTEX)
				.setSize(15.0);

		particleSystem = new PointCloud( geometry, material );
		scene.add( particleSystem );

		//

		context.getRenderer().setClearColor( scene.getFog().getColor(), 1 );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getFrameId() * 0.01;

		particleSystem.getRotation().setX( time * 0.25 );
		particleSystem.getRotation().setY( time * 0.5 );
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Buffered particles";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
