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
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;
import org.parallax3d.parallax.tests.resources.MonjoriShader;

@ThreejsExample("webgl_shader")
public final class MaterialsShaderMonjori extends ParallaxTest 
{
	Scene scene;
	Camera camera;
	
	FastMap<Uniform> uniforms;
	
	@Override
	public void onResize(RenderingContext context) 
	{		
		((Vector2)uniforms.get("resolution").getValue()).setX( context.getWidth() );
		((Vector2)uniforms.get("resolution").getValue()).setY( context.getHeight() );
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
//		EVENT_BUS.addHandler(ViewportResizeEvent.TYPE, this);
		camera = new Camera(); 
		
		camera.getPosition().setZ(1);

		ShaderMaterial material = new ShaderMaterial(new MonjoriShader());
		uniforms = material.getShader().getUniforms();
		
		Mesh mesh = new Mesh( new PlaneBufferGeometry( 2, 2 ), material );
		scene.add( mesh );
		
		((Vector2)uniforms.get("resolution").getValue()).setX( context.getRenderer().getAbsoluteWidth() );
		((Vector2)uniforms.get("resolution").getValue()).setY( context.getRenderer().getAbsoluteHeight() );
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		uniforms.get("time").setValue((Double)uniforms.get("time").getValue() + 0.05);
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Monjori shader";
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
