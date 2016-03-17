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

import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.HemisphereLightHelper")
public class HemisphereLightHelper extends Object3D {

    Mesh lightSphere;
    HemisphereLight light;
    Color[] colors;

    public HemisphereLightHelper(HemisphereLight light, double sphereSize) {
        this.light = light;
        this.light.updateMatrixWorld();

        this.setMatrix(light.getMatrixWorld());
        this.setMatrixAutoUpdate(false);

        this.colors = new Color[]{ new Color(), new Color() };

        SphereGeometry geometry = new SphereGeometry( sphereSize, 4, 2 );
        geometry.rotateX( - Math.PI / 2 );

        for ( int i = 0, il = 8; i < il; i ++ ) {

            geometry.getFaces().get(i).setColor(this.colors[i < 4 ? 0 : 1]);

        }

        MeshBasicMaterial material = new MeshBasicMaterial().setVertexColors(Material.COLORS.FACE).setWireframe(true);

        this.lightSphere = new Mesh( geometry, material );
        this.add( this.lightSphere );

        this.update();
    }

    public void dispose() {

        this.lightSphere.getGeometry().dispose();
        this.lightSphere.getMaterial().dispose();

    }

    static final Vector3 vector = new Vector3();

    public void update() {

        this.colors[ 0 ].copy(this.light.getColor()).multiply(this.light.getIntensity());
        this.colors[ 1 ].copy(this.light.getGroundColor()).multiply(this.light.getIntensity());

        this.lightSphere.lookAt( vector.setFromMatrixPosition(this.light.getMatrixWorld()).negate() );
        this.lightSphere.getGeometry().setColorsNeedUpdate(true);

    }
}
