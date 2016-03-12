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

package org.parallax3d.parallax.graphics.renderers.gl;

import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GLExtensions {

	static final List<GLES20Ext.List> extensions = new ArrayList<>();

	public static boolean check(GL20 gl, GLES20Ext.List id) {

		if(extensions.size() == 0)
		{
			String vals = gl.glGetString(GL20.GL_EXTENSIONS);
			if(vals != null)
				for(String val: vals.split(" "))
					extensions.addAll(Arrays.asList( GLES20Ext.List.valueOf(val).getSynonyms() ));
		}

		return extensions.size() > 0 && extensions.contains( id );
	}
}
