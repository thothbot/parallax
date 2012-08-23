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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLProgram;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLShader;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Mathematics;

import com.google.gwt.core.client.GWT;
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
	/**
	 * Sets the Shaders precision value.
	 */
	public static enum PRECISION 
	{
		HIGHP,
		MEDIUMP,
		LOWP
	}
	
	public interface DefaultResources extends ClientBundle
	{
		@Source("source/default.vs")
		TextResource getVertexShader();

		@Source("source/default.fs")
		TextResource getFragmentShader();
	}
	
	private static enum SHADER_DEFINE {
		VERTEX_TEXTURES, GAMMA_INPUT, GAMMA_OUTPUT, PHYSICALLY_BASED_SHADING,

		MAX_DIR_LIGHTS, // param
		MAX_POINT_LIGHTS, // param
		MAX_SPOT_LIGHTS, // param
		MAX_SHADOWS, // param
		MAX_BONES, // param

		USE_MAP, USE_ENVMAP, USE_LIGHTMAP, USE_COLOR, USE_SKINNING, USE_MORPHTARGETS, USE_MORPHNORMALS,

		PHONG_PER_PIXEL, WRAP_AROUND, DOUBLE_SIDED,

		USE_SHADOWMAP, SHADOWMAP_SOFT, SHADOWMAP_DEBUG, SHADOWMAP_CASCADE,

		USE_SIZEATTENUATION,

		ALPHATEST,

		USE_FOG, FOG_EXP2, METAL;

		public String getValue()
		{
			return "#define " + this.name();
		}

		public String getValue(int param)
		{
			return "#define " + this.name() + " " + param;
		}
	}

	// shader precision. Can be "highp", "mediump" or "lowp".
	private PRECISION precision = PRECISION.HIGHP;

	private WebGLProgram program;

	// Store uniforms and locations 
	private Map<String, Uniform> uniforms;
	// Should be null by default. Think how we can merge two maps.
	private Map<String, Attribute> attributes;
	// Store locations
	private Map<String, Integer> attributesLocations;

	private String vertexShaderSource;
	private String fragmentShaderSource;

	private boolean cache_areCustomAttributesDirty;

	private int id;

	private static int shaderCounter;

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
		this.id = shaderCounter++;

		setVertexSource(vertexShader);
		setFragmentSource(fragmentShader);

		this.uniforms = GWT.isScript() ? 
				new FastMap<Uniform>() : new HashMap<String, Uniform>();

		this.attributesLocations = GWT.isScript() ? 
				new FastMap<Integer>() : new HashMap<String, Integer>();

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
	public WebGLProgram getProgram()
	{
		return this.program;
	}
	
	public Shader buildProgram(WebGLRenderingContext _gl, ProgramParameters parameters) 
	{
		Log.debug("Building new program...");

		initProgram(_gl, parameters);

		// Adds default uniforms
		addUniform("viewMatrix",            new Uniform(Uniform.TYPE.FV1, null));
		addUniform("modelViewMatrix",       new Uniform(Uniform.TYPE.FV1, null));
		addUniform("projectionMatrix",      new Uniform(Uniform.TYPE.FV1, null));
		addUniform("normalMatrix",          new Uniform(Uniform.TYPE.FV1, null));
		addUniform("objectMatrix",          new Uniform(Uniform.TYPE.FV1, null));
		addUniform("cameraPosition",        new Uniform(Uniform.TYPE.FV1, null));
		addUniform("boneGlobalMatrices",    new Uniform(Uniform.TYPE.FV1, null));
		addUniform("morphTargetInfluences", new Uniform(Uniform.TYPE.FV1, null));
				
		// Cache location
		Map<String, Uniform> uniforms = getUniforms();
		for (String id : uniforms.keySet())
			uniforms.get(id).setLocation( _gl.getUniformLocation(this.program, id) );

		// cache attributes locations
		List<String> attributesIds = new ArrayList<String>(Arrays.asList("position", "normal",
				"uv", "uv2", "tangent", "color", "skinVertexA", "skinVertexB", "skinIndex",
				"skinWeight"));

		for (int i = 0; i < parameters.maxMorphTargets; i++)
			attributesIds.add("morphTarget" + i);

		for (int i = 0; i < parameters.maxMorphNormals; i++)
			attributesIds.add("morphNormal" + i);

		Map<String, Attribute> attributes = getAttributes();
		if(attributes != null)
			for (String a : attributes.keySet())
				attributesIds.add(a);

		Map<String, Integer> attributesLocations = getAttributesLocations();
		for (String id : attributesIds)
			attributesLocations.put(id, _gl.getAttribLocation(this.program, id));
		
		return this;
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
		return this.fragmentShaderSource;		
	}

	// Uniforms
	protected abstract void initUniforms();
	
	/**
	 * Gets shader's uniforms.
	 * 
	 * @return the map of name-uniform 
	 */
	public Map<String, Uniform> getUniforms()
	{
		return this.uniforms;
	}
	
	public void setUniforms(Map<String, Uniform> uniforms)
	{
		this.uniforms.putAll(uniforms);
	}

	public void addUniform(String id, Uniform uniform)
	{
		this.uniforms.put(id, uniform);
	}

	@Deprecated
	public Map<String, Integer> getAttributesLocations() {
		return this.attributesLocations;
	}

	public Map<String, Attribute> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
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
	
	public void setPrecision(Shader.PRECISION precision) {
		this.precision = precision;
	}

	private String getShaderPrecisionDefinition()
	{
		return "precision " + precision.name().toLowerCase() + " float;";
	}

	/**
	 * Initializes this shader with the given vertex shader source and fragment
	 * shader source. This should be called within {@link #init(GL2)} to ensure
	 * that the shader is correctly initialized.
	 * 
	 * @param vertexSource   the vertex shader source code
	 * @param fragmentSource the fragment shader source code
	 */
	protected final void initProgram(WebGLRenderingContext _gl, ProgramParameters parameters)
	{
		Log.debug("Called initProgram()");

		this.program = _gl.createProgram();

		String prefix_vertex = getPrefixVertex(parameters);
		String prefix_fragment = getPrefixFragment(parameters);

		_gl.attachShader(this.program, getShader(_gl, ChunksVertexShader.class, prefix_vertex + getVertexSource()));
		_gl.attachShader(this.program, getShader(_gl, ChunksFragmentShader.class, prefix_fragment + getFragmentSource()));

		_gl.linkProgram(this.program);

		if (!_gl.getProgramParameterb(this.program, GLenum.LINK_STATUS.getValue()))
			Log.error("Could not initialise shader\n"
					+ "GL error: " + _gl.getProgramInfoLog(program)
					+ "\n-----\nVERTEX:\n" + prefix_vertex + getVertexSource()
					+ "\n-----\nFRAGMENT:\n" + prefix_fragment + getFragmentSource()
			);

		else
			Log.info("initProgram(): shaders has been initialised");
	}

	/**
	 * Gets the shader.
	 */
	private WebGLShader getShader(WebGLRenderingContext _gl, Class<?> type, String string)
	{
		Log.debug("Called Program.getShader() for type " + type.getName());
		WebGLShader shader = null;

		if (type == ChunksFragmentShader.class)
			shader = _gl.createShader(GLenum.FRAGMENT_SHADER.getValue());

		else if (type == ChunksVertexShader.class)
			shader = _gl.createShader(GLenum.VERTEX_SHADER.getValue());

		_gl.shaderSource(shader, string);
		_gl.compileShader(shader);

		if (!_gl.getShaderParameterb(shader, GLenum.COMPILE_STATUS.getValue())) 
		{
			Log.error(_gl.getShaderInfoLog(shader));
			return null;
		}

		return shader;
	}
	
	private String getPrefixVertex(ProgramParameters parameters)
	{
		Log.debug("Called getPrefixVertex()");
		List<String> options = new ArrayList<String>();
		options.add(getShaderPrecisionDefinition());

		if (parameters.maxVertexTextures > 0)
			options.add(SHADER_DEFINE.VERTEX_TEXTURES.getValue());

		if (parameters.gammaInput)
			options.add(SHADER_DEFINE.GAMMA_INPUT.getValue());

		if (parameters.gammaOutput)
			options.add(SHADER_DEFINE.GAMMA_OUTPUT.getValue());

		if (parameters.physicallyBasedShading)
			options.add(SHADER_DEFINE.PHYSICALLY_BASED_SHADING.getValue());

		options.add(SHADER_DEFINE.MAX_DIR_LIGHTS.getValue(parameters.maxDirLights));
		options.add(SHADER_DEFINE.MAX_POINT_LIGHTS.getValue(parameters.maxPointLights));
		options.add(SHADER_DEFINE.MAX_SPOT_LIGHTS.getValue(parameters.maxSpotLights));

		options.add(SHADER_DEFINE.MAX_SHADOWS.getValue(parameters.maxShadows));

		options.add(SHADER_DEFINE.MAX_BONES.getValue(parameters.maxBones));

		if (parameters.map)
			options.add(SHADER_DEFINE.USE_MAP.getValue());

		if (parameters.envMap)
			options.add(SHADER_DEFINE.USE_ENVMAP.getValue());

		if (parameters.lightMap)
			options.add(SHADER_DEFINE.USE_LIGHTMAP.getValue());

		if (parameters.vertexColors)
			options.add(SHADER_DEFINE.USE_COLOR.getValue());

		if (parameters.skinning)
			options.add(SHADER_DEFINE.USE_SKINNING.getValue());

		if (parameters.morphTargets)
			options.add(SHADER_DEFINE.USE_MORPHTARGETS.getValue());
		if (parameters.morphNormals)
			options.add(SHADER_DEFINE.USE_MORPHNORMALS.getValue());
		
		if (parameters.perPixel)
			options.add(SHADER_DEFINE.PHONG_PER_PIXEL.getValue());
		if (parameters.wrapAround)
			options.add(SHADER_DEFINE.WRAP_AROUND.getValue());
		if (parameters.doubleSided)
			options.add(SHADER_DEFINE.DOUBLE_SIDED.getValue());

		if (parameters.shadowMapEnabled)
			options.add(SHADER_DEFINE.USE_SHADOWMAP.getValue());
		if (parameters.shadowMapSoft)
			options.add(SHADER_DEFINE.SHADOWMAP_SOFT.getValue());
		if (parameters.shadowMapDebug)
			options.add(SHADER_DEFINE.SHADOWMAP_DEBUG.getValue());
		if (parameters.shadowMapCascade)
			options.add(SHADER_DEFINE.SHADOWMAP_CASCADE.getValue());

		if (parameters.sizeAttenuation)
			options.add(SHADER_DEFINE.USE_SIZEATTENUATION.getValue());

		options.add(ChunksVertexShader.DEFAULT_PARS);
		options.add("");

		String retval = "";
		for(String opt: options)
			retval += opt + "\n";
		return retval;
	}
	
	private String getPrefixFragment(ProgramParameters parameters)
	{
		Log.debug("Called getPrefixFragment()");
		List<String> options = new ArrayList<String>();
		options.add(getShaderPrecisionDefinition());

		options.add(SHADER_DEFINE.MAX_DIR_LIGHTS.getValue(parameters.maxDirLights));
		options.add(SHADER_DEFINE.MAX_POINT_LIGHTS.getValue(parameters.maxPointLights));
		options.add(SHADER_DEFINE.MAX_SPOT_LIGHTS.getValue(parameters.maxSpotLights));

		options.add(SHADER_DEFINE.MAX_SHADOWS.getValue(parameters.maxShadows));

		if (parameters.alphaTest > 0)
			options.add(SHADER_DEFINE.ALPHATEST.getValue(parameters.alphaTest));

		if (parameters.gammaInput)
			options.add(SHADER_DEFINE.GAMMA_INPUT.getValue());

		if (parameters.gammaOutput)
			options.add(SHADER_DEFINE.GAMMA_OUTPUT.getValue());

		if (parameters.physicallyBasedShading)
			options.add(SHADER_DEFINE.PHYSICALLY_BASED_SHADING.getValue());

		if (parameters.useFog)
			options.add(SHADER_DEFINE.USE_FOG.getValue());

		if (parameters.useFog2)
			options.add(SHADER_DEFINE.FOG_EXP2.getValue());

		if (parameters.map)
			options.add(SHADER_DEFINE.USE_MAP.getValue());

		if (parameters.envMap)
			options.add(SHADER_DEFINE.USE_ENVMAP.getValue());

		if (parameters.lightMap)
			options.add(SHADER_DEFINE.USE_LIGHTMAP.getValue());

		if (parameters.vertexColors)
			options.add(SHADER_DEFINE.USE_COLOR.getValue());

		if (parameters.metal)
			options.add(SHADER_DEFINE.METAL.getValue());

		if (parameters.perPixel)
			options.add(SHADER_DEFINE.PHONG_PER_PIXEL.getValue());
		if (parameters.wrapAround)
			options.add(SHADER_DEFINE.WRAP_AROUND.getValue());
		if (parameters.doubleSided)
			options.add(SHADER_DEFINE.DOUBLE_SIDED.getValue());

		if (parameters.shadowMapEnabled)
			options.add(SHADER_DEFINE.USE_SHADOWMAP.getValue());
		if (parameters.shadowMapSoft)
			options.add(SHADER_DEFINE.SHADOWMAP_SOFT.getValue());
		if (parameters.shadowMapDebug)
			options.add(SHADER_DEFINE.SHADOWMAP_DEBUG.getValue());
		if (parameters.shadowMapCascade)
			options.add(SHADER_DEFINE.SHADOWMAP_CASCADE.getValue());

		options.add(ChunksFragmentShader.DEFAULT_PARS);

		options.add("");
		String retval = "";
		for(String opt: options)
			retval += opt + "\n";

		return retval;
	}
}
