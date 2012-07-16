/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.squirrel.core.client.shader;

/**
 * Chunks used in Vertex Shader
 * @author thothbot
 *
 */
public class ChunksVertexShader
{
	public static String COLOR_PARS = Chunks.INSTANCE.getColorParsVertex().getText();
	public static String COLOR = Chunks.INSTANCE.getColorVertex().getText();
	
	public static String DEFAULT_PARS = Chunks.INSTANCE.getDefaultParsVertex().getText();
	public static String DEFAULT = Chunks.INSTANCE.getDefaultVertex().getText();
	
	public static String ENVMAP_PARS = Chunks.INSTANCE.getEnvmapParsVertex().getText();
	public static String ENVMAP = Chunks.INSTANCE.getEnvmapVertex().getText();
	
	public static String LIGHTMAP_PARS = Chunks.INSTANCE.getLightmapParsVertex().getText();
	public static String LIGHTMAP = Chunks.INSTANCE.getLightmapVertex().getText();
	
	public static String LIGHTS_LAMBERT_PARS = Chunks.INSTANCE.getLightsLambertParsVertex().getText();
	public static String LIGHTS_LAMBERT = Chunks.INSTANCE.getLightsLambertVertex().getText();
	
	public static String LIGHTS_PHONG_PARS = Chunks.INSTANCE.getLightsPhongParsVertex().getText();
	public static String LIGHTS_PHONG = Chunks.INSTANCE.getLightsPhongVertex().getText();
	
	public static String MAP_PARS = Chunks.INSTANCE.getMapParsVertex().getText();
	public static String MAP = Chunks.INSTANCE.getMapVertex().getText();
	
	public static String MORPH_NORMAL = Chunks.INSTANCE.getMorphnormalVertex().getText();
	
	public static String MORPH_TARGET_PARS = Chunks.INSTANCE.getMorphtargetParsVertex().getText();
	public static String MORPH_TARGET = Chunks.INSTANCE.getMorphtargetVertex().getText();
	
	public static String SHADOWMAP_PARS = Chunks.INSTANCE.getShadowmapParsVertex().getText();
	public static String SHADOWMAP = Chunks.INSTANCE.getShadowmapVertex().getText();
	
	public static String SKINNING_PARS = Chunks.INSTANCE.getSkinningParsVertex().getText();
	public static String SKINNING = Chunks.INSTANCE.getSkinningVertex().getText();
}
