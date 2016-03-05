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

import org.parallax3d.parallax.graphics.renderers.shaders.LambertShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.textures.Texture.OPERATIONS;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.math.Color;

/**
 * A material for non-shiny (Lambertian) surfaces, evaluated per vertex.
 *
 */
@ThreejsObject("THREE.MeshLambertMaterial")
public final class MeshLambertMaterial extends Material 
	implements HasMaterialMap, HasWrap, HasWireframe, HasFog, HasVertexColors,
	HasSkinning, HasAmbientEmissiveColor, HasShading
{
	private Color color;
	private Color ambient;
	private Color emissive;

	private boolean isWrapAround;
	private Vector3 wrapRGB;

	private Texture map;
	private Texture lightMap;
	private Texture specularMap;
	private Texture alphaMap;

	private Texture envMap;
	private Texture.OPERATIONS combine;
	private double reflectivity;
	private double refractionRatio;

	private boolean isFog;

	private Material.SHADING shading;

	private boolean isWireframe;
	private int wireframeLineWidth;

	private Material.COLORS vertexColors;

	private boolean isSkinning;
	private boolean isMorphTargets;
	private boolean isMorphNormals;

	private int numSupportedMorphTargets;
	private int numSupportedMorphNormals;

	public MeshLambertMaterial()
	{
		setWrapRGB(new Vector3( 1, 1, 1 ));
		setWrapAround(false);

		setWireframe(false);
		setWireframeLineWidth(1);

		setCombine(OPERATIONS.MULTIPLY);
		setReflectivity(1.0);
		setRefractionRatio(0.98);

		setFog(true);

		setShading(Material.SHADING.SMOOTH);

		setColor(0xffffff); // diffuse
		setAmbient(0xffffff);
		setEmissive(0x000000);

		setVertexColors(Material.COLORS.NO);
	}

	@Override
	public Shader getAssociatedShader()
	{
		return new LambertShader();
	}

	@Override
	public boolean isWrapAround() {
		return this.isWrapAround;
	}

	@Override
	public MeshLambertMaterial setWrapAround(boolean wrapAround) {
		this.isWrapAround = wrapAround;
		return this;
	}

	@Override
	public Vector3 getWrapRGB() {
		return this.wrapRGB;
	}

	@Override
	public MeshLambertMaterial setWrapRGB(Vector3 wrapRGB) {
		this.wrapRGB = wrapRGB;
		return this;
	}

	@Override
	public boolean isWireframe() {
		return this.isWireframe;
	}

	@Override
	public MeshLambertMaterial setWireframe(boolean wireframe) {
		this.isWireframe = wireframe;
		return this;
	}

	@Override
	public int getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshLambertMaterial setWireframeLineWidth(int wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	@Override
	public Texture getEnvMap() {
		return this.envMap;
	}

	@Override
	public MeshLambertMaterial setEnvMap(Texture envMap) {
		this.envMap = envMap;
		return this;
	}

	@Override
	public Texture getAlphaMap() {
		return this.alphaMap;
	}

	@Override
	public MeshLambertMaterial setAlphaMap(Texture alphaMap) {
		this.alphaMap = alphaMap;
		return this;
	}

	@Override
	public OPERATIONS getCombine() {
		return this.combine;
	}

	@Override
	public MeshLambertMaterial setCombine(OPERATIONS combine) {
		this.combine = combine;
		return this;
	}

	@Override
	public double getReflectivity() {
		return this.reflectivity;
	}

	@Override
	public MeshLambertMaterial setReflectivity(double reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}

	@Override
	public double getRefractionRatio() {
		return this.refractionRatio;
	}

	@Override
	public MeshLambertMaterial setRefractionRatio(double refractionRatio) {
		this.refractionRatio = refractionRatio;
		return this;
	}

	@Override
	public Texture getLightMap() {
		return this.lightMap;
	}

	@Override
	public MeshLambertMaterial setLightMap(Texture lightMap) {
		this.lightMap = lightMap;
		return this;
	}

	@Override
	public boolean isFog() {
		return this.isFog;
	}

	@Override
	public MeshLambertMaterial setFog(boolean fog) {
		this.isFog = fog;
		return this;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public MeshLambertMaterial setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public MeshLambertMaterial setColor(int color) {
		this.color = new Color( color );
		return this;
	}

	@Override
	public Texture getMap() {
		return this.map;
	}

	@Override
	public MeshLambertMaterial setMap(Texture map) {
		this.map = map;
		return this;
	}

	@Override
	public Material.COLORS isVertexColors() {
		return this.vertexColors;
	}

	@Override
	public MeshLambertMaterial setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
		return this;
	}

	@Override
	public boolean isSkinning() {
		return this.isSkinning;
	}

	@Override
	public MeshLambertMaterial setSkinning(boolean isSkinning) {
		this.isSkinning = isSkinning;
		return this;
	}

	@Override
	public boolean isMorphTargets() {
		return this.isMorphTargets;
	}

	@Override
	public MeshLambertMaterial setMorphTargets(boolean isMorphTargets) {
		this.isMorphTargets = isMorphTargets;
		return this;
	}

	@Override
	public boolean isMorphNormals() {
		return this.isMorphNormals;
	}

	@Override
	public MeshLambertMaterial setMorphNormals(boolean isMorphNormals) {
		this.isMorphNormals = isMorphNormals;
		return this;
	}

	@Override
	public Color getAmbient() {
		return this.ambient;
	}

	@Override
	public MeshLambertMaterial setAmbient(Color ambient) {
		this.ambient = ambient;
		return this;
	}

	@Override
	public MeshLambertMaterial setAmbient(int ambient) {
		this.ambient = new Color( ambient );
		return this;
	}

	@Override
	public Color getEmissive() {
		return this.emissive;
	}

	@Override
	public MeshLambertMaterial setEmissive(Color emissive) {
		this.emissive = emissive;
		return this;
	}

	@Override
	public MeshLambertMaterial setEmissive(int emissive) {
		this.emissive = new Color( emissive );
		return this;
	}

	@Override
	public int getNumSupportedMorphTargets() {
		return this.numSupportedMorphTargets;
	}

	@Override
	public MeshLambertMaterial setNumSupportedMorphTargets(int num) {
		this.numSupportedMorphTargets = num;
		return this;
	}

	@Override
	public int getNumSupportedMorphNormals() {
		return this.numSupportedMorphNormals;
	}

	@Override
	public MeshLambertMaterial setNumSupportedMorphNormals(int num) {
		this.numSupportedMorphNormals = num;
		return this;
	}

	@Override
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	@Override
	public MeshLambertMaterial setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
		return this;
	}

	public Material.SHADING getShading() {
		return this.shading;
	}

	public MeshLambertMaterial setShading(Material.SHADING shading) {
		this.shading = shading;
		return this;
	}

	@Override
	public MeshLambertMaterial clone() {

		MeshLambertMaterial material = new MeshLambertMaterial();

		super.clone(material);

		material.color.copy( this.color );
		material.ambient.copy( this.ambient );
		material.emissive.copy( this.emissive );

		material.isWrapAround = this.isWrapAround;
		material.wrapRGB.copy( this.wrapRGB );

		material.map = this.map;

		material.lightMap = this.lightMap;

		material.specularMap = this.specularMap;

		material.alphaMap = this.alphaMap;

		material.envMap = this.envMap;
		material.combine = this.combine;
		material.reflectivity = this.reflectivity;
		material.refractionRatio = this.refractionRatio;

		material.isFog = this.isFog;

		material.shading = this.shading;

		material.isWireframe = this.isWireframe;
		material.wireframeLineWidth = this.wireframeLineWidth;

		material.vertexColors = this.vertexColors;

		material.isSkinning = this.isSkinning;
		material.isMorphTargets = this.isMorphTargets;
		material.isMorphNormals = this.isMorphNormals;

		return material;

	}

	@Override
	public void refreshUniforms(Camera camera, boolean isGammaInput)
	{
		super.refreshUniforms(camera, isGammaInput);
		FastMap<Uniform> uniforms = getShader().getUniforms();

		if ( isGammaInput )
		{
			((Color) uniforms.get("ambient").getValue()).copyGammaToLinear( getAmbient() );
			((Color) uniforms.get("emissive").getValue()).copyGammaToLinear( getEmissive() );
		}
		else
		{
			uniforms.get("ambient").setValue( getAmbient() );
			uniforms.get("emissive").setValue( getEmissive() );
		}

		if ( isWrapAround() )
		{
			((Vector3) uniforms.get("wrapRGB").getValue()).copy( getWrapRGB() );
		}
	}
}
