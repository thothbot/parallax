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

import org.parallax3d.parallax.graphics.renderers.shaders.UniformsUtils;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.ShaderMaterial")
public class ShaderMaterial extends Material 
	implements HasWireframe, HasFog, HasVertexColors, HasSkinning, HasShading
{
	public static class Extensions {
		/**
		 * set to use derivatives
		 */
		boolean derivatives = false;
		/**
		 * set to use fragment depth values
		 */
		boolean fragDepth = false;
		/**
		 * set to use draw buffers
		 */
		boolean drawBuffers = false;
		/**
		 * set to use shader texture LOD
		 */
		boolean shaderTextureLOD = false;
	}

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

	Object defines;
	Shader shader;

	Material.SHADING shading;

	double linewidth = 1.0;

	boolean wireframe = false;
	double wireframeLineWidth = 1.0;

	// set to use scene fog
	boolean fog = false;

	// set to use scene lights
	boolean lights = false;

	Material.COLORS vertexColors = COLORS.NO;

	// set to use skinning attribute streams
	boolean skinning;
	// set to use morph targets
	boolean morphTargets;
	// set to use morph normals
	boolean morphNormals;

	Extensions extensions = new Extensions();

	public ShaderMaterial()
	{
		this.shader = new ShaderMaterialShader(
			"void main() {\\n\\tgl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\\n}",
			"void main() {\\n\\tgl_FragColor = vec4( 1.0, 0.0, 0.0, 1.0 );\\n}"
		);
	}

	/**
	 * If shader already has extensions at this point they will override the
	 * ones created dynamically from ProgramParameters.
	 *
	 * @param shader
	 */
	public ShaderMaterial(Shader shader)
	{
		this.shader = shader;
	}

	public ShaderMaterial(Shader.DefaultResources resource)
	{
		this(resource.getVertexShader().getText(), resource.getFragmentShader().getText());
	}

	public ShaderMaterial(String vertexShader, String fragmentShader)
	{
		this.shader = new ShaderMaterialShader(vertexShader, fragmentShader);
	}

	@Override
	public Shader getAssociatedShader() {
		return shader;
	}

	public boolean isLights() {
		return this.lights;
	}

	/**
	 * Enable/Disable scene lights
	 */
	public ShaderMaterial setLights(boolean isLights) {
		this.lights = isLights;
		return this;
	}

	@Override
	public boolean isWireframe() {
		return this.wireframe;
	}

	@Override
	public ShaderMaterial setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
		return this;
	}

	@Override
	public double getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public ShaderMaterial setWireframeLineWidth(double wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	@Override
	public boolean isFog() {
		return this.fog;
	}

	@Override
	public ShaderMaterial setFog(boolean fog) {
		this.fog = fog;
		return this;
	}

	@Override
	public Material.COLORS getVertexColors() {
		return this.vertexColors;
	}

	@Override
	public ShaderMaterial setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
		return this;
	}

	@Override
	public boolean isSkinning() {
		return this.skinning;
	}

	@Override
	public ShaderMaterial setSkinning(boolean isSkinning) {
		this.skinning = isSkinning;
		return this;
	}

	@Override
	public boolean isMorphTargets() {
		return this.morphTargets;
	}

	@Override
	public ShaderMaterial setMorphTargets(boolean morphTargets) {
		this.morphTargets = morphTargets;
		return this;
	}

	@Override
	public boolean isMorphNormals() {
		return this.morphNormals;
	}

	@Override
	public ShaderMaterial setMorphNormals(boolean morphNormals) {
		this.morphNormals = morphNormals;
		return this;
	}

	public Material.SHADING getShading() {
		return this.shading;
	}

	public ShaderMaterial setShading(Material.SHADING shading) {
		this.shading = shading;
		return this;
	}

	@Override
	public ShaderMaterial clone() {
		return new ShaderMaterial().copy(this);
	}

	public ShaderMaterial copy(ShaderMaterial source) {

		super.copy( source );

		this.shader.setUniforms( UniformsUtils.clone( source.shader.getUniforms() ) );

		this.defines = source.defines;

		this.shading = source.shading;

		this.wireframe = source.wireframe;
		this.wireframeLineWidth = source.wireframeLineWidth;

		this.fog = source.fog;

		this.lights = source.lights;

		this.vertexColors = source.vertexColors;

		this.skinning = source.skinning;

		this.morphTargets = source.morphTargets;
		this.morphNormals = source.morphNormals;

		this.extensions = source.extensions;

		return this;

	}
}
