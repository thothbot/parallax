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

import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.objects.Bone;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;

@ThreejsObject("THREE.SkeletonHelper")
public class SkeletonHelper extends LineSegments {

    Object3D root;
    List<Bone> bones;

    public SkeletonHelper(Object3D object) {
        super(intDefaultGeometry(object), (
                LineBasicMaterial) new LineBasicMaterial()
                    .setVertexColors(Material.COLORS.VERTEX)
                    .setDepthTest(false)
                    .setDepthWrite(false)
                    .setTransparent(true));

        this.root = object;
        this.bones = getBoneList(object);

        this.setMatrix(object.getMatrixWorld());
        this.setMatrixAutoUpdate(false);

        this.update();
    }

    private static Geometry intDefaultGeometry(Object3D object) {

        List<Bone> bones = getBoneList(object);
        Geometry geometry = new Geometry();

        for ( int i = 0; i < bones.size(); i ++ ) {

            Bone bone = bones.get(i);

            if ( bone.getParent() instanceof Bone )
            {
                geometry.getVertices().add( new Vector3() );
                geometry.getVertices().add( new Vector3() );
                geometry.getColors().add( new Color( 0., 0., 1. ) );
                geometry.getColors().add( new Color( 0., 1., 0. ) );

            }

        }

        geometry.setDynamic(true);

        return geometry;
    }

    private static List<Bone> getBoneList( Object3D object ) {

        List<Bone> boneList = new ArrayList<>();

        if ( object instanceof Bone ) {

            boneList.add((Bone) object);

        }

        for ( int i = 0; i < object.getChildren().size(); i ++ ) {

            boneList.addAll(getBoneList(object.getChildren().get(i)));

        }

        return boneList;

    }

    public void update ()
    {

        Geometry geometry = (Geometry) this.geometry;

        Matrix4 matrixWorldInv = new Matrix4().getInverse(this.root.getMatrixWorld());

        Matrix4 boneMatrix = new Matrix4();

        int j = 0;

        for ( int i = 0; i < this.bones.size(); i ++ ) {

            Bone bone = this.bones.get(i);

            if ( bone.getParent() instanceof Bone ) {

                boneMatrix.multiply( matrixWorldInv, bone.getMatrixWorld());
                geometry.getVertices().get(j).setFromMatrixPosition( boneMatrix );

                boneMatrix.multiply( matrixWorldInv, bone.getParent().getMatrixWorld());
                geometry.getVertices().get(j + 1).setFromMatrixPosition( boneMatrix );

                j += 2;

            }

        }

        geometry.setVerticesNeedUpdate(true);

        geometry.computeBoundingSphere();
    }
}
