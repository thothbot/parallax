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
package org.parallax3d.parallax.math;

public class Constants {

    public static double DELTA = 0.00001;

    public final static double x = 2;
    public final static double y = 3;
    public final static double z = 4;
    public final static double w = 5;

    public final static Vector2 negInf2 = new Vector2( Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY );
    public final static Vector2 posInf2 = new Vector2( Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY );

    public final static Vector2 zero2 = new Vector2();
    public final static Vector2 one2 = new Vector2( 1, 1 );
    public final static Vector2 two2 = new Vector2( 2, 2 );

    public final static Vector3 negInf3 = new Vector3( Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY );
    public final static Vector3 posInf3 = new Vector3( Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY );

    public final static Vector3 zero3 = new Vector3();
    public final static Vector3 one3 = new Vector3( 1, 1, 1 );
    public final static Vector3 two3 = new Vector3( 2, 2, 2 );
}
