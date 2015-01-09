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

package thothbot.parallax.plugins.lensflare;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;


public final class LensFlare extends Object3D
{
	public interface Callback
	{
		/*
		 * Update lens flares update positions on all flares based on the screen
		 * position Set myLensFlare.customUpdateCallback to alter the flares in your
		 * project specific way.
		 */
		public void update();
	}

	public class LensSprite
	{
		// Texture
		public Texture texture;
		// size in pixels (-1 = use texture.width)
		public int size;
		// distance (0-1) from light source (0=at light source)
		public double distance;

		// blending
		public Material.BLENDING blending;
		// screen position (-1 => 1) z = 0 is ontop z = 1 is back
		public double x, y, z;
		// scale
		public double scale;
		// rotation
		public double rotation;
		// opacity
		public double opacity;
		// color
		public Color color;
		
		private double wantedRotation;

		public LensSprite(Texture texture, Integer size, double distance, double x,
				double y, double z, double scale, double rotation, double opacity, Color color, Material.BLENDING blending
		) {
			this.texture = texture;
			this.size = size;
			this.distance = distance;
			this.x = x;
			this.y = y;
			this.z = z;
			this.scale = scale;
			this.rotation = rotation;
			this.opacity = opacity;
			this.color = color;
			this.blending = blending;
		}
	}

	private Vector3 positionScreen;
	private List<LensSprite> lensFlares;
	private Callback updateCallback;

	public LensFlare(Texture texture, Integer size, double distance, Material.BLENDING blending, Color color) 
	{
		this.positionScreen = new Vector3();
		this.lensFlares = new ArrayList<LensFlare.LensSprite>();

		setUpdateCallback(new Callback() {
			
			@Override
			public void update() {
				double vecX = -LensFlare.this.positionScreen.getX() * 2.0;
				double vecY = -LensFlare.this.positionScreen.getY() * 2.0;

				for( int f = 0; f < LensFlare.this.lensFlares.size(); f ++ ) 
				{
					LensSprite flare = LensFlare.this.lensFlares.get( f );

					flare.x = LensFlare.this.positionScreen.getX() + vecX * flare.distance;
					flare.y = LensFlare.this.positionScreen.getY() + vecY * flare.distance;

					flare.wantedRotation = flare.x * Math.PI * 0.25;
					flare.rotation += ( flare.wantedRotation - flare.rotation ) * 0.25;
				}
			}
		});

		if (texture != null)
			this.add(texture, size, distance, blending, color, null);
	}

	public void add(Texture texture, Integer size, Double distance, Material.BLENDING blending)
	{
		add(texture, size, distance, blending, null, null);
	}
	
	public void add(Texture texture, Integer size, Double distance, Material.BLENDING blending, Color color, Double opacity)
	{
		Log.debug("LensFlare: add new LensSprite");

		if( size == null ) size = -1;
		if( distance == null ) distance = 0.0;
		if( opacity == null ) opacity = 1.0;
		if( color == null ) color = new Color( 0xffffff );
		if( blending == null ) blending = Material.BLENDING.NORMAL;

		distance = Math.min( distance, Math.max( 0, distance ) );
		
		this.lensFlares.add(new LensSprite(
				texture,
				size,
				distance,
				0, 0, 0, // XYZ
				1.0, // Scale
				1.0, // Rotation
				opacity,
				color,
				blending
				));
	}

	public List<LensSprite> getLensFlares() {
		return this.lensFlares;
	}
	
	public Vector3 getPositionScreen() {
		return this.positionScreen;
	}
	
	public LensFlare.Callback getUpdateCallback() {
		return this.updateCallback;
	}

	public void setUpdateCallback(LensFlare.Callback callback) {
		this.updateCallback = callback;
	}
}
