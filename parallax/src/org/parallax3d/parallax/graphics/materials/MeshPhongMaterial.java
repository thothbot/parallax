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

import org.parallax3d.parallax.graphics.renderers.shaders.PhongShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * A material for shiny surfaces, evaluated per pixel.
 *
 */
@ThreejsObject("THREE.MeshPhongMaterial")
public class MeshPhongMaterial extends Material
	implements HasBumpMap, HasNormalMap, HasWireframe, HasFog, HasVertexColors,
		HasSkinning, HasEmissiveColor, HasShading, HasColor, HasMap, HasAoMap,
		HasEmissiveMap, HasDisplacementMap, HasLightMap, HasEnvMap, HasSpecularMap, HasAlphaMap
{
	Color color = new Color(0xffffff);
	Color specular = new Color(0x111111);
	double shininess = 30.;

	Texture map;

	Texture lightMap;
	double lightMapIntensity = 1.0;

	Texture aoMap;
	double aoMapIntensity = 1.0;

	Color emissive = new Color( 0x000000 );
	Texture emissiveMap;
	double emissiveIntensity = 1.0;

	Texture bumpMap;
	double bumpScale = 1.0;

	Texture normalMap;
	Vector2 normalScale = new Vector2( 1., 1. );

	Texture displacementMap;
	double displacementScale = 1.0;
	double displacementBias = 0.0;

	Texture specularMap;

	Texture alphaMap;

	Texture envMap;
	Texture.OPERATIONS combine = Texture.OPERATIONS.MultiplyOperation;
	double reflectivity = 1.0;
	double refractionRatio = 0.98;

	boolean fog = true;

	Material.SHADING shading = SHADING.SMOOTH;

	boolean wireframe = false;
	double wireframeLineWidth = 1.0;
	String wireframeLineCap = "round";
	String wireframeLineJoin = "round";

	Material.COLORS vertexColors = COLORS.NO;

	boolean skinning;
	boolean morphTargets;
	boolean morphNormals;

	@Override
	public Shader getAssociatedShader() {
		return new PhongShader();
	}

	public Color getSpecular() {
		return specular;
	}

	public MeshPhongMaterial setSpecular(Color specular) {
		this.specular = specular;
		return this;
	}

	public MeshPhongMaterial setSpecular(int specular) {
		this.specular = new Color(specular);
		return this;
	}

	public double getShininess() {
		return shininess;
	}

	public MeshPhongMaterial setShininess(double shininess) {
		this.shininess = shininess;
		return this;
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
	public MeshPhongMaterial setWireframeLineWidth(double wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	@Override
	public Texture getEnvMap() {
		return this.envMap;
	}

	@Override
	public MeshPhongMaterial setEnvMap(Texture envMap) {
		this.envMap = envMap;
		return this;
	}

	@Override
	public Texture.OPERATIONS getCombine() {
		return this.combine;
	}

	@Override
	public MeshPhongMaterial setCombine(Texture.OPERATIONS combine) {
		this.combine = combine;
		return this;
	}

	@Override
	public double getReflectivity() {
		return this.reflectivity;
	}

	@Override
	public MeshPhongMaterial setReflectivity(double reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}

	@Override
	public double getRefractionRatio() {
		return this.refractionRatio;
	}

	@Override
	public MeshPhongMaterial setRefractionRatio(double refractionRatio) {
		this.refractionRatio = refractionRatio;
		return this;
	}

	@Override
	public Texture getLightMap() {
		return this.lightMap;
	}

	@Override
	public double getLightMapIntensity() {
		return 0;
	}

	@Override
	public <T extends Material> T setLightMapIntensity(double intensity) {
		return null;
	}

	@Override
	public MeshPhongMaterial setLightMap(Texture lightMap) {
		this.lightMap = lightMap;
		return this;
	}

	@Override
	public boolean isFog() {
		return this.fog;
	}

	@Override
	public MeshPhongMaterial setFog(boolean fog) {
		this.fog = fog;
		return this;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public MeshPhongMaterial setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public MeshPhongMaterial setColor(int color) {
		this.color = new Color(color);
		return this;
	}

	@Override
	public Texture getMap() {
		return this.map;
	}

	@Override
	public MeshPhongMaterial setMap(Texture map) {
		this.map = map;
		return this;
	}

	@Override
	public Texture getAlphaMap() {
		return this.alphaMap;
	}

	@Override
	public MeshPhongMaterial setAlphaMap(Texture alphaMap) {

		this.alphaMap = alphaMap;
		return this;
	}

	@Override
	public Material.COLORS getVertexColors() {
		return this.vertexColors;
	}

	@Override
	public MeshPhongMaterial setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
		return this;
	}

	@Override
	public boolean isSkinning() {
		return this.skinning;
	}

	@Override
	public MeshPhongMaterial setSkinning(boolean skinning) {
		this.skinning = skinning;
		return this;
	}

	@Override
	public boolean isMorphTargets() {
		return this.morphTargets;
	}

	@Override
	public MeshPhongMaterial setMorphTargets(boolean morphTargets) {
		this.morphTargets = morphTargets;
		return this;
	}

	@Override
	public boolean isMorphNormals() {
		return this.morphNormals;
	}

	@Override
	public MeshPhongMaterial setMorphNormals(boolean morphNormals) {
		this.morphNormals = morphNormals;
		return this;
	}

	@Override
	public Color getEmissive() {
		return this.emissive;
	}

	@Override
	public MeshPhongMaterial setEmissive(Color emissive) {
		this.emissive = emissive;
		return this;
	}

	@Override
	public MeshPhongMaterial setEmissive(int emissive) {
		this.emissive = new Color(emissive);
		return this;
	}

	@Override
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	@Override
	public MeshPhongMaterial setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
		return this;
	}

	@Override
	public Texture getBumpMap() {
		return this.bumpMap;
	}

	@Override
	public MeshPhongMaterial setBumpMap(Texture bumpMap) {
		this.bumpMap = bumpMap;
		return this;
	}

	@Override
	public double getBumpScale() {
		return this.bumpScale;
	}

	@Override
	public MeshPhongMaterial setBumpScale(double bumpScale) {
		this.bumpScale = bumpScale;
		return this;
	}

	@Override
	public Texture getNormalMap() {
		return this.normalMap;
	}

	@Override
	public MeshPhongMaterial setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
		return this;
	}

	@Override
	public Vector2 getNormalScale() {
		return this.normalScale;
	}

	@Override
	public MeshPhongMaterial setNormalScale(Vector2 normalScale) {
		this.normalScale = normalScale;
		return this;
	}

	public Material.SHADING getShading() {
		return this.shading;
	}

	public MeshPhongMaterial setShading(Material.SHADING shading) {
		this.shading = shading;
		return this;
	}

	@Override
	public Texture getAoMap() {
		return aoMap;
	}

	@Override
	public MeshPhongMaterial setAoMap(Texture aoMap) {
		this.aoMap = aoMap;
		return this;
	}

	@Override
	public double getAoMapIntensity() {
		return aoMapIntensity;
	}

	@Override
	public MeshPhongMaterial setAoMapIntensity(double aoMapIntensity) {
		this.aoMapIntensity = aoMapIntensity;
		return this;
	}

	@Override
	public Texture getDisplacementMap() {
		return displacementMap;
	}

	@Override
	public MeshPhongMaterial setDisplacementMap(Texture displacementMap) {
		this.displacementMap = displacementMap;
		return this;
	}

	@Override
	public Texture getEmissiveMap() {
		return emissiveMap;
	}

	@Override
	public MeshPhongMaterial setEmissiveMap(Texture emissiveMap) {
		this.emissiveMap = emissiveMap;
		return this;
	}

	@Override
	public MeshPhongMaterial clone() {
		return new MeshPhongMaterial().copy(this);
	}

	public MeshPhongMaterial copy(MeshPhongMaterial source) {

		super.copy( source );

		this.color.copy( source.color );
		this.specular.copy( source.specular );
		this.shininess = source.shininess;

		this.map = source.map;

		this.lightMap = source.lightMap;
		this.lightMapIntensity = source.lightMapIntensity;

		this.aoMap = source.aoMap;
		this.aoMapIntensity = source.aoMapIntensity;

		this.emissive.copy( source.emissive );
		this.emissiveMap = source.emissiveMap;
		this.emissiveIntensity = source.emissiveIntensity;

		this.bumpMap = source.bumpMap;
		this.bumpScale = source.bumpScale;

		this.normalMap = source.normalMap;
		this.normalScale.copy( source.normalScale );

		this.displacementMap = source.displacementMap;
		this.displacementScale = source.displacementScale;
		this.displacementBias = source.displacementBias;

		this.specularMap = source.specularMap;

		this.alphaMap = source.alphaMap;

		this.envMap = source.envMap;
		this.combine = source.combine;
		this.reflectivity = source.reflectivity;
		this.refractionRatio = source.refractionRatio;

		this.fog = source.fog;

		this.shading = source.shading;

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