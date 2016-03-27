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

package org.parallax3d.parallax.graphics.renderers;

import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.WebGLRenderTargetCube")
public class GLRenderTargetCube extends GLRenderTarget
{
	int activeCubeFace = 0; // PX 0, NX 1, PY 2, NY 3, PZ 4, NZ 5
	int activeMipMapLevel = 0;

	public GLRenderTargetCube(int width, int height, GLRenderTargetOptions options )
	{
		super(width, height, options);
	}

	public int getActiveCubeFace() {
		return activeCubeFace;
	}

	public void setActiveCubeFace(int activeCubeFace) {
		this.activeCubeFace = activeCubeFace;
	}

	public int getActiveMipMapLevel() {
		return activeMipMapLevel;
	}

	public void setActiveMipMapLevel(int activeMipMapLevel) {
		this.activeMipMapLevel = activeMipMapLevel;
	}
}
