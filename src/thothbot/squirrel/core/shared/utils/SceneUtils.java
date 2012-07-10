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

package thothbot.squirrel.core.shared.utils;

import java.util.List;

import thothbot.squirrel.core.shared.core.DimentionObject;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.Object3D;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.objects.Mesh;


public class SceneUtils
{
	public static DimentionObject createMultiMaterialObject( Geometry geometry, List<Material> materials ) 
	{
		DimentionObject group = new Object3D();

		for ( int i = 0; i < materials.size(); i ++ ) 
		{
			Mesh object = new Mesh( geometry, materials.get( i ) );
			group.addChild( object );
		}

		return group;
	}
}
