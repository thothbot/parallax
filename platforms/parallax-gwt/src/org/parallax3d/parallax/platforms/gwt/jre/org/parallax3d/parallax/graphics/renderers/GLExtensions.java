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

package org.parallax3d.parallax.graphics.renderers;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.webgl.client.WebGLExtension;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.platforms.gwt.GwtGL20;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;

import java.util.ArrayList;
import java.util.List;

public final class GLExtensions {

	static final List<GLES20Ext.List> extensions = new ArrayList<>();

	public static boolean isSupported(GL20 gl, GLES20Ext.List id) {

		if(extensions.size() == 0)
		{
			JsArrayString supportedExts = ((GwtGL20)gl).getWebGLRenderingContext().getSupportedExtensions();
			String[] outSupportedExts = new String[supportedExts.length()];
			Log.error(supportedExts);
			for (int i = 0; i < outSupportedExts.length; i++) {
				GLES20Ext.List val = GLES20Ext.List.getValueOf(supportedExts.get(i));
				if(val != null)
					extensions.add(val);
			}
		}

		return extensions.size() > 0 && extensions.contains( id );
	}
}
