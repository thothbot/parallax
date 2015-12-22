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
 * Chunks used in Fragment Shader
 * @author thothbot
 *
 */
public class ChunksFragmentShader
{
	
	public static String ALPHAMAP_PARS = Chunks.getAlphamapParsFragment();
	public static String ALPHAMAP = Chunks.getAlphamapFragment();
	
	public static String ALPHA_TEST = Chunks.getAlphatestFragment();

	public static String BUMPMAP_PARS = Chunks.getBumpmapParsFragment();
	
	public static String COLOR_PARS = Chunks.getColorParsFragment();
	public static String COLOR = Chunks.getColorFragment();
	
	public static String ENVMAP_PARS = Chunks.getEnvmapParsFragment();
	public static String ENVMAP = Chunks.getEnvmapFragment();
	
	public static String FOG_PARS = Chunks.getFogParsFragment();
	public static String FOG = Chunks.getFogFragment();
	
	public static String LIGHTMAP_PARS = Chunks.getLightmapParsFragment();
	public static String LIGHTMAP = Chunks.getLightmapFragment();
	
	public static String LIGHTS_PONG_PARS = Chunks.getLightsPhongParsFragment();
	public static String LIGHTS_PONG = Chunks.getLightsPhongFragment();
	
	public static String LINEAR_TO_GAMMA = Chunks.getLinearToGammaFragment();
	
	public static String LOGDEPTHBUF_PAR = Chunks.getLogdepthbufParFragment();
	public static String LOGDEPTHBUF = Chunks.getLogdepthbufFragment();
	
	public static String MAP_PARS = Chunks.getMapParsFragment();
	public static String MAP = Chunks.getMapFragment();
	
	public static String MAP_PARTICLE_PARS = Chunks.getMapParticleParsFragment();
	public static String MAP_PARTICLE = Chunks.getMapParticleFragment();
	
	public static String NORMALMAP_PARS = Chunks.getNormalmapParsFragment();
	
	public static String SHADOWMAP_PARS = Chunks.getShadowmapParsFragment();
	public static String SHADOWMAP = Chunks.getShadowmapFragment();
	
	public static String SPECULARMAP_PARS = Chunks.getSpecularmapParsFragment();
	public static String SPECULARMAP = Chunks.getSpecularmapFragment();
	
}
