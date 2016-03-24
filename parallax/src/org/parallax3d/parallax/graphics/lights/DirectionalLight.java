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

import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * 
 * <pre>
 * {@code
 * // White directional light at half intensity shining from the top. 
 * 
 * DirectionalLight light = new DirectionalLight( 0xffffff, 0.5 ); 
 * light.position.set( 0, 1, 0 ); 
 * getScene().add( light );
 * }
 * </pre>
 * @author thothbot
 *
 */
@ThreejsObject("THREE.DirectionalLight")
public class DirectionalLight extends Light implements HasShadow<OrthographicCamera>
{

	Object3D target;

	LightShadow<OrthographicCamera> shadow;

	public DirectionalLight() {
		this(0xffffff);
	}

	public DirectionalLight(int hex)
	{
		this(hex, 1.0);
	}

	public DirectionalLight(int hex, double intensity)
	{
		super(hex, intensity);

		this.updateMatrix();

		this.target = new Object3D();

		this.shadow = new LightShadow( new OrthographicCamera( - 5, 5, 5, - 5, 0.5, 500 ) );
	}

	public Object3D getTarget() {
		return target;
	}

	public void setTarget(Object3D target) {
		this.target = target;
	}

	@Override
	public LightShadow<OrthographicCamera> getShadow() {
		return shadow;
	}

	@Override
	public void setShadow(LightShadow<OrthographicCamera> shadow) {
		this.shadow = shadow;
	}

	public DirectionalLight copy(DirectionalLight source ) {

		super.copy( source );

		this.target = source.target.clone();

		this.shadow = source.shadow.clone();

		return this;

	}

	@Override
	public DirectionalLight clone() {
		return new DirectionalLight().copy(this);
	}
}
