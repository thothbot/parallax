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

package thothbot.parallax.core.shared.cameras;

import thothbot.parallax.core.client.events.HasEventBus;
import thothbot.parallax.core.client.events.ViewportResizeEvent;
import thothbot.parallax.core.client.events.ViewportResizeHandler;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Quaternion;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.objects.Object3D;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract base class for cameras.
 * 
 * @author thothbot
 *
 */
public class Camera extends Object3D implements HasEventBus, ViewportResizeHandler
{
	protected Matrix4 matrixWorldInverse;
	protected Matrix4 projectionMatrix;
	protected Matrix4 projectionMatrixInverse;

	public Float32Array _viewMatrixArray;
	public Float32Array _projectionMatrixArray;
	
	public Camera() 
	{
		super();

		this.matrixWorldInverse = new Matrix4();
		this.projectionMatrix = new Matrix4();
		this.projectionMatrixInverse = new Matrix4();
		
		this._viewMatrixArray = Float32Array.create( 16 );
		this._projectionMatrixArray = Float32Array.create( 16 );
		
		addViewportResizeHandler(this);
	}
	
	public HandlerRegistration addViewportResizeHandler(ViewportResizeHandler handler) 
	{
		return EVENT_BUS.addHandler(ViewportResizeEvent.TYPE, handler);
	}
	
	@Override
	public void onResize(ViewportResizeEvent event) {
		//  Empty for capability
	}

	public Matrix4 getMatrixWorldInverse()
	{
		return this.matrixWorldInverse;
	}

	public void setMatrixWorldInverse(Matrix4 matrixWorldInverse)
	{
		this.matrixWorldInverse = matrixWorldInverse;
	}

	public Matrix4 getProjectionMatrix()
	{
		return this.projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4 projectionMatrix)
	{
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4 getProjectionMatrixInverse()
	{
		return this.projectionMatrixInverse;
	}

	public void setProjectionMatrixInverse(Matrix4 projectionMatrix)
	{
		this.projectionMatrixInverse = projectionMatrix;
	}

	@Override
	public void lookAt(Vector3 vector)
	{
		this.matrix.lookAt(this.position, vector, this.up);

		if ( isRotationAutoUpdate() )
		{
			if ( isUseQuaternion() )  
			{
				Quaternion q = new Quaternion();
				getMatrix().decompose(new Vector3(), q, new Vector3());
				this.quaternion.copy( q );
			}
			else 
			{
				this.rotation.setEulerFromRotationMatrix( this.matrix, this.eulerOrder );
			}
		}
	}
}
