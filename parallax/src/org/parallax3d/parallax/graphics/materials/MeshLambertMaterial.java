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
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.textures.Texture.OPERATIONS;
import org.parallax3d.parallax.math.Color;

/**
 * A material for non-shiny (Lambertian) surfaces, evaluated per vertex.
 *
 */
@ThreejsObject("THREE.MeshLambertMaterial")
public final class MeshLambertMaterial extends Material 
	implements HasWireframe, HasFog, HasVertexColors, HasShading,
	HasSkinning, HasEmissiveColor, HasColor, HasMap, HasAoMap, HasLightMap, HasEnvMap, HasEmissiveMap, HasSpecularMap, HasAlphaMap
{

	Color color = new Color(0xffffff);

	Texture map;

	Texture lightMap;
	double lightMapIntensity = 1.0;

	Texture aoMap;
	double aoMapIntensity = 1.0;

	Color emissive = new Color(0x000000);

	Texture emissiveMap;
	double emissiveIntensity = 1.0;

	Texture specularMap;

	Texture alphaMap;

	Texture envMap;
	Texture.OPERATIONS combine = OPERATIONS.MULTIPLY;
	double reflectivity = 1.0;
	double refractionRatio = 0.98;

	boolean fog = true;

	boolean wireframe = false;
	double wireframeLineWidth = 1.0;
	String wireframeLineCap = "round";
	String wireframeLineJoin = "round";

	Material.COLORS vertexColors = COLORS.NO;

	boolean skinning = false;
	boolean morphTargets = false;
	boolean morphNormals = false;

	Material.SHADING shading = SHADING.SMOOTH;

	@Override
	public Shader getAssociatedShader()
	{
		return new LambertShader();
	}

	@Override
	public Material clone() {
		return null;
	}

	@Override
	public boolean isWireframe() {
		return false;
	}

	@Override
	public <T extends Material> T setWireframe(boolean wireframe) {
		return null;
	}

	@Override
	public double getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshLambertMaterial setWireframeLineWidth(double wireframeLineWidth) {
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
	public double getLightMapIntensity() {
		return lightMapIntensity;
	}

	@Override
	public MeshLambertMaterial setLightMapIntensity(double intensity) {
		this.lightMapIntensity = lightMapIntensity;
		return this;
	}

	@Override
	public MeshLambertMaterial setLightMap(Texture lightMap) {
		this.lightMap = lightMap;
		return this;
	}

	@Override
	public boolean isFog() {
		return this.fog;
	}

	@Override
	public MeshLambertMaterial setFog(boolean fog) {
		this.fog = fog;
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
	public Material.COLORS getVertexColors() {
		return this.vertexColors;
	}

	@Override
	public MeshLambertMaterial setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
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
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	@Override
	public MeshLambertMaterial setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
		return this;
	}


	@Override
	public Texture getAoMap() {
		return aoMap;
	}

	@Override
	public MeshLambertMaterial setAoMap(Texture aoMap) {
		this.aoMap = aoMap;
		return this;
	}

	@Override
	public double getAoMapIntensity() {
		return aoMapIntensity;
	}

	@Override
	public MeshLambertMaterial setAoMapIntensity(double aoMapIntensity) {
		this.aoMapIntensity = aoMapIntensity;
		return this;
	}

	@Override
	public Texture getEmissiveMap() {
		return emissiveMap;
	}

	@Override
	public MeshLambertMaterial setEmissiveMap(Texture emissiveMap) {
		this.emissiveMap = emissiveMap;
		return this;
	}

	@Override
	public boolean isSkinning() {
		return skinning;
	}

	@Override
	public MeshLambertMaterial setSkinning(boolean skinning) {
		this.skinning = skinning;
		return this;
	}

	@Override
	public boolean isMorphTargets() {
		return morphTargets;
	}

	@Override
	public MeshLambertMaterial setMorphTargets(boolean morphTargets) {
		this.morphTargets = morphTargets;
		return this;
	}

	@Override
	public boolean isMorphNormals() {
		return morphNormals;
	}

	@Override
	public MeshLambertMaterial setMorphNormals(boolean morphNormals) {
		this.morphNormals = morphNormals;
		return this;
	}

	public Material.SHADING getShading() {
		return this.shading;
	}

	public MeshLambertMaterial setShading(Material.SHADING shading) {
		this.shading = shading;
		return this;
	}

	public MeshLambertMaterial copy(MeshLambertMaterial source) {

		super.copy( source );

		this.color.copy( source.color );

		this.map = source.map;

		this.lightMap = source.lightMap;
		this.lightMapIntensity = source.lightMapIntensity;

		this.aoMap = source.aoMap;
		this.aoMapIntensity = source.aoMapIntensity;

		this.emissive.copy( source.emissive );
		this.emissiveMap = source.emissiveMap;
		this.emissiveIntensity = source.emissiveIntensity;

		this.specularMap = source.specularMap;

		this.alphaMap = source.alphaMap;

		this.envMap = source.envMap;
		this.combine = source.combine;
		this.reflectivity = source.reflectivity;
		this.refractionRatio = source.refractionRatio;

		this.fog = source.fog;

		this.wireframe = source.wireframe;
		this.wireframeLineWidth = source.wireframeLineWidth;
		this.wireframeLineCap = source.wireframeLineCap;
		this.wireframeLineJoin = source.wireframeLineJoin;

		this.vertexColors = source.vertexColors;

		this.skinning = source.skinning;
		this.morphTargets = source.morphTargets;
		this.morphNormals = source.morphNormals;

		return this;
	}
}
