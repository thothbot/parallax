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
import org.parallax3d.parallax.graphics.extras.geometries.PlaneGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.NeedImprovement;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@NeedImprovement("Custom GWT styling")
@ThreejsExample("webgl_materials_texture_anisotropy")
public final class MaterialsTextureAnisotropy extends ParallaxTest implements TouchMoveHandler
{

	private static final String texture = "textures/crate.gif";

	Scene scene1, scene2;
	PerspectiveCamera camera;

	int width = 0, height = 0;
	int mouseX = 0;
	int mouseY = 0;

	@Override
	public void onResize(RenderingContext context) {
		width = context.getWidth();
		height = context.getHeight();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		camera = new PerspectiveCamera(
				35, // fov
				context.getAspectRation(), // aspect
				1, // near
				25000 // far 
		);

		camera.getPosition().setZ(1500);

		scene1 = new Scene();
		scene1 = new Scene();

		scene1.setFog(new Fog(0xf2f7ff, 1, 25000));
		scene2.setFog(scene1.getFog());

		scene1.add( new AmbientLight( 0xeef0ff ) );
		scene2.add( new AmbientLight( 0xeef0ff ) );

		DirectionalLight light1 = new DirectionalLight( 0xffffff, 2 );
		light1.getPosition().set( 1, 1, 1 );
		scene1.add( light1 );

		DirectionalLight light2 = new DirectionalLight( 0xffffff, 2 );
		light2.getPosition().set( 1, 1, 1 );
		scene2.add( light2 );


		// GROUND

		Texture texture1 = new Texture(texture);
		MeshPhongMaterial material1 = new MeshPhongMaterial()
				.setColor(0xffffff)
				.setMap(texture1);

		texture1.setAnisotropy( context.getRenderer().getMaxAnisotropy() );
		texture1.setWrapS(TextureWrapMode.REPEAT);
		texture1.setWrapT(TextureWrapMode.REPEAT);
		texture1.getRepeat().set( 512, 512 );

		Texture texture2 = new Texture(texture);
		MeshPhongMaterial material2 = new MeshPhongMaterial()
				.setColor(0xffffff)
				.setMap(texture2);

		texture2.setAnisotropy( 1 );
		texture2.setWrapS(TextureWrapMode.REPEAT);
		texture2.setWrapT(TextureWrapMode.REPEAT);
		texture2.getRepeat().set( 512, 512 );

		//

		PlaneGeometry geometry = new PlaneGeometry( 100, 100 );

		Mesh mesh1 = new Mesh( geometry, material1 );
		mesh1.getRotation().setX( - Math.PI / 2 );
		mesh1.getScale().set( 1000 );

		Mesh mesh2 = new Mesh( geometry, material2 );
		mesh2.getRotation().setX( - Math.PI / 2 );
		mesh2.getScale().set( 1000 );

		scene1.add( mesh1 );
		scene2.add( mesh2 );

		// RENDERER

		context.getRenderer().setClearColor( scene1.getFog().getColor() );
		context.getRenderer().setAutoClear(false);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().addX( ( mouseX - camera.getPosition().getX()) * .05 );
		camera.getPosition().addY( Mathematics.clamp( camera.getPosition().getY() + ( - ( mouseY - 200 ) - camera.getPosition().getY()) * .05, 50, 1000 ) );

		camera.lookAt(scene1.getPosition());

		context.getRenderer().clear();
		context.getRenderer().setScissorTest( true );

		context.getRenderer().setScissor( 0, 0, context.getWidth()/2 - 2, context.getHeight() );
		context.getRenderer().render( scene1, camera );

		context.getRenderer().setScissor( context.getWidth()/2, 0, context.getWidth()/2 - 2, context.getHeight()  );
		context.getRenderer().render( scene2, camera );

		context.getRenderer().setScissorTest( false );
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = screenX - width / 2;
		mouseY = screenY - height / 2;
	}

	@Override
	public String getName() {
		return "Anisotropic filtering";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

//	@Override
//	public void onFilmReady(AnimationReadyEvent event)
//	{
//		super.onFilmReady(event);
//				
//		FlowPanel panelLeft = new FlowPanel();
//		panelLeft.setStyleName("common-panel", true);
//		panelLeft.setStyleName("corner-panel", true);
//		this.renderingPanel.add(panelLeft);
//		this.renderingPanel.setWidgetLeftWidth(panelLeft, 1, Unit.PX, 80, Unit.PX);
//		this.renderingPanel.setWidgetBottomHeight(panelLeft, 1, Unit.PX, 25, Unit.PX);
//		
//		FlowPanel panelRight = new FlowPanel();
//		panelRight.setStyleName("common-panel", true);
//		panelRight.setStyleName("corner-panel", true);
//		this.renderingPanel.add(panelRight);
//		this.renderingPanel.setWidgetRightWidth(panelRight, 1, Unit.PX, 80, Unit.PX);
//		this.renderingPanel.setWidgetBottomHeight(panelRight, 1, Unit.PX, 25, Unit.PX);
//
//		final DemoScene rs = (DemoScene) this.renderingPanel.getAnimatedScene();
//
//		if ( this.renderingPanel.context.getRenderer().getMaxAnisotropy() > 0 ) 
//		{
//			panelLeft.add(new Label("Anisotropy: " + this.renderingPanel.context.getRenderer().getMaxAnisotropy()));
//			panelRight.add(new Label("Anisotropy: " + 1));
//		} 
//		else
//		{
//			panelLeft.add(new Label("not supported"));
//			panelRight.add(new Label("not supported"));
//		}
//	}
	
}
