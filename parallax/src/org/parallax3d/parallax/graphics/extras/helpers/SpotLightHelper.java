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
import org.parallax3d.parallax.graphics.extras.geometries.CylinderGeometry;
import org.parallax3d.parallax.graphics.lights.SpotLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.Disposable;

public class SpotLightHelper extends Object3D implements Disposable {

    SpotLight light;
    Mesh cone;

    public SpotLightHelper(SpotLight light)
    {
        this.light = light;
        this.light.updateMatrixWorld();

        this.setMatrix(light.getMatrixWorld());
        this.setMatrixAutoUpdate(false);

        CylinderGeometry geometry = new CylinderGeometry( 0., 1., 1., 8, 1 );

        geometry.translate( 0, - 0.5, 0 );
        geometry.rotateX( - Math.PI / 2 );

        MeshBasicMaterial material = new MeshBasicMaterial().setFog(false).setWireframe(true);

        this.cone = new Mesh( geometry, material );
        this.add( this.cone );

        this.update();
    }

    @Override
    public void dispose() {

        this.cone.getGeometry().dispose();
        this.cone.getMaterial().dispose();

    }

    static final Vector3 vector = new Vector3();
    static final Vector3 vector2 = new Vector3();

    public void update() {

        double coneLength = this.light.getDistance() > 0 ? this.light.getDistance() : 10000;
        double coneWidth = coneLength * Math.tan(this.light.getAngle());

        this.cone.getScale().set( coneWidth, coneWidth, coneLength );

        vector.setFromMatrixPosition(this.light.getMatrixWorld());
        vector2.setFromMatrixPosition(this.light.getTarget().getMatrixWorld());

        this.cone.lookAt( vector2.sub( vector ) );

        ((MeshBasicMaterial) this.cone.getMaterial()).getColor().copy(this.light.getColor()).multiply(this.light.getIntensity());

    }
}
