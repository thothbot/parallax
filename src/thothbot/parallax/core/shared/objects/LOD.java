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

	private List<LOD> LODs;
	private double visibleAtDistance;
	private Object3D  object3D;
	
	public LOD() 
	{
		this.setLODs(new ArrayList<LOD>());
	}
	
	public void addLevel ( Object3D object3D) 
	{
		addLevel(object3D, 0);
	}

	// TODO: What is this?
	public void addLevel ( Object3D object3D, double visibleAtDistance ) 
	{
		visibleAtDistance = Math.abs( visibleAtDistance );

		for(LOD lod: this.getLODs())
			if(visibleAtDistance < lod.getVisibleAtDistance())
				break;

		// ??
		LOD lod = this.getLODs().get(1);
		lod.setVisibleAtDistance(visibleAtDistance);
		lod.setObject3D(object3D);

		this.add( object3D );
	}

	public void update ( Camera camera ) 
	{
		if(this.getLODs().size() <= 1) return;

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );

		Matrix4 inverse  = camera.getMatrixWorldInverse();
		double distance = -( inverse.getArray().get(2) 
				* this.matrixWorld.getArray().get(12) + inverse.getArray().get(6) 
				* this.matrixWorld.getArray().get(13) + inverse.getArray().get(10) 
				* this.matrixWorld.getArray().get(14) + inverse.getArray().get(14) );

			this.getLODs().get(0).getObject3D().setVisible(true);

			for ( int l = 1; l < this.getLODs().size(); l ++ ) 
			{
				if( distance >= this.getLODs().get( l ).getVisibleAtDistance() ) 
				{
					this.getLODs().get( l - 1 ).getObject3D().setVisible(false);
					this.getLODs().get( l     ).getObject3D().setVisible(true);
				} 
				else 
				{
					break;
				}
			}

			for(LOD lod: this.getLODs())
				lod.getObject3D().setVisible(false);
	}

	/**
	 * @return the object3D
	 */
	public Object3D getObject3D() {
		return object3D;
	}

	/**
	 * @param object3d the object3D to set
	 */
	public void setObject3D(Object3D object3d) {
		object3D = object3d;
	}

	/**
	 * @return the visibleAtDistance
	 */
	public double getVisibleAtDistance() {
		return visibleAtDistance;
	}

	/**
	 * @param visibleAtDistance the visibleAtDistance to set
	 */
	public void setVisibleAtDistance(double visibleAtDistance) {
		this.visibleAtDistance = visibleAtDistance;
	}

	/**
	 * @return the lODs
	 */
	public List<LOD> getLODs() {
		return LODs;
	}

	/**
	 * @param lODs the lODs to set
	 */
	public void setLODs(List<LOD> lODs) {
		LODs = lODs;
	}
}
