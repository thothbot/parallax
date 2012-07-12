/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.helpers;

import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.Matrix4f;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.geometries.Cylinder;
import thothbot.squirrel.core.shared.materials.LineBasicMaterial;
import thothbot.squirrel.core.shared.materials.MeshBasicMaterial;
import thothbot.squirrel.core.shared.objects.Line;
import thothbot.squirrel.core.shared.objects.Mesh;
import thothbot.squirrel.core.shared.objects.Object3D;

public class ArrowHelper extends Object3D
{

	public Line line;
	public Mesh cone;
	
	public ArrowHelper ( Vector3f dir, Vector3f origin)
	{
		this(dir, origin, 20f);
	}
	
	public ArrowHelper ( Vector3f dir, Vector3f origin, float length)
	{
		this(dir, origin, length, 0xffff00);
	}

	public ArrowHelper ( Vector3f dir, Vector3f origin, float length, int hex ) 
	{
		super();

		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3f( 0, 0, 0 ) );
		lineGeometry.getVertices().add( new Vector3f( 0, 1, 0 ) );

		LineBasicMaterial.LineBasicMaterialOptions lOptions = new LineBasicMaterial.LineBasicMaterialOptions();
		lOptions.color = new Color3f(hex);
		this.line = new Line( lineGeometry, new LineBasicMaterial( lOptions ) );
		this.addChild( this.line );

		Cylinder coneGeometry = new Cylinder( 0f, 0.05f, 0.25f, 5, 1 );

		MeshBasicMaterial.MeshBasicMaterialOptions mOptions = new MeshBasicMaterial.MeshBasicMaterialOptions();
		mOptions.color = new Color3f(hex);
		this.cone = new Mesh( coneGeometry, new MeshBasicMaterial( mOptions ) );
		this.cone.getPosition().set( 0, 1, 0 );
		this.addChild( this.cone );

		if ( origin instanceof Vector3f ) this.position = origin;

		setDirection( dir );
		setLength( length );
	}
	
	public void setDirection( Vector3f dir ) 
	{
		Vector3f axis = new Vector3f( 0, 1, 0 ).cross( dir );

		float radians = (float) Math.acos( new Vector3f( 0, 1, 0 ).dot( dir.clone().normalize() ) );

		this.matrix = new Matrix4f().makeRotationAxis( axis.normalize(), radians );

		this.rotation.getRotationFromMatrix( this.matrix, this.scale );

	}

	public void setLength( float length ) 
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
