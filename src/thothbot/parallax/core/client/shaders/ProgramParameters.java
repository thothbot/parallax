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
	public Shader.PRECISION precision;
	
	public boolean supportsVertexTextures;

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
	
	public boolean sizeAttenuation;
	public boolean logarithmicDepthBuffer;
	
	public boolean skinning;
	public int maxBones;
	public boolean useVertexTexture;
	
	public boolean morphTargets;
	public boolean morphNormals;
	public int maxMorphTargets;
	public int maxMorphNormals;

	public int maxDirLights;
	public int maxPointLights;
	public int maxSpotLights;
	public int maxHemiLights;

	public int maxShadows;
	public boolean shadowMapEnabled;
	public boolean shadowMapSoft;
	public boolean shadowMapDebug;
	public boolean shadowMapCascade;

	public double alphaTest;
	public boolean metal;
	public boolean wrapAround;
	public boolean doubleSided;
	public boolean flipSided;
	
	public boolean gammaInput;
	public boolean gammaOutput;
	
	public String toString() 
	{
		String retval = "";
		retval += supportsVertexTextures + "-1-" 
				
				+ map + ", " + envMap + ", " + lightMap + ", " + bumpMap + ", " + normalMap + ", " + specularMap + ", " + alphaMap + "-2-" 

				+ vertexColors + "-3-" 
				
				+ useFog + ", " + useFog2 + "-4-" 
				
				+ sizeAttenuation + ", " + logarithmicDepthBuffer + "-5-" 
				
				+ skinning + ", " + maxBones + ", " + useVertexTexture + "-6-" 
				
				+ morphTargets + morphNormals + ", " + maxMorphTargets + ", " + maxMorphNormals + "-7-"  				 
								
				+ maxDirLights + ", " + maxPointLights + ", " + maxSpotLights + ", " + maxHemiLights + "-8-"
				
				+ maxShadows + ", " + shadowMapEnabled + ", " + shadowMapSoft + ", " + shadowMapDebug  + ", " + shadowMapCascade + "-9-"  
				
				+ alphaTest + ", "  + metal + ", " + wrapAround + ", " + doubleSided + ", " + flipSided + "-10-"
				
				+ gammaInput + ", " + gammaOutput + "-11-";

		return retval;
	}
}
