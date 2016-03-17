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
package org.parallax3d.parallax.graphics.extras.helpers;

import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.extras.geometries.EdgesGeometry;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author WestLangley / http://github.com/WestLangley
 * between the face normals of adjacent faces,
 * that is required to render an edge. A value of 10 means
 * an edge is only rendered if the angle is at least 10 degrees.
 */
@ThreejsObject("THREE.EdgesHelper")
public class EdgesHelper extends LineSegments {

    public EdgesHelper(GeometryObject object) {
        this(object, 0xffffff, false);
    }

    public EdgesHelper(GeometryObject object, int hex, boolean thresholdAngle)
    {
        super(new EdgesGeometry(object.getGeometry(), thresholdAngle ), new LineBasicMaterial().setColor(hex));

        this.setMatrix(object.getMatrixWorld());
        this.setMatrixAutoUpdate(false);
    }
}
