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

package org.parallax3d.parallax.graphics.materials;

/**
 * A material for shiny surfaces, evaluated per pixel.
 *
 */
public final class MeshPhongRTMaterial extends MeshPhongMaterial implements HasRaytracingMirror, HasRaytracingGlass

{
	private boolean isMirror;
	private boolean isGlass;

	@Override
	public boolean isGlass() {
		return isGlass;
	}

	@Override
	public MeshPhongRTMaterial setGlass(boolean isGlass) {
		this.isGlass = isGlass;
		return this;
	}

	@Override
	public boolean isMirror() {
		return isMirror;
	}

	@Override
	public MeshPhongRTMaterial setMirror(boolean isMirror) {
		this.isMirror = isMirror;
		return this;
	}

	@Override
	public MeshPhongRTMaterial clone() {

		MeshPhongRTMaterial material = new MeshPhongRTMaterial();

		super.clone(material);

		material.isMirror = this.isMirror;
		material.isGlass = this.isGlass;

		return material;

	}
}
