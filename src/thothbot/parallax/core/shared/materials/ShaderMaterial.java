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

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.shared.core.Color;

public class ShaderMaterial extends Material 
	implements HasWireframe, HasFog, HasColor, HasVertexColors, HasSkinning
{ 
	class ShaderMaterialShader extends Shader
	{

		public ShaderMaterialShader(String vertexShader, String fragmentShader) 
		{
			super(vertexShader, fragmentShader);
		}

		@Override
		protected void initUniforms() {

		}
		
	}
	
	private boolean isWireframe;
	private int wireframeLineWidth;
	
	private boolean isFog;
	
	private Color color;
	
	private Material.COLORS vertexColors;
	
	private boolean isSkinning;
	private boolean isMorphTargets;
	private boolean isMorphNormals;
	
	private int numSupportedMorphTargets;
	private int numSupportedMorphNormals;
	
	private boolean isLights;
	
	private Shader shader;
	
	public ShaderMaterial(Shader.DefaultResources resource) 
	{
		this(resource.getVertexShader().getText(), resource.getFragmentShader().getText());
	}

	public ShaderMaterial(String vertexShader, String fragmentShader) 
	{		
		this();
		this.shader = new ShaderMaterialShader(vertexShader, fragmentShader);
	}
	
	public ShaderMaterial(Shader shader)
	{
		this();
		this.shader = shader;
	}
	
	private ShaderMaterial()
	{
		setWireframe(false);
		setWireframeLineWidth(1);
		
		setFog(false);
		
		setColor(new Color(0xffffff));
		
		setVertexColors(Material.COLORS.NO);
	}
	
	public boolean bufferGuessUVType () 
	{
		return true;
	}
	
	@Override
	public Shader getAssociatedShader() {
		return shader;
	}
		
	public boolean isLights() {
		return this.isLights;
	}
	
	/**
	 * Enable/Disable scene lights
	 */
	public void setLights(boolean isLights) {
		this.isLights = isLights;
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
}
