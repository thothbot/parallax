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

package org.parallax3d.parallax.tests.cases.text;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.controllers.TrackballControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.extras.core.ExtrudeGeometry;
import org.parallax3d.parallax.graphics.extras.core.FontData;
import org.parallax3d.parallax.graphics.extras.core.TextGeometry;
import org.parallax3d.parallax.graphics.extras.modifiers.ExplodeModifier;
import org.parallax3d.parallax.graphics.extras.modifiers.TessellateModifier;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.FontLoadHandler;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.TypefacejsLoader;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_modifier_tessellation")
public class TextTessellation extends ParallaxTest
{
	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../../resources/shaders/tessellation.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/tessellation.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	static final String font = "fonts/helvetiker_bold.typeface.js";
	PerspectiveCamera camera;

	Scene scene;

	FastMap<Uniform> uniforms;

	TrackballControls controls;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				50, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far
		);

		camera.getPosition().set( -100, 100, 200 );

		controls = new TrackballControls(camera, context);

		new TypefacejsLoader(font, new FontLoadHandler() {
			@Override
			public void onFontLoaded(Loader loader, FontData fontData) {

				ExtrudeGeometry.ExtrudeGeometryParameters params = new ExtrudeGeometry.ExtrudeGeometryParameters();
				params.curveSegments = 3;
				params.height = 5;
				params.bevelThickness = 2;
				params.bevelSize = 1;
				params.bevelEnabled = true;

				TextGeometry geometry = new TextGeometry("Parallax3D", fontData, 200, params);
				geometry.center();

				TessellateModifier tessellateModifier = new TessellateModifier(8);
//				for ( int i = 0; i < 6; i ++ )
//					tessellateModifier.modify( geometry );
//
//				ExplodeModifier explodeModifier = new ExplodeModifier();
//				explodeModifier.modify( geometry );

				int numFaces = geometry.getFaces().size();
				BufferGeometry bufferGeometry = new BufferGeometry().fromGeometry( geometry );

				Float32Array colors = Float32Array.create( numFaces * 3 * 3 );
				Float32Array displacement = Float32Array.create( numFaces * 3 * 3 );

				Color color = new Color();

				for ( int f = 0; f < numFaces; f ++ ) {

					int index = 9 * f;

					double h = 0.2 * Math.random();
					double s = 0.5 + 0.5 * Math.random();
					double l = 0.5 + 0.5 * Math.random();

					color.setHSL( h, s, l );

					double d = 10. * ( 0.5 - Math.random() );

					for ( int i = 0; i < 3; i ++ ) {

						colors.set( index + ( 3 * i )     , color.getR());
						colors.set( index + ( 3 * i ) + 1 , color.getG());
						colors.set( index + ( 3 * i ) + 2 , color.getB());

						displacement.set( index + ( 3 * i )     , d);
						displacement.set( index + ( 3 * i ) + 1 , d);
						displacement.set( index + ( 3 * i ) + 2 , d);

					}
				}

				bufferGeometry.addAttribute( "customColor", new BufferAttribute( colors, 3 ) );
				bufferGeometry.addAttribute( "displacement", new BufferAttribute( displacement, 3 ) );

				MeshBasicMaterial m = new MeshBasicMaterial();
				m.setWireframe(true);
				m.setTransparent(true);
				ShaderMaterial shaderMaterial = new ShaderMaterial( Resources.INSTANCE );
				uniforms = new FastMap<Uniform>() {{
					put("amplitude", new Uniform(Uniform.TYPE.F, 0.0) );
				}};
				shaderMaterial.setWireframe(true);
				shaderMaterial.setColor(0xffffff);

				shaderMaterial.getShader().setUniforms(uniforms);

				scene.add(new Mesh(geometry, m));
			}

		});

		context.getRenderer().setClearColor( 0x050505 );
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getFrameId() * 0.001;

		if(uniforms != null)
			uniforms.get("amplitude").setValue( 1.0 + Math.sin( time * 0.5 ) );

		controls.update();
		context.getRenderer().render(scene, camera);

	}

	@Override
	public String getName() {
		return "Tessellation modifier";
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
