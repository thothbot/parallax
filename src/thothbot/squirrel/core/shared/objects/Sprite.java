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

package thothbot.squirrel.core.shared.objects;

import java.util.Map;

import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Vector2f;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.materials.Material.BLENDING;
import thothbot.squirrel.core.shared.materials.Material.BLENDING_EQUATION;
import thothbot.squirrel.core.shared.materials.Material.BLENDING_FACTORS;
import thothbot.squirrel.core.shared.textures.Texture;

public class Sprite extends Object3D
{
	/*
	 * Alignment
	 */
	public static enum ALIGNMENT {
		
		TOP_LEFT(1, -1),
		TOP_CENTER(0, -1),
		TOP_RIGHT(-1, -1),
		CENTER_LEFT(1, 0),
		CENTER(0, 0),
		CENTER_RIGHT(-1, 0),
		BOTTOM_LEFT(1, 1),
		BOTTOM_CENTER(0, 1),
		BOTTOM_RIGHT(-1, 1);

		float x, y;
		ALIGNMENT(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public Vector2f get() {
			return new Vector2f(this.x, this.y);
		}
	};
	
	public Color3f color;
	public Vector3f rotation3d;
	public Texture map;
	public Material.BLENDING blending = Material.BLENDING.NORMAL;
	public Material.BLENDING_FACTORS blendSrc = Material.BLENDING_FACTORS.SRC_ALPHA;
	
	public Material.BLENDING_FACTORS  blendDst = Material.BLENDING_FACTORS.ONE_MINUS_SRC_ALPHA; 
	public Material.BLENDING_EQUATION blendEquation = Material.BLENDING_EQUATION.ADD;
	
	public boolean useScreenCoordinates = true;
	public boolean mergeWith3D;
	public boolean affectedByDistance;
	public boolean scaleByViewport;
	
	public Sprite.ALIGNMENT alignment = Sprite.ALIGNMENT.CENTER;
	
	public Vector2f uvOffset;
	public Vector2f uvScale;
	
	public float opacity = 1.0f;
	
	public int rotation;
	
	public Sprite(Map<String, Object> parameters) {
		if(parameters.containsKey("color"))
			this.color = new Color3f( (Integer) parameters.get("color") );
		else
			this.color = new Color3f( 0xffffff );
		
		if(parameters.containsKey("map"))
			this.map = (Texture) parameters.get("map");
		else
			this.map = new Texture();
	
		if(parameters.containsKey("blending"))
			this.blending = (BLENDING) parameters.get("blending");

		if(parameters.containsKey("blendSrc"))
			this.blendSrc = (BLENDING_FACTORS) parameters.get("blendSrc");
		if(parameters.containsKey("blendDst"))
			this.blendDst = (BLENDING_FACTORS) parameters.get("blendDst");
		if(parameters.containsKey("blendEquation"))
			this.blendEquation = (BLENDING_EQUATION) parameters.get("blendEquation");

		if(parameters.containsKey("useScreenCoordinates"))
			this.useScreenCoordinates = (Boolean) parameters.get("useScreenCoordinates");
		
		if(parameters.containsKey("mergeWith3D"))
			this.mergeWith3D = (Boolean) parameters.get("mergeWith3D");
		else 
			this.mergeWith3D = !this.useScreenCoordinates;
		
		if(parameters.containsKey("affectedByDistance"))
			this.affectedByDistance = (Boolean) parameters.get("affectedByDistance");
		else 
			this.affectedByDistance = !this.useScreenCoordinates;
		
		if(parameters.containsKey("scaleByViewport"))
			this.scaleByViewport = (Boolean) parameters.get("scaleByViewport");
		else 
			this.scaleByViewport = !this.affectedByDistance;
		
		if(parameters.containsKey("alignment"))
			this.alignment = (Sprite.ALIGNMENT) parameters.get("alignment");

		//TODO: ?
		//this.rotation3d = this.rotation;
		this.rotation = 0;

		this.uvOffset = new Vector2f( 0, 0 );
		this.uvScale  = new Vector2f( 1, 1 );
	}

	/*
	 * Custom update matrix
	 */
	public void updateMatrix() 
	{
		this.matrix.setPosition( this.position );

		this.rotation3d.set( 0f, 0f, this.rotation );
		this.matrix.setRotationFromEuler( this.rotation3d );

		if ( this.scale.getX() != 1 || this.scale.getY() != 1 ) {
			this.matrix.scale( this.scale );
			this.boundRadiusScale = Math.max( this.scale.getX(), this.scale.getY() );
		}

		this.matrixWorldNeedsUpdate = true;
	}
}
