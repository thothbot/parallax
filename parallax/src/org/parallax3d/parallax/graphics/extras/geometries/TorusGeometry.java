/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.graphics.extras.geometries;

import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/torus.gif" />
 * <p/>
 * Torus geometry
 *
 * @author thothbot
 */
@ThreejsObject("THREE.TorusGeometry")
public final class TorusGeometry extends Geometry {

    public TorusGeometry() {
        this(100, 40, 8, 6);
    }

    public TorusGeometry(double radius, double tube, int segmentsR, int segmentsT) {
        this(radius, tube, segmentsR, segmentsT, Math.PI * 2.0);
    }

    public TorusGeometry(double radius, double tube, int radialSegments, int tubularSegments, double arc) {
        this.fromBufferGeometry(new TorusBufferGeometry(radius, tube, radialSegments, tubularSegments, arc));
    }
}
