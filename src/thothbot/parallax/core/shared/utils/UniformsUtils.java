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

package thothbot.parallax.core.shared.utils;

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.FastMap;

import com.google.gwt.core.client.GWT;

/**
 * The class implements some Uniform related helper methods 
 * 
 * @author thothbot
 *
 */
public class UniformsUtils
{
	/**
	 * Merge two uniforms into one new result uniform.
	 * 
	 * @param uniforms1 the map of first uniforms
	 * @param uniforms2 the map of second uniforms
	 * 
	 * @return new instance of Uniform Map
	 */
	public static Map<String, Uniform> merge( Map<String, Uniform> uniforms1, Map<String, Uniform> uniforms2 ) 
	{
		Map<String, Uniform> result = GWT.isScript() ? 
				new FastMap<Uniform>() : new HashMap<String, Uniform>();
		
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
	public static Map<String, Uniform> clone ( Map<String, Uniform> uniforms ) 
	{
		Map<String, Uniform> result = new HashMap<String, Uniform>();
		
		for(String u : uniforms.keySet())
			result.put(u, uniforms.get(u).clone());

		return result;
	}
}
