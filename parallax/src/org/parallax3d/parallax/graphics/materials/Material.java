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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.system.AbstractPropertyObject;
import org.parallax3d.parallax.system.Disposable;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.events.DisposeEvent;
import org.parallax3d.parallax.system.events.ObjectUpdateEvent;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.enums.BlendEquationMode;
import org.parallax3d.parallax.system.gl.enums.BlendingFactorDest;
import org.parallax3d.parallax.system.gl.enums.BlendingFactorSrc;
import org.parallax3d.parallax.system.gl.enums.DepthFunction;

/**
 * Materials describe the appearance of objects. 
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Material")
public abstract class Material extends AbstractPropertyObject implements Disposable
{
	// When rendered geometry doesn't include these attributes but the material does,
	// use these default values in WebGL. This avoids errors when buffer data is missing.
	public static final FastMap<Float32Array> DEFAULT_ATTRIBUTE_VALUES = new FastMap<Float32Array>(){{
		put("color", Float32Array.create(new double[]{ 1., 1., 1. }));
		put("uv", Float32Array.create(new double[]{ 0, 0 }));
		put("uv2", Float32Array.create(new double[]{ 0, 0 }));
	}};

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

	int id;

	String name;

	// Store shader associated to the material
	Shader shader;

	SIDE side = SIDE.FRONT;

	double opacity = 1.0;
	boolean isTransparent = false;

	BLENDING blending = BLENDING.NORMAL;

	BlendingFactorSrc blendSrc = BlendingFactorSrc.SRC_ALPHA;
	BlendingFactorDest blendDst = BlendingFactorDest.ONE_MINUS_SRC_ALPHA;
	BlendEquationMode blendEquation = BlendEquationMode.FUNC_ADD;

	BlendingFactorSrc blendSrcAlpha = null;
	BlendingFactorDest blendDstAlpha = null;
	BlendEquationMode blendEquationAlpha = null;

	DepthFunction depthFunc = DepthFunction.LEQUAL;
	boolean isDepthTest = true;
	boolean isDepthWrite = true;

	boolean isColorWrite = true;

	/**
	 * override the renderer's default precision for this material
	 */
	Shader.PRECISION precision = null;

	boolean isPolygonOffset = false;
	double polygonOffsetFactor = 0.;
	double polygonOffsetUnits = 0.;

	double alphaTest = 0.;
	boolean premultipliedAlpha = false;

	/**
	 * Overdrawn pixels (typically between 0 and 1) for fixing antialiasing gaps in CanvasRenderer
	 */
	double overdraw = 0;

	boolean isVisible = true;
	boolean isNeedsUpdate = true;

	//

	boolean isShadowPass;

	public Material()
	{
		this.id = Material.MaterialCount++;
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

	public boolean isPremultipliedAlpha() {
		return premultipliedAlpha;
	}

	public <T extends Material> T setPremultipliedAlpha(boolean premultipliedAlpha) {
		this.premultipliedAlpha = premultipliedAlpha;
		return (T)this;
	}

	public <T extends Material> T setName(String name) {
		this.name = name;
		return (T)this;
	}

	public Shader.PRECISION getPrecision() {
		return precision;
	}

	public <T extends Material> T setPrecision(Shader.PRECISION precision) {
		this.precision = precision;
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

	public BlendingFactorSrc getBlendSrcAlpha() {
		return blendSrcAlpha;
	}

	public BlendingFactorDest getBlendDstAlpha() {
		return blendDstAlpha;
	}

	public BlendEquationMode getBlendEquationAlpha() {
		return blendEquationAlpha;
	}

	public DepthFunction getDepthFunc() {
		return depthFunc;
	}

	public boolean isColorWrite() {
		return isColorWrite;
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

	public void setShader(Shader shader) {
		this.shader = shader;
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

	public abstract Shader getAssociatedShader();

	public abstract Material clone();

	public Material copy( Material source )
	{
		this.name = source.name;

		this.side = source.side;

		this.opacity = source.opacity;
		this.isTransparent = source.isTransparent;

		this.blending = source.blending;

		this.blendSrc = source.blendSrc;
		this.blendDst = source.blendDst;
		this.blendEquation = source.blendEquation;
		this.blendSrcAlpha = source.blendSrcAlpha;
		this.blendDstAlpha = source.blendDstAlpha;
		this.blendEquationAlpha = source.blendEquationAlpha;

		this.depthFunc = source.depthFunc;
		this.isDepthTest = source.isDepthTest;
		this.isDepthWrite = source.isDepthWrite;

		this.isColorWrite = source.isColorWrite;

		this.precision = source.precision;

		this.isPolygonOffset = source.isPolygonOffset;
		this.polygonOffsetFactor = source.polygonOffsetFactor;
		this.polygonOffsetUnits = source.polygonOffsetUnits;

		this.alphaTest = source.alphaTest;
		this.premultipliedAlpha = source.premultipliedAlpha;

		this.overdraw = source.overdraw;

		this.isVisible = source.isVisible;

		return this;
	}

	public void update() {

		dispatchEvent( new ObjectUpdateEvent( this ) );

	}

	@Override
	public void dispose() {

		dispatchEvent( new DisposeEvent( this ) );

	}

	public String toString()
	{
		return this.getClass().getSimpleName() + " { id=" + this.getId() + " }";
	}
}
