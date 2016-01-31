/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of the Android port of the Parallax project.
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

package org.parallax3d.parallax.system.gl;

public class GLES20Ext {

	public static final int GL_COMPRESSED_RGB_S3TC_DXT1_EXT = 0x83F0;
	public static final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1;
	public static final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2;
	public static final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3;

	public static final int GL_TEXTURE_MAX_ANISOTROPY_EXT = 0x84FE;
	public static final int GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT = 0x84FF;

	public static final int GL_DEPTH_STENCIL = 0x84F9;
	public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;

	public enum List {
		ANGLE_instanced_arrays,
		EXT_blend_minmax,
		EXT_color_buffer_half_float,
		EXT_disjoint_timer_query,
		EXT_frag_depth,
		EXT_sRGB,
		EXT_shader_texture_lod,
		EXT_texture_filter_anisotropic,
		OES_element_index_uint,
		OES_standard_derivatives,
		OES_texture_float,
		OES_texture_float_linear,
		OES_texture_half_float,
		OES_texture_half_float_linear,
		OES_vertex_array_object,
		WEBGL_color_buffer_float,
		WEBGL_compressed_texture_atc,
		WEBGL_compressed_texture_etc1,
		WEBGL_compressed_texture_pvrtc,
		WEBGL_compressed_texture_s3tc,
		WEBGL_debug_renderer_info,
		WEBGL_debug_shaders,
		WEBGL_depth_texture,
		WEBGL_draw_buffers,
		WEBGL_lose_context;

		public static List getValueOf(String test) {
			for(List val: values())
				if(test.equals(val.name()))
					return val;

			return null;
		}
	}
}
