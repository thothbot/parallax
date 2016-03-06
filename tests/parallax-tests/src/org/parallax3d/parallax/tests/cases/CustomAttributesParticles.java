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

package org.parallax3d.parallax.tests.cases;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_custom_attributes_points")
public class CustomAttributesParticles extends ParallaxTest
{
	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../resources/shaders/custom_attributes_particles.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../resources/shaders/custom_attributes_particles.fs.glsl")
		SourceTextResource getFragmentShader();
	}
	
	static final String texture = "textures/sprites/spark1.png";

	Scene scene;
	PerspectiveCamera camera;
	FastMap<Attribute> attributes;
	PointCloud sphere;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 40,
				context.getAspectRation(),
				1, 
				10000 
		);

		camera.getPosition().setZ(300);

		this.attributes = new FastMap<>();
		this.attributes.put("size", new Attribute(Attribute.TYPE.F, new ArrayList<Integer>()));
		this.attributes.put("ca", new Attribute(Attribute.TYPE.C, new ArrayList<Color>()));

		FastMap<Uniform> uniforms = new FastMap<>();
		uniforms.put("amplitude", new Uniform(Uniform.TYPE.F, 1.0));
		uniforms.put("color", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));
		uniforms.put("texture", new Uniform(Uniform.TYPE.T, new Texture(texture)));

		ShaderMaterial shaderMaterial = new ShaderMaterial( Resources.INSTANCE )
				.setBlending( Material.BLENDING.ADDITIVE )
				.setDepthTest(false)
				.setTransparent( true );

		shaderMaterial.getShader().setAttributes(attributes);
		shaderMaterial.getShader().setUniforms(uniforms);

		double radius = 200;
		Geometry geometry = new Geometry();

		for ( int i = 0; i < 10000; i++ )
		{
			Vector3 vertex = new Vector3();
			vertex.setX(Math.random() * 2.0 - 1.0);
			vertex.setY(Math.random() * 2.0 - 1.0);
			vertex.setZ(Math.random() * 2.0 - 1.0);
			vertex.multiply( radius );

			geometry.getVertices().add( vertex );
		}

		this.sphere = new PointCloud( geometry, shaderMaterial );

		List<Vector3> vertices = ((Geometry)sphere.getGeometry()).getVertices();
		List<Double> values_size = (List<Double>) attributes.get("size").getValue();
		List<Color> values_color = (List<Color>) attributes.get("ca").getValue();

		for( int v = 0; v < vertices.size(); v++ ) 
		{
			values_size.add( v, 10.0);
			values_color.add( v, new Color( 0xffaa00 ));

			if ( vertices.get( v ).getX() < 0 )
				values_color.get( v ).setHSL( 0.5 + 0.1 * ( v / (double)vertices.size() ), 0.7, 0.5 );
			else
				values_color.get( v ).setHSL( 0.0 + 0.1 * ( v / (double)vertices.size() ), 0.9, 0.5 );
		}

		scene.add( sphere );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getFrameId() * 0.05;

		this.sphere.getRotation().setZ(0.01 * time);

		for( int i = 0; i < this.attributes.get("size").getValue().size(); i++ ) 
		{
			List<Double> value = (List<Double>) this.attributes.get("size").getValue(); 
			value.set( i, 14.0 + 13.0 * Math.sin( 0.1 * i + time ));
		}

		this.attributes.get("size").needsUpdate = true;
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Particles";
	}

	@Override
	public String getDescription() {
		return "Here are used custom shaders and sprites.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
