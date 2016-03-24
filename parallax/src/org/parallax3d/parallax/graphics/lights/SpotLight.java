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

import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * A point light that can cast shadow in one direction.
 * <p>
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 *
 * @author thothbot
 *
 */
@ThreejsObject("THREE.SpotLight")
public class SpotLight extends Light implements HasShadow<PerspectiveCamera>
{

	Object3D target = new Object3D();

	double distance;
	double angle;
	double penumbra;
	double decay;

	LightShadow<PerspectiveCamera> shadow;

	public SpotLight(int hex)
	{
		this(hex, 1.0);
	}

	public SpotLight(int hex, double intensity)
	{
		this(hex, intensity, 0, Math.PI / 3., 0, 1);
	}

	public SpotLight(int hex, double intensity, double distance, double angle, double penumbra, double decay)
	{
		super(hex, intensity);

		this.distance = distance;
		this.angle = angle;
		this.penumbra = penumbra;
		this.decay = decay;

		this.getPosition().set( 0, 1, 0 );
		this.updateMatrix();

		this.shadow = new LightShadow( new PerspectiveCamera( 50, 1, 0.5, 500 ) );
	}

	public Object3D getTarget() {
		return target;
	}

	public void setTarget(Object3D target) {
		this.target = target;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getPenumbra() {
		return penumbra;
	}

	public void setPenumbra(double penumbra) {
		this.penumbra = penumbra;
	}

	public double getDecay() {
		return decay;
	}

	public void setDecay(double decay) {
		this.decay = decay;
	}

	@Override
	public LightShadow<PerspectiveCamera> getShadow() {
		return shadow;
	}

	@Override
	public void setShadow(LightShadow<PerspectiveCamera> shadow) {
		this.shadow = shadow;
	}

	public SpotLight copy(SpotLight source ) {

		super.copy( source );

		this.distance = source.distance;
		this.angle = source.angle;
		this.penumbra = source.penumbra;
		this.decay = source.decay;

		this.target = source.target.clone();

		this.shadow = source.shadow.clone();

		return this;

	}

	@Override
	public SpotLight clone() {
		return new SpotLight(0x000000).copy(this);
	}
}
