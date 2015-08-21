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

package thothbot.parallax.core.shared.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;

import com.google.gwt.core.client.GWT;

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
public class CameraHelper extends Line
{
	private Camera camera;
	
	private Map<String, List<Integer>> pointMap;
	
	public CameraHelper(Camera camera)
	{
		super(new Geometry());

		this.camera = camera;
		
		LineBasicMaterial lbm = new LineBasicMaterial();
		lbm.setColor( new Color(0xffffff) );
		lbm.setVertexColors( Material.COLORS.FACE );
		setMaterial(lbm);
		setMode(Line.MODE.PIECES);
		
		setMatrix(camera.getMatrixWorld());
		setMatrixAutoUpdate(false);
		
		this.pointMap = GWT.isScript() ? 
				new FastMap<List<Integer>>() : new HashMap<String, List<Integer>>();

		// colors

		int hexFrustum = 0xffaa00;
		int hexCone	   = 0xff0000;
		int hexUp	   = 0x00aaff;
		int hexTarget  = 0xffffff;
		int hexCross   = 0x333333;

		// near

		addLine( "n1", "n2", hexFrustum );
		addLine( "n2", "n4", hexFrustum );
		addLine( "n4", "n3", hexFrustum );
		addLine( "n3", "n1", hexFrustum );

		// far

		addLine( "f1", "f2", hexFrustum );
		addLine( "f2", "f4", hexFrustum );
		addLine( "f4", "f3", hexFrustum );
		addLine( "f3", "f1", hexFrustum );

		// sides

		addLine( "n1", "f1", hexFrustum );
		addLine( "n2", "f2", hexFrustum );
		addLine( "n3", "f3", hexFrustum );
		addLine( "n4", "f4", hexFrustum );

		// cone

		addLine( "p", "n1", hexCone );
		addLine( "p", "n2", hexCone );
		addLine( "p", "n3", hexCone );
		addLine( "p", "n4", hexCone );

		// up

		addLine( "u1", "u2", hexUp );
		addLine( "u2", "u3", hexUp );
		addLine( "u3", "u1", hexUp );

		// target

		addLine( "c", "t", hexTarget );
		addLine( "p", "c", hexCross );

		// cross

		addLine( "cn1", "cn2", hexCross );
		addLine( "cn3", "cn4", hexCross );

		addLine( "cf1", "cf2", hexCross );
		addLine( "cf3", "cf4", hexCross );
	
		update();
	}
	
	private void addLine( String a, String b, int hex ) 
	{
		addPoint( a, hex );
		addPoint( b, hex );

	}

	private void addPoint( String id, int hex ) 
	{
		((Geometry)getGeometry()).getVertices().add( new Vector3() );
		((Geometry)getGeometry()).getColors().add( new Color( hex ) );

		if ( !this.pointMap.containsKey(id) ) 
			this.pointMap.put( id, new ArrayList<Integer>() );
		
		this.pointMap.get( id ).add(((Geometry)getGeometry()).getVertices().size() - 1 );
	}
	
	Vector3 _vector = new Vector3();
	Camera _camera = new Camera();

	public void update() 
	{
		double w = 1.0;
		double h = 1.0;

		// we need just camera projection matrix
		// world matrix must be identity

		_camera.getProjectionMatrix().copy( this.camera.getProjectionMatrix() );

		// center / target

		setPoint( "c", 0, 0, -1 );
		setPoint( "t", 0, 0,  1 );

		// near

		setPoint( "n1", -w, -h, -1 );
		setPoint( "n2",  w, -h, -1 );
		setPoint( "n3", -w,  h, -1 );
		setPoint( "n4",  w,  h, -1 );

		// far

		setPoint( "f1", -w, -h, 1 );
		setPoint( "f2",  w, -h, 1 );
		setPoint( "f3", -w,  h, 1 );
		setPoint( "f4",  w,  h, 1 );

		// up

		setPoint( "u1",  w * 0.7, h * 1.1, -1 );
		setPoint( "u2", -w * 0.7, h * 1.1, -1 );
		setPoint( "u3",        0, h * 2,   -1 );

		// cross

		setPoint( "cf1", -w,  0, 1 );
		setPoint( "cf2",  w,  0, 1 );
		setPoint( "cf3",  0, -h, 1 );
		setPoint( "cf4",  0,  h, 1 );

		setPoint( "cn1", -w,  0, -1 );
		setPoint( "cn2",  w,  0, -1 );
		setPoint( "cn3",  0, -h, -1 );
		setPoint( "cn4",  0,  h, -1 );

		((Geometry)getGeometry()).setVerticesNeedUpdate( true );
	}
	
	private void setPoint( String point, double x, double y, double z ) 
	{
		_vector.set( x, y, z ).unproject( _camera );

		List<Integer> points = this.pointMap.get( point );

		if ( points != null ) 
		{
			for ( int i = 0, il = points.size(); i < il; i ++ ) 
			{
				int j = points.get( i );
				((Geometry)getGeometry()).getVertices().get( j ).copy( _vector );
			}
		}
	}	
}
