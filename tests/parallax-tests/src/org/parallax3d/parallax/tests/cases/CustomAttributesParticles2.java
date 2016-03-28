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
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Points;
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
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.NotReady;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@NotReady
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
	Points sphere;
	int vertices1;

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

		int radius = 100, segments = 68, rings = 38;

		SphereGeometry geometry1 = new SphereGeometry( radius, segments, rings );
		BoxGeometry geometry2 = new BoxGeometry( 0.8 * radius, 0.8 * radius, 0.8 * radius, 10, 10, 10 );

		this.vertices1 = geometry1.getVertices().size();
		List<Vector3> vertices = new ArrayList<>( geometry1.getVertices() );
		vertices.addAll(geometry2.getVertices());

		Float32Array positions = Float32Array.create(vertices.size() * 3);
		Float32Array colors = Float32Array.create( vertices.size() * 3 );
		Float32Array sizes = Float32Array.create( vertices.size() );

		Color color = new Color();

		for ( int i = 0, l = vertices.size(); i < l; i ++ ) {

			Vector3 vertex = vertices.get(i);
			vertex.toArray( positions, i * 3 );

			if ( i < vertices1 ) {

				color.setHSL( 0.01 + 0.1 * ( i / vertices1 ), 0.99, ( vertex.getY() + radius ) / ( 4. * radius ) );

			} else {

				color.setHSL( 0.6, 0.75, 0.25 + vertex.getY() / ( 2. * radius ) );

			}

			color.toArray( colors, i * 3 );

			sizes.set( i , i < vertices1 ? 10 : 40 );

		}

		BufferGeometry geometry = new BufferGeometry();
		geometry.addAttribute( "position", new BufferAttribute( positions, 3 ) );
		geometry.addAttribute( "size", new BufferAttribute( sizes, 1 ) );
		geometry.addAttribute( "ca", new BufferAttribute( colors, 3 ) );

		//

		final Texture texture = new Texture(image)
				.setWrapS(TextureWrapMode.REPEAT)
				.setWrapT(TextureWrapMode.REPEAT);

		FastMap<Uniform> uniforms = new FastMap<Uniform>() {{
			put("amplitude", new Uniform(Uniform.TYPE.F1, 1.0));
			put("color", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));
			put("texture", new Uniform(Uniform.TYPE.T, texture));
		}};

		ShaderMaterial shaderMaterial = new ShaderMaterial(	Resources.INSTANCE )
				.setTransparent(true);
		shaderMaterial.getShader().setUniforms(uniforms);


		this.sphere = new Points( geometry, shaderMaterial );
		scene.add( sphere );

	}
			
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getFrameId() * 0.05;

		sphere.getRotation().setY(0.02 * time);
		sphere.getRotation().setZ(0.02 * time);

		Geometry geometry = (Geometry) sphere.getGeometry();
//		var attributes = geometry.attributes;
//
//		for( int i = 0; i < attributes.get("size").getValue().size(); i++ )
//		{
//			List<Double> value = (List<Double>) attributes.get("size").getValue();
//			if(i < vertices1)
//				value.set( i, 16.0 + 12.0 * Math.sin( 0.1 * i + time ));
//		}
//
//		attributes.get("size").needsUpdate = true;
		
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
