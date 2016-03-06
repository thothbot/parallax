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
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.textures.TextureData;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.graphics.textures.EmptyTextureData;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_materials_texture_filters")
public final class MaterialsTextureFilter extends ParallaxTest 
{

	private static final String texture = "textures/caravaggio.jpg";
	
	PerspectiveCamera camera;
	int mouseX = 0, mouseY = 0;
	
	Scene scene, scene2;
	
	PlaneGeometry geometry;
	
	Texture texturePainting2;
	Mesh meshCanvas;
	Mesh meshCanvas2;
	
	MeshBasicMaterial materialPainting;
	MeshBasicMaterial materialPainting2;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				35, // fov
				context.getAspectRation(), // aspect
				1, // near
				5000 // far 
		); 
		
		camera.getPosition().setZ(1500);
		
		this.scene2 = new Scene();
		
		scene.setFog( new Fog( 0x000000, 1500, 4000 ));
		scene2.setFog( scene.getFog() );

		// GROUND

//		CanvasElement canvas = Document.get().createElement("canvas").cast();
//		canvas.setWidth(128);
//		canvas.setHeight(128);
//		Context2d 2dcontext = canvas.getContext2d();
//
//		context.setFillStyle( "#444" );
//		context.fillRect( 0, 0, 128, 128 );
//
//		context.setFillStyle( "#fff" );
//		context.fillRect( 0, 0, 64, 64);
//		context.fillRect( 64, 64, 64, 64 );

//		Texture textureCanvas = new Texture( canvas );
//		textureCanvas.setWrapS(TextureWrapMode.REPEAT);
//		textureCanvas.setWrapT(TextureWrapMode.REPEAT);
//		MeshBasicMaterial materialCanvas = new MeshBasicMaterial();
//		materialCanvas.setMap(textureCanvas);
//
//		textureCanvas.setNeedsUpdate(true);
//		textureCanvas.setRepeat(new Vector2(1000, 1000));

//		Texture textureCanvas2 = new Texture( canvas );
//		textureCanvas2.setWrapS(TextureWrapMode.REPEAT);
//		textureCanvas2.setWrapT(TextureWrapMode.REPEAT);
//		textureCanvas2.setMagFilter(TextureMagFilter.NEAREST);
//		textureCanvas2.setMinFilter(TextureMinFilter.NEAREST);
//		MeshBasicMaterial materialCanvas2 = new MeshBasicMaterial();
//		materialCanvas2.setColor(new Color(0xffccaa));
//		materialCanvas2.setMap(textureCanvas2);
//
//		textureCanvas2.setNeedsUpdate(true);
//		textureCanvas2.setRepeat(new Vector2(1000, 1000));

		geometry = new PlaneGeometry( 100, 100 );

