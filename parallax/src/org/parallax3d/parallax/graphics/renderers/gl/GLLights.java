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

package org.parallax3d.parallax.graphics.renderers.gl;

import org.parallax3d.parallax.graphics.lights.*;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;

public class GLLights {

    FastMap<FastMap<Uniform>> lights = new FastMap<>();

    public FastMap<Uniform> get(Light light)
    {

        if ( lights.containsKey( String.valueOf(light.getId()) )) {

            return lights.get( String.valueOf(light.getId()) );

        }

        FastMap<Uniform> uniforms = null;

        if(light instanceof DirectionalLight )
            uniforms = new FastMap<Uniform>(){{
                put("direction", new Uniform(Uniform.TYPE.V3, new Vector3()));
                put("color", new Uniform(Uniform.TYPE.C, new Color()));

                put("shadow", new Uniform(Uniform.TYPE.I, false));
                put("shadowBias", new Uniform(Uniform.TYPE.F, .0));
                put("shadowRadius", new Uniform(Uniform.TYPE.F, 1.));
                put("shadowMapSize", new Uniform(Uniform.TYPE.V2, new Vector2()));
            }};

        else if(light instanceof SpotLight)
            uniforms = new FastMap<Uniform>(){{
                put("position", new Uniform(Uniform.TYPE.V3, new Vector3()));
                put("direction", new Uniform(Uniform.TYPE.V3, new Vector3()));
                put("color", new Uniform(Uniform.TYPE.C, new Color()));
                put("distance", new Uniform(Uniform.TYPE.F, 0.));
                put("coneCos", new Uniform(Uniform.TYPE.F, 0.));
                put("penumbraCos", new Uniform(Uniform.TYPE.F, 0.));
                put("decay", new Uniform(Uniform.TYPE.F, 0.));

                put("shadow", new Uniform(Uniform.TYPE.I, false));
                put("shadowBias", new Uniform(Uniform.TYPE.F, .0));
                put("shadowRadius", new Uniform(Uniform.TYPE.F, 1.));
                put("shadowMapSize", new Uniform(Uniform.TYPE.V2, new Vector2()));
            }};

        else if(light instanceof PointLight)
            uniforms = new FastMap<Uniform>(){{
                put("position", new Uniform(Uniform.TYPE.V3, new Vector3()));
                put("color", new Uniform(Uniform.TYPE.C, new Color()));
                put("distance", new Uniform(Uniform.TYPE.F, 0.));
                put("decay", new Uniform(Uniform.TYPE.F, 0.));

                put("shadow", new Uniform(Uniform.TYPE.I, false));
                put("shadowBias", new Uniform(Uniform.TYPE.F, .0));
                put("shadowRadius", new Uniform(Uniform.TYPE.F, 1.));
                put("shadowMapSize", new Uniform(Uniform.TYPE.V2, new Vector2()));
            }};

        else if(light instanceof HemisphereLight)
            uniforms = new FastMap<Uniform>(){{
                put("distance", new Uniform(Uniform.TYPE.F, 0.));
                put("skyColor", new Uniform(Uniform.TYPE.C, new Color()));
                put("groundColor", new Uniform(Uniform.TYPE.C, new Color()));
            }};

        lights.put(String.valueOf(light.getId()), uniforms);

        return uniforms;

    }
}
