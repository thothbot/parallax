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

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.ShaderBasic;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.client.textures.Texture.OPERATIONS;
import thothbot.parallax.core.shared.core.Color3f;

public class MeshBasicMaterial extends Material
	implements HasMaterialMap, HasWireframe, HasFog, HasVertexColors, HasSkinning
{
	private boolean isWireframe;
	private int wireframeLineWidth;
	
	private Texture envMap;
	private Texture.OPERATIONS combine;
	private float reflectivity;
	private float refractionRatio;
	
	private Texture lightMap;
	
	private boolean isFog;
	
	private Color3f color;
	
	private Texture map;
	
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
		
		setCombine(OPERATIONS.MULTIPLY);
		setReflectivity(1.0f);
		setRefractionRatio(0.98f);
		
		setFog(true);
		
		setColor(new Color3f(0xffffff));
		
		setVertexColors(Material.COLORS.NO);
	}

	@Override
	public Shader getShaderId() 
	{
		return new ShaderBasic();
	}
	
	public Material.SHADING bufferGuessNormalType () 
	{
		// only MeshBasicMaterial and MeshDepthMaterial don't need normals
		if (this.envMap == null)
			return null;

		return super.bufferGuessNormalType();
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
	public Color3f getColor() {
		return color;
	}
	
	@Override
	public void setColor(Color3f color) {
		this.color = color;
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

	// Should be false
	@Override
	public boolean isMorphNormals() {
		return false;
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
	public Texture getMap() {
		return this.map;
	}

	@Override
	public void setMap(Texture map) {
		this.map = map;
	}
}
