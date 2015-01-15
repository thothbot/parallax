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

package thothbot.parallax.core.client.shaders;

public class ProgramParameters 
{
	public boolean gammaInput;
	public boolean gammaOutput;
	public boolean physicallyBasedShading;
	
	public int maxDirLights;
	public int maxPointLights;
	public int maxSpotLights;
	public int maxHemiLights;
	
	public int maxShadows;
	public int maxBones;
	public int boneTextureWidth;
	public int boneTextureHeight;
	public boolean useVertexTexture;
	
	public boolean map;
	public boolean envMap;
	public boolean lightMap;
	public boolean bumpMap;
	public boolean normalMap;
	public boolean specularMap;
	public boolean alphaMap;
	
	public boolean vertexColors;

	public boolean useFog;
	public boolean useFog2;

	public boolean metal;

	public boolean skinning;
	public boolean morphTargets;
	public boolean morphNormals;
	public boolean perPixel;
	public boolean wrapAround;
	public boolean doubleSided;
	public boolean flipSided;
	
	public boolean shadowMapEnabled;
	public boolean shadowMapSoft;
	public boolean shadowMapDebug;
	public boolean shadowMapCascade;
	public boolean sizeAttenuation;
	
	public double alphaTest;
	
	public int maxMorphTargets;
	public int maxMorphNormals;
	
	public boolean isSupportsVertexTextures;
	
	public boolean logarithmicDepthBuffer;
	
	public String toString() 
	{
		String retval = "";
		retval += gammaInput + ", " + gammaOutput + ", " + physicallyBasedShading + "-1-"
				+ maxDirLights + ", " + maxPointLights + ", " + maxSpotLights + ", " + maxHemiLights + "-2-"
				+ maxShadows + ", " + maxBones + ", " + boneTextureWidth + ", " + boneTextureHeight + ", " + useVertexTexture + "-2-"
				+ map + ", " + envMap + ", " + lightMap + ", " + bumpMap + ", " + normalMap + ", " + specularMap + ", " + vertexColors + ", " + skinning + ", " + morphTargets + "-4-"
				+ morphNormals + ", " + perPixel + ", " + wrapAround + ", " + doubleSided + ", " + flipSided + "-5-"
				+ shadowMapEnabled + ", " + shadowMapSoft + ", " + shadowMapDebug  + ", " + shadowMapCascade + ", " + sizeAttenuation + "-6-"
				+ alphaTest + ", " + useFog + ", " + useFog2 + ", " + metal + ", " + maxMorphTargets + ", " + maxMorphNormals + ", " + isSupportsVertexTextures;
		return retval;
	}
}
