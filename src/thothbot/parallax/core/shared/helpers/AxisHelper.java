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

import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.geometries.CylinderGeometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;

public class AxisHelper extends Object3D
{

	public AxisHelper()
	{
		super();

		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3() );
		lineGeometry.getVertices().add( new Vector3( 0, 100, 0 ) );

		CylinderGeometry coneGeometry = new CylinderGeometry( 0, 5, 25, 5, 1 );

		// x
		LineBasicMaterial lbmX = new LineBasicMaterial();
		lbmX.setColor(new Color(0xff0000));

		MeshBasicMaterial mbmX = new MeshBasicMaterial();
		mbmX.setColor( new Color(0xff0000) );
		
		Line line1 = new Line( lineGeometry, lbmX );
		line1.getRotation().setZ( - Math.PI / 2.0 );
		this.add(line1 );
		
		Mesh cone1 = new Mesh( coneGeometry, mbmX );
		cone1.getPosition().setX(100);
		cone1.getRotation().setZ(- Math.PI / 2.0 );
		this.add( cone1 );

		// y
		LineBasicMaterial lbmY = new LineBasicMaterial();
		lbmY.setColor(new Color(0x00ff00));

		MeshBasicMaterial mbmY = new MeshBasicMaterial();
		mbmY.setColor( new Color(0x00ff00) );

		Line line2 = new Line( lineGeometry, lbmY );
		this.add( line2 );

		Mesh cone2 = new Mesh( coneGeometry, mbmY );
		cone2.getPosition().setY(100);
		this.add( cone2 );

		// z
		LineBasicMaterial lbmZ = new LineBasicMaterial();
		lbmZ.setColor(new Color(0x0000ff));

		MeshBasicMaterial mbmZ = new MeshBasicMaterial();
		mbmZ.setColor( new Color(0x0000ff) );

		Line line3 = new Line( lineGeometry, lbmZ );
		line3.getRotation().setX( Math.PI / 2.0 );
		this.add( line3 );

		Mesh cone3 = new Mesh( coneGeometry, mbmZ );
		cone3.getPosition().setZ(100);
		cone3.getRotation().setX( Math.PI / 2.0 );
		this.add( cone3 );
	}
}
