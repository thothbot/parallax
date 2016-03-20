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
import org.parallax3d.parallax.system.SourceBundle;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

public class Chunks {

	public interface Resources extends SourceBundle
	{
		Resources INSTANCE = SourceBundleProxy.create( Resources.class );

		@Source("chunk/alphamap_fragment.glsl")
		SourceTextResource alphamap_fragment();

		@Source("chunk/alphamap_pars_fragment.glsl")
		SourceTextResource alphamap_pars_fragment();

		@Source("chunk/alphatest_fragment.glsl")
		SourceTextResource alphatest_fragment();

		@Source("chunk/ambient_pars.glsl")
		SourceTextResource ambient_pars();

		@Source("chunk/aomap_fragment.glsl")
		SourceTextResource aomap_fragment();

		@Source("chunk/aomap_pars_fragment.glsl")
		SourceTextResource aomap_pars_fragment();

		@Source("chunk/begin_vertex.glsl")
		SourceTextResource begin_vertex();

		@Source("chunk/beginnormal_vertex.glsl")
		SourceTextResource beginnormal_vertex();

		@Source("chunk/bsdfs.glsl")
		SourceTextResource bsdfs();

		@Source("chunk/bumpmap_pars_fragment.glsl")
		SourceTextResource bumpmap_pars_fragment();

		@Source("chunk/color_fragment.glsl")
		SourceTextResource color_fragment();

		@Source("chunk/color_pars_fragment.glsl")
		SourceTextResource color_pars_fragment();

		@Source("chunk/color_pars_vertex.glsl")
		SourceTextResource color_pars_vertex();

		@Source("chunk/color_vertex.glsl")
		SourceTextResource color_vertex();

		@Source("chunk/common.glsl")
		SourceTextResource common();

		@Source("chunk/cube_uv_reflection_fragment.glsl")
		SourceTextResource cube_uv_reflection_fragment();

		@Source("chunk/defaultnormal_vertex.glsl")
		SourceTextResource defaultnormal_vertex();

		@Source("chunk/displacementmap_pars_vertex.glsl")
		SourceTextResource displacementmap_pars_vertex();

		@Source("chunk/displacementmap_vertex.glsl")
		SourceTextResource displacementmap_vertex();

		@Source("chunk/emissivemap_fragment.glsl")
		SourceTextResource emissivemap_fragment();

		@Source("chunk/emissivemap_pars_fragment.glsl")
		SourceTextResource emissivemap_pars_fragment();

		@Source("chunk/encodings_fragment.glsl")
		SourceTextResource encodings_fragment();

		@Source("chunk/encodings_pars_fragment.glsl")
		SourceTextResource encodings_pars_fragment();

		@Source("chunk/envmap_fragment.glsl")
		SourceTextResource envmap_fragment();

		@Source("chunk/envmap_pars_fragment.glsl")
		SourceTextResource envmap_pars_fragment();

		@Source("chunk/envmap_pars_vertex.glsl")
		SourceTextResource envmap_pars_vertex();

		@Source("chunk/envmap_vertex.glsl")
		SourceTextResource envmap_vertex();

		@Source("chunk/fog_fragment.glsl")
		SourceTextResource fog_fragment();

		@Source("chunk/fog_pars_fragment.glsl")
		SourceTextResource fog_pars_fragment();

		@Source("chunk/lightmap_fragment.glsl")
		SourceTextResource lightmap_fragment();

		@Source("chunk/lightmap_pars_fragment.glsl")
		SourceTextResource lightmap_pars_fragment();

		@Source("chunk/lights_lambert_vertex.glsl")
		SourceTextResource lights_lambert_vertex();

		@Source("chunk/lights_pars.glsl")
		SourceTextResource lights_pars();

		@Source("chunk/lights_phong_fragment.glsl")
		SourceTextResource lights_phong_fragment();

		@Source("chunk/lights_phong_pars_fragment.glsl")
		SourceTextResource lights_phong_pars_fragment();

