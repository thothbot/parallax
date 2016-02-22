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

import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceBundle;
import org.parallax3d.parallax.system.SourceTextResource;

/**
 * Source for all chunks.
 * 
 * @author thothbot
 *
 */
public interface Chunks extends SourceBundle
{
	Chunks INSTANCE = SourceBundleProxy.create( Chunks.class );

    @Source("chunk/alphamap_fragment.glsl")
    SourceTextResource getAlphamapFragment();

    @Source("chunk/alphamap_pars_fragment.glsl")
    SourceTextResource getAlphamapParsFragment();

    @Source("chunk/alphatest_fragment.glsl")
    SourceTextResource getAlphatestFragment();

    @Source("chunk/bumpmap_pars_fragment.glsl")
    SourceTextResource getBumpmapParsFragment();

    @Source("chunk/color_fragment.glsl")
    SourceTextResource getColorFragment();

    @Source("chunk/color_pars_fragment.glsl")
    SourceTextResource getColorParsFragment();

    @Source("chunk/color_pars_vertex.glsl")
    SourceTextResource getColorParsVertex();

    @Source("chunk/color_vertex.glsl")
    SourceTextResource getColorVertex();

    @Source("chunk/defaultnormal_vertex.glsl")
    SourceTextResource getDefaultNormalVertex();

    @Source("chunk/default_vertex.glsl")
    SourceTextResource getDefaultVertex();

    @Source("chunk/envmap_fragment.glsl")
    SourceTextResource getEnvmapFragment();

    @Source("chunk/envmap_pars_fragment.glsl")
    SourceTextResource getEnvmapParsFragment();

    @Source("chunk/envmap_pars_vertex.glsl")
    SourceTextResource getEnvmapParsVertex();

    @Source("chunk/envmap_vertex.glsl")
    SourceTextResource getEnvmapVertex();

    @Source("chunk/fog_fragment.glsl")
    SourceTextResource getFogFragment();

    @Source("chunk/fog_pars_fragment.glsl")
    SourceTextResource getFogParsFragment();

    @Source("chunk/lightmap_fragment.glsl")
    SourceTextResource getLightmapFragment();

    @Source("chunk/lightmap_pars_fragment.glsl")
    SourceTextResource getLightmapParsFragment();

    @Source("chunk/lightmap_pars_vertex.glsl")
    SourceTextResource getLightmapParsVertex();

    @Source("chunk/lightmap_vertex.glsl")
    SourceTextResource getLightmapVertex();

    @Source("chunk/lights_lambert_pars_vertex.glsl")
    SourceTextResource getLightsLambertParsVertex();

    @Source("chunk/lights_lambert_vertex.glsl")
    SourceTextResource getLightsLambertVertex();

    @Source("chunk/lights_phong_fragment.glsl")
    SourceTextResource getLightsPhongFragment();

    @Source("chunk/lights_phong_pars_fragment.glsl")
    SourceTextResource getLightsPhongParsFragment();

    @Source("chunk/lights_phong_pars_vertex.glsl")
    SourceTextResource getLightsPhongParsVertex();

    @Source("chunk/lights_phong_vertex.glsl")
    SourceTextResource getLightsPhongVertex();

    @Source("chunk/linear_to_gamma_fragment.glsl")
    SourceTextResource getLinearToGammaFragment();

    @Source("chunk/logdepthbuf_fragment.glsl")
    SourceTextResource getLogdepthbufFragment();

    @Source("chunk/logdepthbuf_par_fragment.glsl")
    SourceTextResource getLogdepthbufParFragment();

    @Source("chunk/logdepthbuf_par_vertex.glsl")
    SourceTextResource getLogdepthbufParVertex();

    @Source("chunk/logdepthbuf_vertex.glsl")
    SourceTextResource getLogdepthbufVertex();

    @Source("chunk/map_fragment.glsl")
    SourceTextResource getMapFragment();

    @Source("chunk/map_pars_fragment.glsl")
    SourceTextResource getMapParsFragment();

    @Source("chunk/map_pars_vertex.glsl")
    SourceTextResource getMapParsVertex();

    @Source("chunk/map_particle_fragment.glsl")
    SourceTextResource getMapParticleFragment();

    @Source("chunk/map_particle_pars_fragment.glsl")
    SourceTextResource getMapParticleParsFragment();

    @Source("chunk/map_vertex.glsl")
    SourceTextResource getMapVertex();

    @Source("chunk/morphnormal_vertex.glsl")
    SourceTextResource getMorphnormalVertex();

    @Source("chunk/morphtarget_pars_vertex.glsl")
    SourceTextResource getMorphtargetParsVertex();

    @Source("chunk/morphtarget_vertex.glsl")
    SourceTextResource getMorphtargetVertex();

    @Source("chunk/normalmap_pars_fragment.glsl")
    SourceTextResource getNormalmapParsFragment();

    @Source("chunk/shadowmap_fragment.glsl")
    SourceTextResource getShadowmapFragment();

    @Source("chunk/shadowmap_pars_fragment.glsl")
    SourceTextResource getShadowmapParsFragment();

    @Source("chunk/shadowmap_pars_vertex.glsl")
    SourceTextResource getShadowmapParsVertex();

    @Source("chunk/shadowmap_vertex.glsl")
    SourceTextResource getShadowmapVertex();

    @Source("chunk/skinning_pars_vertex.glsl")
    SourceTextResource getSkinningParsVertex();

    @Source("chunk/skinning_vertex.glsl")
    SourceTextResource getSkinningVertex();

    @Source("chunk/skinbase_vertex.glsl")
    SourceTextResource getSkinBaseVertex();

    @Source("chunk/skinnormal_vertex.glsl")
    SourceTextResource getSkinNormalVertex();

    @Source("chunk/specularmap_fragment.glsl")
    SourceTextResource getSpecularmapFragment();

    @Source("chunk/specularmap_pars_fragment.glsl")
    SourceTextResource getSpecularmapParsFragment();

    @Source("chunk/worldpos_vertex.glsl")
    SourceTextResource getWorldposVertex();
}
