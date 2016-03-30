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
	implements HasColor, HasWireframe, HasFog, HasMap, HasAoMap, HasSpecularMap, HasAlphaMap, HasEnvMap, HasVertexColors
{

	Color color = new Color(0xffffff);

	Texture map;

	Texture aoMap;
	double aoMapIntensity = 1.0;

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

	@Override
	public Shader getAssociatedShader()
	{
		return new BasicShader();
	}

	@Override
	public boolean isWireframe() {
		return this.wireframe;
	}

	@Override
	public MeshBasicMaterial setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
		return this;
	}

	@Override
	public double getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshBasicMaterial setWireframeLineWidth(double wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	public String getWireframeLineCap() {
		return wireframeLineCap;
	}

	public MeshBasicMaterial setWireframeLineCap(String wireframeLineCap) {
		this.wireframeLineCap = wireframeLineCap;
		return this;
	}

	public String getWireframeLineJoin() {
		return wireframeLineJoin;
	}

	public MeshBasicMaterial setWireframeLineJoin(String wireframeLineJoin) {
		this.wireframeLineJoin = wireframeLineJoin;
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
	public boolean isFog() {
		return this.fog;
	}

	@Override
	public MeshBasicMaterial setFog(boolean fog) {
		this.fog = fog;
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
	public Texture getAoMap() {
		return aoMap;
	}

	@Override
	public MeshBasicMaterial setAoMap(Texture aoMap) {
		this.aoMap = aoMap;
		return this;
	}

	@Override
	public double getAoMapIntensity() {
		return aoMapIntensity;
	}

	@Override
	public MeshBasicMaterial setAoMapIntensity(double aoMapIntensity) {
		this.aoMapIntensity = aoMapIntensity;
		return this;
	}

	@Override
	public COLORS getVertexColors() {
		return vertexColors;
	}

	@Override
	public MeshBasicMaterial setVertexColors(COLORS vertexColors) {
		this.vertexColors = vertexColors;
		return this;
	}

	@Override
	public MeshBasicMaterial clone() {
		return new MeshBasicMaterial().copy(this);
	}

	public MeshBasicMaterial copy(MeshBasicMaterial source)
	{
		super.copy( source );

		this.color.copy( source.color );

		this.map = source.map;

		this.aoMap = source.aoMap;
		this.aoMapIntensity = source.aoMapIntensity;

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

		return this;
	}
}
