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

import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.geometries.Cylinder;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Object3D;

public class AxisHelper extends Object3D
{

	public AxisHelper()
	{
		super();

		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3f() );
		lineGeometry.getVertices().add( new Vector3f( 0, 100, 0 ) );

		Cylinder coneGeometry = new Cylinder( 0, 5, 25, 5, 1 );

		// x
		LineBasicMaterial lbmX = new LineBasicMaterial();
		lbmX.setColor(new Color3f(0xff0000));

		MeshBasicMaterial mbmX = new MeshBasicMaterial();
		mbmX.setColor( new Color3f(0xff0000) );
		
		Line line1 = new Line( lineGeometry, lbmX );
		line1.getRotation().setZ( - Math.PI / 2.0 );
		this.addChild(line1 );
		
		Mesh cone1 = new Mesh( coneGeometry, mbmX );
		cone1.getPosition().setX(100);
		cone1.getRotation().setZ(- Math.PI / 2.0 );
		this.addChild( cone1 );

		// y
		LineBasicMaterial lbmY = new LineBasicMaterial();
		lbmY.setColor(new Color3f(0x00ff00));

		MeshBasicMaterial mbmY = new MeshBasicMaterial();
		mbmY.setColor( new Color3f(0x00ff00) );

		Line line2 = new Line( lineGeometry, lbmY );
		this.addChild( line2 );

		Mesh cone2 = new Mesh( coneGeometry, mbmY );
		cone2.getPosition().setY(100);
		this.addChild( cone2 );

		// z
		LineBasicMaterial lbmZ = new LineBasicMaterial();
		lbmZ.setColor(new Color3f(0x0000ff));

		MeshBasicMaterial mbmZ = new MeshBasicMaterial();
		mbmZ.setColor( new Color3f(0x0000ff) );

		Line line3 = new Line( lineGeometry, lbmZ );
		line3.getRotation().setX( Math.PI / 2.0 );
		this.addChild( line3 );

		Mesh cone3 = new Mesh( coneGeometry, mbmZ );
		cone3.getPosition().setZ(100);
		cone3.getRotation().setX( Math.PI / 2.0 );
		this.addChild( cone3 );
	}
}
