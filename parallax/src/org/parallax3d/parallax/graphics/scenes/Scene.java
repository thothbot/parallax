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

package org.parallax3d.parallax.graphics.scenes;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.Material;

/**
 * 3D Scene. The basic class for rendering.
 * To use this Object in rendering you should add at least one {@link Object3D}
 * and {@link Camera}.
 * Also you may want to add Lights and {@link AbstractFog} to the Scene Object. 
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Scene")
public final class Scene extends Object3D
{
	/**
	 * Here is stored fog
	 */
	private AbstractFog fog;

	private Material overrideMaterial;

	private boolean isAutoUpdate = true;

	/**
	 * This default constructor will create new Scene instance.
	 */
	public Scene()
	{
		super();
	}


	/**
	 * Get fogAbstract associated with the Scene.
	 *
	 * @return the fogAbstract
	 */
	public AbstractFog getFog()
	{
		return this.fog;
	}

	/**
	 * Set the fogAbstract to the Scene.
	 *
	 * @param fog	the AbstractFog
	 */
	public void setFog(AbstractFog fog)
	{
		this.fog = fog;
	}

	public Material getOverrideMaterial() {
		return overrideMaterial;
	}


	public void setOverrideMaterial(Material overrideMaterial) {
		this.overrideMaterial = overrideMaterial;
	}


	public boolean isAutoUpdate() {
		return isAutoUpdate;
	}


	public void setAutoUpdate(boolean isAutoUpdate) {
		this.isAutoUpdate = isAutoUpdate;
	}

	public Scene clone() {
		return clone(new Scene());
	}

	public Scene clone(Scene object ) {

		super.clone(object);

		if ( this.fog != null )
			object.fog = this.fog.clone();
		if ( this.overrideMaterial != null )
			object.overrideMaterial = this.overrideMaterial.clone();

		object.isAutoUpdate = this.isAutoUpdate;
		object.matrixAutoUpdate = this.matrixAutoUpdate;

		return object;

	};
}
