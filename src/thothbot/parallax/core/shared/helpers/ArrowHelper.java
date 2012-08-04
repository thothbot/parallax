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
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.geometries.Cylinder;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Object3D;

public class ArrowHelper extends Object3D
{

	public Line line;
	public Mesh cone;
	
	public ArrowHelper ( Vector3f dir, Vector3f origin)
	{
		this(dir, origin, 20);
	}
	
	public ArrowHelper ( Vector3f dir, Vector3f origin, double length)
	{
		this(dir, origin, length, 0xffff00);
	}

	public ArrowHelper ( Vector3f dir, Vector3f origin, double length, int hex ) 
	{
		super();

		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3f( 0, 0, 0 ) );
		lineGeometry.getVertices().add( new Vector3f( 0, 1, 0 ) );

		LineBasicMaterial lbm = new LineBasicMaterial();
		lbm.setColor(new Color3f(hex));
		this.line = new Line( lineGeometry, lbm );
		this.addChild( this.line );

		Cylinder coneGeometry = new Cylinder( 0, 0.05, 0.25, 5, 1 );

		MeshBasicMaterial mbm = new MeshBasicMaterial();
		mbm.setColor(new Color3f(hex));
		this.cone = new Mesh( coneGeometry, mbm );
		this.cone.getPosition().set( 0, 1, 0 );
		this.addChild( this.cone );

		if ( origin instanceof Vector3f ) this.position = origin;

		setDirection( dir );
		setLength( length );
	}
	
	public void setDirection( Vector3f dir ) 
	{
		Vector3f axis = new Vector3f( 0, 1, 0 ).cross( dir );

		double radians = Math.acos( new Vector3f( 0, 1, 0 ).dot( dir.clone().normalize() ) );

		this.matrix = new Matrix4f().makeRotationAxis( axis.normalize(), radians );

		this.rotation.getRotationFromMatrix( this.matrix, this.scale );

	}

	public void setLength( double length ) 
	{
		this.scale.set( length, length, length );
	}

	public void setColor( int hex ) 
	{
		LineBasicMaterial lMaterial = (LineBasicMaterial) this.line.getMaterial();
		lMaterial.getColor().setHex( hex );

		MeshBasicMaterial mMaterial = (MeshBasicMaterial) this.cone.getMaterial();
		mMaterial.getColor().setHex( hex );
	}
}
