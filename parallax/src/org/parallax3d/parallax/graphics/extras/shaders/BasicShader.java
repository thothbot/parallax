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
package org.parallax3d.parallax.graphics.extras.shaders;

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author mrdoob / http://www.mrdoob.com
 *
 * Simple test shader
 */
@ThreejsObject("THREE.BasicShader")
public class BasicShader extends Shader {

    interface Resources extends DefaultResources
    {
        Resources INSTANCE = SourceBundleProxy.create(Resources.class);

        @Source("source/basic_vert.glsl")
        SourceTextResource getVertexShader();

        @Source("source/basic_frag.glsl")
        SourceTextResource getFragmentShader();
    }

    public BasicShader()
    {
        super(Resources.INSTANCE);
    }

    @Override
    protected void initUniforms()
    {
    }
}