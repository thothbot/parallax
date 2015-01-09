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

/**
 * Chunks used in Vertex Shader
 * @author thothbot
 *
 */
public class ChunksVertexShader
{
	public static String COLOR_PARS = Chunks.INSTANCE.getColorParsVertex().getText();
	public static String COLOR = Chunks.INSTANCE.getColorVertex().getText();
	
	public static String DEFAULT = Chunks.INSTANCE.getDefaultVertex().getText();
	
	public static String DEFAULTNORMAL = Chunks.INSTANCE.getDefaultNormalVertex().getText();
	
	public static String ENVMAP_PARS = Chunks.INSTANCE.getEnvmapParsVertex().getText();
	public static String ENVMAP = Chunks.INSTANCE.getEnvmapVertex().getText();
	
	public static String LIGHTMAP_PARS = Chunks.INSTANCE.getLightmapParsVertex().getText();
	public static String LIGHTMAP = Chunks.INSTANCE.getLightmapVertex().getText();
	
	public static String LIGHTS_LAMBERT_PARS = Chunks.INSTANCE.getLightsLambertParsVertex().getText();
	public static String LIGHTS_LAMBERT = Chunks.INSTANCE.getLightsLambertVertex().getText();
	
	public static String LIGHTS_PHONG_PARS = Chunks.INSTANCE.getLightsPhongParsVertex().getText();
	public static String LIGHTS_PHONG = Chunks.INSTANCE.getLightsPhongVertex().getText();
	
	public static String LOGDEPTHBUF_PAR = Chunks.INSTANCE.getLogdepthbufParVertex().getText();
	public static String LOGDEPTHBUF = Chunks.INSTANCE.getLogdepthbufVertex().getText();
	
	public static String MAP_PARS = Chunks.INSTANCE.getMapParsVertex().getText();
	public static String MAP = Chunks.INSTANCE.getMapVertex().getText();
	
	public static String MORPHNORMAL = Chunks.INSTANCE.getMorphnormalVertex().getText();
	
	public static String MORPHTARGET_PARS = Chunks.INSTANCE.getMorphtargetParsVertex().getText();
	public static String MORPHTARGET = Chunks.INSTANCE.getMorphtargetVertex().getText();
	
	public static String SHADOWMAP_PARS = Chunks.INSTANCE.getShadowmapParsVertex().getText();
	public static String SHADOWMAP = Chunks.INSTANCE.getShadowmapVertex().getText();
	
	public static String SKINBASE = Chunks.INSTANCE.getSkinBaseVertex().getText();
	
	public static String SKINNORMAL = Chunks.INSTANCE.getSkinNormalVertex().getText();
	
	public static String SKINNING_PARS = Chunks.INSTANCE.getSkinningParsVertex().getText();
	public static String SKINNING = Chunks.INSTANCE.getSkinningVertex().getText();
	
	public static String WORLDPOS = Chunks.INSTANCE.getWorldposVertex().getText();
}
