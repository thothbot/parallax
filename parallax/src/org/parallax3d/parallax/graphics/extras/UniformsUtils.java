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

package org.parallax3d.parallax.graphics.extras;

import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * The class implements some Uniform related helper methods 
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.UniformsUtils")
public class UniformsUtils
{
	private UniformsUtils() {}

	/**
	 * Merge two uniforms into one new result uniform.
	 *
	 * @param uniforms1 the map of first uniforms
	 * @param uniforms2 the map of second uniforms
	 *
	 * @return new instance of Uniform Map
	 */
	public static FastMap<Uniform> merge( FastMap<Uniform> uniforms1, FastMap<Uniform> uniforms2 )
	{
		FastMap<Uniform> result = new FastMap<Uniform>();

		result.putAll(uniforms1);
		result.putAll(uniforms2);

		return result;
	}

	/**
	 * Clone uniforms and create new instance of uniforms.
	 *
	 * @param uniforms the uniforms to clone
	 *
	 * @return the new instance of Uniform map
	 */
	public static FastMap<Uniform> clone ( FastMap<Uniform> uniforms )
	{
		FastMap<Uniform> result = new FastMap<Uniform>();

		for(String u : uniforms.keySet())
			result.put(u, uniforms.get(u).clone());

		return result;
	}
}
