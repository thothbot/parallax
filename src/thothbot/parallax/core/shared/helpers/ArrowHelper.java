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
import thothbot.parallax.core.shared.geometries.CylinderGeometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Object3D;

public class ArrowHelper extends Object3D
{

	public Line line;
	public Mesh cone;
	
	public ArrowHelper ( Vector3 dir, Vector3 origin)
	{
		this(dir, origin, 20);
	}
	
	public ArrowHelper ( Vector3 dir, Vector3 origin, double length)
	{
		this(dir, origin, length, 0xffff00);
	}

	public ArrowHelper ( Vector3 dir, Vector3 origin, double length, int hex ) 
	{
		super();

		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3( 0, 0, 0 ) );
		lineGeometry.getVertices().add( new Vector3( 0, 1, 0 ) );

		LineBasicMaterial lbm = new LineBasicMaterial();
		lbm.setColor(new Color(hex));
		this.line = new Line( lineGeometry, lbm );
		this.add( this.line );

		CylinderGeometry coneGeometry = new CylinderGeometry( 0, 0.05, 0.25, 5, 1 );

		MeshBasicMaterial mbm = new MeshBasicMaterial();
		mbm.setColor(new Color(hex));
		this.cone = new Mesh( coneGeometry, mbm );
		this.cone.getPosition().set( 0, 1, 0 );
		this.add( this.cone );

		setPosition( origin );
		setDirection( dir );
		setLength( length );
	}
	
	public void setDirection( Vector3 dir ) 
	{
		Vector3 axis = new Vector3( 0, 1, 0 ).cross( dir );

		double radians = Math.acos( new Vector3( 0, 1, 0 ).dot( dir.clone().normalize() ) );

		this.matrix = new Matrix4().makeRotationAxis( axis.normalize(), radians );

		this.rotation.setEulerFromRotationMatrix( this.matrix, this.eulerOrder );
	}

	public void setLength( double length ) 
	{
		this.scale.set( length );
	}

	public void setColor( int hex ) 
	{
		LineBasicMaterial lMaterial = (LineBasicMaterial) this.line.getMaterial();
		lMaterial.getColor().setHex( hex );

		MeshBasicMaterial mMaterial = (MeshBasicMaterial) this.cone.getMaterial();
		mMaterial.getColor().setHex( hex );
	}
}
