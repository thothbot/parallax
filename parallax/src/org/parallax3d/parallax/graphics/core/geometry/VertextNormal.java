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
package org.parallax3d.parallax.graphics.core.geometry;

import org.parallax3d.parallax.math.Vector3;

public class VertextNormal {

    Vector3 a = new Vector3();
    Vector3 b = new Vector3();
    Vector3 c = new Vector3();

    public Vector3 getA() {
        return a;
    }

    public void setA(Vector3 a) {
        this.a = a;
    }

    public Vector3 getB() {
        return b;
    }

    public void setB(Vector3 b) {
        this.b = b;
    }

    public Vector3 getC() {
        return c;
    }

    public void setC(Vector3 c) {
        this.c = c;
    }
}
