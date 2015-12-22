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

import java.util.Map;

import org.parallax3d.parallax.graphics.renderers.shaders.LambertShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.ObjectMap;
import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.textures.Texture.OPERATIONS;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.math.Color;

/**
 * A material for non-shiny (Lambertian) surfaces, evaluated per vertex.
 *
 */
@ThreeJsObject("THREE.MeshLambertMaterial")
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
	private OPERATIONS combine;
	private float reflectivity;
	private float refractionRatio;
		
	private boolean isFog;
	
	private SHADING shading;
	
	private boolean isWireframe;
	private int wireframeLineWidth;

	private COLORS vertexColors;
	
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
		setReflectivity(1.0f);
		setRefractionRatio(0.98f);
		
		setFog(true);
		
		setShading(SHADING.SMOOTH);
		
		setColor(new Color(0xffffff)); // diffuse
		setAmbient(new Color(0xffffff));
		setEmissive(new Color( 0x000000 ));
		
		setVertexColors(COLORS.NO);
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
	public void setWrapAround(boolean wrapAround) {
		this.isWrapAround = wrapAround;
	}

	@Override
	public Vector3 getWrapRGB() {
		return this.wrapRGB;
	}
	
	@Override
	public void setWrapRGB(Vector3 wrapRGB) {
		this.wrapRGB = wrapRGB;
	}
	
	@Override
	public boolean isWireframe() {
		return this.isWireframe;
	}

	@Override
	public void setWireframe(boolean wireframe) {
		this.isWireframe = wireframe;
	}

	@Override
	public int getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public void setWireframeLineWidth(int wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
	}
	
	@Override
	public Texture getEnvMap() {
		return this.envMap;
	}

	@Override
	public void setEnvMap(Texture envMap) {
		this.envMap = envMap;
	}
	
	@Override
	public Texture getAlphaMap() {
		return this.alphaMap;
	}

	@Override
	public void setAlphaMap(Texture alphaMap) {
		this.alphaMap = alphaMap;
	}

	@Override
	public OPERATIONS getCombine() {
		return this.combine;
	}

	@Override
	public void setCombine(OPERATIONS combine) {
		this.combine = combine;
	}

	@Override
	public float getReflectivity() {
		return this.reflectivity;
	}

	@Override
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	@Override
	public float getRefractionRatio() {
		return this.refractionRatio;
	}

	@Override
	public void setRefractionRatio(float refractionRatio) {
		this.refractionRatio = refractionRatio;
	}
	
	@Override
	public Texture getLightMap() {
		return this.lightMap;
	}

	@Override
	public void setLightMap(Texture lightMap) {
		this.lightMap = lightMap;
	}
	
	@Override
	public boolean isFog() {
		return this.isFog;
	}

	@Override
	public void setFog(boolean fog) {
		this.isFog = fog;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public Texture getMap() {
		return this.map;
	}

	@Override
	public void setMap(Texture map) {
		this.map = map;
	}
	
	@Override
	public COLORS isVertexColors() {
		return this.vertexColors;
	}

	@Override
	public void setVertexColors(COLORS vertexColors) {
		this.vertexColors = vertexColors;
	}
	
	@Override
	public boolean isSkinning() {
		return this.isSkinning;
	}

	@Override
	public void setSkinning(boolean isSkinning) {
		this.isSkinning = isSkinning;
	}

	@Override
	public boolean isMorphTargets() {
		return this.isMorphTargets;
	}

	@Override
	public void setMorphTargets(boolean isMorphTargets) {
		this.isMorphTargets = isMorphTargets;
	}

	@Override
	public boolean isMorphNormals() {
		return this.isMorphNormals;
	}

	@Override
	public void setMorphNormals(boolean isMorphNormals) {
		this.isMorphNormals = isMorphNormals;
	}
	
	@Override
	public Color getAmbient() {
		return this.ambient;
	}

	@Override
	public void setAmbient(Color ambient) {
		this.ambient = ambient;
	}

	@Override
	public Color getEmissive() {
		return this.emissive;
	}

	@Override
	public void setEmissive(Color emissive) {
		this.emissive = emissive;
	}
	
	@Override
	public int getNumSupportedMorphTargets() {
		return this.numSupportedMorphTargets;
	}
	
	@Override
	public void setNumSupportedMorphTargets(int num) {
		this.numSupportedMorphTargets = num;
	}
	
	@Override
	public int getNumSupportedMorphNormals() {
		return this.numSupportedMorphNormals;
	}
	
	@Override
	public void setNumSupportedMorphNormals(int num) {
		this.numSupportedMorphNormals = num;
	}
	
	@Override
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	@Override
	public void setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
	}
	
	public SHADING getShading() {
		return this.shading;
	}

	public void setShading(SHADING shading) {
		this.shading = shading;
	}
	
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
		ObjectMap<String, Uniform> uniforms = getShader().getUniforms();
		
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