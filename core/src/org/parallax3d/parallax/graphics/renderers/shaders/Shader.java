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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ObjectIntMap;
import org.parallax3d.parallax.system.ObjectMap;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.ProgramParameter;

/**
 * Basic abstract shader.
 * 
 * @author thothbot
 * 
 */
public abstract class Shader
{
	/**
	 * Sets the Shaders precision value.
	 */
	public static enum PRECISION 
	{
		HIGHP,
		MEDIUMP,
		LOWP
	}

	// shader precision. Can be "highp", "mediump" or "lowp".
	private PRECISION precision = PRECISION.HIGHP;

	private int program; //WebGLProgram

	// Store uniforms and locations 
	private ObjectMap<String, Uniform> uniforms;
	// Should be null by default. Think how we can merge two maps.
	private ObjectMap<String, Attribute> attributes;
	// Store locations
	private ObjectIntMap<String> attributesLocations;

	private String vertexShaderSource = "";
	private String fragmentShaderSource = "";

	private String vertexExtensions = "";
	private String fragmentExtensions = "";

	private boolean cache_areCustomAttributesDirty;

	private int id;

	private static int shaderCounter;

	/**
	 * This constructor will create new Shader instance. 
	 *
	 */
	public Shader()
	{
		this(Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/default.vs").readString(),
		     Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/default.fs").readString());
	}

	public Shader(String vertexShader, String fragmentShader)
	{
		this.id = shaderCounter++;

		updateVertexSource(vertexShader);
		updateFragmentSource(fragmentShader);

		this.uniforms = new ObjectMap<String, Uniform>();

		this.attributesLocations = new ObjectIntMap<String>();

		initUniforms();
	}

	/**
	 * Gets program ID
	 * 
	 * @return ID
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Gets the shader program.
	 */
	public int getProgram()
	{
		return this.program;
	}
	
	// Called in renderer plugins
	public Shader buildProgram(GL20 gl)
	{
		return buildProgram(gl, false, 0, 0);
	}
	
	public Shader buildProgram(GL20 gl, boolean useVertexTexture, int maxMorphTargets, int maxMorphNormals)
	{
		Parallax.app.debug("Shader", "Building new program...");

		initShaderProgram(gl);

		// Adds default uniforms
		addUniform("viewMatrix",            new Uniform(Uniform.TYPE.FV1 ));
		addUniform("modelViewMatrix",       new Uniform(Uniform.TYPE.FV1 ));
		addUniform("projectionMatrix",      new Uniform(Uniform.TYPE.FV1 ));
		addUniform("normalMatrix",          new Uniform(Uniform.TYPE.FV1 ));
		addUniform("modelMatrix",           new Uniform(Uniform.TYPE.FV1 ));
		addUniform("cameraPosition",        new Uniform(Uniform.TYPE.FV1 ));
		addUniform("morphTargetInfluences", new Uniform(Uniform.TYPE.FV1 ));
		addUniform("bindMatrix",            new Uniform(Uniform.TYPE.FV1 ));
		addUniform("bindMatrixInverse",     new Uniform(Uniform.TYPE.FV1 ));
				
		if ( useVertexTexture ) 
		{
			addUniform("boneTexture", new Uniform(Uniform.TYPE.FV1, null));
			addUniform("boneTextureWidth", new Uniform(Uniform.TYPE.F, null));
			addUniform("boneTextureHeight", new Uniform(Uniform.TYPE.F, null));
		} 
		else 
		{
			addUniform("boneGlobalMatrices", new Uniform(Uniform.TYPE.FV1, null));
		}
		
		// Cache location
		ObjectMap<String, Uniform> uniforms = getUniforms();
		for (String id : uniforms.keys())
			uniforms.get(id).setLocation( gl.glGetUniformLocation(this.program, id) );

		// cache attributes locations
		List<String> attributesIds = new ArrayList<String>(Arrays.asList("position", "normal",
				"uv", "uv2", "tangent", "color", "skinIndex", "skinWeight", "lineDistance"));

		for (int i = 0; i < maxMorphTargets; i++)
			attributesIds.add("morphTarget" + i);

		for (int i = 0; i < maxMorphNormals; i++)
			attributesIds.add("morphNormal" + i);

		ObjectMap<String, Attribute> attributes = getAttributes();
		if(attributes != null)
			for (String a : attributes.keys())
				attributesIds.add(a);

		ObjectIntMap<String> attributesLocations = getAttributesLocations();
		for (String id : attributesIds)
			attributesLocations.put(id, gl.glGetAttribLocation(this.program, id));
		
		return this;
	}
	

	/**
	 * Initializes this shader with the given vertex shader source and fragment
	 * shader source. This should be called within GL to ensure
	 * that the shader is correctly initialized.
	 */
	private void initShaderProgram(GL20 gl)
	{
		Parallax.app.debug("Shader", "Called initProgram()");

		this.program = gl.glCreateProgram();

		String vertex = vertexExtensions + getShaderPrecisionDefinition() + "\n" + getVertexSource();
		String fragment = fragmentExtensions + getShaderPrecisionDefinition() + "\n" + getFragmentSource();
		
		int glVertexShader = getShaderProgram(gl, ChunksVertexShader.class, vertex);
		int glFragmentShader = getShaderProgram(gl, ChunksFragmentShader.class, fragment);
		gl.glAttachShader(this.program, glVertexShader);
		gl.glAttachShader(this.program, glFragmentShader);

		gl.glLinkProgram(this.program);

//		if (!gl.getProgramParameterb(this.program, ProgramParameter.LINK_STATUS))
//			Log.error("Could not initialise shader\n"
//					+ "GL error: " + gl.getProgramInfoLog(program)
//					+ "Shader: " + this.getClass().getName()
//					+ "\n-----\nVERTEX:\n" + vertex
//					+ "\n-----\nFRAGMENT:\n" + fragment
//			);
//
//		else
			Parallax.app.debug("Shader", "initProgram(): shaders has been initialised");

		// clean up
		gl.glDeleteShader(glVertexShader);
		gl.glDeleteShader(glFragmentShader);
	}
	
	public void setPrecision(PRECISION precision) {
		this.precision = precision;
	}

	private String getShaderPrecisionDefinition() {
		return "precision " + precision.name().toLowerCase() + " float;";
	}

	/**
	 * Gets the shader.
	 */
	private int /*WebGLShader*/ getShaderProgram(GL20 gl, Class<?> type, String string)
	{
		Parallax.app.debug("Shader", "Called getShaderProgram() for type " + type.getName());
		Integer shader = null;

		if (type == ChunksFragmentShader.class)
			shader = gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		else if (type == ChunksVertexShader.class)
			shader = gl.glCreateShader(GL20.GL_VERTEX_SHADER);

		gl.glShaderSource(shader, string);
		gl.glCompileShader(shader);

//		if (!gl.glGetShaderParameterb(shader, GL20.GL_COMPILE_STATUS))
//		{
//			Log.error(gl.getShaderInfoLog(shader));
//			return null;
//		}

		return shader;
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
	public ObjectMap<String, Uniform> getUniforms()
	{
		return this.uniforms;
	}
	
	public void setUniforms(ObjectMap<String, Uniform> uniforms)
	{
		this.uniforms.putAll(uniforms);
	}

	public void addUniform(String id, Uniform uniform)
	{
		this.uniforms.put(id, uniform);
	}

	@Deprecated
	public ObjectIntMap<String> getAttributesLocations() {
		return this.attributesLocations;
	}

	public ObjectMap<String, Attribute> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(ObjectMap<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttributes(String id, Attribute attribute) 
	{
		if(this.attributes == null)
		{
			this.attributes = new ObjectMap<String, Attribute>();
		}
		this.attributes.put(id, attribute);
	}

	public boolean areCustomAttributesDirty() 
	{
		if(this.cache_areCustomAttributesDirty)
			return true;

		if(getAttributes() == null)
			return false;
 
		for ( Attribute attribute: getAttributes().values() )
		{
			if ( attribute.needsUpdate )
			{
				this.cache_areCustomAttributesDirty = true;
				return true;
			}
		}

		return false;
	}

	public void clearCustomAttributes() 
	{
		if(!this.cache_areCustomAttributesDirty)
			return;

		if(getAttributes() == null)
			return;

		for ( Attribute attribute: getAttributes().values() )
		{
			attribute.needsUpdate = false;
			this.cache_areCustomAttributesDirty = false;
		}
	}
	
	public String toString()
	{
		return "{id=" + this.id + ", class=" + getClass().getName() + ", uniforms=" + getUniforms() + "}";		
	}
		
	// Methods
	public static String updateShaderSource(String src, List<String> ... allMods)
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
		if(src.indexOf(SHADER_REPLACE_ARG) >= 0)
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

	public static FloatBuffer buildKernel( float sigma )
	{ 
		int kMaxKernelSize = 25; 
		int kernelSize = (int) (2 * Math.ceil( sigma * 3 ) + 1);

		if ( kernelSize > kMaxKernelSize ) 
			kernelSize = kMaxKernelSize;
		
		float halfWidth = ( kernelSize - 1.0f ) * 0.5f;

		FloatBuffer values = BufferUtils.newFloatBuffer(kernelSize);

		float sum = 0.0f;
		for ( int i = 0; i < kernelSize; ++i ) 
		{
			float result = Mathematics.gauss( i - halfWidth, sigma );
			values.put(i, result);
			sum += result;
		}

		// normalize the kernel
		for ( int i = 0; i < kernelSize; ++i ) 
			values.put(i, values.get(i) / sum);

		return values;
	}
}
