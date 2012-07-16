/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.core.client.shader;

/**
 * Parameters for the WebGl Program.
 * 
 * @author thothbot
 *
 */
public class ProgramParameters
{
	public boolean gammaInput;
	public boolean gammaOutput;
	public boolean physicallyBasedShading;
	
	public int maxDirLights;
	public int maxPointLights;
	public int maxSpotLights;
	
	public int maxShadows;
	public int maxBones;
	
	public boolean map;
	public boolean envMap;
	public boolean lightMap;
	public boolean vertexColors;
	public boolean skinning;
	public boolean morphTargets;
	public boolean morphNormals;
	public boolean perPixel;
	public boolean wrapAround;
	public boolean doubleSided;
	
	public boolean shadowMapEnabled;
	public boolean shadowMapSoft;
	public boolean shadowMapDebug;
	public boolean shadowMapCascade;
	public boolean sizeAttenuation;
	
	public int alphaTest;
	public boolean useFog;
	public boolean useFog2;
	public boolean metal;
	
	public int maxMorphTargets;
	public int maxMorphNormals;
	
	/**
	 * Creates a new instance of the {@link ProgramParameters}.
	 */
	public ProgramParameters(int maxDirLights, int maxPointLights, int maxSpotLights, int maxShadows, int maxBones, int maxMorphTargets, int maxMorphNormals)
	{
		this.maxDirLights = maxDirLights;
		this.maxPointLights = maxPointLights;
		this.maxSpotLights = maxSpotLights;
		this.maxShadows = maxShadows;
		this.maxBones = maxBones;

		this.maxMorphTargets = maxMorphTargets;
		this.maxMorphNormals = maxMorphNormals;
	}
	
	public String toString() {
		String retval = "";
		retval += gammaInput + ", " + gammaOutput + ", " + physicallyBasedShading 
				+ maxDirLights + ", " + maxPointLights + ", " + maxSpotLights
				+ maxShadows + ", " + maxBones 
				+ map + ", " + envMap + ", " + lightMap + ", " + vertexColors + ", " + skinning + ", " + morphTargets
				+ morphNormals + ", " + perPixel + ", " + wrapAround + ", " + doubleSided
				+ shadowMapEnabled + ", " + shadowMapSoft + ", " + shadowMapDebug  + ", " + shadowMapCascade + ", " + sizeAttenuation
				+ alphaTest + ", " + useFog + ", " + useFog2 + ", " + metal + ", " + maxMorphTargets + ", " + maxMorphNormals;
		return retval;
	}
}