		@Source("chunk/lights_phong_pars_vertex.glsl")
		SourceTextResource lights_phong_pars_vertex();

		@Source("chunk/lights_phong_vertex.glsl")
		SourceTextResource lights_phong_vertex();

		@Source("chunk/lights_standard_fragment.glsl")
		SourceTextResource lights_standard_fragment();

		@Source("chunk/lights_standard_pars_fragment.glsl")
		SourceTextResource lights_standard_pars_fragment();

		@Source("chunk/lights_template.glsl")
		SourceTextResource lights_template();

		@Source("chunk/logdepthbuf_fragment.glsl")
		SourceTextResource logdepthbuf_fragment();

		@Source("chunk/logdepthbuf_pars_fragment.glsl")
		SourceTextResource logdepthbuf_pars_fragment();

		@Source("chunk/logdepthbuf_pars_vertex.glsl")
		SourceTextResource logdepthbuf_pars_vertex();

		@Source("chunk/logdepthbuf_vertex.glsl")
		SourceTextResource logdepthbuf_vertex();

		@Source("chunk/map_fragment.glsl")
		SourceTextResource map_fragment();

		@Source("chunk/map_pars_fragment.glsl")
		SourceTextResource map_pars_fragment();

		@Source("chunk/map_particle_fragment.glsl")
		SourceTextResource map_particle_fragment();

		@Source("chunk/map_particle_pars_fragment.glsl")
		SourceTextResource map_particle_pars_fragment();

		@Source("chunk/metalnessmap_fragment.glsl")
		SourceTextResource metalnessmap_fragment();

		@Source("chunk/metalnessmap_pars_fragment.glsl")
		SourceTextResource metalnessmap_pars_fragment();

		@Source("chunk/morphnormal_vertex.glsl")
		SourceTextResource morphnormal_vertex();

		@Source("chunk/morphtarget_pars_vertex.glsl")
		SourceTextResource morphtarget_pars_vertex();

		@Source("chunk/morphtarget_vertex.glsl")
		SourceTextResource morphtarget_vertex();

		@Source("chunk/normal_fragment.glsl")
		SourceTextResource normal_fragment();

		@Source("chunk/normalmap_pars_fragment.glsl")
		SourceTextResource normalmap_pars_fragment();

		@Source("chunk/premultiplied_alpha_fragment.glsl")
		SourceTextResource premultiplied_alpha_fragment();

		@Source("chunk/project_vertex.glsl")
		SourceTextResource project_vertex();

		@Source("chunk/roughnessmap_fragment.glsl")
		SourceTextResource roughnessmap_fragment();

		@Source("chunk/roughnessmap_pars_fragment.glsl")
		SourceTextResource roughnessmap_pars_fragment();

		@Source("chunk/shadowmap_pars_fragment.glsl")
		SourceTextResource shadowmap_pars_fragment();

		@Source("chunk/shadowmap_pars_vertex.glsl")
		SourceTextResource shadowmap_pars_vertex();

		@Source("chunk/shadowmap_vertex.glsl")
		SourceTextResource shadowmap_vertex();

		@Source("chunk/shadowmask_pars_fragment.glsl")
		SourceTextResource shadowmask_pars_fragment();

		@Source("chunk/skinbase_vertex.glsl")
		SourceTextResource skinbase_vertex();

		@Source("chunk/skinning_pars_vertex.glsl")
		SourceTextResource skinning_pars_vertex();

		@Source("chunk/skinning_vertex.glsl")
		SourceTextResource skinning_vertex();

		@Source("chunk/skinnormal_vertex.glsl")
		SourceTextResource skinnormal_vertex();

		@Source("chunk/specularmap_fragment.glsl")
		SourceTextResource specularmap_fragment();

		@Source("chunk/specularmap_pars_fragment.glsl")
		SourceTextResource specularmap_pars_fragment();

		@Source("chunk/tonemapping_fragment.glsl")
		SourceTextResource tonemapping_fragment();

		@Source("chunk/tonemapping_pars_fragment.glsl")
		SourceTextResource tonemapping_pars_fragment();

