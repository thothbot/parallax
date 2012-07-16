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

package thothbot.squirrel.core.client.shader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.squirrel.core.client.gl2.WebGLProgram;
import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.WebGLShader;
import thothbot.squirrel.core.client.gl2.WebGLUniformLocation;
import thothbot.squirrel.core.client.gl2.enums.GLenum;
import thothbot.squirrel.core.client.renderers.WebGLRenderer;
import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.core.WebGLCustomAttribute;

/**
 * The class used to create WebGl Program.
 * 
 * @author thothbot
 *
 */
public class Program
{
	public static enum SHADER_DEFINE {
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
	};

	public int id;
	public Map<String, WebGLUniformLocation> uniforms = new HashMap<String, WebGLUniformLocation>();
	public Map<String, Integer> attributes = new HashMap<String, Integer>();

	private WebGLProgram program;

	private WebGLRenderer.PRECISION _precision;
	private int _maxVertexTextures;

	protected WebGLRenderingContext _gl;

	/**
	 * Creates a new instance of the {@link Program}.
	 */
	public Program(WebGLRenderingContext _gl, WebGLRenderer.PRECISION _precision,
			int _maxVertexTextures, String fragmentShader, String vertexShader,
			Map<String, Uniform> uniforms, Map<String, WebGLCustomAttribute> attributes, ProgramParameters parameters)
	{
		this._gl = _gl;
		this._precision = _precision;
		this._maxVertexTextures = _maxVertexTextures;

		Log.debug("Building new program...");

		initProgram(vertexShader, fragmentShader, parameters);

		// cache uniforms locations
		List<String> uniformIds = new ArrayList<String>(Arrays.asList("viewMatrix",
				"modelViewMatrix", "projectionMatrix", "normalMatrix", "objectMatrix",
				"cameraPosition", "boneGlobalMatrices", "morphTargetInfluences"));

		for (String u : uniforms.keySet())
			uniformIds.add(u);

		this.cacheUniformLocations(uniformIds);

		// cache attributes locations
		List<String> attributesIds = new ArrayList<String>(Arrays.asList("position", "normal",
				"uv", "uv2", "tangent", "color", "skinVertexA", "skinVertexB", "skinIndex",
				"skinWeight"));

		for (int i = 0; i < parameters.maxMorphTargets; i++)
			attributesIds.add("morphTarget" + i);

		for (int i = 0; i < parameters.maxMorphNormals; i++)
			attributesIds.add("morphNormal" + i);

		// TODO: check this
		if(attributes != null)
			for (String a : attributes.keySet())
				attributesIds.add(a);

		cacheAttributeLocations(attributesIds);
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
	 * Sets the current program ID.
	 * 
	 * @param id the ID
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Gets the shader program.
	 */
	public WebGLProgram getProgram()
	{
		return this.program;
	}

	/**
	 * Initializes this shader with the given vertex shader source and fragment
	 * shader source. This should be called within {@link #init(GL2)} to ensure
	 * that the shader is correctly initialized.
	 * 
	 * @param vertexSource   the vertex shader source code
	 * @param fragmentSource the fragment shader source code
	 */
	protected final void initProgram(String vertexSource, String fragmentSource,
			ProgramParameters parameters)
	{
		Log.debug("Called initProgram()");

		this.program = this._gl.createProgram();

		String prefix_vertex = getPrefixVertex(parameters);
		String prefix_fragment = getPrefixFragment(parameters);
		
		this._gl.attachShader(this.program,
				getShader(ChunksFragmentShader.class, prefix_fragment + fragmentSource));
		this._gl.attachShader(this.program, getShader(ChunksVertexShader.class, prefix_vertex + vertexSource));

		this._gl.linkProgram(this.program);

		if (!this._gl.getProgramParameterb(this.program, GLenum.LINK_STATUS.getValue()))
			Log.error("Could not initialise shader\n" + "VALIDATE_STATUS: "
					+ ", gl error [" + this._gl.getProgramInfoLog(program) + "]"
			);

		else
			Log.info("initProgram(): shaders has been initialised");
	}

	/**
	 * Gets the shader.
	 */
	private WebGLShader getShader(Class<?> type, String string)
	{
		Log.debug("Called Program.getShader() for type " + type.getName() + " and source is: \n" + string);
		WebGLShader shader = null;

		if (type == ChunksFragmentShader.class)
			shader = this._gl.createShader(GLenum.FRAGMENT_SHADER.getValue());
		else if (type == ChunksVertexShader.class)
			shader = this._gl.createShader(GLenum.VERTEX_SHADER.getValue());

		this._gl.shaderSource(shader, string);
		this._gl.compileShader(shader);

		if (!this._gl.getShaderParameterb(shader, GLenum.COMPILE_STATUS.getValue())) 
		{
			Log.error(this._gl.getShaderInfoLog(shader));
			return null;
		}

		return shader;
	}

	// Shader parameters cache
	private void cacheUniformLocations(List<String> identifiers)
	{
		for (String id : identifiers)
			this.uniforms.put(id, this._gl.getUniformLocation(this.program, id));
	}

	private void cacheAttributeLocations(List<String> identifiers)
	{
		for (String id : identifiers)
			this.attributes.put(id, this._gl.getAttribLocation(this.program, id));
	}

	private String getPrecision()
	{
		String precision = this._precision.name().toLowerCase();
		return "precision " + precision + " float;";
	}

	private String getPrefixFragment(ProgramParameters parameters)
	{
		Log.debug("Called getPrefixFragment()");
		List<String> options = new ArrayList<String>();
		options.add(getPrecision());

		options.add(Program.SHADER_DEFINE.MAX_DIR_LIGHTS.getValue(parameters.maxDirLights));
		options.add(Program.SHADER_DEFINE.MAX_POINT_LIGHTS.getValue(parameters.maxPointLights));
		options.add(Program.SHADER_DEFINE.MAX_SPOT_LIGHTS.getValue(parameters.maxSpotLights));

		options.add(Program.SHADER_DEFINE.MAX_SHADOWS.getValue(parameters.maxShadows));

		if (parameters.alphaTest > 0)
			options.add(Program.SHADER_DEFINE.ALPHATEST.getValue(parameters.alphaTest));

		if (parameters.gammaInput)
			options.add(Program.SHADER_DEFINE.GAMMA_INPUT.getValue());

		if (parameters.gammaOutput)
			options.add(Program.SHADER_DEFINE.GAMMA_OUTPUT.getValue());

		if (parameters.physicallyBasedShading)
			options.add(Program.SHADER_DEFINE.PHYSICALLY_BASED_SHADING.getValue());

		if (parameters.useFog)
			options.add(Program.SHADER_DEFINE.USE_FOG.getValue());

		if (parameters.useFog2)
			options.add(Program.SHADER_DEFINE.FOG_EXP2.getValue());

		if (parameters.map)
			options.add(Program.SHADER_DEFINE.USE_MAP.getValue());

		if (parameters.envMap)
			options.add(Program.SHADER_DEFINE.USE_ENVMAP.getValue());

		if (parameters.lightMap)
			options.add(Program.SHADER_DEFINE.USE_LIGHTMAP.getValue());

		if (parameters.vertexColors)
			options.add(Program.SHADER_DEFINE.USE_COLOR.getValue());

		if (parameters.metal)
			options.add(Program.SHADER_DEFINE.METAL.getValue());

		if (parameters.perPixel)
			options.add(Program.SHADER_DEFINE.PHONG_PER_PIXEL.getValue());
		if (parameters.wrapAround)
			options.add(Program.SHADER_DEFINE.WRAP_AROUND.getValue());
		if (parameters.doubleSided)
			options.add(Program.SHADER_DEFINE.DOUBLE_SIDED.getValue());

		if (parameters.shadowMapEnabled)
			options.add(Program.SHADER_DEFINE.USE_SHADOWMAP.getValue());
		if (parameters.shadowMapSoft)
			options.add(Program.SHADER_DEFINE.SHADOWMAP_SOFT.getValue());
		if (parameters.shadowMapDebug)
			options.add(Program.SHADER_DEFINE.SHADOWMAP_DEBUG.getValue());
		if (parameters.shadowMapCascade)
			options.add(Program.SHADER_DEFINE.SHADOWMAP_CASCADE.getValue());

		options.add(ChunksFragmentShader.DEFAULT_PARS);

		options.add("");
		String retval = "";
		for(String opt: options)
			retval += opt + "\n";

		return retval;
	}

	private String getPrefixVertex(ProgramParameters parameters)
	{
		Log.debug("Called getPrefixVertex()");
		List<String> options = new ArrayList<String>();
		options.add(getPrecision());

		if (this._maxVertexTextures > 0)
			options.add(Program.SHADER_DEFINE.VERTEX_TEXTURES.getValue());

		if (parameters.gammaInput)
			options.add(Program.SHADER_DEFINE.GAMMA_INPUT.getValue());

		if (parameters.gammaOutput)
			options.add(Program.SHADER_DEFINE.GAMMA_OUTPUT.getValue());

		if (parameters.physicallyBasedShading)
			options.add(Program.SHADER_DEFINE.PHYSICALLY_BASED_SHADING.getValue());

		options.add(Program.SHADER_DEFINE.MAX_DIR_LIGHTS.getValue(parameters.maxDirLights));
		options.add(Program.SHADER_DEFINE.MAX_POINT_LIGHTS.getValue(parameters.maxPointLights));
		options.add(Program.SHADER_DEFINE.MAX_SPOT_LIGHTS.getValue(parameters.maxSpotLights));

		options.add(Program.SHADER_DEFINE.MAX_SHADOWS.getValue(parameters.maxShadows));

		options.add(Program.SHADER_DEFINE.MAX_BONES.getValue(parameters.maxBones));

		if (parameters.map)
			options.add(Program.SHADER_DEFINE.USE_MAP.getValue());

		if (parameters.envMap)
			options.add(Program.SHADER_DEFINE.USE_ENVMAP.getValue());

		if (parameters.lightMap)
			options.add(Program.SHADER_DEFINE.USE_LIGHTMAP.getValue());

		if (parameters.vertexColors)
			options.add(Program.SHADER_DEFINE.USE_COLOR.getValue());

		if (parameters.skinning)
			options.add(Program.SHADER_DEFINE.USE_SKINNING.getValue());

		if (parameters.morphTargets)
			options.add(Program.SHADER_DEFINE.USE_MORPHTARGETS.getValue());

		if (parameters.morphNormals)
			options.add(Program.SHADER_DEFINE.USE_MORPHNORMALS.getValue());
		if (parameters.perPixel)
			options.add(Program.SHADER_DEFINE.PHONG_PER_PIXEL.getValue());
		if (parameters.wrapAround)
			options.add(Program.SHADER_DEFINE.WRAP_AROUND.getValue());
		if (parameters.doubleSided)
			options.add(Program.SHADER_DEFINE.DOUBLE_SIDED.getValue());

		if (parameters.shadowMapEnabled)
			options.add(Program.SHADER_DEFINE.USE_SHADOWMAP.getValue());
		if (parameters.shadowMapSoft)
			options.add(Program.SHADER_DEFINE.SHADOWMAP_SOFT.getValue());
		if (parameters.shadowMapDebug)
			options.add(Program.SHADER_DEFINE.SHADOWMAP_DEBUG.getValue());
		if (parameters.shadowMapCascade)
			options.add(Program.SHADER_DEFINE.SHADOWMAP_CASCADE.getValue());

		if (parameters.sizeAttenuation)
			options.add(Program.SHADER_DEFINE.USE_SIZEATTENUATION.getValue());

		options.add(ChunksVertexShader.DEFAULT_PARS);
		options.add("");

		String retval = "";
		for(String opt: options)
			retval += opt + "\n";
		return retval;
	}
}
