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
	public static String COLOR_PARS = Chunks.INSTANCE.getColorParsVertex();
	public static String COLOR = Chunks.INSTANCE.getColorVertex();
	
	public static String DEFAULT = Chunks.INSTANCE.getDefaultVertex();
	
	public static String DEFAULTNORMAL = Chunks.INSTANCE.getDefaultNormalVertex();
	
	public static String ENVMAP_PARS = Chunks.INSTANCE.getEnvmapParsVertex();
	public static String ENVMAP = Chunks.INSTANCE.getEnvmapVertex();
	
	public static String LIGHTMAP_PARS = Chunks.INSTANCE.getLightmapParsVertex();
	public static String LIGHTMAP = Chunks.INSTANCE.getLightmapVertex();
	
	public static String LIGHTS_LAMBERT_PARS = Chunks.INSTANCE.getLightsLambertParsVertex();
	public static String LIGHTS_LAMBERT = Chunks.INSTANCE.getLightsLambertVertex();
	
	public static String LIGHTS_PHONG_PARS = Chunks.INSTANCE.getLightsPhongParsVertex();
	public static String LIGHTS_PHONG = Chunks.INSTANCE.getLightsPhongVertex();
	
	public static String LOGDEPTHBUF_PAR = Chunks.INSTANCE.getLogdepthbufParVertex();
	public static String LOGDEPTHBUF = Chunks.INSTANCE.getLogdepthbufVertex();
	
	public static String MAP_PARS = Chunks.INSTANCE.getMapParsVertex();
	public static String MAP = Chunks.INSTANCE.getMapVertex();
	
	public static String MORPHNORMAL = Chunks.INSTANCE.getMorphnormalVertex();
	
	public static String MORPHTARGET_PARS = Chunks.INSTANCE.getMorphtargetParsVertex();
	public static String MORPHTARGET = Chunks.INSTANCE.getMorphtargetVertex();
	
	public static String SHADOWMAP_PARS = Chunks.INSTANCE.getShadowmapParsVertex();
	public static String SHADOWMAP = Chunks.INSTANCE.getShadowmapVertex();
	
	public static String SKINBASE = Chunks.INSTANCE.getSkinBaseVertex();
	
	public static String SKINNORMAL = Chunks.INSTANCE.getSkinNormalVertex();
	
	public static String SKINNING_PARS = Chunks.INSTANCE.getSkinningParsVertex();
	public static String SKINNING = Chunks.INSTANCE.getSkinningVertex();
	
	public static String WORLDPOS = Chunks.INSTANCE.getWorldposVertex();
}