		@Source("chunk/uv2_pars_fragment.glsl")
		SourceTextResource uv2_pars_fragment();

		@Source("chunk/uv2_pars_vertex.glsl")
		SourceTextResource uv2_pars_vertex();

		@Source("chunk/uv2_vertex.glsl")
		SourceTextResource uv2_vertex();

		@Source("chunk/uv_pars_fragment.glsl")
		SourceTextResource uv_pars_fragment();

		@Source("chunk/uv_pars_vertex.glsl")
		SourceTextResource uv_pars_vertex();

		@Source("chunk/uv_vertex.glsl")
		SourceTextResource uv_vertex();

		@Source("chunk/worldpos_vertex.glsl")
		SourceTextResource worldpos_vertex();
	}

    static final FastMap<SourceTextResource> chunks = new FastMap<SourceTextResource>(){{
		put("alphamap_fragment", Resources.INSTANCE.alphamap_fragment());
		put("alphamap_pars_fragment", Resources.INSTANCE.alphamap_pars_fragment());
		put("alphatest_fragment", Resources.INSTANCE.alphatest_fragment());
		put("ambient_pars", Resources.INSTANCE.ambient_pars());
		put("aomap_fragment", Resources.INSTANCE.aomap_fragment());
		put("aomap_pars_fragment", Resources.INSTANCE.aomap_pars_fragment());
		put("begin_vertex", Resources.INSTANCE.begin_vertex());
		put("beginnormal_vertex", Resources.INSTANCE.beginnormal_vertex());
		put("bsdfs", Resources.INSTANCE.bsdfs());
		put("bumpmap_pars_fragment", Resources.INSTANCE.bumpmap_pars_fragment());
		put("color_fragment", Resources.INSTANCE.color_fragment());
		put("color_pars_fragment", Resources.INSTANCE.color_pars_fragment());
		put("color_pars_vertex", Resources.INSTANCE.color_pars_vertex());
		put("color_vertex", Resources.INSTANCE.color_vertex());
		put("common", Resources.INSTANCE.common());
		put("cube_uv_reflection_fragment", Resources.INSTANCE.cube_uv_reflection_fragment());
		put("defaultnormal_vertex", Resources.INSTANCE.defaultnormal_vertex());
		put("displacementmap_pars_vertex", Resources.INSTANCE.displacementmap_pars_vertex());
		put("displacementmap_vertex", Resources.INSTANCE.displacementmap_vertex());
		put("emissivemap_fragment", Resources.INSTANCE.emissivemap_fragment());
		put("emissivemap_pars_fragment", Resources.INSTANCE.emissivemap_pars_fragment());
		put("encodings_fragment", Resources.INSTANCE.encodings_fragment());
		put("encodings_pars_fragment", Resources.INSTANCE.encodings_pars_fragment());
		put("envmap_fragment", Resources.INSTANCE.envmap_fragment());
		put("envmap_pars_fragment", Resources.INSTANCE.envmap_pars_fragment());
		put("envmap_pars_vertex", Resources.INSTANCE.envmap_pars_vertex());
		put("envmap_vertex", Resources.INSTANCE.envmap_vertex());
		put("fog_fragment", Resources.INSTANCE.fog_fragment());
		put("fog_pars_fragment", Resources.INSTANCE.fog_pars_fragment());
		put("lightmap_fragment", Resources.INSTANCE.lightmap_fragment());
		put("lightmap_pars_fragment", Resources.INSTANCE.lightmap_pars_fragment());
		put("lights_lambert_vertex", Resources.INSTANCE.lights_lambert_vertex());
		put("lights_pars", Resources.INSTANCE.lights_pars());
		put("lights_phong_fragment", Resources.INSTANCE.lights_phong_fragment());
		put("lights_phong_pars_fragment", Resources.INSTANCE.lights_phong_pars_fragment());
		put("lights_phong_pars_vertex", Resources.INSTANCE.lights_phong_pars_vertex());
		put("lights_phong_vertex", Resources.INSTANCE.lights_phong_vertex());
		put("lights_standard_fragment", Resources.INSTANCE.lights_standard_fragment());
		put("lights_standard_pars_fragment", Resources.INSTANCE.lights_standard_pars_fragment());
		put("lights_template", Resources.INSTANCE.lights_template());
		put("logdepthbuf_fragment", Resources.INSTANCE.logdepthbuf_fragment());
		put("logdepthbuf_pars_fragment", Resources.INSTANCE.logdepthbuf_pars_fragment());
		put("logdepthbuf_pars_vertex", Resources.INSTANCE.logdepthbuf_pars_vertex());
		put("logdepthbuf_vertex", Resources.INSTANCE.logdepthbuf_vertex());
		put("map_fragment", Resources.INSTANCE.map_fragment());
		put("map_pars_fragment", Resources.INSTANCE.map_pars_fragment());
		put("map_particle_fragment", Resources.INSTANCE.map_particle_fragment());
		put("map_particle_pars_fragment", Resources.INSTANCE.map_particle_pars_fragment());
		put("metalnessmap_fragment", Resources.INSTANCE.metalnessmap_fragment());
		put("metalnessmap_pars_fragment", Resources.INSTANCE.metalnessmap_pars_fragment());
		put("morphnormal_vertex", Resources.INSTANCE.morphnormal_vertex());
		put("morphtarget_pars_vertex", Resources.INSTANCE.morphtarget_pars_vertex());
		put("morphtarget_vertex", Resources.INSTANCE.morphtarget_vertex());
		put("normal_fragment", Resources.INSTANCE.normal_fragment());
		put("normalmap_pars_fragment", Resources.INSTANCE.normalmap_pars_fragment());
		put("premultiplied_alpha_fragment", Resources.INSTANCE.premultiplied_alpha_fragment());
		put("project_vertex", Resources.INSTANCE.project_vertex());
		put("roughnessmap_fragment", Resources.INSTANCE.roughnessmap_fragment());
		put("roughnessmap_pars_fragment", Resources.INSTANCE.roughnessmap_pars_fragment());
		put("shadowmap_pars_fragment", Resources.INSTANCE.shadowmap_pars_fragment());
		put("shadowmap_pars_vertex", Resources.INSTANCE.shadowmap_pars_vertex());
		put("shadowmap_vertex", Resources.INSTANCE.shadowmap_vertex());
		put("shadowmask_pars_fragment", Resources.INSTANCE.shadowmask_pars_fragment());
		put("skinbase_vertex", Resources.INSTANCE.skinbase_vertex());
		put("skinning_pars_vertex", Resources.INSTANCE.skinning_pars_vertex());
		put("skinning_vertex", Resources.INSTANCE.skinning_vertex());
		put("skinnormal_vertex", Resources.INSTANCE.skinnormal_vertex());
		put("specularmap_fragment", Resources.INSTANCE.specularmap_fragment());
		put("specularmap_pars_fragment", Resources.INSTANCE.specularmap_pars_fragment());
		put("tonemapping_fragment", Resources.INSTANCE.tonemapping_fragment());
		put("tonemapping_pars_fragment", Resources.INSTANCE.tonemapping_pars_fragment());
		put("uv2_pars_fragment", Resources.INSTANCE.uv2_pars_fragment());
		put("uv2_pars_vertex", Resources.INSTANCE.uv2_pars_vertex());
		put("uv2_vertex", Resources.INSTANCE.uv2_vertex());
		put("uv_pars_fragment", Resources.INSTANCE.uv_pars_fragment());
		put("uv_pars_vertex", Resources.INSTANCE.uv_pars_vertex());
		put("uv_vertex", Resources.INSTANCE.uv_vertex());
		put("worldpos_vertex", Resources.INSTANCE.worldpos_vertex());
    }};

    public static String getChunk(String id) {
        if(chunks.containsKey(id))
            return chunks.get(id).getText();

        Log.warn("Chunks.getChunk: can't find chunk by id " + id);
        return null;
    }
}
