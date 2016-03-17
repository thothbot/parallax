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

package org.parallax3d.parallax.graphics.extras.helpers;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;

/**
 * 
 * Shows frustum, line of sight and up of the camera.
 * <p>
 * Based on frustum visualization in lightgl.js shadowmap example
 * <a href="http://evanw.github.com/lightgl.js/tests/shadowmap.html">github.com</a>
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.CameraHelper")
public class CameraHelper extends LineSegments
{
	private Camera camera;
	
	public CameraHelper(Camera camera)
	{
		super(intDefaultGeometry(), initDefaultMaterial());
		this.camera = camera;
		this.camera.updateProjectionMatrix();

		this.setMatrix( camera.getMatrixWorld() );
		this.setMatrixAutoUpdate(false);

		this.update();
	}

	private static Geometry intDefaultGeometry() {
		Geometry geometry = new Geometry();

		int hexFrustum = 0xffaa00;
		int hexCone	   = 0xff0000;
		int hexUp	   = 0x00aaff;
		int hexTarget  = 0xffffff;
		int hexCross   = 0x333333;

		// near

		addLine( geometry, "n1", "n2", hexFrustum );
		addLine( geometry, "n2", "n4", hexFrustum );
		addLine( geometry, "n4", "n3", hexFrustum );
		addLine( geometry, "n3", "n1", hexFrustum );

		// far

		addLine( geometry, "f1", "f2", hexFrustum );
		addLine( geometry, "f2", "f4", hexFrustum );
		addLine( geometry, "f4", "f3", hexFrustum );
		addLine( geometry, "f3", "f1", hexFrustum );

		// sides

		addLine( geometry, "n1", "f1", hexFrustum );
		addLine( geometry, "n2", "f2", hexFrustum );
		addLine( geometry, "n3", "f3", hexFrustum );
		addLine( geometry, "n4", "f4", hexFrustum );

		// cone

		addLine( geometry, "p", "n1", hexCone );
		addLine( geometry, "p", "n2", hexCone );
		addLine( geometry, "p", "n3", hexCone );
		addLine( geometry, "p", "n4", hexCone );

		// up

		addLine( geometry, "u1", "u2", hexUp );
		addLine( geometry, "u2", "u3", hexUp );
		addLine( geometry, "u3", "u1", hexUp );

		// target

		addLine( geometry, "c", "t", hexTarget );
		addLine( geometry, "p", "c", hexCross );

		// cross

		addLine( geometry, "cn1", "cn2", hexCross );
		addLine( geometry, "cn3", "cn4", hexCross );

		addLine( geometry, "cf1", "cf2", hexCross );
		addLine( geometry, "cf3", "cf4", hexCross );
		
		return geometry;
	}

	private static LineBasicMaterial initDefaultMaterial() {
		return new LineBasicMaterial().setColor(0xffffff).setVertexColors(Material.COLORS.FACE);
	}
	
	private static void addLine( Geometry geometry, String a, String b, int hex )
	{
		addPoint(geometry, a, hex );
		addPoint(geometry, b, hex );
	}

	static final FastMap<List<Integer>> pointMap = new FastMap<>();
	private static void addPoint( Geometry geometry, String id, int hex )
	{
		geometry.getVertices().add( new Vector3() );
		geometry.getColors().add( new Color( hex ) );

		if ( !pointMap.containsKey(id) )
			pointMap.put( id, new ArrayList<Integer>() );

		pointMap.get( id ).add(geometry.getVertices().size() - 1 );
	}

	static final Vector3 _vector = new Vector3();
	static final Camera _camera = new Camera();
	public void update()
	{
		double w = 1., h = 1.;

		// we need just camera projection matrix
		// world matrix must be identity

		_camera.getProjectionMatrix().copy(this.camera.getProjectionMatrix());

		// center / target

		setPoint( "c", 0, 0, - 1 );
		setPoint( "t", 0, 0,  1 );

		// near

		setPoint( "n1", - w, - h, - 1 );
		setPoint( "n2",   w, - h, - 1 );
		setPoint( "n3", - w,   h, - 1 );
		setPoint( "n4",   w,   h, - 1 );

		// far

		setPoint( "f1", - w, - h, 1 );
		setPoint( "f2",   w, - h, 1 );
		setPoint( "f3", - w,   h, 1 );
		setPoint( "f4",   w,   h, 1 );

		// up

		setPoint( "u1",   w * 0.7, h * 1.1, - 1 );
		setPoint( "u2", - w * 0.7, h * 1.1, - 1 );
		setPoint( "u3",         0, h * 2,   - 1 );

		// cross

		setPoint( "cf1", - w,   0, 1 );
		setPoint( "cf2",   w,   0, 1 );
		setPoint( "cf3",   0, - h, 1 );
		setPoint( "cf4",   0,   h, 1 );

		setPoint( "cn1", - w,   0, - 1 );
		setPoint( "cn2",   w,   0, - 1 );
		setPoint( "cn3",   0, - h, - 1 );
		setPoint( "cn4",   0,   h, - 1 );

		geometry.setVerticesNeedUpdate(true);
	}

	private void setPoint( String point, double x, double y, double z )
	{
		_vector.set( x, y, z ).unproject( _camera );

		if ( pointMap.containsKey( point) ) {
			List<Integer> points = pointMap.get( point );

			for ( int i = 0, il = points.size(); i < il; i ++ ) {

				((Geometry) geometry).getVertices().get(points.get(i)).copy( _vector );

			}

		}
	}
}
