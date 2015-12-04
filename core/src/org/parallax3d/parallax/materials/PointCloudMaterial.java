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

package org.parallax3d.parallax.materials;

import java.util.Map;

import org.parallax3d.parallax.backends.gwt.client.events.ViewportResizeHandler;
import org.parallax3d.parallax.backends.gwt.client.events.HasEventBus;
import org.parallax3d.parallax.backends.gwt.client.events.ViewportResizeEvent;
import org.parallax3d.parallax.shaders.ParticleBasicShader;
import org.parallax3d.parallax.shaders.Shader;
import org.parallax3d.parallax.shaders.Uniform;
import org.parallax3d.parallax.textures.Texture;
import org.parallax3d.parallax.cameras.Camera;
import org.parallax3d.parallax.math.Color;

public final class PointCloudMaterial extends Material implements HasFog,
		HasColor, HasMap, HasVertexColors, HasEventBus {
	private boolean isFog;

	private Color color;

	private Texture map;

	private COLORS vertexColors;

	private double size;

	private boolean sizeAttenuation;

	public PointCloudMaterial() {

		setFog(true);

		setColor(new Color(0xffffff));

		setSize(1.0);
		setSizeAttenuation(true);

		setVertexColors(COLORS.NO);

	}

	public double getSize() {
		return this.size;
	}

	public void setSize(double size) {
		this.size = size;
		;
	}

	public boolean isSizeAttenuation() {
		return sizeAttenuation;
	}

	public void setSizeAttenuation(boolean sizeAttenuation) {
		this.sizeAttenuation = sizeAttenuation;
	}

	public Shader getAssociatedShader() {
		return new ParticleBasicShader();
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
	
	public PointCloudMaterial clone () {

		PointCloudMaterial material = new PointCloudMaterial();
		
		super.clone(material);

		material.color.copy( this.color );

		material.map = this.map;

		material.size = this.size;
		material.sizeAttenuation = this.sizeAttenuation;

		material.vertexColors = this.vertexColors;

		material.isFog = this.isFog;

		return material;

	}

	@Override
	public void refreshUniforms(Camera camera, boolean isGammaInput) {
		super.refreshUniforms(camera, isGammaInput);
		final Map<String, Uniform> uniforms = getShader().getUniforms();

		uniforms.get("psColor").setValue(getColor());
		uniforms.get("opacity").setValue(getOpacity());
		uniforms.get("size").setValue(getSize());

		EVENT_BUS.addHandler(ViewportResizeEvent.TYPE,
				new ViewportResizeHandler() {

					@Override
					public void onResize(ViewportResizeEvent event) {
						uniforms.get("scale").setValue(
								event.getRenderer().getAbsoluteHeight() / 2.0);
					}
				});

		// Default
		uniforms.get("scale").setValue(500 / 2.0);

		uniforms.get("map").setValue(getMap());
	}
}
