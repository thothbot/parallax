/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Projector;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Object3D;


public class CameraHelper extends Object3D
{
	private Camera camera;
	
	private Line line;

	private Geometry lineGeometry;
	private LineBasicMaterial lineMaterial;
	
	private Map<String, List<Integer>> pointMap;
	
	private static Projector __projector = new Projector();
	private static Vector3f __v = new Vector3f();
	private static Camera __c = new Camera();
	
	public CameraHelper(Camera camera)
	{
		this.camera = camera;
		
		this.lineGeometry = new Geometry();
		
		LineBasicMaterial lbm = new LineBasicMaterial();
		lbm.setColor( new Color3f(0xffffff) );
		lbm.setVertexColors( Material.COLORS.FACE );

		this.lineMaterial = lbm;

		this.pointMap = new HashMap<String, List<Integer>>();

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

		this.line = new Line( this.lineGeometry, this.lineMaterial, Line.TYPE.PIECES );
		this.addChild( this.line );
	}
	
	public Line getLine()
	{
		return this.line;
	}
	
	public void update() 
	{
		float w = 1.0f;
		float h = 1.0f;

		// we need just camera projection matrix
		// world matrix must be identity

		CameraHelper.__c.getProjectionMatrix().copy( this.camera.getProjectionMatrix() );

		// center / target

		setPoint( "c", 0f, 0f, -1f );
		setPoint( "t", 0f, 0f,  1f );

		// near

		setPoint( "n1", -w, -h, -1f );
		setPoint( "n2",  w, -h, -1f );
		setPoint( "n3", -w,  h, -1f );
		setPoint( "n4",  w,  h, -1f );

		// far

		setPoint( "f1", -w, -h, 1f );
		setPoint( "f2",  w, -h, 1f );
		setPoint( "f3", -w,  h, 1f );
		setPoint( "f4",  w,  h, 1f );

		// up

		setPoint( "u1",  w * 0.7f, h * 1.1f, -1f );
		setPoint( "u2", -w * 0.7f, h * 1.1f, -1f );
		setPoint( "u3",        0f, h * 2f,   -1f );

		// cross

		setPoint( "cf1", -w,  0f, 1f );
		setPoint( "cf2",  w,  0f, 1f );
		setPoint( "cf3",  0f, -h, 1f );
		setPoint( "cf4",  0f,  h, 1f );

		setPoint( "cn1", -w,  0f, -1f );
		setPoint( "cn2",  w,  0f, -1f );
		setPoint( "cn3",  0f, -h, -1f );
		setPoint( "cn4",  0f,  h, -1f );

		this.lineGeometry.verticesNeedUpdate = true;
	}

	private void addLine( String a, String b, int hex ) 
	{
		addPoint( a, hex );
		addPoint( b, hex );

	}

	private void addPoint( String id, int hex ) 
	{
		this.lineGeometry.getVertices().add( new Vector3f() );
		this.lineGeometry.getColors().add( new Color3f( hex ) );

		if ( !this.pointMap.containsKey(id) ) 
			this.pointMap.put( id, new ArrayList<Integer>() );
		
		this.pointMap.get( id ).add( this.lineGeometry.getVertices().size() - 1 );
	}
	
	private void setPoint( String point, float x, float y, float z ) 
	{
		CameraHelper.__v.set( x, y, z );
		CameraHelper.__projector.unprojectVector( CameraHelper.__v, CameraHelper.__c );

		List<Integer> points = this.pointMap.get( point );

		if ( points != null ) 
		{
			for ( int i = 0, il = points.size(); i < il; i ++ ) 
			{
				int j = points.get( i );
				this.lineGeometry.getVertices().get( j ).copy( CameraHelper.__v );
			}

		}

	}	
}
