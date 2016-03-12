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

package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author benaadams / https://twitter.com/ben_a_adams
 */
@ThreejsObject("THREE.InstancedBufferGeometry")
public class InstancedBufferGeometry extends BufferGeometry {

    public static class Group {
        public int start;
        public int count;
        public List<Object> instances;

        public Group(int start, int count, List<Object> instances) {
            this.start = start;
            this.count = count;
            this.instances = instances;
        }
    }

    List<Group> groups = new ArrayList<>();
    int maxInstancedCount;

    public int getMaxInstancedCount() {
        return maxInstancedCount;
    }

    public InstancedBufferGeometry addGroup( int start, int count, List<Object> instances )
    {
        this.groups.add( new Group(start, count, instances) );
        return this;
    }

    public InstancedBufferGeometry copy(InstancedBufferGeometry source) {
//        int index = source.index;

        FastMap<BufferAttribute> attributes = source.getAttributes();

        for (Map.Entry<String,BufferAttribute> entry : attributes.entrySet() ) {

            this.addAttribute( entry.getKey(), entry.getValue().clone() );

        }

        this.groups = new ArrayList<>( source.groups );

        return this;

    }

    @Override
    public InstancedBufferGeometry clone() {
        InstancedBufferGeometry instance = (InstancedBufferGeometry) super.clone();

        instance.groups = new ArrayList<>( this.groups );

        return instance;
    }
}
