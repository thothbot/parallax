/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.materials.Material;


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
		public float distance;

		// blending
		public Material.BLENDING blending;
		// screen position (-1 => 1) z = 0 is ontop z = 1 is back
		public float x, y, z;
		// scale
		public float scale;
		// rotation
		public float rotation;
		// opacity
		public float opacity;
		// color
		public Color3f color;
		
		private float wantedRotation;

		public LensSprite(Texture texture, Integer size, Float distance, float x,
				float y, float z, float scale, float rotation, float opacity, Color3f color, Material.BLENDING blending
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

	private Vector3f positionScreen;
	private List<LensSprite> lensFlares;
	private Callback updateCallback;

	public LensFlare(Texture texture, Integer size, Float distance, Material.BLENDING blending, Color3f color) 
	{
		this.positionScreen = new Vector3f();
		this.lensFlares = new ArrayList<LensFlare.LensSprite>();

		setUpdateCallback(new Callback() {
			
			@Override
			public void update() {
				float vecX = -LensFlare.this.positionScreen.getX() * 2f;
				float vecY = -LensFlare.this.positionScreen.getY() * 2f;

				for( int f = 0; f < LensFlare.this.lensFlares.size(); f ++ ) 
				{
					LensSprite flare = LensFlare.this.lensFlares.get( f );

					flare.x = LensFlare.this.positionScreen.getX() + vecX * flare.distance;
					flare.y = LensFlare.this.positionScreen.getY() + vecY * flare.distance;

					flare.wantedRotation = (float) (flare.x * Math.PI * 0.25);
					flare.rotation += ( flare.wantedRotation - flare.rotation ) * 0.25f;
				}
			}
		});

		if (texture != null)
			this.add(texture, size, distance, blending, color, null);
	}

	public void add(Texture texture, Integer size, Float distance, Material.BLENDING blending)
	{
		add(texture, size, distance, blending, null, null);
	}
	
	public void add(Texture texture, Integer size, Float distance, Material.BLENDING blending, Color3f color, Float opacity)
	{
		if( size == null ) size = -1;
		if( distance == null ) distance = 0f;
		if( opacity == null ) opacity = 1f;
		if( color == null ) color = new Color3f( 0xffffff );
		if( blending == null ) blending = Material.BLENDING.NORMAL;

		distance = Math.min( distance, Math.max( 0, distance ) );
		
		this.lensFlares.add(new LensSprite(
				texture,
				size,
				distance,
				0, 0, 0, // XYZ
				1f, // Scale
				1f, // Rotation
				opacity,
				color,
				blending
				));
	}

	public List<LensSprite> getLensFlares() {
		return this.lensFlares;
	}
	
	public Vector3f getPositionScreen() {
		return this.positionScreen;
	}
	
	public LensFlare.Callback getUpdateCallback() {
		return this.updateCallback;
	}

	public void setUpdateCallback(LensFlare.Callback callback) {
		this.updateCallback = callback;
	}
}
