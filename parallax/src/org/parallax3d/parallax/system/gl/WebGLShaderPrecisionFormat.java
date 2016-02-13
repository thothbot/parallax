/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
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

package org.parallax3d.parallax.system.gl;

import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.system.gl.enums.ShaderPrecisionSpecifiedTypes;
import org.parallax3d.parallax.system.gl.enums.Shaders;

public final class WebGLShaderPrecisionFormat {

	Int32Array precision = Int32Array.create(3);

	public WebGLShaderPrecisionFormat(GL20 gl, Shaders shaderType, ShaderPrecisionSpecifiedTypes precisionType) {
		gl.glGetShaderPrecisionFormat(shaderType.getValue(), precisionType.getValue(), precision.getTypedBuffer(), precision.getTypedBuffer());
	}
	
	public int getRangeMin() {
		return this.precision.get(0);
	}
	
	public int getRangeMax() {
		return this.precision.get(0);
	}

	public int getPrecision() {
		return this.precision.get(0);
	}

}
