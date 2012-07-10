/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.utils;

import java.util.HashMap;
import java.util.Map;

import thothbot.squirrel.core.client.shader.Uniform;

public class UniformsUtils
{
	public static Map<String, Uniform> merge( Map<String, Uniform> uniforms1, Map<String, Uniform> uniforms2 ) 
	{
		Map<String, Uniform> result = new HashMap<String, Uniform>();
		
		result.putAll(uniforms1);
		result.putAll(uniforms2);

		return result;
	}

	public static Map<String, Uniform> clone ( Map<String, Uniform> uniforms ) 
	{
		Map<String, Uniform> result = new HashMap<String, Uniform>();
		
		for(String u : uniforms.keySet())
			result.put(u, uniforms.get(u).clone());

		return result;
	}
}
