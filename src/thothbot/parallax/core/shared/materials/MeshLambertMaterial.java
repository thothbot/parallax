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

package thothbot.parallax.core.shared.materials;

import java.util.Map;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.LambertShader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.client.textures.Texture.OPERATIONS;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Vector3;

public final class MeshLambertMaterial extends Material 
	implements HasMaterialMap, HasWrap, HasWireframe, HasFog, HasVertexColors,
	HasSkinning, HasAmbientEmissiveColor
{
	private boolean isWrapAround;
	private Vector3 wrapRGB;
	
	private boolean isWireframe;
	private int wireframeLineWidth;
	
	private Texture envMap;
	private Texture.OPERATIONS combine;
	private double reflectivity;
	private double refractionRatio;
	
	private Texture lightMap;
	
	private Texture specularMap;
	
	private boolean isFog;
	
	private Color color;
	private Color ambient;
	private Color emissive;
	
	private Texture map;
	
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
		
		setColor(new Color(0xffffff)); // diffuse
		setAmbient(new Color(0xffffff));
		setEmissive(new Color( 0x000000 ));
		
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
	
	@Override
	public void refreshUniforms(Canvas3d canvas, Camera camera, boolean isGammaInput) 
	{
		super.refreshUniforms(canvas, camera, isGammaInput);
		Map<String, Uniform> uniforms = getShader().getUniforms(); 
		
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
