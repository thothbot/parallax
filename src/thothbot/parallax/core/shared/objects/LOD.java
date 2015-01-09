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

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.math.Matrix4;


public class LOD extends Object3D 
{

	public List<LOD> LODs;
	public double visibleAtDistance;
	public Object3D  object3D;
	
	public LOD() 
	{
		this.LODs = new ArrayList<LOD>();
	}
	
	public void addLevel ( Object3D object3D) 
	{
		addLevel(object3D, 0);
	}

	// TODO: What is this?
	public void addLevel ( Object3D object3D, double visibleAtDistance ) 
	{
		visibleAtDistance = Math.abs( visibleAtDistance );

		for(LOD lod: this.LODs)
			if(visibleAtDistance < lod.visibleAtDistance)
				break;

		// ??
		LOD lod = this.LODs.get(1);
		lod.visibleAtDistance = visibleAtDistance;
		lod.object3D = object3D;

		this.add( object3D );
	}

	public void update ( Camera camera ) 
	{
		if(this.LODs.size() <= 1) return;

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );

		Matrix4 inverse  = camera.getMatrixWorldInverse();
		double distance = -( inverse.getArray().get(2) 
				* this.matrixWorld.getArray().get(12) + inverse.getArray().get(6) 
				* this.matrixWorld.getArray().get(13) + inverse.getArray().get(10) 
				* this.matrixWorld.getArray().get(14) + inverse.getArray().get(14) );

			this.LODs.get(0).object3D.setVisible(true);

			for ( int l = 1; l < this.LODs.size(); l ++ ) 
			{
				if( distance >= this.LODs.get( l ).visibleAtDistance ) 
				{
					this.LODs.get( l - 1 ).object3D.setVisible(false);
					this.LODs.get( l     ).object3D.setVisible(true);
				} 
				else 
				{
					break;
				}
			}

			for(LOD lod: this.LODs)
				lod.object3D.setVisible(false);
	}
}
