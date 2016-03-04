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

package org.parallax3d.parallax.graphics.lights;

import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;

/**
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * 
 * <pre>
 * {@code
 * PointLight light = new PointLight( 0xff0000, 1, 100 ); 
 * light.getPosition().set( 50, 50, 50 ); 
 * getScene().add( light );
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
public class PointRTLight extends PointLight implements HasRaytracingPhysicalAttenuation
{
	private boolean isPhysicalAttenuation;

	public PointRTLight(int hex) {
		super(hex);
	}

	@Override
	public boolean isPhysicalAttenuation() {
		return isPhysicalAttenuation;
	}

	@Override
	public void setPhysicalAttenuation(boolean isPhysicalAttenuation) {
		this.isPhysicalAttenuation = isPhysicalAttenuation;
	}

	public PointRTLight clone() {

		PointRTLight light = new PointRTLight(0x000000);

		super.clone(light);

		light.isPhysicalAttenuation = this.isPhysicalAttenuation;

		return light;

	}
}
