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

package org.parallax3d.parallax.graphics.materials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.renderers.RenderTargetCubeTexture;
import org.parallax3d.parallax.graphics.renderers.shaders.ProgramParameters;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.core.GeometryGroup;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.BlendEquationMode;
import org.parallax3d.parallax.system.gl.enums.BlendingFactorDest;
import org.parallax3d.parallax.system.gl.enums.BlendingFactorSrc;

/**
 * Materials describe the appearance of objects. 
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Material")
public abstract class Material
{

	private static int MaterialCount;

	/**
	 * Material sides
	 */
	public enum SIDE
	{
		FRONT,
		BACK,
		DOUBLE
	}

	/**
	 * Shading
	 */
	public enum SHADING
	{
		NO, // NoShading = 0;
		FLAT, // FlatShading = 1;
		SMOOTH // SmoothShading = 2;
	}

	/**
	 * Colors
	 */
	public enum COLORS
	{
		NO, // NoColors = 0;
		FACE, // FaceColors = 1;
		VERTEX // VertexColors = 2;
	}

	/**
	 * Blending modes
	 */
	public enum BLENDING
	{
		NO, // NoBlending = 0;
		NORMAL, // NormalBlending = 1;
		ADDITIVE, // AdditiveBlending = 2;
		SUBTRACTIVE, // SubtractiveBlending = 3;
		MULTIPLY, // MultiplyBlending = 4;
		ADDITIVE_ALPHA, // AdditiveAlphaBlending = 5;
		CUSTOM // CustomBlending = 6;
	}

	private enum SHADER_DEFINE {
		VERTEX_TEXTURES, GAMMA_INPUT, GAMMA_OUTPUT,

		MAX_DIR_LIGHTS, // param
		MAX_POINT_LIGHTS, // param
		MAX_SPOT_LIGHTS, // param
		MAX_HEMI_LIGHTS, // param
		MAX_SHADOWS, // param
		MAX_BONES, // param

		USE_MAP, USE_ENVMAP, USE_LIGHTMAP, USE_BUMPMAP, USE_NORMALMAP, USE_SPECULARMAP, USE_ALPHAMAP, USE_COLOR, USE_SKINNING, USE_MORPHTARGETS, USE_MORPHNORMALS,

		BONE_TEXTURE,
		WRAP_AROUND, DOUBLE_SIDED, FLIP_SIDED,

		USE_SHADOWMAP, SHADOWMAP_TYPE_BASIC, SHADOWMAP_TYPE_PCF, SHADOWMAP_TYPE_PCF_SOFT, SHADOWMAP_DEBUG, SHADOWMAP_CASCADE,

		USE_SIZEATTENUATION,

		USE_LOGDEPTHBUF,

		ALPHATEST,

		USE_FOG, FOG_EXP2, METAL;

		private static final String DEFINE = "#define ";

		public String getValue()
		{
			return DEFINE + this.name();
		}

		public String getValue(int param)
		{
			return DEFINE + this.name() + " " + param;
		}

		public String getValue(double param)
		{
			return DEFINE + this.name() + " " + param;
		}
	}

	private int id;

	private String name;

	// Store shader associated to the material
	private Shader shader;

	private SIDE side = SIDE.FRONT;

	private double opacity;
	private boolean isTransparent;

	private BLENDING blending;
	private BlendingFactorSrc blendSrc;
	private BlendingFactorDest blendDst;
	private BlendEquationMode blendEquation;

	private boolean isDepthTest;
	private boolean isDepthWrite;

	private boolean isPolygonOffset;
	private double polygonOffsetFactor;
	private double polygonOffsetUnits;

	private double alphaTest;

	private double overdraw = 0; // Overdrawn pixels (typically between 0 and 1) for fixing antialiasing gaps in CanvasRenderer

	private boolean isVisible = true;
	private boolean isNeedsUpdate = true;

	//

	private boolean isShadowPass;

	public Material()
	{
		this.id = Material.MaterialCount++;

		setOpacity(1.0);
		setTransparent(false);

		setBlending( BLENDING.NORMAL );
		setBlendSrc( BlendingFactorSrc.SRC_ALPHA );
		setBlendDst( BlendingFactorDest.ONE_MINUS_SRC_ALPHA );
		setBlendEquation( BlendEquationMode.FUNC_ADD );

		setDepthTest(true);
		setDepthWrite(true);

		setPolygonOffset(false);
		setPolygonOffsetFactor(0.0);
		setPolygonOffsetUnits(0.0);

		setAlphaTest(0);
	}

	/**
	 * Gets unique number of this material instance.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets material name. Default is an empty string.
	 */
	public String getName() {
		return this.name;
	}

	public <T extends Material> T setName(String name) {
		this.name = name;
		return (T)this;
	}

	public boolean isVisible() {
		return this.isVisible;
	}

	/**
	 * Defines whether this material is visible.
	 * <p>
	 * Default is true.
	 */
	public <T extends Material> T setVisible(boolean visible) {
		this.isVisible = visible;
		return (T)this;
	}

	public SIDE getSides() {
		return this.side;
	}

	/**
	 * Defines which of the face sides will be rendered - front, back or both.
	 * <p>
	 * Default is {@link SIDE#FRONT}
	 *
	 * @param side see options {@link Material.SIDE}.
	 */
	public <T extends Material> T setSide(SIDE side) {
		this.side = side;
		return (T)this;
	}

	public boolean isNeedsUpdate() {
		return this.isNeedsUpdate;
	}

	/**
	 * Specifies that the material needs to be updated, WebGL wise.
	 * Set it to true if you made changes that need to be reflected in WebGL.
	 * <p>
	 * This property is automatically set to true when instancing a new material.
	 */
	public <T extends Material> T setNeedsUpdate(boolean visible) {
		this.isNeedsUpdate = visible;
		return (T)this;
	}

	/**
	 * Gets opacity. Default is 1.
	 */
	public double getOpacity() {
		return opacity;
	}

	/**
	 * Sets opacity. Default is 1.
	 */
	public <T extends Material> T setOpacity(double opacity) {
		this.opacity = opacity;
		return (T)this;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	/**
	 * Defines whether this material is transparent.
	 * <p>
	 * This has an effect on rendering, as transparent objects need an special treatment,
	 * and are rendered after the opaque (i.e. non transparent) objects.
	 */
	public <T extends Material> T setTransparent(boolean transparent) {
		this.isTransparent = transparent;
		return (T)this;
}

	public Material.BLENDING getBlending() {
		return blending;
	}

	/**
	 * Sets which blending to use when displaying objects with this material.
	 * <p>
	 * Default is {@link Material.BLENDING#NORMAL}.
	 */
	public <T extends Material> T setBlending(BLENDING blending) {
		this.blending = blending;
		return (T)this;
	}

	public BlendingFactorSrc getBlendSrc() {
		return blendSrc;
	}

	/**
	 * Sets blending source. It's one of the {@link BlendingFactorSrc} constants.
	 * <p>
	 * Default is {@link BlendingFactorSrc#SRC_ALPHA}.
	 *
	 * @param blendSrc
	 */
	public <T extends Material> T setBlendSrc(BlendingFactorSrc blendSrc) {
		this.blendSrc = blendSrc;
		return (T)this;
	}

	public BlendingFactorDest getBlendDst() {
		return blendDst;
	}

	/**
	 * Sets blending destination. It's one of the {@link BlendingFactorDest} constants.
	 * <p>
	 * Default is {@link BlendingFactorDest#ONE_MINUS_SRC_ALPHA}.
	 *
	 * @param blendDst
	 */
	public <T extends Material> T setBlendDst(BlendingFactorDest blendDst) {
		this.blendDst = blendDst;
		return (T)this;
	}

	public BlendEquationMode getBlendEquation() {
		return blendEquation;
	}

	/**
	 * Sets blending equation to use when applying blending.
	 * It's one of the {@link BlendEquationMode} constants.
	 * <p>
	 * Default is {@link BlendEquationMode#FUNC_ADD}.
	 *
	 * @param blendEquation
	 */
	public <T extends Material> T setBlendEquation(BlendEquationMode blendEquation) {
		this.blendEquation = blendEquation;
		return (T)this;
	}

	public boolean isDepthTest() {
		return isDepthTest;
	}

	/**
	 * Whether to have depth test enabled when rendering this material.
	 * <p>
	 * Default is true.
	 */
	public <T extends Material> T setDepthTest(boolean depthTest) {
		this.isDepthTest = depthTest;
		return (T)this;
	}

	public boolean isDepthWrite() {
		return isDepthWrite;
	}

	/**
	 * Whether rendering this material has any effect on the depth buffer.
	 * <p>
	 * Default is true.
	 * <p>
	 * When drawing 2D overlays it can be useful to disable the depth writing in order
	 * to layer several things together without creating z-index artifacts.
	 */
	public <T extends Material> T setDepthWrite(boolean depthWrite) {
		this.isDepthWrite = depthWrite;
		return (T)this;
	}

	public boolean isPolygonOffset() {
		return isPolygonOffset;
	}

	/**
	 * Whether to use polygon offset.
	 * <p>
	 * Default is false.
	 * <p>
	 * This corresponds to the POLYGON_OFFSET_FILL WebGL feature.
	 */
	public <T extends Material> T setPolygonOffset(boolean polygonOffset) {
		this.isPolygonOffset = polygonOffset;
		return (T)this;
	}

	public double getPolygonOffsetFactor() {
		return polygonOffsetFactor;
	}

	/**
	 * Sets the polygon offset factor.
	 * <p>
	 * Default is 0.
	 */
	public <T extends Material> T setPolygonOffsetFactor(double polygonOffsetFactor) {
		this.polygonOffsetFactor = polygonOffsetFactor;
		return (T)this;
	}

	public double getPolygonOffsetUnits() {
		return polygonOffsetUnits;
	}

	/**
	 * Sets the polygon offset units.
	 * <p>
	 * Default is 0.
	 */
	public <T extends Material> T setPolygonOffsetUnits(double polygonOffsetUnits) {
		this.polygonOffsetUnits = polygonOffsetUnits;
		return (T)this;
	}

	public double getAlphaTest() {
		return alphaTest;
	}

	/**
	 * Sets the alpha value to be used when running an alpha test.
	 * <p>
	 * Default is 0.
	 */
	public <T extends Material> T setAlphaTest(double alphaTest) {
		this.alphaTest = alphaTest;
		return (T)this;
	}

	public boolean isShadowPass() {
		return isShadowPass;
	}

	public <T extends Material> T setShadowPass(boolean isShadowPass) {
		this.isShadowPass = isShadowPass;
		return (T)this;
	}

	public Shader getShader()
	{
		if(shader == null)
		{
			Log.debug("Material.getShader() Called");

			this.shader = getAssociatedShader();
		}

		return this.shader;
	}

	// Must be overwriten
	protected abstract Shader getAssociatedShader();

	public abstract Material clone();

	public Material clone( Material material ) {

		material.name = this.name;

		material.side = this.side;

		material.opacity = this.opacity;
		material.isTransparent = this.isTransparent;

		material.blending = this.blending;

		material.blendSrc = this.blendSrc;
		material.blendDst = this.blendDst;
		material.blendEquation = this.blendEquation;

		material.isDepthTest = this.isDepthTest;
		material.isDepthWrite = this.isDepthWrite;

		material.isPolygonOffset = this.isPolygonOffset;
		material.polygonOffsetFactor = this.polygonOffsetFactor;
		material.polygonOffsetUnits = this.polygonOffsetUnits;

		material.alphaTest = this.alphaTest;

		material.overdraw = this.overdraw;

		material.isVisible = this.isVisible;

		return material;

	}

	public void updateProgramParameters(ProgramParameters parameters)
	{
		parameters.map          = this instanceof HasMap         && ((HasMap)this).getMap() != null;
		parameters.envMap       = this instanceof HasEnvMap      && ((HasEnvMap)this).getEnvMap() != null;
		parameters.lightMap     = this instanceof HasLightMap    &&  ((HasLightMap)this).getLightMap() != null;
		parameters.bumpMap      = this instanceof HasBumpMap     &&  ((HasBumpMap)this).getBumpMap() != null;
		parameters.normalMap    = this instanceof HasNormalMap   &&  ((HasNormalMap)this).getNormalMap() != null;
		parameters.specularMap  = this instanceof HasSpecularMap &&  ((HasSpecularMap)this).getSpecularMap() != null;
		parameters.alphaMap     = this instanceof HasAlphaMap    &&  ((HasAlphaMap)this).getAlphaMap() != null;

		parameters.vertexColors = this instanceof HasVertexColors && ((HasVertexColors)this).isVertexColors() != Material.COLORS.NO;

		parameters.sizeAttenuation = this instanceof PointCloudMaterial && ((PointCloudMaterial)this).isSizeAttenuation();

		if(this instanceof HasSkinning)
		{
			parameters.skinning     = ((HasSkinning)this).isSkinning();
			parameters.morphTargets = ((HasSkinning)this).isMorphTargets();
			parameters.morphNormals = ((HasSkinning)this).isMorphNormals();
		}

		parameters.alphaTest = getAlphaTest();
		if(this instanceof MeshPhongMaterial)
		{
			parameters.metal = ((MeshPhongMaterial)this).isMetal();
		}

		parameters.wrapAround = this instanceof HasWrap && ((HasWrap)this).isWrapAround();
		parameters.doubleSided = this.getSides() == Material.SIDE.DOUBLE;
		parameters.flipSided = this.getSides() == Material.SIDE.BACK;
	}

	public Shader buildShader(GL20 gl, ProgramParameters parameters)
	{
		Shader shader = getShader();

		shader.setPrecision(parameters.precision);

		shader.setVertexExtensions(getExtensionsVertex(parameters));
		shader.setFragmentExtensions(getExtensionsFragment(parameters));

		shader.setVertexSource(getPrefixVertex(parameters) + "\n" + shader.getVertexSource());
		shader.setFragmentSource(getPrefixFragment(parameters) + "\n" + shader.getFragmentSource());

		this.shader = shader.buildProgram(gl, parameters.useVertexTexture, parameters.maxMorphTargets, parameters.maxMorphNormals);

		return this.shader;
	}

	protected String getExtensionsVertex(ProgramParameters parameters)
	{
		return "";
	}

	private String getPrefixVertex(ProgramParameters parameters)
	{
		Log.debug("Shader.getPrefixVertex() Called");
		List<String> options = new ArrayList<String>();

		options.add("");

		if (parameters.supportsVertexTextures)
			options.add(SHADER_DEFINE.VERTEX_TEXTURES.getValue());

		if (parameters.gammaInput)
			options.add(SHADER_DEFINE.GAMMA_INPUT.getValue());

		if (parameters.gammaOutput)
			options.add(SHADER_DEFINE.GAMMA_OUTPUT.getValue());

		options.add(SHADER_DEFINE.MAX_DIR_LIGHTS.getValue(parameters.maxDirLights));
		options.add(SHADER_DEFINE.MAX_POINT_LIGHTS.getValue(parameters.maxPointLights));
		options.add(SHADER_DEFINE.MAX_SPOT_LIGHTS.getValue(parameters.maxSpotLights));
		options.add(SHADER_DEFINE.MAX_HEMI_LIGHTS.getValue(parameters.maxHemiLights));

		options.add(SHADER_DEFINE.MAX_SHADOWS.getValue(parameters.maxShadows));

		options.add(SHADER_DEFINE.MAX_BONES.getValue(parameters.maxBones));

		if (parameters.map)
			options.add(SHADER_DEFINE.USE_MAP.getValue());
		if (parameters.envMap)
			options.add(SHADER_DEFINE.USE_ENVMAP.getValue());
		if (parameters.lightMap)
			options.add(SHADER_DEFINE.USE_LIGHTMAP.getValue());
		if (parameters.bumpMap)
			options.add(SHADER_DEFINE.USE_BUMPMAP.getValue());
		if (parameters.normalMap)
			options.add(SHADER_DEFINE.USE_NORMALMAP.getValue());
		if (parameters.specularMap)
			options.add(SHADER_DEFINE.USE_SPECULARMAP.getValue());
		if (parameters.alphaMap)
			options.add(SHADER_DEFINE.USE_ALPHAMAP.getValue());
		if (parameters.vertexColors)
			options.add(SHADER_DEFINE.USE_COLOR.getValue());

		if (parameters.skinning)
			options.add(SHADER_DEFINE.USE_SKINNING.getValue());
		if (parameters.useVertexTexture)
			options.add(SHADER_DEFINE.BONE_TEXTURE.getValue());

		if (parameters.morphTargets)
			options.add(SHADER_DEFINE.USE_MORPHTARGETS.getValue());
		if (parameters.morphNormals)
			options.add(SHADER_DEFINE.USE_MORPHNORMALS.getValue());
		if (parameters.wrapAround)
			options.add(SHADER_DEFINE.WRAP_AROUND.getValue());
		if (parameters.doubleSided)
			options.add(SHADER_DEFINE.DOUBLE_SIDED.getValue());
		if (parameters.flipSided)
			options.add(SHADER_DEFINE.FLIP_SIDED.getValue());

		if (parameters.shadowMapEnabled) {
			options.add(SHADER_DEFINE.USE_SHADOWMAP.getValue());
			if (parameters.shadowMapSoft)
				options.add(SHADER_DEFINE.SHADOWMAP_TYPE_PCF.getValue());
			else
				options.add(SHADER_DEFINE.SHADOWMAP_TYPE_BASIC.getValue());
		}

		if (parameters.shadowMapDebug)
			options.add(SHADER_DEFINE.SHADOWMAP_DEBUG.getValue());
		if (parameters.shadowMapCascade)
			options.add(SHADER_DEFINE.SHADOWMAP_CASCADE.getValue());

		if (parameters.sizeAttenuation)
			options.add(SHADER_DEFINE.USE_SIZEATTENUATION.getValue());

		if (parameters.logarithmicDepthBuffer)
			options.add(SHADER_DEFINE.USE_LOGDEPTHBUF.getValue());

		options.add("");

		String retval = "";
		for(String opt: options)
			retval += opt + "\n";

		List<String> extra = Arrays.asList("uniform mat4 modelMatrix;",
				"uniform mat4 modelViewMatrix;",
				"uniform mat4 projectionMatrix;",
				"uniform mat4 viewMatrix;",
				"uniform mat3 normalMatrix;",
				"uniform vec3 cameraPosition;",

				"attribute vec3 position;",
				"attribute vec3 normal;",
				"attribute vec2 uv;",
				"attribute vec2 uv2;",

				"#ifdef USE_COLOR",

				"	attribute vec3 color;",

				"#endif",

				"#ifdef USE_MORPHTARGETS",

				"	attribute vec3 morphTarget0;",
				"	attribute vec3 morphTarget1;",
				"	attribute vec3 morphTarget2;",
				"	attribute vec3 morphTarget3;",

				"	#ifdef USE_MORPHNORMALS",

				"		attribute vec3 morphNormal0;",
				"		attribute vec3 morphNormal1;",
				"		attribute vec3 morphNormal2;",
				"		attribute vec3 morphNormal3;",

				"	#else",

				"		attribute vec3 morphTarget4;",
				"		attribute vec3 morphTarget5;",
				"		attribute vec3 morphTarget6;",
				"		attribute vec3 morphTarget7;",

				"	#endif",

				"#endif",

				"#ifdef USE_SKINNING",

				"	attribute vec4 skinIndex;",
				"	attribute vec4 skinWeight;",

				"#endif");

		for(String opt: extra)
			retval += opt + "\n";

		return retval;
	}

	protected String getExtensionsFragment(ProgramParameters parameters)
	{
		String s = "";
		if (parameters.logarithmicDepthBuffer)
			s += "#extension GL_EXT_frag_depth : enable\n\n";
		if (parameters.bumpMap || parameters.normalMap)
			s += "#extension GL_OES_standard_derivatives : enable\n\n";
		return s;
	}

	private String getPrefixFragment(ProgramParameters parameters)
	{
		List<String> options = new ArrayList<String>();

		options.add("");

		options.add(SHADER_DEFINE.MAX_DIR_LIGHTS.getValue(parameters.maxDirLights));
		options.add(SHADER_DEFINE.MAX_POINT_LIGHTS.getValue(parameters.maxPointLights));
		options.add(SHADER_DEFINE.MAX_SPOT_LIGHTS.getValue(parameters.maxSpotLights));
		options.add(SHADER_DEFINE.MAX_HEMI_LIGHTS.getValue(parameters.maxHemiLights));

		options.add(SHADER_DEFINE.MAX_SHADOWS.getValue(parameters.maxShadows));

		if (parameters.alphaTest > 0)
			options.add(SHADER_DEFINE.ALPHATEST.getValue(parameters.alphaTest));

		if (parameters.gammaInput)
			options.add(SHADER_DEFINE.GAMMA_INPUT.getValue());
		if (parameters.gammaOutput)
			options.add(SHADER_DEFINE.GAMMA_OUTPUT.getValue());

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
		if (parameters.bumpMap)
			options.add(SHADER_DEFINE.USE_BUMPMAP.getValue());
		if (parameters.normalMap)
			options.add(SHADER_DEFINE.USE_NORMALMAP.getValue());
		if (parameters.specularMap)
			options.add(SHADER_DEFINE.USE_SPECULARMAP.getValue());
		if (parameters.alphaMap)
			options.add(SHADER_DEFINE.USE_ALPHAMAP.getValue());
		if (parameters.vertexColors)
			options.add(SHADER_DEFINE.USE_COLOR.getValue());

		if (parameters.metal)
			options.add(SHADER_DEFINE.METAL.getValue());
		if (parameters.wrapAround)
			options.add(SHADER_DEFINE.WRAP_AROUND.getValue());
		if (parameters.doubleSided)
			options.add(SHADER_DEFINE.DOUBLE_SIDED.getValue());
		if (parameters.flipSided)
			options.add(SHADER_DEFINE.FLIP_SIDED.getValue());

		if (parameters.shadowMapEnabled) {
			options.add(SHADER_DEFINE.USE_SHADOWMAP.getValue());
			if (parameters.shadowMapSoft)
				options.add(SHADER_DEFINE.SHADOWMAP_TYPE_PCF.getValue());
			else
				options.add(SHADER_DEFINE.SHADOWMAP_TYPE_BASIC.getValue());
		}

		if (parameters.shadowMapDebug)
			options.add(SHADER_DEFINE.SHADOWMAP_DEBUG.getValue());
		if (parameters.shadowMapCascade)
			options.add(SHADER_DEFINE.SHADOWMAP_CASCADE.getValue());

		if (parameters.logarithmicDepthBuffer)
			options.add(SHADER_DEFINE.USE_LOGDEPTHBUF.getValue());

		options.add("");
		String retval = "";
		for(String opt: options)
			retval += opt + "\n";

		List<String> extra = Arrays.asList(
				"uniform mat4 viewMatrix;",
				"uniform vec3 cameraPosition;");

		for(String opt: extra)
			retval += opt + "\n";

		return retval;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public void refreshUniforms(Camera camera, boolean isGammaInput)
	{
		if ( ! (this instanceof HasMaterialMap) )
			return;

		FastMap< Uniform> uniforms = getShader().getUniforms();

		uniforms.get("opacity").setValue( getOpacity() );

		if(this instanceof HasColor)
		{
			if ( isGammaInput )
				((Color) uniforms.get("diffuse").getValue()).copyGammaToLinear( ((HasColor)this).getColor() );

			else
				uniforms.get("diffuse").setValue( ((HasColor)this).getColor() );
		}

		if(this instanceof HasMap)
		{
			uniforms.get("map").setValue( ((HasMap) this).getMap() );
		}

		if(this instanceof HasLightMap)
			uniforms.get("lightMap").setValue( ((HasLightMap)this).getLightMap() );

		if(this instanceof HasSpecularMap)
		{
			uniforms.get("specularMap").setValue( ((HasSpecularMap)this).getSpecularMap() );
		}

		if(this instanceof HasAlphaMap)
		{
			uniforms.get("alphaMap").setValue( ((HasAlphaMap)this).getAlphaMap() );
		}

		if(this instanceof HasBumpMap)
		{
			uniforms.get("bumpMap").setValue( ((HasBumpMap)this).getBumpMap() );
			uniforms.get("bumpScale").setValue( ((HasBumpMap)this).getBumpScale() );
		}

		if(this instanceof HasNormalMap)
		{
			uniforms.get("normalMap").setValue( ((HasNormalMap)this).getNormalMap() );
			uniforms.get("normalScale").setValue( ((HasNormalMap)this).getNormalScale() );
		}

		// uv repeat and offset setting priorities
		//  1. color map
		//  2. specular map
		//  3. normal map
		//  4. bump map
		//  5. alpha map
		Texture uvScaleMap = null;

		if(this instanceof HasMap)
			uvScaleMap = ((HasMap) this).getMap();
		if(uvScaleMap == null && this instanceof HasSpecularMap)
			uvScaleMap = ((HasSpecularMap)this).getSpecularMap();
		if(uvScaleMap == null && this instanceof HasNormalMap)
			uvScaleMap = ((HasNormalMap)this).getNormalMap();
		if(uvScaleMap == null && this instanceof HasBumpMap)
			uvScaleMap = ((HasBumpMap)this).getBumpMap();
		if(uvScaleMap == null && this instanceof HasAlphaMap)
			uvScaleMap = ((HasAlphaMap)this).getAlphaMap();

		if(uvScaleMap != null)
		{
			((Vector4)uniforms.get("offsetRepeat").getValue()).set(
					uvScaleMap.getOffset().getX(),
					uvScaleMap.getOffset().getY(),
					uvScaleMap.getRepeat().getX(),
					uvScaleMap.getRepeat().getY() );
		}

		if(this instanceof HasEnvMap)
		{
			HasEnvMap envMapMaterial = (HasEnvMap)this;

			uniforms.get("envMap").setValue( envMapMaterial.getEnvMap() );
			uniforms.get("flipEnvMap").setValue( ( envMapMaterial.getEnvMap() != null
					&& envMapMaterial.getEnvMap().getClass() == RenderTargetCubeTexture.class ) ? 1.0 : -1.0 );

			if ( isGammaInput )
				uniforms.get("reflectivity").setValue( envMapMaterial.getReflectivity() );

			else
				uniforms.get("reflectivity").setValue( envMapMaterial.getReflectivity() );

			uniforms.get("refractionRatio").setValue( envMapMaterial.getRefractionRatio() );
			uniforms.get("combine").setValue( envMapMaterial.getCombine().getValue() );
			uniforms.get("useRefract").setValue( ( envMapMaterial.getEnvMap() != null
					&& envMapMaterial.getEnvMap().getMapping() == Texture.MAPPING_MODE.CUBE_REFRACTION ) ? 1 : 0 );
		}
	}

	public boolean materialNeedsSmoothNormals()
	{
		return this instanceof HasShading && ((HasShading)this).getShading() != null && ((HasShading)this).getShading() == Material.SHADING.SMOOTH;
	}

	public Material.COLORS bufferGuessVertexColorType()
	{
		if(this instanceof HasVertexColors && ((HasVertexColors)this).isVertexColors() != Material.COLORS.NO)
			return ((HasVertexColors)this).isVertexColors();

		return null;
	}

	public boolean bufferGuessUVType()
	{
		if(this instanceof HasMap && ((HasMap)this).getMap() != null)
			return true;

		if(this instanceof HasLightMap && ((HasLightMap)this).getLightMap() != null)
			return true;

		if(this instanceof HasBumpMap && ((HasBumpMap)this).getBumpMap() != null)
			return true;

		if(this instanceof HasNormalMap && ((HasNormalMap)this).getNormalMap() != null)
			return true;

		if(this instanceof HasSpecularMap && ((HasSpecularMap)this).getSpecularMap() != null)
			return true;

		return false;
	}

	public static Material getBufferMaterial( GeometryObject object, GeometryGroup geometryGroup )
	{
		Material material = null;
		if ( object.getMaterial() instanceof MeshFaceMaterial )
		{
			material = ((MeshFaceMaterial)object.getMaterial()).getMaterials().get( geometryGroup.getMaterialIndex() );
		}
		else if ( geometryGroup.getMaterialIndex() >= 0 )
		{
			material = object.getMaterial();
		}

		return material;
	}


	public void deallocate( GLRenderer renderer )
	{
		int program = getShader().getProgram();
		if ( program == 0 ) {
			return;
		}

//		getShader().setPrecision(null);

		// only deallocate GL program if this was the last use of shared program
		// assumed there is only single copy of any program in the _programs list
		// (that's how it's constructed)

		boolean deleteProgram = false;

		for ( String key: renderer._programs.keySet())
		{
			Shader shader = renderer._programs.get(key);

			if ( shader == getShader() )
			{
				renderer._programs.remove(key);
				deleteProgram = true;
				break;
			}
		}

		if ( deleteProgram )
		{

			renderer.gl.glDeleteProgram(program);

			renderer.getInfo().getMemory().programs --;
		}

	}

	public String toString()
	{
		return this.getClass().getSimpleName() + " { id=" + this.getId() + " }";
	}
}
