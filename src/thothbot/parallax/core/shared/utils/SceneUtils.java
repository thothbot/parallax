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

package thothbot.parallax.core.shared.utils;

import java.util.List;

import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;

/**
 * The class implements some 3D Scene related helper methods
 * 
 * The code is based on js-code written by alteredq 
 * http://alteredqualia.com/
 * 
 * @author thothbot
 *
 */
public class SceneUtils
{
	/**
	 * This method creates multi-material 3D object which contains Mesh objects
	 * in amount of the materials list size. Here are every Mesh object will use
	 * one of material of materials list.
	 * 
	 * @param geometry  the input geometry.
	 * @param list the list of materials.
	 * 
	 * @return the new instance of {@link Object3D}
	 */
	public static Object3D createMultiMaterialObject( Geometry geometry, List<? extends Material> materials ) 
	{
		Object3D group = new Object3D();

		for ( int i = 0, l = materials.size(); i < l; i ++ ) {

			group.add( new Mesh( geometry, materials.get( i ) ) );

		}

		return group;
	}
	
	public static void detach( Object3D child, Object3D parent, Scene scene ) {

		child.applyMatrix( parent.getMatrixWorld() );
		parent.remove( child );
		scene.add( child );

	}

	public static void attach ( Object3D child, Scene scene, Object3D parent ) {

		Matrix4 matrixWorldInverse = new Matrix4();
		matrixWorldInverse.getInverse( parent.getMatrixWorld() );
		child.applyMatrix( matrixWorldInverse );

		scene.remove( child );
		parent.add( child );

	}

}