//		meshCanvas = new Mesh( geometry, materialCanvas );
//		meshCanvas.getRotation().setX( - Math.PI / 2.0 );
//		meshCanvas.getScale().set( 1000 );
//
//		meshCanvas2 = new Mesh( geometry, materialCanvas2 );
//		meshCanvas2.getRotation().setX( - Math.PI / 2.0 );
//		meshCanvas2.getScale().set( 1000 );

		// PAINTING
		Texture texturePainting = new Texture(texture, new Texture.ImageLoadHandler() {

			@Override
			public void onImageLoad(Texture texture) {
				callbackPainting(texture);
			}
		} );
		
		materialPainting = new MeshBasicMaterial();
		materialPainting.setColor(new Color(0xffffff));
		materialPainting.setMap(texturePainting);
		
		texturePainting2 = new Texture();
		materialPainting2 = new MeshBasicMaterial()
				.setColor(0xffccaa)
				.setMap(texturePainting2);

		texturePainting2.setMinFilter(TextureMinFilter.NEAREST);
		texturePainting2.setMagFilter(TextureMagFilter.NEAREST);

		texturePainting.setMinFilter(TextureMinFilter.LINEAR);
		texturePainting.setMagFilter(TextureMagFilter.LINEAR);

		context.getRenderer().setClearColor( scene.getFog().getColor(), 1 );
		context.getRenderer().setAutoClear(false);
	}
	
	private void callbackPainting( Texture texture ) 
	{
		texturePainting2.setImage(texture.getImage());
		texturePainting2.setNeedsUpdate(true);

//		scene.add( meshCanvas );
//		scene2.add( meshCanvas2 );

		PlaneGeometry geometry = new PlaneGeometry( 100, 100 );
		Mesh mesh = new Mesh( geometry, materialPainting );
		Mesh mesh2 = new Mesh( geometry, materialPainting2 );

		addPainting( texture.getImage(), scene, mesh );
		addPainting( texture.getImage(), scene2, mesh2 );
	}
	
	private void addPainting(TextureData image, Scene zscene, Mesh zmesh )
	{
		zmesh.getScale().setX( image.getWidth() / 100.0 ) ;
		zmesh.getScale().setY( image.getHeight() / 100.0 );

		zscene.add( zmesh );

		MeshBasicMaterial mb = new MeshBasicMaterial();
		mb.setColor(new Color(0x000000));
		mb.setPolygonOffset(true);
		mb.setPolygonOffsetFactor(1);
		mb.setPolygonOffsetUnits(5);

		Mesh meshFrame = new Mesh( geometry,  mb);

		meshFrame.getScale().setX( 1.1 * image.getWidth() / 100 );
		meshFrame.getScale().setY( 1.1 * image.getHeight() / 100 );

		zscene.add( meshFrame );

		MeshBasicMaterial mb2 = new MeshBasicMaterial();
		mb2.setColor(new Color(0x000000));
		mb2.setOpacity(0.75);
		mb2.setTransparent(true);

		Mesh meshShadow = new Mesh( geometry, mb2 );
		meshShadow.getPosition().setY( - 1.1 * image.getWidth()/ 2.0 );
		meshShadow.getPosition().setZ( - 1.1 * image.getHeight()/ 2.0 );
		meshShadow.getRotation().setX( - Math.PI / 2 );
		meshShadow.getScale().setX( 1.1 * image.getWidth() / 100.0 );
		meshShadow.getScale().setY( 1.1 * image.getHeight() / 100.0 );
		zscene.add( meshShadow );

		meshShadow.getPosition().setY( - 1.1 * image.getHeight() / 2.0 );

		double floorHeight = - 1.117 * image.getHeight() / 2.0;
		meshCanvas.getPosition().setY( floorHeight );
		meshCanvas2.getPosition().setY( floorHeight );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * .05 );
		camera.getPosition().addY( ( - ( mouseY - 200) - camera.getPosition().getY() ) * .05 );

		camera.lookAt( scene.getPosition() );

		context.getRenderer().enableScissorTest( false );
		context.getRenderer().clear();
		context.getRenderer().enableScissorTest( true );

		context.getRenderer().setScissor( context.getRenderer().getAbsoluteWidth()/2, 0, context.getRenderer().getAbsoluteWidth()/2 - 2, context.getRenderer().getAbsoluteHeight()  );
		context.getRenderer().render( this.scene2, camera );

		context.getRenderer().setScissor( 0, 0, context.getRenderer().getAbsoluteWidth()/2 - 2, context.getRenderer().getAbsoluteHeight() );
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Texture filtering";
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
//	public void onAnimationReady(AnimationReadyEvent event)
//	{
//		super.onAnimationReady(event);
//
//		this.renderingPanel.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
//		      @Override
//		      public void onMouseMove(MouseMoveEvent event)
//		      {
//		    	  	DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//
//		    	  	rs.mouseX = (event.getX() - renderingPanel.context.getRenderer().getAbsoluteWidth() / 2 ); 
//		    	  	rs.mouseY = (event.getY() - renderingPanel.context.getRenderer().getAbsoluteHeight() / 2);
//		      }
//		});
//	}

}
