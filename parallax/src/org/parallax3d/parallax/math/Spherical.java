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

import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author bhouston / http://clara.io
 * @author WestLangley / http://github.com/WestLangley
 *
 * Ref: https://en.wikipedia.org/wiki/Spherical_coordinate_system
 *
 * The poles (phi) are at the positive and negative y axis.
 * The equator starts at positive z.
 */
@ThreejsObject("THREE.Spherical")
public class Spherical {

    double radius = 1.;

    //up / down towards top and bottom pole
    double phi = 0.;

    // around the equator of the sphere
    double theta = 0.;

    public Spherical() {
        this(1., 0., 0.);
    }

    public Spherical( double radius, double phi, double theta ) {

        this.radius = radius;
        this.phi = phi;
        this.theta = theta;
    }

    public double getRadius() {
        return radius;
    }

    public Spherical setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public double getPhi() {
        return phi;
    }

    public Spherical setPhi(double phi) {
        this.phi = phi;
        return this;
    }

    public double getTheta() {
        return theta;
    }

    public Spherical setTheta(double theta) {
        this.theta = theta;
        return this;
    }

    public Spherical set( double radius, double phi, double theta ) {

        this.radius = radius;
        this.phi = phi;
        this.theta = theta;

        return this;
    }

    public Spherical clone() {

        return new Spherical().copy( this );

    }

    public Spherical copy( Spherical other ) {

        this.radius = other.radius;
        this.phi = other.phi;
        this.theta = other.theta;

        return this;
    }

    // restrict phi to be betwee EPS and PI-EPS
    public Spherical makeSafe() {

        double EPS = 0.000001;
        this.phi = Math.max( EPS, Math.min( Math.PI - EPS, this.phi ) );

        return this;
    }

    public Spherical setFromVector3( Vector3 vec3 ) {

        this.radius = vec3.length();

        if ( this.radius == 0 ) {

            this.theta = 0;
            this.phi = 0;

        } else {

            this.theta = Math.atan2( vec3.x, vec3.z ); // equator angle around y-up axis
            this.phi = Math.acos( Mathematics.clamp( vec3.y / this.radius, - 1, 1 ) ); // polar angle

        }

        return this;

    }
}
