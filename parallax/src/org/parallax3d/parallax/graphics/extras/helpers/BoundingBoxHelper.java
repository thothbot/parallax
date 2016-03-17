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
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.math.Box3;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * A helper to show the world-axis-aligned bounding box for an object
 *
 * @author WestLangley / http://github.com/WestLangley
 */
@ThreejsObject("THREE.BoundingBoxHelper")
public class BoundingBoxHelper extends Mesh {

    GeometryObject object;
    Box3 box;

    public BoundingBoxHelper(GeometryObject object) {
        this(object, 0x888888);
    }

    public BoundingBoxHelper(GeometryObject object, int hex)
    {
        super(new BoxGeometry( 1, 1, 1 ), new MeshBasicMaterial( ).setColor(hex).setWireframe(true) );
        this.object = object;
        this.box = new Box3();
    }

    public void update() {

        this.box.setFromObject( this.object );

        this.box.size( this.getScale() );

        this.box.center( this.getPosition() );
    }
}
