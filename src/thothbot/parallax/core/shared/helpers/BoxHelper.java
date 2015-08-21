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

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferAttribute;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;

public class BoxHelper extends Line 
{
	public BoxHelper(Mesh object) 
	{
		super(new BufferGeometry(), new LineBasicMaterial(), Line.MODE.PIECES);
		
		BufferGeometry geometry = (BufferGeometry) getGeometry();
		LineBasicMaterial material = (LineBasicMaterial) getMaterial();
		material.setColor(new Color(0xffff00));
		
		
		geometry.addAttribute( "position", new BufferAttribute( Float32Array.create( 72 ), 3 ) );

		update( object );

	}
	
	public void update( Mesh object ) 
	{

		AbstractGeometry geometry = object.getGeometry();

		if ( geometry.getBoundingBox() == null ) {

			geometry.computeBoundingBox();

		}

		Vector3 min = geometry.getBoundingBox().getMin();
		Vector3 max = geometry.getBoundingBox().getMax();

		/*
		  5____4
		1/___0/|
		| 6__|_7
		2/___3/

		0: max.x, max.y, max.z
		1: min.x, max.y, max.z
		2: min.x, min.y, max.z
		3: max.x, min.y, max.z
		4: max.x, max.y, min.z
		5: min.x, max.y, min.z
		6: min.x, min.y, min.z
		7: max.x, min.y, min.z
		*/

		Float32Array vertices = (Float32Array) ((BufferGeometry)this.geometry).getAttribute("position").getArray();

		vertices.set(  0 , max.getX() ); vertices.set(  1 , max.getY() ); vertices.set(  2 , max.getZ() );
		vertices.set(  3 , min.getX() ); vertices.set(  4 , max.getY() ); vertices.set(  5 , max.getZ() );

		vertices.set(  6 , min.getX() ); vertices.set(  7 , max.getY() ); vertices.set(  8 , max.getZ() );
		vertices.set(  9 , min.getX() ); vertices.set( 10 , min.getY() ); vertices.set( 11 , max.getZ() );

		vertices.set( 12 , min.getX() ); vertices.set( 13 , min.getY() ); vertices.set( 14 , max.getZ() );
		vertices.set( 15 , max.getX() ); vertices.set( 16 , min.getY() ); vertices.set( 17 , max.getZ() );

		vertices.set( 18 , max.getX() ); vertices.set( 19 , min.getY() ); vertices.set( 20 , max.getZ() );
		vertices.set( 21 , max.getX() ); vertices.set( 22 , max.getY() ); vertices.set( 23 , max.getZ() );

		//

		vertices.set( 24 , max.getX() ); vertices.set( 25 , max.getY() ); vertices.set( 26 , min.getZ() );
		vertices.set( 27 , min.getX() ); vertices.set( 28 , max.getY() ); vertices.set( 29 , min.getZ() );

		vertices.set( 30 , min.getX() ); vertices.set( 31 , max.getY() ); vertices.set( 32 , min.getZ() );
		vertices.set( 33 , min.getX() ); vertices.set( 34 , min.getY() ); vertices.set( 35 , min.getZ() );

		vertices.set( 36 , min.getX() ); vertices.set( 37 , min.getY() ); vertices.set( 38 , min.getZ() );
		vertices.set( 39 , max.getX() ); vertices.set( 40 , min.getY() ); vertices.set( 41 , min.getZ() );

		vertices.set( 42 , max.getX() ); vertices.set( 43 , min.getY() ); vertices.set( 44 , min.getZ() );
		vertices.set( 45 , max.getX() ); vertices.set( 46 , max.getY() ); vertices.set( 47 , min.getZ() );

		//

		vertices.set( 48 , max.getX() ); vertices.set( 49 , max.getY() ); vertices.set( 50 , max.getZ() );
		vertices.set( 51 , max.getX() ); vertices.set( 52 , max.getY() ); vertices.set( 53 , min.getZ() );

		vertices.set( 54 , min.getX() ); vertices.set( 55 , max.getY() ); vertices.set( 56 , max.getZ() );
		vertices.set( 57 , min.getX() ); vertices.set( 58 , max.getY() ); vertices.set( 59 , min.getZ() );

		vertices.set( 60 , min.getX() ); vertices.set( 61 , min.getY() ); vertices.set( 62 , max.getZ() );
		vertices.set( 63 , min.getX() ); vertices.set( 64 , min.getY() ); vertices.set( 65 , min.getZ() );

		vertices.set( 66 , max.getX() ); vertices.set( 67 , min.getY() ); vertices.set( 68 , max.getZ() );
		vertices.set( 69 , max.getX() ); vertices.set( 70 , min.getY() ); vertices.set( 71 , min.getZ() );

		((BufferGeometry)this.geometry).getAttribute("position").setNeedsUpdate(true);

		this.geometry.computeBoundingSphere();

		setMatrix( object.getMatrixWorld() );
		this.setMatrixAutoUpdate(false);

	}

}
