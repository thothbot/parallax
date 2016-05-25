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
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ThreejsObject("THREE.TessellateModifier")

/**
 * Break faces with edges longer than maxEdgeLength
 * - not recursive
 *
 * @author alteredq / http://alteredqualia.com/
 */
public class TessellateModifier extends Modifier {
    double maxEdgeLength;
    
    public TessellateModifier(double maxEdgeLength) {
        this.maxEdgeLength = maxEdgeLength;
    }

    @Override
    public void modify(Geometry geometry) {

        int edge;

        List<Face3> faces = new ArrayList<>();
        List<List<List<Vector2>>> faceVertexUvs = new ArrayList<>();
        double maxEdgeLengthSquared = this.maxEdgeLength * this.maxEdgeLength;

        for ( int i = 0, il = geometry.getFaceVertexUvs().size(); i < il; i ++ ) {

            faceVertexUvs.add(new ArrayList<List<Vector2>>());

        }

        for ( int i = 0, il = geometry.getFaces().size(); i < il; i ++ ) {

            Face3 face = geometry.getFaces().get( i );

                int a = face.getA();
                int b = face.getB();
                int c = face.getC();

                Vector3 va = geometry.getVertices().get( a );
                Vector3 vb = geometry.getVertices().get( b );
                Vector3 vc = geometry.getVertices().get( c );

                double dab = va.distanceToSquared( vb );
                double dbc = vb.distanceToSquared( vc );
                double dac = va.distanceToSquared( vc );

                Vector3 vm;
                if ( dab > maxEdgeLengthSquared || dbc > maxEdgeLengthSquared || dac > maxEdgeLengthSquared ) {

                    int m = geometry.getVertices().size();

                    Face3 triA = face.clone();
                    Face3 triB = face.clone();

                    if ( dab >= dbc && dab >= dac ) {

                        vm = va.clone();
                        vm.lerp( vb, 0.5 );

                        triA.setA( a );
                        triA.setB( m );
                        triA.setC( c );

                        triB.setA( m );
                        triB.setB( b );
                        triB.setC( c );

                        if ( face.getVertexNormals().size() == 3 ) {

                            Vector3 vnm = face.getVertexNormals().get( 0 ).clone();
                            vnm.lerp( face.getVertexNormals().get( 1 ), 0.5 );

                            triA.getVertexNormals().get( 1 ).copy( vnm );
                            triB.getVertexNormals().get( 0 ).copy( vnm );

                        }

                        if ( face.getVertexColors().size() == 3 ) {

                            Color vcm = face.getVertexColors().get( 0 ).clone();
                            vcm.lerp( face.getVertexColors().get( 1 ), 0.5 );

                            triA.getVertexColors().get( 1 ).copy( vcm );
                            triB.getVertexColors().get( 0 ).copy( vcm );

                        }

                        edge = 0;

                    } else if ( dbc >= dab && dbc >= dac ) {

                        vm = vb.clone();
                        vm.lerp( vc, 0.5 );

                        triA.setA( a );
                        triA.setB( b );
                        triA.setC( m );

                        triB.setA( m );
                        triB.setB( c );
                        triB.setC( a );

                        if ( face.getVertexNormals().size() == 3 ) {

                            Vector3 vnm = face.getVertexNormals().get( 1 ).clone();
                            vnm.lerp( face.getVertexNormals().get( 2 ), 0.5 );

                            triA.getVertexNormals().get( 2 ).copy( vnm );

                            triB.getVertexNormals().get( 0 ).copy( vnm );
                            triB.getVertexNormals().get( 1 ).copy( face.getVertexNormals().get( 2 ) );
                            triB.getVertexNormals().get( 2 ).copy( face.getVertexNormals().get( 0 ) );

                        }

                        if ( face.getVertexColors().size() == 3 ) {

                            Color vcm = face.getVertexColors().get( 1 ).clone();
                            vcm.lerp( face.getVertexColors().get( 2 ), 0.5 );

                            triA.getVertexColors().get( 2 ).copy( vcm );

                            triB.getVertexColors().get( 0 ).copy( vcm );
                            triB.getVertexColors().get( 1 ).copy( face.getVertexColors().get( 2 ) );
                            triB.getVertexColors().get( 2 ).copy( face.getVertexColors().get( 0 ) );

                        }

                        edge = 1;

                    } else {

                        vm = va.clone();
                        vm.lerp( vc, 0.5 );

                        triA.setA( a );
                        triA.setB( b );
                        triA.setC( m );

                        triB.setA( m );
                        triB.setB( b );
                        triB.setC( c );

                        if ( face.getVertexNormals().size() == 3 ) {

                            Vector3 vnm = face.getVertexNormals().get( 0 ).clone();
                            vnm.lerp( face.getVertexNormals().get( 2 ), 0.5 );

                            triA.getVertexNormals().get( 2 ).copy( vnm );
                            triB.getVertexNormals().get( 0 ).copy( vnm );

                        }

                        if ( face.getVertexColors().size() == 3 ) {

                            Color vcm = face.getVertexColors().get( 0 ).clone();
                            vcm.lerp( face.getVertexColors().get( 2 ), 0.5 );

                            triA.getVertexColors().get( 2 ).copy( vcm );
                            triB.getVertexColors().get( 0 ).copy( vcm );

                        }

                        edge = 2;

                    }

                    faces.add( triA );
                    faces.add( triB );
                    geometry.getVertices().add( vm );

                    for ( int j = 0, jl = geometry.getFaceVertexUvs().size(); j < jl; j ++ ) {

                        if ( !geometry.getFaceVertexUvs().get( j ).isEmpty() ) {

                            List<Vector2> uvs = geometry.getFaceVertexUvs().get( j ).get( i );

                            Vector2 uvA = uvs.get( 0 );
                            Vector2 uvB = uvs.get( 1 );
                            Vector2 uvC = uvs.get( 2 );

                            // AB

                            if ( edge == 0 ) {

                                Vector2 uvM = uvA.clone();
                                uvM.lerp( uvB, 0.5 );

                                faceVertexUvs.get( j ).add(Arrays.asList( uvA.clone(), uvM.clone(), uvC.clone()));
                                faceVertexUvs.get( j ).add(Arrays.asList( uvM.clone(), uvB.clone(), uvC.clone()));

                                // BC

                            } else if ( edge == 1 ) {

                                Vector2 uvM = uvB.clone();
                                uvM.lerp( uvC, 0.5 );

                                faceVertexUvs.get( j ).add(Arrays.asList( uvA.clone(), uvB.clone(), uvM.clone() ));
                                faceVertexUvs.get( j ).add(Arrays.asList( uvM.clone(), uvC.clone(), uvA.clone() ));

                                // AC

                            } else {

                                Vector2 uvM = uvA.clone();
                                uvM.lerp( uvC, 0.5 );

                                faceVertexUvs.get( j ).add(Arrays.asList( uvA.clone(), uvB.clone(), uvM.clone() ));
                                faceVertexUvs.get( j ).add(Arrays.asList( uvM.clone(), uvB.clone(), uvC.clone() ));

                            }
                        }

                    }

                } else {

                    faces.add( face );

                    for ( int j = 0, jl = geometry.getFaceVertexUvs().size(); j < jl; j ++ ) {

                        faceVertexUvs.get( j ).add( geometry.getFaceVertexUvs().get( j ).get( i ) );

                    }

                }

        }

        geometry.setFaces( faces );
        geometry.setFaceVertexUvs( faceVertexUvs );


    }
}
