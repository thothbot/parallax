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

package thothbot.parallax.core.shared.core;

import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;

public class Gyroscope extends Object3D 
{
	private Vector3 translationWorld;
	private Vector3 translationObject;
	private Quaternion rotationWorld;
	private Quaternion rotationObject;
	private Vector3 scaleWorld;
	private Vector3 scaleObject;
	
	public Gyroscope() 
	{
		this.translationWorld = new Vector3();
		this.translationObject = new Vector3();
		this.rotationWorld = new Quaternion();
		this.rotationObject = new Quaternion();
		this.scaleWorld = new Vector3();
		this.scaleObject = new Vector3();
	}

	@Override
	public void updateMatrixWorld( boolean force ) 
	{
		if(this.matrixAutoUpdate)
			this.updateMatrix();

		// update matrixWorld

		if ( this.matrixWorldNeedsUpdate || force ) 
		{
			if ( this.parent != null ) 
			{
				this.matrixWorld.multiply( this.parent.getMatrixWorld(), this.matrix );

				this.matrixWorld.decompose( this.translationWorld, this.rotationWorld, this.scaleWorld );
				this.matrix.decompose( this.translationObject, this.rotationObject, this.scaleObject );

				this.matrixWorld.compose( this.translationWorld, this.rotationObject, this.scaleWorld );
			} 
			else 
			{
				this.matrixWorld.copy( this.matrix );
			}

			this.matrixWorldNeedsUpdate = false;

			force = true;
		}

		// update children

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) 
		{
			this.children.get( i ).updateMatrixWorld( force );
		}
	}
}
