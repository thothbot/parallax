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
 * Chunks used in Fragment Shader
 * @author thothbot
 *
 */
public class ChunksFragmentShader
{
	
	public static String ALPHAMAP_PARS = Chunks.INSTANCE.getAlphamapParsFragment().getText();
	public static String ALPHAMAP = Chunks.INSTANCE.getAlphamapFragment().getText();
	
	public static String ALPHA_TEST = Chunks.INSTANCE.getAlphatestFragment().getText();

	public static String BUMPMAP_PARS = Chunks.INSTANCE.getBumpmapParsFragment().getText();
	
	public static String COLOR_PARS = Chunks.INSTANCE.getColorParsFragment().getText();
	public static String COLOR = Chunks.INSTANCE.getColorFragment().getText();
	
	public static String ENVMAP_PARS = Chunks.INSTANCE.getEnvmapParsFragment().getText();
	public static String ENVMAP = Chunks.INSTANCE.getEnvmapFragment().getText();
	
	public static String FOG_PARS = Chunks.INSTANCE.getFogParsFragment().getText();
	public static String FOG = Chunks.INSTANCE.getFogFragment().getText();
	
	public static String LIGHTMAP_PARS = Chunks.INSTANCE.getLightmapParsFragment().getText();
	public static String LIGHTMAP = Chunks.INSTANCE.getLightmapFragment().getText();
	
	public static String LIGHTS_PONG_PARS = Chunks.INSTANCE.getLightsPhongParsFragment().getText();
	public static String LIGHTS_PONG = Chunks.INSTANCE.getLightsPhongFragment().getText();
	
	public static String LINEAR_TO_GAMMA = Chunks.INSTANCE.getLinearToGammaFragment().getText();
	
	public static String LOGDEPTHBUF_PAR = Chunks.INSTANCE.getLogdepthbufParFragment().getText();
	public static String LOGDEPTHBUF = Chunks.INSTANCE.getLogdepthbufFragment().getText();
	
	public static String MAP_PARS = Chunks.INSTANCE.getMapParsFragment().getText();
	public static String MAP = Chunks.INSTANCE.getMapFragment().getText();
	
	public static String MAP_PARTICLE_PARS = Chunks.INSTANCE.getMapParticleParsFragment().getText();
	public static String MAP_PARTICLE = Chunks.INSTANCE.getMapParticleFragment().getText();
	
	public static String NORMALMAP_PARS = Chunks.INSTANCE.getNormalmapParsFragment().getText();
	
	public static String SHADOWMAP_PARS = Chunks.INSTANCE.getShadowmapParsFragment().getText();
	public static String SHADOWMAP = Chunks.INSTANCE.getShadowmapFragment().getText();
	
	public static String SPECULARMAP_PARS = Chunks.INSTANCE.getSpecularmapParsFragment().getText();
	public static String SPECULARMAP = Chunks.INSTANCE.getSpecularmapFragment().getText();
	
}
