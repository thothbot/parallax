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
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;

public class ArrowHelper extends Object3D
{
	private Line line;
	private Mesh cone;

	public ArrowHelper ( Vector3 dir, Vector3 origin)
	{
		this(dir, origin, 1);
	}
	
	public ArrowHelper ( Vector3 dir, Vector3 origin, double length)
	{
		this(dir, origin, length, 0xffff00);
	}
	
	public ArrowHelper ( Vector3 dir, Vector3 origin, double length, int color) 
	{	
		this(dir, origin, length, color, 0.2 * length, 0.2 * 0.2 * length);
	}

	public ArrowHelper ( Vector3 dir, Vector3 origin, double length, int color, double headLength, double headWidth ) 
	{
		super();
		
		this.getPosition().copy(origin);
		
		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3( 0, 0, 0 ) );
		lineGeometry.getVertices().add( new Vector3( 0, 1, 0 ) );

		CylinderGeometry coneGeometry = new CylinderGeometry( 0, 0.5, 1, 5, 1 );
		coneGeometry.applyMatrix( new Matrix4().makeTranslation( 0, - 0.5, 0 ) );
		
		LineBasicMaterial lbm = new LineBasicMaterial();
		lbm.setColor(new Color(color));
		this.line = new Line( lineGeometry, lbm );
		this.line.setMatrixAutoUpdate(false);
		this.add( this.line );

		MeshBasicMaterial mbm = new MeshBasicMaterial();
		mbm.setColor(new Color(color));
		this.cone = new Mesh( coneGeometry, mbm );
		this.cone.setMatrixAutoUpdate(false);
		this.add( this.cone );

		setDirection( dir );
		setLength( length, headLength, headWidth );
	}
	
	public void setDirection( Vector3 dir ) 
	{
		Vector3 axis = new Vector3();
		double radians;

		// dir is assumed to be normalized

		if ( dir.getY() > 0.99999 ) {

			this.quaternion.set( 0, 0, 0, 1 );

		} else if ( dir.getY() < - 0.99999 ) {

			this.quaternion.set( 1, 0, 0, 0 );

		} else {

			axis.set( dir.getZ(), 0, - dir.getX() ).normalize();

			radians = Math.acos( dir.getY() );

			this.quaternion.setFromAxisAngle( axis, radians );

		}

	}
	
	public void setLength ( double length ) {
		
		double headLength = 0.2 * length;
		double headWidth = 0.2 * headLength;
		
		setLength(length, headLength, headWidth);
	}
	
	public void setLength ( double length, double headLength, double headWidth ) {

		this.line.getScale().set( 1, length, 1 );
		this.line.updateMatrix();

		this.cone.getScale().set( headWidth, headLength, headWidth );
		this.cone.getPosition().setY( length );
		this.cone.updateMatrix();

	}

	public void setColor( int hex ) 
	{
		LineBasicMaterial lMaterial = (LineBasicMaterial) this.line.getMaterial();
		lMaterial.getColor().setHex( hex );

		MeshBasicMaterial mMaterial = (MeshBasicMaterial) this.cone.getMaterial();
		mMaterial.getColor().setHex( hex );
	}
}
