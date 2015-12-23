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

package org.parallax3d.parallax.graphics.renderers.shaders;

/**
 * Chunks used in Vertex Shader
 * @author thothbot
 *
 */
public class ChunksVertexShader
{
	public static String COLOR_PARS = Chunks.getColorParsVertex();
	public static String COLOR = Chunks.getColorVertex();
	
	public static String DEFAULT = Chunks.getDefaultVertex();
	
	public static String DEFAULTNORMAL = Chunks.getDefaultNormalVertex();
	
	public static String ENVMAP_PARS = Chunks.getEnvmapParsVertex();
	public static String ENVMAP = Chunks.getEnvmapVertex();
	
	public static String LIGHTMAP_PARS = Chunks.getLightmapParsVertex();
	public static String LIGHTMAP = Chunks.getLightmapVertex();
	
	public static String LIGHTS_LAMBERT_PARS = Chunks.getLightsLambertParsVertex();
	public static String LIGHTS_LAMBERT = Chunks.getLightsLambertVertex();
	
	public static String LIGHTS_PHONG_PARS = Chunks.getLightsPhongParsVertex();
	public static String LIGHTS_PHONG = Chunks.getLightsPhongVertex();
	
	public static String LOGDEPTHBUF_PAR = Chunks.getLogdepthbufParVertex();
	public static String LOGDEPTHBUF = Chunks.getLogdepthbufVertex();
	
	public static String MAP_PARS = Chunks.getMapParsVertex();
	public static String MAP = Chunks.getMapVertex();
	
	public static String MORPHNORMAL = Chunks.getMorphnormalVertex();
	
	public static String MORPHTARGET_PARS = Chunks.getMorphtargetParsVertex();
	public static String MORPHTARGET = Chunks.getMorphtargetVertex();
	
	public static String SHADOWMAP_PARS = Chunks.getShadowmapParsVertex();
	public static String SHADOWMAP = Chunks.getShadowmapVertex();
	
	public static String SKINBASE = Chunks.getSkinBaseVertex();
	
	public static String SKINNORMAL = Chunks.getSkinNormalVertex();
	
	public static String SKINNING_PARS = Chunks.getSkinningParsVertex();
	public static String SKINNING = Chunks.getSkinningVertex();
	
	public static String WORLDPOS = Chunks.getWorldposVertex();
}
