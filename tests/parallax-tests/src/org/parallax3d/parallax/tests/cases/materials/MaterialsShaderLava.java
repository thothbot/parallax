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

package org.parallax3d.parallax.tests.cases.materials;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.TorusGeometry;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;
import org.parallax3d.parallax.tests.resources.LavaShader;

@ThreejsExample("webgl_shader_lava")
public final class MaterialsShaderLava extends ParallaxTest
{

	static final String img1 = "textures/lava/cloud.png";
	static final String img2 = "textures/lava/lavatile.jpg";

	Scene scene;
	PerspectiveCamera camera;
	FastMap<Uniform> uniforms;
	Mesh mesh;

	private double oldTime;

	@Override
	public void onResize(RenderingContext context)
	{
		((Vector2)uniforms.get("resolution").getValue()).setX( context.getWidth() );
		((Vector2)uniforms.get("resolution").getValue()).setY( context.getHeight() );
	}

	public void onStart(RenderingContext context)
	{
		scene = new Scene();
//		EVENT_BUS.addHandler(ViewportResizeEvent.TYPE, this);

		camera = new PerspectiveCamera(
				35, // fov
				context.getAspectRation(), // aspect
				1, // near
				3000 // far
		);

		camera.getPosition().setZ(4);

		ShaderMaterial material = new ShaderMaterial(new LavaShader());
		uniforms = material.getShader().getUniforms();

		Texture texture1 = new Texture(img1);
		texture1.setWrapS(TextureWrapMode.REPEAT);
		texture1.setWrapT(TextureWrapMode.REPEAT);
		uniforms.get("texture1").setValue(texture1);

		Texture texture2 = new Texture(img2);
		texture2.setWrapS(TextureWrapMode.REPEAT);
		texture2.setWrapT(TextureWrapMode.REPEAT);

		uniforms.get("texture2").setValue(texture2);

		double size = 0.65;

		mesh = new Mesh( new TorusGeometry( size, 0.3, 30, 30 ), material );
		mesh.getRotation().setX( 0.3 );
		scene.add( mesh );

		//

//		Postprocessing composer = new Postprocessing( context.getRenderer(), scene );
//
//		RenderPass renderModel = new RenderPass( scene, camera );
//		BloomPass effectBloom = new BloomPass( 1.25 );
//
//		FilmPass effectFilm = new FilmPass( 0.35, 0.95, 2048, false );
//		effectFilm.setRenderToScreen(true);
//
//		composer.addPass( renderModel );
//		composer.addPass( effectBloom );
//		composer.addPass( effectFilm );

		context.getRenderer().setAutoClear(false);

		this.oldTime = Duration.currentTimeMillis();
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		double delta = (Duration.currentTimeMillis() - this.oldTime) * 0.001 * 5;

		uniforms.get("time").setValue((Double)uniforms.get("time").getValue() + 0.2 * delta );

		mesh.getRotation().addX( 0.05 * delta );
		mesh.getRotation().addY( 0.0125 * delta );

		context.getRenderer().clear();

		this.oldTime = Duration.currentTimeMillis();
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Lava shader";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
