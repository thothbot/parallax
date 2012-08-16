/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

import com.google.gwt.core.client.GWT;

import thothbot.parallax.core.client.gl2.WebGLProgram;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLShader;
import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.FastMap;

/**
 * The class used to create WebGl Program.
 * 
 * @author thothbot
 *
 */
public class Program
{
	/**
	 * Parameters used by the WebGl Program.
	 */
	public static class ProgramParameters
	{
		public WebGLRenderer.PRECISION precision;
		
		public boolean gammaInput;
		public boolean gammaOutput;
		public boolean physicallyBasedShading;
		
		public int maxDirLights;
		public int maxPointLights;
		public int maxSpotLights;
		
		public int maxShadows;
		public int maxBones;
		
		public boolean map;
		public boolean envMap;
		public boolean lightMap;
		public boolean vertexColors;
		public boolean skinning;
		public boolean morphTargets;
		public boolean morphNormals;
		public boolean perPixel;
		public boolean wrapAround;
		public boolean doubleSided;
		
		public boolean shadowMapEnabled;
		public boolean shadowMapSoft;
		public boolean shadowMapDebug;
		public boolean shadowMapCascade;
		public boolean sizeAttenuation;
		
		public int alphaTest;
		public boolean useFog;
		public boolean useFog2;
		public boolean metal;
		
		public int maxMorphTargets;
		public int maxMorphNormals;
		
		public int maxVertexTextures;
		
		public String toString() {
			String retval = "precision=" + precision.name() + ", ";
			retval += gammaInput + ", " + gammaOutput + ", " + physicallyBasedShading + "-1-"
					+ maxDirLights + ", " + maxPointLights + ", " + maxSpotLights + "-2-"
					+ maxShadows + ", " + maxBones + "-2-"
					+ map + ", " + envMap + ", " + lightMap + ", " + vertexColors + ", " + skinning + ", " + morphTargets + "-4-"
					+ morphNormals + ", " + perPixel + ", " + wrapAround + ", " + doubleSided + "-5-"
					+ shadowMapEnabled + ", " + shadowMapSoft + ", " + shadowMapDebug  + ", " + shadowMapCascade + ", " + sizeAttenuation + "-6-"
					+ alphaTest + ", " + useFog + ", " + useFog2 + ", " + metal + ", " + maxMorphTargets + ", " + maxMorphNormals + ", " + maxVertexTextures;
			return retval;
		}
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
	};

	private int id;
	private Map<String, WebGLUniformLocation> uniforms = GWT.isScript() ? 
			new FastMap<WebGLUniformLocation>() : new HashMap<String, WebGLUniformLocation>();
	private Map<String, Integer> attributes = GWT.isScript() ? 
			new FastMap<Integer>() : new HashMap<String, Integer>();

	private WebGLProgram program;

	private WebGLRenderingContext _gl;

	/**
	 * Creates a new instance of the {@link Program}.
	 */
	public Program(WebGLRenderingContext _gl,
			String fragmentShader, String vertexShader,
			Map<String, Uniform> uniforms, Map<String, Attribute> attributes, Program.ProgramParameters parameters)
	{
		this._gl = _gl;

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

	public Map<String, WebGLUniformLocation> getUniforms() {
		return this.uniforms;
	}

	public Map<String, Integer> getAttributes() {
		return this.attributes;
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

		this._gl.attachShader(this.program, getShader(ChunksVertexShader.class, prefix_vertex + vertexSource));
		this._gl.attachShader(this.program, getShader(ChunksFragmentShader.class, prefix_fragment + fragmentSource));

		this._gl.linkProgram(this.program);

		if (!this._gl.getProgramParameterb(this.program, GLenum.LINK_STATUS.getValue()))
			Log.error("Could not initialise shader\n"
					+ "GL error: " + this._gl.getProgramInfoLog(program)
					+ "\n-----\nVERTEX:\n" + prefix_vertex + vertexSource
					+ "\n-----\nFRAGMENT:\n" + prefix_fragment + fragmentSource
			);

		else
			Log.info("initProgram(): shaders has been initialised");
	}

	/**
	 * Gets the shader.
	 */
	private WebGLShader getShader(Class<?> type, String string)
	{
		Log.debug("Called Program.getShader() for type " + type.getName());
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

	private String getPrecision(ProgramParameters parameters)
	{
		String precision = parameters.precision.name().toLowerCase();
		return "precision " + precision + " float;";
	}

	private String getPrefixVertex(ProgramParameters parameters)
	{
		Log.debug("Called getPrefixVertex()");
		List<String> options = new ArrayList<String>();
		options.add(getPrecision(parameters));

		if (parameters.maxVertexTextures > 0)
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
	
	private String getPrefixFragment(ProgramParameters parameters)
	{
		Log.debug("Called getPrefixFragment()");
		List<String> options = new ArrayList<String>();
		options.add(getPrecision(parameters));

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
}
