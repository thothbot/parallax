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

package org.parallax3d.parallax.demo.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;

/**
 * A dodgy sun/sky shader. Makes a bright spot at the sun location. Would be
 * cheaper/faster/simpler to implement this as a simple sun sprite.
 * <p>
 * The code from three.js code
 */
public final class GodraysFakeSunShader extends Shader
{

	interface Resources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@ClientBundle.Source("shaders/godrays.vs")
		TextResource getVertexShader();

		@ClientBundle.Source("shaders/godraysFakeSun.fs")
		TextResource getFragmentShader();
	}
	
	public GodraysFakeSunShader() 
	{
		super(Resources.INSTANCE.getVertexShader().getText(), Resources.INSTANCE.getFragmentShader().getText());
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("vSunPositionScreenSpace", new Uniform(Uniform.TYPE.V2, new Vector2( 0.5, 0.5 )));
		this.addUniform("fAspect", new Uniform(Uniform.TYPE.F, 1.0));
		this.addUniform("sunColor", new Uniform(Uniform.TYPE.C, new Color(0xffee00)));
		this.addUniform("bgColor", new Uniform(Uniform.TYPE.C, new Color(0x000000)));
	}
}
