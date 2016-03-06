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

package org.parallax3d.parallax.graphics.cameras;

import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author mrdoob / http://mrdoob.com/
 */
@ThreejsObject("THREE.StereoCamera")
public class StereoCamera {

    double aspect;

    PerspectiveCamera cameraL;
    PerspectiveCamera cameraR;

    public StereoCamera() {
        this.aspect = 1.0;

        this.cameraL = new PerspectiveCamera();
        this.cameraL.getLayers().enable( 1 );
        this.cameraL.setMatrixAutoUpdate(false);;

        this.cameraR = new PerspectiveCamera();
        this.cameraR.getLayers().enable( 2 );
        this.cameraR.setMatrixAutoUpdate(false);
    }

    public void update(PerspectiveCamera camera) {
        double focalLength = 0, fov = 0, aspect = 0, near = 0, far = 0;

        Matrix4 eyeRight = new Matrix4(),
                eyeLeft = new Matrix4();

        boolean needsUpdate = focalLength != camera.focalLength || fov != camera.fov ||
                aspect != camera.aspect * this.aspect || near != camera.near ||
                far != camera.far;

        if ( needsUpdate ) {

            focalLength = camera.focalLength;
            fov = camera.fov;
            aspect = camera.aspect * this.aspect;
            near = camera.near;
            far = camera.far;

            // Off-axis stereoscopic effect based on
            // http://paulbourke.net/stereographics/stereorender/

            Matrix4 projectionMatrix = camera.projectionMatrix.clone();
            double eyeSep = 0.064 / 2;
            double eyeSepOnProjection = eyeSep * near / focalLength;
            double ymax = near * Math.tan( Mathematics.degToRad( fov * 0.5 ) );
            double xmin, xmax;

            // translate xOffset

            eyeLeft.getArray().set( 12, - eyeSep );
            eyeRight.getArray().set( 12, eyeSep );

            // for left eye

            xmin = - ymax * aspect + eyeSepOnProjection;
            xmax = ymax * aspect + eyeSepOnProjection;

            projectionMatrix.getArray().set( 0 , 2 * near / ( xmax - xmin ));
            projectionMatrix.getArray().set( 8 , ( xmax + xmin ) / ( xmax - xmin ));

            this.cameraL.projectionMatrix.copy( projectionMatrix );

            // for right eye

            xmin = - ymax * aspect - eyeSepOnProjection;
            xmax = ymax * aspect - eyeSepOnProjection;

            projectionMatrix.getArray().set( 0 , 2 * near / ( xmax - xmin ));
            projectionMatrix.getArray().set( 8 , ( xmax + xmin ) / ( xmax - xmin ));

            this.cameraR.projectionMatrix.copy( projectionMatrix );

        }

        this.cameraL.getMatrixWorld().copy( camera.getMatrixWorld() ).multiply( eyeLeft );
        this.cameraR.getMatrixWorld().copy( camera.getMatrixWorld() ).multiply( eyeRight );

    }
}
