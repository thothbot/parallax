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

package org.parallax3d.parallax.tests.cases.plugins;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.renderers.plugins.sprite.Sprite;
import org.parallax3d.parallax.graphics.renderers.plugins.sprite.SpriteMaterial;
import org.parallax3d.parallax.graphics.renderers.plugins.sprite.SpritePlugin;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_sprites")
public final class EffectsSprites extends ParallaxTest 
{

	Scene scene;
	PerspectiveCamera camera;
	OrthographicCamera cameraOrtho;
	
	Object3D group;
	
	Scene sceneOrtho;
	
	Sprite spriteTL, spriteTR, spriteBL, spriteBR, spriteC;
	
	Texture mapB = new Texture( "textures/sprite1.png" );
	Texture mapC = new Texture( "textures/sprite2.png" );
	
	@Override
	public void onResize(RenderingContext context) 
	{
		updateHUDSprites(context);
	}
	
	@Override
	public void onStart(final RenderingContext context)
	{
		scene = new Scene();

		camera = new PerspectiveCamera(
				60, // fov
				context.getAspectRation(), // aspect
				1, // near
				2100 // far 
		);
		
		camera.getPosition().setZ(1500);
		
		cameraOrtho = new OrthographicCamera( context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight(), 1, 10 );
		cameraOrtho.getPosition().setZ( 10 );

		scene.setFog( new Fog( 0x000000, 1500, 2100 ) );
		
		sceneOrtho = new Scene();
		
		int amount = 200;
		int radius = 500;
		
		new SpritePlugin(context.getRenderer(), scene);
		new SpritePlugin(context.getRenderer(), sceneOrtho);
		
		new Texture( "textures/sprite0.png", new Texture.ImageLoadHandler() {

			@Override
			public void onImageLoad(Texture texture) {
				SpriteMaterial material = new SpriteMaterial();
				material.setMap(texture);

				int width = material.getMap().getImage().getWidth();
				int height = material.getMap().getImage().getHeight();

				spriteTL = new Sprite( material );
				spriteTL.getScale().set( width, height, 1 );
				sceneOrtho.add( spriteTL );

				spriteTR = new Sprite( material );
				spriteTR.getScale().set( width, height, 1 );
				sceneOrtho.add( spriteTR );

				spriteBL = new Sprite( material );
				spriteBL.getScale().set( width, height, 1 );
				sceneOrtho.add( spriteBL );

				spriteBR = new Sprite( material );
				spriteBR.getScale().set( width, height, 1 );
				sceneOrtho.add( spriteBR );

				spriteC = new Sprite( material );
				spriteC.getScale().set( width, height, 1 );
				sceneOrtho.add( spriteC );

				updateHUDSprites(context);
			}
		} );

		SpriteMaterial materialC = new SpriteMaterial()
				.setMap(mapC)
				.setColor( 0xffffff )
				.setFog(true);

		SpriteMaterial materialB = new SpriteMaterial()
				.setMap(mapB)
				.setColor( 0xffffff )
				.setFog(true);

		group = new Object3D();
		
		for ( int a = 0; a < amount; a ++ ) {

			double x = Math.random() - 0.5;
			double y = Math.random() - 0.5;
			double z = Math.random() - 0.5;

			SpriteMaterial material;
			
			if ( z < 0 ) {

				material = materialB.clone();

			} else {

				material = materialC.clone();
				material.getColor().setHSL( 0.5 * Math.random(), 0.75, 0.5 );
				material.getMap().getOffset().set( -0.5, -0.5 );
				material.getMap().getRepeat().set( 2, 2 );
			}

			Sprite sprite = new Sprite( material );

			sprite.getPosition()
					.set( x, y, z )
					.normalize()
					.multiply( radius );

			group.add( sprite );

		}

		scene.add( group );

		// To allow render overlay on top of sprited sphere
		context.getRenderer().setAutoClear(false); 
	}
	
	private void updateHUDSprites (RenderingContext context) {

		int width = context.getRenderer().getAbsoluteWidth() / 2;
		int height = context.getRenderer().getAbsoluteHeight() / 2;

		if(spriteTL == null)
			return;

		SpriteMaterial material = (SpriteMaterial) spriteTL.getMaterial();

		int imageWidth =  material.getMap().getImage().getWidth() / 2;
		int imageHeight =  material.getMap().getImage().getHeight() / 2;

		spriteTL.getPosition().set( - width + imageWidth,   height - imageHeight, 1 ); // top left
		spriteTR.getPosition().set(   width - imageWidth,   height - imageHeight, 1 ); // top right
		spriteBL.getPosition().set( - width + imageWidth, - height + imageHeight, 1 ); // bottom left
		spriteBR.getPosition().set(   width - imageWidth, - height + imageHeight, 1 ); // bottom right
		spriteC.getPosition().set( 0, 0, 1 ); // center

	};
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getDeltaTime() * .001;
		
		for ( int i = 0, l = group.getChildren().size(); i < l; i ++ ) 
		{
			Sprite sprite = (Sprite) group.getChildren().get(i);
			SpriteMaterial material = (SpriteMaterial) sprite.getMaterial();
			double scale = Math.sin( time + sprite.getPosition().getX() * 0.01 ) * 0.3 + 1.0;

			int imageWidth = 1;
			int imageHeight = 1;

			if ( material.getMap() != null 
					&& material.getMap().getImage() != null 
					&& material.getMap().getImage().getWidth() > 0 ) {

				imageWidth = material.getMap().getImage().getWidth();
				imageHeight = material.getMap().getImage().getHeight();

			}

			material.setRotation(material.getRotation() + 0.1 * ( (double)i / l ) );
			sprite.getScale().set( scale * imageWidth, scale * imageHeight, 1.0 );

			if ( !material.getMap().equals( mapC ) )
				material.setOpacity( Math.sin( time + sprite.getPosition().getX() * 0.01 ) * 0.4 + 0.6 );
		}
		
		group.getRotation().setX( time * 0.5 );
		group.getRotation().setY( time * 0.75);
		group.getRotation().setZ( time * 1.0 );
		
		context.getRenderer().clear();
		context.getRenderer().render( scene, camera );
		context.getRenderer().clearDepth();
		context.getRenderer().render( sceneOrtho, cameraOrtho );

	}
		
	@Override
	public String getName() {
		return "Sprites";
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
