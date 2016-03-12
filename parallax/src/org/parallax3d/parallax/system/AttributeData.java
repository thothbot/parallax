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
package org.parallax3d.parallax.system;

import org.parallax3d.parallax.system.gl.arrays.TypeArray;

public abstract class AttributeData extends UuidObject {

    public static class UpdateRange {
        public int offset = 0;
        public int count = -1;
    }

    protected TypeArray array;
    boolean dynamic = false;

    int version = 0;

    UpdateRange updateRange = new UpdateRange();

    public AttributeData(TypeArray array) {
        this.array = array;
    }

    public TypeArray getArray() {
        return this.array;
    }

    public void setArray(TypeArray array) {
        this.array = array;
    }

    public int getVersion() {
        return version;
    }

    public void setNeedsUpdate( boolean value ) {

        if ( value ) this.version ++;

    }

    public AttributeData set( TypeArray value ) {

        this.array.set(value);

        return this;

    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic( boolean value ) {

        this.dynamic = value;

    }

    public UpdateRange getUpdateRange() {
        return updateRange;
    }
}
