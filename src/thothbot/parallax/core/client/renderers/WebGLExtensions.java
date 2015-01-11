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

package thothbot.parallax.core.client.renderers;

import thothbot.parallax.core.client.gl2.WebGLExtension;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.shared.Log;

public final class WebGLExtensions {
	
	public static enum Id {
		OES_texture_float,
		OES_texture_float_linear,
		OES_standard_derivatives,
		EXT_texture_filter_anisotropic,
		WEBGL_compressed_texture_s3tc,
		WEBGL_compressed_texture_pvrtc,
		OES_element_index_uint,
		EXT_blend_minmax,
		EXT_frag_depth
	};

	public static WebGLExtension get(WebGLRenderingContext gl, Id id) {
		
		WebGLExtension extension = null;
		
		switch ( id ) {
		
			case OES_texture_float:
				extension = gl.getExtension( "OES_texture_float" );
				break;
	
			case OES_texture_float_linear:
				extension = gl.getExtension( "OES_texture_float_linear" );
				break;
	
			case OES_standard_derivatives:
				extension = gl.getExtension( "OES_standard_derivatives" );
				break;
	
			case EXT_texture_filter_anisotropic:
				extension = gl.getExtension( "EXT_texture_filter_anisotropic" );
				if(extension == null)
					extension = gl.getExtension( "MOZ_EXT_texture_filter_anisotropic" ); 
				if(extension == null)
					extension = gl.getExtension( "WEBKIT_EXT_texture_filter_anisotropic" );
				break;
	
			case WEBGL_compressed_texture_s3tc:
				extension = gl.getExtension( "WEBGL_compressed_texture_s3tc" );
				if(extension == null)
					extension = gl.getExtension( "MOZ_WEBGL_compressed_texture_s3tc" );
				if(extension == null)
					extension = gl.getExtension( "WEBKIT_WEBGL_compressed_texture_s3tc" );
				break;
	
			case WEBGL_compressed_texture_pvrtc:
				extension = gl.getExtension( "WEBGL_compressed_texture_pvrtc" );
				if(extension == null)
					extension = gl.getExtension( "WEBKIT_WEBGL_compressed_texture_pvrtc" );
				break;
	
			case OES_element_index_uint:
				extension = gl.getExtension( "OES_element_index_uint" );
				break;
	
			case EXT_blend_minmax:
				extension = gl.getExtension( "EXT_blend_minmax" );
				break;
	
			case EXT_frag_depth:
				extension = gl.getExtension( "EXT_frag_depth" );
				break;
	
		}
		
		if ( extension == null ) {

			Log.warn( "WebGLRenderer: " + id.toString() + " extension not supported." );

		}
		
		return extension;

	}
	
}
