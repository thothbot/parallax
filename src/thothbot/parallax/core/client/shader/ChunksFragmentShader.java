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
 * Chunks used in Fragment Shader
 * @author thothbot
 *
 */
public class ChunksFragmentShader
{
	public static String ALPHA_TEST = Chunks.INSTANCE.getAlphatestFragment().getText();
	
	public static String COLOR_PARS = Chunks.INSTANCE.getColorParsFragment().getText();
	public static String COLOR = Chunks.INSTANCE.getColorFragment().getText();
	
	public static String ENVMAP_PARS = Chunks.INSTANCE.getEnvmapParsFragment().getText();
	public static String ENVMAP = Chunks.INSTANCE.getEnvmapFragment().getText();
	
	public static String DEFAULT_PARS = Chunks.INSTANCE.getDefaultParsFragment().getText();
	
	public static String FOG_PARS = Chunks.INSTANCE.getFogParsFragment().getText();
	public static String FOG = Chunks.INSTANCE.getFogFragment().getText();
	
	public static String LIGHTMAP_PARS = Chunks.INSTANCE.getLightmapParsFragment().getText();
	public static String LIGHTMAP = Chunks.INSTANCE.getLightmapFragment().getText();
	
	public static String LIGHTS_PONG_PARS = Chunks.INSTANCE.getLightsPhongParsFragment().getText();
	public static String LIGHTS_PONG = Chunks.INSTANCE.getLightsPhongFragment().getText();
	
	public static String LENEAR_TO_GAMMA = Chunks.INSTANCE.getLinearToGammaFragment().getText();
	
	public static String MAP_PARS = Chunks.INSTANCE.getMapParsFragment().getText();
	public static String MAP = Chunks.INSTANCE.getMapFragment().getText();
	
	public static String MAP_PARTICLE_PARS = Chunks.INSTANCE.getMapParticleParsFragment().getText();
	public static String MAP_PARTICLE = Chunks.INSTANCE.getMapParticleFragment().getText();
	
	public static String SHADOWMAP_PARS = Chunks.INSTANCE.getShadowmapParsFragment().getText();
	public static String SHADOWMAP = Chunks.INSTANCE.getShadowmapFragment().getText();
	
}
