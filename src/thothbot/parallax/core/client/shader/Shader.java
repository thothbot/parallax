/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.shader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.Mathematics;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Basic abstract shader.
 * 
 * @author thothbot
 * 
 */
public abstract class Shader
{
	protected interface DefaultResources extends ClientBundle
	{
		@Source("source/default.vs")
		TextResource getVertexShader();

		@Source("source/default.fs")
		TextResource getFragmentShader();
	}
	
	private DefaultResources resource;
	private Map<String, Uniform> uniforms;

	private String vertexShaderSource;
	private String fragmentShaderSource;

	/**
	 * This constructor will create new Shader instance. 
	 * 
	 * @param resource the {@link Shader.DefaultResources} instance.
	 */
	public Shader(DefaultResources resource)
	{
		this.resource = resource;
		this.uniforms = new HashMap<String, Uniform>();
		initUniforms();
	}

	protected void setVertexSource(String src)
	{
		this.vertexShaderSource = "\n" + src;		
	}

	/**
	 * Gets source of the Vertex shader.
	 * 
	 * @return the string with Vertex shader code.
	 */
	public String getVertexSource()
	{
		if(this.vertexShaderSource == null)
			setVertexSource(resource.getVertexShader().getText());
			
		return this.vertexShaderSource;
	}
	
	protected void setFragmentSource(String src)
	{
		this.fragmentShaderSource = "\n" + src;		
	}
	
	/**
	 * Gets source of the Fragment shader.
	 * 
	 * @return the string with Fragment shader code.
	 */
	public String getFragmentSource()
	{
		if(this.fragmentShaderSource == null)
			setFragmentSource(resource.getFragmentShader().getText());

		return this.fragmentShaderSource;		
	}

	// Uniforms
	protected abstract void initUniforms();
	
	protected void addUniform(Map<String, Uniform> uniforms)
	{
		this.uniforms.putAll(uniforms);
	}

	protected void addUniform(String id, Uniform uniform)
	{
		this.uniforms.put(id, uniform);
	}
	
	/**
	 * Gets shader's uniforms.
	 * 
	 * @return the map of name-uniform 
	 */
	public Map<String, Uniform> getUniforms()
	{
		return this.uniforms;
	}
	
	// Methods
	private static String SHADER_REPLACE_ARG = "//[*]";
	protected static String updateShaderSource(String src, List<String> ... allMods)
	{
		StringBuffer result = new StringBuffer();
		int s = 0;
		int e = 0;
		
		if(src.indexOf(SHADER_REPLACE_ARG) >= 0)
		{
			for ( List<String> mods : allMods )
			{
				String replace = "";

				for(String mod : mods)
					replace += mod + "\n";

				if((e = src.indexOf(SHADER_REPLACE_ARG, s)) >= 0)
				{
					result.append(src.substring(s, e));
			        result.append(replace);
			        s = e + SHADER_REPLACE_ARG.length();
				}
			}

			result.append(src.substring(s));
		}

		return result.toString();
	}

	public static Float32Array buildKernel( double sigma ) 
	{ 
		int kMaxKernelSize = 25; 
		int kernelSize = (int) (2 * Math.ceil( sigma * 3 ) + 1);

		if ( kernelSize > kMaxKernelSize ) 
			kernelSize = kMaxKernelSize;
		
		double halfWidth = ( kernelSize - 1.0 ) * 0.5;

		Float32Array values = Float32Array.create(kernelSize);

		float sum = 0.0f;
		for ( int i = 0; i < kernelSize; ++i ) 
		{
			float result = (float) Mathematics.gauss( i - halfWidth, sigma ); 
			values.set(i, result);
			sum += result;
		}

		// normalize the kernel
		for ( int i = 0; i < kernelSize; ++i ) 
			values.set( i, values.get(i) / sum);

		return values;
	}
}
