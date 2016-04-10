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

import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.math.Box3;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

@ThreejsObject("THREE.BoxHelper")
public class BoxHelper extends LineSegments 
{
	public BoxHelper(Object3D object)
	{
		super( intDefaultGeometry(), new LineBasicMaterial().setColor(0xffff00) );
		this.update( object );
	}

	private static BufferGeometry intDefaultGeometry() {
		Uint32Array indices = Uint32Array.create( new int[] { 0, 1, 1, 2, 2, 3, 3, 0, 4, 5, 5, 6, 6, 7, 7, 4, 0, 4, 1, 5, 2, 6, 3, 7 } );
		Float32Array positions = Float32Array.create( 8 * 3 );

		BufferGeometry geometry = new BufferGeometry();
		geometry.setIndex( new BufferAttribute( indices, 1 ) );
		geometry.addAttribute( "position", new BufferAttribute( positions, 3 ) );
		
		return geometry;
	}

	static final Box3 box = new Box3();
	public void update( Object3D object )
	{

		box.setFromObject( object );

		if ( box.isEmpty() ) return;

		Vector3 min = box.getMin();
		Vector3 max = box.getMax();

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

		BufferAttribute position = ((BufferGeometry) this.geometry).getAttributes().get("position");
		Float32Array array = (Float32Array) position.getArray();

		array.set( 0, max.getX()); array.set( 1, max.getY()); array.set( 2, max.getZ());
		array.set( 3, min.getX()); array.set( 4, max.getY()); array.set( 5, max.getZ());
		array.set( 6, min.getX()); array.set( 7, min.getY()); array.set( 8, max.getZ());
		array.set( 9, max.getX()); array.set( 10, min.getY()); array.set( 11, max.getZ());
		array.set( 12, max.getX()); array.set( 13, max.getY()); array.set( 14, min.getZ());
		array.set( 15, min.getX()); array.set( 16, max.getY()); array.set( 17, min.getZ());
		array.set( 18, min.getX()); array.set( 19, min.getY()); array.set( 20, min.getZ());
		array.set( 21, max.getX()); array.set( 22, min.getY()); array.set( 23, min.getZ());

		position.setNeedsUpdate(true);

		this.geometry.computeBoundingSphere();

	}

}
