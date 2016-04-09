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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;

/**
 * @author benaadams / https://twitter.com/ben_a_adams
 */
@ThreejsObject("THREE.InstancedBufferAttribute")
public class InstancedBufferAttribute extends BufferAttribute {

    int meshPerAttribute;

    public InstancedBufferAttribute(TypeArray array, int itemSize) {
        this(array, itemSize, 1);
    }

    public InstancedBufferAttribute(TypeArray array, int itemSize, int meshPerAttribute) {
        super(array, itemSize);

        this.meshPerAttribute = meshPerAttribute;
    }

    public int getMeshPerAttribute() {
        return meshPerAttribute;
    }

    public void setMeshPerAttribute(int meshPerAttribute) {
        this.meshPerAttribute = meshPerAttribute;
    }

    @Override
    public InstancedBufferAttribute clone() {
        return new InstancedBufferAttribute(this.getArray(), this.getItemSize()).copy( this );
    }

    public InstancedBufferAttribute copy( InstancedBufferAttribute source ) {

        super.copy(source);

        this.meshPerAttribute = source.meshPerAttribute;

        return this;

    }
}
