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

package thothbot.squirrel.core.shared.objects;

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.shared.cameras.Camera;
import thothbot.squirrel.core.shared.core.Matrix4f;


public class LOD extends Object3D 
{

	public List<LOD> LODs;
	public float visibleAtDistance;
	public Object3D  object3D;
	
	public LOD() {
		this.LODs = new ArrayList<LOD>();
	}
	
	public void addLevel ( Object3D object3D) {
		addLevel(object3D, 0f);
	}

	// TODO: What is this?
	public void addLevel ( Object3D object3D, float visibleAtDistance ) {
		visibleAtDistance = Math.abs( visibleAtDistance );

		for(LOD lod: this.LODs)
			if(visibleAtDistance < lod.visibleAtDistance)
				break;

		// ??
		LOD lod = this.LODs.get(1);
		lod.visibleAtDistance = visibleAtDistance;
		lod.object3D = object3D;

		this.addChild( object3D );
	}

	public void update ( Camera camera ) 
	{
		if(this.LODs.size() <= 1) return;

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );

		Matrix4f inverse  = camera.getMatrixWorldInverse();
		float distance = -( inverse.getArray().get(2) 
				* this.matrixWorld.getArray().get(12) + inverse.getArray().get(6) 
				* this.matrixWorld.getArray().get(13) + inverse.getArray().get(10) 
				* this.matrixWorld.getArray().get(14) + inverse.getArray().get(14) );

			this.LODs.get(0).object3D.setVisible(true);

			for ( int l = 1; l < this.LODs.size(); l ++ ) {
				if( distance >= this.LODs.get( l ).visibleAtDistance ) {
					this.LODs.get( l - 1 ).object3D.setVisible(false);
					this.LODs.get( l     ).object3D.setVisible(true);
				} else {
					break;
				}
			}

			for(LOD lod: this.LODs)
				lod.object3D.setVisible(false);
	}
}
