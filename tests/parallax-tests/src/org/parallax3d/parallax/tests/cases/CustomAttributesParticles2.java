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
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
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
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_custom_attributes_points2")
public class CustomAttributesParticles2 extends ParallaxTest
{
	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../resources/shaders/custom_attributes_particles.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../resources/shaders/custom_attributes_particles.fs.glsl")
		SourceTextResource getFragmentShader();
	}
	
	private static final String image = "textures/sprites/disc.png";

	Scene scene;
	PerspectiveCamera camera;
	FastMap<Attribute> attributes;
	PointCloud sphere;
	int vc1;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 45,
				context.getAspectRation(),
				1, 
				10000 
			);
		camera.getPosition().setZ(300);

		this.attributes = new FastMap<>();
		attributes.put("size", new Attribute(Attribute.TYPE.F, new ArrayList<Integer>()));
		attributes.put("ca", new Attribute(Attribute.TYPE.C, new ArrayList<Color>()));

		FastMap<Uniform> uniforms = new FastMap<>();
		uniforms.put("amplitude", new Uniform(Uniform.TYPE.F, 1.0));
		uniforms.put("color", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));
		
		Texture texture = new Texture(image);
		texture.setWrapS(TextureWrapMode.REPEAT);
		texture.setWrapT(TextureWrapMode.REPEAT);

		uniforms.put("texture", new Uniform(Uniform.TYPE.T, texture));
	
		ShaderMaterial shaderMaterial = new ShaderMaterial(	Resources.INSTANCE )
				.setTransparent(true);
		shaderMaterial.getShader().setAttributes(attributes);
		shaderMaterial.getShader().setUniforms(uniforms);
		
		int radius = 100, segments = 68, rings = 38;
		
		SphereGeometry geometry = new SphereGeometry( radius, segments, rings );

		this.vc1 = geometry.getVertices().size();

		BoxGeometry geometry2 = new BoxGeometry( 0.8 * radius, 0.8 * radius, 0.8 * radius, 10, 10, 10 );
		geometry.merge( geometry2, null );
	
		this.sphere = new PointCloud( geometry, shaderMaterial );
		this.sphere.setSortParticles(true);
	
		List<Vector3> vertices = ((Geometry)sphere.getGeometry()).getVertices();
		List<Double> values_size = (List<Double>) attributes.get("size").getValue();
		List<Color> values_color = (List<Color>) attributes.get("ca").getValue();
	
		for( int v = 0; v < vertices.size(); v++ ) 
		{
			values_color.add( v, new Color( 0xffffff ));

			if ( v < vc1 ) 
			{	
				values_size.add( v, 10.0);
				values_color.get( v ).setHSL( 0.01 + 0.1 * ( v / (double)vc1 ), 0.99, ( vertices.get( v ).getY() + radius ) / ( 4.0 *radius ) );
			} 
			else 
			{
				values_size.add( v, 40.0);
				values_color.get( v ).setHSL( 0.6, 0.75, 0.25 + vertices.get( v ).getY() / ( 2.0 * radius ) );
			}

		}

		scene.add( sphere );

	}
			
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getFrameId() * 0.05;

		sphere.getRotation().setY(0.02 * time);
		sphere.getRotation().setZ(0.02 * time);
		
		for( int i = 0; i < attributes.get("size").getValue().size(); i++ ) 
		{
			List<Double> value = (List<Double>) attributes.get("size").getValue(); 
			if(i < vc1 )
				value.set( i, 16.0 + 12.0 * Math.sin( 0.1 * i + time ));
		}
		
		attributes.get("size").needsUpdate = true;
		
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Sphere and cube";
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
