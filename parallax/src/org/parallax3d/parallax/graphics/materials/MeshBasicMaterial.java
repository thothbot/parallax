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

import org.parallax3d.parallax.graphics.renderers.shaders.BasicShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;

/**
 * A material for drawing geometries in a simple shaded (flat or wireframe) way.
 * <p>
 * The default will render as flat polygons. To draw the mesh as wireframe, simply set {@link #setWireframe(boolean)} to true.

 * @author thothbot
 *
 */
@ThreejsObject("THREE.MeshBasicMaterial")
public class MeshBasicMaterial extends Material
	implements HasMaterialMap, HasWireframe, HasFog, HasVertexColors, HasSkinning, HasShading
{
	private Color color;

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

	private boolean isWireframe = false;
	private int wireframeLineWidth;

	private Material.COLORS vertexColors;

	private boolean isSkinning;
	private boolean isMorphTargets;
	private boolean isMorphNormals;

	private int numSupportedMorphTargets;
	private int numSupportedMorphNormals;

	public MeshBasicMaterial()
	{
		setWireframe(false);
		setWireframeLineWidth(1);

		setCombine(Texture.OPERATIONS.MULTIPLY);
		setReflectivity(1.0);
		setRefractionRatio(0.98);

		setShading(SHADING.SMOOTH);

		setFog(true);

		setColor(0xffffff);

		setVertexColors(Material.COLORS.NO);
	}

	@Override
	public Shader getAssociatedShader()
	{
		return new BasicShader();
	}

	@Override
	public boolean isWireframe() {
		return this.isWireframe;
	}

	@Override
	public MeshBasicMaterial setWireframe(boolean wireframe) {
		this.isWireframe = wireframe;
		return this;
	}

	@Override
	public int getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshBasicMaterial setWireframeLineWidth(int wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	@Override
	public Texture getEnvMap() {
		return this.envMap;
	}

	@Override
	public MeshBasicMaterial setEnvMap(Texture envMap) {
		this.envMap = envMap;
		return this;
	}

	@Override
	public Texture getAlphaMap() {
		return this.alphaMap;
	}

	@Override
	public MeshBasicMaterial setAlphaMap(Texture alphaMap) {
		this.alphaMap = alphaMap;
		return this;
	}

	@Override
	public Texture.OPERATIONS getCombine() {
		return this.combine;
	}

	@Override
	public MeshBasicMaterial setCombine(Texture.OPERATIONS combine) {
		this.combine = combine;
		return this;
	}

	@Override
	public double getReflectivity() {
		return this.reflectivity;
	}

	@Override
	public MeshBasicMaterial setReflectivity(double reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}

	@Override
	public double getRefractionRatio() {
		return this.refractionRatio;
	}

	@Override
	public MeshBasicMaterial setRefractionRatio(double refractionRatio) {
		this.refractionRatio = refractionRatio;
		return this;
	}

	@Override
	public Texture getLightMap() {
		return this.lightMap;
	}

	@Override
	public MeshBasicMaterial setLightMap(Texture lightMap) {
		this.lightMap = lightMap;
		return this;
	}

	@Override
	public boolean isFog() {
		return this.isFog;
	}

	@Override
	public MeshBasicMaterial setFog(boolean fog) {
		this.isFog = fog;
		return this;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public MeshBasicMaterial setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public MeshBasicMaterial setColor(int color) {
		this.color = new Color( color );
		return this;
	}

	@Override
	public Material.COLORS isVertexColors() {
		return this.vertexColors;
	}

	@Override
	public MeshBasicMaterial setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
		return this;
	}

	@Override
	public boolean isSkinning() {
		return this.isSkinning;
	}

	@Override
	public MeshBasicMaterial setSkinning(boolean isSkinning) {
		this.isSkinning = isSkinning;
		return this;
	}

	@Override
	public boolean isMorphTargets() {
		return this.isMorphTargets;
	}

	@Override
	public MeshBasicMaterial setMorphTargets(boolean isMorphTargets) {
		this.isMorphTargets = isMorphTargets;
		return this;
	}

	// Should be false
	@Override
	public boolean isMorphNormals() {
		return false;
	}

	@Override
	public MeshBasicMaterial setMorphNormals(boolean isMorphNormals) {
		this.isMorphNormals = isMorphNormals;
		return this;
	}

	@Override
	public int getNumSupportedMorphTargets() {
		return this.numSupportedMorphTargets;
	}

	@Override
	public MeshBasicMaterial setNumSupportedMorphTargets(int num) {
		this.numSupportedMorphTargets = num;
		return this;
	}

	@Override
	public int getNumSupportedMorphNormals() {
		return this.numSupportedMorphNormals;
	}

	@Override
	public MeshBasicMaterial setNumSupportedMorphNormals(int num) {
		this.numSupportedMorphNormals = num;
		return this;
	}

	@Override
	public Texture getMap() {
		return this.map;
	}

	@Override
	public MeshBasicMaterial setMap(Texture map) {
		this.map = map;
		return this;
	}

	@Override
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	@Override
	public MeshBasicMaterial setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
		return this;
	}

	public Material.SHADING getShading() {
		return this.shading;
	}

	public MeshBasicMaterial setShading(Material.SHADING shading) {
		this.shading = shading;
		return this;
	}

	@Override
	public MeshBasicMaterial clone() {

		MeshBasicMaterial material = new MeshBasicMaterial();

		super.clone(material);

		material.color.copy( this.color );

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

		return material;

	}
}
