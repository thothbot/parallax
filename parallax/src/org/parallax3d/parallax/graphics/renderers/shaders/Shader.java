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

package org.parallax3d.parallax.graphics.renderers.shaders;

import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.SourceBundle;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.List;

/**
 * Basic abstract shader.
 *
 * @author thothbot
 *
 */
public abstract class Shader
{
	/**
	 * Sets the Shader's precision value.
	 */
	public enum PRECISION
	{
		HIGHP,
		MEDIUMP,
		LOWP
	}

	public interface DefaultResources extends SourceBundle
	{
		@Source("source/default_vert.glsl")
		SourceTextResource getVertexShader();

		@Source("source/default_frag.glsl")
		SourceTextResource getFragmentShader();
	}

	// Store uniforms and locations
	private FastMap<Uniform> uniforms;

	// Should be null by default. Think how we can merge two maps.
	private FastMap<Attribute> attributes;
	// Store locations
	private FastMap<Integer> attributesLocations;

	private String vertexShaderSource = "";
	private String fragmentShaderSource = "";

	private String vertexExtensions = "";
	private String fragmentExtensions = "";

	private boolean cache_areCustomAttributesDirty;

	/**
	 * This constructor will create new Shader instance.
	 *
	 * @param resource the {@link Shader.DefaultResources} instance.
	 */
	public Shader(DefaultResources resource)
	{
		this(resource.getVertexShader().getText(), resource.getFragmentShader().getText());
	}

	public Shader(String vertexShader, String fragmentShader)
	{
		updateVertexSource(vertexShader);
		updateFragmentSource(fragmentShader);

		this.uniforms = new FastMap<Uniform>();

		this.attributesLocations = new FastMap<Integer>();

		initUniforms();
	}

	/**
	 * Gets source of the Vertex shader.
	 *
	 * @return the string with Vertex shader code.
	 */
	public String getVertexSource() {
		return this.vertexShaderSource;
	}

	public void setVertexExtensions(String vertexExtensions)
	{
		this.vertexExtensions = vertexExtensions;
	}

	public String getVertexExtensions()
	{
		return vertexExtensions;
	}

	public void setVertexSource(String src) {
		this.vertexShaderSource = src;
	}

	protected void updateVertexSource(String src) {
		setVertexSource(src);
	}

	/**
	 * Gets source of the Fragment shader.
	 *
	 * @return the string with Fragment shader code.
	 */
	public String getFragmentSource() {
		return this.fragmentShaderSource;
	}

	public void setFragmentSource(String src) {
		this.fragmentShaderSource = src;
	}

	public void setFragmentExtensions(String fragmentExtensions)
	{
		this.fragmentExtensions = fragmentExtensions;
	}

	public String getFragmentExtensions()
	{
		return fragmentExtensions;
	}

	protected void updateFragmentSource(String src) {
		setFragmentSource(src);
	}

	// Uniforms
	protected abstract void initUniforms();

	/**
	 * Gets shader's uniforms.
	 *
	 * @return the map of name-uniform
	 */
	public FastMap<Uniform> getUniforms()
	{
		return this.uniforms;
	}

	public void setUniforms(FastMap<Uniform> uniforms)
	{
		this.uniforms.putAll(uniforms);
	}

	public void addUniform(String id, Uniform uniform)
	{
		this.uniforms.put(id, uniform);
	}

	public FastMap<Integer> getAttributesLocations() {
		return this.attributesLocations;
	}

	public FastMap<Attribute> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(FastMap< Attribute> attributes) {
		this.attributes = attributes;
	}

	public void addAttributes(String id, Attribute attribute)
	{
		if(this.attributes == null)
		{
			this.attributes = new FastMap<Attribute>();
		}
		this.attributes.put(id, attribute);
	}

	public String toString()
	{
		return "{ class=" + getClass().getName() + ", uniforms=" + getUniforms() + "}";
	}

	// Methods
	protected static String updateShaderSource(String src, List<String> ... allMods)
	{
		String[] mods = new String[allMods.length];
		for(int i = 0; i < allMods.length; i++)
		{
			String replace = "";

			for(String mod : allMods[i])
				replace += mod + "\n";

			mods[i] = replace;
		}

		return Shader.updateShaderSource(src, mods);
	}

	private static String SHADER_REPLACE_ARG = "[*]";
	public static String updateShaderSource(String src, String ... allMods)
	{
		if(src.contains(SHADER_REPLACE_ARG))
		{
			StringBuffer result = new StringBuffer();
			int s = 0;
			int e = 0;

			for ( String replace : allMods )
			{
				if((e = src.indexOf(SHADER_REPLACE_ARG, s)) >= 0)
				{
					result.append(src.substring(s, e));
			        result.append(replace);
			        s = e + SHADER_REPLACE_ARG.length();
				}
			}

			result.append(src.substring(s));

			return result.toString();
		}

		return src;
	}

	public static Float32Array buildKernel( double sigma )
	{
		int kMaxKernelSize = 25;
		int kernelSize = (int) (2 * Math.ceil( sigma * 3 ) + 1);

		if ( kernelSize > kMaxKernelSize )
			kernelSize = kMaxKernelSize;

		double halfWidth = ( kernelSize - 1.0 ) * 0.5;

		Float32Array values = Float32Array.create(kernelSize);

		double sum = 0.0;
		for ( int i = 0; i < kernelSize; ++i )
		{
			double result = Mathematics.gauss( i - halfWidth, sigma );
			values.set(i, result);
			sum += result;
		}

		// normalize the kernel
		for ( int i = 0; i < kernelSize; ++i )
			values.set( i, values.get(i) / sum);

		return values;
	}
}
