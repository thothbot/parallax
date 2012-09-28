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

package thothbot.parallax.core.shared.lights;

import java.util.List;

import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;

/**
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * 
 * <pre>
 * {@code
 * // White directional light at half intensity shining from the top. 
 * 
 * DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 0.5 ); 
 * directionalLight.position.set( 0, 1, 0 ); 
 * getScene().add( directionalLight );
 * }
 * </pre>
 * @author thothbot
 *
 */
public class DirectionalLight extends ShadowLight
{	
	private Vector3 shadowCascadeOffset;
	private int shadowCascadeCount = 2;

	private double[] shadowCascadeBias = { 0.0, 0.0, 0.0};
	private int[] shadowCascadeWidth = { 512, 512, 512 };
	private int[] shadowCascadeHeight = { 512, 512, 512 };

	private double[] shadowCascadeNearZ = { -1.000, 0.990, 0.998 };
	private double[] shadowCascadeFarZ = { 0.990, 0.998, 1.000 };

	private List<VirtualLight> shadowCascadeArray;

	//
	
	private int shadowCameraLeft = -500;
	private int shadowCameraRight = 500;
	private int shadowCameraTop = 500;
	private int shadowCameraBottom = -500;

	//
	public DirectionalLight(int hex) 
	{
		this(hex, 1.0);
	}

	public DirectionalLight(int hex, double intensity)
	{
		this(hex, intensity, 0.0);
	}
	
	public DirectionalLight(int hex, double intensity, double distance) 
	{
		super(hex);

		setIntensity(intensity);
		setDistance(distance);

		this.shadowCascadeOffset = new Vector3(0, 0, -1000);
	}
	
	public Vector3 getShadowCascadeOffset() {
		return shadowCascadeOffset;
	}

	public void setShadowCascadeOffset(Vector3 shadowCascadeOffset) {
		this.shadowCascadeOffset = shadowCascadeOffset;
	}

	public int getShadowCascadeCount() {
		return shadowCascadeCount;
	}
	
	public void setShadowCascadeCount(int shadowCascadeCount) {
		this.shadowCascadeCount = shadowCascadeCount;
	}

	public double[] getShadowCascadeBias() {
		return shadowCascadeBias;
	}

	public void setShadowCascadeBias(double[] shadowCascadeBias) {
		this.shadowCascadeBias = shadowCascadeBias;
	}

	public int[] getShadowCascadeWidth() {
		return shadowCascadeWidth;
	}

	public void setShadowCascadeWidth(int[] shadowCascadeWidth) {
		this.shadowCascadeWidth = shadowCascadeWidth;
	}

	public int[] getShadowCascadeHeight() {
		return shadowCascadeHeight;
	}

	public void setShadowCascadeHeight(int[] shadowCascadeHeight) {
		this.shadowCascadeHeight = shadowCascadeHeight;
	}
	
	public double[] getShadowCascadeNearZ() {
		return shadowCascadeNearZ;
	}

	public void setShadowCascadeNearZ(double[] shadowCascadeNearZ) {
		this.shadowCascadeNearZ = shadowCascadeNearZ;
	}

	public double[] getShadowCascadeFarZ() {
		return shadowCascadeFarZ;
	}

	public void setShadowCascadeFarZ(double[] shadowCascadeFarZ) {
		this.shadowCascadeFarZ = shadowCascadeFarZ;
	}

	public List<VirtualLight> getShadowCascadeArray() {
		return shadowCascadeArray;
	}

	public void setShadowCascadeArray(List<VirtualLight> shadowCascadeArray) {
		this.shadowCascadeArray = shadowCascadeArray;
	}

	public int getShadowCameraLeft() {
		return shadowCameraLeft;
	}

	public void setShadowCameraLeft(int shadowCameraLeft) {
		this.shadowCameraLeft = shadowCameraLeft;
	}

	public int getShadowCameraRight() {
		return shadowCameraRight;
	}

	public void setShadowCameraRight(int shadowCameraRight) {
		this.shadowCameraRight = shadowCameraRight;
	}

	public int getShadowCameraTop() {
		return shadowCameraTop;
	}

	public void setShadowCameraTop(int shadowCameraTop) {
		this.shadowCameraTop = shadowCameraTop;
	}

	public int getShadowCameraBottom() {
		return shadowCameraBottom;
	}

	public void setShadowCameraBottom(int shadowCameraBottom) {
		this.shadowCameraBottom = shadowCameraBottom;
	}
}
