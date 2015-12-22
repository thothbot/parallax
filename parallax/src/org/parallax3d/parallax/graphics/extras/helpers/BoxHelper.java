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

import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;

import java.nio.FloatBuffer;

@ThreeJsObject("THREE.BoxHelper")
public class BoxHelper extends Line 
{
	public BoxHelper(Mesh object)
	{
		super(new BufferGeometry(), new LineBasicMaterial(), MODE.PIECES);
		
		BufferGeometry geometry = (BufferGeometry) getGeometry();
		LineBasicMaterial material = (LineBasicMaterial) getMaterial();
		material.setColor(new Color(0xffff00));
		
		
		geometry.addAttribute( "position", new BufferAttribute(BufferUtils.newFloatBuffer(72), 3 ) );

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

		FloatBuffer vertices = (FloatBuffer) ((BufferGeometry)this.geometry).getAttribute("position").getArray();

		vertices.put(  0 , max.getX() ); vertices.put(  1 , max.getY() ); vertices.put(  2 , max.getZ() );
		vertices.put(  3 , min.getX() ); vertices.put(  4 , max.getY() ); vertices.put(  5 , max.getZ() );

		vertices.put(  6 , min.getX() ); vertices.put(  7 , max.getY() ); vertices.put(  8 , max.getZ() );
		vertices.put(  9 , min.getX() ); vertices.put( 10 , min.getY() ); vertices.put( 11 , max.getZ() );

		vertices.put( 12 , min.getX() ); vertices.put( 13 , min.getY() ); vertices.put( 14 , max.getZ() );
		vertices.put( 15 , max.getX() ); vertices.put( 16 , min.getY() ); vertices.put( 17 , max.getZ() );

		vertices.put( 18 , max.getX() ); vertices.put( 19 , min.getY() ); vertices.put( 20 , max.getZ() );
		vertices.put( 21 , max.getX() ); vertices.put( 22 , max.getY() ); vertices.put( 23 , max.getZ() );

		//

		vertices.put( 24 , max.getX() ); vertices.put( 25 , max.getY() ); vertices.put( 26 , min.getZ() );
		vertices.put( 27 , min.getX() ); vertices.put( 28 , max.getY() ); vertices.put( 29 , min.getZ() );

		vertices.put( 30 , min.getX() ); vertices.put( 31 , max.getY() ); vertices.put( 32 , min.getZ() );
		vertices.put( 33 , min.getX() ); vertices.put( 34 , min.getY() ); vertices.put( 35 , min.getZ() );

		vertices.put( 36 , min.getX() ); vertices.put( 37 , min.getY() ); vertices.put( 38 , min.getZ() );
		vertices.put( 39 , max.getX() ); vertices.put( 40 , min.getY() ); vertices.put( 41 , min.getZ() );

		vertices.put( 42 , max.getX() ); vertices.put( 43 , min.getY() ); vertices.put( 44 , min.getZ() );
		vertices.put( 45 , max.getX() ); vertices.put( 46 , max.getY() ); vertices.put( 47 , min.getZ() );

		//

		vertices.put( 48 , max.getX() ); vertices.put( 49 , max.getY() ); vertices.put( 50 , max.getZ() );
		vertices.put( 51 , max.getX() ); vertices.put( 52 , max.getY() ); vertices.put( 53 , min.getZ() );

		vertices.put( 54 , min.getX() ); vertices.put( 55 , max.getY() ); vertices.put( 56 , max.getZ() );
		vertices.put( 57 , min.getX() ); vertices.put( 58 , max.getY() ); vertices.put( 59 , min.getZ() );

		vertices.put( 60 , min.getX() ); vertices.put( 61 , min.getY() ); vertices.put( 62 , max.getZ() );
		vertices.put( 63 , min.getX() ); vertices.put( 64 , min.getY() ); vertices.put( 65 , min.getZ() );

		vertices.put( 66 , max.getX() ); vertices.put( 67 , min.getY() ); vertices.put( 68 , max.getZ() );
		vertices.put( 69 , max.getX() ); vertices.put( 70 , min.getY() ); vertices.put( 71 , min.getZ() );

		((BufferGeometry)this.geometry).getAttribute("position").setNeedsUpdate(true);

		this.geometry.computeBoundingSphere();

		setMatrix( object.getMatrixWorld() );
		this.setMatrixAutoUpdate(false);

	}

}
