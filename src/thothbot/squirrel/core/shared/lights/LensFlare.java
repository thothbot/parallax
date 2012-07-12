/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.lights;

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.objects.Object3D;
import thothbot.squirrel.core.shared.textures.Texture;


public final class LensFlare extends Object3D
{
	final class Light
	{
		public Texture texture;
		public int size;
		public int distance;
		public Material.BLENDING blending;
		public int x, y, z;
		public int scale;
		public float rotation;
		public float wantedRotation;
		public float opacity;

		public Light(Texture texture, int size, int distance, Material.BLENDING blending, int x,
				int y, int z, int scale, float rotation, float opacity) {
			this.texture = texture;
			this.size = size;
			this.distance = distance;
			this.blending = blending;
			this.x = x;
			this.y = y;
			this.z = z;
			this.rotation = rotation;
			this.opacity = opacity;
		}
	}

	private Vector3f positionScreen;
	private List<Light> lensFlares;
	private Object customUpdateCallback;

	public LensFlare() {
		this.positionScreen = new Vector3f();
		this.lensFlares = new ArrayList<LensFlare.Light>();
		this.customUpdateCallback = null;
	}

	public LensFlare(Texture texture, Integer size, Integer distance, Material.BLENDING blending) {
		this();
		if (texture != null) {
			this.add(texture, size, distance, blending);
		}
	}

	public void add(Texture texture, Integer size, Integer distance, Material.BLENDING blending)
	{
		int s = size == null ? -1 : size.intValue();
		int d = distance == null ? 0 : distance.intValue();
		Material.BLENDING b = blending == null ? Material.BLENDING.NORMAL : blending;

		d = Math.min(d, Math.max(0, d));
		this.lensFlares.add(new Light(texture, s, d, b, 0, 0, 0, 1, 1, 1));
	}

	/*
	 * Update lens flares update positions on all flares based on the screen
	 * position Set myLensFlare.customUpdateCallback to alter the flares in your
	 * project specific way.
	 */
	public void updateLensFlares()
	{
		float vecX = -this.positionScreen.getX() * 2;
		float vecY = -this.positionScreen.getY() * 2;

		for (Light flare : this.lensFlares) {
			flare.x = (int) (this.positionScreen.getX() + vecX * flare.distance);
			flare.y = (int) (this.positionScreen.getY() + vecY * flare.distance);

			flare.wantedRotation = (float) (flare.x * Math.PI * 0.25f);
			flare.rotation += (flare.wantedRotation - flare.rotation) * 0.25f;
		}
	}

	public Object getCustomUpdateCallback()
	{
		return customUpdateCallback;
	}
}
