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

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.ShaderParticleBasic;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color3f;

public final class ParticleBasicMaterial extends Material 
	implements HasFog, HasColor, HasMap, HasVertexColors
{
	private boolean isFog;
	
	private Color3f color;
	
	private Texture map;
	
	private Material.COLORS vertexColors;
	
	private double size;
	
	private boolean sizeAttenuation;

	public ParticleBasicMaterial()
	{
		
		setFog(true);
		
		setColor(new Color3f(0xffffff));
		
		setSize(1.0f);
		setSizeAttenuation(true);
		
		setVertexColors(Material.COLORS.NO);
		
	}

	public double getSize() {
		return this.size;
	}
	
	public void setSize(double size) {
		this.size = size;;
	}

	public boolean isSizeAttenuation() {
		return sizeAttenuation;
	}
	
	public void setSizeAttenuation(boolean sizeAttenuation) {
		this.sizeAttenuation = sizeAttenuation;
	}
	
	public Shader getShaderId() 
	{
		return new ShaderParticleBasic();
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
	public void refreshUniforms(Canvas3d canvas, Camera camera, boolean isGammaInput) 
	{
		super.refreshUniforms(canvas, camera, isGammaInput);
		
		getUniforms().get("psColor").setValue( getColor() );
		getUniforms().get("opacity").setValue( getOpacity() );
		getUniforms().get("size").setValue( getSize() );
		getUniforms().get("scale").setValue( canvas.getHeight() / 2.0f );

		getUniforms().get("map").setTexture( getMap() );
	}
}
