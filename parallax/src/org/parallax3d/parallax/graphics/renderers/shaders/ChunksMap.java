/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.SourceTextResource;

public class ChunksMap {

    static final FastMap<SourceTextResource> chunks = new FastMap<SourceTextResource>(){{
		put("alphamap_fragment", Chunks.INSTANCE.alphamap_fragment());
		put("alphamap_pars_fragment", Chunks.INSTANCE.alphamap_pars_fragment());
		put("alphatest_fragment", Chunks.INSTANCE.alphatest_fragment());
		put("ambient_pars", Chunks.INSTANCE.ambient_pars());
		put("aomap_fragment", Chunks.INSTANCE.aomap_fragment());
		put("aomap_pars_fragment", Chunks.INSTANCE.aomap_pars_fragment());
		put("begin_vertex", Chunks.INSTANCE.begin_vertex());
		put("beginnormal_vertex", Chunks.INSTANCE.beginnormal_vertex());
		put("bsdfs", Chunks.INSTANCE.bsdfs());
		put("bumpmap_pars_fragment", Chunks.INSTANCE.bumpmap_pars_fragment());
		put("color_fragment", Chunks.INSTANCE.color_fragment());
		put("color_pars_fragment", Chunks.INSTANCE.color_pars_fragment());
		put("color_pars_vertex", Chunks.INSTANCE.color_pars_vertex());
		put("color_vertex", Chunks.INSTANCE.color_vertex());
		put("common", Chunks.INSTANCE.common());
		put("cube_uv_reflection_fragment", Chunks.INSTANCE.cube_uv_reflection_fragment());
		put("defaultnormal_vertex", Chunks.INSTANCE.defaultnormal_vertex());
		put("displacementmap_pars_vertex", Chunks.INSTANCE.displacementmap_pars_vertex());
		put("displacementmap_vertex", Chunks.INSTANCE.displacementmap_vertex());
		put("emissivemap_fragment", Chunks.INSTANCE.emissivemap_fragment());
		put("emissivemap_pars_fragment", Chunks.INSTANCE.emissivemap_pars_fragment());
		put("encodings_fragment", Chunks.INSTANCE.encodings_fragment());
		put("encodings_pars_fragment", Chunks.INSTANCE.encodings_pars_fragment());
		put("envmap_fragment", Chunks.INSTANCE.envmap_fragment());
		put("envmap_pars_fragment", Chunks.INSTANCE.envmap_pars_fragment());
		put("envmap_pars_vertex", Chunks.INSTANCE.envmap_pars_vertex());
		put("envmap_vertex", Chunks.INSTANCE.envmap_vertex());
		put("fog_fragment", Chunks.INSTANCE.fog_fragment());
		put("fog_pars_fragment", Chunks.INSTANCE.fog_pars_fragment());
		put("lightmap_fragment", Chunks.INSTANCE.lightmap_fragment());
		put("lightmap_pars_fragment", Chunks.INSTANCE.lightmap_pars_fragment());
		put("lights_lambert_vertex", Chunks.INSTANCE.lights_lambert_vertex());
		put("lights_pars", Chunks.INSTANCE.lights_pars());
		put("lights_phong_fragment", Chunks.INSTANCE.lights_phong_fragment());
		put("lights_phong_pars_fragment", Chunks.INSTANCE.lights_phong_pars_fragment());
		put("lights_phong_pars_vertex", Chunks.INSTANCE.lights_phong_pars_vertex());
		put("lights_phong_vertex", Chunks.INSTANCE.lights_phong_vertex());
		put("lights_standard_fragment", Chunks.INSTANCE.lights_standard_fragment());
		put("lights_standard_pars_fragment", Chunks.INSTANCE.lights_standard_pars_fragment());
		put("lights_template", Chunks.INSTANCE.lights_template());
		put("logdepthbuf_fragment", Chunks.INSTANCE.logdepthbuf_fragment());
		put("logdepthbuf_pars_fragment", Chunks.INSTANCE.logdepthbuf_pars_fragment());
		put("logdepthbuf_pars_vertex", Chunks.INSTANCE.logdepthbuf_pars_vertex());
		put("logdepthbuf_vertex", Chunks.INSTANCE.logdepthbuf_vertex());
		put("map_fragment", Chunks.INSTANCE.map_fragment());
		put("map_pars_fragment", Chunks.INSTANCE.map_pars_fragment());
		put("map_particle_fragment", Chunks.INSTANCE.map_particle_fragment());
		put("map_particle_pars_fragment", Chunks.INSTANCE.map_particle_pars_fragment());
		put("metalnessmap_fragment", Chunks.INSTANCE.metalnessmap_fragment());
		put("metalnessmap_pars_fragment", Chunks.INSTANCE.metalnessmap_pars_fragment());
		put("morphnormal_vertex", Chunks.INSTANCE.morphnormal_vertex());
		put("morphtarget_pars_vertex", Chunks.INSTANCE.morphtarget_pars_vertex());
		put("morphtarget_vertex", Chunks.INSTANCE.morphtarget_vertex());
		put("normal_fragment", Chunks.INSTANCE.normal_fragment());
		put("normalmap_pars_fragment", Chunks.INSTANCE.normalmap_pars_fragment());
		put("premultiplied_alpha_fragment", Chunks.INSTANCE.premultiplied_alpha_fragment());
		put("project_vertex", Chunks.INSTANCE.project_vertex());
		put("roughnessmap_fragment", Chunks.INSTANCE.roughnessmap_fragment());
		put("roughnessmap_pars_fragment", Chunks.INSTANCE.roughnessmap_pars_fragment());
		put("shadowmap_pars_fragment", Chunks.INSTANCE.shadowmap_pars_fragment());
		put("shadowmap_pars_vertex", Chunks.INSTANCE.shadowmap_pars_vertex());
		put("shadowmap_vertex", Chunks.INSTANCE.shadowmap_vertex());
		put("shadowmask_pars_fragment", Chunks.INSTANCE.shadowmask_pars_fragment());
		put("skinbase_vertex", Chunks.INSTANCE.skinbase_vertex());
		put("skinning_pars_vertex", Chunks.INSTANCE.skinning_pars_vertex());
		put("skinning_vertex", Chunks.INSTANCE.skinning_vertex());
		put("skinnormal_vertex", Chunks.INSTANCE.skinnormal_vertex());
		put("specularmap_fragment", Chunks.INSTANCE.specularmap_fragment());
		put("specularmap_pars_fragment", Chunks.INSTANCE.specularmap_pars_fragment());
		put("tonemapping_fragment", Chunks.INSTANCE.tonemapping_fragment());
		put("tonemapping_pars_fragment", Chunks.INSTANCE.tonemapping_pars_fragment());
		put("uv2_pars_fragment", Chunks.INSTANCE.uv2_pars_fragment());
		put("uv2_pars_vertex", Chunks.INSTANCE.uv2_pars_vertex());
		put("uv2_vertex", Chunks.INSTANCE.uv2_vertex());
		put("uv_pars_fragment", Chunks.INSTANCE.uv_pars_fragment());
		put("uv_pars_vertex", Chunks.INSTANCE.uv_pars_vertex());
		put("uv_vertex", Chunks.INSTANCE.uv_vertex());
		put("worldpos_vertex", Chunks.INSTANCE.worldpos_vertex());
    }};

    public static String getChunk(String id) {
        if(chunks.containsKey(id))
            return chunks.get(id).getText();

        Log.warn("ChunksMap.getChunk: can't find chunk by id " + id);
        return null;
    }
}
