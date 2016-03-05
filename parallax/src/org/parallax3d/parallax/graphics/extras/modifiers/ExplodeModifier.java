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

package org.parallax3d.parallax.graphics.extras.modifiers;

import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Make all faces use unique vertices
 * so that each face can be separated from others
 *
 * @author alteredq / http://alteredqualia.com/
 */
@ThreejsObject("THREE.ExplodeModifier")
public class ExplodeModifier extends Modifier {

    @Override
    public void modify(Geometry geometry) {

        List<Vector3> vertices = new ArrayList<>();

        for ( int i = 0, il = geometry.getFaces().size(); i < il; i ++ ) {

            int n = vertices.size();

            Face3 face = geometry.getFaces().get( i );

            int a = face.getA();
            int b = face.getB();
            int c = face.getC();

            Vector3 va = geometry.getVertices().get( a );
            Vector3 vb = geometry.getVertices().get( b );
            Vector3 vc = geometry.getVertices().get( c );

            vertices.add( va.clone() );
            vertices.add( vb.clone() );
            vertices.add( vc.clone() );

            face.setA( n );
            face.setB( n + 1 );
            face.setC( n + 2 );

        }

        geometry.setVertices( vertices );
    }
}
