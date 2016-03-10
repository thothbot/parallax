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

import org.parallax3d.parallax.graphics.core.IDObject;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.WebGLProperties")
public class GLProperties {

    FastMap<Object> properties;

    public GLProperties() {

        clear();

    }

    public Object get( IDObject object ) {

        String uuid = object.getUUID();

        if(!properties.containsKey(uuid))
            properties.put(uuid, null);

        return properties.get( uuid );

    }

    public void delete( IDObject object ) {

        String uuid = object.getUUID();

        if(properties.containsKey(uuid))
            properties.remove(uuid);
    }

    public void clear() {
        properties = new FastMap<>();
    }
}
