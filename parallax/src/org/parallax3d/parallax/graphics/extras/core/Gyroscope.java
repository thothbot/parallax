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

package org.parallax3d.parallax.graphics.extras.core;

import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.math.Quaternion;

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
		if(this.isMatrixAutoUpdate())
			this.updateMatrix();

		// update matrixWorld

		if ( this.isMatrixWorldNeedsUpdate() || force )
		{
			if ( this.getParent() != null )
			{
				this.getMatrixWorld().multiply( this.getParent().getMatrixWorld(), this.getMatrix());

				this.getMatrixWorld().decompose( this.translationWorld, this.rotationWorld, this.scaleWorld );
				this.getMatrix().decompose( this.translationObject, this.rotationObject, this.scaleObject );

				this.getMatrixWorld().compose( this.translationWorld, this.rotationObject, this.scaleWorld );
			}
			else
			{
				this.getMatrixWorld().copy(this.getMatrix());
			}

			this.setMatrixWorldNeedsUpdate(false);

			force = true;
		}

		// update children

		for (int i = 0, l = this.getChildren().size(); i < l; i ++ )
		{
			this.getChildren().get( i ).updateMatrixWorld( force );
		}
	}
}
