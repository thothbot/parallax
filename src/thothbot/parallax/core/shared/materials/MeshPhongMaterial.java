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

package thothbot.parallax.core.shared.materials;

import java.util.Map;

import thothbot.parallax.core.client.shaders.PhongShader;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.client.textures.Texture.OPERATIONS;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * A material for shiny surfaces, evaluated per pixel.
 *
 */
public final class MeshPhongMaterial extends Material 
	implements HasMaterialMap, HasBumpMap, HasNormalMap, HasWrap, HasWireframe, HasFog, HasVertexColors,
	HasSkinning, HasAmbientEmissiveColor, HasShading,
	
	//Raytracing
	HasRaytracingMirror, HasRaytracingGlass
	
{
	
	private Color color;
	private Color ambient;
	private Color emissive;
	private Color specular;	
	private double shininess;
	
	private boolean isMetal;
	
	private boolean isWrapAround;
	private Vector3 wrapRGB;
		
	private Texture map;
	private Texture lightMap;
	
	private Texture bumpMap;
	private double bumpScale;
	
	private Texture normalMap;
	private Vector2 normalScale;
	
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
	
	private boolean isMirror;
	private boolean isGlass;
	
	public MeshPhongMaterial()
	{	
		setWrapRGB(new Vector3( 1, 1, 1 ));
		setWrapAround(false);
		
		setWireframe(false);
		setWireframeLineWidth(1);
		
		setCombine(OPERATIONS.MULTIPLY);
		setReflectivity(1.0);
		setRefractionRatio(0.98);
		
		setNormalScale(new Vector2(1, 1));
		
		setFog(true);
		
		setShading(Material.SHADING.SMOOTH);
		
		setColor(new Color(0xffffff));
		setAmbient(new Color(0xffffff));
		setEmissive(new Color(0x000000));
		setSpecular(new Color(0x111111));
		
		setVertexColors(Material.COLORS.NO);
		
		setShininess(30);
		
		setBumpScale(1.0);
	}

	@Override
	public Shader getAssociatedShader()
	{
		return new PhongShader();
	}
	
	
	public Color getSpecular() {
		return specular;
	}

	public void setSpecular(Color specular) {
		this.specular = specular;
	}
	
	public double getShininess() {
		return shininess;
	}
	
	public void setShininess(double shininess) {
		this.shininess = shininess;
	}
	
	public boolean isMetal() {
		return this.isMetal;
	}
	
	public void setMetal(boolean isMetal) {
		this.isMetal = isMetal;
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
	public OPERATIONS getCombine() {
		return this.combine;
	}

	@Override
	public void setCombine(OPERATIONS combine) {
		this.combine = combine;
	}

	@Override
	public double getReflectivity() {
		return this.reflectivity;
	}

	@Override
	public void setReflectivity(double reflectivity) {
		this.reflectivity = reflectivity;
	}

	@Override
	public double getRefractionRatio() {
		return this.refractionRatio;
	}

	@Override
	public void setRefractionRatio(double refractionRatio) {
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
	public Texture getAlphaMap() {
		return this.alphaMap;
	}

	@Override
	public void setAlphaMap(Texture alphaMap) {
		this.alphaMap = alphaMap;
	}
	
	@Override
	public Material.COLORS isVertexColors() {
		return this.vertexColors;
	}

	@Override
	public void setVertexColors(Material.COLORS vertexColors) {
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
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	@Override
	public void setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
	}
	
	@Override
	public Texture getBumpMap() {
		return this.bumpMap;
	}

	@Override
	public void setBumpMap(Texture bumpMap) {
		this.bumpMap = bumpMap;
	}

	@Override
	public double getBumpScale() {
		return this.bumpScale;
	}

	@Override
	public void setBumpScale(double bumpScale) {
		this.bumpScale = bumpScale;
	}
	
	@Override
	public Texture getNormalMap() {
		return this.normalMap;
	}

	@Override
	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
	}

	@Override
	public Vector2 getNormalScale() {
		return this.normalScale;
	}

	@Override
	public void setNormalScale(Vector2 normalScale) {
		this.normalScale = normalScale;
	}
	
	public Material.SHADING getShading() {
		return this.shading;
	}

	public void setShading(Material.SHADING shading) {
		this.shading = shading;
	}
	
	@Override
	public boolean isGlass() {
		return isGlass;
	}

	@Override
	public void setGlass(boolean isGlass) {
		this.isGlass = isGlass;
	}

	@Override
	public boolean isMirror() {
		return isMirror;
	}

	@Override
	public void setMirror(boolean isMirror) {
		this.isMirror = isMirror;
	}
	
	public MeshPhongMaterial clone() {

		MeshPhongMaterial material = new MeshPhongMaterial();
		
		super.clone(material);

		material.color.copy( this.color );
		material.ambient.copy( this.ambient );
		material.emissive.copy( this.emissive );
		material.specular.copy( this.specular );
		material.shininess = this.shininess;

		material.isMetal = this.isMetal;

		material.isWrapAround = this.isWrapAround;
		material.wrapRGB.copy( this.wrapRGB );

		material.map = this.map;

		material.lightMap = this.lightMap;

		material.bumpMap = this.bumpMap;
		material.bumpScale = this.bumpScale;

		material.normalMap = this.normalMap;
		material.normalScale.copy( this.normalScale );

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
		
		Map<String, Uniform> uniforms = getShader().getUniforms(); 
		uniforms.get("shininess").setValue( getShininess() );

		if ( isGammaInput ) 
		{
			((Color) uniforms.get("ambient").getValue()).copyGammaToLinear( getAmbient() );
			((Color) uniforms.get("emissive").getValue()).copyGammaToLinear( getEmissive() );
			((Color) uniforms.get("specular").getValue()).copyGammaToLinear( getSpecular() );
		} 
		else
		{
			uniforms.get("ambient").setValue( getAmbient() );
			uniforms.get("emissive").setValue( getEmissive() );
			uniforms.get("specular").setValue( getSpecular() );
		}

		if ( isWrapAround() ) 
			((Vector3) uniforms.get("wrapRGB").getValue()).copy( getWrapRGB() );
	}
	
}
