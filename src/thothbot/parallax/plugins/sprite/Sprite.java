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

package thothbot.parallax.plugins.sprite;

import thothbot.parallax.core.client.gl2.enums.BlendEquationMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Object3D;

public class Sprite extends Object3D implements Comparable<Sprite>
{
	/*
	 * Alignment
	 */
	public static enum ALIGNMENT 
	{
		TOP_LEFT(1, -1),
		TOP_CENTER(0, -1),
		TOP_RIGHT(-1, -1),
		CENTER_LEFT(1, 0),
		CENTER(0, 0),
		CENTER_RIGHT(-1, 0),
		BOTTOM_LEFT(1, 1),
		BOTTOM_CENTER(0, 1),
		BOTTOM_RIGHT(-1, 1);

		double x, y;
		ALIGNMENT(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public Vector2 get() {
			return new Vector2(this.x, this.y);
		}
	}
	
	private Color color;
	private Vector3 rotation3d;
	private Texture map;
	
	private Material.BLENDING blending = Material.BLENDING.NORMAL;
	private BlendingFactorSrc blendSrc = BlendingFactorSrc.SRC_ALPHA;
	private BlendingFactorDest  blendDst = BlendingFactorDest.ONE_MINUS_SRC_ALPHA; 
	private BlendEquationMode blendEquation = BlendEquationMode.FUNC_ADD;
	
	private boolean useScreenCoordinates = true;
	private boolean mergeWith3D;
	private boolean affectedByDistance;
	private boolean scaleByViewport;
	
	private Sprite.ALIGNMENT alignment = Sprite.ALIGNMENT.CENTER;
	
	private Vector2 uvOffset;
	private Vector2 uvScale;
	
	private double opacity = 1.0;
	
	private double rotationFactor;
	
	private double z;
	
	public Sprite() 
	{
		this.color = new Color( 0xffffff );
		this.map = new Texture();

		this.mergeWith3D = !this.useScreenCoordinates;
		this.affectedByDistance = !this.useScreenCoordinates;
		this.scaleByViewport = !this.affectedByDistance;

		this.rotation3d = this.rotation;
		this.rotationFactor = 0;

		this.uvOffset = new Vector2( 0, 0 );
		this.uvScale  = new Vector2( 1, 1 );
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Texture getMap() {
		return map;
	}

	public void setMap(Texture map) {
		this.map = map;
	}

	public Material.BLENDING getBlending() {
		return blending;
	}

	public void setBlending(Material.BLENDING blending) {
		this.blending = blending;
	}

	public BlendingFactorSrc getBlendSrc() {
		return blendSrc;
	}

	public void setBlendSrc(BlendingFactorSrc blendSrc) {
		this.blendSrc = blendSrc;
	}

	public BlendingFactorDest getBlendDst() {
		return blendDst;
	}

	public void setBlendDst(BlendingFactorDest blendDst) {
		this.blendDst = blendDst;
	}

	public BlendEquationMode getBlendEquation() {
		return blendEquation;
	}

	public void setBlendEquation(BlendEquationMode blendEquation) {
		this.blendEquation = blendEquation;
	}

	public Sprite.ALIGNMENT getAlignment() {
		return alignment;
	}

	public void setAlignment(Sprite.ALIGNMENT alignment) {
		this.alignment = alignment;
	}

	public Vector2 getUvOffset() {
		return uvOffset;
	}

	public void setUvOffset(Vector2 uvOffset) {
		this.uvOffset = uvOffset;
	}

	public Vector2 getUvScale() {
		return uvScale;
	}

	public void setUvScale(Vector2 uvScale) {
		this.uvScale = uvScale;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public double getRotationFactor() {
		return rotationFactor;
	}

	public void setRotationFactor(double rotation) {
		this.rotationFactor = rotation;
	}
	
	public boolean isUseScreenCoordinates() {
		return useScreenCoordinates;
	}

	public void setUseScreenCoordinates(boolean useScreenCoordinates) {
		this.useScreenCoordinates = useScreenCoordinates;
	}

	public boolean isMergeWith3D() {
		return mergeWith3D;
	}

	public void setMergeWith3D(boolean mergeWith3D) {
		this.mergeWith3D = mergeWith3D;
	}
	
	public boolean isAffectedByDistance() {
		return affectedByDistance;
	}

	public void setAffectedByDistance(boolean affectedByDistance) {
		this.affectedByDistance = affectedByDistance;
	}

	public boolean isScaleByViewport() {
		return scaleByViewport;
	}

	public void setScaleByViewport(boolean scaleByViewport) {
		this.scaleByViewport = scaleByViewport;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	/*
	 * Custom update matrix
	 */
	public void updateMatrix() 
	{
		this.matrix.setPosition( this.position );

		this.rotation3d.set( 0, 0, this.rotationFactor );
		this.matrix.setRotationFromEuler( this.rotation3d );

		if ( this.scale.getX() != 1 || this.scale.getY() != 1 ) 
		{
			this.matrix.scale( this.scale );
			this.boundRadiusScale = Math.max( this.scale.getX(), this.scale.getY() );
		}

		this.matrixWorldNeedsUpdate = true;
	}
	
	@Override
	public int compareTo(Sprite o)
	{
		double result = o.z - this.z;
		return (result == 0) ? 0 
				: (result > 0) ? 1 : -1;
	}
}
