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

import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.Arrays;

@ThreejsObject("THREE.DirectionalLightHelper")
public class DirectionalLightHelper extends Object3D {

    DirectionalLight light;
    Line lightPlane;
    Line targetLine;

    public DirectionalLightHelper(DirectionalLight light) {
        this(light, 1.);
    }

    public DirectionalLightHelper(DirectionalLight light, double size) {
        this.light = light;
        this.light.updateMatrixWorld();

        this.setMatrix(light.getMatrixWorld());
        this.setMatrixAutoUpdate(false);

        Geometry geometry = new Geometry();
        geometry.getVertices().addAll(Arrays.asList(
                new Vector3( - size,   size, 0 ),
                new Vector3(   size,   size, 0 ),
                new Vector3(   size, - size, 0 ),
                new Vector3( - size, - size, 0 ),
                new Vector3( - size,   size, 0 )
        ));

        LineBasicMaterial material = new LineBasicMaterial().setFog(false);
        material.getColor().copy(this.light.getColor()).multiply(this.light.getIntensity());

        this.lightPlane = new Line( geometry, material );
        this.add( this.lightPlane );

        geometry = new Geometry();
        geometry.getVertices().addAll(Arrays.asList(
                new Vector3(),
                new Vector3()
        ));

        material = new LineBasicMaterial().setFog(false);
        material.getColor().copy(this.light.getColor()).multiply(this.light.getIntensity());

        this.targetLine = new Line( geometry, material );
        this.add( this.targetLine );

        this.update();
    }

    public void dispose() {

        this.lightPlane.getGeometry().dispose();
        this.lightPlane.getMaterial().dispose();
        this.targetLine.getGeometry().dispose();
        this.targetLine.getMaterial().dispose();

    }

    static final Vector3 v1 = new Vector3(),
        v2 = new Vector3(),
        v3 = new Vector3();

    public void update() {

        v1.setFromMatrixPosition(this.light.getMatrixWorld());
        v2.setFromMatrixPosition(this.light.getTarget().getMatrixWorld());
        v3.sub( v2, v1 );

        this.lightPlane.lookAt( v3 );
        ((LineBasicMaterial) this.lightPlane.getMaterial()).getColor().copy(this.light.getColor()).multiply(this.light.getIntensity());

        ((Geometry) this.targetLine.getGeometry()).getVertices().get(1).copy( v3 );
        this.targetLine.getGeometry().setVerticesNeedUpdate(true);
        ((LineBasicMaterial) this.targetLine.getMaterial()).getColor().copy(((LineBasicMaterial) this.lightPlane.getMaterial()).getColor());

    }
}
